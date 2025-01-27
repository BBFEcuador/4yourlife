package com.foryourlife.admin.programs.teams.domain;

import com.foryourlife.admin.programs.training.domain.Training;
import com.foryourlife.clients.account.user.domain.Users;
import com.foryourlife.shared.domain.AggregateRoot;
import com.foryourlife.shared.domain.events.TeamCreated;
import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "teams")
public class Team extends AggregateRoot {

    @Id
    private String id;
    private String name;
    private String photo;
    @ManyToOne
    @JoinColumn(name = "training_id", referencedColumnName = "id")
    private Training trainingId;
    @ManyToOne
    @JoinColumn(name = "training_number", referencedColumnName = "number")
    private Training trainingNumber;

    @ManyToMany
    @JoinTable(
            name = "team_users",
            joinColumns = @JoinColumn(name = "team_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "users_id", referencedColumnName = "id"))
    private Set<Users> users;
    @ManyToMany
    @JoinTable(
            name = "team_master_life",
            joinColumns = @JoinColumn(name = "team_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "masterlife_id", referencedColumnName = "id"))
    private Set<Users> masterLife;

    protected Team() {
    }

    private Team(String id, String name, String photo, Training trainingId, Training trainingNumber, Set<Users> users, Set<Users> masterLife) {
        this.id = id;
        this.name = name;
        this.photo = photo;
        this.trainingId = trainingId;
        this.trainingNumber = trainingNumber;
        this.users = users;
        this.masterLife = masterLife;
    }

    public static Team create(String id, String name, String photo, Training trainingId, Training trainingNumber, Set<Users> users, Set<Users> masterLife) {
        var team = new Team(id, name, photo, trainingId, trainingNumber, users, masterLife);
        team.record(new TeamCreated(id, team));
        return team;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhoto() {
        return photo;
    }

    public Training getTrainingId() {
        return trainingId;
    }

    public Training getTrainingNumber() {
        return trainingNumber;
    }

    public Set<Users> getUsers() {
        return users;
    }

    public Set<Users> getMasterLife() {
        return masterLife;
    }

    public void setUsers(Set<Users> users) {
        this.users = users;
    }

    public void setMasterLife(Set<Users> masterLife) {
        this.masterLife = masterLife;
    }

    //id, nombre, foto,training id, training number, set<users>, set<users>masterlife,
    //crear, asignar quitar user actualizar
}
