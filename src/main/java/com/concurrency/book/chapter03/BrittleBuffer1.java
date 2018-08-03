package com.concurrency.book.chapter03;

public class BrittleBuffer1 extends Buffer {
    public BrittleBuffer1(int capacity) {
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
}
