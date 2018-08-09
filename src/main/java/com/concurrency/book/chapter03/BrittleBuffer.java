package com.concurrency.book.chapter03;

public class BrittleBuffer extends Buffer {
    public BrittleBuffer(int capacity) {
        super(capacity);
    }

    public synchronized void put(Integer v) throws BufFullException {
        if (isBufFull())
            throw new BufFullException();
        putElem(v);
    }

    public synchronized Integer get() throws BufEmptyException {
        if (isBufEmpty())
            throw new BufEmptyException();
        return getElem();
    }

    public static void main(String[] args) {
        final BrittleBuffer buffer = new BrittleBuffer(5);
        Thread producer = new Thread(new Runnable() {
            @Override
            public void run() {
                for( int i = 0; i < 50; ++ i) {
                    System.out.println("putting elem " + i);
                    buffer.put(i);
                }
            }
        });
        Thread consumer = new Thread(new Runnable() {
            @Override
            public void run() {
                for( int i = 0; i < 50; ++ i) {
                    System.out.println("Getting elem " + buffer.get());
                }
            }
        });

        producer.start();
        consumer.start();
    }
}
