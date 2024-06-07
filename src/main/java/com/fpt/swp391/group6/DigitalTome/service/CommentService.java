package com.fpt.swp391.group6.DigitalTome.service;


import com.fpt.swp391.group6.DigitalTome.entity.CommentEntity;
import com.fpt.swp391.group6.DigitalTome.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CommentService {
    private static final int MIN_LENGTH = 5;
    private static final int MAX_LENGTH = 500;
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

    private final Map<String, Integer> commentFrequencyMap = new HashMap<>();
    private static final int COMMENT_THRESHOLD = 3; // Số lần xuất hiện tối thiểu của một bình luận trước khi bị coi là spam

    @Autowired
    private CommentRepository commentRepository;

    public CommentEntity saveComment(CommentEntity comment) {
        return commentRepository.save(comment);
    }

    public List<CommentEntity> getCommentsByBookId(Long bookId) {
        return commentRepository.findByBookEntityId(bookId);
    }

    public CommentEntity getById(Long id) {
        return commentRepository.findById(id).orElse(null);
    }

    public List<CommentEntity> getReplies(Long parentCommentId) {
        return commentRepository.findByParentCommentId(parentCommentId);
    }

    public List<CommentEntity> getAllComments() {
        return commentRepository.findAll();
    }

    public CommentEntity updateComment(Long id, CommentEntity updatedComment) {
        Optional<CommentEntity> optionalComment = commentRepository.findById(id);
        if (optionalComment.isPresent()) {
            CommentEntity existingComment = optionalComment.get();
            existingComment.setContent(updatedComment.getContent());
            return commentRepository.save(existingComment);
        } else {
            throw new RuntimeException("Comment not found");
        }
    }

    public void deleteComment(Long id) {
        List<CommentEntity> children = commentRepository.findByParentCommentId(id);
        for (CommentEntity child : children) {
            deleteComment(child.getId());
        }
        commentRepository.deleteById(id);
    }


    public boolean isSpam(String comment) {
        commentFrequencyMap.put(comment, commentFrequencyMap.getOrDefault(comment, 0) + 1);
        // Kiểm tra nếu số lần xuất hiện vượt quá ngưỡng
        return commentFrequencyMap.getOrDefault(comment, 0) >= COMMENT_THRESHOLD;
    }
    public boolean isValidComment(String comment) {
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

}
