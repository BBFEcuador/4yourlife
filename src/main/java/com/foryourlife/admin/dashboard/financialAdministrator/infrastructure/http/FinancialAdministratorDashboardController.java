package com.foryourlife.admin.dashboard.financialAdministrator.infrastructure.http;

import com.foryourlife.admin.dashboard.financialAdministrator.application.FinancialAdministratorDashboardService;
import com.foryourlife.admin.dashboard.financialAdministrator.domain.FinancialAdministratorDashboard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/financial-administrator-dashboard")
public class FinancialAdministratorDashboardController {
    @Autowired
    private FinancialAdministratorDashboardService financialAdministratorDashboardService;

    @GetMapping("/{trainingId}")
    public ResponseEntity<FinancialAdministratorDashboard> getDashboardByTrainingId(@PathVariable String trainingId) {
        FinancialAdministratorDashboard dashboard = financialAdministratorDashboardService.getDashboard(trainingId);
        return ResponseEntity.ok(dashboard);
    }
}
