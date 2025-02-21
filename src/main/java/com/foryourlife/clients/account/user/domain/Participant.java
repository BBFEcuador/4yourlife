package com.foryourlife.clients.account.user.domain;

import com.foryourlife.admin.programs.teams.domain.Team;
import com.foryourlife.clients.account.contact.domain.Contact;
import com.foryourlife.clients.account.module.domain.ClientModule;
import com.foryourlife.clients.account.participantLevel.domain.ParticipantLevel;
import com.foryourlife.clients.account.profileDetails.domain.ProfileDetails;
import com.foryourlife.shared.domain.AggregateRoot;
import com.foryourlife.shared.domain.events.UserCreated;
import jakarta.persistence.*;
import org.hibernate.Hibernate;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "participants")
public class Participant extends AggregateRoot {
    @Id
    private String id;
    private String email;
    private String password;
    private String name;
    private String phone;
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
    @OneToMany(mappedBy = "user", targetEntity = Contact.class,fetch = FetchType.EAGER)
    private List<Contact> contacts = new ArrayList<>();
    @ManyToMany(mappedBy = "users", targetEntity = Team.class,fetch = FetchType.EAGER)
    private Set<Team> teams;

    public Team getTeam() {
        if (teams != null && !teams.isEmpty()) {
            var team = teams.stream().findFirst().get();
            Hibernate.initialize(team.getTrainer());
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

    private Participant(String id, String email, String password, String name, String phone, ParticipantLevel role, ProfileDetails profile, String invitationToken) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.participantLevel = role;
        this.profile = profile;
        this.invitationToken = invitationToken;
    }

    public static Participant create(String id, String email, String password, String name, String phone, ParticipantLevel role, ProfileDetails profile, String invitationToken) {
        var user = new Participant(id, email, password, name, phone, role, profile, invitationToken);
        user.record(new UserCreated(id, user));
        return user;
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
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
}
