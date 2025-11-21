package com.foryourlife.admin.crm.call.domain;

import com.foryourlife.shared.domain.criteria.Criteria;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface CallLogRepository {
    void save(CallLog callLog);
    Page<CallLog> getCallLogs(Criteria criteria);
    List<CallLog> getAllCallLogsByCalledBy_Id(String userId);
    List<CallLog> getAllCallLogsByCalledUser_Id(String userId);
    Optional<CallLog> getCallLogById(String id);
}
