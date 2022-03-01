package com.dberna2.springrestdocts.springrestdocs.service.impl;

import com.dberna2.springrestdocts.springrestdocs.domain.Account;
import com.dberna2.springrestdocts.springrestdocs.domain.User;
import com.dberna2.springrestdocts.springrestdocs.dto.AccountDto;
import com.dberna2.springrestdocts.springrestdocs.dto.UserDto;
import com.dberna2.springrestdocts.springrestdocs.exection.AccountAlreadyExistException;
import com.dberna2.springrestdocts.springrestdocs.exection.UserAlreadyExistException;
import com.dberna2.springrestdocts.springrestdocs.exection.UserNotFoundException;
import com.dberna2.springrestdocts.springrestdocs.mapper.UserMapper;
import com.dberna2.springrestdocts.springrestdocs.repository.AccountRepository;
import com.dberna2.springrestdocts.springrestdocs.repository.UserRepository;
import com.dberna2.springrestdocts.springrestdocs.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final AccountRepository accountRepository;
  private final UserMapper userMapper;

  @Override
  public UserDto createUser(UserDto newUser) {

    if (userRepository.existsByEmail(newUser.getEmail())) {
      throw new UserAlreadyExistException();
    }

    User user = userMapper.mapToUserEntity(newUser);
    userRepository.save(user);
    return userMapper.mapToUserDto(user);
  }

  @Override
  public void deleteUserById(Long id) {
    User user = userRepository.findById(id)
            .orElseThrow(UserNotFoundException::new);

    userRepository.delete(user);
  }

  @Override
  public List<AccountDto> getUserAccounts(Long id) {
    User user = userRepository.findById(id)
            .orElseThrow(UserNotFoundException::new);

    return accountRepository.getAccountByUser(user).stream()
        .map(userMapper::mapToAccountDto)
        .collect(toList());
  }

  @Override
  public AccountDto createUserAccount(Long id, AccountDto newUserAccount) {

    if (accountRepository.existsAccountByNumber(newUserAccount.getNumber())) {
      throw new AccountAlreadyExistException();
    }

    Account account = userRepository.findById(id)
            .map(user -> userMapper.mapToAccountEntity(user.getId(), newUserAccount))
            .orElseThrow(UserNotFoundException::new);

    accountRepository.save(account);

    return userMapper.mapToAccountDto(account);
  }

  @Override
  public void deleteUserAccount(Long id, Long accountId) {
    if (userRepository.findById(id).isEmpty()) {
      throw new UserNotFoundException();
    }
    accountRepository.deleteById(accountId);
  }

  @Override
  public UserDto getUserById(Long id) {
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
