package com.ai.agent.verifact.controller;

import com.ai.agent.verifact.service.AiService;
import com.ai.agent.verifact.service.ImageOcrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/api/v1")
public class AiController {

    private final AiService aiService;
    private final ImageOcrService imageOcrService;

    @Autowired
    public AiController(AiService aiService, ImageOcrService imageOcrService) {
        this.aiService = aiService;
        this.imageOcrService = imageOcrService;
    }

    // Endpoint for text-based fake news check
    @GetMapping("/isFakeNews")
    public String isFakeNews(@RequestParam String news) {
        return aiService.isFakeNews(news);
    }

    // Endpoint for image-based fake news check
    @PostMapping("/analyzeImage")
    public String analyzeImage(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return "No file uploaded.";
        }

        try {
            // Save file temporarily
            File tempFile = File.createTempFile("uploaded_", ".jpg");
            file.transferTo(tempFile);

            // OCR extract text from the image
            String extractedText = imageOcrService.extractTextFromImage(tempFile);

            // Analyze the extracted text for fake news
            return aiService.isFakeNews(extractedText);

        } catch (IOException e) {
            return "Failed to process the image: " + e.getMessage();
        }
    }
}