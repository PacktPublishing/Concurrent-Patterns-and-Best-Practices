package com.concurrency.book.chapter05;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LockStripedHashSet<T> extends HashSet<T> {
    final Lock[] locks;

    public LockStripedHashSet(int capacity) {
        super(capacity);
        locks = new Lock[capacity];
        for (int i = 0; i < locks.length; ++i) {
            locks[i] = new ReentrantLock();
        }
    }

    @Override
    protected void lock(T x) {
        locks[x.hashCode() % locks.length].lock();
    }

    @Override
    protected void unlock(T x) {
        locks[x.hashCode() % locks.length].unlock();
    }

    protected boolean shouldResize() {
        return needsToResize.get();
    }

    @Override
    protected void resize() {
        for (Lock lck: locks) {
            lck.lock();
        }
        try {
            if (shouldResize() && recheck()) {
                int oldCapacity = table.length;
                int newCapacity = 2 * oldCapacity;
                List<T>[] oldTable = table;
                table = (List<T>[]) new List[newCapacity];
                for (int i = 0; i < newCapacity; ++i)
                    table[i] = new ArrayList<>();
                for (List<T> bucket : oldTable) {
                    for (T x : bucket) {
                        table[x.hashCode() % table.length].add(x);
                    }
                }
            }
            needsToResize.set(false);
        } finally {
            for (Lock lck: locks) {
                lck.unlock();
            }
        }
    }

    private boolean recheck() {
        for (List<T> list : table) {
            if (list.size() >= LIST_LEN_THRESHOLD) {
                return true;
            }
        }
        return false;
    }

}
