package com.mielniczuk.calculator;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.testng.annotations.BeforeClass;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(JUnitParamsRunner.class)
class IntegersTest {
    private static Integers integers;

    @BeforeClass
    public static void setIntegers() {
        integers = new Integers();
    }

    @Test
    @Parameters({"0,0,0","-5,2,-3","-3,-10,-13","50,74,124"})
    void add(int addend1, int addend2, int sum) {
        assertEquals(sum, integers.add(addend1, addend2));
    }

    @Test
    void substract() {
    }

    @Test
    void multiply() {
    }

    @Test
    void divide() {
    }

    @Test
    void factorial() {
    }
}