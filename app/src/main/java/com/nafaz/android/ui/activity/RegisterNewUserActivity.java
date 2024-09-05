package com.nafaz.android.ui.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;

import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.textfield.TextInputEditText;
import com.nafaz.android.R;
import com.nafaz.android.api.callback.ApiCallback;
import com.nafaz.android.api.entity.RegisterNewUserRequest;
import com.nafaz.android.api.services.RegisterNewUserService;
import com.nafaz.android.app.NafazConfig;
import com.nafaz.android.ui.base.BaseActivity;
import com.nafaz.android.ui.view.button.LoadingMaterialButton;
import com.nafaz.android.util.NetworkUtils;
import com.nafaz.android.util.SnackBarUtils;
import com.nafaz.android.util.ValidationUtil;

import org.jetbrains.annotations.NotNull;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterNewUserActivity extends BaseActivity {

    private String username;
    private String password;

    public static void open(@NotNull Context context) {
        context.startActivity(new Intent(context, RegisterNewUserActivity.class));
    }

    @BindView(R.id.toolbar_common)
    Toolbar commonToolBar;

    @BindView(R.id.snackbarlocation)
    CoordinatorLayout snackBarLocation;

    @BindView(R.id.tiet_user_name)
    TextInputEditText tietUsername;

    @BindView(R.id.tiet_password)
    TextInputEditText tietPassword;

    @BindView(R.id.tiet_confirm_password)
    TextInputEditText tietConfirmPassword;

    @BindView(R.id.btn_new_account_next)
    LoadingMaterialButton btnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_account);
        ButterKnife.bind(this);
        setSupportActionBar(commonToolBar);

        setFieldsGravity();
    }

    @SuppressLint("RtlHardcoded")
    private void setFieldsGravity() {
        tietUsername.setGravity(NafazConfig.isEnglish() ? Gravity.LEFT : Gravity.RIGHT);
        tietPassword.setGravity(NafazConfig.isEnglish() ? Gravity.LEFT : Gravity.RIGHT);
        tietConfirmPassword.setGravity(NafazConfig.isEnglish() ? Gravity.LEFT : Gravity.RIGHT);
    }

    @OnClick(R.id.btn_new_account_next)
    public void onNextButtonClicked() {

        username = tietUsername.getText().toString();
        password = tietPassword.getText().toString();
        final String confirmPassword = tietConfirmPassword.getText().toString();

        // ----- User Name ----- //
        if (!ValidationUtil.isLengthEnough(username)) {
            SnackBarUtils.showSnackBar(this, R.string.error_username_input);
            return;
        }

        if (!ValidationUtil.isContainValidCharactersUsername(username)) {
            SnackBarUtils.showSnackBar(this, R.string.error_contain_only);
            return;
        }

        if (ValidationUtil.isContainSpecialCharacter(username)) {
            SnackBarUtils.showSnackBar(this, R.string.error_username_contain_special);
            return;
        }

        if (!ValidationUtil.isEnglish(username)) {
            SnackBarUtils.showSnackBar(this, R.string.error_contain_arabic);
            return;
        }



        // ----- Password ----- //
        if (!ValidationUtil.isLengthEnough(password)) {
            SnackBarUtils.showSnackBar(this,  R.string.error_pass_input);
            return;
        }

        if (!ValidationUtil.isContainDigit(password)) {
            SnackBarUtils.showSnackBar(this, R.string.error_contain_number);
            return;
        }

        if (!ValidationUtil.isContainSpecialCharacter(password)) {
            SnackBarUtils.showSnackBar(this,  R.string.error_pass_contain_special);
            return;
        }

        if (!ValidationUtil.isEnglish(password)) {
            SnackBarUtils.showSnackBar(this, R.string.error_pass_contain_arabic);
            return;
        }

        if (ValidationUtil.isRepeatingSameCharacters(password)){
            SnackBarUtils.showSnackBar(this, R.string.error_repet_single);
            return;
        }

        if (ValidationUtil.isRepeatingSameBlock(password)){
            SnackBarUtils.showSnackBar(this, R.string.error_repet_block);
            return;
        }

        if (password.contains(username)){
            SnackBarUtils.showSnackBar(this, R.string.error_contain_name);
            return;
        }

        if (!ValidationUtil.isMatchingPasswords(password, confirmPassword)) {
            SnackBarUtils.showSnackBar(this, R.string.error_password_not_match);
            return;
        }

        callRegisterNewUserAPI();
    }

    private void callRegisterNewUserAPI() {

        if (!NetworkUtils.isOnline(this)) {
            showInternetError(snackBarLocation);
            return;
        }

        btnNext.showLoading();

        new RegisterNewUserService().start(new RegisterNewUserRequest(password, username, NafazConfig.getLanguage(this)),
                new ApiCallback() {
                    @Override
                    public void onSuccess(Object object) {
                        btnNext.hideLoading();
                        finish();
                        AuthenticationPromptActivity.open(RegisterNewUserActivity.this, AuthenticationPromptActivity.VerificationPromptType.MOBILE);
//                        HomeActivity.open(RegisterNewUserActivity.this);
                    }

                    @Override
                    public void onError(String errorMessage) {
                        btnNext.hideLoading();
                        showGeneralError(errorMessage);
                    }

                    @Override
                    public void onFail(String failMessage) {
                        btnNext.hideLoading();
                        showGeneralError(failMessage);
                    }
                });
    }
}
