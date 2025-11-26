package com.foryourlife.admin.crm.call.application;

import com.foryourlife.admin.crm.call.domain.Call;
import com.foryourlife.admin.crm.call.domain.CallRepository;
import com.foryourlife.shared.domain.criteria.Criteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CallQueryService {
    private final CallRepository callRepository;

    public CallQueryService(CallRepository callRepository) {
        this.callRepository = callRepository;
    }

    public Call findById(String id) {
        return callRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Call not found with id: " + id)
        );
    }

    public Page<Call> findAll(Pageable pageable, Criteria criteria) {
        return callRepository.findAll(pageable, criteria);
    }

    public List<Call> findAllByTrainingId(String trainingId) {
        return callRepository.findAllByTrainingId(trainingId);
    }
}
