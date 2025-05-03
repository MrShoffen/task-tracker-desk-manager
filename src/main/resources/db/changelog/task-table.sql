--liquibase formatted sql


--changeset mrshoffen:2
CREATE TABLE IF NOT EXISTS tasks
(
    id          UUID PRIMARY KEY,
    name        VARCHAR(128) NOT NULL,
    description VARCHAR(500),
    user_id     UUID         NOT NULL,
    parent_task UUID REFERENCES tasks (id) ON DELETE CASCADE
);