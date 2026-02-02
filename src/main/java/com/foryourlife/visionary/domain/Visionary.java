package com.foryourlife.visionary.domain;

import com.foryourlife.admin.programs.teams.domain.Team;
import com.foryourlife.shared.domain.user.User;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "visionaries")
public class Visionary implements Serializable {
    @Id
    private String id;
    private Boolean isActive = true;
    @OneToOne
    @JoinColumn(name = "user_id",referencedColumnName = "id")
    private User user;

    @ManyToMany(mappedBy = "visionaries", targetEntity = Team.class, fetch = FetchType.EAGER)
    private Set<Team> teams = new HashSet<>();

    protected Visionary() {
    }

    private Visionary(String id, Boolean isActive, User user) {
        this.id = id;
        this.isActive = isActive;
        this.user = user;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public static Visionary create(String id, Boolean isActive, User user){
        return new Visionary(id, isActive, user);
    }
}
