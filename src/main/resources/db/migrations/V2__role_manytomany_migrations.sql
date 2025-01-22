CREATE TABLE IF NOT EXISTS participant_level(
    id text not null,
    roleName text not null,
    isStarted BOOLEAN DEFAULT false,
    CONSTRAINT pk_roles PRIMARY KEY (id)
);