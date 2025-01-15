package com.foryourlife.account.role.domain;

import jakarta.persistence.*;
import com.foryourlife.account.permission.domain.Permissions;

import java.util.Set;

@Entity
@Table(name = "roles")
public class Role {

    @Id
    private String id;
    private String roleName;
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinTable(
            name = "account_roles_permissions",
            joinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id", referencedColumnName = "id"))
    private Set<Permissions> permissions;

    protected Role() {

    }

    private Role(String id, String roleName, Set<Permissions> permissions) {
        this.id = id;
        this.roleName = roleName;
        this.permissions = permissions;
    }

    public String getId() {
        return id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void addPermission(Permissions permission) {
        if (!this.permissions.contains(permission)) {
            this.permissions.add(permission);
        }
    }

    public Role create(String id, String roleName, Set<Permissions> permissions) {
        var newRole = new Role(id, roleName, permissions);
        return newRole;
    }
}
