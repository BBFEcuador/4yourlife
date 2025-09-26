package com.foryourlife.clients.account.promises.application;

import com.foryourlife.admin.programs.teams.application.QueryTeamService;
import com.foryourlife.clients.account.promises.domain.Promise;
import com.foryourlife.clients.account.promises.domain.PromiseRepository;
import com.foryourlife.clients.account.promises.infrastructure.http.PromiseRequest;
import com.foryourlife.shared.domain.exception.BaseException;
import com.foryourlife.shared.domain.level.CourseLevel;
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

    public PromiseCommandService(PromiseRepository promiseRepository, QueryTeamService queryTeamService) {
        this.promiseRepository = promiseRepository;
        this.queryTeamService = queryTeamService;
    }

    public void deletePromiseById(String id) {
        this.promiseRepository.deleteById(id);
    }

    public void createPromises(String trainingId) {
        var team = queryTeamService.getByTrainingId(trainingId);

        EnumSet<CourseLevel> availableLevels = EnumSet.of(CourseLevel.LIFE, CourseLevel.LIFE_2, CourseLevel.LIFE_3, CourseLevel.LIFE_GRADUATE);

        if (!availableLevels.contains(team.getTraining().getCourseLevel())) {
            throw new BaseException("Nivel de curso inválido",
                    List.of("Las promesas solo se pueden crear para entrenamientos de nivel LIFE, LIFE 2 o LIFE 3"));
        }

        team.getUsers().forEach(it -> {
            Promise promise = new Promise(
                    UUID.randomUUID().toString(),
                    it.getTeam().getTraining(),
                    it
            );
            LocalDate today = LocalDate.now();
            promise.setStartDate(today);
            promise.setEndDate(today.plusDays(5));

            this.promiseRepository.save(promise);
        });

    }

    public void savePromise(PromiseRequest promiseRequest) {

        var promise = this.promiseRepository.findById(promiseRequest.id)
                .orElseThrow(() -> new BaseException(
                        "La promesa no existe",
                        List.of("El ID proporcionado no corresponde a ninguna promesa existente")
                ));

        LocalDate today = LocalDate.now();
        LocalDate start = promise.getTraining().getStartDate();
        LocalDate end = promise.getTraining().getEndDate();

        if (today.isBefore(start) || today.isAfter(end)) {
            throw new BaseException(
                    "Fuera de tiempo",
                    List.of("No se puede asignar promesas fuera del periodo del entrenamiento")
            );
        }

        long dayNumber = ChronoUnit.DAYS.between(start, today) + 1;

        switch (promiseRequest.day) {
            case FRIDAY -> {
                if (dayNumber == 1) {
                    promise.setFirstPromise(promiseRequest.promise);
                } else {
                    throw new BaseException(
                            "Día incorrecto",
                            List.of("La promesa del viernes solo se puede registrar el primer día del training.")
                    );
                }
            }
            case SATURDAY -> {
                if (dayNumber == 2) {
                    promise.setSecondPromise(promiseRequest.promise);
                } else {
                    throw new BaseException(
                            "Día incorrecto",
                            List.of("La promesa del sábado solo se puede registrar el segundo día del training.")
                    );
                }
            }
            case SUNDAY -> {
                if (dayNumber == 3) {
                    promise.setThirdPromise(promiseRequest.promise);
                } else {
                    throw new BaseException(
                            "Día incorrecto",
                            List.of("La promesa del domingo solo se puede registrar el tercer día del training.")
                    );
                }
            }
            default -> throw new BaseException(
                    "Día inválido",
                    List.of("El día proporcionado no es válido para promesas.")
            );
        }

        this.promiseRepository.save(promise);
    }

}


