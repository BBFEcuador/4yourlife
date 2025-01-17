package com.foryourlife.account.user.domain;

import com.foryourlife.account.role.domain.Role;
import com.foryourlife.shared.domain.AggregateRoot;
import com.foryourlife.shared.domain.events.UserCreated;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Users extends AggregateRoot {
    @Id
    private String id;
    private String email;
    private String password;
    private String name;
    private String phone;
    @ManyToOne
    @JoinColumn(name = "rol_id", referencedColumnName = "id")
    private Role rol;

    protected Users() {
    }

    private Users(String id, String email, String password, String name, String phone, Role rol) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.rol=rol;
    }

    public static Users create(String id, String email, String password, String name, String phone, Role rol) {
        var user = new Users(id, email, password, name, phone, rol);
        user.record(new UserCreated(id, user));
        return user;
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public Role getRol() {
        return rol;
    }
}
