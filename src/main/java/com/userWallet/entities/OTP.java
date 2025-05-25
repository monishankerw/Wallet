package com.userWallet.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class OTP {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String mobile;
    private String otpCode;
    private LocalDateTime expirationTime;

    // Optional custom constructor (if not using builder)
    public OTP(String mobile, String otpCode, LocalDateTime expirationTime) {
        this.mobile = mobile;
        this.otpCode = otpCode;
        this.expirationTime = expirationTime;
    }
}