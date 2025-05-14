CREATE TABLE IF NOT EXISTS data_invoices
(
    id text not null,
    fullName text not null,
    address text not null,
    document text not null,
    phone text not null,
    email text not null,
    user_id text not null,
    CONSTRAINT fk_data_invoices_on_users FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT pk_data_invoices PRIMARY KEY (id)
);
