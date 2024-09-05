package com.nafaz.android.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.nafaz.android.R;
import com.nafaz.android.ui.base.BaseActivity;

import org.jetbrains.annotations.NotNull;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeActivity extends BaseActivity {

    @BindView(R.id.btn_capture_code)
    Button btnCaptureCode;

    @BindView(R.id.btn_account_management)
    Button btnAccountManagement;

    public static void open(@NotNull Context context) {
        context.startActivity(new Intent(context, HomeActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_capture_code)
    public void onCaptureCodeButtonClicked() {
        // TODO: 2019-06-19
    }

    @OnClick(R.id.btn_account_management)
    public void onAccountManagementButtonClicked() {
        // TODO: 2019-06-19
        AccountManagementActivity.open(this);
    }

}
