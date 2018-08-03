package com.concurrency.book.chapter05;

public class TryThreadLocal {
    public static class ThrLocalRunnable implements Runnable {

        private int start;
        private ThreadLocal<Integer> threadLocal;

        public ThrLocalRunnable(int start) {
            this.start = start;
            this.threadLocal = new ThreadLocal<Integer>();
        }

        @Override
        public void run() {
            this.threadLocal.set(start);

            for (int i = 0; i < 25; ++i) {

                final Integer v = threadLocal.get();
                System.out.println("Thread " + Thread.currentThread().getId() + ", value = " + v);

                threadLocal.set(v + 1);

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    // nothing
                }
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ThrLocalRunnable sharedRunnableInstance = new ThrLocalRunnable(6);

        Thread thread1 = new Thread(sharedRunnableInstance);
        Thread thread2 = new Thread(sharedRunnableInstance);

        thread1.start();
        thread2.start();

        thread1.join(); //wait for thread 1 to terminate
        thread2.join(); //wait for thread 2 to terminate
    }
}
