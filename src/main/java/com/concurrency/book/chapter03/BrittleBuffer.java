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
}
