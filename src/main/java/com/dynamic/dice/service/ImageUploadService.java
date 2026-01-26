package com.dynamic.dice.service;

import com.dynamic.dice.exception.BusinessException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.UUID;

@Service
public class ImageUploadService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    public Map<String, String> store(MultipartFile file, String adminId) throws IOException {

        if (!file.getContentType().startsWith("image/")) {
            throw new BusinessException("Only image files are allowed");
        }

        String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
        int idx = originalFilename.lastIndexOf('.');
        String extension = (idx != -1) ? originalFilename.substring(idx) : "";

        String newFileName = UUID.randomUUID().toString() + extension;

        Path adminDir = Paths.get(uploadDir, adminId);
        Files.createDirectories(adminDir);

        Path filePath = adminDir.resolve(newFileName);
        Files.copy(file.getInputStream(), filePath);

        String imageUrl = "/uploads/dice/" + adminId + "/" + newFileName;
        return Map.of("imageUrl", imageUrl);
    }
}

