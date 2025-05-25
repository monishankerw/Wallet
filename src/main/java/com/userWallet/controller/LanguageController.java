package com.userWallet.controller;

import com.userWallet.dto.LanguageRequest;
import com.userWallet.dto.LanguageResponse;
import com.userWallet.dto.TextRequest;
import com.userWallet.dto.LanguageDetectionResponse;
import com.userWallet.service.LanguageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/language")
public class LanguageController {

    @Autowired
    private  LanguageService languageService;

    // Get available languages
    @GetMapping
    public ResponseEntity<List<LanguageResponse>> getAvailableLanguages() {
        return ResponseEntity.ok(languageService.getActiveLanguages());
    }

    // Set user preferred language
    @PostMapping("/preference")
    public ResponseEntity<Void> setLanguagePreference(
//        @RequestHeader("Authorization") String token,
        @RequestBody LanguageRequest request
    ) {
        languageService.setUserLanguage(request.getLanguageCode());
        return ResponseEntity.ok().build();
    }

    // Get translations for current user
    @GetMapping("/translations")
    public ResponseEntity<Map<String, String>> getTranslations(
        @RequestHeader("Authorization") String token
    ) {
        return ResponseEntity.ok(languageService.getTranslations(token));
    }

    // Auto-detect language from text
    @PostMapping("/detect")
    public ResponseEntity<LanguageDetectionResponse> detectLanguage(
            @RequestBody TextRequest request
    ) {
        return ResponseEntity.ok(languageService.detectLanguage(request.getText()));
    }
}