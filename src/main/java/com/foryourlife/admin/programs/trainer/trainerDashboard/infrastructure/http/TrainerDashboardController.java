package com.foryourlife.admin.programs.trainer.trainerDashboard.infrastructure.http;

import com.foryourlife.admin.programs.trainer.trainerDashboard.application.TrainerViewQueryService;
import com.foryourlife.admin.programs.trainer.trainerDashboard.domain.focus.TrainerFocusView;
import com.foryourlife.admin.programs.trainer.trainerDashboard.domain.life.TrainerLifeView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/trainer/view")
public class TrainerDashboardController {

    @Autowired
    private TrainerViewQueryService trainerViewQueryService;

    @GetMapping("/life/{id}")
    public ResponseEntity<List<TrainerLifeView>> getTrainerViewById(@PathVariable String id) {
        return new ResponseEntity<>(trainerViewQueryService.getTrainerView(id), HttpStatus.OK);
    }

    @GetMapping("/focus/{id}")
    public ResponseEntity<TrainerFocusView> getFocusDashboardById(@PathVariable String id) {
        return new ResponseEntity<>(trainerViewQueryService.getFocusView(id), HttpStatus.OK);
    }

    @GetMapping("/your/{id}")
    public ResponseEntity<?> getYourDashboardById(@PathVariable String id) {
        return new ResponseEntity<>(trainerViewQueryService.getYourView(id), HttpStatus.OK);
    }
}
