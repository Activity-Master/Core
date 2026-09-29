CREATE SCHEMA geography;
CREATE TABLE geography.geography
(
    geographyid                   UUID                        NOT NULL primary key,
    effectivefromdate             timestamp(6) with time zone NOT NULL DEFAULT now(),
    effectivetodate               timestamp(6) with time zone NOT NULL DEFAULT '2999-12-31 23:59:59.999+00',
    warehousecreatedtimestamp     timestamp(6) with time zone NOT NULL DEFAULT now(),
    warehousefromdate             DATE                        NOT NULL DEFAULT current_date,

    warehouselastupdatedtimestamp timestamp(6) with time zone NOT NULL DEFAULT now(),
    originalsourcesystemuniqueid  UUID                        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    geographydesc                 character varying(500)      NOT NULL,
    geographyname                 character varying(500)      NOT NULL,
    activeflagid                  UUID                        NOT NULL,
    enterpriseid                  UUID                        NOT NULL,
    systemid                      UUID                        NOT NULL,
    originalsourcesystemid        UUID                        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    classificationid              UUID                        NOT NULL
);
CREATE TABLE geography.geographysecuritytoken
(
    geographysecuritytokenid      UUID                        NOT NULL primary key,
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
    geographyid                   UUID                        NOT NULL
);
CREATE TABLE geography.geographyxclassification
(
    geographyxclassificationid    UUID                        NOT NULL primary key,
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
    geographyid                   UUID                        NOT NULL
);
CREATE TABLE geography.geographyxclassificationsecuritytoken
(
    geographyxclassificationsecuritytokenid UUID                        NOT NULL primary key,
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
    originalsourcesystemid                   UUID                        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    securitytokenid                         UUID                        NOT NULL,
    systemid                                UUID                        NOT NULL,
    geographyxclassificationid              UUID                        NOT NULL
);
CREATE TABLE geography.geographyxgeography
(
    geographyxgeographyid         UUID                        NOT NULL primary key,
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
    childgeographyid              UUID                        NOT NULL,
    parentgeographyid             UUID                        NOT NULL
);
CREATE TABLE geography.geographyxgeographysecuritytoken
(
    geographyxgeographysecuritytokenid UUID                        NOT NULL primary key,
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
    geographyxgeographyid              UUID                        NOT NULL
);
CREATE TABLE geography.geographyxresourceitem
(
    geographyxresourceitemid      UUID                        NOT NULL primary key,
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
    geographyid                   UUID                        NOT NULL,
    resourceitemid                UUID                        NOT NULL
);
CREATE TABLE geography.geographyxresourceitemsecuritytoken
(
    geographyxresourceitemsecuritytokenid UUID                        NOT NULL primary key,
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
    securitytokenid                       UUID                        NOT NULL,
    systemid                              UUID                        NOT NULL,
    geographyxresourceitemid              UUID                        NOT NULL
);


-- Indexes for geography.geography
CREATE INDEX idx_geo_eff_from ON geography.geography (effectivefromdate);
CREATE INDEX idx_geo_eff_to ON geography.geography (effectivetodate);
CREATE INDEX idx_geo_wh_created ON geography.geography (warehousecreatedtimestamp);
CREATE INDEX idx_geo_wh_updated ON geography.geography (warehouselastupdatedtimestamp);
CREATE INDEX idx_geo_ei_wh ON geography.geography (enterpriseid, warehousefromdate);
CREATE INDEX idx_geo_af_wh ON geography.geography (activeflagid, warehousefromdate);
CREATE INDEX idx_geo_sys_wh ON geography.geography (systemid, warehousefromdate);
CREATE INDEX idx_geo_cl_wh ON geography.geography (classificationid, warehousefromdate);

-- Indexes for geography.geographysecuritytoken
CREATE INDEX idx_gest_eff_from ON geography.geographysecuritytoken (effectivefromdate);
CREATE INDEX idx_gest_eff_to ON geography.geographysecuritytoken (effectivetodate);
CREATE INDEX idx_gest_wh_created ON geography.geographysecuritytoken (warehousecreatedtimestamp);
CREATE INDEX idx_gest_wh_updated ON geography.geographysecuritytoken (warehouselastupdatedtimestamp);
CREATE INDEX idx_gest_ei_wh ON geography.geographysecuritytoken (enterpriseid, warehousefromdate);
CREATE INDEX idx_gest_af_wh ON geography.geographysecuritytoken (activeflagid, warehousefromdate);
CREATE INDEX idx_gest_sys_wh ON geography.geographysecuritytoken (systemid, warehousefromdate);
CREATE INDEX idx_gest_gid_wh ON geography.geographysecuritytoken (geographyid, warehousefromdate);
CREATE INDEX idx_gest_st_wh ON geography.geographysecuritytoken (securitytokenid, warehousefromdate);

-- Indexes for geography.geographyxclassification
CREATE INDEX idx_gxcl_eff_from ON geography.geographyxclassification (effectivefromdate);
CREATE INDEX idx_gxcl_eff_to ON geography.geographyxclassification (effectivetodate);
CREATE INDEX idx_gxcl_wh_created ON geography.geographyxclassification (warehousecreatedtimestamp);
CREATE INDEX idx_gxcl_wh_updated ON geography.geographyxclassification (warehouselastupdatedtimestamp);
CREATE INDEX idx_gxcl_ei_wh ON geography.geographyxclassification (enterpriseid, warehousefromdate);
CREATE INDEX idx_gxcl_af_wh ON geography.geographyxclassification (activeflagid, warehousefromdate);
CREATE INDEX idx_gxcl_sys_wh ON geography.geographyxclassification (systemid, warehousefromdate);
CREATE INDEX idx_gxcl_gid_wh ON geography.geographyxclassification (geographyid, warehousefromdate);
CREATE INDEX idx_gxcl_cl_wh ON geography.geographyxclassification (classificationid, warehousefromdate);

-- Indexes for geography.geographyxclassificationsecuritytoken
CREATE INDEX idx_gxclst_eff_from ON geography.geographyxclassificationsecuritytoken (effectivefromdate);
CREATE INDEX idx_gxclst_eff_to ON geography.geographyxclassificationsecuritytoken (effectivetodate);
CREATE INDEX idx_gxclst_wh_created ON geography.geographyxclassificationsecuritytoken (warehousecreatedtimestamp);
CREATE INDEX idx_gxclst_wh_updated ON geography.geographyxclassificationsecuritytoken (warehouselastupdatedtimestamp);
CREATE INDEX idx_gxclst_ei_wh ON geography.geographyxclassificationsecuritytoken (enterpriseid, warehousefromdate);
CREATE INDEX idx_gxclst_af_wh ON geography.geographyxclassificationsecuritytoken (activeflagid, warehousefromdate);
CREATE INDEX idx_gxclst_sys_wh ON geography.geographyxclassificationsecuritytoken (systemid, warehousefromdate);

-- Indexes for geography.geographyxgeography
CREATE INDEX idx_gxxg_eff_from ON geography.geographyxgeography (effectivefromdate);
CREATE INDEX idx_gxxg_eff_to ON geography.geographyxgeography (effectivetodate);
CREATE INDEX idx_gxxg_wh_created ON geography.geographyxgeography (warehousecreatedtimestamp);
CREATE INDEX idx_gxxg_wh_updated ON geography.geographyxgeography (warehouselastupdatedtimestamp);
CREATE INDEX idx_gxxg_ei_wh ON geography.geographyxgeography (enterpriseid, warehousefromdate);
CREATE INDEX idx_gxxg_af_wh ON geography.geographyxgeography (activeflagid, warehousefromdate);
CREATE INDEX idx_gxxg_sys_wh ON geography.geographyxgeography (systemid, warehousefromdate);
CREATE INDEX idx_gxxg_cl_wh ON geography.geographyxgeography (classificationid, warehousefromdate);

-- Indexes for geography.geographyxgeographysecuritytoken
CREATE INDEX idx_gxxgst_eff_from ON geography.geographyxgeographysecuritytoken (effectivefromdate);
CREATE INDEX idx_gxxgst_eff_to ON geography.geographyxgeographysecuritytoken (effectivetodate);
CREATE INDEX idx_gxxgst_wh_created ON geography.geographyxgeographysecuritytoken (warehousecreatedtimestamp);
CREATE INDEX idx_gxxgst_wh_updated ON geography.geographyxgeographysecuritytoken (warehouselastupdatedtimestamp);
CREATE INDEX idx_gxxgst_ei_wh ON geography.geographyxgeographysecuritytoken (enterpriseid, warehousefromdate);
CREATE INDEX idx_gxxgst_af_wh ON geography.geographyxgeographysecuritytoken (activeflagid, warehousefromdate);
CREATE INDEX idx_gxxgst_sys_wh ON geography.geographyxgeographysecuritytoken (systemid, warehousefromdate);
CREATE INDEX idx_gxxgst_gid_wh ON geography.geographyxgeographysecuritytoken (geographyxgeographyid, warehousefromdate);

-- Indexes for geography.geographyxresourceitem
CREATE INDEX idx_gxri_eff_from ON geography.geographyxresourceitem (effectivefromdate);
CREATE INDEX idx_gxri_eff_to ON geography.geographyxresourceitem (effectivetodate);
CREATE INDEX idx_gxri_wh_created ON geography.geographyxresourceitem (warehousecreatedtimestamp);
CREATE INDEX idx_gxri_wh_updated ON geography.geographyxresourceitem (warehouselastupdatedtimestamp);
CREATE INDEX idx_gxri_ei_wh ON geography.geographyxresourceitem (enterpriseid, warehousefromdate);
CREATE INDEX idx_gxri_af_wh ON geography.geographyxresourceitem (activeflagid, warehousefromdate);
CREATE INDEX idx_gxri_sys_wh ON geography.geographyxresourceitem (systemid, warehousefromdate);
CREATE INDEX idx_gxri_cl_wh ON geography.geographyxresourceitem (classificationid, warehousefromdate);
CREATE INDEX idx_gxri_gid_wh ON geography.geographyxresourceitem (geographyid, warehousefromdate);

-- Indexes for geography.geographyxresourceitemsecuritytoken
CREATE INDEX idx_gxrist_eff_from ON geography.geographyxresourceitemsecuritytoken (effectivefromdate);
CREATE INDEX idx_gxrist_eff_to ON geography.geographyxresourceitemsecuritytoken (effectivetodate);
CREATE INDEX idx_gxrist_wh_created ON geography.geographyxresourceitemsecuritytoken (warehousecreatedtimestamp);
CREATE INDEX idx_gxrist_wh_updated ON geography.geographyxresourceitemsecuritytoken (warehouselastupdatedtimestamp);
CREATE INDEX idx_gxrist_ei_wh ON geography.geographyxresourceitemsecuritytoken (enterpriseid, warehousefromdate);
CREATE INDEX idx_gxrist_af_wh ON geography.geographyxresourceitemsecuritytoken (activeflagid, warehousefromdate);
CREATE INDEX idx_gxrist_sys_wh ON geography.geographyxresourceitemsecuritytoken (systemid, warehousefromdate);
CREATE INDEX idx_gxrist_gid_wh ON geography.geographyxresourceitemsecuritytoken (geographyxresourceitemid, warehousefromdate);
