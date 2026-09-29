CREATE SCHEMA security;
CREATE TABLE security.securityhierarchy
(
    id       UUID NOT NULL,
    name     character varying(255),
    one      integer,
    parentid character varying(36),
    path     character varying(255),
    pather   character varying(255)
);
CREATE TABLE security.securitytoken
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
CREATE TABLE security.securitytokenssecuritytoken
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
CREATE TABLE security.securitytokensxsecuritytokensecuritytoken
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
CREATE TABLE security.securitytokenxclassification
(
    securitytokenxclassificationid UUID                        NOT NULL primary key,
    effectivefromdate              timestamp(6) with time zone NOT NULL,
    effectivetodate                timestamp(6) with time zone NOT NULL,
    warehousecreatedtimestamp      timestamp(6) with time zone NOT NULL,
    warehousefromdate              DATE                        NOT NULL,

    warehouselastupdatedtimestamp  timestamp(6) with time zone NOT NULL,
    originalsourcesystemuniqueid   UUID                        NOT NULL,
    value                          text                        NOT NULL,
    activeflagid                   UUID                        NOT NULL,
    enterpriseid                   UUID                        NOT NULL,
    systemid                       UUID                        NOT NULL,
    originalsourcesystemid         UUID                        NOT NULL,
    classificationid               UUID                        NOT NULL,
    securitytokenid                UUID                        NOT NULL
);
CREATE TABLE security.securitytokenxclassificationsecuritytoken
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
CREATE TABLE security.securitytokenxsecuritytoken
(
    securitytokenxsecuritytokenid UUID                        NOT NULL primary key,
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
    childsecuritytokenid          UUID                        NOT NULL,
    parentsecuritytokenid         UUID                        NOT NULL
);

create index fk3dojhs6jjsp6g4xtwg1gfsg7i_originalsourcesystemid on security.securitytoken (originalsourcesystemid);
create index fk7oknl73tuuwwt63nkgdcm4abq_systemid on security.securitytoken (systemid);
create index fkdxgncdf26cjimvl7qyfqcpv5l_activeflagid on security.securitytoken (activeflagid);
create index fkg5dnn3ygqgg2j80h4ijca6kdp_securitytokenclassificationid on security.securitytoken (securitytokenclassificationid);
create index fksqp2hjm3vhxqy62do1twwtrmf_enterpriseid on security.securitytoken (enterpriseid);
create index fk7d5c4foji5ocaal45ulv0uaxl_enterpriseid on security.securitytokenssecuritytoken (enterpriseid);
create index fkbohmlwb2yuw6vjbohxmmuhu2u_activeflagid on security.securitytokenssecuritytoken (activeflagid);
create index fkgm9ke6kkuxlw3dm67iseaofu8_systemid on security.securitytokenssecuritytoken (systemid);
create index fkn5jt48ip0t79bbac760wnpk4u_originalsourcesystemid on security.securitytokenssecuritytoken (originalsourcesystemid);
create index fksoxo8an11ilt212h41x4c9ofj_securitytokenid on security.securitytokenssecuritytoken (securitytokenid);
create index fkujfypgbk5b3c90rftiwmwv0m_securitytokentoid on security.securitytokenssecuritytoken (securitytokentoid);
create index fk7thwp5xsmuj0tlkpvobud9um7_activeflagid on security.securitytokensxsecuritytokensecuritytoken (activeflagid);
create index fk8ts5kdhf2u71ss5se0eydoyx5_enterpriseid on security.securitytokensxsecuritytokensecuritytoken (enterpriseid);
create index fkamoh9xumd90l12fw52p4t2lr1_securitytokenid on security.securitytokensxsecuritytokensecuritytoken (securitytokenid);
create index fkr9dd79mvxphy35j0l30f60wjo_originalsourcesystemid on security.securitytokensxsecuritytokensecuritytoken (originalsourcesystemid);
create index fkraqvstjiad8yqfg3be6ae1e1r_systemid on security.securitytokensxsecuritytokensecuritytoken (systemid);
create index fks48ohap0caelpi8nu60sp55i6_securitytokenxsecuritytokenid on security.securitytokensxsecuritytokensecuritytoken (securitytokenxsecuritytokenid);
create index fkbeh76kxkmk54rmc3f6m4wsjaf_activeflagid on security.securitytokenxclassification (activeflagid);
create index fkf0ve8e1o43mpjqbkv0eor80ks_classificationid on security.securitytokenxclassification (classificationid);
create index fkiy9192vtwu1t1tucigro1dhbr_enterpriseid on security.securitytokenxclassification (enterpriseid);
create index fkkpec2fqi1saqvfqyim4qibvhc_systemid on security.securitytokenxclassification (systemid);
create index fkrct8kx4cxa9ys52h7kp02yda0_originalsourcesystemid on security.securitytokenxclassification (originalsourcesystemid);
create index fktis44ugn011fdb281jb5onjsv_securitytokenid on security.securitytokenxclassification (securitytokenid);
create index fk23j9r000kvoovvj3lpurf7lro_systemid on security.securitytokenxclassificationsecuritytoken (systemid);
create index fk7b8h1egk35meq3khexlt58p04_enterpriseid on security.securitytokenxclassificationsecuritytoken (enterpriseid);
create index fkctvl0v696371fygrxpr48xfh6_securitytokenid on security.securitytokenxclassificationsecuritytoken (securitytokenid);
create index fke3gxxm2i6buv2rvlr17em38aj_activeflagid on security.securitytokenxclassificationsecuritytoken (activeflagid);
create index fkmvagw1psni1acp68r86p98326_originalsourcesystemid on security.securitytokenxclassificationsecuritytoken (originalsourcesystemid);
create index fkrde1i5gxqtmm8pxu5g9lwepd2_securitytokenxclassificationid on security.securitytokenxclassificationsecuritytoken (securitytokenxclassificationid);
create index fkaa10y63not65gkhd88hn83qke_enterpriseid on security.securitytokenxsecuritytoken (enterpriseid);
create index fkbi1t0uojqlur84bdht26o1b9a_childsecuritytokenid on security.securitytokenxsecuritytoken (childsecuritytokenid);
create index fkfcmwpt8n1j6p4l9y51o4s4b99_classificationid on security.securitytokenxsecuritytoken (classificationid);
create index fkh8wabwfov1k3ah9r2jmm11fg8_parentsecuritytokenid on security.securitytokenxsecuritytoken (parentsecuritytokenid);
create index fkix4kxmf6f9elii179h4fv9opk_activeflagid on security.securitytokenxsecuritytoken (activeflagid);
create index fkkhrctfxn8jbkmugeuq13or9y2_originalsourcesystemid on security.securitytokenxsecuritytoken (originalsourcesystemid);
create index fklyogqrmsivjjwwmqyhs3w8eey_systemid on security.securitytokenxsecuritytoken (systemid);

CREATE INDEX idx_securitytokenxclassification_effectivefromdate ON security.securitytokenxclassification (effectivefromdate);
CREATE INDEX idx_securitytokenxclassification_effectivetodate ON security.securitytokenxclassification (effectivetodate);
CREATE INDEX idx_securitytokenxclassification_warehousecreatedtimestamp ON security.securitytokenxclassification (warehousecreatedtimestamp);
CREATE INDEX idx_securitytokenxclassification_warehouselastupdatedtimestamp ON security.securitytokenxclassification (warehouselastupdatedtimestamp);

CREATE INDEX idx_securitytokenxclassificationsecuritytoken_effectivefromdat ON security.securitytokenxclassificationsecuritytoken (effectivefromdate);
CREATE INDEX idx_securitytokenxclassificationsecuritytoken_effectivetodate ON security.securitytokenxclassificationsecuritytoken (effectivetodate);
CREATE INDEX idx_securitytokenxclassificationsecuritytoken_warehousecreated ON security.securitytokenxclassificationsecuritytoken (warehousecreatedtimestamp);
CREATE INDEX idx_securitytokenxclassificationsecuritytoken_warehouselastupd ON security.securitytokenxclassificationsecuritytoken (warehouselastupdatedtimestamp);
CREATE INDEX idx_securitytokenssecuritytoken_effectivefromdate ON security.securitytokenssecuritytoken (effectivefromdate);
CREATE INDEX idx_securitytokenssecuritytoken_effectivetodate ON security.securitytokenssecuritytoken (effectivetodate);
CREATE INDEX idx_securitytokenssecuritytoken_warehousecreatedtimestamp ON security.securitytokenssecuritytoken (warehousecreatedtimestamp);
CREATE INDEX idx_securitytokenssecuritytoken_warehouselastupdatedtimestamp ON security.securitytokenssecuritytoken (warehouselastupdatedtimestamp);
CREATE INDEX idx_securitytokensxsecuritytokensecuritytoken_effectivefromdat ON security.securitytokensxsecuritytokensecuritytoken (effectivefromdate);
CREATE INDEX idx_securitytokensxsecuritytokensecuritytoken_effectivetodate ON security.securitytokensxsecuritytokensecuritytoken (effectivetodate);
CREATE INDEX idx_securitytokensxsecuritytokensecuritytoken_warehousecreated ON security.securitytokensxsecuritytokensecuritytoken (warehousecreatedtimestamp);
CREATE INDEX idx_securitytokensxsecuritytokensecuritytoken_warehouselastupd ON security.securitytokensxsecuritytokensecuritytoken (warehouselastupdatedtimestamp);
CREATE INDEX idx_securitytokenxsecuritytoken_effectivefromdate ON security.securitytokenxsecuritytoken (effectivefromdate);
CREATE INDEX idx_securitytokenxsecuritytoken_effectivetodate ON security.securitytokenxsecuritytoken (effectivetodate);
CREATE INDEX idx_securitytokenxsecuritytoken_warehousecreatedtimestamp ON security.securitytokenxsecuritytoken (warehousecreatedtimestamp);
CREATE INDEX idx_securitytokenxsecuritytoken_warehouselastupdatedtimestamp ON security.securitytokenxsecuritytoken (warehouselastupdatedtimestamp);

CREATE INDEX idx_securitytoken_effectivefromdate ON security.securitytoken (effectivefromdate);
CREATE INDEX idx_securitytoken_effectivetodate ON security.securitytoken (effectivetodate);
CREATE INDEX idx_securitytoken_warehousecreatedtimestamp ON security.securitytoken (warehousecreatedtimestamp);
CREATE INDEX idx_securitytoken_warehouselastupdatedtimestamp ON security.securitytoken (warehouselastupdatedtimestamp);

CREATE INDEX idx_securitytokenxclassification_value ON security.securitytokenxclassification (value);
CREATE INDEX idx_securitytokenxsecuritytoken_value ON security.securitytokenxsecuritytoken (value);
CREATE INDEX idx_securitytoken_securitytokenfriendlyname ON security.securitytoken (securitytokenfriendlyname);

create index fk3dojhs6jjsp6g4xtwg1gfsg7i_originalsourcesystemidwhcd on security.securitytoken (originalsourcesystemid, warehousefromdate);
create index fk7oknl73tuuwwt63nkgdcm4abq_systemidwhcd on security.securitytoken (systemid, warehousefromdate);
create index fkdxgncdf26cjimvl7qyfqcpv5l_activeflagidwhcd on security.securitytoken (activeflagid, warehousefromdate);
create index fkg5dnn3ygqgg2j80h4ijca6kdp_securitytokenclassificationidwhcd on security.securitytoken (securitytokenclassificationid, warehousefromdate);
create index fksqp2hjm3vhxqy62do1twwtrmf_enterpriseidwhcd on security.securitytoken (enterpriseid, warehousefromdate);
create index fk7d5c4foji5ocaal45ulv0uaxl_enterpriseidwhcd on security.securitytokenssecuritytoken (enterpriseid, warehousefromdate);
create index fkbohmlwb2yuw6vjbohxmmuhu2u_activeflagidwhcd on security.securitytokenssecuritytoken (activeflagid, warehousefromdate);
create index fkgm9ke6kkuxlw3dm67iseaofu8_systemidwhcd on security.securitytokenssecuritytoken (systemid, warehousefromdate);
create index fkn5jt48ip0t79bbac760wnpk4u_originalsourcesystemidwhcd on security.securitytokenssecuritytoken (originalsourcesystemid, warehousefromdate);
create index fksoxo8an11ilt212h41x4c9ofj_securitytokenidwhcd on security.securitytokenssecuritytoken (securitytokenid, warehousefromdate);
create index fkujfypgbk5b3c90rftiwmwv0m_securitytokentoidwhcd on security.securitytokenssecuritytoken (securitytokentoid, warehousefromdate);
create index fk7thwp5xsmuj0tlkpvobud9um7_activeflagidwhcd on security.securitytokensxsecuritytokensecuritytoken (activeflagid, warehousefromdate);
create index fk8ts5kdhf2u71ss5se0eydoyx5_enterpriseidwhcd on security.securitytokensxsecuritytokensecuritytoken (enterpriseid, warehousefromdate);
create index fkamoh9xumd90l12fw52p4t2lr1_securitytokenidwhcd on security.securitytokensxsecuritytokensecuritytoken (securitytokenid, warehousefromdate);
create index fkr9dd79mvxphy35j0l30f60wjo_originalsourcesystemidwhcd on security.securitytokensxsecuritytokensecuritytoken (originalsourcesystemid, warehousefromdate);
create index fkraqvstjiad8yqfg3be6ae1e1r_systemidwhcd on security.securitytokensxsecuritytokensecuritytoken (systemid, warehousefromdate);
create index fks48ohap0caelpi8nu60sp55i6_securitytokenxsecuritytokenidwhcd on security.securitytokensxsecuritytokensecuritytoken (securitytokenxsecuritytokenid, warehousefromdate);
create index fkbeh76kxkmk54rmc3f6m4wsjaf_activeflagidwhcd on security.securitytokenxclassification (activeflagid, warehousefromdate);
create index fkf0ve8e1o43mpjqbkv0eor80ks_classificationidwhcd on security.securitytokenxclassification (classificationid, warehousefromdate);
create index fkiy9192vtwu1t1tucigro1dhbr_enterpriseidwhcd on security.securitytokenxclassification (enterpriseid, warehousefromdate);
create index fkkpec2fqi1saqvfqyim4qibvhc_systemidwhcd on security.securitytokenxclassification (systemid, warehousefromdate);
create index fkrct8kx4cxa9ys52h7kp02yda0_originalsourcesystemidwhcd on security.securitytokenxclassification (originalsourcesystemid, warehousefromdate);
create index fktis44ugn011fdb281jb5onjsv_securitytokenidwhcd on security.securitytokenxclassification (securitytokenid, warehousefromdate);
create index fk23j9r000kvoovvj3lpurf7lro_systemidwhcd on security.securitytokenxclassificationsecuritytoken (systemid, warehousefromdate);
create index fk7b8h1egk35meq3khexlt58p04_enterpriseidwhcd on security.securitytokenxclassificationsecuritytoken (enterpriseid, warehousefromdate);
create index fkctvl0v696371fygrxpr48xfh6_securitytokenidwhcd on security.securitytokenxclassificationsecuritytoken (securitytokenid, warehousefromdate);
create index fke3gxxm2i6buv2rvlr17em38aj_activeflagidwhcd on security.securitytokenxclassificationsecuritytoken (activeflagid, warehousefromdate);
create index fkmvagw1psni1acp68r86p98326_originalsourcesystemidwhcd on security.securitytokenxclassificationsecuritytoken (originalsourcesystemid, warehousefromdate);
create index fkrde1i5gxqtmm8pxu5g9lwepd2_securitytokenxclassificationidwhcd on security.securitytokenxclassificationsecuritytoken (securitytokenxclassificationid, warehousefromdate);
create index fkaa10y63not65gkhd88hn83qke_enterpriseidwhcd on security.securitytokenxsecuritytoken (enterpriseid, warehousefromdate);
create index fkbi1t0uojqlur84bdht26o1b9a_childsecuritytokenidwhcd on security.securitytokenxsecuritytoken (childsecuritytokenid, warehousefromdate);
create index fkfcmwpt8n1j6p4l9y51o4s4b99_classificationidwhcd on security.securitytokenxsecuritytoken (classificationid, warehousefromdate);
create index fkh8wabwfov1k3ah9r2jmm11fg8_parentsecuritytokenidwhcd on security.securitytokenxsecuritytoken (parentsecuritytokenid, warehousefromdate);
create index fkix4kxmf6f9elii179h4fv9opk_activeflagidwhcd on security.securitytokenxsecuritytoken (activeflagid, warehousefromdate);
create index fkkhrctfxn8jbkmugeuq13or9y2_originalsourcesystemidwhcd on security.securitytokenxsecuritytoken (originalsourcesystemid, warehousefromdate);
create index fklyogqrmsivjjwwmqyhs3w8eey_systemidwhcd on security.securitytokenxsecuritytoken (systemid, warehousefromdate);


