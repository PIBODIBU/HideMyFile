package com.android.hidemyfile.AsyncTask;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.android.hidemyfile.Encryption.Encryption;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.NoSuchPaddingException;

public class EncryptionTask extends AsyncTask<Void, Void, Void> {

    private static final String TAG = "EncryptionTask";
    private EncryptionCallbacks encryptionCallbacks;

    private Context context;
    private String secretKey;
    private String filePath;

    /**
     * {@link NoSuchAlgorithmException}
     */
    public static int NO_SUCH_ALGORITHM_EXCEPTION = 0;

    /**
     * {@link NoSuchPaddingException}
     */
    public static int NO_SUCH_PADDING_EXCEPTION = 1;

    /**
     * {@link InvalidKeyException}
     */
    public static int INVALID_KEY_EXCEPTION = 2;

    /**
     * {@link UnsupportedEncodingException}
     */
    public static int UNSUPPORTED_ENCODING_EXCEPTION = 3;

    /**
     * {@link IOException}
     */
    public static int IO_EXCEPTION = 4;

    public EncryptionTask(Context context, String secretKey, String filePath) {
        this.context = context;
        this.secretKey = secretKey;
        this.filePath = filePath;
    }

    @Override
    protected void onPreExecute() {
        if (encryptionCallbacks != null) {
            encryptionCallbacks.onPrepare();
        }
    }

    @Override
    protected Void doInBackground(Void... params) {

        try {
            String encryptedFilePath;
            encryptedFilePath = Encryption.encrypt(context, secretKey, new File(filePath));

            if (encryptionCallbacks != null) {
                encryptionCallbacks.onSuccess(encryptedFilePath);
            }
        } catch (NoSuchAlgorithmException ex) {
            Log.e(TAG, "doInBackground() -> ", ex);
            if (encryptionCallbacks != null) {
                encryptionCallbacks.onException(NO_SUCH_ALGORITHM_EXCEPTION);
            }
        } catch (NoSuchPaddingException ex) {
            Log.e(TAG, "doInBackground() -> ", ex);
            if (encryptionCallbacks != null) {
                encryptionCallbacks.onException(NO_SUCH_PADDING_EXCEPTION);
            }
        } catch (InvalidKeyException ex) {
            Log.e(TAG, "doInBackground() -> ", ex);
            if (encryptionCallbacks != null) {
                encryptionCallbacks.onException(INVALID_KEY_EXCEPTION);
            }
        } catch (UnsupportedEncodingException ex) {
            Log.e(TAG, "doInBackground() -> ", ex);
            if (encryptionCallbacks != null) {
                encryptionCallbacks.onException(UNSUPPORTED_ENCODING_EXCEPTION);
            }
        } catch (IOException ex) {
            Log.e(TAG, "doInBackground() -> ", ex);
            if (encryptionCallbacks != null) {
                encryptionCallbacks.onException(IO_EXCEPTION);
            }
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        if (encryptionCallbacks != null) {
            encryptionCallbacks.onComplete();
        }
    }

    public void setEncryptionCallbacks(EncryptionCallbacks encryptionCallbacks) {
        this.encryptionCallbacks = encryptionCallbacks;
    }

    /**
     * This interface provides Encryption callbacks
     */
    public interface EncryptionCallbacks {
        /**
         * Called before encryption process wa started
         */
        void onPrepare();

        /**
         * Encryption process was completed
         * <p>
         * This method will be called without reference to result.
         * You can use it to remove {@link android.app.ProgressDialog}
         * </p>
         */
        void onComplete();

        /**
         * Encryption process was completed
         * <p/>
         * <p>
         * This method will be called only if encryption process was successful.
         * </p>
         *
         * @param encryptedFilePath - Path of encrypted file
         */
        void onSuccess(String encryptedFilePath);

        /**
         * Exception was trowed during encryption process
         *
         * @param exceptionCode - Code of trowed exception {@link EncryptionTask#IO_EXCEPTION}
         */
        void onException(int exceptionCode);
    }
}
