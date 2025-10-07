package com.foryourlife.admin.programs.trainer.application;

import com.foryourlife.admin.programs.trainer.domain.LoginTrainerResponse;
import com.foryourlife.admin.programs.trainer.domain.TrainerRepository;
import org.springframework.stereotype.Service;

@Service
public class TrainerLoginService {
    private final TrainerRepository _trainerRepository;

    public TrainerLoginService(TrainerRepository trainerRepository) {
        _trainerRepository = trainerRepository;
    }

    public LoginTrainerResponse loginTrainer(String email, String password) {
        return _trainerRepository.loginTrainer(email, password);
    }
}
