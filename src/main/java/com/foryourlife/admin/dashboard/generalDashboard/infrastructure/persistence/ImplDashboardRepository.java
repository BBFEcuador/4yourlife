package com.foryourlife.admin.dashboard.generalDashboard.infrastructure.persistence;

import com.foryourlife.admin.dashboard.generalDashboard.domain.Dashboard;
import com.foryourlife.admin.dashboard.generalDashboard.domain.DashboardRepository;
import com.foryourlife.admin.dashboard.generalDashboard.domain.MonthlyPaymentDashboard;
import com.foryourlife.admin.programs.teams.domain.TeamRepository;
import com.foryourlife.admin.programs.trainer.domain.TrainerRepository;
import com.foryourlife.admin.programs.training.domain.TrainingRepository;
import com.foryourlife.admin.sales.invoices.domain.InvoiceRepository;
import com.foryourlife.admin.sales.payments.payment.domain.PaymentRepository;

import java.util.List;

public class ImplDashboardRepository implements DashboardRepository {
    private final PaymentRepository paymentRepository;
    private final TrainingRepository trainingRepository;
    private final InvoiceRepository invoiceRepository;
    private final TeamRepository teamRepository;
    private final TrainerRepository trainerRepository;

    public ImplDashboardRepository(PaymentRepository paymentRepository, TrainingRepository trainingRepository, InvoiceRepository invoiceRepository, TeamRepository teamRepository, TrainerRepository trainerRepository) {
        this.paymentRepository = paymentRepository;
        this.trainingRepository = trainingRepository;
        this.invoiceRepository = invoiceRepository;
        this.teamRepository = teamRepository;
        this.trainerRepository = trainerRepository;
    }

    @Override
    public Dashboard getDashboard() {
        var teamsCount = teamRepository.countTeams();
        var trainersCount = trainerRepository.countTrainers();

        return null;
    }

    public List<MonthlyPaymentDashboard> buildPaymentsSection() {
        List<MonthlyPaymentDashboard> monthlyPayments;
        for (var i = 0;  i < 12; i++) {

        }
        return null;
    }
}
