package com.mielniczuk;

import javax.crypto.*;
import javax.swing.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class Main {

    private KeyGenerator keygen;
    private SecretKey key;
    private Cipher cipher;
    private byte[] encryptedData;
    private DBConnector dbConnector;
    private int filledBytesNumberForFile;
    private int filledBytesNumberForData;

    private static JFrame frame;
    private JTextArea inputTextArea;
    private JTextArea encryptedTextArea;
    private JTextArea decryptedTextArea;
    private JButton startButton;
    private JPanel mainJPanel;

    private void encrypt() throws IOException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        FileInputStream inputFile = new FileInputStream("src/wolf.jpg");
        FileInputStream inputData = new FileInputStream("src/plik.txt");

        FileOutputStream outputFile = new FileOutputStream("src/wolfEncrypted.cfr");

        String inputText = inputTextArea.getText();

        cipher = Cipher.getInstance("DESede");
        cipher.init(Cipher.ENCRYPT_MODE, key);


        encryptText(inputText);
        encryptFile(inputFile, outputFile);

        ByteArrayOutputStream o1 = (ByteArrayOutputStream)encryptData(inputData);
        dbConnector.setData(new ByteArrayInputStream(o1.toByteArray()));

        inputFile.close();
        outputFile.close();
    }

    private void encryptFile(FileInputStream inputFile, FileOutputStream outputFile) throws IOException {
        CipherOutputStream cipherOut = new CipherOutputStream(outputFile, cipher);
        byte[] buffer = new byte[2048];
        int readBytes;
        while ((readBytes = inputFile.read(buffer)) != -1) {
            cipherOut.write(buffer, 0, readBytes + readBytes % 16);
            filledBytesNumberForFile = readBytes%16;
        }
        cipherOut.close();
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
            cipherOut.write(buffer, 0, readBytes + readBytes % 16);
            filledBytesNumberForData = readBytes %16;
        }
        cipherOut.close();
        return outStream;
    }


    private void decrypt() throws IOException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        FileInputStream inputFile = new FileInputStream("src/wolfEncrypted.cfr");
        FileOutputStream outputFile = new FileOutputStream("src/wolfDecrypted.jpg");
        FileOutputStream outputData = new FileOutputStream("src/plikDecrypted.txt");

        String encryptedText = encryptedTextArea.getText();

        cipher = Cipher.getInstance("DESede");
        cipher.init(Cipher.DECRYPT_MODE, key);

        decryptText(encryptedText);
        decryptFile(inputFile, outputFile);

        InputStream i = dbConnector.getData();
        decryptData(i,outputData);

        inputFile.close();
        outputFile.close();
        outputData.close();
        i.close();
    }

    private void decryptFile(FileInputStream inputFile, FileOutputStream outputFile) throws IOException {
        byte[] buffer = new byte[2048];
        int readBytes;
        while ((readBytes = inputFile.read(buffer)) != -1) {
            if (readBytes < 2048) {
                byte[] tmp = cipher.update(buffer, 0, readBytes);
                outputFile.write(tmp,0,readBytes-filledBytesNumberForFile);
            } else {
                outputFile.write(cipher.update(buffer, 0, readBytes));
            }
        }
    }

    private void decryptText(String encryptedText) throws BadPaddingException, IllegalBlockSizeException {
        String decryptedText = new String(cipher.doFinal(encryptedData));
        decryptedTextArea.setText(decryptedText);
    }

    private void decryptData(InputStream inputStream, FileOutputStream outputFile) throws IOException {
        byte[] buffer = new byte[2048];
        int readBytes;
        while ((readBytes = inputStream.read(buffer)) != -1) {
            if (readBytes < 2048) {
                byte[] tmp = cipher.update(buffer, 0, readBytes);
                outputFile.write(tmp,0,readBytes-filledBytesNumberForData);
            } else {
                outputFile.write(cipher.update(buffer, 0, readBytes));
            }
        }
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
