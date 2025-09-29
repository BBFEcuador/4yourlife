CREATE TABLE IF NOT EXISTS stores
(
    id text not null primary key,
    number text not null,
    address text not null,
    campus_id text not null,
    CONSTRAINT fk_campus_on_store FOREIGN KEY (campus_id) REFERENCES campus (id)
);

CREATE TABLE IF NOT EXISTS cash_boxes
(
    id text not null primary key,
    number text not null,
    isActive boolean not null,
    store_id text not null,
    created_at timestamp not null default now(),
    first_number_invoice numeric not null,
    created_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_modified_date TIMESTAMP,
    created_by VARCHAR(50),
    last_modified_by VARCHAR(50),
    CONSTRAINT fk_store_on_cash_box
    FOREIGN KEY (store_id)
    REFERENCES stores (id)
);

CREATE TABLE IF NOT EXISTS cash_drawers
(
    id text not null primary key,
    status text not null,
    opened_by_user_id text,
    closed_by_user_id text,
    startDate timestamp not null,
    closeDate timestamp,
    opening_balance numeric not null,
    closing_balance numeric,
    details text,
    cash_box_id text not null,
    pin text,
    created_at timestamp not null default now(),
    updated_at timestamp default current_timestamp,
    CONSTRAINT fk_cash_drawer_opened_by_user_id
        FOREIGN KEY (opened_by_user_id)
        REFERENCES users (id),
    CONSTRAINT fk_cash_drawer_closed_by_user_id
        FOREIGN KEY (closed_by_user_id)
        REFERENCES users (id),
    CONSTRAINT fk_cash_drawer_on_cash_box
    FOREIGN KEY (cash_box_id)
    REFERENCES cash_boxes (id)
);