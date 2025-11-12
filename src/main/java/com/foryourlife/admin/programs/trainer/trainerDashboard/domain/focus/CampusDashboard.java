package com.foryourlife.admin.programs.trainer.trainerDashboard.domain.focus;

import java.util.List;

public class CampusDashboard {
    private String day;
    private List<CampusAttendanceCount> campusAttendanceCount ;

    public CampusDashboard(String day, List<CampusAttendanceCount> campusAttendanceCount) {
        this.day = day;
        this.campusAttendanceCount = campusAttendanceCount;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public List<CampusAttendanceCount> getCampusAttendanceCount() {
        return campusAttendanceCount;
    }

    public void setCampusAttendanceCount(List<CampusAttendanceCount> campusAttendanceCount) {
        this.campusAttendanceCount = campusAttendanceCount;
    }
}
