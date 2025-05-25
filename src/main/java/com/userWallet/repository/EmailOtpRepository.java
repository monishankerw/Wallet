package com.userWallet.repository;

import com.userWallet.entities.EmailOtp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailOtpRepository extends JpaRepository<EmailOtp, Long> {
    Optional<EmailOtp> findByEmail(String email);
}