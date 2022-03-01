package com.dberna2.springrestdocts.springrestdocs.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Data
public class AccountDto {

    private Long id;
    @NotNull
    private AccountType type;
    @NotNull
    @Size(min = 16, max = 16)
    private String number;
    @PositiveOrZero
    private BigDecimal balance;
}
