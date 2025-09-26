CREATE TABLE IF NOT EXISTS medical_records
(
    id text not null,
    psychiatric_history_detail text not null,
    medical_history_detail text not null,
    medication_history_detail text not null,
    participant_id text not null,
    created_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_modified_date TIMESTAMP,
    created_by VARCHAR(50),
    last_modified_by VARCHAR(50),
    CONSTRAINT fk_medical_record_on_participant FOREIGN KEY (participant_id) REFERENCES participants (id),
    CONSTRAINT pk_medical_records PRIMARY KEY (id)
);
