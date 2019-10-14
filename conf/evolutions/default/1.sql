# --- !Ups

create table FILE_RECORD
(
    id         INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    name       VARCHAR(255),
    description text,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

# --- !Downs

DROP TABLE FILE_RECORD;