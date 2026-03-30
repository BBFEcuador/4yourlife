package com.foryourlife.admin.programs.teams.application;

import com.foryourlife.admin.crm.call.domain.Call;
import com.foryourlife.admin.crm.call.domain.CallRepository;
import com.foryourlife.admin.programs.attendance.application.CommandAttendanceService;
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
import com.foryourlife.staff.domain.StaffRepository;
import com.foryourlife.visionary.domain.VisionaryRepository;
import org.apache.commons.lang3.SerializationUtils;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class CommandTeamService {
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
    private final Logger logger = LoggerFactory.getLogger(CommandTeamService.class);
    private final ParticipantLevelRepository participantLevelRepository;
    private final TeamBadgePdfService teamBadgePdfService;
    private final CommandAttendanceService commandAttendanceService;

    public CommandTeamService(AttendanceRepositoryImpl attendanceRepository, CallRepository callRepository, TrainingRepository trainingRepository, TeamRepository _teamRepository, EventBus bus, PromiseRepository promiseRepository, ParticipantRepository _participantRepository, MasterLifeRepository masterLifeRepository, StaffRepository staffRepository, VisionaryRepository visionaryRepository, QueryTrainingService queryTrainingService, TrainerQueryService trainerQueryService, QueryMasterLifeService queryMasterLifeService, ParticipantLevelRepository participantLevelRepository, TeamBadgePdfService teamBadgePdfService, CommandAttendanceService commandAttendanceService) {
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
        this.commandAttendanceService = commandAttendanceService;
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
        commandAttendanceService.assignAttendancesAndDeclarations(team, team.getTraining());
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
        commandAttendanceService.assignAttendancesAndDeclarations(team, team.getTraining());
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
        commandAttendanceService.assignAttendancesAndDeclarations(team, team.getTraining());
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
        var team = _teamRepository.findById(teamId).orElseThrow(
                () -> new BaseException("Team not found", List.of(""))
        );

        if (request.getUserIds() != null) {
            var listParticipants = request.getUserIds().stream().map(userId -> {
                var participant = _participantRepository.findById(userId).orElseThrow(
                        () -> new BaseException("Participant not found", List.of(""))
                );
                team.getUsers().forEach(user -> {
                    if (user.getId().equals(userId)) {
                        throw new BaseException("El usuario" + user.getUser().getName() + " ya se encuentra en el equipo asignado", List.of(""));
                    }
                });
                if (team.getTraining().getCourseLevel().equals(CourseLevel.LIFE)) {
                    Promise promise = new Promise(
                            UUID.randomUUID().toString(),
                            team.getTraining(),
                            participant.getUser()
                    );
                    promise.setStartDate(team.getTraining().getEndDate().plusDays(1));
                    promise.setEndDate(team.getTraining().getEndDate().plusDays(5));
                    promiseRepository.save(promise);
                }
                attendanceRepository.save(
                        Attendance.create(
                                UUID.randomUUID().toString(),
                                null,
                                null,
                                null,
                                FylStage.LIFE_GRADUATE,
                                participant.getUser(),
                                team.getTraining()
                        )
                );
                callRepository.save(
                        new Call(
                                UUID.randomUUID().toString(),
                                participant.getUser(),
                                team.getTraining()
                        )
                );
                return participant;
            }).toList();
            team.getUsers().addAll(listParticipants);
        }

        if (request.getStaffIds() != null) {
            var listStaffs = request.getStaffIds().stream().map(staffId -> {
                var staff = staffRepository.findById(staffId).orElseThrow(
                        () -> new BaseException("Staff not found", List.of(""))
                );
                team.getStaffs().forEach(s -> {
                    if (s.getId().equals(staffId)) {
                        throw new BaseException("El staff " + staff.getUser().getName() + " ya se encuentra en el equipo asignado", List.of(""));
                    }
                });
                return staff;
            }).toList();
            team.getStaffs().addAll(listStaffs);
        }

        if (request.getVisitorIds() != null) {
            var listVisionaries = request.getVisitorIds().stream().map(visionaryId -> {
                var visionary = visionaryRepository.findById(visionaryId).orElseThrow(
                        () -> new BaseException("Visionary not found", List.of(""))
                );
                team.getVisionaries().forEach(v -> {
                    if (v.getId().equals(visionaryId)) {
                        throw new BaseException("El visionario " + visionary.getUser().getName() + " ya se encuentra en el equipo asignado", List.of(""));
                    }
                });
                return visionary;
            }).toList();
            team.getVisionaries().addAll(listVisionaries);
        }

        if (request.getMasterLifeIds() != null) {
            var listMasterLife = request.getMasterLifeIds().stream().map(masterLifeId -> {
                var masterLife = queryMasterLifeService.findById(masterLifeId);
                team.getMasterLife().forEach(m -> {
                    if (m.getId().equals(masterLifeId)) {
                        throw new BaseException("El master life " + masterLife.getUser().getName() + " ya se encuentra en el equipo asignado", List.of(""));
                    }
                });
                if (team.getTraining().getCourseLevel().equals(CourseLevel.LIFE)) {
                    attendanceRepository.save(
                            Attendance.create(
                                    UUID.randomUUID().toString(),
                                    null,
                                    null,
                                    null,
                                    FylStage.LIFE_1,
                                    masterLife.getUser(),
                                    team.getTraining()
                            )
                    );
                }
                if (team.getTraining().getCourseLevel().equals(CourseLevel.LIFE_2)) {
                    attendanceRepository.save(
                            Attendance.create(
                                    UUID.randomUUID().toString(),
                                    null,
                                    null,
                                    null,
                                    FylStage.LIFE_2,
                                    masterLife.getUser(),
                                    team.getTraining()
                            )
                    );
                }
                if (team.getTraining().getCourseLevel().equals(CourseLevel.LIFE_3)) {
                    attendanceRepository.save(
                            Attendance.create(
                                    UUID.randomUUID().toString(),
                                    null,
                                    null,
                                    null,
                                    FylStage.LIFE_3,
                                    masterLife.getUser(),
                                    team.getTraining()
                            )
                    );
                }
                Promise promise = new Promise(
                        UUID.randomUUID().toString(),
                        team.getTraining(),
                        masterLife.getUser()
                );
                promise.setStartDate(team.getTraining().getEndDate().plusDays(1));
                promise.setEndDate(team.getTraining().getEndDate().plusDays(5));
                promiseRepository.save(promise);
                return masterLife;
            }).toList();

            team.getMasterLife().addAll(listMasterLife);
        }
        _teamRepository.save(team);
        team.record(new TeamCreated(team.getId(), team));
        bus.publish(team.pullDomainEvents());
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
