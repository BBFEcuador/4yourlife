package com.foryourlife.clients.account.invitations.applications;

import com.foryourlife.clients.account.invitations.domain.InvitationRepository;
import com.foryourlife.shared.domain.bus.DomainEventSubscriber;
import com.foryourlife.shared.domain.events.UserCreated;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@DomainEventSubscriber({UserCreated.class})
public class ConsumedInvitationOnUserCreated {
    private final InvitationRepository repository;

    public ConsumedInvitationOnUserCreated(InvitationRepository repository) {
        this.repository = repository;
    }

    @Async
    @EventListener
    public void on(UserCreated event){
        var token = repository.findByToken(event.getUser().getInvitationToken()).orElseThrow();
        token.consumeToken();
        token.setUsers(event.getUser());
        repository.save(token);
    }
}
