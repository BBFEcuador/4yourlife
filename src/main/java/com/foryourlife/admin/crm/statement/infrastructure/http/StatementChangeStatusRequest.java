package com.foryourlife.admin.crm.statement.infrastructure.http;

public class StatementChangeStatusRequest {
    private String comment;
    private String status;

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
