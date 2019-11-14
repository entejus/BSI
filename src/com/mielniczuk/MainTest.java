package com.mielniczuk;

import junitparams.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import org.apache.commons.io.FileUtils;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;


@RunWith(JUnitParamsRunner.class)
public class MainTest {
    private static Main main;

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @BeforeClass
    public static void setUpClass() throws NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException {
        main = new Main();
    }


    @Test
    @Parameters({"R10IFbRyhvCF9hDvmd96LA==,Tekst,5dn5qv1L9Eg4f/lgWXt25Q==",
            "R10IFbRyhvCF9hDvmd96LA==,Lorem ipsum dolor sit amet consectetur adipiscing elit,Ks/09WIerhha+lvNaoAm7KUDYcguyRfa8ema9nCDDR5Nd858RpnfPXVrbDPnaSHj/EFtluCQTqNfhquAXuBn0g=="})
    public void encryptTextTest(String keyB64, String textToEncrypt, String expectedEncryptedText) throws BadPaddingException, IllegalBlockSizeException {
        //Setting key
        byte[] decodedKey = Base64.getDecoder().decode(keyB64);
        main.setKey(new SecretKeySpec(decodedKey, "AES"));

        byte[] encryptedBytes = main.encryptText(textToEncrypt);
        String encryptedText = Base64.getEncoder().encodeToString(encryptedBytes);
        assertEquals(encryptedText, expectedEncryptedText);
    }

    @Test
    @Parameters({"R10IFbRyhvCF9hDvmd96LA==,5dn5qv1L9Eg4f/lgWXt25Q==,Tekst",
            "R10IFbRyhvCF9hDvmd96LA==,Ks/09WIerhha+lvNaoAm7KUDYcguyRfa8ema9nCDDR5Nd858RpnfPXVrbDPnaSHj/EFtluCQTqNfhquAXuBn0g==,Lorem ipsum dolor sit amet consectetur adipiscing elit"})
    public void decryptTextTest(String keyB64, String encryptedBytes, String expectedDecryptedText) throws BadPaddingException, IllegalBlockSizeException {
        //Setting key
        byte[] decodedKey = Base64.getDecoder().decode(keyB64);
        main.setKey(new SecretKeySpec(decodedKey, "AES"));

        String decryptedText = main.decryptText(Base64.getDecoder().decode(encryptedBytes));
        assertEquals(decryptedText, expectedDecryptedText);
    }

    @Test
    @Parameters({"R10IFbRyhvCF9hDvmd96LA==,Tekst",
            "R10IFbRyhvCF9hDvmd96LA==,Lorem ipsum dolor sit amet consectetur adipiscing elit"})
    public void cryptingTextTest(String keyB64, String cryptedText) throws IOException, BadPaddingException, IllegalBlockSizeException {
        final File encryptedFile = folder.newFile("encryptedText.cfr");

        //Setting key
        byte[] decodedKey = Base64.getDecoder().decode(keyB64);
        main.setKey(new SecretKeySpec(decodedKey, "AES"));

        assertEquals(cryptedText,main.cryptingText(cryptedText,encryptedFile));

    }


    @Test
    @Parameters({"R10IFbRyhvCF9hDvmd96LA==,hello world,tuP5i9mDfZli04eCmayG/g=="})
    public void testEncryptData(String keyB64, String inputData,String expectedEncryptedData) throws IOException {
        final File inputFile = folder.newFile("input.txt");
        FileUtils.writeStringToFile(inputFile,inputData,"UTF-8");
        FileInputStream fileInputStream = new FileInputStream(inputFile);

        //Setting key
        byte[] decodedKey = Base64.getDecoder().decode(keyB64);
        main.setKey(new SecretKeySpec(decodedKey, "AES"));

        byte[] expected = Base64.getDecoder().decode(expectedEncryptedData);

        assertArrayEquals(expected,main.encryptData(fileInputStream));
    }

    @Test
    @Parameters({"R10IFbRyhvCF9hDvmd96LA==,tuP5i9mDfZli04eCmayG/g==,hello world"})
    public void testDecryptData(String keyB64, String inputData,String expectedOutput) throws IOException {
        final File encryptedFile = folder.newFile("encrypted.cfr");
        byte[] b = Base64.getDecoder().decode(inputData);
        FileUtils.writeByteArrayToFile(encryptedFile,b);
        FileInputStream encryptedFileInputStream = new FileInputStream(encryptedFile);

        //Setting key
        byte[] decodedKey = Base64.getDecoder().decode(keyB64);
        main.setKey(new SecretKeySpec(decodedKey, "AES"));

        String decryptedData = new String(main.decryptData(encryptedFileInputStream));

        assertEquals(expectedOutput,decryptedData);
    }

    @Test
    @Parameters({"R10IFbRyhvCF9hDvmd96LA==,Tekst",
            "R10IFbRyhvCF9hDvmd96LA==,Lorem ipsum dolor sit amet consectetur adipiscing elit"})
    public void cryptingFileTest(String keyB64, String cryptedFileContent) throws IOException, BadPaddingException, IllegalBlockSizeException {
        final File inputFile = folder.newFile("input.txt");
        final File encryptedFile = folder.newFile("encryptedFile.cfr");
        final File outputFile = folder.newFile("output.txt");

        FileUtils.writeStringToFile(inputFile,cryptedFileContent,"UTF-8");

        //Setting key
        byte[] decodedKey = Base64.getDecoder().decode(keyB64);
        main.setKey(new SecretKeySpec(decodedKey, "AES"));

        main.cryptingFile(inputFile,encryptedFile,outputFile);

        assertArrayEquals(FileUtils.readFileToByteArray(inputFile),FileUtils.readFileToByteArray(outputFile));
    }

    @Mock
    DBConnector mockedDbConnector;

    @Test
    @Parameters({"R10IFbRyhvCF9hDvmd96LA==,Tekst,5dn5qv1L9Eg4f/lgWXt25Q==",
            "R10IFbRyhvCF9hDvmd96LA==,Lorem ipsum dolor sit amet consectetur adipiscing elit"})
    public void cryptingDataDBTest(String keyB64, String cryptedFileContent) throws IOException, BadPaddingException, IllegalBlockSizeException {
        final File inputFile = folder.newFile("inputDB.txt");
        final File outputFile = folder.newFile("outputDB.txt");

        FileUtils.writeStringToFile(inputFile,cryptedFileContent,"UTF-8");
        doAnswer((i)-> {
            System.out.println("DB setData = " + i.getArgument(0));
            return null;}).when(mockedDbConnector).setData(any(byte[].class));

//        when(mockedDbConnector.getData()).thenReturn();
        //Setting key
        byte[] decodedKey = Base64.getDecoder().decode(keyB64);
        main.setKey(new SecretKeySpec(decodedKey, "AES"));

        main.cryptingDataDB(inputFile,outputFile);
        assertArrayEquals(FileUtils.readFileToByteArray(inputFile),FileUtils.readFileToByteArray(outputFile));
    }

}