package com.userWallet.service;

import com.userWallet.dto.LanguageDetectionResponse;
import com.userWallet.dto.LanguageResponse;

import java.util.List;
import java.util.Map;

public interface LanguageService {
    List<LanguageResponse> getActiveLanguages();
    void setUserLanguage(String languageCode);
    Map<String, String> getTranslations(String token);
    LanguageDetectionResponse detectLanguage(String text);
}