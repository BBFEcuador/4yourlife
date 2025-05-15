package com.foryourlife.clients.account.user.application;

import com.foryourlife.admin.programs.teams.domain.TeamRepository;
import com.foryourlife.clients.account.user.domain.UserNotFoundException;
import com.foryourlife.clients.account.user.domain.UserRepository;
import com.foryourlife.clients.account.user.domain.Participant;
import com.foryourlife.shared.domain.criteria.Criteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class QueryUsersService {
    private final UserRepository _userRepository;
    private final Logger logger = LoggerFactory.getLogger(QueryUsersService.class);

    public QueryUsersService(UserRepository _userRepository, TeamRepository teamRepository) {
        this._userRepository = _userRepository;
    }

    public Participant getUserById(String id) {
        return this._userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("The Id: " + id + " doesn't exist."));

    }

    public Participant getUserTrainerById(String id) {
        return this._userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("The Id: " + id + " doesn't exist."));
    }

    public List<Participant> getAll() {
        return this._userRepository.getAll();
    }
    public Page<Participant> getAll(Pageable pageable,Criteria criteria) {
        return this._userRepository.getAll(pageable,criteria);
    }
    public List<Participant> matchers(Criteria criteria) {
        return this._userRepository.match(criteria);
    }

}
