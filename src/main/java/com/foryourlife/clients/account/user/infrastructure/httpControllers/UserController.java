package com.foryourlife.clients.account.user.infrastructure.httpControllers;

import com.foryourlife.admin.programs.trainer.infrastructure.httpControllers.AvailableTrainerRequest;
import com.foryourlife.clients.account.user.application.CommandUsersService;
import com.foryourlife.clients.account.user.application.QueryUsersService;
import com.foryourlife.shared.domain.criteria.Criteria;
import com.foryourlife.shared.domain.criteria.Filter;
import com.foryourlife.shared.domain.exception.BaseException;
import com.foryourlife.shared.domain.level.CourseLevel;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    private final QueryUsersService queryUsersService;
    private final CommandUsersService commandUsersService;

    public UserController(QueryUsersService queryUsersService, CommandUsersService commandUsersService) {
        this.queryUsersService = queryUsersService;
        this.commandUsersService = commandUsersService;
    }

    @GetMapping("")
    public ResponseEntity<?> getUsers() {
        return new ResponseEntity<>(queryUsersService.getAll(), HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getUser(@PathVariable String id) {
        return new ResponseEntity<>(queryUsersService.getUserTrainerById(id), HttpStatus.OK);
    }

    @PutMapping("")
    public ResponseEntity<?> updateUser(@RequestBody ParticipantRequest participant) {
        commandUsersService.update(participant.toDomain());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/level/{id}/{levelId}")
    public ResponseEntity<?> setLevel(@PathVariable String id, @PathVariable String levelId) {
        System.out.println(levelId);
        commandUsersService.setLevel(id, levelId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/match")
    public ResponseEntity<?> match(@RequestBody Criteria criteria) {
        return new ResponseEntity<>(queryUsersService.matchers(criteria), HttpStatus.OK);
    }

    @PostMapping("/participants-available/{lvl}")
    public ResponseEntity<?> getForTeam(@PathVariable String lvl) {

        switch (lvl) {
            case "FOCUS" -> {
                var criteria = new Criteria(
                        List.of(new Filter(
                                "courseLevel",
                                CourseLevel.FOCUS.toString(),
                                "participantLevel",
                                Filter.Operation.EQUAL,
                                Filter.LogicalOperator.AND
                        ), new Filter(
                                "teams",
                                null,
                                null,
                                Filter.Operation.IS_EMPTY,
                                Filter.LogicalOperator.AND
                        )), Optional.empty(), Optional.empty()
                );
                return new ResponseEntity<>(queryUsersService.matchers(criteria), HttpStatus.OK);
            }
            case "YOUR" -> {
                var criteria = new Criteria(
                        List.of(new Filter(
                                "courseLevel",
                                CourseLevel.YOUR.toString(),
                                "participantLevel",
                                Filter.Operation.EQUAL,
                                Filter.LogicalOperator.AND
                        ), new Filter(
                                "teams",
                                null,
                                null,
                                Filter.Operation.IS_EMPTY,
                                Filter.LogicalOperator.AND
                        )), Optional.empty(), Optional.empty()
                );
                return new ResponseEntity<>(queryUsersService.matchers(criteria), HttpStatus.OK);
            }
            case "LIFE" -> {
                var criteria = new Criteria(
                        List.of(new Filter(
                                "courseLevel",
                                CourseLevel.LIFE.toString(),
                                "participantLevel",
                                Filter.Operation.EQUAL,
                                Filter.LogicalOperator.AND
                        ), new Filter(
                                "teams",
                                null,
                                null,
                                Filter.Operation.IS_EMPTY,
                                Filter.LogicalOperator.AND
                        )), Optional.empty(), Optional.empty()
                );
                return new ResponseEntity<>(queryUsersService.matchers(criteria), HttpStatus.OK);
            }
            default -> throw new BaseException("Illegal argument", List.of("Type must be FOCUS, YOUR or LIFE"));
        }
    }

    @PostMapping("/create-from-admin")
    public ResponseEntity<?> createFromAdmin(@RequestBody ParticipantRequest participant) {
        commandUsersService.createFromAdmin(participant.toDomain());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/master-life-available")
    public ResponseEntity<?> getForTeam(@Valid @RequestBody AvailableTrainerRequest request) {
        return new ResponseEntity<>(queryUsersService.findAvailableMasterLife(request.startDate, request.endDate), HttpStatus.OK);
    }
}
