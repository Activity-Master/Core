CREATE TABLE dbo.systems
(
    systemid                      UUID                        NOT NULL primary key,
    effectivefromdate             timestamp(6) with time zone NOT NULL DEFAULT now(),
    effectivetodate               timestamp(6) with time zone NOT NULL DEFAULT '2999-12-31 23:59:59.999+00',
    warehousecreatedtimestamp     timestamp(6) with time zone NOT NULL DEFAULT now(),
    warehousefromdate             DATE                        NOT NULL DEFAULT current_date,

    warehouselastupdatedtimestamp timestamp(6) with time zone NOT NULL DEFAULT now(),
    systemdesc                    character varying(250)      NOT NULL,
    systemname                    character varying(150)      NOT NULL,
    systemhistoryname             character varying(250)      NOT NULL,
    activeflagid                  UUID                        NOT NULL,
    enterpriseid                  UUID                        NOT NULL
);
CREATE TABLE dbo.systemssecuritytoken
(
    systemssecuritytokenid        UUID                        NOT NULL primary key,
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
CREATE TABLE dbo.systemxclassification
(
    systemxclassificationid       UUID                        NOT NULL primary key,
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
CREATE TABLE dbo.systemxclassificationsecuritytoken
(
    systemxclassificationsecuritytokenid UUID                        NOT NULL primary key,
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
    systemxclassificationid              UUID                        NOT NULL
);
-- Indexes for dbo.systems
CREATE INDEX idx_sys_eff_from ON dbo.systems (effectivefromdate);
CREATE INDEX idx_sys_eff_to ON dbo.systems (effectivetodate);
CREATE INDEX idx_sys_wh_created ON dbo.systems (warehousecreatedtimestamp);
CREATE INDEX idx_sys_wh_updated ON dbo.systems (warehouselastupdatedtimestamp);
CREATE INDEX idx_sys_name ON dbo.systems (systemname);
CREATE INDEX idx_sys_ei_wh ON dbo.systems (enterpriseid, warehousefromdate);
CREATE INDEX idx_sys_af_wh ON dbo.systems (activeflagid, warehousefromdate);

-- Indexes for dbo.systemssecuritytoken
CREATE INDEX idx_sysst_eff_from ON dbo.systemssecuritytoken (effectivefromdate);
CREATE INDEX idx_sysst_eff_to ON dbo.systemssecuritytoken (effectivetodate);
CREATE INDEX idx_sysst_wh_created ON dbo.systemssecuritytoken (warehousecreatedtimestamp);
CREATE INDEX idx_sysst_wh_updated ON dbo.systemssecuritytoken (warehouselastupdatedtimestamp);
CREATE INDEX idx_sysst_ei_wh ON dbo.systemssecuritytoken (enterpriseid, warehousefromdate);
CREATE INDEX idx_sysst_af_wh ON dbo.systemssecuritytoken (activeflagid, warehousefromdate);
CREATE INDEX idx_sysst_sys_wh ON dbo.systemssecuritytoken (systemid, warehousefromdate);
CREATE INDEX idx_sysst_st_wh ON dbo.systemssecuritytoken (securitytokenid, warehousefromdate);

-- Indexes for dbo.systemxclassification
CREATE INDEX idx_sysxc_eff_from ON dbo.systemxclassification (effectivefromdate);
CREATE INDEX idx_sysxc_eff_to ON dbo.systemxclassification (effectivetodate);
CREATE INDEX idx_sysxc_wh_created ON dbo.systemxclassification (warehousecreatedtimestamp);
CREATE INDEX idx_sysxc_wh_updated ON dbo.systemxclassification (warehouselastupdatedtimestamp);
CREATE INDEX idx_sysxc_val ON dbo.systemxclassification (value);
CREATE INDEX idx_sysxc_ei_wh ON dbo.systemxclassification (enterpriseid, warehousefromdate);
CREATE INDEX idx_sysxc_af_wh ON dbo.systemxclassification (activeflagid, warehousefromdate);
CREATE INDEX idx_sysxc_sys_wh ON dbo.systemxclassification (systemid, warehousefromdate);
CREATE INDEX idx_sysxc_cl_wh ON dbo.systemxclassification (classificationid, warehousefromdate);

-- Indexes for dbo.systemxclassificationsecuritytoken
CREATE INDEX idx_sysxcst_eff_from ON dbo.systemxclassificationsecuritytoken (effectivefromdate);
CREATE INDEX idx_sysxcst_eff_to ON dbo.systemxclassificationsecuritytoken (effectivetodate);
CREATE INDEX idx_sysxcst_wh_created ON dbo.systemxclassificationsecuritytoken (warehousecreatedtimestamp);
CREATE INDEX idx_sysxcst_wh_updated ON dbo.systemxclassificationsecuritytoken (warehouselastupdatedtimestamp);
CREATE INDEX idx_sysxcst_ei_wh ON dbo.systemxclassificationsecuritytoken (enterpriseid, warehousefromdate);
CREATE INDEX idx_sysxcst_st_wh ON dbo.systemxclassificationsecuritytoken (securitytokenid, warehousefromdate);
CREATE INDEX idx_sysxcst_sys_wh ON dbo.systemxclassificationsecuritytoken (systemid, warehousefromdate);
CREATE INDEX idx_sysxcst_xcid_wh ON dbo.systemxclassificationsecuritytoken (systemxclassificationid, warehousefromdate);
CREATE INDEX idx_sysxcst_af_wh ON dbo.systemxclassificationsecuritytoken (activeflagid, warehousefromdate);

