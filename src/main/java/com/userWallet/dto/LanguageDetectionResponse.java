package com.userWallet.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

// LanguageDetectionResponse.java
@AllArgsConstructor
@Getter
public class LanguageDetectionResponse {
    private String languageCode;
    private double confidence;
}

