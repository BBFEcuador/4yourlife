CREATE TABLE IF NOT EXISTS client_modules(
    id text not null,
    hasFocus boolean not null,
    hasYour boolean not null,
    hasLife boolean not null,
    user_id text not null,
    CONSTRAINT fk_client_modules_on_user FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT pk_client_modules PRIMARY KEY (id)
    );