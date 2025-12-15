package com.foryourlife.admin.dashboard.operativeAssitantDashboard.infrastructure.http;

import com.foryourlife.admin.dashboard.operativeAssitantDashboard.application.OperativeAssistantDashboardService;
import com.foryourlife.admin.dashboard.operativeAssitantDashboard.domain.OperativeAssistantDashboard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("operative-assistant-dashboard")
public class OperativeAssistantDashboardController {
    @Autowired
    private OperativeAssistantDashboardService operativeAssistantDashboardService;

    @GetMapping("/{teamId}")
    public ResponseEntity<OperativeAssistantDashboard> getDashboardByTeamId(@PathVariable String teamId) {
        OperativeAssistantDashboard dashboard = operativeAssistantDashboardService.getDashboardByTeamId(teamId);
        return ResponseEntity.ok(dashboard);
    }

    @PostMapping("generate/{teamId}")
    public ResponseEntity<byte[]> generateReport(@PathVariable String teamId) {

        String timestamp = LocalDateTime.now().format(
                DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
        ));

        headers.set(
                HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=AdminFinan_" + timestamp + ".xlsx"
        );

        byte[] excelBytes = operativeAssistantDashboardService
                .generateReport(teamId)
                .toByteArray();

        return ResponseEntity
                .ok()
                .headers(headers)
                .body(excelBytes);
    }
}
