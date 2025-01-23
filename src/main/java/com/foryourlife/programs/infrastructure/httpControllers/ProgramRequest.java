package com.foryourlife.programs.infrastructure.httpControllers;

import jakarta.validation.constraints.NotBlank;

public class ProgramRequest {
    @NotBlank(message = "name")
    public String name;

    @NotBlank(message = "level")
    public String level;

    public ProgramRequest(String name, String level) {
        this.name = name;
        this.level = level;
    }

    public ProgramRequest() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }
}
