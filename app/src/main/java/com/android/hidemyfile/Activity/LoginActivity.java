package com.android.hidemyfile.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.hidemyfile.Encryption.Encryption;
import com.android.hidemyfile.R;
import com.android.hidemyfile.Support.File.SharedPreferencesUtils.SharedPreferencesUtils;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    private SharedPreferencesUtils sharedPrefUtils;

    private Toolbar toolbar;

    private TextInputLayout TILPassword;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setUpView();
        setUpToolbar();

        sharedPrefUtils = new SharedPreferencesUtils(this);

        Log.d(TAG, "onCreate() -> Checking key for existing...");
        checkKey();
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_login:
                if (TILPassword.getEditText() != null) {
                    // Getting String from EditText
                    String userKey = TILPassword.getEditText().getText().toString();

                    // Check user's input
                    if (!isUserInputValid(userKey)) {
                        return;
                    }

                    // Encrypting user's key
                    userKey = Encryption.encrypt(userKey);

                    // Check if encryption was successful
                    if (userKey == null) {
                        Log.e(TAG, "onClick() -> Key is null");
                        Toast.makeText(LoginActivity.this, "onClick() -> Key is null", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Comparing hashes
                    if (userKey.equals(sharedPrefUtils.getKey4Password())) {
                        startActivity(new Intent(this, MainActivity.class)
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        finish();
                    } else {
                        showDialogBadPassword();
                    }
                }
                break;
            default:
                break;
        }
    }

    private boolean isUserInputValid(String userInput) {
        if (TextUtils.isEmpty(userInput)) {
            TILPassword.setError(getString(R.string.login_error_message_empty));
            return false;
        } else if (userInput.length() > getResources().getInteger(R.integer.password_4_length_max)) {
            TILPassword.setError(getString(R.string.login_error_message_too_long));
            return false;
        } else if (userInput.length() < getResources().getInteger(R.integer.password_4_length_min)) {
            TILPassword.setError(getString(R.string.login_error_message_too_short));
            return false;
        }

        return true;
    }

    private void showDialogBadPassword() {
        final AlertDialog alertDialog;

        alertDialog = new AlertDialog.Builder(this)
                .setTitle(getString(R.string.login_dialog_error_title))
                .setMessage(getString(R.string.login_dialog_error_message))
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
                        .setTextColor(ContextCompat.getColor(LoginActivity.this, R.color.colorPrimary));
            }
        });
        alertDialog.show();
    }

    private void setUpToolbar() {
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setUpView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        TILPassword = (TextInputLayout) findViewById(R.id.text_input_password);
    }

    @Override
    public void onBackPressed() {
        if (getCurrentFocus() != null) {
            getCurrentFocus().clearFocus();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void checkKey() {
        if (TextUtils.isEmpty(sharedPrefUtils.getKey4Password())) {
            Log.e(TAG, "checkKey() -> Key File doesn't exist, creating new one...");

            try {
                String key = Encryption.encrypt(Encryption.FILE_PASSWORD_DEFAULT);
                if (key != null) {
                    sharedPrefUtils.setKey4Password(key);
                } else {
                    Log.e(TAG, "checkKey() -> Key is null");
                }
            } catch (Exception ex) {
                Log.e(TAG, "checkKey() -> ", ex);
            }
        } else {
            Log.d(TAG, "checkKey() -> Key File already exists");
        }
    }
}
