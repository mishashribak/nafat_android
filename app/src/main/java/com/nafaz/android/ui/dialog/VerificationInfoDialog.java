package com.nafaz.android.ui.dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.nafaz.android.R;
import com.nafaz.android.util.Utils;

public class VerificationInfoDialog {

    public static void show(Context context, DialogType dialogType, final OnVerificationInfoListener listener) {

        final MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(context);

        @SuppressLint("InflateParams") final View customDialog =
                ((Activity) context).getLayoutInflater()
                        .inflate(R.layout.dialog_verification_info, null, false);

        dialogBuilder.setView(customDialog);
        dialogBuilder.setCancelable(false);

        final AlertDialog dialog = dialogBuilder.create();

        // Setting dialog view
        if (null != dialog.getWindow()) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        // Title
        TextView tvTitle = customDialog.findViewById(R.id.tv_dialog_title);
        tvTitle.setText(dialogType == DialogType.VERIFICATION ?
                Utils.getString(R.string.dialog_verification_info_title) :
                Utils.getString(R.string.title_dialog_device_delete));

        // Message
        TextView tvMessage = customDialog.findViewById(R.id.tv_dialog_message);
        tvMessage.setText(dialogType == DialogType.VERIFICATION ?
                Utils.getString(R.string.dialog_verification_info_message) :
                Utils.getString(R.string.message_dialog_device_delete));

        // Positive Button
        MaterialButton btnPositive = customDialog.findViewById(R.id.btn_dialog_next);
        btnPositive.setText(dialogType == DialogType.VERIFICATION ?
                Utils.getString(R.string.btn_next) :
                Utils.getString(R.string.btn_yes));

        btnPositive.setOnClickListener(view -> {
            dialog.dismiss();
            listener.onNext();
        });

        // Negative Button
        MaterialButton btnNegative = customDialog.findViewById(R.id.btn_dialog_back);
        btnNegative.setText(dialogType == DialogType.VERIFICATION ?
                Utils.getString(R.string.btn_back) :
                Utils.getString(R.string.btn_no));

        btnNegative.setOnClickListener(view -> {
            dialog.dismiss();
            listener.onBack();
        });

        dialog.show();
    }

    public interface OnVerificationInfoListener {
        void onNext();

        void onBack();
    }

    public enum DialogType {
        VERIFICATION,
        PROMPT
    }
}
