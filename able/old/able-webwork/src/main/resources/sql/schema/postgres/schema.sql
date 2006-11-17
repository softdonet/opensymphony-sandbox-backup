CREATE TABLE build (
build               BIGINT NOT NULL PRIMARY KEY
);

CREATE SEQUENCE users_seq;
CREATE TABLE users (
user_id             BIGINT PRIMARY KEY DEFAULT NEXTVAL('users_seq'),
username            VARCHAR(32) NOT NULL,
name                VARCHAR(128),
email               VARCHAR(128) NOT NULL,
password_hash       VARCHAR(2048) NOT NULL,
creation_date       TIMESTAMP NOT NULL,
update_date         TIMESTAMP NOT NULL,
UNIQUE (username),
UNIQUE (email)
);

INSERT INTO build VALUES (1);
