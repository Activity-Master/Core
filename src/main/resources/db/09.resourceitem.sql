CREATE SCHEMA resource;
CREATE TABLE resource.resourceitem
(
    resourceitemid                UUID                        NOT NULL primary key,
    effectivefromdate             timestamp(6) with time zone NOT NULL DEFAULT now(),
    effectivetodate               timestamp(6) with time zone NOT NULL DEFAULT '2999-12-31 23:59:59.999+00',
    warehousecreatedtimestamp     timestamp(6) with time zone NOT NULL DEFAULT now(),
    warehousefromdate             DATE                        NOT NULL DEFAULT current_date,

    warehouselastupdatedtimestamp timestamp(6) with time zone NOT NULL DEFAULT now(),
    originalsourcesystemuniqueid  UUID                        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    resourceitemdatatype          character varying(150)      NOT NULL,
    activeflagid                  UUID                        NOT NULL,
    enterpriseid                  UUID                        NOT NULL,
    systemid                      UUID                        NOT NULL,
    originalsourcesystemid        UUID                        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000'
);
CREATE TABLE resource.resourceitemdata
(
    resourceitemdataid            UUID                        NOT NULL primary key,
    effectivefromdate             timestamp(6) with time zone NOT NULL DEFAULT now(),
    effectivetodate               timestamp(6) with time zone NOT NULL DEFAULT '2999-12-31 23:59:59.999+00',
    warehousecreatedtimestamp     timestamp(6) with time zone NOT NULL DEFAULT now(),
    warehousefromdate             DATE                        NOT NULL DEFAULT current_date,

    warehouselastupdatedtimestamp timestamp(6) with time zone NOT NULL DEFAULT now(),
    originalsourcesystemuniqueid  UUID                        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    resourceitemdata              bytea                       NOT NULL,
    activeflagid                  UUID                        NOT NULL,
    enterpriseid                  UUID                        NOT NULL,
    systemid                      UUID                        NOT NULL,
    originalsourcesystemid        UUID                        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    resourceitemid                UUID                        NOT NULL
);
CREATE TABLE resource.resourceitemdatasecuritytoken
(
    resourceitemdatasecuritytokenid UUID                        NOT NULL primary key,
    effectivefromdate               timestamp(6) with time zone NOT NULL DEFAULT now(),
    effectivetodate                 timestamp(6) with time zone NOT NULL DEFAULT '2999-12-31 23:59:59.999+00',
    warehousecreatedtimestamp       timestamp(6) with time zone NOT NULL DEFAULT now(),
    warehousefromdate               DATE                        NOT NULL DEFAULT current_date,

    warehouselastupdatedtimestamp   timestamp(6) with time zone NOT NULL DEFAULT now(),
    createallowed                   INTEGER                     NOT NULL,
    deleteallowed                   INTEGER                     NOT NULL,
    originalsourcesystemuniqueid    UUID                        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    readallowed                     INTEGER                     NOT NULL,
    updateallowed                   INTEGER                     NOT NULL,
    activeflagid                    UUID                        NOT NULL,
    enterpriseid                    UUID                        NOT NULL,
    originalsourcesystemid          UUID                        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    securitytokenid                 UUID                        NOT NULL,
    systemid                        UUID                        NOT NULL,
    resourceitemdataid              UUID                        NOT NULL
);
CREATE TABLE resource.resourceitemdataxclassification
(
    resourceitemdataxclassificationid UUID                        NOT NULL primary key,
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
    resourceitemdataid                UUID                        NOT NULL
);
CREATE TABLE resource.resourceitemdataxclassificationsecuritytoken
(
    resourceitemdataxclassificationsecuritytokenid UUID                        NOT NULL primary key,
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
    resourceitemdataxclassificationid              UUID                        NOT NULL
);
CREATE TABLE resource.resourceitemsecuritytoken
(
    resourceitemsecuritytokenid   UUID                        NOT NULL primary key,
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
    resourceitemid                UUID                        NOT NULL
);
CREATE TABLE resource.resourceitemtype
(
    resourceitemtypeid            UUID                        NOT NULL primary key,
    effectivefromdate             timestamp(6) with time zone NOT NULL DEFAULT now(),
    effectivetodate               timestamp(6) with time zone NOT NULL DEFAULT '2999-12-31 23:59:59.999+00',
    warehousecreatedtimestamp     timestamp(6) with time zone NOT NULL DEFAULT now(),
    warehousefromdate             DATE                        NOT NULL DEFAULT current_date,

    warehouselastupdatedtimestamp timestamp(6) with time zone NOT NULL DEFAULT now(),
    originalsourcesystemuniqueid  UUID                        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    resourceitemtypedesc          character varying(255)      NOT NULL,
    resourceitemtypename          character varying(100)      NOT NULL,
    activeflagid                  UUID                        NOT NULL,
    enterpriseid                  UUID                        NOT NULL,
    systemid                      UUID                        NOT NULL,
    originalsourcesystemid        UUID                        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000'
);
CREATE TABLE resource.resourceitemtypesecuritytoken
(
    resourceitemtypesecuritytokenid UUID                        NOT NULL primary key,
    effectivefromdate               timestamp(6) with time zone NOT NULL DEFAULT now(),
    effectivetodate                 timestamp(6) with time zone NOT NULL DEFAULT '2999-12-31 23:59:59.999+00',
    warehousecreatedtimestamp       timestamp(6) with time zone NOT NULL DEFAULT now(),
    warehousefromdate               DATE                        NOT NULL DEFAULT current_date,

    warehouselastupdatedtimestamp   timestamp(6) with time zone NOT NULL DEFAULT now(),
    createallowed                   INTEGER                     NOT NULL,
    deleteallowed                   INTEGER                     NOT NULL,
    originalsourcesystemuniqueid    UUID                        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    readallowed                     INTEGER                     NOT NULL,
    updateallowed                   INTEGER                     NOT NULL,
    activeflagid                    UUID                        NOT NULL,
    enterpriseid                    UUID                        NOT NULL,
    originalsourcesystemid          UUID                        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    securitytokenid                 UUID                        NOT NULL,
    systemid                        UUID                        NOT NULL,
    resourceitemtypeid              UUID                        NOT NULL
);
CREATE TABLE resource.resourceitemxclassification
(
    resourceitemxclassificationid UUID                        NOT NULL primary key,
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
    resourceitemid                UUID                        NOT NULL
);
CREATE TABLE resource.resourceitemxclassificationsecuritytoken
(
    resourceitemxclassificationsecuritytokenid UUID                        NOT NULL primary key,
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
    resourceitemxclassificationid              UUID                        NOT NULL
);
CREATE TABLE resource.resourceitemxresourceitem
(
    resourceitemxresourceitemid   UUID                        NOT NULL primary key,
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
    childresourceitemid           UUID                        NOT NULL,
    parentresourceitemid          UUID                        NOT NULL
);
CREATE TABLE resource.resourceitemxresourceitemsecuritytoken
(
    resourceitemxresourceitemsecuritytokenid UUID                        NOT NULL primary key,
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
    resourceitemxresourceitemid              UUID                        NOT NULL
);
CREATE TABLE resource.resourceitemxresourceitemtype
(
    resourceitemxresourceitemtypeid UUID                        NOT NULL primary key,
    effectivefromdate               timestamp(6) with time zone NOT NULL DEFAULT now(),
    effectivetodate                 timestamp(6) with time zone NOT NULL DEFAULT '2999-12-31 23:59:59.999+00',
    warehousecreatedtimestamp       timestamp(6) with time zone NOT NULL DEFAULT now(),
    warehousefromdate               DATE                        NOT NULL DEFAULT current_date,

    warehouselastupdatedtimestamp   timestamp(6) with time zone NOT NULL DEFAULT now(),
    originalsourcesystemuniqueid    UUID                        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    value                           varchar(200)                NOT NULL ,
    activeflagid                    UUID                        NOT NULL,
    enterpriseid                    UUID                        NOT NULL,
    systemid                        UUID                        NOT NULL,
    originalsourcesystemid          UUID                        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    classificationid                UUID                        NOT NULL,
    resourceitemid                  UUID                        NOT NULL,
    resourceitemtypeid              UUID                        NOT NULL
);
CREATE TABLE resource.resourceitemxresourceitemtypesecuritytoken
(
    resourceitemxresourceitemtypesecuritytokenid UUID                        NOT NULL primary key,
    effectivefromdate                            timestamp(6) with time zone NOT NULL DEFAULT now(),
    effectivetodate                              timestamp(6) with time zone NOT NULL DEFAULT '2999-12-31 23:59:59.999+00',
    warehousecreatedtimestamp                    timestamp(6) with time zone NOT NULL DEFAULT now(),
    warehousefromdate                            DATE                        NOT NULL DEFAULT current_date,

    warehouselastupdatedtimestamp                timestamp(6) with time zone NOT NULL DEFAULT now(),
    createallowed                                INTEGER                     NOT NULL,
    deleteallowed                                INTEGER                     NOT NULL,
    originalsourcesystemuniqueid                 UUID                        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    readallowed                                  INTEGER                     NOT NULL,
    updateallowed                                INTEGER                     NOT NULL,
    activeflagid                                 UUID                        NOT NULL,
    enterpriseid                                 UUID                        NOT NULL,
    originalsourcesystemid                       UUID                        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    securitytokenid                              UUID                        NOT NULL,
    systemid                                     UUID                        NOT NULL,
    resourceitemxresourceitemtypeid              UUID                        NOT NULL
);


alter table resource.resourceitemdata
    alter COLUMN resourceitemdata SET COMPRESSION lz4;
alter table resource.resourceitemdata
    alter COLUMN resourceitemdata SET STORAGE EXTERNAL;

-- Indexes for resource.resourceitem
CREATE INDEX idx_ri_eff_from ON resource.resourceitem (effectivefromdate);
CREATE INDEX idx_ri_eff_to ON resource.resourceitem (effectivetodate);
CREATE INDEX idx_ri_wh_created ON resource.resourceitem (warehousecreatedtimestamp);
CREATE INDEX idx_ri_wh_updated ON resource.resourceitem (warehouselastupdatedtimestamp);
CREATE INDEX idx_ri_ei_wh ON resource.resourceitem (enterpriseid, warehousefromdate);
CREATE INDEX idx_ri_af_wh ON resource.resourceitem (activeflagid, warehousefromdate);
CREATE INDEX idx_ri_sys_wh ON resource.resourceitem (systemid, warehousefromdate);

-- Indexes for resource.resourceitemdata
CREATE INDEX idx_rid_eff_from ON resource.resourceitemdata (effectivefromdate);
CREATE INDEX idx_rid_eff_to ON resource.resourceitemdata (effectivetodate);
CREATE INDEX idx_rid_wh_created ON resource.resourceitemdata (warehousecreatedtimestamp);
CREATE INDEX idx_rid_wh_updated ON resource.resourceitemdata (warehouselastupdatedtimestamp);
CREATE INDEX idx_rid_ei_wh ON resource.resourceitemdata (enterpriseid, warehousefromdate);
CREATE INDEX idx_rid_af_wh ON resource.resourceitemdata (activeflagid, warehousefromdate);
CREATE INDEX idx_rid_sys_wh ON resource.resourceitemdata (systemid, warehousefromdate);
CREATE INDEX idx_rid_rid_wh ON resource.resourceitemdata (resourceitemid, warehousefromdate);

-- Indexes for resource.resourceitemdatasecuritytoken
CREATE INDEX idx_ridst_eff_from ON resource.resourceitemdatasecuritytoken (effectivefromdate);
CREATE INDEX idx_ridst_eff_to ON resource.resourceitemdatasecuritytoken (effectivetodate);
CREATE INDEX idx_ridst_wh_created ON resource.resourceitemdatasecuritytoken (warehousecreatedtimestamp);
CREATE INDEX idx_ridst_wh_updated ON resource.resourceitemdatasecuritytoken (warehouselastupdatedtimestamp);
CREATE INDEX idx_ridst_ei_wh ON resource.resourceitemdatasecuritytoken (enterpriseid, warehousefromdate);
CREATE INDEX idx_ridst_af_wh ON resource.resourceitemdatasecuritytoken (activeflagid, warehousefromdate);
CREATE INDEX idx_ridst_sys_wh ON resource.resourceitemdatasecuritytoken (systemid, warehousefromdate);
CREATE INDEX idx_ridst_st_wh ON resource.resourceitemdatasecuritytoken (securitytokenid, warehousefromdate);

-- Indexes for resource.resourceitemdataxclassification
CREATE INDEX idx_ridxc_eff_from ON resource.resourceitemdataxclassification (effectivefromdate);
CREATE INDEX idx_ridxc_eff_to ON resource.resourceitemdataxclassification (effectivetodate);
CREATE INDEX idx_ridxc_wh_created ON resource.resourceitemdataxclassification (warehousecreatedtimestamp);
CREATE INDEX idx_ridxc_wh_updated ON resource.resourceitemdataxclassification (warehouselastupdatedtimestamp);
CREATE INDEX idx_ridxc_val ON resource.resourceitemdataxclassification (value);
CREATE INDEX idx_ridxc_ei_wh ON resource.resourceitemdataxclassification (enterpriseid, warehousefromdate);
CREATE INDEX idx_ridxc_af_wh ON resource.resourceitemdataxclassification (activeflagid, warehousefromdate);
CREATE INDEX idx_ridxc_sys_wh ON resource.resourceitemdataxclassification (systemid, warehousefromdate);
CREATE INDEX idx_ridxc_rid_wh ON resource.resourceitemdataxclassification (resourceitemdataid, warehousefromdate);

-- Indexes for resource.resourceitemdataxclassificationsecuritytoken
CREATE INDEX idx_ridxcst_eff_from ON resource.resourceitemdataxclassificationsecuritytoken (effectivefromdate);
CREATE INDEX idx_ridxcst_eff_to ON resource.resourceitemdataxclassificationsecuritytoken (effectivetodate);
CREATE INDEX idx_ridxcst_wh_created ON resource.resourceitemdataxclassificationsecuritytoken (warehousecreatedtimestamp);
CREATE INDEX idx_ridxcst_wh_updated ON resource.resourceitemdataxclassificationsecuritytoken (warehouselastupdatedtimestamp);
CREATE INDEX idx_ridxcst_ei_wh ON resource.resourceitemdataxclassificationsecuritytoken (enterpriseid, warehousefromdate);
CREATE INDEX idx_ridxcst_st_wh ON resource.resourceitemdataxclassificationsecuritytoken (securitytokenid, warehousefromdate);
CREATE INDEX idx_ridxcst_sys_wh ON resource.resourceitemdataxclassificationsecuritytoken (systemid, warehousefromdate);

-- Indexes for resource.resourceitemsecuritytoken
CREATE INDEX idx_rist_eff_from ON resource.resourceitemsecuritytoken (effectivefromdate);
CREATE INDEX idx_rist_eff_to ON resource.resourceitemsecuritytoken (effectivetodate);
CREATE INDEX idx_rist_wh_created ON resource.resourceitemsecuritytoken (warehousecreatedtimestamp);
CREATE INDEX idx_rist_wh_updated ON resource.resourceitemsecuritytoken (warehouselastupdatedtimestamp);
CREATE INDEX idx_rist_ei_wh ON resource.resourceitemsecuritytoken (enterpriseid, warehousefromdate);
CREATE INDEX idx_rist_af_wh ON resource.resourceitemsecuritytoken (activeflagid, warehousefromdate);
CREATE INDEX idx_rist_sys_wh ON resource.resourceitemsecuritytoken (systemid, warehousefromdate);
CREATE INDEX idx_rist_st_wh ON resource.resourceitemsecuritytoken (securitytokenid, warehousefromdate);
CREATE INDEX idx_rist_rid_wh ON resource.resourceitemsecuritytoken (resourceitemid, warehousefromdate);

-- Indexes for resource.resourceitemtype
CREATE INDEX idx_rit_eff_from ON resource.resourceitemtype (effectivefromdate);
CREATE INDEX idx_rit_eff_to ON resource.resourceitemtype (effectivetodate);
CREATE INDEX idx_rit_wh_created ON resource.resourceitemtype (warehousecreatedtimestamp);
CREATE INDEX idx_rit_wh_updated ON resource.resourceitemtype (warehouselastupdatedtimestamp);
CREATE INDEX idx_rit_ei_wh ON resource.resourceitemtype (enterpriseid, warehousefromdate);
CREATE INDEX idx_rit_af_wh ON resource.resourceitemtype (activeflagid, warehousefromdate);
CREATE INDEX idx_rit_sys_wh ON resource.resourceitemtype (systemid, warehousefromdate);

-- Indexes for resource.resourceitemtypesecuritytoken
CREATE INDEX idx_ritst_eff_from ON resource.resourceitemtypesecuritytoken (effectivefromdate);
CREATE INDEX idx_ritst_eff_to ON resource.resourceitemtypesecuritytoken (effectivetodate);
CREATE INDEX idx_ritst_wh_created ON resource.resourceitemtypesecuritytoken (warehousecreatedtimestamp);
CREATE INDEX idx_ritst_wh_updated ON resource.resourceitemtypesecuritytoken (warehouselastupdatedtimestamp);
CREATE INDEX idx_ritst_ei_wh ON resource.resourceitemtypesecuritytoken (enterpriseid, warehousefromdate);
CREATE INDEX idx_ritst_st_wh ON resource.resourceitemtypesecuritytoken (securitytokenid, warehousefromdate);
CREATE INDEX idx_ritst_sys_wh ON resource.resourceitemtypesecuritytoken (systemid, warehousefromdate);

-- Indexes for resource.resourceitemxclassification
CREATE INDEX idx_rixc_eff_from ON resource.resourceitemxclassification (effectivefromdate);
CREATE INDEX idx_rixc_eff_to ON resource.resourceitemxclassification (effectivetodate);
CREATE INDEX idx_rixc_wh_created ON resource.resourceitemxclassification (warehousecreatedtimestamp);
CREATE INDEX idx_rixc_wh_updated ON resource.resourceitemxclassification (warehouselastupdatedtimestamp);
CREATE INDEX idx_rixc_val ON resource.resourceitemxclassification (value);
CREATE INDEX idx_rixc_ei_wh ON resource.resourceitemxclassification (enterpriseid, warehousefromdate);
CREATE INDEX idx_rixc_af_wh ON resource.resourceitemxclassification (activeflagid, warehousefromdate);
CREATE INDEX idx_rixc_sys_wh ON resource.resourceitemxclassification (systemid, warehousefromdate);
CREATE INDEX idx_rixc_rid_wh ON resource.resourceitemxclassification (resourceitemid, warehousefromdate);

-- Indexes for resource.resourceitemxclassificationsecuritytoken
CREATE INDEX idx_rixcst_eff_from ON resource.resourceitemxclassificationsecuritytoken (effectivefromdate);
CREATE INDEX idx_rixcst_eff_to ON resource.resourceitemxclassificationsecuritytoken (effectivetodate);
CREATE INDEX idx_rixcst_wh_created ON resource.resourceitemxclassificationsecuritytoken (warehousecreatedtimestamp);
CREATE INDEX idx_rixcst_wh_updated ON resource.resourceitemxclassificationsecuritytoken (warehouselastupdatedtimestamp);
CREATE INDEX idx_rixcst_ei_wh ON resource.resourceitemxclassificationsecuritytoken (enterpriseid, warehousefromdate);
CREATE INDEX idx_rixcst_st_wh ON resource.resourceitemxclassificationsecuritytoken (securitytokenid, warehousefromdate);
CREATE INDEX idx_rixcst_sys_wh ON resource.resourceitemxclassificationsecuritytoken (systemid, warehousefromdate);

-- Indexes for resource.resourceitemxresourceitem
CREATE INDEX idx_rixri_eff_from ON resource.resourceitemxresourceitem (effectivefromdate);
CREATE INDEX idx_rixri_eff_to ON resource.resourceitemxresourceitem (effectivetodate);
CREATE INDEX idx_rixri_wh_created ON resource.resourceitemxresourceitem (warehousecreatedtimestamp);
CREATE INDEX idx_rixri_wh_updated ON resource.resourceitemxresourceitem (warehouselastupdatedtimestamp);
CREATE INDEX idx_rixri_val ON resource.resourceitemxresourceitem (value);
CREATE INDEX idx_rixri_ei_wh ON resource.resourceitemxresourceitem (enterpriseid, warehousefromdate);
CREATE INDEX idx_rixri_af_wh ON resource.resourceitemxresourceitem (activeflagid, warehousefromdate);
CREATE INDEX idx_rixri_sys_wh ON resource.resourceitemxresourceitem (systemid, warehousefromdate);
CREATE INDEX idx_rixri_cl_wh ON resource.resourceitemxresourceitem (classificationid, warehousefromdate);

-- Indexes for resource.resourceitemxresourceitemsecuritytoken
CREATE INDEX idx_rixrist_eff_from ON resource.resourceitemxresourceitemsecuritytoken (effectivefromdate);
CREATE INDEX idx_rixrist_eff_to ON resource.resourceitemxresourceitemsecuritytoken (effectivetodate);
CREATE INDEX idx_rixrist_wh_created ON resource.resourceitemxresourceitemsecuritytoken (warehousecreatedtimestamp);
CREATE INDEX idx_rixrist_wh_updated ON resource.resourceitemxresourceitemsecuritytoken (warehouselastupdatedtimestamp);
CREATE INDEX idx_rixrist_ei_wh ON resource.resourceitemxresourceitemsecuritytoken (enterpriseid, warehousefromdate);
CREATE INDEX idx_rixrist_st_wh ON resource.resourceitemxresourceitemsecuritytoken (securitytokenid, warehousefromdate);
CREATE INDEX idx_rixrist_sys_wh ON resource.resourceitemxresourceitemsecuritytoken (systemid, warehousefromdate);
CREATE INDEX idx_rixrist_af_wh ON resource.resourceitemxresourceitemsecuritytoken (activeflagid, warehousefromdate);

-- Indexes for resource.resourceitemxresourceitemtype
CREATE INDEX idx_rixrit_eff_from ON resource.resourceitemxresourceitemtype (effectivefromdate);
CREATE INDEX idx_rixrit_eff_to ON resource.resourceitemxresourceitemtype (effectivetodate);
CREATE INDEX idx_rixrit_wh_created ON resource.resourceitemxresourceitemtype (warehousecreatedtimestamp);
CREATE INDEX idx_rixrit_wh_updated ON resource.resourceitemxresourceitemtype (warehouselastupdatedtimestamp);
CREATE INDEX idx_rixrit_val ON resource.resourceitemxresourceitemtype (value);
CREATE INDEX idx_rixrit_ei_wh ON resource.resourceitemxresourceitemtype (enterpriseid, warehousefromdate);
CREATE INDEX idx_rixrit_af_wh ON resource.resourceitemxresourceitemtype (activeflagid, warehousefromdate);
CREATE INDEX idx_rixrit_sys_wh ON resource.resourceitemxresourceitemtype (systemid, warehousefromdate);
CREATE INDEX idx_rixrit_cl_wh ON resource.resourceitemxresourceitemtype (classificationid, warehousefromdate);
CREATE INDEX idx_rixrit_rid_wh ON resource.resourceitemxresourceitemtype (resourceitemid, warehousefromdate);


--drop table if exists resource.resourceitemdatavalue;
-- payload table (LOGGED because you cannot lose data)
CREATE TABLE IF NOT EXISTS resource.resourceitemdatavalue
(
    resourceitemdatavalueid uuid  NOT NULL, -- == resourceitemid
    resourceitemdatavalue   bytea NULL,     -- optional payload
    CONSTRAINT resourceitemdatavalue_pkey PRIMARY KEY (resourceitemdatavalueid)
);

ALTER TABLE resource.resourceitemdatavalue
    ALTER COLUMN resourceitemdatavalue SET STORAGE EXTENDED;

ALTER TABLE resource.resourceitemdatavalue
    ALTER COLUMN resourceitemdatavalue SET COMPRESSION lz4;

DO
$$
    DECLARE
        missing_payload_rows bigint;
        orphan_payload_rows  bigint;
        data_without_item    bigint;
    BEGIN
        -- 1) link column on data (optional)
        IF NOT EXISTS (SELECT 1
                       FROM information_schema.columns
                       WHERE table_schema = 'resource'
                         AND table_name = 'resourceitemdata'
                         AND column_name = 'resourceitemdatavalueid') THEN
            ALTER TABLE resource.resourceitemdata
                ADD COLUMN resourceitemdatavalueid uuid;
        END IF;

        -- 2) copy payloads for rows that actually have payload
        INSERT INTO resource.resourceitemdatavalue (resourceitemdatavalueid, resourceitemdatavalue)
        SELECT d.resourceitemid, d.resourceitemdata
        FROM resource.resourceitemdata d
        WHERE d.resourceitemdata IS NOT NULL
        ON CONFLICT (resourceitemdatavalueid) DO NOTHING;

        -- 3) set the link only where payload exists
        UPDATE resource.resourceitemdata d
        SET resourceitemdatavalueid = d.resourceitemid
        WHERE d.resourceitemdata IS NOT NULL
          AND d.resourceitemdatavalueid IS DISTINCT FROM d.resourceitemid;

    END
$$;

-- autovac + toast tuning (payload churn table)
ALTER TABLE resource.resourceitemdatavalue
    SET (
        autovacuum_vacuum_scale_factor = 0.005,
        autovacuum_analyze_scale_factor = 0.005,
        autovacuum_vacuum_threshold = 2000,
        autovacuum_analyze_threshold = 2000,
        autovacuum_vacuum_cost_limit = 10000,
        toast_tuple_target = 2048
        );

CREATE INDEX IF NOT EXISTS resourceitemdata_resourceitemdatavalueid_notnull_idx
    ON resource.resourceitemdata (resourceitemdatavalueid)
    WHERE resourceitemdatavalueid IS NOT NULL;

alter table resource.resourceitemdata
    drop COLUMN resourceitemdata;

CREATE INDEX IF NOT EXISTS rix_cls_val_effdesc_idx
    ON resource.ResourceItemXClassification
        (ClassificationID, Value, EffectiveFromDate DESC)
    INCLUDE (ResourceItemID);

CREATE INDEX IF NOT EXISTS ric_item_class_eff_idx
    ON resource.ResourceItemXClassification
        (ResourceItemID, ClassificationID, EffectiveFromDate, EffectiveToDate)
    INCLUDE (Value, ActiveFlagID);

CREATE INDEX IF NOT EXISTS ric_class_value_eff_idx
    ON resource.ResourceItemXClassification
        (ClassificationID, Value, EffectiveFromDate DESC)
    INCLUDE (ResourceItemID, ActiveFlagID);

