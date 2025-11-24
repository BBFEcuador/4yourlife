package com.foryourlife.admin.programs.training.application;

import com.foryourlife.admin.programs.teams.domain.TeamRepository;
import com.foryourlife.admin.programs.training.domain.TrainingRepository;
import com.foryourlife.shared.domain.bus.DomainEventSubscriber;
import com.foryourlife.shared.domain.events.TeamToTrainingAssigned;
import org.hibernate.Hibernate;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@DomainEventSubscriber({TeamToTrainingAssigned.class})
public class AddOriginalTeamOnTeamAssigned {
    private final TrainingRepository trainingRepository;
    private final TeamRepository teamRepository;

    public AddOriginalTeamOnTeamAssigned(TeamRepository teamRepository, TrainingRepository trainingRepository) {
        this.trainingRepository = trainingRepository;
        this.teamRepository = teamRepository;
    }

    @Async
    @EventListener
    @Transactional
    public void on(TeamToTrainingAssigned event) {
        var training = trainingRepository.findById(event.getTraining().getId())
                .orElseThrow(() -> new RuntimeException("Entrenamiento no encontrado: " + event.getTraining().getId()));
        var team = teamRepository.findById(event.getTeam().getId())
                .orElseThrow(() -> new RuntimeException("Equipo no encontrado: " + event.getTeam().getId()));

        Hibernate.initialize(team.getStaffs());
        Hibernate.initialize(team.getVisionaries());
        Hibernate.initialize(team.getMasterLife());
        Hibernate.initialize(team.getTrainer());

        training.setOriginalTeam(team);
        trainingRepository.save(training);
    }
}

