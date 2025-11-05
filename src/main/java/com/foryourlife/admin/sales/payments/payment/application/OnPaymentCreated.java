package com.foryourlife.admin.sales.payments.payment.application;

import com.foryourlife.admin.sales.product.application.ProductFinderService;
import com.foryourlife.clients.account.invitations.applications.QueryInvitationServices;
import com.foryourlife.clients.account.module.application.ClientModuleCreatorService;
import com.foryourlife.clients.account.participant.domain.Participant;
import com.foryourlife.clients.account.participant.domain.ParticipantRepository;
import com.foryourlife.clients.account.participantLevel.application.ParticipantLevelService;
import com.foryourlife.clients.account.promises.domain.PromiseRepository;
import com.foryourlife.shared.domain.bus.DomainEventSubscriber;
import com.foryourlife.shared.domain.events.PaymentCreated;
import com.foryourlife.shared.domain.exception.BaseException;
import com.foryourlife.shared.domain.level.CourseLevel;
import jakarta.transaction.Transactional;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@DomainEventSubscriber(value = {PaymentCreated.class})
public class OnPaymentCreated {

    private final ProductFinderService productFinderService;
    private final ClientModuleCreatorService clientModuleCreatorService;
    private final ParticipantRepository participantRepository;
    private final ParticipantLevelService participantLevelRepository;
    private final QueryInvitationServices queryInvitationServices;
    private final PromiseRepository promiseRepository;

    public OnPaymentCreated(ProductFinderService productFinderService, ClientModuleCreatorService clientModuleCreatorService, ParticipantRepository participantRepository, ParticipantLevelService participantLevelRepository, QueryInvitationServices queryInvitationServices, PromiseRepository promiseRepository) {
        this.productFinderService = productFinderService;
        this.clientModuleCreatorService = clientModuleCreatorService;
        this.participantRepository = participantRepository;
        this.participantLevelRepository = participantLevelRepository;
        this.queryInvitationServices = queryInvitationServices;
        this.promiseRepository = promiseRepository;
    }

    @EventListener
    @Transactional
    public void on(PaymentCreated event) {
        Participant participant = participantRepository.findById(event.getPayment().getParticipant().getId()).orElseThrow(NullPointerException::new);
        event.getPayment().getProducts().forEach(product -> {

            var invitation = queryInvitationServices.findInvitationByToken(participant.getInvitationToken());

            var promiseOpt = promiseRepository.findLastByUserId(invitation.getSenderId());

            if (promiseOpt.isPresent()) {
                var promise = promiseOpt.get();

                LocalDate createdDate = participant.getCreatedDate().toLocalDate();
                LocalDate startDate = promise.getStartDate();
                LocalDate endDate = promise.getEndDate();

                if ((createdDate.isEqual(startDate) || createdDate.isAfter(startDate)) &&
                        (createdDate.isEqual(endDate) || createdDate.isBefore(endDate))) {

                    promise.setPaidCount(promise.getPaidCount() + 1);
                    promiseRepository.save(promise);
                }
            }
            var prod = productFinderService.findById(product.getId());

            prod.getPrograms().forEach(program -> {
                switch (program.getCourseLevel()) {
                    case FOCUS -> participant.getModules().setHasFocus(true);
                    case YOUR -> participant.getModules().setHasYour(true);
                    case LIFE -> participant.getModules().setHasLife(true);
                    default -> throw new BaseException("Invalid course level", List.of(""));
                }
            });
            if (event.getPayment().getParticipant().getParticipantLevel().getCourseLevel() == CourseLevel.INIT) {
                var p = event.getPayment().getParticipant();
                var pl = participantLevelRepository.getRolByLevel(CourseLevel.FOCUS);
                p.setParticipantLevel(pl);
                participantRepository.save(p);
            }
        });
        clientModuleCreatorService.createClientModule(participant.getModules());
    }
}