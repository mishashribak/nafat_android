package com.nafaz.android.app;

import android.app.Application;
import android.content.Context;

import androidx.appcompat.widget.AppCompatTextView;

import com.innovatrics.android.dot.Dot;
import com.nafaz.android.BuildConfig;
import com.nafaz.android.R;
import com.nafaz.android.util.LocaleHelper;

import org.jetbrains.annotations.Contract;

import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;
import timber.log.Timber;

public class App extends Application {

    private static final String TAG = App.class.getSimpleName();

    private static App instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        ViewPump.init(ViewPump.builder()
                .addInterceptor(new CalligraphyInterceptor(
                        new CalligraphyConfig.Builder()
                                .setDefaultFontPath(getContext().getResources().getString(R.string.Font_Regular))
                                .addCustomStyle(AppCompatTextView.class, android.R.attr.textViewStyle)
                                .setFontAttrId(R.attr.fontPath)
                                .build()))
                .build());

        // Enable logging in DOT Android Kit. Use this only for debug purposes.
        Dot.setLoggingEnabled(BuildConfig.DEBUG);

        // LicenseId changes with ApplicationId
        Timber.tag(TAG).i("LicenseId: %s", Dot.getInstance().getLicenseId());
    }

    @Contract(pure = true)
    public static synchronized App getContext() {
        return instance;
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(LocaleHelper.wrap(newBase, NafazConfig.getLanguage(newBase))));
    }
}
