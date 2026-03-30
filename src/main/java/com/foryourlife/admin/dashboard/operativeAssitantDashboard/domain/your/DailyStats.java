package com.foryourlife.admin.dashboard.operativeAssitantDashboard.domain.your;

public class DailyStats {
    int participantsFinal;
    int yourCount;
    int yourLifeCount;
    int totalPayments;
    int partialPayments;
    double passPercent;
    double projectedPercent;

    public int getParticipantsFinal() {
        return participantsFinal;
    }

    public void setParticipantsFinal(int participantsFinal) {
        this.participantsFinal = participantsFinal;
    }

    public int getYourCount() {
        return yourCount;
    }

    public void setYourCount(int yourCount) {
        this.yourCount = yourCount;
    }

    public int getYourLifeCount() {
        return yourLifeCount;
    }

    public void setYourLifeCount(int yourLifeCount) {
        this.yourLifeCount = yourLifeCount;
    }

    public int getTotalPayments() {
        return totalPayments;
    }

    public void setTotalPayments(int totalPayments) {
        this.totalPayments = totalPayments;
    }

    public int getPartialPayments() {
        return partialPayments;
    }

    public void setPartialPayments(int partialPayments) {
        this.partialPayments = partialPayments;
    }

    public double getPassPercent() {
        return passPercent;
    }

    public void setPassPercent(double passPercent) {
        this.passPercent = passPercent;
    }

    public double getProjectedPercent() {
        return projectedPercent;
    }

    public void setProjectedPercent(double projectedPercent) {
        this.projectedPercent = projectedPercent;
    }
}