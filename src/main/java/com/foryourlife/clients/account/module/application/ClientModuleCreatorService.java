package com.foryourlife.clients.account.module.application;

import com.foryourlife.clients.account.module.domain.ClientModule;
import com.foryourlife.clients.account.module.domain.ClientModuleRepository;
import com.foryourlife.clients.account.participant.domain.ParticipantRepository;
import com.foryourlife.clients.account.participantLevel.domain.ParticipantLevel;
import com.foryourlife.clients.account.participantLevel.domain.ParticipantLevelRepository;
import com.foryourlife.shared.domain.bus.EventBus;
import com.foryourlife.shared.domain.exception.BaseException;
import com.foryourlife.shared.domain.level.CourseLevel;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientModuleCreatorService {
    private final ClientModuleRepository repository;
    private final ParticipantRepository participantRepository;
    private final ParticipantLevelRepository participantLevelRepository;
    private final EventBus bus;

    public ClientModuleCreatorService(ClientModuleRepository repository, ParticipantRepository participantRepository, ParticipantLevelRepository participantLevelRepository, EventBus bus) {
        this.repository = repository;
        this.participantRepository = participantRepository;
        this.participantLevelRepository = participantLevelRepository;
        this.bus = bus;
    }

    public void createClientModule(ClientModule module){
        this.repository.save(module);
        var participant = participantRepository.findById(module.getUser().getId()).orElseThrow(() -> new BaseException("Not user found with id "+module.getUser().getId(), List.of()));
        if (participant.getParticipantLevel().getCourseLevel() == CourseLevel.INIT && module.getHasFocus()) {
            Specification<ParticipantLevel> specification = (root, query, cb) -> cb.equal(root.get("courseLevel"), cb.literal(CourseLevel.FOCUS.name()));
            var participantLvl = participantLevelRepository.findOneByCriteria(specification).orElseThrow();
            participant.updateLvl(participantLvl);
            participantRepository.save(participant);
        }
        bus.publish(module.pullDomainEvents());
    }

}
