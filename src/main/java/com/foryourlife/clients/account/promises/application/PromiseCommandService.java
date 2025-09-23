package com.foryourlife.clients.account.promises.application;

import com.foryourlife.admin.programs.teams.application.QueryTeamService;
import com.foryourlife.clients.account.promises.domain.Promise;
import com.foryourlife.clients.account.promises.domain.PromiseRepository;
import com.foryourlife.clients.account.promises.infrastructure.http.PromiseRequest;
import com.foryourlife.shared.domain.exception.BaseException;
import com.foryourlife.shared.domain.level.CourseLevel;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.EnumSet;
import java.util.List;
import java.util.UUID;

@Service
public class PromiseCommandService {
    private final PromiseRepository promiseRepository;
    private final QueryTeamService queryTeamService;

    public PromiseCommandService(PromiseRepository promiseRepository, QueryTeamService queryTeamService) {
        this.promiseRepository = promiseRepository;
        this.queryTeamService = queryTeamService;
    }

    public void deletePromiseById(String id) {
        this.promiseRepository.deleteById(id);
    }

    public void createPromises(String trainingId) {
        var team = queryTeamService.getByTrainingId(trainingId);

        EnumSet<CourseLevel> availableLevels = EnumSet.of(CourseLevel.LIFE, CourseLevel.LIFE_2, CourseLevel.LIFE_3);

        if (!availableLevels.contains(team.getTraining().getCourseLevel())) {
            throw new BaseException("Nivel de curso inválido",
                    List.of("Las promesas solo se pueden crear para entrenamientos de nivel LIFE, LIFE 2 o LIFE 3"));
        }

        team.getUsers().forEach( it ->
                this.promiseRepository.save(
                        new Promise(
                                UUID.randomUUID().toString(),
                                it.getTeam().getTraining(),
                                it
                        )
                )
        );
    }

    public void savePromise(PromiseRequest promiseRequest) {

        var promise = this.promiseRepository.findById(promiseRequest.id)
                .orElseThrow(() -> new RuntimeException("La promesa no existe"));

        if (LocalDate.EPOCH.isBefore(promise.getTraining().getStartDate()) && LocalDate.EPOCH.isAfter(promise.getTraining().getEndDate())) {
            throw new BaseException("Fuera de tiempo", List.of("No se puede asignar promesas fuera del periodo del entrenamiento"));
        }

        if (promise.getFirstPromise() == null) {
            promise.setFirstPromise(promiseRequest.promise);
        } else if (promise.getSecondPromise() == null) {
            promise.setSecondPromise(promiseRequest.promise);
        } else if (promise.getThirdPromise() == null) {
            promise.setThirdPromise(promiseRequest.promise);
            promise.setStartDate(LocalDate.now());
            promise.setEndDate(promise.getStartDate().plusDays(5));
        } else {
            throw new IllegalStateException("Todas las promesas ya están asignadas");
        }
        this.promiseRepository.save(promise);
    }
}


