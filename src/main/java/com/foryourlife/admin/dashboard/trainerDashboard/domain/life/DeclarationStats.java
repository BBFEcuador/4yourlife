package com.foryourlife.admin.dashboard.trainerDashboard.domain.life;

public class DeclarationStats {
    private int totalMasterLifePromisesCount;
    private int totalMasterLifeAchievedCount;
    private int totalMasterLifePaidCount;
    private int totalParticipantPromisesCount;
    private int totalParticipantAchievedCount;
    private int totalParticipantPaidCount;
    private int totalTeamLifePromisesCount;
    private int totalTeamAchievedCount;
    private int totalTeamPaidCount;
    private double accomplishmentMasterLife;
    private double accomplishmentParticipant;
    private double accomplishmentTeam;
    private int masterLifeCount;
    private int participantCount;
    private int teamCount;
    private double masterLifeEnrollmentIndex;
    private double participantEnrollmentIndex;
    private double teamEnrollmentIndex;
    private int totalUsersNotEnrolledCount;
    private int totalUsersEnrollersCount;
    private double totalUsersEnrollersPercentage;

    public DeclarationStats(int totalMasterLifePromisesCount, int totalMasterLifeAchievedCount, int totalMasterLifePaidCount, int totalParticipantPromisesCount, int totalParticipantAchievedCount, int totalParticipantPaidCount, int totalTeamLifePromisesCount, int totalTeamAchievedCount, int totalTeamPaidCount, double accomplishmentMasterLife, double accomplishmentParticipant, double accomplishmentTeam, int masterLifeCount, int participantCount, int teamCount, double masterLifeEnrollmentIndex, double participantEnrollmentIndex, double teamEnrollmentIndex, int totalUsersNotEnrolledCount, int totalUsersEnrollersCount, double totalUsersEnrollersPercentage) {
        this.totalMasterLifePromisesCount = totalMasterLifePromisesCount;
        this.totalMasterLifeAchievedCount = totalMasterLifeAchievedCount;
        this.totalMasterLifePaidCount = totalMasterLifePaidCount;
        this.totalParticipantPromisesCount = totalParticipantPromisesCount;
        this.totalParticipantAchievedCount = totalParticipantAchievedCount;
        this.totalParticipantPaidCount = totalParticipantPaidCount;
        this.totalTeamLifePromisesCount = totalTeamLifePromisesCount;
        this.totalTeamAchievedCount = totalTeamAchievedCount;
        this.totalTeamPaidCount = totalTeamPaidCount;
        this.accomplishmentMasterLife = accomplishmentMasterLife;
        this.accomplishmentParticipant = accomplishmentParticipant;
        this.accomplishmentTeam = accomplishmentTeam;
        this.masterLifeCount = masterLifeCount;
        this.participantCount = participantCount;
        this.teamCount = teamCount;
        this.masterLifeEnrollmentIndex = masterLifeEnrollmentIndex;
        this.participantEnrollmentIndex = participantEnrollmentIndex;
        this.teamEnrollmentIndex = teamEnrollmentIndex;
        this.totalUsersNotEnrolledCount = totalUsersNotEnrolledCount;
        this.totalUsersEnrollersCount = totalUsersEnrollersCount;
        this.totalUsersEnrollersPercentage = totalUsersEnrollersPercentage;
    }

    public int getTotalMasterLifePromisesCount() {
        return totalMasterLifePromisesCount;
    }

    public void setTotalMasterLifePromisesCount(int totalMasterLifePromisesCount) {
        this.totalMasterLifePromisesCount = totalMasterLifePromisesCount;
    }

    public int getTotalMasterLifeAchievedCount() {
        return totalMasterLifeAchievedCount;
    }

    public void setTotalMasterLifeAchievedCount(int totalMasterLifeAchievedCount) {
        this.totalMasterLifeAchievedCount = totalMasterLifeAchievedCount;
    }

    public int getTotalMasterLifePaidCount() {
        return totalMasterLifePaidCount;
    }

    public void setTotalMasterLifePaidCount(int totalMasterLifePaidCount) {
        this.totalMasterLifePaidCount = totalMasterLifePaidCount;
    }

    public int getTotalParticipantPromisesCount() {
        return totalParticipantPromisesCount;
    }

    public void setTotalParticipantPromisesCount(int totalParticipantPromisesCount) {
        this.totalParticipantPromisesCount = totalParticipantPromisesCount;
    }

    public int getTotalParticipantAchievedCount() {
        return totalParticipantAchievedCount;
    }

    public void setTotalParticipantAchievedCount(int totalParticipantAchievedCount) {
        this.totalParticipantAchievedCount = totalParticipantAchievedCount;
    }

    public int getTotalParticipantPaidCount() {
        return totalParticipantPaidCount;
    }

    public void setTotalParticipantPaidCount(int totalParticipantPaidCount) {
        this.totalParticipantPaidCount = totalParticipantPaidCount;
    }

    public int getTotalTeamLifePromisesCount() {
        return totalTeamLifePromisesCount;
    }

    public void setTotalTeamLifePromisesCount(int totalTeamLifePromisesCount) {
        this.totalTeamLifePromisesCount = totalTeamLifePromisesCount;
    }

    public int getTotalTeamAchievedCount() {
        return totalTeamAchievedCount;
    }

    public void setTotalTeamAchievedCount(int totalTeamAchievedCount) {
        this.totalTeamAchievedCount = totalTeamAchievedCount;
    }

    public int getTotalTeamPaidCount() {
        return totalTeamPaidCount;
    }

    public void setTotalTeamPaidCount(int totalTeamPaidCount) {
        this.totalTeamPaidCount = totalTeamPaidCount;
    }

    public double getAccomplishmentMasterLife() {
        return accomplishmentMasterLife;
    }

    public void setAccomplishmentMasterLife(double accomplishmentMasterLife) {
        this.accomplishmentMasterLife = accomplishmentMasterLife;
    }

    public double getAccomplishmentParticipant() {
        return accomplishmentParticipant;
    }

    public void setAccomplishmentParticipant(double accomplishmentParticipant) {
        this.accomplishmentParticipant = accomplishmentParticipant;
    }

    public double getAccomplishmentTeam() {
        return accomplishmentTeam;
    }

    public void setAccomplishmentTeam(double accomplishmentTeam) {
        this.accomplishmentTeam = accomplishmentTeam;
    }

    public int getMasterLifeCount() {
        return masterLifeCount;
    }

    public void setMasterLifeCount(int masterLifeCount) {
        this.masterLifeCount = masterLifeCount;
    }

    public int getParticipantCount() {
        return participantCount;
    }

    public void setParticipantCount(int participantCount) {
        this.participantCount = participantCount;
    }

    public int getTeamCount() {
        return teamCount;
    }

    public void setTeamCount(int teamCount) {
        this.teamCount = teamCount;
    }

    public double getMasterLifeEnrollmentIndex() {
        return masterLifeEnrollmentIndex;
    }

    public void setMasterLifeEnrollmentIndex(double masterLifeEnrollmentIndex) {
        this.masterLifeEnrollmentIndex = masterLifeEnrollmentIndex;
    }

    public double getParticipantEnrollmentIndex() {
        return participantEnrollmentIndex;
    }

    public void setParticipantEnrollmentIndex(double participantEnrollmentIndex) {
        this.participantEnrollmentIndex = participantEnrollmentIndex;
    }

    public double getTeamEnrollmentIndex() {
        return teamEnrollmentIndex;
    }

    public void setTeamEnrollmentIndex(double teamEnrollmentIndex) {
        this.teamEnrollmentIndex = teamEnrollmentIndex;
    }

    public int getTotalUsersNotEnrolledCount() {
        return totalUsersNotEnrolledCount;
    }

    public void setTotalUsersNotEnrolledCount(int totalUsersNotEnrolledCount) {
        this.totalUsersNotEnrolledCount = totalUsersNotEnrolledCount;
    }

    public int getTotalUsersEnrollersCount() {
        return totalUsersEnrollersCount;
    }

    public void setTotalUsersEnrollersCount(int totalUsersEnrollersCount) {
        this.totalUsersEnrollersCount = totalUsersEnrollersCount;
    }

    public double getTotalUsersEnrollersPercentage() {
        return totalUsersEnrollersPercentage;
    }

    public void setTotalUsersEnrollersPercentage(double totalUsersEnrollersPercentage) {
        this.totalUsersEnrollersPercentage = totalUsersEnrollersPercentage;
    }
}
