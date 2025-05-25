package com.userWallet.dto;

import com.userWallet.enums.KycType;
import lombok.Data;

@Data
public class KycInitiateDto {
    private String mobile;
    private KycType kycType;
}