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



create index fk5qltxdf1yl3w935f1v6c1s3te_activeflagid on arrangement.arrangement (activeflagid);
create index fk9gnhqn13p53nkwow24uj5lllt_originalsourcesystemid on arrangement.arrangement (originalsourcesystemid);
create index fkhyfnlmxxkjivonnw64dir0iph_enterpriseid on arrangement.arrangement (enterpriseid);
create index fko7k9jfu2q1gdbwkhan19cjg6r_systemid on arrangement.arrangement (systemid);
create index fk8h3yhvjy6fw0rvr2uyxq8voh5_systemid on arrangement.arrangementsecuritytoken (systemid);
create index fkngh7orqtfcriw1bpc7ctcu9ay_originalsourcesystemid on arrangement.arrangementsecuritytoken (originalsourcesystemid);
create index fknjnehgnp3ybq6wcnfbx6clqi8_securitytokenid on arrangement.arrangementsecuritytoken (securitytokenid);
create index fkodrogs5m3dhd54wsy89161ika_enterpriseid on arrangement.arrangementsecuritytoken (enterpriseid);
create index fkrhnkn0cyirv36wdfbx81p2omn_arrangementid on arrangement.arrangementsecuritytoken (arrangementid);
create index fkt5ibl9ewo11erwy04qt07riv3_activeflagid on arrangement.arrangementsecuritytoken (activeflagid);
create index fkaflivu0h1pm24n5y95tw5gv1y_originalsourcesystemid on arrangement.arrangementtype (originalsourcesystemid);
create index fkm3ev8du7vdvw2rislo3gxsvv9_systemid on arrangement.arrangementtype (systemid);
create index fknk2gclr0bkbxp3dvg0yboy59o_activeflagid on arrangement.arrangementtype (activeflagid);
create index fkro0fvyrds9erw2wk2s6gderhd_enterpriseid on arrangement.arrangementtype (enterpriseid);
create index fk1kek1dppp202jrr8e9f692syg_originalsourcesystemid on arrangement.arrangementtypesecuritytoken (originalsourcesystemid);
create index fk4kogklx4xlk4ck1ol3nc3xuf3_enterpriseid on arrangement.arrangementtypesecuritytoken (enterpriseid);
create index fk8to2ic6e1hst2vlk01q5p7cw1_arrangementtypeid on arrangement.arrangementtypesecuritytoken (arrangementtypeid);
create index fk9le4bf2k6hm5nc8ieluakg4fd_securitytokenid on arrangement.arrangementtypesecuritytoken (securitytokenid);
create index fkj02k0j5pke3dkpaxhbgmaikqi_systemid on arrangement.arrangementtypesecuritytoken (systemid);
create index fkp3x2trigpgx2f2q7h92n2tlr2_activeflagid on arrangement.arrangementtypesecuritytoken (activeflagid);
create index fk3cu1a03o9dfjsrxdgg5f8ljc1_systemid on arrangement.arrangementtypexclassification (systemid);
create index fkgqxi8s68s3xjnmr59l1mjrm3g_classificationid on arrangement.arrangementtypexclassification (classificationid);
create index fkhasvr2i2sploam3msr9ujxvg4_arrangementtypeid on arrangement.arrangementtypexclassification (arrangementtypeid);
create index fklexxdvodllklvfn0ua4r0aw0u_originalsourcesystemid on arrangement.arrangementtypexclassification (originalsourcesystemid);
create index fklne0s6fbh4wdnpnvija69e2d8_activeflagid on arrangement.arrangementtypexclassification (activeflagid);
create index fkpi8wetfnfovvirlmsfvhu89bh_enterpriseid on arrangement.arrangementtypexclassification (enterpriseid);
create index fk9veykvob6op2xdb32ad53tu82_arrangementtypexclassificationid on arrangement.arrangementtypexclassificationsecuritytoken (arrangementtypexclassificationid);
create index fkfieks9k5klxucyqm2qel3dvyk_securitytokenid on arrangement.arrangementtypexclassificationsecuritytoken (securitytokenid);
create index fkfuddtuea83j0rqqy24iwxsddr_systemid on arrangement.arrangementtypexclassificationsecuritytoken (systemid);
create index fkhsvu9ibj7uijfgn34h38l5nh3_originalsourcesystemid on arrangement.arrangementtypexclassificationsecuritytoken (originalsourcesystemid);
create index fkko0edkl361kq6uwstfhcmp1e7_enterpriseid on arrangement.arrangementtypexclassificationsecuritytoken (enterpriseid);
create index fkpe27j1xif8mx3f5q5dh6o41an_activeflagid on arrangement.arrangementtypexclassificationsecuritytoken (activeflagid);
create index fk1evl6ld81fkqmo674i0ip26v_parentarrangementid on arrangement.arrangementxarrangement (parentarrangementid);
create index fk5x95ig2e720s90163vs5mru16_activeflagid on arrangement.arrangementxarrangement (activeflagid);
create index fk9g734gsx3xenay6j6aw1t11dv_enterpriseid on arrangement.arrangementxarrangement (enterpriseid);
create index fkbli36e1w9e3a47g2s5a5unn2n_childarrangementid on arrangement.arrangementxarrangement (childarrangementid);
create index fkkg32gfwya6793p80k1no6hmk8_originalsourcesystemid on arrangement.arrangementxarrangement (originalsourcesystemid);
create index fkovmdueeas3icmh2sa2j64g4ck_classificationid on arrangement.arrangementxarrangement (classificationid);
create index fksw7clyy90t99djoxclxg646im_systemid on arrangement.arrangementxarrangement (systemid);
create index fk2fi4ltyv1r2frptrof3moedig_originalsourcesystemid on arrangement.arrangementxarrangementsecuritytoken (originalsourcesystemid);
create index fk2hmmp93bfci8j7pwlv14p7m9_securitytokenid on arrangement.arrangementxarrangementsecuritytoken (securitytokenid);
create index fk4b5sc95gghbab83uot4y8kma4_enterpriseid on arrangement.arrangementxarrangementsecuritytoken (enterpriseid);
create index fkby5vbby0hxu3nnxqjkgpg71xq_arrangementxarrangementid on arrangement.arrangementxarrangementsecuritytoken (arrangementxarrangementid);
create index fkc6bmk80c5n3kl4frhnr5nsn5s_activeflagid on arrangement.arrangementxarrangementsecuritytoken (activeflagid);
create index fkieslcpo9uox0881igyk7rdfrq_systemid on arrangement.arrangementxarrangementsecuritytoken (systemid);
create index fkb2mvoggrta8b3xlgvxav3gv68_activeflagid on arrangement.arrangementxarrangementtype (activeflagid);
create index fkbbug6qxo424aa0l3k0ilpjwh1_arrangementid on arrangement.arrangementxarrangementtype (arrangementid);
create index fkbcb78fk9imiu30j55mudj4w9l_systemid on arrangement.arrangementxarrangementtype (systemid);
create index fkek405uwe4g6xo9tdi9gfhg88y_arrangementtypeid on arrangement.arrangementxarrangementtype (arrangementtypeid);
create index fkofvbeuabms8ebk75wsgnkjbld_originalsourcesystemid on arrangement.arrangementxarrangementtype (originalsourcesystemid);
create index fkqxy44j74nhe3dhfwf595lguwl_enterpriseid on arrangement.arrangementxarrangementtype (enterpriseid);
create index fkshy3d2vx1xpatx6rnu60ag7jg_classificationid on arrangement.arrangementxarrangementtype (classificationid);
create index fk4f2d6k0rr9lafbgy0j4w59405_arrangementxarrangementtypeid on arrangement.arrangementxarrangementtypesecuritytoken (arrangementxarrangementtypeid);
create index fk67y1klef35wuyfupagyk4roic_enterpriseid on arrangement.arrangementxarrangementtypesecuritytoken (enterpriseid);
create index fk7y5i3hvgtvn2u9mtx8wljsh9a_systemid on arrangement.arrangementxarrangementtypesecuritytoken (systemid);
create index fk9q39hofgnynlo6lfnv57d0wn7_activeflagid on arrangement.arrangementxarrangementtypesecuritytoken (activeflagid);
create index fkk5vha5aon2i7roy6wox5r8k1d_securitytokenid on arrangement.arrangementxarrangementtypesecuritytoken (securitytokenid);
create index fkqi48u6i84yjq1jg357fh1qwgi_originalsourcesystemid on arrangement.arrangementxarrangementtypesecuritytoken (originalsourcesystemid);
create index fk5rajkqbykrmn8tkkrqg2clkk1_arrangementid on arrangement.arrangementxclassification (arrangementid);
create index fk9w31er1wt79j9n4v3ugfhb6hn_classificationid on arrangement.arrangementxclassification (classificationid);
create index fkbkgxtaag54r6vr8vyt0p6e2uu_enterpriseid on arrangement.arrangementxclassification (enterpriseid);
create index fkesqu1vwy8pm4ori8xt2hlt00y_activeflagid on arrangement.arrangementxclassification (activeflagid);
create index fkpwmnjntxrxxp88ohtix3f9rld_originalsourcesystemid on arrangement.arrangementxclassification (originalsourcesystemid);
create index fkqryq2ja6bavkj7a5bj7w081kp_systemid on arrangement.arrangementxclassification (systemid);
create index fk6tsk1t4h6o94591ke2oy8fcte_activeflagid on arrangement.arrangementxclassificationsecuritytoken (activeflagid);
create index fk7h39db92fgksv3rwgkq72hsd1_systemid on arrangement.arrangementxclassificationsecuritytoken (systemid);
create index fk8jucexc7kuoodu11v39lpwxj9_securitytokenid on arrangement.arrangementxclassificationsecuritytoken (securitytokenid);
create index fk9c1o9pupbc18y3et9022u6etc_enterpriseid on arrangement.arrangementxclassificationsecuritytoken (enterpriseid);
create index fk9cdguosd6bmsd6ihjueeay3qu_arrangementxclassificationid on arrangement.arrangementxclassificationsecuritytoken (arrangementxclassificationid);
create index fkmxvd2rnt5882sxxk831sdbkqq_originalsourcesystemid on arrangement.arrangementxclassificationsecuritytoken (originalsourcesystemid);
create index fk2nbjk1qgv7xcqx60p5k9fpknv_enterpriseid on arrangement.arrangementxinvolvedparty (enterpriseid);
create index fk6c7521dhlo7ou6o4oeosk7eop_originalsourcesystemid on arrangement.arrangementxinvolvedparty (originalsourcesystemid);
create index fk6ka1ls0qmv9y1iaya2rkr8m5r_classificationid on arrangement.arrangementxinvolvedparty (classificationid);
create index fk71wpg1p7v2qn02lh0cw5drs95_arrangementid on arrangement.arrangementxinvolvedparty (arrangementid);
create index fkc3wsa0x6xjq8pt6mnp8p2390l_systemid on arrangement.arrangementxinvolvedparty (systemid);
create index fki29w8ch3051e71mlykw6gt7p0_activeflagid on arrangement.arrangementxinvolvedparty (activeflagid);
create index fkkpvqwygo5551679i6hds7kada_involvedpartyid on arrangement.arrangementxinvolvedparty (involvedpartyid);
create index fk5nm7udu4cx5rwgqsxiy10vdmj_originalsourcesystemid on arrangement.arrangementxinvolvedpartysecuritytoken (originalsourcesystemid);
create index fkkyktyh5uldr0apnx4fb7rqqhj_arrangementxinvolvedpartyid on arrangement.arrangementxinvolvedpartysecuritytoken (arrangementxinvolvedpartyid);
create index fknhcqo7dtttikbygsv29vbxw64_enterpriseid on arrangement.arrangementxinvolvedpartysecuritytoken (enterpriseid);
create index fkp9gl2xg7lrargsi7wloadqsxa_systemid on arrangement.arrangementxinvolvedpartysecuritytoken (systemid);
create index fkqmjll31vsk7uwahn932wd5vob_activeflagid on arrangement.arrangementxinvolvedpartysecuritytoken (activeflagid);
create index fkqup67qfcgs10hc0x651xne7t9_securitytokenid on arrangement.arrangementxinvolvedpartysecuritytoken (securitytokenid);
create index fk1xsuwtqoogpaxitg2wjqk2rs3_systemid on arrangement.arrangementxproduct (systemid);
create index fk3uwrmqh1e9msanm5ujxk06mqh_productid on arrangement.arrangementxproduct (productid);
create index fkalyhg8tgsoumu832w7ol05mqv_classificationid on arrangement.arrangementxproduct (classificationid);
create index fked4l0kxkvvnw06uq0l4n55e35_enterpriseid on arrangement.arrangementxproduct (enterpriseid);
create index fki2inhxddbgmsdi3bl7mwt7fqi_originalsourcesystemid on arrangement.arrangementxproduct (originalsourcesystemid);
create index fkiissfi60gmgix1sr4ik8wp9fa_arrangementid on arrangement.arrangementxproduct (arrangementid);
create index fkt25tk83urovuvrxc874cm07hh_activeflagid on arrangement.arrangementxproduct (activeflagid);
create index fk2mf0po3jsf10wa7ypxoyj2t0q_activeflagid on arrangement.arrangementxproductsecuritytoken (activeflagid);
create index fk7ychxphmo3yvikab4txsecg1h_originalsourcesystemid on arrangement.arrangementxproductsecuritytoken (originalsourcesystemid);
create index fk82ys481oxq2v892sryshuytj0_arrangementxproductid on arrangement.arrangementxproductsecuritytoken (arrangementxproductid);
create index fkf0liawyl1pg73vaqhl9077ty2_enterpriseid on arrangement.arrangementxproductsecuritytoken (enterpriseid);
create index fkkfajnpjwfuc4kt13avqmjs27i_securitytokenid on arrangement.arrangementxproductsecuritytoken (securitytokenid);
create index fknp4l2srl6e09175ngloxrexer_systemid on arrangement.arrangementxproductsecuritytoken (systemid);
create index fk10auu483ou8vross23ilkm601_classificationid on arrangement.arrangementxresourceitem (classificationid);
create index fk5m4kptkleha73alk03p761cau_resourceitemid on arrangement.arrangementxresourceitem (resourceitemid);
create index fked04m2i7mgfyfrsxu60cmf8or_systemid on arrangement.arrangementxresourceitem (systemid);
create index fkiu0pr9p6o39mwwfh9epgoahom_arrangementid on arrangement.arrangementxresourceitem (arrangementid);
create index fkpj4eok42bmisj8g6r7crx0er5_activeflagid on arrangement.arrangementxresourceitem (activeflagid);
create index fkpjx2bwph3f67laisvffrnopd8_originalsourcesystemid on arrangement.arrangementxresourceitem (originalsourcesystemid);
create index fkrc9mg4osgj3ye4dab54n96ttq_enterpriseid on arrangement.arrangementxresourceitem (enterpriseid);
create index fk4lwhxuk5gfc02ycby5kfro576_securitytokenid on arrangement.arrangementxresourceitemsecuritytoken (securitytokenid);
create index fk8bgg9lfof21l7yvjet4et71xj_activeflagid on arrangement.arrangementxresourceitemsecuritytoken (activeflagid);
create index fki6fgnt16ckmqi9y21s9a8gp47_arrangementxresourceitemid on arrangement.arrangementxresourceitemsecuritytoken (arrangementxresourceitemid);
create index fkn7ohgt9uwimsmg8b1kcclgymq_systemid on arrangement.arrangementxresourceitemsecuritytoken (systemid);
create index fkofagwms8gkw6geqmiyncnj2cx_originalsourcesystemid on arrangement.arrangementxresourceitemsecuritytoken (originalsourcesystemid);
create index fkotwjcy38jqowrom4f1t9u4sxv_enterpriseid on arrangement.arrangementxresourceitemsecuritytoken (enterpriseid);
create index fk3q3s3y2ypar6b6yao94qiavib_activeflagid on arrangement.arrangementxrules (activeflagid);
create index fkbc3k5pxxb18ylj1p929i1t740_enterpriseid on arrangement.arrangementxrules (enterpriseid);
create index fkf8qlkxh19e3pedt1xmnwe786o_originalsourcesystemid on arrangement.arrangementxrules (originalsourcesystemid);
create index fkl1taisa4493ad6n8troarks3c_arrangementid on arrangement.arrangementxrules (arrangementid);
create index fkqyb5d3jb3i9rsxm0cfo208njj_systemid on arrangement.arrangementxrules (systemid);
create index fkrt9gl1ke0yp5rgncnsr0t2x2m_classificationid on arrangement.arrangementxrules (classificationid);
create index fksp0nolvpdwpwmjyghyp486frp_rulesid on arrangement.arrangementxrules (rulesid);
create index fk33pq6ks4wh6ap23lm8cmv11e3_securitytokenid on arrangement.arrangementxrulessecuritytoken (securitytokenid);
create index fk62jqx0o1s2q6r66fn8nrjo4bf_originalsourcesystemid on arrangement.arrangementxrulessecuritytoken (originalsourcesystemid);
create index fk6tttie9brrhlxxh2adq02kffo_enterpriseid on arrangement.arrangementxrulessecuritytoken (enterpriseid);
create index fkc4a0rcrpg0hotxs39ny4ki4qw_activeflagid on arrangement.arrangementxrulessecuritytoken (activeflagid);
create index fkgeha0vtd9yo30vppd0an8kcyp_arrangementxrulesid on arrangement.arrangementxrulessecuritytoken (arrangementxrulesid);
create index fkhf5rvmrtd7mq9vm4obo2m68eh_systemid on arrangement.arrangementxrulessecuritytoken (systemid);
create index fk6343urwgxbykoau8p7h0i58gj_activeflagid on arrangement.arrangementxrulestype (activeflagid);
create index fk69m07mb95iafnyyk11228ihmc_rulestypeid on arrangement.arrangementxrulestype (rulestypeid);
create index fk6rx9646bu0edq22qa8nol81vv_originalsourcesystemid on arrangement.arrangementxrulestype (originalsourcesystemid);
create index fkcnkik4eq3xs4h6v2j13ul3rsv_enterpriseid on arrangement.arrangementxrulestype (enterpriseid);
create index fkd7koulxa7fid1reyw9nyhhuts_arrangementid on arrangement.arrangementxrulestype (arrangementid);
create index fkov2gqdwxiyblbpqmnwc4np7x9_classificationid on arrangement.arrangementxrulestype (classificationid);
create index fkq01c7ongg1cofa4ceibxduma2_systemid on arrangement.arrangementxrulestype (systemid);
create index fk11a4s30v3c7vosoqpy1210fhu_arrangementxrulestypeid on arrangement.arrangementxrulestypesecuritytoken (arrangementxrulestypeid);
create index fk3plf0o1h097qqbme256dh14ms_enterpriseid on arrangement.arrangementxrulestypesecuritytoken (enterpriseid);
create index fk51besipwxf9c4wsjao3x1b2y1_originalsourcesystemid on arrangement.arrangementxrulestypesecuritytoken (originalsourcesystemid);
create index fk533wd6s99uye7jfrvnscmp7h0_securitytokenid on arrangement.arrangementxrulestypesecuritytoken (securitytokenid);
create index fk5m5vtq233eq18lrww0cu5isj9_activeflagid on arrangement.arrangementxrulestypesecuritytoken (activeflagid);
create index fkfxhigbw4io92wri4583cu4v0l_systemid on arrangement.arrangementxrulestypesecuritytoken (systemid);


CREATE INDEX idx_arrangementxarrangementtype_value ON arrangement.arrangementxarrangementtype (value);
CREATE INDEX idx_arrangementxclassification_value ON arrangement.arrangementxclassification (value);

CREATE INDEX idx_arrangementxarrangement_value ON arrangement.arrangementxarrangement (value);
CREATE INDEX idx_arrangementtypexclassification_value ON arrangement.arrangementtypexclassification (value);
CREATE INDEX idx_arrangementxproduct_value ON arrangement.arrangementxproduct (value);
CREATE INDEX idx_arrangementxresourceitem_value ON arrangement.arrangementxresourceitem (value);
CREATE INDEX idx_arrangementxinvolvedparty_value ON arrangement.arrangementxinvolvedparty (value);
CREATE INDEX idx_arrangementxrules_value ON arrangement.arrangementxrules (value);
CREATE INDEX idx_arrangementxrulestype_value ON arrangement.arrangementxrulestype (value);
CREATE INDEX idx_arrangementtype_arrangementtypename ON arrangement.arrangementtype (arrangementtypename);



create index fk5qltxdf1yl3w935f1v6c1s3te_activeflagidwhcd on arrangement.arrangement (activeflagid, warehousefromdate);
create index fk9gnhqn13p53nkwow24uj5lllt_originalsourcesystemidwhcd on arrangement.arrangement (originalsourcesystemid, warehousefromdate);
create index fkhyfnlmxxkjivonnw64dir0iph_enterpriseidwhcd on arrangement.arrangement (enterpriseid, warehousefromdate);
create index fko7k9jfu2q1gdbwkhan19cjg6r_systemidwhcd on arrangement.arrangement (systemid, warehousefromdate);
create index fk8h3yhvjy6fw0rvr2uyxq8voh5_systemidwhcd on arrangement.arrangementsecuritytoken (systemid, warehousefromdate);
create index fkngh7orqtfcriw1bpc7ctcu9ay_originalsourcesystemidwhcd on arrangement.arrangementsecuritytoken (originalsourcesystemid, warehousefromdate);
create index fknjnehgnp3ybq6wcnfbx6clqi8_securitytokenidwhcd on arrangement.arrangementsecuritytoken (securitytokenid, warehousefromdate);
create index fkodrogs5m3dhd54wsy89161ika_enterpriseidwhcd on arrangement.arrangementsecuritytoken (enterpriseid, warehousefromdate);
create index fkrhnkn0cyirv36wdfbx81p2omn_arrangementidwhcd on arrangement.arrangementsecuritytoken (arrangementid, warehousefromdate);
create index fkt5ibl9ewo11erwy04qt07riv3_activeflagidwhcd on arrangement.arrangementsecuritytoken (activeflagid, warehousefromdate);
create index fkaflivu0h1pm24n5y95tw5gv1y_originalsourcesystemidwhcd on arrangement.arrangementtype (originalsourcesystemid, warehousefromdate);
create index fkm3ev8du7vdvw2rislo3gxsvv9_systemidwhcd on arrangement.arrangementtype (systemid, warehousefromdate);
create index fknk2gclr0bkbxp3dvg0yboy59o_activeflagidwhcd on arrangement.arrangementtype (activeflagid, warehousefromdate);
create index fkro0fvyrds9erw2wk2s6gderhd_enterpriseidwhcd on arrangement.arrangementtype (enterpriseid, warehousefromdate);
create index fk1kek1dppp202jrr8e9f692syg_originalsourcesystemidwhcd on arrangement.arrangementtypesecuritytoken (originalsourcesystemid, warehousefromdate);
create index fk4kogklx4xlk4ck1ol3nc3xuf3_enterpriseidwhcd on arrangement.arrangementtypesecuritytoken (enterpriseid, warehousefromdate);
create index fk8to2ic6e1hst2vlk01q5p7cw1_arrangementtypeidwhcd on arrangement.arrangementtypesecuritytoken (arrangementtypeid, warehousefromdate);
create index fk9le4bf2k6hm5nc8ieluakg4fd_securitytokenidwhcd on arrangement.arrangementtypesecuritytoken (securitytokenid, warehousefromdate);
create index fkj02k0j5pke3dkpaxhbgmaikqi_systemidwhcd on arrangement.arrangementtypesecuritytoken (systemid, warehousefromdate);
create index fkp3x2trigpgx2f2q7h92n2tlr2_activeflagidwhcd on arrangement.arrangementtypesecuritytoken (activeflagid, warehousefromdate);
create index fk3cu1a03o9dfjsrxdgg5f8ljc1_systemidwhcd on arrangement.arrangementtypexclassification (systemid, warehousefromdate);
create index fkgqxi8s68s3xjnmr59l1mjrm3g_classificationidwhcd on arrangement.arrangementtypexclassification (classificationid, warehousefromdate);
create index fkhasvr2i2sploam3msr9ujxvg4_arrangementtypeidwhcd on arrangement.arrangementtypexclassification (arrangementtypeid, warehousefromdate);
create index fklexxdvodllklvfn0ua4r0aw0u_originalsourcesystemidwhcd on arrangement.arrangementtypexclassification (originalsourcesystemid, warehousefromdate);
create index fklne0s6fbh4wdnpnvija69e2d8_activeflagidwhcd on arrangement.arrangementtypexclassification (activeflagid, warehousefromdate);
create index fkpi8wetfnfovvirlmsfvhu89bh_enterpriseidwhcd on arrangement.arrangementtypexclassification (enterpriseid, warehousefromdate);
create index fk9veykvob6op2xdb3tu82_arrangemesificationidwhcd on arrangement.arrangementtypexclassificationsecuritytoken (arrangementtypexclassificationid, warehousefromdate);
create index fkfieks9k5klxucyqm2qel3dvyk_securitytokenidwhcd on arrangement.arrangementtypexclassificationsecuritytoken (securitytokenid, warehousefromdate);
create index fkfuddtuea83j0rqqy24iwxsddr_systemidwhcd on arrangement.arrangementtypexclassificationsecuritytoken (systemid, warehousefromdate);
create index fkhsvu9ibj7uijfgn34h38l5nh3_originalsourcesystemidwhcd on arrangement.arrangementtypexclassificationsecuritytoken (originalsourcesystemid, warehousefromdate);
create index fkko0edkl361kq6uwstfhcmp1e7_enterpriseidwhcd on arrangement.arrangementtypexclassificationsecuritytoken (enterpriseid, warehousefromdate);
create index fkpe27j1xif8mx3f5q5dh6o41an_activeflagidwhcd on arrangement.arrangementtypexclassificationsecuritytoken (activeflagid, warehousefromdate);
create index fk1evl6ld81fkqmo674i0ip26v_parentarrangementidwhcd on arrangement.arrangementxarrangement (parentarrangementid, warehousefromdate);
create index fk5x95ig2e720s90163vs5mru16_activeflagidwhcd on arrangement.arrangementxarrangement (activeflagid, warehousefromdate);
create index fk9g734gsx3xenay6j6aw1t11dv_enterpriseidwhcd on arrangement.arrangementxarrangement (enterpriseid, warehousefromdate);
create index fkbli36e1w9e3a47g2s5a5unn2n_childarrangementidwhcd on arrangement.arrangementxarrangement (childarrangementid, warehousefromdate);
create index fkkg32gfwya6793p80k1no6hmk8_originalsourcesystemidwhcd on arrangement.arrangementxarrangement (originalsourcesystemid, warehousefromdate);
create index fkovmdueeas3icmh2sa2j64g4ck_classificationidwhcd on arrangement.arrangementxarrangement (classificationid, warehousefromdate);
create index fksw7clyy90t99djoxclxg646im_systemidwhcd on arrangement.arrangementxarrangement (systemid, warehousefromdate);
create index fk2fi4ltyv1r2frptrof3moedig_originalsourcesystemidwhcd on arrangement.arrangementxarrangementsecuritytoken (originalsourcesystemid, warehousefromdate);
create index fk2hmmp93bfci8j7pwlv14p7m9_securitytokenidwhcd on arrangement.arrangementxarrangementsecuritytoken (securitytokenid, warehousefromdate);
create index fk4b5sc95gghbab83uot4y8kma4_enterpriseidwhcd on arrangement.arrangementxarrangementsecuritytoken (enterpriseid, warehousefromdate);
create index fkby5vbby0hxu3nnxqjkgpg71xq_arrangementxarrangementidwhcd on arrangement.arrangementxarrangementsecuritytoken (arrangementxarrangementid, warehousefromdate);
create index fkc6bmk80c5n3kl4frhnr5nsn5s_activeflagidwhcd on arrangement.arrangementxarrangementsecuritytoken (activeflagid, warehousefromdate);
create index fkieslcpo9uox0881igyk7rdfrq_systemidwhcd on arrangement.arrangementxarrangementsecuritytoken (systemid, warehousefromdate);
create index fkb2mvoggrta8b3xlgvxav3gv68_activeflagidwhcd on arrangement.arrangementxarrangementtype (activeflagid, warehousefromdate);
create index fkbbug6qxo424aa0l3k0ilpjwh1_arrangementidwhcd on arrangement.arrangementxarrangementtype (arrangementid, warehousefromdate);
create index fkbcb78fk9imiu30j55mudj4w9l_systemidwhcd on arrangement.arrangementxarrangementtype (systemid, warehousefromdate);
create index fkek405uwe4g6xo9tdi9gfhg88y_arrangementtypeidwhcd on arrangement.arrangementxarrangementtype (arrangementtypeid, warehousefromdate);
create index fkofvbeuabms8ebk75wsgnkjbld_originalsourcesystemidwhcd on arrangement.arrangementxarrangementtype (originalsourcesystemid, warehousefromdate);
create index fkqxy44j74nhe3dhfwf595lguwl_enterpriseidwhcd on arrangement.arrangementxarrangementtype (enterpriseid, warehousefromdate);
create index fkshy3d2vx1xpatx6rnu60ag7jg_classificationidwhcd on arrangement.arrangementxarrangementtype (classificationid, warehousefromdate);
create index fk4f2d6k0rr9lafbgy0j4w59405_arrangementxarrangementtypeidwhcd on arrangement.arrangementxarrangementtypesecuritytoken (arrangementxarrangementtypeid, warehousefromdate);
create index fk67y1klef35wuyfupagyk4roic_enterpriseidwhcd on arrangement.arrangementxarrangementtypesecuritytoken (enterpriseid, warehousefromdate);
create index fk7y5i3hvgtvn2u9mtx8wljsh9a_systemidwhcd on arrangement.arrangementxarrangementtypesecuritytoken (systemid, warehousefromdate);
create index fk9q39hofgnynlo6lfnv57d0wn7_activeflagidwhcd on arrangement.arrangementxarrangementtypesecuritytoken (activeflagid, warehousefromdate);
create index fkk5vha5aon2i7roy6wox5r8k1d_securitytokenidwhcd on arrangement.arrangementxarrangementtypesecuritytoken (securitytokenid, warehousefromdate);
create index fkqi48u6i84yjq1jg357fh1qwgi_originalsourcesystemidwhcd on arrangement.arrangementxarrangementtypesecuritytoken (originalsourcesystemid, warehousefromdate);
create index fk5rajkqbykrmn8tkkrqg2clkk1_arrangementidwhcd on arrangement.arrangementxclassification (arrangementid, warehousefromdate);
create index fk9w31er1wt79j9n4v3ugfhb6hn_classificationidwhcd on arrangement.arrangementxclassification (classificationid, warehousefromdate);
create index fkbkgxtaag54r6vr8vyt0p6e2uu_enterpriseidwhcd on arrangement.arrangementxclassification (enterpriseid, warehousefromdate);
create index fkesqu1vwy8pm4ori8xt2hlt00y_activeflagidwhcd on arrangement.arrangementxclassification (activeflagid, warehousefromdate);
create index fkpwmnjntxrxxp88ohtix3f9rld_originalsourcesystemidwhcd on arrangement.arrangementxclassification (originalsourcesystemid, warehousefromdate);
create index fkqryq2ja6bavkj7a5bj7w081kp_systemidwhcd on arrangement.arrangementxclassification (systemid, warehousefromdate);
create index fk6tsk1t4h6o94591ke2oy8fcte_activeflagidwhcd on arrangement.arrangementxclassificationsecuritytoken (activeflagid, warehousefromdate);
create index fk7h39db92fgksv3rwgkq72hsd1_systemidwhcd on arrangement.arrangementxclassificationsecuritytoken (systemid, warehousefromdate);
create index fk8jucexc7kuoodu11v39lpwxj9_securitytokenidwhcd on arrangement.arrangementxclassificationsecuritytoken (securitytokenid, warehousefromdate);
create index fk9c1o9pupbc18y3et9022u6etc_enterpriseidwhcd on arrangement.arrangementxclassificationsecuritytoken (enterpriseid, warehousefromdate);
create index fk9cdguosd6bmsd6ihjueeay3qu_arrangementxclassificationidwhcd on arrangement.arrangementxclassificationsecuritytoken (arrangementxclassificationid, warehousefromdate);
create index fkmxvd2rnt5882sxxk831sdbkqq_originalsourcesystemidwhcd on arrangement.arrangementxclassificationsecuritytoken (originalsourcesystemid, warehousefromdate);
create index fk2nbjk1qgv7xcqx60p5k9fpknv_enterpriseidwhcd on arrangement.arrangementxinvolvedparty (enterpriseid, warehousefromdate);
create index fk6c7521dhlo7ou6o4oeosk7eop_originalsourcesystemidwhcd on arrangement.arrangementxinvolvedparty (originalsourcesystemid, warehousefromdate);
create index fk6ka1ls0qmv9y1iaya2rkr8m5r_classificationidwhcd on arrangement.arrangementxinvolvedparty (classificationid, warehousefromdate);
create index fk71wpg1p7v2qn02lh0cw5drs95_arrangementidwhcd on arrangement.arrangementxinvolvedparty (arrangementid, warehousefromdate);
create index fkc3wsa0x6xjq8pt6mnp8p2390l_systemidwhcd on arrangement.arrangementxinvolvedparty (systemid, warehousefromdate);
create index fki29w8ch3051e71mlykw6gt7p0_activeflagidwhcd on arrangement.arrangementxinvolvedparty (activeflagid, warehousefromdate);
create index fkkpvqwygo5551679i6hds7kada_involvedpartyidwhcd on arrangement.arrangementxinvolvedparty (involvedpartyid, warehousefromdate);
create index fk5nm7udu4cx5rwgqsxiy10vdmj_originalsourcesystemidwhcd on arrangement.arrangementxinvolvedpartysecuritytoken (originalsourcesystemid, warehousefromdate);
create index fkkyktyh5uldr0apnx4fb7rqqhj_arrangementxinvolvedpartyidwhcd on arrangement.arrangementxinvolvedpartysecuritytoken (arrangementxinvolvedpartyid, warehousefromdate);
create index fknhcqo7dtttikbygsv29vbxw64_enterpriseidwhcd on arrangement.arrangementxinvolvedpartysecuritytoken (enterpriseid, warehousefromdate);
create index fkp9gl2xg7lrargsi7wloadqsxa_systemidwhcd on arrangement.arrangementxinvolvedpartysecuritytoken (systemid, warehousefromdate);
create index fkqmjll31vsk7uwahn932wd5vob_activeflagidwhcd on arrangement.arrangementxinvolvedpartysecuritytoken (activeflagid, warehousefromdate);
create index fkqup67qfcgs10hc0x651xne7t9_securitytokenidwhcd on arrangement.arrangementxinvolvedpartysecuritytoken (securitytokenid, warehousefromdate);
create index fk1xsuwtqoogpaxitg2wjqk2rs3_systemidwhcd on arrangement.arrangementxproduct (systemid, warehousefromdate);
create index fk3uwrmqh1e9msanm5ujxk06mqh_productidwhcd on arrangement.arrangementxproduct (productid, warehousefromdate);
create index fkalyhg8tgsoumu832w7ol05mqv_classificationidwhcd on arrangement.arrangementxproduct (classificationid, warehousefromdate);
create index fked4l0kxkvvnw06uq0l4n55e35_enterpriseidwhcd on arrangement.arrangementxproduct (enterpriseid, warehousefromdate);
create index fki2inhxddbgmsdi3bl7mwt7fqi_originalsourcesystemidwhcd on arrangement.arrangementxproduct (originalsourcesystemid, warehousefromdate);
create index fkiissfi60gmgix1sr4ik8wp9fa_arrangementidwhcd on arrangement.arrangementxproduct (arrangementid, warehousefromdate);
create index fkt25tk83urovuvrxc874cm07hh_activeflagidwhcd on arrangement.arrangementxproduct (activeflagid, warehousefromdate);
create index fk2mf0po3jsf10wa7ypxoyj2t0q_activeflagidwhcd on arrangement.arrangementxproductsecuritytoken (activeflagid, warehousefromdate);
create index fk7ychxphmo3yvikab4txsecg1h_originalsourcesystemidwhcd on arrangement.arrangementxproductsecuritytoken (originalsourcesystemid, warehousefromdate);
create index fk82ys481oxq2v892sryshuytj0_arrangementxproductidwhcd on arrangement.arrangementxproductsecuritytoken (arrangementxproductid, warehousefromdate);
create index fkf0liawyl1pg73vaqhl9077ty2_enterpriseidwhcd on arrangement.arrangementxproductsecuritytoken (enterpriseid, warehousefromdate);
create index fkkfajnpjwfuc4kt13avqmjs27i_securitytokenidwhcd on arrangement.arrangementxproductsecuritytoken (securitytokenid, warehousefromdate);
create index fknp4l2srl6e09175ngloxrexer_systemidwhcd on arrangement.arrangementxproductsecuritytoken (systemid, warehousefromdate);
create index fk10auu483ou8vross23ilkm601_classificationidwhcd on arrangement.arrangementxresourceitem (classificationid, warehousefromdate);
create index fk5m4kptkleha73alk03p761cau_resourceitemidwhcd on arrangement.arrangementxresourceitem (resourceitemid, warehousefromdate);
create index fked04m2i7mgfyfrsxu60cmf8or_systemidwhcd on arrangement.arrangementxresourceitem (systemid, warehousefromdate);
create index fkiu0pr9p6o39mwwfh9epgoahom_arrangementidwhcd on arrangement.arrangementxresourceitem (arrangementid, warehousefromdate);
create index fkpj4eok42bmisj8g6r7crx0er5_activeflagidwhcd on arrangement.arrangementxresourceitem (activeflagid, warehousefromdate);
create index fkpjx2bwph3f67laisvffrnopd8_originalsourcesystemidwhcd on arrangement.arrangementxresourceitem (originalsourcesystemid, warehousefromdate);
create index fkrc9mg4osgj3ye4dab54n96ttq_enterpriseidwhcd on arrangement.arrangementxresourceitem (enterpriseid, warehousefromdate);
create index fk4lwhxuk5gfc02ycby5kfro576_securitytokenidwhcd on arrangement.arrangementxresourceitemsecuritytoken (securitytokenid, warehousefromdate);
create index fk8bgg9lfof21l7yvjet4et71xj_activeflagidwhcd on arrangement.arrangementxresourceitemsecuritytoken (activeflagid, warehousefromdate);
create index fki6fgnt16ckmqi9y21s9a8gp47_arrangementxresourceitemidwhcd on arrangement.arrangementxresourceitemsecuritytoken (arrangementxresourceitemid, warehousefromdate);
create index fkn7ohgt9uwimsmg8b1kcclgymq_systemidwhcd on arrangement.arrangementxresourceitemsecuritytoken (systemid, warehousefromdate);
create index fkofagwms8gkw6geqmiyncnj2cx_originalsourcesystemidwhcd on arrangement.arrangementxresourceitemsecuritytoken (originalsourcesystemid, warehousefromdate);
create index fkotwjcy38jqowrom4f1t9u4sxv_enterpriseidwhcd on arrangement.arrangementxresourceitemsecuritytoken (enterpriseid, warehousefromdate);
create index fk3q3s3y2ypar6b6yao94qiavib_activeflagidwhcd on arrangement.arrangementxrules (activeflagid, warehousefromdate);
create index fkbc3k5pxxb18ylj1p929i1t740_enterpriseidwhcd on arrangement.arrangementxrules (enterpriseid, warehousefromdate);
create index fkf8qlkxh19e3pedt1xmnwe786o_originalsourcesystemidwhcd on arrangement.arrangementxrules (originalsourcesystemid, warehousefromdate);
create index fkl1taisa4493ad6n8troarks3c_arrangementidwhcd on arrangement.arrangementxrules (arrangementid, warehousefromdate);
create index fkqyb5d3jb3i9rsxm0cfo208njj_systemidwhcd on arrangement.arrangementxrules (systemid, warehousefromdate);
create index fkrt9gl1ke0yp5rgncnsr0t2x2m_classificationidwhcd on arrangement.arrangementxrules (classificationid, warehousefromdate);
create index fksp0nolvpdwpwmjyghyp486frp_rulesidwhcd on arrangement.arrangementxrules (rulesid, warehousefromdate);
create index fk33pq6ks4wh6ap23lm8cmv11e3_securitytokenidwhcd on arrangement.arrangementxrulessecuritytoken (securitytokenid, warehousefromdate);
create index fk62jqx0o1s2q6r66fn8nrjo4bf_originalsourcesystemidwhcd on arrangement.arrangementxrulessecuritytoken (originalsourcesystemid, warehousefromdate);
create index fk6tttie9brrhlxxh2adq02kffo_enterpriseidwhcd on arrangement.arrangementxrulessecuritytoken (enterpriseid, warehousefromdate);
create index fkc4a0rcrpg0hotxs39ny4ki4qw_activeflagidwhcd on arrangement.arrangementxrulessecuritytoken (activeflagid, warehousefromdate);
create index fkgeha0vtd9yo30vppd0an8kcyp_arrangementxrulesidwhcd on arrangement.arrangementxrulessecuritytoken (arrangementxrulesid, warehousefromdate);
create index fkhf5rvmrtd7mq9vm4obo2m68eh_systemidwhcd on arrangement.arrangementxrulessecuritytoken (systemid, warehousefromdate);
create index fk6343urwgxbykoau8p7h0i58gj_activeflagidwhcd on arrangement.arrangementxrulestype (activeflagid, warehousefromdate);
create index fk69m07mb95iafnyyk11228ihmc_rulestypeidwhcd on arrangement.arrangementxrulestype (rulestypeid, warehousefromdate);
create index fk6rx9646bu0edq22qa8nol81vv_originalsourcesystemidwhcd on arrangement.arrangementxrulestype (originalsourcesystemid, warehousefromdate);
create index fkcnkik4eq3xs4h6v2j13ul3rsv_enterpriseidwhcd on arrangement.arrangementxrulestype (enterpriseid, warehousefromdate);
create index fkd7koulxa7fid1reyw9nyhhuts_arrangementidwhcd on arrangement.arrangementxrulestype (arrangementid, warehousefromdate);
create index fkov2gqdwxiyblbpqmnwc4np7x9_classificationidwhcd on arrangement.arrangementxrulestype (classificationid, warehousefromdate);
create index fkq01c7ongg1cofa4ceibxduma2_systemidwhcd on arrangement.arrangementxrulestype (systemid, warehousefromdate);
create index fk11a4s30v3c7vosoqpy1210fhu_arrangementxrulestypeidwhcd on arrangement.arrangementxrulestypesecuritytoken (arrangementxrulestypeid, warehousefromdate);
create index fk3plf0o1h097qqbme256dh14ms_enterpriseidwhcd on arrangement.arrangementxrulestypesecuritytoken (enterpriseid, warehousefromdate);
create index fk51besipwxf9c4wsjao3x1b2y1_originalsourcesystemidwhcd on arrangement.arrangementxrulestypesecuritytoken (originalsourcesystemid, warehousefromdate);
create index fk533wd6s99uye7jfrvnscmp7h0_securitytokenidwhcd on arrangement.arrangementxrulestypesecuritytoken (securitytokenid, warehousefromdate);
create index fk5m5vtq233eq18lrww0cu5isj9_activeflagidwhcd on arrangement.arrangementxrulestypesecuritytoken (activeflagid, warehousefromdate);
create index fkfxhigbw4io92wri4583cu4v0l_systemidwhcd on arrangement.arrangementxrulestypesecuritytoken (systemid, warehousefromdate);

