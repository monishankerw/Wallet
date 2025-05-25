package com.userWallet.entities;

import com.userWallet.enums.KycType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Entity
public class KycRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String userId;
    private String aadhaarNumber;
    private String panNumber;
    
    @Enumerated(EnumType.STRING)
    private KycType requestedType;
    
    private String status; // PENDING, APPROVED, REJECTED
    private LocalDateTime createdAt;
}