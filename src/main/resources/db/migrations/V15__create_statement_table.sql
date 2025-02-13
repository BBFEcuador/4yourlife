CREATE TABLE IF NOT EXISTS statements(
    id text not null,
    statements int not null,
    registered int not null,
    paid int not null,
    isActive boolean not null,
    user_id text not null,
    training_id text not null,
    created_at timestamp not null DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp,
    CONSTRAINT fk_statements_on_user FOREIGN KEY (user_id) REFERENCES participants (id),
    CONSTRAINT fk_statements_on_training FOREIGN KEY (training_id) REFERENCES training (id),
    CONSTRAINT pk_statements PRIMARY KEY (id)
    );