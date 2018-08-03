package com.concurrency.book.chapter03;

public class AppCountDownLatch {
    public static void main(String[] args) throws InterruptedException {
        MyCountDownLatch latch = new MyCountDownLatch(3);

        Runnable w1 = createWorker(3000, latch, "W1");
        Runnable w2 = createWorker(2000, latch, "W2");
        Runnable w3 = createWorker(1000, latch, "W3");

        new Thread(w1).start();
        new Thread(w1).start();
        new Thread(w1).start();

        latch.await();

        System.out.println("We are done");
    }

    private static Runnable createWorker(final int delay, final MyCountDownLatch latch, String w1) {
        return new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(delay);
                    latch.countDown();
                    System.out.println(Thread.currentThread().getName()
                            + " done with processing");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
    }

}

