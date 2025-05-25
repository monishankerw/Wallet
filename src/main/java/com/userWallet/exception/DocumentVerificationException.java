package com.userWallet.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// DocumentVerificationException.java
@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class DocumentVerificationException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public DocumentVerificationException(String message) {
        super(message);
    }

    public DocumentVerificationException(String documentType, String details) {
        super(String.format("Document verification failed for %s: %s", documentType, details));
    }

    public DocumentVerificationException(String message, Throwable cause) {
        super(message, cause);
    }
}