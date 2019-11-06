package com.mielniczuk;

public class Calculator {
    private static void integerMethods(Integers integers, int a, int b){
        System.out.println("Dodawanie: "+integers.add(a,b));
        System.out.println("Odejmowanie: "+integers.substract(a,b));
        System.out.println("Mnożenie: "+integers.multiply(a,b));
        System.out.println("Dzielenie: "+integers.divide(a,b));
        System.out.println("Silnia: "+integers.factorial(a));
    }


    public static void main(String[] args) {
        Integers integers = new Integers();
        Real real = new Real();

        integerMethods(integers,5,2);

    }
}
