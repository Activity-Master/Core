CREATE SCHEMA product;
CREATE TABLE product.product
(
    productid                     UUID                        NOT NULL primary key,
    effectivefromdate             timestamp(6) with time zone NOT NULL DEFAULT now(),
    effectivetodate               timestamp(6) with time zone NOT NULL DEFAULT '2999-12-31 23:59:59.999+00',
    warehousecreatedtimestamp     timestamp(6) with time zone NOT NULL DEFAULT now(),
    warehousefromdate             DATE                        NOT NULL DEFAULT current_date,

    warehouselastupdatedtimestamp timestamp(6) with time zone NOT NULL DEFAULT now(),
    originalsourcesystemuniqueid  UUID                        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    productdesc                   character varying(250)      NOT NULL,
    productname                   character varying(150)      NOT NULL,
    productcode                   character varying(50)       NOT NULL,
    activeflagid                  UUID                        NOT NULL,
    enterpriseid                  UUID                        NOT NULL,
    systemid                      UUID                        NOT NULL,
    originalsourcesystemid        UUID                        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000'
);
CREATE TABLE product.productsecuritytoken
(
    productsecuritytokenid        UUID                        NOT NULL primary key,
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
    productid                     UUID                        NOT NULL
);
CREATE TABLE product.producttype
(
    producttypeid                 UUID                        NOT NULL primary key,
    effectivefromdate             timestamp(6) with time zone NOT NULL DEFAULT now(),
    effectivetodate               timestamp(6) with time zone NOT NULL DEFAULT '2999-12-31 23:59:59.999+00',
    warehousecreatedtimestamp     timestamp(6) with time zone NOT NULL DEFAULT now(),
    warehousefromdate             DATE                        NOT NULL DEFAULT current_date,

    warehouselastupdatedtimestamp timestamp(6) with time zone NOT NULL DEFAULT now(),
    originalsourcesystemuniqueid  UUID                        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    producttypedesc               character varying(200)      NOT NULL,
    producttypename               character varying(200)      NOT NULL,
    activeflagid                  UUID                        NOT NULL,
    enterpriseid                  UUID                        NOT NULL,
    systemid                      UUID                        NOT NULL,
    originalsourcesystemid        UUID                        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000'
);
CREATE TABLE product.producttypessecuritytoken
(
    producttypessecuritytokenid   UUID                        NOT NULL primary key,
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
    producttypesid                UUID                        NOT NULL
);
CREATE TABLE product.producttypexclassification
(
    producttypexclassificationid  UUID                        NOT NULL primary key,
    effectivefromdate             timestamp(6) with time zone NOT NULL DEFAULT now(),
    effectivetodate               timestamp(6) with time zone NOT NULL DEFAULT '2999-12-31 23:59:59.999+00',
    warehousecreatedtimestamp     timestamp(6) with time zone NOT NULL DEFAULT now(),
    warehousefromdate             DATE                        NOT NULL DEFAULT current_date,

    warehouselastupdatedtimestamp timestamp(6) with time zone NOT NULL DEFAULT now(),
    originalsourcesystemuniqueid  UUID                        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    value                         varchar(150)                NOT NULL ,
    activeflagid                  UUID                        NOT NULL,
    enterpriseid                  UUID                        NOT NULL,
    systemid                      UUID                        NOT NULL,
    originalsourcesystemid        UUID                        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    classificationid              UUID                        NOT NULL,
    producttypeid                 UUID                        NOT NULL
);
CREATE TABLE product.producttypexclassificationsecuritytoken
(
    producttypexclassificationsecuritytokenid UUID                        NOT NULL primary key,
    effectivefromdate                         timestamp(6) with time zone NOT NULL DEFAULT now(),
    effectivetodate                           timestamp(6) with time zone NOT NULL DEFAULT '2999-12-31 23:59:59.999+00',
    warehousecreatedtimestamp                 timestamp(6) with time zone NOT NULL DEFAULT now(),
    warehousefromdate                         DATE                        NOT NULL DEFAULT current_date,

    warehouselastupdatedtimestamp             timestamp(6) with time zone NOT NULL DEFAULT now(),
    createallowed                             INTEGER                     NOT NULL,
    deleteallowed                             INTEGER                     NOT NULL,
    originalsourcesystemuniqueid              UUID                        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    readallowed                               INTEGER                     NOT NULL,
    updateallowed                             INTEGER                     NOT NULL,
    activeflagid                              UUID                        NOT NULL,
    enterpriseid                              UUID                        NOT NULL,
    originalsourcesystemid                    UUID                        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    securitytokenid                           UUID                        NOT NULL,
    systemid                                  UUID                        NOT NULL,
    producttypexclassificationid              UUID                        NOT NULL
);
CREATE TABLE product.productxclassification
(
    productxclassificationid      UUID                        NOT NULL primary key,
    effectivefromdate             timestamp(6) with time zone NOT NULL DEFAULT now(),
    effectivetodate               timestamp(6) with time zone NOT NULL DEFAULT '2999-12-31 23:59:59.999+00',
    warehousecreatedtimestamp     timestamp(6) with time zone NOT NULL DEFAULT now(),
    warehousefromdate             DATE                        NOT NULL DEFAULT current_date,

    warehouselastupdatedtimestamp timestamp(6) with time zone NOT NULL DEFAULT now(),
    originalsourcesystemuniqueid  UUID                        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    value                         varchar(150)                NOT NULL ,
    activeflagid                  UUID                        NOT NULL,
    enterpriseid                  UUID                        NOT NULL,
    systemid                      UUID                        NOT NULL,
    originalsourcesystemid        UUID                        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    classificationid              UUID                        NOT NULL,
    productid                     UUID                        NOT NULL
);
CREATE TABLE product.productxclassificationsecuritytoken
(
    productxclassificationsecuritytokenid UUID                        NOT NULL primary key,
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
    productxclassificationid              UUID                        NOT NULL
);
CREATE TABLE product.productxproduct
(
    productxproductid             UUID                        NOT NULL primary key,
    effectivefromdate             timestamp(6) with time zone NOT NULL DEFAULT now(),
    effectivetodate               timestamp(6) with time zone NOT NULL DEFAULT '2999-12-31 23:59:59.999+00',
    warehousecreatedtimestamp     timestamp(6) with time zone NOT NULL DEFAULT now(),
    warehousefromdate             DATE                        NOT NULL DEFAULT current_date,

    warehouselastupdatedtimestamp timestamp(6) with time zone NOT NULL DEFAULT now(),
    originalsourcesystemuniqueid  UUID                        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    value                         varchar(150)                NOT NULL ,
    activeflagid                  UUID                        NOT NULL,
    enterpriseid                  UUID                        NOT NULL,
    systemid                      UUID                        NOT NULL,
    originalsourcesystemid        UUID                        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    classificationid              UUID                        NOT NULL,
    childproductid                UUID                        NOT NULL,
    parentproductid               UUID                        NOT NULL
);
CREATE TABLE product.productxproductsecuritytoken
(
    productxproductsecuritytokenid UUID                        NOT NULL primary key,
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
    productxproductid              UUID                        NOT NULL
);
CREATE TABLE product.productxproducttype
(
    productxproducttypeid         UUID                        NOT NULL primary key,
    effectivefromdate             timestamp(6) with time zone NOT NULL DEFAULT now(),
    effectivetodate               timestamp(6) with time zone NOT NULL DEFAULT '2999-12-31 23:59:59.999+00',
    warehousecreatedtimestamp     timestamp(6) with time zone NOT NULL DEFAULT now(),
    warehousefromdate             DATE                        NOT NULL DEFAULT current_date,

    warehouselastupdatedtimestamp timestamp(6) with time zone NOT NULL DEFAULT now(),
    originalsourcesystemuniqueid  UUID                        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    value                         varchar(150)                NOT NULL ,
    activeflagid                  UUID                        NOT NULL,
    enterpriseid                  UUID                        NOT NULL,
    systemid                      UUID                        NOT NULL,
    originalsourcesystemid        UUID                        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    classificationid              UUID                        NOT NULL,
    productid                     UUID                        NOT NULL,
    producttypeid                 UUID                        NOT NULL
);
CREATE TABLE product.productxproducttypesecuritytoken
(
    productxproducttypesecuritytokenid UUID                        NOT NULL primary key,
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
    productxproducttypeid              UUID                        NOT NULL
);
CREATE TABLE product.productxresourceitem
(
    productxresourceitemid        UUID                        NOT NULL primary key,
    effectivefromdate             timestamp(6) with time zone NOT NULL DEFAULT now(),
    effectivetodate               timestamp(6) with time zone NOT NULL DEFAULT '2999-12-31 23:59:59.999+00',
    warehousecreatedtimestamp     timestamp(6) with time zone NOT NULL DEFAULT now(),
    warehousefromdate             DATE                        NOT NULL DEFAULT current_date,

    warehouselastupdatedtimestamp timestamp(6) with time zone NOT NULL DEFAULT now(),
    originalsourcesystemuniqueid  UUID                        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    value                         varchar(150)                NOT NULL ,
    activeflagid                  UUID                        NOT NULL,
    enterpriseid                  UUID                        NOT NULL,
    systemid                      UUID                        NOT NULL,
    originalsourcesystemid        UUID                        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    classificationid              UUID                        NOT NULL,
    productid                     UUID                        NOT NULL,
    resourceitemid                UUID                        NOT NULL
);
CREATE TABLE product.productxresourceitemsecuritytoken
(
    productxresourceitemsecuritytokenid UUID                        NOT NULL primary key,
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
    productxresourceitemid              UUID                        NOT NULL
);

-- Indexes for product.product
CREATE INDEX idx_pr_eff_from ON product.product (effectivefromdate);
CREATE INDEX idx_pr_eff_to ON product.product (effectivetodate);
CREATE INDEX idx_pr_wh_created ON product.product (warehousecreatedtimestamp);
CREATE INDEX idx_pr_wh_updated ON product.product (warehouselastupdatedtimestamp);
CREATE INDEX idx_pr_ei_wh ON product.product (enterpriseid, warehousefromdate);
CREATE INDEX idx_pr_af_wh ON product.product (activeflagid, warehousefromdate);
CREATE INDEX idx_pr_sys_wh ON product.product (systemid, warehousefromdate);

-- Indexes for product.productsecuritytoken
CREATE INDEX idx_prst_eff_from ON product.productsecuritytoken (effectivefromdate);
CREATE INDEX idx_prst_eff_to ON product.productsecuritytoken (effectivetodate);
CREATE INDEX idx_prst_wh_created ON product.productsecuritytoken (warehousecreatedtimestamp);
CREATE INDEX idx_prst_wh_updated ON product.productsecuritytoken (warehouselastupdatedtimestamp);
CREATE INDEX idx_prst_ei_wh ON product.productsecuritytoken (enterpriseid, warehousefromdate);
CREATE INDEX idx_prst_af_wh ON product.productsecuritytoken (activeflagid, warehousefromdate);
CREATE INDEX idx_prst_sid_wh ON product.productsecuritytoken (productid, warehousefromdate);
CREATE INDEX idx_prst_st_wh ON product.productsecuritytoken (securitytokenid, warehousefromdate);
CREATE INDEX idx_prst_sys_wh ON product.productsecuritytoken (systemid, warehousefromdate);

-- Indexes for product.producttype
CREATE INDEX idx_prt_eff_from ON product.producttype (effectivefromdate);
CREATE INDEX idx_prt_eff_to ON product.producttype (effectivetodate);
CREATE INDEX idx_prt_wh_created ON product.producttype (warehousecreatedtimestamp);
CREATE INDEX idx_prt_wh_updated ON product.producttype (warehouselastupdatedtimestamp);
CREATE INDEX idx_prt_ei_wh ON product.producttype (enterpriseid, warehousefromdate);
CREATE INDEX idx_prt_af_wh ON product.producttype (activeflagid, warehousefromdate);
CREATE INDEX idx_prt_sys_wh ON product.producttype (systemid, warehousefromdate);

-- Indexes for product.producttypessecuritytoken
CREATE INDEX idx_prtst_eff_from ON product.producttypessecuritytoken (effectivefromdate);
CREATE INDEX idx_prtst_eff_to ON product.producttypessecuritytoken (effectivetodate);
CREATE INDEX idx_prtst_wh_created ON product.producttypessecuritytoken (warehousecreatedtimestamp);
CREATE INDEX idx_prtst_wh_updated ON product.producttypessecuritytoken (warehouselastupdatedtimestamp);
CREATE INDEX idx_prtst_ei_wh ON product.producttypessecuritytoken (enterpriseid, warehousefromdate);
CREATE INDEX idx_prtst_st_wh ON product.producttypessecuritytoken (securitytokenid, warehousefromdate);
CREATE INDEX idx_prtst_af_wh ON product.producttypessecuritytoken (activeflagid, warehousefromdate);
CREATE INDEX idx_prtst_sid_wh ON product.producttypessecuritytoken (producttypesid, warehousefromdate);
CREATE INDEX idx_prtst_sys_wh ON product.producttypessecuritytoken (systemid, warehousefromdate);

-- Indexes for product.producttypexclassification
CREATE INDEX idx_prtxc_eff_from ON product.producttypexclassification (effectivefromdate);
CREATE INDEX idx_prtxc_eff_to ON product.producttypexclassification (effectivetodate);
CREATE INDEX idx_prtxc_wh_created ON product.producttypexclassification (warehousecreatedtimestamp);
CREATE INDEX idx_prtxc_wh_updated ON product.producttypexclassification (warehouselastupdatedtimestamp);
CREATE INDEX idx_prtxc_val ON product.producttypexclassification (value);
CREATE INDEX idx_prtxc_ei_wh ON product.producttypexclassification (enterpriseid, warehousefromdate);
CREATE INDEX idx_prtxc_af_wh ON product.producttypexclassification (activeflagid, warehousefromdate);
CREATE INDEX idx_prtxc_sys_wh ON product.producttypexclassification (systemid, warehousefromdate);
CREATE INDEX idx_prtxc_tid_wh ON product.producttypexclassification (producttypeid, warehousefromdate);

-- Indexes for product.producttypexclassificationsecuritytoken
CREATE INDEX idx_prtxcst_eff_from ON product.producttypexclassificationsecuritytoken (effectivefromdate);
CREATE INDEX idx_prtxcst_eff_to ON product.producttypexclassificationsecuritytoken (effectivetodate);
CREATE INDEX idx_prtxcst_wh_created ON product.producttypexclassificationsecuritytoken (warehousecreatedtimestamp);
CREATE INDEX idx_prtxcst_wh_updated ON product.producttypexclassificationsecuritytoken (warehouselastupdatedtimestamp);
CREATE INDEX idx_prtxcst_ei_wh ON product.producttypexclassificationsecuritytoken (enterpriseid, warehousefromdate);
CREATE INDEX idx_prtxcst_st_wh ON product.producttypexclassificationsecuritytoken (securitytokenid, warehousefromdate);
CREATE INDEX idx_prtxcst_sys_wh ON product.producttypexclassificationsecuritytoken (systemid, warehousefromdate);
CREATE INDEX idx_prtxcst_tid_wh ON product.producttypexclassificationsecuritytoken (producttypexclassificationid, warehousefromdate);
CREATE INDEX idx_prtxcst_af_wh ON product.producttypexclassificationsecuritytoken (activeflagid, warehousefromdate);

-- Indexes for product.productxclassification
CREATE INDEX idx_prxc_eff_from ON product.productxclassification (effectivefromdate);
CREATE INDEX idx_prxc_eff_to ON product.productxclassification (effectivetodate);
CREATE INDEX idx_prxc_wh_created ON product.productxclassification (warehousecreatedtimestamp);
CREATE INDEX idx_prxc_wh_updated ON product.productxclassification (warehouselastupdatedtimestamp);
CREATE INDEX idx_prxc_val ON product.productxclassification (value);
CREATE INDEX idx_prxc_ei_wh ON product.productxclassification (enterpriseid, warehousefromdate);
CREATE INDEX idx_prxc_af_wh ON product.productxclassification (activeflagid, warehousefromdate);
CREATE INDEX idx_prxc_sys_wh ON product.productxclassification (systemid, warehousefromdate);
CREATE INDEX idx_prxc_sid_wh ON product.productxclassification (productid, warehousefromdate);

-- Indexes for product.productxclassificationsecuritytoken
CREATE INDEX idx_prxcst_eff_from ON product.productxclassificationsecuritytoken (effectivefromdate);
CREATE INDEX idx_prxcst_eff_to ON product.productxclassificationsecuritytoken (effectivetodate);
CREATE INDEX idx_prxcst_wh_created ON product.productxclassificationsecuritytoken (warehousecreatedtimestamp);
CREATE INDEX idx_prxcst_wh_updated ON product.productxclassificationsecuritytoken (warehouselastupdatedtimestamp);
CREATE INDEX idx_prxcst_ei_wh ON product.productxclassificationsecuritytoken (enterpriseid, warehousefromdate);
CREATE INDEX idx_prxcst_st_wh ON product.productxclassificationsecuritytoken (securitytokenid, warehousefromdate);
CREATE INDEX idx_prxcst_sys_wh ON product.productxclassificationsecuritytoken (systemid, warehousefromdate);
CREATE INDEX idx_prxcst_af_wh ON product.productxclassificationsecuritytoken (activeflagid, warehousefromdate);

-- Indexes for product.productxproduct
CREATE INDEX idx_prxp_eff_from ON product.productxproduct (effectivefromdate);
CREATE INDEX idx_prxp_eff_to ON product.productxproduct (effectivetodate);
CREATE INDEX idx_prxp_wh_created ON product.productxproduct (warehousecreatedtimestamp);
CREATE INDEX idx_prxp_wh_updated ON product.productxproduct (warehouselastupdatedtimestamp);
CREATE INDEX idx_prxp_val ON product.productxproduct (value);
CREATE INDEX idx_prxp_ei_wh ON product.productxproduct (enterpriseid, warehousefromdate);
CREATE INDEX idx_prxp_af_wh ON product.productxproduct (activeflagid, warehousefromdate);
CREATE INDEX idx_prxp_sys_wh ON product.productxproduct (systemid, warehousefromdate);
CREATE INDEX idx_prxp_cl_wh ON product.productxproduct (classificationid, warehousefromdate);

-- Indexes for product.productxproductsecuritytoken
CREATE INDEX idx_prxpst_eff_from ON product.productxproductsecuritytoken (effectivefromdate);
CREATE INDEX idx_prxpst_eff_to ON product.productxproductsecuritytoken (effectivetodate);
CREATE INDEX idx_prxpst_wh_created ON product.productxproductsecuritytoken (warehousecreatedtimestamp);
CREATE INDEX idx_prxpst_wh_updated ON product.productxproductsecuritytoken (warehouselastupdatedtimestamp);
CREATE INDEX idx_prxpst_ei_wh ON product.productxproductsecuritytoken (enterpriseid, warehousefromdate);
CREATE INDEX idx_prxpst_st_wh ON product.productxproductsecuritytoken (securitytokenid, warehousefromdate);
CREATE INDEX idx_prxpst_sys_wh ON product.productxproductsecuritytoken (systemid, warehousefromdate);
CREATE INDEX idx_prxpst_af_wh ON product.productxproductsecuritytoken (activeflagid, warehousefromdate);

-- Indexes for product.productxproducttype
CREATE INDEX idx_prxpt_eff_from ON product.productxproducttype (effectivefromdate);
CREATE INDEX idx_prxpt_eff_to ON product.productxproducttype (effectivetodate);
CREATE INDEX idx_prxpt_wh_created ON product.productxproducttype (warehousecreatedtimestamp);
CREATE INDEX idx_prxpt_wh_updated ON product.productxproducttype (warehouselastupdatedtimestamp);
CREATE INDEX idx_prxpt_val ON product.productxproducttype (value);
CREATE INDEX idx_prxpt_ei_wh ON product.productxproducttype (enterpriseid, warehousefromdate);
CREATE INDEX idx_prxpt_af_wh ON product.productxproducttype (activeflagid, warehousefromdate);
CREATE INDEX idx_prxpt_sys_wh ON product.productxproducttype (systemid, warehousefromdate);
CREATE INDEX idx_prxpt_cl_wh ON product.productxproducttype (classificationid, warehousefromdate);
CREATE INDEX idx_prxpt_rid_wh ON product.productxproducttype (producttypeid, warehousefromdate);

-- Indexes for product.productxproducttypesecuritytoken
CREATE INDEX idx_prxptst_eff_from ON product.productxproducttypesecuritytoken (effectivefromdate);
CREATE INDEX idx_prxptst_eff_to ON product.productxproducttypesecuritytoken (effectivetodate);
CREATE INDEX idx_prxptst_wh_created ON product.productxproducttypesecuritytoken (warehousecreatedtimestamp);
CREATE INDEX idx_prxptst_wh_updated ON product.productxproducttypesecuritytoken (warehouselastupdatedtimestamp);
CREATE INDEX idx_prxptst_ei_wh ON product.productxproducttypesecuritytoken (enterpriseid, warehousefromdate);
CREATE INDEX idx_prxptst_st_wh ON product.productxproducttypesecuritytoken (securitytokenid, warehousefromdate);
CREATE INDEX idx_prxptst_sys_wh ON product.productxproducttypesecuritytoken (systemid, warehousefromdate);
CREATE INDEX idx_prxptst_af_wh ON product.productxproducttypesecuritytoken (activeflagid, warehousefromdate);

-- Indexes for product.productxresourceitem
CREATE INDEX idx_prxrist_eff_from ON product.productxresourceitem (effectivefromdate);
CREATE INDEX idx_prxrist_eff_to ON product.productxresourceitem (effectivetodate);
CREATE INDEX idx_prxrist_wh_created ON product.productxresourceitem (warehousecreatedtimestamp);
CREATE INDEX idx_prxrist_wh_updated ON product.productxresourceitem (warehouselastupdatedtimestamp);
CREATE INDEX idx_prxrist_val ON product.productxresourceitem (value);
CREATE INDEX idx_prxrist_ei_wh ON product.productxresourceitem (enterpriseid, warehousefromdate);
CREATE INDEX idx_prxrist_sys_wh ON product.productxresourceitem (systemid, warehousefromdate);
CREATE INDEX idx_prxrist_af_wh ON product.productxresourceitem (activeflagid, warehousefromdate);
CREATE INDEX idx_prxrist_cl_wh ON product.productxresourceitem (classificationid, warehousefromdate);
CREATE INDEX idx_prxrist_rid_wh ON product.productxresourceitem (resourceitemid, warehousefromdate);

-- Indexes for product.productxresourceitemsecuritytoken
CREATE INDEX idx_prxristst_eff_from ON product.productxresourceitemsecuritytoken (effectivefromdate);
CREATE INDEX idx_prxristst_eff_to ON product.productxresourceitemsecuritytoken (effectivetodate);
CREATE INDEX idx_prxristst_wh_created ON product.productxresourceitemsecuritytoken (warehousecreatedtimestamp);
CREATE INDEX idx_prxristst_wh_updated ON product.productxresourceitemsecuritytoken (warehouselastupdatedtimestamp);
CREATE INDEX idx_prxristst_ei_wh ON product.productxresourceitemsecuritytoken (enterpriseid, warehousefromdate);
CREATE INDEX idx_prxristst_st_wh ON product.productxresourceitemsecuritytoken (securitytokenid, warehousefromdate);
CREATE INDEX idx_prxristst_sys_wh ON product.productxresourceitemsecuritytoken (systemid, warehousefromdate);
CREATE INDEX idx_prxristst_af_wh ON product.productxresourceitemsecuritytoken (activeflagid, warehousefromdate);
CREATE INDEX idx_prxristst_rid_wh ON product.productxresourceitemsecuritytoken (productxresourceitemid, warehousefromdate);