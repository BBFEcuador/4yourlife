package com.foryourlife.admin.programs.attendance.application;

import com.foryourlife.admin.programs.attendance.domain.Attendance;
import com.foryourlife.admin.programs.attendance.domain.AttendanceRepository;
import com.foryourlife.admin.programs.attendance.domain.FylStage;
import com.foryourlife.admin.programs.teams.domain.TeamRepository;
import com.foryourlife.admin.programs.training.domain.TrainingRepository;
import com.foryourlife.shared.domain.bus.DomainEventSubscriber;
import com.foryourlife.shared.domain.events.TeamToTrainingAssigned;
import com.foryourlife.shared.domain.level.CourseLevel;
import jakarta.transaction.Transactional;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@DomainEventSubscriber({TeamToTrainingAssigned.class})
public class AddAttendanceOnTeamAssigned {
    private final TrainingRepository trainingRepository;
    private final TeamRepository teamRepository;
    private final AttendanceRepository attendanceRepository;

    public AddAttendanceOnTeamAssigned(TrainingRepository trainingRepository, TeamRepository teamRepository, AttendanceRepository attendanceRepository) {
        this.trainingRepository = trainingRepository;
        this.teamRepository = teamRepository;
        this.attendanceRepository = attendanceRepository;
    }

    @Async
    @EventListener
    public void on(TeamToTrainingAssigned event) {
        var training = trainingRepository.findById(event.getTraining().getId()).orElseThrow(() -> new RuntimeException(""));
        var team = this.teamRepository.findById(event.getTeam().getId()).orElseThrow(() -> new RuntimeException(""));
        team.getUsers().forEach(user -> {
            switch (training.getCourseLevel()){
                case CourseLevel.FOCUS:
                    attendanceRepository.save(Attendance.create(UUID.randomUUID().toString(),null,null,null, FylStage.FOCUS,user,training));
                    break;
                case CourseLevel.YOUR:
                    attendanceRepository.save(Attendance.create(UUID.randomUUID().toString(),null,null,null,FylStage.YOUR,user,training));
                    break;
                case CourseLevel.LIFE:
                    attendanceRepository.save(Attendance.create(UUID.randomUUID().toString(),null,null,null,FylStage.LIFE_1,user,training));
                    attendanceRepository.save(Attendance.create(UUID.randomUUID().toString(),null,null,null,FylStage.LIFE_2,user,training));
                    attendanceRepository.save(Attendance.create(UUID.randomUUID().toString(),null,null,null,FylStage.LIFE_3,user,training));
                    break;
            }
        });
    }
}
