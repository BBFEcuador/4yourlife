CREATE TABLE IF NOT EXISTS invoices
(
    id text not null primary key,
    fullName text not null,
    address text not null,
    document text not null,
    phone text not null,
    email text not null,
    invoice_number text not null,
    invoice_date date not null,
    products jsonb not null,
    payment_id text not null,
    sent_sri boolean not null default false,
    created_at timestamp not null default now(),
    constraint fk_payment_on_invoices foreign key (payment_id) references payments(id)
);