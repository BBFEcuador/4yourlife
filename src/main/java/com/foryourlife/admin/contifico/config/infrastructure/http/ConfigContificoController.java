package com.foryourlife.admin.contifico.config.infrastructure.http;

import com.foryourlife.admin.contifico.config.application.ConfigContificoCommandService;
import com.foryourlife.admin.contifico.config.application.ConfigContificoQueryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("config-contifico")
public class ConfigContificoController {
    @Autowired
    private ConfigContificoCommandService configContificoCommandService;
    @Autowired
    private ConfigContificoQueryService configContificoQueryService;

    @PostMapping("")
    public ResponseEntity<?> createConfigContifico(@Valid  @RequestBody ConfigContificoRequest configContificoRequest) {
        configContificoCommandService.createConfigContifico(configContificoRequest);
        return ResponseEntity.ok().build();
    }

    @GetMapping("")
    public ResponseEntity<?> getAllConfigContificos() {
        return ResponseEntity.ok(configContificoQueryService.getAllConfigContificos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getConfigContificoById(@PathVariable String id) {
        return ResponseEntity.ok(configContificoQueryService.findConfigContificoById(id));
    }

    @GetMapping("/campus/{campusId}")
    public ResponseEntity<?> getConfigContificoByCampusId(@PathVariable String campusId) {
        return ResponseEntity.ok(configContificoQueryService.findConfigContificoByCampusId(campusId));
    }

    @PostMapping("/manual")
    public ResponseEntity<?> createConfigContificoManual(@Valid  @RequestBody ConfigContificoRequest configContificoRequest) {
        configContificoCommandService.createConfigContifico(configContificoRequest);
        return ResponseEntity.ok().build();
    }
}
