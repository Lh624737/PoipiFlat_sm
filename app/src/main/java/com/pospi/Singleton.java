package com.pospi;

public class Singleton {
    private static Singleton instance = null;

    private Singleton() {
    }

    public static Singleton getInstance() {
        if (null == instance) {
            instance = new Singleton();
        }
        return instance;
    }

//    static {
//        instance = new Singleton();
//    }
//
//    private Singleton() {
//    }
//
//    public static Singleton getInstance() {
//        return instance;
//    }
}