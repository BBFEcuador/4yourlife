package com.foryourlife.admin.dashboard.operativeAssitantDashboard.domain;

import java.io.ByteArrayOutputStream;

public interface OperativeAssistantDashboardRepository {
    OperativeAssistantDashboard getOpAssistDashboardByTeamId(String teamId);
    ByteArrayOutputStream generateReport(String teamId);
}
