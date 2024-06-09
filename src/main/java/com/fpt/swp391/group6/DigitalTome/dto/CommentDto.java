package com.fpt.swp391.group6.DigitalTome.dto;

import com.fpt.swp391.group6.DigitalTome.entity.AccountEntity;
import com.fpt.swp391.group6.DigitalTome.entity.BookEntity;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.util.Date;

/**
 * DTO for {@link com.fpt.swp391.group6.DigitalTome.entity.CommentEntity}
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class CommentDto implements Serializable {
    Long id;
    Long parentCommentId;
    String content;
    Long accountId;
    Long bookId;

    AccountEntity accountEntity;
    BookEntity bookEntity;

    String createdBy;
    String modifiedBy;
    Date createdDate;
    Date modifiedDate;
}