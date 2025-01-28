package com.foryourlife.admin.programs.teams.infraestructure.httpControllers;

import com.foryourlife.admin.programs.teams.domain.Team;
import com.foryourlife.admin.programs.training.domain.Training;
import com.foryourlife.clients.account.user.domain.Users;
import jakarta.persistence.*;

import java.util.Set;

public class SaveTeamRequest {
    private String id;
    private String name;
    private String photo;
    private Training trainingId;
    private Training trainingNumber;
    private Set<Users> users;
    private Set<Users> masterLife;

    public SaveTeamRequest(String id, String name, String photo, Training trainingId, Training trainingNumber, Set<Users> users, Set<Users> masterLife) {
        this.id = id;
        this.name = name;
        this.photo = photo;
        this.trainingId = trainingId;
        this.trainingNumber = trainingNumber;
        this.users = users;
        this.masterLife = masterLife;
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

    public Team toDomain() {
        return Team.create(id, name, photo, trainingId, trainingNumber, users, masterLife);
    }
}
