-- PostgreSQL 15+: exact encrypted lookup headers, plus legacy/plaintext equality.
-- The expression must match EncryptedValuePredicate.LOOKUP_HEADER_PATTERN.
-- Non-unique: identification types can have historical or shared values.
CREATE INDEX IF NOT EXISTS am_party_identification_lookup_header
    ON party.involvedpartyxinvolvedpartyidentificationtype
    (enterpriseid, involvedpartyidentificationtypeid,
     (coalesce(regexp_substr(value, '^amenc:[12]:[A-Za-z0-9_-]+:[0-9a-f]{64}:'), '')));
CREATE INDEX IF NOT EXISTS am_party_identification_lookup_value
    ON party.involvedpartyxinvolvedpartyidentificationtype
    (enterpriseid, involvedpartyidentificationtypeid, value);
CREATE INDEX IF NOT EXISTS am_address_lookup_header
    ON address.address
    (enterpriseid, (coalesce(regexp_substr(value, '^amenc:[12]:[A-Za-z0-9_-]+:[0-9a-f]{64}:'), '')));
CREATE INDEX IF NOT EXISTS am_address_lookup_value
    ON address.address (enterpriseid, value);
