package com.android.hidemyfile.Support.File;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.android.hidemyfile.Encryption.Encryption;
import com.android.hidemyfile.R;

import java.io.File;
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
    private static final int TYPE_VIDEO = 7;

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

    public static boolean isFileHidden(File file) {
        return file.getName().startsWith(".");
    }

    public static String removeHidePreffix(String fileName) {
        return fileName.substring(1, fileName.length());
    }

    public static String removeEncryptionPreffix(String path) {
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
        pathToFile = removeEncryptionPreffix(pathToFile);
        Log.d(TAG, "getEncryptedFileType() -> Path to File: " + pathToFile);

        // Image formats
        if (pathToFile.endsWith(".jpg")) return TYPE_IMAGE;
        else if (pathToFile.endsWith(".jpeg")) return TYPE_IMAGE;
        else if (pathToFile.endsWith(".png")) return TYPE_IMAGE;
        else if (pathToFile.endsWith(".bmp")) return TYPE_IMAGE;

            // GIF
        else if (pathToFile.endsWith(".gif")) return TYPE_GIF;

            // Music formats
        else if (pathToFile.endsWith(".wav")) return TYPE_SOUND;
        else if (pathToFile.endsWith(".aiff")) return TYPE_SOUND;
        else if (pathToFile.endsWith(".aac")) return TYPE_SOUND;
        else if (pathToFile.endsWith(".wma")) return TYPE_SOUND;
        else if (pathToFile.endsWith(".vorbis")) return TYPE_SOUND;
        else if (pathToFile.endsWith(".mp3")) return TYPE_SOUND;
        else if (pathToFile.endsWith(".ra")) return TYPE_SOUND;
        else if (pathToFile.endsWith(".ram")) return TYPE_SOUND;
        else if (pathToFile.endsWith(".rm")) return TYPE_SOUND;
        else if (pathToFile.endsWith(".mid")) return TYPE_SOUND;
        else if (pathToFile.endsWith(".midi")) return TYPE_SOUND;

            // Video formats
        else if (pathToFile.endsWith(".webm")) return TYPE_VIDEO;
        else if (pathToFile.endsWith(".mkv")) return TYPE_VIDEO;
        else if (pathToFile.endsWith(".flv")) return TYPE_VIDEO;
        else if (pathToFile.endsWith(".vob")) return TYPE_VIDEO;
        else if (pathToFile.endsWith(".ogg")) return TYPE_VIDEO;
        else if (pathToFile.endsWith(".ogv")) return TYPE_VIDEO;
        else if (pathToFile.endsWith(".drc")) return TYPE_VIDEO;
        else if (pathToFile.endsWith(".gifv")) return TYPE_VIDEO;
        else if (pathToFile.endsWith(".mng")) return TYPE_VIDEO;
        else if (pathToFile.endsWith(".avi")) return TYPE_VIDEO;
        else if (pathToFile.endsWith(".mov")) return TYPE_VIDEO;
        else if (pathToFile.endsWith(".wmv")) return TYPE_VIDEO;
        else if (pathToFile.endsWith(".yuv")) return TYPE_VIDEO;
        else if (pathToFile.endsWith(".mp4")) return TYPE_VIDEO;
        else if (pathToFile.endsWith(".m4p")) return TYPE_VIDEO;
        else if (pathToFile.endsWith(".mpg")) return TYPE_VIDEO;
        else if (pathToFile.endsWith(".mpeg")) return TYPE_VIDEO;
        else if (pathToFile.endsWith(".mp2")) return TYPE_VIDEO;
        else if (pathToFile.endsWith(".mpv")) return TYPE_VIDEO;
        else if (pathToFile.endsWith(".mpe")) return TYPE_VIDEO;
        else if (pathToFile.endsWith(".m4v")) return TYPE_VIDEO;
        else if (pathToFile.endsWith(".svi")) return TYPE_VIDEO;
        else if (pathToFile.endsWith(".3gp")) return TYPE_VIDEO;
        else if (pathToFile.endsWith(".3g2")) return TYPE_VIDEO;
        else if (pathToFile.endsWith(".mxf")) return TYPE_VIDEO;
        else if (pathToFile.endsWith(".roq")) return TYPE_VIDEO;
        else if (pathToFile.endsWith(".nsv")) return TYPE_VIDEO;
        else if (pathToFile.endsWith(".f4v")) return TYPE_VIDEO;
        else if (pathToFile.endsWith(".f4p")) return TYPE_VIDEO;
        else if (pathToFile.endsWith(".f4a")) return TYPE_VIDEO;
        else if (pathToFile.endsWith(".f4b")) return TYPE_VIDEO;

            // Executable formats
        else if (pathToFile.endsWith(".apk")) return TYPE_APK;

            // Text formats
        else if (pathToFile.endsWith(".txt")) return TYPE_TEXT;

            // MS Office formats
        else if (pathToFile.endsWith(".doc")) return TYPE_DOC;
        else if (pathToFile.endsWith(".doc")) return TYPE_DOC;
        else if (pathToFile.endsWith(".dot")) return TYPE_DOC;
        else if (pathToFile.endsWith(".docm")) return TYPE_DOC;
        else if (pathToFile.endsWith(".dotx")) return TYPE_DOC;
        else if (pathToFile.endsWith(".docb")) return TYPE_DOC;
        else if (pathToFile.endsWith(".xls")) return TYPE_DOC;
        else if (pathToFile.endsWith(".xlt")) return TYPE_DOC;
        else if (pathToFile.endsWith(".xlm")) return TYPE_DOC;
        else if (pathToFile.endsWith(".xlsx")) return TYPE_DOC;
        else if (pathToFile.endsWith(".xltx")) return TYPE_DOC;
        else if (pathToFile.endsWith(".xltm")) return TYPE_DOC;
        else if (pathToFile.endsWith(".xlsb")) return TYPE_DOC;
        else if (pathToFile.endsWith(".xla")) return TYPE_DOC;
        else if (pathToFile.endsWith(".xll")) return TYPE_DOC;
        else if (pathToFile.endsWith(".xlw")) return TYPE_DOC;
        else if (pathToFile.endsWith(".ppt")) return TYPE_DOC;
        else if (pathToFile.endsWith(".pot")) return TYPE_DOC;
        else if (pathToFile.endsWith(".pps")) return TYPE_DOC;
        else if (pathToFile.endsWith(".pptx")) return TYPE_DOC;
        else if (pathToFile.endsWith(".pptm")) return TYPE_DOC;
        else if (pathToFile.endsWith(".potx")) return TYPE_DOC;
        else if (pathToFile.endsWith(".potm")) return TYPE_DOC;
        else if (pathToFile.endsWith(".ppam")) return TYPE_DOC;
        else if (pathToFile.endsWith(".ppsx")) return TYPE_DOC;
        else if (pathToFile.endsWith(".ppsm")) return TYPE_DOC;
        else if (pathToFile.endsWith(".sldx")) return TYPE_DOC;
        else if (pathToFile.endsWith(".sldm")) return TYPE_DOC;
        else if (pathToFile.endsWith(".pub")) return TYPE_DOC;

            // PDF
        else if (pathToFile.endsWith(".pdf")) return TYPE_PDF;

        return TYPE_UNKNOWN;
    }

    public static int getIconFromType(int fileType) {
        switch (fileType) {
            case TYPE_IMAGE: {
                return R.drawable.ic_camera_white_24dp;
            }
            case TYPE_GIF: {
                return R.drawable.ic_gif_grey_24dp;
            }
            case TYPE_SOUND: {
                return R.drawable.ic_music_note_grey_24dp;
            }
            case TYPE_VIDEO: {
                return R.drawable.ic_ondemand_video_white_24dp;
            }
            case TYPE_APK: {
                return R.drawable.ic_android_white_24dp;
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
