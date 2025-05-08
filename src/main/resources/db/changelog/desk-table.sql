--liquibase formatted sql


--changeset mrshoffen:2
CREATE TABLE IF NOT EXISTS desks
(
    id           UUID PRIMARY KEY      DEFAULT gen_random_uuid(),
    name         VARCHAR(128) NOT NULL,
    created_at   TIMESTAMP    NOT NULL,
    order_index  BIGINT       NOT NULL,
    user_id      UUID         NOT NULL,
    workspace_id UUID         NOT NULL,
    UNIQUE (workspace_id, name)
);