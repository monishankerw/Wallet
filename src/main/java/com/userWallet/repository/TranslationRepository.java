package com.userWallet.repository;

import com.userWallet.entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TranslationRepository extends JpaRepository<Transaction, Long> {
    
    List<Transaction> findByLanguageCode(String languageCode);
}