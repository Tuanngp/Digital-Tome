package com.fpt.swp391.group6.DigitalTome.dto;

import com.fpt.swp391.group6.DigitalTome.entity.AccountEntity;
import com.fpt.swp391.group6.DigitalTome.entity.BaseEntity;
import com.fpt.swp391.group6.DigitalTome.entity.BookEntity;
import jakarta.persistence.Column;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

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
    AccountEntity accountEntity;
    BookEntity bookEntity;

    String createdBy;
    Date createdDate;
    String modifiedBy;
    Date modifiedDate;
}