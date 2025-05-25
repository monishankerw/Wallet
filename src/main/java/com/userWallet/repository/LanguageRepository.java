package com.userWallet.repository;

import com.userWallet.entities.Language;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// LanguageRepository.java
public interface LanguageRepository extends JpaRepository<Language, String> {
    
    List<Language> findByIsActiveTrue();
    
    boolean existsByCode(String code);
}

// TranslationRepository.java
