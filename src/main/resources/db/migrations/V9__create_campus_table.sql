CREATE TABLE IF NOT EXISTS campus
(
    id text not null,
    country text not null,
    city text not null,
    address text not null,
    phone text not null,
    CONSTRAINT pk_campus PRIMARY KEY (id)
);