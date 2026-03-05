package com.foryourlife.admin.crm.call.application;

import com.foryourlife.admin.crm.call.domain.Call;
import com.foryourlife.admin.crm.call.domain.CallRepository;
import com.foryourlife.admin.crm.call.domain.CallResponse;
import com.foryourlife.clients.account.invitations.domain.Invitation;
import com.foryourlife.clients.account.invitations.domain.InvitationRepository;
import com.foryourlife.clients.account.participant.domain.Participant;
import com.foryourlife.clients.account.participant.domain.ParticipantRepository;
import com.foryourlife.shared.domain.criteria.Criteria;
import com.foryourlife.shared.domain.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class CallQueryService {
    private final CallRepository callRepository;

    private final ParticipantRepository participantRepository;

    private final InvitationRepository invitationRepository;

    public CallQueryService(CallRepository callRepository, ParticipantRepository participantRepository, InvitationRepository invitationRepository) {
        this.callRepository = callRepository;
        this.participantRepository = participantRepository;
        this.invitationRepository = invitationRepository;
    }

    public Call findById(String id) {
        return callRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Call not found with id: " + id)
        );
    }

    public Page<Call> findAll(Pageable pageable, Criteria criteria) {
        return callRepository.findAll(pageable, criteria);
    }

    public List<CallResponse> findAllByTrainingId(String trainingId) {

        var list = callRepository.findAllByTrainingId(trainingId);

        var usersIds = list.stream()
                .map(Call::getCalledUser)
                .map(User::getId)
                .toList();

        var participants = participantRepository.findAllByUserIds(usersIds);

        Map<String, Participant> participantMap =
                participants.stream().collect(Collectors.toMap(
                        p -> p.getUser().getId(),
                        p -> p
                ));

        var tokens = participants.stream()
                .map(Participant::getInvitationToken)
                .toList();

        var invitations = invitationRepository.findAllByTokenIn(tokens);

        Map<String, Invitation> invitationMap =
                invitations.stream().collect(Collectors.toMap(
                        Invitation::getToken,
                        i -> i
                ));

        return list.stream().map(call -> {

            var p = participantMap.get(call.getCalledUser().getId());
            var i = p != null ? invitationMap.get(p.getInvitationToken()) : null;

            return new CallResponse(
                    call.getId(),
                    p,
                    i,
                    call.getTraining(),
                    call.getCallLogs()
            );

        }).toList();
    }
}
