package com.mielniczuk.calculator;

public class Real {
    double  add (double a, double b){
        return a+b;
    }
    double substract(double a, double b){
        return a-b;
    }
    double multiply(double a, double b){
        return a*b;
    }
    double divide(double a, double b){
        if(b==0){
            throw new IllegalArgumentException("Argument 'divisor' is 0");
        }
        return a/b;
    }
    double power(double a,double b){
        return Math.pow(a,b);
    }
    double squareRoot(double a){
        return Math.sqrt(a);
    }
}
