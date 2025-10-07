package com.foryourlife.admin.programs.trainer.domain;

public class LoginTrainerResponse {
    public Trainer trainer;

    public String token;

    public LoginTrainerResponse(Trainer trainer, String token) {
        this.trainer = trainer;
        this.token = token;
    }
}
