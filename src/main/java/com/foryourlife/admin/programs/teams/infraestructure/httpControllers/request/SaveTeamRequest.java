package com.foryourlife.admin.programs.teams.infraestructure.httpControllers.request;

import com.foryourlife.admin.programs.teams.domain.Team;
import com.foryourlife.admin.programs.trainer.domain.Trainer;
import com.foryourlife.admin.programs.training.domain.Training;
import com.foryourlife.clients.account.participant.domain.Participant;

import java.util.List;

public class SaveTeamRequest {
    public String id;
    public String name;
    public String photo;
    public Training training;
    public Integer trainingNumber;
    public List<Participant> users;
    public Trainer trainer;

    public SaveTeamRequest(
            String id,
            String name,
            String photo,
            Training training,
            Integer trainingNumber,
            List<Participant> users,
            Trainer trainer
    ) {
        this.id = id;
        this.name = name;
        this.photo = photo;
        this.training = training;
        this.trainingNumber = trainingNumber;
        this.users = users;
        this.trainer = trainer;
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

    public Training getTraining() {
        return training;
    }

    public Integer getTrainingNumber() {
        return trainingNumber;
    }

    public List<Participant> getUsers() {
        return users;
    }

    public Trainer getTrainer() {
        return trainer;
    }

    public Team toDomain() {
        return null;
    }
}
