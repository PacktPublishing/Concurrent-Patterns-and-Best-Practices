package com.concurrency.book.chapter03;

public class BrittleBuffer2 extends Buffer {
    public BrittleBuffer2(int capacity) {
        super(capacity);
    }

    public void put(Integer v) throws InterruptedException {
        while (true) {
            synchronized (this) {
                if (!isBufFull()) {
                    putElem(v);
                    return;
                }
            }
            Thread.sleep(1000);
        }
    }

    public Integer get() throws InterruptedException {
        while (true) {
            synchronized (this) {
                if (!isBufEmpty())
                    return getElem();
            }
            Thread.sleep(1000);
        }
    }

    public static void main(String[] args) {
        final BrittleBuffer2 buffer = new BrittleBuffer2(5);
        Thread producer = new Thread(new Runnable() {
            @Override
            public void run() {
                for( int i = 0; i < 50; ++ i) {
                    System.out.println("putting elem " + i);
                    try {
                        buffer.put(i);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        Thread consumer = new Thread(new Runnable() {
            @Override
            public void run() {
                for( int i = 0; i < 50; ++ i) {
                    try {
                        System.out.println("Getting elem " + buffer.get());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        producer.start();
        consumer.start();
    }
}
