package com.foryourlife.shared.domain.user;

import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
@Inheritance(strategy= InheritanceType.TABLE_PER_CLASS)
public abstract class User {
    private String id;
    private String email;
    private String password;
    private String name;
    private String phone;
}
