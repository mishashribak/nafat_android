package com.nafaz.android.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.textfield.TextInputEditText;
import com.nafaz.android.R;
import com.nafaz.android.api.callback.ApiCallback;
import com.nafaz.android.api.constants.APIConstants;
import com.nafaz.android.api.entity.ApiResponse;
import com.nafaz.android.api.entity.CheckMobileRequest;
import com.nafaz.android.api.entity.RegisterMobileRequest;
import com.nafaz.android.api.services.RegisterMobileService;
import com.nafaz.android.api.services.VerifyMobileService;
import com.nafaz.android.app.AppConstants;
import com.nafaz.android.app.NafazConfig;
import com.nafaz.android.entity.Identity;
import com.nafaz.android.entity.MobileInfo;
import com.nafaz.android.ui.base.BaseActivity;
import com.nafaz.android.ui.view.button.LoadingMaterialButton;
import com.nafaz.android.util.NetworkUtils;
import com.nafaz.android.util.SnackBarUtils;
import com.nafaz.android.util.ValidationUtil;
import com.rilixtech.CountryCodePicker;

import org.jetbrains.annotations.NotNull;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.nafaz.android.app.AppConstants.Extras.EXTRA_MOBILE_VER_NORMAL;

public class MobileVerificationActivity extends BaseActivity {

    @BindView(R.id.toolbar_common)
    Toolbar commonToolbar;

    @BindView(R.id.snackbarlocation)
    CoordinatorLayout snackBarLocation;

    @BindView(R.id.tiet_mobile_number)
    TextInputEditText tietMobileNumber;

    @BindView(R.id.ccp)
    CountryCodePicker countryCodePicker;

    @BindView(R.id.btn_mobile_verification_confirm)
    LoadingMaterialButton btnConfirm;

    private String mobileNum;
    private String verificationMode;

    public static void open(@NotNull Context context, String mode) {
        context.startActivity(new Intent(context, MobileVerificationActivity.class)
                .putExtra(AppConstants.Extras.EXTRA_MOBILE_VER_MODE, mode));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_verification);
        ButterKnife.bind(this);
        setSupportActionBar(commonToolbar);
        setupCountryCodePicker();

        parseIntentData();
    }

    private void setupCountryCodePicker() {
        countryCodePicker.setDefaultCountryUsingNameCode(AppConstants.ValidationRules.COUNTRY_CODE_NAME_KSA);
        countryCodePicker.resetToDefaultCountry();
        countryCodePicker.hideNameCode(true);
        countryCodePicker.showFlag(false);
    }

    private void parseIntentData() {
        final Bundle bundle = getIntent().getExtras();
        if (null == bundle)
            return;

        verificationMode = bundle.getString(AppConstants.Extras.EXTRA_MOBILE_VER_MODE);
    }

    @OnClick(R.id.btn_mobile_verification_confirm)
    public void onConfirmButtonClicked() {

        mobileNum = tietMobileNumber.getText().toString();

        if (!ValidationUtil.isValidCountryCode(countryCodePicker.getSelectedCountryCode())
                || !ValidationUtil.isValidMobile(mobileNum)) {
            SnackBarUtils.showSnackBar(this, R.string.error_invalid_phone);
            return;
        }

        if (verificationMode.equals(EXTRA_MOBILE_VER_NORMAL))
            callVerifyMobileAPI();
        else
            callRegisterMobileAPI();
    }

    private void callVerifyMobileAPI() {

        if (null == Identity.id)
            return;

        if (!NetworkUtils.isOnline(this)) {
            showInternetError(snackBarLocation);
            return;
        }

        btnConfirm.showLoading();

        final String fullNumber = countryCodePicker.getSelectedCountryCode() + mobileNum;

        new VerifyMobileService().start(new CheckMobileRequest(fullNumber, Identity.getPersonId(), NafazConfig.getLanguage(this)),
                new ApiCallback() {
                    @Override
                    public void onSuccess(Object object) {
                        btnConfirm.hideLoading();
                        MobileInfo.setMobileNumber(fullNumber);
                        CodeVerificationActivity.open(MobileVerificationActivity.this, CodeVerificationActivity.CodeVerificationType.MOBILE, verificationMode);
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

    private void callRegisterMobileAPI() {
        if (null == Identity.id)
            return;

        if (!NetworkUtils.isOnline(this)) {
            showInternetError(snackBarLocation);
            return;
        }

        btnConfirm.showLoading();

        final String fullNumber = countryCodePicker.getSelectedCountryCode() + mobileNum;

        new RegisterMobileService().start(new RegisterMobileRequest(fullNumber, Identity.getPersonId(), NafazConfig.getLanguage(this)),
                new ApiCallback<ApiResponse>() {
                    @Override
                    public void onSuccess(ApiResponse response) {
                        btnConfirm.hideLoading();
                        MobileInfo.setMobileNumber(fullNumber);

                        if (response.code == APIConstants.ResponseCode.CODE_200)
                                startActivity(new Intent(MobileVerificationActivity.this, ActionActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK), false );

//                            SuccessfulVerificationActivity.open(MobileVerificationActivity.this, AppConstants.Extras.EXTRA_SUCCESS_REGISTRATION);

                        else if (response.code == APIConstants.ResponseCode.CODE_100)
                            CodeVerificationActivity.open(MobileVerificationActivity.this, CodeVerificationActivity.CodeVerificationType.MOBILE, verificationMode);
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
}
