package com.foryourlife.admin.programs.trainer.trainerDashboard.domain.focus;

import java.util.List;

public class TrainerFocusView {
    private GeneralAttendance focusAttendanceDashboard;
    private List<CampusDashboard> campusDashboard;
    private GenderDashboard genderDashboard;
    private AgeDashboard ageDashboard;

    public TrainerFocusView(GeneralAttendance focusAttendanceDashboard, List<CampusDashboard> campusDashboard, GenderDashboard genderDashboard, AgeDashboard ageDashboard) {
        this.focusAttendanceDashboard = focusAttendanceDashboard;
        this.campusDashboard = campusDashboard;
        this.genderDashboard = genderDashboard;
        this.ageDashboard = ageDashboard;
    }

    public GeneralAttendance getFocusAttendanceDashboard() {
        return focusAttendanceDashboard;
    }

    public void setFocusAttendanceDashboard(GeneralAttendance focusAttendanceDashboard) {
        this.focusAttendanceDashboard = focusAttendanceDashboard;
    }

    public List<CampusDashboard> getCampusDashboard() {
        return campusDashboard;
    }

    public void setCampusDashboard(List<CampusDashboard> campusDashboard) {
        this.campusDashboard = campusDashboard;
    }

    public GenderDashboard getGenderDashboard() {
        return genderDashboard;
    }

    public void setGenderDashboard(GenderDashboard genderDashboard) {
        this.genderDashboard = genderDashboard;
    }

    public AgeDashboard getAgeDashboard() {
        return ageDashboard;
    }

    public void setAgeDashboard(AgeDashboard ageDashboard) {
        this.ageDashboard = ageDashboard;
    }
}
