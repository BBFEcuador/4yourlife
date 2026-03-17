package com.foryourlife.admin.dashboard.trainerDashboard.domain.common;

public class NextTrainingAttendance {
    private Integer nextTrainingAttendanceCount;
    private Double nextTrainingAttendancePercentage;

    public NextTrainingAttendance(Integer nextTrainingAttendanceCount, Double nextTrainingAttendancePercentage) {
        this.nextTrainingAttendanceCount = nextTrainingAttendanceCount;
        this.nextTrainingAttendancePercentage = nextTrainingAttendancePercentage;
    }

    public Integer getNextTrainingAttendanceCount() {
        return nextTrainingAttendanceCount;
    }

    public void setNextTrainingAttendanceCount(Integer nextTrainingAttendanceCount) {
        this.nextTrainingAttendanceCount = nextTrainingAttendanceCount;
    }

    public Double getNextTrainingAttendancePercentage() {
        return nextTrainingAttendancePercentage;
    }

    public void setNextTrainingAttendancePercentage(Double nextTrainingAttendancePercentage) {
        this.nextTrainingAttendancePercentage = nextTrainingAttendancePercentage;
    }
}
