package com.android.hidemyfile.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.hidemyfile.Encryption.Encryption;
import com.android.hidemyfile.R;
import com.android.hidemyfile.Support.SharedPreferencesUtils.SharedPreferencesUtils;
import com.android.hidemyfile.Support.View.Utils;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private String userInput = "";

    private SharedPreferencesUtils sharedPrefUtils;

    private Toolbar toolbar;

    private HashMap<Integer, ImageView> indicators = new HashMap<>();
    private ImageView IVPwdIndicator1;
    private ImageView IVPwdIndicator2;
    private ImageView IVPwdIndicator3;
    private ImageView IVPwdIndicator4;

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
                // Getting String from EditText
                String userKey = getUserInput();

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
                break;
            case R.id.button_backspace:
                deleteSymbol();
                break;
            case R.id.key_1:
                addSymbol('1');
                break;
            case R.id.key_2:
                addSymbol('2');
                break;
            case R.id.key_3:
                addSymbol('3');
                break;
            case R.id.key_4:
                addSymbol('4');
                break;
            case R.id.key_5:
                addSymbol('5');
                break;
            case R.id.key_6:
                addSymbol('6');
                break;
            case R.id.key_7:
                addSymbol('7');
                break;
            case R.id.key_8:
                addSymbol('8');
                break;
            case R.id.key_9:
                addSymbol('9');
                break;
            case R.id.key_0:
                addSymbol('0');
                break;
            default:
                break;
        }
    }

    private String addSymbol(Character character) {
        String oldInput = getUserInput();
        String newInput = oldInput;

        if (oldInput.length() < 4) {
            newInput += character;

            Log.d(TAG, "addSymbol() -> newInput: " + newInput);

            setUserInput(newInput);

            setIndicatorActive(indicators.get(newInput.length()));

            Log.d(TAG, "deleteSymbol() -> Setting Active indicator #" + newInput.length());

        }

        return newInput;
    }

    private String deleteSymbol() {
        String oldInput = getUserInput();
        String newInput = oldInput;

        if (oldInput.length() > 0) {
            newInput = newInput.substring(0, oldInput.length() - 1);

            Log.d(TAG, "deleteSymbol() -> newInput: " + newInput);

            setUserInput(newInput);

            setIndicatorInactive(indicators.get(newInput.length() + 1));

            Log.d(TAG, "deleteSymbol() -> Setting Inactive indicator #" + (newInput.length() + 1));
        }

        return newInput;
    }

    private void setIndicatorActive(View indicator) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams((int) Utils.convertDpToPixel(24, this), (int) Utils.convertDpToPixel(24, this));
        layoutParams.setMargins(
                (int) Utils.convertDpToPixel(8, this),
                (int) Utils.convertDpToPixel(8, this),
                (int) Utils.convertDpToPixel(8, this),
                (int) Utils.convertDpToPixel(8, this)
        );
        indicator.setLayoutParams(layoutParams);

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
            indicator.setBackground(ContextCompat.getDrawable(this, R.drawable.circle_accent));
        } else {
            indicator.setBackgroundDrawable(DrawableCompat.wrap(ContextCompat.getDrawable(this, R.drawable.circle_accent)));
        }
    }

    private void setIndicatorInactive(View indicator) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams((int) Utils.convertDpToPixel(18, this), (int) Utils.convertDpToPixel(18, this));
        layoutParams.setMargins(
                (int) Utils.convertDpToPixel(8, this),
                (int) Utils.convertDpToPixel(8, this),
                (int) Utils.convertDpToPixel(8, this),
                (int) Utils.convertDpToPixel(8, this)
        );
        indicator.setLayoutParams(layoutParams);

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
            indicator.setBackground(ContextCompat.getDrawable(this, R.drawable.circle_grey));
        } else {
            indicator.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.circle_grey));
        }
    }

    private String getUserInput() {
        return userInput;
    }

    private void setUserInput(String userInput) {
        this.userInput = userInput;
    }

    private boolean isUserInputValid(String userInput) {
        if (TextUtils.isEmpty(userInput)) {
            return false;
        } else if (userInput.length() != getResources().getInteger(R.integer.password_4_length)) {
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

        IVPwdIndicator1 = (ImageView) findViewById(R.id.pwd_indicator_1);
        IVPwdIndicator2 = (ImageView) findViewById(R.id.pwd_indicator_2);
        IVPwdIndicator3 = (ImageView) findViewById(R.id.pwd_indicator_3);
        IVPwdIndicator4 = (ImageView) findViewById(R.id.pwd_indicator_4);
        setIndicatorInactive(IVPwdIndicator1);
        setIndicatorInactive(IVPwdIndicator2);
        setIndicatorInactive(IVPwdIndicator3);
        setIndicatorInactive(IVPwdIndicator4);
        indicators.put(1, IVPwdIndicator1);
        indicators.put(2, IVPwdIndicator2);
        indicators.put(3, IVPwdIndicator3);
        indicators.put(4, IVPwdIndicator4);
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
                String key = Encryption.encrypt(Encryption.PASSWORD_4DIGIT_DEFAULT);
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
