package org.example;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static org.example.FileEncryptor.decryptFile;
import static org.example.FileEncryptor.encryptFile;


public class StartServer {

    public static void main(String[] args) throws IOException {
        if (args.length != 5) {
            System.out.println("Usage: StartServer <encrypt|decrypt> <inputFilePath> <outputFilePath> <password> <salt>");
            return;
        }

        String operation = args[0];
        String inputFilePath = args[1];
        String outputFilePath = args[2];
        String password = args[3];
        String salt = args[4];

        try {
            if ("encrypt".equalsIgnoreCase(operation)) {
                encryptFile(inputFilePath, outputFilePath, password, salt);
            } else if ("decrypt".equalsIgnoreCase(operation)) {
                decryptFile(inputFilePath, outputFilePath, password, salt);
            } else {
                System.out.println("Invalid operation. Use 'encrypt' or 'decrypt'.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try (InputStream inputStream = StartServer.class.getClassLoader().getResourceAsStream("property.txt")) {
            Properties defaultProps = new Properties();

            defaultProps.load(inputStream);

            String serialize = defaultProps.get("serialize").toString();
            String inputPath = defaultProps.get("inputFile").toString();
            String outputPath = defaultProps.get("outputFile").toString();
            String pass = defaultProps.get("password").toString();
            String saltProperty = defaultProps.get("salt").toString();
            var x = 8;

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

    }
}


