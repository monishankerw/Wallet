package com.userWallet.service;

import com.userWallet.dto.APIResponse;
import com.userWallet.dto.EmailOtpRequest;
import com.userWallet.dto.OtpVerificationDto;

public interface OTPService {
    APIResponse<String> generateOTP(String mobile);
    APIResponse<Object> verifyOTP(String mobile, String otp);
    public APIResponse sendOtp(EmailOtpRequest request);
    public APIResponse verifyOtp(OtpVerificationDto request);
}
