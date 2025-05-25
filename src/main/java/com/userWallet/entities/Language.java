package com.userWallet.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Language {
    @Id
    private String code; // en, hi, bn, or, etc.
    private String name; // English, हिन्दी, বাংলা, ଓଡ଼ିଆ
    private boolean isActive;
}



