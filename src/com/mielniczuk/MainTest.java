package com.mielniczuk;

import junitparams.*;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;


import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import static org.junit.Assert.*;

@RunWith(JUnitParamsRunner.class)
public class MainTest {
    private static Main main;
    @BeforeClass
    public static void setUpClass() throws NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException {
        main = new Main();
    }


    @Test
    @Parameters({"R10IFbRyhvCF9hDvmd96LA==,Tekst,5dn5qv1L9Eg4f/lgWXt25Q==",
            "R10IFbRyhvCF9hDvmd96LA==,Lorem ipsum dolor sit amet consectetur adipiscing elit,Ks/09WIerhha+lvNaoAm7KUDYcguyRfa8ema9nCDDR5Nd858RpnfPXVrbDPnaSHj/EFtluCQTqNfhquAXuBn0g=="})
    public void encryptTextTest(String key,String textToEncrypt,String expectedEncryptedText){
        byte[] decodedKey = Base64.getDecoder().decode(key);
        main.setKey(new SecretKeySpec(decodedKey, "AES"));

        try {
            byte[] encryptedBytes = main.encryptText(textToEncrypt);
            String encryptedText = Base64.getEncoder().encodeToString(encryptedBytes);
            assertEquals(encryptedText,expectedEncryptedText);
        } catch (BadPaddingException | IllegalBlockSizeException e) {
            e.printStackTrace();
        }

    }

    @Test
    @Parameters({"R10IFbRyhvCF9hDvmd96LA==,5dn5qv1L9Eg4f/lgWXt25Q==,Tekst",
            "R10IFbRyhvCF9hDvmd96LA==,Ks/09WIerhha+lvNaoAm7KUDYcguyRfa8ema9nCDDR5Nd858RpnfPXVrbDPnaSHj/EFtluCQTqNfhquAXuBn0g==,Lorem ipsum dolor sit amet consectetur adipiscing elit"})
    public void decryptTextTest(String key,String encryptedBytes,String expectedDecryptedText){
        byte[] decodedKey = Base64.getDecoder().decode(key);
        main.setKey(new SecretKeySpec(decodedKey, "AES"));

        try {
            String decryptedText = main.decryptText(Base64.getDecoder().decode(encryptedBytes));
            assertEquals(decryptedText,expectedDecryptedText);
        } catch (BadPaddingException | IllegalBlockSizeException e) {
            e.printStackTrace();
        }

    }




}