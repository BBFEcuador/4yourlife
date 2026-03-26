package com.foryourlife.admin.crm.statement.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.foryourlife.admin.programs.attendance.domain.AttendanceStatus;

public class StatementDTO {
    private String staffName;
    private String participantName;
    @JsonIgnoreProperties({"training", "participant"})
    private Statement statement;
    private StatementDtoStatusEnum yourPaymentStatus;
    private StatementDtoStatusEnum lifePaymentStatus;
    private String nickname;
    private String enrollmentTeam;
    private String enrollerName;
    private String comment;
    private AttendanceStatus attendanceStatus;

    public StatementDTO(String staffName, String participantName, Statement statement, StatementDtoStatusEnum yourPaymentStatus, StatementDtoStatusEnum lifePaymentStatus, String nickname, String enrollmentTeam, String enrollerName, String comment, AttendanceStatus attendanceStatus) {
        this.staffName = staffName;
        this.participantName = participantName;
        this.statement = statement;
        this.yourPaymentStatus = yourPaymentStatus;
        this.lifePaymentStatus = lifePaymentStatus;
        this.nickname = nickname;
        this.enrollmentTeam = enrollmentTeam;
        this.enrollerName = enrollerName;
        this.comment = comment;
        this.attendanceStatus = attendanceStatus;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public Statement getStatement() {
        return statement;
    }

    public void setStatement(Statement statement) {
        this.statement = statement;
    }

    public StatementDtoStatusEnum getYourPaymentStatus() {
        return yourPaymentStatus;
    }

    public void setYourPaymentStatus(StatementDtoStatusEnum yourPaymentStatus) {
        this.yourPaymentStatus = yourPaymentStatus;
    }

    public StatementDtoStatusEnum getLifePaymentStatus() {
        return lifePaymentStatus;
    }

    public void setLifePaymentStatus(StatementDtoStatusEnum lifePaymentStatus) {
        this.lifePaymentStatus = lifePaymentStatus;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getEnrollmentTeam() {
        return enrollmentTeam;
    }

    public void setEnrollmentTeam(String enrollmentTeam) {
        this.enrollmentTeam = enrollmentTeam;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getParticipantName() {
        return participantName;
    }

    public void setParticipantName(String participantName) {
        this.participantName = participantName;
    }

    public String getEnrollerName() {
        return enrollerName;
    }

    public void setEnrollerName(String enrollerName) {
        this.enrollerName = enrollerName;
    }

    public AttendanceStatus getAttendanceStatus() {
        return attendanceStatus;
    }

    public void setAttendanceStatus(AttendanceStatus attendanceStatus) {
        this.attendanceStatus = attendanceStatus;
    }
}
