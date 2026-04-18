package com.scms.exception;

public class ScmsSystemException extends Exception {

    public ScmsSystemException(String message, Throwable cause) {
        super(message, cause);
    }

    public ScmsSystemException(String message) {
        super(message);
    }
}
