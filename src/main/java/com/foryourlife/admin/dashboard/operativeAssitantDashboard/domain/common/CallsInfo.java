package com.foryourlife.admin.dashboard.operativeAssitantDashboard.domain.common;

import com.foryourlife.admin.crm.callLogs.domain.CallStatus;

public class CallsInfo {
    int totalCalls;
    CallStatus status;

    public int getTotalCalls() {
        return totalCalls;
    }

    public void setTotalCalls(int totalCalls) {
        this.totalCalls = totalCalls;
    }

    public CallStatus getStatus() {
        return status;
    }

    public void setStatus(CallStatus status) {
        this.status = status;
    }
}
