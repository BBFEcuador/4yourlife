package com.foryourlife.admin.programs.teams.application;

import com.foryourlife.admin.crm.call.domain.Call;
import com.foryourlife.admin.crm.call.domain.CallRepository;
import com.foryourlife.admin.programs.attendance.domain.Attendance;
import com.foryourlife.admin.programs.attendance.domain.FylStage;
import com.foryourlife.admin.programs.attendance.infraestructure.AttendanceRepositoryImpl;
import com.foryourlife.admin.programs.teams.domain.Team;
import com.foryourlife.admin.programs.teams.domain.TeamRepository;
import com.foryourlife.admin.programs.teams.infrastructure.httpControllers.AddUsersToTeamRequest;
import com.foryourlife.admin.programs.teams.infrastructure.httpControllers.request.*;
import com.foryourlife.admin.programs.trainer.application.TrainerQueryService;
import com.foryourlife.admin.programs.training.application.QueryTrainingService;
import com.foryourlife.admin.programs.training.domain.Training;
import com.foryourlife.admin.programs.training.domain.TrainingRepository;
import com.foryourlife.clients.account.participant.domain.Participant;
import com.foryourlife.clients.account.participant.domain.ParticipantRepository;
import com.foryourlife.clients.account.participantLevel.domain.ParticipantLevel;
import com.foryourlife.clients.account.participantLevel.domain.ParticipantLevelRepository;
import com.foryourlife.clients.account.promises.application.PromiseCommandService;
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
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CommandTeamService {

    private final AttendanceRepositoryImpl attendanceRepository;
    private final CallRepository callRepository;
    private final TrainingRepository trainingRepository;
    private String baseUrl;
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
    private final QueryTeamService queryTeamService;
    private final Logger logger = LoggerFactory.getLogger(CommandTeamService.class);
    private final ParticipantLevelRepository participantLevelRepository;
    private final PromiseCommandService promiseCommandService;
    private final TeamBadgePdfService teamBadgePdfService;


    public CommandTeamService(TeamRepository _teamRepository, EventBus bus, ParticipantRepository _participantRepository, StaffRepository staffRepository, VisionaryRepository visionaryRepository, QueryTrainingService queryTrainingService, TrainerQueryService trainerQueryService, QueryMasterLifeService queryMasterLifeService, QueryTeamService queryTeamService, AttendanceRepositoryImpl attendanceRepository, CallRepository callRepository, PromiseRepository promiseRepository, ParticipantLevelRepository participantLevelRepository, PromiseCommandService promiseCommandService, TrainingRepository trainingRepository, MasterLifeRepository masterLifeRepository, TeamBadgePdfService teamBadgePdfService) {
        this._teamRepository = _teamRepository;
        this.bus = bus;
        this._participantRepository = _participantRepository;
        this.staffRepository = staffRepository;
        this.visionaryRepository = visionaryRepository;
        this.queryTrainingService = queryTrainingService;
        this.trainerQueryService = trainerQueryService;
        this.queryMasterLifeService = queryMasterLifeService;
        this.queryTeamService = queryTeamService;
        this.attendanceRepository = attendanceRepository;
        this.callRepository = callRepository;
        this.promiseRepository = promiseRepository;
        this.participantLevelRepository = participantLevelRepository;
        this.promiseCommandService = promiseCommandService;
        this.trainingRepository = trainingRepository;
        this.masterLifeRepository = masterLifeRepository;
        this.teamBadgePdfService = teamBadgePdfService;
    }

    public void save(Team team) {
        this._teamRepository.save(team);
        this.bus.publish(team.pullDomainEvents());
    }

    public void saveLifeTeam(SaveLifeTeamRequest request) {
        var training = queryTrainingService.getTrainingById(request.training);
        var trainer = trainerQueryService.findTrainerById(request.trainer).orElseThrow();
        var users = request.users.stream().map(participant -> {
            var p = _participantRepository.findById(participant.getId()).orElseThrow();
            if (p.getTeam() != null) {
                throw new BaseException("Usuario no disponible", List.of("El usuario " + p.getName() + " ya tiene un equipo"));
            }
            if (p.getModules().getHasLife()) return p;
            return null;
        }).toList();
        var masterLife = request.masterLife.stream().map(participant -> {
            return queryMasterLifeService.findById(participant.getId());
        }).toList();
        var team = Team.create(
                request.id != null ? request.id : UUID.randomUUID().toString(),
                request.name,
                null,
                training,
                training.getNumber(),
                users,
                trainer,
                new ArrayList<>(),
                new ArrayList<>(),
                masterLife
        );
        this._teamRepository.save(team);
        this.bus.publish(team.pullDomainEvents());
    }

    public void saveFocusTeam(SaveFocusTeamsRequest request) {
        var training = queryTrainingService.getTrainingById(request.training);
        var trainer = trainerQueryService.findTrainerById(request.trainer).orElseThrow();
        var users = request.users.stream().map(participant -> {
            var p = _participantRepository.findById(participant.getId()).orElseThrow();
            if (p.getTeam() != null) {
                throw new BaseException("Usuario no disponible", List.of("El usuario " + p.getName() + " ya tiene un equipo"));
            }
            return p;
        }).toList();
        var staff = request.staffs.stream().map(participant -> {
            var p = staffRepository.findById(participant.getId()).orElseThrow();
            return p;
        }).toList();
        var visionaries = request.visionaries.stream().map(participant -> {
            var p = visionaryRepository.findById(participant.getId()).orElseThrow();
            return p;
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
        this.bus.publish(team.pullDomainEvents());
        this.addOriginalTeam(team);
    }

    public void saveYourTeam(SaveYourTeamRequest request) {
        var training = queryTrainingService.getTrainingById(request.training);
        var trainer = trainerQueryService.findTrainerById(request.trainer).orElseThrow();
        var users = request.users.stream().map(participant -> {
            var p = _participantRepository.findById(participant.getId()).orElseThrow();
            if (p.getTeam() != null) {
                throw new BaseException("Usuario no disponible", List.of("El usuario " + p.getName() + " ya tiene un equipo"));
            }
            if (p.getModules().getHasYour()) return p;
            return null;
        }).toList();
        var staff = request.staffs.stream().map(participant -> {
            var p = staffRepository.findById(participant.getId()).orElseThrow();
            return p;
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
                new ArrayList<>(),
                new ArrayList<>()
        );
        this._teamRepository.save(team);
        team.getUsers().forEach(user -> {
            var participant = _participantRepository.findById(user.getId()).orElseThrow(() -> new RuntimeException(""));
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
            _participantRepository.save(participant);
        });
//        this.bus.publish(team.pullDomainEvents());
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

    public void assignTeams(String teamId, String trainingId) {
        var team = this._teamRepository.findById(teamId).orElseThrow(() ->
                new BaseException("Team not found", List.of("The team with id " + teamId + " does not exist"))
        );
        var training = this.queryTrainingService.getTrainingById(trainingId);
        team.setTraining(training);
        this._teamRepository.save(team);
        this.bus.publish(team.pullDomainEvents());
    }

    public String updatePhoto(String id, MultipartFile photo) throws IOException {
        String imagePath = null;
        try {
            if (photo != null && !photo.isEmpty()) {
                imagePath = saveImage(id, photo);
                var team = queryTeamService.getTeamById(id);
                team.setPhoto(imagePath);
                save(team);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw e;
        }
        return baseUrl + imagePath;
    }

    public String saveImage(String id, MultipartFile photo) throws IOException {
        String projectDir = System.getProperty("user.dir");
        String uploadDir = projectDir + "/src/main/resources/assets/teamPhotos/";
        String originalFilename = photo.getOriginalFilename();

        if (originalFilename == null) {
            throw new IOException("El nombre del archivo es nulo.");
        }

        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf('.'));

        String newFileName = id + fileExtension;

        String relativeFilePath = "resources/assets/teamPhotos/" + newFileName;

        String filePath = uploadDir + newFileName;

        File directory = new File(uploadDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        File dest = new File(filePath);
        photo.transferTo(dest);

        return relativeFilePath;
    }

    @Transactional
    public void promotionLifeTeam(PromotionLifeRequest request) {
        var team = _teamRepository.findById(request.id).orElseThrow();
        var trainer = trainerQueryService.findTrainerById(request.trainer).orElseThrow();
        var training = team.getTraining().getNextLevel();
        Hibernate.initialize(team.getTraining().getNextLevel());

        this.addOriginalTeam(team);

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
        this.assignAttendancesAndDeclarations(team, team.getTraining());
    }

    @Transactional
    public void promotionYourTeam(PromotionYourRequest request) {
        var team = _teamRepository.findById(request.id)
                .orElseThrow(() -> new BaseException("Team not found", List.of()));

        var trainer = trainerQueryService.findTrainerById(request.trainer)
                .orElseThrow(() -> new BaseException("Trainer not found", List.of()));
        Hibernate.initialize(team.getTraining().getNextLevel());
        this.addOriginalTeam(team);

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

        team.getUsers().stream()
                .filter(p -> !participants.contains(p))
                .forEach(p -> {
                    p.setIsLingerer(true);
                    p.setIsDesertor(true);
                    _participantRepository.save(p);
                });

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
        this.assignAttendancesAndDeclarations(team, team.getTraining());
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

    public void assignAttendancesAndDeclarations(Team team, Training training) {
        team.getUsers().forEach(user -> {
            switch (training.getCourseLevel()) {
                case CourseLevel.FOCUS:
                    attendanceRepository.save(Attendance.create(UUID.randomUUID().toString(), null, null, null, FylStage.FOCUS, user.getUser(), training));
                    callRepository.save(new Call(
                                    UUID.randomUUID().toString(),
                                    user.getUser(),
                                    training
                            )
                    );
                    break;
                case CourseLevel.YOUR:
                    attendanceRepository.save(Attendance.create(UUID.randomUUID().toString(), null, null, null, FylStage.YOUR, user.getUser(), training));
                    callRepository.save(new Call(
                                    UUID.randomUUID().toString(),
                                    user.getUser(),
                                    training
                            )
                    );
                    break;
                case CourseLevel.LIFE:
                    attendanceRepository.save(Attendance.create(UUID.randomUUID().toString(), null, null, null, FylStage.LIFE_1, user.getUser(), training));
                    callRepository.save(new Call(
                                    UUID.randomUUID().toString(),
                                    user.getUser(),
                                    training
                            )
                    );
                    break;
                case CourseLevel.LIFE_2:
                    attendanceRepository.save(Attendance.create(UUID.randomUUID().toString(), null, null, null, FylStage.LIFE_2, user.getUser(), training));
                    callRepository.save(new Call(
                                    UUID.randomUUID().toString(),
                                    user.getUser(),
                                    training
                            )
                    );
                    break;
                case CourseLevel.LIFE_3:
                    attendanceRepository.save(Attendance.create(UUID.randomUUID().toString(), null, null, null, FylStage.LIFE_3, user.getUser(), training));
                    callRepository.save(new Call(
                                    UUID.randomUUID().toString(),
                                    user.getUser(),
                                    training
                            )
                    );
                    break;
                case CourseLevel.LIFE_GRADUATE:
                    attendanceRepository.save(Attendance.create(UUID.randomUUID().toString(), null, null, null, FylStage.LIFE_GRADUATE, user.getUser(), training));
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
                    attendanceRepository.save(Attendance.create(UUID.randomUUID().toString(), null, null, null, FylStage.LIFE_1, masterLifes.getUser(), training));
                    break;
                case CourseLevel.LIFE_2:
                    attendanceRepository.save(Attendance.create(UUID.randomUUID().toString(), null, null, null, FylStage.LIFE_2, masterLifes.getUser(), training));
                    break;
                case CourseLevel.LIFE_3:
                    attendanceRepository.save(Attendance.create(UUID.randomUUID().toString(), null, null, null, FylStage.LIFE_3, masterLifes.getUser(), training));
                    break;
                case CourseLevel.LIFE_GRADUATE:
                    attendanceRepository.save(Attendance.create(UUID.randomUUID().toString(), null, null, null, FylStage.LIFE_GRADUATE, masterLifes.getUser(), training));
                    break;
            }
        });
        promiseCommandService.createPromises(training.getId());
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
}
