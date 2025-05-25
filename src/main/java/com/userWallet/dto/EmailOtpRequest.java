package com.userWallet.dto;

import lombok.Data;

@Data
// Request for sending OTP
public class EmailOtpRequest {
    private String email;
    private String otpCode;

}
