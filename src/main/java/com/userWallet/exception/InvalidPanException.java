package com.userWallet.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// InvalidPanException.java
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidPanException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public InvalidPanException(String message) {
        super(message);
    }
}