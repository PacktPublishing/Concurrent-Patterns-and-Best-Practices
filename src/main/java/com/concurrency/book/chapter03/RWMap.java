package com.concurrency.book.chapter03;

import java.util.Map;

public class RWMap<K, V> {
    private final Map<K, V> map;
    private final ReadWriteLock lock = new ReadWriteLock();
    private final RWLock r = lock.getRdLock();
    private final RWLock w = lock.getWrLock();

    public RWMap(Map<K, V> map) {
        this.map = map;
    }

    public V put(K key, V value) throws InterruptedException {
        w.lock();
        try {
            return map.put(key, value);
        } finally {
            w.unlock();
        }
    }

    public V get(Object key) throws InterruptedException {
        r.lock();
        try {
            return map.get(key);
        } finally {
            r.unlock();
        }
    }

    public V remove(Object key) throws InterruptedException {
        w.lock();
        try {
            return map.remove(key);
        } finally {
           w.unlock();
        }
    }

    public void putAll(Map<? extends K, ? extends V> m) throws InterruptedException {
        w.lock();
        try {
            map.putAll(m);
        } finally {
            w.unlock();
        }
    }


    public void clear() throws InterruptedException {
        w.lock();
        try {
            map.clear();
        } finally {
           w.unlock();
        }
    }
}