CREATE SCHEMA address;
CREATE TABLE address.address
(
    addressid                     UUID                        NOT NULL primary key,
    effectivefromdate             timestamp(6) with time zone NOT NULL DEFAULT now(),
    effectivetodate               timestamp(6) with time zone NOT NULL DEFAULT '2999-12-31 23:59:59.999+00',
    warehousecreatedtimestamp     timestamp(6) with time zone NOT NULL DEFAULT now(),
    warehousefromdate             DATE                        NOT NULL DEFAULT current_date,
    warehouselastupdatedtimestamp timestamp(6) with time zone NOT NULL,
    originalsourcesystemuniqueid  UUID                        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    value                         text                        NOT NULL,
    activeflagid                  UUID                        NOT NULL,
    enterpriseid                  UUID                        NOT NULL,
    systemid                      UUID                        NOT NULL,
    originalsourcesystemid        UUID                        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    classificationid              UUID                        NOT NULL
);
CREATE TABLE address.addresssecuritytoken
(
    addresssecuritytokenid        UUID                        NOT NULL primary key,
    effectivefromdate             timestamp(6) with time zone NOT NULL DEFAULT now(),
    effectivetodate               timestamp(6) with time zone NOT NULL DEFAULT '2999-12-31 23:59:59.999+00',
    warehousecreatedtimestamp     timestamp(6) with time zone NOT NULL DEFAULT now(),
    warehousefromdate             DATE                        NOT NULL DEFAULT current_date,

    warehouselastupdatedtimestamp timestamp(6) with time zone NOT NULL,
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
    addressid                     UUID                        NOT NULL
);
CREATE TABLE address.addressxclassification
(
    addressxclassificationid      UUID                        NOT NULL primary key,
    effectivefromdate             timestamp(6) with time zone NOT NULL DEFAULT now(),
    effectivetodate               timestamp(6) with time zone NOT NULL DEFAULT '2999-12-31 23:59:59.999+00',
    warehousecreatedtimestamp     timestamp(6) with time zone NOT NULL DEFAULT now(),
    warehousefromdate             DATE                        NOT NULL DEFAULT current_date,

    warehouselastupdatedtimestamp timestamp(6) with time zone NOT NULL,
    originalsourcesystemuniqueid  UUID                        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    value                         varchar(150)                NOT NULL ,
    activeflagid                  UUID                        NOT NULL,
    enterpriseid                  UUID                        NOT NULL,
    systemid                      UUID                        NOT NULL,
    originalsourcesystemid        UUID                        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    classificationid              UUID                        NOT NULL,
    addressid                     UUID                        NOT NULL
);
CREATE TABLE address.addressxclassificationsecuritytoken
(
    addressxclassificationsecuritytokenid UUID                        NOT NULL primary key,
    effectivefromdate                     timestamp(6) with time zone NOT NULL DEFAULT now(),
    effectivetodate                       timestamp(6) with time zone NOT NULL DEFAULT '2999-12-31 23:59:59.999+00',
    warehousecreatedtimestamp             timestamp(6) with time zone NOT NULL DEFAULT now(),
    warehousefromdate                     DATE                        NOT NULL DEFAULT current_date,

    warehouselastupdatedtimestamp         timestamp(6) with time zone NOT NULL,
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
    addressxclassificationid              UUID                        NOT NULL
);
CREATE TABLE address.addressxgeography
(
    addressxgeographyid           UUID                        NOT NULL primary key,
    effectivefromdate             timestamp(6) with time zone NOT NULL DEFAULT now(),
    effectivetodate               timestamp(6) with time zone NOT NULL DEFAULT '2999-12-31 23:59:59.999+00',
    warehousecreatedtimestamp     timestamp(6) with time zone NOT NULL DEFAULT now(),
    warehousefromdate             DATE                        NOT NULL DEFAULT current_date,

    warehouselastupdatedtimestamp timestamp(6) with time zone NOT NULL,
    originalsourcesystemuniqueid  UUID                        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    value                         varchar(150)                NOT NULL ,
    activeflagid                  UUID                        NOT NULL,
    enterpriseid                  UUID                        NOT NULL,
    systemid                      UUID                        NOT NULL,
    originalsourcesystemid        UUID                        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    classificationid              UUID                        NOT NULL,
    addressid                     UUID                        NOT NULL,
    geographyid                   UUID                        NOT NULL
);
CREATE TABLE address.addressxgeographysecuritytoken
(
    addressxgeographysecuritytokenid UUID                        NOT NULL primary key,
    effectivefromdate                timestamp(6) with time zone NOT NULL DEFAULT now(),
    effectivetodate                  timestamp(6) with time zone NOT NULL DEFAULT '2999-12-31 23:59:59.999+00',
    warehousecreatedtimestamp        timestamp(6) with time zone NOT NULL DEFAULT now(),
    warehousefromdate                DATE                        NOT NULL DEFAULT current_date,

    warehouselastupdatedtimestamp    timestamp(6) with time zone NOT NULL,
    createallowed                    INTEGER                     NOT NULL,
    deleteallowed                    INTEGER                     NOT NULL,
    originalsourcesystemuniqueid     UUID                        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    readallowed                      INTEGER                     NOT NULL,
    updateallowed                    INTEGER                     NOT NULL,
    activeflagid                     UUID                        NOT NULL,
    enterpriseid                     UUID                        NOT NULL,
    originalsourcesystemid           UUID                        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    securitytokenid                  UUID                        NOT NULL,
    systemid                         UUID                        NOT NULL,
    addressxgeographyid              UUID                        NOT NULL
);
CREATE TABLE address.addressxresourceitem
(
    addressxresourceitemid        UUID                        NOT NULL primary key,
    effectivefromdate             timestamp(6) with time zone NOT NULL DEFAULT now(),
    effectivetodate               timestamp(6) with time zone NOT NULL DEFAULT '2999-12-31 23:59:59.999+00',
    warehousecreatedtimestamp     timestamp(6) with time zone NOT NULL DEFAULT now(),
    warehousefromdate             DATE                        NOT NULL DEFAULT current_date,

    warehouselastupdatedtimestamp timestamp(6) with time zone NOT NULL,
    originalsourcesystemuniqueid  UUID                        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    value                         varchar(150)                NOT NULL ,
    activeflagid                  UUID                        NOT NULL,
    enterpriseid                  UUID                        NOT NULL,
    systemid                      UUID                        NOT NULL,
    originalsourcesystemid        UUID                        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    classificationid              UUID                        NOT NULL,
    addressid                     UUID                        NOT NULL,
    resourceitemid                UUID                        NOT NULL
);
CREATE TABLE address.addressxresourceitemsecuritytoken
(
    addressxresourceitemsecuritytokenid UUID                        NOT NULL primary key,
    effectivefromdate                   timestamp(6) with time zone NOT NULL DEFAULT now(),
    effectivetodate                     timestamp(6) with time zone NOT NULL DEFAULT '2999-12-31 23:59:59.999+00',
    warehousecreatedtimestamp           timestamp(6) with time zone NOT NULL DEFAULT now(),
    warehousefromdate                   DATE                        NOT NULL DEFAULT current_date,

    warehouselastupdatedtimestamp       timestamp(6) with time zone NOT NULL,
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
    addressxresourceitemid              UUID                        NOT NULL
);

-- Indexes for address.address
CREATE INDEX idx_addr_eff_from ON address.address (effectivefromdate);
CREATE INDEX idx_addr_eff_to ON address.address (effectivetodate);
CREATE INDEX idx_addr_wh_created ON address.address (warehousecreatedtimestamp);
CREATE INDEX idx_addr_wh_updated ON address.address (warehouselastupdatedtimestamp);
CREATE INDEX idx_addr_val ON address.address (value);
CREATE INDEX idx_addr_ei_wh ON address.address (enterpriseid, warehousefromdate);
CREATE INDEX idx_addr_af_wh ON address.address (activeflagid, warehousefromdate);
CREATE INDEX idx_addr_cl_wh ON address.address (classificationid, warehousefromdate);
CREATE INDEX idx_addr_sys_wh ON address.address (systemid, warehousefromdate);

-- Indexes for address.addresssecuritytoken
CREATE INDEX idx_addrst_eff_from ON address.addresssecuritytoken (effectivefromdate);
CREATE INDEX idx_addrst_eff_to ON address.addresssecuritytoken (effectivetodate);
CREATE INDEX idx_addrst_wh_created ON address.addresssecuritytoken (warehousecreatedtimestamp);
CREATE INDEX idx_addrst_wh_updated ON address.addresssecuritytoken (warehouselastupdatedtimestamp);
CREATE INDEX idx_addrst_ei_wh ON address.addresssecuritytoken (enterpriseid, warehousefromdate);
CREATE INDEX idx_addrst_st_wh ON address.addresssecuritytoken (securitytokenid, warehousefromdate);
CREATE INDEX idx_addrst_af_wh ON address.addresssecuritytoken (activeflagid, warehousefromdate);
CREATE INDEX idx_addrst_sid_wh ON address.addresssecuritytoken (addressid, warehousefromdate);
CREATE INDEX idx_addrst_sys_wh ON address.addresssecuritytoken (systemid, warehousefromdate);

-- Indexes for address.addressxclassification
CREATE INDEX idx_addrxc_eff_from ON address.addressxclassification (effectivefromdate);
CREATE INDEX idx_addrxc_eff_to ON address.addressxclassification (effectivetodate);
CREATE INDEX idx_addrxc_wh_created ON address.addressxclassification (warehousecreatedtimestamp);
CREATE INDEX idx_addrxc_wh_updated ON address.addressxclassification (warehouselastupdatedtimestamp);
CREATE INDEX idx_addrxc_val ON address.addressxclassification (value);
CREATE INDEX idx_addrxc_ei_wh ON address.addressxclassification (enterpriseid, warehousefromdate);
CREATE INDEX idx_addrxc_af_wh ON address.addressxclassification (activeflagid, warehousefromdate);
CREATE INDEX idx_addrxc_sys_wh ON address.addressxclassification (systemid, warehousefromdate);
CREATE INDEX idx_addrxc_cl_wh ON address.addressxclassification (classificationid, warehousefromdate);
CREATE INDEX idx_addrxc_sid_wh ON address.addressxclassification (addressid, warehousefromdate);

-- Indexes for address.addressxgeography
CREATE INDEX idx_addrxg_eff_from ON address.addressxgeography (effectivefromdate);
CREATE INDEX idx_addrxg_eff_to ON address.addressxgeography (effectivetodate);
CREATE INDEX idx_addrxg_wh_created ON address.addressxgeography (warehousecreatedtimestamp);
CREATE INDEX idx_addrxg_wh_updated ON address.addressxgeography (warehouselastupdatedtimestamp);
CREATE INDEX idx_addrxg_val ON address.addressxgeography (value);
CREATE INDEX idx_addrxg_sid_wh ON address.addressxgeography (addressid, warehousefromdate);
CREATE INDEX idx_addrxg_ei_wh ON address.addressxgeography (enterpriseid, warehousefromdate);
CREATE INDEX idx_addrxg_gid_wh ON address.addressxgeography (geographyid, warehousefromdate);
CREATE INDEX idx_addrxg_sys_wh ON address.addressxgeography (systemid, warehousefromdate);
CREATE INDEX idx_addrxg_cl_wh ON address.addressxgeography (classificationid, warehousefromdate);
CREATE INDEX idx_addrxg_af_wh ON address.addressxgeography (activeflagid, warehousefromdate);

-- Indexes for address.addressxgeographysecuritytoken
CREATE INDEX idx_addrxgst_eff_from ON address.addressxgeographysecuritytoken (effectivefromdate);
CREATE INDEX idx_addrxgst_eff_to ON address.addressxgeographysecuritytoken (effectivetodate);
CREATE INDEX idx_addrxgst_wh_created ON address.addressxgeographysecuritytoken (warehousecreatedtimestamp);
CREATE INDEX idx_addrxgst_wh_updated ON address.addressxgeographysecuritytoken (warehouselastupdatedtimestamp);
CREATE INDEX idx_addrxgst_sid_wh ON address.addressxgeographysecuritytoken (addressxgeographyid, warehousefromdate);
CREATE INDEX idx_addrxgst_ei_wh ON address.addressxgeographysecuritytoken (enterpriseid, warehousefromdate);
CREATE INDEX idx_addrxgst_sys_wh ON address.addressxgeographysecuritytoken (systemid, warehousefromdate);
CREATE INDEX idx_addrxgst_st_wh ON address.addressxgeographysecuritytoken (securitytokenid, warehousefromdate);
CREATE INDEX idx_addrxgst_af_wh ON address.addressxgeographysecuritytoken (activeflagid, warehousefromdate);

-- Indexes for address.addressxresourceitem
CREATE INDEX idx_addrxrist_eff_from ON address.addressxresourceitem (effectivefromdate);
CREATE INDEX idx_addrxrist_eff_to ON address.addressxresourceitem (effectivetodate);
CREATE INDEX idx_addrxrist_wh_created ON address.addressxresourceitem (warehousecreatedtimestamp);
CREATE INDEX idx_addrxrist_wh_updated ON address.addressxresourceitem (warehouselastupdatedtimestamp);
CREATE INDEX idx_addrxrist_val ON address.addressxresourceitem (value);
CREATE INDEX idx_addrxrist_ei_wh ON address.addressxresourceitem (enterpriseid, warehousefromdate);
CREATE INDEX idx_addrxrist_sys_wh ON address.addressxresourceitem (systemid, warehousefromdate);
CREATE INDEX idx_addrxrist_af_wh ON address.addressxresourceitem (activeflagid, warehousefromdate);
CREATE INDEX idx_addrxrist_sid_wh ON address.addressxresourceitem (addressid, warehousefromdate);
CREATE INDEX idx_addrxrist_cl_wh ON address.addressxresourceitem (classificationid, warehousefromdate);
CREATE INDEX idx_addrxrist_rid_wh ON address.addressxresourceitem (resourceitemid, warehousefromdate);

-- Indexes for address.addressxresourceitemsecuritytoken
CREATE INDEX idx_addrxristst_eff_from ON address.addressxresourceitemsecuritytoken (effectivefromdate);
CREATE INDEX idx_addrxristst_eff_to ON address.addressxresourceitemsecuritytoken (effectivetodate);
CREATE INDEX idx_addrxristst_wh_created ON address.addressxresourceitemsecuritytoken (warehousecreatedtimestamp);
CREATE INDEX idx_addrxristst_wh_updated ON address.addressxresourceitemsecuritytoken (warehouselastupdatedtimestamp);
CREATE INDEX idx_addrxristst_ei_wh ON address.addressxresourceitemsecuritytoken (enterpriseid, warehousefromdate);
CREATE INDEX idx_addrxristst_sys_wh ON address.addressxresourceitemsecuritytoken (systemid, warehousefromdate);
CREATE INDEX idx_addrxristst_st_wh ON address.addressxresourceitemsecuritytoken (securitytokenid, warehousefromdate);
CREATE INDEX idx_addrxristst_af_wh ON address.addressxresourceitemsecuritytoken (activeflagid, warehousefromdate);
CREATE INDEX idx_addrxristst_rid_wh ON address.addressxresourceitemsecuritytoken (addressxresourceitemid, warehousefromdate);