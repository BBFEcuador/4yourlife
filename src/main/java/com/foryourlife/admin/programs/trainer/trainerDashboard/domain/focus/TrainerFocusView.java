package com.foryourlife.admin.programs.trainer.trainerDashboard.domain.focus;

import java.util.List;
import java.util.Map;

public class TrainerFocusView {
    private GeneralAttendance focusAttendanceDashboard;
    private List<GenderDashboard> genderByDay;
    private List<AgeDashboard> ageDashboard;
    private List<PaymentDashboard> paymentDashboard;
    private List<String> totalTrainings;
    private Map<String, LifeWeekendAssistant> lifeWeekendAssistants;

    public TrainerFocusView(GeneralAttendance focusAttendanceDashboard, List<GenderDashboard> genderByDay, List<AgeDashboard> ageDashboard, List<PaymentDashboard> paymentDashboard, List<String> totalTrainings, Map<String, LifeWeekendAssistant> lifeWeekendAssistants) {
        this.focusAttendanceDashboard = focusAttendanceDashboard;
        this.genderByDay = genderByDay;
        this.ageDashboard = ageDashboard;
        this.paymentDashboard = paymentDashboard;
        this.totalTrainings = totalTrainings;
        this.lifeWeekendAssistants = lifeWeekendAssistants;
    }

    public List<PaymentDashboard> getPaymentDashboard() {
        return paymentDashboard;
    }

    public void setPaymentDashboard(List<PaymentDashboard> paymentDashboard) {
        this.paymentDashboard = paymentDashboard;
    }

    public GeneralAttendance getFocusAttendanceDashboard() {
        return focusAttendanceDashboard;
    }

    public void setFocusAttendanceDashboard(GeneralAttendance focusAttendanceDashboard) {
        this.focusAttendanceDashboard = focusAttendanceDashboard;
    }

    public List<GenderDashboard> getGenderByDay() {
        return genderByDay;
    }

    public void setGenderByDay(List<GenderDashboard> genderByDay) {
        this.genderByDay = genderByDay;
    }

    public List<AgeDashboard> getAgeDashboard() {
        return ageDashboard;
    }

    public void setAgeDashboard(List<AgeDashboard> ageDashboard) {
        this.ageDashboard = ageDashboard;
    }

    public List<String> getTotalTrainings() {
        return totalTrainings;
    }

    public void setTotalTrainings(List<String> totalTrainings) {
        this.totalTrainings = totalTrainings;
    }

    public Map<String, LifeWeekendAssistant> getLifeWeekendAssistants() {
        return lifeWeekendAssistants;
    }

    public void setLifeWeekendAssistants(Map<String, LifeWeekendAssistant> lifeWeekendAssistants) {
        this.lifeWeekendAssistants = lifeWeekendAssistants;
    }
}
