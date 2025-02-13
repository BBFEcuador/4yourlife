CREATE TABLE IF NOT EXISTS profile_details
(
        id text not null,
        birthday TIMESTAMP not null,
        address text not null,
        occupation text not null,
        gender text not null,
        civilStatus text not null,
        dni text not null,
        city text not null,
        CONSTRAINT pk_profile_details PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS participants
(
        id text not null,
        email text not null unique,
        password text not null,
        phone text not null,
        name text not null,
        invitationToken text not null unique,
        participant_level_id text,
        profile_id text,
        isLingerer BOOLEAN,
        CONSTRAINT fk_users_on_rol FOREIGN KEY (participant_level_id) REFERENCES participant_level (id),
        CONSTRAINT fk_users_on_profile_details FOREIGN KEY (profile_id) REFERENCES profile_details (id),
        CONSTRAINT pk_users PRIMARY KEY (id)
);