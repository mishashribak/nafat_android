package com.nafaz.android.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.nafaz.android.R;
import com.nafaz.android.app.AppConstants;
import com.nafaz.android.entity.AuthLogic;
import com.nafaz.android.ui.base.BaseActivity;
import com.nafaz.android.util.Utils;

import org.jetbrains.annotations.NotNull;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SuccessfulVerificationActivity extends BaseActivity {

    @BindView(R.id.toolbar_common)
    Toolbar commonToolBar;

    @BindView(R.id.tv_successful_verification_description)
    TextView tvDescription;

    private String successMode;

    public static void open(@NotNull Context context) {
        context.startActivity(new Intent(context, SuccessfulVerificationActivity.class));
    }

    public static void open(@NotNull Context context, String successMode) {
        context.startActivity(new Intent(context, SuccessfulVerificationActivity.class)
                .putExtra(AppConstants.Extras.EXTRA_SUCCESS_MODE, successMode));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_successful_verification);
        ButterKnife.bind(this);
        setSupportActionBar(commonToolBar);
        parseIntentData();
        handleScreenTextLogic();
    }

    private void parseIntentData() {
        final Bundle bundle = getIntent().getExtras();
        if (null == bundle)
            return;

        successMode = bundle.getString(AppConstants.Extras.EXTRA_SUCCESS_MODE);
    }

    private void handleScreenTextLogic() {
        if (!Utils.isNullOrEmpty(successMode))
            tvDescription.setText(successMode.equals(AppConstants.Extras.EXTRA_SUCCESS_REGISTRATION) ?
                    getMyContext().getString(R.string.activity_successful_reg_description) :
                    getMyContext().getString(R.string.activity_successful_mobile_verif_description));
    }

    @OnClick(R.id.btn_successful_verification)
    public void onSuccessfulVerificationButtonClicked() {

        finish();

        if(AuthLogic.currentStep == 1 && AuthLogic.hasOneVerifiedMethodsInOneStep()){
            startActivity(new Intent(this, AuthenticationMethodsActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK), false );
        }else if(AuthLogic.currentStep> 1 && AuthLogic.hasOneVerifiedMethodsInTwoStep()){
            startActivity(new Intent(this, ActionActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK), false );
        }

        // This means the user confirms his new mobile
        if (successMode.equals(AppConstants.Extras.EXTRA_SUCCESS_REGISTRATION)) {

            // Clear Memory data
//            clearModelsMemoryData();

            // We go to Splash page for now
            startActivity(new Intent(this, ActionActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK), false );
//            ActionActivity.open(this);

            return;
        }

        // User done both Mobile and Face? go to registration now.
//        if (AuthLogic.hasAtLeastTwoVerifiedMethods()) {
//            RegisterNewUserActivity.open(this);
//            return;
//        }

//        startActivity(new Intent(this, AuthenticationMethodsActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK), false );
//        AuthenticationMethodsActivity.open(this);

        // Otherwise, we check next steps from second array
//        AuthLogic.MethodType methodType = AuthLogic.getNextStepFromSecondArray();
//
//        switch (methodType) {
//            case MOBILE:
//                // Mobile is done now
//                break;
//
//            case FACE:
//                FaceFingerprintActivity.open(this);
//                break;
//
//            case ID_NUM:
//            case PROTECTION_QUES:
//            case EMAIL:
//                // In Later stages
//                break;
//
//            case ALL:
//                AuthenticationMethodsActivity.open(this);
//                break;
//        }
    }

    @Override
    public void onBackPressed() {

    }
}
