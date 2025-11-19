package com.foryourlife.admin.programs.charts.organizationChart.infrastructure.http;

import com.foryourlife.admin.programs.charts.organizationChart.application.OrganizationChartCommandService;
import com.foryourlife.admin.programs.charts.organizationChart.application.OrganizationChartQueryService;
import com.foryourlife.admin.programs.charts.organizationChart.domain.OrganizationChart;
import jakarta.validation.Valid;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/organization")
public class OrganizationalChartController {
    private final OrganizationChartQueryService organizationChartQueryService;

    private final OrganizationChartCommandService organizationChartCommandService;

    public OrganizationalChartController(OrganizationChartQueryService organizationChartQueryService, OrganizationChartCommandService organizationChartCommandService) {
        this.organizationChartQueryService = organizationChartQueryService;
        this.organizationChartCommandService = organizationChartCommandService;
    }

    @PostMapping("")
    public ResponseEntity<?> createOrganizationChart(@RequestBody @Valid OrganizationalChartRequest organizationalChartRequest) {
        organizationChartCommandService.saveOrganizationChart(organizationalChartRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("team/{teamId}")
    public ResponseEntity<List<OrganizationChart>> getOrganizationChart(@PathVariable("teamId") String teamId) {
        return new ResponseEntity<>(organizationChartQueryService.getOrganizationChartByTeamId(teamId), HttpStatus.OK);
    }

    @GetMapping("training/{trainingId}")
    public ResponseEntity<Map<String, Optional<OrganizationChart>>> getOrganizationChartByTrainingId(@PathVariable("trainingId") String trainingId) {
        return new ResponseEntity<>(Map.of("OrganizationChart", organizationChartQueryService.getOrganizationChartByTrainingId(trainingId)), HttpStatus.OK);
    }
}
