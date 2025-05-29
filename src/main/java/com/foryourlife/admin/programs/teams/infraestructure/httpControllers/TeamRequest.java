package com.foryourlife.admin.programs.teams.infraestructure.httpControllers;

import com.foryourlife.clients.account.participant.domain.Participant;

import java.util.Set;

public class TeamRequest {
    public String id;
    public String name;
    public String photo;
    public String training;
    public Set<Participant> users;
    public String trainer;

    public TeamRequest(String id, String name, String photo, String training, Set<Participant> users, String trainer) {
        this.id = id;
        this.name = name;
        this.photo = photo;
        this.training = training;
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

    public String getTraining() {
        return training;
    }

    public Set<Participant> getUsers() {
        return users;
    }

    public String getTrainer() {
        return trainer;
    }
}
