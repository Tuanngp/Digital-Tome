package com.fpt.swp391.group6.DigitalTome.mapper;


import com.fpt.swp391.group6.DigitalTome.dto.PaymentDto;
import com.fpt.swp391.group6.DigitalTome.entity.PaymentEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    PaymentMapper INSTANCE = Mappers.getMapper(PaymentMapper.class);

    @Mapping(source = "accountEntity.id", target = "accountId")
    @Mapping(source = "bookEntity.id", target = "bookId")
    PaymentDto toDto(PaymentEntity paymentEntity);

    @Mapping(source = "accountId", target = "accountEntity.id")
    @Mapping(source = "bookId", target = "bookEntity.id")
    PaymentEntity toEntity(PaymentDto paymentDto);
}
