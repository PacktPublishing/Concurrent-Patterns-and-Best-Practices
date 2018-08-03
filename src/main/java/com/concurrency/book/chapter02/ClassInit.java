package com.concurrency.book.chapter02;


public class ClassInit {

    private ClassInit() {
        System.out.println("ClassInit initializing");
    }

    private static class Inner {
        private static final ClassInit INSTANCE = new ClassInit();
    }

    public static ClassInit getInner() {
        return Inner.INSTANCE;
    }

    public static void main(String[] args) {
        System.out.println("This first");
        System.out.println(getInner().msg());
    }

    private String msg() {
        return "Hello there";
    }


}
