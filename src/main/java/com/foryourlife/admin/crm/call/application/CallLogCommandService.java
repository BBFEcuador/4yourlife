package com.foryourlife.admin.crm.call.application;

import com.foryourlife.admin.crm.call.domain.CallLog;
import com.foryourlife.admin.crm.call.domain.CallLogRepository;
import com.foryourlife.admin.crm.call.domain.CallStatus;
import com.foryourlife.admin.crm.call.domain.CallType;
import com.foryourlife.admin.crm.call.infrastructure.http.CallLogRequest;
import com.foryourlife.shared.domain.exception.BaseException;
import com.foryourlife.shared.domain.user.User;
import com.foryourlife.shared.domain.user.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CallLogCommandService {
    private final CallLogRepository callLogRepository;
    private final UserRepository userRepository;

    public CallLogCommandService(CallLogRepository callLogRepository, UserRepository userRepository) {
        this.callLogRepository = callLogRepository;
        this.userRepository = userRepository;
    }

    public void saveCallLog(CallLogRequest callLogRequest) {
        User calledUser = userRepository.findById(callLogRequest.getCalledUserId()).orElseThrow(
                () -> new BaseException("Usuario llamado no encontrado", List.of("Usuario llamado no encontrado"))
        );

        User calledBy = userRepository.findById(callLogRequest.getCalledById()).orElseThrow(
                () -> new BaseException("Usuario que llama no encontrado", List.of("Usuario que llama no encontrado"))
        );

        CallLog callLog = new CallLog(
                UUID.randomUUID().toString(),
                calledUser,
                calledBy,
                callLogRequest.getStartTime(),
                callLogRequest.getEndTime(),
                CallType.fromValue(callLogRequest.getCallType()),
                CallStatus.fromValue(callLogRequest.getCallStatus()),
                callLogRequest.getNotes()
        );

        callLogRepository.save(callLog);
    }
}
