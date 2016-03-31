package com.android.hidemyfile;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainAcivity";
    private static final int FILE_SELECT_CODE = 0;

    /**
     * View
     */
    private Toolbar toolbar;
    private CoordinatorLayout rootView;

    private Button BFileEncrypt;
    private Button BFileDecrypt;
    private Button FABFileChoose;

    private TextView TVFilePath;
    /*****/

    private String filePath = "";
    private String filePathEncrypted = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**
         * View
         */
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        rootView = (CoordinatorLayout) findViewById(R.id.root_view);

        TVFilePath = (TextView) findViewById(R.id.text_path);
        /*****/

        setSupportActionBar(toolbar);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_encrypt:
                /*try {
                    Encryption.encrypt("MyDifficultPassw", new File(getFilePath()));
                    showInfo("File encrypted");
                } catch (Exception ex) {
                    Log.e(TAG, "onClick() -> ", ex);
                    showInfo("Error while encrypting file");
                }*/
                showDialogFile();
                break;
            case R.id.button_decrypt:
                try {
                    Encryption.decrypt("MyDifficultPassw", new File(getFilePath()));
                    showInfo("File decrypted");
                } catch (Exception ex) {
                    Log.e(TAG, "onClick() -> ", ex);
                    showInfo("Error while decrypting file");
                }
                break;
            case R.id.button_choose:
                showFileChooser();
                break;
            default:
                break;
        }
    }

    private void refreshLayout() {
        TVFilePath.setText(getFilePath());
    }

    private void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    private String getFilePath() {
        return this.filePath;
    }

    private void setFilePathEncrypted(String filePath) {
        this.filePathEncrypted = filePath;
    }

    private String getFilePathEncrypted() {
        return this.filePathEncrypted;
    }

    private void showInfo(String info) {
        Snackbar.make(rootView, info, Snackbar.LENGTH_LONG).show();
    }

    private void showDialogFile() {
        DialogFile dialogFile = new DialogFile();
        dialogFile.init(getFilePath());
        dialogFile.setDialogCallbacks(new DialogFile.DialogCallbacks() {
            @Override
            public void onPositive() {
                try {
                    Encryption.encrypt("MyDifficultPassw", new File(getFilePath()));
                    showInfo("File encrypted");
                } catch (Exception ex) {
                    Log.e(TAG, "onClick() -> ", ex);
                    showInfo("Error while encrypting file");
                }
            }
        });
        dialogFile.show(getSupportFragmentManager(), "DialogFile");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case FILE_SELECT_CODE:
                if (resultCode == RESULT_OK) {
                    // Get the Uri of the selected file
                    Uri uri = data.getData();
                    Log.d(TAG, "onActivityResult() -> File Uri: " + uri.toString());
                    try {
                        // Get the path
                        String path = getPath(this, uri);
                        setFilePath(path);
                        Log.d(TAG, "onActivityResult() -> File Path: " + path);

                        new Handler().post(new Runnable() {
                            @Override
                            public void run() {
                                showDialogFile();
                            }
                        });
                    } catch (Exception ex) {
                        Log.e(TAG, "onActivityResult() -> ", ex);
                        break;
                    } finally {
                        refreshLayout();
                    }

                    // Get the file instance
                    // File file = new File(path);
                    // Initiate the upload
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            startActivityForResult(
                    Intent.createChooser(intent, "Select a File to Upload"),
                    FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            showInfo("Please install a File Manager");
        }
    }

    public static String getPath(Context context, Uri uri) throws URISyntaxException {
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {"_data"};
            Cursor cursor;

            try {
                cursor = context.getContentResolver().query(uri, projection, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow("_data");
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
                cursor.close();
            } catch (Exception ex) {
                Log.e(TAG, "getPath() -> ", ex);
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }
}
