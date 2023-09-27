package org.example;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class FileEncryptor {


    public static void encryptFile(String inputFilePath, String outputFilePath, String password, String salt)
            throws Exception {

        FileInputStream fileInputStream = new FileInputStream(inputFilePath);
        FileOutputStream fileOutputStream = new FileOutputStream(outputFilePath);

        Cipher cipher = getCipher(password, salt, Cipher.ENCRYPT_MODE);

        byte[] inputBytes = new byte[64];
        int bytesRead;
        while ((bytesRead = fileInputStream.read(inputBytes)) != -1) {
            byte[] outputBytes = cipher.update(inputBytes, 0, bytesRead);
            fileOutputStream.write(outputBytes);
        }

        byte[] outputBytes = cipher.doFinal();
        fileOutputStream.write(outputBytes);

        fileInputStream.close();
        fileOutputStream.close();
        System.out.println("Encryption completed.");
    }

    private static Cipher getCipher(String password, String salt, int encryptMode)
            throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException {
        KeySpec keySpec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), 65536, 256);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] keyBytes = keyFactory.generateSecret(keySpec).getEncoded();
        SecretKeySpec secretKey = new SecretKeySpec(keyBytes, "AES");

        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(encryptMode, secretKey);
        return cipher;
    }

    public static void decryptFile(String inputFilePath, String outputFilePath, String password, String salt)
            throws Exception {

        FileInputStream fileInputStream = new FileInputStream(inputFilePath);
        FileOutputStream fileOutputStream = new FileOutputStream(outputFilePath);

        Cipher cipher = getCipher(password, salt, Cipher.DECRYPT_MODE);

        byte[] inputBytes = new byte[64];
        int bytesRead;
        while ((bytesRead = fileInputStream.read(inputBytes)) != -1) {
            byte[] outputBytes = cipher.update(inputBytes, 0, bytesRead);
            fileOutputStream.write(outputBytes);
        }

        byte[] outputBytes = cipher.doFinal();
        fileOutputStream.write(outputBytes);

        fileInputStream.close();
        fileOutputStream.close();
        System.out.println("Decryption completed.");
    }
}