package com.android.hidemyfile.Encryption;

import android.util.Log;

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
import javax.crypto.spec.SecretKeySpec;

public class Encryption {

    private static final String TAG = "Encryption";
    public static final String FILE_PREFIX = ".encrypted";

    public static String encrypt(String secretKey, File file) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
        String filePath = file.getPath() + FILE_PREFIX;
        File newFile = new File(filePath);

        FileInputStream fileInputStream = new FileInputStream(file);
        FileOutputStream fileOutputStream = new FileOutputStream(newFile);

        SecretKeySpec secretKeySpec = new SecretKeySpec(getKeyFromString(secretKey), "AES");

        // Create cipher
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);

        // Wrap the output stream
        CipherOutputStream cipherOutputStream = new CipherOutputStream(fileOutputStream, cipher);

        // Write bytes
        int length;
        byte[] buffer = new byte[8];
        while ((length = fileInputStream.read(buffer)) != -1) {
            cipherOutputStream.write(buffer, 0, length);
        }

        // Flush and close streams.
        cipherOutputStream.flush();
        cipherOutputStream.close();
        fileInputStream.close();

        if (file.delete()) {
            Log.d(TAG, "encrypt() -> Original file deleted");
        } else {
            Log.e(TAG, "encrypt() -> Error while deleting original file");
        }

        return filePath;
    }

    public static void decrypt(String secretKey, File file) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
        String filePath = file.getPath().replace(FILE_PREFIX, "");
        File newFile = new File(filePath);

        FileInputStream fileInputStream = new FileInputStream(file);
        FileOutputStream fileOutputStream = new FileOutputStream(newFile);
        SecretKeySpec secretKeySpec = new SecretKeySpec(getKeyFromString(secretKey), "AES");

        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
        CipherInputStream cipherInputStream = new CipherInputStream(fileInputStream, cipher);

        int b;
        byte[] d = new byte[8];
        while ((b = cipherInputStream.read(d)) != -1) {
            fileOutputStream.write(d, 0, b);
        }

        fileOutputStream.flush();
        fileOutputStream.close();
        cipherInputStream.close();

        if (file.delete()) {
            Log.d(TAG, "decrypt() -> Original file deleted");
        } else {
            Log.e(TAG, "decrypt() -> Error while deleting original file");
        }
    }

    private static String getSalt() {
        return "fucking_static_salt";
    }

    private static byte[] getKeyFromString(String stringKey) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        byte[] key = (getSalt() + stringKey).getBytes("UTF-8");
        MessageDigest sha = MessageDigest.getInstance("SHA-1");

        key = sha.digest(key);
        key = Arrays.copyOf(key, 16); // use only first 128 bit

        return key;
    }
}
