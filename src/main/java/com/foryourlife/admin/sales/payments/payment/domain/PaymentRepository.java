package com.foryourlife.admin.sales.payments.payment.domain;

import com.foryourlife.clients.account.participant.domain.Participant;
import com.foryourlife.shared.domain.criteria.Criteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface PaymentRepository {
    Payment save(Payment payment);
    Payment findById(String id);
    Optional<Payment> findFirstByParticipantIdAndStatusOrderByCreatedDateAsc(
            String participantId,
            PaymentStatus status
    );
    Page<Payment> findByParticipantId(String id, Pageable pageable);
    List<Payment> findByParticipantId(String id);
    Page<Payment> findAll(Pageable pageable);
    Page<Payment> findAll(Pageable pageable, Criteria criteria);
    String generatePdf(Payment payment);
    boolean existsByParticipantIdAndStatus(String participantId, PaymentStatus status);
    List<Payment> findAllByParticipantIn(Collection<Participant> participantIds);
    List<Payment> findAllBetweenDates(LocalDateTime startDate, LocalDateTime endDate);
}
