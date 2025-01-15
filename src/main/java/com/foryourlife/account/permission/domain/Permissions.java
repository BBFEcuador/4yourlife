package com.foryourlife.account.permission.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Permissions {
    @Id
    private String id;
    private String permissionName;

    protected Permissions() {
    }

    public Permissions(String id, String permissionName) {
        this.id = id;
        this.permissionName = permissionName;
    }

    public String getId() {
        return id;
    }

    public String getPermissionName() {
        return permissionName;
    }
}
