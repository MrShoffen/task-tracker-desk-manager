--liquibase formatted sql


--changeset mrshoffen:2
CREATE TABLE IF NOT EXISTS tasks
(
    id          UUID PRIMARY KEY,
    name        VARCHAR(128) NOT NULL,
    description VARCHAR(500),
    created_at TIMESTAMP NOT NULL,
    user_id     UUID         NOT NULL,
    main_task_id UUID REFERENCES tasks (id) ON DELETE CASCADE,
    completed   BOOLEAN DEFAULT FALSE
);