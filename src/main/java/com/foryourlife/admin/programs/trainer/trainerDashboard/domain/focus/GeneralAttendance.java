package com.foryourlife.admin.programs.trainer.trainerDashboard.domain.focus;

import java.util.List;

public class GeneralAttendance {
    private int totalFocus;
    private int totalLingerer;
    private List<UserAttendance> attendances;

    public GeneralAttendance(int totalFocus, int totalLingerer, List<UserAttendance> attendances) {
        this.totalFocus = totalFocus;
        this.totalLingerer = totalLingerer;
        this.attendances = attendances;
    }

    public int getTotalFocus() {
        return totalFocus;
    }

    public void setTotalFocus(int totalFocus) {
        this.totalFocus = totalFocus;
    }

    public int getTotalLingerer() {
        return totalLingerer;
    }

    public void setTotalLingerer(int totalLingerer) {
        this.totalLingerer = totalLingerer;
    }

    public List<UserAttendance> getAttendances() {
        return attendances;
    }

    public void setAttendances(List<UserAttendance> attendances) {
        this.attendances = attendances;
    }
}
