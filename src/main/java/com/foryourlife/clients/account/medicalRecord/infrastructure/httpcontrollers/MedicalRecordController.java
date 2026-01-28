package com.foryourlife.clients.account.medicalRecord.infrastructure.httpcontrollers;

import com.foryourlife.clients.account.medicalRecord.application.MedicalRecordCreatorService;
import com.foryourlife.clients.account.medicalRecord.application.MedicalRecordFinderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/medical-record")
public class MedicalRecordController {
    @Autowired
    private MedicalRecordCreatorService medicalRecordService;

    @Autowired
    private MedicalRecordFinderService medicalRecordFinderService;

    @GetMapping("{userId}")
    public ResponseEntity<?> getMedicalRecord(@RequestParam String userId) {
        var medicalRecord = medicalRecordFinderService.findByParticipant(userId);
        if (medicalRecord.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(medicalRecord, HttpStatus.OK);
    }

    @PostMapping("add")
    public ResponseEntity<?> addMedicalRecord(@RequestBody MedicalRecordRequest request) {
        medicalRecordService.createMedicalRecordByRequest(request);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
