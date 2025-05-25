package com.userWallet.service.impl;

import com.userWallet.dto.LanguageResponse;
import com.userWallet.entities.Language;
import com.userWallet.entities.Transaction;
import com.userWallet.entities.User;
import com.userWallet.exception.InvalidLanguageException;
import com.userWallet.exception.LanguageDetectionException;
import com.userWallet.dto.LanguageDetectionResponse;
import com.userWallet.exception.UserNotFoundException;
import com.userWallet.repository.LanguageRepository;
import com.userWallet.repository.TranslationRepository;
import com.userWallet.repository.UserRepository;
import com.userWallet.service.LanguageService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class LanguageServiceImpl implements LanguageService {

    private final LanguageRepository languageRepository;
    private final TranslationRepository translationRepository;
    private final UserRepository userRepository;
    private final LanguageDetector languageDetector;
    
    private static final Map<String, String> NATIVE_NAMES = Map.of(
        "en", "English",
        "hi", "हिन्दी",
        "bn", "বাংলা",
        "ta", "தமிழ்",
        "te", "తెలుగు",
        "mr", "मराठी",
        "or", "ଓଡ଼ିଆ",
        "gu", "ગુજરાતી",
        "kn", "ಕನ್ನಡ",
        "ml", "മലയാളം",
        "pa", "ਪੰਜਾਬੀ"
    );

    @Override
    public List<LanguageResponse> getActiveLanguages() {
        return languageRepository.findByIsActiveTrue()
            .stream()
            .map(this::convertToResponse)
            .collect(Collectors.toList());
    }

    @Override
    public void setUserLanguage(String token, String languageCode) {
        User user = userRepository.findByToken(token)
            .orElseThrow(UserNotFoundException::new);

        validateLanguageCode(languageCode);

        user.setLanguageCode(languageCode.toLowerCase());
        userRepository.save(user);
    }

    @Override
    public Map<String, String> getTranslations(String token) {
        String languageCode = userRepository.findByToken(token)
            .map(User::getLanguageCode)
            .orElse("en");

        return translationRepository.findByLanguageCode(languageCode)
            .stream()
            .collect(Collectors.toMap(
                Transaction::getKey, Transaction::getValue
            ));
    }

    @Override
    public LanguageDetectionResponse detectLanguage(String text) {
        try {
            List<DetectedLanguage> probabilities = languageDetector.getProbabilities(text);
            
            if(probabilities.isEmpty()) {
                return new LanguageDetectionResponse("unknown", 0.0);
            }
            
            DetectedLanguage best = probabilities.get(0);
            return new LanguageDetectionResponse(
                best.getLocale().getLanguage(),
                best.getProbability()
            );
        } catch (Exception e) {
            throw new LanguageDetectionException("Failed to detect language", e);
        }
    }

    private LanguageResponse convertToResponse(Language language) {
        return new LanguageResponse(
            language.getCode(),
            language.getName(),
            NATIVE_NAMES.getOrDefault(language.getCode(), "")
        );
    }

    private void validateLanguageCode(String code) {
        if(!languageRepository.existsByCode(code.toLowerCase())) {
            throw new InvalidLanguageException("Unsupported language code: " + code);
        }
    }
}