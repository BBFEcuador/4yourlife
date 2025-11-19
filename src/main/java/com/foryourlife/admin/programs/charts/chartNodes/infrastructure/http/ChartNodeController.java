package com.foryourlife.admin.programs.charts.chartNodes.infrastructure.http;

import com.foryourlife.admin.programs.charts.chartNodes.application.ChartNodeCommandService;
import com.foryourlife.admin.programs.charts.chartNodes.application.ChartNodeQueryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chart-nodes")
public class ChartNodeController {
    @Autowired
    private ChartNodeCommandService chartNodeCommandService;
    @Autowired
    private ChartNodeQueryService chartNodeQueryService;

    @PutMapping("update/{id}")
    public ResponseEntity<?> updateChartNode(@RequestBody @Valid ChartNodeRequest chartNodeRequest, @PathVariable("id") String id) {
        chartNodeCommandService.updateNode(id, chartNodeRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("delete/{id}")
    public ResponseEntity<?> deleteChartNode(@PathVariable("id") String id) {
        chartNodeCommandService.deleteNode(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
