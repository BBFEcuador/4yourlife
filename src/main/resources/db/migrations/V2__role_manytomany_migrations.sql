CREATE TABLE IF NOT EXISTS roles(
    id text not null,
    roleName text not null,
    CONSTRAINT pk_roles PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS roles_permissions(
    roleId text not null,
    permissionId text not null,
    CONSTRAINT pk_roles_permissions PRIMARY KEY (roleId, permissionId)
);