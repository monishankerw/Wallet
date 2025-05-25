package com.userWallet.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// InvalidOtpException.java
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidOtpException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public InvalidOtpException(String message) {
        super(message);
    }

    public InvalidOtpException(String referenceId, String reason) {
        super(String.format("OTP verification failed for reference %s: %s", referenceId, reason));
    }
}
