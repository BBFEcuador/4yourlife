package com.foryourlife.admin.dashboard.operativeAssitantDashboard.domain.life;

public class WeekendLifeReport {
    private int initialParticipantsCount;
    private int realParticipantsCount;
    private int participantsDeclarationsCount;
    private int initialMasterLifesCount;
    private int realMasterLifesCount;
    private int masterLifesDeclarationsCount;
    private int initialTotalCount;
    private int realTotalCount;
    private int totalDeclarationsCount;
    private int totalEnrollmentsCount;
    private double declarationIndex;
    private double realIndex;

    public WeekendLifeReport(int initialParticipantsCount, int realParticipantsCount, int participantsDeclarationsCount, int initialMasterLifesCount, int realMasterLifesCount, int masterLifesDeclarationsCount, int initialTotalCount, int realTotalCount, int totalDeclarationsCount, int totalEnrollmentsCount, double declarationIndex, double realIndex) {
        this.initialParticipantsCount = initialParticipantsCount;
        this.realParticipantsCount = realParticipantsCount;
        this.participantsDeclarationsCount = participantsDeclarationsCount;
        this.initialMasterLifesCount = initialMasterLifesCount;
        this.realMasterLifesCount = realMasterLifesCount;
        this.masterLifesDeclarationsCount = masterLifesDeclarationsCount;
        this.initialTotalCount = initialTotalCount;
        this.realTotalCount = realTotalCount;
        this.totalDeclarationsCount = totalDeclarationsCount;
        this.totalEnrollmentsCount = totalEnrollmentsCount;
        this.declarationIndex = declarationIndex;
        this.realIndex = realIndex;
    }

    public int getInitialParticipantsCount() {
        return initialParticipantsCount;
    }

    public void setInitialParticipantsCount(int initialParticipantsCount) {
        this.initialParticipantsCount = initialParticipantsCount;
    }

    public int getRealParticipantsCount() {
        return realParticipantsCount;
    }

    public void setRealParticipantsCount(int realParticipantsCount) {
        this.realParticipantsCount = realParticipantsCount;
    }

    public int getParticipantsDeclarationsCount() {
        return participantsDeclarationsCount;
    }

    public void setParticipantsDeclarationsCount(int participantsDeclarationsCount) {
        this.participantsDeclarationsCount = participantsDeclarationsCount;
    }

    public int getInitialMasterLifesCount() {
        return initialMasterLifesCount;
    }

    public void setInitialMasterLifesCount(int initialMasterLifesCount) {
        this.initialMasterLifesCount = initialMasterLifesCount;
    }

    public int getRealMasterLifesCount() {
        return realMasterLifesCount;
    }

    public void setRealMasterLifesCount(int realMasterLifesCount) {
        this.realMasterLifesCount = realMasterLifesCount;
    }

    public int getMasterLifesDeclarationsCount() {
        return masterLifesDeclarationsCount;
    }

    public void setMasterLifesDeclarationsCount(int masterLifesDeclarationsCount) {
        this.masterLifesDeclarationsCount = masterLifesDeclarationsCount;
    }

    public int getInitialTotalCount() {
        return initialTotalCount;
    }

    public void setInitialTotalCount(int initialTotalCount) {
        this.initialTotalCount = initialTotalCount;
    }

    public int getRealTotalCount() {
        return realTotalCount;
    }

    public void setRealTotalCount(int realTotalCount) {
        this.realTotalCount = realTotalCount;
    }

    public int getTotalDeclarationsCount() {
        return totalDeclarationsCount;
    }

    public void setTotalDeclarationsCount(int totalDeclarationsCount) {
        this.totalDeclarationsCount = totalDeclarationsCount;
    }

    public int getTotalEnrollmentsCount() {
        return totalEnrollmentsCount;
    }

    public void setTotalEnrollmentsCount(int totalEnrollmentsCount) {
        this.totalEnrollmentsCount = totalEnrollmentsCount;
    }

    public double getDeclarationIndex() {
        return declarationIndex;
    }

    public void setDeclarationIndex(double declarationIndex) {
        this.declarationIndex = declarationIndex;
    }

    public double getRealIndex() {
        return realIndex;
    }

    public void setRealIndex(double realIndex) {
        this.realIndex = realIndex;
    }
}
