package com.nafaz.android.app;

import android.content.Context;

import com.nafaz.android.manager.PrefManager;

import static com.nafaz.android.manager.PrefManager.Keys.KEY_LANGUAGE;

public class NafazConfig {

    /**
     * For English locale.
     */
    public static final String LANGUAGE_KEY_ENGLISH = "en";
    /**
     * for Arabic locale.
     */
    public static final String LANGUAGE_KEY_ARABIC = "ar";

    /**
     * Get saved Locale from SharedPreferences.
     *
     * @param context current context.
     * @return current locale key by default return english locale.
     */
    public static String getLanguage(Context context) {
        return PrefManager.getInstance(context).getPreferences(KEY_LANGUAGE, LANGUAGE_KEY_ARABIC);
    }

    /**
     * set pref key.
     *
     * @param context  {@link Context}.
     * @param localeKey locale key of defined languages.
     */
    public static void setLanguage(Context context, String localeKey) {
        PrefManager.getInstance(context).setPreferences(KEY_LANGUAGE, localeKey);
    }

    public static boolean isEnglish() {
        return PrefManager.getInstance(App.getContext())
                .getPreferences(KEY_LANGUAGE, LANGUAGE_KEY_ARABIC).equals(LANGUAGE_KEY_ENGLISH);
    }
}
