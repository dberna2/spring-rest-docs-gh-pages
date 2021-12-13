package com.dberna2.springrestdocts.springrestdocs.service.impl;

import com.dberna2.springrestdocts.springrestdocs.domain.User;
import com.dberna2.springrestdocts.springrestdocs.dto.UserDto;
import com.dberna2.springrestdocts.springrestdocs.mapper.UserMapper;
import com.dberna2.springrestdocts.springrestdocs.repository.UserRepository;
import com.dberna2.springrestdocts.springrestdocs.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserDto createUser(UserDto newUser) {
        User user = userMapper.mapToEntity(newUser);
        userRepository.save(user);
        return userMapper.mapToDto(user);
    }

    @Override
    public void deleteUserById(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("User not found"));
        userRepository.delete(user);
    }

    @Override
    public UserDto getUserById(Integer id) {
        return userRepository.findById(id)
                .map(userMapper::mapToDto)
                .orElseThrow(()-> new RuntimeException("User not found"));
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::mapToDto)
                .collect(Collectors.toList());
    }
}
