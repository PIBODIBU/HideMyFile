package com.android.hidemyfile.Support.File;

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
            for (File file : list) {
                if (!file.getName().startsWith(".")) {
                    if (file.isDirectory()) {
                        Log.d(TAG, "scanRecursively() -> Dir: " + file.getAbsoluteFile());
                        scanRecursively(file, fileModels);
                    } else {
                        Log.d(TAG, "scanRecursively() -> File: " + file.getAbsoluteFile());

                        if (file.getName().endsWith(Encryption.FILE_PREFIX)) {
                            fileModels.add(new FileModel(FileUtils.removeEncryptionPreffix(file.getName()), file.getAbsolutePath()));
                            Log.d(TAG, "scanRecursively() -> Adding new item: " +
                                    "\nName: " + FileUtils.removeEncryptionPreffix(file.getName()) +
                                    "\nPath: " + file.getPath());
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
