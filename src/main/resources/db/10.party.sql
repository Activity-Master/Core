CREATE SCHEMA party;
CREATE TABLE party.involvedparty
(
    involvedpartyid               UUID                        NOT NULL primary key,
    effectivefromdate             timestamp(6) with time zone NOT NULL DEFAULT now(),
    effectivetodate               timestamp(6) with time zone NOT NULL DEFAULT '2999-12-31 23:59:59.999+00',
    warehousecreatedtimestamp     timestamp(6) with time zone NOT NULL DEFAULT now(),
    warehousefromdate             DATE                        NOT NULL DEFAULT current_date,

    warehouselastupdatedtimestamp timestamp(6) with time zone NOT NULL DEFAULT now(),
    originalsourcesystemuniqueid  UUID                        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    activeflagid                  UUID                        NOT NULL,
    enterpriseid                  UUID                        NOT NULL,
    systemid                      UUID                        NOT NULL,
    originalsourcesystemid        UUID                        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000'
);
CREATE TABLE party.involvedpartyidentificationtype
(
    involvedpartyidentificationtypeid UUID                        NOT NULL primary key,
    effectivefromdate                 timestamp(6) with time zone NOT NULL DEFAULT now(),
    effectivetodate                   timestamp(6) with time zone NOT NULL DEFAULT '2999-12-31 23:59:59.999+00',
    warehousecreatedtimestamp         timestamp(6) with time zone NOT NULL DEFAULT now(),
    warehousefromdate                 DATE                        NOT NULL DEFAULT current_date,

    warehouselastupdatedtimestamp     timestamp(6) with time zone NOT NULL DEFAULT now(),
    originalsourcesystemuniqueid      UUID                        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    involvedpartyidentificationdesc   character varying(500)      NOT NULL,
    involvedpartyidentificationname   character varying(150)      NOT NULL,
    activeflagid                      UUID                        NOT NULL,
    enterpriseid                      UUID                        NOT NULL,
    systemid                          UUID                        NOT NULL,
    originalsourcesystemid            UUID                        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000'
);
CREATE TABLE party.involvedpartyidentificationtypesecuritytoken
(
    involvedpartyidentificationtypesecuritytokenid UUID                        NOT NULL primary key,
    effectivefromdate                              timestamp(6) with time zone NOT NULL DEFAULT now(),
    effectivetodate                                timestamp(6) with time zone NOT NULL DEFAULT '2999-12-31 23:59:59.999+00',
    warehousecreatedtimestamp                      timestamp(6) with time zone NOT NULL DEFAULT now(),
    warehousefromdate                              DATE                        NOT NULL DEFAULT current_date,

    warehouselastupdatedtimestamp                  timestamp(6) with time zone NOT NULL DEFAULT now(),
    createallowed                                  INTEGER                     NOT NULL,
    deleteallowed                                  INTEGER                     NOT NULL,
    originalsourcesystemuniqueid                   UUID                        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    readallowed                                    INTEGER                     NOT NULL,
    updateallowed                                  INTEGER                     NOT NULL,
    activeflagid                                   UUID                        NOT NULL,
    enterpriseid                                   UUID                        NOT NULL,
    originalsourcesystemid                         UUID                        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    securitytokenid                                UUID                        NOT NULL,
    systemid                                       UUID                        NOT NULL,
    involvedpartyidentificationtypeid              UUID                        NOT NULL
);
CREATE TABLE party.involvedpartynametype
(
    involvedpartynametypeid       UUID                        NOT NULL primary key,
    effectivefromdate             timestamp(6) with time zone NOT NULL DEFAULT now(),
    effectivetodate               timestamp(6) with time zone NOT NULL DEFAULT '2999-12-31 23:59:59.999+00',
    warehousecreatedtimestamp     timestamp(6) with time zone NOT NULL DEFAULT now(),
    warehousefromdate             DATE                        NOT NULL DEFAULT current_date,

    warehouselastupdatedtimestamp timestamp(6) with time zone NOT NULL DEFAULT now(),
    originalsourcesystemuniqueid  UUID                        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    involvedpartynametypedescr    character varying(500)      NOT NULL,
    involvedpartynametypename     character varying(500)      NOT NULL,
    activeflagid                  UUID                        NOT NULL,
    enterpriseid                  UUID                        NOT NULL,
    systemid                      UUID                        NOT NULL,
    originalsourcesystemid        UUID                        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000'
);
CREATE TABLE party.involvedpartynametypesecuritytoken
(
    involvedpartynametypesecuritytokenid UUID                        NOT NULL primary key,
    effectivefromdate                    timestamp(6) with time zone NOT NULL DEFAULT now(),
    effectivetodate                      timestamp(6) with time zone NOT NULL DEFAULT '2999-12-31 23:59:59.999+00',
    warehousecreatedtimestamp            timestamp(6) with time zone NOT NULL DEFAULT now(),
    warehousefromdate                    DATE                        NOT NULL DEFAULT current_date,

    warehouselastupdatedtimestamp        timestamp(6) with time zone NOT NULL DEFAULT now(),
    createallowed                        INTEGER                     NOT NULL,
    deleteallowed                        INTEGER                     NOT NULL,
    originalsourcesystemuniqueid         UUID                        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    readallowed                          INTEGER                     NOT NULL,
    updateallowed                        INTEGER                     NOT NULL,
    activeflagid                         UUID                        NOT NULL,
    enterpriseid                         UUID                        NOT NULL,
    originalsourcesystemid               UUID                        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    securitytokenid                      UUID                        NOT NULL,
    systemid                             UUID                        NOT NULL,
    involvedpartynametypeid              UUID                        NOT NULL
);
CREATE TABLE party.involvedpartynonorganic
(
    involvedpartynonorganicid     UUID                        NOT NULL primary key,
    effectivefromdate             timestamp(6) with time zone NOT NULL DEFAULT now(),
    effectivetodate               timestamp(6) with time zone NOT NULL DEFAULT '2999-12-31 23:59:59.999+00',
    warehousecreatedtimestamp     timestamp(6) with time zone NOT NULL DEFAULT now(),
    warehousefromdate             DATE                        NOT NULL DEFAULT current_date,

    warehouselastupdatedtimestamp timestamp(6) with time zone NOT NULL DEFAULT now(),
    originalsourcesystemuniqueid  UUID                        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    activeflagid                  UUID                        NOT NULL,
    enterpriseid                  UUID                        NOT NULL,
    systemid                      UUID                        NOT NULL,
    originalsourcesystemid        UUID                        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000'
);
CREATE TABLE party.involvedpartynonorganicsecuritytoken
(
    involvedpartynonorganicsecuritytokenid UUID                        NOT NULL primary key,
    effectivefromdate                      timestamp(6) with time zone NOT NULL DEFAULT now(),
    effectivetodate                        timestamp(6) with time zone NOT NULL DEFAULT '2999-12-31 23:59:59.999+00',
    warehousecreatedtimestamp              timestamp(6) with time zone NOT NULL DEFAULT now(),
    warehousefromdate                      DATE                        NOT NULL DEFAULT current_date,

    warehouselastupdatedtimestamp          timestamp(6) with time zone NOT NULL DEFAULT now(),
    createallowed                          INTEGER                     NOT NULL,
    deleteallowed                          INTEGER                     NOT NULL,
    originalsourcesystemuniqueid           UUID                        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    readallowed                            INTEGER                     NOT NULL,
    updateallowed                          INTEGER                     NOT NULL,
    activeflagid                           UUID                        NOT NULL,
    enterpriseid                           UUID                        NOT NULL,
    originalsourcesystemid                 UUID                        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    securitytokenid                        UUID                        NOT NULL,
    systemid                               UUID                        NOT NULL,
    involvedpartynonorganicid              UUID                        NOT NULL
);
CREATE TABLE party.involvedpartyorganic
(
    involvedpartyorganicid        UUID                        NOT NULL primary key,
    effectivefromdate             timestamp(6) with time zone NOT NULL DEFAULT now(),
    effectivetodate               timestamp(6) with time zone NOT NULL DEFAULT '2999-12-31 23:59:59.999+00',
    warehousecreatedtimestamp     timestamp(6) with time zone NOT NULL DEFAULT now(),
    warehousefromdate             DATE                        NOT NULL DEFAULT current_date,

    warehouselastupdatedtimestamp timestamp(6) with time zone NOT NULL DEFAULT now(),
    originalsourcesystemuniqueid  UUID                        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    activeflagid                  UUID                        NOT NULL,
    enterpriseid                  UUID                        NOT NULL,
    systemid                      UUID                        NOT NULL,
    originalsourcesystemid        UUID                        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000'
);
CREATE TABLE party.involvedpartyorganicsecuritytoken
(
    involvedpartyorganicsecuritytokenid UUID                        NOT NULL primary key,
    effectivefromdate                   timestamp(6) with time zone NOT NULL DEFAULT now(),
    effectivetodate                     timestamp(6) with time zone NOT NULL DEFAULT '2999-12-31 23:59:59.999+00',
    warehousecreatedtimestamp           timestamp(6) with time zone NOT NULL DEFAULT now(),
    warehousefromdate                   DATE                        NOT NULL DEFAULT current_date,

    warehouselastupdatedtimestamp       timestamp(6) with time zone NOT NULL DEFAULT now(),
    createallowed                       INTEGER                     NOT NULL,
    deleteallowed                       INTEGER                     NOT NULL,
    originalsourcesystemuniqueid        UUID                        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    readallowed                         INTEGER                     NOT NULL,
    updateallowed                       INTEGER                     NOT NULL,
    activeflagid                        UUID                        NOT NULL,
    enterpriseid                        UUID                        NOT NULL,
    originalsourcesystemid              UUID                        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    securitytokenid                     UUID                        NOT NULL,
    systemid                            UUID                        NOT NULL,
    involvedpartyorganicid              UUID                        NOT NULL
);
CREATE TABLE party.involvedpartyorganictype
(
    involvedpartyorganictypeid    UUID                        NOT NULL primary key,
    effectivefromdate             timestamp(6) with time zone NOT NULL DEFAULT now(),
    effectivetodate               timestamp(6) with time zone NOT NULL DEFAULT '2999-12-31 23:59:59.999+00',
    warehousecreatedtimestamp     timestamp(6) with time zone NOT NULL DEFAULT now(),
    warehousefromdate             DATE                        NOT NULL DEFAULT current_date,

    warehouselastupdatedtimestamp timestamp(6) with time zone NOT NULL DEFAULT now(),
    originalsourcesystemuniqueid  UUID                        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    involvedpartytypedesc         character varying(500)      NOT NULL,
    involvedpartytypename         character varying(200)      NOT NULL,
    activeflagid                  UUID                        NOT NULL,
    enterpriseid                  UUID                        NOT NULL,
    systemid                      UUID                        NOT NULL,
    originalsourcesystemid        UUID                        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000'
);
CREATE TABLE party.involvedpartyorganictypesecuritytoken
(
    involvedpartyorganictypesecuritytokenid UUID                        NOT NULL primary key,
    effectivefromdate                       timestamp(6) with time zone NOT NULL DEFAULT now(),
    effectivetodate                         timestamp(6) with time zone NOT NULL DEFAULT '2999-12-31 23:59:59.999+00',
    warehousecreatedtimestamp               timestamp(6) with time zone NOT NULL DEFAULT now(),
    warehousefromdate                       DATE                        NOT NULL DEFAULT current_date,

    warehouselastupdatedtimestamp           timestamp(6) with time zone NOT NULL DEFAULT now(),
    createallowed                           INTEGER                     NOT NULL,
    deleteallowed                           INTEGER                     NOT NULL,
    originalsourcesystemuniqueid            UUID                        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    readallowed                             INTEGER                     NOT NULL,
    updateallowed                           INTEGER                     NOT NULL,
    activeflagid                            UUID                        NOT NULL,
    enterpriseid                            UUID                        NOT NULL,
    originalsourcesystemid                  UUID                        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    securitytokenid                         UUID                        NOT NULL,
    systemid                                UUID                        NOT NULL,
    involvedpartyorganictypeid              UUID                        NOT NULL
);
CREATE TABLE party.involvedpartysecuritytoken
(
    involvedpartysecuritytokenid  UUID                        NOT NULL primary key,
    effectivefromdate             timestamp(6) with time zone NOT NULL DEFAULT now(),
    effectivetodate               timestamp(6) with time zone NOT NULL DEFAULT '2999-12-31 23:59:59.999+00',
    warehousecreatedtimestamp     timestamp(6) with time zone NOT NULL DEFAULT now(),
    warehousefromdate             DATE                        NOT NULL DEFAULT current_date,

    warehouselastupdatedtimestamp timestamp(6) with time zone NOT NULL DEFAULT now(),
    createallowed                 INTEGER                     NOT NULL,
    deleteallowed                 INTEGER                     NOT NULL,
    originalsourcesystemuniqueid  UUID                        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    readallowed                   INTEGER                     NOT NULL,
    updateallowed                 INTEGER                     NOT NULL,
    activeflagid                  UUID                        NOT NULL,
    enterpriseid                  UUID                        NOT NULL,
    originalsourcesystemid        UUID                        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    securitytokenid               UUID                        NOT NULL,
    systemid                      UUID                        NOT NULL,
    involvedpartyid               UUID                        NOT NULL
);
CREATE TABLE party.involvedpartytype
(
    involvedpartytypeid           UUID                        NOT NULL primary key,
    effectivefromdate             timestamp(6) with time zone NOT NULL DEFAULT now(),
    effectivetodate               timestamp(6) with time zone NOT NULL DEFAULT '2999-12-31 23:59:59.999+00',
    warehousecreatedtimestamp     timestamp(6) with time zone NOT NULL DEFAULT now(),
    warehousefromdate             DATE                        NOT NULL DEFAULT current_date,

    warehouselastupdatedtimestamp timestamp(6) with time zone NOT NULL DEFAULT now(),
    originalsourcesystemuniqueid  UUID                        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    involvedpartytypedesc         character varying(255)      NOT NULL,
    involvedpartytypename         character varying(100)      NOT NULL,
    activeflagid                  UUID                        NOT NULL,
    enterpriseid                  UUID                        NOT NULL,
    systemid                      UUID                        NOT NULL,
    originalsourcesystemid        UUID                        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000'
);
CREATE TABLE party.involvedpartytypesecuritytoken
(
    involvedpartytypesecuritytokenid UUID                        NOT NULL primary key,
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
    involvedpartytypeid              UUID                        NOT NULL
);
CREATE TABLE party.involvedpartyxaddress
(
    involvedpartyxaddressid       UUID                        NOT NULL primary key,
    effectivefromdate             timestamp(6) with time zone NOT NULL DEFAULT now(),
    effectivetodate               timestamp(6) with time zone NOT NULL DEFAULT '2999-12-31 23:59:59.999+00',
    warehousecreatedtimestamp     timestamp(6) with time zone NOT NULL DEFAULT now(),
    warehousefromdate             DATE                        NOT NULL DEFAULT current_date,

    warehouselastupdatedtimestamp timestamp(6) with time zone NOT NULL DEFAULT now(),
    originalsourcesystemuniqueid  UUID                        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    value                         varchar(200)                NOT NULL ,
    activeflagid                  UUID                        NOT NULL,
    enterpriseid                  UUID                        NOT NULL,
    systemid                      UUID                        NOT NULL,
    originalsourcesystemid        UUID                        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    classificationid              UUID                        NOT NULL,
    addressid                     UUID                        NOT NULL,
    involvedpartyid               UUID                        NOT NULL
);
CREATE TABLE party.involvedpartyxaddresssecuritytoken
(
    involvedpartyxaddresssecuritytokenid UUID                        NOT NULL primary key,
    effectivefromdate                    timestamp(6) with time zone NOT NULL DEFAULT now(),
    effectivetodate                      timestamp(6) with time zone NOT NULL DEFAULT '2999-12-31 23:59:59.999+00',
    warehousecreatedtimestamp            timestamp(6) with time zone NOT NULL DEFAULT now(),
    warehousefromdate                    DATE                        NOT NULL DEFAULT current_date,

    warehouselastupdatedtimestamp        timestamp(6) with time zone NOT NULL DEFAULT now(),
    createallowed                        INTEGER                     NOT NULL,
    deleteallowed                        INTEGER                     NOT NULL,
    originalsourcesystemuniqueid         UUID                        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    readallowed                          INTEGER                     NOT NULL,
    updateallowed                        INTEGER                     NOT NULL,
    activeflagid                         UUID                        NOT NULL,
    enterpriseid                         UUID                        NOT NULL,
    originalsourcesystemid               UUID                        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    securitytokenid                      UUID                        NOT NULL,
    systemid                             UUID                        NOT NULL,
    involvedpartyxaddressid              UUID                        NOT NULL
);
CREATE TABLE party.involvedpartyxclassification
(
    involvedpartyxclassificationid UUID                        NOT NULL primary key,
    effectivefromdate              timestamp(6) with time zone NOT NULL DEFAULT now(),
    effectivetodate                timestamp(6) with time zone NOT NULL DEFAULT '2999-12-31 23:59:59.999+00',
    warehousecreatedtimestamp      timestamp(6) with time zone NOT NULL DEFAULT now(),
    warehousefromdate              DATE                        NOT NULL DEFAULT current_date,

    warehouselastupdatedtimestamp  timestamp(6) with time zone NOT NULL DEFAULT now(),
    originalsourcesystemuniqueid   UUID                        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    value                          varchar(200)                NOT NULL ,
    activeflagid                   UUID                        NOT NULL,
    enterpriseid                   UUID                        NOT NULL,
    systemid                       UUID                        NOT NULL,
    originalsourcesystemid         UUID                        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    classificationid               UUID                        NOT NULL,
    involvedpartyid                UUID                        NOT NULL
);
CREATE TABLE party.involvedpartyxclassificationsecuritytoken
(
    involvedpartyxclassificationsecuritytokenid UUID                        NOT NULL primary key,
    effectivefromdate                           timestamp(6) with time zone NOT NULL DEFAULT now(),
    effectivetodate                             timestamp(6) with time zone NOT NULL DEFAULT '2999-12-31 23:59:59.999+00',
    warehousecreatedtimestamp                   timestamp(6) with time zone NOT NULL DEFAULT now(),
    warehousefromdate                           DATE                        NOT NULL DEFAULT current_date,

    warehouselastupdatedtimestamp               timestamp(6) with time zone NOT NULL DEFAULT now(),
    createallowed                               INTEGER                     NOT NULL,
    deleteallowed                               INTEGER                     NOT NULL,
    originalsourcesystemuniqueid                UUID                        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    readallowed                                 INTEGER                     NOT NULL,
    updateallowed                               INTEGER                     NOT NULL,
    activeflagid                                UUID                        NOT NULL,
    enterpriseid                                UUID                        NOT NULL,
    originalsourcesystemid                      UUID                        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    securitytokenid                             UUID                        NOT NULL,
    systemid                                    UUID                        NOT NULL,
    involvedpartyxclassificationid              UUID                        NOT NULL
);
CREATE TABLE party.involvedpartyxinvolvedparty
(
    involvedpartyxinvolvedpartyid UUID                        NOT NULL primary key,
    effectivefromdate             timestamp(6) with time zone NOT NULL DEFAULT now(),
    effectivetodate               timestamp(6) with time zone NOT NULL DEFAULT '2999-12-31 23:59:59.999+00',
    warehousecreatedtimestamp     timestamp(6) with time zone NOT NULL DEFAULT now(),
    warehousefromdate             DATE                        NOT NULL DEFAULT current_date,

    warehouselastupdatedtimestamp timestamp(6) with time zone NOT NULL DEFAULT now(),
    originalsourcesystemuniqueid  UUID                        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    value                         varchar(200)                NOT NULL ,
    activeflagid                  UUID                        NOT NULL,
    enterpriseid                  UUID                        NOT NULL,
    systemid                      UUID                        NOT NULL,
    originalsourcesystemid        UUID                        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    classificationid              UUID                        NOT NULL,
    childinvolvedpartyid          UUID                        NOT NULL,
    parentinvolvedpartyid         UUID                        NOT NULL
);
CREATE TABLE party.involvedpartyxinvolvedpartyidentificationtype
(
    involvedpartyxinvolvedpartyidentificationtypeid UUID                        NOT NULL primary key,
    effectivefromdate                               timestamp(6) with time zone NOT NULL DEFAULT now(),
    effectivetodate                                 timestamp(6) with time zone NOT NULL DEFAULT '2999-12-31 23:59:59.999+00',
    warehousecreatedtimestamp                       timestamp(6) with time zone NOT NULL DEFAULT now(),
    warehousefromdate                               DATE                        NOT NULL DEFAULT current_date,

    warehouselastupdatedtimestamp                   timestamp(6) with time zone NOT NULL DEFAULT now(),
    originalsourcesystemuniqueid                    UUID                        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    value                                           varchar(200)                NOT NULL ,
    activeflagid                                    UUID                        NOT NULL,
    enterpriseid                                    UUID                        NOT NULL,
    systemid                                        UUID                        NOT NULL,
    originalsourcesystemid                          UUID                        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    classificationid                                UUID                        NOT NULL,
    involvedpartyid                                 UUID                        NOT NULL,
    involvedpartyidentificationtypeid               UUID                        NOT NULL
);
CREATE TABLE party.involvedpartyxinvolvedpartyidentificationtypesecuritytoken
(
    involvedpartyxinvolvedpartyidentificationtypesecuritytokenid UUID                        NOT NULL primary key,
    effectivefromdate                                            timestamp(6) with time zone NOT NULL DEFAULT now(),
    effectivetodate                                              timestamp(6) with time zone NOT NULL DEFAULT '2999-12-31 23:59:59.999+00',
    warehousecreatedtimestamp                                    timestamp(6) with time zone NOT NULL DEFAULT now(),
    warehousefromdate                                            DATE                        NOT NULL DEFAULT current_date,

    warehouselastupdatedtimestamp                                timestamp(6) with time zone NOT NULL DEFAULT now(),
    createallowed                                                INTEGER                     NOT NULL,
    deleteallowed                                                INTEGER                     NOT NULL,
    originalsourcesystemuniqueid                                 UUID                        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    readallowed                                                  INTEGER                     NOT NULL,
    updateallowed                                                INTEGER                     NOT NULL,
    activeflagid                                                 UUID                        NOT NULL,
    enterpriseid                                                 UUID                        NOT NULL,
    originalsourcesystemid                                       UUID                        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    securitytokenid                                              UUID                        NOT NULL,
    systemid                                                     UUID                        NOT NULL,
    involvedpartyxinvolvedpartyidentificationtypeid              UUID                        NOT NULL
);
CREATE TABLE party.involvedpartyxinvolvedpartynametype
(
    involvedpartyxinvolvedpartynametypeid UUID                        NOT NULL primary key,
    effectivefromdate                     timestamp(6) with time zone NOT NULL DEFAULT now(),
    effectivetodate                       timestamp(6) with time zone NOT NULL DEFAULT '2999-12-31 23:59:59.999+00',
    warehousecreatedtimestamp             timestamp(6) with time zone NOT NULL DEFAULT now(),
    warehousefromdate                     DATE                        NOT NULL DEFAULT current_date,

    warehouselastupdatedtimestamp         timestamp(6) with time zone NOT NULL DEFAULT now(),
    originalsourcesystemuniqueid          UUID                        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    value                                 varchar(200)                NOT NULL ,
    activeflagid                          UUID                        NOT NULL,
    enterpriseid                          UUID                        NOT NULL,
    systemid                              UUID                        NOT NULL,
    originalsourcesystemid                UUID                        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    classificationid                      UUID                        NOT NULL,
    involvedpartyid                       UUID                        NOT NULL,
    involvedpartynametypeid               UUID                        NOT NULL
);
CREATE TABLE party.involvedpartyxinvolvedpartynametypesecuritytoken
(
    involvedpartyxinvolvedpartynametypesecuritytokenid UUID                        NOT NULL primary key,
    effectivefromdate                                  timestamp(6) with time zone NOT NULL DEFAULT now(),
    effectivetodate                                    timestamp(6) with time zone NOT NULL DEFAULT '2999-12-31 23:59:59.999+00',
    warehousecreatedtimestamp                          timestamp(6) with time zone NOT NULL DEFAULT now(),
    warehousefromdate                                  DATE                        NOT NULL DEFAULT current_date,

    warehouselastupdatedtimestamp                      timestamp(6) with time zone NOT NULL DEFAULT now(),
    createallowed                                      INTEGER                     NOT NULL,
    deleteallowed                                      INTEGER                     NOT NULL,
    originalsourcesystemuniqueid                       UUID                        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    readallowed                                        INTEGER                     NOT NULL,
    updateallowed                                      INTEGER                     NOT NULL,
    activeflagid                                       UUID                        NOT NULL,
    enterpriseid                                       UUID                        NOT NULL,
    originalsourcesystemid                             UUID                        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    securitytokenid                                    UUID                        NOT NULL,
    systemid                                           UUID                        NOT NULL,
    involvedpartyxinvolvedpartynametypeid              UUID                        NOT NULL
);
CREATE TABLE party.involvedpartyxinvolvedpartysecuritytoken
(
    involvedpartyxinvolvedpartysecuritytokenid UUID                        NOT NULL primary key,
    effectivefromdate                          timestamp(6) with time zone NOT NULL DEFAULT now(),
    effectivetodate                            timestamp(6) with time zone NOT NULL DEFAULT '2999-12-31 23:59:59.999+00',
    warehousecreatedtimestamp                  timestamp(6) with time zone NOT NULL DEFAULT now(),
    warehousefromdate                          DATE                        NOT NULL DEFAULT current_date,

    warehouselastupdatedtimestamp              timestamp(6) with time zone NOT NULL DEFAULT now(),
    createallowed                              INTEGER                     NOT NULL,
    deleteallowed                              INTEGER                     NOT NULL,
    originalsourcesystemuniqueid               UUID                        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    readallowed                                INTEGER                     NOT NULL,
    updateallowed                              INTEGER                     NOT NULL,
    activeflagid                               UUID                        NOT NULL,
    enterpriseid                               UUID                        NOT NULL,
    originalsourcesystemid                     UUID                        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    securitytokenid                            UUID                        NOT NULL,
    systemid                                   UUID                        NOT NULL,
    involvedpartyxinvolvedpartyid              UUID                        NOT NULL
);
CREATE TABLE party.involvedpartyxinvolvedpartytype
(
    involvedpartyxinvolvedpartytypeid UUID                        NOT NULL primary key,
    effectivefromdate                 timestamp(6) with time zone NOT NULL DEFAULT now(),
    effectivetodate                   timestamp(6) with time zone NOT NULL DEFAULT '2999-12-31 23:59:59.999+00',
    warehousecreatedtimestamp         timestamp(6) with time zone NOT NULL DEFAULT now(),
    warehousefromdate                 DATE                        NOT NULL DEFAULT current_date,

    warehouselastupdatedtimestamp     timestamp(6) with time zone NOT NULL DEFAULT now(),
    originalsourcesystemuniqueid      UUID                        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    value                             varchar(200)                NOT NULL ,
    activeflagid                      UUID                        NOT NULL,
    enterpriseid                      UUID                        NOT NULL,
    systemid                          UUID                        NOT NULL,
    originalsourcesystemid            UUID                        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    classificationid                  UUID                        NOT NULL,
    involvedpartyid                   UUID                        NOT NULL,
    involvedpartytypeid               UUID                        NOT NULL
);
CREATE TABLE party.involvedpartyxinvolvedpartytypesecuritytoken
(
    involvedpartyxinvolvedpartytypesecuritytokenid UUID                        NOT NULL primary key,
    effectivefromdate                              timestamp(6) with time zone NOT NULL DEFAULT now(),
    effectivetodate                                timestamp(6) with time zone NOT NULL DEFAULT '2999-12-31 23:59:59.999+00',
    warehousecreatedtimestamp                      timestamp(6) with time zone NOT NULL DEFAULT now(),
    warehousefromdate                              DATE                        NOT NULL DEFAULT current_date,

    warehouselastupdatedtimestamp                  timestamp(6) with time zone NOT NULL DEFAULT now(),
    createallowed                                  INTEGER                     NOT NULL,
    deleteallowed                                  INTEGER                     NOT NULL,
    originalsourcesystemuniqueid                   UUID                        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    readallowed                                    INTEGER                     NOT NULL,
    updateallowed                                  INTEGER                     NOT NULL,
    activeflagid                                   UUID                        NOT NULL,
    enterpriseid                                   UUID                        NOT NULL,
    originalsourcesystemid                         UUID                        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    securitytokenid                                UUID                        NOT NULL,
    systemid                                       UUID                        NOT NULL,
    involvedpartyxinvolvedpartytypeid              UUID                        NOT NULL
);
CREATE TABLE party.involvedpartyxproduct
(
    involvedpartyxproductid       UUID                        NOT NULL primary key,
    effectivefromdate             timestamp(6) with time zone NOT NULL DEFAULT now(),
    effectivetodate               timestamp(6) with time zone NOT NULL DEFAULT '2999-12-31 23:59:59.999+00',
    warehousecreatedtimestamp     timestamp(6) with time zone NOT NULL DEFAULT now(),
    warehousefromdate             DATE                        NOT NULL DEFAULT current_date,

    warehouselastupdatedtimestamp timestamp(6) with time zone NOT NULL DEFAULT now(),
    originalsourcesystemuniqueid  UUID                        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    value                         varchar(200)                NOT NULL ,
    activeflagid                  UUID                        NOT NULL,
    enterpriseid                  UUID                        NOT NULL,
    systemid                      UUID                        NOT NULL,
    originalsourcesystemid        UUID                        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    classificationid              UUID                        NOT NULL,
    involvedpartyid               UUID                        NOT NULL,
    productid                     UUID                        NOT NULL
);
CREATE TABLE party.involvedpartyxproductsecuritytoken
(
    involvedpartyxproductsecuritytokenid UUID                        NOT NULL primary key,
    effectivefromdate                    timestamp(6) with time zone NOT NULL DEFAULT now(),
    effectivetodate                      timestamp(6) with time zone NOT NULL DEFAULT '2999-12-31 23:59:59.999+00',
    warehousecreatedtimestamp            timestamp(6) with time zone NOT NULL DEFAULT now(),
    warehousefromdate                    DATE                        NOT NULL DEFAULT current_date,

    warehouselastupdatedtimestamp        timestamp(6) with time zone NOT NULL DEFAULT now(),
    createallowed                        INTEGER                     NOT NULL,
    deleteallowed                        INTEGER                     NOT NULL,
    originalsourcesystemuniqueid         UUID                        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    readallowed                          INTEGER                     NOT NULL,
    updateallowed                        INTEGER                     NOT NULL,
    activeflagid                         UUID                        NOT NULL,
    enterpriseid                         UUID                        NOT NULL,
    originalsourcesystemid               UUID                        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    securitytokenid                      UUID                        NOT NULL,
    systemid                             UUID                        NOT NULL,
    involvedpartyxproductid              UUID                        NOT NULL
);
CREATE TABLE party.involvedpartyxproducttype
(
    involvedpartyxproducttypeid   UUID                        NOT NULL primary key,
    effectivefromdate             timestamp(6) with time zone NOT NULL DEFAULT now(),
    effectivetodate               timestamp(6) with time zone NOT NULL DEFAULT '2999-12-31 23:59:59.999+00',
    warehousecreatedtimestamp     timestamp(6) with time zone NOT NULL DEFAULT now(),
    warehousefromdate             DATE                        NOT NULL DEFAULT current_date,

    warehouselastupdatedtimestamp timestamp(6) with time zone NOT NULL DEFAULT now(),
    originalsourcesystemuniqueid  UUID                        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    value                         varchar(200)                NOT NULL,
    activeflagid                  UUID                        NOT NULL,
    enterpriseid                  UUID                        NOT NULL,
    systemid                      UUID                        NOT NULL,
    originalsourcesystemid        UUID                        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    classificationid              UUID                        NOT NULL,
    involvedpartyid               UUID                        NOT NULL,
    producttypeid                 UUID                        NOT NULL
);
CREATE TABLE party.involvedpartyxproducttypesecuritytoken
(
    involvedpartyxproducttypesecuritytokenid UUID                        NOT NULL primary key,
    effectivefromdate                        timestamp(6) with time zone NOT NULL DEFAULT now(),
    effectivetodate                          timestamp(6) with time zone NOT NULL DEFAULT '2999-12-31 23:59:59.999+00',
    warehousecreatedtimestamp                timestamp(6) with time zone NOT NULL DEFAULT now(),
    warehousefromdate                        DATE                        NOT NULL DEFAULT current_date,

    warehouselastupdatedtimestamp            timestamp(6) with time zone NOT NULL DEFAULT now(),
    createallowed                            INTEGER                     NOT NULL,
    deleteallowed                            INTEGER                     NOT NULL,
    originalsourcesystemuniqueid             UUID                        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    readallowed                              INTEGER                     NOT NULL,
    updateallowed                            INTEGER                     NOT NULL,
    activeflagid                             UUID                        NOT NULL,
    enterpriseid                             UUID                        NOT NULL,
    originalsourcesystemid                   UUID                        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    securitytokenid                          UUID                        NOT NULL,
    systemid                                 UUID                        NOT NULL,
    involvedpartyxproducttypeid              UUID                        NOT NULL
);
CREATE TABLE party.involvedpartyxresourceitem
(
    involvedpartyxresourceitemid  UUID                        NOT NULL primary key,
    effectivefromdate             timestamp(6) with time zone NOT NULL DEFAULT now(),
    effectivetodate               timestamp(6) with time zone NOT NULL DEFAULT '2999-12-31 23:59:59.999+00',
    warehousecreatedtimestamp     timestamp(6) with time zone NOT NULL DEFAULT now(),
    warehousefromdate             DATE                        NOT NULL DEFAULT current_date,

    warehouselastupdatedtimestamp timestamp(6) with time zone NOT NULL DEFAULT now(),
    originalsourcesystemuniqueid  UUID                        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    value                         varchar(200)                NOT NULL ,
    activeflagid                  UUID                        NOT NULL,
    enterpriseid                  UUID                        NOT NULL,
    systemid                      UUID                        NOT NULL,
    originalsourcesystemid        UUID                        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    classificationid              UUID                        NOT NULL,
    involvedpartyid               UUID                        NOT NULL,
    resourceitemid                UUID                        NOT NULL
);
CREATE TABLE party.involvedpartyxresourceitemsecuritytoken
(
    involvedpartyxresourceitemsecuritytokenid UUID                        NOT NULL primary key,
    effectivefromdate                         timestamp(6) with time zone NOT NULL DEFAULT now(),
    effectivetodate                           timestamp(6) with time zone NOT NULL DEFAULT '2999-12-31 23:59:59.999+00',
    warehousecreatedtimestamp                 timestamp(6) with time zone NOT NULL DEFAULT now(),
    warehousefromdate                         DATE                        NOT NULL DEFAULT current_date,

    warehouselastupdatedtimestamp             timestamp(6) with time zone NOT NULL DEFAULT now(),
    createallowed                             INTEGER                     NOT NULL,
    deleteallowed                             INTEGER                     NOT NULL,
    originalsourcesystemuniqueid              UUID                        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    readallowed                               INTEGER                     NOT NULL,
    updateallowed                             INTEGER                     NOT NULL,
    activeflagid                              UUID                        NOT NULL,
    enterpriseid                              UUID                        NOT NULL,
    originalsourcesystemid                    UUID                        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    securitytokenid                           UUID                        NOT NULL,
    systemid                                  UUID                        NOT NULL,
    involvedpartyxresourceitemid              UUID                        NOT NULL
);
CREATE TABLE party.involvedpartyxrules
(
    involvedpartyxrulesid         UUID                        NOT NULL primary key,
    effectivefromdate             timestamp(6) with time zone NOT NULL DEFAULT now(),
    effectivetodate               timestamp(6) with time zone NOT NULL DEFAULT '2999-12-31 23:59:59.999+00',
    warehousecreatedtimestamp     timestamp(6) with time zone NOT NULL DEFAULT now(),
    warehousefromdate             DATE                        NOT NULL DEFAULT current_date,

    warehouselastupdatedtimestamp timestamp(6) with time zone NOT NULL DEFAULT now(),
    originalsourcesystemuniqueid  UUID                        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    value                         varchar(200)                NOT NULL ,
    activeflagid                  UUID                        NOT NULL,
    enterpriseid                  UUID                        NOT NULL,
    systemid                      UUID                        NOT NULL,
    originalsourcesystemid        UUID                        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    classificationid              UUID                        NOT NULL,
    involvedpartyid               UUID                        NOT NULL,
    rulesid                       UUID                        NOT NULL
);
CREATE TABLE party.involvedpartyxrulessecuritytoken
(
    involvedpartyxrulessecuritytokenid UUID                        NOT NULL primary key,
    effectivefromdate                  timestamp(6) with time zone NOT NULL DEFAULT now(),
    effectivetodate                    timestamp(6) with time zone NOT NULL DEFAULT '2999-12-31 23:59:59.999+00',
    warehousecreatedtimestamp          timestamp(6) with time zone NOT NULL DEFAULT now(),
    warehousefromdate                  DATE                        NOT NULL DEFAULT current_date,

    warehouselastupdatedtimestamp      timestamp(6) with time zone NOT NULL DEFAULT now(),
    createallowed                      INTEGER                     NOT NULL,
    deleteallowed                      INTEGER                     NOT NULL,
    originalsourcesystemuniqueid       UUID                        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    readallowed                        INTEGER                     NOT NULL,
    updateallowed                      INTEGER                     NOT NULL,
    activeflagid                       UUID                        NOT NULL,
    enterpriseid                       UUID                        NOT NULL,
    originalsourcesystemid             UUID                        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    securitytokenid                    UUID                        NOT NULL,
    systemid                           UUID                        NOT NULL,
    involvedpartyxrulesid              UUID                        NOT NULL
);
-- Indexes for party.involvedparty
CREATE INDEX idx_ip_eff_from ON party.involvedparty (effectivefromdate);
CREATE INDEX idx_ip_eff_to ON party.involvedparty (effectivetodate);
CREATE INDEX idx_ip_wh_created ON party.involvedparty (warehousecreatedtimestamp);
CREATE INDEX idx_ip_wh_updated ON party.involvedparty (warehouselastupdatedtimestamp);
CREATE INDEX idx_ip_ei_wh ON party.involvedparty (enterpriseid, warehousefromdate);
CREATE INDEX idx_ip_af_wh ON party.involvedparty (activeflagid, warehousefromdate);
CREATE INDEX idx_ip_sys_wh ON party.involvedparty (systemid, warehousefromdate);

-- Indexes for party.involvedpartyidentificationtype
CREATE INDEX idx_ipit_eff_from ON party.involvedpartyidentificationtype (effectivefromdate);
CREATE INDEX idx_ipit_eff_to ON party.involvedpartyidentificationtype (effectivetodate);
CREATE INDEX idx_ipit_wh_created ON party.involvedpartyidentificationtype (warehousecreatedtimestamp);
CREATE INDEX idx_ipit_wh_updated ON party.involvedpartyidentificationtype (warehouselastupdatedtimestamp);
CREATE INDEX idx_ipit_ei_wh ON party.involvedpartyidentificationtype (enterpriseid, warehousefromdate);
CREATE INDEX idx_ipit_af_wh ON party.involvedpartyidentificationtype (activeflagid, warehousefromdate);
CREATE INDEX idx_ipit_sys_wh ON party.involvedpartyidentificationtype (systemid, warehousefromdate);

-- Indexes for party.involvedpartyidentificationtypesecuritytoken
CREATE INDEX idx_ipitst_eff_from ON party.involvedpartyidentificationtypesecuritytoken (effectivefromdate);
CREATE INDEX idx_ipitst_eff_to ON party.involvedpartyidentificationtypesecuritytoken (effectivetodate);
CREATE INDEX idx_ipitst_wh_created ON party.involvedpartyidentificationtypesecuritytoken (warehousecreatedtimestamp);
CREATE INDEX idx_ipitst_wh_updated ON party.involvedpartyidentificationtypesecuritytoken (warehouselastupdatedtimestamp);
CREATE INDEX idx_ipitst_ei_wh ON party.involvedpartyidentificationtypesecuritytoken (enterpriseid, warehousefromdate);
CREATE INDEX idx_ipitst_af_wh ON party.involvedpartyidentificationtypesecuritytoken (activeflagid, warehousefromdate);
CREATE INDEX idx_ipitst_sid_wh ON party.involvedpartyidentificationtypesecuritytoken (involvedpartyidentificationtypeid, warehousefromdate);
CREATE INDEX idx_ipitst_sys_wh ON party.involvedpartyidentificationtypesecuritytoken (systemid, warehousefromdate);

-- Indexes for party.involvedpartynametype
CREATE INDEX idx_ipnt_eff_from ON party.involvedpartynametype (effectivefromdate);
CREATE INDEX idx_ipnt_eff_to ON party.involvedpartynametype (effectivetodate);
CREATE INDEX idx_ipnt_wh_created ON party.involvedpartynametype (warehousecreatedtimestamp);
CREATE INDEX idx_ipnt_wh_updated ON party.involvedpartynametype (warehouselastupdatedtimestamp);
CREATE INDEX idx_ipnt_ei_wh ON party.involvedpartynametype (enterpriseid, warehousefromdate);
CREATE INDEX idx_ipnt_af_wh ON party.involvedpartynametype (activeflagid, warehousefromdate);
CREATE INDEX idx_ipnt_sys_wh ON party.involvedpartynametype (systemid, warehousefromdate);

-- Indexes for party.involvedpartynametypesecuritytoken
CREATE INDEX idx_ipntst_eff_from ON party.involvedpartynametypesecuritytoken (effectivefromdate);
CREATE INDEX idx_ipntst_eff_to ON party.involvedpartynametypesecuritytoken (effectivetodate);
CREATE INDEX idx_ipntst_wh_created ON party.involvedpartynametypesecuritytoken (warehousecreatedtimestamp);
CREATE INDEX idx_ipntst_wh_updated ON party.involvedpartynametypesecuritytoken (warehouselastupdatedtimestamp);
CREATE INDEX idx_ipntst_ei_wh ON party.involvedpartynametypesecuritytoken (enterpriseid, warehousefromdate);
CREATE INDEX idx_ipntst_af_wh ON party.involvedpartynametypesecuritytoken (activeflagid, warehousefromdate);
CREATE INDEX idx_ipntst_sys_wh ON party.involvedpartynametypesecuritytoken (systemid, warehousefromdate);
CREATE INDEX idx_ipntst_tid_wh ON party.involvedpartynametypesecuritytoken (involvedpartynametypeid, warehousefromdate);

-- Indexes for party.involvedpartynonorganic
CREATE INDEX idx_ipno_eff_from ON party.involvedpartynonorganic (effectivefromdate);
CREATE INDEX idx_ipno_eff_to ON party.involvedpartynonorganic (effectivetodate);
CREATE INDEX idx_ipno_wh_created ON party.involvedpartynonorganic (warehousecreatedtimestamp);
CREATE INDEX idx_ipno_wh_updated ON party.involvedpartynonorganic (warehouselastupdatedtimestamp);
CREATE INDEX idx_ipno_ei_wh ON party.involvedpartynonorganic (enterpriseid, warehousefromdate);
CREATE INDEX idx_ipno_af_wh ON party.involvedpartynonorganic (activeflagid, warehousefromdate);
CREATE INDEX idx_ipno_sys_wh ON party.involvedpartynonorganic (systemid, warehousefromdate);

-- Indexes for party.involvedpartynonorganicsecuritytoken
CREATE INDEX idx_ipnost_eff_from ON party.involvedpartynonorganicsecuritytoken (effectivefromdate);
CREATE INDEX idx_ipnost_eff_to ON party.involvedpartynonorganicsecuritytoken (effectivetodate);
CREATE INDEX idx_ipnost_wh_created ON party.involvedpartynonorganicsecuritytoken (warehousecreatedtimestamp);
CREATE INDEX idx_ipnost_wh_updated ON party.involvedpartynonorganicsecuritytoken (warehouselastupdatedtimestamp);
CREATE INDEX idx_ipnost_ei_wh ON party.involvedpartynonorganicsecuritytoken (enterpriseid, warehousefromdate);
CREATE INDEX idx_ipnost_af_wh ON party.involvedpartynonorganicsecuritytoken (activeflagid, warehousefromdate);
CREATE INDEX idx_ipnost_sys_wh ON party.involvedpartynonorganicsecuritytoken (systemid, warehousefromdate);

-- Indexes for party.involvedpartyorganic
CREATE INDEX idx_ipo_eff_from ON party.involvedpartyorganic (effectivefromdate);
CREATE INDEX idx_ipo_eff_to ON party.involvedpartyorganic (effectivetodate);
CREATE INDEX idx_ipo_wh_created ON party.involvedpartyorganic (warehousecreatedtimestamp);
CREATE INDEX idx_ipo_wh_updated ON party.involvedpartyorganic (warehouselastupdatedtimestamp);
CREATE INDEX idx_ipo_ei_wh ON party.involvedpartyorganic (enterpriseid, warehousefromdate);
CREATE INDEX idx_ipo_af_wh ON party.involvedpartyorganic (activeflagid, warehousefromdate);
CREATE INDEX idx_ipo_sys_wh ON party.involvedpartyorganic (systemid, warehousefromdate);

-- Indexes for party.involvedpartyorganicsecuritytoken
CREATE INDEX idx_ipo_st_eff_from ON party.involvedpartyorganicsecuritytoken (effectivefromdate);
CREATE INDEX idx_ipo_st_eff_to ON party.involvedpartyorganicsecuritytoken (effectivetodate);
CREATE INDEX idx_ipo_st_wh_created ON party.involvedpartyorganicsecuritytoken (warehousecreatedtimestamp);
CREATE INDEX idx_ipo_st_wh_updated ON party.involvedpartyorganicsecuritytoken (warehouselastupdatedtimestamp);
CREATE INDEX idx_ipo_st_ei_wh ON party.involvedpartyorganicsecuritytoken (enterpriseid, warehousefromdate);
CREATE INDEX idx_ipo_st_af_wh ON party.involvedpartyorganicsecuritytoken (activeflagid, warehousefromdate);
CREATE INDEX idx_ipo_st_sys_wh ON party.involvedpartyorganicsecuritytoken (systemid, warehousefromdate);

-- Indexes for party.involvedpartyorganictype
CREATE INDEX idx_ipot_eff_from ON party.involvedpartyorganictype (effectivefromdate);
CREATE INDEX idx_ipot_eff_to ON party.involvedpartyorganictype (effectivetodate);
CREATE INDEX idx_ipot_wh_created ON party.involvedpartyorganictype (warehousecreatedtimestamp);
CREATE INDEX idx_ipot_wh_updated ON party.involvedpartyorganictype (warehouselastupdatedtimestamp);
CREATE INDEX idx_ipot_ei_wh ON party.involvedpartyorganictype (enterpriseid, warehousefromdate);
CREATE INDEX idx_ipot_af_wh ON party.involvedpartyorganictype (activeflagid, warehousefromdate);
CREATE INDEX idx_ipot_sys_wh ON party.involvedpartyorganictype (systemid, warehousefromdate);

-- Indexes for party.involvedpartyorganictypesecuritytoken
CREATE INDEX idx_ipots_eff_from ON party.involvedpartyorganictypesecuritytoken (effectivefromdate);
CREATE INDEX idx_ipots_eff_to ON party.involvedpartyorganictypesecuritytoken (effectivetodate);
CREATE INDEX idx_ipots_wh_created ON party.involvedpartyorganictypesecuritytoken (warehousecreatedtimestamp);
CREATE INDEX idx_ipots_wh_updated ON party.involvedpartyorganictypesecuritytoken (warehouselastupdatedtimestamp);
CREATE INDEX idx_ipots_ei_wh ON party.involvedpartyorganictypesecuritytoken (enterpriseid, warehousefromdate);
CREATE INDEX idx_ipots_af_wh ON party.involvedpartyorganictypesecuritytoken (activeflagid, warehousefromdate);
CREATE INDEX idx_ipots_sys_wh ON party.involvedpartyorganictypesecuritytoken (systemid, warehousefromdate);
CREATE INDEX idx_ipots_tid_wh ON party.involvedpartyorganictypesecuritytoken (involvedpartyorganictypeid, warehousefromdate);

-- Indexes for party.involvedpartysecuritytoken
CREATE INDEX idx_ips_eff_from ON party.involvedpartysecuritytoken (effectivefromdate);
CREATE INDEX idx_ips_eff_to ON party.involvedpartysecuritytoken (effectivetodate);
CREATE INDEX idx_ips_wh_created ON party.involvedpartysecuritytoken (warehousecreatedtimestamp);
CREATE INDEX idx_ips_wh_updated ON party.involvedpartysecuritytoken (warehouselastupdatedtimestamp);
CREATE INDEX idx_ips_ei_wh ON party.involvedpartysecuritytoken (enterpriseid, warehousefromdate);
CREATE INDEX idx_ips_af_wh ON party.involvedpartysecuritytoken (activeflagid, warehousefromdate);
CREATE INDEX idx_ips_sys_wh ON party.involvedpartysecuritytoken (systemid, warehousefromdate);

-- Indexes for party.involvedpartytype
CREATE INDEX idx_iptype_eff_from ON party.involvedpartytype (effectivefromdate);
CREATE INDEX idx_iptype_eff_to ON party.involvedpartytype (effectivetodate);
CREATE INDEX idx_iptype_wh_created ON party.involvedpartytype (warehousecreatedtimestamp);
CREATE INDEX idx_iptype_wh_updated ON party.involvedpartytype (warehouselastupdatedtimestamp);
CREATE INDEX idx_iptype_ei_wh ON party.involvedpartytype (enterpriseid, warehousefromdate);
CREATE INDEX idx_iptype_af_wh ON party.involvedpartytype (activeflagid, warehousefromdate);
CREATE INDEX idx_iptype_sys_wh ON party.involvedpartytype (systemid, warehousefromdate);

-- Indexes for party.involvedpartytypesecuritytoken
CREATE INDEX idx_iptypest_eff_from ON party.involvedpartytypesecuritytoken (effectivefromdate);
CREATE INDEX idx_iptypest_eff_to ON party.involvedpartytypesecuritytoken (effectivetodate);
CREATE INDEX idx_iptypest_wh_created ON party.involvedpartytypesecuritytoken (warehousecreatedtimestamp);
CREATE INDEX idx_iptypest_wh_updated ON party.involvedpartytypesecuritytoken (warehouselastupdatedtimestamp);
CREATE INDEX idx_iptypest_ei_wh ON party.involvedpartytypesecuritytoken (enterpriseid, warehousefromdate);
CREATE INDEX idx_iptypest_af_wh ON party.involvedpartytypesecuritytoken (activeflagid, warehousefromdate);
CREATE INDEX idx_iptypest_sys_wh ON party.involvedpartytypesecuritytoken (systemid, warehousefromdate);

-- Indexes for party.involvedpartyxaddress
CREATE INDEX idx_ipxa_eff_from ON party.involvedpartyxaddress (effectivefromdate);
CREATE INDEX idx_ipxa_eff_to ON party.involvedpartyxaddress (effectivetodate);
CREATE INDEX idx_ipxa_wh_created ON party.involvedpartyxaddress (warehousecreatedtimestamp);
CREATE INDEX idx_ipxa_wh_updated ON party.involvedpartyxaddress (warehouselastupdatedtimestamp);
CREATE INDEX idx_ipxa_ei_wh ON party.involvedpartyxaddress (enterpriseid, warehousefromdate);
CREATE INDEX idx_ipxa_af_wh ON party.involvedpartyxaddress (activeflagid, warehousefromdate);
CREATE INDEX idx_ipxa_sys_wh ON party.involvedpartyxaddress (systemid, warehousefromdate);
CREATE INDEX idx_ipxa_cl_wh ON party.involvedpartyxaddress (classificationid, warehousefromdate);

-- Indexes for party.involvedpartyxaddresssecuritytoken
CREATE INDEX idx_ipxast_eff_from ON party.involvedpartyxaddresssecuritytoken (effectivefromdate);
CREATE INDEX idx_ipxast_eff_to ON party.involvedpartyxaddresssecuritytoken (effectivetodate);
CREATE INDEX idx_ipxast_wh_created ON party.involvedpartyxaddresssecuritytoken (warehousecreatedtimestamp);
CREATE INDEX idx_ipxast_wh_updated ON party.involvedpartyxaddresssecuritytoken (warehouselastupdatedtimestamp);
CREATE INDEX idx_ipxast_ei_wh ON party.involvedpartyxaddresssecuritytoken (enterpriseid, warehousefromdate);
CREATE INDEX idx_ipxast_af_wh ON party.involvedpartyxaddresssecuritytoken (activeflagid, warehousefromdate);
CREATE INDEX idx_ipxast_sys_wh ON party.involvedpartyxaddresssecuritytoken (systemid, warehousefromdate);

-- Indexes for party.involvedpartyxclassification
CREATE INDEX idx_ipxc_eff_from ON party.involvedpartyxclassification (effectivefromdate);
CREATE INDEX idx_ipxc_eff_to ON party.involvedpartyxclassification (effectivetodate);
CREATE INDEX idx_ipxc_wh_created ON party.involvedpartyxclassification (warehousecreatedtimestamp);
CREATE INDEX idx_ipxc_wh_updated ON party.involvedpartyxclassification (warehouselastupdatedtimestamp);
CREATE INDEX idx_ipxc_ei_wh ON party.involvedpartyxclassification (enterpriseid, warehousefromdate);
CREATE INDEX idx_ipxc_af_wh ON party.involvedpartyxclassification (activeflagid, warehousefromdate);
CREATE INDEX idx_ipxc_sys_wh ON party.involvedpartyxclassification (systemid, warehousefromdate);
CREATE INDEX idx_ipxc_cl_wh ON party.involvedpartyxclassification (classificationid, warehousefromdate);

-- Indexes for party.involvedpartyxclassificationsecuritytoken
CREATE INDEX idx_ipxcst_eff_from ON party.involvedpartyxclassificationsecuritytoken (effectivefromdate);
CREATE INDEX idx_ipxcst_eff_to ON party.involvedpartyxclassificationsecuritytoken (effectivetodate);
CREATE INDEX idx_ipxcst_wh_created ON party.involvedpartyxclassificationsecuritytoken (warehousecreatedtimestamp);
CREATE INDEX idx_ipxcst_wh_updated ON party.involvedpartyxclassificationsecuritytoken (warehouselastupdatedtimestamp);
CREATE INDEX idx_ipxcst_ei_wh ON party.involvedpartyxclassificationsecuritytoken (enterpriseid, warehousefromdate);
CREATE INDEX idx_ipxcst_af_wh ON party.involvedpartyxclassificationsecuritytoken (activeflagid, warehousefromdate);
CREATE INDEX idx_ipxcst_sys_wh ON party.involvedpartyxclassificationsecuritytoken (systemid, warehousefromdate);

-- Indexes for party.involvedpartyxinvolvedparty
CREATE INDEX idx_ipxip_eff_from ON party.involvedpartyxinvolvedparty (effectivefromdate);
CREATE INDEX idx_ipxip_eff_to ON party.involvedpartyxinvolvedparty (effectivetodate);
CREATE INDEX idx_ipxip_wh_created ON party.involvedpartyxinvolvedparty (warehousecreatedtimestamp);
CREATE INDEX idx_ipxip_wh_updated ON party.involvedpartyxinvolvedparty (warehouselastupdatedtimestamp);
CREATE INDEX idx_ipxip_ei_wh ON party.involvedpartyxinvolvedparty (enterpriseid, warehousefromdate);
CREATE INDEX idx_ipxip_af_wh ON party.involvedpartyxinvolvedparty (activeflagid, warehousefromdate);
CREATE INDEX idx_ipxip_sys_wh ON party.involvedpartyxinvolvedparty (systemid, warehousefromdate);
CREATE INDEX idx_ipxip_cl_wh ON party.involvedpartyxinvolvedparty (classificationid, warehousefromdate);

-- Indexes for party.involvedpartyxinvolvedpartysecuritytoken
CREATE INDEX idx_ipxipst_eff_from ON party.involvedpartyxinvolvedpartysecuritytoken (effectivefromdate);
CREATE INDEX idx_ipxipst_eff_to ON party.involvedpartyxinvolvedpartysecuritytoken (effectivetodate);
CREATE INDEX idx_ipxipst_wh_created ON party.involvedpartyxinvolvedpartysecuritytoken (warehousecreatedtimestamp);
CREATE INDEX idx_ipxipst_wh_updated ON party.involvedpartyxinvolvedpartysecuritytoken (warehouselastupdatedtimestamp);
CREATE INDEX idx_ipxipst_ei_wh ON party.involvedpartyxinvolvedpartysecuritytoken (enterpriseid, warehousefromdate);
CREATE INDEX idx_ipxipst_af_wh ON party.involvedpartyxinvolvedpartysecuritytoken (activeflagid, warehousefromdate);
CREATE INDEX idx_ipxipst_sys_wh ON party.involvedpartyxinvolvedpartysecuritytoken (systemid, warehousefromdate);

-- Indexes for party.involvedpartyxproduct
CREATE INDEX idx_ipxp_eff_from ON party.involvedpartyxproduct (effectivefromdate);
CREATE INDEX idx_ipxp_eff_to ON party.involvedpartyxproduct (effectivetodate);
CREATE INDEX idx_ipxp_wh_created ON party.involvedpartyxproduct (warehousecreatedtimestamp);
CREATE INDEX idx_ipxp_wh_updated ON party.involvedpartyxproduct (warehouselastupdatedtimestamp);
CREATE INDEX idx_ipxp_ei_wh ON party.involvedpartyxproduct (enterpriseid, warehousefromdate);
CREATE INDEX idx_ipxp_af_wh ON party.involvedpartyxproduct (activeflagid, warehousefromdate);
CREATE INDEX idx_ipxp_sys_wh ON party.involvedpartyxproduct (systemid, warehousefromdate);
CREATE INDEX idx_ipxp_cl_wh ON party.involvedpartyxproduct (classificationid, warehousefromdate);

-- Indexes for party.involvedpartyxproductsecuritytoken
CREATE INDEX idx_ipxpst_eff_from ON party.involvedpartyxproductsecuritytoken (effectivefromdate);
CREATE INDEX idx_ipxpst_eff_to ON party.involvedpartyxproductsecuritytoken (effectivetodate);
CREATE INDEX idx_ipxpst_wh_created ON party.involvedpartyxproductsecuritytoken (warehousecreatedtimestamp);
CREATE INDEX idx_ipxpst_wh_updated ON party.involvedpartyxproductsecuritytoken (warehouselastupdatedtimestamp);
CREATE INDEX idx_ipxpst_ei_wh ON party.involvedpartyxproductsecuritytoken (enterpriseid, warehousefromdate);
CREATE INDEX idx_ipxpst_af_wh ON party.involvedpartyxproductsecuritytoken (activeflagid, warehousefromdate);
CREATE INDEX idx_ipxpst_sys_wh ON party.involvedpartyxproductsecuritytoken (systemid, warehousefromdate);

-- Indexes for party.involvedpartyxresourceitem
CREATE INDEX idx_ipxrist_eff_from ON party.involvedpartyxresourceitem (effectivefromdate);
CREATE INDEX idx_ipxrist_eff_to ON party.involvedpartyxresourceitem (effectivetodate);
CREATE INDEX idx_ipxrist_wh_created ON party.involvedpartyxresourceitem (warehousecreatedtimestamp);
CREATE INDEX idx_ipxrist_wh_updated ON party.involvedpartyxresourceitem (warehouselastupdatedtimestamp);
CREATE INDEX idx_ipxrist_ei_wh ON party.involvedpartyxresourceitem (enterpriseid, warehousefromdate);
CREATE INDEX idx_ipxrist_af_wh ON party.involvedpartyxresourceitem (activeflagid, warehousefromdate);
CREATE INDEX idx_ipxrist_sys_wh ON party.involvedpartyxresourceitem (systemid, warehousefromdate);
CREATE INDEX idx_ipxrist_cl_wh ON party.involvedpartyxresourceitem (classificationid, warehousefromdate);

-- Indexes for party.involvedpartyxresourceitemsecuritytoken
CREATE INDEX idx_ipxristst_eff_from ON party.involvedpartyxresourceitemsecuritytoken (effectivefromdate);
CREATE INDEX idx_ipxristst_eff_to ON party.involvedpartyxresourceitemsecuritytoken (effectivetodate);
CREATE INDEX idx_ipxristst_wh_created ON party.involvedpartyxresourceitemsecuritytoken (warehousecreatedtimestamp);
CREATE INDEX idx_ipxristst_wh_updated ON party.involvedpartyxresourceitemsecuritytoken (warehouselastupdatedtimestamp);
CREATE INDEX idx_ipxristst_ei_wh ON party.involvedpartyxresourceitemsecuritytoken (enterpriseid, warehousefromdate);
CREATE INDEX idx_ipxristst_af_wh ON party.involvedpartyxresourceitemsecuritytoken (activeflagid, warehousefromdate);
CREATE INDEX idx_ipxristst_sys_wh ON party.involvedpartyxresourceitemsecuritytoken (systemid, warehousefromdate);

-- Indexes for party.involvedpartyxrules
CREATE INDEX idx_ipxr_eff_from ON party.involvedpartyxrules (effectivefromdate);
CREATE INDEX idx_ipxr_eff_to ON party.involvedpartyxrules (effectivetodate);
CREATE INDEX idx_ipxr_wh_created ON party.involvedpartyxrules (warehousecreatedtimestamp);
CREATE INDEX idx_ipxr_wh_updated ON party.involvedpartyxrules (warehouselastupdatedtimestamp);
CREATE INDEX idx_ipxr_ei_wh ON party.involvedpartyxrules (enterpriseid, warehousefromdate);
CREATE INDEX idx_ipxr_af_wh ON party.involvedpartyxrules (activeflagid, warehousefromdate);
CREATE INDEX idx_ipxr_sys_wh ON party.involvedpartyxrules (systemid, warehousefromdate);
CREATE INDEX idx_ipxr_cl_wh ON party.involvedpartyxrules (classificationid, warehousefromdate);

-- Indexes for party.involvedpartyxrulessecuritytoken
CREATE INDEX idx_ipxrst_eff_from ON party.involvedpartyxrulessecuritytoken (effectivefromdate);
CREATE INDEX idx_ipxrst_eff_to ON party.involvedpartyxrulessecuritytoken (effectivetodate);
CREATE INDEX idx_ipxrst_wh_created ON party.involvedpartyxrulessecuritytoken (warehousecreatedtimestamp);
CREATE INDEX idx_ipxrst_wh_updated ON party.involvedpartyxrulessecuritytoken (warehouselastupdatedtimestamp);
CREATE INDEX idx_ipxrst_ei_wh ON party.involvedpartyxrulessecuritytoken (enterpriseid, warehousefromdate);
CREATE INDEX idx_ipxrst_af_wh ON party.involvedpartyxrulessecuritytoken (activeflagid, warehousefromdate);
CREATE INDEX idx_ipxrst_sys_wh ON party.involvedpartyxrulessecuritytoken (systemid, warehousefromdate);

-- Indexes for party.involvedpartyxproducttype
CREATE INDEX idx_ipxpt_eff_from ON party.involvedpartyxproducttype (effectivefromdate);
CREATE INDEX idx_ipxpt_eff_to ON party.involvedpartyxproducttype (effectivetodate);
CREATE INDEX idx_ipxpt_wh_created ON party.involvedpartyxproducttype (warehousecreatedtimestamp);
CREATE INDEX idx_ipxpt_wh_updated ON party.involvedpartyxproducttype (warehouselastupdatedtimestamp);
CREATE INDEX idx_ipxpt_ei_wh ON party.involvedpartyxproducttype (enterpriseid, warehousefromdate);
CREATE INDEX idx_ipxpt_af_wh ON party.involvedpartyxproducttype (activeflagid, warehousefromdate);
CREATE INDEX idx_ipxpt_sys_wh ON party.involvedpartyxproducttype (systemid, warehousefromdate);
CREATE INDEX idx_ipxpt_cl_wh ON party.involvedpartyxproducttype (classificationid, warehousefromdate);
