package com.foryourlife.admin.programs.trainer.trainerDashboard.domain.life;

import java.util.List;

public interface TrainerViewRepository {
    List<TrainerLifeView> getTrainerLifeViewByTraining(String trainingId);
}
