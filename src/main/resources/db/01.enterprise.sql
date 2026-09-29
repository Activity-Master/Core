CREATE SCHEMA dbo;

CREATE TABLE dbo.enterprise
(
    enterpriseid                  UUID                        NOT NULL primary key,
    effectivefromdate             timestamp(6) with time zone NOT NULL DEFAULT now(),
    effectivetodate               timestamp(6) with time zone NOT NULL DEFAULT '2999-12-31 23:59:59.999+00',
    warehousecreatedtimestamp     timestamp(6) with time zone NOT NULL DEFAULT now(),
    warehousefromdate             DATE                        NOT NULL DEFAULT current_date,

    warehouselastupdatedtimestamp timestamp(6) with time zone NOT NULL DEFAULT now(),
    enterprisedesc                character varying(255)      NOT NULL,
    enterprisename                character varying(255)      NOT NULL
);
CREATE TABLE dbo.enterprisesecuritytoken
(
    enterprisesecuritytokenid     UUID                        NOT NULL primary key,
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
    systemid                      UUID                        NOT NULL
);
CREATE TABLE dbo.enterprisexclassification
(
    enterprisexclassificationid   UUID                        NOT NULL primary key,
    effectivefromdate             timestamp(6) with time zone NOT NULL DEFAULT now(),
    effectivetodate               timestamp(6) with time zone NOT NULL DEFAULT '2999-12-31 23:59:59.999+00',
    warehousecreatedtimestamp     timestamp(6) with time zone NOT NULL DEFAULT now(),
    warehousefromdate             DATE                        NOT NULL DEFAULT current_date,

    warehouselastupdatedtimestamp timestamp(6) with time zone NOT NULL DEFAULT now(),
    originalsourcesystemuniqueid  UUID                        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    value                         text                        NOT NULL,
    activeflagid                  UUID                        NOT NULL,
    enterpriseid                  UUID                        NOT NULL,
    systemid                      UUID                        NOT NULL,
    originalsourcesystemid        UUID                        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    classificationid              UUID                        NOT NULL
);
CREATE TABLE dbo.enterprisexclassificationsecuritytoken
(
    enterprisexclassificationsecuritytokenid UUID                        NOT NULL primary key,
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
    enterprisexclassificationid              UUID                        NOT NULL
);

-- Indexes for dbo.enterprise
CREATE INDEX idx_ent_eff_from ON dbo.enterprise (effectivefromdate);
CREATE INDEX idx_ent_eff_to ON dbo.enterprise (effectivetodate);
CREATE INDEX idx_ent_wh_created ON dbo.enterprise (warehousecreatedtimestamp);
CREATE INDEX idx_ent_wh_updated ON dbo.enterprise (warehouselastupdatedtimestamp);

-- Indexes for dbo.enterprisesecuritytoken
CREATE INDEX idx_est_eff_from ON dbo.enterprisesecuritytoken (effectivefromdate);
CREATE INDEX idx_est_eff_to ON dbo.enterprisesecuritytoken (effectivetodate);
CREATE INDEX idx_est_wh_created ON dbo.enterprisesecuritytoken (warehousecreatedtimestamp);
CREATE INDEX idx_est_wh_updated ON dbo.enterprisesecuritytoken (warehouselastupdatedtimestamp);
CREATE INDEX idx_est_sys_wh ON dbo.enterprisesecuritytoken (systemid, warehousefromdate);
CREATE INDEX idx_est_af_wh ON dbo.enterprisesecuritytoken (activeflagid, warehousefromdate);
CREATE INDEX idx_est_st_wh ON dbo.enterprisesecuritytoken (securitytokenid, warehousefromdate);
CREATE INDEX idx_est_ei_wh ON dbo.enterprisesecuritytoken (enterpriseid, warehousefromdate);

-- Indexes for dbo.enterprisexclassification
CREATE INDEX idx_excl_eff_from ON dbo.enterprisexclassification (effectivefromdate);
CREATE INDEX idx_excl_eff_to ON dbo.enterprisexclassification (effectivetodate);
CREATE INDEX idx_excl_wh_created ON dbo.enterprisexclassification (warehousecreatedtimestamp);
CREATE INDEX idx_excl_wh_updated ON dbo.enterprisexclassification (warehouselastupdatedtimestamp);
CREATE INDEX idx_excl_val ON dbo.enterprisexclassification (value);
CREATE INDEX idx_excl_ei_wh ON dbo.enterprisexclassification (enterpriseid, warehousefromdate);
CREATE INDEX idx_excl_af_wh ON dbo.enterprisexclassification (activeflagid, warehousefromdate);
CREATE INDEX idx_excl_sys_wh ON dbo.enterprisexclassification (systemid, warehousefromdate);
CREATE INDEX idx_excl_cl_wh ON dbo.enterprisexclassification (classificationid, warehousefromdate);

-- Indexes for dbo.enterprisexclassificationsecuritytoken
CREATE INDEX idx_exclst_eff_from ON dbo.enterprisexclassificationsecuritytoken (effectivefromdate);
CREATE INDEX idx_exclst_eff_to ON dbo.enterprisexclassificationsecuritytoken (effectivetodate);
CREATE INDEX idx_exclst_wh_created ON dbo.enterprisexclassificationsecuritytoken (warehousecreatedtimestamp);
CREATE INDEX idx_exclst_wh_updated ON dbo.enterprisexclassificationsecuritytoken (warehouselastupdatedtimestamp);
CREATE INDEX idx_exclst_st_wh ON dbo.enterprisexclassificationsecuritytoken (securitytokenid, warehousefromdate);
CREATE INDEX idx_exclst_af_wh ON dbo.enterprisexclassificationsecuritytoken (activeflagid, warehousefromdate);
CREATE INDEX idx_exclst_ei_wh ON dbo.enterprisexclassificationsecuritytoken (enterpriseid, warehousefromdate);
CREATE INDEX idx_exclst_sys_wh ON dbo.enterprisexclassificationsecuritytoken (systemid, warehousefromdate);
CREATE INDEX idx_exclst_excl_id_wh ON dbo.enterprisexclassificationsecuritytoken (enterprisexclassificationid, warehousefromdate);
