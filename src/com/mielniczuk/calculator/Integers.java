package com.mielniczuk;

public class Integers {
    int add(int a, int b){
        return a+b;
    }

    int substract(int a, int b){
        return a-b;
    }

    int multiply(int a, int b){
        return a*b;
    }

    int divide(int a,int b){
        if(b == 0){
            throw new IllegalArgumentException("Argument 'divisor' is 0");
        }
        return a/b;
    }

    int factorial(int a){
        if(a == 0)
            return 1;
        return a*factorial(a-1);
    }

}
