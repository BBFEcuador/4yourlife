package com.foryourlife.admin.dashboard.trainerDashboard.domain.common;

public class AgeDashboard {
    public String day;
    public Integer age_less_18;
    public Integer age_18_27;
    public Integer age_28_40;
    public Integer age_41_65;
    public Integer age_above_65;

    public AgeDashboard(){

    }

    public AgeDashboard(String day, Integer age_less_18, Integer age_18_27, Integer age_28_40, Integer age_41_65, Integer age_above_65) {
        this.day = day;
        this.age_less_18 = age_less_18;
        this.age_18_27 = age_18_27;
        this.age_28_40 = age_28_40;
        this.age_41_65 = age_41_65;
        this.age_above_65 = age_above_65;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public Integer getAge_less_18() {
        return age_less_18;
    }

    public void setAge_less_18(Integer age_less_18) {
        this.age_less_18 = age_less_18;
    }

    public Integer getAge_18_27() {
        return age_18_27;
    }

    public void setAge_18_27(Integer age_18_27) {
        this.age_18_27 = age_18_27;
    }

    public Integer getAge_28_40() {
        return age_28_40;
    }

    public void setAge_28_40(Integer age_28_40) {
        this.age_28_40 = age_28_40;
    }

    public Integer getAge_41_65() {
        return age_41_65;
    }

    public void setAge_41_65(Integer age_41_65) {
        this.age_41_65 = age_41_65;
    }

    public Integer getAge_above_65() {
        return age_above_65;
    }

    public void setAge_above_65(Integer age_above_65) {
        this.age_above_65 = age_above_65;
    }
}
