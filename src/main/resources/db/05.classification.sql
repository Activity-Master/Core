CREATE TABLE classification.classification
(
    classificationid              UUID                        NOT NULL primary key,
    effectivefromdate             timestamp(6) with time zone NOT NULL DEFAULT now(),
    effectivetodate               timestamp(6) with time zone NOT NULL DEFAULT '2999-12-31 23:59:59.999+00',
    warehousecreatedtimestamp     timestamp(6) with time zone NOT NULL DEFAULT now(),
    warehousefromdate             DATE                        NOT NULL DEFAULT current_date,

    warehouselastupdatedtimestamp timestamp(6) with time zone NOT NULL,
    originalsourcesystemuniqueid  UUID                        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    classificationsequencenumber  integer                     NOT NULL,
    classificationdesc            character varying(500)      NOT NULL,
    classificationname            character varying(100)      NOT NULL,
    activeflagid                  UUID                        NOT NULL,
    enterpriseid                  UUID                        NOT NULL,
    systemid                      UUID                        NOT NULL,
    originalsourcesystemid        UUID                        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    classificationdataconceptid   UUID                        NOT NULL
);
CREATE TABLE classification.classificationsecuritytoken
(
    classificationsecuritytokenid UUID                        NOT NULL primary key,
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
    classificationid              UUID                        NOT NULL
);
CREATE TABLE classification.classificationxclassification
(
    classificationxclassificationid UUID                        NOT NULL primary key,
    effectivefromdate               timestamp(6) with time zone NOT NULL DEFAULT now(),
    effectivetodate                 timestamp(6) with time zone NOT NULL DEFAULT '2999-12-31 23:59:59.999+00',
    warehousecreatedtimestamp       timestamp(6) with time zone NOT NULL DEFAULT now(),
    warehousefromdate               DATE                        NOT NULL DEFAULT current_date,

    warehouselastupdatedtimestamp   timestamp(6) with time zone NOT NULL,
    originalsourcesystemuniqueid    UUID                        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    value                           text                        NOT NULL,
    activeflagid                    UUID                        NOT NULL,
    enterpriseid                    UUID                        NOT NULL,
    systemid                        UUID                        NOT NULL,
    originalsourcesystemid          UUID                        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    classificationid                UUID                        NOT NULL,
    childclassificationid           UUID                        NOT NULL,
    parentclassificationid          UUID                        NOT NULL
);
CREATE TABLE classification.classificationxclassificationsecuritytoken
(
    classificationxclassificationsecuritytokenid UUID                        NOT NULL primary key,
    effectivefromdate                            timestamp(6) with time zone NOT NULL DEFAULT now(),
    effectivetodate                              timestamp(6) with time zone NOT NULL DEFAULT '2999-12-31 23:59:59.999+00',
    warehousecreatedtimestamp                    timestamp(6) with time zone NOT NULL DEFAULT now(),
    warehousefromdate                            DATE                        NOT NULL DEFAULT current_date,

    warehouselastupdatedtimestamp                timestamp(6) with time zone NOT NULL,
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
    classificationxclassificationid              UUID                        NOT NULL
);
CREATE TABLE classification.classificationxresourceitem
(
    classificationxresourceitemid UUID                        NOT NULL primary key,
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
    classificationid              UUID                        NOT NULL,
    resourceitemid                UUID                        NOT NULL
);
CREATE TABLE classification.classificationxresourceitemsecuritytoken
(
    classificationxresourceitemsecuritytokenid UUID                        NOT NULL primary key,
    effectivefromdate                          timestamp(6) with time zone NOT NULL DEFAULT now(),
    effectivetodate                            timestamp(6) with time zone NOT NULL DEFAULT '2999-12-31 23:59:59.999+00',
    warehousecreatedtimestamp                  timestamp(6) with time zone NOT NULL DEFAULT now(),
    warehousefromdate                          DATE                        NOT NULL DEFAULT current_date,

    warehouselastupdatedtimestamp              timestamp(6) with time zone NOT NULL,
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
    classificationxresourceitemid              UUID                        NOT NULL
);

-- Indexes for classification.classification
CREATE INDEX idx_cls_eff_from ON classification.classification (effectivefromdate);
CREATE INDEX idx_cls_eff_to ON classification.classification (effectivetodate);
CREATE INDEX idx_cls_wh_created ON classification.classification (warehousecreatedtimestamp);
CREATE INDEX idx_cls_wh_updated ON classification.classification (warehouselastupdatedtimestamp);

-- Indexes for classification.classificationsecuritytoken
CREATE INDEX idx_clsst_eff_from ON classification.classificationsecuritytoken (effectivefromdate);
CREATE INDEX idx_clsst_eff_to ON classification.classificationsecuritytoken (effectivetodate);
CREATE INDEX idx_clsst_wh_created ON classification.classificationsecuritytoken (warehousecreatedtimestamp);
CREATE INDEX idx_clsst_wh_updated ON classification.classificationsecuritytoken (warehouselastupdatedtimestamp);

-- Indexes for classification.classificationxclassification
CREATE INDEX idx_clsxc_eff_from ON classification.classificationxclassification (effectivefromdate);
CREATE INDEX idx_clsxc_eff_to ON classification.classificationxclassification (effectivetodate);
CREATE INDEX idx_clsxc_wh_created ON classification.classificationxclassification (warehousecreatedtimestamp);
CREATE INDEX idx_clsxc_wh_updated ON classification.classificationxclassification (warehouselastupdatedtimestamp);
CREATE INDEX idx_clsxc_val ON classification.classificationxclassification (value);

-- Indexes for classification.classificationxclassificationsecuritytoken
CREATE INDEX idx_clsxcst_eff_from ON classification.classificationxclassificationsecuritytoken (effectivefromdate);
CREATE INDEX idx_clsxcst_eff_to ON classification.classificationxclassificationsecuritytoken (effectivetodate);
CREATE INDEX idx_clsxcst_wh_created ON classification.classificationxclassificationsecuritytoken (warehousecreatedtimestamp);
CREATE INDEX idx_clsxcst_wh_updated ON classification.classificationxclassificationsecuritytoken (warehouselastupdatedtimestamp);

-- Indexes for classification.classificationxresourceitem
CREATE INDEX idx_clsxcrist_eff_from ON classification.classificationxresourceitem (effectivefromdate);
CREATE INDEX idx_clsxcrist_eff_to ON classification.classificationxresourceitem (effectivetodate);
CREATE INDEX idx_clsxcrist_wh_created ON classification.classificationxresourceitem (warehousecreatedtimestamp);
CREATE INDEX idx_clsxcrist_wh_updated ON classification.classificationxresourceitem (warehouselastupdatedtimestamp);
CREATE INDEX idx_clsxcrist_val ON classification.classificationxresourceitem (value);

-- Indexes for classification.classificationxresourceitemsecuritytoken
CREATE INDEX idx_clsxcristst_eff_from ON classification.classificationxresourceitemsecuritytoken (effectivefromdate);
CREATE INDEX idx_clsxcristst_wh_created ON classification.classificationxresourceitemsecuritytoken (warehousecreatedtimestamp);