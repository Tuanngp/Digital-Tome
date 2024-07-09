package com.fpt.swp391.group6.DigitalTome.mapper;

import com.fpt.swp391.group6.DigitalTome.dto.FavoriteDto;
import com.fpt.swp391.group6.DigitalTome.entity.FavoriteEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FavoriteMapper {

    @Mapping(target = "bookId", source = "bookEntity.id")
    @Mapping(target = "accountId", source = "accountEntity.id")
    FavoriteDto toDto(FavoriteEntity favoriteEntity);

    @Mapping(target = "bookEntity.id", source = "bookId")
    @Mapping(target = "accountEntity.id", source = "accountId")
    FavoriteEntity toEntity(FavoriteDto favoriteDto);
}
