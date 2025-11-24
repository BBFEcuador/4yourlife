package com.foryourlife.clients.account.promises.application;

import com.foryourlife.clients.account.promises.domain.Promise;
import com.foryourlife.clients.account.promises.domain.PromiseRepository;
import com.foryourlife.shared.domain.criteria.Criteria;
import com.foryourlife.shared.domain.exception.BaseException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PromiseQueryService {
    private final PromiseRepository promiseRepository;

    public PromiseQueryService(PromiseRepository promiseRepository) {
        this.promiseRepository = promiseRepository;
    }

    public Page<Promise> findAll(Pageable pageable, Criteria criteria) {
        return promiseRepository.findAll(pageable, criteria);
    }

    public Promise findById(String id) {
        return promiseRepository.findById(id).orElseThrow(
                () -> new BaseException("Promise not found", List.of())
        );
    }

    public List<Promise> findByTrainingId(String trainingId) {
        return promiseRepository.findByTrainingId(trainingId);
    }

    public Promise findLastByParticipant(String participantId) {
        return promiseRepository.findLastByUserId(participantId).orElseThrow(
                () -> new BaseException("No active promise found for this participant", List.of())
        );
    }
}
