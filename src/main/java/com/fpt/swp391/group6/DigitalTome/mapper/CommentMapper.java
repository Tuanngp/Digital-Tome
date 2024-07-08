package com.fpt.swp391.group6.DigitalTome.mapper;

import com.fpt.swp391.group6.DigitalTome.dto.CommentDto;
import com.fpt.swp391.group6.DigitalTome.entity.CommentEntity;
import org.mapstruct.Mapper;
<<<<<<< HEAD
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;
=======
>>>>>>> origin/khanhduc-workspace

import java.util.List;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    CommentDto toCommentDto(CommentEntity commentEntity);

    CommentEntity toCommentEntity(CommentDto commentDto);

    List<CommentDto> toCommentDto(List<CommentEntity> commentEntities);

    List<CommentEntity> toCommentEntity(List<CommentDto> commentDtos);
}