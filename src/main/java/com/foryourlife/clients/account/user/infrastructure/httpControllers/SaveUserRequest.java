package com.foryourlife.clients.account.user.infrastructure.httpControllers;

import com.foryourlife.clients.account.participantLevel.domain.ParticipantLevel;
import com.foryourlife.clients.account.profileDetails.infrastructure.ProfileDetailRequest;
import com.foryourlife.clients.account.user.domain.Participant;
import com.foryourlife.shared.domain.user.User;
import com.foryourlife.shared.domain.user.UserEntities;
import com.foryourlife.shared.domain.user.UserType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.util.List;
import java.util.UUID;

public class SaveUserRequest {
    public String id;
    @NotNull
    @Email
    @NotBlank(message = "The User/Email field is required")
    public String email;
    @NotNull
    @NotBlank(message = "The password field is required")
    public String password;
    @NotNull
    @NotBlank(message = "The token field is required")
    public String token;
    @NotNull
    @NotBlank(message = "The name field is required")
    public String name;
    @NotNull
    @NotBlank(message = "The phone field is required")
    @Pattern(regexp = "^[0-9]*$", message = "The phone field only accept digits")
    public String phone;
    @NotNull
    @Valid
    public ProfileDetailRequest profile;
    @NotNull
    @Valid
    public MedicalRecordSaveRequest medicalRecord;

    public SaveUserRequest(String id, String email, String password, String token, String name, String phone, ProfileDetailRequest profile, MedicalRecordSaveRequest medicalRecord) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.token = token;
        this.name = name;
        this.phone = phone;
        this.profile = profile;
        this.medicalRecord = medicalRecord;
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

    public String getToken() {
        return token;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public ProfileDetailRequest getProfile() {
        return profile;
    }

    public MedicalRecordSaveRequest getMedicalRecord() {
        return medicalRecord;
    }

    public Participant toDomain() {
        var newId = id != null ? id : UUID.randomUUID().toString();
        return Participant.create(
                newId,
                new User(UUID.randomUUID().toString(), email, password, name, phone, List.of(new UserEntities(newId, UserType.PARTICIPANT.toString()))),
                null,
                profile.toDomain(),
                token
        );
    }
}
