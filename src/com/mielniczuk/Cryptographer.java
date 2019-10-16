package com.mielniczuk;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.NoSuchAlgorithmException;

public class Cryptographer {

    private SecretKey key;
    private InputStream input;
    private OutputStream outputStream;
    private Cipher cipher;

    public Cryptographer(SecretKey key, InputStream input, OutputStream outputStream) throws NoSuchPaddingException, NoSuchAlgorithmException {
        this.key = key;
        this.input = input;
        this.outputStream = outputStream;
        this.cipher = Cipher.getInstance("DESede");

    }


}
