package com.foryourlife.admin.programs.trainer.trainerDashboard.domain.your;

import com.foryourlife.admin.programs.trainer.trainerDashboard.domain.focus.GeneralAttendance;
import com.foryourlife.admin.programs.trainer.trainerDashboard.domain.focus.PaymentDashboard;

import java.util.List;

public class TrainerYourView {
    private GeneralAttendance attendance;
    private List<PaymentYourDashboard> paymentDashboard;

    public TrainerYourView(GeneralAttendance attendance, List<PaymentYourDashboard> paymentDashboard) {
        this.attendance = attendance;
        this.paymentDashboard = paymentDashboard;
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
}
