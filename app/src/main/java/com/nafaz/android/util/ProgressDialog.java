package com.nafaz.android.util;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;

import com.nafaz.android.R;

public class ProgressDialog {

    private static Dialog dialog;

    public static void showProgress(Context context) {
        try{
            if(dialog != null && dialog.isShowing())
                return;

            dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.setCanceledOnTouchOutside(false);
            dialog.setContentView(R.layout.dialog_progressbar);

            dialog.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public static void hideprogressbar() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            dialog=null;
        }
    }

    private static android.app.ProgressDialog progressDialog;

    public static void showProgressDialog(int messageId, Context context) {
        if (progressDialog != null && progressDialog.isShowing()) {
            return;
        }

        progressDialog = new android.app.ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(context.getString(messageId));
        progressDialog.show();
    }

    public static void hideProgressDialog() {
        if (progressDialog == null) {
            return;
        }

        progressDialog.dismiss();
    }

}