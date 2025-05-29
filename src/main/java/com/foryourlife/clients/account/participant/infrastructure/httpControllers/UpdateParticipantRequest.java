package com.foryourlife.clients.account.participant.infrastructure.httpControllers;

import com.foryourlife.clients.account.profileDetails.infrastructure.ProfileDetailRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class UpdateParticipantRequest {
    public String id;
    @NotNull
    @Email
    @NotBlank(message = "The User/Email field is required")
    public String email;
    @NotNull
    @NotBlank(message = "The name1 field is required")
    public String name1;
    @NotNull
    @NotBlank(message = "The name2 field is required")
    public String name2;
    @NotNull
    @NotBlank(message = "The lastname1 field is required")
    public String lastname1;
    @NotNull
    @NotBlank(message = "The lastname2 field is required")
    public String lastname2;
    @NotNull
    @NotBlank(message = "The phone field is required")
    @Pattern(regexp = "^[0-9]*$", message = "The phone field only accept digits")
    public String phone;
    @NotNull
    @Valid
    public ProfileDetailRequest profile;

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getId() {
        return id;
    }

//    public Participant toDomain() {
//        return Participant.create(id, email, null, name, phone, null, profile.toDomain(),null);
//    }
}
