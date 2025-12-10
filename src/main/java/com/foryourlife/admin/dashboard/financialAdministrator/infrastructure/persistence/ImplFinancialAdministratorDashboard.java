package com.foryourlife.admin.dashboard.financialAdministrator.infrastructure.persistence;

import com.foryourlife.admin.dashboard.financialAdministrator.domain.FinancialAdministratorDashboard;
import com.foryourlife.admin.dashboard.financialAdministrator.domain.FinancialAdministratorDashboardRepository;
import com.foryourlife.admin.programs.training.domain.Training;
import com.foryourlife.admin.programs.training.domain.TrainingRepository;
import com.foryourlife.admin.sales.payments.cashDrawer.domain.PaymentMethodSummary;
import com.foryourlife.admin.sales.payments.payment.domain.Payment;
import com.foryourlife.admin.sales.payments.payment.domain.PaymentRepository;
import com.foryourlife.shared.domain.exception.BaseException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
public class ImplFinancialAdministratorDashboard implements FinancialAdministratorDashboardRepository {
    private final PaymentRepository paymentRepository;
    private final TrainingRepository trainingRepository;

    public ImplFinancialAdministratorDashboard(PaymentRepository paymentRepository, TrainingRepository trainingRepository) {
        this.paymentRepository = paymentRepository;
        this.trainingRepository = trainingRepository;
    }

    @Override
    public FinancialAdministratorDashboard findByTrainingId(String trainingId) {
        Training training = trainingRepository.findById(trainingId).orElseThrow(
                () -> new BaseException(
                        "Training not found",
                        List.of("Training with id " + trainingId + " not found")
                )
        );

        List<Payment> payments = new java.util.ArrayList<>(List.of());
        training.getOriginalTeam().getUsers().forEach(participant ->
                paymentRepository.findAllBetweenDates(training.getStartDate().atStartOfDay(), training.getNextLevel().getStartDate().atStartOfDay()).forEach(
                        payment -> {
                            if (payment.getParticipant().getId().equals(participant.getId())) {
                                payments.add(payment);
                            }
                        }
                )
        );

        Map<String, PaymentMethodSummary> paymentMethodMap = getPaymentMethodSummaryMap(payments);

        List<PaymentMethodSummary> paymentMethods = new ArrayList<>(paymentMethodMap.values());

        double totalIncome = paymentMethods.stream()
                .mapToDouble(PaymentMethodSummary::getTotalAmount)
                .sum();

        return new FinancialAdministratorDashboard(
                BigDecimal.valueOf(totalIncome),
                paymentMethods
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
}
