package com.nafaz.android.util;

import androidx.core.content.ContextCompat;

import com.nafaz.android.app.App;

public class UIUtils {

    public static int getCompatColor(int color) {
        return ContextCompat.getColor(App.getContext(), color);
    }
}
