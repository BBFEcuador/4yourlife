package com.foryourlife.admin.dashboard.operativeAssitantDashboard.domain.focus;

public class WeekendFocusReport {
    private int initialParticipantsCount;
    private int realParticipantsCount;
    private int deserterParticipantsCount;
    private int declarationsCount;
    private int visionariesCount;
    private int captainsCount;
    private int staffsCount;

    public WeekendFocusReport(int initialParticipantsCount, int realParticipantsCount, int deserterParticipantsCount, int declarationsCount, int visionariesCount, int captainsCount, int staffsCount) {
        this.initialParticipantsCount = initialParticipantsCount;
        this.realParticipantsCount = realParticipantsCount;
        this.deserterParticipantsCount = deserterParticipantsCount;
        this.declarationsCount = declarationsCount;
        this.visionariesCount = visionariesCount;
        this.captainsCount = captainsCount;
        this.staffsCount = staffsCount;
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

    public int getDeserterParticipantsCount() {
        return deserterParticipantsCount;
    }

    public void setDeserterParticipantsCount(int deserterParticipantsCount) {
        this.deserterParticipantsCount = deserterParticipantsCount;
    }

    public int getDeclarationsCount() {
        return declarationsCount;
    }

    public void setDeclarationsCount(int declarationsCount) {
        this.declarationsCount = declarationsCount;
    }

    public int getVisionariesCount() {
        return visionariesCount;
    }

    public void setVisionariesCount(int visionariesCount) {
        this.visionariesCount = visionariesCount;
    }

    public int getCaptainsCount() {
        return captainsCount;
    }

    public void setCaptainsCount(int captainsCount) {
        this.captainsCount = captainsCount;
    }

    public int getStaffsCount() {
        return staffsCount;
    }

    public void setStaffsCount(int staffsCount) {
        this.staffsCount = staffsCount;
    }
}
