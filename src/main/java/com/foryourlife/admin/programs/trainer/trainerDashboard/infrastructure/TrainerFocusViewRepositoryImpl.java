package com.foryourlife.admin.programs.trainer.trainerDashboard.infrastructure;

import com.foryourlife.admin.programs.attendance.domain.Attendance;
import com.foryourlife.admin.programs.attendance.domain.AttendanceRepository;
import com.foryourlife.admin.programs.attendance.domain.AttendanceStatus;
import com.foryourlife.admin.programs.campus.domain.Campus;
import com.foryourlife.admin.programs.campus.domain.CampusRepository;
import com.foryourlife.admin.programs.trainer.trainerDashboard.domain.focus.*;
import com.foryourlife.clients.account.participant.domain.Participant;
import com.foryourlife.clients.account.participant.domain.ParticipantRepository;
import com.foryourlife.shared.domain.user.UserType;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class TrainerFocusViewRepositoryImpl implements TrainerFocusViewRepository {
    private final AttendanceRepository attendanceRepository;
    private final CampusRepository campusRepository;
    private final TrainerLifeViewRepositoryImpl trainerLifeViewRepositoryImpl;
    private final ParticipantRepository participantRepository;

    public TrainerFocusViewRepositoryImpl(AttendanceRepository attendanceRepository, CampusRepository campusRepository, TrainerLifeViewRepositoryImpl trainerLifeViewRepositoryImpl, ParticipantRepository participantRepository) {
        this.attendanceRepository = attendanceRepository;
        this.campusRepository = campusRepository;
        this.trainerLifeViewRepositoryImpl = trainerLifeViewRepositoryImpl;
        this.participantRepository = participantRepository;
    }

    @Override
    public TrainerFocusView getTrainerFocusViewByTrainingId(String trainingId) {
        var attendances = attendanceRepository.findAttendanceByTraining(trainingId);
        var campuses = campusRepository.getAll();


        return new TrainerFocusView(
                this.buildGeneralAttendance(attendances),
                this.buildCampusDashboard(attendances),
                this.buildGenderDashboard(attendances),
                this.buildAgeDashboard(attendances)
        );
    }

    public GeneralAttendance buildGeneralAttendance(List<Attendance> attendances) {
        List<UserAttendance> userAttendances = attendances.stream()
                .filter(a -> a.getUser() != null)
                .map(attendance -> {
                    String entity = trainerLifeViewRepositoryImpl.resolveUserType(attendance.getUser());
                    return new UserAttendance(
                            attendance.getUser().getName(),
                            UserType.valueOf(entity),
                            attendance.getFridayAttendance(),
                            attendance.getSaturdayAttendance(),
                            attendance.getSundayAttendance()
                    );
                })
                .toList();

        AtomicInteger totalLingerers = new AtomicInteger();
        AtomicInteger totalFocus = new AtomicInteger();

        attendances.forEach(
                attendance -> {
                    participantRepository.findByUserId(attendance.getUser().getId()).ifPresent(participant -> {
                                if (participant.getIsLingerer()) {
                                    totalLingerers.getAndIncrement();
                                } else {
                                    totalFocus.getAndIncrement();
                                }

                            }
                    );
                }
        );

        return new GeneralAttendance(
                totalLingerers.get(),
                totalFocus.get(),
                userAttendances
        );

    }

    public List<CampusDashboard> buildCampusDashboard(List<Attendance> attendances) {
        if (attendances == null || attendances.isEmpty()) {
            return Collections.emptyList();
        }

        Map<String, Map<String, Integer>> dayCampusCount = new HashMap<>();

        for (Attendance attendance : attendances) {
            if (attendance.getUser() == null || attendance.getUser().getEntityMap() == null) continue;

            var participantEntity = attendance.getUser().getEntityMap().stream()
                    .filter(e -> "PARTICIPANT".equals(e.getEntity()))
                    .findFirst()
                    .orElse(null);

            if (participantEntity == null) continue;


            var participant = participantRepository.findByUserId(participantEntity.getId()).orElse(null);

            if (participant == null || participant.getCampus() == null) continue;

            String campusName = participant.getCampus().getCity();

            if (attendance.getFridayAttendance() == AttendanceStatus.ASISTIO) {
                dayCampusCount
                        .computeIfAbsent("Viernes", k -> new HashMap<>())
                        .merge(campusName, 1, Integer::sum);
            }

            if (attendance.getSaturdayAttendance() == AttendanceStatus.ASISTIO) {
                dayCampusCount
                        .computeIfAbsent("Sabado", k -> new HashMap<>())
                        .merge(campusName, 1, Integer::sum);
            }

            if (attendance.getSundayAttendance() == AttendanceStatus.ASISTIO) {
                dayCampusCount
                        .computeIfAbsent("Domingo", k -> new HashMap<>())
                        .merge(campusName, 1, Integer::sum);
            }
        }

        return dayCampusCount.entrySet().stream()
                .map(entry -> {
                    String day = entry.getKey();
                    List<CampusAttendanceCount> campusCounts = entry.getValue().entrySet().stream()
                            .map(e -> new CampusAttendanceCount(e.getKey(), e.getValue()))
                            .toList();
                    return new CampusDashboard(day, campusCounts);
                })
                .toList();
    }


    public GenderDashboard buildGenderDashboard(List<Attendance> attendances) {
//        int maleCount = attendances.stream().filter(a -> a.getUser().getEntityMap());
//        long fridayCount = attendances.stream().filter(isParticipant).filter(a -> a.getFridayAttendance() == AttendanceStatus.ASISTIO).count();

        return null;
    }

    public AgeDashboard buildAgeDashboard(List<Attendance> attendances) {
        return null;
    }
}
