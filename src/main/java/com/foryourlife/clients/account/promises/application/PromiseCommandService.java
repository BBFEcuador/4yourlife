package com.foryourlife.clients.account.promises.application;

import com.foryourlife.admin.programs.attendance.infraestructure.httpController.DaysEnum;
import com.foryourlife.admin.programs.teams.application.QueryTeamService;
import com.foryourlife.clients.account.participant.domain.Participant;
import com.foryourlife.clients.account.promises.domain.Promise;
import com.foryourlife.clients.account.promises.domain.PromiseRepository;
import com.foryourlife.clients.account.promises.infrastructure.http.PromiseRequest;
import com.foryourlife.masterLife.domain.MasterLife;
import com.foryourlife.shared.domain.exception.BaseException;
import com.foryourlife.shared.domain.level.CourseLevel;
import com.foryourlife.shared.domain.user.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.EnumSet;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

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

    @Transactional
    public void createPromises(String trainingId) {

        var team = queryTeamService.getByTrainingId(trainingId);
        var training = team.getTraining();

        EnumSet<CourseLevel> availableLevels = EnumSet.of(
                CourseLevel.LIFE,
                CourseLevel.LIFE_2,
                CourseLevel.LIFE_3,
                CourseLevel.LIFE_GRADUATE
        );

        if (!availableLevels.contains(training.getCourseLevel())) {
            return;
        }

        var startDate = training.getEndDate().plusDays(1);
        var endDate = training.getEndDate().plusDays(5);

        List<User> allUsers = Stream.concat(
                        team.getUsers().stream().map(Participant::getUser),
                        team.getMasterLife().stream().map(MasterLife::getUser)
                )
                .distinct()
                .toList();

        List<Promise> promises = allUsers.stream()
                .map(user -> {
                    Promise promise = new Promise(
                            UUID.randomUUID().toString(),
                            training,
                            user
                    );
                    promise.setStartDate(startDate);
                    promise.setEndDate(endDate);
                    return promise;
                })
                .toList();

        promiseRepository.saveAll(promises);
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

        DaysEnum dayEnum = DaysEnum.fromString(promiseRequest.day);

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


