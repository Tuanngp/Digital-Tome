package com.fpt.swp391.group6.DigitalTome.service;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.microsoft.azure.cognitiveservices.vision.contentmoderator.ContentModeratorClient;
import com.microsoft.azure.cognitiveservices.vision.contentmoderator.ContentModeratorManager;
import com.microsoft.azure.cognitiveservices.vision.contentmoderator.models.AzureRegionBaseUrl;
import com.microsoft.azure.cognitiveservices.vision.contentmoderator.models.DetectedTerms;
import com.microsoft.azure.cognitiveservices.vision.contentmoderator.models.PII;
import com.microsoft.azure.cognitiveservices.vision.contentmoderator.models.Screen;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;


@Service
public class ContentModeratorService {
    private static final String subscriptionKey = "a5b0c787cc3b40ffbe2c2aa88efa4c38";
    private static final String endpoint = "https://digital-tome-swp301.cognitiveservices.azure.com/";
    private static final int MIN_LENGTH = 2;
    private static final int MAX_LENGTH = 500;
    private final Map<String, Integer> commentFrequencyMap = new HashMap<>();
    private static final int COMMENT_THRESHOLD = 3;
    private static final File textFile = new File("src\\main\\resources\\banned_keywords.txt");
    private static final Set<String> BANNED_KEYWORDS = new HashSet<>();
    private final ContentModeratorClient client;

    public ContentModeratorService() {
        client = ContentModeratorManager.authenticate(AzureRegionBaseUrl.fromString(endpoint), subscriptionKey);
    }

    public boolean isContentInappropriate(String content) throws IOException {
        if (!isValidComment(content)) {
            return true;
        }
        Screen textResults = client.textModerations().screenText("text/plain", content.getBytes(), null);
        List<DetectedTerms> terms = textResults.terms();
        PII pII = textResults.pII();
        return (terms != null && !terms.isEmpty()) ||
                (pII != null &&
                    (!pII.email().isEmpty() ||
                    !pII.phone().isEmpty() ||
                    !pII.address().isEmpty()));
    }

    public boolean isSpam(String comment) {
        commentFrequencyMap.put(comment, commentFrequencyMap.getOrDefault(comment, 0) + 1);
        return commentFrequencyMap.getOrDefault(comment, 0) >= COMMENT_THRESHOLD;
    }

    public boolean isValidComment(String comment) throws IOException {
        loadBannedKeywordsFromFile();
        // Kiểm tra độ dài bình luận
        if (comment.length() < MIN_LENGTH || comment.length() > MAX_LENGTH) {
            return false;
        }

        // Kiểm tra từ khóa cấm
        for (String keyword : BANNED_KEYWORDS) {
            if (comment.toLowerCase().contains(keyword.toLowerCase())) {
                return false;
            }
        }
        return !comment.trim().isEmpty();
    }

    public void loadBannedKeywordsFromFile() throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(textFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                BANNED_KEYWORDS.add(line.trim());
            }
        }
    }
}