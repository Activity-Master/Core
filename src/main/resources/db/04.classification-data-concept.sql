CREATE SCHEMA classification;
CREATE TABLE classification.classificationdataconcept
(
    classificationdataconceptid   UUID                        NOT NULL primary key,
    effectivefromdate             timestamp(6) with time zone NOT NULL,
    effectivetodate               timestamp(6) with time zone NOT NULL,
    warehousecreatedtimestamp     timestamp(6) with time zone NOT NULL,
    warehousefromdate             DATE                        NOT NULL,

    warehouselastupdatedtimestamp timestamp(6) with time zone NOT NULL,
    originalsourcesystemuniqueid  UUID                        NOT NULL,
    classificationdataconceptdesc character varying(1500)     NOT NULL,
    classificationdataconceptname character varying(100)      NOT NULL,
    activeflagid                  UUID                        NOT NULL,
    enterpriseid                  UUID                        NOT NULL,
    systemid                      UUID                        NOT NULL,
    originalsourcesystemid        UUID                        NOT NULL
);
CREATE TABLE classification.classificationdataconceptsecuritytoken
(
    classificationdataconceptsecuritytokenid UUID                        NOT NULL primary key,
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
    classificationdataconceptid              UUID                        NOT NULL
);
CREATE TABLE classification.classificationdataconceptxclassification
(
    classificationdataconceptxclassificationid UUID                        NOT NULL primary key,
    effectivefromdate                          timestamp(6) with time zone NOT NULL,
    effectivetodate                            timestamp(6) with time zone NOT NULL,
    warehousecreatedtimestamp                  timestamp(6) with time zone NOT NULL,

    warehousefromdate                          DATE                        NOT NULL,

    warehouselastupdatedtimestamp              timestamp(6) with time zone NOT NULL,
    originalsourcesystemuniqueid               UUID                        NOT NULL,
    value                                      text                        NOT NULL,
    activeflagid                               UUID                        NOT NULL,
    enterpriseid                               UUID                        NOT NULL,
    systemid                                   UUID                        NOT NULL,
    originalsourcesystemid                     UUID                        NOT NULL,
    classificationid                           UUID                        NOT NULL,
    classificationdataconceptid                UUID                        NOT NULL
);
CREATE TABLE classification.classificationdataconceptxclassificationsecuritytoken
(
    classificationdataconceptxclassificationsecuritytokenid UUID                        NOT NULL primary key,
    effectivefromdate                                       timestamp(6) with time zone NOT NULL,
    effectivetodate                                         timestamp(6) with time zone NOT NULL,
    warehousecreatedtimestamp                               timestamp(6) with time zone NOT NULL,
    warehousefromdate                                       DATE                        NOT NULL,

    warehouselastupdatedtimestamp                           timestamp(6) with time zone NOT NULL,
    createallowed                                           INTEGER                     NOT NULL,
    deleteallowed                                           INTEGER                     NOT NULL,
    originalsourcesystemuniqueid                            UUID                        NOT NULL,
    readallowed                                             INTEGER                     NOT NULL,
    updateallowed                                           INTEGER                     NOT NULL,
    activeflagid                                            UUID                        NOT NULL,
    enterpriseid                                            UUID                        NOT NULL,
    originalsourcesystemid                                  UUID                        NOT NULL,
    securitytokenid                                         UUID                        NOT NULL,
    systemid                                                UUID                        NOT NULL,
    classificationdataconceptxclassificationid              UUID                        NOT NULL
);
CREATE TABLE classification.classificationdataconceptxresourceitem
(
    classificationdataconceptxresourceitemid UUID                        NOT NULL primary key,
    effectivefromdate                        timestamp(6) with time zone NOT NULL,
    effectivetodate                          timestamp(6) with time zone NOT NULL,
    warehousecreatedtimestamp                timestamp(6) with time zone NOT NULL,
    warehousefromdate                        DATE                        NOT NULL,

    warehouselastupdatedtimestamp            timestamp(6) with time zone NOT NULL,
    originalsourcesystemuniqueid             UUID                        NOT NULL,
    value                                    text                        NOT NULL,
    activeflagid                             UUID                        NOT NULL,
    enterpriseid                             UUID                        NOT NULL,
    systemid                                 UUID                        NOT NULL,
    originalsourcesystemid                   UUID                        NOT NULL,
    classificationid                         UUID                        NOT NULL,
    classificationdataconceptid              UUID                        NOT NULL,
    resourceitemid                           UUID                        NOT NULL
);
CREATE TABLE classification.classificationdataconceptxresourceitemsecuritytoken
(
    classificationdataconceptxresourceitemsecuritytokenid UUID                        NOT NULL primary key,
    effectivefromdate                                     timestamp(6) with time zone NOT NULL,
    effectivetodate                                       timestamp(6) with time zone NOT NULL,
    warehousecreatedtimestamp                             timestamp(6) with time zone NOT NULL,
    warehousefromdate                                     DATE                        NOT NULL,

    warehouselastupdatedtimestamp                         timestamp(6) with time zone NOT NULL,
    createallowed                                         INTEGER                     NOT NULL,
    deleteallowed                                         INTEGER                     NOT NULL,
    originalsourcesystemuniqueid                          UUID                        NOT NULL,
    readallowed                                           INTEGER                     NOT NULL,
    updateallowed                                         INTEGER                     NOT NULL,
    activeflagid                                          UUID                        NOT NULL,
    enterpriseid                                          UUID                        NOT NULL,
    originalsourcesystemid                                UUID                        NOT NULL,
    securitytokenid                                       UUID                        NOT NULL,
    systemid                                              UUID                        NOT NULL,
    classificationdataconceptxresourceitemid              UUID                        NOT NULL
);
create index fk7anrd6m4u7jmgosom1ov3sup6_systemid on classification.classificationdataconcept (systemid);
create index fkjjkh1tgw58ebgjyfhs55uq0bc_enterpriseid on classification.classificationdataconcept (enterpriseid);
create index fknq22rqtgtemjf9w2iwanl89im_originalsourcesystemid on classification.classificationdataconcept (originalsourcesystemid);
create index fkq95ppsctlqk23bbukl11jfrk9_activeflagid on classification.classificationdataconcept (activeflagid);
create index fk104e6i5qwqmf9codleb3r0sq8_systemid on classification.classificationdataconceptsecuritytoken (systemid);
create index fka96umbw2pt2xsr7dpr7rg59sb_activeflagid on classification.classificationdataconceptsecuritytoken (activeflagid);
create index fkaet7sbnxoa25lvh4fttjxq241_classificationdataconceptid on classification.classificationdataconceptsecuritytoken (classificationdataconceptid);
create index fkdj2s9qftq2ybskhst5rh7kvcs_originalsourcesystemid on classification.classificationdataconceptsecuritytoken (originalsourcesystemid);
create index fko5u4q6stb4e4gp0hfwttgpoof_enterpriseid on classification.classificationdataconceptsecuritytoken (enterpriseid);
create index fks2ygkvtk17e901wed7fjjio7v_securitytokenid on classification.classificationdataconceptsecuritytoken (securitytokenid);
create index fk4wh9c6d6snotj0cdns7wdojty_classificationdataconceptid on classification.classificationdataconceptxclassification (classificationdataconceptid);
create index fk6p7cqsuyxor559t3sfbxvsrsh_activeflagid on classification.classificationdataconceptxclassification (activeflagid);
create index fk8ri5ojfslqput3614qfxh89ko_enterpriseid on classification.classificationdataconceptxclassification (enterpriseid);
create index fkalb9m4tw81jr1nrghxwnhgbsm_systemid on classification.classificationdataconceptxclassification (systemid);
create index fkbkxw52ao5d3w2lww1wcgwc6r8_classificationid on classification.classificationdataconceptxclassification (classificationid);
create index fkptvfaytvks9cf4qvb1ch0nc5q_originalsourcesystemid on classification.classificationdataconceptxclassification (originalsourcesystemid);
create index fk11pvel7kmbcntj5cllihder1x_activeflagid on classification.classificationdataconceptxclassificationsecuritytoken (activeflagid);
create index fk1x5i621v0xt7a60cvh4w88fu5_claconceptxclassificationid on classification.classificationdataconceptxclassificationsecuritytoken (classificationdataconceptxclassificationid);
create index fk60x6yebwf17dn491shf6aobv2_enterpriseid on classification.classificationdataconceptxclassificationsecuritytoken (enterpriseid);
create index fk86t8j5lcn4fxi8km9e839mn76_securitytokenid on classification.classificationdataconceptxclassificationsecuritytoken (securitytokenid);
create index fkglkin1i2uv5gp9dlk1sbgmulg_systemid on classification.classificationdataconceptxclassificationsecuritytoken (systemid);
create index fkqd86yl64ly9efrkoqoo5g6d3a_originalsourcesystemid on classification.classificationdataconceptxclassificationsecuritytoken (originalsourcesystemid);
create index fk26e3u9h3fcxpwbormhncoee4p_enterpriseid on classification.classificationdataconceptxresourceitem (enterpriseid);
create index fk70uhig9qru3lmqnjq9yfgsddn_systemid on classification.classificationdataconceptxresourceitem (systemid);
create index fk7cngjs2bvw18p1j29fg62svn3_resourceitemid on classification.classificationdataconceptxresourceitem (resourceitemid);
create index fkb3i6n3mk43lmc47412g00l07p_classificationdataconceptid on classification.classificationdataconceptxresourceitem (classificationdataconceptid);
create index fkbxuns49b2h4ymjifep6bup0y7_classificationid on classification.classificationdataconceptxresourceitem (classificationid);
create index fkk1tllh3pw17bo91f98qajwv18_activeflagid on classification.classificationdataconceptxresourceitem (activeflagid);
create index fkm5et8roankenbjliy9qigclab_originalsourcesystemid on classification.classificationdataconceptxresourceitem (originalsourcesystemid);
create index fk18b70ps0o5g4y7eo5awrlls3v_clanctxresourceitemid on classification.classificationdataconceptxresourceitemsecuritytoken (classificationdataconceptxresourceitemid);
create index fk31ifcku5xuo9j2f7nyvsheivg_enterpriseid on classification.classificationdataconceptxresourceitemsecuritytoken (enterpriseid);
create index fkk0lwglw1ekrh07d1rp0qnt3v9_originalsourcesystemid on classification.classificationdataconceptxresourceitemsecuritytoken (originalsourcesystemid);
create index fkohnmet2w8qiqs1tqgp3bvaj8s_securitytokenid on classification.classificationdataconceptxresourceitemsecuritytoken (securitytokenid);
create index fkrnilsvgvwpcbv8fs2yr1p87ei_systemid on classification.classificationdataconceptxresourceitemsecuritytoken (systemid);
create index fkrq8gtmddaq83y0vq5v5mvun3s_activeflagid on classification.classificationdataconceptxresourceitemsecuritytoken (activeflagid);
CREATE INDEX idx_classificationdataconceptxclassification_effectivefromdate ON classification.classificationdataconceptxclassification (effectivefromdate);
CREATE INDEX idx_classificationdataconceptxclassification_effectivetodate ON classification.classificationdataconceptxclassification (effectivetodate);
CREATE INDEX idx_classificationdataconceptxclassification_warehousecreatedt ON classification.classificationdataconceptxclassification (warehousecreatedtimestamp);
CREATE INDEX idx_classificationdataconceptxclassification_warehouselastupda ON classification.classificationdataconceptxclassification (warehouselastupdatedtimestamp);
CREATE INDEX idx_classificationdataconcept_effectivefromdate ON classification.classificationdataconcept (effectivefromdate);
CREATE INDEX idx_classificationdataconcept_effectivetodate ON classification.classificationdataconcept (effectivetodate);
CREATE INDEX idx_classificationdataconcept_warehousecreatedtimestamp ON classification.classificationdataconcept (warehousecreatedtimestamp);
CREATE INDEX idx_classificationdataconcept_warehouselastupdatedtimestamp ON classification.classificationdataconcept (warehouselastupdatedtimestamp);

CREATE INDEX idx_classificationdataconceptsecuritytoken_effectivefromdate ON classification.classificationdataconceptsecuritytoken (effectivefromdate);
CREATE INDEX idx_classificationdataconceptsecuritytoken_effectivetodate ON classification.classificationdataconceptsecuritytoken (effectivetodate);
CREATE INDEX idx_classificationdataconceptsecuritytoken_warehousecreatedtim ON classification.classificationdataconceptsecuritytoken (warehousecreatedtimestamp);
CREATE INDEX idx_classificationdataconceptsecuritytoken_warehouselastupdate ON classification.classificationdataconceptsecuritytoken (warehouselastupdatedtimestamp);
CREATE INDEX idx_classificationdataconceptxclassificationsecuritytoken_effe ON classification.classificationdataconceptxclassificationsecuritytoken (effectivefromdate);
CREATE INDEX idx_classificationdataconceptxclassificationsecuritytoken_ware ON classification.classificationdataconceptxclassificationsecuritytoken (warehousecreatedtimestamp);
CREATE INDEX idx_classificationdataconceptxresourceitem_effectivefromdate ON classification.classificationdataconceptxresourceitem (effectivefromdate);
CREATE INDEX idx_classificationdataconceptxresourceitem_effectivetodate ON classification.classificationdataconceptxresourceitem (effectivetodate);
CREATE INDEX idx_classificationdataconceptxresourceitem_warehousecreatedtim ON classification.classificationdataconceptxresourceitem (warehousecreatedtimestamp);
CREATE INDEX idx_classificationdataconceptxresourceitem_warehouselastupdate ON classification.classificationdataconceptxresourceitem (warehouselastupdatedtimestamp);

CREATE INDEX idx_classificationdataconceptxresourceitemsecuritytoken_effect ON classification.classificationdataconceptxresourceitemsecuritytoken (effectivefromdate);
CREATE INDEX idx_classificationdataconceptxresourceitemsecuritytoken_wareho ON classification.classificationdataconceptxresourceitemsecuritytoken (warehousecreatedtimestamp);

CREATE INDEX idx_classificationdataconceptxclassification_value ON classification.classificationdataconceptxclassification (value);
CREATE INDEX idx_classificationdataconceptxresourceitem_value ON classification.classificationdataconceptxresourceitem (value);
CREATE INDEX idx_classificationdataconcept_classificationdataconceptdesc ON classification.classificationdataconcept (classificationdataconceptdesc);
CREATE INDEX idx_classificationdataconcept_classificationdataconceptname ON classification.classificationdataconcept (classificationdataconceptname);

create index fk7anrd6m4u7jmgosom1ov3sup6_systemidwhcd on classification.classificationdataconcept (systemid, warehousefromdate);
create index fkjjkh1tgw58ebgjyfhs55uq0bc_enterpriseidwhcd on classification.classificationdataconcept (enterpriseid, warehousefromdate);
create index fknq22rqtgtemjf9w2iwanl89im_originalsourcesystemidwhcd on classification.classificationdataconcept (originalsourcesystemid, warehousefromdate);
create index fkq95ppsctlqk23bbukl11jfrk9_activeflagidwhcd on classification.classificationdataconcept (activeflagid, warehousefromdate);
create index fk104e6i5qwqmf9codleb3r0sq8_systemidwhcd on classification.classificationdataconceptsecuritytoken (systemid, warehousefromdate);
create index fka96umbw2pt2xsr7dpr7rg59sb_activeflagidwhcd on classification.classificationdataconceptsecuritytoken (activeflagid, warehousefromdate);
create index fkaet7sbnxoa25lvh4fttjxq241_classificationdataconceptidwhcd on classification.classificationdataconceptsecuritytoken (classificationdataconceptid, warehousefromdate);
create index fkdj2s9qftq2ybskhst5rh7kvcs_originalsourcesystemidwhcd on classification.classificationdataconceptsecuritytoken (originalsourcesystemid, warehousefromdate);
create index fko5u4q6stb4e4gp0hfwttgpoof_enterpriseidwhcd on classification.classificationdataconceptsecuritytoken (enterpriseid, warehousefromdate);
create index fks2ygkvtk17e901wed7fjjio7v_securitytokenidwhcd on classification.classificationdataconceptsecuritytoken (securitytokenid, warehousefromdate);
create index fk4wh9c6d6snotj0cdns7wdojty_classificationdataconceptidwhcd on classification.classificationdataconceptxclassification (classificationdataconceptid, warehousefromdate);
create index fk6p7cqsuyxor559t3sfbxvsrsh_activeflagidwhcd on classification.classificationdataconceptxclassification (activeflagid, warehousefromdate);
create index fk8ri5ojfslqput3614qfxh89ko_enterpriseidwhcd on classification.classificationdataconceptxclassification (enterpriseid, warehousefromdate);
create index fkalb9m4tw81jr1nrghxwnhgbsm_systemidwhcd on classification.classificationdataconceptxclassification (systemid, warehousefromdate);
create index fkbkxw52ao5d3w2lww1wcgwc6r8_classificationidwhcd on classification.classificationdataconceptxclassification (classificationid, warehousefromdate);
create index fkptvfaytvks9cf4qvb1ch0nc5q_originalsourcesystemidwhcd on classification.classificationdataconceptxclassification (originalsourcesystemid, warehousefromdate);
create index fk11pvel7kmbcntj5cllihder1x_activeflagidwhcd on classification.classificationdataconceptxclassificationsecuritytoken (activeflagid, warehousefromdate);
create index fk1x5i621v0xt7a60cvh4w88fu5_classificationdatacononidwhcd on classification.classificationdataconceptxclassificationsecuritytoken (classificationdataconceptxclassificationid, warehousefromdate);
create index fk60x6yebwf17dn491shf6aobv2_enterpriseidwhcd on classification.classificationdataconceptxclassificationsecuritytoken (enterpriseid, warehousefromdate);
create index fk86t8j5lcn4fxi8km9e839mn76_securitytokenidwhcd on classification.classificationdataconceptxclassificationsecuritytoken (securitytokenid, warehousefromdate);
create index fkglkin1i2uv5gp9dlk1sbgmulg_systemidwhcd on classification.classificationdataconceptxclassificationsecuritytoken (systemid, warehousefromdate);
create index fkqd86yl64ly9efrkoqoo5g6d3a_originalsourcesystemidwhcd on classification.classificationdataconceptxclassificationsecuritytoken (originalsourcesystemid, warehousefromdate);
create index fk26e3u9h3fcxpwbormhncoee4p_enterpriseidwhcd on classification.classificationdataconceptxresourceitem (enterpriseid, warehousefromdate);
create index fk70uhig9qru3lmqnjq9yfgsddn_systemidwhcd on classification.classificationdataconceptxresourceitem (systemid, warehousefromdate);
create index fk7cngjs2bvw18p1j29fg62svn3_resourceitemidwhcd on classification.classificationdataconceptxresourceitem (resourceitemid, warehousefromdate);
create index fkb3i6n3mk43lmc47412g00l07p_classificationdataconceptidwhcd on classification.classificationdataconceptxresourceitem (classificationdataconceptid, warehousefromdate);
create index fkbxuns49b2h4ymjifep6bup0y7_classificationidwhcd on classification.classificationdataconceptxresourceitem (classificationid, warehousefromdate);
create index fkk1tllh3pw17bo91f98qajwv18_activeflagidwhcd on classification.classificationdataconceptxresourceitem (activeflagid, warehousefromdate);
create index fkm5et8roankenbjliy9qigclab_originalsourcesystemidwhcd on classification.classificationdataconceptxresourceitem (originalsourcesystemid, warehousefromdate);
create index fk18b70ps0o5g4y7eo5awrlls3v_classificationdataconceitemidwhcd on classification.classificationdataconceptxresourceitemsecuritytoken (classificationdataconceptxresourceitemid, warehousefromdate);
create index fk31ifcku5xuo9j2f7nyvsheivg_enterpriseidwhcd on classification.classificationdataconceptxresourceitemsecuritytoken (enterpriseid, warehousefromdate);
create index fkk0lwglw1ekrh07d1rp0qnt3v9_originalsourcesystemidwhcd on classification.classificationdataconceptxresourceitemsecuritytoken (originalsourcesystemid, warehousefromdate);
create index fkohnmet2w8qiqs1tqgp3bvaj8s_securitytokenidwhcd on classification.classificationdataconceptxresourceitemsecuritytoken (securitytokenid, warehousefromdate);
create index fkrnilsvgvwpcbv8fs2yr1p87ei_systemidwhcd on classification.classificationdataconceptxresourceitemsecuritytoken (systemid, warehousefromdate);
create index fkrq8gtmddaq83y0vq5v5mvun3s_activeflagidwhcd on classification.classificationdataconceptxresourceitemsecuritytoken (activeflagid, warehousefromdate);


