CREATE TABLE IF NOT EXISTS cash_drawer_details
(
    id text not null primary key,
    payment_id text not null,
    payment_history_id text not null,
    cash_drawer_id text not null,
    created_at timestamp not null default now(),
    CONSTRAINT fk_payment_on_cash_drawer_details FOREIGN KEY (payment_id) REFERENCES payments (id),
    CONSTRAINT fk_cash_drawer_on_cash_drawer_details FOREIGN KEY (cash_drawer_id) REFERENCES cash_drawers (id)
);