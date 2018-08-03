package com.concurrency.book.chapter03;

public abstract class Buffer {
    private final Integer[] buf;
    private int tail;
    private int head;
    private int cnt;

    protected Buffer(int capacity) {
        this.buf = (Integer[]) new Object[capacity];
        this.tail = 0;
        this.head = 0;
        this.cnt = 0;
    }

    protected synchronized final void putElem(int v) {
        buf[tail] = v;
        if (++tail == buf.length)
            tail = 0;
        ++cnt;
    }

    protected synchronized final Integer getElem() {
        Integer v = buf[head];
        buf[head] = null;
        if (++head == buf.length)
            head = 0;
        --cnt;
        return v;
    }

    public synchronized final boolean isBufFull() {
        return cnt == buf.length;
    }

    public synchronized final boolean isBufEmpty() {
        return cnt == 0;
    }
}