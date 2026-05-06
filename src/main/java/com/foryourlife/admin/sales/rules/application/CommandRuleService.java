package com.foryourlife.admin.sales.rules.application;

import com.foryourlife.admin.sales.rules.domain.Rule;
import com.foryourlife.admin.sales.rules.domain.RuleContext;
import com.foryourlife.admin.sales.rules.domain.RuleRepository;
import com.foryourlife.clients.account.participant.domain.ParticipantRepository;
import com.foryourlife.masterLife.infrastructure.JPAMasterLifeRepository;
import com.foryourlife.shared.domain.exception.BaseException;
import com.foryourlife.shared.domain.level.CourseLevel;
import com.foryourlife.shared.domain.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommandRuleService {
    @Autowired
    private ParticipantRepository participantRepository;
    @Autowired
    private JPAMasterLifeRepository masterLifeRepository;

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

    public List<Rule> getApplicableRules(String userId, String productId, RuleContext context) {
        var participant = participantRepository.findByUserId(userId);
        if (participant.isEmpty()) {
            throw new BaseException("No se encontró participante para el usuario.",
                    List.of("No se encontró participante con el id " + userId));
        }

        var user = participant.get().getUser();
        CourseLevel courseLevel = participant.get().getParticipantLevel().getCourseLevel();

        List<Rule> rules = _ruleRepository.findAllByProductIdAndEnabledTrue(productId);

        return rules.stream()
                .filter(rule -> isRuleApplicable(rule, user, context, courseLevel))
                .toList();
    }

    public boolean isRuleApplicable(Rule rule, User user, RuleContext context, CourseLevel courseLevel) {
        return switch (rule.getRuleType()) {
            case MASTER -> masterLifeRepository.existsByUser_IdAndIsActiveIsTrue(user.getId());
            case EVENT -> context.isEvent();
            case DAY -> context.getDay().equalsIgnoreCase(rule.getDescription());
            case TIME_PERIOD -> context.getTimePeriod().equalsIgnoreCase(rule.getDescription());
            case WEEKEND -> context.getDay().equalsIgnoreCase("SÁBADO") || context.getDay().equalsIgnoreCase("DOMINGO");
            case TRAINING -> context.isDuringTraining();
            case PRE_ENTRY -> context.isPreEntry();
            case STANDARD -> true;
            default -> false;
        };
    }
}
