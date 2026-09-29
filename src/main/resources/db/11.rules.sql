CREATE SCHEMA rules;
CREATE TABLE rules.rules
(
    rulesid                       UUID                        NOT NULL primary key,
    effectivefromdate             timestamp(6) with time zone NOT NULL,
    effectivetodate               timestamp(6) with time zone NOT NULL,
    warehousecreatedtimestamp     timestamp(6) with time zone NOT NULL,
    warehousefromdate             DATE                        NOT NULL,

    warehouselastupdatedtimestamp timestamp(6) with time zone NOT NULL,
    originalsourcesystemuniqueid  UUID                        NOT NULL,
    rulesetdescription            character varying(250)      NOT NULL,
    rulesetname                   character varying(150)      NOT NULL,
    activeflagid                  UUID                        NOT NULL,
    enterpriseid                  UUID                        NOT NULL,
    systemid                      UUID                        NOT NULL,
    originalsourcesystemid        UUID                        NOT NULL
);

CREATE TABLE rules.rulessecuritytoken
(
    rulessecuritytokenid          UUID                        NOT NULL primary key,
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
    rulesid                       UUID                        NOT NULL
);

CREATE TABLE rules.rulestype
(
    rulestypeid                   UUID                        NOT NULL primary key,
    effectivefromdate             timestamp(6) with time zone NOT NULL,
    effectivetodate               timestamp(6) with time zone NOT NULL,
    warehousecreatedtimestamp     timestamp(6) with time zone NOT NULL,
    warehousefromdate             DATE                        NOT NULL,

    warehouselastupdatedtimestamp timestamp(6) with time zone NOT NULL,
    originalsourcesystemuniqueid  UUID                        NOT NULL,
    rulestypedesc                 character varying(200)      NOT NULL,
    rulestypename                 character varying(200)      NOT NULL,
    activeflagid                  UUID                        NOT NULL,
    enterpriseid                  UUID                        NOT NULL,
    systemid                      UUID                        NOT NULL,
    originalsourcesystemid        UUID                        NOT NULL
);
CREATE TABLE rules.rulestypessecuritytoken
(
    rulestypessecuritytokenid     UUID                        NOT NULL primary key,
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
    rulestypesid                  UUID                        NOT NULL
);

CREATE TABLE rules.rulestypexclassification
(
    rulestypexclassificationid    UUID                        NOT NULL primary key,
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
    rulestypeid                   UUID                        NOT NULL
);

CREATE TABLE rules.rulestypexclassificationsecuritytoken
(
    rulestypexclassificationsecuritytokenid UUID                        NOT NULL primary key,
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
    rulestypexclassificationid              UUID                        NOT NULL
);

CREATE TABLE rules.rulestypexresourceitem
(
    rulestypexresourceitemid      UUID                        NOT NULL primary key,
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
    resourceitemid                UUID                        NOT NULL,
    rulestypeid                   UUID                        NOT NULL
);

CREATE TABLE rules.rulestypexresourceitemsecuritytoken
(
    rulestypexresourceitemsecuritytokenid UUID                        NOT NULL primary key,
    effectivefromdate                     timestamp(6) with time zone NOT NULL,
    effectivetodate                       timestamp(6) with time zone NOT NULL,
    warehousecreatedtimestamp             timestamp(6) with time zone NOT NULL,
    warehousefromdate                     DATE                        NOT NULL,

    warehouselastupdatedtimestamp         timestamp(6) with time zone NOT NULL,
    createallowed                         INTEGER                     NOT NULL,
    deleteallowed                         INTEGER                     NOT NULL,
    originalsourcesystemuniqueid          UUID                        NOT NULL,
    readallowed                           INTEGER                     NOT NULL,
    updateallowed                         INTEGER                     NOT NULL,
    activeflagid                          UUID                        NOT NULL,
    enterpriseid                          UUID                        NOT NULL,
    originalsourcesystemid                UUID                        NOT NULL,
    securitytokenid                       UUID                        NOT NULL,
    systemid                              UUID                        NOT NULL,
    rulestypexresourceitemid              UUID                        NOT NULL
);

CREATE TABLE rules.rulesxarrangement
(
    rulesxarrangementsid          UUID                        NOT NULL primary key,
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

CREATE TABLE rules.rulesxarrangementssecuritytoken
(
    rulesxarrangementssecuritytokenid UUID                        NOT NULL primary key,
    effectivefromdate                 timestamp(6) with time zone NOT NULL,
    effectivetodate                   timestamp(6) with time zone NOT NULL,
    warehousecreatedtimestamp         timestamp(6) with time zone NOT NULL,
    warehousefromdate                 DATE                        NOT NULL,

    warehouselastupdatedtimestamp     timestamp(6) with time zone NOT NULL,
    createallowed                     INTEGER                     NOT NULL,
    deleteallowed                     INTEGER                     NOT NULL,
    originalsourcesystemuniqueid      UUID                        NOT NULL,
    readallowed                       INTEGER                     NOT NULL,
    updateallowed                     INTEGER                     NOT NULL,
    activeflagid                      UUID                        NOT NULL,
    enterpriseid                      UUID                        NOT NULL,
    originalsourcesystemid            UUID                        NOT NULL,
    securitytokenid                   UUID                        NOT NULL,
    systemid                          UUID                        NOT NULL,
    rulesxarrangementsid              UUID                        NOT NULL
);
CREATE TABLE rules.rulesxclassification
(
    rulesxclassificationid        UUID                        NOT NULL primary key,
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
    rulesid                       UUID                        NOT NULL
);
CREATE TABLE rules.rulesxclassificationsecuritytoken
(
    rulesxclassificationsecuritytokenid UUID                        NOT NULL primary key,
    effectivefromdate                   timestamp(6) with time zone NOT NULL,
    effectivetodate                     timestamp(6) with time zone NOT NULL,
    warehousecreatedtimestamp           timestamp(6) with time zone NOT NULL,
    warehousefromdate                   DATE                        NOT NULL,

    warehouselastupdatedtimestamp       timestamp(6) with time zone NOT NULL,
    createallowed                       INTEGER                     NOT NULL,
    deleteallowed                       INTEGER                     NOT NULL,
    originalsourcesystemuniqueid        UUID                        NOT NULL,
    readallowed                         INTEGER                     NOT NULL,
    updateallowed                       INTEGER                     NOT NULL,
    activeflagid                        UUID                        NOT NULL,
    enterpriseid                        UUID                        NOT NULL,
    originalsourcesystemid              UUID                        NOT NULL,
    securitytokenid                     UUID                        NOT NULL,
    systemid                            UUID                        NOT NULL,
    rulesxclassificationid              UUID                        NOT NULL
);
CREATE TABLE rules.rulesxinvolvedparty
(
    rulesxinvolvedpartyid         UUID                        NOT NULL primary key,
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
    involvedpartyid               UUID                        NOT NULL,
    rulesid                       UUID                        NOT NULL
);
CREATE TABLE rules.rulesxinvolvedpartysecuritytoken
(
    rulesxinvolvedpartysecuritytokenid UUID                        NOT NULL primary key,
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
    rulesxinvolvedpartyid              UUID                        NOT NULL
);
CREATE TABLE rules.rulesxproduct
(
    rulesxproductid               UUID                        NOT NULL primary key,
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
    productid                     UUID                        NOT NULL,
    rulesid                       UUID                        NOT NULL
);
CREATE TABLE rules.rulesxproductsecuritytoken
(
    rulesxproductsecuritytokenid  UUID                        NOT NULL primary key,
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
    rulesxproductid               UUID                        NOT NULL
);
CREATE TABLE rules.rulesxresourceitem
(
    rulesxresourceitemid          UUID                        NOT NULL primary key,
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
    resourceitemid                UUID                        NOT NULL,
    rulesid                       UUID                        NOT NULL
);
CREATE TABLE rules.rulesxresourceitemsecuritytoken
(
    rulesxresourceitemsecuritytokenid UUID                        NOT NULL primary key,
    effectivefromdate                 timestamp(6) with time zone NOT NULL,
    effectivetodate                   timestamp(6) with time zone NOT NULL,
    warehousecreatedtimestamp         timestamp(6) with time zone NOT NULL,
    warehousefromdate                 DATE                        NOT NULL,

    warehouselastupdatedtimestamp     timestamp(6) with time zone NOT NULL,
    createallowed                     INTEGER                     NOT NULL,
    deleteallowed                     INTEGER                     NOT NULL,
    originalsourcesystemuniqueid      UUID                        NOT NULL,
    readallowed                       INTEGER                     NOT NULL,
    updateallowed                     INTEGER                     NOT NULL,
    activeflagid                      UUID                        NOT NULL,
    enterpriseid                      UUID                        NOT NULL,
    originalsourcesystemid            UUID                        NOT NULL,
    securitytokenid                   UUID                        NOT NULL,
    systemid                          UUID                        NOT NULL,
    rulesxresourceitemid              UUID                        NOT NULL
);
CREATE TABLE rules.rulesxrules
(
    rulesxrulesid                 UUID                        NOT NULL primary key,
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
    childrulesid                  UUID                        NOT NULL,
    parentrulesid                 UUID                        NOT NULL
);
CREATE TABLE rules.rulesxrulessecuritytoken
(
    rulesxrulessecuritytokenid    UUID                        NOT NULL primary key,
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
    rulesxrulesid                 UUID                        NOT NULL
);
CREATE TABLE rules.rulesxrulestype
(
    rulesxrulestypeid             UUID                        NOT NULL primary key,
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
    rulesid                       UUID                        NOT NULL,
    rulestypeid                   UUID                        NOT NULL
);
CREATE TABLE rules.rulesxrulestypesecuritytoken
(
    rulesxrulestypesecuritytokenid UUID                        NOT NULL primary key,
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
    rulesxrulestypeid              UUID                        NOT NULL
);


create index fkbpbgfk1w8xjpcvla2wtr28rfm_enterpriseid on rules.rules (enterpriseid);
create index fkihoftg6akowf7mjyheyfmty0i_systemid on rules.rules (systemid);
create index fknh64xmjpd3alokexjnuqr0unw_activeflagid on rules.rules (activeflagid);
create index fkqkhxbrmhfk0ou8sfgrq6w04yn_originalsourcesystemid on rules.rules (originalsourcesystemid);
create index fk57dd0fcy24o8k2spnqrykgk2a_enterpriseid on rules.rulessecuritytoken (enterpriseid);
create index fk5hjfvbnd3js59ca3fowm88h6t_originalsourcesystemid on rules.rulessecuritytoken (originalsourcesystemid);
create index fkenyc4oy7p7bpluwr2incgf2m6_systemid on rules.rulessecuritytoken (systemid);
create index fkmsl2uqlxr7q5n3bq6e14nknk_activeflagid on rules.rulessecuritytoken (activeflagid);
create index fkmtk7w2kpskec0ebnwnrmo8rmp_rulesid on rules.rulessecuritytoken (rulesid);
create index fknj1ss0ctynt0u0t9nx5wprick_securitytokenid on rules.rulessecuritytoken (securitytokenid);
create index fk11qj2su8f2sy9nvcjss76yfqm_originalsourcesystemid on rules.rulestype (originalsourcesystemid);
create index fk7v51o36ciw802dj5m82giq97q_enterpriseid on rules.rulestype (enterpriseid);
create index fkf7030hfrlt8o2dxdarmt5ftrp_systemid on rules.rulestype (systemid);
create index fkm7jdyuyikwo9qlfe6qae0bv3q_activeflagid on rules.rulestype (activeflagid);
create index fk12fsootndlsrm2gkytfg2gfw0_securitytokenid on rules.rulestypessecuritytoken (securitytokenid);
create index fk5tck1lwdgo3i3lbwyphxlsilw_activeflagid on rules.rulestypessecuritytoken (activeflagid);
create index fk9aexqcupg3q8il9en4moulmo1_rulestypesid on rules.rulestypessecuritytoken (rulestypesid);
create index fkeciu87p8mk0yiel2t1bwipg9a_systemid on rules.rulestypessecuritytoken (systemid);
create index fkic2121k0p1edg44c55hog6oe6_originalsourcesystemid on rules.rulestypessecuritytoken (originalsourcesystemid);
create index fks5xpq0ecpqefbfuwqkymvncsn_enterpriseid on rules.rulestypessecuritytoken (enterpriseid);
create index fk3fotawtun2be400jw1c5mcxr5_originalsourcesystemid on rules.rulestypexclassification (originalsourcesystemid);
create index fk87mojdqprestm5uqmw925bqf1_rulestypeid on rules.rulestypexclassification (rulestypeid);
create index fk9662odo6kpnj3a66uyqqh9gph_enterpriseid on rules.rulestypexclassification (enterpriseid);
create index fkcmpyykrmpk1epxo9jmicem78i_classificationid on rules.rulestypexclassification (classificationid);
create index fkdck5vk5rjw51e9u8ducstsysg_systemid on rules.rulestypexclassification (systemid);
create index fkl2497yt06thxeacoue8jbbaoh_activeflagid on rules.rulestypexclassification (activeflagid);
create index fk3nsh18vueud5iybj4xk2t4j6r_enterpriseid on rules.rulestypexclassificationsecuritytoken (enterpriseid);
create index fk4rylunbc8qipcw0n020nhab5b_systemid on rules.rulestypexclassificationsecuritytoken (systemid);
create index fkamvwb2lq6eh8s3gobs9amn1ns_activeflagid on rules.rulestypexclassificationsecuritytoken (activeflagid);
create index fkcn088ejtlnauiohbrpvvufegu_securitytokenid on rules.rulestypexclassificationsecuritytoken (securitytokenid);
create index fkmjbouj2e7loyrlu87avtm4gtm_originalsourcesystemid on rules.rulestypexclassificationsecuritytoken (originalsourcesystemid);
create index fknn81qs2b5rstr3u748lvjbl61_rulestypexclassificationid on rules.rulestypexclassificationsecuritytoken (rulestypexclassificationid);
create index fk6hivvn41g61n8v941egrae19i_rulestypeid on rules.rulestypexresourceitem (rulestypeid);
create index fk7an7eg5j3seoaxajls3knmrwt_classificationid on rules.rulestypexresourceitem (classificationid);
create index fkkl9dib27d8h3g00pp40q3vpw4_enterpriseid on rules.rulestypexresourceitem (enterpriseid);
create index fko8drrcbtv7to8utrafqb942a_systemid on rules.rulestypexresourceitem (systemid);
create index fkqygy5t9dhj5r3nefikdhaxdb6_activeflagid on rules.rulestypexresourceitem (activeflagid);
create index fkrdh9f21yu45alpi37mrexx5sy_resourceitemid on rules.rulestypexresourceitem (resourceitemid);
create index fksbm8nex7yud5ymui9t5m2wuiw_originalsourcesystemid on rules.rulestypexresourceitem (originalsourcesystemid);
create index fk8xs3tib17edep24d44482bmjp_enterpriseid on rules.rulestypexresourceitemsecuritytoken (enterpriseid);
create index fkcdu908ybhdghbjvhquhpfucsr_activeflagid on rules.rulestypexresourceitemsecuritytoken (activeflagid);
create index fklhnbp8n1wy13okhhtnxwqu5hy_systemid on rules.rulestypexresourceitemsecuritytoken (systemid);
create index fkm6jgd6lrdywbn56w6e0s5awd1_securitytokenid on rules.rulestypexresourceitemsecuritytoken (securitytokenid);
create index fkotlbdpur4kwiwymopsenwumbg_rulestypexresourceitemid on rules.rulestypexresourceitemsecuritytoken (rulestypexresourceitemid);
create index fkrbpyq0298hcisaevbl3mp0m5d_originalsourcesystemid on rules.rulestypexresourceitemsecuritytoken (originalsourcesystemid);
create index fk4nj9r8fu6b1ymd9o8tdryxrtx_originalsourcesystemid on rules.rulesxarrangement (originalsourcesystemid);
create index fk7kbnsdojwukxxly9vxqkraun2_activeflagid on rules.rulesxarrangement (activeflagid);
create index fk7kfpfoyfd5mn1x45215dlee28_systemid on rules.rulesxarrangement (systemid);
create index fk9gfetl178gc0bjfak6tny0keu_arrangementid on rules.rulesxarrangement (arrangementid);
create index fkeu7bn1gb6dg7xt097ognxjwh5_enterpriseid on rules.rulesxarrangement (enterpriseid);
create index fkfj9ondul2lkfugd0u6vdxq1yi_rulesid on rules.rulesxarrangement (rulesid);
create index fkrkfqm429ef4j7jwneu7jxeye4_classificationid on rules.rulesxarrangement (classificationid);
create index fk2xvs1pi7rc32lemnyvj01j301_enterpriseid on rules.rulesxarrangementssecuritytoken (enterpriseid);
create index fk4dxpxc1hj2n1ik4s3ms69w3kw_activeflagid on rules.rulesxarrangementssecuritytoken (activeflagid);
create index fkd1ylioumsfoo6scfb0fdouur7_securitytokenid on rules.rulesxarrangementssecuritytoken (securitytokenid);
create index fkflgh5an92uoxffkehd7g7tp8j_originalsourcesystemid on rules.rulesxarrangementssecuritytoken (originalsourcesystemid);
create index fks5w3o4wg63awg0iuc76km8e1n_rulesxarrangementsid on rules.rulesxarrangementssecuritytoken (rulesxarrangementsid);
create index fkte09hu3a7vurplykt3w97714k_systemid on rules.rulesxarrangementssecuritytoken (systemid);
create index fk38k4xit5ty330vs01iv18sikh_enterpriseid on rules.rulesxclassification (enterpriseid);
create index fk7nyvj635fp8si7pq0qetkfhpb_systemid on rules.rulesxclassification (systemid);
create index fk8txg0h4hi98qbgslaodff7dyt_activeflagid on rules.rulesxclassification (activeflagid);
create index fkbbvhygyarc5j4gi7wjnd052p3_rulesid on rules.rulesxclassification (rulesid);
create index fkiyjrx7w183fwo2nhmwpqh0stw_classificationid on rules.rulesxclassification (classificationid);
create index fklog5hewe9u4c7wb35u4xvv84c_originalsourcesystemid on rules.rulesxclassification (originalsourcesystemid);
create index fkb0vp45d3tkxwvy5938ua9n809_activeflagid on rules.rulesxclassificationsecuritytoken (activeflagid);
create index fkj60pj71vbrlqc7ijopnpprev7_securitytokenid on rules.rulesxclassificationsecuritytoken (securitytokenid);
create index fkjklxc6ukplqhj7wa03ah80qh0_originalsourcesystemid on rules.rulesxclassificationsecuritytoken (originalsourcesystemid);
create index fkmno9p3edeufft6bgiv94tecpa_systemid on rules.rulesxclassificationsecuritytoken (systemid);
create index fkowvdqrq0fiqefy3g85fj60rgq_rulesxclassificationid on rules.rulesxclassificationsecuritytoken (rulesxclassificationid);
create index fkq49rw6kbi9gykntudl1mdodl7_enterpriseid on rules.rulesxclassificationsecuritytoken (enterpriseid);
create index fk3djrrw59r13qf53lxmls4da7g_involvedpartyid on rules.rulesxinvolvedparty (involvedpartyid);
create index fk5ujdw4x8upupcaml412wan0p7_originalsourcesystemid on rules.rulesxinvolvedparty (originalsourcesystemid);
create index fk611oa399omtv631o66ausvb55_enterpriseid on rules.rulesxinvolvedparty (enterpriseid);
create index fk8i91cawihcqkxe2fr9oawrshw_systemid on rules.rulesxinvolvedparty (systemid);
create index fkj8719e5kkmd7kd8bpqr71rt3a_classificationid on rules.rulesxinvolvedparty (classificationid);
create index fkqcugfk4tgg63qe2e7fk4h1jou_rulesid on rules.rulesxinvolvedparty (rulesid);
create index fksqp0envk694667mbnl8hndily_activeflagid on rules.rulesxinvolvedparty (activeflagid);
create index fk3qdk5vxgo4txkaaj5a8y8giwd_systemid on rules.rulesxinvolvedpartysecuritytoken (systemid);
create index fk7lvh9pa4gweufl5x0ubtl38my_securitytokenid on rules.rulesxinvolvedpartysecuritytoken (securitytokenid);
create index fk903el1e8i3wxjag53njlscvkg_originalsourcesystemid on rules.rulesxinvolvedpartysecuritytoken (originalsourcesystemid);
create index fkg0ntb8lcpw9n6p49kpubuyle2_rulesxinvolvedpartyid on rules.rulesxinvolvedpartysecuritytoken (rulesxinvolvedpartyid);
create index fkju1w7gcf2bu52ok8ibtd40gk4_enterpriseid on rules.rulesxinvolvedpartysecuritytoken (enterpriseid);
create index fkunriof269jly3lkmqk8jsp4g_activeflagid on rules.rulesxinvolvedpartysecuritytoken (activeflagid);
create index fk57dubv678gaj1eatqjof0x0nb_classificationid on rules.rulesxproduct (classificationid);
create index fk5m9wrb374b5g1wpwkj3jr09y6_activeflagid on rules.rulesxproduct (activeflagid);
create index fk6a018wimk1mn1ucjsg7m6v42e_enterpriseid on rules.rulesxproduct (enterpriseid);
create index fk9t2smyfxndgfnuoht0l8q42rb_systemid on rules.rulesxproduct (systemid);
create index fkf3t7xp42nph7ypmy38rtkr9c8_rulesid on rules.rulesxproduct (rulesid);
create index fkr9hc797wqs7k8u8iun1psqhi0_productid on rules.rulesxproduct (productid);
create index fkrwm05r7gimvp4itjeem7u6sk6_originalsourcesystemid on rules.rulesxproduct (originalsourcesystemid);
create index fk1inr52n9q3ax5rhqtjhthh78r_rulesxproductid on rules.rulesxproductsecuritytoken (rulesxproductid);
create index fk4g0qn2ufofwya9mua7k1bip3n_enterpriseid on rules.rulesxproductsecuritytoken (enterpriseid);
create index fk50a41rvtkyewefjmf4e8l52sw_securitytokenid on rules.rulesxproductsecuritytoken (securitytokenid);
create index fka1ai0uri46j0quxeqcikkdclv_systemid on rules.rulesxproductsecuritytoken (systemid);
create index fkgenrresjjjhv02rh0slya5d88_activeflagid on rules.rulesxproductsecuritytoken (activeflagid);
create index fklaeiodujthn91pwnj8qag1ao6_originalsourcesystemid on rules.rulesxproductsecuritytoken (originalsourcesystemid);
create index fk19cj3ortt8x7wa4mnkohkpebi_rulesid on rules.rulesxresourceitem (rulesid);
create index fk41dmnx4ijklqr44tlpomg7it5_classificationid on rules.rulesxresourceitem (classificationid);
create index fk5fe2nqc5oe2o0pqnptrrciyra_enterpriseid on rules.rulesxresourceitem (enterpriseid);
create index fkhubiaglxhl6fy8gx1oei33qbc_activeflagid on rules.rulesxresourceitem (activeflagid);
create index fkr1sg6ky0tcfxt3faiqbpvq72o_originalsourcesystemid on rules.rulesxresourceitem (originalsourcesystemid);
create index fktlnk6dgimet9xmad6ajypb5xh_systemid on rules.rulesxresourceitem (systemid);
create index fkvs83cy05gattyxwtir1yyv2s_resourceitemid on rules.rulesxresourceitem (resourceitemid);
create index fk23yuc0ofnmca2ig2dd1wx5ol8_rulesxresourceitemid on rules.rulesxresourceitemsecuritytoken (rulesxresourceitemid);
create index fk5dv1vwep6v9958vjdse4henw0_securitytokenid on rules.rulesxresourceitemsecuritytoken (securitytokenid);
create index fkji8ugnof4vkss37iu1mfuy2l4_originalsourcesystemid on rules.rulesxresourceitemsecuritytoken (originalsourcesystemid);
create index fkju938jbh27xnvuokmjxlnlqos_activeflagid on rules.rulesxresourceitemsecuritytoken (activeflagid);
create index fksm3jfs67p3ly7mjl1jkrualak_systemid on rules.rulesxresourceitemsecuritytoken (systemid);
create index fktlm1dpqcyom9yo1lefe0yqowj_enterpriseid on rules.rulesxresourceitemsecuritytoken (enterpriseid);
create index fk31s09rv16v0yhe5aac7l6ele3_classificationid on rules.rulesxrules (classificationid);
create index fk37pin4ub4l14xqqanw037awej_activeflagid on rules.rulesxrules (activeflagid);
create index fkgivavwvb91qbin4sxepd6wbs5_parentrulesid on rules.rulesxrules (parentrulesid);
create index fkpahnm53g0d7kjo5tfrask49up_childrulesid on rules.rulesxrules (childrulesid);
create index fkqmjt8nooh7ch2vr14euld3bnk_enterpriseid on rules.rulesxrules (enterpriseid);
create index fkrncrmhaj7pk6audwswshmudqj_systemid on rules.rulesxrules (systemid);
create index fkt5ep7dex0ykbws23o3yk8sctu_originalsourcesystemid on rules.rulesxrules (originalsourcesystemid);
create index fk4kklxff6jc1n1bo39g3sdqrix_rulesxrulesid on rules.rulesxrulessecuritytoken (rulesxrulesid);
create index fkfv9sfhe1kdjwa8dgu136k90ut_securitytokenid on rules.rulesxrulessecuritytoken (securitytokenid);
create index fklbxxoxpoelx3t2p1g96nqv90q_enterpriseid on rules.rulesxrulessecuritytoken (enterpriseid);
create index fknwhg52uwedumbo0s6msu4kvu4_systemid on rules.rulesxrulessecuritytoken (systemid);
create index fkohvvumprn80d89qp41tyeesd3_activeflagid on rules.rulesxrulessecuritytoken (activeflagid);
create index fks16mt02lac7ao524fhu5liiys_originalsourcesystemid on rules.rulesxrulessecuritytoken (originalsourcesystemid);
create index fk710wj6aj8es1ht1o9fxdehtua_systemid on rules.rulesxrulestype (systemid);
create index fk8hvei78rfy1byxtb36lwwst06_originalsourcesystemid on rules.rulesxrulestype (originalsourcesystemid);
create index fkbn4oo9asbe39dle7ow6buqvt7_activeflagid on rules.rulesxrulestype (activeflagid);
create index fkbohku2sim3tc8ey86rq8psqpt_rulesid on rules.rulesxrulestype (rulesid);
create index fkdly04cjef5yj3ne8v1uwb8h12_classificationid on rules.rulesxrulestype (classificationid);
create index fkonxshkblvrhnt4fasmfxmnyrf_enterpriseid on rules.rulesxrulestype (enterpriseid);
create index fkrv7w522dypyy838vgcr1r566e_rulestypeid on rules.rulesxrulestype (rulestypeid);
create index fkbn0acfejqvxbal40304ibsbr5_activeflagid on rules.rulesxrulestypesecuritytoken (activeflagid);
create index fkby3adp3oxnvi7q571ij36jj0v_originalsourcesystemid on rules.rulesxrulestypesecuritytoken (originalsourcesystemid);
create index fkibc3w3kdwgur69j3lhbi6l0k5_securitytokenid on rules.rulesxrulestypesecuritytoken (securitytokenid);
create index fkjr2flixl5hw7uqmh9chynqt4h_rulesxrulestypeid on rules.rulesxrulestypesecuritytoken (rulesxrulestypeid);
create index fkm2rtvaw8pdy1l3rvbc9wicom3_enterpriseid on rules.rulesxrulestypesecuritytoken (enterpriseid);
create index fkmst7g4r9cabb1d78ysr4hvppe_systemid on rules.rulesxrulestypesecuritytoken (systemid);


CREATE INDEX idx_rulestypessecuritytoken_effectivefromdate ON rules.rulestypessecuritytoken (effectivefromdate);
CREATE INDEX idx_rulestypessecuritytoken_effectivetodate ON rules.rulestypessecuritytoken (effectivetodate);
CREATE INDEX idx_rulestypessecuritytoken_warehousecreatedtimestamp ON rules.rulestypessecuritytoken (warehousecreatedtimestamp);
CREATE INDEX idx_rulestypessecuritytoken_warehouselastupdatedtimestamp ON rules.rulestypessecuritytoken (warehouselastupdatedtimestamp);
CREATE INDEX idx_rulestypexclassificationsecuritytoken_effectivefromdate ON rules.rulestypexclassificationsecuritytoken (effectivefromdate);
CREATE INDEX idx_rulestypexclassificationsecuritytoken_effectivetodate ON rules.rulestypexclassificationsecuritytoken (effectivetodate);
CREATE INDEX idx_rulestypexclassificationsecuritytoken_warehousecreatedtime ON rules.rulestypexclassificationsecuritytoken (warehousecreatedtimestamp);
CREATE INDEX idx_rulestypexclassificationsecuritytoken_warehouselastupdated ON rules.rulestypexclassificationsecuritytoken (warehouselastupdatedtimestamp);
CREATE INDEX idx_rulestypexresourceitemsecuritytoken_effectivefromdate ON rules.rulestypexresourceitemsecuritytoken (effectivefromdate);
CREATE INDEX idx_rulestypexresourceitemsecuritytoken_effectivetodate ON rules.rulestypexresourceitemsecuritytoken (effectivetodate);
CREATE INDEX idx_rulestypexresourceitemsecuritytoken_warehousecreatedtimest ON rules.rulestypexresourceitemsecuritytoken (warehousecreatedtimestamp);
CREATE INDEX idx_rulestypexresourceitemsecuritytoken_warehouselastupdatedti ON rules.rulestypexresourceitemsecuritytoken (warehouselastupdatedtimestamp);
CREATE INDEX idx_rulessecuritytoken_effectivefromdate ON rules.rulessecuritytoken (effectivefromdate);
CREATE INDEX idx_rulessecuritytoken_effectivetodate ON rules.rulessecuritytoken (effectivetodate);
CREATE INDEX idx_rulessecuritytoken_warehousecreatedtimestamp ON rules.rulessecuritytoken (warehousecreatedtimestamp);
CREATE INDEX idx_rulessecuritytoken_warehouselastupdatedtimestamp ON rules.rulessecuritytoken (warehouselastupdatedtimestamp);
CREATE INDEX idx_rulesxinvolvedparty_effectivefromdate ON rules.rulesxinvolvedparty (effectivefromdate);
CREATE INDEX idx_rulesxinvolvedparty_effectivetodate ON rules.rulesxinvolvedparty (effectivetodate);
CREATE INDEX idx_rulesxinvolvedparty_warehousecreatedtimestamp ON rules.rulesxinvolvedparty (warehousecreatedtimestamp);
CREATE INDEX idx_rulesxinvolvedparty_warehouselastupdatedtimestamp ON rules.rulesxinvolvedparty (warehouselastupdatedtimestamp);
CREATE INDEX idx_rulestypexclassification_effectivefromdate ON rules.rulestypexclassification (effectivefromdate);
CREATE INDEX idx_rulestypexclassification_effectivetodate ON rules.rulestypexclassification (effectivetodate);
CREATE INDEX idx_rulestypexclassification_warehousecreatedtimestamp ON rules.rulestypexclassification (warehousecreatedtimestamp);
CREATE INDEX idx_rulestypexclassification_warehouselastupdatedtimestamp ON rules.rulestypexclassification (warehouselastupdatedtimestamp);
CREATE INDEX idx_rulestype_effectivefromdate ON rules.rulestype (effectivefromdate);
CREATE INDEX idx_rulestype_effectivetodate ON rules.rulestype (effectivetodate);
CREATE INDEX idx_rulestype_warehousecreatedtimestamp ON rules.rulestype (warehousecreatedtimestamp);
CREATE INDEX idx_rulestype_warehouselastupdatedtimestamp ON rules.rulestype (warehouselastupdatedtimestamp);
CREATE INDEX idx_rules_effectivefromdate ON rules.rules (effectivefromdate);
CREATE INDEX idx_rules_effectivetodate ON rules.rules (effectivetodate);
CREATE INDEX idx_rules_warehousecreatedtimestamp ON rules.rules (warehousecreatedtimestamp);
CREATE INDEX idx_rules_warehouselastupdatedtimestamp ON rules.rules (warehouselastupdatedtimestamp);

CREATE INDEX idx_rulesxarrangementssecuritytoken_effectivefromdate ON rules.rulesxarrangementssecuritytoken (effectivefromdate);
CREATE INDEX idx_rulesxarrangementssecuritytoken_effectivetodate ON rules.rulesxarrangementssecuritytoken (effectivetodate);
CREATE INDEX idx_rulesxarrangementssecuritytoken_warehousecreatedtimestamp ON rules.rulesxarrangementssecuritytoken (warehousecreatedtimestamp);
CREATE INDEX idx_rulesxarrangementssecuritytoken_warehouselastupdatedtimest ON rules.rulesxarrangementssecuritytoken (warehouselastupdatedtimestamp);
CREATE INDEX idx_rulesxclassification_effectivefromdate ON rules.rulesxclassification (effectivefromdate);
CREATE INDEX idx_rulesxclassification_effectivetodate ON rules.rulesxclassification (effectivetodate);
CREATE INDEX idx_rulesxclassification_warehousecreatedtimestamp ON rules.rulesxclassification (warehousecreatedtimestamp);
CREATE INDEX idx_rulesxclassification_warehouselastupdatedtimestamp ON rules.rulesxclassification (warehouselastupdatedtimestamp);
CREATE INDEX idx_rulesxarrangement_effectivefromdate ON rules.rulesxarrangement (effectivefromdate);
CREATE INDEX idx_rulesxarrangement_effectivetodate ON rules.rulesxarrangement (effectivetodate);
CREATE INDEX idx_rulesxarrangement_warehousecreatedtimestamp ON rules.rulesxarrangement (warehousecreatedtimestamp);
CREATE INDEX idx_rulesxarrangement_warehouselastupdatedtimestamp ON rules.rulesxarrangement (warehouselastupdatedtimestamp);
CREATE INDEX idx_rulestypexresourceitem_effectivefromdate ON rules.rulestypexresourceitem (effectivefromdate);
CREATE INDEX idx_rulestypexresourceitem_effectivetodate ON rules.rulestypexresourceitem (effectivetodate);
CREATE INDEX idx_rulestypexresourceitem_warehousecreatedtimestamp ON rules.rulestypexresourceitem (warehousecreatedtimestamp);
CREATE INDEX idx_rulestypexresourceitem_warehouselastupdatedtimestamp ON rules.rulestypexresourceitem (warehouselastupdatedtimestamp);
CREATE INDEX idx_rulesxclassificationsecuritytoken_effectivefromdate ON rules.rulesxclassificationsecuritytoken (effectivefromdate);
CREATE INDEX idx_rulesxclassificationsecuritytoken_effectivetodate ON rules.rulesxclassificationsecuritytoken (effectivetodate);
CREATE INDEX idx_rulesxclassificationsecuritytoken_warehousecreatedtimestam ON rules.rulesxclassificationsecuritytoken (warehousecreatedtimestamp);
CREATE INDEX idx_rulesxclassificationsecuritytoken_warehouselastupdatedtime ON rules.rulesxclassificationsecuritytoken (warehouselastupdatedtimestamp);
CREATE INDEX idx_rulesxresourceitemsecuritytoken_effectivefromdate ON rules.rulesxresourceitemsecuritytoken (effectivefromdate);
CREATE INDEX idx_rulesxresourceitemsecuritytoken_effectivetodate ON rules.rulesxresourceitemsecuritytoken (effectivetodate);
CREATE INDEX idx_rulesxresourceitemsecuritytoken_warehousecreatedtimestamp ON rules.rulesxresourceitemsecuritytoken (warehousecreatedtimestamp);
CREATE INDEX idx_rulesxresourceitemsecuritytoken_warehouselastupdatedtimest ON rules.rulesxresourceitemsecuritytoken (warehouselastupdatedtimestamp);
CREATE INDEX idx_rulesxproduct_effectivefromdate ON rules.rulesxproduct (effectivefromdate);
CREATE INDEX idx_rulesxproduct_effectivetodate ON rules.rulesxproduct (effectivetodate);
CREATE INDEX idx_rulesxproduct_warehousecreatedtimestamp ON rules.rulesxproduct (warehousecreatedtimestamp);
CREATE INDEX idx_rulesxproduct_warehouselastupdatedtimestamp ON rules.rulesxproduct (warehouselastupdatedtimestamp);
CREATE INDEX idx_rulesxinvolvedpartysecuritytoken_effectivefromdate ON rules.rulesxinvolvedpartysecuritytoken (effectivefromdate);
CREATE INDEX idx_rulesxinvolvedpartysecuritytoken_effectivetodate ON rules.rulesxinvolvedpartysecuritytoken (effectivetodate);
CREATE INDEX idx_rulesxinvolvedpartysecuritytoken_warehousecreatedtimestamp ON rules.rulesxinvolvedpartysecuritytoken (warehousecreatedtimestamp);
CREATE INDEX idx_rulesxinvolvedpartysecuritytoken_warehouselastupdatedtimes ON rules.rulesxinvolvedpartysecuritytoken (warehouselastupdatedtimestamp);
CREATE INDEX idx_rulesxrulestypesecuritytoken_effectivefromdate ON rules.rulesxrulestypesecuritytoken (effectivefromdate);
CREATE INDEX idx_rulesxrulestypesecuritytoken_effectivetodate ON rules.rulesxrulestypesecuritytoken (effectivetodate);
CREATE INDEX idx_rulesxrulestypesecuritytoken_warehousecreatedtimestamp ON rules.rulesxrulestypesecuritytoken (warehousecreatedtimestamp);
CREATE INDEX idx_rulesxrulestypesecuritytoken_warehouselastupdatedtimestamp ON rules.rulesxrulestypesecuritytoken (warehouselastupdatedtimestamp);

CREATE INDEX idx_rulesxresourceitem_effectivefromdate ON rules.rulesxresourceitem (effectivefromdate);
CREATE INDEX idx_rulesxresourceitem_effectivetodate ON rules.rulesxresourceitem (effectivetodate);
CREATE INDEX idx_rulesxresourceitem_warehousecreatedtimestamp ON rules.rulesxresourceitem (warehousecreatedtimestamp);
CREATE INDEX idx_rulesxresourceitem_warehouselastupdatedtimestamp ON rules.rulesxresourceitem (warehouselastupdatedtimestamp);
CREATE INDEX idx_rulesxrules_effectivefromdate ON rules.rulesxrules (effectivefromdate);
CREATE INDEX idx_rulesxrules_effectivetodate ON rules.rulesxrules (effectivetodate);
CREATE INDEX idx_rulesxrules_warehousecreatedtimestamp ON rules.rulesxrules (warehousecreatedtimestamp);
CREATE INDEX idx_rulesxrules_warehouselastupdatedtimestamp ON rules.rulesxrules (warehouselastupdatedtimestamp);
CREATE INDEX idx_rulesxrulessecuritytoken_effectivefromdate ON rules.rulesxrulessecuritytoken (effectivefromdate);
CREATE INDEX idx_rulesxrulessecuritytoken_effectivetodate ON rules.rulesxrulessecuritytoken (effectivetodate);
CREATE INDEX idx_rulesxrulessecuritytoken_warehousecreatedtimestamp ON rules.rulesxrulessecuritytoken (warehousecreatedtimestamp);
CREATE INDEX idx_rulesxrulessecuritytoken_warehouselastupdatedtimestamp ON rules.rulesxrulessecuritytoken (warehouselastupdatedtimestamp);
CREATE INDEX idx_rulesxrulestype_effectivefromdate ON rules.rulesxrulestype (effectivefromdate);
CREATE INDEX idx_rulesxrulestype_effectivetodate ON rules.rulesxrulestype (effectivetodate);
CREATE INDEX idx_rulesxrulestype_warehousecreatedtimestamp ON rules.rulesxrulestype (warehousecreatedtimestamp);
CREATE INDEX idx_rulesxrulestype_warehouselastupdatedtimestamp ON rules.rulesxrulestype (warehouselastupdatedtimestamp);

CREATE INDEX idx_rulesxproductsecuritytoken_effectivefromdate ON rules.rulesxproductsecuritytoken (effectivefromdate);
CREATE INDEX idx_rulesxproductsecuritytoken_effectivetodate ON rules.rulesxproductsecuritytoken (effectivetodate);
CREATE INDEX idx_rulesxproductsecuritytoken_warehousecreatedtimestamp ON rules.rulesxproductsecuritytoken (warehousecreatedtimestamp);
CREATE INDEX idx_rulesxproductsecuritytoken_warehouselastupdatedtimestamp ON rules.rulesxproductsecuritytoken (warehouselastupdatedtimestamp);


CREATE INDEX idx_rulesxinvolvedparty_value ON rules.rulesxinvolvedparty (value);
CREATE INDEX idx_rulestypexclassification_value ON rules.rulestypexclassification (value);

CREATE INDEX idx_rulesxclassification_value ON rules.rulesxclassification (value);
CREATE INDEX idx_rulesxarrangement_value ON rules.rulesxarrangement (value);
CREATE INDEX idx_rulestypexresourceitem_value ON rules.rulestypexresourceitem (value);
CREATE INDEX idx_rulesxproduct_value ON rules.rulesxproduct (value);

CREATE INDEX idx_rulesxresourceitem_value ON rules.rulesxresourceitem (value);
CREATE INDEX idx_rulesxrules_value ON rules.rulesxrules (value);
CREATE INDEX idx_rulesxrulestype_value ON rules.rulesxrulestype (value);

CREATE INDEX idx_rulestype_rulestypedesc ON rules.rulestype (rulestypedesc);
CREATE INDEX idx_rulestype_rulestypename ON rules.rulestype (rulestypename);
CREATE INDEX idx_rules_rulesetname ON rules.rules (rulesetname);


create index fkbpbgfk1w8xjpcvla2wtr28rfm_enterpriseidwhcd on rules.rules (enterpriseid, warehousefromdate);
create index fkihoftg6akowf7mjyheyfmty0i_systemidwhcd on rules.rules (systemid, warehousefromdate);
create index fknh64xmjpd3alokexjnuqr0unw_activeflagidwhcd on rules.rules (activeflagid, warehousefromdate);
create index fkqkhxbrmhfk0ou8sfgrq6w04yn_originalsourcesystemidwhcd on rules.rules (originalsourcesystemid, warehousefromdate);
create index fk57dd0fcy24o8k2spnqrykgk2a_enterpriseidwhcd on rules.rulessecuritytoken (enterpriseid, warehousefromdate);
create index fk5hjfvbnd3js59ca3fowm88h6t_originalsourcesystemidwhcd on rules.rulessecuritytoken (originalsourcesystemid, warehousefromdate);
create index fkenyc4oy7p7bpluwr2incgf2m6_systemidwhcd on rules.rulessecuritytoken (systemid, warehousefromdate);
create index fkmsl2uqlxr7q5n3bq6e14nknk_activeflagidwhcd on rules.rulessecuritytoken (activeflagid, warehousefromdate);
create index fkmtk7w2kpskec0ebnwnrmo8rmp_rulesidwhcd on rules.rulessecuritytoken (rulesid, warehousefromdate);
create index fknj1ss0ctynt0u0t9nx5wprick_securitytokenidwhcd on rules.rulessecuritytoken (securitytokenid, warehousefromdate);
create index fk11qj2su8f2sy9nvcjss76yfqm_originalsourcesystemidwhcd on rules.rulestype (originalsourcesystemid, warehousefromdate);
create index fk7v51o36ciw802dj5m82giq97q_enterpriseidwhcd on rules.rulestype (enterpriseid, warehousefromdate);
create index fkf7030hfrlt8o2dxdarmt5ftrp_systemidwhcd on rules.rulestype (systemid, warehousefromdate);
create index fkm7jdyuyikwo9qlfe6qae0bv3q_activeflagidwhcd on rules.rulestype (activeflagid, warehousefromdate);
create index fk12fsootndlsrm2gkytfg2gfw0_securitytokenidwhcd on rules.rulestypessecuritytoken (securitytokenid, warehousefromdate);
create index fk5tck1lwdgo3i3lbwyphxlsilw_activeflagidwhcd on rules.rulestypessecuritytoken (activeflagid, warehousefromdate);
create index fk9aexqcupg3q8il9en4moulmo1_rulestypesidwhcd on rules.rulestypessecuritytoken (rulestypesid, warehousefromdate);
create index fkeciu87p8mk0yiel2t1bwipg9a_systemidwhcd on rules.rulestypessecuritytoken (systemid, warehousefromdate);
create index fkic2121k0p1edg44c55hog6oe6_originalsourcesystemidwhcd on rules.rulestypessecuritytoken (originalsourcesystemid, warehousefromdate);
create index fks5xpq0ecpqefbfuwqkymvncsn_enterpriseidwhcd on rules.rulestypessecuritytoken (enterpriseid, warehousefromdate);
create index fk3fotawtun2be400jw1c5mcxr5_originalsourcesystemidwhcd on rules.rulestypexclassification (originalsourcesystemid, warehousefromdate);
create index fk87mojdqprestm5uqmw925bqf1_rulestypeidwhcd on rules.rulestypexclassification (rulestypeid, warehousefromdate);
create index fk9662odo6kpnj3a66uyqqh9gph_enterpriseidwhcd on rules.rulestypexclassification (enterpriseid, warehousefromdate);
create index fkcmpyykrmpk1epxo9jmicem78i_classificationidwhcd on rules.rulestypexclassification (classificationid, warehousefromdate);
create index fkdck5vk5rjw51e9u8ducstsysg_systemidwhcd on rules.rulestypexclassification (systemid, warehousefromdate);
create index fkl2497yt06thxeacoue8jbbaoh_activeflagidwhcd on rules.rulestypexclassification (activeflagid, warehousefromdate);
create index fk3nsh18vueud5iybj4xk2t4j6r_enterpriseidwhcd on rules.rulestypexclassificationsecuritytoken (enterpriseid, warehousefromdate);
create index fk4rylunbc8qipcw0n020nhab5b_systemidwhcd on rules.rulestypexclassificationsecuritytoken (systemid, warehousefromdate);
create index fkamvwb2lq6eh8s3gobs9amn1ns_activeflagidwhcd on rules.rulestypexclassificationsecuritytoken (activeflagid, warehousefromdate);
create index fkcn088ejtlnauiohbrpvvufegu_securitytokenidwhcd on rules.rulestypexclassificationsecuritytoken (securitytokenid, warehousefromdate);
create index fkmjbouj2e7loyrlu87avtm4gtm_originalsourcesystemidwhcd on rules.rulestypexclassificationsecuritytoken (originalsourcesystemid, warehousefromdate);
create index fknn81qs2b5rstr3u748lvjbl61_rulestypexclassificationidwhcd on rules.rulestypexclassificationsecuritytoken (rulestypexclassificationid, warehousefromdate);
create index fk6hivvn41g61n8v941egrae19i_rulestypeidwhcd on rules.rulestypexresourceitem (rulestypeid, warehousefromdate);
create index fk7an7eg5j3seoaxajls3knmrwt_classificationidwhcd on rules.rulestypexresourceitem (classificationid, warehousefromdate);
create index fkkl9dib27d8h3g00pp40q3vpw4_enterpriseidwhcd on rules.rulestypexresourceitem (enterpriseid, warehousefromdate);
create index fko8drrcbtv7to8utrafqb942a_systemidwhcd on rules.rulestypexresourceitem (systemid, warehousefromdate);
create index fkqygy5t9dhj5r3nefikdhaxdb6_activeflagidwhcd on rules.rulestypexresourceitem (activeflagid, warehousefromdate);
create index fkrdh9f21yu45alpi37mrexx5sy_resourceitemidwhcd on rules.rulestypexresourceitem (resourceitemid, warehousefromdate);
create index fksbm8nex7yud5ymui9t5m2wuiw_originalsourcesystemidwhcd on rules.rulestypexresourceitem (originalsourcesystemid, warehousefromdate);
create index fk8xs3tib17edep24d44482bmjp_enterpriseidwhcd on rules.rulestypexresourceitemsecuritytoken (enterpriseid, warehousefromdate);
create index fkcdu908ybhdghbjvhquhpfucsr_activeflagidwhcd on rules.rulestypexresourceitemsecuritytoken (activeflagid, warehousefromdate);
create index fklhnbp8n1wy13okhhtnxwqu5hy_systemidwhcd on rules.rulestypexresourceitemsecuritytoken (systemid, warehousefromdate);
create index fkm6jgd6lrdywbn56w6e0s5awd1_securitytokenidwhcd on rules.rulestypexresourceitemsecuritytoken (securitytokenid, warehousefromdate);
create index fkotlbdpur4kwiwymopsenwumbg_rulestypexresourceitemidwhcd on rules.rulestypexresourceitemsecuritytoken (rulestypexresourceitemid, warehousefromdate);
create index fkrbpyq0298hcisaevbl3mp0m5d_originalsourcesystemidwhcd on rules.rulestypexresourceitemsecuritytoken (originalsourcesystemid, warehousefromdate);
create index fk4nj9r8fu6b1ymd9o8tdryxrtx_originalsourcesystemidwhcd on rules.rulesxarrangement (originalsourcesystemid, warehousefromdate);
create index fk7kbnsdojwukxxly9vxqkraun2_activeflagidwhcd on rules.rulesxarrangement (activeflagid, warehousefromdate);
create index fk7kfpfoyfd5mn1x45215dlee28_systemidwhcd on rules.rulesxarrangement (systemid, warehousefromdate);
create index fk9gfetl178gc0bjfak6tny0keu_arrangementidwhcd on rules.rulesxarrangement (arrangementid, warehousefromdate);
create index fkeu7bn1gb6dg7xt097ognxjwh5_enterpriseidwhcd on rules.rulesxarrangement (enterpriseid, warehousefromdate);
create index fkfj9ondul2lkfugd0u6vdxq1yi_rulesidwhcd on rules.rulesxarrangement (rulesid, warehousefromdate);
create index fkrkfqm429ef4j7jwneu7jxeye4_classificationidwhcd on rules.rulesxarrangement (classificationid, warehousefromdate);
create index fk2xvs1pi7rc32lemnyvj01j301_enterpriseidwhcd on rules.rulesxarrangementssecuritytoken (enterpriseid, warehousefromdate);
create index fk4dxpxc1hj2n1ik4s3ms69w3kw_activeflagidwhcd on rules.rulesxarrangementssecuritytoken (activeflagid, warehousefromdate);
create index fkd1ylioumsfoo6scfb0fdouur7_securitytokenidwhcd on rules.rulesxarrangementssecuritytoken (securitytokenid, warehousefromdate);
create index fkflgh5an92uoxffkehd7g7tp8j_originalsourcesystemidwhcd on rules.rulesxarrangementssecuritytoken (originalsourcesystemid, warehousefromdate);
create index fks5w3o4wg63awg0iuc76km8e1n_rulesxarrangementsidwhcd on rules.rulesxarrangementssecuritytoken (rulesxarrangementsid, warehousefromdate);
create index fkte09hu3a7vurplykt3w97714k_systemidwhcd on rules.rulesxarrangementssecuritytoken (systemid, warehousefromdate);
create index fk38k4xit5ty330vs01iv18sikh_enterpriseidwhcd on rules.rulesxclassification (enterpriseid, warehousefromdate);
create index fk7nyvj635fp8si7pq0qetkfhpb_systemidwhcd on rules.rulesxclassification (systemid, warehousefromdate);
create index fk8txg0h4hi98qbgslaodff7dyt_activeflagidwhcd on rules.rulesxclassification (activeflagid, warehousefromdate);
create index fkbbvhygyarc5j4gi7wjnd052p3_rulesidwhcd on rules.rulesxclassification (rulesid, warehousefromdate);
create index fkiyjrx7w183fwo2nhmwpqh0stw_classificationidwhcd on rules.rulesxclassification (classificationid, warehousefromdate);
create index fklog5hewe9u4c7wb35u4xvv84c_originalsourcesystemidwhcd on rules.rulesxclassification (originalsourcesystemid, warehousefromdate);
create index fkb0vp45d3tkxwvy5938ua9n809_activeflagidwhcd on rules.rulesxclassificationsecuritytoken (activeflagid, warehousefromdate);
create index fkj60pj71vbrlqc7ijopnpprev7_securitytokenidwhcd on rules.rulesxclassificationsecuritytoken (securitytokenid, warehousefromdate);
create index fkjklxc6ukplqhj7wa03ah80qh0_originalsourcesystemidwhcd on rules.rulesxclassificationsecuritytoken (originalsourcesystemid, warehousefromdate);
create index fkmno9p3edeufft6bgiv94tecpa_systemidwhcd on rules.rulesxclassificationsecuritytoken (systemid, warehousefromdate);
create index fkowvdqrq0fiqefy3g85fj60rgq_rulesxclassificationidwhcd on rules.rulesxclassificationsecuritytoken (rulesxclassificationid, warehousefromdate);
create index fkq49rw6kbi9gykntudl1mdodl7_enterpriseidwhcd on rules.rulesxclassificationsecuritytoken (enterpriseid, warehousefromdate);
create index fk3djrrw59r13qf53lxmls4da7g_involvedpartyidwhcd on rules.rulesxinvolvedparty (involvedpartyid, warehousefromdate);
create index fk5ujdw4x8upupcaml412wan0p7_originalsourcesystemidwhcd on rules.rulesxinvolvedparty (originalsourcesystemid, warehousefromdate);
create index fk611oa399omtv631o66ausvb55_enterpriseidwhcd on rules.rulesxinvolvedparty (enterpriseid, warehousefromdate);
create index fk8i91cawihcqkxe2fr9oawrshw_systemidwhcd on rules.rulesxinvolvedparty (systemid, warehousefromdate);
create index fkj8719e5kkmd7kd8bpqr71rt3a_classificationidwhcd on rules.rulesxinvolvedparty (classificationid, warehousefromdate);
create index fkqcugfk4tgg63qe2e7fk4h1jou_rulesidwhcd on rules.rulesxinvolvedparty (rulesid, warehousefromdate);
create index fksqp0envk694667mbnl8hndily_activeflagidwhcd on rules.rulesxinvolvedparty (activeflagid, warehousefromdate);
create index fk3qdk5vxgo4txkaaj5a8y8giwd_systemidwhcd on rules.rulesxinvolvedpartysecuritytoken (systemid, warehousefromdate);
create index fk7lvh9pa4gweufl5x0ubtl38my_securitytokenidwhcd on rules.rulesxinvolvedpartysecuritytoken (securitytokenid, warehousefromdate);
create index fk903el1e8i3wxjag53njlscvkg_originalsourcesystemidwhcd on rules.rulesxinvolvedpartysecuritytoken (originalsourcesystemid, warehousefromdate);
create index fkg0ntb8lcpw9n6p49kpubuyle2_rulesxinvolvedpartyidwhcd on rules.rulesxinvolvedpartysecuritytoken (rulesxinvolvedpartyid, warehousefromdate);
create index fkju1w7gcf2bu52ok8ibtd40gk4_enterpriseidwhcd on rules.rulesxinvolvedpartysecuritytoken (enterpriseid, warehousefromdate);
create index fkunriof269jly3lkmqk8jsp4g_activeflagidwhcd on rules.rulesxinvolvedpartysecuritytoken (activeflagid, warehousefromdate);
create index fk57dubv678gaj1eatqjof0x0nb_classificationidwhcd on rules.rulesxproduct (classificationid, warehousefromdate);
create index fk5m9wrb374b5g1wpwkj3jr09y6_activeflagidwhcd on rules.rulesxproduct (activeflagid, warehousefromdate);
create index fk6a018wimk1mn1ucjsg7m6v42e_enterpriseidwhcd on rules.rulesxproduct (enterpriseid, warehousefromdate);
create index fk9t2smyfxndgfnuoht0l8q42rb_systemidwhcd on rules.rulesxproduct (systemid, warehousefromdate);
create index fkf3t7xp42nph7ypmy38rtkr9c8_rulesidwhcd on rules.rulesxproduct (rulesid, warehousefromdate);
create index fkr9hc797wqs7k8u8iun1psqhi0_productidwhcd on rules.rulesxproduct (productid, warehousefromdate);
create index fkrwm05r7gimvp4itjeem7u6sk6_originalsourcesystemidwhcd on rules.rulesxproduct (originalsourcesystemid, warehousefromdate);
create index fk1inr52n9q3ax5rhqtjhthh78r_rulesxproductidwhcd on rules.rulesxproductsecuritytoken (rulesxproductid, warehousefromdate);
create index fk4g0qn2ufofwya9mua7k1bip3n_enterpriseidwhcd on rules.rulesxproductsecuritytoken (enterpriseid, warehousefromdate);
create index fk50a41rvtkyewefjmf4e8l52sw_securitytokenidwhcd on rules.rulesxproductsecuritytoken (securitytokenid, warehousefromdate);
create index fka1ai0uri46j0quxeqcikkdclv_systemidwhcd on rules.rulesxproductsecuritytoken (systemid, warehousefromdate);
create index fkgenrresjjjhv02rh0slya5d88_activeflagidwhcd on rules.rulesxproductsecuritytoken (activeflagid, warehousefromdate);
create index fklaeiodujthn91pwnj8qag1ao6_originalsourcesystemidwhcd on rules.rulesxproductsecuritytoken (originalsourcesystemid, warehousefromdate);
create index fk19cj3ortt8x7wa4mnkohkpebi_rulesidwhcd on rules.rulesxresourceitem (rulesid, warehousefromdate);
create index fk41dmnx4ijklqr44tlpomg7it5_classificationidwhcd on rules.rulesxresourceitem (classificationid, warehousefromdate);
create index fk5fe2nqc5oe2o0pqnptrrciyra_enterpriseidwhcd on rules.rulesxresourceitem (enterpriseid, warehousefromdate);
create index fkhubiaglxhl6fy8gx1oei33qbc_activeflagidwhcd on rules.rulesxresourceitem (activeflagid, warehousefromdate);
create index fkr1sg6ky0tcfxt3faiqbpvq72o_originalsourcesystemidwhcd on rules.rulesxresourceitem (originalsourcesystemid, warehousefromdate);
create index fktlnk6dgimet9xmad6ajypb5xh_systemidwhcd on rules.rulesxresourceitem (systemid, warehousefromdate);
create index fkvs83cy05gattyxwtir1yyv2s_resourceitemidwhcd on rules.rulesxresourceitem (resourceitemid, warehousefromdate);
create index fk23yuc0ofnmca2ig2dd1wx5ol8_rulesxresourceitemidwhcd on rules.rulesxresourceitemsecuritytoken (rulesxresourceitemid, warehousefromdate);
create index fk5dv1vwep6v9958vjdse4henw0_securitytokenidwhcd on rules.rulesxresourceitemsecuritytoken (securitytokenid, warehousefromdate);
create index fkji8ugnof4vkss37iu1mfuy2l4_originalsourcesystemidwhcd on rules.rulesxresourceitemsecuritytoken (originalsourcesystemid, warehousefromdate);
create index fkju938jbh27xnvuokmjxlnlqos_activeflagidwhcd on rules.rulesxresourceitemsecuritytoken (activeflagid, warehousefromdate);
create index fksm3jfs67p3ly7mjl1jkrualak_systemidwhcd on rules.rulesxresourceitemsecuritytoken (systemid, warehousefromdate);
create index fktlm1dpqcyom9yo1lefe0yqowj_enterpriseidwhcd on rules.rulesxresourceitemsecuritytoken (enterpriseid, warehousefromdate);
create index fk31s09rv16v0yhe5aac7l6ele3_classificationidwhcd on rules.rulesxrules (classificationid, warehousefromdate);
create index fk37pin4ub4l14xqqanw037awej_activeflagidwhcd on rules.rulesxrules (activeflagid, warehousefromdate);
create index fkgivavwvb91qbin4sxepd6wbs5_parentrulesidwhcd on rules.rulesxrules (parentrulesid, warehousefromdate);
create index fkpahnm53g0d7kjo5tfrask49up_childrulesidwhcd on rules.rulesxrules (childrulesid, warehousefromdate);
create index fkqmjt8nooh7ch2vr14euld3bnk_enterpriseidwhcd on rules.rulesxrules (enterpriseid, warehousefromdate);
create index fkrncrmhaj7pk6audwswshmudqj_systemidwhcd on rules.rulesxrules (systemid, warehousefromdate);
create index fkt5ep7dex0ykbws23o3yk8sctu_originalsourcesystemidwhcd on rules.rulesxrules (originalsourcesystemid, warehousefromdate);
create index fk4kklxff6jc1n1bo39g3sdqrix_rulesxrulesidwhcd on rules.rulesxrulessecuritytoken (rulesxrulesid, warehousefromdate);
create index fkfv9sfhe1kdjwa8dgu136k90ut_securitytokenidwhcd on rules.rulesxrulessecuritytoken (securitytokenid, warehousefromdate);
create index fklbxxoxpoelx3t2p1g96nqv90q_enterpriseidwhcd on rules.rulesxrulessecuritytoken (enterpriseid, warehousefromdate);
create index fknwhg52uwedumbo0s6msu4kvu4_systemidwhcd on rules.rulesxrulessecuritytoken (systemid, warehousefromdate);
create index fkohvvumprn80d89qp41tyeesd3_activeflagidwhcd on rules.rulesxrulessecuritytoken (activeflagid, warehousefromdate);
create index fks16mt02lac7ao524fhu5liiys_originalsourcesystemidwhcd on rules.rulesxrulessecuritytoken (originalsourcesystemid, warehousefromdate);
create index fk710wj6aj8es1ht1o9fxdehtua_systemidwhcd on rules.rulesxrulestype (systemid, warehousefromdate);
create index fk8hvei78rfy1byxtb36lwwst06_originalsourcesystemidwhcd on rules.rulesxrulestype (originalsourcesystemid, warehousefromdate);
create index fkbn4oo9asbe39dle7ow6buqvt7_activeflagidwhcd on rules.rulesxrulestype (activeflagid, warehousefromdate);
create index fkbohku2sim3tc8ey86rq8psqpt_rulesidwhcd on rules.rulesxrulestype (rulesid, warehousefromdate);
create index fkdly04cjef5yj3ne8v1uwb8h12_classificationidwhcd on rules.rulesxrulestype (classificationid, warehousefromdate);
create index fkonxshkblvrhnt4fasmfxmnyrf_enterpriseidwhcd on rules.rulesxrulestype (enterpriseid, warehousefromdate);
create index fkrv7w522dypyy838vgcr1r566e_rulestypeidwhcd on rules.rulesxrulestype (rulestypeid, warehousefromdate);
create index fkbn0acfejqvxbal40304ibsbr5_activeflagidwhcd on rules.rulesxrulestypesecuritytoken (activeflagid, warehousefromdate);
create index fkby3adp3oxnvi7q571ij36jj0v_originalsourcesystemidwhcd on rules.rulesxrulestypesecuritytoken (originalsourcesystemid, warehousefromdate);
create index fkibc3w3kdwgur69j3lhbi6l0k5_securitytokenidwhcd on rules.rulesxrulestypesecuritytoken (securitytokenid, warehousefromdate);
create index fkjr2flixl5hw7uqmh9chynqt4h_rulesxrulestypeidwhcd on rules.rulesxrulestypesecuritytoken (rulesxrulestypeid, warehousefromdate);
create index fkm2rtvaw8pdy1l3rvbc9wicom3_enterpriseidwhcd on rules.rulesxrulestypesecuritytoken (enterpriseid, warehousefromdate);
create index fkmst7g4r9cabb1d78ysr4hvppe_systemidwhcd on rules.rulesxrulestypesecuritytoken (systemid, warehousefromdate);

