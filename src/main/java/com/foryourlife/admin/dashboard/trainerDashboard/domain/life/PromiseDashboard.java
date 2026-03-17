package com.foryourlife.admin.dashboard.trainerDashboard.domain.life;

public class PromiseDashboard {
    private int totalFirstPromise;
    private int totalSecondPromise;
    private int totalThirdPromise;
    private int totalFirstMasterLifePromise;
    private int totalSecondMasterLifePromise;
    private int totalThirdMasterLifePromise;
    private int totalMasterLifePromise;
    private int totalMasterLifeAchieved;
    private int totalMasterLifePaid;
    private int totalAchieved;
    private int totalPaid;

    public PromiseDashboard(int totalFirstPromise, int totalSecondPromise, int totalThirdPromise, int totalFirstMasterLifePromise, int totalSecondMasterLifePromise, int totalThirdMasterLifePromise, int totalMasterLifePromise, int totalMasterLifeAchieved, int totalMasterLifePaid, int totalAchieved, int totalPaid) {
        this.totalFirstPromise = totalFirstPromise;
        this.totalSecondPromise = totalSecondPromise;
        this.totalThirdPromise = totalThirdPromise;
        this.totalFirstMasterLifePromise = totalFirstMasterLifePromise;
        this.totalSecondMasterLifePromise = totalSecondMasterLifePromise;
        this.totalThirdMasterLifePromise = totalThirdMasterLifePromise;
        this.totalMasterLifePromise = totalMasterLifePromise;
        this.totalMasterLifeAchieved = totalMasterLifeAchieved;
        this.totalMasterLifePaid = totalMasterLifePaid;
        this.totalAchieved = totalAchieved;
        this.totalPaid = totalPaid;
    }

    public int getTotalFirstPromise() {
        return totalFirstPromise;
    }

    public void setTotalFirstPromise(int totalFirstPromise) {
        this.totalFirstPromise = totalFirstPromise;
    }

    public int getTotalSecondPromise() {
        return totalSecondPromise;
    }

    public void setTotalSecondPromise(int totalSecondPromise) {
        this.totalSecondPromise = totalSecondPromise;
    }

    public int getTotalThirdPromise() {
        return totalThirdPromise;
    }

    public void setTotalThirdPromise(int totalThirdPromise) {
        this.totalThirdPromise = totalThirdPromise;
    }

    public int getTotalFirstMasterLifePromise() {
        return totalFirstMasterLifePromise;
    }

    public void setTotalFirstMasterLifePromise(int totalFirstMasterLifePromise) {
        this.totalFirstMasterLifePromise = totalFirstMasterLifePromise;
    }

    public int getTotalSecondMasterLifePromise() {
        return totalSecondMasterLifePromise;
    }

    public void setTotalSecondMasterLifePromise(int totalSecondMasterLifePromise) {
        this.totalSecondMasterLifePromise = totalSecondMasterLifePromise;
    }

    public int getTotalThirdMasterLifePromise() {
        return totalThirdMasterLifePromise;
    }

    public void setTotalThirdMasterLifePromise(int totalThirdMasterLifePromise) {
        this.totalThirdMasterLifePromise = totalThirdMasterLifePromise;
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
