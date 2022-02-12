package com.dberna2.springrestdocts.springrestdocs.repository;

import com.dberna2.springrestdocts.springrestdocs.domain.Account;
import com.dberna2.springrestdocts.springrestdocs.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {

    List<Account> getAccountByUser(User user);
}
