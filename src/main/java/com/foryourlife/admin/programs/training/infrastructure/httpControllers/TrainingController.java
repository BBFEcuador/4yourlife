package com.foryourlife.admin.programs.training.infrastructure.httpControllers;

import com.foryourlife.admin.programs.training.application.CommandTrainingService;
import com.foryourlife.admin.programs.training.application.QueryTrainingService;
import com.foryourlife.admin.programs.training.domain.Training;
import com.foryourlife.shared.domain.criteria.Criteria;
import com.foryourlife.shared.domain.criteria.Filter;
import com.foryourlife.shared.domain.exception.BaseException;
import com.foryourlife.shared.domain.level.CourseLevel;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/admin/training")
public class TrainingController {
    private final CommandTrainingService commandTrainingService;
    private final QueryTrainingService queryTrainingService;

    public TrainingController(CommandTrainingService commandTrainingService, QueryTrainingService queryTrainingService) {
        this.commandTrainingService = commandTrainingService;
        this.queryTrainingService = queryTrainingService;
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'USER')")
    @PostMapping("generate")
    public ResponseEntity<?> autoGenerate(@Valid @RequestBody TrainingAutoGenerateRequest request) {
        commandTrainingService.autoGenerateTraining(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'USER')")
    @PutMapping("update")
    public ResponseEntity<?> update(@Valid @RequestBody UpdateTrainingRequest request) {
        if (queryTrainingService.getTrainingById(request.getId()) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        commandTrainingService.saveTraining(request.toDomain());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/date")
    public ResponseEntity<?> updateStartDate(@Valid @RequestBody UpdateStartDateTrainingRequest request) {
        if (queryTrainingService.getTrainingById(request.getId()) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        commandTrainingService.updateStartDate(request.getId(), request.getStartDate());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/calendar-event")
    public ResponseEntity<?> getAllCalendarEvent(@RequestParam(value = "page", defaultValue = "0") int page, @RequestParam(value = "perPage", defaultValue = "10") int perPage, @RequestParam(value = "search", defaultValue = "") String search, @RequestParam(value = "campusId", defaultValue = "") String campusId, @RequestParam(value = "startDate", defaultValue = "") String startDate, @RequestParam(value = "endDate", defaultValue = "") String endDate) {
        var p = PageRequest.of(page, perPage, Sort.by("startDate").ascending());
        List<Filter> filters = new ArrayList<>();

        if (!search.isEmpty()) {
            filters.addAll(List.of(new Filter("name", search, null, Filter.Operation.LIKE, Filter.LogicalOperator.OR), new Filter("courseLevel", search, null, Filter.Operation.LIKE, Filter.LogicalOperator.OR)));
        }
        if (!startDate.isEmpty() && !endDate.isEmpty()) {
            filters.add(new Filter("startDate", startDate + "," + endDate, null, Filter.Operation.BETWEEN, Filter.LogicalOperator.OR));
            filters.add(new Filter("endDate", startDate + "," + endDate, null, Filter.Operation.BETWEEN, Filter.LogicalOperator.OR));
        }

        if (!campusId.isEmpty()) {
            filters.add(new Filter("id", campusId, "campus", Filter.Operation.EQUAL, Filter.LogicalOperator.AND));
        }

        var criteria = new Criteria(filters, Optional.empty(), Optional.empty());
        criteria.filters = filters;
        var trainings = queryTrainingService.getAllTrainings(p, criteria);

        List<TrainingResponse> trainingResponseList = trainings.getContent().stream().map(training -> {
            String color = switch (training.getCourseLevel()) {
                case FOCUS -> "#2f49f5";
                case YOUR -> "#f52f9c";
                case LIFE -> "#f5922f";
                case LIFE_2 -> "#418181";
                case LIFE_3 -> "#b875b5";
                case LIFE_GRADUATE -> "#24a830";
                default -> "#00FF00";
            };

            String name = switch (training.getCourseLevel()) {
                case LIFE, LIFE_2, LIFE_3 -> training.getName() + " LIFE";
                case LIFE_GRADUATE -> training.getName() + " GRADUACIÓN 🚀";
                default -> training.getName() + " " + training.getCourseLevel().name();
            };

            return new TrainingResponse(training.getId(), name, training.getStartDate(), training.getEndDate().atTime(23, 59), false, color, new TrainingResponse.ExtendedProps("Epic description", "Nice location", new String[]{"clave", "valor"}), training);
        }).toList();

        Page<TrainingResponse> responsePage = new PageImpl<>(trainingResponseList, trainings.getPageable(), trainings.getTotalElements());

        return ResponseEntity.ok(responsePage);

    }

    @GetMapping("")
    public ResponseEntity<?> getAll(@RequestParam(value = "page", defaultValue = "0") int page, @RequestParam(value = "perPage", defaultValue = "10") int perPage, @RequestParam(value = "search", defaultValue = "") String search, @RequestParam(value = "campusId", defaultValue = "") String campusId) {
        var p = PageRequest.of(page, perPage, Sort.by("startDate").descending());
        List<Filter> filters = new ArrayList<>();

        if (!search.isEmpty()) {
            filters.addAll(List.of(new Filter("name", search, null, Filter.Operation.LIKE, Filter.LogicalOperator.OR), new Filter("number", search, null, Filter.Operation.LIKE, Filter.LogicalOperator.OR), new Filter("courseLevel", search, null, Filter.Operation.LIKE, Filter.LogicalOperator.OR), new Filter("startDate", search, null, Filter.Operation.BETWEEN, Filter.LogicalOperator.OR)));
        }

        if (!campusId.isEmpty()) {
            filters.add(new Filter("id", campusId, "campus", Filter.Operation.EQUAL, Filter.LogicalOperator.AND));
        }

        var criteria = new Criteria(filters, Optional.empty(), Optional.empty());
        criteria.filters = filters;
        return new ResponseEntity<>(queryTrainingService.getAllTrainings(p, criteria), HttpStatus.OK);
    }

    @PostMapping("/filter/{lvl}")
    public ResponseEntity<?> getByLevel(@PathVariable String lvl) {
        switch (lvl) {
            case "FOCUS" -> {
                var criteria = new Criteria(List.of(new Filter("courseLevel", CourseLevel.FOCUS.toString(), null, Filter.Operation.EQUAL, Filter.LogicalOperator.AND), new Filter("originalTeam", null, null, Filter.Operation.IS_NULL, Filter.LogicalOperator.AND)), Optional.empty(), Optional.empty());
                return new ResponseEntity<>(queryTrainingService.match(criteria), HttpStatus.OK);
            }
            case "YOUR" -> {
                var criteria = new Criteria(List.of(new Filter("courseLevel", CourseLevel.YOUR.name(), null, Filter.Operation.EQUAL, Filter.LogicalOperator.AND), new Filter("originalTeam", null, null, Filter.Operation.IS_NULL, Filter.LogicalOperator.AND)), Optional.empty(), Optional.empty());
                return new ResponseEntity<>(queryTrainingService.match(criteria), HttpStatus.OK);
            }
            case "LIFE" -> {
                var criteria = new Criteria(List.of(new Filter("courseLevel", CourseLevel.LIFE.name(), null, Filter.Operation.EQUAL, Filter.LogicalOperator.AND), new Filter("originalTeam", null, null, Filter.Operation.IS_NULL, Filter.LogicalOperator.AND)), Optional.empty(), Optional.empty());
                return new ResponseEntity<>(queryTrainingService.match(criteria), HttpStatus.OK);
            }
            default -> throw new BaseException("Illegal argument", List.of("Type must be FOCUS, YOUR or LIFE"));
        }
    }
}
