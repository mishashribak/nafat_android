package com.nafaz.android.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.goodiebag.pinview.Pinview;
import com.google.android.material.textfield.TextInputEditText;
import com.nafaz.android.R;
import com.nafaz.android.api.callback.ApiCallback;
import com.nafaz.android.api.entity.CheckOtpRequest;
import com.nafaz.android.api.entity.CheckTOTPRequest;
import com.nafaz.android.api.entity.ConfirmMobileRequest;
import com.nafaz.android.api.services.CheckTOTPService;
import com.nafaz.android.api.services.ConfirmMobileService;
import com.nafaz.android.api.services.VerifyCodeService;
import com.nafaz.android.api.services.VerifyMobileService;
import com.nafaz.android.app.AppConstants;
import com.nafaz.android.bus.BusProvider;
import com.nafaz.android.entity.AuthLogic;
import com.nafaz.android.entity.Identity;
import com.nafaz.android.event.AuthMethodDoneEvent;
import com.nafaz.android.ui.base.BaseActivity;
import com.nafaz.android.ui.view.button.LoadingMaterialButton;
import com.nafaz.android.util.AnimationUtils;
import com.nafaz.android.util.NetworkUtils;
import com.nafaz.android.util.ProgressDialog;
import com.nafaz.android.util.SnackBarUtils;
import com.nafaz.android.util.ValidationUtil;

import org.jetbrains.annotations.NotNull;

import java.nio.channels.OverlappingFileLockException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CodeVerificationActivity extends BaseActivity {

    public static void open(@NotNull Context context, @NotNull CodeVerificationType type) {
        context.startActivity(new Intent(context, CodeVerificationActivity.class)
                .putExtra(AppConstants.Extras.EXTRA_TYPE, type.name()));
    }

    public static void open(@NotNull Context context, @NotNull CodeVerificationType type, String mode) {
        context.startActivity(new Intent(context, CodeVerificationActivity.class)
                .putExtra(AppConstants.Extras.EXTRA_TYPE, type.name())
                .putExtra(AppConstants.Extras.EXTRA_MOBILE_VER_MODE, mode));
    }

    @BindView(R.id.toolbar_common)
    Toolbar commonToolBar;

    @BindView(R.id.snackbarlocation)
    CoordinatorLayout snackBarLocation;

    @BindView(R.id.pinText)
    TextInputEditText pinText;

    @BindView(R.id.btn_pincode_verification_confirm)
    LoadingMaterialButton btnConfirm;

    private String codeType;
    private String verificationMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code_verification);
        ButterKnife.bind(this);
        parseIntentData();
        setSupportActionBar(commonToolBar);
        setupToolBarTitle();

        AnimationUtils.animateSlideFromLeft(findViewById(R.id.parent));

        commonToolBar.setNavigationOnClickListener(new View.OnClickListener() {
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

    private void setupToolBarTitle() {
        if (codeType.equals(CodeVerificationType.MOBILE.name())) {
            setToolBarTitle(commonToolBar, R.string.activity_mobile_verification_title);
        } else {
            setToolBarTitle(commonToolBar, R.string.activity_id_card_verification_title);
        }
    }

    private void parseIntentData() {
        final Bundle bundle = getIntent().getExtras();
        if (null == bundle)
            return;

        codeType = bundle.getString(AppConstants.Extras.EXTRA_TYPE);
        verificationMode = bundle.getString(AppConstants.Extras.EXTRA_MOBILE_VER_MODE);

//        if (verificationMode.equals(AppConstants.Extras.EXTRA_MOBILE_VER_NORMAL))
//           pinview.setPinLength(4);
//        else if (verificationMode.equals(AppConstants.Extras.EXTRA_MOBILE_VER_TOTP))
//            pinview.setPinLength(6);
    }

    @OnClick(R.id.btn_pincode_verification_confirm)
    public void onConfirmButtonClicked() {

        if (codeType.equals(CodeVerificationType.MOBILE.name())) {

//            if (!ValidationUtil.isValidOtp(pinText.getText().toString())) {
//                SnackBarUtils.showSnackBar(this, R.string.error_invalid_code);
//                return;
//            }

            if (verificationMode.equals(AppConstants.Extras.EXTRA_MOBILE_VER_NORMAL))
                callVerifyCodeAPI();
            else if (verificationMode.equals(AppConstants.Extras.EXTRA_MOBILE_VER_TOTP))
                callCheckTOTP(pinText.getText().toString());
//            else
//                callConfirmMobileAPI();

            // This section is hidden based on requirements
        } /*else if (codeType.equals(CodeVerificationType.ID_CARD.name())) {

            AuthLogic.add(AuthLogic.MethodType.ID_NUM);

            if (AuthLogic.hasAtLeastTwoVerifiedMethods()) {
                showVerificationDialog();
            } else
                finish();

        }*/
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
                        goNextScreen();

//                        startActivity(new Intent(CodeVerificationActivity.this, SuccessfulVerificationActivity.class).putExtra(
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
            startActivity(new Intent(this, AuthenticationMethodsActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK), false );
        }else if(AuthLogic.currentStep> 1 && AuthLogic.hasOneVerifiedMethodsInTwoStep()){
            startActivity(new Intent(this, ActionActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK), false );
        }
        finish();
    }

    private void callVerifyCodeAPI() {

        if (null == Identity.id)
            return;

        if (!NetworkUtils.isOnline(this)) {
            showInternetError(snackBarLocation);
            return;
        }

        btnConfirm.showLoading();

        new VerifyCodeService().start(
                new CheckOtpRequest(
                        Integer.parseInt(pinText.getText().toString()),
                        Identity.getPersonId()),
                new ApiCallback() {
                    @Override
                    public void onSuccess(Object object) {
                        btnConfirm.hideLoading();


//                        AuthLogic.removeCompletedAuthFromSecondArray(AuthLogic.MethodType.MOBILE);
//                        BusProvider.getInstance().post(new AuthMethodDoneEvent(AuthLogic.MethodType.MOBILE));

                        AuthLogic.add(AuthLogic.MethodType.MOBILE);
//                        if(AuthLogic.hasAllVerifiedMethods()){
//                            startActivity(new Intent(CodeVerificationActivity.this, SuccessfulVerificationActivity.class).putExtra(
//                                    AppConstants.Extras.EXTRA_SUCCESS_MODE, AppConstants.Extras.EXTRA_SUCCESS_REGISTRATION
//                            ), false );
////                            SuccessfulVerificationActivity.open(CodeVerificationActivity.this, AppConstants.Extras.EXTRA_SUCCESS_REGISTRATION);
//                        }else{
//                            startActivity(new Intent(CodeVerificationActivity.this, SuccessfulVerificationActivity.class).putExtra(
//                                    AppConstants.Extras.EXTRA_SUCCESS_MODE, AppConstants.Extras.EXTRA_SUCCESS_MOBILE
//                            ), false );
                        goNextScreen();
//                            SuccessfulVerificationActivity.open(CodeVerificationActivity.this, AppConstants.Extras.EXTRA_SUCCESS_MOBILE);
//                        }

                    }

                    @Override
                    public void onError(String errorMessage) {
                        btnConfirm.hideLoading();
                        showGeneralError(errorMessage);
                    }

                    @Override
                    public void onFail(String failMessage) {
                        btnConfirm.hideLoading();
                        showGeneralError(failMessage);
                    }
                });
    }

    private void callConfirmMobileAPI() {
        if (null == Identity.id)
            return;

        if (!NetworkUtils.isOnline(this)) {
            showInternetError(snackBarLocation);
            return;
        }

        btnConfirm.showLoading();

        new ConfirmMobileService().start(
                new ConfirmMobileRequest(
                        Integer.parseInt(pinText.getText().toString()),
                        Identity.getPersonId()),
                new ApiCallback() {
                    @Override
                    public void onSuccess(Object object) {
                        btnConfirm.hideLoading();
                        startActivity(new Intent(CodeVerificationActivity.this, ActionActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK), false );
//                        SuccessfulVerificationActivity.open(CodeVerificationActivity.this, AppConstants.Extras.EXTRA_SUCCESS_REGISTRATION);
                    }

                    @Override
                    public void onError(String errorMessage) {
                        btnConfirm.hideLoading();
                        showGeneralError(errorMessage);
                    }

                    @Override
                    public void onFail(String failMessage) {
                        btnConfirm.hideLoading();
                        showGeneralError(failMessage);
                    }
                });
    }

//    private void showVerificationDialog() {
//        VerificationInfoDialog.show(this, VerificationInfoDialog.DialogType.VERIFICATION, new VerificationInfoDialog.OnVerificationInfoListener() {
//            @Override
//            public void onNext() {
//                finish();
//                RegisterNewUserActivity.open(CodeVerificationActivity.this);
//            }
//
//            @Override
//            public void onBack() {
//                finish();
//            }
//        });
//    }

    public enum CodeVerificationType {
        MOBILE,
        ID_CARD,
    }
}
