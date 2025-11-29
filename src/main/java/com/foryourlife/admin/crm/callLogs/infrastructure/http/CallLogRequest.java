package com.foryourlife.admin.crm.callLogs.infrastructure.http;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class CallLogRequest {
    private String callType;
    private String callStatus;
    private String notes;
    private String calledById;
    private LocalDate date;
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

    public LocalDate getDate() {
        return date;
    }

    public String getCallId() {
        return callId;
    }
}
