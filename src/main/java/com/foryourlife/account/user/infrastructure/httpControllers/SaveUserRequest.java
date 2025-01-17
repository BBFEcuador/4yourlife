package com.foryourlife.account.user.infrastructure.httpControllers;

import com.foryourlife.account.role.domain.Role;
import com.foryourlife.account.user.domain.Users;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

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
    @NotBlank(message = "The name field is required")
    public String name;
    @NotNull
    @NotBlank(message = "The phone field is required")
    @Pattern(regexp = "^[0-9]*$", message = "The phone field only accept digits")
    public String phone;
    @NotNull
    @NotBlank(message = "The role field is required")
    public Role role;

    public SaveUserRequest(String phone, String name, String password, String email, String id) {
        this.id = id;
        this.phone = phone;
        this.name = name;
        this.password = password;
        this.email = email;
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

    public String getId() {
        return id;
    }

    public Users toDomain() {
        return Users.create(id != null ? id : UUID.randomUUID().toString(), email, password, name, phone, role);
    }
}
