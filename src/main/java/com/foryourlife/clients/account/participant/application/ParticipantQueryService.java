package com.foryourlife.clients.account.participant.application;

import com.foryourlife.admin.programs.teams.domain.TeamRepository;
import com.foryourlife.clients.account.participant.domain.UserNotFoundException;
import com.foryourlife.clients.account.participant.domain.ParticipantRepository;
import com.foryourlife.clients.account.participant.domain.Participant;
import com.foryourlife.shared.domain.criteria.Criteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ParticipantQueryService {
    private final ParticipantRepository _participantRepository;
    private final Logger logger = LoggerFactory.getLogger(ParticipantQueryService.class);

    public ParticipantQueryService(ParticipantRepository _participantRepository, TeamRepository teamRepository) {
        this._participantRepository = _participantRepository;
    }

    public Participant getUserById(String id) {
        return this._participantRepository.findById(id).orElseThrow(() -> new UserNotFoundException("The participant Id: " + id + " doesn't exist."));

    }

    public Participant getUserTrainerById(String id) {
        return this._participantRepository.findById(id).orElseThrow(() -> new UserNotFoundException("The participant Id: " + id + " doesn't exist."));
    }

    public List<Participant> getAll() {
        return this._participantRepository.getAll();
    }
    public Page<Participant> getAll(Pageable pageable,Criteria criteria) {
        return this._participantRepository.getAll(pageable,criteria);
    }
    public List<Participant> matchers(Criteria criteria) {
        return this._participantRepository.match(criteria);
    }

}
