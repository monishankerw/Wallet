package com.userWallet.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LanguageDetectionException extends RuntimeException {
    public LanguageDetectionException(String message, Throwable cause) {
        super(message, cause);
    }
}