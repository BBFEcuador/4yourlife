CREATE TABLE IF NOT EXISTS product_discounts(
    id text not null primary key,
    name text not null,
    discount_type text not null,
    discount_value float not null,
    need_supervision BOOLEAN not null,
    is_active BOOLEAN not null
);