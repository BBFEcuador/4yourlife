package com.foryourlife.admin.dashboard.operativeAssitantDashboard.domain;

import java.time.DayOfWeek;
import java.util.Map;

public class WeeklyPaymentStats {
    private int weekNumber;
    private Map<DayOfWeek, DailyStats> statsPerDay;

    public int getWeekNumber() {
        return weekNumber;
    }

    public void setWeekNumber(int weekNumber) {
        this.weekNumber = weekNumber;
    }

    public Map<DayOfWeek, DailyStats> getStatsPerDay() {
        return statsPerDay;
    }

    public void setStatsPerDay(Map<DayOfWeek, DailyStats> statsPerDay) {
        this.statsPerDay = statsPerDay;
    }
}