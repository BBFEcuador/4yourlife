CREATE TABLE IF NOT EXISTS promises
(
    id             TEXT NOT NULL PRIMARY KEY,
    first_promise  INT,
    second_promise INT,
    third_promise  INT,
    achieved_count INT,
    paid_count     INT,
    start_date     TIMESTAMP,
    end_date       TIMESTAMP,
    participant_id TEXT NOT NULL,
    training_id    TEXT NOT NULL,
    CONSTRAINT fk_participant_on_promises FOREIGN KEY (participant_id) REFERENCES participants (id),
    CONSTRAINT fk_training_on_promises FOREIGN KEY (training_id) REFERENCES training (id)
);