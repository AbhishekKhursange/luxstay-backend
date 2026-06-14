package com.hotel.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/upload")
public class FileUploadController {

    @Value("${upload.dir:uploads/}")
    private String uploadDir;

    @Value("${server.port:8083}")
    private String serverPort;

    @PostMapping("/image")
    public ResponseEntity<Map<String, String>> uploadImage(
            @RequestParam("file") MultipartFile file) throws IOException {

        // Create uploads folder if it doesn't exist
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Generate unique filename to avoid conflicts
        String originalName = file.getOriginalFilename()
                .replaceAll("[^a-zA-Z0-9._-]", "_");
        String filename = UUID.randomUUID() + "_" + originalName;

        // Save file to disk
        Path filePath = uploadPath.resolve(filename);
        Files.copy(file.getInputStream(), filePath,
                StandardCopyOption.REPLACE_EXISTING);

        // Return the public URL
        String imageUrl = "http://localhost:" + serverPort + "/uploads/" + filename;

        return ResponseEntity.ok(Map.of("imageUrl", imageUrl));
    }
}
