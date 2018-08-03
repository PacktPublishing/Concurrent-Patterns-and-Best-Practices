package com.concurrency.book.chapter04;

public class ThreadInterruption {
    public static void main(String[] args) throws InterruptedException {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(40000);
                } catch (InterruptedException e) {
                    System.out.println("I was woken up!!!");;
                }
                System.out.println("Am done");
            }
        };
        Thread t = new Thread(r);
        t.start();
        t.interrupt();
        Thread.sleep(1000);
    }
}
