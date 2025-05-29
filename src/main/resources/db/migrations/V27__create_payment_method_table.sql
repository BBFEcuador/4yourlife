CREATE TABLE IF NOT EXISTS payment_methods
(
    id text not null primary key,
    type text not null,
    isActive boolean not null
);