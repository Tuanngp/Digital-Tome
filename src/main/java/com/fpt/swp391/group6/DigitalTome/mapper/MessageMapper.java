package com.fpt.swp391.group6.DigitalTome.mapper;

import com.fpt.swp391.group6.DigitalTome.dto.MessageDto;
import com.fpt.swp391.group6.DigitalTome.entity.MessageEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MessageMapper {

    @Mapping(source = "sender", target = "sender.username")
    @Mapping(source = "receiver", target = "receiver.username")
    @Mapping(source = "avatarSender", target = "sender.avatarPath")
    MessageEntity toEntity(MessageDto messageDto);

    @Mapping(source = "sender.username", target = "sender")
    @Mapping(source = "receiver.username", target = "receiver")
    @Mapping(source = "sender.avatarPath", target = "avatarSender")
    MessageDto toDto(MessageEntity messageEntity);

    List<MessageDto> toDto(List<MessageEntity> messageEntities);
}
