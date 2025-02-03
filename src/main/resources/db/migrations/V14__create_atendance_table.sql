CREATE TABLE IF NOT EXISTS attendances (
    id text NOT NULL,
    fridayAttendance text,
    saturdayAttendance text,
    sundayAttendance text ,
    stage text NOT NULL,
    user_id text NOT NULL,
    training_id text NOT NULL,
    constraint fk_user_on_attendance foreign key (user_id) references users (id),
    constraint fk_training_on_attendance foreign key (training_id) references training (id),
    CONSTRAINT pk_attendances PRIMARY KEY (id)
);