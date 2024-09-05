package com.nafaz.android.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.nafaz.android.R;
import com.nafaz.android.api.callback.ApiCallback;
import com.nafaz.android.api.entity.RegisterMobileRequest;
import com.nafaz.android.api.services.RegisterMobileService;
import com.nafaz.android.app.AppConstants;
import com.nafaz.android.app.NafazConfig;
import com.nafaz.android.entity.Identity;
import com.nafaz.android.entity.MobileInfo;
import com.nafaz.android.ui.base.BaseActivity;
import com.nafaz.android.ui.view.button.LoadingMaterialButton;
import com.nafaz.android.util.NetworkUtils;
import com.nafaz.android.util.Utils;

import org.jetbrains.annotations.NotNull;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AuthenticationPromptActivity extends BaseActivity {

    public static void open(@NotNull Context context, @NotNull VerificationPromptType type) {
        context.startActivity(new Intent(context, AuthenticationPromptActivity.class)
                .putExtra(AppConstants.Extras.EXTRA_TYPE, type.name()));
    }

    @BindView(R.id.toolbar_common)
    Toolbar commonToolBar;

    @BindView(R.id.snackbarlocation)
    CoordinatorLayout snackBarLocation;

    @BindView(R.id.tv_verification_prompt_description)
    TextView tvDescription;

    @BindView(R.id.btn_verification_prompt_yes)
    LoadingMaterialButton btnYes;

    private String promptType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification_prompt);
        ButterKnife.bind(this);
        parseIntentData();
        setSupportActionBar(commonToolBar);
        setupToolBarTitle();
        setupDescription();
    }

    private void parseIntentData() {
        final Bundle bundle = getIntent().getExtras();
        if (null == bundle)
            return;

        promptType = bundle.getString(AppConstants.Extras.EXTRA_TYPE);
    }

    private void setupToolBarTitle() {
        if (promptType.equals(VerificationPromptType.MOBILE.name())) {
            setToolBarTitle(commonToolBar, R.string.activity_mobile_verification_title);
        } else {
//            setToolBarTitle(commonToolBar, R.string.activity_email_verification_title);
        }
    }

    private void setupDescription() {
        if (promptType.equals(VerificationPromptType.MOBILE.name())) {
            tvDescription.setText(getMyContext().getString(R.string.activity_verification_prompt_number_description_label));
        } else {
            tvDescription.setText(getMyContext().getString(R.string.activity_verification_prompt_device_description_label));
        }
    }

    @OnClick(R.id.btn_verification_prompt_yes)
    public void onYesButtonClicked() {
        if (promptType.equals(VerificationPromptType.MOBILE.name())) {

            callRegisterMobileAPI();

        } else {
            // TODO: 2019-06-25 do logic of device verification
        }
    }

    private void callRegisterMobileAPI() {

        if (null == Identity.id)
            return;

        if (!NetworkUtils.isOnline(this)) {
            showInternetError(snackBarLocation);
            return;
        }

        btnYes.showLoading();

        new RegisterMobileService().start(
                new RegisterMobileRequest(MobileInfo.getMobileNumber(), Identity.getPersonId(), NafazConfig.getLanguage(this)),
                new ApiCallback() {
                    @Override
                    public void onSuccess(Object object) {
                        btnYes.hideLoading();
                        finish();

                        // Clear Memory data
                        clearModelsMemoryData();
                        SplashActivity.open(AuthenticationPromptActivity.this);
                        // Commented for later stages
//                        HomeActivity.open(AuthenticationPromptActivity.this);
                    }

                    @Override
                    public void onError(String errorMessage) {
                        btnYes.hideLoading();
                        showGeneralError(errorMessage);
                    }

                    @Override
                    public void onFail(String failMessage) {
                        btnYes.hideLoading();
                        showGeneralError(failMessage);
                    }
                });
    }

    @OnClick(R.id.btn_verification_prompt_no)
    public void onNoButtonClicked() {
        if (promptType.equals(VerificationPromptType.MOBILE.name())) {
            finish();
            MobileVerificationActivity.open(this, AppConstants.Extras.EXTRA_MOBILE_VER_NEW);

        } else {
            // TODO: 2019-06-25 do logic of device verification
        }
    }

    public enum VerificationPromptType {
        MOBILE,
        DEVICE
    }
}
