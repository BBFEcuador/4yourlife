CREATE TABLE IF NOT EXISTS participant_level(
    id text not null,
    roleName text not null,
    isStarted BOOLEAN DEFAULT false,
    courseLevel text not null,
    CONSTRAINT pk_participant_level PRIMARY KEY (id)
);