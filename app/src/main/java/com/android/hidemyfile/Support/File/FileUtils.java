package com.android.hidemyfile.Support.File;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.android.hidemyfile.Encryption.Encryption;
import com.android.hidemyfile.R;

import java.net.URISyntaxException;

public class FileUtils {

    private static final String TAG = "FileUtils";

    private static final int TYPE_UNKNOWN = -1;
    private static final int TYPE_IMAGE = 0;
    private static final int TYPE_SOUND = 1;
    private static final int TYPE_APK = 2;
    private static final int TYPE_TEXT = 3;
    private static final int TYPE_DOC = 4;
    private static final int TYPE_GIF = 5;
    private static final int TYPE_PDF = 6;

    public static String getPath(Context context, String TAG, Uri uri) throws URISyntaxException {
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

    public static String removeEncryptPreffix(String path) {
        return path.replace(Encryption.FILE_PREFIX, "");
    }

    public static int getFileType(String pathToFile) {
        if (pathToFile.endsWith(".jpg")) return TYPE_IMAGE;
        else if (pathToFile.endsWith(".jpeg")) return TYPE_IMAGE;
        else if (pathToFile.endsWith(".png")) return TYPE_IMAGE;
        else if (pathToFile.endsWith(".bmp")) return TYPE_IMAGE;
        else if (pathToFile.endsWith(".gif")) return TYPE_IMAGE;
        else if (pathToFile.endsWith(".wav")) return TYPE_SOUND;
        else if (pathToFile.endsWith(".mp3")) return TYPE_SOUND;
        else if (pathToFile.endsWith(".apk")) return TYPE_APK;
        else if (pathToFile.endsWith(".txt")) return TYPE_TEXT;
        else if (pathToFile.endsWith(".doc")) return TYPE_DOC;
        else if (pathToFile.endsWith(".docx")) return TYPE_DOC;
        else if (pathToFile.endsWith(".pdf")) return TYPE_PDF;

        return TYPE_UNKNOWN;
    }

    public static int getEncryptedFileType(String pathToFile) {
        pathToFile = removeEncryptPreffix(pathToFile);
        Log.d(TAG, "getEncryptedFileType() -> Path to File: " + pathToFile);

        if (pathToFile.endsWith(".jpg")) return TYPE_IMAGE;
        else if (pathToFile.endsWith(".jpeg")) return TYPE_IMAGE;
        else if (pathToFile.endsWith(".png")) return TYPE_IMAGE;
        else if (pathToFile.endsWith(".bmp")) return TYPE_IMAGE;
        else if (pathToFile.endsWith(".gif")) return TYPE_GIF;
        else if (pathToFile.endsWith(".wav")) return TYPE_SOUND;
        else if (pathToFile.endsWith(".mp3")) return TYPE_SOUND;
        else if (pathToFile.endsWith(".apk")) return TYPE_APK;
        else if (pathToFile.endsWith(".txt")) return TYPE_TEXT;
        else if (pathToFile.endsWith(".doc")) return TYPE_DOC;
        else if (pathToFile.endsWith(".docx")) return TYPE_DOC;
        else if (pathToFile.endsWith(".pdf")) return TYPE_PDF;

        return TYPE_UNKNOWN;
    }

    public static int getIconFromType(int fileType) {
        switch (fileType) {
            case TYPE_IMAGE: {
                return R.drawable.ic_camera_alt_white_24dp;
            }
            case TYPE_GIF: {
                return R.drawable.ic_gif_grey_24dp;
            }
            case TYPE_SOUND: {
                return R.drawable.ic_music_note_grey_24dp;
            }
            case TYPE_APK: {
                return R.drawable.ic_android_grey_24dp;
            }
            case TYPE_TEXT: {
                return R.drawable.ic_receipt_white_24dp;
            }
            case TYPE_DOC: {
                return R.drawable.ic_receipt_white_24dp;
            }
            case TYPE_PDF: {
                return R.drawable.ic_picture_as_pdf_grey_24dp;
            }
            case TYPE_UNKNOWN: {
                return R.drawable.ic_help_outline_grey_24dp;
            }
            default: {
                return R.drawable.ic_help_outline_grey_24dp;
            }
        }
    }
}
