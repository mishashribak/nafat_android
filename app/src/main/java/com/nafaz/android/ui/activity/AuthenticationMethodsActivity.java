package com.nafaz.android.ui.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.nafaz.android.R;
import com.nafaz.android.api.callback.ApiCallback;
import com.nafaz.android.api.entity.CheckTOTPRequest;
import com.nafaz.android.api.entity.OPTAuthRequest;
import com.nafaz.android.api.services.AuthOTPService;
import com.nafaz.android.api.services.CheckTOTPService;
import com.nafaz.android.app.AppConstants;
import com.nafaz.android.app.NafazConfig;
import com.nafaz.android.entity.AllowedStepsOne;
import com.nafaz.android.entity.AllowedStepsTwo;
import com.nafaz.android.entity.AuthLogic;
import com.nafaz.android.entity.Identity;
import com.nafaz.android.entity.VerifyMethodModel;
import com.nafaz.android.event.AuthMethodDoneEvent;
import com.nafaz.android.freeotp.Token;
import com.nafaz.android.manager.PrefManager;
import com.nafaz.android.ui.adapter.MethodAdapter;
import com.nafaz.android.ui.base.BaseActivity;
import com.nafaz.android.util.AnimationUtils;
import com.nafaz.android.util.ProgressDialog;
import com.squareup.otto.Subscribe;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.nafaz.android.app.AppConstants.Extras.EXTRA_MOBILE_VER_NORMAL;
import static com.nafaz.android.app.AppConstants.Extras.EXTRA_MOBILE_VER_TOTP;

public class AuthenticationMethodsActivity extends BaseActivity {

    @BindView(R.id.toolbar_common)
    Toolbar commonToolbar;

//    @BindView(R.id.cl_mobile_verification)
//    LinearLayout clMobileVerification;
//
//    @BindView(R.id.cl_user_verification)
//    LinearLayout clUserVerification;
//
//    @BindView(R.id.cl_face_fingerprint)
//    LinearLayout clFaceFingerprint;
//
//    @BindView(R.id.cl_id)
//    LinearLayout cl_id;
//
//    @BindView(R.id.tv_mobile_verification_label)
//    TextView tv_mobile_verification_label;
//
//    @BindView(R.id.tv_user_verification_label)
//    TextView tv_user_verification_label;
//
//    @BindView(R.id.tv_face_fingerprint_label)
//    TextView tv_face_fingerprint_label;
//
//    @BindView(R.id.tv_id_label)
//    TextView tv_id_label;

    @BindView(R.id.imgAuth)
    ImageView imgAuth;

    @BindView(R.id.tvAuth)
    TextView tvAuth;

    @BindView(R.id.imgAuth2)
    ImageView imgAuth2;

    @BindView(R.id.tvAuth2)
    TextView tvAuth2;

    @BindView(R.id.rvMethods)
    RecyclerView rvMethods;

    ArrayList<VerifyMethodModel> verifyMethodModelList = new ArrayList<>();
    MethodAdapter methodAdapter;


    public static void open(@NotNull Context context) {
        context.startActivity(new Intent(context, AuthenticationMethodsActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication_methods);
        ButterKnife.bind(this);
        setSupportActionBar(commonToolbar);
        handleBusProviderRegistration(true);

        LinearLayout linearLayout = findViewById(R.id.llAnimParent);
        linearLayout.setVisibility(View.INVISIBLE);
        AnimationUtils.animateSlideTopFromDown(linearLayout);

        methodAdapter = new MethodAdapter(this, new MethodAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(VerifyMethodModel verifyMethodModel) {
                if(verifyMethodModel.stepName.equals("OTP")){
                    callCheckOTP();
                }else if(verifyMethodModel.stepName.equals("UP")){
                    startActivity(new Intent(AuthenticationMethodsActivity.this, UsernameVerificationActivity.class), false );
                }else if(verifyMethodModel.stepName.equals("CHECK_FACE")){
                    FaceFingerprintActivity.open(AuthenticationMethodsActivity.this);
                }else if(verifyMethodModel.stepName.equals("CHECK_TOTP")){
                    checkTOTP();
                }
            }
        });

        rvMethods.setLayoutManager(new LinearLayoutManager(this));
        rvMethods.setAdapter(methodAdapter);

        // Loop over first array and show related views based on stepName
        initAuthMethod();
    }

    private void initAuthMethod(){
        if (null != AuthLogic.authMethods) {
            if(AuthLogic.currentStep == 0){
                verifyMethodModelList.clear();
                imgAuth.setVisibility(View.VISIBLE);
                tvAuth.setVisibility(View.VISIBLE);
                imgAuth2.setVisibility(View.GONE);
                tvAuth2.setVisibility(View.GONE);

                final List<AllowedStepsOne> stepOneList = AuthLogic.authMethods.arrayOfStepsOne;
                for (int i = 0; i < stepOneList.size(); i++) {
                    if(AuthLogic.isMobileVerified() && stepOneList.get(i).stepName.equals("OTP")){
                        continue;
                    }

                    if(AuthLogic.isUsernameVerified() && stepOneList.get(i).stepName.equals("UP")){
                        continue;
                    }

                    if(AuthLogic.isTOTPVerified() && stepOneList.get(i).stepName.equals("CHECK_TOTP")){
                        continue;
                    }

//                    if(AuthLogic.isFaceVerified() && stepOneList.get(i).stepName.equals("CHECK_FACE")){
//                        continue;
//                    }

                    if(stepOneList.get(i).stepName.equals("CHECK_FACE")){
                        continue;
                    }

                    if(stepOneList.get(i).stepName.equals("CHECK_MOBILE_OWNED")){
                        continue;
                    }

                    VerifyMethodModel verifyMethodModel = new VerifyMethodModel(stepOneList.get(i).englishLabel,
                            stepOneList.get(i).arabicLabel, stepOneList.get(i).stepName);
                    verifyMethodModelList.add(verifyMethodModel);
                }

            } else if(AuthLogic.currentStep == 1) {
                imgAuth2.setVisibility(View.VISIBLE);
                tvAuth2.setVisibility(View.VISIBLE);
                imgAuth.setVisibility(View.GONE);
                tvAuth.setVisibility(View.GONE);

                final List<AllowedStepsTwo> stepTwoList = AuthLogic.authMethods.arrayOfStepsTwo;
                for (int i = 0; i < stepTwoList.size(); i++) {
                    if(AuthLogic.isMobileVerified() && stepTwoList.get(i).stepName.equals("OTP")){
                        continue;
                    }

                    if(AuthLogic.isUsernameVerified() && stepTwoList.get(i).stepName.equals("UP")){
                        continue;
                    }

                    if(AuthLogic.isTOTPVerified() && stepTwoList.get(i).stepName.equals("CHECK_TOTP")){
                        continue;
                    }

//                    if(AuthLogic.isFaceVerified() && stepTwoList.get(i).stepName.equals("CHECK_FACE")){
//                        continue;
//                    }

                    if(stepTwoList.get(i).stepName.equals("CHECK_FACE")){
                        continue;
                    }

                    if(stepTwoList.get(i).stepName.equals("CHECK_MOBILE_OWNED")){
                        continue;
                    }

                    VerifyMethodModel verifyMethodModel = new VerifyMethodModel(stepTwoList.get(i).englishLabel,
                            stepTwoList.get(i).arabicLabel, stepTwoList.get(i).stepName);
                    verifyMethodModelList.add(verifyMethodModel);
                }
            }
        }

        methodAdapter.setData(verifyMethodModelList);
    }

    private int period;
    private void checkTOTP() {
        String strToken = PrefManager.getInstance(this).getPreferences("totp", "");

        if(strToken.isEmpty()){
            startActivity(new Intent(AuthenticationMethodsActivity.this, CodeVerificationActivity.class)
                    .putExtra(AppConstants.Extras.EXTRA_TYPE, CodeVerificationActivity.CodeVerificationType.MOBILE.name())
                    .putExtra(AppConstants.Extras.EXTRA_MOBILE_VER_MODE, EXTRA_MOBILE_VER_TOTP), false );
        }else{
            Uri uri= Uri.parse(strToken);
            period = Integer.parseInt(uri.getQueryParameter("period"));
            if(period < 0)
                return;
            Token token = null;
            try {
                token =  new Token(strToken);
                String code = token.generateCodes().getCurrentCode();
                callCheckTOTP(code);
            } catch (Token.TokenUriInvalidException e) {
                e.printStackTrace();
            }
        }
    }

    private void callCheckTOTP(String code) {
        if (null == Identity.id)
            return;

        ProgressDialog.showProgress(this);

        new CheckTOTPService().start(new CheckTOTPRequest( Identity.getPersonId(), code),
                new ApiCallback() {
                    @Override
                    public void onSuccess(Object object) {
                        ProgressDialog.hideprogressbar();
                        AuthLogic.add(AuthLogic.MethodType.TOTP);
                        AuthLogic.currentStep++;
                        goNextScreen();
//                        startActivity(new Intent(AuthenticationMethodsActivity.this, SuccessfulVerificationActivity.class).putExtra(
//                                AppConstants.Extras.EXTRA_SUCCESS_MODE, AppConstants.Extras.EXTRA_SUCCESS_TOTP
//                        ), false );
                    }

                    @Override
                    public void onError(String errorMessage) {
                        ProgressDialog.hideprogressbar();
                        showGeneralError(errorMessage);
                    }

                    @Override
                    public void onFail(String failMessage) {
                        ProgressDialog.hideprogressbar();
                        showGeneralError(failMessage);
                    }
                });
    }

    private void goNextScreen(){
        if(AuthLogic.currentStep == 1 && AuthLogic.hasOneVerifiedMethodsInOneStep()){
//            startActivity(new Intent(this, AuthenticationMethodsActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK), false );
            initAuthMethod();
        }else if(AuthLogic.currentStep> 1 && AuthLogic.hasOneVerifiedMethodsInTwoStep()){
            startActivity(new Intent(this, ActionActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK), false );
        }
    }


//    @OnClick(R.id.btn_id)
//    public void onIdNumberButtonClicked() {
//        startActivityForResult(new Intent(this, IDCardActivity.class), RequestCode.CODE_ID_NUM);
//    }
//
//    @OnClick(R.id.btn_protection_questions)
//    public void onSecurityQuestionsButtonClicked() {
//        startActivityForResult(new Intent(this, ProtectionQuestionsActivity.class), RequestCode.CODE_PROTECTION_QUES);
//    }

//    @OnClick(R.id.tv_skip)
//    public void onSkipLabelButtonClicked() {
//        startActivity(new Intent(this, RegisterNewUserActivity.class), false );
////        RegisterNewUserActivity.open(this);
//    }

    @SuppressWarnings("unused")
//        private void handleButtonColor(@NotNull MaterialButton button) {
//            button.setBackgroundTintList(ColorStateList.valueOf(ResourcesCompat.getColor(getResources(), R.color.colorAccent, getTheme())));
//            button.setIconTint(ColorStateList.valueOf(Color.WHITE));
//        }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handleBusProviderRegistration(false);
    }

    @Subscribe
    public void onAuthMethodDone(@NotNull AuthMethodDoneEvent event) {
//        if (AuthLogic.hasAtLeastTwoVerifiedMethods()) {
//            finish();
//            return;
//        }

//            switch (event.methodType) {
//                case MOBILE:
//                    clMobileVerification.setVisibility(View.INVISIBLE);
//                    break;

//            case FACE:
//                clFaceFingerprint.setVisibility(View.INVISIBLE);
//                break;

//                case USERNAME:
//                    clUserVerification.setVisibility(View.INVISIBLE);
//
//                    // To be implemented in later stages based on requirements
//                case ID_NUM:
//                case PROTECTION_QUES:
//                case ALL:
//                    break;
//            }
    }

    @Override
    public void onBackPressed() {
        if(AuthLogic.currentStep == 1) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MyAlertDialogStyle);
            builder.setTitle(getResources().getString(R.string.app_name));
            builder.setMessage(getString(R.string.are_you_sure_back));
            builder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    finish();

                }
            });
            builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();

                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        }else{
            finish();
        }
    }

    private void callCheckOTP() {

        ProgressDialog.showProgress(this);

        new AuthOTPService().start(new OPTAuthRequest(Identity.getPersonId(), NafazConfig.getLanguage(this)),
                new ApiCallback() {
                    @Override
                    public void onSuccess(Object object) {
                        ProgressDialog.hideprogressbar();
//                        if (AuthLogic.authMethods.arrayOfStepsTwo.size() == 3)
                        startActivity(new Intent(AuthenticationMethodsActivity.this, CodeVerificationActivity.class)
                                .putExtra(AppConstants.Extras.EXTRA_TYPE, CodeVerificationActivity.CodeVerificationType.MOBILE.name())
                                .putExtra(AppConstants.Extras.EXTRA_MOBILE_VER_MODE, EXTRA_MOBILE_VER_NORMAL), false );
//                        CodeVerificationActivity.open(AuthenticationMethodsActivity.this, CodeVerificationActivity.CodeVerificationType.MOBILE, EXTRA_MOBILE_VER_NORMAL);
                    }

                    @Override
                    public void onError(String errorMessage) {
                        ProgressDialog.hideprogressbar();
                        showGeneralError(errorMessage);
                    }

                    @Override
                    public void onFail(String failMessage) {
                        ProgressDialog.hideprogressbar();
                        showGeneralError(failMessage);
                    }
                });
    }
}
