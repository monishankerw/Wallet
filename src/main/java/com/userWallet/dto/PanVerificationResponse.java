package com.userWallet.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PanVerificationResponse {
    private String panNumber;
    private boolean isValid;
    private String status;
    private String fullName;
    private String message;
    private LocalDateTime timestamp;
    private String panType;
    private String aadhaarLinked;

    // Static factory methods
    public static PanVerificationResponse valid(String panNumber, String fullName) {
        return PanVerificationResponse.builder()
                .panNumber(panNumber)
                .isValid(true)
                .status("VALID")
                .fullName(fullName)
                .message("PAN verification successful")
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static PanVerificationResponse invalid(String panNumber, String reason) {
        return PanVerificationResponse.builder()
                .panNumber(panNumber)
                .isValid(false)
                .status("INVALID")
                .message("PAN verification failed: " + reason)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static PanVerificationResponse error(String panNumber, String errorMessage) {
        return PanVerificationResponse.builder()
                .panNumber(panNumber)
                .isValid(false)
                .status("ERROR")
                .message(errorMessage)
                .timestamp(LocalDateTime.now())
                .build();
    }
}