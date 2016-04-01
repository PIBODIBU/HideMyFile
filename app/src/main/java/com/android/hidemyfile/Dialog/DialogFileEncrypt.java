package com.android.hidemyfile.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
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

public class DialogFileEncrypt extends DialogFragment {

    private static final String TAG = "DialogFileEncrypt";

    private InputMethodManager inputMethodManager;
    private AlertDialog alertDialog;
    private DialogCallbacks dialogCallbacks;

    private TextInputLayout TILPassword;
    private Button BNegative;
    private Button BPostive;

    private String filePath;

    public void setDialogCallbacks(DialogCallbacks dialogCallbacks) {
        this.dialogCallbacks = dialogCallbacks;
    }

    public void init(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View rootView = getActivity().getLayoutInflater().inflate(R.layout.dialog_encryption, null);

        inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

        TILPassword = (TextInputLayout) rootView.findViewById(R.id.text_input_password);
        BNegative = (Button) rootView.findViewById(R.id.action_negative);
        BPostive = (Button) rootView.findViewById(R.id.action_positive);

        BNegative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeKeyboard();
                dismiss();
            }
        });
        BPostive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeKeyboard();

                if (!checkInput())
                    return;

                dismiss();

                if (dialogCallbacks != null) {
                    if (TILPassword.getEditText() != null) {
                        dialogCallbacks.onPositive(TILPassword.getEditText().getText().toString());
                    }
                }
            }
        });

        openKeyboard();

        alertDialog = new AlertDialog.Builder(getActivity())
                .setTitle(getActivity().getString(R.string.dialog_encryption_title))
                .setView(rootView)
                .create();

        setCancelable(false);

        return alertDialog;
    }

    private boolean checkInput() {
        if (TILPassword.getEditText() != null) {
            String input = TILPassword.getEditText().getText().toString();

            if (TextUtils.isEmpty(input)) {
                TILPassword.setError(getActivity().getString(R.string.dialog_decryption_error_empty));
                return false;
            }

            if (input.length() < getActivity().getResources().getInteger(R.integer.password_length_min)) {
                TILPassword.setError(getActivity().getString(R.string.dialog_decryption_error_short));
                return false;
            }

            if (input.length() > getActivity().getResources().getInteger(R.integer.password_length_max)) {
                TILPassword.setError(getActivity().getString(R.string.dialog_encryption_error_long));
                return false;
            }

            return true;
        } else {
            Log.e(TAG, "checkInput() -> TILPassword.getEditText() is null");
            return false;
        }
    }

    private void openKeyboard() {
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_FORCED);
        } else {
            Log.e(TAG, "openKeyboard() -> View is null");
        }
    }

    private void closeKeyboard() {
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        } else {
            Log.e(TAG, "closeKeyboard() -> View is null");
        }
    }

    public interface DialogCallbacks {
        void onPositive(String password);
    }
}
