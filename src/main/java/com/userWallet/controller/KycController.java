package com.userWallet.controller;

import com.userWallet.dto.*;
import com.userWallet.exception.InvalidPanException;
import com.userWallet.service.KycService;
import com.userWallet.service.impl.PanVerificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/kyc")
public class KycController {

    @Autowired
    private KycService kycService;
    @Autowired
    private PanVerificationService panService;

    @PostMapping("/initiate")
    public ResponseEntity<APIResponse> initiateKyc(@RequestBody KycInitiateDto dto) {
        return kycService.initiateKycProcess(dto);
    }

    @PostMapping("/aadhaar/otp")
    public ResponseEntity<APIResponse> generateAadhaarOtp(@RequestBody AadhaarOtpRequest request) {
        return kycService.generateAadhaarOtp(request);
    }

    @PostMapping("/verify/otp")
    public ResponseEntity<APIResponse> verifyOtp(@RequestBody OtpVerificationDto dto) {
        return kycService.verifyAadhaarOtp(dto);
    }

    @PostMapping("/submit-pan")
    public ResponseEntity<APIResponse> submitPan(@RequestBody PanRequest request) {
        return kycService.verifyPan(request);
    }

    @PostMapping("/verify-pan")
    public ResponseEntity<APIResponse> verifyPan(@RequestBody PanRequest request) {
        PanVerificationResponse response = panService.verifyPan(request.getPanNumber());

        if (response.isValid()) {
            return ResponseEntity.ok(APIResponse.builder()
                    .status("SUCCESS")
                    .message("PAN verification completed")
                    .data(response)
                    .build());
        }

        throw new InvalidPanException(response.getMessage());
    }
    @PostMapping("/upload-documents")
    public ResponseEntity<APIResponse> uploadDocuments(
            @RequestParam("aadhaarFront") MultipartFile aadhaarFront,
            @RequestParam("aadhaarBack") MultipartFile aadhaarBack,
            @RequestParam("pan") MultipartFile pan) {
        return kycService.handleDocumentUpload(aadhaarFront, aadhaarBack, pan);
    }
}