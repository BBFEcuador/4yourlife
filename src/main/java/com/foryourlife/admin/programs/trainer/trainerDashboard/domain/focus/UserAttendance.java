package com.foryourlife.admin.programs.trainer.trainerDashboard.domain.focus;

import com.foryourlife.admin.programs.attendance.domain.AttendanceStatus;
import com.foryourlife.shared.domain.user.UserType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

public class UserAttendance {
    private String userName;
    @Enumerated(EnumType.STRING)
    private UserType userEntity;
    private String forTrainingName;
    private AttendanceStatus fridayAttendance;
    private AttendanceStatus saturdayAttendance;
    private AttendanceStatus sundayAttendance;

    public UserAttendance(String userName, UserType userEntity, AttendanceStatus fridayAttendance, AttendanceStatus saturdayAttendance, AttendanceStatus sundayAttendance) {
        this.userName = userName;
        this.userEntity = userEntity;
        this.fridayAttendance = fridayAttendance;
        this.saturdayAttendance = saturdayAttendance;
        this.sundayAttendance = sundayAttendance;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public UserType getUserEntity() {
        return userEntity;
    }

    public void setUserEntity(UserType userEntity) {
        this.userEntity = userEntity;
    }

    public AttendanceStatus getFridayAttendance() {
        return fridayAttendance;
    }

    public void setFridayAttendance(AttendanceStatus fridayAttendance) {
        this.fridayAttendance = fridayAttendance;
    }

    public AttendanceStatus getSaturdayAttendance() {
        return saturdayAttendance;
    }

    public void setSaturdayAttendance(AttendanceStatus saturdayAttendance) {
        this.saturdayAttendance = saturdayAttendance;
    }

    public AttendanceStatus getSundayAttendance() {
        return sundayAttendance;
    }

    public void setSundayAttendance(AttendanceStatus sundayAttendance) {
        this.sundayAttendance = sundayAttendance;
    }
}
