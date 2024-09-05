package com.nafaz.android.manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

public class PrefManager {

    /**
     * Shared Pref keys
     */
    public interface Keys {
        String KEY_LANGUAGE = "language";
    }

    // Shared Preferences
    private SharedPreferences sharedPreferences;

    private static PrefManager prefManager;

    public static synchronized PrefManager getInstance(Context context) {
        if (prefManager == null) {
            prefManager = new PrefManager(context);
        }
        return prefManager;
    }

    private PrefManager(@NotNull Context context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

    }

    @SuppressWarnings("unused")
    public void setPreferences(String key, String value) {
        final Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public void setPreferences(String key, boolean value) {
        final Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }


    public void setPreferences(String key, int value) {
        final Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }


    @SuppressWarnings("unused")
    public void setPreferences(String key, long value) {
        final Editor editor = sharedPreferences.edit();
        editor.putLong(key, value);
        editor.apply();
    }


    @SuppressWarnings("unused")
    public void setObjectPreferences(String key, Object value) {
        final Editor editor = sharedPreferences.edit();
        final String json = new Gson().toJson(value);
        editor.putString(key, json);
        editor.apply();
    }

    @SuppressWarnings("unused")
    public String getPreferences(String key, String defualt) {
        return sharedPreferences.getString(key, defualt);
    }

    public boolean getPreferences(String key, boolean defualt) {
        return sharedPreferences.getBoolean(key, defualt);
    }

    @SuppressWarnings("unused")
    public long getPreferences(String key, long defualt) {
        return sharedPreferences.getLong(key, defualt);
    }

    public int getPreferences(String key, int defualt) {
        return sharedPreferences.getInt(key, defualt);
    }

    @SuppressWarnings("unused")
    public Object getObjectPreferences(String key, Object defaultValue) {
        final String res = sharedPreferences.getString(key, "");
        Object obj = new Gson().fromJson(res, defaultValue.getClass());
        if (obj == null) {
            obj = defaultValue;
        }
        return obj;
    }

    @SuppressWarnings("unused")
    public void clear() {
        final Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    public boolean isEmpty() {
        return sharedPreferences.getAll().isEmpty();
    }
}