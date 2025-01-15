package com.foryourlife.account.permission.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "permissions")
public class Permissions {
    @Id
    private String id;
    @Column(name = "permissionName",columnDefinition = "permissionName")
    private String permissionName;

    protected Permissions() {
    }

    private Permissions(String id, String permissionName) {
        this.id = id;
        this.permissionName = permissionName;
    }

    public String getId() {
        return id;
    }

    public String getPermissionName() {
        return permissionName;
    }

    public static Permissions create (String id, String permissionName){
        var newPermission = new Permissions(id, permissionName);
        return newPermission;
    }
}
