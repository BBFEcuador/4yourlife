package com.foryourlife.admin.programs.attendance.application;

import com.foryourlife.admin.programs.attendance.domain.Attendance;
import com.foryourlife.admin.programs.attendance.domain.AttendanceRepository;
import com.foryourlife.admin.programs.attendance.domain.AttendanceStatus;
import com.foryourlife.admin.programs.attendance.domain.FylStage;
import com.foryourlife.admin.programs.teams.domain.TeamRepository;
import com.foryourlife.admin.programs.training.domain.TrainingRepository;
import com.foryourlife.clients.account.participant.domain.ParticipantRepository;
import com.foryourlife.shared.domain.bus.DomainEventSubscriber;
import com.foryourlife.shared.domain.events.OnNullDesistedAttend;
import com.foryourlife.shared.domain.events.TeamToTrainingAssigned;
import com.foryourlife.shared.domain.level.CourseLevel;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@DomainEventSubscriber({OnNullDesistedAttend.class})
public class RemoveParticipantOnNullAttend {
    private final TrainingRepository trainingRepository;
    private final TeamRepository teamRepository;
    private final ParticipantRepository participantRepository;
    private final AttendanceRepository attendanceRepository;

    public RemoveParticipantOnNullAttend(TrainingRepository trainingRepository, TeamRepository teamRepository, ParticipantRepository participantRepository, AttendanceRepository attendanceRepository) {
        this.trainingRepository = trainingRepository;
        this.teamRepository = teamRepository;
        this.participantRepository = participantRepository;
        this.attendanceRepository = attendanceRepository;
    }

    @EventListener
    public void on(OnNullDesistedAttend event) {
        var training = trainingRepository.findById(event.getTraining().getId()).orElseThrow();
        var team = teamRepository.findByTrainingId(training.getId()).orElseThrow();
        var user = event.getUser();
        var attendance = event.getAttendance();

        if (attendance.getFridayAttendance() == null) attendance.setFridayAttendance(AttendanceStatus.NO_ASISTIO);
        if (attendance.getSaturdayAttendance() == null) attendance.setSaturdayAttendance(AttendanceStatus.NO_ASISTIO);
        if (attendance.getSundayAttendance() == null) attendance.setSundayAttendance(AttendanceStatus.NO_ASISTIO);

        attendance.setActive(false);
        var participant = participantRepository.findByUserId(user.getId());
        if (participant.isPresent()) {
            participant.get().setIsDesertor(true);
            participant.get().setIsLingerer(true);
            team.removeUser(participant.get());
            participantRepository.save(participant.get());
        }
        teamRepository.save(team);
        attendanceRepository.save(attendance);
    }
}
