package com.dberna2.springrestdocts.springrestdocs.mapper;

import com.dberna2.springrestdocts.springrestdocs.domain.Account;
import com.dberna2.springrestdocts.springrestdocs.domain.User;
import com.dberna2.springrestdocts.springrestdocs.dto.AccountDto;
import com.dberna2.springrestdocts.springrestdocs.dto.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "accounts", ignore = true)
    UserDto mapToUserDto(User user);

    User mapToUserEntity(UserDto user);

    AccountDto mapToAccountDto(Account account);

    @Mapping(source = "userId", target = "user.id")
    Account mapToAccountEntity(Integer userId, AccountDto account);
}
