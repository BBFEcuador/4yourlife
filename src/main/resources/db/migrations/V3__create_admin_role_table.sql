CREATE TABLE IF NOT EXISTS admin_roles
(
    id text not null,
    name text not null,
    type text not null,
    CONSTRAINT pk_admin_roles PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS admins_users
(
    id text not null,
    user_id text not null,
    role_id text not null,
    created_at timestamp not null,
    updated_at timestamp,
    isActive BOOLEAN not NULL DEFAULT true,
    CONSTRAINT fk_admins_on_admin_roles FOREIGN KEY (role_id) REFERENCES admin_roles (id),
    CONSTRAINT fk_admin_on_user FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT pk_admins PRIMARY KEY (id)
);