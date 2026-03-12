package com.foryourlife.admin.programs.trainer.trainerDashboard.domain.your;

import com.foryourlife.admin.programs.trainer.trainerDashboard.domain.focus.GeneralAttendance;
import com.foryourlife.admin.programs.trainer.trainerDashboard.domain.focus.LingererStats;
import com.foryourlife.admin.programs.trainer.trainerDashboard.domain.focus.PaymentDashboard;

import java.util.List;

public class TrainerYourView {
    private GeneralAttendance attendance;
    private List<PaymentYourDashboard> paymentDashboard;
    private LingererStats  lingererStats;

    public TrainerYourView(GeneralAttendance attendance, List<PaymentYourDashboard> paymentDashboard, LingererStats lingererStats) {
        this.attendance = attendance;
        this.paymentDashboard = paymentDashboard;
        this.lingererStats = lingererStats;
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
}
