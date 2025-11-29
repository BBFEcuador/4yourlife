package com.foryourlife.admin.crm.callLogs.application;

import com.foryourlife.admin.crm.callLogs.domain.CallLog;
import com.foryourlife.admin.crm.callLogs.domain.CallLogRepository;
import com.foryourlife.shared.domain.criteria.Criteria;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CallLogQueryService {
    private final CallLogRepository callLogRepository;

    public CallLogQueryService(CallLogRepository callLogRepository) {
        this.callLogRepository = callLogRepository;
    }

    public Page<CallLog> findAll(Criteria criteria) {
        return callLogRepository.getCallLogs(criteria);
    }

    public Optional<CallLog> findById(String id) {
        return callLogRepository.getCallLogById(id);
    }

    public List<CallLog> findAllByCalledById(String calledById) {
        return callLogRepository.getAllCallLogsByCalledBy_Id(calledById);
    }
}
