package com.fpt.swp391.group6.DigitalTome.mapper;

import com.fpt.swp391.group6.DigitalTome.dto.BookDetailDto;
import com.fpt.swp391.group6.DigitalTome.entity.BookEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BookDetailMapper {
    BookDetailDto toDto(BookEntity bookEntity);
    BookEntity toEntity(BookDetailDto bookDetailDto);
}
