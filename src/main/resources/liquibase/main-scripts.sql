-- liquibase formatted sql

-- changeSet ivan:1

CREATE TABLE site_user
(
    user_id         SERIAL PRIMARY KEY,
    user_first_name TEXT,
    user_last_name  TEXT,
    user_phone      TEXT,
    user_email      TEXT,
    user_password   TEXT
);