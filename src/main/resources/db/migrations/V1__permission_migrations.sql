CREATE TABLE IF NOT EXISTS permissions(
    id text not null,
    permissionName text not null,
    CONSTRAINT pk_permissions PRIMARY KEY (id)
)