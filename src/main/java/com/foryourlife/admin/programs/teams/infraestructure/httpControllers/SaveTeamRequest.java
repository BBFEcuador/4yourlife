package com.foryourlife.admin.programs.teams.infraestructure.httpControllers;

import com.foryourlife.admin.programs.teams.domain.Team;
import com.foryourlife.admin.programs.training.domain.Training;
import com.foryourlife.clients.account.user.domain.Participant;

import java.util.Set;

public class SaveTeamRequest {
    private String id;
    private String name;
    private String photo;
    private Training trainingId;
    private Integer trainingNumber;
    private Set<Participant> users;
    private Set<Participant> masterLife;

    public SaveTeamRequest(String id, String name, String photo, Training trainingId, Integer trainingNumber, Set<Participant> users, Set<Participant> masterLife) {
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

    public Integer getTrainingNumber() {
        return trainingNumber;
    }

    public Set<Participant> getUsers() {
        return users;
    }

    public Set<Participant> getMasterLife() {
        return masterLife;
    }

    public Team toDomain() {
        return Team.create(id, name, photo, trainingId, trainingNumber, users, masterLife);
    }
}
