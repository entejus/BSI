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
    private byte[] encryptedTextBytes;
    private DBConnector dbConnector;


    private static JFrame frame;
    private JTextArea inputTextArea;
    private JTextArea encryptedTextArea;
    private JTextArea decryptedTextArea;
    private JButton startButton;
    private JPanel mainJPanel;

    private void encrypt() throws IOException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        String inputText = inputTextArea.getText();
        FileInputStream inputFile = new FileInputStream(FILE_PATH);
        FileInputStream inputData = new FileInputStream(DATA_PATH);

        Cipher cipher = Cipher.getInstance("DESede");
        cipher.init(Cipher.ENCRYPT_MODE, key);


        String encryptedText = encryptText(inputText,cipher);
        encryptedTextArea.setText(encryptedText);

        encryptFile(inputFile,ENCRYPTED_FILE_PATH,cipher);

        ByteArrayOutputStream encryptedDataOutput = (ByteArrayOutputStream) encryptData(inputData,cipher);
        ByteArrayInputStream encryptedDataInput = new ByteArrayInputStream(encryptedDataOutput.toByteArray());
        dbConnector.setData(encryptedDataInput);

        inputFile.close();
        encryptedDataOutput.close();
        encryptedDataInput.close();
    }

    private String encryptText(String inputText, Cipher cipher) throws BadPaddingException, IllegalBlockSizeException {
        encryptedTextBytes = cipher.doFinal(inputText.getBytes());
        return new String(encryptedTextBytes);
    }

    private void encryptFile(FileInputStream inputFile,String outputFilePath,Cipher cipher) throws IOException {
        FileOutputStream outputFile = new FileOutputStream(outputFilePath);
        encryptStream(inputFile,outputFile,cipher);
        outputFile.close();
    }

    private OutputStream encryptData(FileInputStream inputFile,Cipher cipher) throws IOException {
        OutputStream outStream = new ByteArrayOutputStream();
        encryptStream(inputFile,outStream,cipher);
        return outStream;
    }
    private void encryptStream(InputStream inputStream, OutputStream outputStream,Cipher cipher) throws IOException {
        CipherOutputStream cipherOut = new CipherOutputStream(outputStream, cipher);
        byte[] buffer = new byte[2048];
        int readBytes;
        while ((readBytes = inputStream.read(buffer)) != -1) {
            cipherOut.write(buffer, 0, readBytes);
        }
        cipherOut.close();
    }


    private void decrypt() throws IOException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        String encryptedText = encryptedTextArea.getText();
        FileInputStream inputFile = new FileInputStream(ENCRYPTED_FILE_PATH);
        FileOutputStream outputData = new FileOutputStream(DECRYPTED_DATA_PATH);

        Cipher cipher = Cipher.getInstance("DESede");
        cipher.init(Cipher.DECRYPT_MODE, key);

        String decryptedText = decryptText(cipher);
        decryptedTextArea.setText(decryptedText);

        decryptFile(inputFile, DECRYPTED_FILE_PATH,cipher);

        ByteArrayInputStream i = dbConnector.getData();
        decryptData(i, outputData,cipher);

        inputFile.close();
        outputData.close();
        i.close();
    }

    private String decryptText(Cipher cipher) throws BadPaddingException, IllegalBlockSizeException {
        return new String(cipher.doFinal(encryptedTextBytes));
    }

    private void decryptFile(FileInputStream inputFile, String outputFilePath,Cipher cipher) throws IOException {
        FileOutputStream outputFile = new FileOutputStream(outputFilePath);
        decryptStream(inputFile,outputFile,cipher);
        outputFile.close();
    }


    private void decryptData(ByteArrayInputStream inputStream, FileOutputStream outputFile, Cipher cipher) throws IOException {
        decryptStream(inputStream,outputFile,cipher);
    }

    private void decryptStream(InputStream inputStream, OutputStream outputStream,Cipher cipher) throws IOException {
        CipherOutputStream cipherOut = new CipherOutputStream(outputStream, cipher);
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
                dbConnector.close();
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
