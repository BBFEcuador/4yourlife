package com.foryourlife.clients.account.medicalRecord.infrastructure.httpcontrollers;

import com.foryourlife.clients.account.medicalRecord.application.MedicalRecordCreatorService;
import com.foryourlife.clients.account.medicalRecord.application.MedicalRecordFinderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/medical-record")
public class MedicalRecordController {
    @Autowired
    private MedicalRecordCreatorService medicalRecordService;

    @Autowired
    private MedicalRecordFinderService medicalRecordFinderService;

    @GetMapping("{participantId}")
    public ResponseEntity<?> getMedicalRecord(@PathVariable String participantId) {
        var medicalRecord = medicalRecordFinderService.findByParticipant(participantId);
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

    @PutMapping("update/{id}")
    public ResponseEntity<?> updateMedicalRecord(@PathVariable String id,@RequestBody MedicalRecordUpdateRequest request) {
        medicalRecordService.updateMedicalRecordByRequest(id,request);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
