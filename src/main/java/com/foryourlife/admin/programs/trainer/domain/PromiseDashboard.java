package com.foryourlife.admin.programs.trainer.domain;

public class PromiseDashboard {
    private int totalFirstPromise;
    private int totalSecondPromise;
    private int totalThirdPromise;
    private int totalAchieved;
    private int totalPaid;

    public PromiseDashboard(int totalFirstPromise, int totalSecondPromise, int totalThirdPromise, int totalAchieved, int totalPaid) {
        this.totalFirstPromise = totalFirstPromise;
        this.totalSecondPromise = totalSecondPromise;
        this.totalThirdPromise = totalThirdPromise;
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
