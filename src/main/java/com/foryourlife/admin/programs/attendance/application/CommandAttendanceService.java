package com.foryourlife.admin.programs.attendance.application;

import com.foryourlife.admin.crm.call.domain.Call;
import com.foryourlife.admin.crm.call.domain.CallRepository;
import com.foryourlife.admin.programs.attendance.domain.Attendance;
import com.foryourlife.admin.programs.attendance.domain.AttendanceRepository;
import com.foryourlife.admin.programs.attendance.domain.AttendanceStatus;
import com.foryourlife.admin.programs.attendance.domain.FylStage;
import com.foryourlife.admin.programs.attendance.infraestructure.httpController.DaysEnum;
import com.foryourlife.admin.programs.attendance.infraestructure.httpController.SaveAttendanceRequest;
import com.foryourlife.admin.programs.teams.domain.Team;
import com.foryourlife.admin.programs.training.domain.Training;
import com.foryourlife.clients.account.participant.domain.ParticipantRepository;
import com.foryourlife.clients.account.promises.application.PromiseCommandService;
import com.foryourlife.shared.domain.bus.EventBus;
import com.foryourlife.shared.domain.exception.BaseException;
import com.foryourlife.shared.domain.level.CourseLevel;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CommandAttendanceService {
    private final AttendanceRepository _attendanceRepository;
    private final EventBus bus;
    private final CallRepository callRepository;
    private final PromiseCommandService promiseCommandService;
    private final ParticipantRepository _participantRepository;

    public CommandAttendanceService(AttendanceRepository _attendanceRepository, EventBus bus, CallRepository callRepository, PromiseCommandService promiseCommandService, ParticipantRepository participantRepository) {
        this._attendanceRepository = _attendanceRepository;
        this.bus = bus;
        this.callRepository = callRepository;
        this.promiseCommandService = promiseCommandService;
        _participantRepository = participantRepository;
    }

    public void saveAttendance(SaveAttendanceRequest attendanceRequest) {

        var attendance = _attendanceRepository.findById(attendanceRequest.id)
                .orElseThrow(() -> new BaseException(
                        "Registro no encontrado",
                        List.of("El ID proporcionado no corresponde a ningún registro de asistencia.")
                ));

        DaysEnum dayEnum = DaysEnum.fromString(attendanceRequest.day);

        switch (dayEnum) {
            case FRIDAY -> {
                attendance.setFridayAttendance(attendanceRequest.attendanceStatus);
                if (attendance.getFridayAttendance().equals(AttendanceStatus.ASISTIO)) {
                    attendance.setSaturdayAttendance(AttendanceStatus.ASISTIO);
                    attendance.setSundayAttendance(AttendanceStatus.ASISTIO);
                } else if (attendance.getFridayAttendance().equals(AttendanceStatus.NO_ASISTIO)) {
                    attendance.setSaturdayAttendance(AttendanceStatus.DESERTO);
                    attendance.setSundayAttendance(AttendanceStatus.DESERTO);
                }
            }
            case SATURDAY -> {
                attendance.setSaturdayAttendance(attendanceRequest.attendanceStatus);
                if (attendance.getSaturdayAttendance().equals(AttendanceStatus.ASISTIO)) {
                    attendance.setSundayAttendance(AttendanceStatus.ASISTIO);
                } else if (attendance.getSaturdayAttendance().equals(AttendanceStatus.NO_ASISTIO) || attendance.getSaturdayAttendance().equals(AttendanceStatus.DESERTO)) {
                    attendance.setSundayAttendance(AttendanceStatus.DESERTO);
                }
            }
            case SUNDAY -> attendance.setSundayAttendance(attendanceRequest.attendanceStatus);
            default -> throw new BaseException(
                    "El día seleccionado no coincide con el día del entrenamiento",
                    List.of("El día proporcionado no es válido para registrar asistencia.")
            );
        }

        _attendanceRepository.save(attendance);

        updateParticipantAttendanceStatus(attendance);
    }

    public void closeAttendance(String id) {
        _attendanceRepository.findAttendanceByTraining(id).forEach(attendance -> {
            attendance.setActive(false);
            if (attendance.getFridayAttendance() == null) attendance.setFridayAttendance(AttendanceStatus.ASISTIO);
            if (attendance.getSaturdayAttendance() == null) attendance.setSaturdayAttendance(AttendanceStatus.ASISTIO);
            if (attendance.getSundayAttendance() == null) attendance.setSundayAttendance(AttendanceStatus.ASISTIO);
            updateParticipantAttendanceStatus(attendance);
            _attendanceRepository.save(attendance);
        });
    }

    public void assignAttendancesAndDeclarations(Team team, Training training) {
        team.getUsers().forEach(user -> {
            switch (training.getCourseLevel()) {
                case CourseLevel.FOCUS:
                    _attendanceRepository.save(Attendance.create(UUID.randomUUID().toString(), null, null, null, FylStage.FOCUS, user.getUser(), training));
                    callRepository.save(new Call(
                                    UUID.randomUUID().toString(),
                                    user.getUser(),
                                    training
                            )
                    );
                    break;
                case CourseLevel.YOUR:
                    _attendanceRepository.save(Attendance.create(UUID.randomUUID().toString(), null, null, null, FylStage.YOUR, user.getUser(), training));
                    callRepository.save(new Call(
                                    UUID.randomUUID().toString(),
                                    user.getUser(),
                                    training
                            )
                    );
                    break;
                case CourseLevel.LIFE:
                    _attendanceRepository.save(Attendance.create(UUID.randomUUID().toString(), null, null, null, FylStage.LIFE_1, user.getUser(), training));
                    callRepository.save(new Call(
                                    UUID.randomUUID().toString(),
                                    user.getUser(),
                                    training
                            )
                    );
                    break;
                case CourseLevel.LIFE_2:
                    _attendanceRepository.save(Attendance.create(UUID.randomUUID().toString(), null, null, null, FylStage.LIFE_2, user.getUser(), training));
                    callRepository.save(new Call(
                                    UUID.randomUUID().toString(),
                                    user.getUser(),
                                    training
                            )
                    );
                    break;
                case CourseLevel.LIFE_3:
                    _attendanceRepository.save(Attendance.create(UUID.randomUUID().toString(), null, null, null, FylStage.LIFE_3, user.getUser(), training));
                    callRepository.save(new Call(
                                    UUID.randomUUID().toString(),
                                    user.getUser(),
                                    training
                            )
                    );
                    break;
                case CourseLevel.LIFE_GRADUATE:
                    _attendanceRepository.save(Attendance.create(UUID.randomUUID().toString(), null, null, null, FylStage.LIFE_GRADUATE, user.getUser(), training));
                    callRepository.save(new Call(
                                    UUID.randomUUID().toString(),
                                    user.getUser(),
                                    training
                            )
                    );
                    break;
            }
        });

        team.getMasterLife().forEach(masterLifes -> {
            switch (training.getCourseLevel()) {
                case CourseLevel.LIFE:
                    _attendanceRepository.save(Attendance.create(UUID.randomUUID().toString(), null, null, null, FylStage.LIFE_1, masterLifes.getUser(), training));
                    break;
                case CourseLevel.LIFE_2:
                    _attendanceRepository.save(Attendance.create(UUID.randomUUID().toString(), null, null, null, FylStage.LIFE_2, masterLifes.getUser(), training));
                    break;
                case CourseLevel.LIFE_3:
                    _attendanceRepository.save(Attendance.create(UUID.randomUUID().toString(), null, null, null, FylStage.LIFE_3, masterLifes.getUser(), training));
                    break;
                case CourseLevel.LIFE_GRADUATE:
                    _attendanceRepository.save(Attendance.create(UUID.randomUUID().toString(), null, null, null, FylStage.LIFE_GRADUATE, masterLifes.getUser(), training));
                    break;
            }
        });
        promiseCommandService.createPromises(training.getId());
    }

    public void updateParticipantAttendanceStatus(Attendance attendance) {

        var user = attendance.getUser();
        var participant = _participantRepository.findByUserId(user.getId());

        if (participant.isPresent()) {

            boolean fullAttendance = attendance.HasFullAttendance();

            participant.get().setIsDesertor(!fullAttendance);
            participant.get().setIsLingerer(!fullAttendance);

            _participantRepository.save(participant.get());
        }
    }
}
