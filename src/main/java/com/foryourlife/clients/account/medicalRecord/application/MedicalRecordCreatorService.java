package com.foryourlife.clients.account.medicalRecord.application;

import com.foryourlife.clients.account.medicalRecord.domain.MedicalRecord;
import com.foryourlife.clients.account.medicalRecord.domain.MedicalRecordRepository;
import com.foryourlife.clients.account.medicalRecord.infrastructure.httpcontrollers.MedicalRecordRequest;
import com.foryourlife.clients.account.medicalRecord.infrastructure.httpcontrollers.MedicalRecordUpdateRequest;
import com.foryourlife.clients.account.participant.domain.ParticipantRepository;
import com.foryourlife.shared.domain.exception.BaseException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class MedicalRecordCreatorService {
    private final MedicalRecordRepository repository;
    private final ParticipantRepository participantRepository;

    public MedicalRecordCreatorService(MedicalRecordRepository repository, ParticipantRepository participantRepository) {
        this.repository = repository;
        this.participantRepository = participantRepository;
    }

    public void createMedicalRecord(MedicalRecord medicalRecord) {
        repository.save(medicalRecord);
    }

    public void createMedicalRecordByRequest(MedicalRecordRequest request) {

        var participant = participantRepository.findById(request.participantId).orElseThrow(
                () -> new BaseException("Participant not found", List.of("Participant with id " + request.participantId + " not found"))
        );

        if(repository.findByParticipantId(request.participantId).isPresent()){
            throw new BaseException("El record medico ya existe", List.of("Ya existe un record medico para el participante con id " + request.participantId));
        }

        MedicalRecord medicalRecord = new MedicalRecord(
                UUID.randomUUID().toString(),
                request.psychiatric_history_detail,
                request.medical_history_detail,
                request.medication_history_detail,
                participant
        );

        repository.save(medicalRecord);
    }

    public void updateMedicalRecordByRequest(String id, MedicalRecordUpdateRequest request) {

        var medicalRecordOptional = repository.findByMedicalRecordId(id).orElseThrow(
                () -> new BaseException("Medical record not found", List.of("Medical record with id " + id + " not found"))
        );


        medicalRecordOptional.setMedical_history_detail(request.medical_history_detail);
        medicalRecordOptional.setMedication_history_detail(request.medication_history_detail);
        medicalRecordOptional.setPsychiatric_history_detail(request.psychiatric_history_detail);

        repository.save(medicalRecordOptional);

    }
}
