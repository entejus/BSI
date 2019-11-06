package com.mielniczuk.calculator;

import junitparams.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.testng.annotations.BeforeClass;

import static org.junit.Assert.*;

@RunWith(JUnitParamsRunner.class)
public class IntegersTest {
    public static Integers integers;

    @BeforeClass
    public static void setIntegers() {
        integers = new Integers();
    }

    @Test
    @Parameters({"0,0,0", "-5,2,-3", "-3,-10,-13", "50,74,124"})
    public void add(int addend1, int addend2, int sum) {
        integers = new Integers();
        assertEquals(sum, integers.add(addend1, addend2));
    }

    @Test
    public void substract() {
    }

    @Test
    public void multiply() {
    }

    @Test
    public void divide() {
    }

    @Test
    public void factorial() {
    }
}