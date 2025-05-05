--liquibase formatted sql


--changeset mrshoffen:2
CREATE TABLE IF NOT EXISTS main_tasks
(
    id            UUID PRIMARY KEY,
    name          VARCHAR(128) NOT NULL,
    description   VARCHAR(500),
    created_at    TIMESTAMP    NOT NULL,
    user_id       UUID         NOT NULL,
    task_board_id UUID         NOT NULL,
    completed     BOOLEAN DEFAULT FALSE,
    UNIQUE (task_board_id, name)
);