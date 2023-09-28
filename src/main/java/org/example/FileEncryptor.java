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

    private final EncryptionConfig encryptionConfig;
    private static final int BUFFER_SIZE = 2048;

    public FileEncryptor(EncryptionConfig encryptionConfig) {
        this.encryptionConfig = encryptionConfig;
    }

    public void encryptFile(String inputFilePath, String outputFilePath) throws Exception {
        encrypt(inputFilePath, outputFilePath, Cipher.ENCRYPT_MODE);
    }

    public void decryptFile(String inputFilePath, String outputFilePath) throws Exception {
        encrypt(inputFilePath, outputFilePath, Cipher.DECRYPT_MODE);
    }

    private void encrypt(String inputFilePath, String outputFilePath, int encryptMode) throws Exception {
        try (FileInputStream fileInputStream = new FileInputStream(inputFilePath);
             FileOutputStream fileOutputStream = new FileOutputStream(outputFilePath)) {

            Cipher cipher = getCipher(encryptMode);

            byte[] inputBytes = new byte[BUFFER_SIZE];
            int bytesRead;
            while ((bytesRead = fileInputStream.read(inputBytes)) != -1) {
                byte[] outputBytes = cipher.update(inputBytes, 0, bytesRead);
                fileOutputStream.write(outputBytes);
            }

            byte[] outputBytes = cipher.doFinal();
            fileOutputStream.write(outputBytes);
        }

    }

    private Cipher getCipher(int encryptMode)
            throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException {
        KeySpec keySpec = new PBEKeySpec(encryptionConfig.password().toCharArray(), encryptionConfig.salt().getBytes(), 65536, 256);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] keyBytes = keyFactory.generateSecret(keySpec).getEncoded();
        SecretKeySpec secretKey = new SecretKeySpec(keyBytes, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(encryptMode, secretKey);
        return cipher;
    }
}