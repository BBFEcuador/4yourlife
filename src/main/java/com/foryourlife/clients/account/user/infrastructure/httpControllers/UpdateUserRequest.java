package com.foryourlife.clients.account.user.infrastructure.httpControllers;

import com.foryourlife.clients.account.profileDetails.infrastructure.ProfileDetailRequest;
import com.foryourlife.clients.account.user.domain.Participant;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.util.UUID;

public class UpdateUserRequest {
    public String id;
    @NotNull
    @Email
    @NotBlank(message = "The User/Email field is required")
    public String email;
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

    public UpdateUserRequest(String phone, String name, String email, String id) {
        this.id = id;
        this.phone = phone;
        this.name = name;
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getId() {
        return id;
    }

    public Participant toDomain() {
        return Participant.create(id, email, null, name, phone, null, profile.toDomain(),null);
    }
}
