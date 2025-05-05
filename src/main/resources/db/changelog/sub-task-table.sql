--liquibase formatted sql


--changeset mrshoffen:2
CREATE TABLE IF NOT EXISTS sub_tasks
(
    id           UUID PRIMARY KEY,
    name         VARCHAR(128) NOT NULL,
    description  VARCHAR(500),
    created_at   TIMESTAMP    NOT NULL,
    main_task_id UUID         NOT NULL REFERENCES main_tasks (id) ON DELETE CASCADE,
    completed    BOOLEAN DEFAULT FALSE,
    UNIQUE (main_task_id, name)
);