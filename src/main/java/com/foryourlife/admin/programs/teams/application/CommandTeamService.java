package com.foryourlife.admin.programs.teams.application;

import com.foryourlife.admin.programs.teams.domain.Team;
import com.foryourlife.admin.programs.teams.domain.TeamRepository;
import com.foryourlife.admin.programs.training.application.QueryTrainingService;
import com.foryourlife.clients.account.user.domain.UserRepository;
import com.foryourlife.clients.account.user.domain.Users;
import com.foryourlife.shared.domain.bus.EventBus;
import com.foryourlife.shared.domain.exception.BaseException;
import com.foryourlife.shared.domain.level.CourseLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
public class CommandTeamService {

    @Value("${api.url}")
    private String baseUrl;
    private final TeamRepository _teamRepository;
    private final EventBus bus;
    private final UserRepository _userRepository;
    private final QueryTrainingService queryTrainingService;
    private final QueryTeamService queryTeamService;
    private final Logger logger = LoggerFactory.getLogger(CommandTeamService.class);

    public CommandTeamService(TeamRepository _teamRepository, EventBus bus, UserRepository _userRepository, QueryTrainingService queryTrainingService, QueryTeamService queryTeamService) {
        this._teamRepository = _teamRepository;
        this.bus = bus;
        this._userRepository = _userRepository;
        this.queryTrainingService = queryTrainingService;
        this.queryTeamService = queryTeamService;
    }

    public void save(Team team) {
        this._teamRepository.save(team);
        this.bus.publish(team.pullDomainEvents());
    }

    public void update(Team team) {
        this._teamRepository.findById(team.getId())
                .orElseThrow(() -> new BaseException("Team not found", List.of("")));
        this._teamRepository.save(team);
    }

    public void assignParticipants(String teamId, String userId) {
        var team = _teamRepository.findById(teamId);
        if (!team.isPresent()) {
            throw new BaseException("Team not found", List.of(""));
        } else if (!_userRepository.findById(userId).isPresent()) {
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
        } else if (_userRepository.findById(userId).isEmpty()) {
            throw new BaseException("User not found", List.of(""));
        }
        Users user = _userRepository.findById(userId).get();
        if (user.getParticipantLevel().getCourseLevel() == CourseLevel.MASTER_LIFE) {
            _teamRepository.assignMastersLife(teamId, userId);
        } else {
            throw new BaseException("User is not MasterLife", List.of(""));
        }
    }

    public void removeParticipants(String teamId, String userId) {
        if (this._teamRepository.findById(teamId).isEmpty()) {
            throw new BaseException("Team not found", List.of(""));
        } else if (_userRepository.findById(userId).isEmpty()) {
            throw new BaseException("User not found", List.of(""));
        }
        _teamRepository.removeParticipants(teamId, userId);
    }

    public void removeMastersLife(String teamId, String userId) {
        if (this._teamRepository.findById(teamId).isEmpty()) {
            throw new BaseException("Team not found", List.of(""));
        } else if (_userRepository.findById(userId).isEmpty()) {
            throw new BaseException("User not found", List.of(""));
        }
        _teamRepository.removeMastersLife(teamId, userId);
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

        String relativeFilePath = "/teamPhotos/" + newFileName;

        String filePath = uploadDir + newFileName;

        File directory = new File(uploadDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        File dest = new File(filePath);
        photo.transferTo(dest);

        return relativeFilePath;
    }
}
