CREATE TABLE IF NOT EXISTS call_logs(
    id VARCHAR(100) PRIMARY KEY,
    called_user_id TEXT NOT NULL,
    called_by_user_id TEXT NOT NULL,
    call_start_time TIMESTAMP NOT NULL,
    call_end_time TIMESTAMP NOT NULL,
    type TEXT NOT NULL,
    status TEXT NOT NULL,
    notes TEXT,

    created_at timestamp not null default now(),
    created_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_modified_date TIMESTAMP,
    created_by VARCHAR(50),
    last_modified_by VARCHAR(50),

    CONSTRAINT fk_called_user FOREIGN KEY (called_user_id) REFERENCES users(id),
    CONSTRAINT fk_called_by_user FOREIGN KEY (called_by_user_id) REFERENCES users(id)
);