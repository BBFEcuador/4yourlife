package com.foryourlife.admin.programs.charts.organizationChart.infrastructure.http;

import com.foryourlife.admin.programs.charts.organizationChart.application.OrganizationChartCommandService;
import com.foryourlife.admin.programs.charts.organizationChart.application.OrganizationChartQueryService;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<?> createOrganizationChart(OrganizationalChartRequest organizationalChartRequest) {
        organizationChartCommandService.saveOrganizationChart(organizationalChartRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
