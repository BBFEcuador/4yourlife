CREATE TABLE IF NOT EXISTS permissions (
    id TEXT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    description TEXT,
    isActive BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS roles_permissions (
    role_id TEXT NOT NULL,
    permission_id TEXT NOT NULL,
    PRIMARY KEY (role_id, permission_id),
    CONSTRAINT fk_role_on_roles_permissions FOREIGN KEY (role_id) REFERENCES admin_roles (id),
    CONSTRAINT fk_permission_on_roles_permissions FOREIGN KEY (permission_id) REFERENCES permissions (id)
);