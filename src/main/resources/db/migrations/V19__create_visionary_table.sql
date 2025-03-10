CREATE TABLE If NOT EXISTS visionaries(
    id text not null primary key,
    role text not null,
    user_id text not null,
    CONSTRAINT fk_staff_on_user FOREIGN KEY (user_id) REFERENCES users (id)
)