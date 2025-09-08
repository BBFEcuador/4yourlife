package com.foryourlife.admin.programs.teams.application;

import com.foryourlife.admin.programs.teams.domain.Team;
import com.foryourlife.admin.programs.teams.domain.TeamRepository;
import com.foryourlife.admin.programs.teams.infrastructure.httpControllers.request.*;
import com.foryourlife.admin.programs.trainer.application.TrainerFinderService;
import com.foryourlife.admin.programs.training.application.QueryTrainingService;
import com.foryourlife.clients.account.participant.domain.ParticipantRepository;
import com.foryourlife.masterLife.application.QueryMasterLifeService;
import com.foryourlife.masterLife.domain.MasterLife;
import com.foryourlife.shared.domain.bus.EventBus;
import com.foryourlife.shared.domain.exception.BaseException;
import com.foryourlife.staff.domain.StaffRepository;
import com.foryourlife.visionary.domain.VisionaryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class CommandTeamService {

    private String baseUrl;
    private final TeamRepository _teamRepository;
    private final EventBus bus;
    private final ParticipantRepository _participantRepository;
    private final StaffRepository staffRepository;
    private final VisionaryRepository visionaryRepository;
    private final QueryTrainingService queryTrainingService;
    private final TrainerFinderService trainerFinderService;
    private final QueryMasterLifeService queryMasterLifeService;
    private final QueryTeamService queryTeamService;
    private final Logger logger = LoggerFactory.getLogger(CommandTeamService.class);


    public CommandTeamService(TeamRepository _teamRepository, EventBus bus, ParticipantRepository _participantRepository, StaffRepository staffRepository, VisionaryRepository visionaryRepository, QueryTrainingService queryTrainingService, TrainerFinderService trainerFinderService, QueryMasterLifeService queryMasterLifeService, QueryTeamService queryTeamService) {
        this._teamRepository = _teamRepository;
        this.bus = bus;
        this._participantRepository = _participantRepository;
        this.staffRepository = staffRepository;
        this.visionaryRepository = visionaryRepository;
        this.queryTrainingService = queryTrainingService;
        this.trainerFinderService = trainerFinderService;
        this.queryMasterLifeService = queryMasterLifeService;
        this.queryTeamService = queryTeamService;
    }

    public void save(Team team) {
        this._teamRepository.save(team);
        this.bus.publish(team.pullDomainEvents());
    }

    public void saveLifeTeam(SaveLifeTeamRequest request) {
        var training = queryTrainingService.getTrainingById(request.training);
        var trainer = trainerFinderService.findTrainerById(request.trainer).orElseThrow();
        var users = request.users.stream().map(participant -> {
            var p = _participantRepository.findById(participant.getId()).orElseThrow();
            if (p.getTeam() != null) {
                throw new BaseException("User not available", List.of("The user " + p.getName() + " has team"));
            }
            if (p.getModules().getHasLife()) return p;
            return null;
        }).toList();
        var masterLife = request.masterLife.stream().map(participant -> {
            var p = queryMasterLifeService.findById(participant.getId());
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
                new ArrayList<>(),
                new ArrayList<>(),
                masterLife
        );
        this._teamRepository.save(team);
        this.bus.publish(team.pullDomainEvents());
    }

    public void saveFocusTeam(SaveFocusTeamsRequest request) {
        var training = queryTrainingService.getTrainingById(request.training);
        var trainer = trainerFinderService.findTrainerById(request.trainer).orElseThrow();
        var users = request.users.stream().map(participant -> {
            var p = _participantRepository.findById(participant.getId()).orElseThrow();
            if (p.getTeam() != null) {
                throw new BaseException("User not available", List.of("The user " + p.getName() + " has team"));
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
    }

    public void saveYourTeam(SaveYourTeamRequest request) {
        var training = queryTrainingService.getTrainingById(request.training);
        var trainer = trainerFinderService.findTrainerById(request.trainer).orElseThrow();
        var users = request.users.stream().map(participant -> {
            var p = _participantRepository.findById(participant.getId()).orElseThrow();
            if (p.getTeam() != null) {
                throw new BaseException("User not available", List.of("The user " + p.getName() + " has team"));
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
        this.bus.publish(team.pullDomainEvents());
    }
    @Transactional
    public void promotionYourTeam(PromotionYourRequest request) {
        var team = _teamRepository.findById(request.id).orElseThrow(() -> new BaseException("Team not found", List.of()));
        var trainer = trainerFinderService.findTrainerById(request.trainer).orElseThrow(() -> new BaseException("Trainer not found", List.of()));
        var training = team.getTraining().getNextLevel();
        var users = request.users.stream().map(participant -> {
            var p = _participantRepository.findById(participant.getId()).orElseThrow();
            if (p.getTeam() != null && !Objects.equals(p.getTeam().getId(), team.getId())) {
                throw new BaseException("Usuario no disponible", List.of("El usuario " + p.getName() + " tiene un equipo"));
            }
            if (p.getModules().getHasYour()) return p;
            p.setLingerer(true);
            p.setDesertor(true);
            _participantRepository.save(p);
            return null;
        }).toList();
        if (users.stream().filter(Objects::nonNull).toList().isEmpty()) {
            throw new BaseException("No hay usuarios suficientes", List.of());
        }
        var staff = request.staffs.stream().map(participant -> {
            var p = staffRepository.findById(participant.getId()).orElseThrow();
            if (!staffRepository.isStaffAvailable(p.getId(), training.getStartDate(), training.getEndDate(), training.getId())) {
                throw new BaseException("Staff not available", List.of("The user " + p.getUser().getName() + " has team"));
            }
            return p;
        }).toList();
        var n = Team.create(
                team.getId(),
                training.getCourseLevel().name()+"-" + training.getNumber(),
                team.getPhoto(),
                training,
                training.getNumber(),
                users,
                trainer,
                staff,
                new ArrayList<>(),
                new ArrayList<>()
        );
        _teamRepository.save(n);
        bus.publish(n.pullDomainEvents());
    }

    public void update(Team team) {
        this._teamRepository.findById(team.getId())
                .orElseThrow(() -> new BaseException("Team not found", List.of("")));
        this._teamRepository.save(team);
    }

    public void assignParticipants(String teamId, String userId) {
        var team = _teamRepository.findById(teamId);
        if (team.isEmpty()) {
            throw new BaseException("Team not found", List.of(""));
        } else if (_participantRepository.findById(userId).isEmpty()) {
            throw new BaseException("User not found", List.of(""));
        }
        team.orElseThrow().getUsers().forEach(user -> {
            if (user.getId().equals(userId)) {
                throw new BaseException("User already assigned", List.of(""));
            }
        });
        _teamRepository.assignParticipants(teamId, userId);

    }

    public void assignMastersLife(String teamId, String userId) {
        if (this._teamRepository.findById(teamId).isEmpty()) {
            throw new BaseException("Team not found", List.of(""));
        }
        MasterLife user = queryMasterLifeService.findById(userId);
        _teamRepository.assignMastersLife(teamId, userId);
    }

    public void removeParticipants(String teamId, String userId, Boolean lingerer, Boolean desertor) {
        if (this._teamRepository.findById(teamId).isEmpty()) {
            throw new BaseException("Team not found", List.of(""));
        } else if (_participantRepository.findById(userId).isEmpty()) {
            throw new BaseException("User not found", List.of(""));
        }

        var user = _participantRepository.findById(userId).get();
        user.setLingerer(lingerer);
        user.setDesertor(desertor);
        _participantRepository.save(user);
        _teamRepository.removeParticipants(teamId, userId);
    }

    public void removeMastersLife(String teamId, String userId) {
        if (this._teamRepository.findById(teamId).isEmpty()) {
            throw new BaseException("Team not found", List.of(""));
        } else if (_participantRepository.findById(userId).isEmpty()) {
            throw new BaseException("User not found", List.of(""));
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
        var trainer = trainerFinderService.findTrainerById(request.trainer).orElseThrow();
        var training = team.getTraining().getNextLevel();
        var users = request.users.stream().map(participant -> {
            var p = _participantRepository.findById(participant.getId()).orElseThrow();
            if (p.getTeam() != null && !Objects.equals(p.getTeam().getId(), team.getId())) {
                throw new BaseException("User not available", List.of("The user " + p.getName() + " has team"));
            }
            if (p.getModules().getHasLife()) return p;
            p.setLingerer(true);
            p.setDesertor(true);
            _participantRepository.save(p);
            return null;
        }).toList();
        if (users.stream().filter(Objects::nonNull).toList().isEmpty()) {
            throw new BaseException("No hay usuarios suficientes", List.of());
        }
        var masterLife = request.masterLife.stream().map(participant -> {
            var p = queryMasterLifeService.findById(participant.getId());
            if (!queryMasterLifeService.isMasterLifeAvailable(p.getId(), training.getStartDate(), training.getEndDate(), training.getId())) {
                throw new BaseException("Master life not available", List.of("The user " + p.getUser().getName() + " has team"));
            }
            return p;
        }).toList();
        var n = Team.create(
                team.getId(),
                request.name != null ? request.name : training.getCourseLevel().name() + "-" + training.getNumber(),
                team.getPhoto(),
                training,
                training.getNumber(),
                users,
                trainer,
                new ArrayList<>(),
                new ArrayList<>(),
                masterLife
        );
        _teamRepository.save(n);
        bus.publish(n.pullDomainEvents());
    }

    public void removeVisionaries(String teamId, String id) {
        if (this._teamRepository.findById(teamId).isEmpty()) {
            throw new BaseException("Team not found", List.of(""));
        } else if (visionaryRepository.findById(id).isEmpty()) {
            throw new BaseException("Staff not found", List.of(""));
        }
        _teamRepository.removeVisionaries(teamId, id);
    }
}
