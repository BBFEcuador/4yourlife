package com.foryourlife.clients.account.participant.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.foryourlife.admin.programs.campus.domain.Campus;
import com.foryourlife.admin.programs.teams.domain.Team;
import com.foryourlife.clients.account.contact.domain.Contact;
import com.foryourlife.clients.account.invitations.domain.Invitation;
import com.foryourlife.clients.account.medicalRecord.domain.MedicalRecord;
import com.foryourlife.clients.account.module.domain.ClientModule;
import com.foryourlife.clients.account.participantLevel.domain.ParticipantLevel;
import com.foryourlife.clients.account.profileDetails.domain.ProfileDetails;
import com.foryourlife.shared.domain.events.UserCreated;
import com.foryourlife.shared.domain.user.User;
import com.foryourlife.shared.infrastructure.auditable.AuditableEntity;
import jakarta.persistence.*;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.*;

@Entity
@Table(name = "participants")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Participant extends AuditableEntity implements Serializable {
    @Id
    private String id;
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(referencedColumnName = "id", name = "user_id")
    @JsonIgnoreProperties("invitations")
    private User user;
    @ManyToOne
    @JoinColumn(name = "participant_level_id", referencedColumnName = "id")
    private ParticipantLevel participantLevel;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(referencedColumnName = "id", name = "profile_id")
    private ProfileDetails profile;
    @ManyToOne()
    @JoinColumn(referencedColumnName = "id", name = "campus_id")
    private Campus campus;
    private String invitationToken;
    private Boolean isLingerer = false;
    private Boolean isDesertor = false;
    @OneToOne(mappedBy = "user")
    private ClientModule modules;
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    @JsonIgnoreProperties("user")
    private List<Contact> contacts = new ArrayList<>();
    @ManyToMany(mappedBy = "users", fetch = FetchType.EAGER)
    private Set<Team> teams = new HashSet<>();
    @OneToOne(mappedBy = "participant", fetch = FetchType.EAGER)
    @JsonIgnoreProperties("participant")
    private MedicalRecord medicalRecord;

    public Team getTeam() {
        if (teams != null && !teams.isEmpty()) {
            var team = teams.stream().findFirst().get();
            Hibernate.initialize(team.getTrainer());
            Hibernate.initialize(team.getTraining());
            return team;
        }
        return null;
    }

    protected Participant() {
    }

    public void setIsLingerer(Boolean lingerer) {
        isLingerer = lingerer;
    }

    public Campus getCampus() {
        return campus;
    }

    public void setCampus(Campus campus) {
        this.campus = campus;
    }

    public Boolean getIsDesertor() {
        return isDesertor;
    }

    public void setIsDesertor(Boolean desertor) {
        isDesertor = desertor;
    }

    public ClientModule getModules() {
        return modules;
    }

    public List<Contact> getContacts() {
        return contacts;
    }

    private Participant(String id, User user, ParticipantLevel role, ProfileDetails profile, String invitationToken, Boolean isLingerer, Boolean isDesertor, Campus campus) {
        this.id = id;
        this.user = user;
        this.participantLevel = role;
        this.profile = profile;
        this.invitationToken = invitationToken;
        this.isLingerer = isLingerer;
        this.isDesertor = isDesertor;
        this.campus = campus;
    }

    public static Participant create(String id, User user, ParticipantLevel role, ProfileDetails profile, String invitationToken, Boolean isLingerer, Boolean isDesertor, Campus campus) {
        var participant = new Participant(id, user, role, profile, invitationToken, isLingerer, isDesertor, campus);
        participant.record(new UserCreated(id, participant));
        return participant;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    public void setProfile(ProfileDetails profile) {
        this.profile = profile;
    }

    public String getInvitationToken() {
        return invitationToken;
    }

    public void setInvitationToken(String invitationToken) {
        this.invitationToken = invitationToken;
    }

    public Boolean getIsLingerer() {
        return isLingerer;
    }

    public void setLingerer(Boolean lingerer) {
        isLingerer = lingerer;
    }

    public Boolean getDesertor() {
        return isDesertor;
    }

    public void setDesertor(Boolean desertor) {
        isDesertor = desertor;
    }

    public void setModules(ClientModule modules) {
        this.modules = modules;
    }

    public void setContacts(List<Contact> contacts) {
        this.contacts = contacts;
    }

    public void updateLvl(ParticipantLevel newLvl) {
        this.participantLevel = newLvl;
    }

    public Set<Team> getTeams() {
        return teams;
    }

    public void setTeams(Set<Team> teams) {
        this.teams = teams;
    }

    public MedicalRecord getMedicalRecord() {
        return medicalRecord;
    }

    public void setMedicalRecord(MedicalRecord medicalRecord) {
        this.medicalRecord = medicalRecord;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Participant that = (Participant) o;
        return Objects.equals(id, that.id) && Objects.equals(user, that.user) && Objects.equals(participantLevel, that.participantLevel) && Objects.equals(profile, that.profile) && Objects.equals(campus, that.campus) && Objects.equals(invitationToken, that.invitationToken) && Objects.equals(isLingerer, that.isLingerer) && Objects.equals(isDesertor, that.isDesertor) && Objects.equals(modules, that.modules) && Objects.equals(contacts, that.contacts) && Objects.equals(teams, that.teams) && Objects.equals(medicalRecord, that.medicalRecord);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, participantLevel, profile, campus, invitationToken, isLingerer, isDesertor, modules, contacts, teams, medicalRecord);
    }

    @Override
    public String toString() {
        return "Participant{" +
                "id='" + id + '\'' +
                '}';
    }
}
