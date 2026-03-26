package com.foryourlife.admin.crm.statement.application;

import com.foryourlife.admin.crm.statement.domain.Statement;
import com.foryourlife.admin.crm.statement.domain.StatementDtoStatusEnum;
import com.foryourlife.admin.crm.statement.domain.StatementRepository;
import com.foryourlife.admin.crm.statement.domain.StatementStatusEnum;
import com.foryourlife.admin.crm.statement.infrastructure.http.StatementChangeStatusRequest;
import com.foryourlife.admin.sales.payments.payment.domain.Payment;
import com.foryourlife.admin.sales.payments.payment.domain.PaymentRepository;
import com.foryourlife.admin.sales.payments.payment.domain.PaymentStatus;
import com.foryourlife.shared.domain.exception.BaseException;
import com.foryourlife.shared.domain.level.CourseLevel;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StatementCommandService {
    private final StatementRepository statementRepository;
    private final PaymentRepository paymentRepository;

    public StatementCommandService(StatementRepository statementRepository, PaymentRepository paymentRepository) {
        this.statementRepository = statementRepository;
        this.paymentRepository = paymentRepository;
    }

    public void saveAll(List<Statement> statements) {

        var participants = statements.stream()
                .filter(statement -> statement.getParticipant() != null)
                .collect(java.util.stream.Collectors.toMap(Statement::getParticipant, Statement::getParticipant, (existing, replacement) -> existing));

        List<Payment> payments = paymentRepository.findAllByParticipantIn(participants.values());

        Map<String, List<Payment>> paymentsByParticipantId = payments.stream()
                .collect(Collectors.groupingBy(
                        p -> p.getParticipant().getUser().getId()
                ));

        statements.forEach(statement -> {

            var participant = statement.getParticipant();
            if (participant == null) return;

            String participantId = participant.getUser().getId();

            List<Payment> participantPayments = paymentsByParticipantId
                    .getOrDefault(participantId, List.of());

            // aquí ya trabajas con los pagos del participante

            boolean isConfirmed = participantPayments.stream()
                    .filter(p -> PaymentStatus.COMPLETED.equals(p.getStatus()))
                    .anyMatch(p -> hasMatchingCourseLevel(p, statement));

            if (isConfirmed) {
                statement.setStatus(StatementStatusEnum.CONFIRMED);
            }


        });
        statementRepository.saveAll(statements);
    }

    private boolean hasMatchingCourseLevel(Payment payment, Statement statement) {
        if (payment.getProducts() == null) return false;

        var targetLevel = statement.getTraining()
                .getNextLevel()
                .getCourseLevel();

        return payment.getProducts().stream()
                .filter(product -> product.getPrograms() != null)
                .anyMatch(product -> product.getPrograms().stream()
                        .anyMatch(program -> program.getCourseLevel().equals(targetLevel))
                );
    }


    public void changeStatementStatus(StatementChangeStatusRequest request, String id) {
        Statement statement = statementRepository.findById(id).orElseThrow(
                () -> new BaseException("Statement not found with id: " + id, List.of(""))
        );

        statement.setStatus(StatementStatusEnum.valueOf(request.getStatus()));
        statement.setComment(request.getComment());

        statementRepository.save(statement);
    }
}
