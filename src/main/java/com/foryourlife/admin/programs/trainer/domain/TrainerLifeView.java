package com.foryourlife.admin.programs.trainer.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.foryourlife.admin.programs.training.domain.Training;

public class TrainerLifeView {
    @JsonIgnoreProperties({"nextLevel", "campus"})
    private Training life1;
    @JsonIgnoreProperties({"nextLevel", "campus"})
    private Training life2;
    @JsonIgnoreProperties({"nextLevel", "campus"})
    private Training life3;
    @JsonIgnoreProperties({"nextLevel", "campus"})
    private Training graduate;

    protected TrainerLifeView() {
    }

    public TrainerLifeView(Training life1, Training life2, Training life3, Training graduate) {
        this.life1 = life1;
        this.life2 = life2;
        this.life3 = life3;
        this.graduate = graduate;
    }

    public Training getLife1() {
        return life1;
    }

    public Training getLife2() {
        return life2;
    }

    public Training getLife3() {
        return life3;
    }

    public Training getGraduate() {
        return graduate;
    }
}
