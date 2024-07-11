package com.fpt.swp391.group6.DigitalTome.mapper;


import com.fpt.swp391.group6.DigitalTome.dto.UserDto;
import com.fpt.swp391.group6.DigitalTome.dto.RegisterDto;
import com.fpt.swp391.group6.DigitalTome.entity.AccountEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface UserMapper {

    AccountEntity toUSer(RegisterDto registerDto);

    AccountEntity toProfile(UserDto userDto);

    List<RegisterDto> toUserDto(List<AccountEntity> user);

    @Mapping(source = "fullname", target = "fullName")
    @Mapping(source = "username", target = "username")
    UserDto toDto(AccountEntity account);

    List<UserDto> toDto(List<AccountEntity> account);
}
