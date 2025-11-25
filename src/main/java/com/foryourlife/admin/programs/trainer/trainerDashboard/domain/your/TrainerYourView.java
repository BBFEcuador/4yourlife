package com.foryourlife.admin.programs.trainer.trainerDashboard.domain.your;

import com.foryourlife.admin.programs.trainer.trainerDashboard.domain.focus.GeneralAttendance;
import com.foryourlife.admin.programs.trainer.trainerDashboard.domain.focus.PaymentDashboard;

import java.util.List;

public class TrainerYourView {
    private GeneralAttendance attendance;
    private FocusResumeDashboard focusResumeDashboard;
    private List<PaymentDashboard> paymentDashboard;

    public TrainerYourView(GeneralAttendance attendance, FocusResumeDashboard focusResumeDashboard, List<PaymentDashboard> paymentDashboard) {
        this.attendance = attendance;
        this.focusResumeDashboard = focusResumeDashboard;
        this.paymentDashboard = paymentDashboard;
    }

    public GeneralAttendance getAttendance() {
        return attendance;
    }

    public void setAttendance(GeneralAttendance attendance) {
        this.attendance = attendance;
    }

    public FocusResumeDashboard getFocusResumeDashboard() {
        return focusResumeDashboard;
    }

    public void setFocusResumeDashboard(FocusResumeDashboard focusResumeDashboard) {
        this.focusResumeDashboard = focusResumeDashboard;
    }

    public List<PaymentDashboard> getPaymentDashboard() {
        return paymentDashboard;
    }

    public void setPaymentDashboard(List<PaymentDashboard> paymentDashboard) {
        this.paymentDashboard = paymentDashboard;
    }
}
