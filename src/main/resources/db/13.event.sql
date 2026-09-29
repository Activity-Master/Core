CREATE SCHEMA event;
CREATE TABLE event.event
(
    eventid                       UUID                        NOT NULL primary key,
    effectivefromdate             timestamp(6) with time zone NOT NULL,
    effectivetodate               timestamp(6) with time zone NOT NULL,
    warehousecreatedtimestamp     timestamp(6) with time zone NOT NULL,

    warehousefromdate             DATE                        NOT NULL,

    warehouselastupdatedtimestamp timestamp(6) with time zone NOT NULL,
    originalsourcesystemuniqueid  UUID                        NOT NULL,
    dayid                         INTEGER                     NOT NULL,
    hourid                        INTEGER                     NOT NULL,
    minuteid                      INTEGER                     NOT NULL,
    activeflagid                  UUID                        NOT NULL,
    enterpriseid                  UUID                        NOT NULL,
    systemid                      UUID                        NOT NULL,
    originalsourcesystemid        UUID                        NOT NULL
);
CREATE TABLE event.eventsecuritytoken
(
    eventssecuritytokenid         UUID                        NOT NULL primary key,
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
    eventsid                      UUID                        NOT NULL
);
CREATE TABLE event.eventtype
(
    eventtypeid                   UUID                        NOT NULL primary key,
    effectivefromdate             timestamp(6) with time zone NOT NULL,
    effectivetodate               timestamp(6) with time zone NOT NULL,
    warehousecreatedtimestamp     timestamp(6) with time zone NOT NULL,
    warehousefromdate             DATE                        NOT NULL,

    warehouselastupdatedtimestamp timestamp(6) with time zone NOT NULL,
    originalsourcesystemuniqueid  UUID                        NOT NULL,
    eventtypedesc                 character varying(200)      NOT NULL,
    eventtypename                 character varying(200)      NOT NULL,
    activeflagid                  UUID                        NOT NULL,
    enterpriseid                  UUID                        NOT NULL,
    systemid                      UUID                        NOT NULL,
    originalsourcesystemid        UUID                        NOT NULL
);
CREATE TABLE event.eventtypessecuritytoken
(
    eventtypessecuritytokenid     UUID                        NOT NULL primary key,
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
    eventtypesid                  UUID                        NOT NULL
);
CREATE TABLE event.eventxaddress
(
    eventxaddressid               UUID                        NOT NULL primary key,
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
    eventid                       UUID                        NOT NULL
);
CREATE TABLE event.eventxaddresssecuritytoken
(
    eventxaddresssecuritytokenid  UUID                        NOT NULL primary key,
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
    eventxaddressid               UUID                        NOT NULL
);
CREATE TABLE event.eventxarrangement
(
    eventxarrangementsid          UUID                        NOT NULL primary key,
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
    eventid                       UUID                        NOT NULL
);
CREATE TABLE event.eventxarrangementssecuritytoken
(
    eventxarrangementssecuritytokenid UUID                        NOT NULL primary key,
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
    eventxarrangementsid              UUID                        NOT NULL
);
CREATE TABLE event.eventxclassification
(
    eventxclassificationid        UUID                        NOT NULL primary key,
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
    eventid                       UUID                        NOT NULL
);
CREATE TABLE event.eventxclassificationsecuritytoken
(
    eventxclassificationssecuritytokenid UUID                        NOT NULL primary key,
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
    eventxclassificationsid              UUID                        NOT NULL
);
CREATE TABLE event.eventxevent
(
    eventxeventid                 UUID                        NOT NULL primary key,
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
    childeventid                  UUID                        NOT NULL,
    parenteventid                 UUID                        NOT NULL
);
CREATE TABLE event.eventxeventsecuritytoken
(
    eventxeventsecuritytokenid    UUID                        NOT NULL primary key,
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
    eventxeventid                 UUID                        NOT NULL
);
CREATE TABLE event.eventxeventtype
(
    eventxeventtypeid             UUID                        NOT NULL primary key,
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
    eventid                       UUID                        NOT NULL,
    eventtypeid                   UUID                        NOT NULL
);
CREATE TABLE event.eventxeventtypesecuritytoken
(
    eventxeventtypesecuritytokenid UUID                        NOT NULL primary key,
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
    eventxeventtypeid              UUID                        NOT NULL
);
CREATE TABLE event.eventxgeography
(
    eventxgeographyid             UUID                        NOT NULL primary key,
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
    eventid                       UUID                        NOT NULL,
    geographyid                   UUID                        NOT NULL
);
CREATE TABLE event.eventxgeographysecuritytoken
(
    eventxgeographysecuritytokenid UUID                        NOT NULL primary key,
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
    eventxgeographyid              UUID                        NOT NULL
);
CREATE TABLE event.eventxinvolvedparty
(
    eventxinvolvedpartyid         UUID                        NOT NULL primary key,
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
    eventid                       UUID                        NOT NULL,
    involvedpartyid               UUID                        NOT NULL
);
CREATE TABLE event.eventxinvolvedpartysecuritytoken
(
    eventxinvolvedpartysecuritytokenid UUID                        NOT NULL primary key,
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
    eventxinvolvedpartyid              UUID                        NOT NULL
);
CREATE TABLE event.eventxproduct
(
    eventxproductid               UUID                        NOT NULL primary key,
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
    eventid                       UUID                        NOT NULL,
    productid                     UUID                        NOT NULL
);
CREATE TABLE event.eventxproductsecuritytoken
(
    eventxproductsecuritytokenid  UUID                        NOT NULL primary key,
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
    eventxproductid               UUID                        NOT NULL
);
CREATE TABLE event.eventxresourceitem
(
    eventxresourceitemid          UUID                        NOT NULL primary key,
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
    eventid                       UUID                        NOT NULL,
    resourceitemid                UUID                        NOT NULL
);
CREATE TABLE event.eventxresourceitemsecuritytoken
(
    eventxresourceitemsecuritytokenid UUID                        NOT NULL primary key,
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
    eventxresourceitemid              UUID                        NOT NULL
);
CREATE TABLE event.eventxrules
(
    eventxrulesid                 UUID                        NOT NULL primary key,
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
    eventid                       UUID                        NOT NULL,
    rulesid                       UUID                        NOT NULL
);
CREATE TABLE event.eventxrulessecuritytoken
(
    eventxrulessecuritytokenid    UUID                        NOT NULL primary key,
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
    eventxrulesid                 UUID                        NOT NULL
);


create index fk5a6m41a8gggwx8fqsrs4at8tg_originalsourcesystemid on event.event (originalsourcesystemid);
create index fk6ifge8hyt5n7895pe83e7l8ic_enterpriseid on event.event (enterpriseid);
create index fk944ydkbqk682cotb21mlyedhc_activeflagid on event.event (activeflagid);
create index fkt9syxmvp6c5pcyhyiv9c8qpb1_systemid on event.event (systemid);
create index fk5l2t98n1cb4rsa23u81thgv50_securitytokenid on event.eventsecuritytoken (securitytokenid);
create index fkb5qngfntxfvo5cn45y0afpq6x_enterpriseid on event.eventsecuritytoken (enterpriseid);
create index fkmwxej7o84e382w8iy2y6fwlmf_activeflagid on event.eventsecuritytoken (activeflagid);
create index fko5kqswpu433kf5hcc3yh1w10e_systemid on event.eventsecuritytoken (systemid);
create index fkqcwlmmiy1hf3p3ol2dy865mx5_eventsid on event.eventsecuritytoken (eventsid);
create index fksa47kc62uve787xbc9h6syv0t_originalsourcesystemid on event.eventsecuritytoken (originalsourcesystemid);
create index fk3a6ig1a98nle8uhil7c00wtxj_activeflagid on event.eventtype (activeflagid);
create index fk7bpo42s41tkfou4bopgs8xeqg_originalsourcesystemid on event.eventtype (originalsourcesystemid);
create index fk91msecp4vf69nvn0t6362tmvy_enterpriseid on event.eventtype (enterpriseid);
create index fkd28eytnr3sps354vu7k75r6ds_systemid on event.eventtype (systemid);
create index fk3sqx94u3an8m27b0aotdca2wb_securitytokenid on event.eventtypessecuritytoken (securitytokenid);
create index fk533lul2cwy1gxqwdlkwot3xli_eventtypesid on event.eventtypessecuritytoken (eventtypesid);
create index fk69no16tl8ojgbi6so0jp3s68a_originalsourcesystemid on event.eventtypessecuritytoken (originalsourcesystemid);
create index fke2wss5k3p8r4nxol9e95m3k3q_activeflagid on event.eventtypessecuritytoken (activeflagid);
create index fkr2xa1q4402m8pc5evyj1tw9q5_enterpriseid on event.eventtypessecuritytoken (enterpriseid);
create index fktcukxvcv6bvhbmcelmt16vroq_systemid on event.eventtypessecuritytoken (systemid);
create index fk3bbi34ppkvf938mm8618n7cdi_enterpriseid on event.eventxaddress (enterpriseid);
create index fkd6wdra1kpon3dn8wcfjrk6msl_addressid on event.eventxaddress (addressid);
create index fkf3dfxx8n3ijrkin1n3rr8jcek_classificationid on event.eventxaddress (classificationid);
create index fkjkfm7v3719rvi0mu4nev2xu6d_eventid on event.eventxaddress (eventid);
create index fklmpsaajrvosc033g4idqh17bv_activeflagid on event.eventxaddress (activeflagid);
create index fkm52nfxs3cy5syh6ar5mly3k64_systemid on event.eventxaddress (systemid);
create index fkmtwd6myq1nc0g02s2s2nhkteo_originalsourcesystemid on event.eventxaddress (originalsourcesystemid);
create index fk1b8yahv9f17vt9brstr9lu5s_securitytokenid on event.eventxaddresssecuritytoken (securitytokenid);
create index fk2n5hwft2sainpe6l7cerwj4o6_systemid on event.eventxaddresssecuritytoken (systemid);
create index fk4delkrhbqq9uhqpsmqbdc8eh1_enterpriseid on event.eventxaddresssecuritytoken (enterpriseid);
create index fk6frs7aq2uychxsnl6r89fi7on_eventxaddressid on event.eventxaddresssecuritytoken (eventxaddressid);
create index fk86mt4pshsp9bxjaubl1729701_originalsourcesystemid on event.eventxaddresssecuritytoken (originalsourcesystemid);
create index fkd7r65wrbjj132a0hama4srqfu_activeflagid on event.eventxaddresssecuritytoken (activeflagid);
create index fk7607u935wf0x8yuf84h0xseyl_arrangementid on event.eventxarrangement (arrangementid);
create index fk7tkxsheat6us20jq1hhsmhkan_originalsourcesystemid on event.eventxarrangement (originalsourcesystemid);
create index fk9l3iwtdok5r27i83ywbjt0nto_enterpriseid on event.eventxarrangement (enterpriseid);
create index fkhyhd3s7f217ugwq3wo7pk5t1y_activeflagid on event.eventxarrangement (activeflagid);
create index fkii5wwqv91rn4ji3nwgiy3wd2l_eventid on event.eventxarrangement (eventid);
create index fklj26m0snewjsmso5n6jskt2a4_systemid on event.eventxarrangement (systemid);
create index fkpwoqs4xag1rw5tyrb062tyagd_classificationid on event.eventxarrangement (classificationid);
create index fk2tudn6gio54xpxa5ecyryl2v0_systemid on event.eventxarrangementssecuritytoken (systemid);
create index fk33w71cp61prmc7i0uboecicda_enterpriseid on event.eventxarrangementssecuritytoken (enterpriseid);
create index fkom7ax04agkpio3h8xw30vb60m_activeflagid on event.eventxarrangementssecuritytoken (activeflagid);
create index fkq3ik0e8bbij1nqvdu0jn1unvg_eventxarrangementsid on event.eventxarrangementssecuritytoken (eventxarrangementsid);
create index fks8terumw31sybh6o0x4sdbc2t_securitytokenid on event.eventxarrangementssecuritytoken (securitytokenid);
create index fktiw0truwtwa3ublkae3jexugw_originalsourcesystemid on event.eventxarrangementssecuritytoken (originalsourcesystemid);
create index fk7gqyx4i1xca4ioveotqcctmd3_enterpriseid on event.eventxclassification (enterpriseid);
create index fkf1gqpk872jacljscf2kv0i24r_eventid on event.eventxclassification (eventid);
create index fko28x8wv64sef2ifxjm8a4i2ft_classificationid on event.eventxclassification (classificationid);
create index fkojl271yprimpjrrv6u1a2ggb6_activeflagid on event.eventxclassification (activeflagid);
create index fkp7fx787hsyq9nf8820wl2rh1x_originalsourcesystemid on event.eventxclassification (originalsourcesystemid);
create index fkpssgfk2o0m8r84lhnb04bh0om_systemid on event.eventxclassification (systemid);
create index fk2uwfplnar8o0onw3e93phd69k_enterpriseid on event.eventxclassificationsecuritytoken (enterpriseid);
create index fk4hjmqpqf99dnag748layycl1d_activeflagid on event.eventxclassificationsecuritytoken (activeflagid);
create index fkfoq8c32icxr3vxx3t050nwdqm_eventxclassificationsid on event.eventxclassificationsecuritytoken (eventxclassificationsid);
create index fkg821wl3o2mwy3k7ysebk2donl_securitytokenid on event.eventxclassificationsecuritytoken (securitytokenid);
create index fkkdkwjb6nhj76m65s840einvmw_originalsourcesystemid on event.eventxclassificationsecuritytoken (originalsourcesystemid);
create index fkoq4421g5p0yjlklv0231xbjel_systemid on event.eventxclassificationsecuritytoken (systemid);
create index fk27gym41nw6dls3e5oll0nkcbr_originalsourcesystemid on event.eventxevent (originalsourcesystemid);
create index fk6gr2cpldlbe8dfgk92iceiksh_parenteventid on event.eventxevent (parenteventid);
create index fk8q11uo1x1xogw2rytdqbnhqn1_childeventid on event.eventxevent (childeventid);
create index fki35b9qnbbkiv3vxem4lbvlhov_classificationid on event.eventxevent (classificationid);
create index fkm690re4pvdyyeo38mfwgatnhk_enterpriseid on event.eventxevent (enterpriseid);
create index fko7pb9lg0uhpllekuxaeml1xip_systemid on event.eventxevent (systemid);
create index fks7qe80gktw6nesd2ijfu3j438_activeflagid on event.eventxevent (activeflagid);
create index fk5ce8ibb6xf53yy6w5nvrna86b_securitytokenid on event.eventxeventsecuritytoken (securitytokenid);
create index fk6k5gowbbqddlswcein88ndcuf_systemid on event.eventxeventsecuritytoken (systemid);
create index fk7kw08dh8lncu1lfruwll5ejtm_originalsourcesystemid on event.eventxeventsecuritytoken (originalsourcesystemid);
create index fkaddyvw0he74ynmpwy5rtvn26r_enterpriseid on event.eventxeventsecuritytoken (enterpriseid);
create index fkaybh5nk6h1pn25l38m9sb807f_eventxeventid on event.eventxeventsecuritytoken (eventxeventid);
create index fkhljdtw3p9eoe701gg8a3o21iy_activeflagid on event.eventxeventsecuritytoken (activeflagid);
create index fk2s0kehqs4vd8un8pa781t8abk_originalsourcesystemid on event.eventxeventtype (originalsourcesystemid);
create index fkh0rt5ii9pw04gwly7db3xu1k_eventid on event.eventxeventtype (eventid);
create index fkid8h6sd0odvwwwicoto22miy8_activeflagid on event.eventxeventtype (activeflagid);
create index fkp99ppwwnp2w9bnqg5pmf8b2hv_eventtypeid on event.eventxeventtype (eventtypeid);
create index fkpurdwniain7du77wld6bi9kfk_classificationid on event.eventxeventtype (classificationid);
create index fkqf8vxnj6qupg8u3wuiqcd247x_enterpriseid on event.eventxeventtype (enterpriseid);
create index fkw45a7dsxmiph0mrg1j2infhv_systemid on event.eventxeventtype (systemid);
create index fk70y0xcygmmf2v7dbnjwf15tuu_originalsourcesystemid on event.eventxeventtypesecuritytoken (originalsourcesystemid);
create index fkd7rws5evj37jj64ccvudlyxmc_enterpriseid on event.eventxeventtypesecuritytoken (enterpriseid);
create index fkfdhh2w146eu56jmq685a7vlo7_systemid on event.eventxeventtypesecuritytoken (systemid);
create index fki6cykrbt81enfl7o22ujsidj9_securitytokenid on event.eventxeventtypesecuritytoken (securitytokenid);
create index fknq4vx7pf7dug9j4pr7u3mxas7_eventxeventtypeid on event.eventxeventtypesecuritytoken (eventxeventtypeid);
create index fkvymcuqjxqpd4so83dmpnj5mk_activeflagid on event.eventxeventtypesecuritytoken (activeflagid);
create index fk7ttvl8fdvn2nghkfr1u5adhgx_classificationid on event.eventxgeography (classificationid);
create index fk9hobmhai131oqagl3obeablxc_eventid on event.eventxgeography (eventid);
create index fkbe9ptj3nqor5lbwjb48yl44jg_activeflagid on event.eventxgeography (activeflagid);
create index fki5u3ixu9xfcy27ntux56f0gx6_enterpriseid on event.eventxgeography (enterpriseid);
create index fkiyrhx2xjplgljfvo8swoilr1q_systemid on event.eventxgeography (systemid);
create index fkohfy1ao57carvvjcwcok5sch5_originalsourcesystemid on event.eventxgeography (originalsourcesystemid);
create index fkqnbhmdykbnj6recqytrcm5x52_geographyid on event.eventxgeography (geographyid);
create index fk1i4qgb7md1vd911fwvxii42tx_securitytokenid on event.eventxgeographysecuritytoken (securitytokenid);
create index fk3k3bo2t7xd8q3va2g8whp7l7v_eventxgeographyid on event.eventxgeographysecuritytoken (eventxgeographyid);
create index fk85ggy74dqr0025fhhn8nva2v1_originalsourcesystemid on event.eventxgeographysecuritytoken (originalsourcesystemid);
create index fkiw30isi83vw6xrvmxj8nou8lf_systemid on event.eventxgeographysecuritytoken (systemid);
create index fklr37auqe3m87viorqc1tsmkn_enterpriseid on event.eventxgeographysecuritytoken (enterpriseid);
create index fkrekwdq9yy06eyoqgiwlh0qw4n_activeflagid on event.eventxgeographysecuritytoken (activeflagid);
create index fk14n4sdaea68x8ye5kdnm92m4v_eventid on event.eventxinvolvedparty (eventid);
create index fk8xntnjn0yestyj21n227fwi9t_originalsourcesystemid on event.eventxinvolvedparty (originalsourcesystemid);
create index fk9eoeiem966sixemrfgnq76211_enterpriseid on event.eventxinvolvedparty (enterpriseid);
create index fkcr5fhf97fu39srlrsjwm8bjsw_involvedpartyid on event.eventxinvolvedparty (involvedpartyid);
create index fkiynqnmd943q3oxlqkjvdah2h_activeflagid on event.eventxinvolvedparty (activeflagid);
create index fkm4w7sl97j4p81cd2rehtd5jpd_systemid on event.eventxinvolvedparty (systemid);
create index fksoe05vt9d1tt0ot0rd7ns68l0_classificationid on event.eventxinvolvedparty (classificationid);
create index fk6ckmfi6y2y2topapiybevk1rg_systemid on event.eventxinvolvedpartysecuritytoken (systemid);
create index fk72yo43h7hi9spviftt74k46si_originalsourcesystemid on event.eventxinvolvedpartysecuritytoken (originalsourcesystemid);
create index fkgechwpr8prmtfarbxf4s395tu_enterpriseid on event.eventxinvolvedpartysecuritytoken (enterpriseid);
create index fkh04u3m7l7xqsx19gjxiq084nw_eventxinvolvedpartyid on event.eventxinvolvedpartysecuritytoken (eventxinvolvedpartyid);
create index fkin148rfphl52k2k9ihia0993r_activeflagid on event.eventxinvolvedpartysecuritytoken (activeflagid);
create index fkssmudrecemndf6iufso5jpyoi_securitytokenid on event.eventxinvolvedpartysecuritytoken (securitytokenid);
create index fk2wg3fp1s1jjl3lng6uak99fr4_originalsourcesystemid on event.eventxproduct (originalsourcesystemid);
create index fk6kw3pcx56g4uy28x9t6q9w5g3_classificationid on event.eventxproduct (classificationid);
create index fk9dtyhip89oe1wyjki0ph2kynb_activeflagid on event.eventxproduct (activeflagid);
create index fkfyq4xws9l9ndtorw2cgrbtc5b_eventid on event.eventxproduct (eventid);
create index fkgxnd3jbg2f39e8ik5c0ivxdru_enterpriseid on event.eventxproduct (enterpriseid);
create index fkiskxy3i7nn1pjcegr6e1kmuxh_productid on event.eventxproduct (productid);
create index fkqj3yu6g3em9mm3gabikm12hke_systemid on event.eventxproduct (systemid);
create index fk7wsudn2jsus0x9h44mrgv4nad_securitytokenid on event.eventxproductsecuritytoken (securitytokenid);
create index fkgl97bfv9e5ua7l2clt415514f_originalsourcesystemid on event.eventxproductsecuritytoken (originalsourcesystemid);
create index fkgpkp7841e76d4r6jv2drrorjf_enterpriseid on event.eventxproductsecuritytoken (enterpriseid);
create index fkn5dxxoefbl6hg9dm8wpthnyro_eventxproductid on event.eventxproductsecuritytoken (eventxproductid);
create index fkod2lhedbtyh8y4sd0dg38l3dc_systemid on event.eventxproductsecuritytoken (systemid);
create index fkt6d41dk6xtiue499wasf738r_activeflagid on event.eventxproductsecuritytoken (activeflagid);
create index fk30l7kjmsyo5va5n3exkkl364i_eventid on event.eventxresourceitem (eventid);
create index fkbyjir0fafd1lcw9bta2vjmc1n_classificationid on event.eventxresourceitem (classificationid);
create index fkfpea4o9v60x6us7p60hss1aqu_originalsourcesystemid on event.eventxresourceitem (originalsourcesystemid);
create index fkk74s5n31kgmbvmr362yle6kvd_resourceitemid on event.eventxresourceitem (resourceitemid);
create index fkmioseq29u1gk2kh4uhxsrpr5b_enterpriseid on event.eventxresourceitem (enterpriseid);
create index fkns5wkh4kiqo2l7a16029ntkpg_systemid on event.eventxresourceitem (systemid);
create index fkq5cq69kmrvba5howgpqkpyv2t_activeflagid on event.eventxresourceitem (activeflagid);
create index fkds5ohuq35i98a2mbbtiv844uq_activeflagid on event.eventxresourceitemsecuritytoken (activeflagid);
create index fkfwm7s7q3f572ajckfelr5c25d_originalsourcesystemid on event.eventxresourceitemsecuritytoken (originalsourcesystemid);
create index fkmoagxghmr17ytrthakoidoxvn_eventxresourceitemid on event.eventxresourceitemsecuritytoken (eventxresourceitemid);
create index fknjjtfvj551fm36x4gwx2ny47f_systemid on event.eventxresourceitemsecuritytoken (systemid);
create index fko04e70wvi9b3hehhsu7pjj30y_enterpriseid on event.eventxresourceitemsecuritytoken (enterpriseid);
create index fkq8fobq1u0tsuvj481ya8icdhe_securitytokenid on event.eventxresourceitemsecuritytoken (securitytokenid);
create index fk3pv6r2calb05a669dg4aaqvy3_rulesid on event.eventxrules (rulesid);
create index fk6kh594jdfv92e12q994afvkq2_classificationid on event.eventxrules (classificationid);
create index fk795v6hn2a3tim8hhf6ery2yf8_activeflagid on event.eventxrules (activeflagid);
create index fk9koqxie6a6q0b4elyxadxe9bi_eventid on event.eventxrules (eventid);
create index fkavxgy5uwlh1u87pyd3wr431w_systemid on event.eventxrules (systemid);
create index fkb3ssh2npba39x5f4e6hofnsej_originalsourcesystemid on event.eventxrules (originalsourcesystemid);
create index fkfsgbiovv87rrf7llv66q5li2d_enterpriseid on event.eventxrules (enterpriseid);
create index fk4cfg8atxhbgoya17d1vp1bpf7_activeflagid on event.eventxrulessecuritytoken (activeflagid);
create index fkd42bwmq984cam0ylnap2s3wpc_originalsourcesystemid on event.eventxrulessecuritytoken (originalsourcesystemid);
create index fke143eyeb17r5ye7xvqygem8a6_securitytokenid on event.eventxrulessecuritytoken (securitytokenid);
create index fkfxyf5j49rr38955mbno4ynfqv_systemid on event.eventxrulessecuritytoken (systemid);
create index fkkosmoasfra60wqac2vrx5qddw_enterpriseid on event.eventxrulessecuritytoken (enterpriseid);
create index fkos5etiopqb270oj8wjby9dld9_eventxrulesid on event.eventxrulessecuritytoken (eventxrulesid);


CREATE INDEX idx_eventtypessecuritytoken_effectivefromdate ON event.eventtypessecuritytoken (effectivefromdate);
CREATE INDEX idx_eventtypessecuritytoken_effectivetodate ON event.eventtypessecuritytoken (effectivetodate);
CREATE INDEX idx_eventtypessecuritytoken_warehousecreatedtimestamp ON event.eventtypessecuritytoken (warehousecreatedtimestamp);
CREATE INDEX idx_eventtypessecuritytoken_warehouselastupdatedtimestamp ON event.eventtypessecuritytoken (warehouselastupdatedtimestamp);
CREATE INDEX idx_eventxclassification_effectivefromdate ON event.eventxclassification (effectivefromdate);
CREATE INDEX idx_eventxclassification_effectivetodate ON event.eventxclassification (effectivetodate);
CREATE INDEX idx_eventxclassification_warehousecreatedtimestamp ON event.eventxclassification (warehousecreatedtimestamp);
CREATE INDEX idx_eventxclassification_warehouselastupdatedtimestamp ON event.eventxclassification (warehouselastupdatedtimestamp);

CREATE INDEX idx_event_effectivefromdate ON event.event (effectivefromdate);
CREATE INDEX idx_event_effectivetodate ON event.event (effectivetodate);
CREATE INDEX idx_event_warehousecreatedtimestamp ON event.event (warehousecreatedtimestamp);
CREATE INDEX idx_event_warehouselastupdatedtimestamp ON event.event (warehouselastupdatedtimestamp);
CREATE INDEX idx_eventxaddresssecuritytoken_effectivefromdate ON event.eventxaddresssecuritytoken (effectivefromdate);
CREATE INDEX idx_eventxaddresssecuritytoken_effectivetodate ON event.eventxaddresssecuritytoken (effectivetodate);
CREATE INDEX idx_eventxaddresssecuritytoken_warehousecreatedtimestamp ON event.eventxaddresssecuritytoken (warehousecreatedtimestamp);
CREATE INDEX idx_eventxaddresssecuritytoken_warehouselastupdatedtimestamp ON event.eventxaddresssecuritytoken (warehouselastupdatedtimestamp);
CREATE INDEX idx_eventxarrangementssecuritytoken_effectivefromdate ON event.eventxarrangementssecuritytoken (effectivefromdate);
CREATE INDEX idx_eventxarrangementssecuritytoken_effectivetodate ON event.eventxarrangementssecuritytoken (effectivetodate);
CREATE INDEX idx_eventxarrangementssecuritytoken_warehousecreatedtimestamp ON event.eventxarrangementssecuritytoken (warehousecreatedtimestamp);
CREATE INDEX idx_eventxarrangementssecuritytoken_warehouselastupdatedtimest ON event.eventxarrangementssecuritytoken (warehouselastupdatedtimestamp);
CREATE INDEX idx_eventxclassificationsecuritytoken_effectivefromdate ON event.eventxclassificationsecuritytoken (effectivefromdate);
CREATE INDEX idx_eventxclassificationsecuritytoken_effectivetodate ON event.eventxclassificationsecuritytoken (effectivetodate);
CREATE INDEX idx_eventxclassificationsecuritytoken_warehousecreatedtimestam ON event.eventxclassificationsecuritytoken (warehousecreatedtimestamp);
CREATE INDEX idx_eventxclassificationsecuritytoken_warehouselastupdatedtime ON event.eventxclassificationsecuritytoken (warehouselastupdatedtimestamp);
CREATE INDEX idx_eventtype_effectivefromdate ON event.eventtype (effectivefromdate);
CREATE INDEX idx_eventtype_effectivetodate ON event.eventtype (effectivetodate);
CREATE INDEX idx_eventtype_warehousecreatedtimestamp ON event.eventtype (warehousecreatedtimestamp);
CREATE INDEX idx_eventtype_warehouselastupdatedtimestamp ON event.eventtype (warehouselastupdatedtimestamp);
CREATE INDEX idx_eventxaddress_effectivefromdate ON event.eventxaddress (effectivefromdate);
CREATE INDEX idx_eventxaddress_effectivetodate ON event.eventxaddress (effectivetodate);
CREATE INDEX idx_eventxaddress_warehousecreatedtimestamp ON event.eventxaddress (warehousecreatedtimestamp);
CREATE INDEX idx_eventxaddress_warehouselastupdatedtimestamp ON event.eventxaddress (warehouselastupdatedtimestamp);
CREATE INDEX idx_eventsecuritytoken_effectivefromdate ON event.eventsecuritytoken (effectivefromdate);
CREATE INDEX idx_eventsecuritytoken_effectivetodate ON event.eventsecuritytoken (effectivetodate);
CREATE INDEX idx_eventsecuritytoken_warehousecreatedtimestamp ON event.eventsecuritytoken (warehousecreatedtimestamp);
CREATE INDEX idx_eventsecuritytoken_warehouselastupdatedtimestamp ON event.eventsecuritytoken (warehouselastupdatedtimestamp);
CREATE INDEX idx_eventxarrangement_effectivefromdate ON event.eventxarrangement (effectivefromdate);
CREATE INDEX idx_eventxarrangement_effectivetodate ON event.eventxarrangement (effectivetodate);
CREATE INDEX idx_eventxarrangement_warehousecreatedtimestamp ON event.eventxarrangement (warehousecreatedtimestamp);
CREATE INDEX idx_eventxarrangement_warehouselastupdatedtimestamp ON event.eventxarrangement (warehouselastupdatedtimestamp);
CREATE INDEX idx_eventxinvolvedpartysecuritytoken_effectivefromdate ON event.eventxinvolvedpartysecuritytoken (effectivefromdate);
CREATE INDEX idx_eventxinvolvedpartysecuritytoken_effectivetodate ON event.eventxinvolvedpartysecuritytoken (effectivetodate);
CREATE INDEX idx_eventxinvolvedpartysecuritytoken_warehousecreatedtimestamp ON event.eventxinvolvedpartysecuritytoken (warehousecreatedtimestamp);
CREATE INDEX idx_eventxinvolvedpartysecuritytoken_warehouselastupdatedtimes ON event.eventxinvolvedpartysecuritytoken (warehouselastupdatedtimestamp);
CREATE INDEX idx_eventxgeographysecuritytoken_effectivefromdate ON event.eventxgeographysecuritytoken (effectivefromdate);
CREATE INDEX idx_eventxgeographysecuritytoken_effectivetodate ON event.eventxgeographysecuritytoken (effectivetodate);
CREATE INDEX idx_eventxgeographysecuritytoken_warehousecreatedtimestamp ON event.eventxgeographysecuritytoken (warehousecreatedtimestamp);
CREATE INDEX idx_eventxgeographysecuritytoken_warehouselastupdatedtimestamp ON event.eventxgeographysecuritytoken (warehouselastupdatedtimestamp);
CREATE INDEX idx_eventxinvolvedparty_effectivefromdate ON event.eventxinvolvedparty (effectivefromdate);
CREATE INDEX idx_eventxinvolvedparty_effectivetodate ON event.eventxinvolvedparty (effectivetodate);
CREATE INDEX idx_eventxinvolvedparty_warehousecreatedtimestamp ON event.eventxinvolvedparty (warehousecreatedtimestamp);
CREATE INDEX idx_eventxinvolvedparty_warehouselastupdatedtimestamp ON event.eventxinvolvedparty (warehouselastupdatedtimestamp);
CREATE INDEX idx_eventxeventtype_effectivefromdate ON event.eventxeventtype (effectivefromdate);
CREATE INDEX idx_eventxeventtype_effectivetodate ON event.eventxeventtype (effectivetodate);
CREATE INDEX idx_eventxeventtype_warehousecreatedtimestamp ON event.eventxeventtype (warehousecreatedtimestamp);
CREATE INDEX idx_eventxeventtype_warehouselastupdatedtimestamp ON event.eventxeventtype (warehouselastupdatedtimestamp);
CREATE INDEX idx_eventxgeography_effectivefromdate ON event.eventxgeography (effectivefromdate);
CREATE INDEX idx_eventxgeography_effectivetodate ON event.eventxgeography (effectivetodate);
CREATE INDEX idx_eventxgeography_warehousecreatedtimestamp ON event.eventxgeography (warehousecreatedtimestamp);
CREATE INDEX idx_eventxgeography_warehouselastupdatedtimestamp ON event.eventxgeography (warehouselastupdatedtimestamp);
CREATE INDEX idx_eventxeventtypesecuritytoken_effectivefromdate ON event.eventxeventtypesecuritytoken (effectivefromdate);
CREATE INDEX idx_eventxeventtypesecuritytoken_effectivetodate ON event.eventxeventtypesecuritytoken (effectivetodate);
CREATE INDEX idx_eventxeventtypesecuritytoken_warehousecreatedtimestamp ON event.eventxeventtypesecuritytoken (warehousecreatedtimestamp);
CREATE INDEX idx_eventxeventtypesecuritytoken_warehouselastupdatedtimestamp ON event.eventxeventtypesecuritytoken (warehouselastupdatedtimestamp);
CREATE INDEX idx_eventxrules_effectivefromdate ON event.eventxrules (effectivefromdate);
CREATE INDEX idx_eventxrules_effectivetodate ON event.eventxrules (effectivetodate);
CREATE INDEX idx_eventxrules_warehousecreatedtimestamp ON event.eventxrules (warehousecreatedtimestamp);
CREATE INDEX idx_eventxrules_warehouselastupdatedtimestamp ON event.eventxrules (warehouselastupdatedtimestamp);
CREATE INDEX idx_eventxrulessecuritytoken_effectivefromdate ON event.eventxrulessecuritytoken (effectivefromdate);
CREATE INDEX idx_eventxrulessecuritytoken_effectivetodate ON event.eventxrulessecuritytoken (effectivetodate);
CREATE INDEX idx_eventxrulessecuritytoken_warehousecreatedtimestamp ON event.eventxrulessecuritytoken (warehousecreatedtimestamp);
CREATE INDEX idx_eventxrulessecuritytoken_warehouselastupdatedtimestamp ON event.eventxrulessecuritytoken (warehouselastupdatedtimestamp);



CREATE INDEX idx_eventxproduct_effectivefromdate ON event.eventxproduct (effectivefromdate);
CREATE INDEX idx_eventxproduct_effectivetodate ON event.eventxproduct (effectivetodate);
CREATE INDEX idx_eventxproduct_warehousecreatedtimestamp ON event.eventxproduct (warehousecreatedtimestamp);
CREATE INDEX idx_eventxproduct_warehouselastupdatedtimestamp ON event.eventxproduct (warehouselastupdatedtimestamp);
CREATE INDEX idx_eventxresourceitem_effectivefromdate ON event.eventxresourceitem (effectivefromdate);
CREATE INDEX idx_eventxresourceitem_effectivetodate ON event.eventxresourceitem (effectivetodate);
CREATE INDEX idx_eventxresourceitem_warehousecreatedtimestamp ON event.eventxresourceitem (warehousecreatedtimestamp);
CREATE INDEX idx_eventxresourceitem_warehouselastupdatedtimestamp ON event.eventxresourceitem (warehouselastupdatedtimestamp);
CREATE INDEX idx_eventxproductsecuritytoken_effectivefromdate ON event.eventxproductsecuritytoken (effectivefromdate);
CREATE INDEX idx_eventxproductsecuritytoken_effectivetodate ON event.eventxproductsecuritytoken (effectivetodate);
CREATE INDEX idx_eventxproductsecuritytoken_warehousecreatedtimestamp ON event.eventxproductsecuritytoken (warehousecreatedtimestamp);
CREATE INDEX idx_eventxproductsecuritytoken_warehouselastupdatedtimestamp ON event.eventxproductsecuritytoken (warehouselastupdatedtimestamp);
CREATE INDEX idx_eventxresourceitemsecuritytoken_effectivefromdate ON event.eventxresourceitemsecuritytoken (effectivefromdate);
CREATE INDEX idx_eventxresourceitemsecuritytoken_effectivetodate ON event.eventxresourceitemsecuritytoken (effectivetodate);
CREATE INDEX idx_eventxresourceitemsecuritytoken_warehousecreatedtimestamp ON event.eventxresourceitemsecuritytoken (warehousecreatedtimestamp);
CREATE INDEX idx_eventxresourceitemsecuritytoken_warehouselastupdatedtimest ON event.eventxresourceitemsecuritytoken (warehouselastupdatedtimestamp);


CREATE INDEX idx_eventxevent_effectivefromdate ON event.eventxevent (effectivefromdate);
CREATE INDEX idx_eventxevent_effectivetodate ON event.eventxevent (effectivetodate);
CREATE INDEX idx_eventxevent_warehousecreatedtimestamp ON event.eventxevent (warehousecreatedtimestamp);
CREATE INDEX idx_eventxevent_warehouselastupdatedtimestamp ON event.eventxevent (warehouselastupdatedtimestamp);
CREATE INDEX idx_eventxeventsecuritytoken_effectivefromdate ON event.eventxeventsecuritytoken (effectivefromdate);
CREATE INDEX idx_eventxeventsecuritytoken_effectivetodate ON event.eventxeventsecuritytoken (effectivetodate);
CREATE INDEX idx_eventxeventsecuritytoken_warehousecreatedtimestamp ON event.eventxeventsecuritytoken (warehousecreatedtimestamp);
CREATE INDEX idx_eventxeventsecuritytoken_warehouselastupdatedtimestamp ON event.eventxeventsecuritytoken (warehouselastupdatedtimestamp);


CREATE INDEX idx_eventxclassification_value ON event.eventxclassification (value);

CREATE INDEX idx_eventxaddress_value ON event.eventxaddress (value);
CREATE INDEX idx_eventxarrangement_value ON event.eventxarrangement (value);
CREATE INDEX idx_eventxinvolvedparty_value ON event.eventxinvolvedparty (value);
CREATE INDEX idx_eventxeventtype_value ON event.eventxeventtype (value);
CREATE INDEX idx_eventxgeography_value ON event.eventxgeography (value);
CREATE INDEX idx_eventxrules_value ON event.eventxrules (value);
CREATE INDEX idx_eventxproduct_value ON event.eventxproduct (value);
CREATE INDEX idx_eventxresourceitem_value ON event.eventxresourceitem (value);

CREATE INDEX idx_eventxevent_value ON event.eventxevent (value);
CREATE INDEX idx_eventtype_eventtypedesc ON event.eventtype (eventtypedesc);
CREATE INDEX idx_eventtype_eventtypename ON event.eventtype (eventtypename);


create index fk5a6m41a8gggwx8fqsrs4at8tg_originalsourcesystemidwhcd on event.event (originalsourcesystemid, warehousefromdate);
create index fk6ifge8hyt5n7895pe83e7l8ic_enterpriseidwhcd on event.event (enterpriseid, warehousefromdate);
create index fk944ydkbqk682cotb21mlyedhc_activeflagidwhcd on event.event (activeflagid, warehousefromdate);
create index fkt9syxmvp6c5pcyhyiv9c8qpb1_systemidwhcd on event.event (systemid, warehousefromdate);
create index fk5l2t98n1cb4rsa23u81thgv50_securitytokenidwhcd on event.eventsecuritytoken (securitytokenid, warehousefromdate);
create index fkb5qngfntxfvo5cn45y0afpq6x_enterpriseidwhcd on event.eventsecuritytoken (enterpriseid, warehousefromdate);
create index fkmwxej7o84e382w8iy2y6fwlmf_activeflagidwhcd on event.eventsecuritytoken (activeflagid, warehousefromdate);
create index fko5kqswpu433kf5hcc3yh1w10e_systemidwhcd on event.eventsecuritytoken (systemid, warehousefromdate);
create index fkqcwlmmiy1hf3p3ol2dy865mx5_eventsidwhcd on event.eventsecuritytoken (eventsid, warehousefromdate);
create index fksa47kc62uve787xbc9h6syv0t_originalsourcesystemidwhcd on event.eventsecuritytoken (originalsourcesystemid, warehousefromdate);
create index fk3a6ig1a98nle8uhil7c00wtxj_activeflagidwhcd on event.eventtype (activeflagid, warehousefromdate);
create index fk7bpo42s41tkfou4bopgs8xeqg_originalsourcesystemidwhcd on event.eventtype (originalsourcesystemid, warehousefromdate);
create index fk91msecp4vf69nvn0t6362tmvy_enterpriseidwhcd on event.eventtype (enterpriseid, warehousefromdate);
create index fkd28eytnr3sps354vu7k75r6ds_systemidwhcd on event.eventtype (systemid, warehousefromdate);
create index fk3sqx94u3an8m27b0aotdca2wb_securitytokenidwhcd on event.eventtypessecuritytoken (securitytokenid, warehousefromdate);
create index fk533lul2cwy1gxqwdlkwot3xli_eventtypesidwhcd on event.eventtypessecuritytoken (eventtypesid, warehousefromdate);
create index fk69no16tl8ojgbi6so0jp3s68a_originalsourcesystemidwhcd on event.eventtypessecuritytoken (originalsourcesystemid, warehousefromdate);
create index fke2wss5k3p8r4nxol9e95m3k3q_activeflagidwhcd on event.eventtypessecuritytoken (activeflagid, warehousefromdate);
create index fkr2xa1q4402m8pc5evyj1tw9q5_enterpriseidwhcd on event.eventtypessecuritytoken (enterpriseid, warehousefromdate);
create index fktcukxvcv6bvhbmcelmt16vroq_systemidwhcd on event.eventtypessecuritytoken (systemid, warehousefromdate);
create index fk3bbi34ppkvf938mm8618n7cdi_enterpriseidwhcd on event.eventxaddress (enterpriseid, warehousefromdate);
create index fkd6wdra1kpon3dn8wcfjrk6msl_addressidwhcd on event.eventxaddress (addressid, warehousefromdate);
create index fkf3dfxx8n3ijrkin1n3rr8jcek_classificationidwhcd on event.eventxaddress (classificationid, warehousefromdate);
create index fkjkfm7v3719rvi0mu4nev2xu6d_eventidwhcd on event.eventxaddress (eventid, warehousefromdate);
create index fklmpsaajrvosc033g4idqh17bv_activeflagidwhcd on event.eventxaddress (activeflagid, warehousefromdate);
create index fkm52nfxs3cy5syh6ar5mly3k64_systemidwhcd on event.eventxaddress (systemid, warehousefromdate);
create index fkmtwd6myq1nc0g02s2s2nhkteo_originalsourcesystemidwhcd on event.eventxaddress (originalsourcesystemid, warehousefromdate);
create index fk1b8yahv9f17vt9brstr9lu5s_securitytokenidwhcd on event.eventxaddresssecuritytoken (securitytokenid, warehousefromdate);
create index fk2n5hwft2sainpe6l7cerwj4o6_systemidwhcd on event.eventxaddresssecuritytoken (systemid, warehousefromdate);
create index fk4delkrhbqq9uhqpsmqbdc8eh1_enterpriseidwhcd on event.eventxaddresssecuritytoken (enterpriseid, warehousefromdate);
create index fk6frs7aq2uychxsnl6r89fi7on_eventxaddressidwhcd on event.eventxaddresssecuritytoken (eventxaddressid, warehousefromdate);
create index fk86mt4pshsp9bxjaubl1729701_originalsourcesystemidwhcd on event.eventxaddresssecuritytoken (originalsourcesystemid, warehousefromdate);
create index fkd7r65wrbjj132a0hama4srqfu_activeflagidwhcd on event.eventxaddresssecuritytoken (activeflagid, warehousefromdate);
create index fk7607u935wf0x8yuf84h0xseyl_arrangementidwhcd on event.eventxarrangement (arrangementid, warehousefromdate);
create index fk7tkxsheat6us20jq1hhsmhkan_originalsourcesystemidwhcd on event.eventxarrangement (originalsourcesystemid, warehousefromdate);
create index fk9l3iwtdok5r27i83ywbjt0nto_enterpriseidwhcd on event.eventxarrangement (enterpriseid, warehousefromdate);
create index fkhyhd3s7f217ugwq3wo7pk5t1y_activeflagidwhcd on event.eventxarrangement (activeflagid, warehousefromdate);
create index fkii5wwqv91rn4ji3nwgiy3wd2l_eventidwhcd on event.eventxarrangement (eventid, warehousefromdate);
create index fklj26m0snewjsmso5n6jskt2a4_systemidwhcd on event.eventxarrangement (systemid, warehousefromdate);
create index fkpwoqs4xag1rw5tyrb062tyagd_classificationidwhcd on event.eventxarrangement (classificationid, warehousefromdate);
create index fk2tudn6gio54xpxa5ecyryl2v0_systemidwhcd on event.eventxarrangementssecuritytoken (systemid, warehousefromdate);
create index fk33w71cp61prmc7i0uboecicda_enterpriseidwhcd on event.eventxarrangementssecuritytoken (enterpriseid, warehousefromdate);
create index fkom7ax04agkpio3h8xw30vb60m_activeflagidwhcd on event.eventxarrangementssecuritytoken (activeflagid, warehousefromdate);
create index fkq3ik0e8bbij1nqvdu0jn1unvg_eventxarrangementsidwhcd on event.eventxarrangementssecuritytoken (eventxarrangementsid, warehousefromdate);
create index fks8terumw31sybh6o0x4sdbc2t_securitytokenidwhcd on event.eventxarrangementssecuritytoken (securitytokenid, warehousefromdate);
create index fktiw0truwtwa3ublkae3jexugw_originalsourcesystemidwhcd on event.eventxarrangementssecuritytoken (originalsourcesystemid, warehousefromdate);
create index fk7gqyx4i1xca4ioveotqcctmd3_enterpriseidwhcd on event.eventxclassification (enterpriseid, warehousefromdate);
create index fkf1gqpk872jacljscf2kv0i24r_eventidwhcd on event.eventxclassification (eventid, warehousefromdate);
create index fko28x8wv64sef2ifxjm8a4i2ft_classificationidwhcd on event.eventxclassification (classificationid, warehousefromdate);
create index fkojl271yprimpjrrv6u1a2ggb6_activeflagidwhcd on event.eventxclassification (activeflagid, warehousefromdate);
create index fkp7fx787hsyq9nf8820wl2rh1x_originalsourcesystemidwhcd on event.eventxclassification (originalsourcesystemid, warehousefromdate);
create index fkpssgfk2o0m8r84lhnb04bh0om_systemidwhcd on event.eventxclassification (systemid, warehousefromdate);
create index fk2uwfplnar8o0onw3e93phd69k_enterpriseidwhcd on event.eventxclassificationsecuritytoken (enterpriseid, warehousefromdate);
create index fk4hjmqpqf99dnag748layycl1d_activeflagidwhcd on event.eventxclassificationsecuritytoken (activeflagid, warehousefromdate);
create index fkfoq8c32icxr3vxx3t050nwdqm_eventxclassificationsidwhcd on event.eventxclassificationsecuritytoken (eventxclassificationsid, warehousefromdate);
create index fkg821wl3o2mwy3k7ysebk2donl_securitytokenidwhcd on event.eventxclassificationsecuritytoken (securitytokenid, warehousefromdate);
create index fkkdkwjb6nhj76m65s840einvmw_originalsourcesystemidwhcd on event.eventxclassificationsecuritytoken (originalsourcesystemid, warehousefromdate);
create index fkoq4421g5p0yjlklv0231xbjel_systemidwhcd on event.eventxclassificationsecuritytoken (systemid, warehousefromdate);
create index fk27gym41nw6dls3e5oll0nkcbr_originalsourcesystemidwhcd on event.eventxevent (originalsourcesystemid, warehousefromdate);
create index fk6gr2cpldlbe8dfgk92iceiksh_parenteventidwhcd on event.eventxevent (parenteventid, warehousefromdate);
create index fk8q11uo1x1xogw2rytdqbnhqn1_childeventidwhcd on event.eventxevent (childeventid, warehousefromdate);
create index fki35b9qnbbkiv3vxem4lbvlhov_classificationidwhcd on event.eventxevent (classificationid, warehousefromdate);
create index fkm690re4pvdyyeo38mfwgatnhk_enterpriseidwhcd on event.eventxevent (enterpriseid, warehousefromdate);
create index fko7pb9lg0uhpllekuxaeml1xip_systemidwhcd on event.eventxevent (systemid, warehousefromdate);
create index fks7qe80gktw6nesd2ijfu3j438_activeflagidwhcd on event.eventxevent (activeflagid, warehousefromdate);
create index fk5ce8ibb6xf53yy6w5nvrna86b_securitytokenidwhcd on event.eventxeventsecuritytoken (securitytokenid, warehousefromdate);
create index fk6k5gowbbqddlswcein88ndcuf_systemidwhcd on event.eventxeventsecuritytoken (systemid, warehousefromdate);
create index fk7kw08dh8lncu1lfruwll5ejtm_originalsourcesystemidwhcd on event.eventxeventsecuritytoken (originalsourcesystemid, warehousefromdate);
create index fkaddyvw0he74ynmpwy5rtvn26r_enterpriseidwhcd on event.eventxeventsecuritytoken (enterpriseid, warehousefromdate);
create index fkaybh5nk6h1pn25l38m9sb807f_eventxeventidwhcd on event.eventxeventsecuritytoken (eventxeventid, warehousefromdate);
create index fkhljdtw3p9eoe701gg8a3o21iy_activeflagidwhcd on event.eventxeventsecuritytoken (activeflagid, warehousefromdate);
create index fk2s0kehqs4vd8un8pa781t8abk_originalsourcesystemidwhcd on event.eventxeventtype (originalsourcesystemid, warehousefromdate);
create index fkh0rt5ii9pw04gwly7db3xu1k_eventidwhcd on event.eventxeventtype (eventid, warehousefromdate);
create index fkid8h6sd0odvwwwicoto22miy8_activeflagidwhcd on event.eventxeventtype (activeflagid, warehousefromdate);
create index fkp99ppwwnp2w9bnqg5pmf8b2hv_eventtypeidwhcd on event.eventxeventtype (eventtypeid, warehousefromdate);
create index fkpurdwniain7du77wld6bi9kfk_classificationidwhcd on event.eventxeventtype (classificationid, warehousefromdate);
create index fkqf8vxnj6qupg8u3wuiqcd247x_enterpriseidwhcd on event.eventxeventtype (enterpriseid, warehousefromdate);
create index fkw45a7dsxmiph0mrg1j2infhv_systemidwhcd on event.eventxeventtype (systemid, warehousefromdate);
create index fk70y0xcygmmf2v7dbnjwf15tuu_originalsourcesystemidwhcd on event.eventxeventtypesecuritytoken (originalsourcesystemid, warehousefromdate);
create index fkd7rws5evj37jj64ccvudlyxmc_enterpriseidwhcd on event.eventxeventtypesecuritytoken (enterpriseid, warehousefromdate);
create index fkfdhh2w146eu56jmq685a7vlo7_systemidwhcd on event.eventxeventtypesecuritytoken (systemid, warehousefromdate);
create index fki6cykrbt81enfl7o22ujsidj9_securitytokenidwhcd on event.eventxeventtypesecuritytoken (securitytokenid, warehousefromdate);
create index fknq4vx7pf7dug9j4pr7u3mxas7_eventxeventtypeidwhcd on event.eventxeventtypesecuritytoken (eventxeventtypeid, warehousefromdate);
create index fkvymcuqjxqpd4so83dmpnj5mk_activeflagidwhcd on event.eventxeventtypesecuritytoken (activeflagid, warehousefromdate);
create index fk7ttvl8fdvn2nghkfr1u5adhgx_classificationidwhcd on event.eventxgeography (classificationid, warehousefromdate);
create index fk9hobmhai131oqagl3obeablxc_eventidwhcd on event.eventxgeography (eventid, warehousefromdate);
create index fkbe9ptj3nqor5lbwjb48yl44jg_activeflagidwhcd on event.eventxgeography (activeflagid, warehousefromdate);
create index fki5u3ixu9xfcy27ntux56f0gx6_enterpriseidwhcd on event.eventxgeography (enterpriseid, warehousefromdate);
create index fkiyrhx2xjplgljfvo8swoilr1q_systemidwhcd on event.eventxgeography (systemid, warehousefromdate);
create index fkohfy1ao57carvvjcwcok5sch5_originalsourcesystemidwhcd on event.eventxgeography (originalsourcesystemid, warehousefromdate);
create index fkqnbhmdykbnj6recqytrcm5x52_geographyidwhcd on event.eventxgeography (geographyid, warehousefromdate);
create index fk1i4qgb7md1vd911fwvxii42tx_securitytokenidwhcd on event.eventxgeographysecuritytoken (securitytokenid, warehousefromdate);
create index fk3k3bo2t7xd8q3va2g8whp7l7v_eventxgeographyidwhcd on event.eventxgeographysecuritytoken (eventxgeographyid, warehousefromdate);
create index fk85ggy74dqr0025fhhn8nva2v1_originalsourcesystemidwhcd on event.eventxgeographysecuritytoken (originalsourcesystemid, warehousefromdate);
create index fkiw30isi83vw6xrvmxj8nou8lf_systemidwhcd on event.eventxgeographysecuritytoken (systemid, warehousefromdate);
create index fklr37auqe3m87viorqc1tsmkn_enterpriseidwhcd on event.eventxgeographysecuritytoken (enterpriseid, warehousefromdate);
create index fkrekwdq9yy06eyoqgiwlh0qw4n_activeflagidwhcd on event.eventxgeographysecuritytoken (activeflagid, warehousefromdate);
create index fk14n4sdaea68x8ye5kdnm92m4v_eventidwhcd on event.eventxinvolvedparty (eventid, warehousefromdate);
create index fk8xntnjn0yestyj21n227fwi9t_originalsourcesystemidwhcd on event.eventxinvolvedparty (originalsourcesystemid, warehousefromdate);
create index fk9eoeiem966sixemrfgnq76211_enterpriseidwhcd on event.eventxinvolvedparty (enterpriseid, warehousefromdate);
create index fkcr5fhf97fu39srlrsjwm8bjsw_involvedpartyidwhcd on event.eventxinvolvedparty (involvedpartyid, warehousefromdate);
create index fkiynqnmd943q3oxlqkjvdah2h_activeflagidwhcd on event.eventxinvolvedparty (activeflagid, warehousefromdate);
create index fkm4w7sl97j4p81cd2rehtd5jpd_systemidwhcd on event.eventxinvolvedparty (systemid, warehousefromdate);
create index fksoe05vt9d1tt0ot0rd7ns68l0_classificationidwhcd on event.eventxinvolvedparty (classificationid, warehousefromdate);
create index fk6ckmfi6y2y2topapiybevk1rg_systemidwhcd on event.eventxinvolvedpartysecuritytoken (systemid, warehousefromdate);
create index fk72yo43h7hi9spviftt74k46si_originalsourcesystemidwhcd on event.eventxinvolvedpartysecuritytoken (originalsourcesystemid, warehousefromdate);
create index fkgechwpr8prmtfarbxf4s395tu_enterpriseidwhcd on event.eventxinvolvedpartysecuritytoken (enterpriseid, warehousefromdate);
create index fkh04u3m7l7xqsx19gjxiq084nw_eventxinvolvedpartyidwhcd on event.eventxinvolvedpartysecuritytoken (eventxinvolvedpartyid, warehousefromdate);
create index fkin148rfphl52k2k9ihia0993r_activeflagidwhcd on event.eventxinvolvedpartysecuritytoken (activeflagid, warehousefromdate);
create index fkssmudrecemndf6iufso5jpyoi_securitytokenidwhcd on event.eventxinvolvedpartysecuritytoken (securitytokenid, warehousefromdate);
create index fk2wg3fp1s1jjl3lng6uak99fr4_originalsourcesystemidwhcd on event.eventxproduct (originalsourcesystemid, warehousefromdate);
create index fk6kw3pcx56g4uy28x9t6q9w5g3_classificationidwhcd on event.eventxproduct (classificationid, warehousefromdate);
create index fk9dtyhip89oe1wyjki0ph2kynb_activeflagidwhcd on event.eventxproduct (activeflagid, warehousefromdate);
create index fkfyq4xws9l9ndtorw2cgrbtc5b_eventidwhcd on event.eventxproduct (eventid, warehousefromdate);
create index fkgxnd3jbg2f39e8ik5c0ivxdru_enterpriseidwhcd on event.eventxproduct (enterpriseid, warehousefromdate);
create index fkiskxy3i7nn1pjcegr6e1kmuxh_productidwhcd on event.eventxproduct (productid, warehousefromdate);
create index fkqj3yu6g3em9mm3gabikm12hke_systemidwhcd on event.eventxproduct (systemid, warehousefromdate);
create index fk7wsudn2jsus0x9h44mrgv4nad_securitytokenidwhcd on event.eventxproductsecuritytoken (securitytokenid, warehousefromdate);
create index fkgl97bfv9e5ua7l2clt415514f_originalsourcesystemidwhcd on event.eventxproductsecuritytoken (originalsourcesystemid, warehousefromdate);
create index fkgpkp7841e76d4r6jv2drrorjf_enterpriseidwhcd on event.eventxproductsecuritytoken (enterpriseid, warehousefromdate);
create index fkn5dxxoefbl6hg9dm8wpthnyro_eventxproductidwhcd on event.eventxproductsecuritytoken (eventxproductid, warehousefromdate);
create index fkod2lhedbtyh8y4sd0dg38l3dc_systemidwhcd on event.eventxproductsecuritytoken (systemid, warehousefromdate);
create index fkt6d41dk6xtiue499wasf738r_activeflagidwhcd on event.eventxproductsecuritytoken (activeflagid, warehousefromdate);
create index fk30l7kjmsyo5va5n3exkkl364i_eventidwhcd on event.eventxresourceitem (eventid, warehousefromdate);
create index fkbyjir0fafd1lcw9bta2vjmc1n_classificationidwhcd on event.eventxresourceitem (classificationid, warehousefromdate);
create index fkfpea4o9v60x6us7p60hss1aqu_originalsourcesystemidwhcd on event.eventxresourceitem (originalsourcesystemid, warehousefromdate);
create index fkk74s5n31kgmbvmr362yle6kvd_resourceitemidwhcd on event.eventxresourceitem (resourceitemid, warehousefromdate);
create index fkmioseq29u1gk2kh4uhxsrpr5b_enterpriseidwhcd on event.eventxresourceitem (enterpriseid, warehousefromdate);
create index fkns5wkh4kiqo2l7a16029ntkpg_systemidwhcd on event.eventxresourceitem (systemid, warehousefromdate);
create index fkq5cq69kmrvba5howgpqkpyv2t_activeflagidwhcd on event.eventxresourceitem (activeflagid, warehousefromdate);
create index fkds5ohuq35i98a2mbbtiv844uq_activeflagidwhcd on event.eventxresourceitemsecuritytoken (activeflagid, warehousefromdate);
create index fkfwm7s7q3f572ajckfelr5c25d_originalsourcesystemidwhcd on event.eventxresourceitemsecuritytoken (originalsourcesystemid, warehousefromdate);
create index fkmoagxghmr17ytrthakoidoxvn_eventxresourceitemidwhcd on event.eventxresourceitemsecuritytoken (eventxresourceitemid, warehousefromdate);
create index fknjjtfvj551fm36x4gwx2ny47f_systemidwhcd on event.eventxresourceitemsecuritytoken (systemid, warehousefromdate);
create index fko04e70wvi9b3hehhsu7pjj30y_enterpriseidwhcd on event.eventxresourceitemsecuritytoken (enterpriseid, warehousefromdate);
create index fkq8fobq1u0tsuvj481ya8icdhe_securitytokenidwhcd on event.eventxresourceitemsecuritytoken (securitytokenid, warehousefromdate);
create index fk3pv6r2calb05a669dg4aaqvy3_rulesidwhcd on event.eventxrules (rulesid, warehousefromdate);
create index fk6kh594jdfv92e12q994afvkq2_classificationidwhcd on event.eventxrules (classificationid, warehousefromdate);
create index fk795v6hn2a3tim8hhf6ery2yf8_activeflagidwhcd on event.eventxrules (activeflagid, warehousefromdate);
create index fk9koqxie6a6q0b4elyxadxe9bi_eventidwhcd on event.eventxrules (eventid, warehousefromdate);
create index fkavxgy5uwlh1u87pyd3wr431w_systemidwhcd on event.eventxrules (systemid, warehousefromdate);
create index fkb3ssh2npba39x5f4e6hofnsej_originalsourcesystemidwhcd on event.eventxrules (originalsourcesystemid, warehousefromdate);
create index fkfsgbiovv87rrf7llv66q5li2d_enterpriseidwhcd on event.eventxrules (enterpriseid, warehousefromdate);
create index fk4cfg8atxhbgoya17d1vp1bpf7_activeflagidwhcd on event.eventxrulessecuritytoken (activeflagid, warehousefromdate);
create index fkd42bwmq984cam0ylnap2s3wpc_originalsourcesystemidwhcd on event.eventxrulessecuritytoken (originalsourcesystemid, warehousefromdate);
create index fke143eyeb17r5ye7xvqygem8a6_securitytokenidwhcd on event.eventxrulessecuritytoken (securitytokenid, warehousefromdate);
create index fkfxyf5j49rr38955mbno4ynfqv_systemidwhcd on event.eventxrulessecuritytoken (systemid, warehousefromdate);
create index fkkosmoasfra60wqac2vrx5qddw_enterpriseidwhcd on event.eventxrulessecuritytoken (enterpriseid, warehousefromdate);
create index fkos5etiopqb270oj8wjby9dld9_eventxrulesidwhcd on event.eventxrulessecuritytoken (eventxrulesid, warehousefromdate);



CREATE OR REPLACE FUNCTION public.create_event_types_view(event_type_desc TEXT)
    RETURNS VOID AS
$$
DECLARE
    view_name TEXT;
    query     TEXT;
BEGIN
    -- Generate the view name dynamically by replacing spaces with underscores and appending '_received_barcodes'
    view_name := LOWER(REPLACE(event_type_desc, ' ', '_'));

    -- Build the query to create or replace the view
    query := FORMAT($f$
        CREATE OR REPLACE VIEW public.%I AS
        SELECT DISTINCT exet.value
        FROM event.event e
                 JOIN event.eventxeventtype exet ON e.eventid::text = exet.eventid::text
                 JOIN event.eventtype et ON et.eventtypeid::text = exet.eventtypeid::text
        WHERE et.eventtypedesc::text = %L
    $f$, view_name, event_type_desc);

    -- Execute the query
    EXECUTE query;

    RAISE NOTICE 'View % created successfully.', view_name;
END;
$$ LANGUAGE plpgsql;