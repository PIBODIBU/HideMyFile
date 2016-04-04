package com.android.hidemyfile.Dialog;

import android.app.Activity;
import android.app.Dialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.View;

import com.android.hidemyfile.R;

public class DialogFileAction extends BottomSheetDialogFragment {

    private static final String TAG = "DialogFileAction";

    private Activity activity;
    private DialogCallBacks dialogCallBacks;

    @Override
    public void setupDialog(final Dialog dialog, int style) {
        activity = getActivity();
        View rootView = activity.getLayoutInflater().inflate(R.layout.dialog_bottom_file_action, null);

        View VActionDecrypt = rootView.findViewById(R.id.action_decrypt);
        View VActionProperties = rootView.findViewById(R.id.action_properties);
        View VActionDelete = rootView.findViewById(R.id.action_delete);

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

        dialog.setContentView(rootView);
        super.setupDialog(dialog, style);
    }

    public void setDialogCallbacks(DialogCallBacks dialogCallbacks) {
        this.dialogCallBacks = dialogCallbacks;
    }

    public interface DialogCallBacks {
        void onDecrypt(Dialog dialog);

        void onProperties(Dialog dialog);

        void onDelete(Dialog dialog);
    }
}
