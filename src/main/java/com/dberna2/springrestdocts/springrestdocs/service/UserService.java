package com.dberna2.springrestdocts.springrestdocs.service;

import com.dberna2.springrestdocts.springrestdocs.dto.AccountDto;
import com.dberna2.springrestdocts.springrestdocs.dto.UserDto;

import java.util.List;

public interface UserService {

    UserDto getUserById(Long id);

    List<UserDto> getAllUsers();

    UserDto createUser(UserDto newUser);

    void deleteUserById(Long id);

    List<AccountDto> getUserAccounts(Long id);

    AccountDto createUserAccount(Long id, AccountDto newUserAccount);

    void deleteUserAccount(Long id, Long accountId);
}
