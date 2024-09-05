package com.nafaz.android.ui.activity;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.nafaz.android.R;
import com.nafaz.android.app.NafazConfig;
import com.nafaz.android.entity.AuthLogic;
import com.nafaz.android.entity.UserProfile;
import com.nafaz.android.manager.PrefManager;
import com.nafaz.android.ui.base.BaseActivity;
import com.nafaz.android.util.AnimationUtils;

import org.jetbrains.annotations.NotNull;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SplashActivity extends BaseActivity {

    @BindView(R.id.imgLang)
    ImageView imgLang;

    @BindView(R.id.btn_onetime)
    Button btnOnetime;

    @BindView(R.id.btn_auth)
    Button btnAuth;

    public static void open(@NotNull Context context) {
        context.startActivity(new Intent(context, SplashActivity.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ButterKnife.bind(this);

        if(NafazConfig.isEnglish()){
            imgLang.setImageDrawable(getResources().getDrawable(R.drawable.ar_lang));
        }else{
            imgLang.setImageDrawable(getResources().getDrawable(R.drawable.en_lang));
        }

        animateButtons();

        UserProfile.set(null);

        if(! PrefManager.getInstance(this).getPreferences("totp", "").isEmpty()){
            btnOnetime.setEnabled(true);
        } else{
            btnOnetime.setEnabled(false);
        }

    }

    private void animateButtons() {
        AnimationUtils.animateSlideDownFromTop(imgLang);
        AnimationUtils.animateSlideDownFromTop(btnOnetime);
        AnimationUtils.animateSlideDownFromTop(btnAuth);
    }

    @OnClick(R.id.imgLang)
    public void onLanguageButtonClicked() {
        // Change language and save in prefs
        NafazConfig.setLanguage(this, NafazConfig.isEnglish() ?
                NafazConfig.LANGUAGE_KEY_ARABIC : NafazConfig.LANGUAGE_KEY_ENGLISH);

        // Restart page
        finish(false);
        startActivity(getIntent(), false);
    }

    @OnClick(R.id.btn_onetime)
    public void onLoginButtonClicked() {
        startActivity(new Intent(this, GeneratorOTPActivity.class), false );
    }

    @OnClick(R.id.btn_auth)
    public void onRegisterButtonClicked() {
        // TODO: 2019-06-19
        startActivity(new Intent(this, IDNumberActivity.class), false );
    }
}
