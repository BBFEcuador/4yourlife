package com.foryourlife.admin.programs.trainer.domain;

import com.foryourlife.admin.programs.attendance.domain.AttendanceStatus;

public class UserDashboardDto {
    private String userName;
    private AttendanceStatus fridayAttendance;
    private AttendanceStatus saturdayAttendance;
    private AttendanceStatus sundayAttendance;
    private Integer firstPromise;
    private Integer secondPromise;
    private Integer thirdPromise;
    private Integer achievedCount;
    private Integer paidCount;

    public UserDashboardDto(String userName, AttendanceStatus fridayAttendance, AttendanceStatus saturdayAttendance, AttendanceStatus sundayAttendance, Integer firstPromise, Integer secondPromise, Integer thirdPromise, Integer achievedCount, Integer paidCount) {
        this.userName = userName;
        this.fridayAttendance = fridayAttendance;
        this.saturdayAttendance = saturdayAttendance;
        this.sundayAttendance = sundayAttendance;
        this.firstPromise = firstPromise;
        this.secondPromise = secondPromise;
        this.thirdPromise = thirdPromise;
        this.achievedCount = achievedCount;
        this.paidCount = paidCount;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public Integer getFirstPromise() {
        return firstPromise;
    }

    public void setFirstPromise(Integer firstPromise) {
        this.firstPromise = firstPromise;
    }

    public Integer getSecondPromise() {
        return secondPromise;
    }

    public void setSecondPromise(Integer secondPromise) {
        this.secondPromise = secondPromise;
    }

    public Integer getThirdPromise() {
        return thirdPromise;
    }

    public void setThirdPromise(Integer thirdPromise) {
        this.thirdPromise = thirdPromise;
    }

    public Integer getAchievedCount() {
        return achievedCount;
    }

    public void setAchievedCount(Integer achievedCount) {
        this.achievedCount = achievedCount;
    }

    public Integer getPaidCount() {
        return paidCount;
    }

    public void setPaidCount(Integer paidCount) {
        this.paidCount = paidCount;
    }
}