package com.android.hidemyfile.Dialog;

import android.app.Activity;
import android.app.Dialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.View;
import android.widget.TextView;

import com.android.hidemyfile.R;
import com.android.hidemyfile.Support.File.FileModel;

public class DialogFileAction extends BottomSheetDialogFragment {

    private static final String TAG = "DialogFileAction";

    private Activity activity;
    private FileModel fileModel;
    private DialogCallBacks dialogCallBacks;

    @Override
    public void setupDialog(final Dialog dialog, int style) {
        activity = getActivity();
        View rootView = activity.getLayoutInflater().inflate(R.layout.dialog_bottom_file_action, null);

        View VActionDecrypt = rootView.findViewById(R.id.action_decrypt);

        View VActionProperties = rootView.findViewById(R.id.action_properties);

        View VActionDelete = rootView.findViewById(R.id.action_delete);

        View VActionHide = rootView.findViewById(R.id.action_hide);
        final TextView TVActionHide = (TextView) rootView.findViewById(R.id.text_hide);

        if (getFileModel().isHidden()) {
            TVActionHide.setText(getString(R.string.dialog_file_action_show));
        } else {
            TVActionHide.setText(getString(R.string.dialog_file_action_hide));
        }

        VActionDecrypt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialogCallBacks != null) {
                    dialogCallBacks.onDecrypt(dialog);
                }
            }
        });

        VActionProperties.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialogCallBacks != null) {
                    dialogCallBacks.onProperties(dialog);
                }
            }
        });

        VActionDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialogCallBacks != null) {
                    dialogCallBacks.onDelete(dialog);
                }
            }
        });

        VActionHide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getFileModel().isHidden()) {
                    if (dialogCallBacks != null) {
                        dialogCallBacks.onUnHide(dialog);
                    }
                } else {
                    if (dialogCallBacks != null) {
                        dialogCallBacks.onHide(dialog);
                    }
                }
            }
        });

        dialog.setContentView(rootView);
        super.setupDialog(dialog, style);
    }

    public void setFileModel(FileModel fileModel) {
        this.fileModel = fileModel;
    }

    public FileModel getFileModel() {
        return fileModel;
    }

    public void setDialogCallbacks(DialogCallBacks dialogCallbacks) {
        this.dialogCallBacks = dialogCallbacks;
    }

    public interface DialogCallBacks {
        void onDecrypt(Dialog dialog);

        void onProperties(Dialog dialog);

        void onDelete(Dialog dialog);

        void onHide(Dialog dialog);

        void onUnHide(Dialog dialog);
    }
}
