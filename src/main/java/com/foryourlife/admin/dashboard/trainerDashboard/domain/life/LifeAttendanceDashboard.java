package com.foryourlife.admin.dashboard.trainerDashboard.domain.life;

public class LifeAttendanceDashboard {
    private int totalParticipants;
    private int totalMasterParticipants;
    private int totalTotalUsers;
    private int participantAttendancesCount;
    private int masterAttendancesCount;
    private int totalAttendancesCount;
    private int deserterParticipantsCount;
    private double deserterParticipantsPercentage;
    private int participantEnrolledCount;
    private int masterEnrolledCount;
    private int totalEnrollmentCount;
    private double enrollmentIndex;
    private double realEnrollmentIndex;
    private int totalUsersEnrollersCount;
    private double totalUsersEnrollersPercentage;
    private int totalFocusAttendancesCount;
    private double enrollmentEffectiveness;

    public LifeAttendanceDashboard(int totalParticipants, int totalMasterParticipants, int totalTotalUsers, int participantAttendancesCount, int masterAttendancesCount, int totalAttendancesCount, int deserterParticipantsCount, double deserterParticipantsPercentage, int participantEnrolledCount, int masterEnrolledCount, int totalEnrollmentCount, double enrollmentIndex, double realEnrollmentIndex, int totalUsersEnrollersCount, double totalUsersEnrollersPercentage, int totalFocusAttendancesCount, double enrollmentEffectiveness) {
        this.totalParticipants = totalParticipants;
        this.totalMasterParticipants = totalMasterParticipants;
        this.totalTotalUsers = totalTotalUsers;
        this.participantAttendancesCount = participantAttendancesCount;
        this.masterAttendancesCount = masterAttendancesCount;
        this.totalAttendancesCount = totalAttendancesCount;
        this.deserterParticipantsCount = deserterParticipantsCount;
        this.deserterParticipantsPercentage = deserterParticipantsPercentage;
        this.participantEnrolledCount = participantEnrolledCount;
        this.masterEnrolledCount = masterEnrolledCount;
        this.totalEnrollmentCount = totalEnrollmentCount;
        this.enrollmentIndex = enrollmentIndex;
        this.realEnrollmentIndex = realEnrollmentIndex;
        this.totalUsersEnrollersCount = totalUsersEnrollersCount;
        this.totalUsersEnrollersPercentage = totalUsersEnrollersPercentage;
        this.totalFocusAttendancesCount = totalFocusAttendancesCount;
        this.enrollmentEffectiveness = enrollmentEffectiveness;
    }

    public double getTotalUsersEnrollersPercentage() {
        return totalUsersEnrollersPercentage;
    }

    public void setTotalUsersEnrollersPercentage(double totalUsersEnrollersPercentage) {
        this.totalUsersEnrollersPercentage = totalUsersEnrollersPercentage;
    }

    public int getParticipantAttendancesCount() {
        return participantAttendancesCount;
    }

    public void setParticipantAttendancesCount(int participantAttendancesCount) {
        this.participantAttendancesCount = participantAttendancesCount;
    }

    public int getMasterAttendancesCount() {
        return masterAttendancesCount;
    }

    public void setMasterAttendancesCount(int masterAttendancesCount) {
        this.masterAttendancesCount = masterAttendancesCount;
    }

    public int getTotalAttendancesCount() {
        return totalAttendancesCount;
    }

    public void setTotalAttendancesCount(int totalAttendancesCount) {
        this.totalAttendancesCount = totalAttendancesCount;
    }

    public int getTotalParticipants() {
        return totalParticipants;
    }

    public void setTotalParticipants(int totalParticipants) {
        this.totalParticipants = totalParticipants;
    }

    public int getTotalMasterParticipants() {
        return totalMasterParticipants;
    }

    public void setTotalMasterParticipants(int totalMasterParticipants) {
        this.totalMasterParticipants = totalMasterParticipants;
    }

    public int getTotalTotalUsers() {
        return totalTotalUsers;
    }

    public void setTotalTotalUsers(int totalTotalUsers) {
        this.totalTotalUsers = totalTotalUsers;
    }

    public int getDeserterParticipantsCount() {
        return deserterParticipantsCount;
    }

    public void setDeserterParticipantsCount(int deserterParticipantsCount) {
        this.deserterParticipantsCount = deserterParticipantsCount;
    }

    public double getDeserterParticipantsPercentage() {
        return deserterParticipantsPercentage;
    }

    public void setDeserterParticipantsPercentage(double deserterParticipantsPercentage) {
        this.deserterParticipantsPercentage = deserterParticipantsPercentage;
    }

    public int getParticipantEnrolledCount() {
        return participantEnrolledCount;
    }

    public void setParticipantEnrolledCount(int participantEnrolledCount) {
        this.participantEnrolledCount = participantEnrolledCount;
    }

    public int getMasterEnrolledCount() {
        return masterEnrolledCount;
    }

    public void setMasterEnrolledCount(int masterEnrolledCount) {
        this.masterEnrolledCount = masterEnrolledCount;
    }

    public int getTotalEnrollmentCount() {
        return totalEnrollmentCount;
    }

    public void setTotalEnrollmentCount(int totalEnrollmentCount) {
        this.totalEnrollmentCount = totalEnrollmentCount;
    }

    public double getEnrollmentIndex() {
        return enrollmentIndex;
    }

    public void setEnrollmentIndex(double enrollmentIndex) {
        this.enrollmentIndex = enrollmentIndex;
    }

    public double getRealEnrollmentIndex() {
        return realEnrollmentIndex;
    }

    public void setRealEnrollmentIndex(double realEnrollmentIndex) {
        this.realEnrollmentIndex = realEnrollmentIndex;
    }

    public int getTotalUsersEnrollersCount() {
        return totalUsersEnrollersCount;
    }

    public void setTotalUsersEnrollersCount(int totalUsersEnrollersCount) {
        this.totalUsersEnrollersCount = totalUsersEnrollersCount;
    }

    public int getTotalFocusAttendancesCount() {
        return totalFocusAttendancesCount;
    }

    public void setTotalFocusAttendancesCount(int totalFocusAttendancesCount) {
        this.totalFocusAttendancesCount = totalFocusAttendancesCount;
    }

    public double getEnrollmentEffectiveness() {
        return enrollmentEffectiveness;
    }

    public void setEnrollmentEffectiveness(double enrollmentEffectiveness) {
        this.enrollmentEffectiveness = enrollmentEffectiveness;
    }
}
