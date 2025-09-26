CREATE TABLE IF NOT EXISTS contacts(
    id text not null,
    name text not null,
    relationship text not null,
    phone text not null,
    user_id text not null,
    created_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_modified_date TIMESTAMP,
    created_by VARCHAR(50),
    last_modified_by VARCHAR(50),
    CONSTRAINT fk_contact_on_user FOREIGN KEY (user_id) REFERENCES participants (id),
    CONSTRAINT pk_contacts PRIMARY KEY (id)
);
CREATE TABLE IF NOT EXISTS phones(
    id text not null,
    phone text not null,
    user_id text not null,
    CONSTRAINT fk_phone_on_user FOREIGN KEY (user_id) REFERENCES participants (id),
    CONSTRAINT pk_phone PRIMARY KEY (id)
);