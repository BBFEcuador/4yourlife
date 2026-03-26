package com.foryourlife.admin.dashboard.operativeAssitantDashboard.domain.your;

public class OperativeYourWeeklyPayments {
    private int dayNumber;
    private YourDailyStats yourDailyStats;

    public OperativeYourWeeklyPayments(int dayNumber, YourDailyStats yourDailyStats) {
        this.dayNumber = dayNumber;
        this.yourDailyStats = yourDailyStats;
    }

    public int getDayNumber() {
        return dayNumber;
    }

    public void setDayNumber(int dayNumber) {
        this.dayNumber = dayNumber;
    }

    public YourDailyStats getYourDailyStats() {
        return yourDailyStats;
    }

    public void setYourDailyStats(YourDailyStats yourDailyStats) {
        this.yourDailyStats = yourDailyStats;
    }
}
