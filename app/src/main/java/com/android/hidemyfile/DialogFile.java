package com.android.hidemyfile;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.TextView;

public class DialogFile extends DialogFragment {

    private static final String TAG = "DialogFile";

    private static final String PATTERN_JPG = ".jpg";

    private AlertDialog alertDialog;
    private AlertDialog.Builder alertDialogBuilder;
    private DialogCallbacks dialogCallbacks;

    private TextView TVFileType;

    private String filePath;

    public void setDialogCallbacks(DialogCallbacks dialogCallbacks) {
        this.dialogCallbacks = dialogCallbacks;
    }

    public void init(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View rootView = getActivity().getLayoutInflater().inflate(R.layout.dialog_file, null);

        TVFileType = (TextView) rootView.findViewById(R.id.text_type);

        if (filePath.endsWith(PATTERN_JPG)) {
            TVFileType.setText("Music File");
        }

        alertDialogBuilder = new AlertDialog.Builder(getActivity())
                .setTitle("File options")
                .setCancelable(false)
                .setView(rootView)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dismiss();
                    }
                })
                .setPositiveButton("Encrypt", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (dialogCallbacks != null) {
                            dialogCallbacks.onPositive();
                        }
                    }
                });

        setCancelable(false);

        alertDialog = alertDialogBuilder.create();

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogArg) {
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(getActivity(), R.color.black));
            }
        });

        return alertDialog;
    }

    public interface DialogCallbacks {
        void onPositive();
    }
}
