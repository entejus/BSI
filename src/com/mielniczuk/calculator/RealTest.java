package com.mielniczuk.calculator;

import junitparams.*;
import org.junit.Test;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

@RunWith(JUnitParamsRunner.class)
public class RealTest {

    private static Real real;
    @BeforeClass
    public static void setUp() {
        real = new Real();
    }

    @Test
    @Parameters({"0.0,0.0,0.0", "-5.2,2.3,-2.9", "-3.1,-10.0,-13.1", "50.1111,74.7777,124.8888"})
    public void add(double addend1, double addend2, double sum) {
        assertEquals(sum,real.add(addend1,addend2),0.001);
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
    public void power() {
    }

    @Test
    public void squareRoot() {
    }
}