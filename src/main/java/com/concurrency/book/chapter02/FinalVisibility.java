package com.concurrency.book.chapter02;

public class FinalVisibility {
    final int a;
    int b;
    private static FinalVisibility fv;

    public FinalVisibility() {
        a = 0;
        b = 1;
    }

    public static void main(String[] args) {
        extractVal();
        getVal();
    }

    private static void extractVal() {
        if (fv != null) {
            int aa = fv.a;
            int bb = fv.b;
        }
    }

    public static FinalVisibility getVal() {
        fv = new FinalVisibility();
        return fv;
    }
}
