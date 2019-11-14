package com.mielniczuk;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

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



    public byte[] encryptText(String inputText) throws BadPaddingException, IllegalBlockSizeException {
        byte[] inputBytes = inputText.getBytes();
        return encryptCipher.doFinal(inputBytes);
    }


    public String decryptText(byte[] encryptedBytes) throws BadPaddingException, IllegalBlockSizeException {
        return new String(decryptCipher.doFinal(encryptedBytes));
    }


    public byte[] encryptData(InputStream inputFile) throws IOException {
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

    public String cryptingText(String inputText, File encryptedTextFile) throws IOException, BadPaddingException, IllegalBlockSizeException {
        //Encrypting
        FileOutputStream encryptedTextFileOS = new FileOutputStream(encryptedTextFile);
        byte[] t = encryptText(inputText);
        encryptedTextFileOS.write(t);
        encryptedTextFileOS.close();

        //Decrypting
        FileInputStream encryptedTextFileIS = new FileInputStream(encryptedTextFile);
        String decryptedText = decryptText(encryptedTextFileIS.readAllBytes());
        encryptedTextFileIS.close();
        return decryptedText;
    }

    public void cryptingFile(File inputFile, File encryptedFile, File outputFile) throws IOException {
        //Encrypting
        FileInputStream inputFileIS = new FileInputStream(inputFile);
        FileOutputStream encryptedFileOS = new FileOutputStream(encryptedFile);
        encryptedFileOS.write(encryptData(inputFileIS));
        inputFileIS.close();
        encryptedFileOS.close();

        //Decrypting
        FileInputStream encryptedFileIS = new FileInputStream(encryptedFile);
        FileOutputStream outputFileOS = new FileOutputStream(outputFile);
        outputFileOS.write(decryptData(encryptedFileIS));
        encryptedFileIS.close();
        outputFileOS.close();
    }

    public void cryptingDataDB(DBConnector tmpdbConnector,File inputFile, File outputFile) throws IOException {
        //Ecnrypting
        FileInputStream inputDataIS = new FileInputStream(inputFile);
        byte[] encryptedDataInput = encryptData(inputDataIS);
        tmpdbConnector.setData(encryptedDataInput);

        //Decrypting
        FileOutputStream outputDataOS = new FileOutputStream(outputFile);
        byte[] encryptedDataInputDB = tmpdbConnector.getData();
        outputDataOS.write(decryptData(new ByteArrayInputStream(encryptedDataInputDB)));
        outputDataOS.close();
    }





    public Main() throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
        KeyGenerator keygen = KeyGenerator.getInstance(KEY_ALGORITHM);
//        key = keygen.generateKey();
        byte[] decodedKey = Base64.getDecoder().decode("R10IFbRyhvCF9hDvmd96LA==");
        key = new SecretKeySpec(decodedKey, KEY_ALGORITHM);
        String encodedKey = Base64.getEncoder().encodeToString(key.getEncoded());

        //Encrypting cipher
        encryptCipher = Cipher.getInstance(ALGORITHM);
        encryptCipher.init(Cipher.ENCRYPT_MODE, key);

        //Decrypting cipher
        decryptCipher = Cipher.getInstance(ALGORITHM);
        decryptCipher.init(Cipher.DECRYPT_MODE, key);


        //Text cryptography
        File encryptedTextFile = new File(ENCRYPTED_TEXT_FILE_PATH);

        //File cryptogrtaphy
        File inputFile = new File(FILE_PATH);
        File encryptedFile = new File(ENCRYPTED_FILE_PATH);
        File outputFile = new File(DECRYPTED_FILE_PATH);

        //Data DB cryptography
        File inputDataFile = new File(DATA_PATH);
        File outputDataFile = new File(DECRYPTED_DATA_PATH);


        startButton.addActionListener(e -> {
            try {
                String inputText = inputTextArea.getText();
                decryptedTextArea.setText(cryptingText(inputText,encryptedTextFile));


                cryptingFile(inputFile,encryptedFile,outputFile);

                MysqlDataSource dataSource = new MysqlDataSource();
                dataSource.setServerName("localhost");
                dataSource.setPortNumber(3306);
                dataSource.setDatabaseName("bsi");
                dataSource.setUser("root");
                dataSource.setPassword("password");
                dbConnector = new DBConnector(dataSource);
                cryptingDataDB(dbConnector,inputDataFile,outputDataFile);
                dbConnector.close();

                System.out.println("Key: "+encodedKey);
                System.out.println("Key[HEX]: "+bytesToHex(key.getEncoded()));
            } catch (IOException | BadPaddingException | IllegalBlockSizeException ex) {
                ex.printStackTrace();
            } finally {
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
