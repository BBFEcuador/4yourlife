package com.foryourlife.clients.account.promises.application;

import com.foryourlife.clients.account.participant.application.ParticipantQueryService;
import com.foryourlife.clients.account.promises.domain.Promise;
import com.foryourlife.clients.account.promises.domain.PromiseRepository;
import com.foryourlife.clients.account.promises.infrastructure.http.PromiseRequest;
import com.foryourlife.shared.domain.criteria.Criteria;
import com.foryourlife.shared.domain.criteria.Filter;
import com.foryourlife.shared.domain.level.CourseLevel;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PromiseCommandService {
    private final PromiseRepository promiseRepository;
    private final ParticipantQueryService participantQueryService;

    public PromiseCommandService(PromiseRepository promiseRepository, ParticipantQueryService participantQueryService) {
        this.promiseRepository = promiseRepository;
        this.participantQueryService = participantQueryService;
    }

    public void deletePromiseById(String id) {
        this.promiseRepository.deleteById(id);
    }

    public void createPromise() {
        var criteria = new Criteria(List.of(
                new Filter("courseLevel", CourseLevel.LIFE.toString(), "participantLevel", Filter.Operation.EQUAL, Filter.LogicalOperator.AND)
        ),
                Optional.empty(),
                Optional.empty()
        );

        var lifeParticipants = this.participantQueryService.matchers(criteria);

        lifeParticipants.forEach(participant ->

                this.promiseRepository.save(
                        new Promise(
                                UUID.randomUUID().toString(),
                                participant.getTeam().getTraining(),
                                participant
                        )
                )
        );
    }

    public void savePromise(PromiseRequest promiseRequest) {
        var promise = this.promiseRepository.findById(promiseRequest.id)
                .orElseThrow(() -> new RuntimeException("La promesa no existe"));

        if (promise.getFirstPromise() == null) {
            promise.setFirstPromise(promiseRequest.promise);
        } else if (promise.getSecondPromise() == null) {
            promise.setSecondPromise(promiseRequest.promise);
        } else if (promise.getThirdPromise() == null) {
            promise.setThirdPromise(promiseRequest.promise);
        } else {
            throw new IllegalStateException("Todas las promesas ya están asignadas");
        }
        this.promiseRepository.save(promise);
    }

}


