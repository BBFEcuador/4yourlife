package com.foryourlife.masterLife.domain;

import com.foryourlife.admin.programs.teams.domain.Team;
import com.foryourlife.shared.domain.user.User;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "master_life")
public class MasterLife {
    @Id
    private String id;

    private Boolean isActive = true;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @ManyToMany(mappedBy = "staffs", targetEntity = Team.class, fetch = FetchType.EAGER)
    private Set<Team> teams = new HashSet<>();


    protected MasterLife() {
    }

    public MasterLife(String id, Boolean isActive, User user) {
        this.id = id;
        this.isActive = isActive;
        this.user = user;
    }

    public String getId() {
        return id;
    }

    public Boolean getActive() {
        return isActive;
    }

    public User getUser() {
        return user;
    }

    public Set<Team> getTeams() {
        return teams;
    }
}
