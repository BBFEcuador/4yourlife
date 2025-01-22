CREATE TABLE IF NOT EXISTS users
(
        id text not null,
        email text not null,
        password text not null,
        phone text not null,
        name text not null,
        participant_level_id text,
        CONSTRAINT fk_users_on_rol FOREIGN KEY (participant_level_id) REFERENCES participant_level (id),
        CONSTRAINT pk_users PRIMARY KEY (id)
);