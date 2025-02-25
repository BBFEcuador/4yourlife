ALTER TABLE admins_users
    ADD COLUMN isActive BOOLEAN NULL DEFAULT true;

    update admins_users set isActive = true;