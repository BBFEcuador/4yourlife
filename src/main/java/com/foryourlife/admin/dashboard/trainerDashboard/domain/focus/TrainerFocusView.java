package com.foryourlife.admin.dashboard.trainerDashboard.domain.focus;

import com.foryourlife.admin.dashboard.trainerDashboard.domain.common.*;
import com.foryourlife.shared.domain.level.CourseLevel;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class TrainerFocusView {
    private String trainerName;
    private String trainingName;
    private String trainingDate;
    private CourseLevel courseLevel = CourseLevel.FOCUS;
    private GeneralAttendance focusAttendanceDashboard;
    private List<CityParticipantDashboard> cityParticipantDashboard;
    private List<GenderDashboard> genderByDay;
    private List<AgeDashboard> ageDashboard;
    private List<PaymentFocusDashboard> paymentFocusDashboard;
    private Map<String, LifeWeekendAssistant> lifeWeekendAssistants;
    private LingererStats lingererStats;
    private NextTrainingAttendance nextTrainingAttendance;

    public TrainerFocusView(String trainerName, String trainingName, String trainingDate, GeneralAttendance focusAttendanceDashboard, List<CityParticipantDashboard> cityParticipantDashboard, List<GenderDashboard> genderByDay, List<AgeDashboard> ageDashboard, List<PaymentFocusDashboard> paymentFocusDashboard, Map<String, LifeWeekendAssistant> lifeWeekendAssistants, LingererStats lingererStats, NextTrainingAttendance nextTrainingAttendance) {
        this.trainerName = trainerName;
        this.trainingName = trainingName;
        this.trainingDate = trainingDate;
        this.focusAttendanceDashboard = focusAttendanceDashboard;
        this.cityParticipantDashboard = cityParticipantDashboard;
        this.genderByDay = genderByDay;
        this.ageDashboard = ageDashboard;
        this.paymentFocusDashboard = paymentFocusDashboard;
        this.lifeWeekendAssistants = lifeWeekendAssistants;
        this.lingererStats = lingererStats;
        this.nextTrainingAttendance = nextTrainingAttendance;
    }

    public String getTrainingDate() {
        return trainingDate;
    }

    public void setTrainingDate(String trainingDate) {
        this.trainingDate = trainingDate;
    }

    public List<PaymentFocusDashboard> getPaymentDashboard() {
        return paymentFocusDashboard;
    }

    public void setPaymentDashboard(List<PaymentFocusDashboard> paymentFocusDashboard) {
        this.paymentFocusDashboard = paymentFocusDashboard;
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

    public Map<String, LifeWeekendAssistant> getLifeWeekendAssistants() {
        return lifeWeekendAssistants;
    }

    public void setLifeWeekendAssistants(Map<String, LifeWeekendAssistant> lifeWeekendAssistants) {
        this.lifeWeekendAssistants = lifeWeekendAssistants;
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

    public List<PaymentFocusDashboard> getPaymentFocusDashboard() {
        return paymentFocusDashboard;
    }

    public void setPaymentFocusDashboard(List<PaymentFocusDashboard> paymentFocusDashboard) {
        this.paymentFocusDashboard = paymentFocusDashboard;
    }

    public List<CityParticipantDashboard> getCityParticipantDashboard() {
        return cityParticipantDashboard;
    }

    public void setCityParticipantDashboard(List<CityParticipantDashboard> cityParticipantDashboard) {
        this.cityParticipantDashboard = cityParticipantDashboard;
    }

    public CourseLevel getCourseLevel() {
        return courseLevel;
    }

    public void setCourseLevel(CourseLevel courseLevel) {
        this.courseLevel = courseLevel;
    }
}
