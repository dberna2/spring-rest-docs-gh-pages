package com.dberna2.springrestdocts.springrestdocs.repository;

import com.dberna2.springrestdocts.springrestdocs.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
}
