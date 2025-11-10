package com.foryourlife.admin.programs.trainer.domain;

public class PromiseDashboard {
    private int totalParticipantPromise;
    private int totalMasterLifePromise;
    private int totalMasterLifeAchieved;
    private int totalMasterLifePaid;
    private int totalAchieved;
    private int totalPaid;

    public PromiseDashboard(int totalParticipantPromise, int totalMasterLifePromise, int totalMasterLifeAchieved, int totalMasterLifePaid, int totalAchieved, int totalPaid) {
        this.totalParticipantPromise = totalParticipantPromise;
        this.totalMasterLifePromise = totalMasterLifePromise;
        this.totalMasterLifeAchieved = totalMasterLifeAchieved;
        this.totalMasterLifePaid = totalMasterLifePaid;
        this.totalAchieved = totalAchieved;
        this.totalPaid = totalPaid;
    }

    public int getTotalMasterLifePromise() {
        return totalMasterLifePromise;
    }

    public void setTotalMasterLifePromise(int totalMasterLifePromise) {
        this.totalMasterLifePromise = totalMasterLifePromise;
    }

    public int getTotalMasterLifeAchieved() {
        return totalMasterLifeAchieved;
    }

    public void setTotalMasterLifeAchieved(int totalMasterLifeAchieved) {
        this.totalMasterLifeAchieved = totalMasterLifeAchieved;
    }

    public int getTotalMasterLifePaid() {
        return totalMasterLifePaid;
    }

    public void setTotalMasterLifePaid(int totalMasterLifePaid) {
        this.totalMasterLifePaid = totalMasterLifePaid;
    }


    public int getTotalParticipantPromise() {
        return totalParticipantPromise;
    }

    public void setTotalParticipantPromise(int totalParticipantPromise) {
        this.totalParticipantPromise = totalParticipantPromise;
    }

    public int getTotalAchieved() {
        return totalAchieved;
    }

    public void setTotalAchieved(int totalAchieved) {
        this.totalAchieved = totalAchieved;
    }

    public int getTotalPaid() {
        return totalPaid;
    }

    public void setTotalPaid(int totalPaid) {
        this.totalPaid = totalPaid;
    }
}
