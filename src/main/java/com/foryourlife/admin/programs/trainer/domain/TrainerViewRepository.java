package com.foryourlife.admin.programs.trainer.domain;

public interface TrainerViewRepository {
    TrainerLifeView getTrainerLifeViewByTraining(String trainingId);
}
