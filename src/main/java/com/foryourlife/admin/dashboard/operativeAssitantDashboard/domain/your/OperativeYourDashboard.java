package com.foryourlife.admin.dashboard.operativeAssitantDashboard.domain.your;

import com.foryourlife.admin.dashboard.trainerDashboard.domain.common.GeneralAttendance;
import com.foryourlife.shared.domain.level.CourseLevel;

public class OperativeYourDashboard {
    private String trainingName;
    private String trainerName;
    private String courseLevel = CourseLevel.YOUR.getValue();
    private int captainCount;
    private int staffCount;
    private GeneralAttendance attendance;
    private OperativeYourPayments operativeYourPayments;

    public OperativeYourDashboard(String trainingName, String trainerName, int captainCount, int staffCount, GeneralAttendance attendance, OperativeYourPayments operativeYourPayments) {
        this.trainingName = trainingName;
        this.trainerName = trainerName;
        this.captainCount = captainCount;
        this.staffCount = staffCount;
        this.attendance = attendance;
        this.operativeYourPayments = operativeYourPayments;
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

    public GeneralAttendance getAttendance() {
        return attendance;
    }

    public void setAttendance(GeneralAttendance attendance) {
        this.attendance = attendance;
    }

    public OperativeYourPayments getOperativeYourPayments() {
        return operativeYourPayments;
    }

    public void setOperativeYourPayments(OperativeYourPayments operativeYourPayments) {
        this.operativeYourPayments = operativeYourPayments;
    }

    public int getCaptainCount() {
        return captainCount;
    }

    public void setCaptainCount(int captainCount) {
        this.captainCount = captainCount;
    }

    public int getStaffCount() {
        return staffCount;
    }

    public void setStaffCount(int staffCount) {
        this.staffCount = staffCount;
    }

    public String getCourseLevel() {
        return courseLevel;
    }

    public void setCourseLevel(String courseLevel) {
        this.courseLevel = courseLevel;
    }
}
