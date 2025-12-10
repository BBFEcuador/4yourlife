package com.foryourlife.admin.dashboard.operativeAssitantDashboard.application;

import com.foryourlife.admin.dashboard.operativeAssitantDashboard.domain.OperativeAssistantDashboard;
import com.foryourlife.admin.dashboard.operativeAssitantDashboard.domain.OperativeAssistantDashboardRepository;
import org.springframework.stereotype.Service;

@Service
public class OperativeAssistantDashboardService {
    private final OperativeAssistantDashboardRepository operativeAssistantDashboardRepository;

    public OperativeAssistantDashboardService(OperativeAssistantDashboardRepository operativeAssistantDashboardRepository) {
        this.operativeAssistantDashboardRepository = operativeAssistantDashboardRepository;
    }

    public OperativeAssistantDashboard getDashboardByTeamId(String teamId) {
        return operativeAssistantDashboardRepository.getOpAssistDashboardByTeamId(teamId);
    }
}
