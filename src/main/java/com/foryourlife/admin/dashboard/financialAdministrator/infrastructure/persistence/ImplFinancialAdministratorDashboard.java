package com.foryourlife.admin.dashboard.financialAdministrator.infrastructure.persistence;

import com.foryourlife.admin.dashboard.financialAdministrator.domain.FinancialAdministratorDashboard;
import com.foryourlife.admin.dashboard.financialAdministrator.domain.FinancialAdministratorDashboardRepository;
import com.foryourlife.admin.programs.training.domain.Training;
import com.foryourlife.admin.programs.training.domain.TrainingRepository;
import com.foryourlife.admin.sales.payments.cashDrawer.domain.PaymentMethodSummary;
import com.foryourlife.admin.sales.payments.payment.domain.Payment;
import com.foryourlife.admin.sales.payments.payment.domain.PaymentHistory;
import com.foryourlife.admin.sales.payments.payment.domain.PaymentRepository;
import com.foryourlife.admin.sales.payments.payment.domain.PaymentStatus;
import com.foryourlife.shared.domain.exception.BaseException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class ImplFinancialAdministratorDashboard implements FinancialAdministratorDashboardRepository {
    private final PaymentRepository paymentRepository;
    private final TrainingRepository trainingRepository;

    public ImplFinancialAdministratorDashboard(PaymentRepository paymentRepository, TrainingRepository trainingRepository) {
        this.paymentRepository = paymentRepository;
        this.trainingRepository = trainingRepository;
    }

    @Override
    @Transactional
    public FinancialAdministratorDashboard findByTrainingId(String trainingId) {
        Training training = trainingRepository.findById(trainingId).orElseThrow(
                () -> new BaseException(
                        "Training not found",
                        List.of("Training with id " + trainingId + " not found")
                )
        );

        var team = training.getOriginalTeam();
        if (team == null) {
            throw new BaseException(
                    "Equipo no encontrado",
                    List.of("Team for training with id " + trainingId + " not found")
            );
        }
        if (training.getOriginalTeam() == null) {
            return new FinancialAdministratorDashboard(
                    BigDecimal.valueOf(0),
                    List.of(),
                    List.of(),
                    BigDecimal.valueOf(0),
                    0,
                    BigDecimal.valueOf(0),
                    0
            );
        }

        List<Payment> payments = new ArrayList<>();
        var listPayments = paymentRepository.findAllBetweenDates(
                training.getStartDate().atStartOfDay().minusWeeks(1),
                training.getStartDate().atStartOfDay()
        );
        training.getOriginalTeam().getUsers().forEach(participant ->
                listPayments.stream()
                        .filter(payment -> payment.getParticipant() != null && payment.getParticipant().getId().equals(participant.getId()))
                        .forEach(payments::add)
        );

        AtomicInteger totalPendingPayments = new AtomicInteger(0);
        AtomicInteger totalCompletedPayments = new AtomicInteger(0);
        AtomicReference<BigDecimal> pendingPaymentsAmount = new AtomicReference<>(BigDecimal.ZERO);
        AtomicReference<BigDecimal> totalPaymentsAmount = new AtomicReference<>(BigDecimal.ZERO);

        for (Payment payment : payments) {
            BigDecimal totalPaid = payment.getPaymentshistory().stream()
                    .map(PaymentHistory::getAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            if (PaymentStatus.PENDING.equals(payment.getStatus())) {
                totalPendingPayments.incrementAndGet();
                BigDecimal remaining = payment.getTotal().subtract(totalPaid);
                totalPaymentsAmount.updateAndGet(v -> v.add(totalPaid));
                if (remaining.compareTo(BigDecimal.ZERO) > 0) {
                    pendingPaymentsAmount.updateAndGet(v -> v.add(remaining));
                }
            } else if (PaymentStatus.COMPLETED.equals(payment.getStatus())) {
                totalCompletedPayments.incrementAndGet();
                totalPaymentsAmount.updateAndGet(v -> v.add(totalPaid));
            }
        }

        Map<String, PaymentMethodSummary> paymentMethodMap = getPaymentMethodSummaryMap(payments);
        List<PaymentMethodSummary> paymentMethods = new ArrayList<>(paymentMethodMap.values());

        BigDecimal totalIncome = paymentMethods.stream()
                .map(PaymentMethodSummary::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new FinancialAdministratorDashboard(
                totalIncome,
                paymentMethods,
                null,
                pendingPaymentsAmount.get(),
                totalPendingPayments.get(),
                totalPaymentsAmount.get(),
                totalCompletedPayments.get()
        );
    }


    private static Map<String, PaymentMethodSummary> getPaymentMethodSummaryMap(List<Payment> payments) {

        Map<String, PaymentMethodSummary> summaryMap = new HashMap<>();
        Set<String> processedPayments = new HashSet<>();

        for (Payment payment : payments) {

            if (!processedPayments.add(payment.getId())) {
                continue;
            }

            for (var history : payment.getPaymentshistory()) {

                String methodId = history.getPaymentMethod().getId();

                PaymentMethodSummary summary = summaryMap.computeIfAbsent(
                        methodId,
                        k -> new PaymentMethodSummary(history.getPaymentMethod())
                );

                summary.addPayment(history.getAmount());
            }
        }
        return summaryMap;
    }

    @Override
    public ByteArrayOutputStream generateReport(String trainingId) {
        return null;
    }
}
