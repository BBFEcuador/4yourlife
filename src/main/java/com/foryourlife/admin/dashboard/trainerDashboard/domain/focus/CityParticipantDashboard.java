package com.foryourlife.admin.dashboard.trainerDashboard.domain.focus;

public class CityParticipantDashboard {
    private String day;
    private int quito;
    private int province;

    public CityParticipantDashboard(String day, int quito, int province) {
        this.day = day;
        this.quito = quito;
        this.province = province;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public int getQuito() {
        return quito;
    }

    public void setQuito(int quito) {
        this.quito = quito;
    }

    public int getProvince() {
        return province;
    }

    public void setProvince(int province) {
        this.province = province;
    }
}
