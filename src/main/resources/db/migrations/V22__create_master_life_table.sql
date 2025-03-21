CREATE TABLE If NOT EXISTS master_life(
    id text not null primary key,
    user_id text not null,
    CONSTRAINT fk_master_life_on_user FOREIGN KEY (user_id) REFERENCES users (id)
)