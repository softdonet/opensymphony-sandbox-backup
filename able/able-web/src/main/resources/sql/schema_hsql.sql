--
-- For tracking database upgrades; increases by 1 with each new version
--
CREATE TABLE build (
id                  BIGINT NOT NULL PRIMARY KEY,
build               BIGINT NOT NULL,
tag                 VARCHAR,
description         VARCHAR
);

-- Make sure the schema is marked as up to date
INSERT INTO build VALUES (0, 0, 'initial', 'This is the initial build');
