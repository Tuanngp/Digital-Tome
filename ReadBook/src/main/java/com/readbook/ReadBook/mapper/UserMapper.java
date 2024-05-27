package com.readbook.ReadBook.mapper;


import com.readbook.ReadBook.dto.UserDto;
import com.readbook.ReadBook.entity.AccountEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    AccountEntity toUSer(UserDto userDto);
    List<UserDto> toUserDto(List<AccountEntity> user);
}
