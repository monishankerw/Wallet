package com.userWallet.exception;

import lombok.Getter;

@Getter
// Exceptions.java
public class InvalidLanguageException extends RuntimeException {
    public InvalidLanguageException(String message) {
        super(message);
    }
}
