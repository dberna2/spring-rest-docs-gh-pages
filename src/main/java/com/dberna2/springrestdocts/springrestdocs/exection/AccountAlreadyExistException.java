package com.dberna2.springrestdocts.springrestdocs.exection;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class AccountAlreadyExistException extends RuntimeException {

    public AccountAlreadyExistException() {
        super("Account already exist");
    }
}
