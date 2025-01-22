CREATE TABLE IF NOT EXISTS invitation(
    id text not null,
    token text not null unique,
    isAdmin BOOLEAN not null default false,
    isUsed BOOLEAN not null default false,
    user_id text,
    CONSTRAINT fk_invitation_on_user FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT pk_invitation PRIMARY KEY (id)
);