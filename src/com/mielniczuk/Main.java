package com.mielniczuk;

import javax.crypto.*;
import javax.swing.*;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class Main {

    private static String FILE_PATH = "src/wolf.jpg";
    private static String DECRYPTED_FILE_PATH = "src/wolfDecrypted.jpg";
    private static String ENCRYPTED_FILE_PATH = "src/wolfEncrypted.cfr";
    private static String DATA_PATH = "src/plik.txt";
    private static String DECRYPTED_DATA_PATH = "src/plikDecrypted.txt";

    private KeyGenerator keygen;
    private SecretKey key;
    private Cipher cipher;
    private byte[] encryptedData;
    private DBConnector dbConnector;


    private static JFrame frame;
    private JTextArea inputTextArea;
    private JTextArea encryptedTextArea;
    private JTextArea decryptedTextArea;
    private JButton startButton;
    private JPanel mainJPanel;

    private void encrypt() throws IOException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        FileInputStream inputFile = new FileInputStream(FILE_PATH);
        FileInputStream inputData = new FileInputStream(DATA_PATH);


        String inputText = inputTextArea.getText();

        cipher = Cipher.getInstance("DESede");
        cipher.init(Cipher.ENCRYPT_MODE, key);


        encryptText(inputText);
        encryptFile(inputFile);

        ByteArrayOutputStream o1 = (ByteArrayOutputStream) encryptData(inputData);
        ByteArrayInputStream i = new ByteArrayInputStream(o1.toByteArray());
        dbConnector.setData(i);

        inputFile.close();
        o1.close();
        i.close();
    }

    private void encryptFile(FileInputStream inputFile) throws IOException {
        FileOutputStream outputFile = new FileOutputStream(ENCRYPTED_FILE_PATH);
        CipherOutputStream cipherOut = new CipherOutputStream(outputFile, cipher);
        byte[] buffer = new byte[2048];
        int readBytes;
        while ((readBytes = inputFile.read(buffer)) != -1) {
            cipherOut.write(buffer, 0, readBytes);
        }
        cipherOut.close();
        outputFile.close();
    }

    private void encryptText(String inputText) throws BadPaddingException, IllegalBlockSizeException {
        encryptedData = cipher.doFinal(inputText.getBytes());
        String encryptedText = new String(encryptedData);
        encryptedTextArea.setText(encryptedText);
    }

    private OutputStream encryptData(FileInputStream inputFile) throws IOException {
        OutputStream outStream = new ByteArrayOutputStream();
        CipherOutputStream cipherOut = new CipherOutputStream(outStream, cipher);
        byte[] buffer = new byte[2048];
        int readBytes;
        while ((readBytes = inputFile.read(buffer)) != -1) {
            cipherOut.write(buffer, 0, readBytes);
        }
        cipherOut.close();
        return outStream;
    }


    private void decrypt() throws IOException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        FileInputStream inputFile = new FileInputStream(ENCRYPTED_FILE_PATH);
        FileOutputStream outputFile = new FileOutputStream(DECRYPTED_FILE_PATH);
        FileOutputStream outputData = new FileOutputStream(DECRYPTED_DATA_PATH);

        String encryptedText = encryptedTextArea.getText();

        cipher = Cipher.getInstance("DESede");
        cipher.init(Cipher.DECRYPT_MODE, key);

        decryptText(encryptedText);
        decryptFile(inputFile, outputFile);

        ByteArrayInputStream i = dbConnector.getData();
        decryptData(i, outputData);

        inputFile.close();
        outputFile.close();
        outputData.close();
        i.close();
    }

    private void decryptFile(FileInputStream inputFile, FileOutputStream outputFile) throws IOException {
        CipherOutputStream cipherOut = new CipherOutputStream(outputFile, cipher);
        byte[] buffer = new byte[2048];
        int readBytes;
        while ((readBytes = inputFile.read(buffer)) != -1) {
            cipherOut.write(buffer, 0, readBytes);
        }
        cipherOut.close();
    }

    private void decryptText(String encryptedText) throws BadPaddingException, IllegalBlockSizeException {
        String decryptedText = new String(cipher.doFinal(encryptedData));
        decryptedTextArea.setText(decryptedText);
    }

    private void decryptData(ByteArrayInputStream inputStream, FileOutputStream outputFile) throws IOException {
        CipherOutputStream cipherOut = new CipherOutputStream(outputFile, cipher);
        byte[] buffer = new byte[2048];
        int readBytes;
        while ((readBytes = inputStream.read(buffer)) != -1) {
            cipherOut.write(buffer, 0, readBytes);
        }
        cipherOut.close();
    }

    private Main() throws NoSuchAlgorithmException {
        dbConnector = new DBConnector();

        keygen = KeyGenerator.getInstance("DESede");
        key = keygen.generateKey();

        startButton.addActionListener(e -> {
            try {
                encrypt();
                decrypt();
            } catch (IOException | NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | BadPaddingException | IllegalBlockSizeException ex) {
                ex.printStackTrace();
            }
        });
    }

    public static void main(String[] args) throws NoSuchAlgorithmException {
        frame = new JFrame("Cryptography");
        frame.setContentPane(new Main().mainJPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setResizable(true);
    }
}
