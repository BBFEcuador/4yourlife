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

    private String name1;
    private String name2;
    private String lastname1;
    private String lastname2;

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

    public User(String id, String email, String password, String name1, String name2, String lastname1, String lastname2, String name, String phone, List<UserEntities> entityMap) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name1 = name1;
        this.name2 = name2;
        this.lastname1 = lastname1;
        this.lastname2 = lastname2;
        this.name = name;
        this.phone = phone;
        this.entityMap = entityMap;
    }

    public String getName1() {
        return name1;
    }

    public void setName1(String name1) {
        this.name1 = name1;
    }

    public String getName2() {
        return name2;
    }

    public void setName2(String name2) {
        this.name2 = name2;
    }

    public String getLastname1() {
        return lastname1;
    }

    public void setLastname1(String lastname1) {
        this.lastname1 = lastname1;
    }

    public String getLastname2() {
        return lastname2;
    }

    public void setLastname2(String lastname2) {
        this.lastname2 = lastname2;
    }

    public void setName(String name) {
        this.name = name;
    }

    protected User() {
    }

    public List<UserEntities> getEntityMap() {
        return entityMap;
    }
}
