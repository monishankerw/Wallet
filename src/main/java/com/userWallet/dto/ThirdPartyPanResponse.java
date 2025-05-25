package com.userWallet.dto;

import lombok.Builder;
import lombok.Data;

@Data
    @Builder
    public   class ThirdPartyPanResponse {
        private boolean isValid;
        private String fullName;
        private String panType;
        private boolean aadhaarLinked;
    private String status;
    }