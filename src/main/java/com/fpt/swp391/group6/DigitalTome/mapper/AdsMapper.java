package com.fpt.swp391.group6.DigitalTome.mapper;
import com.fpt.swp391.group6.DigitalTome.dto.AdsDto;
import com.fpt.swp391.group6.DigitalTome.entity.AdsAssignmentEntity;
import com.fpt.swp391.group6.DigitalTome.entity.AdsEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AdsMapper {

    @Mapping(target = "typeId", source = "adsType.id")
    @Mapping(target = "title", source = "title")
    @Mapping(target = "imageUrl", source = "imageUrl")
    @Mapping(target = "adsType", source = "adsType.name")
    @Mapping(target = "adsId", source = "id")
    @Mapping(target = "content", source = "content")
    @Mapping(target = "customer", source = "publisher.username")
    @Mapping(target = "urlCustomer", source = "publisher.avatarPath")
    AdsDto toDto(AdsEntity adsEntity);

    @Mapping(target = "adsType.id", source = "typeId")
    @Mapping(target = "title", source = "title")
    @Mapping(target = "imageUrl", source = "imageUrl")
    @Mapping(target = "content", source = "content")
    @Mapping(target = "link", source = "link")
    @Mapping(target = "status", source = "status")
    @Mapping(target = "id", source = "adsId")
    AdsEntity toEntity(AdsDto adsDto);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "adsId", source = "ads.id")
    @Mapping(target = "placementId", source = "placement.id")
    @Mapping(target = "startDate", source = "startDate")
    @Mapping(target = "endDate", source = "endDate")
    @Mapping(target = "adsType", source = "ads.adsType.name")
    @Mapping(target = "status", source = "ads.status")
    @Mapping(target = "title", source = "ads.title")
    @Mapping(target = "imageUrl", source = "ads.imageUrl")
    @Mapping(target = "typeId", source = "ads.adsType.id")
    @Mapping(target = "cost", source = "cost")
    @Mapping(target = "adsPlacement", source = "placement.name")
    @Mapping(target = "link", source = "ads.link")
    @Mapping(target = "customer", source = "ads.publisher.username")
    @Mapping(target = "urlCustomer", source = "ads.publisher.avatarPath")
    @Mapping(target = "content", source = "ads.content")
    @Mapping(target = "typeDes", source = "ads.adsType.description")
    @Mapping(target = "placementPrice", source = "ads.adsType.price")
    AdsDto toDto(AdsAssignmentEntity adsAssignmentEntity);
}
