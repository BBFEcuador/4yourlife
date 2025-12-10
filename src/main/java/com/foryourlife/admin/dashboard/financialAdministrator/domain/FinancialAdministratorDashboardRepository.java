package com.foryourlife.admin.dashboard.financialAdministrator.domain;

public interface FinancialAdministratorDashboardRepository {
    FinancialAdministratorDashboard findByTrainingId(String trainingId);
}
