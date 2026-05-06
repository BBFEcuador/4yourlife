package com.foryourlife.admin.programs.teams.application;

import com.foryourlife.admin.crm.call.domain.Call;
import com.foryourlife.admin.crm.call.domain.CallRepository;
import com.foryourlife.admin.crm.statement.application.StatementCommandService;
import com.foryourlife.admin.crm.statement.domain.Statement;
import com.foryourlife.admin.crm.statement.domain.StatementStatusEnum;
import com.foryourlife.admin.programs.attendance.application.AttendanceCommandService;
import com.foryourlife.admin.programs.attendance.domain.Attendance;
import com.foryourlife.admin.programs.attendance.domain.FylStage;
import com.foryourlife.admin.programs.attendance.infraestructure.AttendanceRepositoryImpl;
import com.foryourlife.admin.programs.teams.domain.Team;
import com.foryourlife.admin.programs.teams.domain.TeamRepository;
import com.foryourlife.admin.programs.teams.infrastructure.httpControllers.AddUsersToTeamRequest;
import com.foryourlife.admin.programs.teams.infrastructure.httpControllers.request.PromotionLifeRequest;
import com.foryourlife.admin.programs.teams.infrastructure.httpControllers.request.PromotionYourRequest;
import com.foryourlife.admin.programs.teams.infrastructure.httpControllers.request.SaveFocusTeamsRequest;
import com.foryourlife.admin.programs.trainer.application.TrainerQueryService;
import com.foryourlife.admin.programs.training.application.QueryTrainingService;
import com.foryourlife.admin.programs.training.domain.Training;
import com.foryourlife.admin.programs.training.domain.TrainingRepository;
import com.foryourlife.clients.account.participant.domain.Participant;
import com.foryourlife.clients.account.participant.domain.ParticipantRepository;
import com.foryourlife.clients.account.participantLevel.domain.ParticipantLevel;
import com.foryourlife.clients.account.participantLevel.domain.ParticipantLevelRepository;
import com.foryourlife.clients.account.promises.domain.Promise;
import com.foryourlife.clients.account.promises.domain.PromiseRepository;
import com.foryourlife.masterLife.application.QueryMasterLifeService;
import com.foryourlife.masterLife.domain.MasterLife;
import com.foryourlife.masterLife.domain.MasterLifeRepository;
import com.foryourlife.shared.domain.bus.EventBus;
import com.foryourlife.shared.domain.events.TeamCreated;
import com.foryourlife.shared.domain.exception.BaseException;
import com.foryourlife.shared.domain.level.CourseLevel;
import com.foryourlife.shared.domain.user.User;
import com.foryourlife.staff.domain.Staff;
import com.foryourlife.staff.domain.StaffRepository;
import com.foryourlife.visionary.domain.Visionary;
import com.foryourlife.visionary.domain.VisionaryRepository;
import org.apache.commons.lang3.SerializationUtils;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

@Service
public class TeamCommandService {
    private final AttendanceRepositoryImpl attendanceRepository;
    private final CallRepository callRepository;
    private final TrainingRepository trainingRepository;
    private final TeamRepository _teamRepository;
    private final EventBus bus;
    private final PromiseRepository promiseRepository;
    private final ParticipantRepository _participantRepository;
    private final MasterLifeRepository masterLifeRepository;
    private final StaffRepository staffRepository;
    private final VisionaryRepository visionaryRepository;
    private final QueryTrainingService queryTrainingService;
    private final TrainerQueryService trainerQueryService;
    private final QueryMasterLifeService queryMasterLifeService;
    private final Logger logger = LoggerFactory.getLogger(TeamCommandService.class);
    private final ParticipantLevelRepository participantLevelRepository;
    private final TeamBadgePdfService teamBadgePdfService;
    private final AttendanceCommandService attendanceCommandService;
    private final StatementCommandService statementCommandService;

    public TeamCommandService(AttendanceRepositoryImpl attendanceRepository, CallRepository callRepository, TrainingRepository trainingRepository, TeamRepository _teamRepository, EventBus bus, PromiseRepository promiseRepository, ParticipantRepository _participantRepository, MasterLifeRepository masterLifeRepository, StaffRepository staffRepository, VisionaryRepository visionaryRepository, QueryTrainingService queryTrainingService, TrainerQueryService trainerQueryService, QueryMasterLifeService queryMasterLifeService, ParticipantLevelRepository participantLevelRepository, TeamBadgePdfService teamBadgePdfService, AttendanceCommandService attendanceCommandService, StatementCommandService statementCommandService) {
        this.attendanceRepository = attendanceRepository;
        this.callRepository = callRepository;
        this.trainingRepository = trainingRepository;
        this._teamRepository = _teamRepository;
        this.bus = bus;
        this.promiseRepository = promiseRepository;
        this._participantRepository = _participantRepository;
        this.masterLifeRepository = masterLifeRepository;
        this.staffRepository = staffRepository;
        this.visionaryRepository = visionaryRepository;
        this.queryTrainingService = queryTrainingService;
        this.trainerQueryService = trainerQueryService;
        this.queryMasterLifeService = queryMasterLifeService;
        this.participantLevelRepository = participantLevelRepository;
        this.teamBadgePdfService = teamBadgePdfService;
        this.attendanceCommandService = attendanceCommandService;
        this.statementCommandService = statementCommandService;
    }

    public void save(Team team) {
        this._teamRepository.save(team);
        this.bus.publish(team.pullDomainEvents());
    }

    @Transactional
    public void saveFocusTeam(SaveFocusTeamsRequest request) {
        var training = queryTrainingService.getTrainingById(request.training);
        var trainer = trainerQueryService.findTrainerById(request.trainer).orElseThrow();
        var users = request.users.stream().map(participant -> {
            var p = _participantRepository.findById(participant.getId()).orElseThrow();
            if (p.getTeam() != null) {
                throw new BaseException("Usuario no disponible", List.of("El usuario " + p.getUser().getName() + " ya tiene un equipo"));
            }
            return p;
        }).toList();
        var staff = request.staffs.stream().map(participant -> {
            return staffRepository.findById(participant.getId()).orElseThrow();
        }).toList();
        var visionaries = request.visionaries.stream().map(participant -> {
            return visionaryRepository.findById(participant.getId()).orElseThrow();
        }).toList();
        var team = Team.create(
                request.id != null ? request.id : UUID.randomUUID().toString(),
                request.name,
                null,
                training,
                training.getNumber(),
                users,
                trainer,
                staff,
                visionaries,
                new ArrayList<>()
        );
        this._teamRepository.save(team);
        attendanceCommandService.assignAttendancesAndDeclarations(team, team.getTraining());
        initializeTeamProperties(team);
    }

    static void assignLevelParticipant(Training training, Team team, ParticipantRepository participantRepository, ParticipantLevelRepository participantLevelRepository) {
        team.getUsers().forEach(user -> {
            var participant = participantRepository.findById(user.getId()).orElseThrow(() -> new RuntimeException(""));
            switch (training.getCourseLevel()) {
                case LIFE_2, LIFE_3 -> {
                    participant.setParticipantLevel(
                            (participantLevelRepository.findOneByCriteria(
                                    (root, query, cb) ->
                                            cb.equal(root.get("courseLevel"), cb.literal(CourseLevel.LIFE.toString()))
                            ).orElseThrow(() -> new RuntimeException(""))
                            )
                    );
                }
                default -> {
                    participant.setParticipantLevel(
                            (participantLevelRepository.findOneByCriteria(
                                    (root, query, cb) ->
                                            cb.equal(root.get("courseLevel"), training.getCourseLevel())
                            ).orElseThrow(() -> new RuntimeException(""))
                            )
                    );
                }
            }
            participantRepository.save(participant);
        });
    }

    public void update(Team team) {
        this._teamRepository.findById(team.getId())
                .orElseThrow(() -> new BaseException("Equipo no encontrado", List.of("")));
        this._teamRepository.save(team);
    }

    public void assignParticipants(String teamId, String userId) {
        var team = _teamRepository.findById(teamId);
        if (team.isEmpty()) {
            throw new BaseException("Equipo no encontrado", List.of(""));
        } else if (_participantRepository.findById(userId).isEmpty()) {
            throw new BaseException("Usuario no encontrado", List.of(""));
        }
        team.orElseThrow().getUsers().forEach(user -> {
            if (user.getId().equals(userId)) {
                throw new BaseException("El usuario ya fue asignado", List.of(""));
            }
        });
        _teamRepository.assignParticipants(teamId, userId);

    }

    public void assignMastersLife(String teamId, String userId) {
        if (this._teamRepository.findById(teamId).isEmpty()) {
            throw new BaseException("Equipo no encontrado", List.of(""));
        }
        MasterLife user = queryMasterLifeService.findById(userId);
        _teamRepository.assignMastersLife(teamId, userId);
    }

    public void removeParticipants(String teamId, String userId, Boolean lingerer, Boolean desertor) {
        if (this._teamRepository.findById(teamId).isEmpty()) {
            throw new BaseException("Equipo no encontrado", List.of(""));
        } else if (_participantRepository.findById(userId).isEmpty()) {
            throw new BaseException("Usuario no encontrado", List.of(""));
        }

        var user = _participantRepository.findById(userId).get();
        user.setIsLingerer(lingerer);
        user.setIsDesertor(desertor);
        _participantRepository.save(user);
        _teamRepository.removeParticipants(teamId, userId);
        user.getTeam().record(new TeamCreated(user.getTeam().getId(), user.getTeam()));
        bus.publish(user.getTeam().pullDomainEvents());
    }

    public void removeMastersLife(String teamId, String userId) {
        if (this._teamRepository.findById(teamId).isEmpty()) {
            throw new BaseException("Equipo no encontrado", List.of(""));
        } else if (masterLifeRepository.findById(userId).isEmpty()) {
            throw new BaseException("Masterlife no encontrado no encontrado", List.of(""));
        }
        _teamRepository.removeMastersLife(teamId, userId);
    }

    public void removeStaffs(String teamId, String userId) {
        if (this._teamRepository.findById(teamId).isEmpty()) {
            throw new BaseException("Team not found", List.of(""));
        } else if (staffRepository.findById(userId).isEmpty()) {
            throw new BaseException("Staff not found", List.of(""));
        }
        _teamRepository.removeStaffs(teamId, userId);
    }

    @Transactional
    public void promotionLifeTeam(PromotionLifeRequest request) {
        var team = _teamRepository.findById(request.id).orElseThrow();
        var trainer = trainerQueryService.findTrainerById(request.trainer).orElseThrow();
        var training = team.getTraining().getNextLevel();
        initializeTeamProperties(team);

        var participants = request.users.stream()
                .map(u -> _participantRepository.findById(u.getId()).orElseThrow())
                .toList();

        var invalidParticipants = participants.stream()
                .filter(p -> !p.getModules().getHasLife())
                .map(p -> "El participante " + p.getUser().getName() + " no tiene el módulo LIFE")
                .toList();

        if (!invalidParticipants.isEmpty()) {
            throw new BaseException("Participantes inválidos", invalidParticipants);
        }

        var filteredUsers = participants.stream()
                .filter(p -> p.getModules().getHasLife())
                .toList();

        if (filteredUsers.isEmpty()) {
            throw new BaseException("No hay usuarios suficientes", List.of());
        }

        team.getUsers().stream()
                .filter(p -> !participants.contains(p))
                .forEach(p -> {
                    p.setIsLingerer(true);
                    p.setIsDesertor(true);
                    _participantRepository.save(p);
                });

        var masterLife = request.masterLife.stream().map(participant -> {
            var p = queryMasterLifeService.findById(participant.getId());
            if (!queryMasterLifeService.isMasterLifeAvailable(
                    p.getId(),
                    training.getStartDate(),
                    training.getEndDate(),
                    training.getId())) {
                throw new BaseException("Master life not available",
                        List.of("The user " + p.getUser().getName() + " has team"));
            }
            return p;
        }).toList();

        team.setName(training.getCourseLevel().name() + "-" + training.getNumber());
        team.setTraining(training);
        team.setTrainingNumber(training.getNumber());
        team.setTrainer(trainer);

        team.getUsers().clear();
        team.getUsers().addAll(filteredUsers);

        team.getMasterLife().clear();
        team.getMasterLife().addAll(masterLife);

        team.getStaffs().clear();
        _teamRepository.save(team);
        assignParticipantLevelToUsers(team.getUsers(), training);
        attendanceCommandService.assignAttendancesAndDeclarations(team, team.getTraining());
    }

    @Transactional
    public void promotionYourTeam(PromotionYourRequest request) {
        var team = _teamRepository.findById(request.id)
                .orElseThrow(() -> new BaseException("Team not found", List.of()));

        var trainer = trainerQueryService.findTrainerById(request.trainer)
                .orElseThrow(() -> new BaseException("Trainer not found", List.of()));
        initializeTeamProperties(team);

        var training = team.getTraining().getNextLevel();

        var participants = request.users.stream()
                .map(u -> _participantRepository.findById(u.getId()).orElseThrow())
                .toList();

        var invalidParticipants = participants.stream()
                .filter(p -> !p.getModules().getHasYour())
                .map(p -> "Participante " + p.getUser().getName() + " no tiene el módulo YOUR")
                .toList();

        if (!invalidParticipants.isEmpty()) {
            throw new BaseException("Participantes inválidos", invalidParticipants);
        }

        var filteredUsers = participants.stream()
                .filter(p -> p.getModules().getHasYour())
                .toList();

        if (filteredUsers.isEmpty()) {
            throw new BaseException("No hay usuarios suficientes", List.of());
        }
        var newParticipant = new ArrayList<Participant>();
        team.getUsers().stream()
                .filter(p -> !participants.contains(p))
                .forEach(p -> {
                    p.setIsLingerer(true);
                    p.setIsDesertor(true);
                    newParticipant.add(p);
                });
        _participantRepository.saveAllAndFlush(newParticipant);
        var staff = request.staffs.stream().map(participant -> {
            var p = staffRepository.findById(participant.getId()).orElseThrow();
            if (!staffRepository.isStaffAvailable(
                    p.getId(),
                    training.getStartDate(),
                    training.getEndDate(),
                    training.getId()
            )) {
                throw new BaseException("Staff not available",
                        List.of("The user " + p.getUser().getName() + " has team"));
            }
            return p;
        }).toList();

        team.setName(training.getCourseLevel().name() + "-" + training.getNumber());
        team.setTraining(training);
        team.setTrainingNumber(training.getNumber());
        team.setTrainer(trainer);

        team.getUsers().clear();
        team.getUsers().addAll(filteredUsers);

        team.getStaffs().clear();
        team.getStaffs().addAll(staff);

        team.getVisionaries().clear();
        team.getMasterLife().clear();

        _teamRepository.save(team);
        assignParticipantLevelToUsers(team.getUsers(), team.getTraining());
        attendanceCommandService.assignAttendancesAndDeclarations(team, team.getTraining());
    }

    private void initializeTeamProperties(Team team) {
        Hibernate.initialize(team.getTraining().getNextLevel());
        Hibernate.initialize(team.getStaffs());
        Hibernate.initialize(team.getMasterLife());
        Hibernate.initialize(team.getVisionaries());
        Hibernate.initialize(team.getUsers());

        var newTeam = SerializationUtils.clone(team);
        this.addOriginalTeam(newTeam);
    }

    public void removeVisionaries(String teamId, String id) {
        if (this._teamRepository.findById(teamId).isEmpty()) {
            throw new BaseException("Team not found", List.of(""));
        } else if (visionaryRepository.findById(id).isEmpty()) {
            throw new BaseException("Staff not found", List.of(""));
        }
        _teamRepository.removeVisionaries(teamId, id);
    }

    @Transactional
    public void addParticipants(String teamId, AddUsersToTeamRequest request) {

        var team = _teamRepository.findById(teamId)
                .orElseThrow(() -> new BaseException("Team not found", List.of("")));

        addParticipantsToTeam(team, request.getUserIds());
        addStaffToTeam(team, request.getStaffIds());
        addVisionaryToTeam(team, request.getVisitorIds());
        addMasterLifeToTeam(team, request.getMasterLifeIds());

        _teamRepository.save(team);

        team.record(new TeamCreated(team.getId(), team));
        bus.publish(team.pullDomainEvents());
    }

    private void addParticipantsToTeam(Team team, List<String> userIds) {
        if (userIds == null) return;

        var participants = userIds.stream().map(userId -> {

            var participant = _participantRepository.findById(userId)
                    .orElseThrow(() -> new BaseException("Participant not found", List.of("")));

            validateNotInTeam(
                    team.getUsers(),
                    userId,
                    Participant::getId,
                    "usuario",
                    participant.getUser().getName()
            );

            handleParticipantLogic(team, participant);

            return participant;

        }).toList();

        team.getUsers().addAll(participants);
    }

    private void addStaffToTeam(Team team, List<String> staffsIds) {
        if (staffsIds == null) return;

        var staffs = staffsIds.stream().map(staffId -> {
            var staff = staffRepository.findById(staffId)
                    .orElseThrow(() -> new BaseException("Staff not found", List.of("")));

            validateNotInTeam(
                    team.getStaffs(),
                    staffId,
                    Staff::getId,
                    "staff",
                    staff.getUser().getName()
            );

            return staff;
        }).toList();

        team.getStaffs().addAll(staffs);
    }

    private void addVisionaryToTeam(Team team, List<String> visionaryIds) {
        if (visionaryIds == null) return;
        var visionaries = visionaryIds.stream().map(visionaryId -> {
            var visionary = visionaryRepository.findById(visionaryId)
                    .orElseThrow(() -> new BaseException("Visionary not found", List.of("")));

            validateNotInTeam(
                    team.getVisionaries(),
                    visionaryId,
                    Visionary::getId,
                    "visionario",
                    visionary.getUser().getName()
            );

            return visionary;
        }).toList();

        team.getVisionaries().addAll(visionaries);
    }

    private void addMasterLifeToTeam(Team team, List<String> ids) {
        if (ids == null) return;

        var list = ids.stream().map(id -> {

            var master = queryMasterLifeService.findById(id);

            validateNotInTeam(
                    team.getMasterLife(),
                    id,
                    MasterLife::getId,
                    "master life",
                    master.getUser().getName()
            );

            handleMasterLifeAttendance(team, master);

            createPromise(team.getTraining(), master.getUser());

            return master;

        }).toList();

        team.getMasterLife().addAll(list);
    }

    private void handleParticipantLogic(Team team, Participant participant) {

        var training = team.getTraining();
        var user = participant.getUser();

        if (training.getCourseLevel().equals(CourseLevel.LIFE)) {
            createPromise(training, user);
        }

        attendanceRepository.save(
                Attendance.create(
                        UUID.randomUUID().toString(),
                        null, null, null,
                        FylStage.LIFE_GRADUATE,
                        user,
                        training
                )
        );

        callRepository.save(new Call(
                UUID.randomUUID().toString(),
                user,
                training
        ));

        if (training.getCourseLevel().equals(CourseLevel.FOCUS) ||
                training.getCourseLevel().equals(CourseLevel.YOUR)) {

            var statement = new Statement(
                    UUID.randomUUID().toString(),
                    training,
                    participant,
                    StatementStatusEnum.EMPTY,
                    training.getCourseLevel(),
                    null
            );

            statementCommandService.saveAll(List.of(statement));
        }
    }

    private <T> void validateNotInTeam(
            Collection<T> collection,
            String id,
            Function<T, String> getId,
            String type,
            String name
    ) {
        boolean exists = collection.stream()
                .anyMatch(e -> getId.apply(e).equals(id));

        if (exists) {
            throw new BaseException(
                    "El " + type + " " + name + " ya se encuentra en el equipo asignado",
                    List.of("")
            );
        }
    }

    private void createPromise(Training training, User user) {
        var promise = new Promise(
                UUID.randomUUID().toString(),
                training,
                user
        );

        promise.setStartDate(training.getEndDate().plusDays(1));
        promise.setEndDate(training.getEndDate().plusDays(5));

        promiseRepository.save(promise);
    }

    private void handleMasterLifeAttendance(Team team, MasterLife master) {

        var training = team.getTraining();
        FylStage stage;

        switch (training.getCourseLevel()) {
            case LIFE -> stage = FylStage.LIFE_1;
            case LIFE_2 -> stage = FylStage.LIFE_2;
            case LIFE_3 -> stage = FylStage.LIFE_3;
            default -> {
                return;
            }
        }

        attendanceRepository.save(
                Attendance.create(
                        UUID.randomUUID().toString(),
                        null, null, null,
                        stage,
                        master.getUser(),
                        training
                )
        );
    }

    private void assignParticipantLevelToUsers(
            List<Participant> participants,
            Training training
    ) {
        CourseLevel levelToAssign =
                switch (training.getCourseLevel()) {
                    case LIFE_2, LIFE_3 -> CourseLevel.LIFE;
                    default -> training.getCourseLevel();
                };

        ParticipantLevel participantLevel =
                participantLevelRepository
                        .findByCourseLevelId(levelToAssign)
                        .orElseThrow(() ->
                                new RuntimeException("ParticipantLevel not found for " + levelToAssign));

        participants.forEach(p -> p.setParticipantLevel(participantLevel));

        _participantRepository.saveAll(participants);
    }

    private void addOriginalTeam(Team team) {
        team.setName(team.getName());
        team.getTraining().setOriginalTeam(team);
        trainingRepository.save(team.getTraining());
    }

    @Transactional
    public byte[] gafetes(String id) throws IOException {
        var team = _teamRepository.findById(id)
                .orElseThrow(() -> new BaseException("Error", List.of("Equipo no encontrado")));

        Hibernate.initialize(team.getUsers());
        Hibernate.initialize(team.getVisionaries());
        Hibernate.initialize(team.getStaffs());
        Hibernate.initialize(team.getMasterLife());

        return teamBadgePdfService.generatePdf(team);
    }

    public void updateTrainer(String id, String trainerId) {
        var trainer = trainerQueryService.findTrainerById(trainerId).orElseThrow(() ->
                new BaseException("Error", List.of("Entrenador no encontrado"))
        );
        var team = _teamRepository.findById(id).orElseThrow(() ->
                new BaseException("Error", List.of("Equipo no encontrado"))
        );
        team.setTrainer(trainer);
        _teamRepository.save(team);
    }

    public byte[] getParticipantList(String id) throws IOException {
        return teamBadgePdfService.getParticipantList(id).toByteArray();
    }
}
