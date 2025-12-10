package com.foryourlife.admin.dashboard.operativeAssitantDashboard.domain;

import com.foryourlife.shared.domain.level.CourseLevel;

import java.util.List;

public class TrainingInfo {
    CourseLevel courseLevel;
    String trainerName;
    String teamName;
    String teamNumber;
    int totalParticipants;
    int totalParticipantAssistants;
    int totalParticipantsDeclarations;
    int totalMasterLifes;
    int totalMasterLifesAssistants;
    int totalMasterLifesDeclarations;
    int totalEnrolments;
    int totalEnrolmentsAssistants;
    int totalEnrolmentsDeclarations;
    List<CallsInfo> callsInfoList;
    List<WeeklyPaymentStats> weeklyPaymentStatsList;

    public TrainingInfo(CourseLevel courseLevel, String trainerName, String teamName, String teamNumber, int totalParticipants, int totalParticipantAssistants, int totalParticipantsDeclarations, int totalMasterLifes, int totalMasterLifesAssistants, int totalMasterLifesDeclarations, int totalEnrolments, int totalEnrolmentsAssistants, int totalEnrolmentsDeclarations, List<CallsInfo> callsInfoList, List<WeeklyPaymentStats> weeklyPaymentStatsList) {
        this.courseLevel = courseLevel;
        this.trainerName = trainerName;
        this.teamName = teamName;
        this.teamNumber = teamNumber;
        this.totalParticipants = totalParticipants;
        this.totalParticipantAssistants = totalParticipantAssistants;
        this.totalParticipantsDeclarations = totalParticipantsDeclarations;
        this.totalMasterLifes = totalMasterLifes;
        this.totalMasterLifesAssistants = totalMasterLifesAssistants;
        this.totalMasterLifesDeclarations = totalMasterLifesDeclarations;
        this.totalEnrolments = totalEnrolments;
        this.totalEnrolmentsAssistants = totalEnrolmentsAssistants;
        this.totalEnrolmentsDeclarations = totalEnrolmentsDeclarations;
        this.callsInfoList = callsInfoList;
        this.weeklyPaymentStatsList = weeklyPaymentStatsList;
    }

    public int getTotalParticipantAssistants() {
        return totalParticipantAssistants;
    }

    public void setTotalParticipantAssistants(int totalParticipantAssistants) {
        this.totalParticipantAssistants = totalParticipantAssistants;
    }

    public int getTotalMasterLifesAssistants() {
        return totalMasterLifesAssistants;
    }

    public void setTotalMasterLifesAssistants(int totalMasterLifesAssistants) {
        this.totalMasterLifesAssistants = totalMasterLifesAssistants;
    }

    public int getTotalEnrolmentsAssistants() {
        return totalEnrolmentsAssistants;
    }

    public void setTotalEnrolmentsAssistants(int totalEnrolmentsAssistants) {
        this.totalEnrolmentsAssistants = totalEnrolmentsAssistants;
    }

    public CourseLevel getCourseLevel() {
        return courseLevel;
    }

    public void setCourseLevel(CourseLevel courseLevel) {
        this.courseLevel = courseLevel;
    }

    public List<CallsInfo> getCallsInfoList() {
        return callsInfoList;
    }

    public void setCallsInfoList(List<CallsInfo> callsInfoList) {
        this.callsInfoList = callsInfoList;
    }

    public String getTrainerName() {
        return trainerName;
    }

    public void setTrainerName(String trainerName) {
        this.trainerName = trainerName;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getTeamNumber() {
        return teamNumber;
    }

    public void setTeamNumber(String teamNumber) {
        this.teamNumber = teamNumber;
    }

    public int getTotalParticipants() {
        return totalParticipants;
    }

    public void setTotalParticipants(int totalParticipants) {
        this.totalParticipants = totalParticipants;
    }

    public int getTotalParticipantsDeclarations() {
        return totalParticipantsDeclarations;
    }

    public void setTotalParticipantsDeclarations(int totalParticipantsDeclarations) {
        this.totalParticipantsDeclarations = totalParticipantsDeclarations;
    }

    public int getTotalMasterLifes() {
        return totalMasterLifes;
    }

    public void setTotalMasterLifes(int totalMasterLifes) {
        this.totalMasterLifes = totalMasterLifes;
    }

    public int getTotalMasterLifesDeclarations() {
        return totalMasterLifesDeclarations;
    }

    public void setTotalMasterLifesDeclarations(int totalMasterLifesDeclarations) {
        this.totalMasterLifesDeclarations = totalMasterLifesDeclarations;
    }

    public int getTotalEnrolments() {
        return totalEnrolments;
    }

    public void setTotalEnrolments(int totalEnrolments) {
        this.totalEnrolments = totalEnrolments;
    }

    public int getTotalEnrolmentsDeclarations() {
        return totalEnrolmentsDeclarations;
    }

    public void setTotalEnrolmentsDeclarations(int totalEnrolmentsDeclarations) {
        this.totalEnrolmentsDeclarations = totalEnrolmentsDeclarations;
    }
}
