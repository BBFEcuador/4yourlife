CREATE TABLE IF NOT EXISTS statements (
    id TEXT PRIMARY KEY,
    training_id TEXT NOT NULL,
    participant_id TEXT NOT NULL,
    status VARCHAR(100) NOT NULL,
    course_level VARCHAR(100) NOT NULL,
    comment TEXT,
    CONSTRAINT fk_statements_training FOREIGN KEY (training_id) REFERENCES training (id) ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT fk_statements_participant FOREIGN KEY (participant_id) REFERENCES participants (id) ON UPDATE CASCADE ON DELETE RESTRICT
);

CREATE INDEX IF NOT EXISTS idx_statements_training_id ON statements (training_id);
CREATE INDEX IF NOT EXISTS idx_statements_participant_id ON statements (participant_id);