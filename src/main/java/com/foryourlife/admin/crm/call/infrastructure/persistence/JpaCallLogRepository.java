package com.foryourlife.admin.crm.call.infrastructure.persistence;

import com.foryourlife.admin.crm.call.domain.CallLog;
import com.foryourlife.admin.crm.call.domain.CallLogRepository;
import com.foryourlife.shared.domain.criteria.Criteria;
import com.foryourlife.shared.infrastructure.criteria.JPACriteriaConverter;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class JpaCallLogRepository implements CallLogRepository {

    private final JPAImplCallLogRepository jpaImplCallLogRepository;
    private final JPACriteriaConverter<CallLog> converter;

    public JpaCallLogRepository(JPAImplCallLogRepository jpaImplCallLogRepository, JPACriteriaConverter<CallLog> converter) {
        this.jpaImplCallLogRepository = jpaImplCallLogRepository;
        this.converter = converter;
    }

    @Override
    public void save(CallLog callLog) {
        jpaImplCallLogRepository.save(callLog);
    }

    @Override
    public Page<CallLog> getCallLogs(Criteria criteria) {
        return jpaImplCallLogRepository.findAll(
                converter.getJpaSpecifications(criteria),
                converter.getJpaPageable(criteria)
        );
    }

    @Override
    public List<CallLog> getAllCallLogsByCalledBy_Id(String userId) {
        return jpaImplCallLogRepository.findAllByCalledBy_Id(userId);
    }

    @Override
    public List<CallLog> getAllCallLogsByCalledUser_Id(String userId) {
        return jpaImplCallLogRepository.findAllByCalledUser_Id(userId);
    }

    @Override
    public Optional<CallLog> getCallLogById(String id) {
        return jpaImplCallLogRepository.findById(id);
    }
}
