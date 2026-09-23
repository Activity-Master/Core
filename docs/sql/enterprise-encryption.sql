-- Apply to an existing ActivityMaster database before enabling enterprise encryption.
-- Restrict this table to application preparation and authorized key-administration roles.
CREATE TABLE IF NOT EXISTS security.enterpriseencryptionkey (
    keyid varchar(32) PRIMARY KEY CHECK (keyid ~ '^[0-9a-f]{32}$'),
    enterpriseid uuid NOT NULL REFERENCES dbo.enterprise(enterpriseid),
    keyreference varchar(1024) NOT NULL,
    wrappedkey varchar(4096) NOT NULL,
    active boolean NOT NULL
);
CREATE UNIQUE INDEX IF NOT EXISTS enterpriseencryptionkey_one_active
    ON security.enterpriseencryptionkey(enterpriseid) WHERE active;
CREATE INDEX IF NOT EXISTS enterpriseencryptionkey_enterprise
    ON security.enterpriseencryptionkey(enterpriseid);

