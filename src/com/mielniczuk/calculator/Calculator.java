package com.mielniczuk.calculator;

public class Calculator {
    private static void integerMethods(Integers integers, int a, int b){
        System.out.println("Dodawanie: "+integers.add(a,b));
        System.out.println("Odejmowanie: "+integers.subtract(a,b));
        System.out.println("Mnożenie: "+integers.multiply(a,b));
        System.out.println("Dzielenie: "+integers.divide(a,b));
        System.out.println("Silnia: "+integers.factorial(a));
    }


    private static void realMethods(Real real, double a,double b){
        System.out.println("Dodawanie: "+real.add(a,b));
        System.out.println("Odejmowanie: "+real.subtract(a,b));
        System.out.println("Mnożenie: "+real.multiply(a,b));
        System.out.println("Dzielenie: "+real.divide(a,b));
        System.out.println("Potęgowanie: "+real.power(a,b));
        System.out.println("Pierwiastkowanie: "+real.squareRoot(a));
    }

    public static void main(String[] args) {
        Integers integers = new Integers();
        Real real = new Real();

        integerMethods(integers,5,2);
        System.out.println();
        realMethods(real,-2,-2.5);

    }
}
