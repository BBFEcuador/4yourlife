package com.foryourlife.admin.sales.payments.payment.infrastructure.persistence;

import com.foryourlife.admin.sales.payments.payment.domain.Payment;
import com.foryourlife.admin.sales.payments.payment.domain.PaymentStatus;
import com.foryourlife.clients.account.participant.domain.Participant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface JPAPaymentRepository extends JpaRepository<Payment, String>, JpaSpecificationExecutor<Payment> {
    Page<Payment> findByParticipantId(String id, Pageable page);

    List<Payment> findByParticipantId(String id);

    boolean existsByParticipant_IdAndStatus(String participantId, PaymentStatus status);

    List<Payment> findAllByParticipantIn(Collection<Participant> participants);

    @EntityGraph(attributePaths = {"invoices"})
    Optional<Payment> findFirstByParticipant_IdAndStatusOrderByCreatedDateAsc(String participantId, PaymentStatus status);

    List<Payment> findAllByCreatedDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    List<Payment> findAllByTraining_Id(String trainingId);
}
