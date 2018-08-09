package com.concurrency.book.chapter02;

import java.util.concurrent.atomic.AtomicInteger;

public class NoLocksCounter {
    private final AtomicInteger c = new AtomicInteger(0);

    public int getValue() {
        return c.get();
    }

    public int inc() {
        while (true) {
            int currVal = getValue();
            int newVal = currVal + 1;
            if (c.compareAndSet(currVal, newVal)) {
                return newVal;
            }
        }
    }

    public static void main(String[] args) {
        final NoLocksCounter counter = new NoLocksCounter();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10; ++i) {
                    int v = counter.inc(); // note
                    System.out.println("Thread "
                            + Thread.currentThread().getName()
                            + " set it to " + v
                            + " got " + counter.getValue()); // note the window
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        Thread t1 = new Thread(runnable);
        Thread t2 = new Thread(runnable);

        t1.start();
        t2.start();
    }
}
