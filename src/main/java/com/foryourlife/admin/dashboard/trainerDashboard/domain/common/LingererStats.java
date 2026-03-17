package com.foryourlife.admin.dashboard.trainerDashboard.domain.common;

public class LingererStats {
    private int total;
    private int attended;
    private int notAttended;

    private LingererGroupStats previousTraining;
    private LingererGroupStats twoTrainingsAgo;
    private LingererGroupStats threeTrainingsAgo;
    private LingererGroupStats others;

    public LingererStats(int total, int attended, int notAttended, LingererGroupStats previousTraining, LingererGroupStats twoTrainingsAgo, LingererGroupStats threeTrainingsAgo, LingererGroupStats others) {
        this.total = total;
        this.attended = attended;
        this.notAttended = notAttended;
        this.previousTraining = previousTraining;
        this.twoTrainingsAgo = twoTrainingsAgo;
        this.threeTrainingsAgo = threeTrainingsAgo;
        this.others = others;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getAttended() {
        return attended;
    }

    public void setAttended(int attended) {
        this.attended = attended;
    }

    public int getNotAttended() {
        return notAttended;
    }

    public void setNotAttended(int notAttended) {
        this.notAttended = notAttended;
    }

    public LingererGroupStats getPreviousTraining() {
        return previousTraining;
    }

    public void setPreviousTraining(LingererGroupStats previousTraining) {
        this.previousTraining = previousTraining;
    }

    public LingererGroupStats getTwoTrainingsAgo() {
        return twoTrainingsAgo;
    }

    public void setTwoTrainingsAgo(LingererGroupStats twoTrainingsAgo) {
        this.twoTrainingsAgo = twoTrainingsAgo;
    }

    public LingererGroupStats getThreeTrainingsAgo() {
        return threeTrainingsAgo;
    }

    public void setThreeTrainingsAgo(LingererGroupStats threeTrainingsAgo) {
        this.threeTrainingsAgo = threeTrainingsAgo;
    }

    public LingererGroupStats getOthers() {
        return others;
    }

    public void setOthers(LingererGroupStats others) {
        this.others = others;
    }
}
