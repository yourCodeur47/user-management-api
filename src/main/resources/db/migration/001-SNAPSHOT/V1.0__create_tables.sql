CREATE SEQUENCE IF NOT EXISTS app_user_seq START WITH 1 INCREMENT BY 1;

CREATE SEQUENCE IF NOT EXISTS token_seq START WITH 1 INCREMENT BY 1;

CREATE SEQUENCE refresh_token_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE app_user
(
    id         INTEGER PRIMARY KEY,
    firstname  VARCHAR NOT NULL,
    lastname   VARCHAR NOT NULL,
    email      VARCHAR UNIQUE NOT NULL,
    password   VARCHAR NOT NULL,
    role       VARCHAR NOT NULL,
    enable     BOOLEAN NOT NULL
);

CREATE TABLE token
(
    id           INTEGER PRIMARY KEY,
    token        VARCHAR NOT NULL,
    expiration_date  TIMESTAMP NOT NULL,
    user_id      INTEGER,
    FOREIGN KEY (user_id) REFERENCES app_user(id)
);

CREATE TABLE refresh_token
(
    id            INTEGER PRIMARY KEY,
    token         VARCHAR NOT NULL,
    creation_date TIMESTAMP NOT NULL
);