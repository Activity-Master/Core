CREATE TABLE dbo.activeflag
(
    activeflagid                  UUID                        NOT NULL primary key,
    effectivefromdate             timestamp(6) with time zone NOT NULL,
    effectivetodate               timestamp(6) with time zone NOT NULL,
    warehousecreatedtimestamp     timestamp(6) with time zone NOT NULL,
    warehousefromdate             DATE                        NOT NULL,

    warehouselastupdatedtimestamp timestamp(6) with time zone NOT NULL,
    allowaccess                   INTEGER                     NOT NULL,
    activeflagdescription         character varying(100)      NOT NULL,
    activeflagname                character varying(100)      NOT NULL,
    enterpriseid                  UUID                        NOT NULL
);
CREATE TABLE dbo.activeflagsecuritytoken
(
    activeflagsecuritytokenid     UUID                        NOT NULL primary key,
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
    securitytokenactiveflagid     UUID                        NOT NULL
);
CREATE TABLE dbo.activeflagxclassification
(
    activeflagxclassificationid   UUID                        NOT NULL primary key,
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
CREATE TABLE dbo.activeflagxclassificationsecuritytoken
(
    activeflagxclassificationsecuritytokenid UUID                        NOT NULL primary key,
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
    activeflagxclassificationid              UUID                        NOT NULL
);

create index fkme13h3ny7lm8n86kltbw69ni1_enterpriseid on dbo.activeflag (enterpriseid);
create index fk20yjws7ewoq2cqkx3ypki36rx_systemid on dbo.activeflagsecuritytoken (systemid);
create index fk5upfxgyprbf9blrljkanx6dq1_originalsourcesystemid on dbo.activeflagsecuritytoken (originalsourcesystemid);
create index fkf1tv1gusx83k2ytmhi4g3tqbm_securitytokenid on dbo.activeflagsecuritytoken (securitytokenid);
create index fki4tkjwmtimx0nsi9nkmvjejd8_activeflagid on dbo.activeflagsecuritytoken (activeflagid);
create index fklh0yk0eo7gs9tjd368uaqa0vt_securitytokenactiveflagid on dbo.activeflagsecuritytoken (securitytokenactiveflagid);
create index fkr9uxaoa4e7qc848omo02gnv8q_enterpriseid on dbo.activeflagsecuritytoken (enterpriseid);
create index fk7p1a2n77n5ic2exm71aemudot_originalsourcesystemid on dbo.activeflagxclassification (originalsourcesystemid);
create index fkblul4g9hdfavb3grhcn5bu32h_classificationid on dbo.activeflagxclassification (classificationid);
create index fkid9su2w15uts2jocr2ix6p9n6_enterpriseid on dbo.activeflagxclassification (enterpriseid);
create index fkjcnjquu3mlf3fqxljya6hhh32_activeflagid on dbo.activeflagxclassification (activeflagid);
create index fko09sv3aidiqrfa034t31l067w_systemid on dbo.activeflagxclassification (systemid);
create index fk415n0jfxdeuva0evd9mvlooaa_securitytokenid on dbo.activeflagxclassificationsecuritytoken (securitytokenid);
create index fk6lfmv9ervio1pg5wiowy4hvve_systemid on dbo.activeflagxclassificationsecuritytoken (systemid);
create index fkahuj23jai69r76hk3u9iqos5d_enterpriseid on dbo.activeflagxclassificationsecuritytoken (enterpriseid);
create index fkjt5o1jc7herohd02v678yyba6_activeflagxclassificationid on dbo.activeflagxclassificationsecuritytoken (activeflagxclassificationid);
create index fkl8ovwqdvb6bagyf17g7gk46qy_originalsourcesystemid on dbo.activeflagxclassificationsecuritytoken (originalsourcesystemid);
create index fklmqtxiiqcje4o4vix6egjsg27_activeflagid on dbo.activeflagxclassificationsecuritytoken (activeflagid);


CREATE INDEX idx_activeflagsecuritytoken_effectivefromdate ON dbo.activeflagsecuritytoken (effectivefromdate);
CREATE INDEX idx_activeflagsecuritytoken_effectivetodate ON dbo.activeflagsecuritytoken (effectivetodate);
CREATE INDEX idx_activeflagsecuritytoken_warehousecreatedtimestamp ON dbo.activeflagsecuritytoken (warehousecreatedtimestamp);
CREATE INDEX idx_activeflagsecuritytoken_warehouselastupdatedtimestamp ON dbo.activeflagsecuritytoken (warehouselastupdatedtimestamp);

CREATE INDEX idx_activeflag_effectivefromdate ON dbo.activeflag (effectivefromdate);
CREATE INDEX idx_activeflag_effectivetodate ON dbo.activeflag (effectivetodate);
CREATE INDEX idx_activeflag_warehousecreatedtimestamp ON dbo.activeflag (warehousecreatedtimestamp);
CREATE INDEX idx_activeflag_warehouselastupdatedtimestamp ON dbo.activeflag (warehouselastupdatedtimestamp);

CREATE INDEX idx_activeflagxclassificationsecuritytoken_effectivefromdate ON dbo.activeflagxclassificationsecuritytoken (effectivefromdate);
CREATE INDEX idx_activeflagxclassificationsecuritytoken_effectivetodate ON dbo.activeflagxclassificationsecuritytoken (effectivetodate);
CREATE INDEX idx_activeflagxclassificationsecuritytoken_warehousecreatedtim ON dbo.activeflagxclassificationsecuritytoken (warehousecreatedtimestamp);
CREATE INDEX idx_activeflagxclassificationsecuritytoken_warehouselastupdate ON dbo.activeflagxclassificationsecuritytoken (warehouselastupdatedtimestamp);
CREATE INDEX idx_activeflagxclassification_effectivefromdate ON dbo.activeflagxclassification (effectivefromdate);
CREATE INDEX idx_activeflagxclassification_effectivetodate ON dbo.activeflagxclassification (effectivetodate);
CREATE INDEX idx_activeflagxclassification_warehousecreatedtimestamp ON dbo.activeflagxclassification (warehousecreatedtimestamp);
CREATE INDEX idx_activeflagxclassification_warehouselastupdatedtimestamp ON dbo.activeflagxclassification (warehouselastupdatedtimestamp);

CREATE INDEX idx_activeflagxclassification_value ON dbo.activeflagxclassification (value);
CREATE INDEX idx_activeflag_activeflagname ON dbo.activeflag (activeflagname);
create index fkme13h3ny7lm8n86kltbw69ni1_enterpriseidwhcd on dbo.activeflag (enterpriseid, warehousefromdate);
create index fk20yjws7ewoq2cqkx3ypki36rx_systemidwhcd on dbo.activeflagsecuritytoken (systemid, warehousefromdate);
create index fk5upfxgyprbf9blrljkanx6dq1_originalsourcesystemidwhcd on dbo.activeflagsecuritytoken (originalsourcesystemid, warehousefromdate);
create index fkf1tv1gusx83k2ytmhi4g3tqbm_securitytokenidwhcd on dbo.activeflagsecuritytoken (securitytokenid, warehousefromdate);
create index fki4tkjwmtimx0nsi9nkmvjejd8_activeflagidwhcd on dbo.activeflagsecuritytoken (activeflagid, warehousefromdate);
create index fklh0yk0eo7gs9tjd368uaqa0vt_securitytokenactiveflagidwhcd on dbo.activeflagsecuritytoken (securitytokenactiveflagid, warehousefromdate);
create index fkr9uxaoa4e7qc848omo02gnv8q_enterpriseidwhcd on dbo.activeflagsecuritytoken (enterpriseid, warehousefromdate);
create index fk7p1a2n77n5ic2exm71aemudot_originalsourcesystemidwhcd on dbo.activeflagxclassification (originalsourcesystemid, warehousefromdate);
create index fkblul4g9hdfavb3grhcn5bu32h_classificationidwhcd on dbo.activeflagxclassification (classificationid, warehousefromdate);
create index fkid9su2w15uts2jocr2ix6p9n6_enterpriseidwhcd on dbo.activeflagxclassification (enterpriseid, warehousefromdate);
create index fkjcnjquu3mlf3fqxljya6hhh32_activeflagidwhcd on dbo.activeflagxclassification (activeflagid, warehousefromdate);
create index fko09sv3aidiqrfa034t31l067w_systemidwhcd on dbo.activeflagxclassification (systemid, warehousefromdate);
create index fk415n0jfxdeuva0evd9mvlooaa_securitytokenidwhcd on dbo.activeflagxclassificationsecuritytoken (securitytokenid, warehousefromdate);
create index fk6lfmv9ervio1pg5wiowy4hvve_systemidwhcd on dbo.activeflagxclassificationsecuritytoken (systemid, warehousefromdate);
create index fkahuj23jai69r76hk3u9iqos5d_enterpriseidwhcd on dbo.activeflagxclassificationsecuritytoken (enterpriseid, warehousefromdate);
create index fkjt5o1jc7herohd02v678yyba6_activeflagxclassificationidwhcd on dbo.activeflagxclassificationsecuritytoken (activeflagxclassificationid, warehousefromdate);
create index fkl8ovwqdvb6bagyf17g7gk46qy_originalsourcesystemidwhcd on dbo.activeflagxclassificationsecuritytoken (originalsourcesystemid, warehousefromdate);
create index fklmqtxiiqcje4o4vix6egjsg27_activeflagidwhcd on dbo.activeflagxclassificationsecuritytoken (activeflagid, warehousefromdate);





