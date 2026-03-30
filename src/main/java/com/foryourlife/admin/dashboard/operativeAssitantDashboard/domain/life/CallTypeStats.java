package com.foryourlife.admin.dashboard.operativeAssitantDashboard.domain.life;

import com.foryourlife.admin.crm.callLogs.domain.CallType;

import java.util.List;

public class CallTypeStats {
    private CallType callType;
    private List<CallsInfo> statuses;
    private int cuadre;
    private double effectivenessPercentage;
    private double projectedCallsPercentage;

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

    public double getEffectivenessPercentage() {
        return effectivenessPercentage;
    }

    public void setEffectivenessPercentage(double effectivenessPercentage) {
        this.effectivenessPercentage = effectivenessPercentage;
    }

    public double getProjectedCallsPercentage() {
        return projectedCallsPercentage;
    }

    public void setProjectedCallsPercentage(double projectedCallsPercentage) {
        this.projectedCallsPercentage = projectedCallsPercentage;
    }
}