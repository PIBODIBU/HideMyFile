package com.android.hidemyfile.Encryption;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Base64;
import android.util.Log;

import com.android.hidemyfile.Support.File.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * This class provides encryption/decryption logic
 */
public class Encryption {

    private static final String TAG = "Encryption";

    public static final String FILE_ENCRYPTION_PREFIX = ".encrypted";
    public static final String PASSWORD_4DIGIT_DEFAULT = "0000";

    private static final String ENCRYPTION_AES = "AES";
    private static final String ENCRYPTION_SHA1 = "SHA-1";
    private static final String ENCRYPTION_SALT = "fucking_static_salt";
    private static final String ENCODING_UTF8 = "UTF-8";
    private static final int SECRET_KEY_LENGTH_BYTE = 16; // 128 bits

    /**
     * Method for encrypting files
     *
     * @param context   - Context of Application/Activity. Used for scanning MediaStorage after encryption is finished
     * @param secretKey - Secret key for encryption algorithm
     * @param file      - {@link File} for encryption
     * @return (String) path of encrypted file
     * @throws IOException              - Throws is several situations:
     *                                  <p>
     *                                  - Encryption process was interrupted
     *                                  - User provided bad secret key
     *                                  - File path was edited during encryption process
     *                                  etc.
     *                                  </p>
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws InvalidKeyException
     */
    public static String encrypt(Context context, String secretKey, File file) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
        String filePath = file.getPath() + FILE_ENCRYPTION_PREFIX;

        // Create new output File
        File newFile = new File(filePath);

        // Creating new streams
        FileInputStream fileInputStream = new FileInputStream(file);
        FileOutputStream fileOutputStream = new FileOutputStream(newFile);

        // Create new instance of SecretKeySpec with user's secretKey
        SecretKeySpec secretKeySpec = new SecretKeySpec(getKeyFromString(secretKey), ENCRYPTION_AES);

        // Create new instance of Cipher
        Cipher cipher = Cipher.getInstance(ENCRYPTION_AES);
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);

        // Wrap the output stream
        CipherOutputStream cipherOutputStream = new CipherOutputStream(fileOutputStream, cipher);

        // Write encrypted file
        int byteCount;
        int byteOffset = 0;
        byte[] buffer = new byte[8];
        while ((byteCount = fileInputStream.read(buffer)) != -1) {
            cipherOutputStream.write(buffer, byteOffset, byteCount);
        }

        // Flush and close streams.
        cipherOutputStream.flush();
        cipherOutputStream.close();
        fileInputStream.close();

        // Delete old file
        if (file.delete()) {
            Log.d(TAG, "encrypt() -> Original file deleted");
        } else {
            Log.e(TAG, "encrypt() -> Error while deleting original file");
        }

        // Refresh Media cache of device
        scanMediaWithIntent(context, file);
        scanMediaWithIntent(context, newFile);

        return filePath;
    }

    public static String encrypt(String value) {
        try {
            IvParameterSpec ivParameterSpec = new IvParameterSpec(getKeyFromString(getSalt()));
            SecretKeySpec secretKeySpec = new SecretKeySpec(getKeyFromString(value), ENCRYPTION_AES);

            Cipher cipher = Cipher.getInstance(ENCRYPTION_AES);
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);

            byte[] encrypted = cipher.doFinal(value.getBytes());
            Log.d(TAG, "encrypted string: " + Base64.encodeToString(encrypted, Base64.DEFAULT));

            return Base64.encodeToString(encrypted, Base64.DEFAULT);
        } catch (Exception ex) {
            Log.e(TAG, "encrypt() -> ", ex);
        }

        return null;
    }

    /**
     * Method for decrypting files
     *
     * @param context   - Context of Application/Activity. Used for scanning MediaStorage after decryption is finished
     * @param secretKey - Secret key for decryption algorithm
     * @param file      - {@link File} for decryption
     * @return (String) path of decrypted file
     * @throws IOException              - Throws is several situations:
     *                                  <p>
     *                                  - Decryption process was interrupted
     *                                  - User provided bad secret key
     *                                  - File path was edited during Decryption process
     *                                  etc.
     *                                  </p>
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws InvalidKeyException
     */
    public static String decrypt(Context context, String secretKey, File file) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
        String filePath = file.getPath().replace(FILE_ENCRYPTION_PREFIX, "");

        // Create new output File
        File newFile = new File(filePath);

        // Creating new streams
        FileInputStream fileInputStream = new FileInputStream(file);
        FileOutputStream fileOutputStream = new FileOutputStream(newFile);

        // Create new instance of SecretKeySpec with user's secretKey
        SecretKeySpec secretKeySpec = new SecretKeySpec(getKeyFromString(secretKey), ENCRYPTION_AES);

        // Create new instance of Cipher
        Cipher cipher = Cipher.getInstance(ENCRYPTION_AES);
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);

        // Wrap the output stream
        CipherInputStream cipherInputStream = new CipherInputStream(fileInputStream, cipher);

        // Write encrypted file
        int byteCount;
        int byteOffset = 0;
        byte[] buffer = new byte[8];
        while ((byteCount = cipherInputStream.read(buffer)) != -1) {
            fileOutputStream.write(buffer, byteOffset, byteCount);
        }

        // Flush and close streams.
        fileOutputStream.flush();
        fileOutputStream.close();
        cipherInputStream.close();

        // Delete old file
        if (file.delete()) {
            Log.d(TAG, "decrypt() -> Original file deleted");
        } else {
            Log.e(TAG, "decrypt() -> Error while deleting original file");
        }

        if (FileUtils.isFileHidden(newFile)) {
            unHideFile(filePath);
        }

        // Refresh Media cache of device
        scanMediaWithIntent(context, file);
        scanMediaWithIntent(context, newFile);

        return filePath;
    }

    /**
     * Method for hiding file
     * <p/>
     * <p>
     * Method adds prefix "." before file name.
     * </p>
     *
     * @param filePath - Path of file for hiding
     * @return - path of hidden file
     * - Empty {@link String} if hiding was unsuccessful
     */
    public static String hideFile(String filePath) {
        File file = new File(filePath);

        String fileName = file.getName();
        String filePathWithoutName = filePath.replace(fileName, "");
        String hiddenFileName = "." + fileName;

        if (file.renameTo(new File(filePathWithoutName + hiddenFileName))) {
            return file.getAbsolutePath();
        } else {
            return "";
        }
    }

    /**
     * Method for making hidden file visible
     * <p/>
     * <p>
     * Method removes prefix "." before file name.
     * {@link Encryption#hideFile(String)}
     * </p>
     *
     * @param filePath - Path of file for making it visible
     * @return - new path of file
     * - Empty {@link String} if process was unsuccessful
     */
    public static String unHideFile(String filePath) {
        File file = new File(filePath);

        String fileName = file.getName();
        String filePathWithoutName = filePath.replace(fileName, "");
        String unHiddenFileName = FileUtils.removeHidePrefix(fileName);

        if (file.renameTo(new File(filePathWithoutName + unHiddenFileName))) {
            return file.getAbsolutePath();
        } else {
            return "";
        }
    }

    /**
     * Method for getting salt for encryption
     *
     * @return (String) salt
     */
    private static String getSalt() {
        return ENCRYPTION_SALT;
    }

    // TODO change method for user's key encrypting (Getting first 128 bits is dangerous)

    /**
     * Method performs merging user's secret key with salt and encryption new String with SHA-1 algorithm
     *
     * @param stringKey - Secret key for encryption
     * @return - array of bytes - first 128 bits of encrypted key
     * @throws UnsupportedEncodingException
     * @throws NoSuchAlgorithmException
     */
    private static byte[] getKeyFromString(String stringKey) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        byte[] key = (getSalt() + stringKey).getBytes(ENCODING_UTF8);
        MessageDigest sha = MessageDigest.getInstance(ENCRYPTION_SHA1);

        key = sha.digest(key);
        key = Arrays.copyOf(key, SECRET_KEY_LENGTH_BYTE); // use only first 128 bits

        return key;
    }

    /**
     * Scan MediaStorage of device
     * <p/>
     * <p>
     * This method is used for scanning device's storage after encryption/decryption of File
     * i.e notifying device about deleting old File and creating new.
     * </p>
     *
     * @param context - Context of Application/Activity
     * @param file    - {@link File} for scanning
     */
    private static void scanMediaWithIntent(Context context, File file) {
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));
    }
}
