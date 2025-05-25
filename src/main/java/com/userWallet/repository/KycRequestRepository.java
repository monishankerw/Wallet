package com.userWallet.repository;

import com.userWallet.entities.KycRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface KycRequestRepository extends JpaRepository<KycRequest, Long> {
    
    @Modifying
    @Query("UPDATE KycRequest k SET k.aadhaarStatus = :status WHERE k.referenceId = :referenceId")
    void updateAadhaarStatus(@Param("referenceId") String referenceId,
                           @Param("status") String status);

    @Modifying
    @Query("UPDATE KycRequest k SET k.panStatus = :status WHERE k.panNumber = :panNumber")
    void updatePanStatus(@Param("panNumber") String panNumber, 
                       @Param("status") String status);
}