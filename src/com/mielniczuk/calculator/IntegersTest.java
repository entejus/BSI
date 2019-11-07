package com.mielniczuk.calculator;

import junitparams.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.testng.annotations.BeforeClass;

import static org.junit.Assert.*;

@RunWith(JUnitParamsRunner.class)
public class IntegersTest {
    public  Integers integers = new Integers();

    @Test
    @Parameters({"0,0,0", "-5,2,-3", "-3,-10,-13", "50,74,124"})
    public void add(int addend1, int addend2, int sum) {
        assertEquals(sum, integers.add(addend1, addend2));
    }

    @Test
    @Parameters({"0,0,0", "-5,2,-7", "-3,-10,7", "50,74,-24","1034,-66,1100"})
    public void subtract(int minuend, int subtrahend, int difference) {
        assertEquals(difference,integers.subtract(minuend,subtrahend));
    }

    @Test
    @Parameters({"0,0,0", "-5,2,-10", "-3,-10,30", "50,74,3700","1034,0,0"})
    public void multiply(int factor1,int factor2,int product) {
        assertEquals(product,integers.multiply(factor1,factor2));
    }

    @Test
    @Parameters({"0,12,0", "-5,2,-2", "182,2,91", "1024,-16,-64","1034,-66,-15"})
    public void divide(int dividend, int divisor,int quotient) {
        assertEquals(quotient,integers.divide(dividend,divisor));
    }

    @Test
    public void factorial() {
    }
}