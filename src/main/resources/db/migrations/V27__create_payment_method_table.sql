CREATE TABLE IF NOT EXISTS banks
(
    id text not null primary key,
    name text not null,
    number text not null,
    contificoId text,
    campus_id text not null,
    created_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_modified_date TIMESTAMP,
    created_by VARCHAR(50),
    last_modified_by VARCHAR(50),
    CONSTRAINT fk_payments_on_campus FOREIGN KEY (campus_id) REFERENCES campus (id)
);

CREATE TABLE IF NOT EXISTS sri_payment_methods
(
    id text not null primary key,
    method text not null,
    name text not null,
    code text not null
);

CREATE TABLE IF NOT EXISTS payment_methods
(
    id text not null primary key,
    type text not null,
    isActive boolean not null,
    code text not null,
    campus_id text not null,
    bank_id text,
    created_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_modified_date TIMESTAMP,
    created_by VARCHAR(50),
    last_modified_by VARCHAR(50),
    CONSTRAINT fk_payment_methods_on_campus FOREIGN KEY (campus_id) REFERENCES campus (id),
    CONSTRAINT fk_payment_methods_on_bank FOREIGN KEY (bank_id) REFERENCES banks (id)
);