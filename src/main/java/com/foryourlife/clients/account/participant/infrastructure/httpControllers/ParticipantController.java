package com.foryourlife.clients.account.participant.infrastructure.httpControllers;

import com.foryourlife.clients.account.participant.application.ParticipantCommandService;
import com.foryourlife.clients.account.participant.application.ParticipantQueryService;
import com.foryourlife.shared.domain.criteria.Criteria;
import com.foryourlife.shared.domain.criteria.Filter;
import com.foryourlife.shared.domain.exception.BaseException;
import com.foryourlife.shared.domain.level.CourseLevel;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class ParticipantController {

    private final ParticipantQueryService participantQueryService;
    private final ParticipantCommandService participantCommandService;

    public ParticipantController(ParticipantQueryService participantQueryService, ParticipantCommandService participantCommandService) {
        this.participantQueryService = participantQueryService;
        this.participantCommandService = participantCommandService;
    }

    @GetMapping("")
    public ResponseEntity<?> getUsers(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "perPage", defaultValue = "10") int perPage,
            @RequestParam(value = "search", defaultValue = "") String search,
            @RequestParam(value = "campusId", defaultValue = "") String campusId
    ) {
        var pageable = PageRequest.of(page, perPage, Sort.by("id").descending());
        List<Filter> filters = new ArrayList<>();

        if (!search.isEmpty()) {
            filters.addAll(List.of(
                    new Filter("name", search, "user", Filter.Operation.LIKE, Filter.LogicalOperator.OR),
                    new Filter("email", search, "user", Filter.Operation.LIKE, Filter.LogicalOperator.OR),
                    new Filter("phone", search, "user", Filter.Operation.LIKE, Filter.LogicalOperator.OR),
                    new Filter("city", search, "campus", Filter.Operation.LIKE, Filter.LogicalOperator.OR),
                    new Filter("dni", search, "profile", Filter.Operation.LIKE, Filter.LogicalOperator.OR),
                    new Filter("name", search, "teams", Filter.Operation.LIKE, Filter.LogicalOperator.OR),
                    new Filter("courseLevel", search, "participantLevel", Filter.Operation.LIKE, Filter.LogicalOperator.OR)
            ));
        }

        if (!campusId.isEmpty()) {
            filters.add(new Filter("id", campusId, "campus", Filter.Operation.EQUAL, Filter.LogicalOperator.AND));
        }

        var criteria = new Criteria(filters, Optional.empty(), Optional.empty());
        var result = participantQueryService.getAll(pageable, criteria);
        return ResponseEntity.ok(result);
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getUser(@PathVariable String id) {
        return new ResponseEntity<>(participantQueryService.getParticipantById(id), HttpStatus.OK);
    }

    @PutMapping("")
    public ResponseEntity<?> updateUser(@RequestBody ParticipantRequest participant) {
        participantCommandService.update(participant.toDomain());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/level/{id}/{levelId}")
    public ResponseEntity<?> setLevel(@PathVariable String id, @PathVariable String levelId) {
        System.out.println(levelId);
        participantCommandService.setLevel(id, levelId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/match")
    public ResponseEntity<?> match(@RequestBody Criteria criteria) {
        return new ResponseEntity<>(participantQueryService.matchers(criteria), HttpStatus.OK);
    }

    @PostMapping("/participants-available/{lvl}")
    public ResponseEntity<?> getForTeam(
            @RequestParam(value = "campusId", defaultValue = "") String campusId,
            @PathVariable String lvl) {
        switch (lvl) {
            case "FOCUS" -> {
                var filters = List.of(
                        new Filter("courseLevel", CourseLevel.FOCUS.toString(), "participantLevel", Filter.Operation.EQUAL, Filter.LogicalOperator.AND),
                        new Filter("teams", null, null, Filter.Operation.IS_EMPTY, Filter.LogicalOperator.AND)

                );
                if (!campusId.isBlank()){
                    new Filter("id", campusId, "campus", Filter.Operation.EQUAL, Filter.LogicalOperator.AND);
                }
                var criteria = new Criteria(
                        filters,
                        Optional.empty(),
                        Optional.empty()
                );

                return new ResponseEntity<>(participantQueryService.matchers(criteria), HttpStatus.OK);
            }
            case "YOUR" -> {
                var filters = List.of(
                        new Filter("courseLevel", CourseLevel.YOUR.toString(), "participantLevel", Filter.Operation.EQUAL, Filter.LogicalOperator.AND),
                        new Filter("teams", null, null, Filter.Operation.IS_EMPTY, Filter.LogicalOperator.AND),
                        new Filter("id", campusId, "campus", Filter.Operation.EQUAL, Filter.LogicalOperator.AND)
                );
                if (!campusId.isBlank()){
                    new Filter("id", campusId, "campus", Filter.Operation.EQUAL, Filter.LogicalOperator.AND);
                }
                var criteria = new Criteria(
                        filters,
                        Optional.empty(),
                        Optional.empty()
                );
                return new ResponseEntity<>(participantQueryService.matchers(criteria), HttpStatus.OK);
            }
            case "LIFE" -> {
                var filter = List.of(
                        new Filter("courseLevel", CourseLevel.LIFE.toString(), "participantLevel", Filter.Operation.EQUAL, Filter.LogicalOperator.AND),
                        new Filter("teams", null, null, Filter.Operation.IS_EMPTY, Filter.LogicalOperator.AND),
                        new Filter("id", campusId, "campus", Filter.Operation.EQUAL, Filter.LogicalOperator.AND)
                );
                if (!campusId.isBlank()){
                    new Filter("id", campusId, "campus", Filter.Operation.EQUAL, Filter.LogicalOperator.AND);
                }
                var criteria = new Criteria(
                        filter,
                        Optional.empty(),
                        Optional.empty()
                );
                return new ResponseEntity<>(participantQueryService.matchers(criteria), HttpStatus.OK);
            }
            default -> throw new BaseException("Illegal argument", List.of("Type must be FOCUS, YOUR or LIFE"));
        }
    }

    @PostMapping("/create-from-admin")
    public ResponseEntity<?> createFromAdmin(@RequestBody ParticipantRequest participant) {
        participantCommandService.createFromAdmin(participant.toDomain());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/promotion-master/{id}")
    public ResponseEntity<?> promotionMasterLife(@PathVariable String id) {
        participantCommandService.promotionToMasterLife(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/change-campus/{participantId}/{campusId}")
    public ResponseEntity<?> changeParticipantCampus(@PathVariable String participantId, @PathVariable String campusId) {
        participantCommandService.changeParticipantCampus(participantId, campusId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/get-contract/{participantId}")
    public ResponseEntity<?> getContract(@PathVariable String participantId) {
        ByteArrayOutputStream pdfBytes = participantCommandService.getContract(participantId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("filename", "FULL POTENTIAL - CONTRATO PRESTACIÓN SERVICIOS ENTRENAMIENTO.pdf");

        return ResponseEntity.ok().headers(headers).body(pdfBytes.toByteArray());    }
}
