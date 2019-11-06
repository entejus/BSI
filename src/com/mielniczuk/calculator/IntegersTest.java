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
    public void multiply() {
    }

    @Test
    public void divide() {
    }

    @Test
    public void factorial() {
    }
}