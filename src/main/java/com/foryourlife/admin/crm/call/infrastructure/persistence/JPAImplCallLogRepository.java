package com.foryourlife.admin.crm.call.infrastructure.persistence;

import com.foryourlife.admin.crm.call.domain.CallLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface JPAImplCallLogRepository extends JpaRepository<CallLog, String>, JpaSpecificationExecutor<CallLog> {
    List<CallLog> findAllByCalledBy_Id(String calledById);

    List<CallLog> findAllByCalledUser_Id(String calledUserId);
}
