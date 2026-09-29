CREATE SCHEMA dbo;
CREATE TABLE dbo.enterprise
(
    enterpriseid                  UUID                        NOT NULL primary key,
    effectivefromdate             timestamp(6) with time zone NOT NULL,
    effectivetodate               timestamp(6) with time zone NOT NULL,
    warehousecreatedtimestamp     timestamp(6) with time zone NOT NULL,
    warehousefromdate             DATE                        NOT NULL,

    warehouselastupdatedtimestamp timestamp(6) with time zone NOT NULL,
    enterprisedesc                character varying(255)      NOT NULL,
    enterprisename                character varying(255)      NOT NULL
);
CREATE TABLE dbo.enterprisesecuritytoken
(
    enterprisesecuritytokenid     UUID                        NOT NULL primary key,
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
    systemid                      UUID                        NOT NULL
);
CREATE TABLE dbo.enterprisexclassification
(
    enterprisexclassificationid   UUID                        NOT NULL primary key,
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

CREATE TABLE dbo.enterprisexclassificationsecuritytoken
(
    enterprisexclassificationsecuritytokenid UUID                        NOT NULL primary key,
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
    enterprisexclassificationid              UUID                        NOT NULL
);

create index fk3604iuvyi4psepv3rdn69jf76_systemid on dbo.enterprisesecuritytoken (systemid);
create index fkcja1yq5j2ywm4mearg7gfbeg8_activeflagid on dbo.enterprisesecuritytoken (activeflagid);
create index fki2166mtej2yhf4qeegbrtwsom_securitytokenid on dbo.enterprisesecuritytoken (securitytokenid);
create index fki27vhcoq25358slocd9mn9481_originalsourcesystemid on dbo.enterprisesecuritytoken (originalsourcesystemid);
create index fkkfbukc1416acs2qhjduyaslun_enterpriseid on dbo.enterprisesecuritytoken (enterpriseid);
create index fk7dcbw0medved7abfmh1y0btj_enterpriseid on dbo.enterprisexclassification (enterpriseid);
create index fk9wfebpqldiybaphp969e7vu9q_activeflagid on dbo.enterprisexclassification (activeflagid);
create index fkd8u5qov86csi5bmvn97648jyq_originalsourcesystemid on dbo.enterprisexclassification (originalsourcesystemid);
create index fkmt4djrkvv0w1ef778yy01fone_systemid on dbo.enterprisexclassification (systemid);
create index fkpubcpyc3hm0x9l70u19lj96r0_classificationid on dbo.enterprisexclassification (classificationid);
create index fk5l36jvcvedgw6gaa8301xjr62_securitytokenid on dbo.enterprisexclassificationsecuritytoken (securitytokenid);
create index fk75tx4r7sp36qtylr8r888gbkr_activeflagid on dbo.enterprisexclassificationsecuritytoken (activeflagid);
create index fk7jfxn7qp93vt98lob9q7g5exc_enterpriseid on dbo.enterprisexclassificationsecuritytoken (enterpriseid);
create index fklojg3xmk98ghle10liw96742m_systemid on dbo.enterprisexclassificationsecuritytoken (systemid);
create index fkp7wseksjtv3n3vok7qm68dmdb_originalsourcesystemid on dbo.enterprisexclassificationsecuritytoken (originalsourcesystemid);
create index fkr6xvbr7r4rmk73etcyyx887ao_enterprisexclassificationid on dbo.enterprisexclassificationsecuritytoken (enterprisexclassificationid);

CREATE INDEX idx_enterprise_effectivefromdate ON dbo.enterprise (effectivefromdate);
CREATE INDEX idx_enterprise_effectivetodate ON dbo.enterprise (effectivetodate);
CREATE INDEX idx_enterprise_warehousecreatedtimestamp ON dbo.enterprise (warehousecreatedtimestamp);
CREATE INDEX idx_enterprise_warehouselastupdatedtimestamp ON dbo.enterprise (warehouselastupdatedtimestamp);

CREATE INDEX idx_enterprisesecuritytoken_effectivefromdate ON dbo.enterprisesecuritytoken (effectivefromdate);
CREATE INDEX idx_enterprisesecuritytoken_effectivetodate ON dbo.enterprisesecuritytoken (effectivetodate);
CREATE INDEX idx_enterprisesecuritytoken_warehousecreatedtimestamp ON dbo.enterprisesecuritytoken (warehousecreatedtimestamp);
CREATE INDEX idx_enterprisesecuritytoken_warehouselastupdatedtimestamp ON dbo.enterprisesecuritytoken (warehouselastupdatedtimestamp);
CREATE INDEX idx_enterprisexclassification_effectivefromdate ON dbo.enterprisexclassification (effectivefromdate);
CREATE INDEX idx_enterprisexclassification_effectivetodate ON dbo.enterprisexclassification (effectivetodate);
CREATE INDEX idx_enterprisexclassification_warehousecreatedtimestamp ON dbo.enterprisexclassification (warehousecreatedtimestamp);
CREATE INDEX idx_enterprisexclassification_warehouselastupdatedtimestamp ON dbo.enterprisexclassification (warehouselastupdatedtimestamp);

CREATE INDEX idx_enterprisexclassificationsecuritytoken_effectivefromdate ON dbo.enterprisexclassificationsecuritytoken (effectivefromdate);
CREATE INDEX idx_enterprisexclassificationsecuritytoken_effectivetodate ON dbo.enterprisexclassificationsecuritytoken (effectivetodate);
CREATE INDEX idx_enterprisexclassificationsecuritytoken_warehousecreatedtim ON dbo.enterprisexclassificationsecuritytoken (warehousecreatedtimestamp);
CREATE INDEX idx_enterprisexclassificationsecuritytoken_warehouselastupdate ON dbo.enterprisexclassificationsecuritytoken (warehouselastupdatedtimestamp);

CREATE INDEX idx_enterprisexclassification_value ON dbo.enterprisexclassification (value);

CREATE INDEX idx_enterprise_enterprisedesc ON dbo.enterprise (enterprisedesc);
CREATE INDEX idx_enterprise_enterprisename ON dbo.enterprise (enterprisename);


create index fk3604iuvyi4psepv3rdn69jf76_systemidwhcd on dbo.enterprisesecuritytoken (systemid, warehousefromdate);
create index fkcja1yq5j2ywm4mearg7gfbeg8_activeflagidwhcd on dbo.enterprisesecuritytoken (activeflagid, warehousefromdate);
create index fki2166mtej2yhf4qeegbrtwsom_securitytokenidwhcd on dbo.enterprisesecuritytoken (securitytokenid, warehousefromdate);
create index fki27vhcoq25358slocd9mn9481_originalsourcesystemidwhcd on dbo.enterprisesecuritytoken (originalsourcesystemid, warehousefromdate);
create index fkkfbukc1416acs2qhjduyaslun_enterpriseidwhcd on dbo.enterprisesecuritytoken (enterpriseid, warehousefromdate);
create index fk7dcbw0medved7abfmh1y0btj_enterpriseidwhcd on dbo.enterprisexclassification (enterpriseid, warehousefromdate);
create index fk9wfebpqldiybaphp969e7vu9q_activeflagidwhcd on dbo.enterprisexclassification (activeflagid, warehousefromdate);
create index fkd8u5qov86csi5bmvn97648jyq_originalsourcesystemidwhcd on dbo.enterprisexclassification (originalsourcesystemid, warehousefromdate);
create index fkmt4djrkvv0w1ef778yy01fone_systemidwhcd on dbo.enterprisexclassification (systemid, warehousefromdate);
create index fkpubcpyc3hm0x9l70u19lj96r0_classificationidwhcd on dbo.enterprisexclassification (classificationid, warehousefromdate);
create index fk5l36jvcvedgw6gaa8301xjr62_securitytokenidwhcd on dbo.enterprisexclassificationsecuritytoken (securitytokenid, warehousefromdate);
create index fk75tx4r7sp36qtylr8r888gbkr_activeflagidwhcd on dbo.enterprisexclassificationsecuritytoken (activeflagid, warehousefromdate);
create index fk7jfxn7qp93vt98lob9q7g5exc_enterpriseidwhcd on dbo.enterprisexclassificationsecuritytoken (enterpriseid, warehousefromdate);
create index fklojg3xmk98ghle10liw96742m_systemidwhcd on dbo.enterprisexclassificationsecuritytoken (systemid, warehousefromdate);
create index fkp7wseksjtv3n3vok7qm68dmdb_originalsourcesystemidwhcd on dbo.enterprisexclassificationsecuritytoken (originalsourcesystemid, warehousefromdate);
create index fkr6xvbr7r4rmk73etcyyx887ao_enterprisexclassificationidwhcd on dbo.enterprisexclassificationsecuritytoken (enterprisexclassificationid, warehousefromdate);

