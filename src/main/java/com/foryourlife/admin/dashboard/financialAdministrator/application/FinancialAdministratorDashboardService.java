package com.foryourlife.admin.dashboard.financialAdministrator.application;

import com.foryourlife.admin.dashboard.financialAdministrator.domain.FinancialAdministratorDashboard;
import com.foryourlife.admin.dashboard.financialAdministrator.domain.FinancialAdministratorDashboardRepository;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;

@Service
public class FinancialAdministratorDashboardService {
    private final FinancialAdministratorDashboardRepository repository;

    public FinancialAdministratorDashboardService(FinancialAdministratorDashboardRepository repository) {
        this.repository = repository;
    }

    public FinancialAdministratorDashboard getDashboard(String trainingId) {
        return repository.findByTrainingId(trainingId);
    }

    public ByteArrayOutputStream generateReport(String trainingId) {
        return repository.generateReport(trainingId);
    }
}

