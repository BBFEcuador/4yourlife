package com.foryourlife.admin.programs.trainer.trainerDashboard.domain.focus;

import java.util.List;

public class GeneralAttendance {
    private int initialPx;
    private int totalFocus;
    private int totalLingerer;
    private int totalDistorter;
    private double distortionPercentage;
    private List<UserAttendance> attendances;

    public GeneralAttendance(int initialPx, int totalFocus, int totalLingerer, int totalDistorter, double distortionPercentage, List<UserAttendance> attendances) {
        this.initialPx = initialPx;
        this.totalFocus = totalFocus;
        this.totalLingerer = totalLingerer;
        this.totalDistorter = totalDistorter;
        this.distortionPercentage = distortionPercentage;
        this.attendances = attendances;
    }

    public int getTotalFocus() {
        return totalFocus;
    }

    public void setTotalFocus(int totalFocus) {
        this.totalFocus = totalFocus;
    }

    public int getTotalLingerer() {
        return totalLingerer;
    }

    public void setTotalLingerer(int totalLingerer) {
        this.totalLingerer = totalLingerer;
    }

    public List<UserAttendance> getAttendances() {
        return attendances;
    }

    public void setAttendances(List<UserAttendance> attendances) {
        this.attendances = attendances;
    }

    public int getTotalDistorter() {
        return totalDistorter;
    }

    public void setTotalDistorter(int totalDistorter) {
        this.totalDistorter = totalDistorter;
    }

    public int getInitialPx() {
        return initialPx;
    }

    public void setInitialPx(int initialPx) {
        this.initialPx = initialPx;
    }

    public double getDistortionPercentage() {
        return distortionPercentage;
    }

    public void setDistortionPercentage(double distortionPercentage) {
        this.distortionPercentage = distortionPercentage;
    }
}
