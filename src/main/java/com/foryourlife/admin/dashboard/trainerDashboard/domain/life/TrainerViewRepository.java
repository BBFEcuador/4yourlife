package com.foryourlife.admin.dashboard.trainerDashboard.domain.life;

import java.util.List;

public interface TrainerViewRepository {
    List<TrainerLifeView> getTrainerLifeViewByTraining(String trainingId);
    String generateExcelReport(List<TrainerLifeView> trainerLifeViews);
}
