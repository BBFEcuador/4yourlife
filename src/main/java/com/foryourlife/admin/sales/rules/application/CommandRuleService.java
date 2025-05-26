package com.foryourlife.admin.sales.rules.application;

import com.foryourlife.admin.sales.rules.domain.Rule;
import com.foryourlife.admin.sales.rules.domain.RuleRepository;
import com.foryourlife.clients.account.user.domain.Participant;
import com.foryourlife.clients.account.user.domain.UserRepository;
import com.foryourlife.shared.domain.exception.BaseException;
import com.foryourlife.shared.domain.level.CourseLevel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommandRuleService {
    @Autowired
    private UserRepository participantRepository;

    private final RuleRepository _ruleRepository;

    public CommandRuleService(RuleRepository _ruleRepository) {
        this._ruleRepository = _ruleRepository;
    }

    public void save(Rule rule) {
        _ruleRepository.save(rule);
    }

    public void disable(String id) {
        _ruleRepository.disable(id);
    }

    public List<Rule> getApplicableRules(String userId, String productId) {
        Participant participant = participantRepository.findByUserId(userId);

        if (participant == null) {
            throw new BaseException("No se encontró participante para el usuario.", List.of("No se encontró participante con el id " + userId));
        }

        CourseLevel courseLevel = participant.getParticipantLevel().getCourseLevel();

        return _ruleRepository.findAllApplicableRule(productId, courseLevel);
    }
}
