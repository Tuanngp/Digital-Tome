package com.fpt.swp391.group6.DigitalTome.dto;

import com.fpt.swp391.group6.DigitalTome.entity.AccountEntity;
import com.fpt.swp391.group6.DigitalTome.entity.BookEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

/**
 * DTO for {@link com.fpt.swp391.group6.DigitalTome.entity.CommentEntity}
 */
@Value
@Data
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class CommentDto implements Serializable {
    String content;
    Long parentCommentId;
    AccountEntity accountEntity;
    BookEntity bookEntity;
}