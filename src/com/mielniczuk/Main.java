package com.mielniczuk;

import javax.crypto.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class Main {

    private KeyGenerator keygen;
    private SecretKey key;
    private Cipher cipher;


    private static JFrame frame;
    private JTextArea textArea1;
    private JTextArea textArea2;
    private JTextArea textArea3;
    private JButton startButton;
    private JPanel mainJPanel;

    private void encrypt() throws IOException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        FileInputStream inputFile = new FileInputStream("src/wolf.jpg");
        FileOutputStream outputFile = new FileOutputStream("src/wolfEncrypted.cfr");
        cipher = Cipher.getInstance("DESede");
        cipher.init(Cipher.ENCRYPT_MODE, key);

        encryptFile(inputFile, outputFile);
    }

    private void encryptFile(FileInputStream inputFile, FileOutputStream outputFile) throws IOException {
        CipherOutputStream cipherOut = new CipherOutputStream(outputFile,cipher);
        byte[] buffer = new byte[2048];
        int readBytes;
        while ((readBytes= inputFile.read(buffer)) != -1) {
            cipherOut.write(buffer, 0, readBytes);
        }
    }

    private void decrypt() throws IOException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        FileInputStream inputFile = new FileInputStream("src/wolfEncrypted.cfr");
        FileOutputStream outputFile = new FileOutputStream("src/wolfDecrypted.jpg");

        cipher = Cipher.getInstance("DESede");
        cipher.init(Cipher.DECRYPT_MODE, key);

        decryptFile(inputFile, outputFile);
    }

    private void decryptFile(FileInputStream inputFile, FileOutputStream outputFile) throws IOException {
        byte[] buffer = new byte[2048];
        int readBytes;
        while ((readBytes= inputFile.read(buffer)) != -1) {
            outputFile.write(cipher.update(buffer,0,readBytes));
        }
    }

    private Main() throws NoSuchAlgorithmException {
        keygen = KeyGenerator.getInstance("DESede");
        key = keygen.generateKey();

        startButton.addActionListener(e -> {
            try {
                encrypt();
                decrypt();
            } catch (IOException | NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException ex) {
                ex.printStackTrace();
            }
        });
    }

    public static void main(String[] args) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        frame = new JFrame("Cryptography");
        frame.setContentPane(new Main().mainJPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setResizable(true);
    }
}
