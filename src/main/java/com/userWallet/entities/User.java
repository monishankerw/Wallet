package com.userWallet.entities;

import com.userWallet.enums.KycType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String mobile;
    private String email;
    private String name;
    
    @Enumerated(EnumType.STRING)
    private KycType kycType;
    
    private LocalDateTime kycValidUntil;
    private BigDecimal annualLimit;
    private BigDecimal transactionLimit;
    
    // Encrypted documents
    private String aadhaarHash;
    private String panHash;
}