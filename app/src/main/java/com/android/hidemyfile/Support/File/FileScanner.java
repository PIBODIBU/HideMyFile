package com.android.hidemyfile.Support.File;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.android.hidemyfile.Encryption.Encryption;

import java.io.File;
import java.util.ArrayList;

public class FileScanner {

    private static final String TAG = "FileScanner";

    private OnScanPrepareListener onScanPrepareListener;
    private OnScanCompleteListener onScanCompleteListener;

    public void scan(final File root, final ArrayList<FileModel> fileModels) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPreExecute() {
                if (onScanPrepareListener != null) {
                    onScanPrepareListener.onScanPrepare();
                }
            }

            @Override
            protected Void doInBackground(Void... params) {
                Log.d(TAG, "scan() -> doInBackground()");

                scanRecursively(root, fileModels);

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                Log.d(TAG, "scan() -> onPostExecute() -> Total Files count: " + fileModels.size());

                if (onScanCompleteListener != null) {
                    onScanCompleteListener.onScanComplete();
                }
            }
        }.execute();
    }

    private void scanRecursively(File root, ArrayList<FileModel> fileModels) {
        Log.d(TAG, "--------------------");

        File[] list = root.listFiles();

        if (list != null) {
            for (File f : list) {
                if (!f.getName().startsWith(".")) {
                    if (f.isDirectory()) {
                        Log.d(TAG, "scanRecursively() -> Dir: " + f.getAbsoluteFile());
                        scanRecursively(f, fileModels);
                    } else {
                        Log.d(TAG, "scanRecursively() -> File: " + f.getAbsoluteFile());

                        if (f.getName().endsWith(Encryption.FILE_PREFIX)) {
                            fileModels.add(new FileModel(FileUtils.removeEncryptPreffix(f.getName()), f.getAbsolutePath()));
                        }
                    }
                }
            }
        } else {
            Log.e(TAG, "File is null");
        }

        Log.d(TAG, "--------------------");
    }

    public void setOnScanCompleteListener(OnScanCompleteListener onScanCompleteListener) {
        this.onScanCompleteListener = onScanCompleteListener;
    }

    public interface OnScanCompleteListener {
        void onScanComplete();
    }

    public void setOnScanPrepareListener(OnScanPrepareListener onScanPrepareListener) {
        this.onScanPrepareListener = onScanPrepareListener;
    }

    public interface OnScanPrepareListener {
        void onScanPrepare();
    }
}
