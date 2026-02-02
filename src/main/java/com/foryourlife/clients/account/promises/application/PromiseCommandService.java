package com.foryourlife.clients.account.promises.application;

import com.foryourlife.admin.programs.attendance.infraestructure.httpController.DaysEnum;
import com.foryourlife.admin.programs.teams.application.QueryTeamService;
import com.foryourlife.clients.account.promises.domain.Promise;
import com.foryourlife.clients.account.promises.domain.PromiseRepository;
import com.foryourlife.clients.account.promises.infrastructure.http.PromiseRequest;
import com.foryourlife.shared.domain.exception.BaseException;
import com.foryourlife.shared.domain.level.CourseLevel;
import com.foryourlife.admin.programs.training.application.TrainingValidationService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.EnumSet;
import java.util.List;
import java.util.UUID;

@Service
public class PromiseCommandService {
    private final PromiseRepository promiseRepository;
    private final QueryTeamService queryTeamService;
    private final TrainingValidationService trainingValidationService;

    public PromiseCommandService(PromiseRepository promiseRepository, QueryTeamService queryTeamService, TrainingValidationService trainingValidationService) {
        this.promiseRepository = promiseRepository;
        this.queryTeamService = queryTeamService;
        this.trainingValidationService = trainingValidationService;
    }

    public void deletePromiseById(String id) {
        this.promiseRepository.deleteById(id);
    }

    public void createPromises(String trainingId) {
        var team = queryTeamService.getByTrainingId(trainingId);

        EnumSet<CourseLevel> availableLevels = EnumSet.of(CourseLevel.LIFE, CourseLevel.LIFE_2, CourseLevel.LIFE_3, CourseLevel.LIFE_GRADUATE);

        if (!availableLevels.contains(team.getTraining().getCourseLevel())) {
            System.err.println(team.getTraining().getCourseLevel() + " is not available");
            return;
        }

        team.getUsers().forEach(it -> {
            Promise promise = new Promise(
                    UUID.randomUUID().toString(),
                    team.getTraining(),
                    it.getUser()
            );
            promise.setStartDate(team.getTraining().getEndDate().plusDays(1));
            promise.setEndDate(team.getTraining().getEndDate().plusDays(5));

            this.promiseRepository.save(promise);
        });

        team.getMasterLife().forEach(it -> {
            Promise promise = new Promise(
                    UUID.randomUUID().toString(),
                    team.getTraining(),
                    it.getUser()
            );
            promise.setStartDate(team.getTraining().getEndDate().plusDays(1));
            promise.setEndDate(team.getTraining().getEndDate().plusDays(5));

            this.promiseRepository.save(promise);
        });

    }

    public void savePromise(PromiseRequest promiseRequest) {

        var promise = promiseRepository.findById(promiseRequest.id)
                .orElseThrow(() -> new BaseException(
                        "La promesa no existe",
                        List.of("El ID proporcionado no corresponde a ninguna promesa existente.")
                ));

        LocalDate today = LocalDate.now();
        LocalDate start = promise.getTraining().getStartDate();
        LocalDate end = promise.getTraining().getEndDate();

        trainingValidationService.validateDateInTrainingPeriod(today, start, end);
        long dayNumber = ChronoUnit.DAYS.between(start, today) + 1;
        DaysEnum dayEnum = DaysEnum.fromString(promiseRequest.day);

        trainingValidationService.validateDayConsistency(dayEnum, dayNumber);

        switch (dayEnum) {
            case FRIDAY -> promise.setFirstPromise(promiseRequest.promise);
            case SATURDAY -> promise.setSecondPromise(promiseRequest.promise);
            case SUNDAY -> promise.setThirdPromise(promiseRequest.promise);
            default -> throw new BaseException(
                    "Día inválido",
                    List.of("El día proporcionado no es válido para promesas.")
            );
        }

        promiseRepository.save(promise);
    }
}


