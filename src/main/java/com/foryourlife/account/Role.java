package com.foryourlife.account;

import jakarta.persistence.Entity;

@Entity
public class Role {

    private String id;
    private String roleName;

    protected Role() {

    }

    private Role(String id, String roleName) {
        this.id = id;
        this.roleName = roleName;
    }

    public String getId() {
        return id;
    }

    public String getRoleName() {
        return roleName;
    }

    public Role create(String id, String roleName) {
        var newRole = new Role(id, roleName);
        return newRole;
    }
}
