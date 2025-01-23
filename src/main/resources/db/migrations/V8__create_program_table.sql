CREATE TABLE IF NOT EXISTS programs
(
    id varchar not null,
    name varchar not null,
    level integer not null,
    CONSTRAINT pk_admin_programs PRIMARY KEY (id)
);