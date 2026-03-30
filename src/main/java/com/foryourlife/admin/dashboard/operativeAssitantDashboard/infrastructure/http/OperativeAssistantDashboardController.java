package com.foryourlife.admin.dashboard.operativeAssitantDashboard.infrastructure.http;

import com.foryourlife.admin.dashboard.operativeAssitantDashboard.application.OperativeAssistantDashboardService;
import com.foryourlife.admin.dashboard.operativeAssitantDashboard.domain.OperativeAssistantDashboard;
import com.foryourlife.admin.dashboard.operativeAssitantDashboard.domain.focus.OperativeFocusDashboard;
import com.foryourlife.admin.dashboard.operativeAssitantDashboard.domain.life.OperativeLifeDashboard;
import com.foryourlife.admin.dashboard.operativeAssitantDashboard.domain.your.OperativeYourDashboard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("operative-assistant-dashboard")
public class OperativeAssistantDashboardController {
    private final OperativeAssistantDashboardService operativeAssistantDashboardService;

    public OperativeAssistantDashboardController(OperativeAssistantDashboardService operativeAssistantDashboardService) {
        this.operativeAssistantDashboardService = operativeAssistantDashboardService;
    }

//    @PostMapping("generate/{teamId}")
//    public ResponseEntity<byte[]> generateReport(@PathVariable String teamId) {
//
//        String timestamp = LocalDateTime.now().format(
//                DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")
//        );
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.parseMediaType(
//                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
//        ));
//
//        headers.set(
//                HttpHeaders.CONTENT_DISPOSITION,
//                "attachment; filename=AdminFinan_" + timestamp + ".xlsx"
//        );
//
//        byte[] excelBytes = operativeAssistantDashboardService
//                .generateReport(teamId)
//                .toByteArray();
//
//        return ResponseEntity
//                .ok()
//                .headers(headers)
//                .body(excelBytes);
//    }

    @GetMapping("focus/{trainingId}")
    public ResponseEntity<OperativeFocusDashboard> getFocusDashboardByTrainingId(@PathVariable String trainingId) {
        return ResponseEntity.ok(operativeAssistantDashboardService.getFocusDashboardByTrainingId(trainingId));
    }

    @GetMapping("your/{trainingId}")
    public ResponseEntity<OperativeYourDashboard> getYourDashboardByTrainingId(@PathVariable String trainingId) {
        return ResponseEntity.ok(operativeAssistantDashboardService.getYourDashboardByTrainingId(trainingId));
    }

    @GetMapping("life/{trainingId}")
    public ResponseEntity<List<OperativeLifeDashboard>> getLifeDashboardByTrainingId(@PathVariable String trainingId) {
        return ResponseEntity.ok(operativeAssistantDashboardService.getLifeDashboardByTrainingId(trainingId));
    }
}
