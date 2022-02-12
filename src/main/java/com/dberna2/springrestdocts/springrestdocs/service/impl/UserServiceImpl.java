package com.dberna2.springrestdocts.springrestdocs.service.impl;

import com.dberna2.springrestdocts.springrestdocs.domain.User;
import com.dberna2.springrestdocts.springrestdocs.dto.AccountDto;
import com.dberna2.springrestdocts.springrestdocs.dto.UserDto;
import com.dberna2.springrestdocts.springrestdocs.exection.UserNotFoundException;
import com.dberna2.springrestdocts.springrestdocs.mapper.UserMapper;
import com.dberna2.springrestdocts.springrestdocs.repository.AccountRepository;
import com.dberna2.springrestdocts.springrestdocs.repository.UserRepository;
import com.dberna2.springrestdocts.springrestdocs.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final UserMapper userMapper;

    @Override
    public UserDto createUser(UserDto newUser) {
        User user = userMapper.mapToUserEntity(newUser);
        userRepository.save(user);
        return userMapper.mapToUserDto(user);
    }

    @Override
    public void deleteUserById(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);
        userRepository.delete(user);
    }

    @Override
    public List<AccountDto> getUserAccounts(Integer id) {
         User user = userRepository.findById(id)
                 .orElseThrow(UserNotFoundException::new);
         return accountRepository.getAccountByUser(user).stream()
                .map(userMapper::mapToAccountDto).collect(toList());
    }

    @Override
    public AccountDto createUserAccount(Integer id, AccountDto newUserAccount) {
        return userRepository.findById(id)
                .map(user -> userMapper.mapToAccountEntity(user.getId(), newUserAccount))
                .map(accountRepository::save)
                .map(userMapper::mapToAccountDto)
                .orElseThrow(UserNotFoundException::new);
    }

    @Override
    public void deleteUserAccount(Integer id, Integer accountId) {
        if(userRepository.findById(id).isEmpty()) {
         throw new UserNotFoundException();
        }
        accountRepository.deleteById(accountId);
    }

    @Override
    public UserDto getUserById(Integer id) {
        return userRepository.findById(id)
                .map(userMapper::mapToUserDto)
                .orElseThrow(UserNotFoundException::new);
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::mapToUserDto).collect(toList());
    }
}
