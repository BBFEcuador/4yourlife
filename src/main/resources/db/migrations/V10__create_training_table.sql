CREATE TABLE IF NOT EXISTS training(
    id text not null,
    number integer not null,
    name text not null,
    startDate DATE not null,
    endDate DATE not null,
    courseLevel course_level not null,
    nextLevel text,
    campus_id text not null,
    state BOOLEAN not null,
    CONSTRAINT pk_training PRIMARY KEY (id),
    CONSTRAINT fk_training_on_campus FOREIGN KEY (campus_id) REFERENCES campus (id),
    CONSTRAINT fk_training_on_nextLevel FOREIGN KEY (nextLevel) REFERENCES training (id)
);