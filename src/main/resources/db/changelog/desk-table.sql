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

CREATE INDEX IF NOT EXISTS desks_workspace_id_id_idx ON desks (id, workspace_id);

CREATE INDEX IF NOT EXISTS desks_workspace_id_idx ON desks (workspace_id);