package com.foryourlife.admin.crm.statement.domain;

public class StatementDTO {
    private String staffName;
    private Statement statement;
    private StatementDtoStatusEnum yourPaymentStatus;
    private StatementDtoStatusEnum lifePaymentStatus;
    private String nickname;
    private String enrollmentTeam;
    private String comment;

    public StatementDTO(String staffName, Statement statement, StatementDtoStatusEnum yourPaymentStatus, StatementDtoStatusEnum lifePaymentStatus, String nickname, String enrollmentTeam, String comment) {
        this.staffName = staffName;
        this.statement = statement;
        this.yourPaymentStatus = yourPaymentStatus;
        this.lifePaymentStatus = lifePaymentStatus;
        this.nickname = nickname;
        this.enrollmentTeam = enrollmentTeam;
        this.comment = comment;
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
}
