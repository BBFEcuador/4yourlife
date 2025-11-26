package com.foryourlife.admin.crm.callLogs.application;

import com.foryourlife.admin.crm.call.domain.Call;
import com.foryourlife.admin.crm.call.domain.CallRepository;
import com.foryourlife.admin.crm.callLogs.domain.CallLog;
import com.foryourlife.admin.crm.callLogs.domain.CallLogRepository;
import com.foryourlife.admin.crm.callLogs.domain.CallStatus;
import com.foryourlife.admin.crm.callLogs.domain.CallType;
import com.foryourlife.admin.crm.callLogs.infrastructure.http.CallLogRequest;
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
    private final CallRepository callRepository;

    public CallLogCommandService(CallLogRepository callLogRepository, UserRepository userRepository, CallRepository callRepository) {
        this.callLogRepository = callLogRepository;
        this.userRepository = userRepository;
        this.callRepository = callRepository;
    }

    public void saveCallLog(CallLogRequest callLogRequest) {
        User calledBy = userRepository.findById(callLogRequest.getCalledById()).orElseThrow(
                () -> new BaseException("Usuario que llama no encontrado", List.of("Usuario que llama no encontrado"))
        );

        Call call = callRepository.findById(callLogRequest.getCallId()).orElseThrow(
                () -> new BaseException("Llamada no encontrada", List.of("Llamada no encontrada"))
        );

        CallLog callLog = new CallLog(
                UUID.randomUUID().toString(),
                calledBy,
                callLogRequest.getDate(),
                CallType.fromValue(callLogRequest.getCallType()),
                CallStatus.fromValue(callLogRequest.getCallStatus()),
                callLogRequest.getNotes(),
                call
        );

        callLogRepository.save(callLog);
    }
}
