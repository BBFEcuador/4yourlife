package com.foryourlife.clients.account.invitations.domain;

import java.io.Serializable;

public class Sender implements Serializable {
    private String id;
    private String name;
    private String trainingName = null;
    private String contact;

    protected Sender() {
    }

    public Sender(String id, String name, String trainingName, String contact) {
        this.id = id;
        this.name = name;
        this.trainingName = trainingName;
        this.contact = contact;
    }

    public String getName() {
        return name;
    }

    public String getContact() {
        return contact;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getTrainingName() {
        return trainingName;
    }

    public void setTrainingName(String trainingName) {
        this.trainingName = trainingName;
    }
}
