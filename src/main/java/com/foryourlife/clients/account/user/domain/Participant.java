package com.foryourlife.clients.account.user.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.foryourlife.admin.programs.teams.domain.Team;
import com.foryourlife.clients.account.contact.domain.Contact;
import com.foryourlife.clients.account.medicalRecord.domain.MedicalRecord;
import com.foryourlife.clients.account.module.domain.ClientModule;
import com.foryourlife.clients.account.participantLevel.domain.ParticipantLevel;
import com.foryourlife.clients.account.profileDetails.domain.ProfileDetails;
import com.foryourlife.shared.domain.AggregateRoot;
import com.foryourlife.shared.domain.events.UserCreated;
import com.foryourlife.shared.domain.user.User;
import jakarta.persistence.*;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "participants")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Participant extends AggregateRoot implements Serializable {
    @Id
    private String id;
    @OneToOne()
    @JoinColumn(referencedColumnName = "id", name = "user_id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "participant_level_id", referencedColumnName = "id")
    private ParticipantLevel participantLevel;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(referencedColumnName = "id", name = "profile_id")
    private ProfileDetails profile;
    private String invitationToken;
    private Boolean isLingerer = false;
    @OneToOne(mappedBy = "user", targetEntity = ClientModule.class)
    private ClientModule modules;
    @OneToMany(mappedBy = "user", targetEntity = Contact.class, fetch = FetchType.EAGER)
    private List<Contact> contacts = new ArrayList<>();
    @ManyToMany(mappedBy = "users", targetEntity = Team.class, fetch = FetchType.EAGER)
    private Set<Team> teams = new HashSet<>();
    @OneToOne(mappedBy = "participant",targetEntity = MedicalRecord.class,fetch = FetchType.EAGER)
    @JsonIgnoreProperties("participant")
    private MedicalRecord medicalRecord;

    public Team getTeam() {
        if (teams != null && !teams.isEmpty()) {
            var team = teams.stream().findFirst().get();
            Hibernate.initialize(team.getTrainer());
            Hibernate.initialize(team.getTraining());
            System.out.println(team.getTraining());
            return team;
        }
        return null;
    }

    protected Participant() {
    }

    public ClientModule getModules() {
        return modules;
    }

    public List<Contact> getContacts() {
        return contacts;
    }

    private Participant(String id, User user, ParticipantLevel role, ProfileDetails profile, String invitationToken) {
        this.id = id;
        this.user = user;
        this.participantLevel = role;
        this.profile = profile;
        this.invitationToken = invitationToken;
    }

    public static Participant create(String id, User user, ParticipantLevel role, ProfileDetails profile, String invitationToken) {
        var participant = new Participant(id, user, role, profile, invitationToken);
        participant.record(new UserCreated(id, participant));
        return participant;
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return user.getEmail();
    }

    public String getPassword() {
        return user.getPassword();
    }

    public String getName() {
        return user.getName();
    }

    public String getPhone() {
        return user.getPhone();
    }

    public ParticipantLevel getParticipantLevel() {
        return participantLevel;
    }

    public void setParticipantLevel(ParticipantLevel participantLevel) {
        this.participantLevel = participantLevel;
    }

    public String getRoleId() {
        return participantLevel.getId();
    }

    public ProfileDetails getProfile() {
        return profile;
    }

    public String getInvitationToken() {
        return invitationToken;
    }

    public Boolean getLingerer() {
        return isLingerer;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setProfile(ProfileDetails profile) {
        this.profile = profile;
    }

    public User getUser() {
        return user;
    }

    public void updateLvl(ParticipantLevel newLvl){
        this.participantLevel = newLvl;
    }
    public MedicalRecord getMedicalRecord() {
        return medicalRecord;
    }
}
