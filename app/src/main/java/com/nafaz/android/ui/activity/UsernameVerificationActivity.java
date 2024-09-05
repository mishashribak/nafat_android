package com.nafaz.android.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.ObjectKey;
import com.google.android.material.textfield.TextInputEditText;
import com.nafaz.android.R;
import com.nafaz.android.api.callback.ApiCallback;
import com.nafaz.android.api.constants.APIConstants;
import com.nafaz.android.api.entity.ApiResponse;
import com.nafaz.android.api.entity.LoginRequest;
import com.nafaz.android.api.services.CaptchaService;
import com.nafaz.android.api.services.LoginService;
import com.nafaz.android.app.AppConstants;
import com.nafaz.android.entity.AuthLogic;
import com.nafaz.android.entity.Identity;
import com.nafaz.android.manager.PrefManager;
import com.nafaz.android.ui.base.BaseActivity;
import com.nafaz.android.ui.view.button.LoadingMaterialButton;
import com.nafaz.android.util.AnimationUtils;
import com.nafaz.android.util.ProgressDialog;
import com.nafaz.android.util.SnackBarUtils;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;

import static com.nafaz.android.manager.PrefManager.Keys.KEY_LANGUAGE;

public class UsernameVerificationActivity extends BaseActivity {

    @BindView(R.id.toolbar_common)
    Toolbar commonToolbar;

    @BindView(R.id.tiet_username)
    TextInputEditText tietUsername;

    @BindView(R.id.tiet_password)
    TextInputEditText tietPassword;

    @BindView(R.id.tiet_code)
    TextInputEditText tietCode;

    @BindView(R.id.imgCode)
    ImageView imgCode;

    @BindView(R.id.btn_confirm)
    LoadingMaterialButton btnConfirm;


    public static void open(@NotNull Context context) {
        context.startActivity(new Intent(context, UsernameVerificationActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_username_verification);
        ButterKnife.bind(this);
        setSupportActionBar(commonToolbar);

        LinearLayout linearLayout = findViewById(R.id.llAnimParent);
        linearLayout.setVisibility(View.INVISIBLE);
        AnimationUtils.animateSlideTopFromDown(linearLayout);
        tietUsername.setText(Identity.getPersonId()+"");
        getCaptcha();
        commonToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        AuthLogic.currentStep++;
    }

    @Override
    public void onBackPressed() {
        if(AuthLogic.currentStep > 0)
            AuthLogic.currentStep--;
        finish();
    }

    @OnClick(R.id.btn_confirm)
    public void onConfirmButtonClicked() {
        if (tietUsername.getText().toString().isEmpty()) {
            SnackBarUtils.showSnackBar(this, R.string.username_required);
            return;
        }

        if (tietPassword.getText().toString().isEmpty()) {
            SnackBarUtils.showSnackBar(this, R.string.pasword_required);
            return;
        }

        if (tietCode.getText().toString().isEmpty()) {
            SnackBarUtils.showSnackBar(this, R.string.captcha_required);
            return;
        }

        callLoginAPI();
    }

    private void callLoginAPI() {

        btnConfirm.showLoading();

        String cookie = PrefManager.getInstance(this).getPreferences("cookie", "");

        new LoginService().start(cookie, new LoginRequest(tietUsername.getText().toString(), tietPassword.getText().toString(),
                        tietCode.getText().toString()),
                new ApiCallback<ApiResponse>() {
                    @Override
                    public void onSuccess(ApiResponse response) {
                        btnConfirm.hideLoading();

                        if (response.code == APIConstants.ResponseCode.CODE_200){
                            AuthLogic.add(AuthLogic.MethodType.USERNAME);
//                            if(AuthLogic.hasAllVerifiedMethods()){
//                                startActivity(new Intent(UsernameVerificationActivity.this, SuccessfulVerificationActivity.class).putExtra(
//                                        AppConstants.Extras.EXTRA_SUCCESS_MODE, AppConstants.Extras.EXTRA_SUCCESS_REGISTRATION
//                                ), false );
////                                SuccessfulVerificationActivity.open(UsernameVerificationActivity.this, AppConstants.Extras.EXTRA_SUCCESS_REGISTRATION);
//                            }else{
//                                startActivity(new Intent(UsernameVerificationActivity.this, SuccessfulVerificationActivity.class).putExtra(
//////                                        AppConstants.Extras.EXTRA_SUCCESS_MODE, AppConstants.Extras.EXTRA_SUCCESS_USERNAME
//////                                ), false );
                            goNextScreen();
//                                SuccessfulVerificationActivity.open(UsernameVerificationActivity.this, AppConstants.Extras.EXTRA_SUCCESS_USERNAME);
//                            }

                        }else{
                            getCaptcha();
                        }

                    }

                    @Override
                    public void onError(String errorMessage) {
                        btnConfirm.hideLoading();
                        getCaptcha();
                        showGeneralError(errorMessage);
                    }

                    @Override
                    public void onFail(String failMessage) {
                        btnConfirm.hideLoading();
                        getCaptcha();
                        showGeneralError(failMessage);
                    }
                });
    }

    private void goNextScreen(){
        if(AuthLogic.currentStep == 1 && AuthLogic.hasOneVerifiedMethodsInOneStep()){
            startActivity(new Intent(this, AuthenticationMethodsActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK), false );
        }else if(AuthLogic.currentStep> 1 && AuthLogic.hasOneVerifiedMethodsInTwoStep()){
            startActivity(new Intent(this, ActionActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK), false );
        }
        finish();
    }

    private void getCaptcha(){
//        ProgressDialog.showProgress(this);
        new CaptchaService().start(new ApiCallback<ResponseBody>() {
            @Override
            public void onSuccess(ResponseBody response) {
//                ProgressDialog.hideprogressbar();
                if(writeResponseBodyToDisk(response)){
                    Glide
                            .with(UsernameVerificationActivity.this)
                            .load(outFile.getPath())
                            .signature(new ObjectKey(System.currentTimeMillis()))
                            .into(imgCode);
                }
            }

            @Override
            public void onError(String errorMessage) {
//                ProgressDialog.hideprogressbar();
                showGeneralError(errorMessage);
            }

            @Override
            public void onFail(String failMessage) {
//                ProgressDialog.hideprogressbar();
                showGeneralError(failMessage);
            }
        });
    }

    File outFile;
    private boolean writeResponseBodyToDisk(ResponseBody body) {
        try {
            // todo change the file location/name according to your needs
            outFile = new File(getExternalFilesDir(null) + File.separator + "code.png");

            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[4096];

                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(outFile);

                while (true) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }

                    outputStream.write(fileReader, 0, read);

                    fileSizeDownloaded += read;

                }

                outputStream.flush();

                return true;
            } catch (IOException e) {
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return false;
        }
    }
}

