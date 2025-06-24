CREATE TABLE IF NOT EXISTS cash_drawers
(
    id text not null primary key,
    number text not null,
    isActive boolean not null,
    status text not null,
    opened_by_user_id text,
    closed_by_user_id text,
    startDate timestamp not null,
    closeDate timestamp,
    opening_balance numeric not null,
    closing_balance numeric,
    details text,
    created_at timestamp not null default now(),
    updated_at timestamp default current_timestamp,
    created_by_user text not null,
    CONSTRAINT fk_cash_drawer_opened_by_user_id
        FOREIGN KEY (opened_by_user_id)
        REFERENCES users (id),
    CONSTRAINT fk_cash_drawer_closed_by_user_id
        FOREIGN KEY (closed_by_user_id)
        REFERENCES users (id),
    CONSTRAINT fk_cash_drawer_created_by_user
        FOREIGN KEY (created_by_user)
        REFERENCES users (id)
);