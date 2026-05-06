package com.foryourlife.admin.programs.teams.application;

import com.foryourlife.admin.programs.teams.domain.TeamRepository;
import com.foryourlife.admin.programs.training.domain.TrainingRepository;
import com.foryourlife.clients.account.participantLevel.domain.ParticipantLevelRepository;
import com.foryourlife.clients.account.participant.domain.ParticipantRepository;
import com.foryourlife.shared.domain.bus.DomainEventSubscriber;
import com.foryourlife.shared.domain.events.TeamToTrainingAssigned;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
@DomainEventSubscriber({TeamToTrainingAssigned.class})
public class UpdateCourseLevelOnTeamAssigned {
    private final TrainingRepository trainingRepository;
    private final TeamRepository teamRepository;
    private final ParticipantRepository participantRepository;
    private final ParticipantLevelRepository participantLevelRepository;

    public UpdateCourseLevelOnTeamAssigned(TrainingRepository trainingRepository, TeamRepository teamRepository1, ParticipantRepository participantRepository, ParticipantLevelRepository participantLevelRepository) {
        this.trainingRepository = trainingRepository;
        this.teamRepository = teamRepository1;
        this.participantRepository = participantRepository;
        this.participantLevelRepository = participantLevelRepository;
    }

    @EventListener
    @TransactionalEventListener(
            phase = TransactionPhase.AFTER_COMMIT
    )
    public void on(TeamToTrainingAssigned event) {
        var training = trainingRepository.findById(event.getTraining().getId()).orElseThrow(() -> new RuntimeException(""));
        var team = this.teamRepository.findById(event.getTeam().getId()).orElseThrow(() -> new RuntimeException(""));
        TeamCommandService.assignLevelParticipant(training, team, participantRepository, participantLevelRepository);
    }
}
