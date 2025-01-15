CREATE TABLE IF NOT EXISTS roles(
    id text not null,
    roleName text not null,
    CONSTRAINT pk_roles PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS roles_permissions(
    role_id text not null,
    permission_id text not null,
    CONSTRAINT pk_roles_permissions PRIMARY KEY (role_id, permission_id)
);