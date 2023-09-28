package org.example;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class StartServer {

    public static void main(String[] args) throws Exception {

        if (args.length != 3) {
            System.out.println("Usage: StartServer <encrypt|decrypt> <inputFilePath> <outputFilePath>");
            return;
        }
        String operation = args[0];
        String inputFilePath = args[1];
        String outputFilePath = args[2];

        EncryptionConfig config = getProperties();
        FileEncryptor fileEncryptor = new FileEncryptor(config);
        switch (operation) {
            case "encrypt" -> {
                fileEncryptor.encryptFile(inputFilePath, outputFilePath);
                System.out.println("Encryption completed.");
            }
            case "decrypt" -> {
                fileEncryptor.decryptFile(inputFilePath, outputFilePath);
                System.out.println("Decryption completed.");
            }
            default -> System.err.println("Invalid operation. Use 'encrypt' or 'decrypt'.");
        }

    }

    private static EncryptionConfig getProperties() throws IOException {
        try (InputStream inputStream = StartServer.class.getClassLoader().getResourceAsStream("configs.properties")) {
            Properties defaultProps = new Properties();

            defaultProps.load(inputStream);

            String password = defaultProps.get("password").toString();
            String salt = defaultProps.get("salt").toString();

            return new EncryptionConfig(password, salt);

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}


