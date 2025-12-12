package com.foryourlife.admin.dashboard.financialAdministrator.domain;

import java.io.ByteArrayOutputStream;

public interface FinancialAdministratorDashboardRepository {
    FinancialAdministratorDashboard findByTrainingId(String trainingId);
    ByteArrayOutputStream generateReport(String trainingId);
}
