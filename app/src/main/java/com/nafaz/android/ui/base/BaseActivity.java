package com.nafaz.android.ui.base;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.nafaz.android.R;
import com.nafaz.android.app.NafazConfig;
import com.nafaz.android.bus.BusProvider;
import com.nafaz.android.entity.AuthLogic;
import com.nafaz.android.entity.UserProfile;
import com.nafaz.android.util.LocaleHelper;
import com.nafaz.android.util.SnackBarUtils;
import com.nafaz.android.util.Utils;
import com.squareup.otto.Bus;

import org.jetbrains.annotations.Nullable;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;

import static android.content.pm.PackageManager.GET_META_DATA;
import static com.nafaz.android.app.App.getContext;

@SuppressLint("Registered")
public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        resetTitles();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
//        super.attachBaseContext(ViewPumpContextWrapper.wrap(LocaleManager.setLocale(newBase)));
        super.attachBaseContext(ViewPumpContextWrapper.wrap(LocaleHelper.wrap(newBase, NafazConfig.getLanguage(newBase))));
    }

    public Context getMyContext(){
        return LocaleHelper.wrap(getContext(),NafazConfig.getLanguage(getContext()));
    }

    @Override
    public void applyOverrideConfiguration(Configuration overrideConfiguration) {
        super.applyOverrideConfiguration(getBaseContext().getResources().getConfiguration());
    }

    protected void resetTitles() {
        try {
            ActivityInfo info = getPackageManager().getActivityInfo(getComponentName(), GET_META_DATA);
            if (info.labelRes != 0) {
                setTitle(info.labelRes);
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setSupportActionBar(@androidx.annotation.Nullable Toolbar toolbar) {
        super.setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    protected void setToolBarTitle(Toolbar toolBar, @StringRes int toolBarTitleResId) {
        toolBar.postDelayed(() -> {
            toolBar.setTitle(toolBarTitleResId);
        }, 50);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void finish() {
        super.finish();
        finish(true);
    }

    protected void finish(boolean withAnimations) {
        if (withAnimations)
            overridePendingTransitionExit();
        else {
            super.finish();
            overridePendingTransition(0, 0);
        }
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        startActivity(intent, true);
    }

    protected void startActivity(Intent intent, boolean withAnimations) {
        if (withAnimations)
            overridePendingTransitionEnter();
        else {
            super.startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        }
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        overridePendingTransitionEnter();
    }

    /**
     * Overrides the pending Activity transition by performing the "Enter" animation.
     */
    protected void overridePendingTransitionEnter() {
        if (NafazConfig.isEnglish())
            overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
        else
            overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }

    /**
     * Overrides the pending Activity transition by performing the "Exit" animation.
     */
    protected void overridePendingTransitionExit() {
        if (NafazConfig.isEnglish())
            overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
        else
            overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransitionExit();

        clearModelsMemoryData();
    }

    protected void clearModelsMemoryData() {
        AuthLogic.authMethods = null;
        AuthLogic.methodTypes = null;
        UserProfile.set(null);
    }


    protected void showGeneralError(String message) {
        SnackBarUtils.showSnackBar(this, null != message ? message : getMyContext().getString(R.string.error_general));
    }

    protected void showInternetError(ViewGroup viewGroup) {
        SnackBarUtils.showNoInternetSnackBar(viewGroup);
    }

    /**
     * Handle @{@link BusProvider} registration/un-registration action when needed
     *
     * @param isRegister Flag indicating intended action
     */
    public void handleBusProviderRegistration(boolean isRegister) {
        final Bus busProvider = BusProvider.getInstance();
        if (busProvider == null)
            return;

        if (isRegister)
            busProvider.register(this);
        else
            busProvider.unregister(this);
    }
}

