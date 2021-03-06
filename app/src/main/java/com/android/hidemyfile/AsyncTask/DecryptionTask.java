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

public class DecryptionTask extends AsyncTask<Void, Void, Void> {

    private static final String TAG = "DecryptionTask";
    private DecryptionCallbacks decryptionCallbacks;

    private Context context;
    private String secretKey;
    private String filePath;

    public static int NO_SUCH_ALGORITHM_EXCEPTION = 0;
    public static int NO_SUCH_PADDING_EXCEPTION = 1;
    public static int INVALID_KEY_EXCEPTION = 2;
    public static int UNSUPPORTED_ENCODING_EXCEPTION = 3;
    public static int IO_EXCEPTION = 4;

    public DecryptionTask(Context context, String secretKey, String filePath) {
        this.context = context;
        this.secretKey = secretKey;
        this.filePath = filePath;
    }

    @Override
    protected void onPreExecute() {
        if (decryptionCallbacks != null) {
            decryptionCallbacks.onPrepare();
        }
    }

    @Override
    protected Void doInBackground(Void... params) {

        try {
            String decryptedFilePath;
            decryptedFilePath = Encryption.decrypt(context, secretKey, new File(filePath));

            if (decryptionCallbacks != null) {
                decryptionCallbacks.onSuccess(decryptedFilePath);
            }
        } catch (NoSuchAlgorithmException ex) {
            Log.e(TAG, "doInBackground() -> ", ex);
            if (decryptionCallbacks != null) {
                decryptionCallbacks.onException(NO_SUCH_ALGORITHM_EXCEPTION);
            }
        } catch (NoSuchPaddingException ex) {
            Log.e(TAG, "doInBackground() -> ", ex);
            if (decryptionCallbacks != null) {
                decryptionCallbacks.onException(NO_SUCH_PADDING_EXCEPTION);
            }
        } catch (InvalidKeyException ex) {
            Log.e(TAG, "doInBackground() -> ", ex);
            if (decryptionCallbacks != null) {
                decryptionCallbacks.onException(INVALID_KEY_EXCEPTION);
            }
        } catch (UnsupportedEncodingException ex) {
            Log.e(TAG, "doInBackground() -> ", ex);
            if (decryptionCallbacks != null) {
                decryptionCallbacks.onException(UNSUPPORTED_ENCODING_EXCEPTION);
            }
        } catch (IOException ex) {
            Log.e(TAG, "doInBackground() -> ", ex);
            if (decryptionCallbacks != null) {
                decryptionCallbacks.onException(IO_EXCEPTION);
            }
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        if (decryptionCallbacks != null) {
            decryptionCallbacks.onComplete();
        }
    }

    public void setDecryptionCallbacks(DecryptionCallbacks decryptionCallbacks) {
        this.decryptionCallbacks = decryptionCallbacks;
    }

    public interface DecryptionCallbacks {
        /**
         * Called before decryption process wa started
         */
        void onPrepare();

        /**
         * Decryption process was completed
         * <p>
         * This method will be called without reference to result.
         * You can use it to remove {@link android.app.ProgressDialog}
         * </p>
         */
        void onComplete();

        /**
         * Decryption process was completed
         * <p/>
         * <p>
         * This method will be called only if decryption process was successful.
         * </p>
         *
         * @param decryptedFilePath - Path of decrypted file
         */
        void onSuccess(String decryptedFilePath);

        /**
         * Exception was trowed during decryption process
         *
         * @param exceptionCode - Code of trowed exception {@link DecryptionTask#IO_EXCEPTION}
         */
        void onException(int exceptionCode);
    }
}
