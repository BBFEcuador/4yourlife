package com.foryourlife.admin.programs.trainer.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.foryourlife.admin.programs.teams.domain.Team;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "trainers")
public class Trainer {
    @Id
    private String id;

    private String name;

    private String email;

    private String phone;

    private String password;

    private Boolean isActive;

    @JsonIgnoreProperties({"trainer"})
    @OneToMany(mappedBy = "trainer", fetch = FetchType.LAZY)
    private List<Team> teams;

    protected Trainer() {
    }

    private Trainer(String id, String name, String email, String phone, String password, Boolean isActive) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.isActive = isActive;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public static Trainer create(String id, String name, String email, String phone, String password, Boolean isActive) {
        return new Trainer(id, name, email, phone, password, isActive);
    }

    public List<Team> getTeams() {
        return teams;
    }

    public void setTeams(List<Team> teams) {
        this.teams = teams;
    }
}
