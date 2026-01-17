package com.foryourlife.masterLife.infrastructure.httpController;

import com.foryourlife.admin.programs.trainer.infrastructure.httpControllers.AvailableTrainerRequest;
import com.foryourlife.masterLife.application.CommandMasterLifeService;
import com.foryourlife.masterLife.application.QueryMasterLifeService;
import com.foryourlife.shared.domain.criteria.Criteria;
import com.foryourlife.shared.domain.criteria.Filter;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/master-life")
public class MasterLifeController {
    private final CommandMasterLifeService commandMasterLifeService;
    private final QueryMasterLifeService queryMasterLifeService;

    public MasterLifeController(CommandMasterLifeService commandMasterLifeService, QueryMasterLifeService queryMasterLifeService) {
        this.commandMasterLifeService = commandMasterLifeService;
        this.queryMasterLifeService = queryMasterLifeService;
    }

    @PutMapping("/change-status/{id}")
    public ResponseEntity<?> deleteStaff(@PathVariable String id) {
        commandMasterLifeService.changeStatus(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/available")
    public ResponseEntity<?> getForTeam(@Valid @RequestBody AvailableTrainerRequest request) {
        return new ResponseEntity<>(queryMasterLifeService.findavailableMasterLifes(request.startDate, request.endDate), HttpStatus.OK);
    }

    @PostMapping("add")
    public ResponseEntity<?> createStaff(@RequestBody @Valid MasterLifeSaveRequest request) {
        commandMasterLifeService.save(request.toDomain());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("masterlife-participant/{id}")
    public ResponseEntity<?> createMasterLifeFromParticipant(@PathVariable String id) {
        commandMasterLifeService.saveFromParticipant(id);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }


    @GetMapping("")
    public ResponseEntity<?> findStaffByUserId(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "perPage", defaultValue = "10") int perPage,
            @RequestParam(value = "search", defaultValue = "") String search
    ) {
        var p = PageRequest.of(page, perPage, Sort.by("id").descending());
        Criteria criteria = new Criteria(
                List.of(), Optional.empty(), Optional.empty()
        );
        if (!search.isEmpty()) {
            criteria.filters =
                    List.of(
                            new Filter(
                                    "name",
                                    search,
                                    "user",
                                    Filter.Operation.LIKE,
                                    Filter.LogicalOperator.OR
                            ),
                            new Filter(
                                    "email",
                                    search,
                                    "user",
                                    Filter.Operation.LIKE,
                                    Filter.LogicalOperator.OR
                            ),
                            new Filter(
                                    "phone",
                                    search,
                                    "user",
                                    Filter.Operation.LIKE,
                                    Filter.LogicalOperator.OR
                            )
                    );
        }
        return new ResponseEntity<>(queryMasterLifeService.getAll(p, criteria), HttpStatus.OK);
    }
}
