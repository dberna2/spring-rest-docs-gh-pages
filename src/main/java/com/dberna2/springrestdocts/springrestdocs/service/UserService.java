package com.dberna2.springrestdocts.springrestdocs.service;

import com.dberna2.springrestdocts.springrestdocs.dto.UserDto;

import java.util.List;

public interface UserService {

    UserDto getUserById(Integer id);

    List<UserDto> getAllUsers();

    UserDto createUser(UserDto newUser);

    void deleteUserById(Integer id);
}
