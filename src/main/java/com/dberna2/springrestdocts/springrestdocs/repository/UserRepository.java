package com.dberna2.springrestdocts.springrestdocs.repository;

import com.dberna2.springrestdocts.springrestdocs.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);
}
