package com.foryourlife.admin.programs.trainer.trainerDashboard.domain.focus;

import java.util.List;

public class FocusAttendanceDashboard {
    private GeneralAttendance generalAttendance;
    private List<UserAttendance> userAttendances;

    public FocusAttendanceDashboard(GeneralAttendance generalAttendance, List<UserAttendance> userAttendances) {
        this.generalAttendance = generalAttendance;
        this.userAttendances = userAttendances;
    }

    public GeneralAttendance getGeneralAttendance() {
        return generalAttendance;
    }

    public void setGeneralAttendance(GeneralAttendance generalAttendance) {
        this.generalAttendance = generalAttendance;
    }

    public List<UserAttendance> getUserAttendances() {
        return userAttendances;
    }

    public void setUserAttendances(List<UserAttendance> userAttendances) {
        this.userAttendances = userAttendances;
    }
}
