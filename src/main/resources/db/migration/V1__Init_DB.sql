CREATE TABLE IF NOT EXISTS post (
    id                          BIGSERIAL PRIMARY KEY,
    publishing_time             TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    author_id                   BIGINT NOT NULL,
    title                       VARCHAR(255) NOT NULL,
    text                        TEXT NOT NULL,
    is_delete                   BOOLEAN NOT NULL DEFAULT false
)