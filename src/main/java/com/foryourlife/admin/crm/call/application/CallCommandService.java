package com.foryourlife.admin.crm.call.application;

import com.foryourlife.admin.crm.call.domain.Call;
import com.foryourlife.admin.crm.call.domain.CallRepository;
import com.foryourlife.admin.crm.call.infrastructure.http.CallRequest;
import com.foryourlife.admin.programs.training.domain.TrainingRepository;
import com.foryourlife.shared.domain.exception.BaseException;
import com.foryourlife.shared.domain.user.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CallCommandService {
    private final CallRepository callRepository;
    private final UserRepository userRepository;
    private final TrainingRepository trainingRepository;

    public CallCommandService(CallRepository callRepository, UserRepository userRepository, TrainingRepository trainingRepository) {
        this.callRepository = callRepository;
        this.userRepository = userRepository;
        this.trainingRepository = trainingRepository;
    }

    public void createCall(CallRequest callRequest) {
        var calledUser = userRepository.findById(callRequest.getCalledUserId()).orElseThrow(
                () -> new BaseException("Usuario llamado no encontrado", List.of())
        );

        var training = trainingRepository.findById(callRequest.getTrainingId()).orElseThrow(
                () -> new BaseException("Entrenamiento no encontrado", List.of())
        );

        var call = new Call(
                UUID.randomUUID().toString(),
                calledUser,
                training
        );

        callRepository.save(call);
    }
}
