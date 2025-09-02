CREATE TABLE If NOT EXISTS staffs(
    id text not null primary key,
    rol text not null,
    user_id text not null,
    isActive BOOLEAN not null DEFAULT TRUE,
    CONSTRAINT fk_staff_on_user FOREIGN KEY (user_id) REFERENCES users (id)
)