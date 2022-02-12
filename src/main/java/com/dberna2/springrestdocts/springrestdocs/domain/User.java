package com.dberna2.springrestdocts.springrestdocs.domain;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@ToString
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String lastname;
    private Integer age;
    private String email;

    @ToString.Exclude
    @OneToMany(mappedBy="user")
    private List<Account> accounts;

}
