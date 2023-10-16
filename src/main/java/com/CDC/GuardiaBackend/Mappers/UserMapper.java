package com.CDC.GuardiaBackend.Mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.CDC.GuardiaBackend.Entities.User;
import com.CDC.GuardiaBackend.dtos.SignUpDto;
import com.CDC.GuardiaBackend.dtos.UserDto;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto toUserDto(User user);
    @Mapping(target = "password", ignore = true)
    User signUpToUser(SignUpDto signUpDto);

}
