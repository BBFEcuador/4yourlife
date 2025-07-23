CREATE TABLE IF NOT EXISTS invitation(
    id text not null,
    token text not null unique,
    isAdmin BOOLEAN not null default false,
    isUsed BOOLEAN not null default false,
    users jsonb,
    quantity INTEGER not null,
    senderId text not null,
    enrolled jsonb not null,
    CONSTRAINT pk_invitation PRIMARY KEY (id)
);