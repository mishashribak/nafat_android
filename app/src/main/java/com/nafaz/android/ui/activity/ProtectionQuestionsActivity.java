package com.nafaz.android.ui.activity;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.nafaz.android.R;
import com.nafaz.android.ui.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProtectionQuestionsActivity extends BaseActivity {


    @BindView(R.id.toolbar_common)
    Toolbar commonToolbar;

    @BindView(R.id.snackbarlocation)
    CoordinatorLayout snackBarLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_protection_questions);
        ButterKnife.bind(this);
        setSupportActionBar(commonToolbar);
    }

//    @OnClick(R.id.btn_protection_questions_confirm)
//    public void onConfirmButtonClicked() {
//
//        AuthLogic.add(AuthLogic.MethodType.PROTECTION_QUES);
//
//        if (AuthLogic.hasAtLeastTwoVerifiedMethods()) {
//            VerificationInfoDialog.show(
//                    this,
//                    VerificationInfoDialog.DialogType.VERIFICATION,
//                    new VerificationInfoDialog.OnVerificationInfoListener() {
//                        @Override
//                        public void onNext() {
//                            finish();
//                            RegisterNewUserActivity.open(ProtectionQuestionsActivity.this);
//                        }
//
//                        @Override
//                        public void onBack() {
//                            finish();
//                        }
//                    });
//
//        } else
//            finish();
//    }
}
