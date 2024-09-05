package com.nafaz.android.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.textfield.TextInputEditText;
import com.nafaz.android.R;
import com.nafaz.android.api.callback.ApiCallback;
import com.nafaz.android.api.entity.CheckIdRequest;
import com.nafaz.android.api.entity.CheckIdResponse;
import com.nafaz.android.api.services.CheckIdentityService;
import com.nafaz.android.app.AppConstants;
import com.nafaz.android.app.NafazConfig;
import com.nafaz.android.entity.AuthLogic;
import com.nafaz.android.entity.AuthenticationMethod;
import com.nafaz.android.entity.Identity;
import com.nafaz.android.ui.base.BaseActivity;
import com.nafaz.android.ui.view.button.LoadingMaterialButton;
import com.nafaz.android.util.AnimationUtils;
import com.nafaz.android.util.NetworkUtils;
import com.nafaz.android.util.SnackBarUtils;
import com.nafaz.android.util.ValidationUtil;

import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class IDNumberActivity extends BaseActivity {

    @BindView(R.id.toolbar_common)
    Toolbar commonToolbar;

    @BindView(R.id.snackbarlocation)
    CoordinatorLayout snackBarLocation;

    @BindView(R.id.tiet_id_number)
    TextInputEditText fieldIdNumber;

    @BindView(R.id.btn_id_num_confirm)
    LoadingMaterialButton btnConfirm;

    @BindView(R.id.imgIdNumber)
    ImageView imgIdNumber;

    @BindView(R.id.tv_enter_id_label)
    TextView tvIdLabel;


    public static void open(@NotNull Context context) {
        context.startActivity(new Intent(context, IDNumberActivity.class));
    }

    private String personId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_id_number);
        ButterKnife.bind(this);
        setSupportActionBar(commonToolbar);

        imgIdNumber.setVisibility(View.INVISIBLE);
        btnConfirm.setVisibility(View.INVISIBLE);
        fieldIdNumber.setVisibility(View.INVISIBLE);
        findViewById(R.id.til_id_number).setVisibility(View.INVISIBLE);
        tvIdLabel.setVisibility(View.INVISIBLE);
        AnimationUtils.animateSlideFromRight(imgIdNumber);
        AnimationUtils.animateSlideFromLeft(btnConfirm);
        AnimationUtils.animateSlideFromLeft(fieldIdNumber);
        AnimationUtils.animateSlideFromLeft(findViewById(R.id.til_id_number));
        AnimationUtils.animateSlideFromLeft(tvIdLabel);

    }

    @OnClick(R.id.btn_id_num_confirm)
    public void onConfirmButtonClicked() {

        personId = fieldIdNumber.getText().toString();

        if (!ValidationUtil.isValidID(personId)) {
            SnackBarUtils.showSnackBar(this, R.string.error_invalid_id);
            return;
        }

        callConfirmIdAPI();
    }

    private void callConfirmIdAPI() {

        if (!NetworkUtils.isOnline(this)) {
            showInternetError(snackBarLocation);
            return;
        }

        btnConfirm.showLoading();

        new CheckIdentityService().start(IDNumberActivity.this,
                new CheckIdRequest(NetworkUtils.getIPAddress(true), getNationalId(), "1234"),
                new ApiCallback<CheckIdResponse>() {
                    @Override
                    public void onSuccess(CheckIdResponse checkIdResponse) {
                        btnConfirm.hideLoading();

                        Identity.setPersonId(personId);
                        AuthLogic.authMethods = checkIdResponse.authenticationMethods;
                        AuthLogic.availableActions = checkIdResponse.availableActions;

                        handleRoutingLogic();
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

    private void handleRoutingLogic() {
        final AuthLogic.MethodType which = AuthLogic.decideRouteFromFirstStep(this);
        if (which != null) {

            switch (which) {

                case MOBILE:
//                    MobileVerificationActivity.open(this, AppConstants.Extras.EXTRA_MOBILE_VER_NORMAL);
                    startActivity(new Intent(this, MobileVerificationActivity.class), false );
                    break;

                case FACE:
//                    FaceFingerprintActivity.open(this);
                    startActivity(new Intent(this, FaceFingerprintActivity.class), false );
                    break;

                case ALL:
//                    AuthenticationMethodsActivity.open(this);
                    AuthLogic.methodTypes = null;
                    AuthLogic.currentStep = 0;
                    startActivity(new Intent(this, AuthenticationMethodsActivity.class), false );
                    break;

                // Those sections are hidden based on requirements
                case ID_NUM:
                case PROTECTION_QUES:
                default:
                    break;
            }

        } else
            showGeneralError(null);
    }

    // Get user personId/NationalId
    @NotNull
    private BigInteger getNationalId() {
        return BigInteger.valueOf(Long.parseLong(personId));
    }

//    @OnClick(R.id.tv_create_new_account)
//    public void onRegisterAccountLabelClicked() {
//        // TODO: 2019-06-20 go to register page
//        SnackBarUtils.showSnackBar(this, R.string.snackbar_message_coming_soon);
//    }
}
