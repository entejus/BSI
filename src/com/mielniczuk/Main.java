package com.mielniczuk;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
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
    private static final String ALGORITHM = "AES";
    private static final String KEY_ALGORITHM = "AES";

    private SecretKey key;
    private DBConnector dbConnector;
    private Cipher encryptCipher;
    private Cipher decryptCipher;


    private JTextArea inputTextArea;
    private JTextArea decryptedTextArea;
    private JButton startButton;
    private JPanel mainJPanel;

    public SecretKey getKey() {
        return key;
    }

    public void setKey(SecretKey key) {
        this.key = key;
    }

    private static String bytesToHex(byte[] hashInBytes) {

        StringBuilder sb = new StringBuilder();
        for (byte b : hashInBytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();

    }

    public void encrypt() throws IOException, BadPaddingException, IllegalBlockSizeException {
        //Text encryption
        String inputText = inputTextArea.getText();
        FileOutputStream encryptedTextFile = new FileOutputStream(ENCRYPTED_TEXT_FILE_PATH);
        byte[] t = encryptText(inputText);
        encryptedTextFile.write(t);
        encryptedTextFile.close();

        //File encryption
        FileInputStream inputFile = new FileInputStream(FILE_PATH);
        FileOutputStream outputFile = new FileOutputStream(ENCRYPTED_FILE_PATH);
        outputFile.write(encryptData(inputFile));
        inputFile.close();
        outputFile.close();

        //Data encryption
        FileInputStream inputData = new FileInputStream(DATA_PATH);
        ByteArrayInputStream encryptedDataInput = new ByteArrayInputStream(encryptData(inputData));
        dbConnector.setData(encryptedDataInput);
        encryptedDataInput.close();
    }

    public byte[] encryptText(String inputText) throws BadPaddingException, IllegalBlockSizeException {
        byte[] inputBytes = inputText.getBytes();
        return encryptCipher.doFinal(inputBytes);
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
        outStream.close();
        return outStream.toByteArray();
    }


    public void decrypt() throws IOException, BadPaddingException, IllegalBlockSizeException {
        //Text decryption
        FileInputStream inputTextFile = new FileInputStream(ENCRYPTED_TEXT_FILE_PATH);
        String decryptedText = decryptText(inputTextFile.readAllBytes());
        decryptedTextArea.setText(decryptedText);
        inputTextFile.close();

        //File decryption
        FileInputStream inputFile = new FileInputStream(ENCRYPTED_FILE_PATH);
        FileOutputStream outputFile = new FileOutputStream(DECRYPTED_FILE_PATH);
        outputFile.write(decryptData(inputFile));
        inputFile.close();
        outputFile.close();

        //Data decryption
        FileOutputStream outputData = new FileOutputStream(DECRYPTED_DATA_PATH);
        ByteArrayInputStream encryptedDataInput = dbConnector.getData();
        outputData.write(decryptData(encryptedDataInput));
        encryptedDataInput.close();
        outputData.close();

    }

    public String decryptText(byte[] encryptedBytes) throws BadPaddingException, IllegalBlockSizeException {
        return new String(decryptCipher.doFinal(encryptedBytes));
    }


    public byte[] decryptData(InputStream inputStream) throws IOException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        CipherOutputStream cipherOut = new CipherOutputStream(outStream, decryptCipher);
        byte[] buffer = new byte[2048];
        int readBytes;
        while ((readBytes = inputStream.read(buffer)) != -1) {
            cipherOut.write(buffer, 0, readBytes);
        }
        cipherOut.close();
        outStream.close();
        return outStream.toByteArray();
    }


    public Main() throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
        KeyGenerator keygen = KeyGenerator.getInstance(KEY_ALGORITHM);
//        key = keygen.generateKey();
        byte[] decodedKey = Base64.getDecoder().decode("R10IFbRyhvCF9hDvmd96LA==");
        key = new SecretKeySpec(decodedKey, KEY_ALGORITHM);
        String encodedKey = Base64.getEncoder().encodeToString(key.getEncoded());
        decryptCipher = Cipher.getInstance(ALGORITHM);
        decryptCipher.init(Cipher.DECRYPT_MODE, key);

        encryptCipher = Cipher.getInstance(ALGORITHM);
        encryptCipher.init(Cipher.ENCRYPT_MODE, key);

        startButton.addActionListener(e -> {
            try {
                dbConnector = new DBConnector();
                encrypt();
                decrypt();
                System.out.println("Key: "+encodedKey);
                System.out.println("Key[HEX]: "+bytesToHex(key.getEncoded()));
                dbConnector.close();
            } catch (IOException | BadPaddingException | IllegalBlockSizeException ex) {
                ex.printStackTrace();
            }
        });
    }

    public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException {
        JFrame frame = new JFrame("Cryptography");
        frame.setContentPane(new Main().mainJPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setResizable(true);
    }
}
