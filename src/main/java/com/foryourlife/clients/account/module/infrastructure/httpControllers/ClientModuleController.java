package com.foryourlife.clients.account.module.infrastructure.httpControllers;

import com.foryourlife.clients.account.module.application.ClientModuleCreatorService;
import com.foryourlife.clients.account.module.application.ClientModuleFinderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/client-module")
public class ClientModuleController {
    @Autowired
    private ClientModuleCreatorService clientModuleService;

    @Autowired
    private ClientModuleFinderService clientModuleFinderService;

    @PostMapping("update")
    public ResponseEntity<?> updateClientModule(@RequestBody ClientModuleRequest request) {
        clientModuleService.createClientModule(request.toDomain());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("{userId}")
    public ResponseEntity<?> getClientModuleByUserId(@PathVariable String userId) {
        return new ResponseEntity<>(clientModuleFinderService.findByUserId(userId), HttpStatus.OK);
    }
}
