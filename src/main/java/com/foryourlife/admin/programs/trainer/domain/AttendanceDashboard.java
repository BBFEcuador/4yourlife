package com.foryourlife.admin.programs.trainer.domain;

public class AttendanceDashboard {
    private long fridayCount;
    private long saturdayCount;
    private long sundayCount;
    private long totalParticipants;

    public AttendanceDashboard(long fridayCount, long saturdayCount, long sundayCount, long totalParticipants) {
        this.fridayCount = fridayCount;
        this.saturdayCount = saturdayCount;
        this.sundayCount = sundayCount;
        this.totalParticipants = totalParticipants;
    }

    public long getFridayCount() {
        return fridayCount;
    }

    public void setFridayCount(long fridayCount) {
        this.fridayCount = fridayCount;
    }

    public long getSaturdayCount() {
        return saturdayCount;
    }

    public void setSaturdayCount(long saturdayCount) {
        this.saturdayCount = saturdayCount;
    }

    public long getSundayCount() {
        return sundayCount;
    }

    public void setSundayCount(long sundayCount) {
        this.sundayCount = sundayCount;
    }

    public long getTotalParticipants() {
        return totalParticipants;
    }

    public void setTotalParticipants(long totalParticipants) {
        this.totalParticipants = totalParticipants;
    }
}
