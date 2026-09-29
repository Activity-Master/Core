CREATE SCHEMA geography;
CREATE TABLE geography.geography
(
    geographyid                   UUID                        NOT NULL primary key,
    effectivefromdate             timestamp(6) with time zone NOT NULL,
    effectivetodate               timestamp(6) with time zone NOT NULL,
    warehousecreatedtimestamp     timestamp(6) with time zone NOT NULL,
    warehousefromdate             DATE                        NOT NULL,

    warehouselastupdatedtimestamp timestamp(6) with time zone NOT NULL,
    originalsourcesystemuniqueid  UUID                        NOT NULL,
    geographydesc                 character varying(500)      NOT NULL,
    geographyname                 character varying(500)      NOT NULL,
    activeflagid                  UUID                        NOT NULL,
    enterpriseid                  UUID                        NOT NULL,
    systemid                      UUID                        NOT NULL,
    originalsourcesystemid        UUID                        NOT NULL,
    classificationid              UUID                        NOT NULL
);
CREATE TABLE geography.geographysecuritytoken
(
    geographysecuritytokenid      UUID                        NOT NULL primary key,
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
    geographyid                   UUID                        NOT NULL
);
CREATE TABLE geography.geographyxclassification
(
    geographyxclassificationid    UUID                        NOT NULL primary key,
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
    geographyid                   UUID                        NOT NULL
);
CREATE TABLE geography.geographyxclassificationsecuritytoken
(
    geographyxclassificationsecuritytokenid UUID                        NOT NULL primary key,
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
    geographyxclassificationid              UUID                        NOT NULL
);
CREATE TABLE geography.geographyxgeography
(
    geographyxgeographyid         UUID                        NOT NULL primary key,
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
    childgeographyid              UUID                        NOT NULL,
    parentgeographyid             UUID                        NOT NULL
);
CREATE TABLE geography.geographyxgeographysecuritytoken
(
    geographyxgeographysecuritytokenid UUID                        NOT NULL primary key,
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
    geographyxgeographyid              UUID                        NOT NULL
);
CREATE TABLE geography.geographyxresourceitem
(
    geographyxresourceitemid      UUID                        NOT NULL primary key,
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
    geographyid                   UUID                        NOT NULL,
    resourceitemid                UUID                        NOT NULL
);
CREATE TABLE geography.geographyxresourceitemsecuritytoken
(
    geographyxresourceitemsecuritytokenid UUID                        NOT NULL primary key,
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
    geographyxresourceitemid              UUID                        NOT NULL
);

create index fk3hp5hnnmkc6wh089ye90tm3o6_activeflagid on geography.geography (activeflagid);
create index fkd30h42gp0snfq2lrm61hxeo7a_classificationid on geography.geography (classificationid);
create index fke3cvg85u45hqr34o3srx8njbv_systemid on geography.geography (systemid);
create index fkfe6vl7bajuvj2k024mtfxvjwy_originalsourcesystemid on geography.geography (originalsourcesystemid);
create index fkhfy9e7gg8xlswihrx45wk0093_enterpriseid on geography.geography (enterpriseid);
create index fk7ate5yexxkqmll1407emnf36i_activeflagid on geography.geographysecuritytoken (activeflagid);
create index fk8obgl7xdf7mth9vgblc6u057d_originalsourcesystemid on geography.geographysecuritytoken (originalsourcesystemid);
create index fk9u01t3it5fhcfyw2q3x2r19f6_systemid on geography.geographysecuritytoken (systemid);
create index fkbrt91rfhyvn2hp76aga8vvdoq_enterpriseid on geography.geographysecuritytoken (enterpriseid);
create index fkh6x5jqejxvpq70sg8w8bcddvu_geographyid on geography.geographysecuritytoken (geographyid);
create index fkmpal48yq5kf23jj4x9r77v0g7_securitytokenid on geography.geographysecuritytoken (securitytokenid);
create index fk5ohjj567mkcmt76ptgsq2qsin_classificationid on geography.geographyxclassification (classificationid);
create index fk5p12kjl7g353s2w727ulp384s_originalsourcesystemid on geography.geographyxclassification (originalsourcesystemid);
create index fkafecxpt71ni5iel6aklaku4f5_enterpriseid on geography.geographyxclassification (enterpriseid);
create index fkgt1brdxa60taodmc6cg28r0lb_geographyid on geography.geographyxclassification (geographyid);
create index fkl1bl1px429vykkiut1fdl7o0n_activeflagid on geography.geographyxclassification (activeflagid);
create index fkrtu1y36y10o03rcrhrvacvki1_systemid on geography.geographyxclassification (systemid);
create index fk5jg1k2am65j8ibiky7mc0inxf_systemid on geography.geographyxclassificationsecuritytoken (systemid);
create index fkeenbngjgunycd80bgeoaylrwp_securitytokenid on geography.geographyxclassificationsecuritytoken (securitytokenid);
create index fkffvnvtxqjjbur12t6x7qcbn2h_activeflagid on geography.geographyxclassificationsecuritytoken (activeflagid);
create index fkl5321rf4hio0kms78ic3f8bst_geographyxclassificationid on geography.geographyxclassificationsecuritytoken (geographyxclassificationid);
create index fkmhery9mlbon8qfm9i7vymp6jy_originalsourcesystemid on geography.geographyxclassificationsecuritytoken (originalsourcesystemid);
create index fkp6nlv6glhcy1fyqj5bj5hggud_enterpriseid on geography.geographyxclassificationsecuritytoken (enterpriseid);
create index fk1dvwqevxm9g1ajxfofeyog39o_activeflagid on geography.geographyxgeography (activeflagid);
create index fk1x9ioslmt95w0fjxh1h7tgh22_originalsourcesystemid on geography.geographyxgeography (originalsourcesystemid);
create index fkdwrnhxis6ef8f2rkhxv56jmgu_childgeographyid on geography.geographyxgeography (childgeographyid);
create index fkdxliyywqpd69bpwfsbm58tia1_systemid on geography.geographyxgeography (systemid);
create index fkglfnoy0sok1ayveu5glm26neo_parentgeographyid on geography.geographyxgeography (parentgeographyid);
create index fkh8w4rt46cn0a8rdk7p46i6yw6_classificationid on geography.geographyxgeography (classificationid);
create index fkp5cwyefygfcshowmbuctqm2bd_enterpriseid on geography.geographyxgeography (enterpriseid);
create index fk2ho20x8qldli7owo63ieh7pdk_enterpriseid on geography.geographyxgeographysecuritytoken (enterpriseid);
create index fke94i2ly219wcofp3qlsyepcjx_securitytokenid on geography.geographyxgeographysecuritytoken (securitytokenid);
create index fkjqo2qgds42bxfxae3xjj8528w_activeflagid on geography.geographyxgeographysecuritytoken (activeflagid);
create index fklclax5c7qktjee2sf4mysgub8_originalsourcesystemid on geography.geographyxgeographysecuritytoken (originalsourcesystemid);
create index fkqd1qu9501eat5nh9sjc6ejg27_systemid on geography.geographyxgeographysecuritytoken (systemid);
create index fkqth25orgkxdx6xvepkv9dkco4_geographyxgeographyid on geography.geographyxgeographysecuritytoken (geographyxgeographyid);
create index fk4cbs953wqd4v6mb2yco9uu2cy_systemid on geography.geographyxresourceitem (systemid);
create index fkan3li0682x6rj5rnqgo2s3bm3_originalsourcesystemid on geography.geographyxresourceitem (originalsourcesystemid);
create index fkbvccen8s30rkpie76wyr1mixd_classificationid on geography.geographyxresourceitem (classificationid);
create index fkgd4x1t5pb1lnp2etg9yptt7n7_enterpriseid on geography.geographyxresourceitem (enterpriseid);
create index fkikpxdma2cnfy7ak4hqqskegdt_resourceitemid on geography.geographyxresourceitem (resourceitemid);
create index fkmfe0l4k1dgd3890oxlt65lm8g_activeflagid on geography.geographyxresourceitem (activeflagid);
create index fkplqcl8yui4k6xm0faaa3v9v3v_geographyid on geography.geographyxresourceitem (geographyid);
create index fk57l6e5bda4i1n4re0hw21706u_originalsourcesystemid on geography.geographyxresourceitemsecuritytoken (originalsourcesystemid);
create index fk5j6a8twkt85ffakx17foe5klo_activeflagid on geography.geographyxresourceitemsecuritytoken (activeflagid);
create index fk6m9u05ntxa7n822r58ewn00e6_enterpriseid on geography.geographyxresourceitemsecuritytoken (enterpriseid);
create index fkfwajhfr051wxdtw76afh5totc_securitytokenid on geography.geographyxresourceitemsecuritytoken (securitytokenid);
create index fkodr0owqhi39gwkkygjyyk3sd9_geographyxresourceitemid on geography.geographyxresourceitemsecuritytoken (geographyxresourceitemid);
create index fkpj8pp6mw0061pdlxv6p8f9qv8_systemid on geography.geographyxresourceitemsecuritytoken (systemid);



CREATE INDEX idx_geographyxgeographysecuritytoken_effectivefromdate ON geography.geographyxgeographysecuritytoken (effectivefromdate);
CREATE INDEX idx_geographyxgeographysecuritytoken_effectivetodate ON geography.geographyxgeographysecuritytoken (effectivetodate);
CREATE INDEX idx_geographyxgeographysecuritytoken_warehousecreatedtimestamp ON geography.geographyxgeographysecuritytoken (warehousecreatedtimestamp);
CREATE INDEX idx_geographyxgeographysecuritytoken_warehouselastupdatedtimes ON geography.geographyxgeographysecuritytoken (warehouselastupdatedtimestamp);
CREATE INDEX idx_geographyxresourceitemsecuritytoken_effectivefromdate ON geography.geographyxresourceitemsecuritytoken (effectivefromdate);
CREATE INDEX idx_geographyxresourceitemsecuritytoken_effectivetodate ON geography.geographyxresourceitemsecuritytoken (effectivetodate);
CREATE INDEX idx_geographyxresourceitemsecuritytoken_warehousecreatedtimest ON geography.geographyxresourceitemsecuritytoken (warehousecreatedtimestamp);
CREATE INDEX idx_geographyxresourceitemsecuritytoken_warehouselastupdatedti ON geography.geographyxresourceitemsecuritytoken (warehouselastupdatedtimestamp);

CREATE INDEX idx_geographyxgeography_effectivefromdate ON geography.geographyxgeography (effectivefromdate);
CREATE INDEX idx_geographyxgeography_effectivetodate ON geography.geographyxgeography (effectivetodate);
CREATE INDEX idx_geographyxgeography_warehousecreatedtimestamp ON geography.geographyxgeography (warehousecreatedtimestamp);
CREATE INDEX idx_geographyxgeography_warehouselastupdatedtimestamp ON geography.geographyxgeography (warehouselastupdatedtimestamp);
CREATE INDEX idx_geographyxresourceitem_effectivefromdate ON geography.geographyxresourceitem (effectivefromdate);
CREATE INDEX idx_geographyxresourceitem_effectivetodate ON geography.geographyxresourceitem (effectivetodate);
CREATE INDEX idx_geographyxresourceitem_warehousecreatedtimestamp ON geography.geographyxresourceitem (warehousecreatedtimestamp);
CREATE INDEX idx_geographyxresourceitem_warehouselastupdatedtimestamp ON geography.geographyxresourceitem (warehouselastupdatedtimestamp);
CREATE INDEX idx_geographyxclassificationsecuritytoken_effectivefromdate ON geography.geographyxclassificationsecuritytoken (effectivefromdate);
CREATE INDEX idx_geographyxclassificationsecuritytoken_effectivetodate ON geography.geographyxclassificationsecuritytoken (effectivetodate);
CREATE INDEX idx_geographyxclassificationsecuritytoken_warehousecreatedtime ON geography.geographyxclassificationsecuritytoken (warehousecreatedtimestamp);
CREATE INDEX idx_geographyxclassificationsecuritytoken_warehouselastupdated ON geography.geographyxclassificationsecuritytoken (warehouselastupdatedtimestamp);
CREATE INDEX idx_geographyxclassification_effectivefromdate ON geography.geographyxclassification (effectivefromdate);
CREATE INDEX idx_geographyxclassification_effectivetodate ON geography.geographyxclassification (effectivetodate);
CREATE INDEX idx_geographyxclassification_warehousecreatedtimestamp ON geography.geographyxclassification (warehousecreatedtimestamp);
CREATE INDEX idx_geographyxclassification_warehouselastupdatedtimestamp ON geography.geographyxclassification (warehouselastupdatedtimestamp);



CREATE INDEX idx_geography_effectivefromdate ON geography.geography (effectivefromdate);
CREATE INDEX idx_geography_effectivetodate ON geography.geography (effectivetodate);
CREATE INDEX idx_geography_warehousecreatedtimestamp ON geography.geography (warehousecreatedtimestamp);
CREATE INDEX idx_geography_warehouselastupdatedtimestamp ON geography.geography (warehouselastupdatedtimestamp);


CREATE INDEX idx_geographysecuritytoken_effectivefromdate ON geography.geographysecuritytoken (effectivefromdate);
CREATE INDEX idx_geographysecuritytoken_effectivetodate ON geography.geographysecuritytoken (effectivetodate);
CREATE INDEX idx_geographysecuritytoken_warehousecreatedtimestamp ON geography.geographysecuritytoken (warehousecreatedtimestamp);
CREATE INDEX idx_geographysecuritytoken_warehouselastupdatedtimestamp ON geography.geographysecuritytoken (warehouselastupdatedtimestamp);



CREATE INDEX idx_geography_geographydesc ON geography.geography (geographydesc);
CREATE INDEX idx_geography_geographyname ON geography.geography (geographyname);
create index fk3hp5hnnmkc6wh089ye90tm3o6_activeflagidwhcd on geography.geography (activeflagid, warehousefromdate);
create index fkd30h42gp0snfq2lrm61hxeo7a_classificationidwhcd on geography.geography (classificationid, warehousefromdate);
create index fke3cvg85u45hqr34o3srx8njbv_systemidwhcd on geography.geography (systemid, warehousefromdate);
create index fkfe6vl7bajuvj2k024mtfxvjwy_originalsourcesystemidwhcd on geography.geography (originalsourcesystemid, warehousefromdate);
create index fkhfy9e7gg8xlswihrx45wk0093_enterpriseidwhcd on geography.geography (enterpriseid, warehousefromdate);
create index fk7ate5yexxkqmll1407emnf36i_activeflagidwhcd on geography.geographysecuritytoken (activeflagid, warehousefromdate);
create index fk8obgl7xdf7mth9vgblc6u057d_originalsourcesystemidwhcd on geography.geographysecuritytoken (originalsourcesystemid, warehousefromdate);
create index fk9u01t3it5fhcfyw2q3x2r19f6_systemidwhcd on geography.geographysecuritytoken (systemid, warehousefromdate);
create index fkbrt91rfhyvn2hp76aga8vvdoq_enterpriseidwhcd on geography.geographysecuritytoken (enterpriseid, warehousefromdate);
create index fkh6x5jqejxvpq70sg8w8bcddvu_geographyidwhcd on geography.geographysecuritytoken (geographyid, warehousefromdate);
create index fkmpal48yq5kf23jj4x9r77v0g7_securitytokenidwhcd on geography.geographysecuritytoken (securitytokenid, warehousefromdate);
create index fk5ohjj567mkcmt76ptgsq2qsin_classificationidwhcd on geography.geographyxclassification (classificationid, warehousefromdate);
create index fk5p12kjl7g353s2w727ulp384s_originalsourcesystemidwhcd on geography.geographyxclassification (originalsourcesystemid, warehousefromdate);
create index fkafecxpt71ni5iel6aklaku4f5_enterpriseidwhcd on geography.geographyxclassification (enterpriseid, warehousefromdate);
create index fkgt1brdxa60taodmc6cg28r0lb_geographyidwhcd on geography.geographyxclassification (geographyid, warehousefromdate);
create index fkl1bl1px429vykkiut1fdl7o0n_activeflagidwhcd on geography.geographyxclassification (activeflagid, warehousefromdate);
create index fkrtu1y36y10o03rcrhrvacvki1_systemidwhcd on geography.geographyxclassification (systemid, warehousefromdate);
create index fk5jg1k2am65j8ibiky7mc0inxf_systemidwhcd on geography.geographyxclassificationsecuritytoken (systemid, warehousefromdate);
create index fkeenbngjgunycd80bgeoaylrwp_securitytokenidwhcd on geography.geographyxclassificationsecuritytoken (securitytokenid, warehousefromdate);
create index fkffvnvtxqjjbur12t6x7qcbn2h_activeflagidwhcd on geography.geographyxclassificationsecuritytoken (activeflagid, warehousefromdate);
create index fkl5321rf4hio0kms78ic3f8bst_geographyxclassificationidwhcd on geography.geographyxclassificationsecuritytoken (geographyxclassificationid, warehousefromdate);
create index fkmhery9mlbon8qfm9i7vymp6jy_originalsourcesystemidwhcd on geography.geographyxclassificationsecuritytoken (originalsourcesystemid, warehousefromdate);
create index fkp6nlv6glhcy1fyqj5bj5hggud_enterpriseidwhcd on geography.geographyxclassificationsecuritytoken (enterpriseid, warehousefromdate);
create index fk1dvwqevxm9g1ajxfofeyog39o_activeflagidwhcd on geography.geographyxgeography (activeflagid, warehousefromdate);
create index fk1x9ioslmt95w0fjxh1h7tgh22_originalsourcesystemidwhcd on geography.geographyxgeography (originalsourcesystemid, warehousefromdate);
create index fkdwrnhxis6ef8f2rkhxv56jmgu_childgeographyidwhcd on geography.geographyxgeography (childgeographyid, warehousefromdate);
create index fkdxliyywqpd69bpwfsbm58tia1_systemidwhcd on geography.geographyxgeography (systemid, warehousefromdate);
create index fkglfnoy0sok1ayveu5glm26neo_parentgeographyidwhcd on geography.geographyxgeography (parentgeographyid, warehousefromdate);
create index fkh8w4rt46cn0a8rdk7p46i6yw6_classificationidwhcd on geography.geographyxgeography (classificationid, warehousefromdate);
create index fkp5cwyefygfcshowmbuctqm2bd_enterpriseidwhcd on geography.geographyxgeography (enterpriseid, warehousefromdate);
create index fk2ho20x8qldli7owo63ieh7pdk_enterpriseidwhcd on geography.geographyxgeographysecuritytoken (enterpriseid, warehousefromdate);
create index fke94i2ly219wcofp3qlsyepcjx_securitytokenidwhcd on geography.geographyxgeographysecuritytoken (securitytokenid, warehousefromdate);
create index fkjqo2qgds42bxfxae3xjj8528w_activeflagidwhcd on geography.geographyxgeographysecuritytoken (activeflagid, warehousefromdate);
create index fklclax5c7qktjee2sf4mysgub8_originalsourcesystemidwhcd on geography.geographyxgeographysecuritytoken (originalsourcesystemid, warehousefromdate);
create index fkqd1qu9501eat5nh9sjc6ejg27_systemidwhcd on geography.geographyxgeographysecuritytoken (systemid, warehousefromdate);
create index fkqth25orgkxdx6xvepkv9dkco4_geographyxgeographyidwhcd on geography.geographyxgeographysecuritytoken (geographyxgeographyid, warehousefromdate);
create index fk4cbs953wqd4v6mb2yco9uu2cy_systemidwhcd on geography.geographyxresourceitem (systemid, warehousefromdate);
create index fkan3li0682x6rj5rnqgo2s3bm3_originalsourcesystemidwhcd on geography.geographyxresourceitem (originalsourcesystemid, warehousefromdate);
create index fkbvccen8s30rkpie76wyr1mixd_classificationidwhcd on geography.geographyxresourceitem (classificationid, warehousefromdate);
create index fkgd4x1t5pb1lnp2etg9yptt7n7_enterpriseidwhcd on geography.geographyxresourceitem (enterpriseid, warehousefromdate);
create index fkikpxdma2cnfy7ak4hqqskegdt_resourceitemidwhcd on geography.geographyxresourceitem (resourceitemid, warehousefromdate);
create index fkmfe0l4k1dgd3890oxlt65lm8g_activeflagidwhcd on geography.geographyxresourceitem (activeflagid, warehousefromdate);
create index fkplqcl8yui4k6xm0faaa3v9v3v_geographyidwhcd on geography.geographyxresourceitem (geographyid, warehousefromdate);
create index fk57l6e5bda4i1n4re0hw21706u_originalsourcesystemidwhcd on geography.geographyxresourceitemsecuritytoken (originalsourcesystemid, warehousefromdate);
create index fk5j6a8twkt85ffakx17foe5klo_activeflagidwhcd on geography.geographyxresourceitemsecuritytoken (activeflagid, warehousefromdate);
create index fk6m9u05ntxa7n822r58ewn00e6_enterpriseidwhcd on geography.geographyxresourceitemsecuritytoken (enterpriseid, warehousefromdate);
create index fkfwajhfr051wxdtw76afh5totc_securitytokenidwhcd on geography.geographyxresourceitemsecuritytoken (securitytokenid, warehousefromdate);
create index fkodr0owqhi39gwkkygjyyk3sd9_geographyxresourceitemidwhcd on geography.geographyxresourceitemsecuritytoken (geographyxresourceitemid, warehousefromdate);
create index fkpj8pp6mw0061pdlxv6p8f9qv8_systemidwhcd on geography.geographyxresourceitemsecuritytoken (systemid, warehousefromdate);



CREATE INDEX idx_geographyxgeography_value ON geography.geographyxgeography (value);
CREATE INDEX idx_geographyxresourceitem_value ON geography.geographyxresourceitem (value);
CREATE INDEX idx_geographyxclassification_value ON geography.geographyxclassification (value);


