package com.foryourlife.admin.crm.callLogs.infrastructure.http;

import java.time.LocalDateTime;

public class CallLogRequest {
    private String callType;
    private String callStatus;
    private String notes;
    private String calledById;
    private LocalDateTime date;
    private String callId;

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

    public LocalDateTime getDate() {
        return date;
    }

    public String getCallId() {
        return callId;
    }
}
