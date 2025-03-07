CREATE TABLE IF NOT EXISTS users(
    id text not null,
    email text not null unique,
    password text not null,
    phone text not null,
    name text not null,
    entityMap json not null,
    CONSTRAINT pk_users PRIMARY KEY (id)
);