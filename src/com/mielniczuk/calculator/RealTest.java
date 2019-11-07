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
    @Parameters({"0.0,0.0,0.0", "-5.2,2.3,-2.9", "-3.1,-10.0,-13.1",
            "50.1111,74.7777,124.8888"})
    public void add(double addend1, double addend2, double sum) {
        assertEquals(sum,real.add(addend1,addend2),0.001);
    }

    @Test
    @Parameters({"0.0,0.0,0.0", "-5.2,2.3,-7.5", "-3.5,-10.3,6.8",
            "50.1111,74.7777,-24.6666","1033.6666,-66.3333,1099.9999"})
    public void subtract(double minuend, double subtrahend, double difference) {
        assertEquals(difference,real.subtract(minuend,subtrahend),0.001);
    }

    @Test
    @Parameters({"0.0,0.0,0.0", "-5.2,2.3,-11.96", "-3.5,-10.3,36.05",
            "50.1111,74.7777,3747.193","1034.1231,0.0,0.0"})
    public void multiply(double factor1,double factor2,double product) {
        assertEquals(product,real.multiply(factor1,factor2),0.01);
    }

    @Test
    @Parameters({"0.0,12.0,0.0", "-5.2,2.3,-2.26", "182.88,2.0,91.44",
            "1024.1024,-16.16,-63.372","1034.0,-66.0,-15.66"})
    public void divide(double dividend, double divisor,double quotient) {
        assertEquals(quotient,real.divide(dividend,divisor),0.01);
    }

    @Test
    @Parameters({"0.0,12.0,0.0", "-5.2,2.3,-2.26", "182.88,2.0,91.44",
            "1024.1024,-16.16,-63.372","1034.0,-66.0,-15.66"})
    public void power(double base,double exponent,double result) {
        assertEquals(result,real.power(base,exponent),0.01);
    }

    @Test
    public void squareRoot() {
    }
}