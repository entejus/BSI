package com.mielniczuk;

import junitparams.*;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;


import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;

import static org.junit.Assert.*;

public class MainTest {
    private static Main main;
    @BeforeClass
    public static void setUpClass() throws NoSuchAlgorithmException {
        main = new Main();
    }


}