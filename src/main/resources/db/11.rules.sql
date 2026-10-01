DO $fsdm_schema$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_catalog.pg_namespace WHERE nspname = 'rules') THEN
        CREATE SCHEMA IF NOT EXISTS rules;
    END IF;
END;
$fsdm_schema$;
CREATE TABLE IF NOT EXISTS rules.rules
(
    rulesid                       UUID                        NOT NULL primary key,
    effectivefromdate             timestamp(6) with time zone NOT NULL DEFAULT now(),
    effectivetodate               timestamp(6) with time zone NOT NULL DEFAULT '2999-12-31 23:59:59.999+00',
    warehousecreatedtimestamp     timestamp(6) with time zone NOT NULL DEFAULT now(),
    warehousefromdate             DATE                        NOT NULL DEFAULT current_date,

    warehouselastupdatedtimestamp timestamp(6) with time zone NOT NULL DEFAULT now(),
    originalsourcesystemuniqueid  UUID                        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    rulesetdescription            character varying(250)      NOT NULL,
    rulesetname                   character varying(150)      NOT NULL,
    activeflagid                  UUID                        NOT NULL,
    enterpriseid                  UUID                        NOT NULL,
    systemid                      UUID                        NOT NULL,
    originalsourcesystemid        UUID                        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000'
);
CREATE TABLE IF NOT EXISTS rules.rulessecuritytoken
(
    rulessecuritytokenid          UUID                        NOT NULL primary key,
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
    rulesid                       UUID                        NOT NULL
);
CREATE TABLE IF NOT EXISTS rules.rulestype
(
    rulestypeid                   UUID                        NOT NULL primary key,
    effectivefromdate             timestamp(6) with time zone NOT NULL DEFAULT now(),
    effectivetodate               timestamp(6) with time zone NOT NULL DEFAULT '2999-12-31 23:59:59.999+00',
    warehousecreatedtimestamp     timestamp(6) with time zone NOT NULL DEFAULT now(),
    warehousefromdate             DATE                        NOT NULL DEFAULT current_date,

    warehouselastupdatedtimestamp timestamp(6) with time zone NOT NULL DEFAULT now(),
    originalsourcesystemuniqueid  UUID                        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    rulestypedesc                 character varying(200)      NOT NULL,
    rulestypename                 character varying(200)      NOT NULL,
    activeflagid                  UUID                        NOT NULL,
    enterpriseid                  UUID                        NOT NULL,
    systemid                      UUID                        NOT NULL,
    originalsourcesystemid        UUID                        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000'
);
CREATE TABLE IF NOT EXISTS rules.rulestypessecuritytoken
(
    rulestypessecuritytokenid     UUID                        NOT NULL primary key,
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
    rulestypesid                  UUID                        NOT NULL
);
CREATE TABLE IF NOT EXISTS rules.rulestypexclassification
(
    rulestypexclassificationid    UUID                        NOT NULL primary key,
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
    rulestypeid                   UUID                        NOT NULL
);
CREATE TABLE IF NOT EXISTS rules.rulestypexclassificationsecuritytoken
(
    rulestypexclassificationsecuritytokenid UUID                        NOT NULL primary key,
    effectivefromdate                         timestamp(6) with time zone NOT NULL DEFAULT now(),
    effectivetodate                           timestamp(6) with time zone NOT NULL DEFAULT '2999-12-31 23:59:59.999+00',
    warehousecreatedtimestamp                 timestamp(6) with time zone NOT NULL DEFAULT now(),
    warehousefromdate                         DATE                        NOT NULL DEFAULT current_date,

    warehouselastupdatedtimestamp           timestamp(6) with time zone NOT NULL DEFAULT now(),
    createallowed                           INTEGER                     NOT NULL,
    deleteallowed                           INTEGER                     NOT NULL,
    originalsourcesystemuniqueid            UUID                        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    readallowed                             INTEGER                     NOT NULL,
    updateallowed                           INTEGER                     NOT NULL,
    activeflagid                            UUID                        NOT NULL,
    enterpriseid                            UUID                        NOT NULL,
    originalsourcesystemid                   UUID                        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    securitytokenid                         UUID                        NOT NULL,
    systemid                                UUID                        NOT NULL,
    rulestypexclassificationid              UUID                        NOT NULL
);
CREATE TABLE IF NOT EXISTS rules.rulestypexresourceitem
(
    rulestypexresourceitemid      UUID                        NOT NULL primary key,
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
    resourceitemid                UUID                        NOT NULL,
    rulestypeid                   UUID                        NOT NULL
);
CREATE TABLE IF NOT EXISTS rules.rulestypexresourceitemsecuritytoken
(
    rulestypexresourceitemsecuritytokenid UUID                        NOT NULL primary key,
    effectivefromdate                     timestamp(6) with time zone NOT NULL DEFAULT now(),
    effectivetodate                       timestamp(6) with time zone NOT NULL DEFAULT '2999-12-31 23:59:59.999+00',
    warehousecreatedtimestamp             timestamp(6) with time zone NOT NULL DEFAULT now(),
    warehousefromdate                     DATE                        NOT NULL DEFAULT current_date,

    warehouselastupdatedtimestamp         timestamp(6) with time zone NOT NULL DEFAULT now(),
    createallowed                         INTEGER                     NOT NULL,
    deleteallowed                         INTEGER                     NOT NULL,
    originalsourcesystemuniqueid          UUID                        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    readallowed                           INTEGER                     NOT NULL,
    updateallowed                         INTEGER                     NOT NULL,
    activeflagid                          UUID                        NOT NULL,
    enterpriseid                          UUID                        NOT NULL,
    originalsourcesystemid                UUID                        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    securitytokenid                      UUID                        NOT NULL,
    systemid                              UUID                        NOT NULL,
    rulestypexresourceitemid              UUID                        NOT NULL
);
CREATE TABLE IF NOT EXISTS rules.rulesxarrangement
(
    rulesxarrangementsid          UUID                        NOT NULL primary key,
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
    rulesid                       UUID                        NOT NULL
);
CREATE TABLE IF NOT EXISTS rules.rulesxarrangementssecuritytoken
(
    rulesxarrangementssecuritytokenid UUID                        NOT NULL primary key,
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
    rulesxarrangementsid              UUID                        NOT NULL
);
CREATE TABLE IF NOT EXISTS rules.rulesxclassification
(
    rulesxclassificationid        UUID                        NOT NULL primary key,
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
    rulesid                       UUID                        NOT NULL
);
CREATE TABLE IF NOT EXISTS rules.rulesxclassificationsecuritytoken
(
    rulesxclassificationsecuritytokenid UUID                        NOT NULL primary key,
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
    rulesxclassificationid              UUID                        NOT NULL
);
CREATE TABLE IF NOT EXISTS rules.rulesxinvolvedparty
(
    rulesxinvolvedpartyid         UUID                        NOT NULL primary key,
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
CREATE TABLE IF NOT EXISTS rules.rulesxinvolvedpartysecuritytoken
(
    rulesxinvolvedpartysecuritytokenid UUID                        NOT NULL primary key,
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
    rulesxinvolvedpartyid              UUID                        NOT NULL
);
CREATE TABLE IF NOT EXISTS rules.rulesxproduct
(
    rulesxproductid               UUID                        NOT NULL primary key,
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
    productid                     UUID                        NOT NULL,
    rulesid                       UUID                        NOT NULL
);
CREATE TABLE IF NOT EXISTS rules.rulesxproductsecuritytoken
(
    rulesxproductsecuritytokenid  UUID                        NOT NULL primary key,
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
    rulesxproductid               UUID                        NOT NULL
);
CREATE TABLE IF NOT EXISTS rules.rulesxresourceitem
(
    rulesxresourceitemid          UUID                        NOT NULL primary key,
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
    resourceitemid                UUID                        NOT NULL,
    rulesid                       UUID                        NOT NULL
);
CREATE TABLE IF NOT EXISTS rules.rulesxresourceitemsecuritytoken
(
    rulesxresourceitemsecuritytokenid UUID                        NOT NULL primary key,
    effectivefromdate                 timestamp(6) with time zone NOT NULL DEFAULT now(),
    effectivetodate                   timestamp(6) with time zone NOT NULL DEFAULT '2999-12-31 23:59:59.999+00',
    warehousecreatedtimestamp           timestamp(6) with time zone NOT NULL DEFAULT now(),
    warehousefromdate                 DATE                        NOT NULL DEFAULT current_date,

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
    rulesxresourceitemid              UUID                        NOT NULL
);
CREATE TABLE IF NOT EXISTS rules.rulesxrules
(
    rulesxrulesid                 UUID                        NOT NULL primary key,
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
    childrulesid                  UUID                        NOT NULL,
    parentrulesid                 UUID                        NOT NULL
);
CREATE TABLE IF NOT EXISTS rules.rulesxrulessecuritytoken
(
    rulesxrulessecuritytokenid    UUID                        NOT NULL primary key,
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
    rulesxrulesid                 UUID                        NOT NULL
);
CREATE TABLE IF NOT EXISTS rules.rulesxrulestype
(
    rulesxrulestypeid             UUID                        NOT NULL primary key,
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
    rulesid                       UUID                        NOT NULL,
    rulestypeid                   UUID                        NOT NULL
);
CREATE TABLE IF NOT EXISTS rules.rulesxrulestypessecuritytoken
(
    rulesxrulestypessecuritytokenid UUID                        NOT NULL primary key,
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
    rulesxrulestypeid                    UUID                        NOT NULL
);



-- Indexes for rules.rules
CREATE INDEX IF NOT EXISTS idx_rul_eff_from ON rules.rules (effectivefromdate);
CREATE INDEX IF NOT EXISTS idx_rul_eff_to ON rules.rules (effectivetodate);
CREATE INDEX IF NOT EXISTS idx_rul_wh_created ON rules.rules (warehousecreatedtimestamp);
CREATE INDEX IF NOT EXISTS idx_rul_wh_updated ON rules.rules (warehouselastupdatedtimestamp);
CREATE INDEX IF NOT EXISTS idx_rul_ei_wh ON rules.rules (enterpriseid, warehousefromdate);
CREATE INDEX IF NOT EXISTS idx_rul_af_wh ON rules.rules (activeflagid, warehousefromdate);
CREATE INDEX IF NOT EXISTS idx_rul_sys_wh ON rules.rules (systemid, warehousefromdate);

-- Indexes for rules.rulessecuritytoken
CREATE INDEX IF NOT EXISTS idx_rulst_eff_from ON rules.rulessecuritytoken (effectivefromdate);
CREATE INDEX IF NOT EXISTS idx_rulst_eff_to ON rules.rulessecuritytoken (effectivetodate);
CREATE INDEX IF NOT EXISTS idx_rulst_wh_created ON rules.rulessecuritytoken (warehousecreatedtimestamp);
CREATE INDEX IF NOT EXISTS idx_rulst_wh_updated ON rules.rulessecuritytoken (warehouselastupdatedtimestamp);
CREATE INDEX IF NOT EXISTS idx_rulst_ei_wh ON rules.rulessecuritytoken (enterpriseid, warehousefromdate);
CREATE INDEX IF NOT EXISTS idx_rulst_af_wh ON rules.rulessecuritytoken (activeflagid, warehousefromdate);
CREATE INDEX IF NOT EXISTS idx_rulst_sid_wh ON rules.rulessecuritytoken (securitytokenid, warehousefromdate);
CREATE INDEX IF NOT EXISTS idx_rulst_rid_wh ON rules.rulessecuritytoken (rulesid, warehousefromdate);
CREATE INDEX IF NOT EXISTS idx_rulst_sys_wh ON rules.rulessecuritytoken (systemid, warehousefromdate);

-- Indexes for rules.rulestype
CREATE INDEX IF NOT EXISTS idx_rlt_eff_from ON rules.rulestype (effectivefromdate);
CREATE INDEX IF NOT EXISTS idx_rlt_eff_to ON rules.rulestype (effectivetodate);
CREATE INDEX IF NOT EXISTS idx_rlt_wh_created ON rules.rulestype (warehousecreatedtimestamp);
CREATE INDEX IF NOT EXISTS idx_rlt_wh_updated ON rules.rulestype (warehouselastupdatedtimestamp);
CREATE INDEX IF NOT EXISTS idx_rlt_ei_wh ON rules.rulestype (enterpriseid, warehousefromdate);
CREATE INDEX IF NOT EXISTS idx_rlt_af_wh ON rules.rulestype (activeflagid, warehousefromdate);
CREATE INDEX IF NOT EXISTS idx_rlt_sys_wh ON rules.rulestype (systemid, warehousefromdate);

-- Indexes for rules.rulestypessecuritytoken
CREATE INDEX IF NOT EXISTS idx_rltst_eff_from ON rules.rulestypessecuritytoken (effectivefromdate);
CREATE INDEX IF NOT EXISTS idx_rltst_eff_to ON rules.rulestypessecuritytoken (effectivetodate);
CREATE INDEX IF NOT EXISTS idx_rltst_wh_created ON rules.rulestypessecuritytoken (warehousecreatedtimestamp);
CREATE INDEX IF NOT EXISTS idx_rltst_wh_updated ON rules.rulestypessecuritytoken (warehouselastupdatedtimestamp);
CREATE INDEX IF NOT EXISTS idx_rltst_ei_wh ON rules.rulestypessecuritytoken (enterpriseid, warehousefromdate);
CREATE INDEX IF NOT EXISTS idx_rltst_af_wh ON rules.rulestypessecuritytoken (activeflagid, warehousefromdate);
CREATE INDEX IF NOT EXISTS idx_rltst_st_wh ON rules.rulestypessecuritytoken (securitytokenid, warehousefromdate);
CREATE INDEX IF NOT EXISTS idx_rltst_sid_wh ON rules.rulestypessecuritytoken (systemid, warehousefromdate);
CREATE INDEX IF NOT EXISTS idx_rltst_tid_wh ON rules.rulestypessecuritytoken (rulestypesid, warehousefromdate);

-- Indexes for rules.rulestypexclassification
CREATE INDEX IF NOT EXISTS idx_rltxc_eff_from ON rules.rulestypexclassification (effectivefromdate);
CREATE INDEX IF NOT EXISTS idx_rltxc_eff_to ON rules.rulestypexclassification (effectivetodate);
CREATE INDEX IF NOT EXISTS idx_rltxc_wh_created ON rules.rulestypexclassification (warehousecreatedtimestamp);
CREATE INDEX IF NOT EXISTS idx_rltxc_wh_updated ON rules.rulestypexclassification (warehouselastupdatedtimestamp);
CREATE INDEX IF NOT EXISTS idx_rltxc_val ON rules.rulestypexclassification (value);
CREATE INDEX IF NOT EXISTS idx_rltxc_ei_wh ON rules.rulestypexclassification (enterpriseid, warehousefromdate);
CREATE INDEX IF NOT EXISTS idx_rltxc_af_wh ON rules.rulestypexclassification (activeflagid, warehousefromdate);
CREATE INDEX IF NOT EXISTS idx_rltxc_sys_wh ON rules.rulestypexclassification (systemid, warehousefromdate);
CREATE INDEX IF NOT EXISTS idx_rltxc_tid_wh ON rules.rulestypexclassification (rulestypeid, warehousefromdate);

-- Indexes for rules.rulestypexclassificationsecuritytoken
CREATE INDEX IF NOT EXISTS idx_rltxcst_eff_from ON rules.rulestypexclassificationsecuritytoken (effectivefromdate);
CREATE INDEX IF NOT EXISTS idx_rltxcst_eff_to ON rules.rulestypexclassificationsecuritytoken (effectivetodate);
CREATE INDEX IF NOT EXISTS idx_rltxcst_wh_created ON rules.rulestypexclassificationsecuritytoken (warehousecreatedtimestamp);
CREATE INDEX IF NOT EXISTS idx_rltxcst_wh_updated ON rules.rulestypexclassificationsecuritytoken (warehouselastupdatedtimestamp);
CREATE INDEX IF NOT EXISTS idx_rltxcst_ei_wh ON rules.rulestypexclassificationsecuritytoken (enterpriseid, warehousefromdate);
CREATE INDEX IF NOT EXISTS idx_rltxcst_st_wh ON rules.rulestypexclassificationsecuritytoken (securitytokenid, warehousefromdate);
CREATE INDEX IF NOT EXISTS idx_rltxcst_sys_wh ON rules.rulestypexclassificationsecuritytoken (systemid, warehousefromdate);
CREATE INDEX IF NOT EXISTS idx_rltxcst_tid_wh ON rules.rulestypexclassificationsecuritytoken (rulestypexclassificationid, warehousefromdate);

-- Indexes for rules.rulestypexresourceitem
CREATE INDEX IF NOT EXISTS idx_rltxri_eff_from ON rules.rulestypexresourceitem (effectivefromdate);
CREATE INDEX IF NOT EXISTS idx_rltxri_eff_to ON rules.rulestypexresourceitem (effectivetodate);
CREATE INDEX IF NOT EXISTS idx_rltxri_wh_created ON rules.rulestypexresourceitem (warehousecreatedtimestamp);
CREATE INDEX IF NOT EXISTS idx_rltxri_wh_updated ON rules.rulestypexresourceitem (warehouselastupdatedtimestamp);
CREATE INDEX IF NOT EXISTS idx_rltxri_val ON rules.rulestypexresourceitem (value);
CREATE INDEX IF NOT EXISTS idx_rltxri_ei_wh ON rules.rulestypexresourceitem (enterpriseid, warehousefromdate);
CREATE INDEX IF NOT EXISTS idx_rltxri_af_wh ON rules.rulestypexresourceitem (activeflagid, warehousefromdate);
CREATE INDEX IF NOT EXISTS idx_rltxri_sys_wh ON rules.rulestypexresourceitem (systemid, warehousefromdate);
CREATE INDEX IF NOT EXISTS idx_rltxri_tid_wh ON rules.rulestypexresourceitem (rulestypeid, warehousefromdate);

-- Indexes for rules.rulestypexresourceitemsecuritytoken
CREATE INDEX IF NOT EXISTS idx_rltxrist_eff_from ON rules.rulestypexresourceitemsecuritytoken (effectivefromdate);
CREATE INDEX IF NOT EXISTS idx_rltxrist_eff_to ON rules.rulestypexresourceitemsecuritytoken (effectivetodate);
CREATE INDEX IF NOT EXISTS idx_rltxrist_wh_created ON rules.rulestypexresourceitemsecuritytoken (warehousecreatedtimestamp);
CREATE INDEX IF NOT EXISTS idx_rltxrist_wh_updated ON rules.rulestypexresourceitemsecuritytoken (warehouselastupdatedtimestamp);
CREATE INDEX IF NOT EXISTS idx_rltxrist_ei_wh ON rules.rulestypexresourceitemsecuritytoken (enterpriseid, warehousefromdate);
CREATE INDEX IF NOT EXISTS idx_rltxrist_st_wh ON rules.rulestypexresourceitemsecuritytoken (securitytokenid, warehousefromdate);
CREATE INDEX IF NOT EXISTS idx_rltxrist_sys_wh ON rules.rulestypexresourceitemsecuritytoken (systemid, warehousefromdate);

-- Indexes for rules.rulesxarrangement
CREATE INDEX IF NOT EXISTS idx_rxar_eff_from ON rules.rulesxarrangement (effectivefromdate);
CREATE INDEX IF NOT EXISTS idx_rxar_eff_to ON rules.rulesxarrangement (effectivetodate);
CREATE INDEX IF NOT EXISTS idx_rxar_wh_created ON rules.rulesxarrangement (warehousecreatedtimestamp);
CREATE INDEX IF NOT EXISTS idx_rxar_wh_updated ON rules.rulesxarrangement (warehouselastupdatedtimestamp);
CREATE INDEX IF NOT EXISTS idx_rxar_val ON rules.rulesxarrangement (value);
CREATE INDEX IF NOT EXISTS idx_rxar_ei_wh ON rules.rulesxarrangement (enterpriseid, warehousefromdate);
CREATE INDEX IF NOT EXISTS idx_rxar_af_wh ON rules.rulesxarrangement (activeflagid, warehousefromdate);
CREATE INDEX IF NOT EXISTS idx_rxar_sys_wh ON rules.rulesxarrangement (systemid, warehousefromdate);
CREATE INDEX IF NOT EXISTS idx_rxar_rid_wh ON rules.rulesxarrangement (rulesid, warehousefromdate);

-- Indexes for rules.rulesxarrangementssecuritytoken
CREATE INDEX IF NOT EXISTS idx_rxast_eff_from ON rules.rulesxarrangementssecuritytoken (effectivefromdate);
CREATE INDEX IF NOT EXISTS idx_rxast_eff_to ON rules.rulesxarrangementssecuritytoken (effectivetodate);
CREATE INDEX IF NOT EXISTS idx_rxast_wh_created ON rules.rulesxarrangementssecuritytoken (warehousecreatedtimestamp);
CREATE INDEX IF NOT EXISTS idx_rxast_wh_updated ON rules.rulesxarrangementssecuritytoken (warehouselastupdatedtimestamp);
CREATE INDEX IF NOT EXISTS idx_rxast_ei_wh ON rules.rulesxarrangementssecuritytoken (enterpriseid, warehousefromdate);
CREATE INDEX IF NOT EXISTS idx_rxast_af_wh ON rules.rulesxarrangementssecuritytoken (activeflagid, warehousefromdate);
CREATE INDEX IF NOT EXISTS idx_rxast_st_wh ON rules.rulesxarrangementssecuritytoken (securitytokenid, warehousefromdate);
CREATE INDEX IF NOT EXISTS idx_rxast_sys_wh ON rules.rulesxarrangementssecuritytoken (systemid, warehousefromdate);

-- Indexes for rules.rulesxclassification
CREATE INDEX IF NOT EXISTS idx_rxcl_eff_from ON rules.rulesxclassification (effectivefromdate);
CREATE INDEX IF NOT EXISTS idx_rxcl_eff_to ON rules.rulesxclassification (effectivetodate);
CREATE INDEX IF NOT EXISTS idx_rxcl_wh_created ON rules.rulesxclassification (warehousecreatedtimestamp);
CREATE INDEX IF NOT EXISTS idx_rxcl_wh_updated ON rules.rulesxclassification (warehouselastupdatedtimestamp);
CREATE INDEX IF NOT EXISTS idx_rxcl_val ON rules.rulesxclassification (value);
CREATE INDEX IF NOT EXISTS idx_rxcl_ei_wh ON rules.rulesxclassification (enterpriseid, warehousefromdate);
CREATE INDEX IF NOT EXISTS idx_rxcl_af_wh ON rules.rulesxclassification (activeflagid, warehousefromdate);
CREATE INDEX IF NOT EXISTS idx_rxcl_sys_wh ON rules.rulesxclassification (systemid, warehousefromdate);
CREATE INDEX IF NOT EXISTS idx_rxcl_rid_wh ON rules.rulesxclassification (rulesid, warehousefromdate);

-- Indexes for rules.rulesxclassificationsecuritytoken
CREATE INDEX IF NOT EXISTS idx_rxcst_eff_from ON rules.rulesxclassificationsecuritytoken (effectivefromdate);
CREATE INDEX IF NOT EXISTS idx_rxcst_eff_to ON rules.rulesxclassificationsecuritytoken (effectivetodate);
CREATE INDEX IF NOT EXISTS idx_rxcst_wh_created ON rules.rulesxclassificationsecuritytoken (warehousecreatedtimestamp);
CREATE INDEX IF NOT EXISTS idx_rxcst_wh_updated ON rules.rulesxclassificationsecuritytoken (warehouselastupdatedtimestamp);
CREATE INDEX IF NOT EXISTS idx_rxcst_ei_wh ON rules.rulesxclassificationsecuritytoken (enterpriseid, warehousefromdate);
CREATE INDEX IF NOT EXISTS idx_rxcst_st_wh ON rules.rulesxclassificationsecuritytoken (securitytokenid, warehousefromdate);
CREATE INDEX IF NOT EXISTS idx_rxcst_sys_wh ON rules.rulesxclassificationsecuritytoken (systemid, warehousefromdate);

-- Indexes for rules.rulesxinvolvedparty
CREATE INDEX IF NOT EXISTS idx_rxip_eff_from ON rules.rulesxinvolvedparty (effectivefromdate);
CREATE INDEX IF NOT EXISTS idx_rxip_eff_to ON rules.rulesxinvolvedparty (effectivetodate);
CREATE INDEX IF NOT EXISTS idx_rxip_wh_created ON rules.rulesxinvolvedparty (warehousecreatedtimestamp);
CREATE INDEX IF NOT EXISTS idx_rxip_wh_updated ON rules.rulesxinvolvedparty (warehouselastupdatedtimestamp);
CREATE INDEX IF NOT EXISTS idx_rxip_val ON rules.rulesxinvolvedparty (value);
CREATE INDEX IF NOT EXISTS idx_rxip_ei_wh ON rules.rulesxinvolvedparty (enterpriseid, warehousefromdate);
CREATE INDEX IF NOT EXISTS idx_rxip_af_wh ON rules.rulesxinvolvedparty (activeflagid, warehousefromdate);
CREATE INDEX IF NOT EXISTS idx_rxip_sys_wh ON rules.rulesxinvolvedparty (systemid, warehousefromdate);
CREATE INDEX IF NOT EXISTS idx_rxip_rid_wh ON rules.rulesxinvolvedparty (rulesid, warehousefromdate);

-- Indexes for rules.rulesxinvolvedpartysecuritytoken
CREATE INDEX IF NOT EXISTS idx_rxipst_eff_from ON rules.rulesxinvolvedpartysecuritytoken (effectivefromdate);
CREATE INDEX IF NOT EXISTS idx_rxipst_eff_to ON rules.rulesxinvolvedpartysecuritytoken (effectivetodate);
CREATE INDEX IF NOT EXISTS idx_rxipst_wh_created ON rules.rulesxinvolvedpartysecuritytoken (warehousecreatedtimestamp);
CREATE INDEX IF NOT EXISTS idx_rxipst_wh_updated ON rules.rulesxinvolvedpartysecuritytoken (warehouselastupdatedtimestamp);
CREATE INDEX IF NOT EXISTS idx_rxipst_ei_wh ON rules.rulesxinvolvedpartysecuritytoken (enterpriseid, warehousefromdate);
CREATE INDEX IF NOT EXISTS idx_rxipst_af_wh ON rules.rulesxinvolvedpartysecuritytoken (activeflagid, warehousefromdate);
CREATE INDEX IF NOT EXISTS idx_rxipst_st_wh ON rules.rulesxinvolvedpartysecuritytoken (securitytokenid, warehousefromdate);
CREATE INDEX IF NOT EXISTS idx_rxipst_sys_wh ON rules.rulesxinvolvedpartysecuritytoken (systemid, warehousefromdate);

-- Indexes for rules.rulesxproduct
CREATE INDEX IF NOT EXISTS idx_rxpr_eff_from ON rules.rulesxproduct (effectivefromdate);
CREATE INDEX IF NOT EXISTS idx_rxpr_eff_to ON rules.rulesxproduct (effectivetodate);
CREATE INDEX IF NOT EXISTS idx_rxpr_wh_created ON rules.rulesxproduct (warehousecreatedtimestamp);
CREATE INDEX IF NOT EXISTS idx_rxpr_wh_updated ON rules.rulesxproduct (warehouselastupdatedtimestamp);
CREATE INDEX IF NOT EXISTS idx_rxpr_val ON rules.rulesxproduct (value);
CREATE INDEX IF NOT EXISTS idx_rxpr_ei_wh ON rules.rulesxproduct (enterpriseid, warehousefromdate);
CREATE INDEX IF NOT EXISTS idx_rxpr_af_wh ON rules.rulesxproduct (activeflagid, warehousefromdate);
CREATE INDEX IF NOT EXISTS idx_rxpr_sys_wh ON rules.rulesxproduct (systemid, warehousefromdate);
CREATE INDEX IF NOT EXISTS idx_rxpr_rid_wh ON rules.rulesxproduct (rulesid, warehousefromdate);

-- Indexes for rules.rulesxproductsecuritytoken
CREATE INDEX IF NOT EXISTS idx_rxprst_eff_from ON rules.rulesxproductsecuritytoken (effectivefromdate);
CREATE INDEX IF NOT EXISTS idx_rxprst_eff_to ON rules.rulesxproductsecuritytoken (effectivetodate);
CREATE INDEX IF NOT EXISTS idx_rxprst_wh_created ON rules.rulesxproductsecuritytoken (warehousecreatedtimestamp);
CREATE INDEX IF NOT EXISTS idx_rxprst_wh_updated ON rules.rulesxproductsecuritytoken (warehouselastupdatedtimestamp);
CREATE INDEX IF NOT EXISTS idx_rxprst_ei_wh ON rules.rulesxproductsecuritytoken (enterpriseid, warehousefromdate);
CREATE INDEX IF NOT EXISTS idx_rxprst_af_wh ON rules.rulesxproductsecuritytoken (activeflagid, warehousefromdate);
CREATE INDEX IF NOT EXISTS idx_rxprst_st_wh ON rules.rulesxproductsecuritytoken (securitytokenid, warehousefromdate);
CREATE INDEX IF NOT EXISTS idx_rxprst_sys_wh ON rules.rulesxproductsecuritytoken (systemid, warehousefromdate);

-- Indexes for rules.rulesxresourceitem
CREATE INDEX IF NOT EXISTS idx_rxri_eff_from ON rules.rulesxresourceitem (effectivefromdate);
CREATE INDEX IF NOT EXISTS idx_rxri_eff_to ON rules.rulesxresourceitem (effectivetodate);
CREATE INDEX IF NOT EXISTS idx_rxri_wh_created ON rules.rulesxresourceitem (warehousecreatedtimestamp);
CREATE INDEX IF NOT EXISTS idx_rxri_wh_updated ON rules.rulesxresourceitem (warehouselastupdatedtimestamp);
CREATE INDEX IF NOT EXISTS idx_rxri_val ON rules.rulesxresourceitem (value);
CREATE INDEX IF NOT EXISTS idx_rxri_ei_wh ON rules.rulesxresourceitem (enterpriseid, warehousefromdate);
CREATE INDEX IF NOT EXISTS idx_rxri_af_wh ON rules.rulesxresourceitem (activeflagid, warehousefromdate);
CREATE INDEX IF NOT EXISTS idx_rxri_sys_wh ON rules.rulesxresourceitem (systemid, warehousefromdate);
CREATE INDEX IF NOT EXISTS idx_rxri_rid_wh ON rules.rulesxresourceitem (rulesid, warehousefromdate);

-- Indexes for rules.rulesxresourceitemsecuritytoken
CREATE INDEX IF NOT EXISTS idx_rxrist_eff_from ON rules.rulesxresourceitemsecuritytoken (effectivefromdate);
CREATE INDEX IF NOT EXISTS idx_rxrist_eff_to ON rules.rulesxresourceitemsecuritytoken (effectivetodate);
CREATE INDEX IF NOT EXISTS idx_rxrist_wh_created ON rules.rulesxresourceitemsecuritytoken (warehousecreatedtimestamp);
CREATE INDEX IF NOT EXISTS idx_rxrist_wh_updated ON rules.rulesxresourceitemsecuritytoken (warehouselastupdatedtimestamp);
CREATE INDEX IF NOT EXISTS idx_rxrist_ei_wh ON rules.rulesxresourceitemsecuritytoken (enterpriseid, warehousefromdate);
CREATE INDEX IF NOT EXISTS idx_rxrist_af_wh ON rules.rulesxresourceitemsecuritytoken (activeflagid, warehousefromdate);
CREATE INDEX IF NOT EXISTS idx_rxrist_st_wh ON rules.rulesxresourceitemsecuritytoken (securitytokenid, warehousefromdate);
CREATE INDEX IF NOT EXISTS idx_rxrist_sys_wh ON rules.rulesxresourceitemsecuritytoken (systemid, warehousefromdate);

-- Indexes for rules.rulesxrules
CREATE INDEX IF NOT EXISTS idx_rxrs_eff_from ON rules.rulesxrules (effectivefromdate);
CREATE INDEX IF NOT EXISTS idx_rxrs_eff_to ON rules.rulesxrules (effectivetodate);
CREATE INDEX IF NOT EXISTS idx_rxrs_wh_created ON rules.rulesxrules (warehousecreatedtimestamp);
CREATE INDEX IF NOT EXISTS idx_rxrs_wh_updated ON rules.rulesxrules (warehouselastupdatedtimestamp);
CREATE INDEX IF NOT EXISTS idx_rxrs_val ON rules.rulesxrules (value);
CREATE INDEX IF NOT EXISTS idx_rxrs_ei_wh ON rules.rulesxrules (enterpriseid, warehousefromdate);
CREATE INDEX IF NOT EXISTS idx_rxrs_af_wh ON rules.rulesxrules (activeflagid, warehousefromdate);
CREATE INDEX IF NOT EXISTS idx_rxrs_sys_wh ON rules.rulesxrules (systemid, warehousefromdate);
CREATE INDEX IF NOT EXISTS idx_rxrs_rid_wh ON rules.rulesxrules (childrulesid, warehousefromdate);
CREATE INDEX IF NOT EXISTS idx_rxrs_rid_wh_p ON rules.rulesxrules (parentrulesid, warehousefromdate);

-- Indexes for rules.rulesxrulessecuritytoken
CREATE INDEX IF NOT EXISTS idx_rxrst_eff_from ON rules.rulesxrulessecuritytoken (effectivefromdate);
CREATE INDEX IF NOT EXISTS idx_rxrst_eff_to ON rules.rulesxrulessecuritytoken (effectivetodate);
CREATE INDEX IF NOT EXISTS idx_rxrst_wh_created ON rules.rulesxrulessecuritytoken (warehousecreatedtimestamp);
CREATE INDEX IF NOT EXISTS idx_rxrst_wh_updated ON rules.rulesxrulessecuritytoken (warehouselastupdatedtimestamp);
CREATE INDEX IF NOT EXISTS idx_rxrst_ei_wh ON rules.rulesxrulessecuritytoken (enterpriseid, warehousefromdate);
CREATE INDEX IF NOT EXISTS idx_rxrst_af_wh ON rules.rulesxrulessecuritytoken (activeflagid, warehousefromdate);
CREATE INDEX IF NOT EXISTS idx_rxrst_st_wh ON rules.rulesxrulessecuritytoken (securitytokenid, warehousefromdate);
CREATE INDEX IF NOT EXISTS idx_rxrst_sys_wh ON rules.rulesxrulessecuritytoken (systemid, warehousefromdate);

-- Indexes for rules.rulesxrulestype
CREATE INDEX IF NOT EXISTS idx_rxrt_eff_from ON rules.rulesxrulestype (effectivefromdate);
CREATE INDEX IF NOT EXISTS idx_rxrt_eff_to ON rules.rulesxrulestype (effectivetodate);
CREATE INDEX IF NOT EXISTS idx_rxrt_wh_created ON rules.rulesxrulestype (warehousecreatedtimestamp);
CREATE INDEX IF NOT EXISTS idx_rxrt_wh_updated ON rules.rulesxrulestype (warehouselastupdatedtimestamp);
CREATE INDEX IF NOT EXISTS idx_rxrt_val ON rules.rulesxrulestype (value);
CREATE INDEX IF NOT EXISTS idx_rxrt_ei_wh ON rules.rulesxrulestype (enterpriseid, warehousefromdate);
CREATE INDEX IF NOT EXISTS idx_rxrt_af_wh ON rules.rulesxrulestype (activeflagid, warehousefromdate);
CREATE INDEX IF NOT EXISTS idx_rxrt_sys_wh ON rules.rulesxrulestype (systemid, warehousefromdate);
CREATE INDEX IF NOT EXISTS idx_rxrt_rid_wh ON rules.rulesxrulestype (rulesid, warehousefromdate);

-- Indexes for rules.rulesxrulestypessecuritytoken
CREATE INDEX IF NOT EXISTS idx_rxrtst_eff_from ON rules.rulesxrulestypessecuritytoken (effectivefromdate);
CREATE INDEX IF NOT EXISTS idx_rxrtst_eff_to ON rules.rulesxrulestypessecuritytoken (effectivetodate);
CREATE INDEX IF NOT EXISTS idx_rxrtst_wh_created ON rules.rulesxrulestypessecuritytoken (warehousecreatedtimestamp);
CREATE INDEX IF NOT EXISTS idx_rxrtst_wh_updated ON rules.rulesxrulestypessecuritytoken (warehouselastupdatedtimestamp);
CREATE INDEX IF NOT EXISTS idx_rxrtst_ei_wh ON rules.rulesxrulestypessecuritytoken (enterpriseid, warehousefromdate);
CREATE INDEX IF NOT EXISTS idx_rxrtst_af_wh ON rules.rulesxrulestypessecuritytoken (activeflagid, warehousefromdate);
CREATE INDEX IF NOT EXISTS idx_rxrtst_st_wh ON rules.rulesxrulestypessecuritytoken (securitytokenid, warehousefromdate);
CREATE INDEX IF NOT EXISTS idx_rxrtst_sys_wh ON rules.rulesxrulestypessecuritytoken (systemid, warehousefromdate);
