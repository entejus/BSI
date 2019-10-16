package com.mielniczuk;

import javax.crypto.*;
import javax.swing.*;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class Main {

    private KeyGenerator keygen;
    private SecretKey key;
    private Cipher cipher;
    private byte[] encryptedData;


    private static JFrame frame;
    private JTextArea inputTextArea;
    private JTextArea encryptedTextArea;
    private JTextArea decryptedTextArea;
    private JButton startButton;
    private JPanel mainJPanel;

    private void encrypt() throws IOException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        FileInputStream inputFile = new FileInputStream("src/wolf.jpg");
        FileOutputStream outputFile = new FileOutputStream("src/wolfEncrypted.cfr");

        String inputText = inputTextArea.getText();

        cipher = Cipher.getInstance("DESede");
        cipher.init(Cipher.ENCRYPT_MODE, key);

        encryptText(inputText);
        encryptFile(inputFile, outputFile);
    }

    private void encryptFile(FileInputStream inputFile, FileOutputStream outputFile) throws IOException {
        CipherOutputStream cipherOut = new CipherOutputStream(outputFile, cipher);
        byte[] buffer = new byte[2048];
        int readBytes;
        while ((readBytes = inputFile.read(buffer)) != -1) {
            cipherOut.write(buffer, 0, readBytes);
        }
    }

    private void encryptText(String inputText) throws BadPaddingException, IllegalBlockSizeException {
        encryptedData = cipher.doFinal(inputText.getBytes());
        String encryptedText = new String(encryptedData);
        encryptedTextArea.setText(encryptedText);
    }


    private void decrypt() throws IOException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        FileInputStream inputFile = new FileInputStream("src/wolfEncrypted.cfr");
        FileOutputStream outputFile = new FileOutputStream("src/wolfDecrypted.jpg");

        String encryptedText = encryptedTextArea.getText();

        cipher = Cipher.getInstance("DESede");
        cipher.init(Cipher.DECRYPT_MODE, key);

        decryptText(encryptedText);
        decryptFile(inputFile, outputFile);
    }

    private void decryptFile(FileInputStream inputFile, FileOutputStream outputFile) throws IOException {
        byte[] buffer = new byte[2048];
        int readBytes;
        while ((readBytes = inputFile.read(buffer)) != -1) {
            outputFile.write(cipher.update(buffer, 0, readBytes));
        }
    }

    private void decryptText(String encryptedText) throws BadPaddingException, IllegalBlockSizeException {
        String decryptedText = new String(cipher.doFinal(encryptedData));
        decryptedTextArea.setText(decryptedText);
    }

    private Main() throws NoSuchAlgorithmException {
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
