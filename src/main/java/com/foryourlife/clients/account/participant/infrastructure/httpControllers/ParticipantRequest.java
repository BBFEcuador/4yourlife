package com.foryourlife.clients.account.participant.infrastructure.httpControllers;

import com.foryourlife.admin.programs.campus.domain.Campus;
import com.foryourlife.clients.account.participantLevel.domain.ParticipantLevel;
import com.foryourlife.clients.account.profileDetails.domain.ProfileDetails;
import com.foryourlife.clients.account.participant.domain.Participant;
import com.foryourlife.shared.domain.user.User;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;

public class ParticipantRequest {

    @Null
    private String id;
    @NotNull
    private User user;
    @NotNull
    private ParticipantLevel participantLevel;
    @NotNull
    private ProfileDetails profile;
    @NotNull
    private String invitationToken;
    @NotNull
    private Boolean isLingerer;
    @NotNull
    private Boolean isDesertor;

    private Campus campus;

    public String getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public ParticipantLevel getParticipantLevel() {
        return participantLevel;
    }

    public ProfileDetails getProfile() {
        return profile;
    }

    public String getInvitationToken() {
        return invitationToken;
    }

    public Boolean getIsLingerer() {
        return isLingerer;
    }

    public Boolean getIsDesertor() {
        return isDesertor;
    }

    public Campus getCampus() {
        return campus;
    }

    public void setCampus(Campus campus) {
        this.campus = campus;
    }

    public Participant toDomain() {
        return Participant.create(id, user, participantLevel, profile, invitationToken, isLingerer, isDesertor, campus != null ? campus : null);
    }
}
