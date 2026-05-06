package com.foryourlife.admin.crm.call.domain;

import com.foryourlife.admin.crm.callLogs.domain.CallLog;
import com.foryourlife.admin.programs.training.domain.Training;
import com.foryourlife.clients.account.invitations.domain.Invitation;
import com.foryourlife.clients.account.participant.domain.Participant;

import java.util.ArrayList;
import java.util.List;

public class CallResponse {

    private String id;
    private Participant participant;
    private Invitation invitation;
    private Training training;
    private List<CallLog> callLogs = new ArrayList<>();

    public CallResponse() {
    }

    public CallResponse(String id, Participant participant, Invitation invitation, Training training, List<CallLog> callLogs) {
        this.id = id;
        this.participant = participant;
        this.invitation = invitation;
        this.training = training;
        this.callLogs = callLogs;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Participant getParticipant() {
        return participant;
    }

    public void setParticipant(Participant participant) {
        this.participant = participant;
    }

    public Invitation getInvitation() {
        return invitation;
    }

    public void setInvitation(Invitation invitation) {
        this.invitation = invitation;
    }

    public Training getTraining() {
        return training;
    }

    public void setTraining(Training training) {
        this.training = training;
    }

    public List<CallLog> getCallLogs() {
        return callLogs;
    }

    public void setCallLogs(List<CallLog> callLogs) {
        this.callLogs = callLogs;
    }
}
