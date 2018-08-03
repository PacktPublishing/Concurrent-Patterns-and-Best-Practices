package com.concurrency.book.chapter05;

public class QueueIsEmptyException extends RuntimeException {
    public QueueIsEmptyException(String message) {
        super(message);
    }

    public QueueIsEmptyException() {
        this("");
    }
}
