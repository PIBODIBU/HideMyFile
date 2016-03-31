package com.android.hidemyfile.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.hidemyfile.Dialog.DialogFileDecrypt;
import com.android.hidemyfile.Dialog.DialogFileEncrypt;
import com.android.hidemyfile.Encryption.Encryption;
import com.android.hidemyfile.R;
import com.android.hidemyfile.Support.File.FileAdapter;
import com.android.hidemyfile.Support.File.FileModel;
import com.android.hidemyfile.Support.File.FileScanner;
import com.android.hidemyfile.Support.File.FileUtils;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainAcivity";
    private static final int FILE_SELECT_CODE = 0;

    /**
     * Main Layout Views
     */
    private Toolbar toolbar;
    private CoordinatorLayout rootView;

    private Button FABFileChoose;
    /*****/

    /**
     * Warning message "No encrypted files" Views
     */
    private View messageRootView;
    private ImageView messageImage;
    private TextView messageText;
    /*****/

    /**
     * RecyclerView
     */
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private FileAdapter fileAdapter;
    private ArrayList<FileModel> dataSet;

    /*****/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUpView();
        setUpRecyclerView();
        setSupportActionBar(toolbar);
    }

    private void setUpView() {
        /** Main Layout Views **/
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        rootView = (CoordinatorLayout) findViewById(R.id.root_view);

        /** Warning message "No encrypted files" Views **/
        messageRootView = findViewById(R.id.container_message_empty);
    }

    private void setUpRecyclerView() {
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        linearLayoutManager = new LinearLayoutManager(this);
        dataSet = new ArrayList<>();
        fileAdapter = new FileAdapter(this, dataSet);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(fileAdapter);

        fileAdapter.setRecyclerViewCallbacks(new FileAdapter.RecyclerViewCallbacks() {
            @Override
            public void itemClicked(int position) {
                showDialogFileDecrypt(dataSet.get(position).getFilePath());
            }
        });

        refreshAdapterItems();
    }

    private void refreshAdapterItems() {
        FileScanner fileScanner = new FileScanner();
        String rootPath = Environment.getExternalStorageDirectory().toString();

        Log.d(TAG, "refreshAdapterItems() -> Directory: " + rootPath);

        fileScanner.setOnScanCompleteListener(new FileScanner.OnScanCompleteListener() {
            @Override
            public void onScanComplete() {
                fileAdapter.notifyDataSetChanged();

                refreshLayout();
            }
        });

        dataSet.clear();
        fileScanner.scan(new File(rootPath + "/"), dataSet); // TODO remove "/"
    }

    private void refreshLayout() {
        if (dataSet.size() == 0) {
            messageRootView.setVisibility(View.VISIBLE);
        } else {
            messageRootView.setVisibility(View.GONE);
        }
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_choose:
                showFileChooser();
                break;
            default:
                break;
        }
    }

    private void showInfo(String info) {
        Snackbar.make(rootView, info, Snackbar.LENGTH_LONG).show();
    }

    private void showDialogFileEncrypt(final String filePath) {
        DialogFileEncrypt dialogFileEncrypt = new DialogFileEncrypt();
        dialogFileEncrypt.init(filePath);
        dialogFileEncrypt.setDialogCallbacks(new DialogFileEncrypt.DialogCallbacks() {
            @Override
            public void onPositive() {
                try {
                    Encryption.encrypt("MyDifficultPassw", new File(filePath));
                    showInfo("File encrypted");
                } catch (Exception ex) {
                    Log.e(TAG, "onClick() -> ", ex);
                    showInfo("Error while encrypting file");
                } finally {
                    refreshAdapterItems();
                }
            }
        });
        dialogFileEncrypt.show(getSupportFragmentManager(), "DialogFileEncrypt");
    }

    private void showDialogFileDecrypt(final String filePath) {
        DialogFileDecrypt dialogFileDecrypt = new DialogFileDecrypt();
        dialogFileDecrypt.init(filePath);
        dialogFileDecrypt.setDialogCallbacks(new DialogFileDecrypt.DialogCallbacks() {
            @Override
            public void onPositive() {
                try {
                    Encryption.decrypt("MyDifficultPassw", new File(filePath));
                    showInfo("File decrypted");
                } catch (Exception ex) {
                    Log.e(TAG, "onClick() -> ", ex);
                    showInfo("Error while decrypting file");
                } finally {
                    refreshAdapterItems();
                }
            }
        });
        dialogFileDecrypt.show(getSupportFragmentManager(), "DialogFileDecrypt");
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
                        final String path = FileUtils.getPath(this, TAG, uri);
                        Log.d(TAG, "onActivityResult() -> File Path: " + path);

                        new Handler().post(new Runnable() {
                            @Override
                            public void run() {
                                showDialogFileEncrypt(path);
                            }
                        });
                    } catch (Exception ex) {
                        Log.e(TAG, "onActivityResult() -> ", ex);
                        break;
                    }
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
            showInfo("Please install a File Manager");
        }
    }
}
