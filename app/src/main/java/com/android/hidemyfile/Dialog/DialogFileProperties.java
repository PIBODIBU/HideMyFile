package com.android.hidemyfile.Dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.TextView;

import com.android.hidemyfile.R;
import com.android.hidemyfile.Support.File.FileModel;
import com.android.hidemyfile.Support.Utils;

import java.io.File;

public class DialogFileProperties extends DialogFragment {

    private static final String TAG = "DialogFileProperties";

    private Activity activity;
    private FileModel fileModel;
    private AlertDialog alertDialog;

    private TextView TVName;
    private TextView TVPath;
    private TextView TVSize;
    private TextView TVModified;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        activity = getActivity();
        View rootView = activity.getLayoutInflater().inflate(R.layout.dialog_file_info, null);

        TVName = (TextView) rootView.findViewById(R.id.name);
        TVPath = (TextView) rootView.findViewById(R.id.path);
        TVSize = (TextView) rootView.findViewById(R.id.size);
        TVModified = (TextView) rootView.findViewById(R.id.modified);

        File file = new File(fileModel.getFilePath());

        TVName.setText(fileModel.getFileName());
        TVPath.setText(fileModel.getFilePath());
        TVSize.setText(String.valueOf(Utils.bytesToHumanReadable(file.length(), true)));
        TVModified.setText(Utils.convertMsToDate(String.valueOf(file.lastModified())));

        alertDialog = new AlertDialog.Builder(getActivity())
                .setView(rootView)
                .setTitle(getString(R.string.dialog_file_properties_title))
                .setPositiveButton(getString(R.string.dialog_file_properties_positive), new DialogInterface.OnClickListener() {
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
            }
        });

        setCancelable(false);

        return alertDialog;
    }

    public void setFileModel(@NonNull FileModel fileModel) {
        this.fileModel = fileModel;
    }

    public FileModel getFileModel() {
        return fileModel;
    }
}
