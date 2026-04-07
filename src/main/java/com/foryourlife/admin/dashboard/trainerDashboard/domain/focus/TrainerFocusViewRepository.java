package com.foryourlife.admin.dashboard.trainerDashboard.domain.focus;

public interface TrainerFocusViewRepository {
    TrainerFocusView getTrainerFocusViewByTrainingId(String trainingId);
    String generateExcelReport(TrainerFocusView trainerFocusView);
}
