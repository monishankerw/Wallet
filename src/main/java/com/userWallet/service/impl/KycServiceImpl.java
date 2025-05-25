package com.userWallet.service.impl;

import com.userWallet.dto.*;
import com.userWallet.entities.KycDocuments;
import com.userWallet.entities.KycRequest;
import com.userWallet.entities.User;
import com.userWallet.enums.KycType;
import com.userWallet.exception.DocumentVerificationException;
import com.userWallet.exception.InvalidOtpException;
import com.userWallet.exception.InvalidPanException;
import com.userWallet.exception.UserNotFoundException;
import com.userWallet.repository.KycRequestRepository;
import com.userWallet.repository.UserRepository;
import com.userWallet.service.KycService;
import com.userWallet.service.OTPService;
import com.userWallet.utility.UidaiClient;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDateTime;

// Service Implementation
@Service
@RequiredArgsConstructor
public class KycServiceImpl implements KycService {

    private final UserRepository userRepository;
    private final KycRequestRepository kycRequestRepository;
    private final UidaiClient uidaiClient;
    private final PanVerificationService panService;
    private final DocumentStorageService storageService;
    private final OTPService otpService;

    @Value("${kyc.partial.limit}")
    private BigDecimal partialLimit;

    @Value("${kyc.full.limit}")
    private BigDecimal fullLimit;

    @Override
    @Transactional
    public ResponseEntity<APIResponse> initiateKycProcess(KycInitiateDto dto) {
        User user = (User) userRepository.findByMobile(dto.getMobile())
                .orElseThrow(UserNotFoundException::new);

        if (dto.getKycType() == KycType.PARTIAL) {
            return handlePartialKyc(user);
        }
        return handleFullKycInitiation(user);
    }

    @Override
    public ResponseEntity<APIResponse> generateAadhaarOtp(AadhaarOtpRequest request) {
        String referenceId = uidaiClient.generateOtp(request.getAadhaarNumber());
        otpService.generateOTP(request.getAadhaarNumber());
        return ResponseEntity.ok(new APIResponse("SUCCESS", "OTP sent successfully", referenceId));
    }

    @Override
    @Transactional
    public ResponseEntity<APIResponse> verifyAadhaarOtp(OtpVerificationDto dto) {
        boolean isValid = uidaiClient.verifyOtp(dto.getReferenceId(), dto.getOtp());
        if (!isValid) {
            throw new InvalidOtpException("Aadhaar verification failed");
        }

        kycRequestRepository.updateAadhaarStatus(dto.getReferenceId(), "VERIFIED");
        return ResponseEntity.ok(new APIResponse("SUCCESS", "Aadhaar Verified","null"));
    }

    @Override
    @Transactional
    public ResponseEntity<APIResponse> verifyPan(PanRequest request) {
        PanVerificationResponse response = panService.verifyPan(request.getPanNumber());
        if (!response.isValid()) {
            throw new InvalidPanException("PAN verification failed");
        }

        kycRequestRepository.updatePanStatus(request.getPanNumber(), "VERIFIED");
        return ResponseEntity.ok(new APIResponse("SUCCESS", "PAN Verified",""));
    }

    @Override
    @Transactional
    public ResponseEntity<APIResponse> handleDocumentUpload(MultipartFile aadhaarFront,
                                                            MultipartFile aadhaarBack,
                                                            MultipartFile pan) {
        KycDocuments documents = new KycDocuments();

        documents.setAadhaarFrontPath(storageService.storeDocument(aadhaarFront));
        documents.setAadhaarBackPath(storageService.storeDocument(aadhaarBack));
        documents.setPanDocumentPath(storageService.storeDocument(pan));

        boolean ocrSuccess = performOcrVerification(documents);

        if (!ocrSuccess) {
            throw new DocumentVerificationException("Document details mismatch");
        }

        return ResponseEntity.ok(new APIResponse("SUCCESS", "Documents verified and stored", ""));
    }

    private ResponseEntity<APIResponse> handlePartialKyc(User user) {
        user.setKycType(KycType.PARTIAL);
        user.setKycValidUntil(LocalDateTime.now().plusYears(1));
        user.setAnnualLimit(partialLimit);
        userRepository.save(user);
        return ResponseEntity.ok(new APIResponse("SUCCESS", "Partial KYC Completed","null"));
    }

    private ResponseEntity<APIResponse> handleFullKycInitiation(User user) {
        KycRequest request = new KycRequest();
        request.setUserId(String.valueOf(user.getId()));
        request.setRequestedType(KycType.FULL);
        request.setStatus("PENDING");
        request.setCreatedAt(LocalDateTime.now());
        kycRequestRepository.save(request);

        return ResponseEntity.ok(new APIResponse("SUCCESS", "Full KYC Initiated", request.getId()));
    }

    private boolean performOcrVerification(KycDocuments documents) {
        // OCR implementation logic
        // Compare extracted data with user provided information
        return true;
    }
}
