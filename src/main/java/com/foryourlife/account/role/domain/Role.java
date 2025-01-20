package com.foryourlife.account.role.domain;

import jakarta.persistence.*;
import com.foryourlife.account.permission.domain.Permissions;

import java.util.Set;

@Entity
@Table(name = "roles")
public class Role {

    @Id
    private String id;
    @Column(name = "roleName")
    private String roleName;
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinTable(
            name = "roles_permissions",
            joinColumns = @JoinColumn(name = "roleId", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "permissionId", referencedColumnName = "id"))
    private Set<Permissions> permissions;
    private Boolean isStarted;

    private Role() {

    }

    private Role(String id, String roleName, Set<Permissions> permissions, Boolean isStarted) {
        this.id = id;
        this.roleName = roleName;
        this.permissions = permissions;
        this.isStarted = isStarted;
    }

    public String getId() {
        return id;
    }

    public String getRoleName() {
        return roleName;
    }

    public Set<Permissions> getPermissions() {
        return permissions;
    }

    public Boolean getStarted() {
        return isStarted;
    }

    public void addPermission(Permissions permission) {
        this.permissions.add(permission);
    }

    public static Role create(String id, String roleName, Set<Permissions> permissions, Boolean isStarted) {
        return new Role(id, roleName, permissions, isStarted);
    }
}
