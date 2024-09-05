package com.nafaz.android.util;

import android.content.Context;

import androidx.annotation.NonNull;

import com.nafaz.android.app.App;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class Utils {

    /**
     * @return whether the obj(String, List, HashMap) is null or empty
     */
    @Contract("null -> true")
    public static boolean isNullOrEmpty(Object obj) {
        if (obj == null) {
            return true;
        } else if (obj instanceof String) {
            return ((String) obj).trim().length() == 0;
        } else if (obj instanceof List) {
            return ((List) obj).isEmpty();
        } else
            return obj instanceof HashMap && ((HashMap) obj).isEmpty();
    }

    @NonNull
    public static String getString(int resourceId) {
        return App.getContext().getString(resourceId);
    }

    @NotNull
    public static File getTempFile(@NotNull Context context) {
        try {
            return File.createTempFile("photo_", ".jpg", context.getCacheDir());
        } catch (final IOException e) {
            throw new RuntimeException("Unable to create photo file.");
        }
    }
}
