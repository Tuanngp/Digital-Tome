package com.fpt.swp391.group6.DigitalTome.mapper;

import com.fpt.swp391.group6.DigitalTome.dto.BookDto;
import com.fpt.swp391.group6.DigitalTome.entity.BookEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BookMapper {

    BookEntity toBook(BookDto bookDto);
}
