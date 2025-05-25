package com.userWallet.service.impl;

import com.userWallet.dto.PanVerificationResponse;
import com.userWallet.dto.ThirdPartyPanResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
public class PanVerificationService {

    public PanVerificationResponse verifyPan(String panNumber) {
        try {
            // Validate PAN format
            if (!isValidPanFormat(panNumber)) {
                return PanVerificationResponse.invalid(panNumber, "Invalid PAN format");
            }

            // Mock external API call
            ThirdPartyPanResponse apiResponse = callPanVerificationApi(panNumber);

            return PanVerificationResponse.builder()
                    .panNumber(panNumber)
                    .isValid(apiResponse.isValid())
                    .status(apiResponse.getStatus())
                    .fullName(apiResponse.getFullName())
                    .panType(apiResponse.getPanType())
                    .aadhaarLinked(apiResponse.isAadhaarLinked() ? "YES" : "NO")
                    .message("Verification completed successfully")
                    .timestamp(LocalDateTime.now())
                    .build();

        } catch (Exception e) {
            log.error("PAN verification error for {}: {}", panNumber, e.getMessage());
            return PanVerificationResponse.error(panNumber, "Verification service unavailable");
        }
    }

    private boolean isValidPanFormat(String panNumber) {
        return panNumber.matches("[A-Z]{5}[0-9]{4}[A-Z]{1}");
    }

    // Mock external API call
    private ThirdPartyPanResponse callPanVerificationApi(String panNumber) {
        // Implementation would call real PAN verification API
        return ThirdPartyPanResponse.builder()
                .isValid(true)
                .fullName("John Doe")
                .panType("Individual")
                .aadhaarLinked(true)
                .build();
    }

    // External API response structure

}