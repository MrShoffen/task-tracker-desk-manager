--liquibase formatted sql


--changeset mrshoffen:3
ALTER TABLE desks
    ADD COLUMN color VARCHAR(32) DEFAULT NULL;