package com.concurrency.book.chapter03;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class AppCyclicBarrier {
    public static void main(String[] args) throws BrokenBarrierException, InterruptedException {
        Runnable barrierAction = new Runnable() {
            public void run() {
                System.out.println("BarrierAction 1 executed ");
            }
        };

        CyclicBarrier barrier = new CyclicBarrier(3, barrierAction);

        Runnable w1 = createWorker(barrier);
        Runnable w2 = createWorker(barrier);

        new Thread(w1).start();
        new Thread(w2).start();

        barrier.await();
        System.out.println("Done");
    }

    private static Runnable createWorker(final CyclicBarrier barrier) {
        return new Runnable() {

            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    System.out.println("Waiting at barrier");
                    try {
                        barrier.await();
                    } catch (BrokenBarrierException e) {
                        e.printStackTrace();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        };
    }
}
