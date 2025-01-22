package com.foryourlife.clients.account.participantLevel.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "participant_level")
public class ParticipantLevel {

    @Id
    private String id;
    @Column(name = "roleName")
    private String roleName;
    private Boolean isStarted;

    private ParticipantLevel() {

    }

    private ParticipantLevel(String id, String roleName, Boolean isStarted) {
        this.id = id;
        this.roleName = roleName;
        this.isStarted = isStarted;
    }

    public String getId() {
        return id;
    }

    public String getRoleName() {
        return roleName;
    }

    public Boolean getStarted() {
        return isStarted;
    }

    public static ParticipantLevel create(String id, String roleName, Boolean isStarted) {
        return new ParticipantLevel(id, roleName, isStarted);
    }
}
