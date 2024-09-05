package com.nafaz.android.util;

import android.app.Activity;
import android.content.Context;
import android.view.ViewGroup;

import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;
import com.nafaz.android.R;
import com.nafaz.android.app.App;

public class SnackBarUtils {

    public static void showSnackBar(ViewGroup parentLayout, int messageResId) {
        Snackbar.make(parentLayout, messageResId, Snackbar.LENGTH_SHORT).show();
    }

    public static void showSnackBar(ViewGroup parentLayout, String message) {
        Snackbar.make(parentLayout, message, Snackbar.LENGTH_SHORT).show();
    }

    public static void showNoInternetSnackBar(ViewGroup parentLayout) {
        Snackbar snackbar = Snackbar.make(parentLayout, R.string.error_internet_not_present, Snackbar.LENGTH_LONG);
        snackbar.getView().setBackgroundColor(ContextCompat.getColor(App.getContext(), R.color.red));
        snackbar.show();
    }

    /**
     * Show a {@link Snackbar} with given message resource id.
     *
     * @param context  {@link Context} of the calling screen.
     * @param msgResId message resource id.
     */
    public static void showSnackBar(Context context, int msgResId) {
        // Get first child of current xml page and anchor the view to it.
        // This is much generic solution rather than declaring root layout in each screen then
        // pass it to the Snackbar's make method.
        final ViewGroup viewGroup = (ViewGroup) ((ViewGroup) ((Activity) context)
                .findViewById(android.R.id.content)).getChildAt(0);

        Snackbar.make(
                viewGroup,
                msgResId,
                Snackbar.LENGTH_LONG)
                .show();
    }

    /**
     * Show a {@link Snackbar} with given message resource id.
     *
     * @param context {@link Context} of the calling screen.
     * @param message message .
     */
    public static void showSnackBar(Context context, String message) {
        // Get first child of current xml page and anchor the view to it.
        // This is much generic solution rather than declaring root layout in each screen then
        // pass it to the Snackbar's make method.
        final ViewGroup viewGroup = (ViewGroup) ((ViewGroup) ((Activity) context)
                .findViewById(android.R.id.content)).getChildAt(0);

        Snackbar.make(
                viewGroup,
                message,
                Snackbar.LENGTH_LONG)
                .show();
    }
}
