package com.foryourlife.admin.programs.trainer.infrastructure.httpControllers;

import com.foryourlife.admin.programs.trainer.application.TrainerLoginService;
import com.foryourlife.admin.programs.trainer.domain.LoginTrainerResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class TrainerAuthController {
    @Autowired
    private TrainerLoginService trainerLoginService;

    @PostMapping("/trainer/login")
    public ResponseEntity<LoginTrainerResponse> login(@Valid @RequestBody LoginTrainerRequest request) {
        return new ResponseEntity<>(trainerLoginService.loginTrainer(request.email, request.password), HttpStatus.OK);
    }
}
