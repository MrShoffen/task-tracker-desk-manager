--liquibase formatted sql


--changeset mrshoffen:2
CREATE TABLE IF NOT EXISTS desks
(
    id           UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name         VARCHAR(128) NOT NULL,
    description  VARCHAR(500),
    created_at   TIMESTAMP    NOT NULL,
    user_id      UUID         NOT NULL,
    workspace_id UUID         NOT NULL,
    UNIQUE (workspace_id, name)
);