package com.nafaz.android.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.nafaz.android.R;
import com.nafaz.android.ui.base.BaseActivity;

import org.jetbrains.annotations.NotNull;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class IDCardActivity extends BaseActivity {

    @BindView(R.id.toolbar_common)
    Toolbar commonToolbar;

    @BindView(R.id.snackbarlocation)
    CoordinatorLayout snackBarLocation;

    public static void open(@NotNull Context context) {
        context.startActivity(new Intent(context, IDCardActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_id_card);
        ButterKnife.bind(this);
        setSupportActionBar(commonToolbar);
    }

    @OnClick(R.id.btn_id_card_capture)
    public void onCaptureButtonClicked() {
        finish();
        CodeVerificationActivity.open(this, CodeVerificationActivity.CodeVerificationType.ID_CARD);

        // TODO: 2019-06-22 call api
    }
}
