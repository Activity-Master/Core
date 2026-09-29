CREATE TABLE dbo.systems
(
    systemid                      UUID                        NOT NULL primary key,
    effectivefromdate             timestamp(6) with time zone NOT NULL,
    effectivetodate               timestamp(6) with time zone NOT NULL,
    warehousecreatedtimestamp     timestamp(6) with time zone NOT NULL,
    warehousefromdate             DATE                        NOT NULL,

    warehouselastupdatedtimestamp timestamp(6) with time zone NOT NULL,
    systemdesc                    character varying(250)      NOT NULL,
    systemname                    character varying(150)      NOT NULL,
    systemhistoryname             character varying(250)      NOT NULL,
    activeflagid                  UUID                        NOT NULL,
    enterpriseid                  UUID                        NOT NULL
);

CREATE TABLE dbo.systemssecuritytoken
(
    systemssecuritytokenid        UUID                        NOT NULL primary key,
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

CREATE TABLE dbo.systemxclassification
(
    systemxclassificationid       UUID                        NOT NULL primary key,
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

CREATE TABLE dbo.systemxclassificationsecuritytoken
(
    systemxclassificationsecuritytokenid UUID                        NOT NULL primary key,
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
    systemxclassificationid              UUID                        NOT NULL
);

create index fkgrtajtg4kade8b7dvqjynt2c6_enterpriseid on dbo.systems (enterpriseid);
create index fkk9wup45yifecrf4o0fb5lyuy6_activeflagid on dbo.systems (activeflagid);
create index fk569rtrkku5kg7sponne0xwgkf_enterpriseid on dbo.systemssecuritytoken (enterpriseid);
create index fk78ntmjytf0l9egbfp8w5quysa_activeflagid on dbo.systemssecuritytoken (activeflagid);
create index fk7vvidgjeuilwrvmphf0pwrdqm_systemid on dbo.systemssecuritytoken (systemid);
create index fkgq2e9xc2wsw93xl4p5yhaysx_originalsourcesystemid on dbo.systemssecuritytoken (originalsourcesystemid);
create index fks9aoyq05lmsl0jpqnja4gy5g8_securitytokenid on dbo.systemssecuritytoken (securitytokenid);
create index fk6ejo5y1g32jdsi6nh5wufvdg9_enterpriseid on dbo.systemxclassification (enterpriseid);
create index fkf8j3lg5l7fp5qthp3s7v5cpsy_activeflagid on dbo.systemxclassification (activeflagid);
create index fko0c1ywx0xwsytkccp23xtolxd_originalsourcesystemid on dbo.systemxclassification (originalsourcesystemid);
create index fkq69xnhoy37i0vgl4n0ye0deql_systemid on dbo.systemxclassification (systemid);
create index fksok0wnd9e14j1shyqkn1fkpf3_classificationid on dbo.systemxclassification (classificationid);
create index fkammfvlpf58c86bmebac2l5fhy_enterpriseid on dbo.systemxclassificationsecuritytoken (enterpriseid);
create index fkaxmrulkvngggqqt0yjk39qjx0_securitytokenid on dbo.systemxclassificationsecuritytoken (securitytokenid);
create index fkb1iyf0i1s8q5tmc9wrwdfi52i_systemid on dbo.systemxclassificationsecuritytoken (systemid);
create index fkhpnepymhd6det14tmy3vsx6g8_systemxclassificationid on dbo.systemxclassificationsecuritytoken (systemxclassificationid);
create index fkj9spyu6coc19mcroktrbdym0i_activeflagid on dbo.systemxclassificationsecuritytoken (activeflagid);
create index fktm7py81728d8oor46wotp8y0f_originalsourcesystemid on dbo.systemxclassificationsecuritytoken (originalsourcesystemid);

CREATE INDEX idx_systemxclassificationsecuritytoken_effectivefromdate ON dbo.systemxclassificationsecuritytoken (effectivefromdate);
CREATE INDEX idx_systemxclassificationsecuritytoken_effectivetodate ON dbo.systemxclassificationsecuritytoken (effectivetodate);
CREATE INDEX idx_systemxclassificationsecuritytoken_warehousecreatedtimesta ON dbo.systemxclassificationsecuritytoken (warehousecreatedtimestamp);
CREATE INDEX idx_systemxclassificationsecuritytoken_warehouselastupdatedtim ON dbo.systemxclassificationsecuritytoken (warehouselastupdatedtimestamp);


CREATE INDEX idx_systems_effectivefromdate ON dbo.systems (effectivefromdate);
CREATE INDEX idx_systems_effectivetodate ON dbo.systems (effectivetodate);
CREATE INDEX idx_systems_warehousecreatedtimestamp ON dbo.systems (warehousecreatedtimestamp);
CREATE INDEX idx_systems_warehouselastupdatedtimestamp ON dbo.systems (warehouselastupdatedtimestamp);
CREATE INDEX idx_systemssecuritytoken_effectivefromdate ON dbo.systemssecuritytoken (effectivefromdate);
CREATE INDEX idx_systemssecuritytoken_effectivetodate ON dbo.systemssecuritytoken (effectivetodate);
CREATE INDEX idx_systemssecuritytoken_warehousecreatedtimestamp ON dbo.systemssecuritytoken (warehousecreatedtimestamp);
CREATE INDEX idx_systemssecuritytoken_warehouselastupdatedtimestamp ON dbo.systemssecuritytoken (warehouselastupdatedtimestamp);

CREATE INDEX idx_systemxclassification_effectivefromdate ON dbo.systemxclassification (effectivefromdate);
CREATE INDEX idx_systemxclassification_effectivetodate ON dbo.systemxclassification (effectivetodate);
CREATE INDEX idx_systemxclassification_warehousecreatedtimestamp ON dbo.systemxclassification (warehousecreatedtimestamp);
CREATE INDEX idx_systemxclassification_warehouselastupdatedtimestamp ON dbo.systemxclassification (warehouselastupdatedtimestamp);

CREATE INDEX idx_systemxclassification_value ON dbo.systemxclassification (value);
CREATE INDEX idx_systems_systemdesc ON dbo.systems (systemdesc);
CREATE INDEX idx_systems_systemname ON dbo.systems (systemname);
CREATE INDEX idx_systems_systemhistoryname ON dbo.systems (systemhistoryname);

create index fkgrtajtg4kade8b7dvqjynt2c6_enterpriseidwhcd on dbo.systems (enterpriseid, warehousefromdate);
create index fkk9wup45yifecrf4o0fb5lyuy6_activeflagidwhcd on dbo.systems (activeflagid, warehousefromdate);
create index fk569rtrkku5kg7sponne0xwgkf_enterpriseidwhcd on dbo.systemssecuritytoken (enterpriseid, warehousefromdate);
create index fk78ntmjytf0l9egbfp8w5quysa_activeflagidwhcd on dbo.systemssecuritytoken (activeflagid, warehousefromdate);
create index fk7vvidgjeuilwrvmphf0pwrdqm_systemidwhcd on dbo.systemssecuritytoken (systemid, warehousefromdate);
create index fkgq2e9xc2wsw93xl4p5yhaysx_originalsourcesystemidwhcd on dbo.systemssecuritytoken (originalsourcesystemid, warehousefromdate);
create index fks9aoyq05lmsl0jpqnja4gy5g8_securitytokenidwhcd on dbo.systemssecuritytoken (securitytokenid, warehousefromdate);
create index fk6ejo5y1g32jdsi6nh5wufvdg9_enterpriseidwhcd on dbo.systemxclassification (enterpriseid, warehousefromdate);
create index fkf8j3lg5l7fp5qthp3s7v5cpsy_activeflagidwhcd on dbo.systemxclassification (activeflagid, warehousefromdate);
create index fko0c1ywx0xwsytkccp23xtolxd_originalsourcesystemidwhcd on dbo.systemxclassification (originalsourcesystemid, warehousefromdate);
create index fkq69xnhoy37i0vgl4n0ye0deql_systemidwhcd on dbo.systemxclassification (systemid, warehousefromdate);
create index fksok0wnd9e14j1shyqkn1fkpf3_classificationidwhcd on dbo.systemxclassification (classificationid, warehousefromdate);
create index fkammfvlpf58c86bmebac2l5fhy_enterpriseidwhcd on dbo.systemxclassificationsecuritytoken (enterpriseid, warehousefromdate);
create index fkaxmrulkvngggqqt0yjk39qjx0_securitytokenidwhcd on dbo.systemxclassificationsecuritytoken (securitytokenid, warehousefromdate);
create index fkb1iyf0i1s8q5tmc9wrwdfi52i_systemidwhcd on dbo.systemxclassificationsecuritytoken (systemid, warehousefromdate);
create index fkhpnepymhd6det14tmy3vsx6g8_systemxclassificationidwhcd on dbo.systemxclassificationsecuritytoken (systemxclassificationid, warehousefromdate);
create index fkj9spyu6coc19mcroktrbdym0i_activeflagidwhcd on dbo.systemxclassificationsecuritytoken (activeflagid, warehousefromdate);
create index fktm7py81728d8oor46wotp8y0f_originalsourcesystemidwhcd on dbo.systemxclassificationsecuritytoken (originalsourcesystemid, warehousefromdate);




