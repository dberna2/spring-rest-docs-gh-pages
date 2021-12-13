package com.dberna2.springrestdocts.springrestdocs.dto;

import lombok.Data;

@Data
public class UserDto {

    private Integer id;
    private String name;
    private String lastname;
    private Integer age;
    private String email;
}
