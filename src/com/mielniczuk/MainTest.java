package com.mielniczuk;

import org.junit.jupiter.api.Test;
import org.testng.annotations.BeforeClass;

import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {
    private static Main mainClass;
    @BeforeClass
    public static void setUpClass() throws NoSuchAlgorithmException {
        mainClass = new Main();
    }

    @Test
    public void testEncryptText() {

    }
}