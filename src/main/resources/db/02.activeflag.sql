CREATE TABLE dbo.activeflag
(
    activeflagid                  UUID                        NOT NULL primary key,
    effectivefromdate             timestamp(6) with time zone NOT NULL,
    effectivetodate               timestamp(6) with time zone NOT NULL,
    warehousecreatedtimestamp     timestamp(6) with time zone NOT NULL,
    warehousefromdate             DATE                        NOT NULL,

    warehouselastupdatedtimestamp timestamp(6) with time zone NOT NULL,
    allowaccess                   INTEGER                     NOT NULL,
    activeflagdescription         character varying(100)      NOT NULL,
    activeflagname                character varying(100)      NOT NULL,
    enterpriseid                  UUID                        NOT NULL
);
CREATE TABLE dbo.activeflagsecuritytoken
(
    activeflagsecuritytokenid     UUID                        NOT NULL primary key,
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
    securitytokenactiveflagid     UUID                        NOT NULL
);
CREATE TABLE dbo.activeflagxclassification
(
    activeflagxclassificationid   UUID                        NOT NULL primary key,
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
    classificationid              UUID                        NOT NULL
);
CREATE TABLE dbo.activeflagxclassificationsecuritytoken
(
    activeflagxclassificationsecuritytokenid UUID                        NOT NULL primary key,
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
    activeflagxclassificationid              UUID                        NOT NULL
);
-- Indexes for dbo.activeflag
CREATE INDEX idx_af_eff_from ON dbo.activeflag (effectivefromdate);
CREATE INDEX idx_af_eff_to ON dbo.activeflag (effectivetodate);
CREATE INDEX idx_af_wh_created ON dbo.activeflag (warehousecreatedtimestamp);
CREATE INDEX idx_af_wh_updated ON dbo.activeflag (warehouselastupdatedtimestamp);
CREATE INDEX idx_af_ei_wh ON dbo.activeflag (enterpriseid, warehousefromdate);

-- Indexes for dbo.activeflagsecuritytoken
CREATE INDEX idx_afst_eff_from ON dbo.activeflagsecuritytoken (effectivefromdate);
CREATE INDEX idx_afst_eff_to ON dbo.activeflagsecuritytoken (effectivetodate);
CREATE INDEX idx_afst_wh_created ON dbo.activeflagsecuritytoken (warehousecreatedtimestamp);
CREATE INDEX idx_afst_wh_updated ON dbo.activeflagsecuritytoken (warehouselastupdatedtimestamp);
CREATE INDEX idx_afst_sys_wh ON dbo.activeflagsecuritytoken (systemid, warehousefromdate);
CREATE INDEX idx_afst_st_wh ON dbo.activeflagsecuritytoken (securitytokenid, warehousefromdate);
CREATE INDEX idx_afst_af_wh ON dbo.activeflagsecuritytoken (activeflagid, warehousefromdate);
CREATE INDEX idx_afst_saf_wh ON dbo.activeflagsecuritytoken (securitytokenactiveflagid, warehousefromdate);
CREATE INDEX idx_afst_ei_wh ON dbo.activeflagsecuritytoken (enterpriseid, warehousefromdate);

-- Indexes for dbo.activeflagxclassification
CREATE INDEX idx_afxc_eff_from ON dbo.activeflagxclassification (effectivefromdate);
CREATE INDEX idx_afxc_eff_to ON dbo.activeflagxclassification (effectivetodate);
CREATE INDEX idx_afxc_wh_created ON dbo.activeflagxclassification (warehousecreatedtimestamp);
CREATE INDEX idx_afxc_wh_updated ON dbo.activeflagxclassification (warehouselastupdatedtimestamp);
CREATE INDEX idx_afxc_val ON dbo.activeflagxclassification (value);
CREATE INDEX idx_afxc_ei_wh ON dbo.activeflagxclassification (enterpriseid, warehousefromdate);
CREATE INDEX idx_afxc_af_wh ON dbo.activeflagxclassification (activeflagid, warehousefromdate);
CREATE INDEX idx_afxc_sys_wh ON dbo.activeflagxclassification (systemid, warehousefromdate);
CREATE INDEX idx_afxc_cl_wh ON dbo.activeflagxclassification (classificationid, warehousefromdate);

-- Indexes for dbo.activeflagxclassificationsecuritytoken
CREATE INDEX idx_afxcst_eff_from ON dbo.activeflagxclassificationsecuritytoken (effectivefromdate);
CREATE INDEX idx_afxcst_eff_to ON dbo.activeflagxclassificationsecuritytoken (effectivetodate);
CREATE INDEX idx_afxcst_wh_created ON dbo.activeflagxclassificationsecuritytoken (warehousecreatedtimestamp);
CREATE INDEX idx_afxcst_wh_updated ON dbo.activeflagxclassificationsecuritytoken (warehouselastupdatedtimestamp);
CREATE INDEX idx_afxcst_st_wh ON dbo.activeflagxclassificationsecuritytoken (securitytokenid, warehousefromdate);
CREATE INDEX idx_afxcst_sys_wh ON dbo.activeflagxclassificationsecuritytoken (systemid, warehousefromdate);
CREATE INDEX idx_afxcst_ei_wh ON dbo.activeflagxclassificationsecuritytoken (enterpriseid, warehousefromdate);
CREATE INDEX idx_afxcst_afxc_wh ON dbo.activeflagxclassificationsecuritytoken (activeflagxclassificationid, warehousefromdate);
CREATE INDEX idx_afxcst_af_wh ON dbo.activeflagxclassificationsecuritytoken (activeflagid, warehousefromdate);


