package com.concurrency.book.chapter02;

public class FinalVisibility {
    final int a;
    int b;
    private static FinalVisibility fv;

    public FinalVisibility() {
        a = 0;
        b = 1;
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

    public static void main(String[] args) {

        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                FinalVisibility.extractVal();
                final FinalVisibility v = FinalVisibility.getVal();
                System.out.println("a = " + v.a + " b = " + v.b); 
            }
        };

        Thread t1 = new Thread(runnable);
        Thread t2 = new Thread(runnable);

        t1.start();
        t2.start();
    }
}
