package com.foryourlife.shared.domain.user;

import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.Type;

import java.util.List;
import java.util.Map;

@Entity
@Table(name = "users")
public class User {
    @Id
    private String id;
    private String email;
    private String password;
    private String name;
    private String phone;
    @Type(JsonType.class)
    @Column(columnDefinition = "jsonb")
    private List<UserEntities> entityMap;

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public User(String id, String email, String password, String name, String phone, List<UserEntities> entityMap) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.entityMap = entityMap;
    }

    protected User() {
    }
    public List<UserEntities> getEntityMap() {
        return entityMap;
    }
}
