package com.foryourlife.admin.dashboard.operativeAssitantDashboard.infrastructure.utils;

import com.foryourlife.admin.dashboard.operativeAssitantDashboard.domain.focus.OperativeFocusDashboard;
import com.foryourlife.admin.dashboard.operativeAssitantDashboard.domain.focus.OperativeFocusDashboardRepository;
import org.springframework.stereotype.Service;

@Service
public class ImplOperativeFocusDashboardRepository implements OperativeFocusDashboardRepository {
    @Override
    public OperativeFocusDashboard getOpAssistDashboardByTrainingId(String trainingId) {
        return null;
    }
}
