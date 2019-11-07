package com.mielniczuk.calculator;

public class Integers {
    int add(int addend1, int addend2){
        return addend1+addend2;
    }

    int subtract(int minuend, int subtrahend){
        return minuend-subtrahend;
    }

    int multiply(int factor1, int factor2){
        return factor1*factor2;
    }

    int divide(int dividend,int divisor){
        if(divisor == 0){
            throw new IllegalArgumentException("Argument 'divisor' is 0");
        }
        return dividend/divisor;
    }

    int factorial(int a){
        if(a == 0)
            return 1;
        return a*factorial(a-1);
    }

}
