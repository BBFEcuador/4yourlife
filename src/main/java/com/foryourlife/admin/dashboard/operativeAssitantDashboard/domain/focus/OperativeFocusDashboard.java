package com.foryourlife.admin.dashboard.operativeAssitantDashboard.domain.focus;

import com.foryourlife.shared.domain.level.CourseLevel;

public class OperativeFocusDashboard {
    private String trainingName;
    private String trainerName;
    private String courseLevel = CourseLevel.FOCUS.getValue();
    private WeekendFocusReport weekendFocusReport;
    private OperativeFocusPayments operativeFocusPayments;

    public OperativeFocusDashboard(String trainingName, String trainerName, WeekendFocusReport weekendFocusReport, OperativeFocusPayments operativeFocusPayments) {
        this.trainingName = trainingName;
        this.trainerName = trainerName;
        this.weekendFocusReport = weekendFocusReport;
        this.operativeFocusPayments = operativeFocusPayments;
    }

    public String getTrainingName() {
        return trainingName;
    }

    public void setTrainingName(String trainingName) {
        this.trainingName = trainingName;
    }

    public String getTrainerName() {
        return trainerName;
    }

    public void setTrainerName(String trainerName) {
        this.trainerName = trainerName;
    }

    public WeekendFocusReport getWeekendFocusReport() {
        return weekendFocusReport;
    }

    public void setWeekendFocusReport(WeekendFocusReport weekendFocusReport) {
        this.weekendFocusReport = weekendFocusReport;
    }

    public OperativeFocusPayments getOperativeFocusPayments() {
        return operativeFocusPayments;
    }

    public void setOperativeFocusPayments(OperativeFocusPayments operativeFocusPayments) {
        this.operativeFocusPayments = operativeFocusPayments;
    }

    public String getCourseLevel() {
        return courseLevel;
    }

    public void setCourseLevel(String courseLevel) {
        this.courseLevel = courseLevel;
    }
}
