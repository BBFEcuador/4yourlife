package com.foryourlife.admin.programs.attendance.application;

import com.foryourlife.admin.programs.teams.domain.TeamRepository;
import com.foryourlife.admin.programs.training.domain.TrainingRepository;
import com.foryourlife.clients.account.promises.application.PromiseCommandService;
import com.foryourlife.shared.domain.bus.DomainEventSubscriber;
import com.foryourlife.shared.domain.events.TeamToTrainingAssigned;
import jakarta.transaction.Transactional;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
@DomainEventSubscriber({TeamToTrainingAssigned.class})
public class AddAttendanceOnTeamAssigned {
    private final TrainingRepository trainingRepository;
    private final TeamRepository teamRepository;
    private final PromiseCommandService promiseCommandService;
    private final AttendanceCommandService attendanceCommandService;

    public AddAttendanceOnTeamAssigned(TrainingRepository trainingRepository, TeamRepository teamRepository, PromiseCommandService promiseCommandService, AttendanceCommandService attendanceCommandService) {
        this.trainingRepository = trainingRepository;
        this.teamRepository = teamRepository;
        this.promiseCommandService = promiseCommandService;
        this.attendanceCommandService = attendanceCommandService;
    }

    @EventListener
    @Transactional
    public void on(TeamToTrainingAssigned event) {
        var training = trainingRepository.findById(event.getTraining().getId()).orElseThrow(() -> new RuntimeException(""));
        var team = this.teamRepository.findById(event.getTeam().getId()).orElseThrow(() -> new RuntimeException("Equipo no encontrado"));
        attendanceCommandService.assignAttendancesAndDeclarations(team, team.getTraining());
        promiseCommandService.createPromises(training.getId());
    }
}
