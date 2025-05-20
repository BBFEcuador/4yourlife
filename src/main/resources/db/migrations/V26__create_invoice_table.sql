CREATE TABLE IF NOT EXISTS invoices
(
    id text not null primary key,
    invoice_number text not null,
    invoice_date date not null,
    dataInvoice_id text not null,
    products jsonb not null,
    payment_id text not null,
    constraint fk_dataInvoice_on_invoices foreign key (dataInvoice_id) references data_invoices(id),
    constraint fk_payment_on_invoices foreign key (payment_id) references payments(id)
);