package com.foryourlife.admin.programs.attendance.application;

import com.foryourlife.admin.crm.call.domain.Call;
import com.foryourlife.admin.crm.call.domain.CallRepository;
import com.foryourlife.admin.crm.statement.application.StatementCommandService;
import com.foryourlife.admin.crm.statement.domain.Statement;
import com.foryourlife.admin.crm.statement.domain.StatementStatusEnum;
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
import com.foryourlife.shared.domain.exception.BaseException;
import com.foryourlife.shared.domain.level.CourseLevel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class CommandAttendanceService {
    private final AttendanceRepository _attendanceRepository;
    private final CallRepository callRepository;
    private final PromiseCommandService promiseCommandService;
    private final ParticipantRepository _participantRepository;
    private final StatementCommandService statementCommandService;

    public CommandAttendanceService(AttendanceRepository _attendanceRepository, CallRepository callRepository, PromiseCommandService promiseCommandService, ParticipantRepository _participantRepository, StatementCommandService statementCommandService) {
        this._attendanceRepository = _attendanceRepository;
        this.callRepository = callRepository;
        this.promiseCommandService = promiseCommandService;
        this._participantRepository = _participantRepository;
        this.statementCommandService = statementCommandService;
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

    @Transactional
    public void assignAttendancesAndDeclarations(Team team, Training training) {

        FylStage stage = mapStage(training.getCourseLevel());

        List<Attendance> attendances = team.getUsers().stream()
                .map(user -> Attendance.create(
                        UUID.randomUUID().toString(),
                        null,
                        null,
                        null,
                        stage,
                        user.getUser(),
                        training
                ))
                .toList();

        List<Call> calls = team.getUsers().stream()
                .map(user -> new Call(
                        UUID.randomUUID().toString(),
                        user.getUser(),
                        training
                ))
                .toList();

        List<Attendance> masterAttendances = team.getMasterLife().stream()
                .filter(m -> isLifeLevel(training.getCourseLevel()))
                .map(m -> Attendance.create(
                        UUID.randomUUID().toString(),
                        null,
                        null,
                        null,
                        stage,
                        m.getUser(),
                        training
                ))
                .toList();

        _attendanceRepository.saveAll(attendances);
        _attendanceRepository.saveAll(masterAttendances);
        callRepository.saveAll(calls);

        promiseCommandService.createPromises(training.getId());

        if (training.getCourseLevel().equals(CourseLevel.FOCUS) || training.getCourseLevel().equals(CourseLevel.YOUR)) {;
            List<Statement> statements = team.getUsers().stream()
                    .map(participant -> new Statement(
                            UUID.randomUUID().toString(),
                            training,
                            participant,
                            StatementStatusEnum.EMPTY,
                            training.getCourseLevel(),
                            null
                    ))
                    .toList();

            statementCommandService.saveAll(statements);
        }
    }

    private boolean isLifeLevel(CourseLevel level) {
        return switch (level) {
            case LIFE, LIFE_2, LIFE_3, LIFE_GRADUATE -> true;
            default -> false;
        };
    }

    private FylStage mapStage(CourseLevel level) {
        return switch (level) {
            case INIT -> null;
            case FOCUS -> FylStage.FOCUS;
            case YOUR -> FylStage.YOUR;
            case LIFE -> FylStage.LIFE_1;
            case LIFE_2 -> FylStage.LIFE_2;
            case LIFE_3 -> FylStage.LIFE_3;
            case LIFE_GRADUATE -> FylStage.LIFE_GRADUATE;
        };
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
