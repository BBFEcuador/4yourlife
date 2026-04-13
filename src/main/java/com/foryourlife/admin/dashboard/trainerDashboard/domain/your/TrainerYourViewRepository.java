package com.foryourlife.admin.dashboard.trainerDashboard.domain.your;

public interface TrainerYourViewRepository {
    TrainerYourView getTrainerYourViewByTrainingId(String trainingId);
    String generateExcelReport(TrainerYourView trainerYourView);
}
