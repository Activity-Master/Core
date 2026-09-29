CREATE SCHEMA arrangement;
CREATE TABLE arrangement.arrangement
(
    arrangementid                 UUID                        NOT NULL primary key,
    effectivefromdate             timestamp(6) with time zone NOT NULL,
    effectivetodate               timestamp(6) with time zone NOT NULL,
    warehousecreatedtimestamp     timestamp(6) with time zone NOT NULL,
    warehousefromdate             DATE                        NOT NULL,

    warehouselastupdatedtimestamp timestamp(6) with time zone NOT NULL,
    originalsourcesystemuniqueid  UUID                        NOT NULL,
    activeflagid                  UUID                        NOT NULL,
    enterpriseid                  UUID                        NOT NULL,
    systemid                      UUID                        NOT NULL,
    originalsourcesystemid        UUID                        NOT NULL
);
CREATE TABLE arrangement.arrangementsecuritytoken
(
    arrangementsecuritytokenid    UUID                        NOT NULL primary key,
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
    arrangementid                 UUID                        NOT NULL
);
CREATE TABLE arrangement.arrangementtype
(
    arrangementtypeid             UUID                        NOT NULL primary key,
    effectivefromdate             timestamp(6) with time zone NOT NULL,
    effectivetodate               timestamp(6) with time zone NOT NULL,
    warehousecreatedtimestamp     timestamp(6) with time zone NOT NULL,
    warehousefromdate             DATE                        NOT NULL,

    warehouselastupdatedtimestamp timestamp(6) with time zone NOT NULL,
    originalsourcesystemuniqueid  UUID                        NOT NULL,
    arrangementtypedescription    character varying(500)      NOT NULL,
    arrangementtypename           character varying(150)      NOT NULL,
    activeflagid                  UUID                        NOT NULL,
    enterpriseid                  UUID                        NOT NULL,
    systemid                      UUID                        NOT NULL,
    originalsourcesystemid        UUID                        NOT NULL
);
CREATE TABLE arrangement.arrangementtypesecuritytoken
(
    arrangementtypesecuritytokenid UUID                        NOT NULL primary key,
    effectivefromdate              timestamp(6) with time zone NOT NULL,
    effectivetodate                timestamp(6) with time zone NOT NULL,
    warehousecreatedtimestamp      timestamp(6) with time zone NOT NULL,
    warehousefromdate              DATE                        NOT NULL,

    warehouselastupdatedtimestamp  timestamp(6) with time zone NOT NULL,
    createallowed                  INTEGER                     NOT NULL,
    deleteallowed                  INTEGER                     NOT NULL,
    originalsourcesystemuniqueid   UUID                        NOT NULL,
    readallowed                    INTEGER                     NOT NULL,
    updateallowed                  INTEGER                     NOT NULL,
    activeflagid                   UUID                        NOT NULL,
    enterpriseid                   UUID                        NOT NULL,
    originalsourcesystemid         UUID                        NOT NULL,
    securitytokenid                UUID                        NOT NULL,
    systemid                       UUID                        NOT NULL,
    arrangementtypeid              UUID                        NOT NULL
);
CREATE TABLE arrangement.arrangementtypexclassification
(
    arrangementtypexclassificationid UUID                        NOT NULL primary key,
    effectivefromdate                timestamp(6) with time zone NOT NULL,
    effectivetodate                  timestamp(6) with time zone NOT NULL,
    warehousecreatedtimestamp        timestamp(6) with time zone NOT NULL,
    warehousefromdate                DATE                        NOT NULL,

    warehouselastupdatedtimestamp    timestamp(6) with time zone NOT NULL,
    originalsourcesystemuniqueid     UUID                        NOT NULL,
    value                            text                        NOT NULL,
    activeflagid                     UUID                        NOT NULL,
    enterpriseid                     UUID                        NOT NULL,
    systemid                         UUID                        NOT NULL,
    originalsourcesystemid           UUID                        NOT NULL,
    classificationid                 UUID                        NOT NULL,
    arrangementtypeid                UUID                        NOT NULL
);
CREATE TABLE arrangement.arrangementtypexclassificationsecuritytoken
(
    arrangementtypexclassificationsecuritytokenid UUID                        NOT NULL primary key,
    effectivefromdate                             timestamp(6) with time zone NOT NULL,
    effectivetodate                               timestamp(6) with time zone NOT NULL,
    warehousecreatedtimestamp                     timestamp(6) with time zone NOT NULL,
    warehousefromdate                             DATE                        NOT NULL,

    warehouselastupdatedtimestamp                 timestamp(6) with time zone NOT NULL,
    createallowed                                 INTEGER                     NOT NULL,
    deleteallowed                                 INTEGER                     NOT NULL,
    originalsourcesystemuniqueid                  UUID                        NOT NULL,
    readallowed                                   INTEGER                     NOT NULL,
    updateallowed                                 INTEGER                     NOT NULL,
    activeflagid                                  UUID                        NOT NULL,
    enterpriseid                                  UUID                        NOT NULL,
    originalsourcesystemid                        UUID                        NOT NULL,
    securitytokenid                               UUID                        NOT NULL,
    systemid                                      UUID                        NOT NULL,
    arrangementtypexclassificationid              UUID                        NOT NULL
);
CREATE TABLE arrangement.arrangementxarrangement
(
    arrangementxarrangementid     UUID                        NOT NULL primary key,
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
    classificationid              UUID                        NOT NULL,
    childarrangementid            UUID                        NOT NULL,
    parentarrangementid           UUID                        NOT NULL
);
CREATE TABLE arrangement.arrangementxarrangementsecuritytoken
(
    arrangementxarrangementsecuritytokenid UUID                        NOT NULL primary key,
    effectivefromdate                      timestamp(6) with time zone NOT NULL,
    effectivetodate                        timestamp(6) with time zone NOT NULL,
    warehousecreatedtimestamp              timestamp(6) with time zone NOT NULL,
    warehousefromdate                      DATE                        NOT NULL,

    warehouselastupdatedtimestamp          timestamp(6) with time zone NOT NULL,
    createallowed                          INTEGER                     NOT NULL,
    deleteallowed                          INTEGER                     NOT NULL,
    originalsourcesystemuniqueid           UUID                        NOT NULL,
    readallowed                            INTEGER                     NOT NULL,
    updateallowed                          INTEGER                     NOT NULL,
    activeflagid                           UUID                        NOT NULL,
    enterpriseid                           UUID                        NOT NULL,
    originalsourcesystemid                 UUID                        NOT NULL,
    securitytokenid                        UUID                        NOT NULL,
    systemid                               UUID                        NOT NULL,
    arrangementxarrangementid              UUID                        NOT NULL
);
CREATE TABLE arrangement.arrangementxarrangementtype
(
    arrangementxarrangementtypeid UUID                        NOT NULL primary key,
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
    classificationid              UUID                        NOT NULL,
    arrangementid                 UUID                        NOT NULL,
    arrangementtypeid             UUID                        NOT NULL
);
CREATE TABLE arrangement.arrangementxarrangementtypesecuritytoken
(
    arrangementxarrangementtypesecuritytokenid UUID                        NOT NULL primary key,
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
    arrangementxarrangementtypeid              UUID                        NOT NULL
);
CREATE TABLE arrangement.arrangementxclassification
(
    arrangementxclassificationid  UUID                        NOT NULL primary key,
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
    classificationid              UUID                        NOT NULL,
    arrangementid                 UUID                        NOT NULL
);

CREATE TABLE arrangement.arrangementxclassificationsecuritytoken
(
    arrangementxclassificationsecuritytokenid UUID                        NOT NULL primary key,
    effectivefromdate                         timestamp(6) with time zone NOT NULL,
    effectivetodate                           timestamp(6) with time zone NOT NULL,
    warehousecreatedtimestamp                 timestamp(6) with time zone NOT NULL,
    warehousefromdate                         DATE                        NOT NULL,

    warehouselastupdatedtimestamp             timestamp(6) with time zone NOT NULL,
    createallowed                             INTEGER                     NOT NULL,
    deleteallowed                             INTEGER                     NOT NULL,
    originalsourcesystemuniqueid              UUID                        NOT NULL,
    readallowed                               INTEGER                     NOT NULL,
    updateallowed                             INTEGER                     NOT NULL,
    activeflagid                              UUID                        NOT NULL,
    enterpriseid                              UUID                        NOT NULL,
    originalsourcesystemid                    UUID                        NOT NULL,
    securitytokenid                           UUID                        NOT NULL,
    systemid                                  UUID                        NOT NULL,
    arrangementxclassificationid              UUID                        NOT NULL
);
CREATE TABLE arrangement.arrangementxinvolvedparty
(
    arrangementxinvolvedpartyid   UUID                        NOT NULL primary key,
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
    classificationid              UUID                        NOT NULL,
    arrangementid                 UUID                        NOT NULL,
    involvedpartyid               UUID                        NOT NULL
);

CREATE TABLE arrangement.arrangementxinvolvedpartysecuritytoken
(
    arrangementxinvolvedpartysecuritytokenid UUID                        NOT NULL primary key,
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
    arrangementxinvolvedpartyid              UUID                        NOT NULL
);
CREATE TABLE arrangement.arrangementxproduct
(
    arrangementxproductid         UUID                        NOT NULL primary key,
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
    classificationid              UUID                        NOT NULL,
    arrangementid                 UUID                        NOT NULL,
    productid                     UUID                        NOT NULL
);
CREATE TABLE arrangement.arrangementxproductsecuritytoken
(
    arrangementxproductsecuritytokenid UUID                        NOT NULL primary key,
    effectivefromdate                  timestamp(6) with time zone NOT NULL,
    effectivetodate                    timestamp(6) with time zone NOT NULL,
    warehousecreatedtimestamp          timestamp(6) with time zone NOT NULL,
    warehousefromdate                  DATE                        NOT NULL,

    warehouselastupdatedtimestamp      timestamp(6) with time zone NOT NULL,
    createallowed                      INTEGER                     NOT NULL,
    deleteallowed                      INTEGER                     NOT NULL,
    originalsourcesystemuniqueid       UUID                        NOT NULL,
    readallowed                        INTEGER                     NOT NULL,
    updateallowed                      INTEGER                     NOT NULL,
    activeflagid                       UUID                        NOT NULL,
    enterpriseid                       UUID                        NOT NULL,
    originalsourcesystemid             UUID                        NOT NULL,
    securitytokenid                    UUID                        NOT NULL,
    systemid                           UUID                        NOT NULL,
    arrangementxproductid              UUID                        NOT NULL
);
CREATE TABLE arrangement.arrangementxresourceitem
(
    arrangementxresourceitemid    UUID                        NOT NULL primary key,
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
    classificationid              UUID                        NOT NULL,
    arrangementid                 UUID                        NOT NULL,
    resourceitemid                UUID                        NOT NULL
);
CREATE TABLE arrangement.arrangementxresourceitemsecuritytoken
(
    arrangementxresourceitemsecuritytokenid UUID                        NOT NULL primary key,
    effectivefromdate                       timestamp(6) with time zone NOT NULL,
    effectivetodate                         timestamp(6) with time zone NOT NULL,
    warehousecreatedtimestamp               timestamp(6) with time zone NOT NULL,
    warehousefromdate                       DATE                        NOT NULL,

    warehouselastupdatedtimestamp           timestamp(6) with time zone NOT NULL,
    createallowed                           INTEGER                     NOT NULL,
    deleteallowed                           INTEGER                     NOT NULL,
    originalsourcesystemuniqueid            UUID                        NOT NULL,
    readallowed                             INTEGER                     NOT NULL,
    updateallowed                           INTEGER                     NOT NULL,
    activeflagid                            UUID                        NOT NULL,
    enterpriseid                            UUID                        NOT NULL,
    originalsourcesystemid                  UUID                        NOT NULL,
    securitytokenid                         UUID                        NOT NULL,
    systemid                                UUID                        NOT NULL,
    arrangementxresourceitemid              UUID                        NOT NULL
);
CREATE TABLE arrangement.arrangementxrules
(
    arrangementxrulesid           UUID                        NOT NULL primary key,
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
    classificationid              UUID                        NOT NULL,
    arrangementid                 UUID                        NOT NULL,
    rulesid                       UUID                        NOT NULL
);
CREATE TABLE arrangement.arrangementxrulessecuritytoken
(
    arrangementxrulessecuritytokenid UUID                        NOT NULL primary key,
    effectivefromdate                timestamp(6) with time zone NOT NULL,
    effectivetodate                  timestamp(6) with time zone NOT NULL,
    warehousecreatedtimestamp        timestamp(6) with time zone NOT NULL,
    warehousefromdate                DATE                        NOT NULL,

    warehouselastupdatedtimestamp    timestamp(6) with time zone NOT NULL,
    createallowed                    INTEGER                     NOT NULL,
    deleteallowed                    INTEGER                     NOT NULL,
    originalsourcesystemuniqueid     UUID                        NOT NULL,
    readallowed                      INTEGER                     NOT NULL,
    updateallowed                    INTEGER                     NOT NULL,
    activeflagid                     UUID                        NOT NULL,
    enterpriseid                     UUID                        NOT NULL,
    originalsourcesystemid           UUID                        NOT NULL,
    securitytokenid                  UUID                        NOT NULL,
    systemid                         UUID                        NOT NULL,
    arrangementxrulesid              UUID                        NOT NULL
);
CREATE TABLE arrangement.arrangementxrulestype
(
    arrangementxrulestypeid       UUID                        NOT NULL primary key,
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
    classificationid              UUID                        NOT NULL,
    arrangementid                 UUID                        NOT NULL,
    rulestypeid                   UUID                        NOT NULL
);
CREATE TABLE arrangement.arrangementxrulestypesecuritytoken
(
    arrangementxrulestypesecuritytokenid UUID                        NOT NULL primary key,
    effectivefromdate                    timestamp(6) with time zone NOT NULL,
    effectivetodate                      timestamp(6) with time zone NOT NULL,
    warehousecreatedtimestamp            timestamp(6) with time zone NOT NULL,
    warehousefromdate                    DATE                        NOT NULL,

    warehouselastupdatedtimestamp        timestamp(6) with time zone NOT NULL,
    createallowed                        INTEGER                     NOT NULL,
    deleteallowed                        INTEGER                     NOT NULL,
    originalsourcesystemuniqueid         UUID                        NOT NULL,
    readallowed                          INTEGER                     NOT NULL,
    updateallowed                        INTEGER                     NOT NULL,
    activeflagid                         UUID                        NOT NULL,
    enterpriseid                         UUID                        NOT NULL,
    originalsourcesystemid               UUID                        NOT NULL,
    securitytokenid                      UUID                        NOT NULL,
    systemid                             UUID                        NOT NULL,
    arrangementxrulestypeid              UUID                        NOT NULL
);

-- Indexes for arrangement.arrangement
CREATE INDEX idx_ar_eff_from ON arrangement.arrangement (effectivefromdate);
CREATE INDEX idx_ar_eff_to ON arrangement.arrangement (effectivetodate);
CREATE INDEX idx_ar_wh_created ON arrangement.arrangement (warehousecreatedtimestamp);
CREATE INDEX idx_ar_wh_updated ON arrangement.arrangement (warehouselastupdatedtimestamp);
CREATE INDEX idx_ar_ei_wh ON arrangement.arrangement (enterpriseid, warehousefromdate);
CREATE INDEX idx_ar_af_wh ON arrangement.arrangement (activeflagid, warehousefromdate);
CREATE INDEX idx_ar_sys_wh ON arrangement.arrangement (systemid, warehousefromdate);

-- Indexes for arrangement.arrangementsecuritytoken
CREATE INDEX idx_arst_eff_from ON arrangement.arrangementsecuritytoken (effectivefromdate);
CREATE INDEX idx_arst_eff_to ON arrangement.arrangementsecuritytoken (effectivetodate);
CREATE INDEX idx_arst_wh_created ON arrangement.arrangementsecuritytoken (warehousecreatedtimestamp);
CREATE INDEX idx_arst_wh_updated ON arrangement.arrangementsecuritytoken (warehouselastupdatedtimestamp);
CREATE INDEX idx_arst_ei_wh ON arrangement.arrangementsecuritytoken (enterpriseid, warehousefromdate);
CREATE INDEX idx_arst_st_wh ON arrangement.arrangementsecuritytoken (securitytokenid, warehousefromdate);
CREATE INDEX idx_arst_sid_wh ON arrangement.arrangementsecuritytoken (arrangementid, warehousefromdate);
CREATE INDEX idx_arst_af_wh ON arrangement.arrangementsecuritytoken (activeflagid, warehousefromdate);
CREATE INDEX idx_arst_sys_wh ON arrangement.arrangementsecuritytoken (systemid, warehousefromdate);

-- Indexes for arrangement.arrangementtype
CREATE INDEX idx_art_eff_from ON arrangement.arrangementtype (effectivefromdate);
CREATE INDEX idx_art_eff_to ON arrangement.arrangementtype (effectivetodate);
CREATE INDEX idx_art_wh_created ON arrangement.arrangementtype (warehousecreatedtimestamp);
CREATE INDEX idx_art_wh_updated ON arrangement.arrangementtype (warehouselastupdatedtimestamp);
CREATE INDEX idx_art_ei_wh ON arrangement.arrangementtype (enterpriseid, warehousefromdate);
CREATE INDEX idx_art_af_wh ON arrangement.arrangementtype (activeflagid, warehousefromdate);
CREATE INDEX idx_art_sys_wh ON arrangement.arrangementtype (systemid, warehousefromdate);

-- Indexes for arrangement.arrangementtypesecuritytoken
CREATE INDEX idx_artst_eff_from ON arrangement.arrangementtypesecuritytoken (effectivefromdate);
CREATE INDEX idx_artst_eff_to ON arrangement.arrangementtypesecuritytoken (effectivetodate);
CREATE INDEX idx_artst_wh_created ON arrangement.arrangementtypesecuritytoken (warehousecreatedtimestamp);
CREATE INDEX idx_artst_wh_updated ON arrangement.arrangementtypesecuritytoken (warehouselastupdatedtimestamp);
CREATE INDEX idx_artst_ei_wh ON arrangement.arrangementtypesecuritytoken (enterpriseid, warehousefromdate);
CREATE INDEX idx_artst_st_wh ON arrangement.arrangementtypesecuritytoken (securitytokenid, warehousefromdate);
CREATE INDEX idx_artst_sys_wh ON arrangement.arrangementtypesecuritytoken (systemid, warehousefromdate);
CREATE INDEX idx_artst_af_wh ON arrangement.arrangementtypesecuritytoken (activeflagid, warehousefromdate);
CREATE INDEX idx_artst_tid_wh ON arrangement.arrangementtypesecuritytoken (arrangementtypeid, warehousefromdate);

-- Indexes for arrangement.arrangementtypexclassification
CREATE INDEX idx_artxc_eff_from ON arrangement.arrangementtypexclassification (effectivefromdate);
CREATE INDEX idx_artxc_eff_to ON arrangement.arrangementtypexclassification (effectivetodate);
CREATE INDEX idx_artxc_wh_created ON arrangement.arrangementtypexclassification (warehousecreatedtimestamp);
CREATE INDEX idx_artxc_wh_updated ON arrangement.arrangementtypexclassification (warehouselastupdatedtimestamp);
CREATE INDEX idx_artxc_val ON arrangement.arrangementtypexclassification (value);
CREATE INDEX idx_artxc_ei_wh ON arrangement.arrangementtypexclassification (enterpriseid, warehousefromdate);
CREATE INDEX idx_artxc_af_wh ON arrangement.arrangementtypexclassification (activeflagid, warehousefromdate);
CREATE INDEX idx_artxc_sys_wh ON arrangement.arrangementtypexclassification (systemid, warehousefromdate);
CREATE INDEX idx_artxc_tid_wh ON arrangement.arrangementtypexclassification (arrangementtypeid, warehousefromdate);

-- Indexes for arrangement.arrangementtypexclassificationsecuritytoken
CREATE INDEX idx_artxcst_eff_from ON arrangement.arrangementtypexclassificationsecuritytoken (effectivefromdate);
CREATE INDEX idx_artxcst_eff_to ON arrangement.arrangementtypexclassificationsecuritytoken (effectivetodate);
CREATE INDEX idx_artxcst_wh_created ON arrangement.arrangementtypexclassificationsecuritytoken (warehousecreatedtimestamp);
CREATE INDEX idx_artxcst_wh_updated ON arrangement.arrangementtypexclassificationsecuritytoken (warehouselastupdatedtimestamp);
CREATE INDEX idx_artxcst_ei_wh ON arrangement.arrangementtypexclassificationsecuritytoken (enterpriseid, warehousefromdate);
CREATE INDEX idx_artxcst_st_wh ON arrangement.arrangementtypexclassificationsecuritytoken (securitytokenid, warehousefromdate);
CREATE INDEX idx_artxcst_sys_wh ON arrangement.arrangementtypexclassificationsecuritytoken (systemid, warehousefromdate);
CREATE INDEX idx_artxcst_tid_wh ON arrangement.arrangementtypexclassificationsecuritytoken (arrangementtypexclassificationid, warehousefromdate);
CREATE INDEX idx_artxcst_af_wh ON arrangement.arrangementtypexclassificationsecuritytoken (activeflagid, warehousefromdate);

-- Indexes for arrangement.arrangementxarrangement
CREATE INDEX idx_arxa_eff_from ON arrangement.arrangementxarrangement (effectivefromdate);
CREATE INDEX idx_arxa_eff_to ON arrangement.arrangementxarrangement (effectivetodate);
CREATE INDEX idx_arxa_wh_created ON arrangement.arrangementxarrangement (warehousecreatedtimestamp);
CREATE INDEX idx_arxa_wh_updated ON arrangement.arrangementxarrangement (warehouselastupdatedtimestamp);
CREATE INDEX idx_arxa_val ON arrangement.arrangementxarrangement (value);
CREATE INDEX idx_arxa_ei_wh ON arrangement.arrangementxarrangement (enterpriseid, warehousefromdate);
CREATE INDEX idx_arxa_af_wh ON arrangement.arrangementxarrangement (activeflagid, warehousefromdate);
CREATE INDEX idx_arxa_sys_wh ON arrangement.arrangementxarrangement (systemid, warehousefromdate);
CREATE INDEX idx_arxa_cl_wh ON arrangement.arrangementxarrangement (classificationid, warehousefromdate);

-- Indexes for arrangement.arrangementxarrangementsecuritytoken
CREATE INDEX idx_arxast_eff_from ON arrangement.arrangementxarrangementsecuritytoken (effectivefromdate);
CREATE INDEX idx_arxast_eff_to ON arrangement.arrangementxarrangementsecuritytoken (effectivetodate);
CREATE INDEX idx_arxast_wh_created ON arrangement.arrangementxarrangementsecuritytoken (warehousecreatedtimestamp);
CREATE INDEX idx_arxast_wh_updated ON arrangement.arrangementxarrangementsecuritytoken (warehouselastupdatedtimestamp);
CREATE INDEX idx_arxast_ei_wh ON arrangement.arrangementxarrangementsecuritytoken (enterpriseid, warehousefromdate);
CREATE INDEX idx_arxast_st_wh ON arrangement.arrangementxarrangementsecuritytoken (securitytokenid, warehousefromdate);
CREATE INDEX idx_arxast_af_wh ON arrangement.arrangementxarrangementsecuritytoken (activeflagid, warehousefromdate);
CREATE INDEX idx_arxast_sys_wh ON arrangement.arrangementxarrangementsecuritytoken (systemid, warehousefromdate);

-- Indexes for arrangement.arrangementxarrangementtype
CREATE INDEX idx_arxat_eff_from ON arrangement.arrangementxarrangementtype (effectivefromdate);
CREATE INDEX idx_arxat_eff_to ON arrangement.arrangementxarrangementtype (effectivetodate);
CREATE INDEX idx_arxat_wh_created ON arrangement.arrangementxarrangementtype (warehousecreatedtimestamp);
CREATE INDEX idx_arxat_wh_updated ON arrangement.arrangementxarrangementtype (warehouselastupdatedtimestamp);
CREATE INDEX idx_arxat_val ON arrangement.arrangementxarrangementtype (value);
CREATE INDEX idx_arxat_ei_wh ON arrangement.arrangementxarrangementtype (enterpriseid, warehousefromdate);
CREATE INDEX idx_arxat_af_wh ON arrangement.arrangementxarrangementtype (activeflagid, warehousefromdate);
CREATE INDEX idx_arxat_sys_wh ON arrangement.arrangementxarrangementtype (systemid, warehousefromdate);
CREATE INDEX idx_arxat_cl_wh ON arrangement.arrangementxarrangementtype (classificationid, warehousefromdate);

-- Indexes for arrangement.arrangementxclassification
CREATE INDEX idx_arxc_eff_from ON arrangement.arrangementxclassification (effectivefromdate);
CREATE INDEX idx_arxc_eff_to ON arrangement.arrangementxclassification (effectivetodate);
CREATE INDEX idx_arxc_wh_created ON arrangement.arrangementxclassification (warehousecreatedtimestamp);
CREATE INDEX idx_arxc_wh_updated ON arrangement.arrangementxclassification (warehouselastupdatedtimestamp);
CREATE INDEX idx_arxc_val ON arrangement.arrangementxclassification (value);
CREATE INDEX idx_arxc_ei_wh ON arrangement.arrangementxclassification (enterpriseid, warehousefromdate);
CREATE INDEX idx_arxc_af_wh ON arrangement.arrangementxclassification (activeflagid, warehousefromdate);
CREATE INDEX idx_arxc_sys_wh ON arrangement.arrangementxclassification (systemid, warehousefromdate);
CREATE INDEX idx_arxc_cl_wh ON arrangement.arrangementxclassification (classificationid, warehousefromdate);
CREATE INDEX idx_arxc_sid_wh ON arrangement.arrangementxclassification (arrangementid, warehousefromdate);

-- Indexes for arrangement.arrangementxclassificationsecuritytoken
CREATE INDEX idx_arxcst_eff_from ON arrangement.arrangementxclassificationsecuritytoken (effectivefromdate);
CREATE INDEX idx_arxcst_eff_to ON arrangement.arrangementxclassificationsecuritytoken (effectivetodate);
CREATE INDEX idx_arxcst_wh_created ON arrangement.arrangementxclassificationsecuritytoken (warehousecreatedtimestamp);
CREATE INDEX idx_arxcst_wh_updated ON arrangement.arrangementxclassificationsecuritytoken (warehouselastupdatedtimestamp);
CREATE INDEX idx_arxcst_ei_wh ON arrangement.arrangementxclassificationsecuritytoken (enterpriseid, warehousefromdate);
CREATE INDEX idx_arxcst_st_wh ON arrangement.arrangementxclassificationsecuritytoken (securitytokenid, warehousefromdate);
CREATE INDEX idx_arxcst_sys_wh ON arrangement.arrangementxclassificationsecuritytoken (systemid, warehousefromdate);
CREATE INDEX idx_arxcst_af_wh ON arrangement.arrangementxclassificationsecuritytoken (activeflagid, warehousefromdate);

-- Indexes for arrangement.arrangementxinvolvedparty
CREATE INDEX idx_arxip_eff_from ON arrangement.arrangementxinvolvedparty (effectivefromdate);
CREATE INDEX idx_arxip_eff_to ON arrangement.arrangementxinvolvedparty (effectivetodate);
CREATE INDEX idx_arxip_wh_created ON arrangement.arrangementxinvolvedparty (warehousecreatedtimestamp);
CREATE INDEX idx_arxip_wh_updated ON arrangement.arrangementxinvolvedparty (warehouselastupdatedtimestamp);
CREATE INDEX idx_arxip_val ON arrangement.arrangementxinvolvedparty (value);
CREATE INDEX idx_arxip_ei_wh ON arrangement.arrangementxinvolvedparty (enterpriseid, warehousefromdate);
CREATE INDEX idx_arxip_af_wh ON arrangement.arrangementxinvolvedparty (activeflagid, warehousefromdate);
CREATE INDEX idx_arxip_sys_wh ON arrangement.arrangementxinvolvedparty (systemid, warehousefromdate);
CREATE INDEX idx_arxip_sid_wh ON arrangement.arrangementxinvolvedparty (arrangementid, warehousefromdate);

-- Indexes for arrangement.arrangementxinvolvedpartysecuritytoken
CREATE INDEX idx_arxipst_eff_from ON arrangement.arrangementxinvolvedpartysecuritytoken (effectivefromdate);
CREATE INDEX idx_arxipst_eff_to ON arrangement.arrangementxinvolvedpartysecuritytoken (effectivetodate);
CREATE INDEX idx_arxipst_wh_created ON arrangement.arrangementxinvolvedpartysecuritytoken (warehousecreatedtimestamp);
CREATE INDEX idx_arxipst_wh_updated ON arrangement.arrangementxinvolvedpartysecuritytoken (warehouselastupdatedtimestamp);
CREATE INDEX idx_arxipst_ei_wh ON arrangement.arrangementxinvolvedpartysecuritytoken (enterpriseid, warehousefromdate);
CREATE INDEX idx_arxipst_st_wh ON arrangement.arrangementxinvolvedpartysecuritytoken (securitytokenid, warehousefromdate);
CREATE INDEX idx_arxipst_sys_wh ON arrangement.arrangementxinvolvedpartysecuritytoken (systemid, warehousefromdate);
CREATE INDEX idx_arxipst_af_wh ON arrangement.arrangementxinvolvedpartysecuritytoken (activeflagid, warehousefromdate);

-- Indexes for arrangement.arrangementxproduct
CREATE INDEX idx_arxp_eff_from ON arrangement.arrangementxproduct (effectivefromdate);
CREATE INDEX idx_arxp_eff_to ON arrangement.arrangementxproduct (effectivetodate);
CREATE INDEX idx_arxp_wh_created ON arrangement.arrangementxproduct (warehousecreatedtimestamp);
CREATE INDEX idx_arxp_wh_updated ON arrangement.arrangementxproduct (warehouselastupdatedtimestamp);
CREATE INDEX idx_arxp_val ON arrangement.arrangementxproduct (value);
CREATE INDEX idx_arxp_ei_wh ON arrangement.arrangementxproduct (enterpriseid, warehousefromdate);
CREATE INDEX idx_arxp_af_wh ON arrangement.arrangementxproduct (activeflagid, warehousefromdate);
CREATE INDEX idx_arxp_sys_wh ON arrangement.arrangementxproduct (systemid, warehousefromdate);
CREATE INDEX idx_arxp_cl_wh ON arrangement.arrangementxproduct (classificationid, warehousefromdate);
CREATE INDEX idx_arxp_sid_wh ON arrangement.arrangementxproduct (arrangementid, warehousefromdate);

-- Indexes for arrangement.arrangementxproductsecuritytoken
CREATE INDEX idx_arxpst_eff_from ON arrangement.arrangementxproductsecuritytoken (effectivefromdate);
CREATE INDEX idx_arxpst_eff_to ON arrangement.arrangementxproductsecuritytoken (effectivetodate);
CREATE INDEX idx_arxpst_wh_created ON arrangement.arrangementxproductsecuritytoken (warehousecreatedtimestamp);
CREATE INDEX idx_arxpst_wh_updated ON arrangement.arrangementxproductsecuritytoken (warehouselastupdatedtimestamp);
CREATE INDEX idx_arxpst_ei_wh ON arrangement.arrangementxproductsecuritytoken (enterpriseid, warehousefromdate);
CREATE INDEX idx_arxpst_st_wh ON arrangement.arrangementxproductsecuritytoken (securitytokenid, warehousefromdate);
CREATE INDEX idx_arxpst_sys_wh ON arrangement.arrangementxproductsecuritytoken (systemid, warehousefromdate);
CREATE INDEX idx_arxpst_af_wh ON arrangement.arrangementxproductsecuritytoken (activeflagid, warehousefromdate);

-- Indexes for arrangement.arrangementxresourceitem
CREATE INDEX idx_arxrist_eff_from ON arrangement.arrangementxresourceitem (effectivefromdate);
CREATE INDEX idx_arxrist_eff_to ON arrangement.arrangementxresourceitem (effectivetodate);
CREATE INDEX idx_arxrist_wh_created ON arrangement.arrangementxresourceitem (warehousecreatedtimestamp);
CREATE INDEX idx_arxrist_wh_updated ON arrangement.arrangementxresourceitem (warehouselastupdatedtimestamp);
CREATE INDEX idx_arxrist_val ON arrangement.arrangementxresourceitem (value);
CREATE INDEX idx_arxrist_ei_wh ON arrangement.arrangementxresourceitem (enterpriseid, warehousefromdate);
CREATE INDEX idx_arxrist_af_wh ON arrangement.arrangementxresourceitem (activeflagid, warehousefromdate);
CREATE INDEX idx_arxrist_sys_wh ON arrangement.arrangementxresourceitem (systemid, warehousefromdate);
CREATE INDEX idx_arxrist_cl_wh ON arrangement.arrangementxresourceitem (classificationid, warehousefromdate);
CREATE INDEX idx_arxrist_sid_wh ON arrangement.arrangementxresourceitem (arrangementid, warehousefromdate);
CREATE INDEX idx_arxrist_rid_wh ON arrangement.arrangementxresourceitem (resourceitemid, warehousefromdate);

-- Indexes for arrangement.arrangementxresourceitemsecuritytoken
CREATE INDEX idx_arxristst_eff_from ON arrangement.arrangementxresourceitemsecuritytoken (effectivefromdate);
CREATE INDEX idx_arxristst_eff_to ON arrangement.arrangementxresourceitemsecuritytoken (effectivetodate);
CREATE INDEX idx_arxristst_wh_created ON arrangement.arrangementxresourceitemsecuritytoken (warehousecreatedtimestamp);
CREATE INDEX idx_arxristst_wh_updated ON arrangement.arrangementxresourceitemsecuritytoken (warehouselastupdatedtimestamp);
CREATE INDEX idx_arxristst_ei_wh ON arrangement.arrangementxresourceitemsecuritytoken (enterpriseid, warehousefromdate);
CREATE INDEX idx_arxristst_st_wh ON arrangement.arrangementxresourceitemsecuritytoken (securitytokenid, warehousefromdate);
CREATE INDEX idx_arxristst_sys_wh ON arrangement.arrangementxresourceitemsecuritytoken (systemid, warehousefromdate);
CREATE INDEX idx_arxristst_af_wh ON arrangement.arrangementxresourceitemsecuritytoken (activeflagid, warehousefromdate);
CREATE INDEX idx_arxristst_rid_wh ON arrangement.arrangementxresourceitemsecuritytoken (arrangementxresourceitemid, warehousefromdate);

-- Indexes for arrangement.arrangementxrules
CREATE INDEX idx_arxrules_eff_from ON arrangement.arrangementxrules (effectivefromdate);
CREATE INDEX idx_arxrules_eff_to ON arrangement.arrangementxrules (effectivetodate);
CREATE INDEX idx_arxrules_wh_created ON arrangement.arrangementxrules (warehousecreatedtimestamp);
CREATE INDEX idx_arxrules_wh_updated ON arrangement.arrangementxrules (warehouselastupdatedtimestamp);
CREATE INDEX idx_arxrules_val ON arrangement.arrangementxrules (value);
CREATE INDEX idx_arxrules_ei_wh ON arrangement.arrangementxrules (enterpriseid, warehousefromdate);
CREATE INDEX idx_arxrules_af_wh ON arrangement.arrangementxrules (activeflagid, warehousefromdate);
CREATE INDEX idx_arxrules_sys_wh ON arrangement.arrangementxrules (systemid, warehousefromdate);
CREATE INDEX idx_arxrules_cl_wh ON arrangement.arrangementxrules (classificationid, warehousefromdate);
CREATE INDEX idx_arxrules_sid_wh ON arrangement.arrangementxrules (arrangementid, warehousefromdate);
CREATE INDEX idx_arxrules_rid_wh ON arrangement.arrangementxrules (rulesid, warehousefromdate);

-- Indexes for arrangement.arrangementxrulessecuritytoken
CREATE INDEX idx_arxrulesst_eff_from ON arrangement.arrangementxrulessecuritytoken (effectivefromdate);
CREATE INDEX idx_arxrulesst_eff_to ON arrangement.arrangementxrulessecuritytoken (effectivetodate);
CREATE INDEX idx_arxrulesst_wh_created ON arrangement.arrangementxrulessecuritytoken (warehousecreatedtimestamp);
CREATE INDEX idx_arxrulesst_wh_updated ON arrangement.arrangementxrulessecuritytoken (warehouselastupdatedtimestamp);
CREATE INDEX idx_arxrulesst_ei_wh ON arrangement.arrangementxrulessecuritytoken (enterpriseid, warehousefromdate);
CREATE INDEX idx_arxrulesst_st_wh ON arrangement.arrangementxrulessecuritytoken (securitytokenid, warehousefromdate);
CREATE INDEX idx_arxrulesst_sys_wh ON arrangement.arrangementxrulessecuritytoken (systemid, warehousefromdate);
CREATE INDEX idx_arxrulesst_af_wh ON arrangement.arrangementxrulessecuritytoken (activeflagid, warehousefromdate);

-- Indexes for arrangement.arrangementxrulestype
CREATE INDEX idx_arxrt_eff_from ON arrangement.arrangementxrulestype (effectivefromdate);
CREATE INDEX idx_arxrt_eff_to ON arrangement.arrangementxrulestype (effectivetodate);
CREATE INDEX idx_arxrt_wh_created ON arrangement.arrangementxrulestype (warehousecreatedtimestamp);
CREATE INDEX idx_arxrt_wh_updated ON arrangement.arrangementxrulestype (warehouselastupdatedtimestamp);
CREATE INDEX idx_arxrt_val ON arrangement.arrangementxrulestype (value);
CREATE INDEX idx_arxrt_ei_wh ON arrangement.arrangementxrulestype (enterpriseid, warehousefromdate);
CREATE INDEX idx_arxrt_af_wh ON arrangement.arrangementxrulestype (activeflagid, warehousefromdate);
CREATE INDEX idx_arxrt_sys_wh ON arrangement.arrangementxrulestype (systemid, warehousefromdate);
CREATE INDEX idx_arxrt_cl_wh ON arrangement.arrangementxrulestype (classificationid, warehousefromdate);
CREATE INDEX idx_arxrt_sid_wh ON arrangement.arrangementxrulestype (arrangementid, warehousefromdate);
CREATE INDEX idx_arxrt_rid_wh ON arrangement.arrangementxrulestype (rulestypeid, warehousefromdate);

-- Indexes for arrangement.arrangementxrulestypesecuritytoken
CREATE INDEX idx_arxrtst_eff_from ON arrangement.arrangementxrulestypesecuritytoken (effectivefromdate);
CREATE INDEX idx_arxrtst_eff_to ON arrangement.arrangementxrulestypesecuritytoken (effectivetodate);
CREATE INDEX idx_arxrtst_wh_created ON arrangement.arrangementxrulestypesecuritytoken (warehousecreatedtimestamp);
CREATE INDEX idx_arxrtst_wh_updated ON arrangement.arrangementxrulestypesecuritytoken (warehouselastupdatedtimestamp);
CREATE INDEX idx_arxrtst_ei_wh ON arrangement.arrangementxrulestypesecuritytoken (enterpriseid, warehousefromdate);
CREATE INDEX idx_arxrtst_st_wh ON arrangement.arrangementxrulestypesecuritytoken (securitytokenid, warehousefromdate);
CREATE INDEX idx_arxrtst_sys_wh ON arrangement.arrangementxrulestypesecuritytoken (systemid, warehousefromdate);
CREATE INDEX idx_arxrtst_af_wh ON arrangement.arrangementxrulestypesecuritytoken (activeflagid, warehousefromdate);
CREATE INDEX idx_arxrtst_tid_wh ON arrangement.arrangementxrulestypesecuritytoken (arrangementxrulestypeid, warehousefromdate);