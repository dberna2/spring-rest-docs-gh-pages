package com.dberna2.springrestdocts.springrestdocs.mapper;

import com.dberna2.springrestdocts.springrestdocs.domain.User;
import com.dberna2.springrestdocts.springrestdocs.dto.UserDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto mapToDto(User user);

    User mapToEntity(UserDto user);
}
