CREATE TABLE IF NOT EXISTS configs_contifico
(
    id text not null primary key,
    apiKey text not null,
    apiSecret text not null,
    ruc text not null,
    campus_id text NOT NULL,
    CONSTRAINT fk_campus_on_products FOREIGN KEY (campus_id) REFERENCES campus (id)
);