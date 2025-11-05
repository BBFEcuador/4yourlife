package com.foryourlife.clients.account.invitations.applications;

import com.foryourlife.clients.account.invitations.domain.EnrolledUsers;
import com.foryourlife.clients.account.invitations.domain.InvitationRepository;
import com.foryourlife.clients.account.promises.domain.PromiseRepository;
import com.foryourlife.shared.domain.bus.DomainEventSubscriber;
import com.foryourlife.shared.domain.events.UserCreated;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@DomainEventSubscriber({UserCreated.class})
public class ConsumedInvitationOnUserCreated {
    private final InvitationRepository repository;
    private final PromiseRepository promiseRepository;

    public ConsumedInvitationOnUserCreated(InvitationRepository repository, PromiseRepository promiseRepository) {
        this.repository = repository;
        this.promiseRepository = promiseRepository;
    }

    @Async
    @EventListener
    public void on(UserCreated event) {
        var token = repository.findByToken(event.getUser().getInvitationToken()).orElseThrow();
        token.consumeToken();
        var enrolledUserList = token.getUsers();
        enrolledUserList.add(new EnrolledUsers(event.getUser().getId(), LocalDate.now(), event.getUser().getName()));
        token.setUsers(enrolledUserList);
        if (!token.getAdmin()){
            var promise = promiseRepository.findLastByUserId(event.getUser().getId());
            if (promise.isPresent()) {
                promise.get().setAchievedCount(promise.get().getAchievedCount() + 1);
                promiseRepository.save(promise.get());
            }
        }
        repository.save(token);
    }
}
