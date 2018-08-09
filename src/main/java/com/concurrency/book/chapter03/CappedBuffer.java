package com.concurrency.book.chapter03;

public class CappedBuffer extends Buffer {
    public CappedBuffer(int capacity) {
        super(capacity);
    }

    public synchronized void put(Integer v) throws InterruptedException {
        while (isBufFull()) {
            wait();
        }
        putElem(v);
        notifyAll();
    }

    public synchronized Integer get() throws InterruptedException {
        while (isBufEmpty()) {
            wait();
        }
        Integer elem = getElem();
        notifyAll();
        return elem;
    }

    public static void main(String[] args) {
        final CappedBuffer buffer = new CappedBuffer(5);
        Thread producer = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 50; ++i) {
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
                for (int i = 0; i < 50; ++i) {
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
