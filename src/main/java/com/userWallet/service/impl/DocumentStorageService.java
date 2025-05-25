package com.userWallet.service.impl;

import com.userWallet.exception.DocumentNotFoundException;
import com.userWallet.exception.DocumentStorageException;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class DocumentStorageService {

    @Value("${document.storage.path}")
    private String storagePath;

    public String storeDocument(MultipartFile file) {
        try {
            // Generate unique filename
            String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path path = Paths.get(storagePath, filename);

            // Create directories if not exists
            Files.createDirectories(path.getParent());

            // Save file
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

            return filename;
        } catch (IOException e) {
            throw new DocumentStorageException("Failed to store document", e);
        }
    }
    public Resource loadDocument (String filename){
        try {
            Path path = Paths.get(storagePath).resolve(filename);
            UrlResource resource = new UrlResource(path.toUri());

            if (resource.exists() || resource.isReadable()) {
                return (Resource) resource;
            }
            throw new DocumentNotFoundException("File not found: " + filename);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            throw new DocumentNotFoundException("Invalid file path");
        }
    }

}