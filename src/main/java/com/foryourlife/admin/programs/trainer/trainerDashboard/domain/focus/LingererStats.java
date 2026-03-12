package com.foryourlife.admin.programs.trainer.trainerDashboard.domain.focus;

public class LingererStats {
    private int total;
    private int attended;
    private int notAttended;

    private int previousTraining;
    private int twoTrainingsAgo;
    private int others;

    public LingererStats(
            int total,
            int attended,
            int notAttended,
            int previousTraining,
            int twoTrainingsAgo,
            int others
    ) {
        this.total = total;
        this.attended = attended;
        this.notAttended = notAttended;
        this.previousTraining = previousTraining;
        this.twoTrainingsAgo = twoTrainingsAgo;
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

    public int getPreviousTraining() {
        return previousTraining;
    }

    public void setPreviousTraining(int previousTraining) {
        this.previousTraining = previousTraining;
    }

    public int getTwoTrainingsAgo() {
        return twoTrainingsAgo;
    }

    public void setTwoTrainingsAgo(int twoTrainingsAgo) {
        this.twoTrainingsAgo = twoTrainingsAgo;
    }

    public int getOthers() {
        return others;
    }

    public void setOthers(int others) {
        this.others = others;
    }
}
