CREATE SCHEMA event;
CREATE TABLE event.event
(
    eventid                       UUID                        NOT NULL primary key,
    effectivefromdate             timestamp(6) with time zone NOT NULL DEFAULT now(),
    effectivetodate               timestamp(6) with time zone NOT NULL DEFAULT '2999-12-31 23:59:59.999+00',
    warehousecreatedtimestamp     timestamp(6) with time zone NOT NULL DEFAULT now(),
    warehousefromdate             DATE                        NOT NULL DEFAULT current_date,

    warehouselastupdatedtimestamp timestamp(6) with time zone NOT NULL DEFAULT now(),
    originalsourcesystemuniqueid  UUID                        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    dayid                         INTEGER                     NOT NULL,
    hourid                        INTEGER                     NOT NULL,
    minuteid                      INTEGER                     NOT NULL,
    activeflagid                  UUID                        NOT NULL,
    enterpriseid                  UUID                        NOT NULL,
    systemid                      UUID                        NOT NULL,
    originalsourcesystemid        UUID                        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000'
);
CREATE TABLE event.eventsecuritytoken
(
    eventssecuritytokenid         UUID                        NOT NULL primary key,
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
    eventsid                      UUID                        NOT NULL
);
CREATE TABLE event.eventtype
(
    eventtypeid                   UUID                        NOT NULL primary key,
    effectivefromdate             timestamp(6) with time zone NOT NULL DEFAULT now(),
    effectivetodate               timestamp(6) with time zone NOT NULL DEFAULT '2999-12-31 23:59:59.999+00',
    warehousecreatedtimestamp     timestamp(6) with time zone NOT NULL DEFAULT now(),
    warehousefromdate             DATE                        NOT NULL DEFAULT current_date,

    warehouselastupdatedtimestamp timestamp(6) with time zone NOT NULL DEFAULT now(),
    originalsourcesystemuniqueid  UUID                        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    eventtypedesc                 character varying(200)      NOT NULL,
    eventtypename                 character varying(200)      NOT NULL,
    activeflagid                  UUID                        NOT NULL,
    enterpriseid                  UUID                        NOT NULL,
    systemid                      UUID                        NOT NULL,
    originalsourcesystemid        UUID                        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000'
);

CREATE TABLE event.eventtypessecuritytoken
(
    eventtypessecuritytokenid     UUID                        NOT NULL primary key,
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
    eventtypesid                  UUID                        NOT NULL
);
CREATE TABLE event.eventxaddress
(
    eventxaddressid               UUID                        NOT NULL primary key,
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
    eventid                       UUID                        NOT NULL
);
CREATE TABLE event.eventxaddresssecuritytoken
(
    eventxaddresssecuritytokenid  UUID                        NOT NULL primary key,
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
    eventxaddressid               UUID                        NOT NULL
);
CREATE TABLE event.eventxarrangement
(
    eventxarrangementsid          UUID                        NOT NULL primary key,
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
    arrangementid                 UUID                        NOT NULL,
    eventid                       UUID                        NOT NULL
);
CREATE TABLE event.eventxarrangementssecuritytoken
(
    eventxarrangementssecuritytokenid UUID                        NOT NULL primary key,
    effectivefromdate                 timestamp(6) with time zone NOT NULL DEFAULT now(),
    effectivetodate                   timestamp(6) with time zone NOT NULL DEFAULT '2999-12-31 23:59:59.999+00',
    warehousecreatedtimestamp         timestamp(6) with time zone NOT NULL DEFAULT now(),
    warehousefromdate                 DATE                        NOT NULL DEFAULT current_date,

    warehouselastupdatedtimestamp     timestamp(6) with time zone NOT NULL DEFAULT now(),
    createallowed                     INTEGER                     NOT NULL,
    deleteallowed                     INTEGER                     NOT NULL,
    originalsourcesystemuniqueid      UUID                        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    readallowed                       INTEGER                     NOT NULL,
    updateallowed                     INTEGER                     NOT NULL,
    activeflagid                      UUID                        NOT NULL,
    enterpriseid                      UUID                        NOT NULL,
    originalsourcesystemid            UUID                        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    securitytokenid                   UUID                        NOT NULL,
    systemid                          UUID                        NOT NULL,
    eventxarrangementsid              UUID                        NOT NULL
);
CREATE TABLE event.eventxclassification
(
    eventxclassificationid        UUID                        NOT NULL primary key,
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
    eventid                       UUID                        NOT NULL
);
CREATE TABLE event.eventxclassificationsecuritytoken
(
    eventxclassificationssecuritytokenid UUID                        NOT NULL primary key,
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
    eventxclassificationsid              UUID                        NOT NULL
);
CREATE TABLE event.eventxevent
(
    eventxeventid                 UUID                        NOT NULL primary key,
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
    childeventid                  UUID                        NOT NULL,
    parenteventid                 UUID                        NOT NULL
);
CREATE TABLE event.eventxeventsecuritytoken
(
    eventxeventsecuritytokenid    UUID                        NOT NULL primary key,
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
    eventxeventid                 UUID                        NOT NULL
);
CREATE TABLE event.eventxeventtype
(
    eventxeventtypeid             UUID                        NOT NULL primary key,
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
    eventid                       UUID                        NOT NULL,
    eventtypeid                   UUID                        NOT NULL
);
CREATE TABLE event.eventxeventtypesecuritytoken
(
    eventxeventtypesecuritytokenid UUID                        NOT NULL primary key,
    effectivefromdate              timestamp(6) with time zone NOT NULL DEFAULT now(),
    effectivetodate                timestamp(6) with time zone NOT NULL DEFAULT '2999-12-31 23:59:59.999+00',
    warehousecreatedtimestamp      timestamp(6) with time zone NOT NULL DEFAULT now(),
    warehousefromdate              DATE                        NOT NULL DEFAULT current_date,

    warehouselastupdatedtimestamp  timestamp(6) with time zone NOT NULL DEFAULT now(),
    createallowed                  INTEGER                     NOT NULL,
    deleteallowed                  INTEGER                     NOT NULL,
    originalsourcesystemuniqueid   UUID                        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    readallowed                    INTEGER                     NOT NULL,
    updateallowed                  INTEGER                     NOT NULL,
    activeflagid                   UUID                        NOT NULL,
    enterpriseid                   UUID                        NOT NULL,
    originalsourcesystemid         UUID                        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    securitytokenid                UUID                        NOT NULL,
    systemid                       UUID                        NOT NULL,
    eventxeventtypeid              UUID                        NOT NULL
);
CREATE TABLE event.eventxgeography
(
    eventxgeographyid             UUID                        NOT NULL primary key,
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
    eventid                       UUID                        NOT NULL,
    geographyid                   UUID                        NOT NULL
);
CREATE TABLE event.eventxgeographysecuritytoken
(
    eventxgeographysecuritytokenid UUID                        NOT NULL primary key,
    effectivefromdate              timestamp(6) with time zone NOT NULL DEFAULT now(),
    effectivetodate                timestamp(6) with time zone NOT NULL DEFAULT '2999-12-31 23:59:59.999+00',
    warehousecreatedtimestamp      timestamp(6) with time zone NOT NULL DEFAULT now(),
    warehousefromdate              DATE                        NOT NULL DEFAULT current_date,

    warehouselastupdatedtimestamp  timestamp(6) with time zone NOT NULL DEFAULT now(),
    createallowed                  INTEGER                     NOT NULL,
    deleteallowed                  INTEGER                     NOT NULL,
    originalsourcesystemuniqueid   UUID                        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    readallowed                    INTEGER                     NOT NULL,
    updateallowed                  INTEGER                     NOT NULL,
    activeflagid                   UUID                        NOT NULL,
    enterpriseid                   UUID                        NOT NULL,
    originalsourcesystemid         UUID                        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    securitytokenid                UUID                        NOT NULL,
    systemid                       UUID                        NOT NULL,
    eventxgeographyid              UUID                        NOT NULL
);
CREATE TABLE event.eventxinvolvedparty
(
    eventxinvolvedpartyid         UUID                        NOT NULL primary key,
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
    eventid                       UUID                        NOT NULL,
    involvedpartyid               UUID                        NOT NULL
);
CREATE TABLE event.eventxinvolvedpartysecuritytoken
(
    eventxinvolvedpartysecuritytokenid UUID                        NOT NULL primary key,
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
    eventxinvolvedpartyid              UUID                        NOT NULL
);
CREATE TABLE event.eventxproduct
(
    eventxproductid               UUID                        NOT NULL primary key,
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
    eventid                       UUID                        NOT NULL,
    productid                     UUID                        NOT NULL
);
CREATE TABLE event.eventxproductsecuritytoken
(
    eventxproductsecuritytokenid  UUID                        NOT NULL primary key,
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
    eventxproductid               UUID                        NOT NULL
);
CREATE TABLE event.eventxresourceitem
(
    eventxresourceitemid          UUID                        NOT NULL primary key,
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
    eventid                       UUID                        NOT NULL,
    resourceitemid                UUID                        NOT NULL
);
CREATE TABLE event.eventxresourceitemsecuritytoken
(
    eventxresourceitemsecuritytokenid UUID                        NOT NULL primary key,
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
    securitytokenid                    UUID                        NOT NULL,
    systemid                           UUID                        NOT NULL,
    eventxresourceitemid              UUID                        NOT NULL
);
CREATE TABLE event.eventxrules
(
    eventxrulesid                 UUID                        NOT NULL primary key,
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
    eventid                       UUID                        NOT NULL,
    rulesid                       UUID                        NOT NULL
);
CREATE TABLE event.eventxrulessecuritytoken
(
    eventxrulessecuritytokenid    UUID                        NOT NULL primary key,
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
    eventxrulesid                 UUID                        NOT NULL
);
-- Indexes for event.event
CREATE INDEX idx_ev_eff_from ON event.event (effectivefromdate);
CREATE INDEX idx_ev_eff_to ON event.event (effectivetodate);
CREATE INDEX idx_ev_wh_created ON event.event (warehousecreatedtimestamp);
CREATE INDEX idx_ev_wh_updated ON event.event (warehouselastupdatedtimestamp);
CREATE INDEX idx_ev_ei_wh ON event.event (enterpriseid, warehousefromdate);
CREATE INDEX idx_ev_af_wh ON event.event (activeflagid, warehousefromdate);
CREATE INDEX idx_ev_sys_wh ON event.event (systemid, warehousefromdate);

-- Indexes for event.eventsecuritytoken
CREATE INDEX idx_evst_eff_from ON event.eventsecuritytoken (effectivefromdate);
CREATE INDEX idx_evst_eff_to ON event.eventsecuritytoken (effectivetodate);
CREATE INDEX idx_evst_wh_created ON event.eventsecuritytoken (warehousecreatedtimestamp);
CREATE INDEX idx_evst_wh_updated ON event.eventsecuritytoken (warehouselastupdatedtimestamp);
CREATE INDEX idx_evst_ei_wh ON event.eventsecuritytoken (enterpriseid, warehousefromdate);
CREATE INDEX idx_evst_af_wh ON event.eventsecuritytoken (activeflagid, warehousefromdate);
CREATE INDEX idx_evst_sid_wh ON event.eventsecuritytoken (eventsid, warehousefromdate);

-- Indexes for event.eventtype
CREATE INDEX idx_evt_eff_from ON event.eventtype (effectivefromdate);
CREATE INDEX idx_evt_eff_to ON event.eventtype (effectivetodate);
CREATE INDEX idx_evt_wh_created ON event.eventtype (warehousecreatedtimestamp);
CREATE INDEX idx_evt_wh_updated ON event.eventtype (warehouselastupdatedtimestamp);
CREATE INDEX idx_evt_ei_wh ON event.eventtype (enterpriseid, warehousefromdate);
CREATE INDEX idx_evt_af_wh ON event.eventtype (activeflagid, warehousefromdate);
CREATE INDEX idx_evt_sys_wh ON event.eventtype (systemid, warehousefromdate);

-- Indexes for event.eventtypessecuritytoken
CREATE INDEX idx_evtst_eff_from ON event.eventtypessecuritytoken (effectivefromdate);
CREATE INDEX idx_evtst_eff_to ON event.eventtypessecuritytoken (effectivetodate);
CREATE INDEX idx_evtst_wh_created ON event.eventtypessecuritytoken (warehousecreatedtimestamp);
CREATE INDEX idx_evtst_wh_updated ON event.eventtypessecuritytoken (warehouselastupdatedtimestamp);
CREATE INDEX idx_evtst_ei_wh ON event.eventtypessecuritytoken (enterpriseid, warehousefromdate);
CREATE INDEX idx_evtst_af_wh ON event.eventtypessecuritytoken (activeflagid, warehousefromdate);
CREATE INDEX idx_evtst_sid_wh ON event.eventtypessecuritytoken (systemid, warehousefromdate);
CREATE INDEX idx_evtst_tid_wh ON event.eventtypessecuritytoken (eventtypesid, warehousefromdate);

-- Indexes for event.eventxaddress
CREATE INDEX idx_evxa_eff_from ON event.eventxaddress (effectivefromdate);
CREATE INDEX idx_evxa_eff_to ON event.eventxaddress (effectivetodate);
CREATE INDEX idx_evxa_wh_created ON event.eventxaddress (warehousecreatedtimestamp);
CREATE INDEX idx_evxa_wh_updated ON event.eventxaddress (warehouselastupdatedtimestamp);
CREATE INDEX idx_evxa_ei_wh ON event.eventxaddress (enterpriseid, warehousefromdate);
CREATE INDEX idx_evxa_af_wh ON event.eventxaddress (activeflagid, warehousefromdate);
CREATE INDEX idx_evxa_sys_wh ON event.eventxaddress (systemid, warehousefromdate);
CREATE INDEX idx_evxa_cl_wh ON event.eventxaddress (classificationid, warehousefromdate);
CREATE INDEX idx_evxa_sid_wh ON event.eventxaddress (eventid, warehousefromdate);

-- Indexes for event.eventxaddresssecuritytoken
CREATE INDEX idx_evxast_eff_from ON event.eventxaddresssecuritytoken (effectivefromdate);
CREATE INDEX idx_evxast_eff_to ON event.eventxaddresssecuritytoken (effectivetodate);
CREATE INDEX idx_evxast_wh_created ON event.eventxaddresssecuritytoken (warehousecreatedtimestamp);
CREATE INDEX idx_evxast_wh_updated ON event.eventxaddresssecuritytoken (warehouselastupdatedtimestamp);
CREATE INDEX idx_evxast_ei_wh ON event.eventxaddresssecuritytoken (enterpriseid, warehousefromdate);
CREATE INDEX idx_evxast_af_wh ON event.eventxaddresssecuritytoken (activeflagid, warehousefromdate);
CREATE INDEX idx_evxast_sys_wh ON event.eventxaddresssecuritytoken (systemid, warehousefromdate);
CREATE INDEX idx_evxast_sid_wh ON event.eventxaddresssecuritytoken (eventxaddressid, warehousefromdate);

-- Indexes for event.eventxarrangement
CREATE INDEX idx_evxarr_eff_from ON event.eventxarrangement (effectivefromdate);
CREATE INDEX idx_evxarr_eff_to ON event.eventxarrangement (effectivetodate);
CREATE INDEX idx_evxarr_wh_created ON event.eventxarrangement (warehousecreatedtimestamp);
CREATE INDEX idx_evxarr_wh_updated ON event.eventxarrangement (warehouselastupdatedtimestamp);
CREATE INDEX idx_evxarr_ei_wh ON event.eventxarrangement (enterpriseid, warehousefromdate);
CREATE INDEX idx_evxarr_af_wh ON event.eventxarrangement (activeflagid, warehousefromdate);
CREATE INDEX idx_evxarr_sys_wh ON event.eventxarrangement (systemid, warehousefromdate);
CREATE INDEX idx_evxarr_cl_wh ON event.eventxarrangement (classificationid, warehousefromdate);
CREATE INDEX idx_evxarr_sid_wh ON event.eventxarrangement (eventid, warehousefromdate);

-- Indexes for event.eventxarrangementssecuritytoken
CREATE INDEX idx_evxarrst_eff_from ON event.eventxarrangementssecuritytoken (effectivefromdate);
CREATE INDEX idx_evxarrst_eff_to ON event.eventxarrangementssecuritytoken (effectivetodate);
CREATE INDEX idx_evxarrst_wh_created ON event.eventxarrangementssecuritytoken (warehousecreatedtimestamp);
CREATE INDEX idx_evxarrst_wh_updated ON event.eventxarrangementssecuritytoken (warehouselastupdatedtimestamp);
CREATE INDEX idx_evxarrst_ei_wh ON event.eventxarrangementssecuritytoken (enterpriseid, warehousefromdate);
CREATE INDEX idx_evxarrst_af_wh ON event.eventxarrangementssecuritytoken (activeflagid, warehousefromdate);
CREATE INDEX idx_evxarrst_sys_wh ON event.eventxarrangementssecuritytoken (systemid, warehousefromdate);
CREATE INDEX idx_evxarrst_sid_wh ON event.eventxarrangementssecuritytoken (eventxarrangementsid, warehousefromdate);

-- Indexes for event.eventxclassification
CREATE INDEX idx_evxc_eff_from ON event.eventxclassification (effectivefromdate);
CREATE INDEX idx_evxc_eff_to ON event.eventxclassification (effectivetodate);
CREATE INDEX idx_evxc_wh_created ON event.eventxclassification (warehousecreatedtimestamp);
CREATE INDEX idx_evxc_wh_updated ON event.eventxclassification (warehouselastupdatedtimestamp);
CREATE INDEX idx_evxc_ei_wh ON event.eventxclassification (enterpriseid, warehousefromdate);
CREATE INDEX idx_evxc_af_wh ON event.eventxclassification (activeflagid, warehousefromdate);
CREATE INDEX idx_evxc_sys_wh ON event.eventxclassification (systemid, warehousefromdate);
CREATE INDEX idx_evxc_cl_wh ON event.eventxclassification (classificationid, warehousefromdate);
CREATE INDEX idx_evxc_sid_wh ON event.eventxclassification (eventid, warehousefromdate);

-- Indexes for event.eventxclassificationsecuritytoken
CREATE INDEX idx_evxcst_eff_from ON event.eventxclassificationsecuritytoken (effectivefromdate);
CREATE INDEX idx_evxcst_eff_to ON event.eventxclassificationsecuritytoken (effectivetodate);
CREATE INDEX idx_evxcst_wh_created ON event.eventxclassificationsecuritytoken (warehousecreatedtimestamp);
CREATE INDEX idx_evxcst_wh_updated ON event.eventxclassificationsecuritytoken (warehouselastupdatedtimestamp);
CREATE INDEX idx_evxcst_ei_wh ON event.eventxclassificationsecuritytoken (enterpriseid, warehousefromdate);
CREATE INDEX idx_evxcst_af_wh ON event.eventxclassificationsecuritytoken (activeflagid, warehousefromdate);
CREATE INDEX idx_evxcst_sys_wh ON event.eventxclassificationsecuritytoken (systemid, warehousefromdate);
CREATE INDEX idx_evxcst_sid_wh ON event.eventxclassificationsecuritytoken (eventxclassificationsid, warehousefromdate);

-- Indexes for event.eventxevent
CREATE INDEX idx_evxe_eff_from ON event.eventxevent (effectivefromdate);
CREATE INDEX idx_evxe_eff_to ON event.eventxevent (effectivetodate);
CREATE INDEX idx_evxe_wh_created ON event.eventxevent (warehousecreatedtimestamp);
CREATE INDEX idx_evxe_wh_updated ON event.eventxevent (warehouselastupdatedtimestamp);
CREATE INDEX idx_evxe_ei_wh ON event.eventxevent (enterpriseid, warehousefromdate);
CREATE INDEX idx_evxe_af_wh ON event.eventxevent (activeflagid, warehousefromdate);
CREATE INDEX idx_evxe_sys_wh ON event.eventxevent (systemid, warehousefromdate);
CREATE INDEX idx_evxe_cl_wh ON event.eventxevent (classificationid, warehousefromdate);

-- Indexes for event.eventxeventsecuritytoken
CREATE INDEX idx_evxest_eff_from ON event.eventxeventsecuritytoken (effectivefromdate);
CREATE INDEX idx_evxest_eff_to ON event.eventxeventsecuritytoken (effectivetodate);
CREATE INDEX idx_evxest_wh_created ON event.eventxeventsecuritytoken (warehousecreatedtimestamp);
CREATE INDEX idx_evxest_wh_updated ON event.eventxeventsecuritytoken (warehouselastupdatedtimestamp);
CREATE INDEX idx_evxest_ei_wh ON event.eventxeventsecuritytoken (enterpriseid, warehousefromdate);
CREATE INDEX idx_evxest_af_wh ON event.eventxeventsecuritytoken (activeflagid, warehousefromdate);
CREATE INDEX idx_evxest_sys_wh ON event.eventxeventsecuritytoken (systemid, warehousefromdate);

-- Indexes for event.eventxeventtype
CREATE INDEX idx_evxet_eff_from ON event.eventxeventtype (effectivefromdate);
CREATE INDEX idx_evxet_eff_to ON event.eventxeventtype (effectivetodate);
CREATE INDEX idx_evxet_wh_created ON event.eventxeventtype (warehousecreatedtimestamp);
CREATE INDEX idx_evxet_wh_updated ON event.eventxeventtype (warehouselastupdatedtimestamp);
CREATE INDEX idx_evxet_ei_wh ON event.eventxeventtype (enterpriseid, warehousefromdate);
CREATE INDEX idx_evxet_af_wh ON event.eventxeventtype (activeflagid, warehousefromdate);
CREATE INDEX idx_evxet_sys_wh ON event.eventxeventtype (systemid, warehousefromdate);
CREATE INDEX idx_evxet_cl_wh ON event.eventxeventtype (classificationid, warehousefromdate);
CREATE INDEX idx_evxet_sid_wh ON event.eventxeventtype (eventid, warehousefromdate);

-- Indexes for event.eventxgeography
CREATE INDEX idx_evxg_eff_from ON event.eventxgeography (effectivefromdate);
CREATE INDEX idx_evxg_eff_to ON event.eventxgeography (effectivetodate);
CREATE INDEX idx_evxg_wh_created ON event.eventxgeography (warehousecreatedtimestamp);
CREATE INDEX idx_evxg_wh_updated ON event.eventxgeography (warehouselastupdatedtimestamp);
CREATE INDEX idx_evxg_ei_wh ON event.eventxgeography (enterpriseid, warehousefromdate);
CREATE INDEX idx_evxg_af_wh ON event.eventxgeography (activeflagid, warehousefromdate);
CREATE INDEX idx_evxg_sys_wh ON event.eventxgeography (systemid, warehousefromdate);
CREATE INDEX idx_evxg_cl_wh ON event.eventxgeography (classificationid, warehousefromdate);
CREATE INDEX idx_evxg_sid_wh ON event.eventxgeography (eventid, warehousefromdate);

-- Indexes for event.eventxgeographysecuritytoken
CREATE INDEX idx_evxgst_eff_from ON event.eventxgeographysecuritytoken (effectivefromdate);
CREATE INDEX idx_evxgst_eff_to ON event.eventxgeographysecuritytoken (effectivetodate);
CREATE INDEX idx_evxgst_wh_created ON event.eventxgeographysecuritytoken (warehousecreatedtimestamp);
CREATE INDEX idx_evxgst_wh_updated ON event.eventxgeographysecuritytoken (warehouselastupdatedtimestamp);
CREATE INDEX idx_evxgst_ei_wh ON event.eventxgeographysecuritytoken (enterpriseid, warehousefromdate);
CREATE INDEX idx_evxgst_af_wh ON event.eventxgeographysecuritytoken (activeflagid, warehousefromdate);
CREATE INDEX idx_evxgst_sys_wh ON event.eventxgeographysecuritytoken (systemid, warehousefromdate);

-- Indexes for event.eventxinvolvedparty
CREATE INDEX idx_evxip_eff_from ON event.eventxinvolvedparty (effectivefromdate);
CREATE INDEX idx_evxip_eff_to ON event.eventxinvolvedparty (effectivetodate);
CREATE INDEX idx_evxip_wh_created ON event.eventxinvolvedparty (warehousecreatedtimestamp);
CREATE INDEX idx_evxip_wh_updated ON event.eventxinvolvedparty (warehouselastupdatedtimestamp);
CREATE INDEX idx_evxip_ei_wh ON event.eventxinvolvedparty (enterpriseid, warehousefromdate);
CREATE INDEX idx_evxip_af_wh ON event.eventxinvolvedparty (activeflagid, warehousefromdate);
CREATE INDEX idx_evxip_sys_wh ON event.eventxinvolvedparty (systemid, warehousefromdate);
CREATE INDEX idx_evxip_cl_wh ON event.eventxinvolvedparty (classificationid, warehousefromdate);
CREATE INDEX idx_evxip_sid_wh ON event.eventxinvolvedparty (eventid, warehousefromdate);

-- Indexes for event.eventxinvolvedpartysecuritytoken
CREATE INDEX idx_evxipst_eff_from ON event.eventxinvolvedpartysecuritytoken (effectivefromdate);
CREATE INDEX idx_evxipst_eff_to ON event.eventxinvolvedpartysecuritytoken (effectivetodate);
CREATE INDEX idx_evxipst_wh_created ON event.eventxinvolvedpartysecuritytoken (warehousecreatedtimestamp);
CREATE INDEX idx_evxipst_wh_updated ON event.eventxinvolvedpartysecuritytoken (warehouselastupdatedtimestamp);
CREATE INDEX idx_evxipst_ei_wh ON event.eventxinvolvedpartysecuritytoken (enterpriseid, warehousefromdate);
CREATE INDEX idx_evxipst_af_wh ON event.eventxinvolvedpartysecuritytoken (activeflagid, warehousefromdate);
CREATE INDEX idx_evxipst_sys_wh ON event.eventxinvolvedpartysecuritytoken (systemid, warehousefromdate);

-- Indexes for event.eventxproduct
CREATE INDEX idx_evxp_eff_from ON event.eventxproduct (effectivefromdate);
CREATE INDEX idx_evxp_eff_to ON event.eventxproduct (effectivetodate);
CREATE INDEX idx_evxp_wh_created ON event.eventxproduct (warehousecreatedtimestamp);
CREATE INDEX idx_evxp_wh_updated ON event.eventxproduct (warehouselastupdatedtimestamp);
CREATE INDEX idx_evxp_ei_wh ON event.eventxproduct (enterpriseid, warehousefromdate);
CREATE INDEX idx_evxp_af_wh ON event.eventxproduct (activeflagid, warehousefromdate);
CREATE INDEX idx_evxp_sys_wh ON event.eventxproduct (systemid, warehousefromdate);
CREATE INDEX idx_evxp_cl_wh ON event.eventxproduct (classificationid, warehousefromdate);
CREATE INDEX idx_evxp_sid_wh ON event.eventxproduct (eventid, warehousefromdate);

-- Indexes for event.eventxproductsecuritytoken
CREATE INDEX idx_evxpst_eff_from ON event.eventxproductsecuritytoken (effectivefromdate);
CREATE INDEX idx_evxpst_eff_to ON event.eventxproductsecuritytoken (effectivetodate);
CREATE INDEX idx_evxpst_wh_created ON event.eventxproductsecuritytoken (warehousecreatedtimestamp);
CREATE INDEX idx_evxpst_wh_updated ON event.eventxproductsecuritytoken (warehouselastupdatedtimestamp);
CREATE INDEX idx_evxpst_ei_wh ON event.eventxproductsecuritytoken (enterpriseid, warehousefromdate);
CREATE INDEX idx_evxpst_af_wh ON event.eventxproductsecuritytoken (activeflagid, warehousefromdate);
CREATE INDEX idx_evxpst_sys_wh ON event.eventxproductsecuritytoken (systemid, warehousefromdate);

-- Indexes for event.eventxresourceitem
CREATE INDEX idx_evxrist_eff_from ON event.eventxresourceitem (effectivefromdate);
CREATE INDEX idx_evxrist_eff_to ON event.eventxresourceitem (effectivetodate);
CREATE INDEX idx_evxrist_wh_created ON event.eventxresourceitem (warehousecreatedtimestamp);
CREATE INDEX idx_evxrist_wh_updated ON event.eventxresourceitem (warehouselastupdatedtimestamp);
CREATE INDEX idx_evxrist_ei_wh ON event.eventxresourceitem (enterpriseid, warehousefromdate);
CREATE INDEX idx_evxrist_af_wh ON event.eventxresourceitem (activeflagid, warehousefromdate);
CREATE INDEX idx_evxrist_sys_wh ON event.eventxresourceitem (systemid, warehousefromdate);
CREATE INDEX idx_evxrist_cl_wh ON event.eventxresourceitem (classificationid, warehousefromdate);
CREATE INDEX idx_evxrist_sid_wh ON event.eventxresourceitem (eventid, warehousefromdate);

-- Indexes for event.eventxresourceitemsecuritytoken
CREATE INDEX idx_evxristst_eff_from ON event.eventxresourceitemsecuritytoken (effectivefromdate);
CREATE INDEX idx_evxristst_eff_to ON event.eventxresourceitemsecuritytoken (effectivetodate);
CREATE INDEX idx_evxristst_wh_created ON event.eventxresourceitemsecuritytoken (warehousecreatedtimestamp);
CREATE INDEX idx_evxristst_wh_updated ON event.eventxresourceitemsecuritytoken (warehouselastupdatedtimestamp);
CREATE INDEX idx_evxristst_ei_wh ON event.eventxresourceitemsecuritytoken (enterpriseid, warehousefromdate);
CREATE INDEX idx_evxristst_af_wh ON event.eventxresourceitemsecuritytoken (activeflagid, warehousefromdate);
CREATE INDEX idx_evxristst_sys_wh ON event.eventxresourceitemsecuritytoken (systemid, warehousefromdate);

-- Indexes for event.eventxrules
CREATE INDEX idx_evxr_eff_from ON event.eventxrules (effectivefromdate);
CREATE INDEX idx_evxr_eff_to ON event.eventxrules (effectivetodate);
CREATE INDEX idx_evxr_wh_created ON event.eventxrules (warehousecreatedtimestamp);
CREATE INDEX idx_evxr_wh_updated ON event.eventxrules (warehouselastupdatedtimestamp);
CREATE INDEX idx_evxr_ei_wh ON event.eventxrules (enterpriseid, warehousefromdate);
CREATE INDEX idx_evxr_af_wh ON event.eventxrules (activeflagid, warehousefromdate);
CREATE INDEX idx_evxr_sys_wh ON event.eventxrules (systemid, warehousefromdate);
CREATE INDEX idx_evxr_cl_wh ON event.eventxrules (classificationid, warehousefromdate);
CREATE INDEX idx_evxr_sid_wh ON event.eventxrules (eventid, warehousefromdate);

-- Indexes for event.eventxrulessecuritytoken
CREATE INDEX idx_evxrst_eff_from ON event.eventxrulessecuritytoken (effectivefromdate);
CREATE INDEX idx_evxrst_eff_to ON event.eventxrulessecuritytoken (effectivetodate);
CREATE INDEX idx_evxrst_wh_created ON event.eventxrulessecuritytoken (warehousecreatedtimestamp);
CREATE INDEX idx_evxrst_wh_updated ON event.eventxrulessecuritytoken (warehouselastupdatedtimestamp);
CREATE INDEX idx_evxrst_ei_wh ON event.eventxrulessecuritytoken (enterpriseid, warehousefromdate);
CREATE INDEX idx_evxrst_af_wh ON event.eventxrulessecuritytoken (activeflagid, warehousefromdate);
CREATE INDEX idx_evxrst_sys_wh ON event.eventxrulessecuritytoken (systemid, warehousefromdate);

