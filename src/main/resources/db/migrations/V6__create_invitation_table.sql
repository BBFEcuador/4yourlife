CREATE TABLE IF NOT EXISTS invitation(
    id text not null,
    token text not null unique,
    isAdmin BOOLEAN not null default false,
    isUsed BOOLEAN not null default false,
    users jsonb,
    quantity INTEGER not null,
    senderId text not null,
    enrolled jsonb not null,
    campus_id text not null,
    CONSTRAINT fk_invitation_on_campus FOREIGN KEY (campus_id) REFERENCES campus (id),
    CONSTRAINT pk_invitation PRIMARY KEY (id)
);