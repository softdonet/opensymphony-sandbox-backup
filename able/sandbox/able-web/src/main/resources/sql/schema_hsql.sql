--
-- For tracking database upgrades; increases by 1 with each new version
--
CREATE TABLE build (
build               BIGINT NOT NULL PRIMARY KEY
);

-- Make sure the schema is marked as up to date
INSERT INTO build VALUES (0);
