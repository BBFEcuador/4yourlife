package com.foryourlife.admin.crm.call.infrastructure.http;

import java.time.LocalDateTime;

public class CallLogRequest {
    private String callType;
    private String callStatus;
    private String notes;
    private String calledById;
    private String calledUserId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public String getCallType() {
        return callType;
    }

    public String getCallStatus() {
        return callStatus;
    }

    public String getNotes() {
        return notes;
    }

    public String getCalledById() {
        return calledById;
    }

    public String getCalledUserId() {
        return calledUserId;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }


}
