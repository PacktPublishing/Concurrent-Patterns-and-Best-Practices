package com.concurrency.book.chapter02;

public class LazyInitialization {
    private LazyInitialization() {
    } // force clients to use the factory method

    // resource expensive members - not shown

    private static class LazyHolder {
        private static final LazyInitialization INSTANCE =
                new LazyInitialization();
    }

    public static LazyInitialization getInstance() {
        return LazyHolder.INSTANCE;
    }

    public static void main(String[] args) {
        LazyInitialization lazyInitialization = LazyInitialization.getInstance();
        System.out.println(lazyInitialization);
    }
}
