package com.readbook.ReadBook.mapper;

import com.readbook.ReadBook.dto.UserDto;
import com.readbook.ReadBook.entity.AccountEntity;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-05-27T18:08:22+0700",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.10 (Oracle Corporation)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public AccountEntity toUSer(UserDto userDto) {
        if ( userDto == null ) {
            return null;
        }

        AccountEntity accountEntity = new AccountEntity();

        accountEntity.setId( userDto.getId() );
        accountEntity.setUsername( userDto.getUsername() );
        accountEntity.setPassword( userDto.getPassword() );
        accountEntity.setEmail( userDto.getEmail() );

        return accountEntity;
    }

    @Override
    public List<UserDto> toUserDto(List<AccountEntity> user) {
        if ( user == null ) {
            return null;
        }

        List<UserDto> list = new ArrayList<UserDto>( user.size() );
        for ( AccountEntity accountEntity : user ) {
            list.add( accountEntityToUserDto( accountEntity ) );
        }

        return list;
    }

    protected UserDto accountEntityToUserDto(AccountEntity accountEntity) {
        if ( accountEntity == null ) {
            return null;
        }

        UserDto userDto = new UserDto();

        userDto.setId( accountEntity.getId() );
        userDto.setUsername( accountEntity.getUsername() );
        userDto.setEmail( accountEntity.getEmail() );
        userDto.setPassword( accountEntity.getPassword() );

        return userDto;
    }
}
