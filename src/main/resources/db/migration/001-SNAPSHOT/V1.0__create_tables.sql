CREATE SEQUENCE IF NOT EXISTS app_user_seq START WITH 1 INCREMENT BY 1;

CREATE SEQUENCE IF NOT EXISTS token_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE app_user
(
    id        INTEGER NOT NULL,
    firstname VARCHAR(255),
    lastname  VARCHAR(255),
    email     VARCHAR(255),
    password  VARCHAR(255),
    role      VARCHAR(255),
    CONSTRAINT pk_app_user PRIMARY KEY (id)
);

CREATE TABLE token
(
    id         INTEGER NOT NULL,
    token      VARCHAR(255),
    token_type VARCHAR(255),
    revoked    BOOLEAN NOT NULL,
    expired    BOOLEAN NOT NULL,
    user_id    INTEGER,
    CONSTRAINT pk_token PRIMARY KEY (id)
);

ALTER TABLE token
    ADD CONSTRAINT uc_token_token UNIQUE (token);

ALTER TABLE token
    ADD CONSTRAINT FK_TOKEN_ON_USER FOREIGN KEY (user_id) REFERENCES app_user (id);