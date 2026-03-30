package com.foryourlife.admin.dashboard.operativeAssitantDashboard.domain.focus;

import com.foryourlife.admin.dashboard.operativeAssitantDashboard.domain.your.YourDailyStats;

import java.time.DayOfWeek;
import java.util.Map;

public class FocusWeeklyPaymentStats {
    private int weekNumber;
    private Map<DayOfWeek, FocusDailyStats> focusWeeklyPayments;

    public int getWeekNumber() {
        return weekNumber;
    }

    public void setWeekNumber(int weekNumber) {
        this.weekNumber = weekNumber;
    }

    public Map<DayOfWeek, FocusDailyStats> getFocusWeeklyPayments() {
        return focusWeeklyPayments;
    }

    public void setFocusWeeklyPayments(Map<DayOfWeek, FocusDailyStats> focusWeeklyPayments) {
        this.focusWeeklyPayments = focusWeeklyPayments;
    }
}
