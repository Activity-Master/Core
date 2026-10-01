-- Additive FSDM relationship scope. Does not rewrite or remove historical values.
ALTER TABLE party.involvedpartyxinvolvedpartyidentificationtype
    ADD COLUMN IF NOT EXISTS addressid uuid;
CREATE INDEX IF NOT EXISTS am_party_identification_address
    ON party.involvedpartyxinvolvedpartyidentificationtype
        (enterpriseid, involvedpartyid, addressid, involvedpartyidentificationtypeid);

-- Canonical address roles and reusable address-component relationships.
CREATE TABLE IF NOT EXISTS address.addresstype
(
    addresstypeid           UUID                        NOT NULL primary key,
    effectivefromdate             timestamp(6) with time zone NOT NULL DEFAULT now(),
    effectivetodate               timestamp(6) with time zone NOT NULL DEFAULT '2999-12-31 23:59:59.999+00',
    warehousecreatedtimestamp     timestamp(6) with time zone NOT NULL DEFAULT now(),
    warehousefromdate             DATE                        NOT NULL DEFAULT current_date,

    warehouselastupdatedtimestamp timestamp(6) with time zone NOT NULL DEFAULT now(),
    originalsourcesystemuniqueid  UUID                        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    addresstypedesc         character varying(255)      NOT NULL,
    addresstypename         character varying(100)      NOT NULL,
    activeflagid                  UUID                        NOT NULL,
    enterpriseid                  UUID                        NOT NULL,
    systemid                      UUID                        NOT NULL,
    originalsourcesystemid        UUID                        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000'
);
CREATE TABLE IF NOT EXISTS address.addresstypesecuritytoken
(
    addresstypesecuritytokenid UUID                        NOT NULL primary key,
    effectivefromdate                timestamp(6) with time zone NOT NULL DEFAULT now(),
    effectivetodate                  timestamp(6) with time zone NOT NULL DEFAULT '2999-12-31 23:59:59.999+00',
    warehousecreatedtimestamp        timestamp(6) with time zone NOT NULL DEFAULT now(),
    warehousefromdate                DATE                        NOT NULL DEFAULT current_date,

    warehouselastupdatedtimestamp    timestamp(6) with time zone NOT NULL DEFAULT now(),
    createallowed                    INTEGER                     NOT NULL,
    deleteallowed                    INTEGER                     NOT NULL,
    originalsourcesystemuniqueid     UUID                        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    readallowed                      INTEGER                     NOT NULL,
    updateallowed                    INTEGER                     NOT NULL,
    activeflagid                     UUID                        NOT NULL,
    enterpriseid                     UUID                        NOT NULL,
    originalsourcesystemid           UUID                        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    securitytokenid                  UUID                        NOT NULL,
    systemid                         UUID                        NOT NULL,
    addresstypeid              UUID                        NOT NULL
);
CREATE TABLE IF NOT EXISTS address.addressxaddress
(
    addressxaddressid           UUID                        NOT NULL primary key,
    effectivefromdate             timestamp(6) with time zone NOT NULL DEFAULT now(),
    effectivetodate               timestamp(6) with time zone NOT NULL DEFAULT '2999-12-31 23:59:59.999+00',
    warehousecreatedtimestamp     timestamp(6) with time zone NOT NULL DEFAULT now(),
    warehousefromdate             DATE                        NOT NULL DEFAULT current_date,

    warehouselastupdatedtimestamp timestamp(6) with time zone NOT NULL,
    originalsourcesystemuniqueid  UUID                        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    value                         varchar(200)                NOT NULL ,
    activeflagid                  UUID                        NOT NULL,
    enterpriseid                  UUID                        NOT NULL,
    systemid                      UUID                        NOT NULL,
    originalsourcesystemid        UUID                        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    classificationid              UUID                        NOT NULL,
    addressid                     UUID                        NOT NULL,
    componentaddressid                   UUID                        NOT NULL
);
CREATE TABLE IF NOT EXISTS address.addressxaddresssecuritytoken
(
    addressxaddresssecuritytokenid UUID                        NOT NULL primary key,
    effectivefromdate                timestamp(6) with time zone NOT NULL DEFAULT now(),
    effectivetodate                  timestamp(6) with time zone NOT NULL DEFAULT '2999-12-31 23:59:59.999+00',
    warehousecreatedtimestamp        timestamp(6) with time zone NOT NULL DEFAULT now(),
    warehousefromdate                DATE                        NOT NULL DEFAULT current_date,

    warehouselastupdatedtimestamp    timestamp(6) with time zone NOT NULL,
    createallowed                    INTEGER                     NOT NULL,
    deleteallowed                    INTEGER                     NOT NULL,
    originalsourcesystemuniqueid     UUID                        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    readallowed                      INTEGER                     NOT NULL,
    updateallowed                    INTEGER                     NOT NULL,
    activeflagid                     UUID                        NOT NULL,
    enterpriseid                     UUID                        NOT NULL,
    originalsourcesystemid           UUID                        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    securitytokenid                  UUID                        NOT NULL,
    systemid                         UUID                        NOT NULL,
    addressxaddressid              UUID                        NOT NULL
);
ALTER TABLE address.address ADD COLUMN IF NOT EXISTS addresstypeid uuid;
ALTER TABLE party.involvedpartyxinvolvedpartyidentificationtype ADD COLUMN IF NOT EXISTS addresstypeid uuid;
CREATE INDEX IF NOT EXISTS am_address_type_value ON address.address(enterpriseid, addresstypeid, value);
CREATE INDEX IF NOT EXISTS am_address_type_name ON address.addresstype(enterpriseid, addresstypename);
CREATE INDEX IF NOT EXISTS am_address_components ON address.addressxaddress(enterpriseid, addressid, componentaddressid);

-- Relationship identifiers are indexed; FSDM does not enforce foreign keys.
CREATE INDEX IF NOT EXISTS am_party_identification_addressid ON party.involvedpartyxinvolvedpartyidentificationtype(addressid);
CREATE INDEX IF NOT EXISTS am_party_identification_addresstypeid ON party.involvedpartyxinvolvedpartyidentificationtype(addresstypeid);
CREATE INDEX IF NOT EXISTS am_address_addresstypeid ON address.address(addresstypeid);
CREATE INDEX IF NOT EXISTS am_address_component_owner ON address.addressxaddress(addressid);
CREATE INDEX IF NOT EXISTS am_address_component_target ON address.addressxaddress(componentaddressid);
CREATE INDEX IF NOT EXISTS am_address_type_security_owner ON address.addresstypesecuritytoken(addresstypeid);
CREATE INDEX IF NOT EXISTS am_address_component_security_owner ON address.addressxaddresssecuritytoken(addressxaddressid);
