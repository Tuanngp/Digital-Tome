package com.fpt.swp391.group6.DigitalTome.service;


import com.microsoft.azure.cognitiveservices.vision.contentmoderator.ContentModeratorClient;
import com.microsoft.azure.cognitiveservices.vision.contentmoderator.ContentModeratorManager;
import com.microsoft.azure.cognitiveservices.vision.contentmoderator.models.AzureRegionBaseUrl;
import com.microsoft.azure.cognitiveservices.vision.contentmoderator.models.DetectedTerms;
import com.microsoft.azure.cognitiveservices.vision.contentmoderator.models.PII;
import com.microsoft.azure.cognitiveservices.vision.contentmoderator.models.Screen;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class ContentModeratorService {
    private static final String subscriptionKey = "a5b0c787cc3b40ffbe2c2aa88efa4c38";
    private static final String endpoint = "https://digital-tome-swp301.cognitiveservices.azure.com/";
    private static final int MIN_LENGTH = 1;
    private static final int MAX_LENGTH = 500;
    private final Map<String, Integer> commentFrequencyMap = new HashMap<>();
    private static final int COMMENT_THRESHOLD = 3; // Số lần xuất hiện tối thiểu của một bình luận trước khi bị coi là spam
    private static final List<String> BANNED_KEYWORDS = Arrays.asList(
            "chửi thề", "tục tĩu", "bậy bạ", "đụ", "cút", "cặc", "địt", "má", "con",
            "chó", "chết", "vãi", "bậy", "lồn", "phò", "điếm", "đĩ", "gà", "vú",
            "kỳ thị", "phân biệt", "bọn", "tụi", "da đen", "châu Phi", "Châu Á", "da trắng",
            "giết", "đánh", "bom", "khủng bố", "hại", "hành hạ", "đâm", "súng", "dao",
            "súng ống", "bắn", "chết chóc", "ngu", "dốt", "khùng", "điên", "bại", "bất tài",
            "bất tài", "bất lực", "bất lực", "điên rồ", "điên loạn", "điên cuồng", "điên dại",
            "lừa đảo", "thằng", "con", "khốn nạn", "mất dạy", "vô học", "ngu xuẩn", "bẩn thỉu",
            "ma túy", "cần sa", "thuốc phiện", "mại dâm", "hối lộ", "tham nhũng", "lừa đảo",
            "trộm cắp", "cờ bạc", "đánh bạc", "mại dâm", "buôn người", "bán người", "bắt cóc",
            "giết người", "đánh nhau", "đánh ghen", "ganh ghét", "ghen tị", "ganh tị", "ganh ghét",
            "gian lận", "lừa dối", "lừa đảo", "lừa", "dối", "lừa lọc", "lừa bịp", "lừa gạt"
    );
    private final ContentModeratorClient client;

    public ContentModeratorService() {
        client = ContentModeratorManager.authenticate(AzureRegionBaseUrl.fromString(endpoint), subscriptionKey);
    }

    public boolean isContentInappropriate(String content) {
        Screen textResults = client.textModerations().screenText("text/plain", content.getBytes(), null);
        List<DetectedTerms> terms = textResults.terms();
        PII pII = textResults.pII();
        return (terms != null && !terms.isEmpty()) ||
                (pII != null &&
                    (!pII.email().isEmpty() ||
                    !pII.phone().isEmpty() ||
                    !pII.address().isEmpty())) ||
                !isValidComment(content);
    }

    public boolean isSpam(String comment) {
        commentFrequencyMap.put(comment, commentFrequencyMap.getOrDefault(comment, 0) + 1);
        return commentFrequencyMap.getOrDefault(comment, 0) >= COMMENT_THRESHOLD;
    }

    public boolean isValidComment(String comment) {
        // Kiểm tra độ dài bình luận
        if (comment.isEmpty() || comment.length() > MAX_LENGTH) {
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
}