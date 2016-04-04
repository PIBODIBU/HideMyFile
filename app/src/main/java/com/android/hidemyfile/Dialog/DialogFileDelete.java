package com.android.hidemyfile.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import com.android.hidemyfile.R;

public class DialogFileDelete extends DialogFragment {

    private static final String TAG = "DialogFileDecrypt";

    private AlertDialog alertDialog;
    private DialogCallbacks dialogCallbacks;

    public void setDialogCallbacks(DialogCallbacks dialogCallbacks) {
        this.dialogCallbacks = dialogCallbacks;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        alertDialog = new AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.dialog_file_delete_title))
                .setMessage(getString(R.string.dialog_file_delete_message))
                .setPositiveButton(getString(R.string.dialog_file_delete_positive), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (dialogCallbacks != null) {
                            dialogCallbacks.onPositive();
                        }
                    }
                })
                .setNegativeButton(getString(R.string.dialog_file_delete_negative), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(getActivity(), R.color.black));
            }
        });

        setCancelable(false);

        return alertDialog;
    }

    public interface DialogCallbacks {
        void onPositive();
    }
}
