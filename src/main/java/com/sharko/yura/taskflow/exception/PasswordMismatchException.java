package com.sharko.yura.taskflow.exception;

public class PasswordMismatchException extends RuntimeException {

    public PasswordMismatchException(String message) {
        super(message);
    }

    public PasswordMismatchException(String message, Throwable cause) {
        super(message, cause);
    }

    public PasswordMismatchException(Throwable cause) {
        super(cause);
    }

}
