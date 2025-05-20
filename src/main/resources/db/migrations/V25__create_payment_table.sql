CREATE TABLE IF NOT EXISTS payments(
    id text not null primary key,
    product_id text not null,
    discount_id text not null,
    participant_id text not null,
    campus_id text not null,
    payments_history jsonb not null,
    total float not null,
    status BOOLEAN not null,
    CONSTRAINT fk_product_on_payment FOREIGN KEY (product_id) REFERENCES products (id),
    CONSTRAINT fk_discount_on_payment FOREIGN KEY (discount_id) REFERENCES product_discounts (id),
    CONSTRAINT fk_participant_on_payment FOREIGN KEY (participant_id) REFERENCES participants (id),
    CONSTRAINT fk_campus_on_payment FOREIGN KEY (campus_id) REFERENCES campus (id)
);