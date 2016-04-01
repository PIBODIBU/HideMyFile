package com.android.hidemyfile.AsyncTask;

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

    private String secretKey;
    private String filePath;

    public static int NO_SUCH_ALGORITHM_EXCEPTION = 0;
    public static int NO_SUCH_PADDING_EXCEPTION = 1;
    public static int INVALID_KEY_EXCEPTION = 2;
    public static int UNSUPPORTED_ENCODING_EXCEPTION = 3;
    public static int IO_EXCEPTION = 4;

    public DecryptionTask(String secretKey, String filePath) {
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
            Encryption.decrypt(secretKey, new File(filePath));

            if (decryptionCallbacks != null) {
                decryptionCallbacks.onSuccess();
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
        void onPrepare();

        void onComplete();

        void onSuccess();

        void onException(int exceptionCode);
    }
}
