CREATE TABLE task (
    id bigint NOT NULL,
    description varchar(200) NOT NULL,
    priority bigint,
    PRIMARY KEY (id));