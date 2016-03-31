package com.android.hidemyfile.Encryption;

import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

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
        // This stream write the encrypted text. This stream will be wrapped by another stream.
        FileOutputStream fileOutputStream = new FileOutputStream(newFile);

        // Length is 16 byte
        // Careful when taking user input!!! http://stackoverflow.com/a/3452620/1188357
        SecretKeySpec secretKeySpec = new SecretKeySpec("MyDifficultPassw".getBytes(), "AES");

        // Create cipher
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);

        // Wrap the output stream
        CipherOutputStream cos = new CipherOutputStream(fileOutputStream, cipher);

        // Write bytes
        int b;
        byte[] d = new byte[8];
        while ((b = fileInputStream.read(d)) != -1) {
            cos.write(d, 0, b);
        }

        // Flush and close streams.
        cos.flush();
        cos.close();
        fileInputStream.close();

        file.delete();

        return filePath;
    }

    public static void decrypt(String secretKey, File file) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
        String filePath = file.getPath().replace(FILE_PREFIX, "");
        File newFile = new File(filePath);

        FileInputStream fileInputStream = new FileInputStream(file);
        FileOutputStream fileOutputStream = new FileOutputStream(newFile);
        SecretKeySpec secretKeySpec = new SecretKeySpec("MyDifficultPassw".getBytes(), "AES");

        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
        CipherInputStream cipherInputStream = new CipherInputStream(fileInputStream, cipher);

        int b;
        byte[] d = new byte[8];
        while ((b = cipherInputStream.read(d)) != -1) {
            fileOutputStream.write(d, 0, b);
        }

        file.delete();

        fileOutputStream.flush();
        fileOutputStream.close();
        cipherInputStream.close();
    }
}
