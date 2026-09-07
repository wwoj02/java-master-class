package com.wojtek.exception;

public class FileDataAccessException extends RuntimeException {
    public FileDataAccessException(String message) {
        super(message);
    }

    public FileDataAccessException(String message, Throwable cause) {
        super(message, cause);
    }
}
