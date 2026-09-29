CREATE SCHEMA address;
CREATE TABLE address.address
(
    addressid                     UUID                        NOT NULL primary key,
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
    classificationid              UUID                        NOT NULL
);

CREATE TABLE address.addresssecuritytoken
(
    addresssecuritytokenid        UUID                        NOT NULL primary key,
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
    addressid                     UUID                        NOT NULL
);
CREATE TABLE address.addressxclassification
(
    addressxclassificationid      UUID                        NOT NULL primary key,
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
    addressid                     UUID                        NOT NULL
);
CREATE TABLE address.addressxclassificationsecuritytoken
(
    addressxclassificationsecuritytokenid UUID                        NOT NULL primary key,
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
    addressxclassificationid              UUID                        NOT NULL
);
CREATE TABLE address.addressxgeography
(
    addressxgeographyid           UUID                        NOT NULL primary key,
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
    addressid                     UUID                        NOT NULL,
    geographyid                   UUID                        NOT NULL
);
CREATE TABLE address.addressxgeographysecuritytoken
(
    addressxgeographysecuritytokenid UUID                        NOT NULL primary key,
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
    addressxgeographyid              UUID                        NOT NULL
);
CREATE TABLE address.addressxresourceitem
(
    addressxresourceitemid        UUID                        NOT NULL primary key,
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
    addressid                     UUID                        NOT NULL,
    resourceitemid                UUID                        NOT NULL
);
CREATE TABLE address.addressxresourceitemsecuritytoken
(
    addressxresourceitemsecuritytokenid UUID                        NOT NULL primary key,
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
    addressxresourceitemid              UUID                        NOT NULL
);


create index fk9iyq6jfpe1xg4oaba71hnicr7_activeflagid on address.address (activeflagid);
create index fkf5n0oi8hso8dpemjbb6733utp_enterpriseid on address.address (enterpriseid);
create index fkgh65u3w9ww52fhyerw4038tvf_classificationid on address.address (classificationid);
create index fkoj04ku2v02yibdyt7p9nuphyf_originalsourcesystemid on address.address (originalsourcesystemid);
create index fksx02yl44uidocj3ryaqom1urb_systemid on address.address (systemid);
create index fk4iwbt1caylolasmb7919al3vo_enterpriseid on address.addresssecuritytoken (enterpriseid);
create index fkgc74tjta2etx11rg02xqt293l_securitytokenid on address.addresssecuritytoken (securitytokenid);
create index fki5s2v3sm6uuf3j8sbqbuxcfu6_activeflagid on address.addresssecuritytoken (activeflagid);
create index fknc36gmq9xrt6k0cj6clmbh13p_addressid on address.addresssecuritytoken (addressid);
create index fkog9cqhou7pe3e0cwc7uavblc0_systemid on address.addresssecuritytoken (systemid);
create index fks8ohxcn7hgiuyyyy7koaught4_originalsourcesystemid on address.addresssecuritytoken (originalsourcesystemid);
create index fk10gn5jpbgrhhp4e99c6i8p9ps_enterpriseid on address.addressxclassification (enterpriseid);
create index fk6lyv1kvg7y348bj3pmbionfkr_activeflagid on address.addressxclassification (activeflagid);
create index fkgelsst4chd2utcx5ed62or2h7_systemid on address.addressxclassification (systemid);
create index fkobw11cghkuvasdoprpf8jeax5_classificationid on address.addressxclassification (classificationid);
create index fkqemdst9c0gqhhpeqrixd4567g_addressid on address.addressxclassification (addressid);
create index fks747gghpsg2w01eb6xycbkem_originalsourcesystemid on address.addressxclassification (originalsourcesystemid);
create index fk8r4rrxr1r79me66xvgxk7rnrr_activeflagid on address.addressxclassificationsecuritytoken (activeflagid);
create index fkaoaax8wtnqlmuh9uh7oewam8w_systemid on address.addressxclassificationsecuritytoken (systemid);
create index fkbnb6j2jg2da9a096a6mdqugc2_addressxclassificationid on address.addressxclassificationsecuritytoken (addressxclassificationid);
create index fki37g7k1r7gjvpsmrglofsk4t5_enterpriseid on address.addressxclassificationsecuritytoken (enterpriseid);
create index fkoo05nqmk8a03k1nje9outisjd_securitytokenid on address.addressxclassificationsecuritytoken (securitytokenid);
create index fktnrs65dum0qh9qgh0mlyhkf27_originalsourcesystemid on address.addressxclassificationsecuritytoken (originalsourcesystemid);
create index fk1jym0y5b721wadwh42kt6jhl7_addressid on address.addressxgeography (addressid);
create index fk8tb5og3v55dnhqp8aw31nwwmt_enterpriseid on address.addressxgeography (enterpriseid);
create index fkgf8llc9jdhc8s0eatod3ja84o_geographyid on address.addressxgeography (geographyid);
create index fkm2u3jmfqc5akhsq86tod7q6nc_systemid on address.addressxgeography (systemid);
create index fknjongjciautlpcno0p44ekxn1_originalsourcesystemid on address.addressxgeography (originalsourcesystemid);
create index fkoni218klw7gbcm5ksonbvvvld_classificationid on address.addressxgeography (classificationid);
create index fkpn4h36o5q70i13pbhluyugaxw_activeflagid on address.addressxgeography (activeflagid);
create index fk44g7fv09en02ov1n5mtvwm9g0_systemid on address.addressxgeographysecuritytoken (systemid);
create index fkcp386daxldghx156aklcouc4y_activeflagid on address.addressxgeographysecuritytoken (activeflagid);
create index fkfrqil82x8r1wpyn0ral52ypi3_addressxgeographyid on address.addressxgeographysecuritytoken (addressxgeographyid);
create index fkgbtljbtwk7lsj92gxbx6su7dp_originalsourcesystemid on address.addressxgeographysecuritytoken (originalsourcesystemid);
create index fkhp6f4qu8ywoe5aj507yc3gghn_securitytokenid on address.addressxgeographysecuritytoken (securitytokenid);
create index fkq6uacmwuavt3j9bani8jg5k50_enterpriseid on address.addressxgeographysecuritytoken (enterpriseid);
create index fk4fdkipv2j9v5vo3ejwd5k2lkc_classificationid on address.addressxresourceitem (classificationid);
create index fk6gvqnudvt7tsnt1gxo59d96di_addressid on address.addressxresourceitem (addressid);
create index fk8233bmyvh84df7grsv59kx99x_originalsourcesystemid on address.addressxresourceitem (originalsourcesystemid);
create index fkappieo5e929jb2sushbbrw3xh_enterpriseid on address.addressxresourceitem (enterpriseid);
create index fkeckhwk0f0a03h24a7we03pi2q_systemid on address.addressxresourceitem (systemid);
create index fkm191nhp3l761p7sgm5r0nafnl_activeflagid on address.addressxresourceitem (activeflagid);
create index fks421flo2pfa9xgnh0kaaju64f_resourceitemid on address.addressxresourceitem (resourceitemid);
create index fk3483wmiodjy481digc0kciwho_securitytokenid on address.addressxresourceitemsecuritytoken (securitytokenid);
create index fk94ahf4mdoc5kymdb9kd3ck141_originalsourcesystemid on address.addressxresourceitemsecuritytoken (originalsourcesystemid);
create index fkds3id5ml3ium9v870rdxryoav_enterpriseid on address.addressxresourceitemsecuritytoken (enterpriseid);
create index fkjdtgh57fk70hgg6owk878wdrv_systemid on address.addressxresourceitemsecuritytoken (systemid);
create index fkqcrw559ebisww70vhco1yn1l3_addressxresourceitemid on address.addressxresourceitemsecuritytoken (addressxresourceitemid);
create index fksx2i6hs70nkrw122gnxi1oljt_activeflagid on address.addressxresourceitemsecuritytoken (activeflagid);

CREATE INDEX idx_addressxresourceitemsecuritytoken_effectivefromdate ON address.addressxresourceitemsecuritytoken (effectivefromdate);
CREATE INDEX idx_addressxresourceitemsecuritytoken_effectivetodate ON address.addressxresourceitemsecuritytoken (effectivetodate);
CREATE INDEX idx_addressxresourceitemsecuritytoken_warehousecreatedtimestam ON address.addressxresourceitemsecuritytoken (warehousecreatedtimestamp);
CREATE INDEX idx_addressxresourceitemsecuritytoken_warehouselastupdatedtime ON address.addressxresourceitemsecuritytoken (warehouselastupdatedtimestamp);
CREATE INDEX idx_address_effectivefromdate ON address.address (effectivefromdate);
CREATE INDEX idx_address_effectivetodate ON address.address (effectivetodate);
CREATE INDEX idx_address_warehousecreatedtimestamp ON address.address (warehousecreatedtimestamp);
CREATE INDEX idx_address_warehouselastupdatedtimestamp ON address.address (warehouselastupdatedtimestamp);
CREATE INDEX idx_addressxgeographysecuritytoken_effectivefromdate ON address.addressxgeographysecuritytoken (effectivefromdate);
CREATE INDEX idx_addressxgeographysecuritytoken_effectivetodate ON address.addressxgeographysecuritytoken (effectivetodate);
CREATE INDEX idx_addressxgeographysecuritytoken_warehousecreatedtimestamp ON address.addressxgeographysecuritytoken (warehousecreatedtimestamp);
CREATE INDEX idx_addressxgeographysecuritytoken_warehouselastupdatedtimesta ON address.addressxgeographysecuritytoken (warehouselastupdatedtimestamp);
CREATE INDEX idx_addresssecuritytoken_effectivefromdate ON address.addresssecuritytoken (effectivefromdate);
CREATE INDEX idx_addresssecuritytoken_effectivetodate ON address.addresssecuritytoken (effectivetodate);
CREATE INDEX idx_addresssecuritytoken_warehousecreatedtimestamp ON address.addresssecuritytoken (warehousecreatedtimestamp);
CREATE INDEX idx_addresssecuritytoken_warehouselastupdatedtimestamp ON address.addresssecuritytoken (warehouselastupdatedtimestamp);
CREATE INDEX idx_addressxclassification_effectivefromdate ON address.addressxclassification (effectivefromdate);
CREATE INDEX idx_addressxclassification_effectivetodate ON address.addressxclassification (effectivetodate);
CREATE INDEX idx_addressxclassification_warehousecreatedtimestamp ON address.addressxclassification (warehousecreatedtimestamp);
CREATE INDEX idx_addressxclassification_warehouselastupdatedtimestamp ON address.addressxclassification (warehouselastupdatedtimestamp);
CREATE INDEX idx_addressxresourceitem_effectivefromdate ON address.addressxresourceitem (effectivefromdate);
CREATE INDEX idx_addressxresourceitem_effectivetodate ON address.addressxresourceitem (effectivetodate);
CREATE INDEX idx_addressxresourceitem_warehousecreatedtimestamp ON address.addressxresourceitem (warehousecreatedtimestamp);
CREATE INDEX idx_addressxresourceitem_warehouselastupdatedtimestamp ON address.addressxresourceitem (warehouselastupdatedtimestamp);
CREATE INDEX idx_addressxclassificationsecuritytoken_effectivefromdate ON address.addressxclassificationsecuritytoken (effectivefromdate);
CREATE INDEX idx_addressxclassificationsecuritytoken_effectivetodate ON address.addressxclassificationsecuritytoken (effectivetodate);
CREATE INDEX idx_addressxclassificationsecuritytoken_warehousecreatedtimest ON address.addressxclassificationsecuritytoken (warehousecreatedtimestamp);
CREATE INDEX idx_addressxclassificationsecuritytoken_warehouselastupdatedti ON address.addressxclassificationsecuritytoken (warehouselastupdatedtimestamp);


CREATE INDEX idx_addressxgeography_effectivefromdate ON address.addressxgeography (effectivefromdate);
CREATE INDEX idx_addressxgeography_effectivetodate ON address.addressxgeography (effectivetodate);
CREATE INDEX idx_addressxgeography_warehousecreatedtimestamp ON address.addressxgeography (warehousecreatedtimestamp);
CREATE INDEX idx_addressxgeography_warehouselastupdatedtimestamp ON address.addressxgeography (warehouselastupdatedtimestamp);

CREATE INDEX idx_address_value ON address.address (value);
CREATE INDEX idx_addressxclassification_value ON address.addressxclassification (value);
CREATE INDEX idx_addressxresourceitem_value ON address.addressxresourceitem (value);
CREATE INDEX idx_addressxgeography_value ON address.addressxgeography (value);
create index fk9iyq6jfpe1xg4oaba71hnicr7_activeflagidwhcd on address.address (activeflagid, warehousefromdate);
create index fkf5n0oi8hso8dpemjbb6733utp_enterpriseidwhcd on address.address (enterpriseid, warehousefromdate);
create index fkgh65u3w9ww52fhyerw4038tvf_classificationidwhcd on address.address (classificationid, warehousefromdate);
create index fkoj04ku2v02yibdyt7p9nuphyf_originalsourcesystemidwhcd on address.address (originalsourcesystemid, warehousefromdate);
create index fksx02yl44uidocj3ryaqom1urb_systemidwhcd on address.address (systemid, warehousefromdate);
create index fk4iwbt1caylolasmb7919al3vo_enterpriseidwhcd on address.addresssecuritytoken (enterpriseid, warehousefromdate);
create index fkgc74tjta2etx11rg02xqt293l_securitytokenidwhcd on address.addresssecuritytoken (securitytokenid, warehousefromdate);
create index fki5s2v3sm6uuf3j8sbqbuxcfu6_activeflagidwhcd on address.addresssecuritytoken (activeflagid, warehousefromdate);
create index fknc36gmq9xrt6k0cj6clmbh13p_addressidwhcd on address.addresssecuritytoken (addressid, warehousefromdate);
create index fkog9cqhou7pe3e0cwc7uavblc0_systemidwhcd on address.addresssecuritytoken (systemid, warehousefromdate);
create index fks8ohxcn7hgiuyyyy7koaught4_originalsourcesystemidwhcd on address.addresssecuritytoken (originalsourcesystemid, warehousefromdate);
create index fk10gn5jpbgrhhp4e99c6i8p9ps_enterpriseidwhcd on address.addressxclassification (enterpriseid, warehousefromdate);
create index fk6lyv1kvg7y348bj3pmbionfkr_activeflagidwhcd on address.addressxclassification (activeflagid, warehousefromdate);
create index fkgelsst4chd2utcx5ed62or2h7_systemidwhcd on address.addressxclassification (systemid, warehousefromdate);
create index fkobw11cghkuvasdoprpf8jeax5_classificationidwhcd on address.addressxclassification (classificationid, warehousefromdate);
create index fkqemdst9c0gqhhpeqrixd4567g_addressidwhcd on address.addressxclassification (addressid, warehousefromdate);
create index fks747gghpsg2w01eb6xycbkem_originalsourcesystemidwhcd on address.addressxclassification (originalsourcesystemid, warehousefromdate);
create index fk8r4rrxr1r79me66xvgxk7rnrr_activeflagidwhcd on address.addressxclassificationsecuritytoken (activeflagid, warehousefromdate);
create index fkaoaax8wtnqlmuh9uh7oewam8w_systemidwhcd on address.addressxclassificationsecuritytoken (systemid, warehousefromdate);
create index fkbnb6j2jg2da9a096a6mdqugc2_addressxclassificationidwhcd on address.addressxclassificationsecuritytoken (addressxclassificationid, warehousefromdate);
create index fki37g7k1r7gjvpsmrglofsk4t5_enterpriseidwhcd on address.addressxclassificationsecuritytoken (enterpriseid, warehousefromdate);
create index fkoo05nqmk8a03k1nje9outisjd_securitytokenidwhcd on address.addressxclassificationsecuritytoken (securitytokenid, warehousefromdate);
create index fktnrs65dum0qh9qgh0mlyhkf27_originalsourcesystemidwhcd on address.addressxclassificationsecuritytoken (originalsourcesystemid, warehousefromdate);
create index fk1jym0y5b721wadwh42kt6jhl7_addressidwhcd on address.addressxgeography (addressid, warehousefromdate);
create index fk8tb5og3v55dnhqp8aw31nwwmt_enterpriseidwhcd on address.addressxgeography (enterpriseid, warehousefromdate);
create index fkgf8llc9jdhc8s0eatod3ja84o_geographyidwhcd on address.addressxgeography (geographyid, warehousefromdate);
create index fkm2u3jmfqc5akhsq86tod7q6nc_systemidwhcd on address.addressxgeography (systemid, warehousefromdate);
create index fknjongjciautlpcno0p44ekxn1_originalsourcesystemidwhcd on address.addressxgeography (originalsourcesystemid, warehousefromdate);
create index fkoni218klw7gbcm5ksonbvvvld_classificationidwhcd on address.addressxgeography (classificationid, warehousefromdate);
create index fkpn4h36o5q70i13pbhluyugaxw_activeflagidwhcd on address.addressxgeography (activeflagid, warehousefromdate);
create index fk44g7fv09en02ov1n5mtvwm9g0_systemidwhcd on address.addressxgeographysecuritytoken (systemid, warehousefromdate);
create index fkcp386daxldghx156aklcouc4y_activeflagidwhcd on address.addressxgeographysecuritytoken (activeflagid, warehousefromdate);
create index fkfrqil82x8r1wpyn0ral52ypi3_addressxgeographyidwhcd on address.addressxgeographysecuritytoken (addressxgeographyid, warehousefromdate);
create index fkgbtljbtwk7lsj92gxbx6su7dp_originalsourcesystemidwhcd on address.addressxgeographysecuritytoken (originalsourcesystemid, warehousefromdate);
create index fkhp6f4qu8ywoe5aj507yc3gghn_securitytokenidwhcd on address.addressxgeographysecuritytoken (securitytokenid, warehousefromdate);
create index fkq6uacmwuavt3j9bani8jg5k50_enterpriseidwhcd on address.addressxgeographysecuritytoken (enterpriseid, warehousefromdate);
create index fk4fdkipv2j9v5vo3ejwd5k2lkc_classificationidwhcd on address.addressxresourceitem (classificationid, warehousefromdate);
create index fk6gvqnudvt7tsnt1gxo59d96di_addressidwhcd on address.addressxresourceitem (addressid, warehousefromdate);
create index fk8233bmyvh84df7grsv59kx99x_originalsourcesystemidwhcd on address.addressxresourceitem (originalsourcesystemid, warehousefromdate);
create index fkappieo5e929jb2sushbbrw3xh_enterpriseidwhcd on address.addressxresourceitem (enterpriseid, warehousefromdate);
create index fkeckhwk0f0a03h24a7we03pi2q_systemidwhcd on address.addressxresourceitem (systemid, warehousefromdate);
create index fkm191nhp3l761p7sgm5r0nafnl_activeflagidwhcd on address.addressxresourceitem (activeflagid, warehousefromdate);
create index fks421flo2pfa9xgnh0kaaju64f_resourceitemidwhcd on address.addressxresourceitem (resourceitemid, warehousefromdate);
create index fk3483wmiodjy481digc0kciwho_securitytokenidwhcd on address.addressxresourceitemsecuritytoken (securitytokenid, warehousefromdate);
create index fk94ahf4mdoc5kymdb9kd3ck141_originalsourcesystemidwhcd on address.addressxresourceitemsecuritytoken (originalsourcesystemid, warehousefromdate);
create index fkds3id5ml3ium9v870rdxryoav_enterpriseidwhcd on address.addressxresourceitemsecuritytoken (enterpriseid, warehousefromdate);
create index fkjdtgh57fk70hgg6owk878wdrv_systemidwhcd on address.addressxresourceitemsecuritytoken (systemid, warehousefromdate);
create index fkqcrw559ebisww70vhco1yn1l3_addressxresourceitemidwhcd on address.addressxresourceitemsecuritytoken (addressxresourceitemid, warehousefromdate);
create index fksx2i6hs70nkrw122gnxi1oljt_activeflagidwhcd on address.addressxresourceitemsecuritytoken (activeflagid, warehousefromdate);
