package com.dberna2.springrestdocts.springrestdocs.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {

    private Integer id;
    @NotNull private String name;
    @NotNull private String lastname;
    @NotNull private Integer age;
    @Email @NotNull  private String email;
    private List<AccountDto> accounts;
}
