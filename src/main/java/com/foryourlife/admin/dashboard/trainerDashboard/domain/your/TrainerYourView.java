package com.foryourlife.admin.dashboard.trainerDashboard.domain.your;

import com.foryourlife.admin.dashboard.trainerDashboard.domain.common.GeneralAttendance;
import com.foryourlife.admin.dashboard.trainerDashboard.domain.common.LingererStats;
import com.foryourlife.admin.dashboard.trainerDashboard.domain.common.NextTrainingAttendance;
import com.foryourlife.admin.dashboard.trainerDashboard.domain.common.PreviousTrainingStats;
import com.foryourlife.shared.domain.level.CourseLevel;

import java.util.List;

public class TrainerYourView {
    private String trainerName;
    private String trainingName;
    private String trainingDate;
    private CourseLevel courseLevel = CourseLevel.YOUR;
    private GeneralAttendance attendance;
    private List<PaymentYourDashboard> paymentYourDashboard;
    private LingererStats lingererStats;
    private NextTrainingAttendance nextTrainingAttendance;
    private PreviousTrainingStats previousTrainingStats;
    private YourRecoveryPaymentStats yourRecoveryPaymentStats;

    public TrainerYourView(String trainerName, String trainingName, String trainingDate, GeneralAttendance attendance, List<PaymentYourDashboard> paymentYourDashboard, LingererStats lingererStats, NextTrainingAttendance nextTrainingAttendance, PreviousTrainingStats previousTrainingStats, YourRecoveryPaymentStats yourRecoveryPaymentStats) {
        this.trainerName = trainerName;
        this.trainingName = trainingName;
        this.trainingDate = trainingDate;
        this.attendance = attendance;
        this.paymentYourDashboard = paymentYourDashboard;
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

    public List<PaymentYourDashboard> getPaymentYourDashboard() {
        return paymentYourDashboard;
    }

    public void setPaymentYourDashboard(List<PaymentYourDashboard> paymentYourDashboard) {
        this.paymentYourDashboard = paymentYourDashboard;
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

    public String getTrainerName() {
        return trainerName;
    }

    public void setTrainerName(String trainerName) {
        this.trainerName = trainerName;
    }

    public String getTrainingName() {
        return trainingName;
    }

    public void setTrainingName(String trainingName) {
        this.trainingName = trainingName;
    }

    public String getTrainingDate() {
        return trainingDate;
    }

    public void setTrainingDate(String trainingDate) {
        this.trainingDate = trainingDate;
    }

    public CourseLevel getCourseLevel() {
        return courseLevel;
    }

    public void setCourseLevel(CourseLevel courseLevel) {
        this.courseLevel = courseLevel;
    }
}
