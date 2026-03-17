package com.foryourlife.admin.dashboard.trainerDashboard.domain.your;

import com.foryourlife.admin.dashboard.trainerDashboard.domain.common.GeneralAttendance;
import com.foryourlife.admin.dashboard.trainerDashboard.domain.common.LingererStats;
import com.foryourlife.admin.dashboard.trainerDashboard.domain.common.NextTrainingAttendance;
import com.foryourlife.admin.dashboard.trainerDashboard.domain.common.PreviousTrainingStats;

import java.util.List;

public class TrainerYourView {
    private GeneralAttendance attendance;
    private List<PaymentYourDashboard> paymentDashboard;
    private LingererStats lingererStats;
    private NextTrainingAttendance nextTrainingAttendance;
    private PreviousTrainingStats previousTrainingStats;
    private YourRecoveryPaymentStats yourRecoveryPaymentStats;

    public TrainerYourView(GeneralAttendance attendance, List<PaymentYourDashboard> paymentDashboard, LingererStats lingererStats, NextTrainingAttendance nextTrainingAttendance, PreviousTrainingStats previousTrainingStats, YourRecoveryPaymentStats yourRecoveryPaymentStats) {
        this.attendance = attendance;
        this.paymentDashboard = paymentDashboard;
        this.lingererStats = lingererStats;
        this.nextTrainingAttendance = nextTrainingAttendance;
        this.previousTrainingStats = previousTrainingStats;
        this.yourRecoveryPaymentStats = yourRecoveryPaymentStats;
    }

    public GeneralAttendance getAttendance() {
        return attendance;
    }

    public void setAttendance(GeneralAttendance attendance) {
        this.attendance = attendance;
    }

    public List<PaymentYourDashboard> getPaymentDashboard() {
        return paymentDashboard;
    }

    public void setPaymentDashboard(List<PaymentYourDashboard> paymentDashboard) {
        this.paymentDashboard = paymentDashboard;
    }

    public LingererStats getLingererStats() {
        return lingererStats;
    }

    public void setLingererStats(LingererStats lingererStats) {
        this.lingererStats = lingererStats;
    }

    public NextTrainingAttendance getNextTrainingAttendance() {
        return nextTrainingAttendance;
    }

    public void setNextTrainingAttendance(NextTrainingAttendance nextTrainingAttendance) {
        this.nextTrainingAttendance = nextTrainingAttendance;
    }

    public PreviousTrainingStats getPreviousTrainingStats() {
        return previousTrainingStats;
    }

    public void setPreviousTrainingStats(PreviousTrainingStats previousTrainingStats) {
        this.previousTrainingStats = previousTrainingStats;
    }

    public YourRecoveryPaymentStats getYourRecoveryPaymentStats() {
        return yourRecoveryPaymentStats;
    }

    public void setYourRecoveryPaymentStats(YourRecoveryPaymentStats yourRecoveryPaymentStats) {
        this.yourRecoveryPaymentStats = yourRecoveryPaymentStats;
    }
}
