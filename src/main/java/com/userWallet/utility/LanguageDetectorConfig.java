package com.userWallet.utility;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

@Configuration
public class LanguageDetectorConfig {

    // Path to custom Indian language profiles
    private static final String LANG_PROFILES_DIR = "src/main/resources/language-profiles/";

    @Bean
    public LanguageDetector languageDetector() throws IOException {
        // Create detector builder
        LanguageDetectorBuilder builder = LanguageDetectorBuilder.create(NgramExtractors.standard())
                .shortTextAlgorithm(50)
                .prefixFactor(1.5)
                .suffixFactor(2.0);

        // Load built-in profiles
        LanguageProfileLoader loader = new LanguageProfileLoader();
        builder.withProfiles(loader.loadAllBuiltIn());

        // Load Indian language profiles
        loadIndianLanguages(builder);

        return builder.build();
    }

    private void loadIndianLanguages(LanguageDetectorBuilder builder) throws IOException {
        String[] indianLangs = {"hi", "bn", "ta", "te", "mr", "gu", "kn", "ml", "or", "pa"};

        for (String lang : indianLangs) {
            File profileFile = Paths.get(LANG_PROFILES_DIR, lang).toFile();
            if (profileFile.exists()) {
                LanguageProfile profile = new LanguageProfileReader().read(profileFile);
                builder.addProfile(profile);
            }
        }
    }
}
