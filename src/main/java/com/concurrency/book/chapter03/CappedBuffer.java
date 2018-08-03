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
}
