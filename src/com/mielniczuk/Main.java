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
    private static final String ENCRYPTED_FILE2_PATH = "src/text.cfr";
    private static final String DATA_PATH = "src/plik.txt";
    private static final String DECRYPTED_DATA_PATH = "src/plikDecrypted.txt";

    private SecretKey key;
    private DBConnector dbConnector;
    private Cipher encryptCipher;
    private Cipher decryptCipher;




    private JTextArea inputTextArea;
    private JTextArea encryptedTextArea;
    private JTextArea decryptedTextArea;
    private JButton startButton;
    private JPanel mainJPanel;


    public void encrypt() throws IOException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        String inputText = inputTextArea.getText();
        FileInputStream inputFile = new FileInputStream(FILE_PATH);
        FileInputStream inputData = new FileInputStream(DATA_PATH);

        encryptCipher = Cipher.getInstance("DESede");
        encryptCipher.init(Cipher.ENCRYPT_MODE, key);

        String encryptedText = encryptText(inputText, ENCRYPTED_FILE2_PATH,encryptCipher);
        encryptedTextArea.setText(encryptedText);

        encryptFile(inputFile, ENCRYPTED_FILE_PATH, encryptCipher);

        ByteArrayOutputStream encryptedDataOutput = (ByteArrayOutputStream) encryptData(inputData, encryptCipher);
        ByteArrayInputStream encryptedDataInput = new ByteArrayInputStream(encryptedDataOutput.toByteArray());
        dbConnector.setData(encryptedDataInput);

        inputFile.close();
        encryptedDataOutput.close();
        encryptedDataInput.close();
    }

    public String encryptText(String inputText,  String outputFilePath,Cipher cipher) throws BadPaddingException, IllegalBlockSizeException, IOException {
        FileOutputStream outputFile = new FileOutputStream(outputFilePath);
        byte[] inputBytes = inputText.getBytes();
        outputFile.write(cipher.doFinal(inputBytes));
        outputFile.close();
        return Base64.getEncoder().encodeToString(cipher.doFinal(inputBytes));
    }

    public void encryptFile(FileInputStream inputFile, String outputFilePath, Cipher cipher) throws IOException {
        FileOutputStream outputFile = new FileOutputStream(outputFilePath);
        cryptingStream(inputFile, outputFile, cipher);
        outputFile.close();
    }

    public OutputStream encryptData(FileInputStream inputFile, Cipher cipher) throws IOException {
        OutputStream outStream = new ByteArrayOutputStream();
        cryptingStream(inputFile, outStream, cipher);
        return outStream;
    }


    public void decrypt() throws IOException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        String encryptedText = encryptedTextArea.getText();
        FileInputStream inputFile = new FileInputStream(ENCRYPTED_FILE_PATH);
        FileInputStream inputFile2 = new FileInputStream(ENCRYPTED_FILE2_PATH);
        FileOutputStream outputData = new FileOutputStream(DECRYPTED_DATA_PATH);

        decryptCipher = Cipher.getInstance("DESede");
        decryptCipher.init(Cipher.DECRYPT_MODE, key);

        String decryptedText = decryptText(encryptedText,inputFile2,decryptCipher);
        decryptedTextArea.setText(decryptedText);

        decryptFile(inputFile, DECRYPTED_FILE_PATH, decryptCipher);

        ByteArrayInputStream encryptedDataInput = dbConnector.getData();
        decryptData(encryptedDataInput, outputData, decryptCipher);

        inputFile.close();
        inputFile2.close();
        outputData.close();
        encryptedDataInput.close();
    }

    public String decryptText(String encryptedText ,FileInputStream inputFile, Cipher cipher) throws BadPaddingException, IllegalBlockSizeException, IOException {
//        byte[] encryptedBytes = Base64.getDecoder().decode(encryptedText);
        byte[] encryptedBytes = inputFile.readAllBytes();
        return new String(cipher.doFinal(encryptedBytes));
    }

    public void decryptFile(FileInputStream inputFile, String outputFilePath, Cipher cipher) throws IOException {
        FileOutputStream outputFile = new FileOutputStream(outputFilePath);
        cryptingStream(inputFile, outputFile, cipher);
        outputFile.close();
    }


    public void decryptData(ByteArrayInputStream inputStream, FileOutputStream outputFile, Cipher cipher) throws IOException {
        cryptingStream(inputStream, outputFile, cipher);
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
