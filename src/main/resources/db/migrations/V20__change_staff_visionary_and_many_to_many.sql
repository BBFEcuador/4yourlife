CREATE TABLE IF NOT EXISTS team_staff
(
    team_id text not null,
    staff_id text not null,
    CONSTRAINT fk_team_on_staff FOREIGN KEY (team_id) REFERENCES teams (id),
    CONSTRAINT fk_staff_on_team FOREIGN KEY (staff_id) REFERENCES staffs (id),
    CONSTRAINT pk_team_staff PRIMARY KEY (team_id, staff_id)
);

CREATE TABLE IF NOT EXISTS team_visionary
(
    team_id text not null,
    visionary_id text not null,
    CONSTRAINT fk_visionary_on_team FOREIGN KEY (visionary_id) REFERENCES visionaries (id),
    CONSTRAINT fk_team_on_visionary FOREIGN KEY (team_id) REFERENCES teams (id),
    CONSTRAINT pk_team_visionary PRIMARY KEY (team_id, visionary_id)
);