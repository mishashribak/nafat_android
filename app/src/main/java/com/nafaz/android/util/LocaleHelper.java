package com.nafaz.android.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Configuration;
import android.os.Build;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public class LocaleHelper extends ContextWrapper {

    public LocaleHelper(Context base) {
        super(base);
    }

    @NotNull
    @Contract("_, _ -> new")
    public static ContextWrapper wrap(@NotNull Context context, @NotNull String language) {
        Configuration config = context.getResources().getConfiguration();

        if (!language.equals("")) {
            Locale locale = new Locale(language);
            Locale.setDefault(locale);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                setSystemLocale(config, locale);
            } else {
                setSystemLocaleLegacy(config, locale);
            }

            config.setLayoutDirection(locale);
            context = context.createConfigurationContext(config);
        }

        return new LocaleHelper(context);
    }

    @Contract(pure = true)
    public static Locale getSystemLocaleLegacy(@NotNull Configuration config) {
        return config.locale;
    }

    @TargetApi(Build.VERSION_CODES.N)
    public static Locale getSystemLocale(@NotNull Configuration config) {
        return config.getLocales().get(0);
    }

    public static void setSystemLocaleLegacy(@NotNull Configuration config, Locale locale) {
        config.locale = locale;
    }

    @TargetApi(Build.VERSION_CODES.N)
    public static void setSystemLocale(@NotNull Configuration config, Locale locale) {
        config.setLocale(locale);
    }
}