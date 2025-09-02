CREATE TABLE IF NOT EXISTS campus
(
    id text not null,
    country text not null,
    city text not null,
    address text not null,
    phone text not null,
    CONSTRAINT pk_campus PRIMARY KEY (id)
);
CREATE TABLE IF NOT EXISTS campus_admins
(
    campus_id text not null,
    admin_id text not null,
    CONSTRAINT fk_campus FOREIGN KEY (campus_id) REFERENCES campus (id),
    CONSTRAINT fk_admin FOREIGN KEY (admin_id) REFERENCES admins_users (id),
    CONSTRAINT pk_campus_admins PRIMARY KEY (campus_id, admin_id)
);