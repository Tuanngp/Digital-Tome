package com.fpt.swp391.group6.DigitalTome.mapper;


import com.fpt.swp391.group6.DigitalTome.dto.UserDto;
import com.fpt.swp391.group6.DigitalTome.entity.AccountEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    AccountEntity toUSer(UserDto userDto);
    List<UserDto> toUserDto(List<AccountEntity> user);
}
