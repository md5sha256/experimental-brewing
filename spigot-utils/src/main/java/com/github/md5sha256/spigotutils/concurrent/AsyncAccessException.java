package com.github.md5sha256.spigotutils.concurrent;

public class AsyncAccessException extends RuntimeException {

    public AsyncAccessException() {
    }

    public AsyncAccessException(String message) {
        super(message);
    }

    public AsyncAccessException(String message, Throwable cause) {
        super(message, cause);
    }

    public AsyncAccessException(Throwable cause) {
        super(cause);
    }

    public AsyncAccessException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
