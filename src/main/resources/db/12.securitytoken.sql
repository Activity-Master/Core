DO $fsdm_schema$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_catalog.pg_namespace WHERE nspname = 'security') THEN
        CREATE SCHEMA IF NOT EXISTS security;
    END IF;
END;
$fsdm_schema$;
CREATE TABLE IF NOT EXISTS security.securitytoken
(
    securitytokenid                  UUID                        NOT NULL primary key,
    effectivefromdate                timestamp(6) with time zone NOT NULL,
    effectivetodate                  timestamp(6) with time zone NOT NULL,
    warehousecreatedtimestamp        timestamp(6) with time zone NOT NULL,
    warehousefromdate                DATE                        NOT NULL,

    warehouselastupdatedtimestamp    timestamp(6) with time zone NOT NULL,
    originalsourcesystemuniqueid     UUID                        NOT NULL,
    securitytokenfriendlydescription character varying(255)      NOT NULL,
    securitytokenfriendlyname        character varying(255)      NOT NULL,
    securitytoken                    character varying(128)      NOT NULL,
    activeflagid                     UUID                        NOT NULL,
    enterpriseid                     UUID                        NOT NULL,
    systemid                         UUID                        NOT NULL,
    originalsourcesystemid           UUID                        NOT NULL,
    securitytokenclassificationid    UUID                        NOT NULL
);
CREATE TABLE IF NOT EXISTS security.securitytokenssecuritytoken
(
    securitytokenaccessid         UUID                        NOT NULL primary key,
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
    securitytokentoid             UUID                        NOT NULL
);
CREATE TABLE IF NOT EXISTS security.securitytokensxsecuritytokensecuritytoken
(
    securitytokenxsecuritytokensecuritytokenid UUID                        NOT NULL primary key,
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
    securitytokenxsecuritytokenid              UUID                        NOT NULL
);
CREATE TABLE IF NOT EXISTS security.securitytokenxclassification
(
    securitytokenxclassificationid UUID                        NOT NULL primary key,
    effectivefromdate              timestamp(6) with time zone NOT NULL,
    effectivetodate                timestamp(6) with time zone NOT NULL,
    warehousecreatedtimestamp      timestamp(6) with time zone NOT NULL,
    warehousefromdate              DATE                        NOT NULL,

    warehouselastupdatedtimestamp  timestamp(6) with time zone NOT NULL,
    originalsourcesystemuniqueid   UUID                        NOT NULL,
    value                          varchar(200)                NOT NULL,
    activeflagid                   UUID                        NOT NULL,
    enterpriseid                   UUID                        NOT NULL,
    systemid                       UUID                        NOT NULL,
    originalsourcesystemid         UUID                        NOT NULL,
    classificationid               UUID                        NOT NULL,
    securitytokenid                UUID                        NOT NULL
);
CREATE TABLE IF NOT EXISTS security.securitytokenxclassificationsecuritytoken
(
    securitytokenxclassificationsecuritytokenid UUID                        NOT NULL primary key,
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
    securitytokenxclassificationid              UUID                        NOT NULL
);
CREATE TABLE IF NOT EXISTS security.securitytokenxsecuritytoken
(
    securitytokenxsecuritytokenid UUID                        NOT NULL primary key,
    effectivefromdate             timestamp(6) with time zone NOT NULL,
    effectivetodate               timestamp(6) with time zone NOT NULL,
    warehousecreatedtimestamp     timestamp(6) with time zone NOT NULL,
    warehousefromdate             DATE                        NOT NULL,

    warehouselastupdatedtimestamp timestamp(6) with time zone NOT NULL,
    originalsourcesystemuniqueid  UUID                        NOT NULL,
    value                         varchar(200)                NOT NULL,
    activeflagid                  UUID                        NOT NULL,
    enterpriseid                  UUID                        NOT NULL,
    systemid                      UUID                        NOT NULL,
    originalsourcesystemid        UUID                        NOT NULL,
    classificationid              UUID                        NOT NULL,
    childsecuritytokenid          UUID                        NOT NULL,
    parentsecuritytokenid         UUID                        NOT NULL
);
-- Indexes for security.securitytoken
CREATE INDEX IF NOT EXISTS idx_st_eff_from ON security.securitytoken (effectivefromdate);
CREATE INDEX IF NOT EXISTS idx_st_eff_to ON security.securitytoken (effectivetodate);
CREATE INDEX IF NOT EXISTS idx_st_wh_created ON security.securitytoken (warehousecreatedtimestamp);
CREATE INDEX IF NOT EXISTS idx_st_wh_updated ON security.securitytoken (warehouselastupdatedtimestamp);
CREATE INDEX IF NOT EXISTS idx_st_ei_wh ON security.securitytoken (enterpriseid, warehousefromdate);
CREATE INDEX IF NOT EXISTS idx_st_af_wh ON security.securitytoken (activeflagid, warehousefromdate);
CREATE INDEX IF NOT EXISTS idx_st_sys_wh ON security.securitytoken (systemid, warehousefromdate);
CREATE INDEX IF NOT EXISTS idx_st_cl_wh ON security.securitytoken (securitytokenclassificationid, warehousefromdate);

-- Indexes for security.securitytokenssecuritytoken
CREATE INDEX IF NOT EXISTS idx_sst_eff_from ON security.securitytokenssecuritytoken (effectivefromdate);
CREATE INDEX IF NOT EXISTS idx_sst_eff_to ON security.securitytokenssecuritytoken (effectivetodate);
CREATE INDEX IF NOT EXISTS idx_sst_wh_created ON security.securitytokenssecuritytoken (warehousecreatedtimestamp);
CREATE INDEX IF NOT EXISTS idx_sst_wh_updated ON security.securitytokenssecuritytoken (warehouselastupdatedtimestamp);
CREATE INDEX IF NOT EXISTS idx_sst_ei_wh ON security.securitytokenssecuritytoken (enterpriseid, warehousefromdate);
CREATE INDEX IF NOT EXISTS idx_sst_af_wh ON security.securitytokenssecuritytoken (activeflagid, warehousefromdate);
CREATE INDEX IF NOT EXISTS idx_sst_sid_wh ON security.securitytokenssecuritytoken (securitytokenid, warehousefromdate);
CREATE INDEX IF NOT EXISTS idx_sst_tid_wh ON security.securitytokenssecuritytoken (securitytokentoid, warehousefromdate);

-- Indexes for security.securitytokensxsecuritytokensecuritytoken
CREATE INDEX IF NOT EXISTS idx_sstxst_eff_from ON security.securitytokensxsecuritytokensecuritytoken (effectivefromdate);
CREATE INDEX IF NOT EXISTS idx_sstxst_eff_to ON security.securitytokensxsecuritytokensecuritytoken (effectivetodate);
CREATE INDEX IF NOT EXISTS idx_sstxst_wh_created ON security.securitytokensxsecuritytokensecuritytoken (warehousecreatedtimestamp);
CREATE INDEX IF NOT EXISTS idx_sstxst_wh_updated ON security.securitytokensxsecuritytokensecuritytoken (warehouselastupdatedtimestamp);
CREATE INDEX IF NOT EXISTS idx_sstxst_ei_wh ON security.securitytokensxsecuritytokensecuritytoken (enterpriseid, warehousefromdate);
CREATE INDEX IF NOT EXISTS idx_sstxst_af_wh ON security.securitytokensxsecuritytokensecuritytoken (activeflagid, warehousefromdate);
CREATE INDEX IF NOT EXISTS idx_sstxst_sid_wh ON security.securitytokensxsecuritytokensecuritytoken (securitytokenid, warehousefromdate);

-- Indexes for security.securitytokenxclassification
CREATE INDEX IF NOT EXISTS idx_stxc_eff_from ON security.securitytokenxclassification (effectivefromdate);
CREATE INDEX IF NOT EXISTS idx_stxc_eff_to ON security.securitytokenxclassification (effectivetodate);
CREATE INDEX IF NOT EXISTS idx_stxc_wh_created ON security.securitytokenxclassification (warehousecreatedtimestamp);
CREATE INDEX IF NOT EXISTS idx_stxc_wh_updated ON security.securitytokenxclassification (warehouselastupdatedtimestamp);
CREATE INDEX IF NOT EXISTS idx_stxc_ei_wh ON security.securitytokenxclassification (enterpriseid, warehousefromdate);
CREATE INDEX IF NOT EXISTS idx_stxc_af_wh ON security.securitytokenxclassification (activeflagid, warehousefromdate);
CREATE INDEX IF NOT EXISTS idx_stxc_sys_wh ON security.securitytokenxclassification (systemid, warehousefromdate);
CREATE INDEX IF NOT EXISTS idx_stxc_sid_wh ON security.securitytokenxclassification (securitytokenid, warehousefromdate);

-- Indexes for security.securitytokenxclassificationsecuritytoken
CREATE INDEX IF NOT EXISTS idx_stxcst_eff_from ON security.securitytokenxclassificationsecuritytoken (effectivefromdate);
CREATE INDEX IF NOT EXISTS idx_stxcst_eff_to ON security.securitytokenxclassificationsecuritytoken (effectivetodate);
CREATE INDEX IF NOT EXISTS idx_stxcst_wh_created ON security.securitytokenxclassificationsecuritytoken (warehousecreatedtimestamp);
CREATE INDEX IF NOT EXISTS idx_stxcst_wh_updated ON security.securitytokenxclassificationsecuritytoken (warehouselastupdatedtimestamp);
CREATE INDEX IF NOT EXISTS idx_stxcst_ei_wh ON security.securitytokenxclassificationsecuritytoken (enterpriseid, warehousefromdate);
CREATE INDEX IF NOT EXISTS idx_stxcst_af_wh ON security.securitytokenxclassificationsecuritytoken (activeflagid, warehousefromdate);
CREATE INDEX IF NOT EXISTS idx_stxcst_sys_wh ON security.securitytokenxclassificationsecuritytoken (systemid, warehousefromdate);

-- Indexes for security.securitytokenxsecuritytoken
CREATE INDEX IF NOT EXISTS idx_stxst_eff_from ON security.securitytokenxsecuritytoken (effectivefromdate);
CREATE INDEX IF NOT EXISTS idx_stxst_eff_to ON security.securitytokenxsecuritytoken (effectivetodate);
CREATE INDEX IF NOT EXISTS idx_stxst_wh_created ON security.securitytokenxsecuritytoken (warehousecreatedtimestamp);
CREATE INDEX IF NOT EXISTS idx_stxst_wh_updated ON security.securitytokenxsecuritytoken (warehouselastupdatedtimestamp);
CREATE INDEX IF NOT EXISTS idx_stxst_ei_wh ON security.securitytokenxsecuritytoken (enterpriseid, warehousefromdate);
CREATE INDEX IF NOT EXISTS idx_stxst_af_wh ON security.securitytokenxsecuritytoken (activeflagid, warehousefromdate);
CREATE INDEX IF NOT EXISTS idx_stxst_sys_wh ON security.securitytokenxsecuritytoken (systemid, warehousefromdate);
CREATE INDEX IF NOT EXISTS idx_stxst_cl_wh ON security.securitytokenxsecuritytoken (classificationid, warehousefromdate);
