CREATE TABLE If NOT EXISTS master_life(
    id text not null primary key,
    isActive BOOLEAN not null default true,
    user_id text not null,
    CONSTRAINT fk_master_life_on_user FOREIGN KEY (user_id) REFERENCES users (id)
);
CREATE TABLE IF NOT EXISTS team_master_life
(
    team_id text not null,
    masterlife_id text not null,
    CONSTRAINT fk_masterlife_on_team FOREIGN KEY (masterlife_id) REFERENCES master_life (id),
    CONSTRAINT fk_team_on_masterlife FOREIGN KEY (team_id) REFERENCES teams (id),
    CONSTRAINT pk_team_master_life PRIMARY KEY (team_id, masterlife_id)
);