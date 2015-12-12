package com.example.kostya.test;

/**
 * Created by Kostya on 26.05.2015.
 */
public class test {
    public static boolean checkString(String string) {
        if (string == null) return false;
        return string.matches("^-?\\d+$");
    }

    public static void main(String[] args) {
        System.out.println(test.checkString("asdasd 123"));
        System.out.println(test.checkString("2154458"));
    }
}
