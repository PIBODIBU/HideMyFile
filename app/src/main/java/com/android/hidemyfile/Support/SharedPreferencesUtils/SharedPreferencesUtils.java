package com.android.hidemyfile.Support.SharedPreferencesUtils;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.android.hidemyfile.Encryption.Encryption;

public class SharedPreferencesUtils {

    private static final String TAG = "SharedPreferencesUtils";
    private static final String SHARED_PREFERENCES_NAME = "com.android.hidemyfile.app_preferences";

    private Context context;
    private SharedPreferences sharedPreferences;

    private static final String KEY_4_PASSWORD = "DO_NOT_TOUCH_THIS";
    private static final String KEY_SCAN_HIDDEN = "KEY_SCAN_HIDDEN";
    private static final String KEY_HIDE_AFTER_SCAN = "KEY_HIDE_AFTER_SCAN";

    public SharedPreferencesUtils(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    public void setKey4Password(@NonNull String password) {
        sharedPreferences.edit().putString(KEY_4_PASSWORD, password).apply();
    }

    @NonNull
    public String getKey4Password() {
        return sharedPreferences.getString(KEY_4_PASSWORD, "");
    }

    public void setScanHidden(@NonNull boolean scanHidden) {
        sharedPreferences.edit().putBoolean(KEY_SCAN_HIDDEN, scanHidden).apply();
    }

    @NonNull
    public boolean getScanHidden() {
        return sharedPreferences.getBoolean(KEY_SCAN_HIDDEN, false);
    }

    public void setHideAfterScan(@NonNull boolean hideAfterScan) {
        sharedPreferences.edit().putBoolean(KEY_HIDE_AFTER_SCAN, hideAfterScan).apply();
    }

    @NonNull
    public boolean getHideAfterScan() {
        return sharedPreferences.getBoolean(KEY_HIDE_AFTER_SCAN, false);
    }
}
