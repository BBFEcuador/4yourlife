package com.foryourlife.admin.programs.trainer.domain;

import java.util.List;

public interface TrainerViewRepository {
    List<TrainerLifeView> getTrainerLifeViewByTraining(String trainingId);
}
