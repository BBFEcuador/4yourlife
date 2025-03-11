package com.foryourlife.staff.domain;

import com.foryourlife.admin.programs.teams.domain.Team;
import com.foryourlife.shared.domain.user.User;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "staffs")
public class Staff {
    @Id
    private String id;

    private String rol;
    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @ManyToMany(mappedBy = "staffs", targetEntity = Team.class, fetch = FetchType.EAGER)
    private Set<Team> teams = new HashSet<>();

    protected Staff() {

    }

    private Staff(String id, String rol, User user) {
        this.id = id;
        this.rol = rol;
        this.user = user;
    }

    public static Staff create(String id, String rol, User user) {
        return new Staff(id, rol, user);
    }

    public String getId() {
        return id;
    }

    public String getRol() {
        return rol;
    }

    public User getUser() {
        return user;
    }

    public Set<Team> getTeams() {
        return teams;
    }
}
