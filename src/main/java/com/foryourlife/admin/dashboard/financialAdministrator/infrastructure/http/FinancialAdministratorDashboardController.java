package com.foryourlife.admin.dashboard.financialAdministrator.infrastructure.http;

import com.foryourlife.admin.dashboard.financialAdministrator.application.FinancialAdministratorDashboardService;
import com.foryourlife.admin.dashboard.financialAdministrator.domain.FinancialAdministratorDashboard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

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

    @PostMapping("generate/{trainingId}")
    public ResponseEntity<byte[]> generateReport(@PathVariable String trainingId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(
                MediaType.parseMediaType(
                        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
                )
        );
        headers.set(
                HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=ReporteAdministradorFinanciero" + LocalDateTime.now() + ".xlsx"
        );

        byte[] excelBytes = financialAdministratorDashboardService.generateReport(trainingId).toByteArray();

        return ResponseEntity
                .ok()
                .headers(headers)
                .body(excelBytes);
    }
}
