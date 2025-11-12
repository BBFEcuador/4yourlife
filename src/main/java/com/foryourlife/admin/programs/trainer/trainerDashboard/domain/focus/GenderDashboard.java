package com.foryourlife.admin.programs.trainer.trainerDashboard.domain.focus;

public class GenderDashboard {
    private String day;
    private int male;
    private int female;

    public GenderDashboard(String day, int male, int female) {
        this.day = day;
        this.male = male;
        this.female = female;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public int getMale() {
        return male;
    }

    public void setMale(int male) {
        this.male = male;
    }

    public int getFemale() {
        return female;
    }

    public void setFemale(int female) {
        this.female = female;
    }
}
