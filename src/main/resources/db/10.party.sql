CREATE SCHEMA party;
CREATE TABLE party.involvedparty
(
    involvedpartyid               UUID                        NOT NULL primary key,
    effectivefromdate             timestamp(6) with time zone NOT NULL,
    effectivetodate               timestamp(6) with time zone NOT NULL,
    warehousecreatedtimestamp     timestamp(6) with time zone NOT NULL,
    warehousefromdate             DATE                        NOT NULL,

    warehouselastupdatedtimestamp timestamp(6) with time zone NOT NULL,
    originalsourcesystemuniqueid  UUID                        NOT NULL,
    activeflagid                  UUID                        NOT NULL,
    enterpriseid                  UUID                        NOT NULL,
    systemid                      UUID                        NOT NULL,
    originalsourcesystemid        UUID                        NOT NULL
);
CREATE TABLE party.involvedpartyidentificationtype
(
    involvedpartyidentificationtypeid UUID                        NOT NULL primary key,
    effectivefromdate                 timestamp(6) with time zone NOT NULL,
    effectivetodate                   timestamp(6) with time zone NOT NULL,
    warehousecreatedtimestamp         timestamp(6) with time zone NOT NULL,
    warehousefromdate                 DATE                        NOT NULL,

    warehouselastupdatedtimestamp     timestamp(6) with time zone NOT NULL,
    originalsourcesystemuniqueid      UUID                        NOT NULL,
    involvedpartyidentificationdesc   character varying(500)      NOT NULL,
    involvedpartyidentificationname   character varying(150)      NOT NULL,
    activeflagid                      UUID                        NOT NULL,
    enterpriseid                      UUID                        NOT NULL,
    systemid                          UUID                        NOT NULL,
    originalsourcesystemid            UUID                        NOT NULL
);
CREATE TABLE party.involvedpartyidentificationtypesecuritytoken
(
    involvedpartyidentificationtypesecuritytokenid UUID                        NOT NULL primary key,
    effectivefromdate                              timestamp(6) with time zone NOT NULL,
    effectivetodate                                timestamp(6) with time zone NOT NULL,
    warehousecreatedtimestamp                      timestamp(6) with time zone NOT NULL,
    warehousefromdate                              DATE                        NOT NULL,

    warehouselastupdatedtimestamp                  timestamp(6) with time zone NOT NULL,
    createallowed                                  INTEGER                     NOT NULL,
    deleteallowed                                  INTEGER                     NOT NULL,
    originalsourcesystemuniqueid                   UUID                        NOT NULL,
    readallowed                                    INTEGER                     NOT NULL,
    updateallowed                                  INTEGER                     NOT NULL,
    activeflagid                                   UUID                        NOT NULL,
    enterpriseid                                   UUID                        NOT NULL,
    originalsourcesystemid                         UUID                        NOT NULL,
    securitytokenid                                UUID                        NOT NULL,
    systemid                                       UUID                        NOT NULL,
    involvedpartyidentificationtypeid              UUID                        NOT NULL
);
CREATE TABLE party.involvedpartynametype
(
    involvedpartynametypeid       UUID                        NOT NULL primary key,
    effectivefromdate             timestamp(6) with time zone NOT NULL,
    effectivetodate               timestamp(6) with time zone NOT NULL,
    warehousecreatedtimestamp     timestamp(6) with time zone NOT NULL,
    warehousefromdate             DATE                        NOT NULL,

    warehouselastupdatedtimestamp timestamp(6) with time zone NOT NULL,
    originalsourcesystemuniqueid  UUID                        NOT NULL,
    involvedpartynametypedescr    character varying(500)      NOT NULL,
    involvedpartynametypename     character varying(500)      NOT NULL,
    activeflagid                  UUID                        NOT NULL,
    enterpriseid                  UUID                        NOT NULL,
    systemid                      UUID                        NOT NULL,
    originalsourcesystemid        UUID                        NOT NULL
);
CREATE TABLE party.involvedpartynametypesecuritytoken
(
    involvedpartynametypesecuritytokenid UUID                        NOT NULL primary key,
    effectivefromdate                    timestamp(6) with time zone NOT NULL,
    effectivetodate                      timestamp(6) with time zone NOT NULL,
    warehousecreatedtimestamp            timestamp(6) with time zone NOT NULL,
    warehousefromdate                    DATE                        NOT NULL,

    warehouselastupdatedtimestamp        timestamp(6) with time zone NOT NULL,
    createallowed                        INTEGER                     NOT NULL,
    deleteallowed                        INTEGER                     NOT NULL,
    originalsourcesystemuniqueid         UUID                        NOT NULL,
    readallowed                          INTEGER                     NOT NULL,
    updateallowed                        INTEGER                     NOT NULL,
    activeflagid                         UUID                        NOT NULL,
    enterpriseid                         UUID                        NOT NULL,
    originalsourcesystemid               UUID                        NOT NULL,
    securitytokenid                      UUID                        NOT NULL,
    systemid                             UUID                        NOT NULL,
    involvedpartynametypeid              UUID                        NOT NULL
);

CREATE TABLE party.involvedpartynonorganic
(
    involvedpartynonorganicid     UUID                        NOT NULL primary key,
    effectivefromdate             timestamp(6) with time zone NOT NULL,
    effectivetodate               timestamp(6) with time zone NOT NULL,
    warehousecreatedtimestamp     timestamp(6) with time zone NOT NULL,
    warehousefromdate             DATE                        NOT NULL,

    warehouselastupdatedtimestamp timestamp(6) with time zone NOT NULL,
    originalsourcesystemuniqueid  UUID                        NOT NULL,
    activeflagid                  UUID                        NOT NULL,
    enterpriseid                  UUID                        NOT NULL,
    systemid                      UUID                        NOT NULL,
    originalsourcesystemid        UUID                        NOT NULL
);

CREATE TABLE party.involvedpartynonorganicsecuritytoken
(
    involvedpartynonorganicsecuritytokenid UUID                        NOT NULL primary key,
    effectivefromdate                      timestamp(6) with time zone NOT NULL,
    effectivetodate                        timestamp(6) with time zone NOT NULL,
    warehousecreatedtimestamp              timestamp(6) with time zone NOT NULL,
    warehousefromdate                      DATE                        NOT NULL,

    warehouselastupdatedtimestamp          timestamp(6) with time zone NOT NULL,
    createallowed                          INTEGER                     NOT NULL,
    deleteallowed                          INTEGER                     NOT NULL,
    originalsourcesystemuniqueid           UUID                        NOT NULL,
    readallowed                            INTEGER                     NOT NULL,
    updateallowed                          INTEGER                     NOT NULL,
    activeflagid                           UUID                        NOT NULL,
    enterpriseid                           UUID                        NOT NULL,
    originalsourcesystemid                 UUID                        NOT NULL,
    securitytokenid                        UUID                        NOT NULL,
    systemid                               UUID                        NOT NULL,
    involvedpartynonorganicid              UUID                        NOT NULL
);

CREATE TABLE party.involvedpartyorganic
(
    involvedpartyorganicid        UUID                        NOT NULL primary key,
    effectivefromdate             timestamp(6) with time zone NOT NULL,
    effectivetodate               timestamp(6) with time zone NOT NULL,
    warehousecreatedtimestamp     timestamp(6) with time zone NOT NULL,
    warehousefromdate             DATE                        NOT NULL,

    warehouselastupdatedtimestamp timestamp(6) with time zone NOT NULL,
    originalsourcesystemuniqueid  UUID                        NOT NULL,
    activeflagid                  UUID                        NOT NULL,
    enterpriseid                  UUID                        NOT NULL,
    systemid                      UUID                        NOT NULL,
    originalsourcesystemid        UUID                        NOT NULL
);

CREATE TABLE party.involvedpartyorganicsecuritytoken
(
    involvedpartyorganicsecuritytokenid UUID                        NOT NULL primary key,
    effectivefromdate                   timestamp(6) with time zone NOT NULL,
    effectivetodate                     timestamp(6) with time zone NOT NULL,
    warehousecreatedtimestamp           timestamp(6) with time zone NOT NULL,
    warehousefromdate                   DATE                        NOT NULL,

    warehouselastupdatedtimestamp       timestamp(6) with time zone NOT NULL,
    createallowed                       INTEGER                     NOT NULL,
    deleteallowed                       INTEGER                     NOT NULL,
    originalsourcesystemuniqueid        UUID                        NOT NULL,
    readallowed                         INTEGER                     NOT NULL,
    updateallowed                       INTEGER                     NOT NULL,
    activeflagid                        UUID                        NOT NULL,
    enterpriseid                        UUID                        NOT NULL,
    originalsourcesystemid              UUID                        NOT NULL,
    securitytokenid                     UUID                        NOT NULL,
    systemid                            UUID                        NOT NULL,
    involvedpartyorganicid              UUID                        NOT NULL
);

CREATE TABLE party.involvedpartyorganictype
(
    involvedpartyorganictypeid    UUID                        NOT NULL primary key,
    effectivefromdate             timestamp(6) with time zone NOT NULL,
    effectivetodate               timestamp(6) with time zone NOT NULL,
    warehousecreatedtimestamp     timestamp(6) with time zone NOT NULL,
    warehousefromdate             DATE                        NOT NULL,

    warehouselastupdatedtimestamp timestamp(6) with time zone NOT NULL,
    originalsourcesystemuniqueid  UUID                        NOT NULL,
    involvedpartytypedesc         character varying(500)      NOT NULL,
    involvedpartytypename         character varying(200)      NOT NULL,
    activeflagid                  UUID                        NOT NULL,
    enterpriseid                  UUID                        NOT NULL,
    systemid                      UUID                        NOT NULL,
    originalsourcesystemid        UUID                        NOT NULL
);

CREATE TABLE party.involvedpartyorganictypesecuritytoken
(
    involvedpartyorganictypesecuritytokenid UUID                        NOT NULL primary key,
    effectivefromdate                       timestamp(6) with time zone NOT NULL,
    effectivetodate                         timestamp(6) with time zone NOT NULL,
    warehousecreatedtimestamp               timestamp(6) with time zone NOT NULL,
    warehousefromdate                       DATE                        NOT NULL,

    warehouselastupdatedtimestamp           timestamp(6) with time zone NOT NULL,
    createallowed                           INTEGER                     NOT NULL,
    deleteallowed                           INTEGER                     NOT NULL,
    originalsourcesystemuniqueid            UUID                        NOT NULL,
    readallowed                             INTEGER                     NOT NULL,
    updateallowed                           INTEGER                     NOT NULL,
    activeflagid                            UUID                        NOT NULL,
    enterpriseid                            UUID                        NOT NULL,
    originalsourcesystemid                  UUID                        NOT NULL,
    securitytokenid                         UUID                        NOT NULL,
    systemid                                UUID                        NOT NULL,
    involvedpartyorganictypeid              UUID                        NOT NULL
);

CREATE TABLE party.involvedpartysecuritytoken
(
    involvedpartysecuritytokenid  UUID                        NOT NULL primary key,
    effectivefromdate             timestamp(6) with time zone NOT NULL,
    effectivetodate               timestamp(6) with time zone NOT NULL,
    warehousecreatedtimestamp     timestamp(6) with time zone NOT NULL,
    warehousefromdate             DATE                        NOT NULL,

    warehouselastupdatedtimestamp timestamp(6) with time zone NOT NULL,
    createallowed                 INTEGER                     NOT NULL,
    deleteallowed                 INTEGER                     NOT NULL,
    originalsourcesystemuniqueid  UUID                        NOT NULL,
    readallowed                   INTEGER                     NOT NULL,
    updateallowed                 INTEGER                     NOT NULL,
    activeflagid                  UUID                        NOT NULL,
    enterpriseid                  UUID                        NOT NULL,
    originalsourcesystemid        UUID                        NOT NULL,
    securitytokenid               UUID                        NOT NULL,
    systemid                      UUID                        NOT NULL,
    involvedpartyid               UUID                        NOT NULL
);

CREATE TABLE party.involvedpartytype
(
    involvedpartytypeid           UUID                        NOT NULL primary key,
    effectivefromdate             timestamp(6) with time zone NOT NULL,
    effectivetodate               timestamp(6) with time zone NOT NULL,
    warehousecreatedtimestamp     timestamp(6) with time zone NOT NULL,
    warehousefromdate             DATE                        NOT NULL,

    warehouselastupdatedtimestamp timestamp(6) with time zone NOT NULL,
    originalsourcesystemuniqueid  UUID                        NOT NULL,
    involvedpartytypedesc         character varying(255)      NOT NULL,
    involvedpartytypename         character varying(100)      NOT NULL,
    activeflagid                  UUID                        NOT NULL,
    enterpriseid                  UUID                        NOT NULL,
    systemid                      UUID                        NOT NULL,
    originalsourcesystemid        UUID                        NOT NULL
);

CREATE TABLE party.involvedpartytypesecuritytoken
(
    involvedpartytypesecuritytokenid UUID                        NOT NULL primary key,
    effectivefromdate                timestamp(6) with time zone NOT NULL,
    effectivetodate                  timestamp(6) with time zone NOT NULL,
    warehousecreatedtimestamp        timestamp(6) with time zone NOT NULL,
    warehousefromdate                DATE                        NOT NULL,

    warehouselastupdatedtimestamp    timestamp(6) with time zone NOT NULL,
    createallowed                    INTEGER                     NOT NULL,
    deleteallowed                    INTEGER                     NOT NULL,
    originalsourcesystemuniqueid     UUID                        NOT NULL,
    readallowed                      INTEGER                     NOT NULL,
    updateallowed                    INTEGER                     NOT NULL,
    activeflagid                     UUID                        NOT NULL,
    enterpriseid                     UUID                        NOT NULL,
    originalsourcesystemid           UUID                        NOT NULL,
    securitytokenid                  UUID                        NOT NULL,
    systemid                         UUID                        NOT NULL,
    involvedpartytypeid              UUID                        NOT NULL
);

CREATE TABLE party.involvedpartyxaddress
(
    involvedpartyxaddressid       UUID                        NOT NULL primary key,
    effectivefromdate             timestamp(6) with time zone NOT NULL,
    effectivetodate               timestamp(6) with time zone NOT NULL,
    warehousecreatedtimestamp     timestamp(6) with time zone NOT NULL,
    warehousefromdate             DATE                        NOT NULL,

    warehouselastupdatedtimestamp timestamp(6) with time zone NOT NULL,
    originalsourcesystemuniqueid  UUID                        NOT NULL,
    value                         text                        NOT NULL,
    activeflagid                  UUID                        NOT NULL,
    enterpriseid                  UUID                        NOT NULL,
    systemid                      UUID                        NOT NULL,
    originalsourcesystemid        UUID                        NOT NULL,
    classificationid              UUID                        NOT NULL,
    addressid                     UUID                        NOT NULL,
    involvedpartyid               UUID                        NOT NULL
);

CREATE TABLE party.involvedpartyxaddresssecuritytoken
(
    involvedpartyxaddresssecuritytokenid UUID                        NOT NULL primary key,
    effectivefromdate                    timestamp(6) with time zone NOT NULL,
    effectivetodate                      timestamp(6) with time zone NOT NULL,
    warehousecreatedtimestamp            timestamp(6) with time zone NOT NULL,
    warehousefromdate                    DATE                        NOT NULL,

    warehouselastupdatedtimestamp        timestamp(6) with time zone NOT NULL,
    createallowed                        INTEGER                     NOT NULL,
    deleteallowed                        INTEGER                     NOT NULL,
    originalsourcesystemuniqueid         UUID                        NOT NULL,
    readallowed                          INTEGER                     NOT NULL,
    updateallowed                        INTEGER                     NOT NULL,
    activeflagid                         UUID                        NOT NULL,
    enterpriseid                         UUID                        NOT NULL,
    originalsourcesystemid               UUID                        NOT NULL,
    securitytokenid                      UUID                        NOT NULL,
    systemid                             UUID                        NOT NULL,
    involvedpartyxaddressid              UUID                        NOT NULL
);
CREATE TABLE party.involvedpartyxclassification
(
    involvedpartyxclassificationid UUID                        NOT NULL primary key,
    effectivefromdate              timestamp(6) with time zone NOT NULL,
    effectivetodate                timestamp(6) with time zone NOT NULL,
    warehousecreatedtimestamp      timestamp(6) with time zone NOT NULL,
    warehousefromdate              DATE                        NOT NULL,

    warehouselastupdatedtimestamp  timestamp(6) with time zone NOT NULL,
    originalsourcesystemuniqueid   UUID                        NOT NULL,
    value                          text                        NOT NULL,
    activeflagid                   UUID                        NOT NULL,
    enterpriseid                   UUID                        NOT NULL,
    systemid                       UUID                        NOT NULL,
    originalsourcesystemid         UUID                        NOT NULL,
    classificationid               UUID                        NOT NULL,
    involvedpartyid                UUID                        NOT NULL
);
CREATE TABLE party.involvedpartyxclassificationsecuritytoken
(
    involvedpartyxclassificationsecuritytokenid UUID                        NOT NULL primary key,
    effectivefromdate                           timestamp(6) with time zone NOT NULL,
    effectivetodate                             timestamp(6) with time zone NOT NULL,
    warehousecreatedtimestamp                   timestamp(6) with time zone NOT NULL,
    warehousefromdate                           DATE                        NOT NULL,

    warehouselastupdatedtimestamp               timestamp(6) with time zone NOT NULL,
    createallowed                               INTEGER                     NOT NULL,
    deleteallowed                               INTEGER                     NOT NULL,
    originalsourcesystemuniqueid                UUID                        NOT NULL,
    readallowed                                 INTEGER                     NOT NULL,
    updateallowed                               INTEGER                     NOT NULL,
    activeflagid                                UUID                        NOT NULL,
    enterpriseid                                UUID                        NOT NULL,
    originalsourcesystemid                      UUID                        NOT NULL,
    securitytokenid                             UUID                        NOT NULL,
    systemid                                    UUID                        NOT NULL,
    involvedpartyxclassificationid              UUID                        NOT NULL
);
CREATE TABLE party.involvedpartyxinvolvedparty
(
    involvedpartyxinvolvedpartyid UUID                        NOT NULL primary key,
    effectivefromdate             timestamp(6) with time zone NOT NULL,
    effectivetodate               timestamp(6) with time zone NOT NULL,
    warehousecreatedtimestamp     timestamp(6) with time zone NOT NULL,
    warehousefromdate             DATE                        NOT NULL,

    warehouselastupdatedtimestamp timestamp(6) with time zone NOT NULL,
    originalsourcesystemuniqueid  UUID                        NOT NULL,
    value                         text                        NOT NULL,
    activeflagid                  UUID                        NOT NULL,
    enterpriseid                  UUID                        NOT NULL,
    systemid                      UUID                        NOT NULL,
    originalsourcesystemid        UUID                        NOT NULL,
    classificationid              UUID                        NOT NULL,
    childinvolvedpartyid          UUID                        NOT NULL,
    parentinvolvedpartyid         UUID                        NOT NULL
);

CREATE TABLE party.involvedpartyxinvolvedpartyidentificationtype
(
    involvedpartyxinvolvedpartyidentificationtypeid UUID                        NOT NULL primary key,
    effectivefromdate                               timestamp(6) with time zone NOT NULL,
    effectivetodate                                 timestamp(6) with time zone NOT NULL,
    warehousecreatedtimestamp                       timestamp(6) with time zone NOT NULL,
    warehousefromdate                               DATE                        NOT NULL,

    warehouselastupdatedtimestamp                   timestamp(6) with time zone NOT NULL,
    originalsourcesystemuniqueid                    UUID                        NOT NULL,
    value                                           text                        NOT NULL,
    activeflagid                                    UUID                        NOT NULL,
    enterpriseid                                    UUID                        NOT NULL,
    systemid                                        UUID                        NOT NULL,
    originalsourcesystemid                          UUID                        NOT NULL,
    classificationid                                UUID                        NOT NULL,
    involvedpartyid                                 UUID                        NOT NULL,
    involvedpartyidentificationtypeid               UUID                        NOT NULL
);
CREATE TABLE party.involvedpartyxinvolvedpartyidentificationtypesecuritytoken
(
    involvedpartyxinvolvedpartyidentificationtypesecuritytokenid UUID                        NOT NULL primary key,
    effectivefromdate                                            timestamp(6) with time zone NOT NULL,
    effectivetodate                                              timestamp(6) with time zone NOT NULL,
    warehousecreatedtimestamp                                    timestamp(6) with time zone NOT NULL,
    warehousefromdate                                            DATE                        NOT NULL,

    warehouselastupdatedtimestamp                                timestamp(6) with time zone NOT NULL,
    createallowed                                                INTEGER                     NOT NULL,
    deleteallowed                                                INTEGER                     NOT NULL,
    originalsourcesystemuniqueid                                 UUID                        NOT NULL,
    readallowed                                                  INTEGER                     NOT NULL,
    updateallowed                                                INTEGER                     NOT NULL,
    activeflagid                                                 UUID                        NOT NULL,
    enterpriseid                                                 UUID                        NOT NULL,
    originalsourcesystemid                                       UUID                        NOT NULL,
    securitytokenid                                              UUID                        NOT NULL,
    systemid                                                     UUID                        NOT NULL,
    involvedpartyxinvolvedpartyidentificationtypeid              UUID                        NOT NULL
);
CREATE TABLE party.involvedpartyxinvolvedpartynametype
(
    involvedpartyxinvolvedpartynametypeid UUID                        NOT NULL primary key,
    effectivefromdate                     timestamp(6) with time zone NOT NULL,
    effectivetodate                       timestamp(6) with time zone NOT NULL,
    warehousecreatedtimestamp             timestamp(6) with time zone NOT NULL,
    warehousefromdate                     DATE                        NOT NULL,

    warehouselastupdatedtimestamp         timestamp(6) with time zone NOT NULL,
    originalsourcesystemuniqueid          UUID                        NOT NULL,
    value                                 text                        NOT NULL,
    activeflagid                          UUID                        NOT NULL,
    enterpriseid                          UUID                        NOT NULL,
    systemid                              UUID                        NOT NULL,
    originalsourcesystemid                UUID                        NOT NULL,
    classificationid                      UUID                        NOT NULL,
    involvedpartyid                       UUID                        NOT NULL,
    involvedpartynametypeid               UUID                        NOT NULL
);
CREATE TABLE party.involvedpartyxinvolvedpartynametypesecuritytoken
(
    involvedpartyxinvolvedpartynametypesecuritytokenid UUID                        NOT NULL primary key,
    effectivefromdate                                  timestamp(6) with time zone NOT NULL,
    effectivetodate                                    timestamp(6) with time zone NOT NULL,
    warehousecreatedtimestamp                          timestamp(6) with time zone NOT NULL,
    warehousefromdate                                  DATE                        NOT NULL,

    warehouselastupdatedtimestamp                      timestamp(6) with time zone NOT NULL,
    createallowed                                      INTEGER                     NOT NULL,
    deleteallowed                                      INTEGER                     NOT NULL,
    originalsourcesystemuniqueid                       UUID                        NOT NULL,
    readallowed                                        INTEGER                     NOT NULL,
    updateallowed                                      INTEGER                     NOT NULL,
    activeflagid                                       UUID                        NOT NULL,
    enterpriseid                                       UUID                        NOT NULL,
    originalsourcesystemid                             UUID                        NOT NULL,
    securitytokenid                                    UUID                        NOT NULL,
    systemid                                           UUID                        NOT NULL,
    involvedpartyxinvolvedpartynametypeid              UUID                        NOT NULL
);
CREATE TABLE party.involvedpartyxinvolvedpartysecuritytoken
(
    involvedpartyxinvolvedpartysecuritytokenid UUID                        NOT NULL primary key,
    effectivefromdate                          timestamp(6) with time zone NOT NULL,
    effectivetodate                            timestamp(6) with time zone NOT NULL,
    warehousecreatedtimestamp                  timestamp(6) with time zone NOT NULL,
    warehousefromdate                          DATE                        NOT NULL,

    warehouselastupdatedtimestamp              timestamp(6) with time zone NOT NULL,
    createallowed                              INTEGER                     NOT NULL,
    deleteallowed                              INTEGER                     NOT NULL,
    originalsourcesystemuniqueid               UUID                        NOT NULL,
    readallowed                                INTEGER                     NOT NULL,
    updateallowed                              INTEGER                     NOT NULL,
    activeflagid                               UUID                        NOT NULL,
    enterpriseid                               UUID                        NOT NULL,
    originalsourcesystemid                     UUID                        NOT NULL,
    securitytokenid                            UUID                        NOT NULL,
    systemid                                   UUID                        NOT NULL,
    involvedpartyxinvolvedpartyid              UUID                        NOT NULL
);
CREATE TABLE party.involvedpartyxinvolvedpartytype
(
    involvedpartyxinvolvedpartytypeid UUID                        NOT NULL primary key,
    effectivefromdate                 timestamp(6) with time zone NOT NULL,
    effectivetodate                   timestamp(6) with time zone NOT NULL,
    warehousecreatedtimestamp         timestamp(6) with time zone NOT NULL,
    warehousefromdate                 DATE                        NOT NULL,

    warehouselastupdatedtimestamp     timestamp(6) with time zone NOT NULL,
    originalsourcesystemuniqueid      UUID                        NOT NULL,
    value                             text                        NOT NULL,
    activeflagid                      UUID                        NOT NULL,
    enterpriseid                      UUID                        NOT NULL,
    systemid                          UUID                        NOT NULL,
    originalsourcesystemid            UUID                        NOT NULL,
    classificationid                  UUID                        NOT NULL,
    involvedpartyid                   UUID                        NOT NULL,
    involvedpartytypeid               UUID                        NOT NULL
);
CREATE TABLE party.involvedpartyxinvolvedpartytypesecuritytoken
(
    involvedpartyxinvolvedpartytypesecuritytokenid UUID                        NOT NULL primary key,
    effectivefromdate                              timestamp(6) with time zone NOT NULL,
    effectivetodate                                timestamp(6) with time zone NOT NULL,
    warehousecreatedtimestamp                      timestamp(6) with time zone NOT NULL,
    warehousefromdate                              DATE                        NOT NULL,

    warehouselastupdatedtimestamp                  timestamp(6) with time zone NOT NULL,
    createallowed                                  INTEGER                     NOT NULL,
    deleteallowed                                  INTEGER                     NOT NULL,
    originalsourcesystemuniqueid                   UUID                        NOT NULL,
    readallowed                                    INTEGER                     NOT NULL,
    updateallowed                                  INTEGER                     NOT NULL,
    activeflagid                                   UUID                        NOT NULL,
    enterpriseid                                   UUID                        NOT NULL,
    originalsourcesystemid                         UUID                        NOT NULL,
    securitytokenid                                UUID                        NOT NULL,
    systemid                                       UUID                        NOT NULL,
    involvedpartyxinvolvedpartytypeid              UUID                        NOT NULL
);
CREATE TABLE party.involvedpartyxproduct
(
    involvedpartyxproductid       UUID                        NOT NULL primary key,
    effectivefromdate             timestamp(6) with time zone NOT NULL,
    effectivetodate               timestamp(6) with time zone NOT NULL,
    warehousecreatedtimestamp     timestamp(6) with time zone NOT NULL,
    warehousefromdate             DATE                        NOT NULL,

    warehouselastupdatedtimestamp timestamp(6) with time zone NOT NULL,
    originalsourcesystemuniqueid  UUID                        NOT NULL,
    value                         text                        NOT NULL,
    activeflagid                  UUID                        NOT NULL,
    enterpriseid                  UUID                        NOT NULL,
    systemid                      UUID                        NOT NULL,
    originalsourcesystemid        UUID                        NOT NULL,
    classificationid              UUID                        NOT NULL,
    involvedpartyid               UUID                        NOT NULL,
    productid                     UUID                        NOT NULL
);
CREATE TABLE party.involvedpartyxproductsecuritytoken
(
    involvedpartyxproductsecuritytokenid UUID                        NOT NULL primary key,
    effectivefromdate                    timestamp(6) with time zone NOT NULL,
    effectivetodate                      timestamp(6) with time zone NOT NULL,
    warehousecreatedtimestamp            timestamp(6) with time zone NOT NULL,
    warehousefromdate                    DATE                        NOT NULL,

    warehouselastupdatedtimestamp        timestamp(6) with time zone NOT NULL,
    createallowed                        INTEGER                     NOT NULL,
    deleteallowed                        INTEGER                     NOT NULL,
    originalsourcesystemuniqueid         UUID                        NOT NULL,
    readallowed                          INTEGER                     NOT NULL,
    updateallowed                        INTEGER                     NOT NULL,
    activeflagid                         UUID                        NOT NULL,
    enterpriseid                         UUID                        NOT NULL,
    originalsourcesystemid               UUID                        NOT NULL,
    securitytokenid                      UUID                        NOT NULL,
    systemid                             UUID                        NOT NULL,
    involvedpartyxproductid              UUID                        NOT NULL
);
CREATE TABLE party.involvedpartyxproducttype
(
    involvedpartyxproducttypeid   UUID                        NOT NULL primary key,
    effectivefromdate             timestamp(6) with time zone NOT NULL,
    effectivetodate               timestamp(6) with time zone NOT NULL,
    warehousecreatedtimestamp     timestamp(6) with time zone NOT NULL,
    warehousefromdate             DATE                        NOT NULL,

    warehouselastupdatedtimestamp timestamp(6) with time zone NOT NULL,
    originalsourcesystemuniqueid  UUID                        NOT NULL,
    value                         text                        NOT NULL,
    activeflagid                  UUID                        NOT NULL,
    enterpriseid                  UUID                        NOT NULL,
    systemid                      UUID                        NOT NULL,
    originalsourcesystemid        UUID                        NOT NULL,
    classificationid              UUID                        NOT NULL,
    involvedpartyid               UUID                        NOT NULL,
    producttypeid                 UUID                        NOT NULL
);
CREATE TABLE party.involvedpartyxproducttypesecuritytoken
(
    involvedpartyxproducttypesecuritytokenid UUID                        NOT NULL primary key,
    effectivefromdate                        timestamp(6) with time zone NOT NULL,
    effectivetodate                          timestamp(6) with time zone NOT NULL,
    warehousecreatedtimestamp                timestamp(6) with time zone NOT NULL,
    warehousefromdate                        DATE                        NOT NULL,

    warehouselastupdatedtimestamp            timestamp(6) with time zone NOT NULL,
    createallowed                            INTEGER                     NOT NULL,
    deleteallowed                            INTEGER                     NOT NULL,
    originalsourcesystemuniqueid             UUID                        NOT NULL,
    readallowed                              INTEGER                     NOT NULL,
    updateallowed                            INTEGER                     NOT NULL,
    activeflagid                             UUID                        NOT NULL,
    enterpriseid                             UUID                        NOT NULL,
    originalsourcesystemid                   UUID                        NOT NULL,
    securitytokenid                          UUID                        NOT NULL,
    systemid                                 UUID                        NOT NULL,
    involvedpartyxproducttypeid              UUID                        NOT NULL
);
CREATE TABLE party.involvedpartyxresourceitem
(
    involvedpartyxresourceitemid  UUID                        NOT NULL primary key,
    effectivefromdate             timestamp(6) with time zone NOT NULL,
    effectivetodate               timestamp(6) with time zone NOT NULL,
    warehousecreatedtimestamp     timestamp(6) with time zone NOT NULL,
    warehousefromdate             DATE                        NOT NULL,

    warehouselastupdatedtimestamp timestamp(6) with time zone NOT NULL,
    originalsourcesystemuniqueid  UUID                        NOT NULL,
    value                         text                        NOT NULL,
    activeflagid                  UUID                        NOT NULL,
    enterpriseid                  UUID                        NOT NULL,
    systemid                      UUID                        NOT NULL,
    originalsourcesystemid        UUID                        NOT NULL,
    classificationid              UUID                        NOT NULL,
    involvedpartyid               UUID                        NOT NULL,
    resourceitemid                UUID                        NOT NULL
);
CREATE TABLE party.involvedpartyxresourceitemsecuritytoken
(
    involvedpartyxresourceitemsecuritytokenid UUID                        NOT NULL primary key,
    effectivefromdate                         timestamp(6) with time zone NOT NULL,
    effectivetodate                           timestamp(6) with time zone NOT NULL,
    warehousecreatedtimestamp                 timestamp(6) with time zone NOT NULL,
    warehousefromdate                         DATE                        NOT NULL,

    warehouselastupdatedtimestamp             timestamp(6) with time zone NOT NULL,
    createallowed                             INTEGER                     NOT NULL,
    deleteallowed                             INTEGER                     NOT NULL,
    originalsourcesystemuniqueid              UUID                        NOT NULL,
    readallowed                               INTEGER                     NOT NULL,
    updateallowed                             INTEGER                     NOT NULL,
    activeflagid                              UUID                        NOT NULL,
    enterpriseid                              UUID                        NOT NULL,
    originalsourcesystemid                    UUID                        NOT NULL,
    securitytokenid                           UUID                        NOT NULL,
    systemid                                  UUID                        NOT NULL,
    involvedpartyxresourceitemid              UUID                        NOT NULL
);
CREATE TABLE party.involvedpartyxrules
(
    involvedpartyxrulesid         UUID                        NOT NULL primary key,
    effectivefromdate             timestamp(6) with time zone NOT NULL,
    effectivetodate               timestamp(6) with time zone NOT NULL,
    warehousecreatedtimestamp     timestamp(6) with time zone NOT NULL,
    warehousefromdate             DATE                        NOT NULL,

    warehouselastupdatedtimestamp timestamp(6) with time zone NOT NULL,
    originalsourcesystemuniqueid  UUID                        NOT NULL,
    value                         text                        NOT NULL,
    activeflagid                  UUID                        NOT NULL,
    enterpriseid                  UUID                        NOT NULL,
    systemid                      UUID                        NOT NULL,
    originalsourcesystemid        UUID                        NOT NULL,
    classificationid              UUID                        NOT NULL,
    involvedpartyid               UUID                        NOT NULL,
    rulesid                       UUID                        NOT NULL
);
CREATE TABLE party.involvedpartyxrulessecuritytoken
(
    involvedpartyxrulessecuritytokenid UUID                        NOT NULL primary key,
    effectivefromdate                  timestamp(6) with time zone NOT NULL,
    effectivetodate                    timestamp(6) with time zone NOT NULL,
    warehousecreatedtimestamp          timestamp(6) with time zone NOT NULL,
    warehousefromdate                  DATE                        NOT NULL,

    warehouselastupdatedtimestamp      timestamp(6) with time zone NOT NULL,
    createallowed                      INTEGER                     NOT NULL,
    deleteallowed                      INTEGER                     NOT NULL,
    originalsourcesystemuniqueid       UUID                        NOT NULL,
    readallowed                        INTEGER                     NOT NULL,
    updateallowed                      INTEGER                     NOT NULL,
    activeflagid                       UUID                        NOT NULL,
    enterpriseid                       UUID                        NOT NULL,
    originalsourcesystemid             UUID                        NOT NULL,
    securitytokenid                    UUID                        NOT NULL,
    systemid                           UUID                        NOT NULL,
    involvedpartyxrulesid              UUID                        NOT NULL
);


create index fk4gadr7utku5dispktvjwbl2cu_systemid on party.involvedparty (systemid);
create index fkd9g7suhvo150i79rthg96ktwu_enterpriseid on party.involvedparty (enterpriseid);
create index fkpni39h3ejdgolqlvxwoltisl2_originalsourcesystemid on party.involvedparty (originalsourcesystemid);
create index fkq6epu7tek9j6nfmtx80i7jv55_activeflagid on party.involvedparty (activeflagid);
create index fk9t4s53f0ihecoo8ufdth1vbq2_systemid on party.involvedpartyidentificationtype (systemid);
create index fkeuxx0k0eufg56jpbymmg0wnju_enterpriseid on party.involvedpartyidentificationtype (enterpriseid);
create index fkkeqqy519age78syf29vttmwu9_activeflagid on party.involvedpartyidentificationtype (activeflagid);
create index fklxima3r9ohrv77gnodycrb2su_originalsourcesystemid on party.involvedpartyidentificationtype (originalsourcesystemid);
create index fk1o1ufjc0u6yv02pfqokf19xhv_enterpriseid on party.involvedpartyidentificationtypesecuritytoken (enterpriseid);
create index fk4pb209rtplqb1uj5wtx2xr5mg_activeflagid on party.involvedpartyidentificationtypesecuritytoken (activeflagid);
create index fk7dkwueip8veb59dox327ia0a8_involvedpartyidentificationtypeid on party.involvedpartyidentificationtypesecuritytoken (involvedpartyidentificationtypeid);
create index fkdvt59k7lx9fobcggw3naabwkc_originalsourcesystemid on party.involvedpartyidentificationtypesecuritytoken (originalsourcesystemid);
create index fkspj0etctwf7i8sj0gu0d6tvs8_systemid on party.involvedpartyidentificationtypesecuritytoken (systemid);
create index fkt53nic14klq9pmlooadcird6a_securitytokenid on party.involvedpartyidentificationtypesecuritytoken (securitytokenid);
create index fk2xslqqsshflrv1q0ps8p5woad_originalsourcesystemid on party.involvedpartynametype (originalsourcesystemid);
create index fka7cnvd016j105uffrbuh94xx5_activeflagid on party.involvedpartynametype (activeflagid);
create index fkaaa9iyxq2koyservqjmrfeqee_enterpriseid on party.involvedpartynametype (enterpriseid);
create index fkl18vvxbpd5f6oq57wy3jtnbp8_systemid on party.involvedpartynametype (systemid);
create index fk5dtp2b2133pffte1i7gxtom1t_enterpriseid on party.involvedpartynametypesecuritytoken (enterpriseid);
create index fk7t8d65pscktrxlw1we4idhlyj_involvedpartynametypeid on party.involvedpartynametypesecuritytoken (involvedpartynametypeid);
create index fkclphu45dfvj92ca97kfmhd0il_securitytokenid on party.involvedpartynametypesecuritytoken (securitytokenid);
create index fkdkkd4w22d3p250hyn7vwkrp6a_originalsourcesystemid on party.involvedpartynametypesecuritytoken (originalsourcesystemid);
create index fkh3e0lgk341y3qrbdp9y7eo6uf_activeflagid on party.involvedpartynametypesecuritytoken (activeflagid);
create index fkr9iq4s6tpt0gt97prluo7sia_systemid on party.involvedpartynametypesecuritytoken (systemid);
create index fk2fa0fq7vy2j3i2fog95gffalm_enterpriseid on party.involvedpartynonorganic (enterpriseid);
create index fkcphls8bnbiwk5rkf278le5kad_systemid on party.involvedpartynonorganic (systemid);
create index fklb5mxasbqff0f09q3tsqkgsi1_originalsourcesystemid on party.involvedpartynonorganic (originalsourcesystemid);
create index fkt3m2esc7actxvqhmovs8wly1p_activeflagid on party.involvedpartynonorganic (activeflagid);
create index fk4igjsit3bwp3b387pa19s1sej_involvedpartynonorganicid on party.involvedpartynonorganicsecuritytoken (involvedpartynonorganicid);
create index fk8a7thuoro1mbxsng2amjluimk_activeflagid on party.involvedpartynonorganicsecuritytoken (activeflagid);
create index fkbo9u2l1rksyb0qw3poycrj3y2_originalsourcesystemid on party.involvedpartynonorganicsecuritytoken (originalsourcesystemid);
create index fkii44sojyhs3kd1d607mnqldov_systemid on party.involvedpartynonorganicsecuritytoken (systemid);
create index fkkkks9t7wiiiku7knnyo6vvc3r_securitytokenid on party.involvedpartynonorganicsecuritytoken (securitytokenid);
create index fkp0jkfk12oi63j8blb7dvepu5i_enterpriseid on party.involvedpartynonorganicsecuritytoken (enterpriseid);
create index fk2u5a4jkumk881cv7x3ei6vv22_originalsourcesystemid on party.involvedpartyorganic (originalsourcesystemid);
create index fkf7p05lygebyxjhhtrkbn2n2md_systemid on party.involvedpartyorganic (systemid);
create index fkmkbm0yyns5o1ll48bo5yce5sc_activeflagid on party.involvedpartyorganic (activeflagid);
create index fknyksrtkiekj3cd65vtj6d641e_enterpriseid on party.involvedpartyorganic (enterpriseid);
create index fk1lqut70vjbvicq402lw3avani_originalsourcesystemid on party.involvedpartyorganicsecuritytoken (originalsourcesystemid);
create index fkeyo56crc5eg0qtbt3o79rblnp_securitytokenid on party.involvedpartyorganicsecuritytoken (securitytokenid);
create index fkg9iyrrj29vcyc3b6sxsgy95on_activeflagid on party.involvedpartyorganicsecuritytoken (activeflagid);
create index fkmyjwts72ixlq4gmviiba55a6e_enterpriseid on party.involvedpartyorganicsecuritytoken (enterpriseid);
create index fks4ev0kg4ufyi8iw19xqwn2fxb_systemid on party.involvedpartyorganicsecuritytoken (systemid);
create index fkt03i6kwyod9krg1cpoki9kc5x_involvedpartyorganicid on party.involvedpartyorganicsecuritytoken (involvedpartyorganicid);
create index fk1wmvw7vfk12aop3lrals1o1pj_systemid on party.involvedpartyorganictype (systemid);
create index fk6ei1dirma8r1y7lh4glpn9cuy_activeflagid on party.involvedpartyorganictype (activeflagid);
create index fke1juu2g32hsp6fwtfw7f9d3jx_originalsourcesystemid on party.involvedpartyorganictype (originalsourcesystemid);
create index fkq2twef17pvkati3vquevv6w8c_enterpriseid on party.involvedpartyorganictype (enterpriseid);
create index fk2rtgc2ltnx9facnuwt0aj6fpe_originalsourcesystemid on party.involvedpartyorganictypesecuritytoken (originalsourcesystemid);
create index fkejueyv4755b9ec8xoyb7wpu4x_systemid on party.involvedpartyorganictypesecuritytoken (systemid);
create index fkkj7fqfd2urqo5hgy3vxagu45t_enterpriseid on party.involvedpartyorganictypesecuritytoken (enterpriseid);
create index fkluot9f3p0qcl04j9vytxoy5w2_involvedpartyorganictypeid on party.involvedpartyorganictypesecuritytoken (involvedpartyorganictypeid);
create index fkr6a971hibsnykjeadhdgnmwm6_securitytokenid on party.involvedpartyorganictypesecuritytoken (securitytokenid);
create index fkrbdy36uy6fi319o482uvgbic1_activeflagid on party.involvedpartyorganictypesecuritytoken (activeflagid);
create index fk14xr935tw00ygoqib0cxoxmvf_securitytokenid on party.involvedpartysecuritytoken (securitytokenid);
create index fkgtrgr11h04p33g04pukt5x1sy_originalsourcesystemid on party.involvedpartysecuritytoken (originalsourcesystemid);
create index fkialc3kro04ovq8q26gg2hce1s_involvedpartyid on party.involvedpartysecuritytoken (involvedpartyid);
create index fknkgdmsl2don2yegx2qfgnykqw_enterpriseid on party.involvedpartysecuritytoken (enterpriseid);
create index fko24pnibqq5rx8t4soy88rynmd_activeflagid on party.involvedpartysecuritytoken (activeflagid);
create index fkpogqfy578mwtlm6p81g0btv3n_systemid on party.involvedpartysecuritytoken (systemid);
create index fk1v128xeri7d1pkfnltmnoo1co_enterpriseid on party.involvedpartytype (enterpriseid);
create index fk6wemtilbmlhs3b040hu5tfn7v_originalsourcesystemid on party.involvedpartytype (originalsourcesystemid);
create index fkh8ct5p1uw4f7s19svr7525boe_activeflagid on party.involvedpartytype (activeflagid);
create index fkqwx9fppibmcrytwu4t30w0f7e_systemid on party.involvedpartytype (systemid);
create index fk217dwfh6c779f14rd2w8sm1vi_securitytokenid on party.involvedpartytypesecuritytoken (securitytokenid);
create index fk3fs2knats407bhami7ulk0ch9_enterpriseid on party.involvedpartytypesecuritytoken (enterpriseid);
create index fkc8cf751cac8asi0iu3rkexxt0_systemid on party.involvedpartytypesecuritytoken (systemid);
create index fkdtx295xce0hprr5jy1la820eh_involvedpartytypeid on party.involvedpartytypesecuritytoken (involvedpartytypeid);
create index fkjhy9dh04vj80o53adnsk1xi9t_originalsourcesystemid on party.involvedpartytypesecuritytoken (originalsourcesystemid);
create index fksoyq6n0n6er6eyhn229l4oxu6_activeflagid on party.involvedpartytypesecuritytoken (activeflagid);
create index fk3ehfta90cc7ha3uo6wplucc09_addressid on party.involvedpartyxaddress (addressid);
create index fk4flitthmlghp6mj3h7maq4w84_enterpriseid on party.involvedpartyxaddress (enterpriseid);
create index fk6c2fjdp7lxcfy0ar93ni78dcv_originalsourcesystemid on party.involvedpartyxaddress (originalsourcesystemid);
create index fkdwkp9qbashp1p04uip3rg2hls_classificationid on party.involvedpartyxaddress (classificationid);
create index fkejxbg01t325qgchlwnwmpl7a0_activeflagid on party.involvedpartyxaddress (activeflagid);
create index fkf0jjy7kksugbaamt8638ktw8a_systemid on party.involvedpartyxaddress (systemid);
create index fki1pmmm070ua8yd0xrk19qq206_involvedpartyid on party.involvedpartyxaddress (involvedpartyid);
create index fk32jyotpfsamhn8afbs8xgdw5y_originalsourcesystemid on party.involvedpartyxaddresssecuritytoken (originalsourcesystemid);
create index fk58kov7xpcnh58m7m0rxhs6g30_involvedpartyxaddressid on party.involvedpartyxaddresssecuritytoken (involvedpartyxaddressid);
create index fk9h5dpjakcr9j4c61u628tte1b_activeflagid on party.involvedpartyxaddresssecuritytoken (activeflagid);
create index fkietr95ln1k6gcutfwaqg26k24_systemid on party.involvedpartyxaddresssecuritytoken (systemid);
create index fkkwbmkde209srp4k0q0s7cd1u3_enterpriseid on party.involvedpartyxaddresssecuritytoken (enterpriseid);
create index fklcmcbwu95pa7hnchv2tw2l6e4_securitytokenid on party.involvedpartyxaddresssecuritytoken (securitytokenid);
create index fk1k9352drnr2qjxk0itdlpiphl_activeflagid on party.involvedpartyxclassification (activeflagid);
create index fk1l1b8kh214rut78airw106ska_involvedpartyid on party.involvedpartyxclassification (involvedpartyid);
create index fk7jbx6dlrjcvu3ifb2s1w0ulct_enterpriseid on party.involvedpartyxclassification (enterpriseid);
create index fkdfqy11c8yvskjx2yu9wa2ay8r_classificationid on party.involvedpartyxclassification (classificationid);
create index fkdinundrqqh853d336evxy3bcf_originalsourcesystemid on party.involvedpartyxclassification (originalsourcesystemid);
create index fkq80p3cmg58mvp6pb85vee4twn_systemid on party.involvedpartyxclassification (systemid);
create index fk3jrcy00rqjcc9lotsouknd88_systemid on party.involvedpartyxclassificationsecuritytoken (systemid);
create index fk6pqhjtv3yvs3afa2dg5erfbad_securitytokenid on party.involvedpartyxclassificationsecuritytoken (securitytokenid);
create index fkbd75ir0ahamjur4981w6ki6tu_originalsourcesystemid on party.involvedpartyxclassificationsecuritytoken (originalsourcesystemid);
create index fkbu7cxnreyofsgf9cdr0s3i9a1_enterpriseid on party.involvedpartyxclassificationsecuritytoken (enterpriseid);
create index fkmsyb25jnjb9yam4x4779256w2_activeflagid on party.involvedpartyxclassificationsecuritytoken (activeflagid);
create index fksmurlugdvahy37fyxobdauy42_involvedpartyxclassificationid on party.involvedpartyxclassificationsecuritytoken (involvedpartyxclassificationid);
create index fk1opo02p7o9r0k39gmafbukmdk_childinvolvedpartyid on party.involvedpartyxinvolvedparty (childinvolvedpartyid);
create index fk55h5hqcp98fursty4tgy1krgd_systemid on party.involvedpartyxinvolvedparty (systemid);
create index fk6ih93cr42n69eamwdjj8a2ygn_parentinvolvedpartyid on party.involvedpartyxinvolvedparty (parentinvolvedpartyid);
create index fkisqmvjyuuk8aqaogyeipmxtmi_classificationid on party.involvedpartyxinvolvedparty (classificationid);
create index fklrica71csov4p3987b56q8vxg_activeflagid on party.involvedpartyxinvolvedparty (activeflagid);
create index fkocrvxgjfr7ogilbsg5ija9raf_originalsourcesystemid on party.involvedpartyxinvolvedparty (originalsourcesystemid);
create index fkq53n0bblmmvb6j4n4ub3r9tea_enterpriseid on party.involvedpartyxinvolvedparty (enterpriseid);
create index fk6mi0299iv61l8tmmegrfwi2w3_involvedpartyidentificationtypeid on party.involvedpartyxinvolvedpartyidentificationtype (involvedpartyidentificationtypeid);
create index fk9k1s2vuya90it56mtuij9qk0d_enterpriseid on party.involvedpartyxinvolvedpartyidentificationtype (enterpriseid);
create index fka8ayy9j0gddpl8adc2c6ug1y1_classificationid on party.involvedpartyxinvolvedpartyidentificationtype (classificationid);
create index fkkb48h7qs2y9d208ho8l8lu5my_systemid on party.involvedpartyxinvolvedpartyidentificationtype (systemid);
create index fkn7qd136m19by1jp2xgntox5nr_involvedpartyid on party.involvedpartyxinvolvedpartyidentificationtype (involvedpartyid);
create index fkoj896cehdnvqta4i4psp2g5c_originalsourcesystemid on party.involvedpartyxinvolvedpartyidentificationtype (originalsourcesystemid);
create index fktdfx6obcjum68sqbp0u7efean_activeflagid on party.involvedpartyxinvolvedpartyidentificationtype (activeflagid);
create index fk1cih29htv8dr2nqs6113mhy20_originalsourcesystemid on party.involvedpartyxinvolvedpartyidentificationtypesecuritytoken (originalsourcesystemid);
create index fk7ot9s9hc1icogcwvd3_involpartynvolvedentificationtypeid on party.involvedpartyxinvolvedpartyidentificationtypesecuritytoken (involvedpartyxinvolvedpartyidentificationtypeid);
create index fkmt218lxijt1k3knyl48ebot0f_systemid on party.involvedpartyxinvolvedpartyidentificationtypesecuritytoken (systemid);
create index fkpk4kkstfg7ebwsy3y9pmt6sia_securitytokenid on party.involvedpartyxinvolvedpartyidentificationtypesecuritytoken (securitytokenid);
create index fkqx276013uinthqhdmr1lqpucn_enterpriseid on party.involvedpartyxinvolvedpartyidentificationtypesecuritytoken (enterpriseid);
create index fktltopjihcyxw2xet9w7toxvm1_activeflagid on party.involvedpartyxinvolvedpartyidentificationtypesecuritytoken (activeflagid);
create index fk5x12pwiby030udsbagyaqxluf_enterpriseid on party.involvedpartyxinvolvedpartynametype (enterpriseid);
create index fkiatc66e8wafha0n8xgeslpypc_involvedpartyid on party.involvedpartyxinvolvedpartynametype (involvedpartyid);
create index fkkxftjgdli2l9ro94w41wclkfx_originalsourcesystemid on party.involvedpartyxinvolvedpartynametype (originalsourcesystemid);
create index fkn2foayal7pve60wm8tps4eq67_systemid on party.involvedpartyxinvolvedpartynametype (systemid);
create index fkpi2yp7u241j1k9an8b0mttcib_activeflagid on party.involvedpartyxinvolvedpartynametype (activeflagid);
create index fkt5o5c0529tdsv531mn84tmm9f_involvedpartynametypeid on party.involvedpartyxinvolvedpartynametype (involvedpartynametypeid);
create index fktl0dvsfwf7kt9mg6hean9j9xo_classificationid on party.involvedpartyxinvolvedpartynametype (classificationid);
create index fk7xl5vyatrbubvu8on89cnkdsj_enterpriseid on party.involvedpartyxinvolvedpartynametypesecuritytoken (enterpriseid);
create index fkc7m7jk9bba59unhsafmgxbu4f_securitytokenid on party.involvedpartyxinvolvedpartynametypesecuritytoken (securitytokenid);
create index fkeafj6qgxpt157ayq3csmbo5sj_originalsourcesystemid on party.involvedpartyxinvolvedpartynametypesecuritytoken (originalsourcesystemid);
create index fkl8eohlltm8o9u06rc0tqtf5io_activeflagid on party.involvedpartyxinvolvedpartynametypesecuritytoken (activeflagid);
create index fkmkjv3992oc9plpbavw8qwtqxx_systemid on party.involvedpartyxinvolvedpartynametypesecuritytoken (systemid);
create index fknl7qjhkyalhnoouq0jt8pq_invyxinvolvedpartynametypeid on party.involvedpartyxinvolvedpartynametypesecuritytoken (involvedpartyxinvolvedpartynametypeid);
create index fk18x9igr3j8p5ug8wm2mattpsn_systemid on party.involvedpartyxinvolvedpartysecuritytoken (systemid);
create index fk6l4fxgap9wfdttqp33s0wyut4_securitytokenid on party.involvedpartyxinvolvedpartysecuritytoken (securitytokenid);
create index fkbjx132wxvnoasynr6sdwopoiv_involvedpartyxinvolvedpartyid on party.involvedpartyxinvolvedpartysecuritytoken (involvedpartyxinvolvedpartyid);
create index fkec644efp6gtbnydr4qmfmytrn_enterpriseid on party.involvedpartyxinvolvedpartysecuritytoken (enterpriseid);
create index fkgx4f05xe52w7yxl6f4mgycsyu_activeflagid on party.involvedpartyxinvolvedpartysecuritytoken (activeflagid);
create index fkp3f6axkd1puang58hu44ll5dh_originalsourcesystemid on party.involvedpartyxinvolvedpartysecuritytoken (originalsourcesystemid);
create index fk1w89pqnk0aoy7o41vm6wmidxd_classificationid on party.involvedpartyxinvolvedpartytype (classificationid);
create index fk3pqtk4c0d0raqtidrxy0kpohi_involvedpartyid on party.involvedpartyxinvolvedpartytype (involvedpartyid);
create index fkelc5v745ispspjwh2cwse92bh_systemid on party.involvedpartyxinvolvedpartytype (systemid);
create index fkj0mx9sjb7tx19f8y6g6aha7ae_enterpriseid on party.involvedpartyxinvolvedpartytype (enterpriseid);
create index fkkm9ehabqxotucg1e13vhmcgge_involvedpartytypeid on party.involvedpartyxinvolvedpartytype (involvedpartytypeid);
create index fkn68mf1pa4afxo1tx2q83kksx4_activeflagid on party.involvedpartyxinvolvedpartytype (activeflagid);
create index fkpfkqk9ya2eb37pvaq9y6pwesj_originalsourcesystemid on party.involvedpartyxinvolvedpartytype (originalsourcesystemid);
create index fk2erptg0ji67dbv45kqtodxb0c_involvedpartyxinvolvedpartytypeid on party.involvedpartyxinvolvedpartytypesecuritytoken (involvedpartyxinvolvedpartytypeid);
create index fk4jy62mro7v8k9isc2tc9ikaat_securitytokenid on party.involvedpartyxinvolvedpartytypesecuritytoken (securitytokenid);
create index fk8kb1lmh3wvovdurs0rq2jtotk_systemid on party.involvedpartyxinvolvedpartytypesecuritytoken (systemid);
create index fk9y6c21dewoi7fs477176gh0hv_enterpriseid on party.involvedpartyxinvolvedpartytypesecuritytoken (enterpriseid);
create index fkamb8ax10bbah3k81o43u11kod_activeflagid on party.involvedpartyxinvolvedpartytypesecuritytoken (activeflagid);
create index fkc10kevx7v3jol7lbh6vw0smi4_originalsourcesystemid on party.involvedpartyxinvolvedpartytypesecuritytoken (originalsourcesystemid);
create index fkau2m5a079qpprk5hhhdqum1e2_activeflagid on party.involvedpartyxproduct (activeflagid);
create index fkchhwxcvsrfev3k76luwrc1di3_productid on party.involvedpartyxproduct (productid);
create index fkm5l1oipl6mvv54tc6xx24yk06_classificationid on party.involvedpartyxproduct (classificationid);
create index fko98uqwvsmtmebl7e64anytc63_involvedpartyid on party.involvedpartyxproduct (involvedpartyid);
create index fkp5ixt88una33viktp0w23kwj6_enterpriseid on party.involvedpartyxproduct (enterpriseid);
create index fks9ps3fhh31svi0qpmwv3utwww_systemid on party.involvedpartyxproduct (systemid);
create index fkstmxsovkkg83xipa6jb1v9tqv_originalsourcesystemid on party.involvedpartyxproduct (originalsourcesystemid);
create index fk6tukdpqv5fg05yawnk4f35bun_securitytokenid on party.involvedpartyxproductsecuritytoken (securitytokenid);
create index fkbfn2hrolgktevx0ena7ia8gr1_involvedpartyxproductid on party.involvedpartyxproductsecuritytoken (involvedpartyxproductid);
create index fkghbsln22v6fh0b1jdcblved0n_originalsourcesystemid on party.involvedpartyxproductsecuritytoken (originalsourcesystemid);
create index fklvpxfkxf414rr3h2hd7qfbt6x_enterpriseid on party.involvedpartyxproductsecuritytoken (enterpriseid);
create index fkn7l4i9kxjbtg61rjqwin84k52_systemid on party.involvedpartyxproductsecuritytoken (systemid);
create index fktoih3sskv7bfum9756f55oveh_activeflagid on party.involvedpartyxproductsecuritytoken (activeflagid);
create index fk2jbtjrhepl2mad2ogocxmdr2h_systemid on party.involvedpartyxproducttype (systemid);
create index fk9qw1jha2e5bmcbfm5tv9vi4f0_enterpriseid on party.involvedpartyxproducttype (enterpriseid);
create index fkanja7q0nr3e57ilnlo9mualbo_classificationid on party.involvedpartyxproducttype (classificationid);
create index fkfwrjy39tsf1ctjt6rx6siphyo_activeflagid on party.involvedpartyxproducttype (activeflagid);
create index fkjoxnotjs00eoek4eboy9ob7x3_involvedpartyid on party.involvedpartyxproducttype (involvedpartyid);
create index fkk7gt22kjj7aajqm1i61s53dto_producttypeid on party.involvedpartyxproducttype (producttypeid);
create index fkrdm87u0ipc3rwnmd9jk22bwrk_originalsourcesystemid on party.involvedpartyxproducttype (originalsourcesystemid);
create index fk8ftebinncegtl979d5gsqnkah_originalsourcesystemid on party.involvedpartyxproducttypesecuritytoken (originalsourcesystemid);
create index fkeaiel3pwjpomb5v1gyvnswnhi_involvedpartyxproducttypeid on party.involvedpartyxproducttypesecuritytoken (involvedpartyxproducttypeid);
create index fkebb1xe5bii4fw0alp6uk7yx3g_systemid on party.involvedpartyxproducttypesecuritytoken (systemid);
create index fkjkwmptgw4rcxlvjycdbevrdbh_securitytokenid on party.involvedpartyxproducttypesecuritytoken (securitytokenid);
create index fkqt1rigtuob9p89owrlanmtlt2_enterpriseid on party.involvedpartyxproducttypesecuritytoken (enterpriseid);
create index fksidcxt1uldnhq44dumo7lesno_activeflagid on party.involvedpartyxproducttypesecuritytoken (activeflagid);
create index fk2lynx18ubx3g0afp6rv021wty_involvedpartyid on party.involvedpartyxresourceitem (involvedpartyid);
create index fk3igrqj1tpl6viwlb4jn4nlr9_activeflagid on party.involvedpartyxresourceitem (activeflagid);
create index fkbtxxv9kh2p77i3qt1nhgi5epp_classificationid on party.involvedpartyxresourceitem (classificationid);
create index fkf1eri71wcx822turop42m1raw_resourceitemid on party.involvedpartyxresourceitem (resourceitemid);
create index fkhkmp4ty2oydnlut2dtgkv8se8_enterpriseid on party.involvedpartyxresourceitem (enterpriseid);
create index fkjo97cd2ria6n18uyt9y9n1k40_originalsourcesystemid on party.involvedpartyxresourceitem (originalsourcesystemid);
create index fkqq23awk6k3o0swj127dcgd0st_systemid on party.involvedpartyxresourceitem (systemid);
create index fk27wqwvha1jdn24hktf0sx5qs9_activeflagid on party.involvedpartyxresourceitemsecuritytoken (activeflagid);
create index fk72adgv7vc7uthg1eiq4sceq89_systemid on party.involvedpartyxresourceitemsecuritytoken (systemid);
create index fkbv1ja6o5cpxl4jpwm7bneb4ys_involvedpartyxresourceitemid on party.involvedpartyxresourceitemsecuritytoken (involvedpartyxresourceitemid);
create index fkm62y4d4wr0udqi7iexcc3gaa5_originalsourcesystemid on party.involvedpartyxresourceitemsecuritytoken (originalsourcesystemid);
create index fkp8fhw7rn2g46bjsnhvss90xt0_enterpriseid on party.involvedpartyxresourceitemsecuritytoken (enterpriseid);
create index fktnbu592x1dn0dtkv0lfo7cs6b_securitytokenid on party.involvedpartyxresourceitemsecuritytoken (securitytokenid);
create index fk1306t8qbngy6vd6gt28t6w515_activeflagid on party.involvedpartyxrules (activeflagid);
create index fk6hhyqulmrh1w7g3212r7jfkaw_classificationid on party.involvedpartyxrules (classificationid);
create index fk8yk3mt51je6xtf3as7f1n5koa_enterpriseid on party.involvedpartyxrules (enterpriseid);
create index fkhd6xjhmmugixhj5pl6fdocdwy_rulesid on party.involvedpartyxrules (rulesid);
create index fkqcygcbsndr9g4owqwp22nkrdg_involvedpartyid on party.involvedpartyxrules (involvedpartyid);
create index fkscjxiiwoomhsis6829bfcljai_originalsourcesystemid on party.involvedpartyxrules (originalsourcesystemid);
create index fkwjuotwflalqakh6pqixxoa1l_systemid on party.involvedpartyxrules (systemid);
create index fk1gcn1u4lcsxql11rx2ydvwnbu_activeflagid on party.involvedpartyxrulessecuritytoken (activeflagid);
create index fk86e07awdx5h97q548mtjxnlp1_securitytokenid on party.involvedpartyxrulessecuritytoken (securitytokenid);
create index fk86h342si0g1hy4qpnkap8oq08_systemid on party.involvedpartyxrulessecuritytoken (systemid);
create index fker91inm2bcxuigrl4g4051u4o_enterpriseid on party.involvedpartyxrulessecuritytoken (enterpriseid);
create index fkm4u2pspis8jhbsy7bpti76wsd_originalsourcesystemid on party.involvedpartyxrulessecuritytoken (originalsourcesystemid);
create index fkqd2pskmew84uw4kpp9tscnftg_involvedpartyxrulesid on party.involvedpartyxrulessecuritytoken (involvedpartyxrulesid);
CREATE INDEX idx_involvedparty_effectivefromdate ON party.involvedparty (effectivefromdate);
CREATE INDEX idx_involvedparty_effectivetodate ON party.involvedparty (effectivetodate);
CREATE INDEX idx_involvedparty_warehousecreatedtimestamp ON party.involvedparty (warehousecreatedtimestamp);
CREATE INDEX idx_involvedparty_warehouselastupdatedtimestamp ON party.involvedparty (warehouselastupdatedtimestamp);
CREATE INDEX idx_involvedpartysecuritytoken_effectivefromdate ON party.involvedpartysecuritytoken (effectivefromdate);
CREATE INDEX idx_involvedpartysecuritytoken_effectivetodate ON party.involvedpartysecuritytoken (effectivetodate);
CREATE INDEX idx_involvedpartysecuritytoken_warehousecreatedtimestamp ON party.involvedpartysecuritytoken (warehousecreatedtimestamp);
CREATE INDEX idx_involvedpartysecuritytoken_warehouselastupdatedtimestamp ON party.involvedpartysecuritytoken (warehouselastupdatedtimestamp);

CREATE INDEX idx_involvedpartynonorganicsecuritytoken_effectivefromdate ON party.involvedpartynonorganicsecuritytoken (effectivefromdate);
CREATE INDEX idx_involvedpartynonorganicsecuritytoken_effectivetodate ON party.involvedpartynonorganicsecuritytoken (effectivetodate);
CREATE INDEX idx_involvedpartynonorganicsecuritytoken_warehousecreatedtimes ON party.involvedpartynonorganicsecuritytoken (warehousecreatedtimestamp);
CREATE INDEX idx_involvedpartynonorganicsecuritytoken_warehouselastupdatedt ON party.involvedpartynonorganicsecuritytoken (warehouselastupdatedtimestamp);
CREATE INDEX idx_involvedpartynonorganic_effectivefromdate ON party.involvedpartynonorganic (effectivefromdate);
CREATE INDEX idx_involvedpartynonorganic_effectivetodate ON party.involvedpartynonorganic (effectivetodate);
CREATE INDEX idx_involvedpartynonorganic_warehousecreatedtimestamp ON party.involvedpartynonorganic (warehousecreatedtimestamp);
CREATE INDEX idx_involvedpartynonorganic_warehouselastupdatedtimestamp ON party.involvedpartynonorganic (warehouselastupdatedtimestamp);

CREATE INDEX idx_involvedpartyorganicsecuritytoken_effectivefromdate ON party.involvedpartyorganicsecuritytoken (effectivefromdate);
CREATE INDEX idx_involvedpartyorganicsecuritytoken_effectivetodate ON party.involvedpartyorganicsecuritytoken (effectivetodate);
CREATE INDEX idx_involvedpartyorganicsecuritytoken_warehousecreatedtimestam ON party.involvedpartyorganicsecuritytoken (warehousecreatedtimestamp);
CREATE INDEX idx_involvedpartyorganicsecuritytoken_warehouselastupdatedtime ON party.involvedpartyorganicsecuritytoken (warehouselastupdatedtimestamp);
CREATE INDEX idx_involvedpartyidentificationtypesecuritytoken_effectivefrom ON party.involvedpartyidentificationtypesecuritytoken (effectivefromdate);
CREATE INDEX idx_involvedpartyidentificationtypesecuritytoken_effectivetoda ON party.involvedpartyidentificationtypesecuritytoken (effectivetodate);
CREATE INDEX idx_involvedpartyidentificationtypesecuritytoken_warehousecrea ON party.involvedpartyidentificationtypesecuritytoken (warehousecreatedtimestamp);
CREATE INDEX idx_involvedpartyidentificationtypesecuritytoken_warehouselast ON party.involvedpartyidentificationtypesecuritytoken (warehouselastupdatedtimestamp);
CREATE INDEX idx_involvedpartyorganictype_effectivefromdate ON party.involvedpartyorganictype (effectivefromdate);
CREATE INDEX idx_involvedpartyorganictype_effectivetodate ON party.involvedpartyorganictype (effectivetodate);
CREATE INDEX idx_involvedpartyorganictype_warehousecreatedtimestamp ON party.involvedpartyorganictype (warehousecreatedtimestamp);
CREATE INDEX idx_involvedpartyorganictype_warehouselastupdatedtimestamp ON party.involvedpartyorganictype (warehouselastupdatedtimestamp);
CREATE INDEX idx_involvedpartyorganictypesecuritytoken_effectivefromdate ON party.involvedpartyorganictypesecuritytoken (effectivefromdate);
CREATE INDEX idx_involvedpartyorganictypesecuritytoken_effectivetodate ON party.involvedpartyorganictypesecuritytoken (effectivetodate);
CREATE INDEX idx_involvedpartyorganictypesecuritytoken_warehousecreatedtime ON party.involvedpartyorganictypesecuritytoken (warehousecreatedtimestamp);
CREATE INDEX idx_involvedpartyorganictypesecuritytoken_warehouselastupdated ON party.involvedpartyorganictypesecuritytoken (warehouselastupdatedtimestamp);
CREATE INDEX idx_involvedpartyorganic_effectivefromdate ON party.involvedpartyorganic (effectivefromdate);
CREATE INDEX idx_involvedpartyorganic_effectivetodate ON party.involvedpartyorganic (effectivetodate);
CREATE INDEX idx_involvedpartyorganic_warehousecreatedtimestamp ON party.involvedpartyorganic (warehousecreatedtimestamp);
CREATE INDEX idx_involvedpartyorganic_warehouselastupdatedtimestamp ON party.involvedpartyorganic (warehouselastupdatedtimestamp);
CREATE INDEX idx_involvedpartynametype_effectivefromdate ON party.involvedpartynametype (effectivefromdate);
CREATE INDEX idx_involvedpartynametype_effectivetodate ON party.involvedpartynametype (effectivetodate);
CREATE INDEX idx_involvedpartynametype_warehousecreatedtimestamp ON party.involvedpartynametype (warehousecreatedtimestamp);
CREATE INDEX idx_involvedpartynametype_warehouselastupdatedtimestamp ON party.involvedpartynametype (warehouselastupdatedtimestamp);
CREATE INDEX idx_involvedpartynametypesecuritytoken_effectivefromdate ON party.involvedpartynametypesecuritytoken (effectivefromdate);
CREATE INDEX idx_involvedpartynametypesecuritytoken_effectivetodate ON party.involvedpartynametypesecuritytoken (effectivetodate);
CREATE INDEX idx_involvedpartynametypesecuritytoken_warehousecreatedtimesta ON party.involvedpartynametypesecuritytoken (warehousecreatedtimestamp);
CREATE INDEX idx_involvedpartynametypesecuritytoken_warehouselastupdatedtim ON party.involvedpartynametypesecuritytoken (warehouselastupdatedtimestamp);
CREATE INDEX idx_involvedpartyidentificationtype_effectivefromdate ON party.involvedpartyidentificationtype (effectivefromdate);
CREATE INDEX idx_involvedpartyidentificationtype_effectivetodate ON party.involvedpartyidentificationtype (effectivetodate);
CREATE INDEX idx_involvedpartyidentificationtype_warehousecreatedtimestamp ON party.involvedpartyidentificationtype (warehousecreatedtimestamp);
CREATE INDEX idx_involvedpartyidentificationtype_warehouselastupdatedtimest ON party.involvedpartyidentificationtype (warehouselastupdatedtimestamp);
CREATE INDEX idx_involvedpartyxclassificationsecuritytoken_effectivefromdat ON party.involvedpartyxclassificationsecuritytoken (effectivefromdate);
CREATE INDEX idx_involvedpartyxclassificationsecuritytoken_effectivetodate ON party.involvedpartyxclassificationsecuritytoken (effectivetodate);
CREATE INDEX idx_involvedpartyxclassificationsecuritytoken_warehousecreated ON party.involvedpartyxclassificationsecuritytoken (warehousecreatedtimestamp);
CREATE INDEX idx_involvedpartyxclassificationsecuritytoken_warehouselastupd ON party.involvedpartyxclassificationsecuritytoken (warehouselastupdatedtimestamp);
CREATE INDEX idx_involvedpartyxinvolvedparty_effectivefromdate ON party.involvedpartyxinvolvedparty (effectivefromdate);
CREATE INDEX idx_involvedpartyxinvolvedparty_effectivetodate ON party.involvedpartyxinvolvedparty (effectivetodate);
CREATE INDEX idx_involvedpartyxinvolvedparty_warehousecreatedtimestamp ON party.involvedpartyxinvolvedparty (warehousecreatedtimestamp);
CREATE INDEX idx_involvedpartyxinvolvedparty_warehouselastupdatedtimestamp ON party.involvedpartyxinvolvedparty (warehouselastupdatedtimestamp);
CREATE INDEX idx_involvedpartytype_effectivefromdate ON party.involvedpartytype (effectivefromdate);
CREATE INDEX idx_involvedpartytype_effectivetodate ON party.involvedpartytype (effectivetodate);
CREATE INDEX idx_involvedpartytype_warehousecreatedtimestamp ON party.involvedpartytype (warehousecreatedtimestamp);
CREATE INDEX idx_involvedpartytype_warehouselastupdatedtimestamp ON party.involvedpartytype (warehouselastupdatedtimestamp);
CREATE INDEX idx_involvedpartyxproductsecuritytoken_effectivefromdate ON party.involvedpartyxproductsecuritytoken (effectivefromdate);
CREATE INDEX idx_involvedpartyxproductsecuritytoken_effectivetodate ON party.involvedpartyxproductsecuritytoken (effectivetodate);
CREATE INDEX idx_involvedpartyxproductsecuritytoken_warehousecreatedtimesta ON party.involvedpartyxproductsecuritytoken (warehousecreatedtimestamp);
CREATE INDEX idx_involvedpartyxproductsecuritytoken_warehouselastupdatedtim ON party.involvedpartyxproductsecuritytoken (warehouselastupdatedtimestamp);
CREATE INDEX idx_involvedpartyxinvolvedpartyidentificationtype_effectivefro ON party.involvedpartyxinvolvedpartyidentificationtype (effectivefromdate);
CREATE INDEX idx_involvedpartyxinvolvedpartyidentificationtype_effectivetod ON party.involvedpartyxinvolvedpartyidentificationtype (effectivetodate);
CREATE INDEX idx_involvedpartyxinvolvedpartyidentificationtype_warehousecre ON party.involvedpartyxinvolvedpartyidentificationtype (warehousecreatedtimestamp);
CREATE INDEX idx_involvedpartyxinvolvedpartyidentificationtype_warehouselas ON party.involvedpartyxinvolvedpartyidentificationtype (warehouselastupdatedtimestamp);
CREATE INDEX idx_involvedpartytypesecuritytoken_effectivefromdate ON party.involvedpartytypesecuritytoken (effectivefromdate);
CREATE INDEX idx_involvedpartytypesecuritytoken_effectivetodate ON party.involvedpartytypesecuritytoken (effectivetodate);
CREATE INDEX idx_involvedpartytypesecuritytoken_warehousecreatedtimestamp ON party.involvedpartytypesecuritytoken (warehousecreatedtimestamp);
CREATE INDEX idx_involvedpartytypesecuritytoken_warehouselastupdatedtimesta ON party.involvedpartytypesecuritytoken (warehouselastupdatedtimestamp);
CREATE INDEX idx_involvedpartyxproducttype_effectivefromdate ON party.involvedpartyxproducttype (effectivefromdate);
CREATE INDEX idx_involvedpartyxproducttype_effectivetodate ON party.involvedpartyxproducttype (effectivetodate);
CREATE INDEX idx_involvedpartyxproducttype_warehousecreatedtimestamp ON party.involvedpartyxproducttype (warehousecreatedtimestamp);
CREATE INDEX idx_involvedpartyxproducttype_warehouselastupdatedtimestamp ON party.involvedpartyxproducttype (warehouselastupdatedtimestamp);
CREATE INDEX idx_involvedpartyxresourceitem_effectivefromdate ON party.involvedpartyxresourceitem (effectivefromdate);
CREATE INDEX idx_involvedpartyxresourceitem_effectivetodate ON party.involvedpartyxresourceitem (effectivetodate);
CREATE INDEX idx_involvedpartyxresourceitem_warehousecreatedtimestamp ON party.involvedpartyxresourceitem (warehousecreatedtimestamp);
CREATE INDEX idx_involvedpartyxresourceitem_warehouselastupdatedtimestamp ON party.involvedpartyxresourceitem (warehouselastupdatedtimestamp);
CREATE INDEX idx_involvedpartyxaddresssecuritytoken_effectivefromdate ON party.involvedpartyxaddresssecuritytoken (effectivefromdate);
CREATE INDEX idx_involvedpartyxaddresssecuritytoken_effectivetodate ON party.involvedpartyxaddresssecuritytoken (effectivetodate);
CREATE INDEX idx_involvedpartyxaddresssecuritytoken_warehousecreatedtimesta ON party.involvedpartyxaddresssecuritytoken (warehousecreatedtimestamp);
CREATE INDEX idx_involvedpartyxaddresssecuritytoken_warehouselastupdatedtim ON party.involvedpartyxaddresssecuritytoken (warehouselastupdatedtimestamp);
CREATE INDEX idx_involvedpartyxinvolvedpartysecuritytoken_effectivefromdate ON party.involvedpartyxinvolvedpartysecuritytoken (effectivefromdate);
CREATE INDEX idx_involvedpartyxinvolvedpartysecuritytoken_effectivetodate ON party.involvedpartyxinvolvedpartysecuritytoken (effectivetodate);
CREATE INDEX idx_involvedpartyxinvolvedpartysecuritytoken_warehousecreatedt ON party.involvedpartyxinvolvedpartysecuritytoken (warehousecreatedtimestamp);
CREATE INDEX idx_involvedpartyxinvolvedpartysecuritytoken_warehouselastupda ON party.involvedpartyxinvolvedpartysecuritytoken (warehouselastupdatedtimestamp);
CREATE INDEX idx_involvedpartyxrules_effectivefromdate ON party.involvedpartyxrules (effectivefromdate);
CREATE INDEX idx_involvedpartyxrules_effectivetodate ON party.involvedpartyxrules (effectivetodate);
CREATE INDEX idx_involvedpartyxrules_warehousecreatedtimestamp ON party.involvedpartyxrules (warehousecreatedtimestamp);
CREATE INDEX idx_involvedpartyxrules_warehouselastupdatedtimestamp ON party.involvedpartyxrules (warehouselastupdatedtimestamp);
CREATE INDEX idx_involvedpartyxinvolvedpartyidentificationtypesecuritytoken ON party.involvedpartyxinvolvedpartyidentificationtypesecuritytoken (effectivefromdate);
CREATE INDEX idx_involvedpartyxrulessecuritytoken_effectivefromdate ON party.involvedpartyxrulessecuritytoken (effectivefromdate);
CREATE INDEX idx_involvedpartyxrulessecuritytoken_effectivetodate ON party.involvedpartyxrulessecuritytoken (effectivetodate);
CREATE INDEX idx_involvedpartyxrulessecuritytoken_warehousecreatedtimestamp ON party.involvedpartyxrulessecuritytoken (warehousecreatedtimestamp);
CREATE INDEX idx_involvedpartyxrulessecuritytoken_warehouselastupdatedtimes ON party.involvedpartyxrulessecuritytoken (warehouselastupdatedtimestamp);
CREATE INDEX idx_involvedpartyxclassification_effectivefromdate ON party.involvedpartyxclassification (effectivefromdate);
CREATE INDEX idx_involvedpartyxclassification_effectivetodate ON party.involvedpartyxclassification (effectivetodate);
CREATE INDEX idx_involvedpartyxclassification_warehousecreatedtimestamp ON party.involvedpartyxclassification (warehousecreatedtimestamp);
CREATE INDEX idx_involvedpartyxclassification_warehouselastupdatedtimestamp ON party.involvedpartyxclassification (warehouselastupdatedtimestamp);
CREATE INDEX idx_involvedpartyxinvolvedpartytype_effectivefromdate ON party.involvedpartyxinvolvedpartytype (effectivefromdate);
CREATE INDEX idx_involvedpartyxinvolvedpartytype_effectivetodate ON party.involvedpartyxinvolvedpartytype (effectivetodate);
CREATE INDEX idx_involvedpartyxinvolvedpartytype_warehousecreatedtimestamp ON party.involvedpartyxinvolvedpartytype (warehousecreatedtimestamp);
CREATE INDEX idx_involvedpartyxinvolvedpartytype_warehouselastupdatedtimest ON party.involvedpartyxinvolvedpartytype (warehouselastupdatedtimestamp);
CREATE INDEX idx_involvedpartyxresourceitemsecuritytoken_effectivefromdate ON party.involvedpartyxresourceitemsecuritytoken (effectivefromdate);
CREATE INDEX idx_involvedpartyxresourceitemsecuritytoken_effectivetodate ON party.involvedpartyxresourceitemsecuritytoken (effectivetodate);
CREATE INDEX idx_involvedpartyxresourceitemsecuritytoken_warehousecreatedti ON party.involvedpartyxresourceitemsecuritytoken (warehousecreatedtimestamp);
CREATE INDEX idx_involvedpartyxresourceitemsecuritytoken_warehouselastupdat ON party.involvedpartyxresourceitemsecuritytoken (warehouselastupdatedtimestamp);
CREATE INDEX idx_involvedpartyxinvolvedpartytypesecuritytoken_effectivefrom ON party.involvedpartyxinvolvedpartytypesecuritytoken (effectivefromdate);
CREATE INDEX idx_involvedpartyxinvolvedpartytypesecuritytoken_effectivetoda ON party.involvedpartyxinvolvedpartytypesecuritytoken (effectivetodate);
CREATE INDEX idx_involvedpartyxinvolvedpartytypesecuritytoken_warehousecrea ON party.involvedpartyxinvolvedpartytypesecuritytoken (warehousecreatedtimestamp);
CREATE INDEX idx_involvedpartyxinvolvedpartytypesecuritytoken_warehouselast ON party.involvedpartyxinvolvedpartytypesecuritytoken (warehouselastupdatedtimestamp);
CREATE INDEX idx_involvedpartyxaddress_effectivefromdate ON party.involvedpartyxaddress (effectivefromdate);
CREATE INDEX idx_involvedpartyxaddress_effectivetodate ON party.involvedpartyxaddress (effectivetodate);
CREATE INDEX idx_involvedpartyxaddress_warehousecreatedtimestamp ON party.involvedpartyxaddress (warehousecreatedtimestamp);
CREATE INDEX idx_involvedpartyxaddress_warehouselastupdatedtimestamp ON party.involvedpartyxaddress (warehouselastupdatedtimestamp);
CREATE INDEX idx_involvedpartyxinvolvedpartynametype_effectivefromdate ON party.involvedpartyxinvolvedpartynametype (effectivefromdate);
CREATE INDEX idx_involvedpartyxinvolvedpartynametype_effectivetodate ON party.involvedpartyxinvolvedpartynametype (effectivetodate);
CREATE INDEX idx_involvedpartyxinvolvedpartynametype_warehousecreatedtimest ON party.involvedpartyxinvolvedpartynametype (warehousecreatedtimestamp);
CREATE INDEX idx_involvedpartyxinvolvedpartynametype_warehouselastupdatedti ON party.involvedpartyxinvolvedpartynametype (warehouselastupdatedtimestamp);
CREATE INDEX idx_involvedpartyxinvolvedpartynametypesecuritytoken_effective ON party.involvedpartyxinvolvedpartynametypesecuritytoken (effectivefromdate);

CREATE INDEX idx_involvedpartyxinvolvedpartynametypesecuritytoken_warehouse ON party.involvedpartyxinvolvedpartynametypesecuritytoken (warehousecreatedtimestamp);

CREATE INDEX idx_involvedpartyxproducttypesecuritytoken_effectivefromdate ON party.involvedpartyxproducttypesecuritytoken (effectivefromdate);
CREATE INDEX idx_involvedpartyxproducttypesecuritytoken_effectivetodate ON party.involvedpartyxproducttypesecuritytoken (effectivetodate);
CREATE INDEX idx_involvedpartyxproducttypesecuritytoken_warehousecreatedtim ON party.involvedpartyxproducttypesecuritytoken (warehousecreatedtimestamp);
CREATE INDEX idx_involvedpartyxproducttypesecuritytoken_warehouselastupdate ON party.involvedpartyxproducttypesecuritytoken (warehouselastupdatedtimestamp);

CREATE INDEX idx_involvedpartyxproduct_effectivefromdate ON party.involvedpartyxproduct (effectivefromdate);
CREATE INDEX idx_involvedpartyxproduct_effectivetodate ON party.involvedpartyxproduct (effectivetodate);
CREATE INDEX idx_involvedpartyxproduct_warehousecreatedtimestamp ON party.involvedpartyxproduct (warehousecreatedtimestamp);
CREATE INDEX idx_involvedpartyxproduct_warehouselastupdatedtimestamp ON party.involvedpartyxproduct (warehouselastupdatedtimestamp);


CREATE INDEX idx_involvedpartyxinvolvedparty_value ON party.involvedpartyxinvolvedparty (value);
CREATE INDEX idx_involvedpartyxinvolvedpartyidentificationtype_value ON party.involvedpartyxinvolvedpartyidentificationtype (value);
CREATE INDEX idx_involvedpartyxproducttype_value ON party.involvedpartyxproducttype (value);
CREATE INDEX idx_involvedpartyxresourceitem_value ON party.involvedpartyxresourceitem (value);
CREATE INDEX idx_involvedpartyxrules_value ON party.involvedpartyxrules (value);
CREATE INDEX idx_involvedpartyxclassification_value ON party.involvedpartyxclassification (value);
CREATE INDEX idx_involvedpartyxinvolvedpartytype_value ON party.involvedpartyxinvolvedpartytype (value);
CREATE INDEX idx_involvedpartyxaddress_value ON party.involvedpartyxaddress (value);
CREATE INDEX idx_involvedpartyxinvolvedpartynametype_value ON party.involvedpartyxinvolvedpartynametype (value);

CREATE INDEX idx_involvedpartyorganictype_involvedpartytypedesc ON party.involvedpartyorganictype (involvedpartytypedesc);
CREATE INDEX idx_involvedpartyorganictype_involvedpartytypename ON party.involvedpartyorganictype (involvedpartytypename);
CREATE INDEX idx_involvedpartynametype_involvedpartynametypename ON party.involvedpartynametype (involvedpartynametypename);
CREATE INDEX idx_involvedpartyidentificationtype_involvedpartyidentificatio ON party.involvedpartyidentificationtype (involvedpartyidentificationdesc);
--CREATE INDEX idx_involvedpartyidentificationtype_involvedpartyidentificatio ON party.involvedpartyidentificationtype (involvedpartyidentificationname);
CREATE INDEX idx_involvedpartytype_involvedpartytypedesc ON party.involvedpartytype (involvedpartytypedesc);
CREATE INDEX idx_involvedpartytype_involvedpartytypename ON party.involvedpartytype (involvedpartytypename);


CREATE INDEX idx_involvedpartyxproduct_value ON party.involvedpartyxproduct (value);

create index fk4gadr7utku5dispktvjwbl2cu_systemidwhcd on party.involvedparty (systemid, warehousefromdate);
create index fkd9g7suhvo150i79rthg96ktwu_enterpriseidwhcd on party.involvedparty (enterpriseid, warehousefromdate);
create index fkpni39h3ejdgolqlvxwoltisl2_originalsourcesystemidwhcd on party.involvedparty (originalsourcesystemid, warehousefromdate);
create index fkq6epu7tek9j6nfmtx80i7jv55_activeflagidwhcd on party.involvedparty (activeflagid, warehousefromdate);
create index fk9t4s53f0ihecoo8ufdth1vbq2_systemidwhcd on party.involvedpartyidentificationtype (systemid, warehousefromdate);
create index fkeuxx0k0eufg56jpbymmg0wnju_enterpriseidwhcd on party.involvedpartyidentificationtype (enterpriseid, warehousefromdate);
create index fkkeqqy519age78syf29vttmwu9_activeflagidwhcd on party.involvedpartyidentificationtype (activeflagid, warehousefromdate);
create index fklxima3r9ohrv77gnodycrb2su_originalsourcesystemidwhcd on party.involvedpartyidentificationtype (originalsourcesystemid, warehousefromdate);
create index fk1o1ufjc0u6yv02pfqokf19xhv_enterpriseidwhcd on party.involvedpartyidentificationtypesecuritytoken (enterpriseid, warehousefromdate);
create index fk4pb209rtplqb1uj5wtx2xr5mg_activeflagidwhcd on party.involvedpartyidentificationtypesecuritytoken (activeflagid, warehousefromdate);
create index fk7dkwueip8veb59dox32nvolvedficationtypeidwhcd on party.involvedpartyidentificationtypesecuritytoken (involvedpartyidentificationtypeid, warehousefromdate);
create index fkdvt59k7lx9fobcggw3naabwkc_originalsourcesystemidwhcd on party.involvedpartyidentificationtypesecuritytoken (originalsourcesystemid, warehousefromdate);
create index fkspj0etctwf7i8sj0gu0d6tvs8_systemidwhcd on party.involvedpartyidentificationtypesecuritytoken (systemid, warehousefromdate);
create index fkt53nic14klq9pmlooadcird6a_securitytokenidwhcd on party.involvedpartyidentificationtypesecuritytoken (securitytokenid, warehousefromdate);
create index fk2xslqqsshflrv1q0ps8p5woad_originalsourcesystemidwhcd on party.involvedpartynametype (originalsourcesystemid, warehousefromdate);
create index fka7cnvd016j105uffrbuh94xx5_activeflagidwhcd on party.involvedpartynametype (activeflagid, warehousefromdate);
create index fkaaa9iyxq2koyservqjmrfeqee_enterpriseidwhcd on party.involvedpartynametype (enterpriseid, warehousefromdate);
create index fkl18vvxbpd5f6oq57wy3jtnbp8_systemidwhcd on party.involvedpartynametype (systemid, warehousefromdate);
create index fk5dtp2b2133pffte1i7gxtom1t_enterpriseidwhcd on party.involvedpartynametypesecuritytoken (enterpriseid, warehousefromdate);
create index fk7t8d65pscktrxlw1we4idhlyj_involvedpartynametypeidwhcd on party.involvedpartynametypesecuritytoken (involvedpartynametypeid, warehousefromdate);
create index fkclphu45dfvj92ca97kfmhd0il_securitytokenidwhcd on party.involvedpartynametypesecuritytoken (securitytokenid, warehousefromdate);
create index fkdkkd4w22d3p250hyn7vwkrp6a_originalsourcesystemidwhcd on party.involvedpartynametypesecuritytoken (originalsourcesystemid, warehousefromdate);
create index fkh3e0lgk341y3qrbdp9y7eo6uf_activeflagidwhcd on party.involvedpartynametypesecuritytoken (activeflagid, warehousefromdate);
create index fkr9iq4s6tpt0gt97prluo7sia_systemidwhcd on party.involvedpartynametypesecuritytoken (systemid, warehousefromdate);
create index fk2fa0fq7vy2j3i2fog95gffalm_enterpriseidwhcd on party.involvedpartynonorganic (enterpriseid, warehousefromdate);
create index fkcphls8bnbiwk5rkf278le5kad_systemidwhcd on party.involvedpartynonorganic (systemid, warehousefromdate);
create index fklb5mxasbqff0f09q3tsqkgsi1_originalsourcesystemidwhcd on party.involvedpartynonorganic (originalsourcesystemid, warehousefromdate);
create index fkt3m2esc7actxvqhmovs8wly1p_activeflagidwhcd on party.involvedpartynonorganic (activeflagid, warehousefromdate);
create index fk4igjsit3bwp3b387pa19s1sej_involvedpartynonorganicidwhcd on party.involvedpartynonorganicsecuritytoken (involvedpartynonorganicid, warehousefromdate);
create index fk8a7thuoro1mbxsng2amjluimk_activeflagidwhcd on party.involvedpartynonorganicsecuritytoken (activeflagid, warehousefromdate);
create index fkbo9u2l1rksyb0qw3poycrj3y2_originalsourcesystemidwhcd on party.involvedpartynonorganicsecuritytoken (originalsourcesystemid, warehousefromdate);
create index fkii44sojyhs3kd1d607mnqldov_systemidwhcd on party.involvedpartynonorganicsecuritytoken (systemid, warehousefromdate);
create index fkkkks9t7wiiiku7knnyo6vvc3r_securitytokenidwhcd on party.involvedpartynonorganicsecuritytoken (securitytokenid, warehousefromdate);
create index fkp0jkfk12oi63j8blb7dvepu5i_enterpriseidwhcd on party.involvedpartynonorganicsecuritytoken (enterpriseid, warehousefromdate);
create index fk2u5a4jkumk881cv7x3ei6vv22_originalsourcesystemidwhcd on party.involvedpartyorganic (originalsourcesystemid, warehousefromdate);
create index fkf7p05lygebyxjhhtrkbn2n2md_systemidwhcd on party.involvedpartyorganic (systemid, warehousefromdate);
create index fkmkbm0yyns5o1ll48bo5yce5sc_activeflagidwhcd on party.involvedpartyorganic (activeflagid, warehousefromdate);
create index fknyksrtkiekj3cd65vtj6d641e_enterpriseidwhcd on party.involvedpartyorganic (enterpriseid, warehousefromdate);
create index fk1lqut70vjbvicq402lw3avani_originalsourcesystemidwhcd on party.involvedpartyorganicsecuritytoken (originalsourcesystemid, warehousefromdate);
create index fkeyo56crc5eg0qtbt3o79rblnp_securitytokenidwhcd on party.involvedpartyorganicsecuritytoken (securitytokenid, warehousefromdate);
create index fkg9iyrrj29vcyc3b6sxsgy95on_activeflagidwhcd on party.involvedpartyorganicsecuritytoken (activeflagid, warehousefromdate);
create index fkmyjwts72ixlq4gmviiba55a6e_enterpriseidwhcd on party.involvedpartyorganicsecuritytoken (enterpriseid, warehousefromdate);
create index fks4ev0kg4ufyi8iw19xqwn2fxb_systemidwhcd on party.involvedpartyorganicsecuritytoken (systemid, warehousefromdate);
create index fkt03i6kwyod9krg1cpoki9kc5x_involvedpartyorganicidwhcd on party.involvedpartyorganicsecuritytoken (involvedpartyorganicid, warehousefromdate);
create index fk1wmvw7vfk12aop3lrals1o1pj_systemidwhcd on party.involvedpartyorganictype (systemid, warehousefromdate);
create index fk6ei1dirma8r1y7lh4glpn9cuy_activeflagidwhcd on party.involvedpartyorganictype (activeflagid, warehousefromdate);
create index fke1juu2g32hsp6fwtfw7f9d3jx_originalsourcesystemidwhcd on party.involvedpartyorganictype (originalsourcesystemid, warehousefromdate);
create index fkq2twef17pvkati3vquevv6w8c_enterpriseidwhcd on party.involvedpartyorganictype (enterpriseid, warehousefromdate);
create index fk2rtgc2ltnx9facnuwt0aj6fpe_originalsourcesystemidwhcd on party.involvedpartyorganictypesecuritytoken (originalsourcesystemid, warehousefromdate);
create index fkejueyv4755b9ec8xoyb7wpu4x_systemidwhcd on party.involvedpartyorganictypesecuritytoken (systemid, warehousefromdate);
create index fkkj7fqfd2urqo5hgy3vxagu45t_enterpriseidwhcd on party.involvedpartyorganictypesecuritytoken (enterpriseid, warehousefromdate);
create index fkluot9f3p0qcl04j9vytxoy5w2_involvedpartyorganictypeidwhcd on party.involvedpartyorganictypesecuritytoken (involvedpartyorganictypeid, warehousefromdate);
create index fkr6a971hibsnykjeadhdgnmwm6_securitytokenidwhcd on party.involvedpartyorganictypesecuritytoken (securitytokenid, warehousefromdate);
create index fkrbdy36uy6fi319o482uvgbic1_activeflagidwhcd on party.involvedpartyorganictypesecuritytoken (activeflagid, warehousefromdate);
create index fk14xr935tw00ygoqib0cxoxmvf_securitytokenidwhcd on party.involvedpartysecuritytoken (securitytokenid, warehousefromdate);
create index fkgtrgr11h04p33g04pukt5x1sy_originalsourcesystemidwhcd on party.involvedpartysecuritytoken (originalsourcesystemid, warehousefromdate);
create index fkialc3kro04ovq8q26gg2hce1s_involvedpartyidwhcd on party.involvedpartysecuritytoken (involvedpartyid, warehousefromdate);
create index fknkgdmsl2don2yegx2qfgnykqw_enterpriseidwhcd on party.involvedpartysecuritytoken (enterpriseid, warehousefromdate);
create index fko24pnibqq5rx8t4soy88rynmd_activeflagidwhcd on party.involvedpartysecuritytoken (activeflagid, warehousefromdate);
create index fkpogqfy578mwtlm6p81g0btv3n_systemidwhcd on party.involvedpartysecuritytoken (systemid, warehousefromdate);
create index fk1v128xeri7d1pkfnltmnoo1co_enterpriseidwhcd on party.involvedpartytype (enterpriseid, warehousefromdate);
create index fk6wemtilbmlhs3b040hu5tfn7v_originalsourcesystemidwhcd on party.involvedpartytype (originalsourcesystemid, warehousefromdate);
create index fkh8ct5p1uw4f7s19svr7525boe_activeflagidwhcd on party.involvedpartytype (activeflagid, warehousefromdate);
create index fkqwx9fppibmcrytwu4t30w0f7e_systemidwhcd on party.involvedpartytype (systemid, warehousefromdate);
create index fk217dwfh6c779f14rd2w8sm1vi_securitytokenidwhcd on party.involvedpartytypesecuritytoken (securitytokenid, warehousefromdate);
create index fk3fs2knats407bhami7ulk0ch9_enterpriseidwhcd on party.involvedpartytypesecuritytoken (enterpriseid, warehousefromdate);
create index fkc8cf751cac8asi0iu3rkexxt0_systemidwhcd on party.involvedpartytypesecuritytoken (systemid, warehousefromdate);
create index fkdtx295xce0hprr5jy1la820eh_involvedpartytypeidwhcd on party.involvedpartytypesecuritytoken (involvedpartytypeid, warehousefromdate);
create index fkjhy9dh04vj80o53adnsk1xi9t_originalsourcesystemidwhcd on party.involvedpartytypesecuritytoken (originalsourcesystemid, warehousefromdate);
create index fksoyq6n0n6er6eyhn229l4oxu6_activeflagidwhcd on party.involvedpartytypesecuritytoken (activeflagid, warehousefromdate);
create index fk3ehfta90cc7ha3uo6wplucc09_addressidwhcd on party.involvedpartyxaddress (addressid, warehousefromdate);
create index fk4flitthmlghp6mj3h7maq4w84_enterpriseidwhcd on party.involvedpartyxaddress (enterpriseid, warehousefromdate);
create index fk6c2fjdp7lxcfy0ar93ni78dcv_originalsourcesystemidwhcd on party.involvedpartyxaddress (originalsourcesystemid, warehousefromdate);
create index fkdwkp9qbashp1p04uip3rg2hls_classificationidwhcd on party.involvedpartyxaddress (classificationid, warehousefromdate);
create index fkejxbg01t325qgchlwnwmpl7a0_activeflagidwhcd on party.involvedpartyxaddress (activeflagid, warehousefromdate);
create index fkf0jjy7kksugbaamt8638ktw8a_systemidwhcd on party.involvedpartyxaddress (systemid, warehousefromdate);
create index fki1pmmm070ua8yd0xrk19qq206_involvedpartyidwhcd on party.involvedpartyxaddress (involvedpartyid, warehousefromdate);
create index fk32jyotpfsamhn8afbs8xgdw5y_originalsourcesystemidwhcd on party.involvedpartyxaddresssecuritytoken (originalsourcesystemid, warehousefromdate);
create index fk58kov7xpcnh58m7m0rxhs6g30_involvedpartyxaddressidwhcd on party.involvedpartyxaddresssecuritytoken (involvedpartyxaddressid, warehousefromdate);
create index fk9h5dpjakcr9j4c61u628tte1b_activeflagidwhcd on party.involvedpartyxaddresssecuritytoken (activeflagid, warehousefromdate);
create index fkietr95ln1k6gcutfwaqg26k24_systemidwhcd on party.involvedpartyxaddresssecuritytoken (systemid, warehousefromdate);
create index fkkwbmkde209srp4k0q0s7cd1u3_enterpriseidwhcd on party.involvedpartyxaddresssecuritytoken (enterpriseid, warehousefromdate);
create index fklcmcbwu95pa7hnchv2tw2l6e4_securitytokenidwhcd on party.involvedpartyxaddresssecuritytoken (securitytokenid, warehousefromdate);
create index fk1k9352drnr2qjxk0itdlpiphl_activeflagidwhcd on party.involvedpartyxclassification (activeflagid, warehousefromdate);
create index fk1l1b8kh214rut78airw106ska_involvedpartyidwhcd on party.involvedpartyxclassification (involvedpartyid, warehousefromdate);
create index fk7jbx6dlrjcvu3ifb2s1w0ulct_enterpriseidwhcd on party.involvedpartyxclassification (enterpriseid, warehousefromdate);
create index fkdfqy11c8yvskjx2yu9wa2ay8r_classificationidwhcd on party.involvedpartyxclassification (classificationid, warehousefromdate);
create index fkdinundrqqh853d336evxy3bcf_originalsourcesystemidwhcd on party.involvedpartyxclassification (originalsourcesystemid, warehousefromdate);
create index fkq80p3cmg58mvp6pb85vee4twn_systemidwhcd on party.involvedpartyxclassification (systemid, warehousefromdate);
create index fk3jrcy00rqjcc9lotsouknd88_systemidwhcd on party.involvedpartyxclassificationsecuritytoken (systemid, warehousefromdate);
create index fk6pqhjtv3yvs3afa2dg5erfbad_securitytokenidwhcd on party.involvedpartyxclassificationsecuritytoken (securitytokenid, warehousefromdate);
create index fkbd75ir0ahamjur4981w6ki6tu_originalsourcesystemidwhcd on party.involvedpartyxclassificationsecuritytoken (originalsourcesystemid, warehousefromdate);
create index fkbu7cxnreyofsgf9cdr0s3i9a1_enterpriseidwhcd on party.involvedpartyxclassificationsecuritytoken (enterpriseid, warehousefromdate);
create index fkmsyb25jnjb9yam4x4779256w2_activeflagidwhcd on party.involvedpartyxclassificationsecuritytoken (activeflagid, warehousefromdate);
create index fksmurlugdvahy37fyxobdauy42_involvedpartyxclassificationidwhcd on party.involvedpartyxclassificationsecuritytoken (involvedpartyxclassificationid, warehousefromdate);
create index fk1opo02p7o9r0k39gmafbukmdk_childinvolvedpartyidwhcd on party.involvedpartyxinvolvedparty (childinvolvedpartyid, warehousefromdate);
create index fk55h5hqcp98fursty4tgy1krgd_systemidwhcd on party.involvedpartyxinvolvedparty (systemid, warehousefromdate);
create index fk6ih93cr42n69eamwdjj8a2ygn_parentinvolvedpartyidwhcd on party.involvedpartyxinvolvedparty (parentinvolvedpartyid, warehousefromdate);
create index fkisqmvjyuuk8aqaogyeipmxtmi_classificationidwhcd on party.involvedpartyxinvolvedparty (classificationid, warehousefromdate);
create index fklrica71csov4p3987b56q8vxg_activeflagidwhcd on party.involvedpartyxinvolvedparty (activeflagid, warehousefromdate);
create index fkocrvxgjfr7ogilbsg5ija9raf_originalsourcesystemidwhcd on party.involvedpartyxinvolvedparty (originalsourcesystemid, warehousefromdate);
create index fkq53n0bblmmvb6j4n4ub3r9tea_enterpriseidwhcd on party.involvedpartyxinvolvedparty (enterpriseid, warehousefromdate);
create index fk6mi0299iv61l8tmmegrfwi2w3_involvificationtypeidwhcd on party.involvedpartyxinvolvedpartyidentificationtype (involvedpartyidentificationtypeid, warehousefromdate);
create index fk9k1s2vuya90it56mtuij9qk0d_enterpriseidwhcd on party.involvedpartyxinvolvedpartyidentificationtype (enterpriseid, warehousefromdate);
create index fka8ayy9j0gddpl8adc2c6ug1y1_classificationidwhcd on party.involvedpartyxinvolvedpartyidentificationtype (classificationid, warehousefromdate);
create index fkkb48h7qs2y9d208ho8l8lu5my_systemidwhcd on party.involvedpartyxinvolvedpartyidentificationtype (systemid, warehousefromdate);
create index fkn7qd136m19by1jp2xgntox5nr_involvedpartyidwhcd on party.involvedpartyxinvolvedpartyidentificationtype (involvedpartyid, warehousefromdate);
create index fkoj896cehdnvqta4i4psp2g5c_originalsourcesystemidwhcd on party.involvedpartyxinvolvedpartyidentificationtype (originalsourcesystemid, warehousefromdate);
create index fktdfx6obcjum68sqbp0u7efean_activeflagidwhcd on party.involvedpartyxinvolvedpartyidentificationtype (activeflagid, warehousefromdate);
create index fk1cih29htv8dr2nqs6113mhy20_originalsourcesystemidwhcd on party.involvedpartyxinvolvedpartyidentificationtypesecuritytoken (originalsourcesystemid, warehousefromdate);
create index fk7ot9s9hc1w0icwm3ogcwhqvd3_involvedpartyxinvolydwhcd on party.involvedpartyxinvolvedpartyidentificationtypesecuritytoken (involvedpartyxinvolvedpartyidentificationtypeid, warehousefromdate);
create index fkmt218lxijt1k3knyl48ebot0f_systemidwhcd on party.involvedpartyxinvolvedpartyidentificationtypesecuritytoken (systemid, warehousefromdate);
create index fkpk4kkstfg7ebwsy3y9pmt6sia_securitytokenidwhcd on party.involvedpartyxinvolvedpartyidentificationtypesecuritytoken (securitytokenid, warehousefromdate);
create index fkqx276013uinthqhdmr1lqpucn_enterpriseidwhcd on party.involvedpartyxinvolvedpartyidentificationtypesecuritytoken (enterpriseid, warehousefromdate);
create index fktltopjihcyxw2xet9w7toxvm1_activeflagidwhcd on party.involvedpartyxinvolvedpartyidentificationtypesecuritytoken (activeflagid, warehousefromdate);
create index fk5x12pwiby030udsbagyaqxluf_enterpriseidwhcd on party.involvedpartyxinvolvedpartynametype (enterpriseid, warehousefromdate);
create index fkiatc66e8wafha0n8xgeslpypc_involvedpartyidwhcd on party.involvedpartyxinvolvedpartynametype (involvedpartyid, warehousefromdate);
create index fkkxftjgdli2l9ro94w41wclkfx_originalsourcesystemidwhcd on party.involvedpartyxinvolvedpartynametype (originalsourcesystemid, warehousefromdate);
create index fkn2foayal7pve60wm8tps4eq67_systemidwhcd on party.involvedpartyxinvolvedpartynametype (systemid, warehousefromdate);
create index fkpi2yp7u241j1k9an8b0mttcib_activeflagidwhcd on party.involvedpartyxinvolvedpartynametype (activeflagid, warehousefromdate);
create index fkt5o5c0529tdsv531mn84tmm9f_involvedpartynametypeidwhcd on party.involvedpartyxinvolvedpartynametype (involvedpartynametypeid, warehousefromdate);
create index fktl0dvsfwf7kt9mg6hean9j9xo_classificationidwhcd on party.involvedpartyxinvolvedpartynametype (classificationid, warehousefromdate);
create index fk7xl5vyatrbubvu8on89cnkdsj_enterpriseidwhcd on party.involvedpartyxinvolvedpartynametypesecuritytoken (enterpriseid, warehousefromdate);
create index fkc7m7jk9bba59unhsafmgxbu4f_securitytokenidwhcd on party.involvedpartyxinvolvedpartynametypesecuritytoken (securitytokenid, warehousefromdate);
create index fkeafj6qgxpt157ayq3csmbo5sj_originalsourcesystemidwhcd on party.involvedpartyxinvolvedpartynametypesecuritytoken (originalsourcesystemid, warehousefromdate);
create index fkl8eohlltm8o9u06rc0tqtf5io_activeflagidwhcd on party.involvedpartyxinvolvedpartynametypesecuritytoken (activeflagid, warehousefromdate);
create index fkmkjv3992oc9plpbavw8qwtqxx_systemidwhcd on party.involvedpartyxinvolvedpartynametypesecuritytoken (systemid, warehousefromdate);
create index fknl7qjhkyalhn6skoouq0jt8pq_involvedpartyxinvolvedidwhcd on party.involvedpartyxinvolvedpartynametypesecuritytoken (involvedpartyxinvolvedpartynametypeid, warehousefromdate);
create index fk18x9igr3j8p5ug8wm2mattpsn_systemidwhcd on party.involvedpartyxinvolvedpartysecuritytoken (systemid, warehousefromdate);
create index fk6l4fxgap9wfdttqp33s0wyut4_securitytokenidwhcd on party.involvedpartyxinvolvedpartysecuritytoken (securitytokenid, warehousefromdate);
create index fkbjx132wxvnoasynr6sdwopoiv_involvedpartyxinvolvedpartyidwhcd on party.involvedpartyxinvolvedpartysecuritytoken (involvedpartyxinvolvedpartyid, warehousefromdate);
create index fkec644efp6gtbnydr4qmfmytrn_enterpriseidwhcd on party.involvedpartyxinvolvedpartysecuritytoken (enterpriseid, warehousefromdate);
create index fkgx4f05xe52w7yxl6f4mgycsyu_activeflagidwhcd on party.involvedpartyxinvolvedpartysecuritytoken (activeflagid, warehousefromdate);
create index fkp3f6axkd1puang58hu44ll5dh_originalsourcesystemidwhcd on party.involvedpartyxinvolvedpartysecuritytoken (originalsourcesystemid, warehousefromdate);
create index fk1w89pqnk0aoy7o41vm6wmidxd_classificationidwhcd on party.involvedpartyxinvolvedpartytype (classificationid, warehousefromdate);
create index fk3pqtk4c0d0raqtidrxy0kpohi_involvedpartyidwhcd on party.involvedpartyxinvolvedpartytype (involvedpartyid, warehousefromdate);
create index fkelc5v745ispspjwh2cwse92bh_systemidwhcd on party.involvedpartyxinvolvedpartytype (systemid, warehousefromdate);
create index fkj0mx9sjb7tx19f8y6g6aha7ae_enterpriseidwhcd on party.involvedpartyxinvolvedpartytype (enterpriseid, warehousefromdate);
create index fkkm9ehabqxotucg1e13vhmcgge_involvedpartytypeidwhcd on party.involvedpartyxinvolvedpartytype (involvedpartytypeid, warehousefromdate);
create index fkn68mf1pa4afxo1tx2q83kksx4_activeflagidwhcd on party.involvedpartyxinvolvedpartytype (activeflagid, warehousefromdate);
create index fkpfkqk9ya2eb37pvaq9y6pwesj_originalsourcesystemidwhcd on party.involvedpartyxinvolvedpartytype (originalsourcesystemid, warehousefromdate);
create index fk2erptg0ji67dbv45kqtodxb0c_involvedpartyxintypeidwhcd on party.involvedpartyxinvolvedpartytypesecuritytoken (involvedpartyxinvolvedpartytypeid, warehousefromdate);
create index fk4jy62mro7v8k9isc2tc9ikaat_securitytokenidwhcd on party.involvedpartyxinvolvedpartytypesecuritytoken (securitytokenid, warehousefromdate);
create index fk8kb1lmh3wvovdurs0rq2jtotk_systemidwhcd on party.involvedpartyxinvolvedpartytypesecuritytoken (systemid, warehousefromdate);
create index fk9y6c21dewoi7fs477176gh0hv_enterpriseidwhcd on party.involvedpartyxinvolvedpartytypesecuritytoken (enterpriseid, warehousefromdate);
create index fkamb8ax10bbah3k81o43u11kod_activeflagidwhcd on party.involvedpartyxinvolvedpartytypesecuritytoken (activeflagid, warehousefromdate);
create index fkc10kevx7v3jol7lbh6vw0smi4_originalsourcesystemidwhcd on party.involvedpartyxinvolvedpartytypesecuritytoken (originalsourcesystemid, warehousefromdate);
create index fkau2m5a079qpprk5hhhdqum1e2_activeflagidwhcd on party.involvedpartyxproduct (activeflagid, warehousefromdate);
create index fkchhwxcvsrfev3k76luwrc1di3_productidwhcd on party.involvedpartyxproduct (productid, warehousefromdate);
create index fkm5l1oipl6mvv54tc6xx24yk06_classificationidwhcd on party.involvedpartyxproduct (classificationid, warehousefromdate);
create index fko98uqwvsmtmebl7e64anytc63_involvedpartyidwhcd on party.involvedpartyxproduct (involvedpartyid, warehousefromdate);
create index fkp5ixt88una33viktp0w23kwj6_enterpriseidwhcd on party.involvedpartyxproduct (enterpriseid, warehousefromdate);
create index fks9ps3fhh31svi0qpmwv3utwww_systemidwhcd on party.involvedpartyxproduct (systemid, warehousefromdate);
create index fkstmxsovkkg83xipa6jb1v9tqv_originalsourcesystemidwhcd on party.involvedpartyxproduct (originalsourcesystemid, warehousefromdate);
create index fk6tukdpqv5fg05yawnk4f35bun_securitytokenidwhcd on party.involvedpartyxproductsecuritytoken (securitytokenid, warehousefromdate);
create index fkbfn2hrolgktevx0ena7ia8gr1_involvedpartyxproductidwhcd on party.involvedpartyxproductsecuritytoken (involvedpartyxproductid, warehousefromdate);
create index fkghbsln22v6fh0b1jdcblved0n_originalsourcesystemidwhcd on party.involvedpartyxproductsecuritytoken (originalsourcesystemid, warehousefromdate);
create index fklvpxfkxf414rr3h2hd7qfbt6x_enterpriseidwhcd on party.involvedpartyxproductsecuritytoken (enterpriseid, warehousefromdate);
create index fkn7l4i9kxjbtg61rjqwin84k52_systemidwhcd on party.involvedpartyxproductsecuritytoken (systemid, warehousefromdate);
create index fktoih3sskv7bfum9756f55oveh_activeflagidwhcd on party.involvedpartyxproductsecuritytoken (activeflagid, warehousefromdate);
create index fk2jbtjrhepl2mad2ogocxmdr2h_systemidwhcd on party.involvedpartyxproducttype (systemid, warehousefromdate);
create index fk9qw1jha2e5bmcbfm5tv9vi4f0_enterpriseidwhcd on party.involvedpartyxproducttype (enterpriseid, warehousefromdate);
create index fkanja7q0nr3e57ilnlo9mualbo_classificationidwhcd on party.involvedpartyxproducttype (classificationid, warehousefromdate);
create index fkfwrjy39tsf1ctjt6rx6siphyo_activeflagidwhcd on party.involvedpartyxproducttype (activeflagid, warehousefromdate);
create index fkjoxnotjs00eoek4eboy9ob7x3_involvedpartyidwhcd on party.involvedpartyxproducttype (involvedpartyid, warehousefromdate);
create index fkk7gt22kjj7aajqm1i61s53dto_producttypeidwhcd on party.involvedpartyxproducttype (producttypeid, warehousefromdate);
create index fkrdm87u0ipc3rwnmd9jk22bwrk_originalsourcesystemidwhcd on party.involvedpartyxproducttype (originalsourcesystemid, warehousefromdate);
create index fk8ftebinncegtl979d5gsqnkah_originalsourcesystemidwhcd on party.involvedpartyxproducttypesecuritytoken (originalsourcesystemid, warehousefromdate);
create index fkeaiel3pwjpomb5v1gyvnswnhi_involvedpartyxproducttypeidwhcd on party.involvedpartyxproducttypesecuritytoken (involvedpartyxproducttypeid, warehousefromdate);
create index fkebb1xe5bii4fw0alp6uk7yx3g_systemidwhcd on party.involvedpartyxproducttypesecuritytoken (systemid, warehousefromdate);
create index fkjkwmptgw4rcxlvjycdbevrdbh_securitytokenidwhcd on party.involvedpartyxproducttypesecuritytoken (securitytokenid, warehousefromdate);
create index fkqt1rigtuob9p89owrlanmtlt2_enterpriseidwhcd on party.involvedpartyxproducttypesecuritytoken (enterpriseid, warehousefromdate);
create index fksidcxt1uldnhq44dumo7lesno_activeflagidwhcd on party.involvedpartyxproducttypesecuritytoken (activeflagid, warehousefromdate);
create index fk2lynx18ubx3g0afp6rv021wty_involvedpartyidwhcd on party.involvedpartyxresourceitem (involvedpartyid, warehousefromdate);
create index fk3igrqj1tpl6viwlb4jn4nlr9_activeflagidwhcd on party.involvedpartyxresourceitem (activeflagid, warehousefromdate);
create index fkbtxxv9kh2p77i3qt1nhgi5epp_classificationidwhcd on party.involvedpartyxresourceitem (classificationid, warehousefromdate);
create index fkf1eri71wcx822turop42m1raw_resourceitemidwhcd on party.involvedpartyxresourceitem (resourceitemid, warehousefromdate);
create index fkhkmp4ty2oydnlut2dtgkv8se8_enterpriseidwhcd on party.involvedpartyxresourceitem (enterpriseid, warehousefromdate);
create index fkjo97cd2ria6n18uyt9y9n1k40_originalsourcesystemidwhcd on party.involvedpartyxresourceitem (originalsourcesystemid, warehousefromdate);
create index fkqq23awk6k3o0swj127dcgd0st_systemidwhcd on party.involvedpartyxresourceitem (systemid, warehousefromdate);
create index fk27wqwvha1jdn24hktf0sx5qs9_activeflagidwhcd on party.involvedpartyxresourceitemsecuritytoken (activeflagid, warehousefromdate);
create index fk72adgv7vc7uthg1eiq4sceq89_systemidwhcd on party.involvedpartyxresourceitemsecuritytoken (systemid, warehousefromdate);
create index fkbv1ja6o5cpxl4jpwm7bneb4ys_involvedpartyxresourceitemidwhcd on party.involvedpartyxresourceitemsecuritytoken (involvedpartyxresourceitemid, warehousefromdate);
create index fkm62y4d4wr0udqi7iexcc3gaa5_originalsourcesystemidwhcd on party.involvedpartyxresourceitemsecuritytoken (originalsourcesystemid, warehousefromdate);
create index fkp8fhw7rn2g46bjsnhvss90xt0_enterpriseidwhcd on party.involvedpartyxresourceitemsecuritytoken (enterpriseid, warehousefromdate);
create index fktnbu592x1dn0dtkv0lfo7cs6b_securitytokenidwhcd on party.involvedpartyxresourceitemsecuritytoken (securitytokenid, warehousefromdate);
create index fk1306t8qbngy6vd6gt28t6w515_activeflagidwhcd on party.involvedpartyxrules (activeflagid, warehousefromdate);
create index fk6hhyqulmrh1w7g3212r7jfkaw_classificationidwhcd on party.involvedpartyxrules (classificationid, warehousefromdate);
create index fk8yk3mt51je6xtf3as7f1n5koa_enterpriseidwhcd on party.involvedpartyxrules (enterpriseid, warehousefromdate);
create index fkhd6xjhmmugixhj5pl6fdocdwy_rulesidwhcd on party.involvedpartyxrules (rulesid, warehousefromdate);
create index fkqcygcbsndr9g4owqwp22nkrdg_involvedpartyidwhcd on party.involvedpartyxrules (involvedpartyid, warehousefromdate);
create index fkscjxiiwoomhsis6829bfcljai_originalsourcesystemidwhcd on party.involvedpartyxrules (originalsourcesystemid, warehousefromdate);
create index fkwjuotwflalqakh6pqixxoa1l_systemidwhcd on party.involvedpartyxrules (systemid, warehousefromdate);
create index fk1gcn1u4lcsxql11rx2ydvwnbu_activeflagidwhcd on party.involvedpartyxrulessecuritytoken (activeflagid, warehousefromdate);
create index fk86e07awdx5h97q548mtjxnlp1_securitytokenidwhcd on party.involvedpartyxrulessecuritytoken (securitytokenid, warehousefromdate);
create index fk86h342si0g1hy4qpnkap8oq08_systemidwhcd on party.involvedpartyxrulessecuritytoken (systemid, warehousefromdate);
create index fker91inm2bcxuigrl4g4051u4o_enterpriseidwhcd on party.involvedpartyxrulessecuritytoken (enterpriseid, warehousefromdate);
create index fkm4u2pspis8jhbsy7bpti76wsd_originalsourcesystemidwhcd on party.involvedpartyxrulessecuritytoken (originalsourcesystemid, warehousefromdate);
create index fkqd2pskmew84uw4kpp9tscnftg_involvedpartyxrulesidwhcd on party.involvedpartyxrulessecuritytoken (involvedpartyxrulesid, warehousefromdate);



