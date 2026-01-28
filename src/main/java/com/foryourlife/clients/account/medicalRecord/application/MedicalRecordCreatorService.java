package com.foryourlife.clients.account.medicalRecord.application;

import com.foryourlife.clients.account.medicalRecord.domain.MedicalRecord;
import com.foryourlife.clients.account.medicalRecord.domain.MedicalRecordRepository;
import com.foryourlife.clients.account.medicalRecord.infrastructure.httpcontrollers.MedicalRecordRequest;
import com.foryourlife.clients.account.participant.domain.ParticipantRepository;
import com.foryourlife.shared.domain.exception.BaseException;
import net.datafaker.providers.base.Medical;
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

    public MedicalRecord createMedicalRecordByRequest(MedicalRecordRequest request) {

        var participant = participantRepository.findById(request.participantId).orElseThrow(
                () -> new BaseException("Participant not found", List.of("Participant with id " + request.participantId + " not found"))
        );

        var medicalRecordOpt = repository.findByParticipantId(request.participantId).orElseThrow(
                () -> new BaseException("", List.of("Participant with id " + request.participantId + " already has a medical record")
                )
        );
        MedicalRecord medicalRecord = new MedicalRecord(
                UUID.randomUUID().toString(),
                request.psychiatric_history_detail,
                request.medical_history_detail,
                request.medication_history_detail,
                participant
        );
        repository.save(medicalRecord);
        return medicalRecord;
    }
}
