package com.foryourlife.admin.programs.trainer.infrastructure.httpControllers;

import com.foryourlife.admin.programs.trainer.domain.Trainer;
import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public class TrainerRequest {

    public String id;

    @NotBlank(message = "El nombre es requerido")
    public String name;

    @NotBlank(message = "El email es requerido")
    public String email;

    @NotBlank(message = "El contraseña es requerido")
    public String password;

    @NotBlank(message = "El telefono es requerido")
    public String phone;

    public TrainerRequest() {
    }

    public TrainerRequest(String id, String name, String email, String password, String phone) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }


    public Trainer toDomain(){
        return Trainer.create(id != null ? id : UUID.randomUUID().toString(), this.name, this.email.toLowerCase(), this.phone, this.password, true);
    }
}
