CREATE SCHEMA resource;
CREATE TABLE resource.resourceitem
(
    resourceitemid                UUID                        NOT NULL primary key,
    effectivefromdate             timestamp(6) with time zone NOT NULL,
    effectivetodate               timestamp(6) with time zone NOT NULL,
    warehousecreatedtimestamp     timestamp(6) with time zone NOT NULL,
    warehousefromdate             DATE                        NOT NULL,

    warehouselastupdatedtimestamp timestamp(6) with time zone NOT NULL,
    originalsourcesystemuniqueid  UUID                        NOT NULL,
    resourceitemdatatype          character varying(150)      NOT NULL,
    activeflagid                  UUID                        NOT NULL,
    enterpriseid                  UUID                        NOT NULL,
    systemid                      UUID                        NOT NULL,
    originalsourcesystemid        UUID                        NOT NULL
);
CREATE TABLE resource.resourceitemdata
(
    resourceitemdataid            UUID                        NOT NULL primary key,
    effectivefromdate             timestamp(6) with time zone NOT NULL,
    effectivetodate               timestamp(6) with time zone NOT NULL,
    warehousecreatedtimestamp     timestamp(6) with time zone NOT NULL,
    warehousefromdate             DATE                        NOT NULL,

    warehouselastupdatedtimestamp timestamp(6) with time zone NOT NULL,
    originalsourcesystemuniqueid  UUID                        NOT NULL,
    resourceitemdata              bytea                       NOT NULL,
    activeflagid                  UUID                        NOT NULL,
    enterpriseid                  UUID                        NOT NULL,
    systemid                      UUID                        NOT NULL,
    originalsourcesystemid        UUID                        NOT NULL,
    resourceitemid                UUID                        NOT NULL
);
CREATE TABLE resource.resourceitemdatasecuritytoken
(
    resourceitemdatasecuritytokenid UUID                        NOT NULL primary key,
    effectivefromdate               timestamp(6) with time zone NOT NULL,
    effectivetodate                 timestamp(6) with time zone NOT NULL,
    warehousecreatedtimestamp       timestamp(6) with time zone NOT NULL,
    warehousefromdate               DATE                        NOT NULL,

    warehouselastupdatedtimestamp   timestamp(6) with time zone NOT NULL,
    createallowed                   INTEGER                     NOT NULL,
    deleteallowed                   INTEGER                     NOT NULL,
    originalsourcesystemuniqueid    UUID                        NOT NULL,
    readallowed                     INTEGER                     NOT NULL,
    updateallowed                   INTEGER                     NOT NULL,
    activeflagid                    UUID                        NOT NULL,
    enterpriseid                    UUID                        NOT NULL,
    originalsourcesystemid          UUID                        NOT NULL,
    securitytokenid                 UUID                        NOT NULL,
    systemid                        UUID                        NOT NULL,
    resourceitemdataid              UUID                        NOT NULL
);
CREATE TABLE resource.resourceitemdataxclassification
(
    resourceitemdataxclassificationid UUID                        NOT NULL primary key,
    effectivefromdate                 timestamp(6) with time zone NOT NULL,
    effectivetodate                   timestamp(6) with time zone NOT NULL,
    warehousecreatedtimestamp         timestamp(6) with time zone NOT NULL,
    warehousefromdate                 DATE                        NOT NULL,

    warehouselastupdatedtimestamp     timestamp(6) with time zone NOT NULL,
    originalsourcesystemuniqueid      UUID                        NOT NULL,
    value                             text                        NOT NULL,
    activeflagid                      UUID                        NOT NULL,
    enterpriseid                      UUID                        NOT NULL,
    systemid                          UUID                        NOT NULL,
    originalsourcesystemid            UUID                        NOT NULL,
    classificationid                  UUID                        NOT NULL,
    resourceitemdataid                UUID                        NOT NULL
);
CREATE TABLE resource.resourceitemdataxclassificationsecuritytoken
(
    resourceitemdataxclassificationsecuritytokenid UUID                        NOT NULL primary key,
    effectivefromdate                              timestamp(6) with time zone NOT NULL,
    effectivetodate                                timestamp(6) with time zone NOT NULL,
    warehousecreatedtimestamp                      timestamp(6) with time zone NOT NULL,
    warehousefromdate                              DATE                        NOT NULL,

    warehouselastupdatedtimestamp                  timestamp(6) with time zone NOT NULL,
    createallowed                                  INTEGER                     NOT NULL,
    deleteallowed                                  INTEGER                     NOT NULL,
    originalsourcesystemuniqueid                   UUID                        NOT NULL,
    readallowed                                    INTEGER                     NOT NULL,
    updateallowed                                  INTEGER                     NOT NULL,
    activeflagid                                   UUID                        NOT NULL,
    enterpriseid                                   UUID                        NOT NULL,
    originalsourcesystemid                         UUID                        NOT NULL,
    securitytokenid                                UUID                        NOT NULL,
    systemid                                       UUID                        NOT NULL,
    resourceitemdataxclassificationid              UUID                        NOT NULL
);
CREATE TABLE resource.resourceitemsecuritytoken
(
    resourceitemsecuritytokenid   UUID                        NOT NULL primary key,
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
    resourceitemid                UUID                        NOT NULL
);
CREATE TABLE resource.resourceitemtype
(
    resourceitemtypeid            UUID                        NOT NULL primary key,
    effectivefromdate             timestamp(6) with time zone NOT NULL,
    effectivetodate               timestamp(6) with time zone NOT NULL,
    warehousecreatedtimestamp     timestamp(6) with time zone NOT NULL,
    warehousefromdate             DATE                        NOT NULL,

    warehouselastupdatedtimestamp timestamp(6) with time zone NOT NULL,
    originalsourcesystemuniqueid  UUID                        NOT NULL,
    resourceitemtypedesc          character varying(255)      NOT NULL,
    resourceitemtypename          character varying(100)      NOT NULL,
    activeflagid                  UUID                        NOT NULL,
    enterpriseid                  UUID                        NOT NULL,
    systemid                      UUID                        NOT NULL,
    originalsourcesystemid        UUID                        NOT NULL
);
CREATE TABLE resource.resourceitemtypesecuritytoken
(
    resourceitemtypesecuritytokenid UUID                        NOT NULL primary key,
    effectivefromdate               timestamp(6) with time zone NOT NULL,
    effectivetodate                 timestamp(6) with time zone NOT NULL,
    warehousecreatedtimestamp       timestamp(6) with time zone NOT NULL,
    warehousefromdate               DATE                        NOT NULL,

    warehouselastupdatedtimestamp   timestamp(6) with time zone NOT NULL,
    createallowed                   INTEGER                     NOT NULL,
    deleteallowed                   INTEGER                     NOT NULL,
    originalsourcesystemuniqueid    UUID                        NOT NULL,
    readallowed                     INTEGER                     NOT NULL,
    updateallowed                   INTEGER                     NOT NULL,
    activeflagid                    UUID                        NOT NULL,
    enterpriseid                    UUID                        NOT NULL,
    originalsourcesystemid          UUID                        NOT NULL,
    securitytokenid                 UUID                        NOT NULL,
    systemid                        UUID                        NOT NULL,
    resourceitemtypeid              UUID                        NOT NULL
);
CREATE TABLE resource.resourceitemxclassification
(
    resourceitemxclassificationid UUID                        NOT NULL primary key,
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
CREATE TABLE resource.resourceitemxclassificationsecuritytoken
(
    resourceitemxclassificationsecuritytokenid UUID                        NOT NULL primary key,
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
    resourceitemxclassificationid              UUID                        NOT NULL
);
CREATE TABLE resource.resourceitemxresourceitem
(
    resourceitemxresourceitemid   UUID                        NOT NULL primary key,
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
    childresourceitemid           UUID                        NOT NULL,
    parentresourceitemid          UUID                        NOT NULL
);
CREATE TABLE resource.resourceitemxresourceitemsecuritytoken
(
    resourceitemxresourceitemsecuritytokenid UUID                        NOT NULL primary key,
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
    resourceitemxresourceitemid              UUID                        NOT NULL
);
CREATE TABLE resource.resourceitemxresourceitemtype
(
    resourceitemxresourceitemtypeid UUID                        NOT NULL primary key,
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
    resourceitemid                  UUID                        NOT NULL,
    resourceitemtypeid              UUID                        NOT NULL
);
CREATE TABLE resource.resourceitemxresourceitemtypesecuritytoken
(
    resourceitemxresourceitemtypesecuritytokenid UUID                        NOT NULL primary key,
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
    resourceitemxresourceitemtypeid              UUID                        NOT NULL
);


alter table resource.resourceitemdata
    alter COLUMN resourceitemdata SET COMPRESSION lz4;
alter table resource.resourceitemdata
    alter COLUMN resourceitemdata SET STORAGE EXTERNAL;


create index fk200sir97l7uhxxs2l0b23ok3r_enterpriseid on resource.resourceitem (enterpriseid);
create index fk8cpmhhiy16t26qcrc94hxyngi_activeflagid on resource.resourceitem (activeflagid);
create index fk94ha303yukem4jhrogbx5e06w_systemid on resource.resourceitem (systemid);
create index fkc07twhlsn4lwl4lmx290qts4r_originalsourcesystemid on resource.resourceitem (originalsourcesystemid);
create index fk1fo3xmni8ec47xrr4ga2eff84_systemid on resource.resourceitemdata (systemid);
create index fkdc81hbiavl5m699l1e4n4qtp_activeflagid on resource.resourceitemdata (activeflagid);
create index fkht0uy53uvuomcxp1m9d76l2f2_resourceitemid on resource.resourceitemdata (resourceitemid);
create index fkigdv809qbjivrueu1pbkvr9j3_enterpriseid on resource.resourceitemdata (enterpriseid);
create index fkole6gj7jyg1pdvt918naap8n5_originalsourcesystemid on resource.resourceitemdata (originalsourcesystemid);
create index fk3w9axr9msmakpp6ferrlkuvdy_activeflagid on resource.resourceitemdatasecuritytoken (activeflagid);
create index fkfx83b2i5f4bu61o46gd6jpq3m_resourceitemdataid on resource.resourceitemdatasecuritytoken (resourceitemdataid);
create index fkhcwacfqh36t292fibcvgdtr3m_originalsourcesystemid on resource.resourceitemdatasecuritytoken (originalsourcesystemid);
create index fkhpb19eadjrkws43r16fgy3mjl_enterpriseid on resource.resourceitemdatasecuritytoken (enterpriseid);
create index fkll81jjs16jumy0yll1ql0c21_systemid on resource.resourceitemdatasecuritytoken (systemid);
create index fkomv4xy8vibg1lxewrtq0lj42t_securitytokenid on resource.resourceitemdatasecuritytoken (securitytokenid);
create index fk2dy40tyg4ideqgywl6lh9gly5_resourceitemdataid on resource.resourceitemdataxclassification (resourceitemdataid);
create index fk663fflx6v3085crppbxdji50p_systemid on resource.resourceitemdataxclassification (systemid);
create index fkiaq7u3ym056htigwqw14ilcsn_originalsourcesystemid on resource.resourceitemdataxclassification (originalsourcesystemid);
create index fkjsk9t2sreiwpv88hanu3khr74_activeflagid on resource.resourceitemdataxclassification (activeflagid);
create index fkox913icfxl6rbqwjw04y7odq6_enterpriseid on resource.resourceitemdataxclassification (enterpriseid);
create index fkp9oe45dlyale0o4ug986voncj_classificationid on resource.resourceitemdataxclassification (classificationid);
create index fk79odxnrnlexs55g44rmjdw9my_resourceitemdataxclassificationid on resource.resourceitemdataxclassificationsecuritytoken (resourceitemdataxclassificationid);
create index fk7phkn8oemhek6rr9400ha8lh3_systemid on resource.resourceitemdataxclassificationsecuritytoken (systemid);
create index fka0xjd26qyqrft0dop3wn7f0np_activeflagid on resource.resourceitemdataxclassificationsecuritytoken (activeflagid);
create index fkab7vvw2nd2839r3whhm76iel_originalsourcesystemid on resource.resourceitemdataxclassificationsecuritytoken (originalsourcesystemid);
create index fkajq33984mmb4dan1b1ujl28qh_securitytokenid on resource.resourceitemdataxclassificationsecuritytoken (securitytokenid);
create index fkb8riptu7wfv9qs6scj4k6bdnh_enterpriseid on resource.resourceitemdataxclassificationsecuritytoken (enterpriseid);
create index fk84jknr1n5cnm1wtlwn07d1r64_securitytokenid on resource.resourceitemsecuritytoken (securitytokenid);
create index fk861bu9d1oka3g8dh0mb85tyc4_resourceitemid on resource.resourceitemsecuritytoken (resourceitemid);
create index fkeut88pwpr6r3by6poh8s3lgn5_activeflagid on resource.resourceitemsecuritytoken (activeflagid);
create index fklri3qjyh3vwi43xe3crxm8xdq_enterpriseid on resource.resourceitemsecuritytoken (enterpriseid);
create index fko0923krxdcp1issfcfcl7a0a7_originalsourcesystemid on resource.resourceitemsecuritytoken (originalsourcesystemid);
create index fktj25xftljow24gev8hl3dsjw8_systemid on resource.resourceitemsecuritytoken (systemid);
create index fk6qq7tnhpgyvvt5vly41nemv50_systemid on resource.resourceitemtype (systemid);
create index fkgxtvsg5e3c8sq9j7y8bx0ib5v_originalsourcesystemid on resource.resourceitemtype (originalsourcesystemid);
create index fkh8avb7iv2w72vgk90sh1d2p4f_enterpriseid on resource.resourceitemtype (enterpriseid);
create index fki9pjt06s4hopvhur99agga4k6_activeflagid on resource.resourceitemtype (activeflagid);
create index fk3p3b9iwh15wwgccqgj3lpysqn_enterpriseid on resource.resourceitemtypesecuritytoken (enterpriseid);
create index fk4cdo57v9c89cmsd70444brarb_systemid on resource.resourceitemtypesecuritytoken (systemid);
create index fk5wdvel5htabl0xh6uo6do4nmw_activeflagid on resource.resourceitemtypesecuritytoken (activeflagid);
create index fkml0sk2cyf2m3s7w1idm5b1fv2_securitytokenid on resource.resourceitemtypesecuritytoken (securitytokenid);
create index fko2usew1i3g60p11uwfuv2ajmd_originalsourcesystemid on resource.resourceitemtypesecuritytoken (originalsourcesystemid);
create index fkrpcr2w5s8sdyuh9e5bsiqpsl_resourceitemtypeid on resource.resourceitemtypesecuritytoken (resourceitemtypeid);
create index fk3wmuxdwncxa9duotxup8kots8_activeflagid on resource.resourceitemxclassification (activeflagid);
create index fk5ad2d7e8c7kiwjtnfv4baj6k5_classificationid on resource.resourceitemxclassification (classificationid);
create index fk8obyha4m3ud6wg5wugtj0efl3_resourceitemid on resource.resourceitemxclassification (resourceitemid);
create index fkgjx0hpxxegn5wge4pdrchmidh_enterpriseid on resource.resourceitemxclassification (enterpriseid);
create index fkjrhfr20kvirghjffwvd80r39m_originalsourcesystemid on resource.resourceitemxclassification (originalsourcesystemid);
create index fko1fubrmjfvsi58rt1amf7u7fw_systemid on resource.resourceitemxclassification (systemid);
create index fk3i66vwdq4it2rgp85m6d0jq81_activeflagid on resource.resourceitemxclassificationsecuritytoken (activeflagid);
create index fk7ns6uhcbucfbl2uioayhiydj2_securitytokenid on resource.resourceitemxclassificationsecuritytoken (securitytokenid);
create index fk7wx6yqn7nc5fajp9dhi1f2mjx_systemid on resource.resourceitemxclassificationsecuritytoken (systemid);
create index fknvnmjioardv2gat6o4shntlxd_enterpriseid on resource.resourceitemxclassificationsecuritytoken (enterpriseid);
create index fko7a0ts75p7f0b5ekl3e5nr2jx_resourceitemxclassificationid on resource.resourceitemxclassificationsecuritytoken (resourceitemxclassificationid);
create index fkq4lxbn9s7559mqng8hmhau4s4_originalsourcesystemid on resource.resourceitemxclassificationsecuritytoken (originalsourcesystemid);
create index fk2ki967a981nc2wof2y4u5kwtw_originalsourcesystemid on resource.resourceitemxresourceitem (originalsourcesystemid);
create index fk5jtsbc1lwwjo7gimmifc90cxv_parentresourceitemid on resource.resourceitemxresourceitem (parentresourceitemid);
create index fkbb8g8afmpudbornwchcbn8d6x_childresourceitemid on resource.resourceitemxresourceitem (childresourceitemid);
create index fkcwb9vx2pur7f8if9mh62f8yta_systemid on resource.resourceitemxresourceitem (systemid);
create index fkesiucs3j6sufylpxxpldbhwbl_activeflagid on resource.resourceitemxresourceitem (activeflagid);
create index fkgguetxp2rpaak5nhtais4qipi_enterpriseid on resource.resourceitemxresourceitem (enterpriseid);
create index fkoyv9alf1u6nf5wbkwvj207pd6_classificationid on resource.resourceitemxresourceitem (classificationid);
create index fk1n6u8l8jhlesanln4et9jehgh_resourceitemxresourceitemid on resource.resourceitemxresourceitemsecuritytoken (resourceitemxresourceitemid);
create index fk6sn4lyt8rfdltu9cbblt5uieh_securitytokenid on resource.resourceitemxresourceitemsecuritytoken (securitytokenid);
create index fk8qo9i93o20ejx9qm1wys3xwmt_originalsourcesystemid on resource.resourceitemxresourceitemsecuritytoken (originalsourcesystemid);
create index fkexfo0hib3thx9lan7x40hj7gr_systemid on resource.resourceitemxresourceitemsecuritytoken (systemid);
create index fkm9ls44frfnxy77f2yitjmfb4w_enterpriseid on resource.resourceitemxresourceitemsecuritytoken (enterpriseid);
create index fkmylvnsbdyx59j60lktojr074f_activeflagid on resource.resourceitemxresourceitemsecuritytoken (activeflagid);
create index fkcvs3qfiioi6vtrvsx9fydc5w9_originalsourcesystemid on resource.resourceitemxresourceitemtype (originalsourcesystemid);
create index fkg86r3f2ftu2ex3xkplrd9tar7_resourceitemid on resource.resourceitemxresourceitemtype (resourceitemid);
create index fklsy82htwl5f124qkuxb5cuq8y_activeflagid on resource.resourceitemxresourceitemtype (activeflagid);
create index fkpirwchdhynd810k5mp231e2sh_resourceitemtypeid on resource.resourceitemxresourceitemtype (resourceitemtypeid);
create index fkqbn3rldlgiftql2sxfoc020k7_classificationid on resource.resourceitemxresourceitemtype (classificationid);
create index fkr19kv8ueg4bkxh2pvj156qvwk_systemid on resource.resourceitemxresourceitemtype (systemid);
create index fksw4ntk83q20lccxb0t56uvvun_enterpriseid on resource.resourceitemxresourceitemtype (enterpriseid);
create index fk2ep9v4fv470h60ye8kq7lfybi_activeflagid on resource.resourceitemxresourceitemtypesecuritytoken (activeflagid);
create index fkc63ne5lom1f43u7t6lfi6imtn_originalsourcesystemid on resource.resourceitemxresourceitemtypesecuritytoken (originalsourcesystemid);
create index fkchn8hstbo7rkgh5817jqqcxpx_securitytokenid on resource.resourceitemxresourceitemtypesecuritytoken (securitytokenid);
create index fki8ap9tegp5e5e2687a8rlolq3_systemid on resource.resourceitemxresourceitemtypesecuritytoken (systemid);
create index fkiwc4nixnum6cvlrtlpopjlrrp_resourceitemxresourceitemtypeid on resource.resourceitemxresourceitemtypesecuritytoken (resourceitemxresourceitemtypeid);
create index fkkj0hvw87ec6j3mlt4m96ky91p_enterpriseid on resource.resourceitemxresourceitemtypesecuritytoken (enterpriseid);


CREATE INDEX idx_resourceitem_effectivefromdate ON resource.resourceitem (effectivefromdate);
CREATE INDEX idx_resourceitem_effectivetodate ON resource.resourceitem (effectivetodate);
CREATE INDEX idx_resourceitem_warehousecreatedtimestamp ON resource.resourceitem (warehousecreatedtimestamp);
CREATE INDEX idx_resourceitem_warehouselastupdatedtimestamp ON resource.resourceitem (warehouselastupdatedtimestamp);

CREATE INDEX idx_resourceitemxresourceitemtypesecuritytoken_effectivefromda ON resource.resourceitemxresourceitemtypesecuritytoken (effectivefromdate);
CREATE INDEX idx_resourceitemxresourceitemtypesecuritytoken_effectivetodate ON resource.resourceitemxresourceitemtypesecuritytoken (effectivetodate);
CREATE INDEX idx_resourceitemxresourceitemtypesecuritytoken_warehousecreate ON resource.resourceitemxresourceitemtypesecuritytoken (warehousecreatedtimestamp);
CREATE INDEX idx_resourceitemxresourceitemtypesecuritytoken_warehouselastup ON resource.resourceitemxresourceitemtypesecuritytoken (warehouselastupdatedtimestamp);
CREATE INDEX idx_resourceitemtype_effectivefromdate ON resource.resourceitemtype (effectivefromdate);
CREATE INDEX idx_resourceitemtype_effectivetodate ON resource.resourceitemtype (effectivetodate);
CREATE INDEX idx_resourceitemtype_warehousecreatedtimestamp ON resource.resourceitemtype (warehousecreatedtimestamp);
CREATE INDEX idx_resourceitemtype_warehouselastupdatedtimestamp ON resource.resourceitemtype (warehouselastupdatedtimestamp);
CREATE INDEX idx_resourceitemdataxclassification_effectivefromdate ON resource.resourceitemdataxclassification (effectivefromdate);
CREATE INDEX idx_resourceitemdataxclassification_effectivetodate ON resource.resourceitemdataxclassification (effectivetodate);
CREATE INDEX idx_resourceitemdataxclassification_warehousecreatedtimestamp ON resource.resourceitemdataxclassification (warehousecreatedtimestamp);
CREATE INDEX idx_resourceitemdataxclassification_warehouselastupdatedtimest ON resource.resourceitemdataxclassification (warehouselastupdatedtimestamp);

CREATE INDEX idx_resourceitemdata_effectivefromdate ON resource.resourceitemdata (effectivefromdate);
CREATE INDEX idx_resourceitemdata_effectivetodate ON resource.resourceitemdata (effectivetodate);
CREATE INDEX idx_resourceitemdata_warehousecreatedtimestamp ON resource.resourceitemdata (warehousecreatedtimestamp);
CREATE INDEX idx_resourceitemdata_warehouselastupdatedtimestamp ON resource.resourceitemdata (warehouselastupdatedtimestamp);
CREATE INDEX idx_resourceitemxresourceitem_effectivefromdate ON resource.resourceitemxresourceitem (effectivefromdate);
CREATE INDEX idx_resourceitemxresourceitem_effectivetodate ON resource.resourceitemxresourceitem (effectivetodate);
CREATE INDEX idx_resourceitemxresourceitem_warehousecreatedtimestamp ON resource.resourceitemxresourceitem (warehousecreatedtimestamp);
CREATE INDEX idx_resourceitemxresourceitem_warehouselastupdatedtimestamp ON resource.resourceitemxresourceitem (warehouselastupdatedtimestamp);
CREATE INDEX idx_resourceitemxclassificationsecuritytoken_effectivefromdate ON resource.resourceitemxclassificationsecuritytoken (effectivefromdate);
CREATE INDEX idx_resourceitemxclassificationsecuritytoken_effectivetodate ON resource.resourceitemxclassificationsecuritytoken (effectivetodate);
CREATE INDEX idx_resourceitemxclassificationsecuritytoken_warehousecreatedt ON resource.resourceitemxclassificationsecuritytoken (warehousecreatedtimestamp);
CREATE INDEX idx_resourceitemxclassificationsecuritytoken_warehouselastupda ON resource.resourceitemxclassificationsecuritytoken (warehouselastupdatedtimestamp);
CREATE INDEX idx_resourceitemtypesecuritytoken_effectivefromdate ON resource.resourceitemtypesecuritytoken (effectivefromdate);
CREATE INDEX idx_resourceitemtypesecuritytoken_effectivetodate ON resource.resourceitemtypesecuritytoken (effectivetodate);
CREATE INDEX idx_resourceitemtypesecuritytoken_warehousecreatedtimestamp ON resource.resourceitemtypesecuritytoken (warehousecreatedtimestamp);
CREATE INDEX idx_resourceitemtypesecuritytoken_warehouselastupdatedtimestam ON resource.resourceitemtypesecuritytoken (warehouselastupdatedtimestamp);
CREATE INDEX idx_resourceitemdatasecuritytoken_effectivefromdate ON resource.resourceitemdatasecuritytoken (effectivefromdate);
CREATE INDEX idx_resourceitemdatasecuritytoken_effectivetodate ON resource.resourceitemdatasecuritytoken (effectivetodate);
CREATE INDEX idx_resourceitemdatasecuritytoken_warehousecreatedtimestamp ON resource.resourceitemdatasecuritytoken (warehousecreatedtimestamp);
CREATE INDEX idx_resourceitemdatasecuritytoken_warehouselastupdatedtimestam ON resource.resourceitemdatasecuritytoken (warehouselastupdatedtimestamp);
CREATE INDEX idx_resourceitemxclassification_effectivefromdate ON resource.resourceitemxclassification (effectivefromdate);
CREATE INDEX idx_resourceitemxclassification_effectivetodate ON resource.resourceitemxclassification (effectivetodate);
CREATE INDEX idx_resourceitemxclassification_warehousecreatedtimestamp ON resource.resourceitemxclassification (warehousecreatedtimestamp);
CREATE INDEX idx_resourceitemxclassification_warehouselastupdatedtimestamp ON resource.resourceitemxclassification (warehouselastupdatedtimestamp);
CREATE INDEX idx_resourceitemdataxclassificationsecuritytoken_effectivefrom ON resource.resourceitemdataxclassificationsecuritytoken (effectivefromdate);
CREATE INDEX idx_resourceitemdataxclassificationsecuritytoken_effectivetoda ON resource.resourceitemdataxclassificationsecuritytoken (effectivetodate);
CREATE INDEX idx_resourceitemdataxclassificationsecuritytoken_warehousecrea ON resource.resourceitemdataxclassificationsecuritytoken (warehousecreatedtimestamp);
CREATE INDEX idx_resourceitemdataxclassificationsecuritytoken_warehouselast ON resource.resourceitemdataxclassificationsecuritytoken (warehouselastupdatedtimestamp);
CREATE INDEX idx_resourceitemsecuritytoken_effectivefromdate ON resource.resourceitemsecuritytoken (effectivefromdate);
CREATE INDEX idx_resourceitemsecuritytoken_effectivetodate ON resource.resourceitemsecuritytoken (effectivetodate);
CREATE INDEX idx_resourceitemsecuritytoken_warehousecreatedtimestamp ON resource.resourceitemsecuritytoken (warehousecreatedtimestamp);
CREATE INDEX idx_resourceitemsecuritytoken_warehouselastupdatedtimestamp ON resource.resourceitemsecuritytoken (warehouselastupdatedtimestamp);
CREATE INDEX idx_resourceitemxresourceitemtype_effectivefromdate ON resource.resourceitemxresourceitemtype (effectivefromdate);
CREATE INDEX idx_resourceitemxresourceitemtype_effectivetodate ON resource.resourceitemxresourceitemtype (effectivetodate);
CREATE INDEX idx_resourceitemxresourceitemtype_warehousecreatedtimestamp ON resource.resourceitemxresourceitemtype (warehousecreatedtimestamp);
CREATE INDEX idx_resourceitemxresourceitemtype_warehouselastupdatedtimestam ON resource.resourceitemxresourceitemtype (warehouselastupdatedtimestamp);

CREATE INDEX idx_resourceitemxresourceitemsecuritytoken_effectivefromdate ON resource.resourceitemxresourceitemsecuritytoken (effectivefromdate);
CREATE INDEX idx_resourceitemxresourceitemsecuritytoken_effectivetodate ON resource.resourceitemxresourceitemsecuritytoken (effectivetodate);
CREATE INDEX idx_resourceitemxresourceitemsecuritytoken_warehousecreatedtim ON resource.resourceitemxresourceitemsecuritytoken (warehousecreatedtimestamp);
CREATE INDEX idx_resourceitemxresourceitemsecuritytoken_warehouselastupdate ON resource.resourceitemxresourceitemsecuritytoken (warehouselastupdatedtimestamp);
CREATE INDEX idx_resourceitemxresourceitem_value ON resource.resourceitemxresourceitem (value);
CREATE INDEX idx_resourceitemxclassification_value ON resource.resourceitemxclassification (value);
CREATE INDEX idx_resourceitemxresourceitemtype_value ON resource.resourceitemxresourceitemtype (value);
CREATE INDEX idx_resourceitemdataxclassification_value ON resource.resourceitemdataxclassification (value);

CREATE INDEX idx_resourceitemtype_resourceitemtypedesc ON resource.resourceitemtype (resourceitemtypedesc);
CREATE INDEX idx_resourceitemtype_resourceitemtypename ON resource.resourceitemtype (resourceitemtypename);



create index fk200sir97l7uhxxs2l0b23ok3r_enterpriseidwhcd on resource.resourceitem (enterpriseid, warehousefromdate);
create index fk8cpmhhiy16t26qcrc94hxyngi_activeflagidwhcd on resource.resourceitem (activeflagid, warehousefromdate);
create index fk94ha303yukem4jhrogbx5e06w_systemidwhcd on resource.resourceitem (systemid, warehousefromdate);
create index fkc07twhlsn4lwl4lmx290qts4r_originalsourcesystemidwhcd on resource.resourceitem (originalsourcesystemid, warehousefromdate);
create index fk1fo3xmni8ec47xrr4ga2eff84_systemidwhcd on resource.resourceitemdata (systemid, warehousefromdate);
create index fkdc81hbiavl5m699l1e4n4qtp_activeflagidwhcd on resource.resourceitemdata (activeflagid, warehousefromdate);
create index fkht0uy53uvuomcxp1m9d76l2f2_resourceitemidwhcd on resource.resourceitemdata (resourceitemid, warehousefromdate);
create index fkigdv809qbjivrueu1pbkvr9j3_enterpriseidwhcd on resource.resourceitemdata (enterpriseid, warehousefromdate);
create index fkole6gj7jyg1pdvt918naap8n5_originalsourcesystemidwhcd on resource.resourceitemdata (originalsourcesystemid, warehousefromdate);
create index fk3w9axr9msmakpp6ferrlkuvdy_activeflagidwhcd on resource.resourceitemdatasecuritytoken (activeflagid, warehousefromdate);
create index fkfx83b2i5f4bu61o46gd6jpq3m_resourceitemdataidwhcd on resource.resourceitemdatasecuritytoken (resourceitemdataid, warehousefromdate);
create index fkhcwacfqh36t292fibcvgdtr3m_originalsourcesystemidwhcd on resource.resourceitemdatasecuritytoken (originalsourcesystemid, warehousefromdate);
create index fkhpb19eadjrkws43r16fgy3mjl_enterpriseidwhcd on resource.resourceitemdatasecuritytoken (enterpriseid, warehousefromdate);
create index fkll81jjs16jumy0yll1ql0c21_systemidwhcd on resource.resourceitemdatasecuritytoken (systemid, warehousefromdate);
create index fkomv4xy8vibg1lxewrtq0lj42t_securitytokenidwhcd on resource.resourceitemdatasecuritytoken (securitytokenid, warehousefromdate);
create index fk2dy40tyg4ideqgywl6lh9gly5_resourceitemdataidwhcd on resource.resourceitemdataxclassification (resourceitemdataid, warehousefromdate);
create index fk663fflx6v3085crppbxdji50p_systemidwhcd on resource.resourceitemdataxclassification (systemid, warehousefromdate);
create index fkiaq7u3ym056htigwqw14ilcsn_originalsourcesystemidwhcd on resource.resourceitemdataxclassification (originalsourcesystemid, warehousefromdate);
create index fkjsk9t2sreiwpv88hanu3khr74_activeflagidwhcd on resource.resourceitemdataxclassification (activeflagid, warehousefromdate);
create index fkox913icfxl6rbqwjw04y7odq6_enterpriseidwhcd on resource.resourceitemdataxclassification (enterpriseid, warehousefromdate);
create index fkp9oe45dlyale0o4ug986voncj_classificationidwhcd on resource.resourceitemdataxclassification (classificationid, warehousefromdate);
create index fk79odxnrnlexs55g44rmjdw9my_resourceitemdataxclassificationidwhcd on resource.resourceitemdataxclassificationsecuritytoken (resourceitemdataxclassificationid, warehousefromdate);
create index fk7phkn8oemhek6rr9400ha8lh3_systemidwhcd on resource.resourceitemdataxclassificationsecuritytoken (systemid, warehousefromdate);
create index fka0xjd26qyqrft0dop3wn7f0np_activeflagidwhcd on resource.resourceitemdataxclassificationsecuritytoken (activeflagid, warehousefromdate);
create index fkab7vvw2nd2839r3whhm76iel_originalsourcesystemidwhcd on resource.resourceitemdataxclassificationsecuritytoken (originalsourcesystemid, warehousefromdate);
create index fkajq33984mmb4dan1b1ujl28qh_securitytokenidwhcd on resource.resourceitemdataxclassificationsecuritytoken (securitytokenid, warehousefromdate);
create index fkb8riptu7wfv9qs6scj4k6bdnh_enterpriseidwhcd on resource.resourceitemdataxclassificationsecuritytoken (enterpriseid, warehousefromdate);
create index fk84jknr1n5cnm1wtlwn07d1r64_securitytokenidwhcd on resource.resourceitemsecuritytoken (securitytokenid, warehousefromdate);
create index fk861bu9d1oka3g8dh0mb85tyc4_resourceitemidwhcd on resource.resourceitemsecuritytoken (resourceitemid, warehousefromdate);
create index fkeut88pwpr6r3by6poh8s3lgn5_activeflagidwhcd on resource.resourceitemsecuritytoken (activeflagid, warehousefromdate);
create index fklri3qjyh3vwi43xe3crxm8xdq_enterpriseidwhcd on resource.resourceitemsecuritytoken (enterpriseid, warehousefromdate);
create index fko0923krxdcp1issfcfcl7a0a7_originalsourcesystemidwhcd on resource.resourceitemsecuritytoken (originalsourcesystemid, warehousefromdate);
create index fktj25xftljow24gev8hl3dsjw8_systemidwhcd on resource.resourceitemsecuritytoken (systemid, warehousefromdate);
create index fk6qq7tnhpgyvvt5vly41nemv50_systemidwhcd on resource.resourceitemtype (systemid, warehousefromdate);
create index fkgxtvsg5e3c8sq9j7y8bx0ib5v_originalsourcesystemidwhcd on resource.resourceitemtype (originalsourcesystemid, warehousefromdate);
create index fkh8avb7iv2w72vgk90sh1d2p4f_enterpriseidwhcd on resource.resourceitemtype (enterpriseid, warehousefromdate);
create index fki9pjt06s4hopvhur99agga4k6_activeflagidwhcd on resource.resourceitemtype (activeflagid, warehousefromdate);
create index fk3p3b9iwh15wwgccqgj3lpysqn_enterpriseidwhcd on resource.resourceitemtypesecuritytoken (enterpriseid, warehousefromdate);
create index fk4cdo57v9c89cmsd70444brarb_systemidwhcd on resource.resourceitemtypesecuritytoken (systemid, warehousefromdate);
create index fk5wdvel5htabl0xh6uo6do4nmw_activeflagidwhcd on resource.resourceitemtypesecuritytoken (activeflagid, warehousefromdate);
create index fkml0sk2cyf2m3s7w1idm5b1fv2_securitytokenidwhcd on resource.resourceitemtypesecuritytoken (securitytokenid, warehousefromdate);
create index fko2usew1i3g60p11uwfuv2ajmd_originalsourcesystemidwhcd on resource.resourceitemtypesecuritytoken (originalsourcesystemid, warehousefromdate);
create index fkrpcr2w5s8sdyuh9e5bsiqpsl_resourceitemtypeidwhcd on resource.resourceitemtypesecuritytoken (resourceitemtypeid, warehousefromdate);
create index fk3wmuxdwncxa9duotxup8kots8_activeflagidwhcd on resource.resourceitemxclassification (activeflagid, warehousefromdate);
create index fk5ad2d7e8c7kiwjtnfv4baj6k5_classificationidwhcd on resource.resourceitemxclassification (classificationid, warehousefromdate);
create index fk8obyha4m3ud6wg5wugtj0efl3_resourceitemidwhcd on resource.resourceitemxclassification (resourceitemid, warehousefromdate);
create index fkgjx0hpxxegn5wge4pdrchmidh_enterpriseidwhcd on resource.resourceitemxclassification (enterpriseid, warehousefromdate);
create index fkjrhfr20kvirghjffwvd80r39m_originalsourcesystemidwhcd on resource.resourceitemxclassification (originalsourcesystemid, warehousefromdate);
create index fko1fubrmjfvsi58rt1amf7u7fw_systemidwhcd on resource.resourceitemxclassification (systemid, warehousefromdate);
create index fk3i66vwdq4it2rgp85m6d0jq81_activeflagidwhcd on resource.resourceitemxclassificationsecuritytoken (activeflagid, warehousefromdate);
create index fk7ns6uhcbucfbl2uioayhiydj2_securitytokenidwhcd on resource.resourceitemxclassificationsecuritytoken (securitytokenid, warehousefromdate);
create index fk7wx6yqn7nc5fajp9dhi1f2mjx_systemidwhcd on resource.resourceitemxclassificationsecuritytoken (systemid, warehousefromdate);
create index fknvnmjioardv2gat6o4shntlxd_enterpriseidwhcd on resource.resourceitemxclassificationsecuritytoken (enterpriseid, warehousefromdate);
create index fko7a0ts75p7f0b5ekl3e5nr2jx_resourceitemxclassificationidwhcd on resource.resourceitemxclassificationsecuritytoken (resourceitemxclassificationid, warehousefromdate);
create index fkq4lxbn9s7559mqng8hmhau4s4_originalsourcesystemidwhcd on resource.resourceitemxclassificationsecuritytoken (originalsourcesystemid, warehousefromdate);
create index fk2ki967a981nc2wof2y4u5kwtw_originalsourcesystemidwhcd on resource.resourceitemxresourceitem (originalsourcesystemid, warehousefromdate);
create index fk5jtsbc1lwwjo7gimmifc90cxv_parentresourceitemidwhcd on resource.resourceitemxresourceitem (parentresourceitemid, warehousefromdate);
create index fkbb8g8afmpudbornwchcbn8d6x_childresourceitemidwhcd on resource.resourceitemxresourceitem (childresourceitemid, warehousefromdate);
create index fkcwb9vx2pur7f8if9mh62f8yta_systemidwhcd on resource.resourceitemxresourceitem (systemid, warehousefromdate);
create index fkesiucs3j6sufylpxxpldbhwbl_activeflagidwhcd on resource.resourceitemxresourceitem (activeflagid, warehousefromdate);
create index fkgguetxp2rpaak5nhtais4qipi_enterpriseidwhcd on resource.resourceitemxresourceitem (enterpriseid, warehousefromdate);
create index fkoyv9alf1u6nf5wbkwvj207pd6_classificationidwhcd on resource.resourceitemxresourceitem (classificationid, warehousefromdate);
create index fk1n6u8l8jhlesanln4et9jehgh_resourceitemxresourceitemidwhcd on resource.resourceitemxresourceitemsecuritytoken (resourceitemxresourceitemid, warehousefromdate);
create index fk6sn4lyt8rfdltu9cbblt5uieh_securitytokenidwhcd on resource.resourceitemxresourceitemsecuritytoken (securitytokenid, warehousefromdate);
create index fk8qo9i93o20ejx9qm1wys3xwmt_originalsourcesystemidwhcd on resource.resourceitemxresourceitemsecuritytoken (originalsourcesystemid, warehousefromdate);
create index fkexfo0hib3thx9lan7x40hj7gr_systemidwhcd on resource.resourceitemxresourceitemsecuritytoken (systemid, warehousefromdate);
create index fkm9ls44frfnxy77f2yitjmfb4w_enterpriseidwhcd on resource.resourceitemxresourceitemsecuritytoken (enterpriseid, warehousefromdate);
create index fkmylvnsbdyx59j60lktojr074f_activeflagidwhcd on resource.resourceitemxresourceitemsecuritytoken (activeflagid, warehousefromdate);
create index fkcvs3qfiioi6vtrvsx9fydc5w9_originalsourcesystemidwhcd on resource.resourceitemxresourceitemtype (originalsourcesystemid, warehousefromdate);
create index fkg86r3f2ftu2ex3xkplrd9tar7_resourceitemidwhcd on resource.resourceitemxresourceitemtype (resourceitemid, warehousefromdate);
create index fklsy82htwl5f124qkuxb5cuq8y_activeflagidwhcd on resource.resourceitemxresourceitemtype (activeflagid, warehousefromdate);
create index fkpirwchdhynd810k5mp231e2sh_resourceitemtypeidwhcd on resource.resourceitemxresourceitemtype (resourceitemtypeid, warehousefromdate);
create index fkqbn3rldlgiftql2sxfoc020k7_classificationidwhcd on resource.resourceitemxresourceitemtype (classificationid, warehousefromdate);
create index fkr19kv8ueg4bkxh2pvj156qvwk_systemidwhcd on resource.resourceitemxresourceitemtype (systemid, warehousefromdate);
create index fksw4ntk83q20lccxb0t56uvvun_enterpriseidwhcd on resource.resourceitemxresourceitemtype (enterpriseid, warehousefromdate);
create index fk2ep9v4fv470h60ye8kq7lfybi_activeflagidwhcd on resource.resourceitemxresourceitemtypesecuritytoken (activeflagid, warehousefromdate);
create index fkc63ne5lom1f43u7t6lfi6imtn_originalsourcesystemidwhcd on resource.resourceitemxresourceitemtypesecuritytoken (originalsourcesystemid, warehousefromdate);
create index fkchn8hstbo7rkgh5817jqqcxpx_securitytokenidwhcd on resource.resourceitemxresourceitemtypesecuritytoken (securitytokenid, warehousefromdate);
create index fki8ap9tegp5e5e2687a8rlolq3_systemidwhcd on resource.resourceitemxresourceitemtypesecuritytoken (systemid, warehousefromdate);
create index fkiwc4nixnum6cvlrtlpopjlrrp_resourceitemxresourceitemtypeidwhcd on resource.resourceitemxresourceitemtypesecuritytoken (resourceitemxresourceitemtypeid, warehousefromdate);
create index fkkj0hvw87ec6j3mlt4m96ky91p_enterpriseidwhcd on resource.resourceitemxresourceitemtypesecuritytoken (enterpriseid, warehousefromdate);


drop table if exists resource.resourceitemdatavalue;
-- payload table (LOGGED because you cannot lose data)
CREATE TABLE IF NOT EXISTS resource.resourceitemdatavalue
(
    resourceitemdatavalueid uuid  NOT NULL, -- == resourceitemid
    resourceitemdatavalue   bytea NULL,     -- optional payload
    CONSTRAINT resourceitemdatavalue_pkey PRIMARY KEY (resourceitemdatavalueid)
);

ALTER TABLE resource.resourceitemdatavalue
    ALTER COLUMN resourceitemdatavalue SET STORAGE EXTENDED;

ALTER TABLE resource.resourceitemdatavalue
    ALTER COLUMN resourceitemdatavalue SET COMPRESSION lz4;

DO
$$
    DECLARE
        missing_payload_rows bigint;
        orphan_payload_rows  bigint;
        data_without_item    bigint;
    BEGIN
        -- 1) link column on data (optional)
        IF NOT EXISTS (SELECT 1
                       FROM information_schema.columns
                       WHERE table_schema = 'resource'
                         AND table_name = 'resourceitemdata'
                         AND column_name = 'resourceitemdatavalueid') THEN
            ALTER TABLE resource.resourceitemdata
                ADD COLUMN resourceitemdatavalueid uuid;
        END IF;

        -- 2) copy payloads for rows that actually have payload
        INSERT INTO resource.resourceitemdatavalue (resourceitemdatavalueid, resourceitemdatavalue)
        SELECT d.resourceitemid, d.resourceitemdata
        FROM resource.resourceitemdata d
        WHERE d.resourceitemdata IS NOT NULL
        ON CONFLICT (resourceitemdatavalueid) DO NOTHING;

        -- 3) set the link only where payload exists
        UPDATE resource.resourceitemdata d
        SET resourceitemdatavalueid = d.resourceitemid
        WHERE d.resourceitemdata IS NOT NULL
          AND d.resourceitemdatavalueid IS DISTINCT FROM d.resourceitemid;

    END
$$;

-- autovac + toast tuning (payload churn table)
ALTER TABLE resource.resourceitemdatavalue
    SET (
        autovacuum_vacuum_scale_factor = 0.005,
        autovacuum_analyze_scale_factor = 0.005,
        autovacuum_vacuum_threshold = 2000,
        autovacuum_analyze_threshold = 2000,
        autovacuum_vacuum_cost_limit = 10000,
        toast_tuple_target = 2048
        );

CREATE INDEX IF NOT EXISTS resourceitemdata_resourceitemdatavalueid_notnull_idx
    ON resource.resourceitemdata (resourceitemdatavalueid)
    WHERE resourceitemdatavalueid IS NOT NULL;

alter table resource.resourceitemdata
    drop COLUMN resourceitemdata;

CREATE INDEX IF NOT EXISTS rix_cls_val_effdesc_idx
    ON resource.ResourceItemXClassification
        (ClassificationID, Value, EffectiveFromDate DESC)
    INCLUDE (ResourceItemID);

CREATE INDEX IF NOT EXISTS ric_item_class_eff_idx
    ON resource.ResourceItemXClassification
        (ResourceItemID, ClassificationID, EffectiveFromDate, EffectiveToDate)
    INCLUDE (Value, ActiveFlagID);

CREATE INDEX IF NOT EXISTS ric_class_value_eff_idx
    ON resource.ResourceItemXClassification
        (ClassificationID, Value, EffectiveFromDate DESC)
    INCLUDE (ResourceItemID, ActiveFlagID);

-- if ResourceItemID is PK you're mostly covered, but effective filtering can still benefit:
CREATE INDEX IF NOT EXISTS ri_active_eff_idx
    ON resource.ResourceItem
        (ResourceItemID, ActiveFlagID, EffectiveFromDate, EffectiveToDate);


