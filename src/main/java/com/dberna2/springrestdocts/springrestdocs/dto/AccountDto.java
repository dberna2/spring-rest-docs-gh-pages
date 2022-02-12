package com.dberna2.springrestdocts.springrestdocs.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class AccountDto {

    private Integer id;
    @NotNull private String type;
    @NotNull private String number;
}
