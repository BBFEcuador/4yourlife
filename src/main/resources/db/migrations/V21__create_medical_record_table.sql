CREATE TABLE IF NOT EXISTS medical_records
(
    id text not null,
    psychiatric_history_detail text not null,
    medical_history_detail text not null,
    medication_history_detail text not null,
    participant_id text not null,
    CONSTRAINT fk_medical_record_on_participant FOREIGN KEY (participant_id) REFERENCES participants (id),
    CONSTRAINT pk_medical_records PRIMARY KEY (id)
);
