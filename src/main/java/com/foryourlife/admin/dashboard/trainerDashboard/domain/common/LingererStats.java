package com.foryourlife.admin.dashboard.trainerDashboard.domain.common;

public class LingererStats {
    private int total;
    private int attended;
    private int notAttended;

    private LingererGroupStats finalJornal;
    private LingererGroupStats penultimateJornal;
    private LingererGroupStats penultimateMatchJornal;
    private LingererGroupStats recovered;


    public LingererStats(int total, int attended, int notAttended, LingererGroupStats finalJornal, LingererGroupStats penultimateJornal, LingererGroupStats penultimateMatchJornal, LingererGroupStats recovered) {
        this.total = total;
        this.attended = attended;
        this.notAttended = notAttended;
        this.finalJornal = finalJornal;
        this.penultimateJornal = penultimateJornal;
        this.penultimateMatchJornal = penultimateMatchJornal;
        this.recovered = recovered;
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

    public LingererGroupStats getFinalJornal() {
        return finalJornal;
    }

    public void setFinalJornal(LingererGroupStats finalJornal) {
        this.finalJornal = finalJornal;
    }

    public LingererGroupStats getPenultimateJornal() {
        return penultimateJornal;
    }

    public void setPenultimateJornal(LingererGroupStats penultimateJornal) {
        this.penultimateJornal = penultimateJornal;
    }

    public LingererGroupStats getPenultimateMatchJornal() {
        return penultimateMatchJornal;
    }

    public void setPenultimateMatchJornal(LingererGroupStats penultimateMatchJornal) {
        this.penultimateMatchJornal = penultimateMatchJornal;
    }

    public LingererGroupStats getRecovered() {
        return recovered;
    }

    public void setRecovered(LingererGroupStats recovered) {
        this.recovered = recovered;
    }
}
