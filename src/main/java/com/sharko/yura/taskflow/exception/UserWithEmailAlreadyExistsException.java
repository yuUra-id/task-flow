package com.sharko.yura.taskflow.exception;

public class UserWithEmailAlreadyExistsException extends RuntimeException{

    public UserWithEmailAlreadyExistsException(String message) {
        super(message);
    }

    public UserWithEmailAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserWithEmailAlreadyExistsException(Throwable cause) {
        super(cause);
    }
}
