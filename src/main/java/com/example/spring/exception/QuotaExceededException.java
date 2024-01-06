package com.example.spring.exception;

public class QuotaExceededException extends RuntimeException {

    public QuotaExceededException() {
        super();
    }

    public QuotaExceededException(String message) {
        super(message);
    }

    public QuotaExceededException(String message, Throwable cause) {
        super(message, cause);
    }

    public QuotaExceededException(Throwable cause) {
        super(cause);
    }
}
