package com.foryourlife.admin.dashboard.operativeAssitantDashboard.domain;

import com.foryourlife.admin.crm.callLogs.domain.CallType;

import java.util.List;

public class CallTypeStats {
    private CallType callType;
    private List<CallsInfo> statuses;
    private int cuadre;
    private float effectivenessPercentage;
    private float projectedCallsPercentage;

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

    public int getCuadre() {
        return cuadre;
    }

    public void setCuadre(int cuadre) {
        this.cuadre = cuadre;
    }

    public float getEffectivenessPercentage() {
        return effectivenessPercentage;
    }

    public void setEffectivenessPercentage(float effectivenessPercentage) {
        this.effectivenessPercentage = effectivenessPercentage;
    }

    public float getProjectedCallsPercentage() {
        return projectedCallsPercentage;
    }

    public void setProjectedCallsPercentage(float projectedCallsPercentage) {
        this.projectedCallsPercentage = projectedCallsPercentage;
    }
}