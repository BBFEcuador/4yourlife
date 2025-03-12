package com.foryourlife.visionary.domain;

import com.foryourlife.admin.programs.teams.domain.Team;
import com.foryourlife.shared.domain.user.User;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "visionaries")
public class Visionary {
    @Id
    private String id;
    private String role;
    private Boolean isActive = true;
    @OneToOne
    @JoinColumn(name = "user_id",referencedColumnName = "id")
    private User user;

    @ManyToMany(mappedBy = "visionaries", targetEntity = Team.class, fetch = FetchType.EAGER)
    private Set<Team> teams = new HashSet<>();

    protected Visionary() {
    }

    private Visionary(String id, String role, Boolean isActive, User user) {
        this.id = id;
        this.role = role;
        this.isActive = isActive;
        this.user = user;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public Set<Team> getTeams() {
        return teams;
    }

    public void changeStatus(){
        this.isActive = !this.isActive;
    }

    public static Visionary create(String id, String role, Boolean isActive, User user){
        return new Visionary(id, role, isActive, user);
    }
}
