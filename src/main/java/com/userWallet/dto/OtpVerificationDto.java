package com.userWallet.dto;

import lombok.Data;

@Data
public class OtpVerificationDto {
    private String mobile;
    private String otp;
    private String email;
    private String referenceId;
}