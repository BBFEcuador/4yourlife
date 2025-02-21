package com.foryourlife.admin.programs.teams.application;

import com.foryourlife.admin.programs.teams.domain.TeamRepository;
import com.foryourlife.admin.programs.training.domain.TrainingRepository;
import com.foryourlife.clients.account.participantLevel.domain.ParticipantLevel;
import com.foryourlife.clients.account.participantLevel.domain.ParticipantLevelRepository;
import com.foryourlife.clients.account.user.domain.Participant;
import com.foryourlife.clients.account.user.domain.UserRepository;
import com.foryourlife.shared.domain.bus.DomainEventSubscriber;
import com.foryourlife.shared.domain.events.TeamToTrainingAssigned;
import jakarta.transaction.Transactional;
import org.springframework.context.event.EventListener;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@DomainEventSubscriber({TeamToTrainingAssigned.class})
public class UpdateCourseLevelOnTeamAssigned {
    private final TrainingRepository trainingRepository;
    private final TeamRepository teamRepository;
    private final UserRepository userRepository;
    private final ParticipantLevelRepository participantLevelRepository;

    public UpdateCourseLevelOnTeamAssigned( TrainingRepository trainingRepository, TeamRepository teamRepository1, UserRepository userRepository, ParticipantLevelRepository participantLevelRepository) {
        this.trainingRepository = trainingRepository;
        this.teamRepository = teamRepository1;
        this.userRepository = userRepository;
        this.participantLevelRepository = participantLevelRepository;
    }

    @Async
    @EventListener
    @Transactional
    public void on(TeamToTrainingAssigned event) {
        var training = trainingRepository.findById(event.getTraining().getId()).orElseThrow(() -> new RuntimeException(""));
        var team = this.teamRepository.findById(event.getTeam().getId()).orElseThrow(() -> new RuntimeException(""));
        team.getUsers().forEach(user -> {
            var participant = userRepository.findById(user.getId()).orElseThrow(() -> new RuntimeException(""));
            participant.setParticipantLevel(
                    (participantLevelRepository.findOneByCriteria(
                            (root, query, cb) ->
                                    cb.equal(root.get("courseLevel"), training.getCourseLevel())
                            ).orElseThrow(() -> new RuntimeException(""))
                    )
            );
            userRepository.save(participant);
        });
    }
}
