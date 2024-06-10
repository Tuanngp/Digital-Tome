package com.fpt.swp391.group6.DigitalTome.quickstart;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.microsoft.azure.cognitiveservices.vision.contentmoderator.ContentModeratorClient;
import com.microsoft.azure.cognitiveservices.vision.contentmoderator.ContentModeratorManager;
import com.microsoft.azure.cognitiveservices.vision.contentmoderator.models.AzureRegionBaseUrl;
import com.microsoft.azure.cognitiveservices.vision.contentmoderator.models.Screen;

import java.io.*;

public class ContentModeratorQuickstart {
    private static final String subscriptionKey = "a5b0c787cc3b40ffbe2c2aa88efa4c38";
    private static final String endpoint = "https://digital-tome-swp301.cognitiveservices.azure.com/";
    // TEXT MODERATION variable
    private static final File textFile = new File("src\\main\\resources\\TextModeration.txt");

    public static void main(String[] args) throws IOException {
        ContentModeratorClient client = ContentModeratorManager.authenticate(AzureRegionBaseUrl.fromString(endpoint),
                subscriptionKey);

        // Moderate text from file
        moderateText(client);

    }

    public static void moderateText(ContentModeratorClient client) throws IOException {
        System.out.println("---------------------------------------");
        System.out.println("MODERATE TEXT");
        System.out.println();

        try (BufferedReader inputStream = new BufferedReader(new FileReader(textFile))) {
            String line;
            Screen textResults = null;
            // For formatting the printed results
            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            while ((line = inputStream.readLine()) != null) {
                if (!line.isEmpty()) {
                    textResults = client.textModerations().screenText("text/plain", line.getBytes(), null);
//                    Uncomment below line to print in console
//                    System.out.println(gson.toJson(textResults).toString());
                }
            }

            System.out.println("Text moderation status: " + textResults.status().description());
            System.out.println();

            // Create output results file to TextModerationOutput.json
            BufferedWriter writer = new BufferedWriter(
                    new FileWriter(new File("src\\main\\resources\\TextModerationOutput.json")));
            writer.write(gson.toJson(textResults).toString());
            System.out.println("Check TextModerationOutput.json to see printed results.");
            System.out.println();
            writer.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
}
