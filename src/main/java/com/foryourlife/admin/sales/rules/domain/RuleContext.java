package com.foryourlife.admin.sales.rules.domain;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.TextStyle;
import java.util.Locale;

public class RuleContext {
    private String day;
    private String timePeriod;
    private boolean isWeekend;
    private boolean isMaster;
    private boolean isDuringTraining;
    private boolean isPreEntry;
    private boolean isEvent;

    public RuleContext() {
    }

    public RuleContext(String day, String timePeriod, boolean isWeekend, boolean isMaster, boolean isDuringTraining, boolean isPreEntry, boolean isEvent) {
        this.day = day;
        this.timePeriod = timePeriod;
        this.isWeekend = isWeekend;
        this.isMaster = isMaster;
        this.isDuringTraining = isDuringTraining;
        this.isPreEntry = isPreEntry;
        this.isEvent = isEvent;
    }

    public static RuleContext autoFromSystem() {
        RuleContext ctx = new RuleContext();

        DayOfWeek dayOfWeek = LocalDate.now().getDayOfWeek();
        ctx.day = dayOfWeek.getDisplayName(TextStyle.FULL, new Locale("es", "ES")).toUpperCase(); // "SUNDAY", "SATURDAY", etc.

        LocalTime now = LocalTime.now();
        if (now.isBefore(LocalTime.of(12, 0))) {
            ctx.timePeriod = "MORNING";
        } else if (now.isBefore(LocalTime.of(18, 0))) {
            ctx.timePeriod = "AFTERNOON";
        } else {
            ctx.timePeriod = "NIGHT";
        }

        ctx.isWeekend = (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY);

        ctx.isMaster = false;
        ctx.isDuringTraining = false;
        ctx.isPreEntry = false;
        ctx.isEvent = false;

        return ctx;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getTimePeriod() {
        return timePeriod;
    }

    public void setTimePeriod(String timePeriod) {
        this.timePeriod = timePeriod;
    }

    public boolean isWeekend() {
        return isWeekend;
    }

    public void setWeekend(boolean weekend) {
        isWeekend = weekend;
    }

    public boolean isMaster() {
        return isMaster;
    }

    public void setMaster(boolean master) {
        isMaster = master;
    }

    public boolean isDuringTraining() {
        return isDuringTraining;
    }

    public void setDuringTraining(boolean duringTraining) {
        isDuringTraining = duringTraining;
    }

    public boolean isPreEntry() {
        return isPreEntry;
    }

    public void setPreEntry(boolean preEntry) {
        isPreEntry = preEntry;
    }

    public boolean isEvent() {
        return isEvent;
    }

    public void setEvent(boolean event) {
        isEvent = event;
    }
}
