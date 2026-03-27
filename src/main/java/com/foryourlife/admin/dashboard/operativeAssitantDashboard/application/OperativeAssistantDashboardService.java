package com.foryourlife.admin.dashboard.operativeAssitantDashboard.application;

import com.foryourlife.admin.dashboard.operativeAssitantDashboard.domain.OperativeAssistantDashboard;
import com.foryourlife.admin.dashboard.operativeAssitantDashboard.domain.OperativeAssistantDashboardRepository;
import com.foryourlife.admin.dashboard.operativeAssitantDashboard.domain.focus.OperativeFocusDashboard;
import com.foryourlife.admin.dashboard.operativeAssitantDashboard.domain.focus.OperativeFocusDashboardRepository;
import com.foryourlife.admin.dashboard.operativeAssitantDashboard.domain.life.OperativeLifeDashboard;
import com.foryourlife.admin.dashboard.operativeAssitantDashboard.domain.life.OperativeLifeDashboardRepository;
import com.foryourlife.admin.dashboard.operativeAssitantDashboard.domain.your.OperativeYourDashboard;
import com.foryourlife.admin.dashboard.operativeAssitantDashboard.domain.your.OperativeYourDashboardRepository;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
public class OperativeAssistantDashboardService {
    private final OperativeAssistantDashboardRepository operativeAssistantDashboardRepository;
    private final OperativeFocusDashboardRepository operativeFocusDashboardRepository;
    private final OperativeYourDashboardRepository operativeYourDashboardRepository;
    private final OperativeLifeDashboardRepository operativeLifeDashboardRepository;

    public OperativeAssistantDashboardService(OperativeAssistantDashboardRepository operativeAssistantDashboardRepository, OperativeFocusDashboardRepository operativeFocusDashboardRepository, OperativeYourDashboardRepository operativeYourDashboardRepository, OperativeLifeDashboardRepository operativeLifeDashboardRepository) {
        this.operativeAssistantDashboardRepository = operativeAssistantDashboardRepository;
        this.operativeFocusDashboardRepository = operativeFocusDashboardRepository;
        this.operativeYourDashboardRepository = operativeYourDashboardRepository;
        this.operativeLifeDashboardRepository = operativeLifeDashboardRepository;
    }

    public OperativeAssistantDashboard getDashboardByTeamId(String teamId) {
        return operativeAssistantDashboardRepository.getOpAssistDashboardByTeamId(teamId);
    }

    public ByteArrayOutputStream generateReport(String trainingId) {
        return operativeAssistantDashboardRepository.generateReport(trainingId);
    }

    public OperativeFocusDashboard getFocusDashboardByTrainingId(String trainingId) {
        return operativeFocusDashboardRepository.getOpAssistDashboardByTrainingId(trainingId);
    }

    public OperativeYourDashboard  getYourDashboardByTrainingId(String trainingId) {
        return operativeYourDashboardRepository.findByTrainingId(trainingId);
    }

    public List<OperativeLifeDashboard> getLifeDashboardByTrainingId(String trainingId) {
        return operativeLifeDashboardRepository.getOperativeLifeDashboard(trainingId);
    }
}
