package com.foryourlife.admin.programs.teams.infrastructure.httpControllers;

import com.foryourlife.admin.programs.teams.application.CommandTeamService;
import com.foryourlife.admin.programs.teams.application.QueryTeamService;
import com.foryourlife.admin.programs.teams.infrastructure.httpControllers.request.*;
import com.foryourlife.shared.domain.criteria.Criteria;
import com.foryourlife.shared.domain.criteria.Filter;
import com.foryourlife.shared.domain.exception.BaseException;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/teams")
public class TeamController {

    private final CommandTeamService commandTeamService;
    private final QueryTeamService queryTeamService;

    public TeamController(CommandTeamService commandTeamService, QueryTeamService queryTeamService) {
        this.commandTeamService = commandTeamService;
        this.queryTeamService = queryTeamService;
    }

    @GetMapping("")
    public ResponseEntity<?> getAll(@RequestParam(value = "page", defaultValue = "0") int page, @RequestParam(value = "perPage", defaultValue = "10") int perPage, @RequestParam(value = "search", defaultValue = "") String search, @RequestParam(value = "campusId", defaultValue = "") String campusId) {
        var p = PageRequest.of(page, perPage, Sort.by("id").descending());
        List<Filter> filters = new ArrayList<>();

        if (!campusId.isEmpty()) {
            filters.add(new Filter("id", campusId, "training.campus", Filter.Operation.EQUAL, Filter.LogicalOperator.AND));
        }

        if (!search.isEmpty()) {
            filters.addAll(List.of(new Filter("name", search, null, Filter.Operation.LIKE, Filter.LogicalOperator.OR), new Filter("courseLevel", search, "training", Filter.Operation.LIKE, Filter.LogicalOperator.OR), new Filter("name", search, "trainer", Filter.Operation.LIKE, Filter.LogicalOperator.OR)));
        }
        Criteria criteria = new Criteria(filters, Optional.empty(), Optional.empty());
        return new ResponseEntity<>(this.queryTeamService.getAllTeams(p, criteria), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable String id) {
        return new ResponseEntity<>(this.queryTeamService.getTeamById(id), HttpStatus.OK);
    }

    @PostMapping("/gafetes/{id}")
    public ResponseEntity<byte[]> gafete(@PathVariable String id) throws IOException {
        String timestamp = LocalDateTime.now().format(
                DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")
        );
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
        ));
        headers.set(
                HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=gefetes" + timestamp + ".pdf"
        );
        byte[] excelBytes = this.commandTeamService.gafetes(id);
        return ResponseEntity
                .ok()
                .headers(headers)
                .body(excelBytes);
    }

    @GetMapping("/photo/{id}")
    public ResponseEntity<?> getPhoto(@PathVariable String id) {
        return new ResponseEntity<>(this.queryTeamService.getPhoto(id), HttpStatus.OK);
    }

    @PostMapping("/save/focus")
    public ResponseEntity<?> saveTeamFocus(@Valid @RequestBody SaveFocusTeamsRequest request) {
        commandTeamService.saveFocusTeam(request);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/save/life")
    public ResponseEntity<?> saveTeamLife(@Valid @RequestBody SaveLifeTeamRequest request) {
        commandTeamService.saveLifeTeam(request);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/save/your")
    public ResponseEntity<?> saveTeamYour(@Valid @RequestBody SaveYourTeamRequest request) {
        commandTeamService.saveYourTeam(request);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/promotion/life")
    public ResponseEntity<?> promoTeamToLife(@Valid @RequestBody PromotionLifeRequest request) {
        commandTeamService.promotionLifeTeam(request);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/promotion/your")
    public ResponseEntity<?> promoTeamToYour(@Valid @RequestBody PromotionYourRequest request) {
        commandTeamService.promotionYourTeam(request);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/match")
    public ResponseEntity<?> match(@Valid @RequestBody Criteria request) {
        return new ResponseEntity<>(queryTeamService.match(request), HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateTeam(@RequestBody SaveTeamRequest request) {
        commandTeamService.update(request.toDomain());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/updatePhoto/{id}")
    public ResponseEntity<?> updatePhoto(@PathVariable String id, @RequestParam("photo") MultipartFile photo) {
        try {
            return new ResponseEntity<>(commandTeamService.updatePhoto(id, photo), HttpStatus.CREATED);
        } catch (IOException e) {
            throw new BaseException(e.getMessage(), List.of(""));
        }
    }

    @PostMapping("/assignParticipants")
    public ResponseEntity<?> assignParticipants(@RequestBody AssignParticipantsRequest request) {
        for (var user : request.getUsers()) {
            commandTeamService.assignParticipants(request.getTeamId(), user.getId());
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/assignMaterlife")
    public ResponseEntity<?> assignMasterlife(@RequestBody AssignParticipantsRequest request) {
        for (var user : request.getUsers()) {
            commandTeamService.assignMastersLife(request.getTeamId(), user.getId());
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/removeParticipants")
    public ResponseEntity<?> removeParticipants(@RequestBody AssignParticipantsRequest request) {
        for (var user : request.getUsers()) {
            commandTeamService.removeParticipants(request.getTeamId(), user.getId(), user.getIsLingerer(), user.getIsDesertor());
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/removeMasterlife")
    public ResponseEntity<?> removeMasterlife(@RequestBody AssignParticipantsRequest request) {
        for (var user : request.getUsers()) {
            commandTeamService.removeMastersLife(request.getTeamId(), user.getId());
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/removeStaffs")
    public ResponseEntity<?> removeStaffs(@RequestBody AssignParticipantsRequest request) {
        for (var user : request.getUsers()) {
            commandTeamService.removeStaffs(request.getTeamId(), user.getId());
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/removeVisionaries")
    public ResponseEntity<?> removeVisionaries(@RequestBody AssignParticipantsRequest request) {
        for (var user : request.getUsers()) {
            commandTeamService.removeVisionaries(request.getTeamId(), user.getId());
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/byTraining/{trainingId}")
    public ResponseEntity<?> getByTrainingId(@PathVariable String trainingId) {
        return new ResponseEntity<>(this.queryTeamService.getByTrainingId(trainingId), HttpStatus.OK);
    }

    @GetMapping("/trainer/{trainerId}")
    public ResponseEntity<?> getByTrainerId(@PathVariable String trainerId) {
        return new ResponseEntity<>(this.queryTeamService.getByTrainerId(trainerId), HttpStatus.OK);
    }

    @PutMapping("/add-users/{teamId}")
    public ResponseEntity<?> addUsersToTeam(@PathVariable String teamId, @RequestBody AddUsersToTeamRequest request) {
        commandTeamService.addParticipants(teamId, request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/{id}/trainer/{trainerId}")
    public ResponseEntity<?> updateTrainer(@PathVariable String trainerId,@PathVariable String id) {
        commandTeamService.updateTrainer(id, trainerId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
