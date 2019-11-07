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
        if(base<0 && exponent == Math.floor(exponent)){
            throw new IllegalArgumentException("Argument 'base' is less than 0 and 'exponent' is'n integer");
        }
        if(base == 0 && exponent < 0){
            throw new IllegalArgumentException("Argument 'base' is 0 and 'exponent' is less then 0");
        }
        return Math.pow(base,exponent);
    }
    double squareRoot(double number){
        return Math.sqrt(number);
    }
}
