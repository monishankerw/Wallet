package com.userWallet.utility;

import com.userWallet.exception.InvalidAadhaarException;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UidaiClient {
    
    // Mock implementation - real implementation would use UIDAI APIs
    public String generateOtp(String aadhaarNumber) {
        // Validate Aadhaar format
        if(!aadhaarNumber.matches("^[2-9]{1}[0-9]{11}$")) {
            throw new InvalidAadhaarException("Invalid Aadhaar number");
        }
        
        // Generate mock reference ID
        String referenceId = "UIDAI-" + UUID.randomUUID().toString().substring(0, 8);
        return referenceId;
    }

    public boolean verifyOtp(String referenceId, String otp) {
        // Mock verification - real implementation would call UIDAI API
        return otp.length() == 6 && otp.matches("\\d+");
    }
}