CREATE TABLE IF NOT EXISTS payments(
    id text not null primary key,
    products jsonb not null,
    discount_id text,
    note text,
    participant_id text not null,
    campus_id text not null,
    payments_history jsonb,
    total float not null,
    status text not null,
    created_at timestamp not null default now(),
    created_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_modified_date TIMESTAMP,
    created_by VARCHAR(50),
    last_modified_by VARCHAR(50),
    CONSTRAINT fk_discount_on_payment FOREIGN KEY (discount_id) REFERENCES product_discounts (id),
    CONSTRAINT fk_participant_on_payment FOREIGN KEY (participant_id) REFERENCES participants (id),
    CONSTRAINT fk_campus_on_payment FOREIGN KEY (campus_id) REFERENCES campus (id)
);