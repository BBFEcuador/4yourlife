CREATE TABLE IF NOT EXISTS teams
(
    id text not null,
    name text not null,
    photo text not null,
    training_id text not null,
    training_number text not null,
    CONSTRAINT fk_trainingId FOREIGN KEY (training_id) REFERENCES training (id),
    CONSTRAINT pk_teams PRIMARY KEY (id)
);
CREATE TABLE IF NOT EXISTS team_users
(
    team_id text not null,
    users_id text not null,
    CONSTRAINT fk_team_on_user FOREIGN KEY (team_id) REFERENCES teams (id),
    CONSTRAINT fk_user_on_team FOREIGN KEY (users_id) REFERENCES users (id),
    CONSTRAINT pk_team_users PRIMARY KEY (team_id, users_id)
);
CREATE TABLE IF NOT EXISTS team_master_life
(
    team_id text not null,
    masterlife_id text not null,
    CONSTRAINT fk_masterlife_on_team FOREIGN KEY (masterlife_id) REFERENCES users (id),
    CONSTRAINT fk_team_on_masterlife FOREIGN KEY (team_id) REFERENCES teams (id),
    CONSTRAINT pk_team_master_life PRIMARY KEY (team_id, masterlife_id)
);