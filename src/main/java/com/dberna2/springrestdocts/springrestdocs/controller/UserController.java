package com.dberna2.springrestdocts.springrestdocs.controller;

import com.dberna2.springrestdocts.springrestdocs.dto.UserDto;
import com.dberna2.springrestdocts.springrestdocs.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(
        value = "/users",
        produces= APPLICATION_JSON_VALUE,
        consumes = APPLICATION_JSON_VALUE
)
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto newUser)  {
        UserDto user = userService.createUser(newUser);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(user.getId()).toUri();
        return ResponseEntity.created(uri).body(user);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Integer id)  {
        UserDto user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers()  {
        List<UserDto> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<List<UserDto>> deleteUserById(@PathVariable Integer id)  {
        userService.deleteUserById(id);
        return ResponseEntity.accepted().build();
    }
}
