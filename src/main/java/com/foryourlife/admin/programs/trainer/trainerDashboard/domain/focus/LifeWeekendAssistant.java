package com.foryourlife.admin.programs.trainer.trainerDashboard.domain.focus;

public class LifeWeekendAssistant {
    private Integer assistant;
    private Integer enrolled;
    private Integer percentage;

    public LifeWeekendAssistant(Integer assistant, Integer enrolled, Integer percentage) {
        this.assistant = assistant;
        this.enrolled = enrolled;
        this.percentage = percentage;
    }

    public Integer getAssistant() {
        return assistant;
    }

    public void setAssistant(Integer assistant) {
        this.assistant = assistant;
    }

    public Integer getEnrolled() {
        return enrolled;
    }

    public void setEnrolled(Integer enrolled) {
        this.enrolled = enrolled;
    }

    public Integer getPercentage() {
        return percentage;
    }

    public void setPercentage(Integer percentage) {
        this.percentage = percentage;
    }
}


