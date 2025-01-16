CREATE TABLE IF NOT EXISTS users
(
        id text not null,
        email text not null,
        password text not null,
        phone text not null,
        name text not null,
        rol_id text,
        CONSTRAINT fk_users_on_rol FOREIGN KEY (rol_id) REFERENCES roles (id),
        CONSTRAINT pk_users PRIMARY KEY (id)
);