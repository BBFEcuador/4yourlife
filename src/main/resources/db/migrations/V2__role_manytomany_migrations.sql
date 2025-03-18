CREATE TYPE course_level AS ENUM ('INIT','FOCUS', 'YOUR', 'LIFE','MASTER_LIFE','LIFE_2','LIFE_3','LIFE_GRADUATE');
CREATE TABLE IF NOT EXISTS participant_level(
    id text not null,
    roleName text not null,
    isStarted BOOLEAN DEFAULT false,
    courseLevel course_level not null,
    CONSTRAINT pk_participant_level PRIMARY KEY (id)
);