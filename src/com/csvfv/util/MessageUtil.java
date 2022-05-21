package com.csvfv.util;

public class MessageUtil {

    public static String ErrorNotValid(String name) {
        return name + " TIDAK VALID";
    }
    public static String ErrorWithMessage(String name, String message) {
        return name + " " +  message;
    }
    public static String NotEmptyMessage(String name) {
        return name + " HARUS DIISI";
    }
}
