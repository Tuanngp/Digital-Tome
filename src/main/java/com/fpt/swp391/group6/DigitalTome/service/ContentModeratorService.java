package com.fpt.swp391.group6.DigitalTome.service;


import com.microsoft.azure.cognitiveservices.vision.contentmoderator.ContentModeratorClient;
import com.microsoft.azure.cognitiveservices.vision.contentmoderator.ContentModeratorManager;
import com.microsoft.azure.cognitiveservices.vision.contentmoderator.models.AzureRegionBaseUrl;
import com.microsoft.azure.cognitiveservices.vision.contentmoderator.models.Screen;
import org.springframework.stereotype.Service;


@Service
public class ContentModeratorService {
    private static final String subscriptionKey = "a5b0c787cc3b40ffbe2c2aa88efa4c38";
    private static final String endpoint = "https://digital-tome-swp301.cognitiveservices.azure.com/";

    private final ContentModeratorClient client;

    public ContentModeratorService() {
        client = ContentModeratorManager.authenticate(AzureRegionBaseUrl.fromString(endpoint), subscriptionKey);
    }

    public boolean isContentInappropriate(String content) {
        Screen textResults = client.textModerations().screenText("text/plain", content.getBytes(), null);
        return textResults.terms() != null && !textResults.terms().isEmpty();
    }
}