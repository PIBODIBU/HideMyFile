package com.android.hidemyfile.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import com.android.hidemyfile.Encryption.Encryption;
import com.android.hidemyfile.R;
import com.android.hidemyfile.Support.SharedPreferencesUtils.SharedPreferencesUtils;

public class DialogPasswordChange extends DialogFragment {

    private static final String TAG = "DialogPasswordChange";

    private SharedPreferencesUtils sharedPreferencesUtils;
    private InputMethodManager inputMethodManager;
    private AlertDialog alertDialog;
    private PasswordChangeCallbacks passwordChangeCallbacks;

    private View rootView;

    private Button BNegative;
    private Button BPositive;

    private TextInputLayout TILPasswordOld;
    private TextInputLayout TILPasswordNew;
    private TextInputLayout TILPasswordRepeat;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        sharedPreferencesUtils = new SharedPreferencesUtils(getActivity());

        setUpLayout();

        BNegative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeKeyboard();
                dismiss();
            }
        });
        BPositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeKeyboard();

                if (!isInputValid())
                    return;

                dismiss();

                if (passwordChangeCallbacks != null) {
                    passwordChangeCallbacks.onSuccess(Encryption.encrypt(TILPasswordNew.getEditText().getText().toString()));
                } else {
                    Log.e(TAG, "onClick() -> dialogCallbacks is null");
                }
            }
        });

        alertDialog = new AlertDialog.Builder(getActivity())
                .setTitle(getActivity().getString(R.string.dialog_password_change_title))
                .setView(rootView)
                .create();

        setCancelable(false);

        return alertDialog;
    }

    private void setUpLayout() {
        rootView = getActivity().getLayoutInflater().inflate(R.layout.dialog_password_change, null);

        BNegative = (Button) rootView.findViewById(R.id.action_negative);
        BPositive = (Button) rootView.findViewById(R.id.action_positive);

        TILPasswordOld = (TextInputLayout) rootView.findViewById(R.id.text_input_password_old);
        TILPasswordNew = (TextInputLayout) rootView.findViewById(R.id.text_input_password_new);
        TILPasswordRepeat = (TextInputLayout) rootView.findViewById(R.id.text_input_password_repeat);
    }

    private boolean isInputValid() {
        if (TILPasswordOld.getEditText() != null &&
                TILPasswordNew.getEditText() != null &&
                TILPasswordRepeat.getEditText() != null) {

            String pwdOld = TILPasswordOld.getEditText().getText().toString();
            String pwdNew = TILPasswordNew.getEditText().getText().toString();
            String pwdRepeat = TILPasswordRepeat.getEditText().getText().toString();

            // Check for empty input
            if (TextUtils.isEmpty(pwdOld)) {
                TILPasswordOld.setError(getString(R.string.dialog_password_change_error_empty));
                return false;
            } else {
                TILPasswordOld.setError("");
            }
            if (TextUtils.isEmpty(pwdNew)) {
                TILPasswordNew.setError(getString(R.string.dialog_password_change_error_empty));
                return false;
            } else {
                TILPasswordNew.setError("");
            }
            if (TextUtils.isEmpty(pwdRepeat)) {
                TILPasswordRepeat.setError(getString(R.string.dialog_password_change_error_empty));
                return false;
            } else {
                TILPasswordRepeat.setError("");
            }

            // Check for input length
            if (pwdOld.length() < getResources().getInteger(R.integer.password_4_length)) {
                TILPasswordOld.setError(getString(R.string.dialog_password_change_error_length_short));
                return false;
            } else {
                TILPasswordOld.setError("");
            }
            if (pwdNew.length() < getResources().getInteger(R.integer.password_4_length)) {
                TILPasswordNew.setError(getString(R.string.dialog_password_change_error_length_short));
                return false;
            } else {
                TILPasswordNew.setError("");
            }
            if (pwdRepeat.length() < getResources().getInteger(R.integer.password_4_length)) {
                TILPasswordRepeat.setError(getString(R.string.dialog_password_change_error_length_short));
                return false;
            } else {
                TILPasswordRepeat.setError("");
            }

            if (pwdOld.length() > getResources().getInteger(R.integer.password_4_length)) {
                TILPasswordOld.setError(getString(R.string.dialog_password_change_error_length_long));
                return false;
            } else {
                TILPasswordOld.setError("");
            }
            if (pwdNew.length() > getResources().getInteger(R.integer.password_4_length)) {
                TILPasswordNew.setError(getString(R.string.dialog_password_change_error_length_long));
                return false;
            } else {
                TILPasswordNew.setError("");
            }
            if (pwdRepeat.length() > getResources().getInteger(R.integer.password_4_length)) {
                TILPasswordRepeat.setError(getString(R.string.dialog_password_change_error_length_long));
                return false;
            } else {
                TILPasswordRepeat.setError("");
            }

            // Check for old and repeat passwords matching
            if (!pwdNew.equals(pwdRepeat)) {
                TILPasswordRepeat.setError(getString(R.string.dialog_password_change_error_match));
                return false;
            } else {
                TILPasswordRepeat.setError("");
            }

            try {
                if (!Encryption.encrypt(pwdOld).equals(sharedPreferencesUtils.getKey4Password())) {
                    TILPasswordOld.setError(getString(R.string.dialog_password_change_error_bad_pwd));
                    return false;
                } else {
                    TILPasswordOld.setError("");
                }
            } catch (Exception ex) {
                Log.e(TAG, "isInputValid() -> ", ex);
                return false;
            }

            return true;
        } else {
            Log.e(TAG, "isInputValid() -> One or more of TextInputLayout are null");
            return false;
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

    public void setPasswordChangeCallbacks(PasswordChangeCallbacks passwordChangeCallbacks) {
        this.passwordChangeCallbacks = passwordChangeCallbacks;
    }

    public interface PasswordChangeCallbacks {
        void onSuccess(String password);
    }
}
