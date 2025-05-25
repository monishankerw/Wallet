package com.userWallet.service;

import com.userWallet.dto.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface KycService {
    ResponseEntity<APIResponse> initiateKycProcess(KycInitiateDto dto);
    ResponseEntity<APIResponse> generateAadhaarOtp(AadhaarOtpRequest request);
    ResponseEntity<APIResponse> verifyAadhaarOtp(OtpVerificationDto dto);
    ResponseEntity<APIResponse> verifyPan(PanRequest request);
    ResponseEntity<APIResponse> handleDocumentUpload(MultipartFile aadhaarFront,
                                                     MultipartFile aadhaarBack,
                                                     MultipartFile pan);
}
