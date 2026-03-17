package com.foryourlife.admin.dashboard.trainerDashboard.domain.life;

public class AttendanceDashboard {
    private long fridayCount;
    private long saturdayCount;
    private long sundayCount;
    private long totalParticipants;
    private long masterFridayCount;
    private long masterSaturdayCount;
    private long masterSundayCount;
    private long totalMasterParticipants;

    public AttendanceDashboard(long fridayCount, long saturdayCount, long sundayCount, long totalParticipants, long masterFridayCount, long masterSaturdayCount, long masterSundayCount, long totalMasterParticipants) {
        this.fridayCount = fridayCount;
        this.saturdayCount = saturdayCount;
        this.sundayCount = sundayCount;
        this.totalParticipants = totalParticipants;
        this.masterFridayCount = masterFridayCount;
        this.masterSaturdayCount = masterSaturdayCount;
        this.masterSundayCount = masterSundayCount;
        this.totalMasterParticipants = totalMasterParticipants;
    }

    public long getFridayCount() {
        return fridayCount;
    }

    public void setFridayCount(long fridayCount) {
        this.fridayCount = fridayCount;
    }

    public long getMasterFridayCount() {
        return masterFridayCount;
    }

    public void setMasterFridayCount(long masterFridayCount) {
        this.masterFridayCount = masterFridayCount;
    }

    public long getMasterSaturdayCount() {
        return masterSaturdayCount;
    }

    public void setMasterSaturdayCount(long masterSaturdayCount) {
        this.masterSaturdayCount = masterSaturdayCount;
    }

    public long getMasterSundayCount() {
        return masterSundayCount;
    }

    public void setMasterSundayCount(long masterSundayCount) {
        this.masterSundayCount = masterSundayCount;
    }

    public long getTotalMasterParticipants() {
        return totalMasterParticipants;
    }

    public void setTotalMasterParticipants(long totalMasterParticipants) {
        this.totalMasterParticipants = totalMasterParticipants;
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
