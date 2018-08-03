package com.concurrency.book.chapter05;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BigLockHashSet<T> extends HashSet<T> {
    final Lock lock;

    public BigLockHashSet(int capacity) {
        super(capacity);
        lock = new ReentrantLock();
    }

    @Override
    protected void resize() {
        lock.lock();
        try {
            if (shouldResize() && recheck()) {
                int currCapacity = table.length;
                int newCapacity = 2 * currCapacity;
                List<T>[] oldTable = table;
                table = (List<T>[]) new List[newCapacity];
                for (int i = 0; i < newCapacity; ++i)
                    table[i] = new ArrayList<>();
                for (List<T> list : oldTable) {
                    for (T elem : list) {
                        table[elem.hashCode() % table.length].add(elem);
                    }
                }
            }
            needsToResize.set(false);
        } finally {
            lock.unlock();
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

    @Override
    protected boolean shouldResize() {
        return needsToResize.get();
    }

    @Override
    protected void unlock(T x) {
        lock.unlock();
    }

    @Override
    protected void lock(T x) {
        lock.lock();
    }
}
