package com.foryourlife.staff.infrastructure.httpControllers;

import com.foryourlife.admin.programs.training.domain.Training;
import com.foryourlife.clients.account.user.domain.Participant;
import com.foryourlife.shared.domain.user.User;
import com.foryourlife.staff.domain.Staff;

public class StaffRequest {

    private String id;

    private Training training;

    private Participant participant;

    private User user;

    public StaffRequest(String id, Training training, Participant participant, User user) {
        this.id = id;
        this.training = training;
        this.participant = participant;
        this.user = user;
    }

    public String getId() {
        return id;
    }

    public Training getTraining() {
        return training;
    }

    public Participant getParticipant() {
        return participant;
    }

    public User getUser() {
        return user;
    }

    public Staff toDomain(){
        return Staff.create(id, training, participant, user);
    }
}
