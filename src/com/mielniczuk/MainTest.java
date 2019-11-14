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
import javax.sql.DataSource;

import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.Base64;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;


@RunWith(JUnitParamsRunner.class)
public class MainTest {
    private static Main main;

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Mock
    private DBConnector mockDbConnector;
    @Mock
    private DataSource mockDataSource;
    @Mock
    private Connection mockConnection;
    @Mock
    private Statement mockStatement;
    @Mock
    private PreparedStatement mockPreparedStatement;
    @Mock
    private ResultSet mockResultSet;


    @Before
    public void setUp() throws SQLException {
        MockitoAnnotations.initMocks(this);

        assertNotNull(mockDataSource);

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


    @Test
    @Parameters({"R10IFbRyhvCF9hDvmd96LA==,Tekst,5dn5qv1L9Eg4f/lgWXt25Q==",
            "R10IFbRyhvCF9hDvmd96LA==,Lorem ipsum dolor sit amet consectetur adipiscing elit,Ks/09WIerhha+lvNaoAm7KUDYcguyRfa8ema9nCDDR5Nd858RpnfPXVrbDPnaSHj/EFtluCQTqNfhquAXuBn0g=="})
    public void cryptingDataDBTest(String keyB64, String cryptedFileContent, String encryptedFileData) throws IOException , SQLException {
        final File inputFile = folder.newFile("inputDB.txt");
        final File outputFile = folder.newFile("outputDB.txt");

        FileUtils.writeStringToFile(inputFile,cryptedFileContent,"UTF-8");


        when(mockDataSource.getConnection()).thenReturn(mockConnection);

        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockConnection.prepareStatement(any(String.class))).thenReturn(mockPreparedStatement);
        doAnswer((i)->true).when(mockConnection).close();

        when(mockStatement.executeQuery(anyString())).thenReturn(mockResultSet);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);
        doAnswer((i)->true).when(mockPreparedStatement).close();

        when(mockResultSet.first()).thenReturn(true);
        doAnswer((i)->true).when(mockResultSet).close();

        byte[] encryptedFileDataBytes =Base64.getDecoder().decode(encryptedFileData);
        when(mockResultSet.getBytes(anyString())).thenReturn(encryptedFileDataBytes);
        doAnswer((i)->1).when(mockPreparedStatement).setBytes(anyInt(),any(byte[].class));
        doAnswer((i)-> 1).when(mockDbConnector).setData(any(byte[].class));
        when(mockDbConnector.getData()).thenReturn(encryptedFileDataBytes);


        //Setting key
        byte[] decodedKey = Base64.getDecoder().decode(keyB64);
        main.setKey(new SecretKeySpec(decodedKey, "AES"));

        main.cryptingDataDB(mockDbConnector,inputFile,outputFile);
        assertArrayEquals(FileUtils.readFileToByteArray(inputFile),FileUtils.readFileToByteArray(outputFile));
    }

}