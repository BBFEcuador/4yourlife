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
    code text not null
);