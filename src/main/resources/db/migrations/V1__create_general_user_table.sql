CREATE TABLE IF NOT EXISTS users(
    id text not null,
    email text not null unique,
    password text not null,
    phone text not null,
    name1 text not null,
    name2 text ,
    lastname1 text not null,
    lastname2 text,
    name text not null,
    entityMap json not null,
    CONSTRAINT pk_users PRIMARY KEY (id)
);