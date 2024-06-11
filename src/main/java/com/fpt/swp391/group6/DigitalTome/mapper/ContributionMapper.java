package com.fpt.swp391.group6.DigitalTome.mapper;

import com.fpt.swp391.group6.DigitalTome.dto.ContributionDto;
import com.fpt.swp391.group6.DigitalTome.entity.ContributionEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ContributionMapper {
    @Mapping(source = "accountEntity.id", target = "publisherId")
    @Mapping(source = "accountEntity.username", target = "publisherName")
    @Mapping(source = "accountEntity.avatarPath", target = "publisherAvatarPath")
    @Mapping(source = "bookEntity.isbn", target = "isbn")
    @Mapping(source = "bookEntity.bookCover", target = "bookCoverPath")
    @Mapping(source = "bookEntity.title", target = "title")
    @Mapping(source = "bookEntity.description", target = "description")
    @Mapping(source = "bookEntity.publicationDate", target = "publicationDate")
    ContributionDto toDto(ContributionEntity contributionEntity);
}
