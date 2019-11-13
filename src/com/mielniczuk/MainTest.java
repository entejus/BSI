package com.mielniczuk;

import junitparams.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.apache.commons.io.FileUtils;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import static org.junit.Assert.*;


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
    public void encryptTextTest(String key, String textToEncrypt, String expectedEncryptedText) throws BadPaddingException, IllegalBlockSizeException {
        byte[] decodedKey = Base64.getDecoder().decode(key);
        main.setKey(new SecretKeySpec(decodedKey, "AES"));
        byte[] encryptedBytes = main.encryptText(textToEncrypt);
        String encryptedText = Base64.getEncoder().encodeToString(encryptedBytes);
        assertEquals(encryptedText, expectedEncryptedText);
    }

    @Test
    @Parameters({"R10IFbRyhvCF9hDvmd96LA==,5dn5qv1L9Eg4f/lgWXt25Q==,Tekst",
            "R10IFbRyhvCF9hDvmd96LA==,Ks/09WIerhha+lvNaoAm7KUDYcguyRfa8ema9nCDDR5Nd858RpnfPXVrbDPnaSHj/EFtluCQTqNfhquAXuBn0g==,Lorem ipsum dolor sit amet consectetur adipiscing elit"})
    public void decryptTextTest(String key, String encryptedBytes, String expectedDecryptedText) throws BadPaddingException, IllegalBlockSizeException {
        byte[] decodedKey = Base64.getDecoder().decode(key);
        main.setKey(new SecretKeySpec(decodedKey, "AES"));
        String decryptedText = main.decryptText(Base64.getDecoder().decode(encryptedBytes));
        assertEquals(decryptedText, expectedDecryptedText);
    }


    @Test
    @Parameters({"R10IFbRyhvCF9hDvmd96LA==,hello world,tuP5i9mDfZli04eCmayG/g=="})
    public void testEncryptData(String key, String inputData,String expectedEncryptedData) throws IOException {
        final File inputFile = folder.newFile("input.txt");
        FileUtils.writeStringToFile(inputFile,inputData,"UTF-8");
        FileInputStream fileInputStream = new FileInputStream(inputFile);
        byte[] decodedKey = Base64.getDecoder().decode(key);
        main.setKey(new SecretKeySpec(decodedKey, "AES"));

        byte[] expected = Base64.getDecoder().decode(expectedEncryptedData);

        assertArrayEquals(expected,main.encryptData(fileInputStream));
    }

    @Test
    @Parameters({"R10IFbRyhvCF9hDvmd96LA==,tuP5i9mDfZli04eCmayG/g==,hello world"})
    public void testDecryptData(String key, String inputData,String expectedOutput) throws IOException {
        final File inputFile = folder.newFile("encrypted.cfr");
        byte[] b = Base64.getDecoder().decode(inputData);
        FileUtils.writeByteArrayToFile(inputFile,b);
        FileInputStream fileInputStream = new FileInputStream(inputFile);
        byte[] decodedKey = Base64.getDecoder().decode(key);
        main.setKey(new SecretKeySpec(decodedKey, "AES"));

        String decryptedData = new String(main.decryptData(fileInputStream));

        assertEquals(expectedOutput,decryptedData);
    }
//
//    @Test
//    @Parameters({"R10IFbRyhvCF9hDvmd96LA==,hello world"})
//    public void testFileCrypting(String key, String inputData) throws IOException {
//        final File inputFile = folder.newFile("input.txt");
//        BufferedWriter bw = new BufferedWriter(new FileWriter(inputFile));
//        bw.write(inputData);
//        final File encryptedFile =  folder.newFile("inputENC.cfr");
//        final File decryptedFile =  folder.newFile("inputDEC.txt");
//
//
//        byte[] decodedKey = Base64.getDecoder().decode(key);
//        main.setKey(new SecretKeySpec(decodedKey, "AES"));
//
//        FileInputStream fileInputStream = new FileInputStream(inputFile);
//        FileOutputStream encryptedFileOutputStream = new FileOutputStream(encryptedFile);
//        encryptedFileOutputStream.write(main.encryptData(fileInputStream));
//        encryptedFileOutputStream.close();
//
//        FileInputStream encryptedFileInputStream = new FileInputStream(encryptedFile);
//        FileOutputStream decryptedFileOutputStream = new FileOutputStream(decryptedFile);
//        decryptedFileOutputStream.write(main.decryptData(encryptedFileInputStream));
//        decryptedFileOutputStream.close();
//
//        FileInputStream decryptedFileInputStream = new FileInputStream(decryptedFile);
//
//        assertArrayEquals(fileInputStream.readAllBytes(),decryptedFileInputStream.readAllBytes());
//    }


}