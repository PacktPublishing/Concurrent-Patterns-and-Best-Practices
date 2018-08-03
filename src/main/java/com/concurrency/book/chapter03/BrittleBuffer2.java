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
                    return get();
            }
            Thread.sleep(1000);
        }
    }
}
