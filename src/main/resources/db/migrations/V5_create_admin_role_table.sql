CREATE TABLE IF NOT EXISTS admin_roles
(
    id varchar not null,
    name varchar not null,
    type varchar not null,
    CONSTRAINT pk_roles PRIMARY KEY (id)
    );

CREATE TABLE IF NOT EXISTS admins
(
    id varchar not null,
    name varchar not null,
    email varchar not null,
    password varchar not null,
    role_id varchar not null,
    created_at timestamp not null,
    updated_at timestamp,
    CONSTRAINT fk_admins_on_admin_roles FOREIGN KEY (role_id) REFERENCES plan (id),
    CONSTRAINT pk_admins PRIMARY KEY (id)
);