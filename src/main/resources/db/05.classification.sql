CREATE TABLE classification.classification
(
    classificationid              UUID                        NOT NULL primary key,
    effectivefromdate             timestamp(6) with time zone NOT NULL,
    effectivetodate               timestamp(6) with time zone NOT NULL,
    warehousecreatedtimestamp     timestamp(6) with time zone NOT NULL,
    warehousefromdate             DATE                        NOT NULL,

    warehouselastupdatedtimestamp timestamp(6) with time zone NOT NULL,
    originalsourcesystemuniqueid  UUID                        NOT NULL,
    classificationsequencenumber  integer                     NOT NULL,
    classificationdesc            character varying(500)      NOT NULL,
    classificationname            character varying(100)      NOT NULL,
    activeflagid                  UUID                        NOT NULL,
    enterpriseid                  UUID                        NOT NULL,
    systemid                      UUID                        NOT NULL,
    originalsourcesystemid        UUID                        NOT NULL,
    classificationdataconceptid   UUID                        NOT NULL
);

CREATE TABLE classification.classificationsecuritytoken
(
    classificationsecuritytokenid UUID                        NOT NULL primary key,
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
    classificationid              UUID                        NOT NULL
);

CREATE TABLE classification.classificationxclassification
(
    classificationxclassificationid UUID                        NOT NULL primary key,
    effectivefromdate               timestamp(6) with time zone NOT NULL,
    effectivetodate                 timestamp(6) with time zone NOT NULL,
    warehousecreatedtimestamp       timestamp(6) with time zone NOT NULL,
    warehousefromdate               DATE                        NOT NULL,

    warehouselastupdatedtimestamp   timestamp(6) with time zone NOT NULL,
    originalsourcesystemuniqueid    UUID                        NOT NULL,
    value                           text                        NOT NULL,
    activeflagid                    UUID                        NOT NULL,
    enterpriseid                    UUID                        NOT NULL,
    systemid                        UUID                        NOT NULL,
    originalsourcesystemid          UUID                        NOT NULL,
    classificationid                UUID                        NOT NULL,
    childclassificationid           UUID                        NOT NULL,
    parentclassificationid          UUID                        NOT NULL
);

CREATE TABLE classification.classificationxclassificationsecuritytoken
(
    classificationxclassificationsecuritytokenid UUID                        NOT NULL primary key,
    effectivefromdate                            timestamp(6) with time zone NOT NULL,
    effectivetodate                              timestamp(6) with time zone NOT NULL,
    warehousecreatedtimestamp                    timestamp(6) with time zone NOT NULL,
    warehousefromdate                            DATE                        NOT NULL,

    warehouselastupdatedtimestamp                timestamp(6) with time zone NOT NULL,
    createallowed                                INTEGER                     NOT NULL,
    deleteallowed                                INTEGER                     NOT NULL,
    originalsourcesystemuniqueid                 UUID                        NOT NULL,
    readallowed                                  INTEGER                     NOT NULL,
    updateallowed                                INTEGER                     NOT NULL,
    activeflagid                                 UUID                        NOT NULL,
    enterpriseid                                 UUID                        NOT NULL,
    originalsourcesystemid                       UUID                        NOT NULL,
    securitytokenid                              UUID                        NOT NULL,
    systemid                                     UUID                        NOT NULL,
    classificationxclassificationid              UUID                        NOT NULL
);

CREATE TABLE classification.classificationxresourceitem
(
    classificationxresourceitemid UUID                        NOT NULL primary key,
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
    resourceitemid                UUID                        NOT NULL
);

CREATE TABLE classification.classificationxresourceitemsecuritytoken
(
    classificationxresourceitemsecuritytokenid UUID                        NOT NULL primary key,
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
    classificationxresourceitemid              UUID                        NOT NULL
);


create index fk8cywgvl24kx13sapgd5ppnxgn_activeflagid on classification.classification (activeflagid);
create index fk977a6joatob09p0ati780wx3i_originalsourcesystemid on classification.classification (originalsourcesystemid);
create index fkdameu9rwq0ndi6g9pnasalwf2_systemid on classification.classification (systemid);
create index fkradegxgr2qgtrmpcnr280wrow_enterpriseid on classification.classification (enterpriseid);
create index fkuyay717baco8bei1qoxcauux_classificationdataconceptid on classification.classification (classificationdataconceptid);
create index fk3pyb4fg9krm6cs8gyymn96d1b_classificationid on classification.classificationsecuritytoken (classificationid);
create index fkalw5xpd22jfxd5le05m2baego_originalsourcesystemid on classification.classificationsecuritytoken (originalsourcesystemid);
create index fkarnkwlgj9y3433pngmhg39j5f_systemid on classification.classificationsecuritytoken (systemid);
create index fkpgoqkscc2c9ynucppx7kqfq17_securitytokenid on classification.classificationsecuritytoken (securitytokenid);
create index fktb0dfsliwpcbdw26ivstchoxa_enterpriseid on classification.classificationsecuritytoken (enterpriseid);
create index fkxo8jweipcwyhjky1sj6asi70_activeflagid on classification.classificationsecuritytoken (activeflagid);
create index fk5lxvsymahaue1a1gx87ll8w9t_systemid on classification.classificationxclassification (systemid);
create index fka0edqa9iht684osp3290grh5j_classificationid on classification.classificationxclassification (classificationid);
create index fkbutwoudrgvo3cbjn39cd3ftn1_parentclassificationid on classification.classificationxclassification (parentclassificationid);
create index fkgt5llwl8oh4q2fo4fv9vfmui2_activeflagid on classification.classificationxclassification (activeflagid);
create index fklolls1qwf58fhw1lo6plg1u2d_childclassificationid on classification.classificationxclassification (childclassificationid);
create index fkmdyinn1kd9lrjajlts2832kgp_enterpriseid on classification.classificationxclassification (enterpriseid);
create index fkng5lu9wyocw42hm2yq6966eic_originalsourcesystemid on classification.classificationxclassification (originalsourcesystemid);
create index fk382so754rj84e0moqjj7q6cmd_classificationxclassificationid on classification.classificationxclassificationsecuritytoken (classificationxclassificationid);
create index fkidlqquwljda1kte0kjjjkkopi_activeflagid on classification.classificationxclassificationsecuritytoken (activeflagid);
create index fkktsowuwqxuk0snthieryx7pny_enterpriseid on classification.classificationxclassificationsecuritytoken (enterpriseid);
create index fkovne1f69w7lejinl9o202wq8r_originalsourcesystemid on classification.classificationxclassificationsecuritytoken (originalsourcesystemid);
create index fkp0uayhfmnxp6mbojs6b906uo1_systemid on classification.classificationxclassificationsecuritytoken (systemid);
create index fktgfix5de1n30m7rvhxrb1yel2_securitytokenid on classification.classificationxclassificationsecuritytoken (securitytokenid);
create index fk41ad965y444vlu7ib0yr8qf96_systemid on classification.classificationxresourceitem (systemid);
create index fkg9sk8egvr7m48ajxuq08kr36a_enterpriseid on classification.classificationxresourceitem (enterpriseid);
create index fkgm9umwfpdxs1ao0jb2ye8t4qi_classificationid on classification.classificationxresourceitem (classificationid);
create index fkisdanawdo1rw8v7t1jks92pgf_activeflagid on classification.classificationxresourceitem (activeflagid);
create index fklhxbcwpk3r33hdnpbirj5q8e0_resourceitemid on classification.classificationxresourceitem (resourceitemid);
create index fkq78r34v2adpvnc8u50fdo2mth_originalsourcesystemid on classification.classificationxresourceitem (originalsourcesystemid);
create index fk202whr6s26bncotttl943yl80_activeflagid on classification.classificationxresourceitemsecuritytoken (activeflagid);
create index fkmxlv95qskrx8l20xdjs55gxwy_classificationxresourceitemid on classification.classificationxresourceitemsecuritytoken (classificationxresourceitemid);
create index fknudqvddewc67sunrqr4wnh0aq_systemid on classification.classificationxresourceitemsecuritytoken (systemid);
create index fkpkswoxe9sd4fcqdc735yo80kc_securitytokenid on classification.classificationxresourceitemsecuritytoken (securitytokenid);
create index fkqof2ui2s3pfau54hqtylrpbcf_enterpriseid on classification.classificationxresourceitemsecuritytoken (enterpriseid);
create index fkt2qwo9050jpmg0k59is420r1h_originalsourcesystemid on classification.classificationxresourceitemsecuritytoken (originalsourcesystemid);

CREATE INDEX idx_classificationxresourceitemsecuritytoken_effectivefromdate ON classification.classificationxresourceitemsecuritytoken (effectivefromdate);
CREATE INDEX idx_classificationxresourceitemsecuritytoken_effectivetodate ON classification.classificationxresourceitemsecuritytoken (effectivetodate);
CREATE INDEX idx_classificationxresourceitemsecuritytoken_warehousecreatedt ON classification.classificationxresourceitemsecuritytoken (warehousecreatedtimestamp);
CREATE INDEX idx_classificationxresourceitemsecuritytoken_warehouselastupda ON classification.classificationxresourceitemsecuritytoken (warehouselastupdatedtimestamp);

CREATE INDEX idx_classificationsecuritytoken_effectivefromdate ON classification.classificationsecuritytoken (effectivefromdate);
CREATE INDEX idx_classificationsecuritytoken_effectivetodate ON classification.classificationsecuritytoken (effectivetodate);
CREATE INDEX idx_classificationsecuritytoken_warehousecreatedtimestamp ON classification.classificationsecuritytoken (warehousecreatedtimestamp);
CREATE INDEX idx_classificationsecuritytoken_warehouselastupdatedtimestamp ON classification.classificationsecuritytoken (warehouselastupdatedtimestamp);
CREATE INDEX idx_classification_effectivefromdate ON classification.classification (effectivefromdate);
CREATE INDEX idx_classification_effectivetodate ON classification.classification (effectivetodate);
CREATE INDEX idx_classification_warehousecreatedtimestamp ON classification.classification (warehousecreatedtimestamp);
CREATE INDEX idx_classification_warehouselastupdatedtimestamp ON classification.classification (warehouselastupdatedtimestamp);
CREATE INDEX idx_classificationxclassification_effectivefromdate ON classification.classificationxclassification (effectivefromdate);
CREATE INDEX idx_classificationxclassification_effectivetodate ON classification.classificationxclassification (effectivetodate);
CREATE INDEX idx_classificationxclassification_warehousecreatedtimestamp ON classification.classificationxclassification (warehousecreatedtimestamp);
CREATE INDEX idx_classificationxclassification_warehouselastupdatedtimestam ON classification.classificationxclassification (warehouselastupdatedtimestamp);
CREATE INDEX idx_classificationxclassificationsecuritytoken_effectivefromda ON classification.classificationxclassificationsecuritytoken (effectivefromdate);
CREATE INDEX idx_classificationxclassificationsecuritytoken_effectivetodate ON classification.classificationxclassificationsecuritytoken (effectivetodate);
CREATE INDEX idx_classificationxclassificationsecuritytoken_warehousecreate ON classification.classificationxclassificationsecuritytoken (warehousecreatedtimestamp);
CREATE INDEX idx_classificationxclassificationsecuritytoken_warehouselastup ON classification.classificationxclassificationsecuritytoken (warehouselastupdatedtimestamp);
CREATE INDEX idx_classificationxresourceitem_effectivefromdate ON classification.classificationxresourceitem (effectivefromdate);
CREATE INDEX idx_classificationxresourceitem_effectivetodate ON classification.classificationxresourceitem (effectivetodate);
CREATE INDEX idx_classificationxresourceitem_warehousecreatedtimestamp ON classification.classificationxresourceitem (warehousecreatedtimestamp);
CREATE INDEX idx_classificationxresourceitem_warehouselastupdatedtimestamp ON classification.classificationxresourceitem (warehouselastupdatedtimestamp);

CREATE INDEX idx_classificationxclassification_value ON classification.classificationxclassification (value);
CREATE INDEX idx_classificationxresourceitem_value ON classification.classificationxresourceitem (value);

CREATE INDEX idx_classification_classificationdesc ON classification.classification (classificationdesc);
CREATE INDEX idx_classification_classificationname ON classification.classification (classificationname);

create index fk8cywgvl24kx13sapgd5ppnxgn_activeflagidwhcd on classification.classification (activeflagid, warehousefromdate);
create index fk977a6joatob09p0ati780wx3i_originalsourcesystemidwhcd on classification.classification (originalsourcesystemid, warehousefromdate);
create index fkdameu9rwq0ndi6g9pnasalwf2_systemidwhcd on classification.classification (systemid, warehousefromdate);
create index fkradegxgr2qgtrmpcnr280wrow_enterpriseidwhcd on classification.classification (enterpriseid, warehousefromdate);
create index fkuyay717baco8bei1qoxcauux_classificationdataconceptidwhcd on classification.classification (classificationdataconceptid, warehousefromdate);
create index fk3pyb4fg9krm6cs8gyymn96d1b_classificationidwhcd on classification.classificationsecuritytoken (classificationid, warehousefromdate);
create index fkalw5xpd22jfxd5le05m2baego_originalsourcesystemidwhcd on classification.classificationsecuritytoken (originalsourcesystemid, warehousefromdate);
create index fkarnkwlgj9y3433pngmhg39j5f_systemidwhcd on classification.classificationsecuritytoken (systemid, warehousefromdate);
create index fkpgoqkscc2c9ynucppx7kqfq17_securitytokenidwhcd on classification.classificationsecuritytoken (securitytokenid, warehousefromdate);
create index fktb0dfsliwpcbdw26ivstchoxa_enterpriseidwhcd on classification.classificationsecuritytoken (enterpriseid, warehousefromdate);
create index fkxo8jweipcwyhjky1sj6asi70_activeflagidwhcd on classification.classificationsecuritytoken (activeflagid, warehousefromdate);
create index fk5lxvsymahaue1a1gx87ll8w9t_systemidwhcd on classification.classificationxclassification (systemid, warehousefromdate);
create index fka0edqa9iht684osp3290grh5j_classificationidwhcd on classification.classificationxclassification (classificationid, warehousefromdate);
create index fkbutwoudrgvo3cbjn39cd3ftn1_parentclassificationidwhcd on classification.classificationxclassification (parentclassificationid, warehousefromdate);
create index fkgt5llwl8oh4q2fo4fv9vfmui2_activeflagidwhcd on classification.classificationxclassification (activeflagid, warehousefromdate);
create index fklolls1qwf58fhw1lo6plg1u2d_childclassificationidwhcd on classification.classificationxclassification (childclassificationid, warehousefromdate);
create index fkmdyinn1kd9lrjajlts2832kgp_enterpriseidwhcd on classification.classificationxclassification (enterpriseid, warehousefromdate);
create index fkng5lu9wyocw42hm2yq6966eic_originalsourcesystemidwhcd on classification.classificationxclassification (originalsourcesystemid, warehousefromdate);
create index fk382so754rj84e0moqjj7q6cmd_classificationxclassificationidwhcd on classification.classificationxclassificationsecuritytoken (classificationxclassificationid, warehousefromdate);
create index fkidlqquwljda1kte0kjjjkkopi_activeflagidwhcd on classification.classificationxclassificationsecuritytoken (activeflagid, warehousefromdate);
create index fkktsowuwqxuk0snthieryx7pny_enterpriseidwhcd on classification.classificationxclassificationsecuritytoken (enterpriseid, warehousefromdate);
create index fkovne1f69w7lejinl9o202wq8r_originalsourcesystemidwhcd on classification.classificationxclassificationsecuritytoken (originalsourcesystemid, warehousefromdate);
create index fkp0uayhfmnxp6mbojs6b906uo1_systemidwhcd on classification.classificationxclassificationsecuritytoken (systemid, warehousefromdate);
create index fktgfix5de1n30m7rvhxrb1yel2_securitytokenidwhcd on classification.classificationxclassificationsecuritytoken (securitytokenid, warehousefromdate);
create index fk41ad965y444vlu7ib0yr8qf96_systemidwhcd on classification.classificationxresourceitem (systemid, warehousefromdate);
create index fkg9sk8egvr7m48ajxuq08kr36a_enterpriseidwhcd on classification.classificationxresourceitem (enterpriseid, warehousefromdate);
create index fkgm9umwfpdxs1ao0jb2ye8t4qi_classificationidwhcd on classification.classificationxresourceitem (classificationid, warehousefromdate);
create index fkisdanawdo1rw8v7t1jks92pgf_activeflagidwhcd on classification.classificationxresourceitem (activeflagid, warehousefromdate);
create index fklhxbcwpk3r33hdnpbirj5q8e0_resourceitemidwhcd on classification.classificationxresourceitem (resourceitemid, warehousefromdate);
create index fkq78r34v2adpvnc8u50fdo2mth_originalsourcesystemidwhcd on classification.classificationxresourceitem (originalsourcesystemid, warehousefromdate);
create index fk202whr6s26bncotttl943yl80_activeflagidwhcd on classification.classificationxresourceitemsecuritytoken (activeflagid, warehousefromdate);
create index fkmxlv95qskrx8l20xdjs55gxwy_classificationxresourceitemidwhcd on classification.classificationxresourceitemsecuritytoken (classificationxresourceitemid, warehousefromdate);
create index fknudqvddewc67sunrqr4wnh0aq_systemidwhcd on classification.classificationxresourceitemsecuritytoken (systemid, warehousefromdate);
create index fkpkswoxe9sd4fcqdc735yo80kc_securitytokenidwhcd on classification.classificationxresourceitemsecuritytoken (securitytokenid, warehousefromdate);
create index fkqof2ui2s3pfau54hqtylrpbcf_enterpriseidwhcd on classification.classificationxresourceitemsecuritytoken (enterpriseid, warehousefromdate);
create index fkt2qwo9050jpmg0k59is420r1h_originalsourcesystemidwhcd on classification.classificationxresourceitemsecuritytoken (originalsourcesystemid, warehousefromdate);

CREATE INDEX IF NOT EXISTS classification_name_idx
    ON classification.Classification (ClassificationName)
    INCLUDE (ClassificationID, ActiveFlagID, EffectiveFromDate, EffectiveToDate);


