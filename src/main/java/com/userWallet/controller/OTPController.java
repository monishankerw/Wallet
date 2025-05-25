package com.userWallet.controller;

import com.userWallet.dto.APIResponse;
import com.userWallet.dto.CustomerRequestDto;
import com.userWallet.dto.EmailOtpRequest;
import com.userWallet.dto.OtpVerificationDto;
import com.userWallet.service.OTPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/otp")
public class OTPController {
    @Autowired
    private OTPService otpService;

    @PostMapping("/mobile")
    public APIResponse<String> generateOTP(@RequestBody CustomerRequestDto request) {
        return otpService.generateOTP(request.getMobile());
    }
    @PostMapping("/login")
    public APIResponse<Object> verifyOTP(@RequestBody OtpVerificationDto dto) {
        return otpService.verifyOTP(dto.getMobile(), dto.getOtp());
    }

    @PostMapping("/email")
    public ResponseEntity<APIResponse> sendOtp(@RequestBody EmailOtpRequest request) {
        return ResponseEntity.ok(otpService.sendOtp(request));
    }

    @PostMapping("/verify-sms")
    public ResponseEntity<APIResponse> verifyOtp(@RequestBody OtpVerificationDto request) {
        return ResponseEntity.ok(otpService.verifyOtp(request));
    }
}