package com.foryourlife.clients.account.participantLevel.application;

import com.foryourlife.clients.account.participantLevel.domain.ParticipantLevel;
import com.foryourlife.clients.account.participantLevel.domain.ParticipantLevelRepository;
import com.foryourlife.clients.account.participant.domain.ParticipantRepository;
import com.foryourlife.shared.domain.bus.DomainEventSubscriber;
import com.foryourlife.shared.domain.events.ClientModulesUpdated;
import com.foryourlife.shared.domain.exception.BaseException;
import com.foryourlife.shared.domain.level.CourseLevel;
import org.springframework.context.event.EventListener;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@DomainEventSubscriber({ClientModulesUpdated.class})
@Service
public class UpdateParticipantLevelOnClientModuleUpdate {

    private final ParticipantRepository participantRepository;
    private final ParticipantLevelRepository participantLevelRepository;

    public UpdateParticipantLevelOnClientModuleUpdate(ParticipantRepository participantRepository, ParticipantLevelRepository participantLevelRepository) {
        this.participantRepository = participantRepository;
        this.participantLevelRepository = participantLevelRepository;
    }

    @Async
    @EventListener
    @Transactional
    public void on(ClientModulesUpdated event) {
        var participant = participantRepository.findById(event.getClientModule().getUser().getId()).orElseThrow(() -> new BaseException("Not user found with id "+event.getClientModule().getUser().getId(), List.of()));
        if (participant.getParticipantLevel().getCourseLevel() == CourseLevel.INIT && event.getClientModule().getHasFocus()) {
            Specification<ParticipantLevel> specification = (root, query, cb) -> cb.equal(root.get("courseLevel"), cb.literal(CourseLevel.FOCUS.name()));
            var participantLvl = participantLevelRepository.findOneByCriteria(specification).orElseThrow();
            participant.updateLvl(participantLvl);
            participantRepository.save(participant);
        }
    }
}
