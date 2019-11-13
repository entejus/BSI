package com.mielniczuk;

import javax.crypto.*;
import javax.swing.*;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class Main {


    private static final String FILE_PATH = "src/wolf.jpg";
    private static final String DECRYPTED_FILE_PATH = "src/wolfDecrypted.jpg";
    private static final String ENCRYPTED_FILE_PATH = "src/wolfEncrypted.cfr";
    private static final String ENCRYPTED_TEXT_FILE_PATH = "src/text.cfr";
    private static final String DATA_PATH = "src/plik.txt";
    private static final String DECRYPTED_DATA_PATH = "src/plikDecrypted.txt";

    private SecretKey key;
    private DBConnector dbConnector;
    private Cipher encryptCipher;
    private Cipher decryptCipher;


    private JTextArea inputTextArea;
    private JTextArea decryptedTextArea;
    private JButton startButton;
    private JPanel mainJPanel;


    public void encrypt() throws IOException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        //Cipher initialization
        encryptCipher = Cipher.getInstance("DESede");
        encryptCipher.init(Cipher.ENCRYPT_MODE, key);

        String inputText = inputTextArea.getText();
        FileOutputStream encryptedTextFile = new FileOutputStream(ENCRYPTED_TEXT_FILE_PATH);
        encryptText(inputText, encryptedTextFile);

        FileInputStream inputFile = new FileInputStream(FILE_PATH);
        FileOutputStream outputFile = new FileOutputStream(ENCRYPTED_FILE_PATH);
        outputFile.write(encryptData(inputFile));
//        encryptFile(inputFile, outputFile);

        FileInputStream inputData = new FileInputStream(DATA_PATH);
        ByteArrayInputStream encryptedDataInput = new ByteArrayInputStream(encryptData(inputData));
        dbConnector.setData(encryptedDataInput);

        inputFile.close();
        outputFile.close();
        encryptedDataInput.close();
        encryptedTextFile.close();
    }

    public void encryptText(String inputText, FileOutputStream outputFileStream) throws BadPaddingException, IllegalBlockSizeException, IOException {
        byte[] inputBytes = inputText.getBytes();
        outputFileStream.write(encryptCipher.doFinal(inputBytes));
        outputFileStream.close();
    }


    public byte[] encryptData(FileInputStream inputFile) throws IOException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        CipherOutputStream cipherOut = new CipherOutputStream(outStream, encryptCipher);
        byte[] buffer = new byte[2048];
        int readBytes;
        while ((readBytes = inputFile.read(buffer)) != -1) {
            cipherOut.write(buffer, 0, readBytes);
        }
        cipherOut.close();
        return outStream.toByteArray();
    }


    public void decrypt() throws IOException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {


        decryptCipher = Cipher.getInstance("DESede");
        decryptCipher.init(Cipher.DECRYPT_MODE, key);

        FileInputStream inputFile = new FileInputStream(ENCRYPTED_FILE_PATH);

        FileInputStream inputTextFile = new FileInputStream(ENCRYPTED_TEXT_FILE_PATH);
        String decryptedText = decryptText(inputTextFile);
        decryptedTextArea.setText(decryptedText);

        FileOutputStream outputFile = new FileOutputStream(DECRYPTED_FILE_PATH);
        decryptData(inputFile, outputFile);


        FileOutputStream outputData = new FileOutputStream(DECRYPTED_DATA_PATH);
        ByteArrayInputStream encryptedDataInput = dbConnector.getData();
        decryptData(encryptedDataInput, outputData);

        inputFile.close();
        outputFile.close();
        inputTextFile.close();
        outputData.close();
        encryptedDataInput.close();
    }

    public String decryptText(FileInputStream inputFile) throws BadPaddingException, IllegalBlockSizeException, IOException {
        byte[] encryptedBytes = inputFile.readAllBytes();
        return new String(decryptCipher.doFinal(encryptedBytes));
    }


    public void decryptData(InputStream inputStream, FileOutputStream outputFile) throws IOException {
        CipherOutputStream cipherOut = new CipherOutputStream(outputFile, decryptCipher);
        byte[] buffer = new byte[2048];
        int readBytes;
        while ((readBytes = inputStream.read(buffer)) != -1) {
            cipherOut.write(buffer, 0, readBytes);
        }
        cipherOut.close();
        outputFile.close();
    }


    public Main() throws NoSuchAlgorithmException {

        KeyGenerator keygen = KeyGenerator.getInstance("DESede");
        key = keygen.generateKey();

        startButton.addActionListener(e -> {
            try {
                dbConnector = new DBConnector();
                encrypt();
                decrypt();
                dbConnector.close();
            } catch (IOException | NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | BadPaddingException | IllegalBlockSizeException ex) {
                ex.printStackTrace();
            }
        });
    }

    public static void main(String[] args) throws NoSuchAlgorithmException {
        JFrame frame = new JFrame("Cryptography");
        frame.setContentPane(new Main().mainJPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setResizable(true);
    }
}
