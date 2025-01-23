CREATE TABLE IF NOT EXISTS contacts(
    id text not null,
    name text not null,
    relationship text not null,
    phone text not null,
    user_id text not null,
    CONSTRAINT fk_contact_on_user FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT pk_contacts PRIMARY KEY (id)
);