package com.foryourlife.admin.dashboard.operativeAssitantDashboard.infrastructure.http;

import com.foryourlife.admin.dashboard.operativeAssitantDashboard.application.OperativeAssistantDashboardService;
import com.foryourlife.admin.dashboard.operativeAssitantDashboard.domain.OperativeAssistantDashboard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
