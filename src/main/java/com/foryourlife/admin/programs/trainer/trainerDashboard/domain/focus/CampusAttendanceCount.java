package com.foryourlife.admin.programs.trainer.trainerDashboard.domain.focus;

public class CampusAttendanceCount {
    private String campusName;
    private int attendanceCount = 0;

    public CampusAttendanceCount(String campusName, int attendanceCount) {
        this.campusName = campusName;
        this.attendanceCount = attendanceCount;
    }

    public String getCampusName() {
        return campusName;
    }

    public void setCampusName(String campusName) {
        this.campusName = campusName;
    }

    public int getAttendanceCount() {
        return attendanceCount;
    }

    public void setAttendanceCount(int attendanceCount) {
        this.attendanceCount = attendanceCount;
    }
}
