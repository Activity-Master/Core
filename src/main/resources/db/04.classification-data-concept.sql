DO $fsdm_schema$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_catalog.pg_namespace WHERE nspname = 'classification') THEN
        CREATE SCHEMA IF NOT EXISTS classification;
    END IF;
END;
$fsdm_schema$;
CREATE TABLE IF NOT EXISTS classification.classificationdataconcept
(
    classificationdataconceptid   UUID                        NOT NULL primary key,
    effectivefromdate             timestamp(6) with time zone NOT NULL DEFAULT now(),
    effectivetodate               timestamp(6) with time zone NOT NULL DEFAULT '2999-12-31 23:59:59.999+00',
    warehousecreatedtimestamp     timestamp(6) with time zone NOT NULL DEFAULT now(),
    warehousefromdate             DATE                        NOT NULL DEFAULT current_date,

    warehouselastupdatedtimestamp timestamp(6) with time zone NOT NULL,
    originalsourcesystemuniqueid  UUID                        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    classificationdataconceptdesc character varying(1500)     NOT NULL,
    classificationdataconceptname character varying(100)      NOT NULL,
    activeflagid                  UUID                        NOT NULL,
    enterpriseid                  UUID                        NOT NULL,
    systemid                      UUID                        NOT NULL,
    originalsourcesystemid        UUID                        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000'
);
CREATE TABLE IF NOT EXISTS classification.classificationdataconceptsecuritytoken
(
    classificationdataconceptsecuritytokenid UUID                        NOT NULL primary key,
    effectivefromdate                        timestamp(6) with time zone NOT NULL DEFAULT now(),
    effectivetodate                          timestamp(6) with time zone NOT NULL DEFAULT '2999-12-31 23:59:59.999+00',
    warehousecreatedtimestamp                timestamp(6) with time zone NOT NULL DEFAULT now(),
    warehousefromdate                        DATE                        NOT NULL DEFAULT current_date,

    warehouselastupdatedtimestamp            timestamp(6) with time zone NOT NULL,
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
    classificationdataconceptid              UUID                        NOT NULL
);
CREATE TABLE IF NOT EXISTS classification.classificationdataconceptxclassification
(
    classificationdataconceptxclassificationid UUID                        NOT NULL primary key,
    effectivefromdate                          timestamp(6) with time zone NOT NULL DEFAULT now(),
    effectivetodate                            timestamp(6) with time zone NOT NULL DEFAULT '2999-12-31 23:59:59.999+00',
    warehousecreatedtimestamp                  timestamp(6) with time zone NOT NULL DEFAULT now(),
    warehousefromdate                          DATE                        NOT NULL DEFAULT current_date,

    warehouselastupdatedtimestamp              timestamp(6) with time zone NOT NULL,
    originalsourcesystemuniqueid               UUID                        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    value                                      varchar(200)                NOT NULL,
    activeflagid                               UUID                        NOT NULL,
    enterpriseid                               UUID                        NOT NULL,
    systemid                                   UUID                        NOT NULL,
    originalsourcesystemid                     UUID                        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    classificationid                           UUID                        NOT NULL,
    classificationdataconceptid                UUID                        NOT NULL
);
CREATE TABLE IF NOT EXISTS classification.classificationdataconceptxclassificationsecuritytoken
(
    classificationdataconceptxclassificationsecuritytokenid UUID                        NOT NULL primary key,
    effectivefromdate                                       timestamp(6) with time zone NOT NULL,
    effectivetodate                                         timestamp(6) with time zone NOT NULL,
    warehousecreatedtimestamp                               timestamp(6) with time zone NOT NULL,
    warehousefromdate                                       DATE                        NOT NULL,

    warehouselastupdatedtimestamp                           timestamp(6) with time zone NOT NULL,
    createallowed                                           INTEGER                     NOT NULL,
    deleteallowed                                           INTEGER                     NOT NULL,
    originalsourcesystemuniqueid                            UUID                        NOT NULL,
    readallowed                                             INTEGER                     NOT NULL,
    updateallowed                                           INTEGER                     NOT NULL,
    activeflagid                                            UUID                        NOT NULL,
    enterpriseid                                            UUID                        NOT NULL,
    originalsourcesystemid                                  UUID                        NOT NULL,
    securitytokenid                                         UUID                        NOT NULL,
    systemid                                                UUID                        NOT NULL,
    classificationdataconceptxclassificationid              UUID                        NOT NULL
);
CREATE TABLE IF NOT EXISTS classification.classificationdataconceptxresourceitem
(
    classificationdataconceptxresourceitemid UUID                        NOT NULL primary key,
    effectivefromdate                        timestamp(6) with time zone NOT NULL DEFAULT now(),
    effectivetodate                          timestamp(6) with time zone NOT NULL DEFAULT '2999-12-31 23:59:59.999+00',
    warehousecreatedtimestamp                timestamp(6) with time zone NOT NULL DEFAULT now(),
    warehousefromdate                        DATE                        NOT NULL DEFAULT current_date,

    warehouselastupdatedtimestamp            timestamp(6) with time zone NOT NULL,
    originalsourcesystemuniqueid             UUID                        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    value                                    varchar(200)                NOT NULL,
    activeflagid                             UUID                        NOT NULL,
    enterpriseid                             UUID                        NOT NULL,
    systemid                                 UUID                        NOT NULL,
    originalsourcesystemid                   UUID                        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    classificationid                         UUID                        NOT NULL,
    classificationdataconceptid              UUID                        NOT NULL,
    resourceitemid                           UUID                        NOT NULL
);

CREATE TABLE IF NOT EXISTS classification.classificationdataconceptxresourceitemsecuritytoken
(
    classificationdataconceptxresourceitemsecuritytokenid UUID                        NOT NULL primary key,
    effectivefromdate                                     timestamp(6) with time zone NOT NULL DEFAULT now(),
    effectivetodate                                       timestamp(6) with time zone NOT NULL DEFAULT '2999-12-31 23:59:59.999+00',
    warehousecreatedtimestamp                             timestamp(6) with time zone NOT NULL DEFAULT now(),
    warehousefromdate                                     DATE                        NOT NULL DEFAULT current_date,

    warehouselastupdatedtimestamp                         timestamp(6) with time zone NOT NULL,
    createallowed                                         INTEGER                     NOT NULL,
    deleteallowed                                         INTEGER                     NOT NULL,
    originalsourcesystemuniqueid                          UUID                        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    readallowed                                           INTEGER                     NOT NULL,
    updateallowed                                         INTEGER                     NOT NULL,
    activeflagid                                          UUID                        NOT NULL,
    enterpriseid                                          UUID                        NOT NULL,
    originalsourcesystemid                                UUID                        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    securitytokenid                                       UUID                        NOT NULL,
    systemid                                              UUID                        NOT NULL,
    classificationdataconceptxresourceitemid              UUID                        NOT NULL
);

-- Indexes for classification.classificationdataconcept
CREATE INDEX IF NOT EXISTS idx_cd_eff_from ON classification.classificationdataconcept (effectivefromdate);
CREATE INDEX IF NOT EXISTS idx_cd_eff_to ON classification.classificationdataconcept (effectivetodate);
CREATE INDEX IF NOT EXISTS idx_cd_wh_created ON classification.classificationdataconcept (warehousecreatedtimestamp);
CREATE INDEX IF NOT EXISTS idx_cd_wh_updated ON classification.classificationdataconcept (warehouselastupdatedtimestamp);

-- Indexes for classification.classificationdataconceptsecuritytoken
CREATE INDEX IF NOT EXISTS idx_cdst_eff_from ON classification.classificationdataconceptsecuritytoken (effectivefromdate);
CREATE INDEX IF NOT EXISTS idx_cdst_eff_to ON classification.classificationdataconceptsecuritytoken (effectivetodate);
CREATE INDEX IF NOT EXISTS idx_cdst_wh_created ON classification.classificationdataconceptsecuritytoken (warehousecreatedtimestamp);
CREATE INDEX IF NOT EXISTS idx_cdst_wh_updated ON classification.classificationdataconceptsecuritytoken (warehouselastupdatedtimestamp);

-- Indexes for classification.classificationdataconceptxclassification
CREATE INDEX IF NOT EXISTS idx_cdxc_eff_from ON classification.classificationdataconceptxclassification (effectivefromdate);
CREATE INDEX IF NOT EXISTS idx_cdxc_eff_to ON classification.classificationdataconceptxclassification (effectivetodate);
CREATE INDEX IF NOT EXISTS idx_cdxc_wh_created ON classification.classificationdataconceptxclassification (warehousecreatedtimestamp);
CREATE INDEX IF NOT EXISTS idx_cdxc_wh_updated ON classification.classificationdataconceptxclassification (warehouselastupdatedtimestamp);
CREATE INDEX IF NOT EXISTS idx_cdxc_val ON classification.classificationdataconceptxclassification (value);

-- Indexes for classification.classificationdataconceptxclassificationsecuritytoken
CREATE INDEX IF NOT EXISTS idx_cdxcst_eff_from ON classification.classificationdataconceptxclassificationsecuritytoken (effectivefromdate);
CREATE INDEX IF NOT EXISTS idx_cdxcst_eff_to ON classification.classificationdataconceptxclassificationsecuritytoken (effectivetodate);
CREATE INDEX IF NOT EXISTS idx_cdxcst_wh_created ON classification.classificationdataconceptxclassificationsecuritytoken (warehousecreatedtimestamp);
CREATE INDEX IF NOT EXISTS idx_cdxcst_wh_updated ON classification.classificationdataconceptxclassificationsecuritytoken (warehouselastupdatedtimestamp);

-- Indexes for classification.classificationdataconceptxresourceitem
CREATE INDEX IF NOT EXISTS idx_cdxcrist_eff_from ON classification.classificationdataconceptxresourceitem (effectivefromdate);
CREATE INDEX IF NOT EXISTS idx_cdxcrist_eff_to ON classification.classificationdataconceptxresourceitem (effectivetodate);
CREATE INDEX IF NOT EXISTS idx_cdxcrist_wh_created ON classification.classificationdataconceptxresourceitem (warehousecreatedtimestamp);
CREATE INDEX IF NOT EXISTS idx_cdxcrist_wh_updated ON classification.classificationdataconceptxresourceitem (warehouselastupdatedtimestamp);
CREATE INDEX IF NOT EXISTS idx_cdxcrist_val ON classification.classificationdataconceptxresourceitem (value);

-- Indexes for classification.classificationdataconceptxresourceitemsecuritytoken
CREATE INDEX IF NOT EXISTS idx_cdxcristst_eff_from ON classification.classificationdataconceptxresourceitemsecuritytoken (effectivefromdate);
CREATE INDEX IF NOT EXISTS idx_cdxcristst_wh_created ON classification.classificationdataconceptxresourceitemsecuritytoken (warehousecreatedtimestamp);
