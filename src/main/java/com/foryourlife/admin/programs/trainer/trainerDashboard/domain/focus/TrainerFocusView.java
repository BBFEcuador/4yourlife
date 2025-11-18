package com.foryourlife.admin.programs.trainer.trainerDashboard.domain.focus;

import java.util.List;

public class TrainerFocusView {
    private GeneralAttendance focusAttendanceDashboard;
    private List<GenderDashboard> genderByDay;
    private List<AgeDashboard> ageDashboard;

    public TrainerFocusView(GeneralAttendance focusAttendanceDashboard, List<GenderDashboard> genderByDay, List<AgeDashboard> ageDashboard) {
        this.focusAttendanceDashboard = focusAttendanceDashboard;
        this.genderByDay = genderByDay;
        this.ageDashboard = ageDashboard;
    }

    public GeneralAttendance getFocusAttendanceDashboard() {
        return focusAttendanceDashboard;
    }

    public void setFocusAttendanceDashboard(GeneralAttendance focusAttendanceDashboard) {
        this.focusAttendanceDashboard = focusAttendanceDashboard;
    }

    public List<GenderDashboard> getGenderByDay() {
        return genderByDay;
    }

    public void setGenderByDay(List<GenderDashboard> genderByDay) {
        this.genderByDay = genderByDay;
    }

    public List<AgeDashboard> getAgeDashboard() {
        return ageDashboard;
    }

    public void setAgeDashboard(List<AgeDashboard> ageDashboard) {
        this.ageDashboard = ageDashboard;
    }
}
