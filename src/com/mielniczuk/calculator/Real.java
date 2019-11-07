package com.mielniczuk.calculator;

public class Real {
    double  add (double addend1, double addend2){
        return addend1+addend2;
    }
    double subtract(double minuend, double subtrahend){
        return minuend-subtrahend;
    }
    double multiply(double factor1, double factor2){
        return factor1*factor2;
    }
    double divide(double dividend, double divisor){
        if(divisor==0){
            throw new IllegalArgumentException("Argument 'divisor' is 0");
        }
        return dividend/divisor;
    }
    double power(double base,double exponent){
        return Math.pow(base,exponent);
    }
    double squareRoot(double number){
        return Math.sqrt(number);
    }
}
