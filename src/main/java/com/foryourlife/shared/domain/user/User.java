package com.foryourlife.shared.domain.user;

import com.foryourlife.clients.account.invitations.domain.Invitation;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Type;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Entity
@BatchSize(size = 50)
@Table(name = "users")
public class User implements Serializable {
    @Id
    private String id;
    private String email;
    private String password;

    private String name1;

    private String nickname;
    private String name2;
    private String lastname1;
    private String lastname2;

    private String name;
    private String phone;
    @Type(JsonType.class)
    @Column(columnDefinition = "jsonb")
    private List<UserEntities> entityMap;
    @Transient
    private List<Invitation> invitations = new ArrayList<>();

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email.toLowerCase();
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
        this.email = email.toLowerCase();
        this.password = password;
        this.name1 = name1;
        this.name2 = name2;
        this.lastname1 = lastname1;
        this.lastname2 = lastname2;
        this.name = name;
        this.phone = phone;
        this.entityMap = entityMap;
    }

    public User(String id, String email, String password, String name1, String nickname, String name2, String lastname1, String lastname2, String name, String phone, List<UserEntities> entityMap) {
        this.id = id;
        this.email = email.toLowerCase();
        this.password = password;
        this.name1 = name1;
        this.nickname = nickname;
        this.name2 = name2;
        this.lastname1 = lastname1;
        this.lastname2 = lastname2;
        this.name = name;
        this.phone = phone;
        this.entityMap = entityMap;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
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

    public void setEntityMap(List<UserEntities> entityMap) {
        this.entityMap = entityMap;
    }

    public List<UserEntities> getEntityMap() {
        return entityMap;
    }

    public List<Invitation> getInvitations() {
        return invitations;
    }

    public void setInvitations(List<Invitation> invitations) {
        this.invitations = invitations;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
