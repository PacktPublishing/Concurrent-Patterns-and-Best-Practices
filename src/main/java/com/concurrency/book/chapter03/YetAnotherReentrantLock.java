package com.concurrency.book.chapter03;

public class YetAnotherReentrantLock {
    private Thread lockedBy = null;
    private int lockCount = 0;

    private boolean isLocked() {
        return lockedBy != null;
    }

    private boolean isLockedByMe() {
        return Thread.currentThread() == lockedBy;
    }

    public synchronized void lock() throws InterruptedException {
        while (isLocked() && !isLockedByMe()) {
            this.wait();
        }
        lockedBy = Thread.currentThread();
        lockCount++;
    }

    public synchronized void unlock() {
        if (isLockedByMe()) {
            lockCount--;
        }
        if (lockCount == 0) {
            lockedBy = null;
            this.notify();
        }
    }
}
