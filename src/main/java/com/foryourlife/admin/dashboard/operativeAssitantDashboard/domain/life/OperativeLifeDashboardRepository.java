package com.foryourlife.admin.dashboard.operativeAssitantDashboard.domain.life;

import java.util.List;

public interface OperativeLifeDashboardRepository {
    public List<OperativeLifeDashboard> getOperativeLifeDashboard(String trainingId);
}
