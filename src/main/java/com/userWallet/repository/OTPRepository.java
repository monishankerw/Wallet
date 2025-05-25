package com.userWallet.repository;

import com.userWallet.entities.OTP;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OTPRepository extends JpaRepository<OTP, String> {

    Optional<OTP> findTopByMobileOrderByExpirationTimeDesc(String mobile);

}
