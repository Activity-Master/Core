CREATE SCHEMA product;

CREATE TABLE product.product
(
    productid                     UUID                        NOT NULL primary key,
    effectivefromdate             timestamp(6) with time zone NOT NULL,
    effectivetodate               timestamp(6) with time zone NOT NULL,
    warehousecreatedtimestamp     timestamp(6) with time zone NOT NULL,
    warehousefromdate             DATE                        NOT NULL,

    warehouselastupdatedtimestamp timestamp(6) with time zone NOT NULL,
    originalsourcesystemuniqueid  UUID                        NOT NULL,
    productdesc                   character varying(250)      NOT NULL,
    productname                   character varying(150)      NOT NULL,
    productcode                   character varying(50)       NOT NULL,
    activeflagid                  UUID                        NOT NULL,
    enterpriseid                  UUID                        NOT NULL,
    systemid                      UUID                        NOT NULL,
    originalsourcesystemid        UUID                        NOT NULL
);
CREATE TABLE product.productsecuritytoken
(
    productsecuritytokenid        UUID                        NOT NULL primary key,
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
    productid                     UUID                        NOT NULL
);
CREATE TABLE product.producttype
(
    producttypeid                 UUID                        NOT NULL primary key,
    effectivefromdate             timestamp(6) with time zone NOT NULL,
    effectivetodate               timestamp(6) with time zone NOT NULL,
    warehousecreatedtimestamp     timestamp(6) with time zone NOT NULL,
    warehousefromdate             DATE                        NOT NULL,

    warehouselastupdatedtimestamp timestamp(6) with time zone NOT NULL,
    originalsourcesystemuniqueid  UUID                        NOT NULL,
    producttypedesc               character varying(200)      NOT NULL,
    producttypename               character varying(200)      NOT NULL,
    activeflagid                  UUID                        NOT NULL,
    enterpriseid                  UUID                        NOT NULL,
    systemid                      UUID                        NOT NULL,
    originalsourcesystemid        UUID                        NOT NULL
);
CREATE TABLE product.producttypessecuritytoken
(
    producttypessecuritytokenid   UUID                        NOT NULL primary key,
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
    producttypesid                UUID                        NOT NULL
);
CREATE TABLE product.producttypexclassification
(
    producttypexclassificationid  UUID                        NOT NULL primary key,
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
    producttypeid                 UUID                        NOT NULL
);
CREATE TABLE product.producttypexclassificationsecuritytoken
(
    producttypexclassificationsecuritytokenid UUID                        NOT NULL primary key,
    effectivefromdate                         timestamp(6) with time zone NOT NULL,
    effectivetodate                           timestamp(6) with time zone NOT NULL,
    warehousecreatedtimestamp                 timestamp(6) with time zone NOT NULL,
    warehousefromdate                         DATE                        NOT NULL,

    warehouselastupdatedtimestamp             timestamp(6) with time zone NOT NULL,
    createallowed                             INTEGER                     NOT NULL,
    deleteallowed                             INTEGER                     NOT NULL,
    originalsourcesystemuniqueid              UUID                        NOT NULL,
    readallowed                               INTEGER                     NOT NULL,
    updateallowed                             INTEGER                     NOT NULL,
    activeflagid                              UUID                        NOT NULL,
    enterpriseid                              UUID                        NOT NULL,
    originalsourcesystemid                    UUID                        NOT NULL,
    securitytokenid                           UUID                        NOT NULL,
    systemid                                  UUID                        NOT NULL,
    producttypexclassificationid              UUID                        NOT NULL
);
CREATE TABLE product.productxclassification
(
    productxclassificationid      UUID                        NOT NULL primary key,
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
    productid                     UUID                        NOT NULL
);
CREATE TABLE product.productxclassificationsecuritytoken
(
    productxclassificationsecuritytokenid UUID                        NOT NULL primary key,
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
    productxclassificationid              UUID                        NOT NULL
);
CREATE TABLE product.productxproduct
(
    productxproductid             UUID                        NOT NULL primary key,
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
    childproductid                UUID                        NOT NULL,
    parentproductid               UUID                        NOT NULL
);
CREATE TABLE product.productxproductsecuritytoken
(
    productxproductsecuritytokenid UUID                        NOT NULL primary key,
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
    productxproductid              UUID                        NOT NULL
);
CREATE TABLE product.productxproducttype
(
    productxproducttypeid         UUID                        NOT NULL primary key,
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
    producttypeid                 UUID                        NOT NULL
);
CREATE TABLE product.productxproducttypesecuritytoken
(
    productxproducttypesecuritytokenid UUID                        NOT NULL primary key,
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
    productxproducttypeid              UUID                        NOT NULL
);
CREATE TABLE product.productxresourceitem
(
    productxresourceitemid        UUID                        NOT NULL primary key,
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
    resourceitemid                UUID                        NOT NULL
);
CREATE TABLE product.productxresourceitemsecuritytoken
(
    productxresourceitemsecuritytokenid UUID                        NOT NULL primary key,
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
    productxresourceitemid              UUID                        NOT NULL
);

create index fk1uilqm7vj2gtc2d8x638robxd_activeflagid on product.product (activeflagid);
create index fk5igcn0xk318avw7alibquq364_systemid on product.product (systemid);
create index fkfvbgcjo7xxxjqbqwy6rf9okxe_enterpriseid on product.product (enterpriseid);
create index fkhd5i44wqur3jhd2e66aj1e38_originalsourcesystemid on product.product (originalsourcesystemid);
create index fk6nu64a57s4xf2f259fg2ds2vx_originalsourcesystemid on product.productsecuritytoken (originalsourcesystemid);
create index fkhwbgx5m4cqg3drq0kbl2ln218_systemid on product.productsecuritytoken (systemid);
create index fkix4kyq2jvwpfkwr07hb5418wk_enterpriseid on product.productsecuritytoken (enterpriseid);
create index fklfavoymys95w6sr5vg7fc2lpd_productid on product.productsecuritytoken (productid);
create index fkobdmay5wpjxkq8fmnh5jb4wax_securitytokenid on product.productsecuritytoken (securitytokenid);
create index fkpp00icvq84wm807qt11c7di0o_activeflagid on product.productsecuritytoken (activeflagid);
create index fk6qo5a8hhlrogyas0wxpefi252_activeflagid on product.producttype (activeflagid);
create index fk7jpihblwon21gvxequinglp4u_enterpriseid on product.producttype (enterpriseid);
create index fkaji1ysqjjctugqxgktlyq4jb7_systemid on product.producttype (systemid);
create index fkg36gb46ujtrxwet1ac4f2k43b_originalsourcesystemid on product.producttype (originalsourcesystemid);
create index fk124dnn13mvx64w9sscl56x0e9_systemid on product.producttypessecuritytoken (systemid);
create index fka4hffxoba5up3ewpbck3bydrl_securitytokenid on product.producttypessecuritytoken (securitytokenid);
create index fkbw6gf8n6ce2ob0qbvc0hdr876_originalsourcesystemid on product.producttypessecuritytoken (originalsourcesystemid);
create index fkhqgmcxurg87rgu1ekgl6xqb4_activeflagid on product.producttypessecuritytoken (activeflagid);
create index fkkpofjm6j3idsftpk1nilvd5am_producttypesid on product.producttypessecuritytoken (producttypesid);
create index fkpevw33iuc65w72j6c9j1scwi_enterpriseid on product.producttypessecuritytoken (enterpriseid);
create index fkd0fwhreudgdgcdipqbmxnrpw0_classificationid on product.producttypexclassification (classificationid);
create index fkefusbrg6b297xo4uaifrnf7of_systemid on product.producttypexclassification (systemid);
create index fklpladubwvqlvl4m7vv5k7i04a_activeflagid on product.producttypexclassification (activeflagid);
create index fkm04vi4rjib6p4wstxivhv5uwb_originalsourcesystemid on product.producttypexclassification (originalsourcesystemid);
create index fkon0srs81eq9qmdm4clgh7lf1r_producttypeid on product.producttypexclassification (producttypeid);
create index fktby5ywfw3h2p0kwing53rbw83_enterpriseid on product.producttypexclassification (enterpriseid);
create index fkhp82lysbkher7sh9nmdc8j5mt_activeflagid on product.producttypexclassificationsecuritytoken (activeflagid);
create index fki005cybjhowcm4dc6a4rur35f_originalsourcesystemid on product.producttypexclassificationsecuritytoken (originalsourcesystemid);
create index fkkpdeivhwgmup6yq5or7mkhh3i_enterpriseid on product.producttypexclassificationsecuritytoken (enterpriseid);
create index fkm2t70qvf88y1hc6xxtp5yvedj_systemid on product.producttypexclassificationsecuritytoken (systemid);
create index fkmtyho44wd7jen9pckqv90td0j_producttypexclassificationid on product.producttypexclassificationsecuritytoken (producttypexclassificationid);
create index fkn22khncgjxl940nga25siqctl_securitytokenid on product.producttypexclassificationsecuritytoken (securitytokenid);
create index fk170jhdqdisurhraj01enjp07f_originalsourcesystemid on product.productxclassification (originalsourcesystemid);
create index fk4rxn4f51nq7nachqwenoijt19_systemid on product.productxclassification (systemid);
create index fk6if9d2r0jph42fstek03stxde_classificationid on product.productxclassification (classificationid);
create index fk8b81utqms0112fwiuv724pss1_activeflagid on product.productxclassification (activeflagid);
create index fk8bl09vfxm9qhghywxbfk36qra_enterpriseid on product.productxclassification (enterpriseid);
create index fktjv0q3owa2dvherxn8lwy4umj_productid on product.productxclassification (productid);
create index fk25k9qm7701i83jtdlw9pma3it_enterpriseid on product.productxclassificationsecuritytoken (enterpriseid);
create index fk2a1344eckqbgsu5qtw1hqtmii_productxclassificationid on product.productxclassificationsecuritytoken (productxclassificationid);
create index fkalo1aiwrqa3qvcoyta4wmron0_originalsourcesystemid on product.productxclassificationsecuritytoken (originalsourcesystemid);
create index fkncist9vnbkli1wfjeyqeqpbhi_systemid on product.productxclassificationsecuritytoken (systemid);
create index fkphp040h86si2e7sthuts7u84a_activeflagid on product.productxclassificationsecuritytoken (activeflagid);
create index fksqaqrh3mqcptl4uk5ucmkn34h_securitytokenid on product.productxclassificationsecuritytoken (securitytokenid);
create index fk6by61ucqfknganlsl2nrl75qi_childproductid on product.productxproduct (childproductid);
create index fk77xliloqs4xw4ci0ajbplqpww_systemid on product.productxproduct (systemid);
create index fkdcpq7m2melgfwv5auqyagl72h_activeflagid on product.productxproduct (activeflagid);
create index fkjtuo8wff4bgrag27r7b90e0cr_classificationid on product.productxproduct (classificationid);
create index fkkh1fb4vobxxm3gqr902b2sy9q_enterpriseid on product.productxproduct (enterpriseid);
create index fklwu619drh230ni7r1dow2jdo1_parentproductid on product.productxproduct (parentproductid);
create index fkpjcmb3bm0uykh5ycoxw327a6y_originalsourcesystemid on product.productxproduct (originalsourcesystemid);
create index fk1ig8t64jsxnh0bpi3ql9st9ox_enterpriseid on product.productxproductsecuritytoken (enterpriseid);
create index fk87msnwio1ifds9frgcawk703q_securitytokenid on product.productxproductsecuritytoken (securitytokenid);
create index fkfov8qx41nggbg75jj8ely4wrp_activeflagid on product.productxproductsecuritytoken (activeflagid);
create index fkk3c9qgnvemsrtifm1qkfwwpai_originalsourcesystemid on product.productxproductsecuritytoken (originalsourcesystemid);
create index fkm2agj4yop5p5pdbbqalr9ys6d_systemid on product.productxproductsecuritytoken (systemid);
create index fksmhfy43ik4seggjpeh0hd7ohe_productxproductid on product.productxproductsecuritytoken (productxproductid);
create index fk1v5k0dbqs8u10y8qplq99qoiq_productid on product.productxproducttype (productid);
create index fk2n7njekvkkktqita4lo8o2rop_systemid on product.productxproducttype (systemid);
create index fk64ad5aqtu7abdea2c46wmwsyp_activeflagid on product.productxproducttype (activeflagid);
create index fk7qloe9c2vsgtb7r52s8rwam77_producttypeid on product.productxproducttype (producttypeid);
create index fki3kgl08d83biiovc4ipk0i82k_classificationid on product.productxproducttype (classificationid);
create index fkqx5p60ei4jsps9ajg56g6y75n_originalsourcesystemid on product.productxproducttype (originalsourcesystemid);
create index fktn5yb54e9i7r3hwtr1bojxh6e_enterpriseid on product.productxproducttype (enterpriseid);
create index fk24ny4320upy05vtrp0bl40edh_securitytokenid on product.productxproducttypesecuritytoken (securitytokenid);
create index fk3h3ux71dsf7opesl0p4fq9y2n_productxproducttypeid on product.productxproducttypesecuritytoken (productxproducttypeid);
create index fk5r6sj8u01cbw7ndxn6nkbqbgs_activeflagid on product.productxproducttypesecuritytoken (activeflagid);
create index fke8k4xro3jq77djofuxtcqjnb7_originalsourcesystemid on product.productxproducttypesecuritytoken (originalsourcesystemid);
create index fknnkyxacvaltro761yay2h0a0t_systemid on product.productxproducttypesecuritytoken (systemid);
create index fknr2deq2638s77m37vyx16g6i0_enterpriseid on product.productxproducttypesecuritytoken (enterpriseid);
create index fk2kfndi7vipw90dmibvhml8luf_classificationid on product.productxresourceitem (classificationid);
create index fk3c9bk1tu9tr4ajj57j50lq61s_enterpriseid on product.productxresourceitem (enterpriseid);
create index fk4ircw0cakrrbxh9eb947do1r1_activeflagid on product.productxresourceitem (activeflagid);
create index fkdlspqxuytfux61envksg2oblc_systemid on product.productxresourceitem (systemid);
create index fkjcm094m4k21700pj371gyg28_productid on product.productxresourceitem (productid);
create index fkk4j0vxf6l51756si5xlelvhyx_resourceitemid on product.productxresourceitem (resourceitemid);
create index fkq078arvokvft7rp6g01drm5ga_originalsourcesystemid on product.productxresourceitem (originalsourcesystemid);
create index fk2n140poncbaodw93p66dr19wm_enterpriseid on product.productxresourceitemsecuritytoken (enterpriseid);
create index fk4pq3n38ujll0fn7l4mw2f0uj2_productxresourceitemid on product.productxresourceitemsecuritytoken (productxresourceitemid);
create index fk8ljhn730t04d2yrudqin3b4rx_originalsourcesystemid on product.productxresourceitemsecuritytoken (originalsourcesystemid);
create index fk9rwmunm7mftbfjjefdk9i0ryh_activeflagid on product.productxresourceitemsecuritytoken (activeflagid);
create index fkhgl7dki8ue6gmivh1ulwel4j1_systemid on product.productxresourceitemsecuritytoken (systemid);
create index fklwlxp6u9a4k42nl1xqst9y0y_securitytokenid on product.productxresourceitemsecuritytoken (securitytokenid);

CREATE INDEX idx_producttype_effectivefromdate ON product.producttype (effectivefromdate);
CREATE INDEX idx_producttype_effectivetodate ON product.producttype (effectivetodate);
CREATE INDEX idx_producttype_warehousecreatedtimestamp ON product.producttype (warehousecreatedtimestamp);
CREATE INDEX idx_producttype_warehouselastupdatedtimestamp ON product.producttype (warehouselastupdatedtimestamp);
CREATE INDEX idx_productsecuritytoken_effectivefromdate ON product.productsecuritytoken (effectivefromdate);
CREATE INDEX idx_productsecuritytoken_effectivetodate ON product.productsecuritytoken (effectivetodate);
CREATE INDEX idx_productsecuritytoken_warehousecreatedtimestamp ON product.productsecuritytoken (warehousecreatedtimestamp);
CREATE INDEX idx_productsecuritytoken_warehouselastupdatedtimestamp ON product.productsecuritytoken (warehouselastupdatedtimestamp);
CREATE INDEX idx_producttypexclassificationsecuritytoken_effectivefromdate ON product.producttypexclassificationsecuritytoken (effectivefromdate);
CREATE INDEX idx_producttypexclassificationsecuritytoken_effectivetodate ON product.producttypexclassificationsecuritytoken (effectivetodate);
CREATE INDEX idx_producttypexclassificationsecuritytoken_warehousecreatedti ON product.producttypexclassificationsecuritytoken (warehousecreatedtimestamp);
CREATE INDEX idx_producttypexclassificationsecuritytoken_warehouselastupdat ON product.producttypexclassificationsecuritytoken (warehouselastupdatedtimestamp);
CREATE INDEX idx_productxproductsecuritytoken_effectivefromdate ON product.productxproductsecuritytoken (effectivefromdate);
CREATE INDEX idx_productxproductsecuritytoken_effectivetodate ON product.productxproductsecuritytoken (effectivetodate);
CREATE INDEX idx_productxproductsecuritytoken_warehousecreatedtimestamp ON product.productxproductsecuritytoken (warehousecreatedtimestamp);
CREATE INDEX idx_productxproductsecuritytoken_warehouselastupdatedtimestamp ON product.productxproductsecuritytoken (warehouselastupdatedtimestamp);
CREATE INDEX idx_producttypexclassification_effectivefromdate ON product.producttypexclassification (effectivefromdate);
CREATE INDEX idx_producttypexclassification_effectivetodate ON product.producttypexclassification (effectivetodate);
CREATE INDEX idx_producttypexclassification_warehousecreatedtimestamp ON product.producttypexclassification (warehousecreatedtimestamp);
CREATE INDEX idx_producttypexclassification_warehouselastupdatedtimestamp ON product.producttypexclassification (warehouselastupdatedtimestamp);
CREATE INDEX idx_productxproducttypesecuritytoken_effectivefromdate ON product.productxproducttypesecuritytoken (effectivefromdate);
CREATE INDEX idx_productxproducttypesecuritytoken_effectivetodate ON product.productxproducttypesecuritytoken (effectivetodate);
CREATE INDEX idx_productxproducttypesecuritytoken_warehousecreatedtimestamp ON product.productxproducttypesecuritytoken (warehousecreatedtimestamp);
CREATE INDEX idx_productxproducttypesecuritytoken_warehouselastupdatedtimes ON product.productxproducttypesecuritytoken (warehouselastupdatedtimestamp);
CREATE INDEX idx_productxproduct_effectivefromdate ON product.productxproduct (effectivefromdate);
CREATE INDEX idx_productxproduct_effectivetodate ON product.productxproduct (effectivetodate);
CREATE INDEX idx_productxproduct_warehousecreatedtimestamp ON product.productxproduct (warehousecreatedtimestamp);
CREATE INDEX idx_productxproduct_warehouselastupdatedtimestamp ON product.productxproduct (warehouselastupdatedtimestamp);
CREATE INDEX idx_producttypessecuritytoken_effectivefromdate ON product.producttypessecuritytoken (effectivefromdate);
CREATE INDEX idx_producttypessecuritytoken_effectivetodate ON product.producttypessecuritytoken (effectivetodate);
CREATE INDEX idx_producttypessecuritytoken_warehousecreatedtimestamp ON product.producttypessecuritytoken (warehousecreatedtimestamp);
CREATE INDEX idx_producttypessecuritytoken_warehouselastupdatedtimestamp ON product.producttypessecuritytoken (warehouselastupdatedtimestamp);
CREATE INDEX idx_productxclassification_effectivefromdate ON product.productxclassification (effectivefromdate);
CREATE INDEX idx_productxclassification_effectivetodate ON product.productxclassification (effectivetodate);
CREATE INDEX idx_productxclassification_warehousecreatedtimestamp ON product.productxclassification (warehousecreatedtimestamp);
CREATE INDEX idx_productxclassification_warehouselastupdatedtimestamp ON product.productxclassification (warehouselastupdatedtimestamp);
CREATE INDEX idx_productxproducttype_effectivefromdate ON product.productxproducttype (effectivefromdate);
CREATE INDEX idx_productxproducttype_effectivetodate ON product.productxproducttype (effectivetodate);
CREATE INDEX idx_productxproducttype_warehousecreatedtimestamp ON product.productxproducttype (warehousecreatedtimestamp);
CREATE INDEX idx_productxproducttype_warehouselastupdatedtimestamp ON product.productxproducttype (warehouselastupdatedtimestamp);
CREATE INDEX idx_productxclassificationsecuritytoken_effectivefromdate ON product.productxclassificationsecuritytoken (effectivefromdate);
CREATE INDEX idx_productxclassificationsecuritytoken_effectivetodate ON product.productxclassificationsecuritytoken (effectivetodate);
CREATE INDEX idx_productxclassificationsecuritytoken_warehousecreatedtimest ON product.productxclassificationsecuritytoken (warehousecreatedtimestamp);
CREATE INDEX idx_productxclassificationsecuritytoken_warehouselastupdatedti ON product.productxclassificationsecuritytoken (warehouselastupdatedtimestamp);
CREATE INDEX idx_productxresourceitem_effectivefromdate ON product.productxresourceitem (effectivefromdate);
CREATE INDEX idx_productxresourceitem_effectivetodate ON product.productxresourceitem (effectivetodate);
CREATE INDEX idx_productxresourceitem_warehousecreatedtimestamp ON product.productxresourceitem (warehousecreatedtimestamp);
CREATE INDEX idx_productxresourceitem_warehouselastupdatedtimestamp ON product.productxresourceitem (warehouselastupdatedtimestamp);
CREATE INDEX idx_product_effectivefromdate ON product.product (effectivefromdate);
CREATE INDEX idx_product_effectivetodate ON product.product (effectivetodate);
CREATE INDEX idx_product_warehousecreatedtimestamp ON product.product (warehousecreatedtimestamp);
CREATE INDEX idx_product_warehouselastupdatedtimestamp ON product.product (warehouselastupdatedtimestamp);
CREATE INDEX idx_productxresourceitemsecuritytoken_effectivefromdate ON product.productxresourceitemsecuritytoken (effectivefromdate);
CREATE INDEX idx_productxresourceitemsecuritytoken_effectivetodate ON product.productxresourceitemsecuritytoken (effectivetodate);
CREATE INDEX idx_productxresourceitemsecuritytoken_warehousecreatedtimestam ON product.productxresourceitemsecuritytoken (warehousecreatedtimestamp);
CREATE INDEX idx_productxresourceitemsecuritytoken_warehouselastupdatedtime ON product.productxresourceitemsecuritytoken (warehouselastupdatedtimestamp);

CREATE INDEX idx_producttypexclassification_value ON product.producttypexclassification (value);
CREATE INDEX idx_productxproduct_value ON product.productxproduct (value);
CREATE INDEX idx_productxclassification_value ON product.productxclassification (value);
CREATE INDEX idx_productxproducttype_value ON product.productxproducttype (value);
CREATE INDEX idx_productxresourceitem_value ON product.productxresourceitem (value);

CREATE INDEX idx_producttype_producttypedesc ON product.producttype (producttypedesc);
CREATE INDEX idx_producttype_producttypename ON product.producttype (producttypename);
CREATE INDEX idx_product_productdesc ON product.product (productdesc);
CREATE INDEX idx_product_productname ON product.product (productname);
create index fk1uilqm7vj2gtc2d8x638robxd_activeflagidwhcd on product.product (activeflagid, warehousefromdate);
create index fk5igcn0xk318avw7alibquq364_systemidwhcd on product.product (systemid, warehousefromdate);
create index fkfvbgcjo7xxxjqbqwy6rf9okxe_enterpriseidwhcd on product.product (enterpriseid, warehousefromdate);
create index fkhd5i44wqur3jhd2e66aj1e38_originalsourcesystemidwhcd on product.product (originalsourcesystemid, warehousefromdate);
create index fk6nu64a57s4xf2f259fg2ds2vx_originalsourcesystemidwhcd on product.productsecuritytoken (originalsourcesystemid, warehousefromdate);
create index fkhwbgx5m4cqg3drq0kbl2ln218_systemidwhcd on product.productsecuritytoken (systemid, warehousefromdate);
create index fkix4kyq2jvwpfkwr07hb5418wk_enterpriseidwhcd on product.productsecuritytoken (enterpriseid, warehousefromdate);
create index fklfavoymys95w6sr5vg7fc2lpd_productidwhcd on product.productsecuritytoken (productid, warehousefromdate);
create index fkobdmay5wpjxkq8fmnh5jb4wax_securitytokenidwhcd on product.productsecuritytoken (securitytokenid, warehousefromdate);
create index fkpp00icvq84wm807qt11c7di0o_activeflagidwhcd on product.productsecuritytoken (activeflagid, warehousefromdate);
create index fk6qo5a8hhlrogyas0wxpefi252_activeflagidwhcd on product.producttype (activeflagid, warehousefromdate);
create index fk7jpihblwon21gvxequinglp4u_enterpriseidwhcd on product.producttype (enterpriseid, warehousefromdate);
create index fkaji1ysqjjctugqxgktlyq4jb7_systemidwhcd on product.producttype (systemid, warehousefromdate);
create index fkg36gb46ujtrxwet1ac4f2k43b_originalsourcesystemidwhcd on product.producttype (originalsourcesystemid, warehousefromdate);
create index fk124dnn13mvx64w9sscl56x0e9_systemidwhcd on product.producttypessecuritytoken (systemid, warehousefromdate);
create index fka4hffxoba5up3ewpbck3bydrl_securitytokenidwhcd on product.producttypessecuritytoken (securitytokenid, warehousefromdate);
create index fkbw6gf8n6ce2ob0qbvc0hdr876_originalsourcesystemidwhcd on product.producttypessecuritytoken (originalsourcesystemid, warehousefromdate);
create index fkhqgmcxurg87rgu1ekgl6xqb4_activeflagidwhcd on product.producttypessecuritytoken (activeflagid, warehousefromdate);
create index fkkpofjm6j3idsftpk1nilvd5am_producttypesidwhcd on product.producttypessecuritytoken (producttypesid, warehousefromdate);
create index fkpevw33iuc65w72j6c9j1scwi_enterpriseidwhcd on product.producttypessecuritytoken (enterpriseid, warehousefromdate);
create index fkd0fwhreudgdgcdipqbmxnrpw0_classificationidwhcd on product.producttypexclassification (classificationid, warehousefromdate);
create index fkefusbrg6b297xo4uaifrnf7of_systemidwhcd on product.producttypexclassification (systemid, warehousefromdate);
create index fklpladubwvqlvl4m7vv5k7i04a_activeflagidwhcd on product.producttypexclassification (activeflagid, warehousefromdate);
create index fkm04vi4rjib6p4wstxivhv5uwb_originalsourcesystemidwhcd on product.producttypexclassification (originalsourcesystemid, warehousefromdate);
create index fkon0srs81eq9qmdm4clgh7lf1r_producttypeidwhcd on product.producttypexclassification (producttypeid, warehousefromdate);
create index fktby5ywfw3h2p0kwing53rbw83_enterpriseidwhcd on product.producttypexclassification (enterpriseid, warehousefromdate);
create index fkhp82lysbkher7sh9nmdc8j5mt_activeflagidwhcd on product.producttypexclassificationsecuritytoken (activeflagid, warehousefromdate);
create index fki005cybjhowcm4dc6a4rur35f_originalsourcesystemidwhcd on product.producttypexclassificationsecuritytoken (originalsourcesystemid, warehousefromdate);
create index fkkpdeivhwgmup6yq5or7mkhh3i_enterpriseidwhcd on product.producttypexclassificationsecuritytoken (enterpriseid, warehousefromdate);
create index fkm2t70qvf88y1hc6xxtp5yvedj_systemidwhcd on product.producttypexclassificationsecuritytoken (systemid, warehousefromdate);
create index fkmtyho44wd7jen9pckqv90td0j_producttypexclassificationidwhcd on product.producttypexclassificationsecuritytoken (producttypexclassificationid, warehousefromdate);
create index fkn22khncgjxl940nga25siqctl_securitytokenidwhcd on product.producttypexclassificationsecuritytoken (securitytokenid, warehousefromdate);
create index fk170jhdqdisurhraj01enjp07f_originalsourcesystemidwhcd on product.productxclassification (originalsourcesystemid, warehousefromdate);
create index fk4rxn4f51nq7nachqwenoijt19_systemidwhcd on product.productxclassification (systemid, warehousefromdate);
create index fk6if9d2r0jph42fstek03stxde_classificationidwhcd on product.productxclassification (classificationid, warehousefromdate);
create index fk8b81utqms0112fwiuv724pss1_activeflagidwhcd on product.productxclassification (activeflagid, warehousefromdate);
create index fk8bl09vfxm9qhghywxbfk36qra_enterpriseidwhcd on product.productxclassification (enterpriseid, warehousefromdate);
create index fktjv0q3owa2dvherxn8lwy4umj_productidwhcd on product.productxclassification (productid, warehousefromdate);
create index fk25k9qm7701i83jtdlw9pma3it_enterpriseidwhcd on product.productxclassificationsecuritytoken (enterpriseid, warehousefromdate);
create index fk2a1344eckqbgsu5qtw1hqtmii_productxclassificationidwhcd on product.productxclassificationsecuritytoken (productxclassificationid, warehousefromdate);
create index fkalo1aiwrqa3qvcoyta4wmron0_originalsourcesystemidwhcd on product.productxclassificationsecuritytoken (originalsourcesystemid, warehousefromdate);
create index fkncist9vnbkli1wfjeyqeqpbhi_systemidwhcd on product.productxclassificationsecuritytoken (systemid, warehousefromdate);
create index fkphp040h86si2e7sthuts7u84a_activeflagidwhcd on product.productxclassificationsecuritytoken (activeflagid, warehousefromdate);
create index fksqaqrh3mqcptl4uk5ucmkn34h_securitytokenidwhcd on product.productxclassificationsecuritytoken (securitytokenid, warehousefromdate);
create index fk6by61ucqfknganlsl2nrl75qi_childproductidwhcd on product.productxproduct (childproductid, warehousefromdate);
create index fk77xliloqs4xw4ci0ajbplqpww_systemidwhcd on product.productxproduct (systemid, warehousefromdate);
create index fkdcpq7m2melgfwv5auqyagl72h_activeflagidwhcd on product.productxproduct (activeflagid, warehousefromdate);
create index fkjtuo8wff4bgrag27r7b90e0cr_classificationidwhcd on product.productxproduct (classificationid, warehousefromdate);
create index fkkh1fb4vobxxm3gqr902b2sy9q_enterpriseidwhcd on product.productxproduct (enterpriseid, warehousefromdate);
create index fklwu619drh230ni7r1dow2jdo1_parentproductidwhcd on product.productxproduct (parentproductid, warehousefromdate);
create index fkpjcmb3bm0uykh5ycoxw327a6y_originalsourcesystemidwhcd on product.productxproduct (originalsourcesystemid, warehousefromdate);
create index fk1ig8t64jsxnh0bpi3ql9st9ox_enterpriseidwhcd on product.productxproductsecuritytoken (enterpriseid, warehousefromdate);
create index fk87msnwio1ifds9frgcawk703q_securitytokenidwhcd on product.productxproductsecuritytoken (securitytokenid, warehousefromdate);
create index fkfov8qx41nggbg75jj8ely4wrp_activeflagidwhcd on product.productxproductsecuritytoken (activeflagid, warehousefromdate);
create index fkk3c9qgnvemsrtifm1qkfwwpai_originalsourcesystemidwhcd on product.productxproductsecuritytoken (originalsourcesystemid, warehousefromdate);
create index fkm2agj4yop5p5pdbbqalr9ys6d_systemidwhcd on product.productxproductsecuritytoken (systemid, warehousefromdate);
create index fksmhfy43ik4seggjpeh0hd7ohe_productxproductidwhcd on product.productxproductsecuritytoken (productxproductid, warehousefromdate);
create index fk1v5k0dbqs8u10y8qplq99qoiq_productidwhcd on product.productxproducttype (productid, warehousefromdate);
create index fk2n7njekvkkktqita4lo8o2rop_systemidwhcd on product.productxproducttype (systemid, warehousefromdate);
create index fk64ad5aqtu7abdea2c46wmwsyp_activeflagidwhcd on product.productxproducttype (activeflagid, warehousefromdate);
create index fk7qloe9c2vsgtb7r52s8rwam77_producttypeidwhcd on product.productxproducttype (producttypeid, warehousefromdate);
create index fki3kgl08d83biiovc4ipk0i82k_classificationidwhcd on product.productxproducttype (classificationid, warehousefromdate);
create index fkqx5p60ei4jsps9ajg56g6y75n_originalsourcesystemidwhcd on product.productxproducttype (originalsourcesystemid, warehousefromdate);
create index fktn5yb54e9i7r3hwtr1bojxh6e_enterpriseidwhcd on product.productxproducttype (enterpriseid, warehousefromdate);
create index fk24ny4320upy05vtrp0bl40edh_securitytokenidwhcd on product.productxproducttypesecuritytoken (securitytokenid, warehousefromdate);
create index fk3h3ux71dsf7opesl0p4fq9y2n_productxproducttypeidwhcd on product.productxproducttypesecuritytoken (productxproducttypeid, warehousefromdate);
create index fk5r6sj8u01cbw7ndxn6nkbqbgs_activeflagidwhcd on product.productxproducttypesecuritytoken (activeflagid, warehousefromdate);
create index fke8k4xro3jq77djofuxtcqjnb7_originalsourcesystemidwhcd on product.productxproducttypesecuritytoken (originalsourcesystemid, warehousefromdate);
create index fknnkyxacvaltro761yay2h0a0t_systemidwhcd on product.productxproducttypesecuritytoken (systemid, warehousefromdate);
create index fknr2deq2638s77m37vyx16g6i0_enterpriseidwhcd on product.productxproducttypesecuritytoken (enterpriseid, warehousefromdate);
create index fk2kfndi7vipw90dmibvhml8luf_classificationidwhcd on product.productxresourceitem (classificationid, warehousefromdate);
create index fk3c9bk1tu9tr4ajj57j50lq61s_enterpriseidwhcd on product.productxresourceitem (enterpriseid, warehousefromdate);
create index fk4ircw0cakrrbxh9eb947do1r1_activeflagidwhcd on product.productxresourceitem (activeflagid, warehousefromdate);
create index fkdlspqxuytfux61envksg2oblc_systemidwhcd on product.productxresourceitem (systemid, warehousefromdate);
create index fkjcm094m4k21700pj371gyg28_productidwhcd on product.productxresourceitem (productid, warehousefromdate);
create index fkk4j0vxf6l51756si5xlelvhyx_resourceitemidwhcd on product.productxresourceitem (resourceitemid, warehousefromdate);
create index fkq078arvokvft7rp6g01drm5ga_originalsourcesystemidwhcd on product.productxresourceitem (originalsourcesystemid, warehousefromdate);
create index fk2n140poncbaodw93p66dr19wm_enterpriseidwhcd on product.productxresourceitemsecuritytoken (enterpriseid, warehousefromdate);
create index fk4pq3n38ujll0fn7l4mw2f0uj2_productxresourceitemidwhcd on product.productxresourceitemsecuritytoken (productxresourceitemid, warehousefromdate);
create index fk8ljhn730t04d2yrudqin3b4rx_originalsourcesystemidwhcd on product.productxresourceitemsecuritytoken (originalsourcesystemid, warehousefromdate);
create index fk9rwmunm7mftbfjjefdk9i0ryh_activeflagidwhcd on product.productxresourceitemsecuritytoken (activeflagid, warehousefromdate);
create index fkhgl7dki8ue6gmivh1ulwel4j1_systemidwhcd on product.productxresourceitemsecuritytoken (systemid, warehousefromdate);
create index fklwlxp6u9a4k42nl1xqst9y0y_securitytokenidwhcd on product.productxresourceitemsecuritytoken (securitytokenid, warehousefromdate);

