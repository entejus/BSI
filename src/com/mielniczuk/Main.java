package com.mielniczuk;

import javax.crypto.*;
import javax.swing.*;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

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
        //Input data to encrypt
        String inputText = inputTextArea.getText();
        FileInputStream inputFile = new FileInputStream(FILE_PATH);
        FileInputStream inputData = new FileInputStream(DATA_PATH);

        //Cipher initialization
        encryptCipher = Cipher.getInstance("DESede");
        encryptCipher.init(Cipher.ENCRYPT_MODE, key);

        FileOutputStream encryptedTextFile = new FileOutputStream(ENCRYPTED_TEXT_FILE_PATH);
        encryptText(inputText, encryptedTextFile);

        encryptFile(inputFile, ENCRYPTED_FILE_PATH);

        ByteArrayOutputStream encryptedDataOutput = (ByteArrayOutputStream) encryptData(inputData);
        ByteArrayInputStream encryptedDataInput = new ByteArrayInputStream(encryptedDataOutput.toByteArray());
        dbConnector.setData(encryptedDataInput);

        inputFile.close();
        encryptedDataOutput.close();
        encryptedDataInput.close();
    }

    public void encryptText(String inputText,  FileOutputStream outputFileStream) throws BadPaddingException, IllegalBlockSizeException, IOException {
        byte[] inputBytes = inputText.getBytes();
        outputFileStream.write(encryptCipher.doFinal(inputBytes));
        outputFileStream.close();
    }

    public void encryptFile(FileInputStream inputFile, String outputFilePath) throws IOException {
        FileOutputStream outputFile = new FileOutputStream(outputFilePath);
        cryptingStream(inputFile, outputFile, encryptCipher);
        outputFile.close();
    }

    public OutputStream encryptData(FileInputStream inputFile) throws IOException {
        OutputStream outStream = new ByteArrayOutputStream();
        cryptingStream(inputFile, outStream, encryptCipher);
        return outStream;
    }


    public void decrypt() throws IOException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        FileInputStream inputFile = new FileInputStream(ENCRYPTED_FILE_PATH);
        FileInputStream inputFile2 = new FileInputStream(ENCRYPTED_TEXT_FILE_PATH);
        FileOutputStream outputData = new FileOutputStream(DECRYPTED_DATA_PATH);

        decryptCipher = Cipher.getInstance("DESede");
        decryptCipher.init(Cipher.DECRYPT_MODE, key);

        String decryptedText = decryptText(inputFile2);
        decryptedTextArea.setText(decryptedText);

        decryptFile(inputFile, DECRYPTED_FILE_PATH);

        ByteArrayInputStream encryptedDataInput = dbConnector.getData();
        decryptData(encryptedDataInput, outputData);

        inputFile.close();
        inputFile2.close();
        outputData.close();
        encryptedDataInput.close();
    }

    public String decryptText(FileInputStream inputFile) throws BadPaddingException, IllegalBlockSizeException, IOException {
        byte[] encryptedBytes = inputFile.readAllBytes();
        return new String(decryptCipher.doFinal(encryptedBytes));
    }

    public void decryptFile(FileInputStream inputFile, String outputFilePath) throws IOException {
        FileOutputStream outputFile = new FileOutputStream(outputFilePath);
        cryptingStream(inputFile, outputFile, decryptCipher);
        outputFile.close();
    }


    public void decryptData(ByteArrayInputStream inputStream, FileOutputStream outputFile) throws IOException {
        cryptingStream(inputStream, outputFile, decryptCipher);
    }


    private void cryptingStream(InputStream inputStream, OutputStream outputStream, Cipher cipher) throws IOException {
        CipherOutputStream cipherOut = new CipherOutputStream(outputStream, cipher);
        byte[] buffer = new byte[2048];
        int readBytes;
        while ((readBytes = inputStream.read(buffer)) != -1) {
            cipherOut.write(buffer, 0, readBytes);
        }
        cipherOut.close();
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
