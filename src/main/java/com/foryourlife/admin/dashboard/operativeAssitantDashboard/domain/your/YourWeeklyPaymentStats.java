package com.foryourlife.admin.dashboard.operativeAssitantDashboard.domain.your;

import com.foryourlife.admin.dashboard.operativeAssitantDashboard.domain.DailyStats;

import java.time.DayOfWeek;
import java.util.Map;

public class YourWeeklyPaymentStats {
    private int weekNumber;
    private Map<DayOfWeek, YourDailyStats> weeklyPayments;

    public int getWeekNumber() {
        return weekNumber;
    }

    public void setWeekNumber(int weekNumber) {
        this.weekNumber = weekNumber;
    }

    public Map<DayOfWeek, YourDailyStats> getWeeklyPayments() {
        return weeklyPayments;
    }

    public void setWeeklyPayments(Map<DayOfWeek, YourDailyStats> weeklyPayments) {
        this.weeklyPayments = weeklyPayments;
    }
}
