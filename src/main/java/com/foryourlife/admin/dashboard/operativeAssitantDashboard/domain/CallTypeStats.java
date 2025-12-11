package com.foryourlife.admin.dashboard.operativeAssitantDashboard.domain;

import com.foryourlife.admin.crm.callLogs.domain.CallType;

import java.util.List;

public class CallTypeStats {
    private CallType callType;
    private List<CallsInfo> statuses;

    public CallType getCallType() {
        return callType;
    }

    public void setCallType(CallType callType) {
        this.callType = callType;
    }

    public List<CallsInfo> getStatuses() {
        return statuses;
    }

    public void setStatuses(List<CallsInfo> statuses) {
        this.statuses = statuses;
    }
}