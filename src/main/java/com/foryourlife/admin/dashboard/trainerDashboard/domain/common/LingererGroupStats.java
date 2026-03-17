package com.foryourlife.admin.dashboard.trainerDashboard.domain.common;

public class LingererGroupStats {
    private int total;
    private int attended;

    public LingererGroupStats(int total, int attended) {
        this.total = total;
        this.attended = attended;
    }

    public void incrementTotal() {
        this.total++;
    }

    public void incrementAttended() {
        this.attended++;
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
}
