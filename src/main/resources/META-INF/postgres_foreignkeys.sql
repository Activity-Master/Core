
--
-- TOC entry 4008 (class 2606 OID 205645)
-- Name: address address_pkey; Type: CONSTRAINT; Schema: address; Owner: postgres
--

ALTER TABLE address.address
    ADD CONSTRAINT address_pkey PRIMARY KEY (addressid, warehousecreateddate);


--
-- TOC entry 4010 (class 2606 OID 205647)
-- Name: addresssecuritytoken addresssecuritytoken_pkey; Type: CONSTRAINT; Schema: address; Owner: postgres
--

ALTER TABLE address.addresssecuritytoken
    ADD CONSTRAINT addresssecuritytoken_pkey PRIMARY KEY (addresssecuritytokenid, warehousecreateddate);


--
-- TOC entry 4012 (class 2606 OID 205649)
-- Name: addressxclassification addressxclassification_pkey; Type: CONSTRAINT; Schema: address; Owner: postgres
--

ALTER TABLE address.addressxclassification
    ADD CONSTRAINT addressxclassification_pkey PRIMARY KEY (addressxclassificationid, warehousecreateddate);


--
-- TOC entry 4014 (class 2606 OID 205651)
-- Name: addressxclassificationsecuritytoken addressxclassificationsecuritytoken_pkey; Type: CONSTRAINT; Schema: address; Owner: postgres
--

ALTER TABLE address.addressxclassificationsecuritytoken
    ADD CONSTRAINT addressxclassificationsecuritytoken_pkey PRIMARY KEY (addressxclassificationsecuritytokenid, warehousecreateddate);


--
-- TOC entry 4016 (class 2606 OID 205653)
-- Name: addressxgeography addressxgeography_pkey; Type: CONSTRAINT; Schema: address; Owner: postgres
--

ALTER TABLE address.addressxgeography
    ADD CONSTRAINT addressxgeography_pkey PRIMARY KEY (addressxgeographyid, warehousecreateddate);


--
-- TOC entry 4018 (class 2606 OID 205655)
-- Name: addressxgeographysecuritytoken addressxgeographysecuritytoken_pkey; Type: CONSTRAINT; Schema: address; Owner: postgres
--

ALTER TABLE address.addressxgeographysecuritytoken
    ADD CONSTRAINT addressxgeographysecuritytoken_pkey PRIMARY KEY (addressxgeographysecuritytokenid, warehousecreateddate);


--
-- TOC entry 4020 (class 2606 OID 205657)
-- Name: addressxresourceitem addressxresourceitem_pkey; Type: CONSTRAINT; Schema: address; Owner: postgres
--

ALTER TABLE address.addressxresourceitem
    ADD CONSTRAINT addressxresourceitem_pkey PRIMARY KEY (addressxresourceitemid, warehousecreateddate);


--
-- TOC entry 4022 (class 2606 OID 205659)
-- Name: addressxresourceitemsecuritytoken addressxresourceitemsecuritytoken_pkey; Type: CONSTRAINT; Schema: address; Owner: postgres
--

ALTER TABLE address.addressxresourceitemsecuritytoken
    ADD CONSTRAINT addressxresourceitemsecuritytoken_pkey PRIMARY KEY (addressxresourceitemsecuritytokenid, warehousecreateddate);


--
-- TOC entry 4372 (class 2606 OID 213068)
-- Name: arrangement arrangement_pkey; Type: CONSTRAINT; Schema: arrangement; Owner: postgres
--

ALTER TABLE arrangement.arrangement
    ADD CONSTRAINT arrangement_pkey PRIMARY KEY (arrangementid, warehousecreateddate);


--
-- TOC entry 4374 (class 2606 OID 213075)
-- Name: arrangementsecuritytoken arrangementsecuritytoken_pkey; Type: CONSTRAINT; Schema: arrangement; Owner: postgres
--

ALTER TABLE arrangement.arrangementsecuritytoken
    ADD CONSTRAINT arrangementsecuritytoken_pkey PRIMARY KEY (arrangementsecuritytokenid, warehousecreateddate);


--
-- TOC entry 4376 (class 2606 OID 213082)
-- Name: arrangementtype arrangementtype_pkey; Type: CONSTRAINT; Schema: arrangement; Owner: postgres
--

ALTER TABLE arrangement.arrangementtype
    ADD CONSTRAINT arrangementtype_pkey PRIMARY KEY (arrangementtypeid, warehousecreateddate);


--
-- TOC entry 4378 (class 2606 OID 213089)
-- Name: arrangementtypesecuritytoken arrangementtypesecuritytoken_pkey; Type: CONSTRAINT; Schema: arrangement; Owner: postgres
--

ALTER TABLE arrangement.arrangementtypesecuritytoken
    ADD CONSTRAINT arrangementtypesecuritytoken_pkey PRIMARY KEY (arrangementtypesecuritytokenid, warehousecreateddate);


--
-- TOC entry 4380 (class 2606 OID 213096)
-- Name: arrangementtypexclassification arrangementtypexclassification_pkey; Type: CONSTRAINT; Schema: arrangement; Owner: postgres
--

ALTER TABLE arrangement.arrangementtypexclassification
    ADD CONSTRAINT arrangementtypexclassification_pkey PRIMARY KEY (arrangementtypexclassificationid, warehousecreateddate);


--
-- TOC entry 4382 (class 2606 OID 213103)
-- Name: arrangementtypexclassificationsecuritytoken arrangementtypexclassificationsecuritytoken_pkey; Type: CONSTRAINT; Schema: arrangement; Owner: postgres
--

ALTER TABLE arrangement.arrangementtypexclassificationsecuritytoken
    ADD CONSTRAINT arrangementtypexclassificationsecuritytoken_pkey PRIMARY KEY (arrangementtypexclassificationsecuritytokenid, warehousecreateddate);


--
-- TOC entry 4384 (class 2606 OID 213110)
-- Name: arrangementxarrangement arrangementxarrangement_pkey; Type: CONSTRAINT; Schema: arrangement; Owner: postgres
--

ALTER TABLE arrangement.arrangementxarrangement
    ADD CONSTRAINT arrangementxarrangement_pkey PRIMARY KEY (arrangementxarrangementid, warehousecreateddate);


--
-- TOC entry 4386 (class 2606 OID 213117)
-- Name: arrangementxarrangementsecuritytoken arrangementxarrangementsecuritytoken_pkey; Type: CONSTRAINT; Schema: arrangement; Owner: postgres
--

ALTER TABLE arrangement.arrangementxarrangementsecuritytoken
    ADD CONSTRAINT arrangementxarrangementsecuritytoken_pkey PRIMARY KEY (arrangementxarrangementsecuritytokenid, warehousecreateddate);


--
-- TOC entry 4388 (class 2606 OID 213124)
-- Name: arrangementxarrangementtype arrangementxarrangementtype_pkey; Type: CONSTRAINT; Schema: arrangement; Owner: postgres
--

ALTER TABLE arrangement.arrangementxarrangementtype
    ADD CONSTRAINT arrangementxarrangementtype_pkey PRIMARY KEY (arrangementxarrangementtypeid, warehousecreateddate);


--
-- TOC entry 4390 (class 2606 OID 213131)
-- Name: arrangementxarrangementtypesecuritytoken arrangementxarrangementtypesecuritytoken_pkey; Type: CONSTRAINT; Schema: arrangement; Owner: postgres
--

ALTER TABLE arrangement.arrangementxarrangementtypesecuritytoken
    ADD CONSTRAINT arrangementxarrangementtypesecuritytoken_pkey PRIMARY KEY (arrangementxarrangementtypesecuritytokenid, warehousecreateddate);


--
-- TOC entry 4392 (class 2606 OID 213138)
-- Name: arrangementxclassification arrangementxclassification_pkey; Type: CONSTRAINT; Schema: arrangement; Owner: postgres
--

ALTER TABLE arrangement.arrangementxclassification
    ADD CONSTRAINT arrangementxclassification_pkey PRIMARY KEY (arrangementxclassificationid, warehousecreateddate);


--
-- TOC entry 4394 (class 2606 OID 213145)
-- Name: arrangementxclassificationsecuritytoken arrangementxclassificationsecuritytoken_pkey; Type: CONSTRAINT; Schema: arrangement; Owner: postgres
--

ALTER TABLE arrangement.arrangementxclassificationsecuritytoken
    ADD CONSTRAINT arrangementxclassificationsecuritytoken_pkey PRIMARY KEY (arrangementxclassificationsecuritytokenid, warehousecreateddate);


--
-- TOC entry 4396 (class 2606 OID 213152)
-- Name: arrangementxinvolvedparty arrangementxinvolvedparty_pkey; Type: CONSTRAINT; Schema: arrangement; Owner: postgres
--

ALTER TABLE arrangement.arrangementxinvolvedparty
    ADD CONSTRAINT arrangementxinvolvedparty_pkey PRIMARY KEY (arrangementxinvolvedpartyid, warehousecreateddate);


--
-- TOC entry 4398 (class 2606 OID 213159)
-- Name: arrangementxinvolvedpartysecuritytoken arrangementxinvolvedpartysecuritytoken_pkey; Type: CONSTRAINT; Schema: arrangement; Owner: postgres
--

ALTER TABLE arrangement.arrangementxinvolvedpartysecuritytoken
    ADD CONSTRAINT arrangementxinvolvedpartysecuritytoken_pkey PRIMARY KEY (arrangementxinvolvedpartysecuritytokenid, warehousecreateddate);


--
-- TOC entry 4400 (class 2606 OID 213166)
-- Name: arrangementxproduct arrangementxproduct_pkey; Type: CONSTRAINT; Schema: arrangement; Owner: postgres
--

ALTER TABLE arrangement.arrangementxproduct
    ADD CONSTRAINT arrangementxproduct_pkey PRIMARY KEY (arrangementxproductid, warehousecreateddate);


--
-- TOC entry 4402 (class 2606 OID 213173)
-- Name: arrangementxproductsecuritytoken arrangementxproductsecuritytoken_pkey; Type: CONSTRAINT; Schema: arrangement; Owner: postgres
--

ALTER TABLE arrangement.arrangementxproductsecuritytoken
    ADD CONSTRAINT arrangementxproductsecuritytoken_pkey PRIMARY KEY (arrangementxproductsecuritytokenid, warehousecreateddate);


--
-- TOC entry 4404 (class 2606 OID 213180)
-- Name: arrangementxresourceitem arrangementxresourceitem_pkey; Type: CONSTRAINT; Schema: arrangement; Owner: postgres
--

ALTER TABLE arrangement.arrangementxresourceitem
    ADD CONSTRAINT arrangementxresourceitem_pkey PRIMARY KEY (arrangementxresourceitemid, warehousecreateddate);


--
-- TOC entry 4406 (class 2606 OID 213187)
-- Name: arrangementxresourceitemsecuritytoken arrangementxresourceitemsecuritytoken_pkey; Type: CONSTRAINT; Schema: arrangement; Owner: postgres
--

ALTER TABLE arrangement.arrangementxresourceitemsecuritytoken
    ADD CONSTRAINT arrangementxresourceitemsecuritytoken_pkey PRIMARY KEY (arrangementxresourceitemsecuritytokenid, warehousecreateddate);


--
-- TOC entry 4408 (class 2606 OID 213194)
-- Name: arrangementxrules arrangementxrules_pkey; Type: CONSTRAINT; Schema: arrangement; Owner: postgres
--

ALTER TABLE arrangement.arrangementxrules
    ADD CONSTRAINT arrangementxrules_pkey PRIMARY KEY (arrangementxrulesid, warehousecreateddate);


--
-- TOC entry 4410 (class 2606 OID 213201)
-- Name: arrangementxrulessecuritytoken arrangementxrulessecuritytoken_pkey; Type: CONSTRAINT; Schema: arrangement; Owner: postgres
--

ALTER TABLE arrangement.arrangementxrulessecuritytoken
    ADD CONSTRAINT arrangementxrulessecuritytoken_pkey PRIMARY KEY (arrangementxrulessecuritytokenid, warehousecreateddate);


--
-- TOC entry 4412 (class 2606 OID 213208)
-- Name: arrangementxrulestype arrangementxrulestype_pkey; Type: CONSTRAINT; Schema: arrangement; Owner: postgres
--

ALTER TABLE arrangement.arrangementxrulestype
    ADD CONSTRAINT arrangementxrulestype_pkey PRIMARY KEY (arrangementxrulestypeid, warehousecreateddate);


--
-- TOC entry 4414 (class 2606 OID 213215)
-- Name: arrangementxrulestypesecuritytoken arrangementxrulestypesecuritytoken_pkey; Type: CONSTRAINT; Schema: arrangement; Owner: postgres
--

ALTER TABLE arrangement.arrangementxrulestypesecuritytoken
    ADD CONSTRAINT arrangementxrulestypesecuritytoken_pkey PRIMARY KEY (arrangementxrulestypesecuritytokenid, warehousecreateddate);


--
-- TOC entry 4024 (class 2606 OID 205661)
-- Name: classification classification_pkey; Type: CONSTRAINT; Schema: classification; Owner: postgres
--

ALTER TABLE classification.classification
    ADD CONSTRAINT classification_pkey PRIMARY KEY (classificationid, warehousecreateddate);


--
-- TOC entry 4026 (class 2606 OID 205663)
-- Name: classificationdataconcept classificationdataconcept_pkey; Type: CONSTRAINT; Schema: classification; Owner: postgres
--

ALTER TABLE classification.classificationdataconcept
    ADD CONSTRAINT classificationdataconcept_pkey PRIMARY KEY (classificationdataconceptid, warehousecreateddate);


--
-- TOC entry 4028 (class 2606 OID 205665)
-- Name: classificationdataconceptsecuritytoken classificationdataconceptsecuritytoken_pkey; Type: CONSTRAINT; Schema: classification; Owner: postgres
--

ALTER TABLE classification.classificationdataconceptsecuritytoken
    ADD CONSTRAINT classificationdataconceptsecuritytoken_pkey PRIMARY KEY (classificationdataconceptsecuritytokenid, warehousecreateddate);


--
-- TOC entry 4030 (class 2606 OID 205667)
-- Name: classificationdataconceptxclassification classificationdataconceptxclassification_pkey; Type: CONSTRAINT; Schema: classification; Owner: postgres
--

ALTER TABLE classification.classificationdataconceptxclassification
    ADD CONSTRAINT classificationdataconceptxclassification_pkey PRIMARY KEY (classificationdataconceptxclassificationid, warehousecreateddate);


--
-- TOC entry 4032 (class 2606 OID 205669)
-- Name: classificationdataconceptxclassificationsecuritytoken classificationdataconceptxclassificationsecuritytoken_pkey; Type: CONSTRAINT; Schema: classification; Owner: postgres
--

ALTER TABLE classification.classificationdataconceptxclassificationsecuritytoken
    ADD CONSTRAINT classificationdataconceptxclassificationsecuritytoken_pkey PRIMARY KEY (classificationdataconceptxclassificationsecuritytokenid,
                                                                                           warehousecreateddate);


--
-- TOC entry 4034 (class 2606 OID 205671)
-- Name: classificationdataconceptxresourceitem classificationdataconceptxresourceitem_pkey; Type: CONSTRAINT; Schema: classification; Owner: postgres
--

ALTER TABLE classification.classificationdataconceptxresourceitem
    ADD CONSTRAINT classificationdataconceptxresourceitem_pkey PRIMARY KEY (classificationdataconceptxresourceitemid, warehousecreateddate);


--
-- TOC entry 4036 (class 2606 OID 205673)
-- Name: classificationdataconceptxresourceitemsecuritytoken classificationdataconceptxresourceitemsecuritytoken_pkey; Type: CONSTRAINT; Schema: classification; Owner: postgres
--

ALTER TABLE classification.classificationdataconceptxresourceitemsecuritytoken
    ADD CONSTRAINT classificationdataconceptxresourceitemsecuritytoken_pkey PRIMARY KEY (classificationdataconceptxresourceitemsecuritytokenid,
                                                                                         warehousecreateddate);


--
-- TOC entry 4038 (class 2606 OID 205675)
-- Name: classificationsecuritytoken classificationsecuritytoken_pkey; Type: CONSTRAINT; Schema: classification; Owner: postgres
--

ALTER TABLE classification.classificationsecuritytoken
    ADD CONSTRAINT classificationsecuritytoken_pkey PRIMARY KEY (classificationsecuritytokenid, warehousecreateddate);


--
-- TOC entry 4040 (class 2606 OID 205677)
-- Name: classificationxclassification classificationxclassification_pkey; Type: CONSTRAINT; Schema: classification; Owner: postgres
--

ALTER TABLE classification.classificationxclassification
    ADD CONSTRAINT classificationxclassification_pkey PRIMARY KEY (classificationxclassificationid, warehousecreateddate);


--
-- TOC entry 4042 (class 2606 OID 205679)
-- Name: classificationxclassificationsecuritytoken classificationxclassificationsecuritytoken_pkey; Type: CONSTRAINT; Schema: classification; Owner: postgres
--

ALTER TABLE classification.classificationxclassificationsecuritytoken
    ADD CONSTRAINT classificationxclassificationsecuritytoken_pkey PRIMARY KEY (classificationxclassificationsecuritytokenid, warehousecreateddate);


--
-- TOC entry 4044 (class 2606 OID 205681)
-- Name: classificationxresourceitem classificationxresourceitem_pkey; Type: CONSTRAINT; Schema: classification; Owner: postgres
--

ALTER TABLE classification.classificationxresourceitem
    ADD CONSTRAINT classificationxresourceitem_pkey PRIMARY KEY (classificationxresourceitemid, warehousecreateddate);


--
-- TOC entry 4046 (class 2606 OID 205683)
-- Name: classificationxresourceitemsecuritytoken classificationxresourceitemsecuritytoken_pkey; Type: CONSTRAINT; Schema: classification; Owner: postgres
--

ALTER TABLE classification.classificationxresourceitemsecuritytoken
    ADD CONSTRAINT classificationxresourceitemsecuritytoken_pkey PRIMARY KEY (classificationxresourceitemsecuritytokenid, warehousecreateddate);


--
-- TOC entry 4048 (class 2606 OID 205685)
-- Name: activeflag activeflag_pkey; Type: CONSTRAINT; Schema: dbo; Owner: postgres
--

ALTER TABLE dbo.activeflag
    ADD CONSTRAINT activeflag_pkey PRIMARY KEY (activeflagid, warehousecreateddate);


--
-- TOC entry 4050 (class 2606 OID 205687)
-- Name: activeflagsecuritytoken activeflagsecuritytoken_pkey; Type: CONSTRAINT; Schema: dbo; Owner: postgres
--

ALTER TABLE dbo.activeflagsecuritytoken
    ADD CONSTRAINT activeflagsecuritytoken_pkey PRIMARY KEY (activeflagsecuritytokenid, warehousecreateddate);


--
-- TOC entry 4052 (class 2606 OID 205689)
-- Name: activeflagxclassification activeflagxclassification_pkey; Type: CONSTRAINT; Schema: dbo; Owner: postgres
--

ALTER TABLE dbo.activeflagxclassification
    ADD CONSTRAINT activeflagxclassification_pkey PRIMARY KEY (activeflagxclassificationid, warehousecreateddate);


--
-- TOC entry 4054 (class 2606 OID 205691)
-- Name: activeflagxclassificationsecuritytoken activeflagxclassificationsecuritytoken_pkey; Type: CONSTRAINT; Schema: dbo; Owner: postgres
--

ALTER TABLE dbo.activeflagxclassificationsecuritytoken
    ADD CONSTRAINT activeflagxclassificationsecuritytoken_pkey PRIMARY KEY (activeflagxclassificationsecuritytokenid, warehousecreateddate);


--
-- TOC entry 4056 (class 2606 OID 205693)
-- Name: enterprise enterprise_pkey; Type: CONSTRAINT; Schema: dbo; Owner: postgres
--

ALTER TABLE dbo.enterprise
    ADD CONSTRAINT enterprise_pkey PRIMARY KEY (enterpriseid, warehousecreateddate);


--
-- TOC entry 4058 (class 2606 OID 205695)
-- Name: enterprisesecuritytoken enterprisesecuritytoken_pkey; Type: CONSTRAINT; Schema: dbo; Owner: postgres
--

ALTER TABLE dbo.enterprisesecuritytoken
    ADD CONSTRAINT enterprisesecuritytoken_pkey PRIMARY KEY (enterprisesecuritytokenid, warehousecreateddate);


--
-- TOC entry 4060 (class 2606 OID 205697)
-- Name: enterprisexclassification enterprisexclassification_pkey; Type: CONSTRAINT; Schema: dbo; Owner: postgres
--

ALTER TABLE dbo.enterprisexclassification
    ADD CONSTRAINT enterprisexclassification_pkey PRIMARY KEY (enterprisexclassificationid, warehousecreateddate);


--
-- TOC entry 4062 (class 2606 OID 205699)
-- Name: enterprisexclassificationsecuritytoken enterprisexclassificationsecuritytoken_pkey; Type: CONSTRAINT; Schema: dbo; Owner: postgres
--

ALTER TABLE dbo.enterprisexclassificationsecuritytoken
    ADD CONSTRAINT enterprisexclassificationsecuritytoken_pkey PRIMARY KEY (enterprisexclassificationsecuritytokenid, warehousecreateddate);


--
-- TOC entry 4064 (class 2606 OID 205701)
-- Name: systems systems_pkey; Type: CONSTRAINT; Schema: dbo; Owner: postgres
--

ALTER TABLE dbo.systems
    ADD CONSTRAINT systems_pkey PRIMARY KEY (systemid, warehousecreateddate);


--
-- TOC entry 4066 (class 2606 OID 205703)
-- Name: systemssecuritytoken systemssecuritytoken_pkey; Type: CONSTRAINT; Schema: dbo; Owner: postgres
--

ALTER TABLE dbo.systemssecuritytoken
    ADD CONSTRAINT systemssecuritytoken_pkey PRIMARY KEY (systemssecuritytokenid, warehousecreateddate);


--
-- TOC entry 4068 (class 2606 OID 205705)
-- Name: systemxclassification systemxclassification_pkey; Type: CONSTRAINT; Schema: dbo; Owner: postgres
--

ALTER TABLE dbo.systemxclassification
    ADD CONSTRAINT systemxclassification_pkey PRIMARY KEY (systemxclassificationid, warehousecreateddate);


--
-- TOC entry 4070 (class 2606 OID 205707)
-- Name: systemxclassificationsecuritytoken systemxclassificationsecuritytoken_pkey; Type: CONSTRAINT; Schema: dbo; Owner: postgres
--

ALTER TABLE dbo.systemxclassificationsecuritytoken
    ADD CONSTRAINT systemxclassificationsecuritytoken_pkey PRIMARY KEY (systemxclassificationsecuritytokenid, warehousecreateddate);


--
-- TOC entry 4072 (class 2606 OID 205709)
-- Name: event event_pkey; Type: CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.event
    ADD CONSTRAINT event_pkey PRIMARY KEY (eventid, warehousecreateddate);


--
-- TOC entry 4074 (class 2606 OID 205711)
-- Name: eventsecuritytoken eventsecuritytoken_pkey; Type: CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.eventsecuritytoken
    ADD CONSTRAINT eventsecuritytoken_pkey PRIMARY KEY (eventssecuritytokenid, warehousecreateddate);


--
-- TOC entry 4076 (class 2606 OID 205713)
-- Name: eventtype eventtype_pkey; Type: CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.eventtype
    ADD CONSTRAINT eventtype_pkey PRIMARY KEY (eventtypeid, warehousecreateddate);


--
-- TOC entry 4078 (class 2606 OID 205715)
-- Name: eventtypessecuritytoken eventtypessecuritytoken_pkey; Type: CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.eventtypessecuritytoken
    ADD CONSTRAINT eventtypessecuritytoken_pkey PRIMARY KEY (eventtypessecuritytokenid, warehousecreateddate);


--
-- TOC entry 4080 (class 2606 OID 205717)
-- Name: eventxaddress eventxaddress_pkey; Type: CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.eventxaddress
    ADD CONSTRAINT eventxaddress_pkey PRIMARY KEY (eventxaddressid, warehousecreateddate);


--
-- TOC entry 4082 (class 2606 OID 205719)
-- Name: eventxaddresssecuritytoken eventxaddresssecuritytoken_pkey; Type: CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.eventxaddresssecuritytoken
    ADD CONSTRAINT eventxaddresssecuritytoken_pkey PRIMARY KEY (eventxaddresssecuritytokenid, warehousecreateddate);


--
-- TOC entry 4084 (class 2606 OID 205721)
-- Name: eventxarrangement eventxarrangement_pkey; Type: CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.eventxarrangement
    ADD CONSTRAINT eventxarrangement_pkey PRIMARY KEY (eventxarrangementsid, warehousecreateddate);


--
-- TOC entry 4086 (class 2606 OID 205723)
-- Name: eventxarrangementssecuritytoken eventxarrangementssecuritytoken_pkey; Type: CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.eventxarrangementssecuritytoken
    ADD CONSTRAINT eventxarrangementssecuritytoken_pkey PRIMARY KEY (eventxarrangementssecuritytokenid, warehousecreateddate);


--
-- TOC entry 4088 (class 2606 OID 205725)
-- Name: eventxclassification eventxclassification_pkey; Type: CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.eventxclassification
    ADD CONSTRAINT eventxclassification_pkey PRIMARY KEY (eventxclassificationid, warehousecreateddate);


--
-- TOC entry 4090 (class 2606 OID 205727)
-- Name: eventxclassificationsecuritytoken eventxclassificationsecuritytoken_pkey; Type: CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.eventxclassificationsecuritytoken
    ADD CONSTRAINT eventxclassificationsecuritytoken_pkey PRIMARY KEY (eventxclassificationssecuritytokenid, warehousecreateddate);


--
-- TOC entry 4092 (class 2606 OID 205729)
-- Name: eventxevent eventxevent_pkey; Type: CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.eventxevent
    ADD CONSTRAINT eventxevent_pkey PRIMARY KEY (eventxeventid, warehousecreateddate);


--
-- TOC entry 4094 (class 2606 OID 205731)
-- Name: eventxeventsecuritytoken eventxeventsecuritytoken_pkey; Type: CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.eventxeventsecuritytoken
    ADD CONSTRAINT eventxeventsecuritytoken_pkey PRIMARY KEY (eventxeventsecuritytokenid, warehousecreateddate);


--
-- TOC entry 4096 (class 2606 OID 205733)
-- Name: eventxeventtype eventxeventtype_pkey; Type: CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.eventxeventtype
    ADD CONSTRAINT eventxeventtype_pkey PRIMARY KEY (eventxeventtypeid, warehousecreateddate);


--
-- TOC entry 4098 (class 2606 OID 205735)
-- Name: eventxeventtypesecuritytoken eventxeventtypesecuritytoken_pkey; Type: CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.eventxeventtypesecuritytoken
    ADD CONSTRAINT eventxeventtypesecuritytoken_pkey PRIMARY KEY (eventxeventtypesecuritytokenid, warehousecreateddate);


--
-- TOC entry 4100 (class 2606 OID 205737)
-- Name: eventxgeography eventxgeography_pkey; Type: CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.eventxgeography
    ADD CONSTRAINT eventxgeography_pkey PRIMARY KEY (eventxgeographyid, warehousecreateddate);


--
-- TOC entry 4102 (class 2606 OID 205739)
-- Name: eventxgeographysecuritytoken eventxgeographysecuritytoken_pkey; Type: CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.eventxgeographysecuritytoken
    ADD CONSTRAINT eventxgeographysecuritytoken_pkey PRIMARY KEY (eventxgeographysecuritytokenid, warehousecreateddate);


--
-- TOC entry 4104 (class 2606 OID 205741)
-- Name: eventxinvolvedparty eventxinvolvedparty_pkey; Type: CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.eventxinvolvedparty
    ADD CONSTRAINT eventxinvolvedparty_pkey PRIMARY KEY (eventxinvolvedpartyid, warehousecreateddate);


--
-- TOC entry 4106 (class 2606 OID 205743)
-- Name: eventxinvolvedpartysecuritytoken eventxinvolvedpartysecuritytoken_pkey; Type: CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.eventxinvolvedpartysecuritytoken
    ADD CONSTRAINT eventxinvolvedpartysecuritytoken_pkey PRIMARY KEY (eventxinvolvedpartysecuritytokenid, warehousecreateddate);


--
-- TOC entry 4108 (class 2606 OID 205745)
-- Name: eventxproduct eventxproduct_pkey; Type: CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.eventxproduct
    ADD CONSTRAINT eventxproduct_pkey PRIMARY KEY (eventxproductid, warehousecreateddate);


--
-- TOC entry 4110 (class 2606 OID 205747)
-- Name: eventxproductsecuritytoken eventxproductsecuritytoken_pkey; Type: CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.eventxproductsecuritytoken
    ADD CONSTRAINT eventxproductsecuritytoken_pkey PRIMARY KEY (eventxproductsecuritytokenid, warehousecreateddate);


--
-- TOC entry 4112 (class 2606 OID 205749)
-- Name: eventxresourceitem eventxresourceitem_pkey; Type: CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.eventxresourceitem
    ADD CONSTRAINT eventxresourceitem_pkey PRIMARY KEY (eventxresourceitemid, warehousecreateddate);


--
-- TOC entry 4114 (class 2606 OID 205751)
-- Name: eventxresourceitemsecuritytoken eventxresourceitemsecuritytoken_pkey; Type: CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.eventxresourceitemsecuritytoken
    ADD CONSTRAINT eventxresourceitemsecuritytoken_pkey PRIMARY KEY (eventxresourceitemsecuritytokenid, warehousecreateddate);


--
-- TOC entry 4116 (class 2606 OID 205753)
-- Name: eventxrules eventxrules_pkey; Type: CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.eventxrules
    ADD CONSTRAINT eventxrules_pkey PRIMARY KEY (eventxrulesid, warehousecreateddate);


--
-- TOC entry 4118 (class 2606 OID 205755)
-- Name: eventxrulessecuritytoken eventxrulessecuritytoken_pkey; Type: CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.eventxrulessecuritytoken
    ADD CONSTRAINT eventxrulessecuritytoken_pkey PRIMARY KEY (eventxrulessecuritytokenid, warehousecreateddate);


--
-- TOC entry 4120 (class 2606 OID 205757)
-- Name: geography geography_pkey; Type: CONSTRAINT; Schema: geography; Owner: postgres
--

ALTER TABLE geography.geography
    ADD CONSTRAINT geography_pkey PRIMARY KEY (geographyid, warehousecreateddate);


--
-- TOC entry 4122 (class 2606 OID 205759)
-- Name: geographysecuritytoken geographysecuritytoken_pkey; Type: CONSTRAINT; Schema: geography; Owner: postgres
--

ALTER TABLE geography.geographysecuritytoken
    ADD CONSTRAINT geographysecuritytoken_pkey PRIMARY KEY (geographysecuritytokenid, warehousecreateddate);


--
-- TOC entry 4124 (class 2606 OID 205761)
-- Name: geographyxclassification geographyxclassification_pkey; Type: CONSTRAINT; Schema: geography; Owner: postgres
--

ALTER TABLE geography.geographyxclassification
    ADD CONSTRAINT geographyxclassification_pkey PRIMARY KEY (geographyxclassificationid, warehousecreateddate);


--
-- TOC entry 4126 (class 2606 OID 205763)
-- Name: geographyxclassificationsecuritytoken geographyxclassificationsecuritytoken_pkey; Type: CONSTRAINT; Schema: geography; Owner: postgres
--

ALTER TABLE geography.geographyxclassificationsecuritytoken
    ADD CONSTRAINT geographyxclassificationsecuritytoken_pkey PRIMARY KEY (geographyxclassificationsecuritytokenid, warehousecreateddate);


--
-- TOC entry 4128 (class 2606 OID 205765)
-- Name: geographyxgeography geographyxgeography_pkey; Type: CONSTRAINT; Schema: geography; Owner: postgres
--

ALTER TABLE geography.geographyxgeography
    ADD CONSTRAINT geographyxgeography_pkey PRIMARY KEY (geographyxgeographyid, warehousecreateddate);


--
-- TOC entry 4130 (class 2606 OID 205767)
-- Name: geographyxgeographysecuritytoken geographyxgeographysecuritytoken_pkey; Type: CONSTRAINT; Schema: geography; Owner: postgres
--

ALTER TABLE geography.geographyxgeographysecuritytoken
    ADD CONSTRAINT geographyxgeographysecuritytoken_pkey PRIMARY KEY (geographyxgeographysecuritytokenid, warehousecreateddate);


--
-- TOC entry 4132 (class 2606 OID 205769)
-- Name: geographyxresourceitem geographyxresourceitem_pkey; Type: CONSTRAINT; Schema: geography; Owner: postgres
--

ALTER TABLE geography.geographyxresourceitem
    ADD CONSTRAINT geographyxresourceitem_pkey PRIMARY KEY (geographyxresourceitemid, warehousecreateddate);


--
-- TOC entry 4134 (class 2606 OID 205771)
-- Name: geographyxresourceitemsecuritytoken geographyxresourceitemsecuritytoken_pkey; Type: CONSTRAINT; Schema: geography; Owner: postgres
--

ALTER TABLE geography.geographyxresourceitemsecuritytoken
    ADD CONSTRAINT geographyxresourceitemsecuritytoken_pkey PRIMARY KEY (geographyxresourceitemsecuritytokenid, warehousecreateddate);


--
-- TOC entry 4136 (class 2606 OID 205773)
-- Name: involvedparty involvedparty_pkey; Type: CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedparty
    ADD CONSTRAINT involvedparty_pkey PRIMARY KEY (involvedpartyid, warehousecreateddate);


--
-- TOC entry 4138 (class 2606 OID 205775)
-- Name: involvedpartyidentificationtype involvedpartyidentificationtype_pkey; Type: CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyidentificationtype
    ADD CONSTRAINT involvedpartyidentificationtype_pkey PRIMARY KEY (involvedpartyidentificationtypeid, warehousecreateddate);


--
-- TOC entry 4140 (class 2606 OID 205777)
-- Name: involvedpartyidentificationtypesecuritytoken involvedpartyidentificationtypesecuritytoken_pkey; Type: CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyidentificationtypesecuritytoken
    ADD CONSTRAINT involvedpartyidentificationtypesecuritytoken_pkey PRIMARY KEY (involvedpartyidentificationtypesecuritytokenid, warehousecreateddate);


--
-- TOC entry 4142 (class 2606 OID 205779)
-- Name: involvedpartynametype involvedpartynametype_pkey; Type: CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartynametype
    ADD CONSTRAINT involvedpartynametype_pkey PRIMARY KEY (involvedpartynametypeid, warehousecreateddate);


--
-- TOC entry 4144 (class 2606 OID 205781)
-- Name: involvedpartynametypesecuritytoken involvedpartynametypesecuritytoken_pkey; Type: CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartynametypesecuritytoken
    ADD CONSTRAINT involvedpartynametypesecuritytoken_pkey PRIMARY KEY (involvedpartynametypesecuritytokenid, warehousecreateddate);


--
-- TOC entry 4146 (class 2606 OID 205783)
-- Name: involvedpartynonorganic involvedpartynonorganic_pkey; Type: CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartynonorganic
    ADD CONSTRAINT involvedpartynonorganic_pkey PRIMARY KEY (involvedpartynonorganicid, warehousecreateddate);


--
-- TOC entry 4148 (class 2606 OID 205785)
-- Name: involvedpartynonorganicsecuritytoken involvedpartynonorganicsecuritytoken_pkey; Type: CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartynonorganicsecuritytoken
    ADD CONSTRAINT involvedpartynonorganicsecuritytoken_pkey PRIMARY KEY (involvedpartynonorganicsecuritytokenid, warehousecreateddate);


--
-- TOC entry 4150 (class 2606 OID 205787)
-- Name: involvedpartyorganic involvedpartyorganic_pkey; Type: CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyorganic
    ADD CONSTRAINT involvedpartyorganic_pkey PRIMARY KEY (involvedpartyorganicid, warehousecreateddate);


--
-- TOC entry 4152 (class 2606 OID 205789)
-- Name: involvedpartyorganicsecuritytoken involvedpartyorganicsecuritytoken_pkey; Type: CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyorganicsecuritytoken
    ADD CONSTRAINT involvedpartyorganicsecuritytoken_pkey PRIMARY KEY (involvedpartyorganicsecuritytokenid, warehousecreateddate);


--
-- TOC entry 4154 (class 2606 OID 205791)
-- Name: involvedpartyorganictype involvedpartyorganictype_pkey; Type: CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyorganictype
    ADD CONSTRAINT involvedpartyorganictype_pkey PRIMARY KEY (involvedpartyorganictypeid, warehousecreateddate);


--
-- TOC entry 4156 (class 2606 OID 205793)
-- Name: involvedpartyorganictypesecuritytoken involvedpartyorganictypesecuritytoken_pkey; Type: CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyorganictypesecuritytoken
    ADD CONSTRAINT involvedpartyorganictypesecuritytoken_pkey PRIMARY KEY (involvedpartyorganictypesecuritytokenid, warehousecreateddate);


--
-- TOC entry 4158 (class 2606 OID 205795)
-- Name: involvedpartysecuritytoken involvedpartysecuritytoken_pkey; Type: CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartysecuritytoken
    ADD CONSTRAINT involvedpartysecuritytoken_pkey PRIMARY KEY (involvedpartysecuritytokenid, warehousecreateddate);


--
-- TOC entry 4160 (class 2606 OID 205797)
-- Name: involvedpartytype involvedpartytype_pkey; Type: CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartytype
    ADD CONSTRAINT involvedpartytype_pkey PRIMARY KEY (involvedpartytypeid, warehousecreateddate);


--
-- TOC entry 4162 (class 2606 OID 205799)
-- Name: involvedpartytypesecuritytoken involvedpartytypesecuritytoken_pkey; Type: CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartytypesecuritytoken
    ADD CONSTRAINT involvedpartytypesecuritytoken_pkey PRIMARY KEY (involvedpartytypesecuritytokenid, warehousecreateddate);


--
-- TOC entry 4164 (class 2606 OID 205801)
-- Name: involvedpartyxaddress involvedpartyxaddress_pkey; Type: CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyxaddress
    ADD CONSTRAINT involvedpartyxaddress_pkey PRIMARY KEY (involvedpartyxaddressid, warehousecreateddate);


--
-- TOC entry 4166 (class 2606 OID 205803)
-- Name: involvedpartyxaddresssecuritytoken involvedpartyxaddresssecuritytoken_pkey; Type: CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyxaddresssecuritytoken
    ADD CONSTRAINT involvedpartyxaddresssecuritytoken_pkey PRIMARY KEY (involvedpartyxaddresssecuritytokenid, warehousecreateddate);


--
-- TOC entry 4168 (class 2606 OID 205805)
-- Name: involvedpartyxclassification involvedpartyxclassification_pkey; Type: CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyxclassification
    ADD CONSTRAINT involvedpartyxclassification_pkey PRIMARY KEY (involvedpartyxclassificationid, warehousecreateddate);


--
-- TOC entry 4170 (class 2606 OID 205807)
-- Name: involvedpartyxclassificationsecuritytoken involvedpartyxclassificationsecuritytoken_pkey; Type: CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyxclassificationsecuritytoken
    ADD CONSTRAINT involvedpartyxclassificationsecuritytoken_pkey PRIMARY KEY (involvedpartyxclassificationsecuritytokenid, warehousecreateddate);


--
-- TOC entry 4172 (class 2606 OID 205809)
-- Name: involvedpartyxinvolvedparty involvedpartyxinvolvedparty_pkey; Type: CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyxinvolvedparty
    ADD CONSTRAINT involvedpartyxinvolvedparty_pkey PRIMARY KEY (involvedpartyxinvolvedpartyid, warehousecreateddate);


--
-- TOC entry 4174 (class 2606 OID 205811)
-- Name: involvedpartyxinvolvedpartyidentificationtype involvedpartyxinvolvedpartyidentificationtype_pkey; Type: CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyxinvolvedpartyidentificationtype
    ADD CONSTRAINT involvedpartyxinvolvedpartyidentificationtype_pkey PRIMARY KEY (involvedpartyxinvolvedpartyidentificationtypeid, warehousecreateddate);


--
-- TOC entry 4176 (class 2606 OID 205813)
-- Name: involvedpartyxinvolvedpartyidentificationtypesecuritytoken involvedpartyxinvolvedpartyidentificationtypesecuritytoken_pkey; Type: CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyxinvolvedpartyidentificationtypesecuritytoken
    ADD CONSTRAINT involvedpartyxinvolvedpartyidentificationtypesecuritytoken_pkey PRIMARY KEY (involvedpartyxinvolvedpartyidentificationtypesecuritytokenid,
                                                                                                warehousecreateddate);


--
-- TOC entry 4178 (class 2606 OID 205815)
-- Name: involvedpartyxinvolvedpartynametype involvedpartyxinvolvedpartynametype_pkey; Type: CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyxinvolvedpartynametype
    ADD CONSTRAINT involvedpartyxinvolvedpartynametype_pkey PRIMARY KEY (involvedpartyxinvolvedpartynametypeid, warehousecreateddate);


--
-- TOC entry 4180 (class 2606 OID 205817)
-- Name: involvedpartyxinvolvedpartynametypesecuritytoken involvedpartyxinvolvedpartynametypesecuritytoken_pkey; Type: CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyxinvolvedpartynametypesecuritytoken
    ADD CONSTRAINT involvedpartyxinvolvedpartynametypesecuritytoken_pkey PRIMARY KEY (involvedpartyxinvolvedpartynametypesecuritytokenid,
                                                                                      warehousecreateddate);


--
-- TOC entry 4182 (class 2606 OID 205819)
-- Name: involvedpartyxinvolvedpartysecuritytoken involvedpartyxinvolvedpartysecuritytoken_pkey; Type: CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyxinvolvedpartysecuritytoken
    ADD CONSTRAINT involvedpartyxinvolvedpartysecuritytoken_pkey PRIMARY KEY (involvedpartyxinvolvedpartysecuritytokenid, warehousecreateddate);


--
-- TOC entry 4184 (class 2606 OID 205821)
-- Name: involvedpartyxinvolvedpartytype involvedpartyxinvolvedpartytype_pkey; Type: CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyxinvolvedpartytype
    ADD CONSTRAINT involvedpartyxinvolvedpartytype_pkey PRIMARY KEY (involvedpartyxinvolvedpartytypeid, warehousecreateddate);


--
-- TOC entry 4186 (class 2606 OID 205823)
-- Name: involvedpartyxinvolvedpartytypesecuritytoken involvedpartyxinvolvedpartytypesecuritytoken_pkey; Type: CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyxinvolvedpartytypesecuritytoken
    ADD CONSTRAINT involvedpartyxinvolvedpartytypesecuritytoken_pkey PRIMARY KEY (involvedpartyxinvolvedpartytypesecuritytokenid, warehousecreateddate);


--
-- TOC entry 4188 (class 2606 OID 205825)
-- Name: involvedpartyxproduct involvedpartyxproduct_pkey; Type: CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyxproduct
    ADD CONSTRAINT involvedpartyxproduct_pkey PRIMARY KEY (involvedpartyxproductid, warehousecreateddate);


--
-- TOC entry 4190 (class 2606 OID 205827)
-- Name: involvedpartyxproductsecuritytoken involvedpartyxproductsecuritytoken_pkey; Type: CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyxproductsecuritytoken
    ADD CONSTRAINT involvedpartyxproductsecuritytoken_pkey PRIMARY KEY (involvedpartyxproductsecuritytokenid, warehousecreateddate);


--
-- TOC entry 4192 (class 2606 OID 205829)
-- Name: involvedpartyxproducttype involvedpartyxproducttype_pkey; Type: CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyxproducttype
    ADD CONSTRAINT involvedpartyxproducttype_pkey PRIMARY KEY (involvedpartyxproducttypeid, warehousecreateddate);


--
-- TOC entry 4194 (class 2606 OID 205831)
-- Name: involvedpartyxproducttypesecuritytoken involvedpartyxproducttypesecuritytoken_pkey; Type: CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyxproducttypesecuritytoken
    ADD CONSTRAINT involvedpartyxproducttypesecuritytoken_pkey PRIMARY KEY (involvedpartyxproducttypesecuritytokenid, warehousecreateddate);


--
-- TOC entry 4196 (class 2606 OID 205833)
-- Name: involvedpartyxresourceitem involvedpartyxresourceitem_pkey; Type: CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyxresourceitem
    ADD CONSTRAINT involvedpartyxresourceitem_pkey PRIMARY KEY (involvedpartyxresourceitemid, warehousecreateddate);


--
-- TOC entry 4198 (class 2606 OID 205835)
-- Name: involvedpartyxresourceitemsecuritytoken involvedpartyxresourceitemsecuritytoken_pkey; Type: CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyxresourceitemsecuritytoken
    ADD CONSTRAINT involvedpartyxresourceitemsecuritytoken_pkey PRIMARY KEY (involvedpartyxresourceitemsecuritytokenid, warehousecreateddate);


--
-- TOC entry 4200 (class 2606 OID 205837)
-- Name: involvedpartyxrules involvedpartyxrules_pkey; Type: CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyxrules
    ADD CONSTRAINT involvedpartyxrules_pkey PRIMARY KEY (involvedpartyxrulesid, warehousecreateddate);


--
-- TOC entry 4202 (class 2606 OID 205839)
-- Name: involvedpartyxrulessecuritytoken involvedpartyxrulessecuritytoken_pkey; Type: CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyxrulessecuritytoken
    ADD CONSTRAINT involvedpartyxrulessecuritytoken_pkey PRIMARY KEY (involvedpartyxrulessecuritytokenid, warehousecreateddate);


--
-- TOC entry 4204 (class 2606 OID 205841)
-- Name: product product_pkey; Type: CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE product.product
    ADD CONSTRAINT product_pkey PRIMARY KEY (productid, warehousecreateddate);


--
-- TOC entry 4206 (class 2606 OID 205843)
-- Name: productsecuritytoken productsecuritytoken_pkey; Type: CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE product.productsecuritytoken
    ADD CONSTRAINT productsecuritytoken_pkey PRIMARY KEY (productsecuritytokenid, warehousecreateddate);


--
-- TOC entry 4208 (class 2606 OID 205845)
-- Name: producttype producttype_pkey; Type: CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE product.producttype
    ADD CONSTRAINT producttype_pkey PRIMARY KEY (producttypeid, warehousecreateddate);


--
-- TOC entry 4210 (class 2606 OID 205847)
-- Name: producttypessecuritytoken producttypessecuritytoken_pkey; Type: CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE product.producttypessecuritytoken
    ADD CONSTRAINT producttypessecuritytoken_pkey PRIMARY KEY (producttypessecuritytokenid, warehousecreateddate);


--
-- TOC entry 4212 (class 2606 OID 205849)
-- Name: producttypexclassification producttypexclassification_pkey; Type: CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE product.producttypexclassification
    ADD CONSTRAINT producttypexclassification_pkey PRIMARY KEY (producttypexclassificationid, warehousecreateddate);


--
-- TOC entry 4214 (class 2606 OID 205851)
-- Name: producttypexclassificationsecuritytoken producttypexclassificationsecuritytoken_pkey; Type: CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE product.producttypexclassificationsecuritytoken
    ADD CONSTRAINT producttypexclassificationsecuritytoken_pkey PRIMARY KEY (producttypexclassificationsecuritytokenid, warehousecreateddate);


--
-- TOC entry 4216 (class 2606 OID 205853)
-- Name: productxclassification productxclassification_pkey; Type: CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE product.productxclassification
    ADD CONSTRAINT productxclassification_pkey PRIMARY KEY (productxclassificationid, warehousecreateddate);


--
-- TOC entry 4218 (class 2606 OID 205855)
-- Name: productxclassificationsecuritytoken productxclassificationsecuritytoken_pkey; Type: CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE product.productxclassificationsecuritytoken
    ADD CONSTRAINT productxclassificationsecuritytoken_pkey PRIMARY KEY (productxclassificationsecuritytokenid, warehousecreateddate);


--
-- TOC entry 4220 (class 2606 OID 205857)
-- Name: productxproduct productxproduct_pkey; Type: CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE product.productxproduct
    ADD CONSTRAINT productxproduct_pkey PRIMARY KEY (productxproductid, warehousecreateddate);


--
-- TOC entry 4222 (class 2606 OID 205859)
-- Name: productxproductsecuritytoken productxproductsecuritytoken_pkey; Type: CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE product.productxproductsecuritytoken
    ADD CONSTRAINT productxproductsecuritytoken_pkey PRIMARY KEY (productxproductsecuritytokenid, warehousecreateddate);


--
-- TOC entry 4224 (class 2606 OID 205861)
-- Name: productxproducttype productxproducttype_pkey; Type: CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE product.productxproducttype
    ADD CONSTRAINT productxproducttype_pkey PRIMARY KEY (productxproducttypeid, warehousecreateddate);


--
-- TOC entry 4226 (class 2606 OID 205863)
-- Name: productxproducttypesecuritytoken productxproducttypesecuritytoken_pkey; Type: CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE product.productxproducttypesecuritytoken
    ADD CONSTRAINT productxproducttypesecuritytoken_pkey PRIMARY KEY (productxproducttypesecuritytokenid, warehousecreateddate);


--
-- TOC entry 4228 (class 2606 OID 205865)
-- Name: productxresourceitem productxresourceitem_pkey; Type: CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE product.productxresourceitem
    ADD CONSTRAINT productxresourceitem_pkey PRIMARY KEY (productxresourceitemid, warehousecreateddate);


--
-- TOC entry 4230 (class 2606 OID 205867)
-- Name: productxresourceitemsecuritytoken productxresourceitemsecuritytoken_pkey; Type: CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE product.productxresourceitemsecuritytoken
    ADD CONSTRAINT productxresourceitemsecuritytoken_pkey PRIMARY KEY (productxresourceitemsecuritytokenid, warehousecreateddate);


--
-- TOC entry 4232 (class 2606 OID 205869)
-- Name: arrangementhierarchyview arrangementhierarchyview_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE public.arrangementhierarchyview
    ADD CONSTRAINT arrangementhierarchyview_pkey PRIMARY KEY (id);


--
-- TOC entry 4234 (class 2606 OID 205871)
-- Name: classificationhierarchyview classificationhierarchyview_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE public.classificationhierarchyview
    ADD CONSTRAINT classificationhierarchyview_pkey PRIMARY KEY (id);


--
-- TOC entry 4236 (class 2606 OID 205873)
-- Name: geographyhierarchyview geographyhierarchyview_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE public.geographyhierarchyview
    ADD CONSTRAINT geographyhierarchyview_pkey PRIMARY KEY (id);


--
-- TOC entry 4238 (class 2606 OID 205875)
-- Name: producthierarchyview producthierarchyview_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE public.producthierarchyview
    ADD CONSTRAINT producthierarchyview_pkey PRIMARY KEY (id);


--
-- TOC entry 4242 (class 2606 OID 205877)
-- Name: resourceitemhierarchyview resourceitemhierarchyview_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE public.resourceitemhierarchyview
    ADD CONSTRAINT resourceitemhierarchyview_pkey PRIMARY KEY (id);


--
-- TOC entry 4244 (class 2606 OID 205879)
-- Name: ruleshierarchyview ruleshierarchyview_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE public.ruleshierarchyview
    ADD CONSTRAINT ruleshierarchyview_pkey PRIMARY KEY (id);


--
-- TOC entry 4246 (class 2606 OID 205881)
-- Name: securityhierarchyparents securityhierarchyparents_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE public.securityhierarchyparents
    ADD CONSTRAINT securityhierarchyparents_pkey PRIMARY KEY (id);


--
-- TOC entry 4240 (class 2606 OID 205883)
-- Name: quarters_months uk_snpn7ecm4aaxya8sdplfi575j; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE public.quarters_months
    ADD CONSTRAINT uk_snpn7ecm4aaxya8sdplfi575j UNIQUE (lumonthslist_monthid);


--
-- TOC entry 4248 (class 2606 OID 205885)
-- Name: resourceitem resourceitem_pkey; Type: CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE resource.resourceitem
    ADD CONSTRAINT resourceitem_pkey PRIMARY KEY (resourceitemid, warehousecreateddate);


--
-- TOC entry 4250 (class 2606 OID 205887)
-- Name: resourceitemdata resourceitemdata_pkey; Type: CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE resource.resourceitemdata
    ADD CONSTRAINT resourceitemdata_pkey PRIMARY KEY (resourceitemdataid, warehousecreateddate);


--
-- TOC entry 4252 (class 2606 OID 205889)
-- Name: resourceitemdatasecuritytoken resourceitemdatasecuritytoken_pkey; Type: CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE resource.resourceitemdatasecuritytoken
    ADD CONSTRAINT resourceitemdatasecuritytoken_pkey PRIMARY KEY (resourceitemdatasecuritytokenid, warehousecreateddate);


--
-- TOC entry 4254 (class 2606 OID 205891)
-- Name: resourceitemdataxclassification resourceitemdataxclassification_pkey; Type: CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE resource.resourceitemdataxclassification
    ADD CONSTRAINT resourceitemdataxclassification_pkey PRIMARY KEY (resourceitemdataxclassificationid, warehousecreateddate);


--
-- TOC entry 4256 (class 2606 OID 205893)
-- Name: resourceitemdataxclassificationsecuritytoken resourceitemdataxclassificationsecuritytoken_pkey; Type: CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE resource.resourceitemdataxclassificationsecuritytoken
    ADD CONSTRAINT resourceitemdataxclassificationsecuritytoken_pkey PRIMARY KEY (resourceitemdataxclassificationsecuritytokenid, warehousecreateddate);


--
-- TOC entry 4258 (class 2606 OID 205895)
-- Name: resourceitemsecuritytoken resourceitemsecuritytoken_pkey; Type: CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE resource.resourceitemsecuritytoken
    ADD CONSTRAINT resourceitemsecuritytoken_pkey PRIMARY KEY (resourceitemsecuritytokenid, warehousecreateddate);


--
-- TOC entry 4260 (class 2606 OID 205897)
-- Name: resourceitemtype resourceitemtype_pkey; Type: CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE resource.resourceitemtype
    ADD CONSTRAINT resourceitemtype_pkey PRIMARY KEY (resourceitemtypeid, warehousecreateddate);


--
-- TOC entry 4262 (class 2606 OID 205899)
-- Name: resourceitemtypesecuritytoken resourceitemtypesecuritytoken_pkey; Type: CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE resource.resourceitemtypesecuritytoken
    ADD CONSTRAINT resourceitemtypesecuritytoken_pkey PRIMARY KEY (resourceitemtypesecuritytokenid, warehousecreateddate);


--
-- TOC entry 4264 (class 2606 OID 205901)
-- Name: resourceitemxclassification resourceitemxclassification_pkey; Type: CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE resource.resourceitemxclassification
    ADD CONSTRAINT resourceitemxclassification_pkey PRIMARY KEY (resourceitemxclassificationid, warehousecreateddate);


--
-- TOC entry 4266 (class 2606 OID 205903)
-- Name: resourceitemxclassificationsecuritytoken resourceitemxclassificationsecuritytoken_pkey; Type: CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE resource.resourceitemxclassificationsecuritytoken
    ADD CONSTRAINT resourceitemxclassificationsecuritytoken_pkey PRIMARY KEY (resourceitemxclassificationsecuritytokenid, warehousecreateddate);


--
-- TOC entry 4268 (class 2606 OID 205905)
-- Name: resourceitemxresourceitem resourceitemxresourceitem_pkey; Type: CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE resource.resourceitemxresourceitem
    ADD CONSTRAINT resourceitemxresourceitem_pkey PRIMARY KEY (resourceitemxresourceitemid, warehousecreateddate);


--
-- TOC entry 4270 (class 2606 OID 205907)
-- Name: resourceitemxresourceitemsecuritytoken resourceitemxresourceitemsecuritytoken_pkey; Type: CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE resource.resourceitemxresourceitemsecuritytoken
    ADD CONSTRAINT resourceitemxresourceitemsecuritytoken_pkey PRIMARY KEY (resourceitemxresourceitemsecuritytokenid, warehousecreateddate);


--
-- TOC entry 4272 (class 2606 OID 205909)
-- Name: resourceitemxresourceitemtype resourceitemxresourceitemtype_pkey; Type: CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE resource.resourceitemxresourceitemtype
    ADD CONSTRAINT resourceitemxresourceitemtype_pkey PRIMARY KEY (resourceitemxresourceitemtypeid, warehousecreateddate);


--
-- TOC entry 4274 (class 2606 OID 205911)
-- Name: resourceitemxresourceitemtypesecuritytoken resourceitemxresourceitemtypesecuritytoken_pkey; Type: CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE resource.resourceitemxresourceitemtypesecuritytoken
    ADD CONSTRAINT resourceitemxresourceitemtypesecuritytoken_pkey PRIMARY KEY (resourceitemxresourceitemtypesecuritytokenid, warehousecreateddate);


--
-- TOC entry 4276 (class 2606 OID 205913)
-- Name: rules rules_pkey; Type: CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE rules.rules
    ADD CONSTRAINT rules_pkey PRIMARY KEY (rulesid, warehousecreateddate);


--
-- TOC entry 4278 (class 2606 OID 205915)
-- Name: rulessecuritytoken rulessecuritytoken_pkey; Type: CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE rules.rulessecuritytoken
    ADD CONSTRAINT rulessecuritytoken_pkey PRIMARY KEY (rulessecuritytokenid, warehousecreateddate);


--
-- TOC entry 4280 (class 2606 OID 205917)
-- Name: rulestype rulestype_pkey; Type: CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE rules.rulestype
    ADD CONSTRAINT rulestype_pkey PRIMARY KEY (rulestypeid, warehousecreateddate);


--
-- TOC entry 4282 (class 2606 OID 205919)
-- Name: rulestypessecuritytoken rulestypessecuritytoken_pkey; Type: CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE rules.rulestypessecuritytoken
    ADD CONSTRAINT rulestypessecuritytoken_pkey PRIMARY KEY (rulestypessecuritytokenid, warehousecreateddate);


--
-- TOC entry 4284 (class 2606 OID 205921)
-- Name: rulestypexclassification rulestypexclassification_pkey; Type: CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE rules.rulestypexclassification
    ADD CONSTRAINT rulestypexclassification_pkey PRIMARY KEY (rulestypexclassificationid, warehousecreateddate);


--
-- TOC entry 4286 (class 2606 OID 205923)
-- Name: rulestypexclassificationsecuritytoken rulestypexclassificationsecuritytoken_pkey; Type: CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE rules.rulestypexclassificationsecuritytoken
    ADD CONSTRAINT rulestypexclassificationsecuritytoken_pkey PRIMARY KEY (rulestypexclassificationsecuritytokenid, warehousecreateddate);


--
-- TOC entry 4288 (class 2606 OID 205925)
-- Name: rulestypexresourceitem rulestypexresourceitem_pkey; Type: CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE rules.rulestypexresourceitem
    ADD CONSTRAINT rulestypexresourceitem_pkey PRIMARY KEY (rulestypexresourceitemid, warehousecreateddate);


--
-- TOC entry 4290 (class 2606 OID 205927)
-- Name: rulestypexresourceitemsecuritytoken rulestypexresourceitemsecuritytoken_pkey; Type: CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE rules.rulestypexresourceitemsecuritytoken
    ADD CONSTRAINT rulestypexresourceitemsecuritytoken_pkey PRIMARY KEY (rulestypexresourceitemsecuritytokenid, warehousecreateddate);


--
-- TOC entry 4292 (class 2606 OID 205929)
-- Name: rulesxarrangement rulesxarrangement_pkey; Type: CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE rules.rulesxarrangement
    ADD CONSTRAINT rulesxarrangement_pkey PRIMARY KEY (rulesxarrangementsid, warehousecreateddate);


--
-- TOC entry 4294 (class 2606 OID 205931)
-- Name: rulesxarrangementssecuritytoken rulesxarrangementssecuritytoken_pkey; Type: CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE rules.rulesxarrangementssecuritytoken
    ADD CONSTRAINT rulesxarrangementssecuritytoken_pkey PRIMARY KEY (rulesxarrangementssecuritytokenid, warehousecreateddate);


--
-- TOC entry 4296 (class 2606 OID 205933)
-- Name: rulesxclassification rulesxclassification_pkey; Type: CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE rules.rulesxclassification
    ADD CONSTRAINT rulesxclassification_pkey PRIMARY KEY (rulesxclassificationid, warehousecreateddate);


--
-- TOC entry 4298 (class 2606 OID 205935)
-- Name: rulesxclassificationsecuritytoken rulesxclassificationsecuritytoken_pkey; Type: CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE rules.rulesxclassificationsecuritytoken
    ADD CONSTRAINT rulesxclassificationsecuritytoken_pkey PRIMARY KEY (rulesxclassificationsecuritytokenid, warehousecreateddate);


--
-- TOC entry 4300 (class 2606 OID 205937)
-- Name: rulesxinvolvedparty rulesxinvolvedparty_pkey; Type: CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE rules.rulesxinvolvedparty
    ADD CONSTRAINT rulesxinvolvedparty_pkey PRIMARY KEY (rulesxinvolvedpartyid, warehousecreateddate);


--
-- TOC entry 4302 (class 2606 OID 205939)
-- Name: rulesxinvolvedpartysecuritytoken rulesxinvolvedpartysecuritytoken_pkey; Type: CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE rules.rulesxinvolvedpartysecuritytoken
    ADD CONSTRAINT rulesxinvolvedpartysecuritytoken_pkey PRIMARY KEY (rulesxinvolvedpartysecuritytokenid, warehousecreateddate);


--
-- TOC entry 4304 (class 2606 OID 205941)
-- Name: rulesxproduct rulesxproduct_pkey; Type: CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE rules.rulesxproduct
    ADD CONSTRAINT rulesxproduct_pkey PRIMARY KEY (rulesxproductid, warehousecreateddate);


--
-- TOC entry 4306 (class 2606 OID 205943)
-- Name: rulesxproductsecuritytoken rulesxproductsecuritytoken_pkey; Type: CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE rules.rulesxproductsecuritytoken
    ADD CONSTRAINT rulesxproductsecuritytoken_pkey PRIMARY KEY (rulesxproductsecuritytokenid, warehousecreateddate);


--
-- TOC entry 4308 (class 2606 OID 205945)
-- Name: rulesxresourceitem rulesxresourceitem_pkey; Type: CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE rules.rulesxresourceitem
    ADD CONSTRAINT rulesxresourceitem_pkey PRIMARY KEY (rulesxresourceitemid, warehousecreateddate);


--
-- TOC entry 4310 (class 2606 OID 205947)
-- Name: rulesxresourceitemsecuritytoken rulesxresourceitemsecuritytoken_pkey; Type: CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE rules.rulesxresourceitemsecuritytoken
    ADD CONSTRAINT rulesxresourceitemsecuritytoken_pkey PRIMARY KEY (rulesxresourceitemsecuritytokenid, warehousecreateddate);


--
-- TOC entry 4312 (class 2606 OID 205949)
-- Name: rulesxrules rulesxrules_pkey; Type: CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE rules.rulesxrules
    ADD CONSTRAINT rulesxrules_pkey PRIMARY KEY (rulesxrulesid, warehousecreateddate);


--
-- TOC entry 4314 (class 2606 OID 205951)
-- Name: rulesxrulessecuritytoken rulesxrulessecuritytoken_pkey; Type: CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE rules.rulesxrulessecuritytoken
    ADD CONSTRAINT rulesxrulessecuritytoken_pkey PRIMARY KEY (rulesxrulessecuritytokenid, warehousecreateddate);


--
-- TOC entry 4316 (class 2606 OID 205953)
-- Name: rulesxrulestype rulesxrulestype_pkey; Type: CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE rules.rulesxrulestype
    ADD CONSTRAINT rulesxrulestype_pkey PRIMARY KEY (rulesxrulestypeid, warehousecreateddate);


--
-- TOC entry 4318 (class 2606 OID 205955)
-- Name: rulesxrulestypesecuritytoken rulesxrulestypesecuritytoken_pkey; Type: CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE rules.rulesxrulestypesecuritytoken
    ADD CONSTRAINT rulesxrulestypesecuritytoken_pkey PRIMARY KEY (rulesxrulestypesecuritytokenid, warehousecreateddate);


--
-- TOC entry 4320 (class 2606 OID 205957)
-- Name: securityhierarchy securityhierarchy_pkey; Type: CONSTRAINT; Schema: security; Owner: postgres
--

ALTER TABLE security.securityhierarchy
    ADD CONSTRAINT securityhierarchy_pkey PRIMARY KEY (id);


--
-- TOC entry 4322 (class 2606 OID 205959)
-- Name: securitytoken securitytoken_pkey; Type: CONSTRAINT; Schema: security; Owner: postgres
--

ALTER TABLE security.securitytoken
    ADD CONSTRAINT securitytoken_pkey PRIMARY KEY (securitytokenid, warehousecreateddate);


--
-- TOC entry 4324 (class 2606 OID 205961)
-- Name: securitytokenssecuritytoken securitytokenssecuritytoken_pkey; Type: CONSTRAINT; Schema: security; Owner: postgres
--

ALTER TABLE security.securitytokenssecuritytoken
    ADD CONSTRAINT securitytokenssecuritytoken_pkey PRIMARY KEY (securitytokenaccessid, warehousecreateddate);


--
-- TOC entry 4326 (class 2606 OID 205963)
-- Name: securitytokensxsecuritytokensecuritytoken securitytokensxsecuritytokensecuritytoken_pkey; Type: CONSTRAINT; Schema: security; Owner: postgres
--

ALTER TABLE security.securitytokensxsecuritytokensecuritytoken
    ADD CONSTRAINT securitytokensxsecuritytokensecuritytoken_pkey PRIMARY KEY (securitytokenxsecuritytokensecuritytokenid, warehousecreateddate);


--
-- TOC entry 4328 (class 2606 OID 205965)
-- Name: securitytokenxclassification securitytokenxclassification_pkey; Type: CONSTRAINT; Schema: security; Owner: postgres
--

ALTER TABLE security.securitytokenxclassification
    ADD CONSTRAINT securitytokenxclassification_pkey PRIMARY KEY (securitytokenxclassificationid, warehousecreateddate);


--
-- TOC entry 4330 (class 2606 OID 205967)
-- Name: securitytokenxclassificationsecuritytoken securitytokenxclassificationsecuritytoken_pkey; Type: CONSTRAINT; Schema: security; Owner: postgres
--

ALTER TABLE security.securitytokenxclassificationsecuritytoken
    ADD CONSTRAINT securitytokenxclassificationsecuritytoken_pkey PRIMARY KEY (securitytokenxclassificationsecuritytokenid, warehousecreateddate);


--
-- TOC entry 4332 (class 2606 OID 205969)
-- Name: securitytokenxsecuritytoken securitytokenxsecuritytoken_pkey; Type: CONSTRAINT; Schema: security; Owner: postgres
--

ALTER TABLE security.securitytokenxsecuritytoken
    ADD CONSTRAINT securitytokenxsecuritytoken_pkey PRIMARY KEY (securitytokenxsecuritytokenid, warehousecreateddate);


--
-- TOC entry 4334 (class 2606 OID 205971)
-- Name: daynames daynames_pkey; Type: CONSTRAINT; Schema: time; Owner: postgres
--

ALTER TABLE "time".daynames
    ADD CONSTRAINT daynames_pkey PRIMARY KEY (daynameid);


--
-- TOC entry 4336 (class 2606 OID 205973)
-- Name: dayparts dayparts_pkey; Type: CONSTRAINT; Schema: time; Owner: postgres
--

ALTER TABLE "time".dayparts
    ADD CONSTRAINT dayparts_pkey PRIMARY KEY (daypartid);


--
-- TOC entry 4338 (class 2606 OID 205975)
-- Name: days days_pkey; Type: CONSTRAINT; Schema: time; Owner: postgres
--

ALTER TABLE "time".days
    ADD CONSTRAINT days_pkey PRIMARY KEY (dayid);


--
-- TOC entry 4340 (class 2606 OID 205977)
-- Name: halfhourdayparts halfhourdayparts_pkey; Type: CONSTRAINT; Schema: time; Owner: postgres
--

ALTER TABLE "time".halfhourdayparts
    ADD CONSTRAINT halfhourdayparts_pkey PRIMARY KEY (halfhourdaypartid);


--
-- TOC entry 4344 (class 2606 OID 205979)
-- Name: halfhours halfhours_pkey; Type: CONSTRAINT; Schema: time; Owner: postgres
--

ALTER TABLE "time".halfhours
    ADD CONSTRAINT halfhours_pkey PRIMARY KEY (hourid, minuteid);


--
-- TOC entry 4346 (class 2606 OID 205981)
-- Name: hours hours_pkey; Type: CONSTRAINT; Schema: time; Owner: postgres
--

ALTER TABLE "time".hours
    ADD CONSTRAINT hours_pkey PRIMARY KEY (hourid);


--
-- TOC entry 4348 (class 2606 OID 205983)
-- Name: monthofyear monthofyear_pkey; Type: CONSTRAINT; Schema: time; Owner: postgres
--

ALTER TABLE "time".monthofyear
    ADD CONSTRAINT monthofyear_pkey PRIMARY KEY (monthofyearid);


--
-- TOC entry 4350 (class 2606 OID 205985)
-- Name: months months_pkey; Type: CONSTRAINT; Schema: time; Owner: postgres
--

ALTER TABLE "time".months
    ADD CONSTRAINT months_pkey PRIMARY KEY (monthid);


--
-- TOC entry 4352 (class 2606 OID 205987)
-- Name: publicholidays publicholidays_pkey; Type: CONSTRAINT; Schema: time; Owner: postgres
--

ALTER TABLE "time".publicholidays
    ADD CONSTRAINT publicholidays_pkey PRIMARY KEY (publicholidayid);


--
-- TOC entry 4354 (class 2606 OID 205989)
-- Name: quarters quarters_pkey; Type: CONSTRAINT; Schema: time; Owner: postgres
--

ALTER TABLE "time".quarters
    ADD CONSTRAINT quarters_pkey PRIMARY KEY (quarterid);


--
-- TOC entry 4356 (class 2606 OID 205991)
-- Name: time time_pkey; Type: CONSTRAINT; Schema: time; Owner: postgres
--

ALTER TABLE "time"."time"
    ADD CONSTRAINT time_pkey PRIMARY KEY (hourid, minuteid);


--
-- TOC entry 4358 (class 2606 OID 205993)
-- Name: trans_fiscal trans_fiscal_pkey; Type: CONSTRAINT; Schema: time; Owner: postgres
--

ALTER TABLE "time".trans_fiscal
    ADD CONSTRAINT trans_fiscal_pkey PRIMARY KEY (dayid);


--
-- TOC entry 4360 (class 2606 OID 205995)
-- Name: trans_mtd trans_mtd_pkey; Type: CONSTRAINT; Schema: time; Owner: postgres
--

ALTER TABLE "time".trans_mtd
    ADD CONSTRAINT trans_mtd_pkey PRIMARY KEY (dayid, mtddayid);


--
-- TOC entry 4362 (class 2606 OID 205997)
-- Name: trans_qtd trans_qtd_pkey; Type: CONSTRAINT; Schema: time; Owner: postgres
--

ALTER TABLE "time".trans_qtd
    ADD CONSTRAINT trans_qtd_pkey PRIMARY KEY (dayid, qtddayid);


--
-- TOC entry 4364 (class 2606 OID 205999)
-- Name: trans_qtm trans_qtm_pkey; Type: CONSTRAINT; Schema: time; Owner: postgres
--

ALTER TABLE "time".trans_qtm
    ADD CONSTRAINT trans_qtm_pkey PRIMARY KEY (monthid, qtm_monthid);


--
-- TOC entry 4366 (class 2606 OID 206001)
-- Name: trans_ytd trans_ytd_pkey; Type: CONSTRAINT; Schema: time; Owner: postgres
--

ALTER TABLE "time".trans_ytd
    ADD CONSTRAINT trans_ytd_pkey PRIMARY KEY (dayid, ytddayid);


--
-- TOC entry 4342 (class 2606 OID 206003)
-- Name: halfhourdayparts uked29i3cg4n4acdg569otn4r08; Type: CONSTRAINT; Schema: time; Owner: postgres
--

ALTER TABLE "time".halfhourdayparts
    ADD CONSTRAINT uked29i3cg4n4acdg569otn4r08 UNIQUE (hourid, minuteid);


--
-- TOC entry 4368 (class 2606 OID 206005)
-- Name: weeks weeks_pkey; Type: CONSTRAINT; Schema: time; Owner: postgres
--

ALTER TABLE "time".weeks
    ADD CONSTRAINT weeks_pkey PRIMARY KEY (weekid);


--
-- TOC entry 4370 (class 2606 OID 206007)
-- Name: years years_pkey; Type: CONSTRAINT; Schema: time; Owner: postgres
--

ALTER TABLE "time".years
    ADD CONSTRAINT years_pkey PRIMARY KEY (yearid);


--
-- TOC entry 4426 (class 2606 OID 206008)
-- Name: addressxclassification fk10gn5jpbgrhhp4e99c6i8p9ps; Type: FK CONSTRAINT; Schema: address; Owner: postgres
--

ALTER TABLE address.addressxclassification
    ADD CONSTRAINT fk10gn5jpbgrhhp4e99c6i8p9ps FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 4438 (class 2606 OID 206013)
-- Name: addressxgeography fk1jym0y5b721wadwh42kt6jhl7; Type: FK CONSTRAINT; Schema: address; Owner: postgres
--

ALTER TABLE address.addressxgeography
    ADD CONSTRAINT fk1jym0y5b721wadwh42kt6jhl7 FOREIGN KEY (addressid, warehousecreateddate) REFERENCES address.address (addressid, warehousecreateddate);


--
-- TOC entry 4458 (class 2606 OID 206018)
-- Name: addressxresourceitemsecuritytoken fk3483wmiodjy481digc0kciwho; Type: FK CONSTRAINT; Schema: address; Owner: postgres
--

ALTER TABLE address.addressxresourceitemsecuritytoken
    ADD CONSTRAINT fk3483wmiodjy481digc0kciwho FOREIGN KEY (securitytokenid, warehousecreateddate) REFERENCES security.securitytoken (securitytokenid, warehousecreateddate);


--
-- TOC entry 4445 (class 2606 OID 206023)
-- Name: addressxgeographysecuritytoken fk44g7fv09en02ov1n5mtvwm9g0; Type: FK CONSTRAINT; Schema: address; Owner: postgres
--

ALTER TABLE address.addressxgeographysecuritytoken
    ADD CONSTRAINT fk44g7fv09en02ov1n5mtvwm9g0 FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4451 (class 2606 OID 206028)
-- Name: addressxresourceitem fk4fdkipv2j9v5vo3ejwd5k2lkc; Type: FK CONSTRAINT; Schema: address; Owner: postgres
--

ALTER TABLE address.addressxresourceitem
    ADD CONSTRAINT fk4fdkipv2j9v5vo3ejwd5k2lkc FOREIGN KEY (classificationid, warehousecreateddate) REFERENCES classification.classification (classificationid, warehousecreateddate);


--
-- TOC entry 4420 (class 2606 OID 206033)
-- Name: addresssecuritytoken fk4iwbt1caylolasmb7919al3vo; Type: FK CONSTRAINT; Schema: address; Owner: postgres
--

ALTER TABLE address.addresssecuritytoken
    ADD CONSTRAINT fk4iwbt1caylolasmb7919al3vo FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 4452 (class 2606 OID 206038)
-- Name: addressxresourceitem fk6gvqnudvt7tsnt1gxo59d96di; Type: FK CONSTRAINT; Schema: address; Owner: postgres
--

ALTER TABLE address.addressxresourceitem
    ADD CONSTRAINT fk6gvqnudvt7tsnt1gxo59d96di FOREIGN KEY (addressid, warehousecreateddate) REFERENCES address.address (addressid, warehousecreateddate);


--
-- TOC entry 4427 (class 2606 OID 206043)
-- Name: addressxclassification fk6lyv1kvg7y348bj3pmbionfkr; Type: FK CONSTRAINT; Schema: address; Owner: postgres
--

ALTER TABLE address.addressxclassification
    ADD CONSTRAINT fk6lyv1kvg7y348bj3pmbionfkr FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 4453 (class 2606 OID 206048)
-- Name: addressxresourceitem fk8233bmyvh84df7grsv59kx99x; Type: FK CONSTRAINT; Schema: address; Owner: postgres
--

ALTER TABLE address.addressxresourceitem
    ADD CONSTRAINT fk8233bmyvh84df7grsv59kx99x FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4432 (class 2606 OID 206053)
-- Name: addressxclassificationsecuritytoken fk8r4rrxr1r79me66xvgxk7rnrr; Type: FK CONSTRAINT; Schema: address; Owner: postgres
--

ALTER TABLE address.addressxclassificationsecuritytoken
    ADD CONSTRAINT fk8r4rrxr1r79me66xvgxk7rnrr FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 4439 (class 2606 OID 206058)
-- Name: addressxgeography fk8tb5og3v55dnhqp8aw31nwwmt; Type: FK CONSTRAINT; Schema: address; Owner: postgres
--

ALTER TABLE address.addressxgeography
    ADD CONSTRAINT fk8tb5og3v55dnhqp8aw31nwwmt FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 4459 (class 2606 OID 206063)
-- Name: addressxresourceitemsecuritytoken fk94ahf4mdoc5kymdb9kd3ck141; Type: FK CONSTRAINT; Schema: address; Owner: postgres
--

ALTER TABLE address.addressxresourceitemsecuritytoken
    ADD CONSTRAINT fk94ahf4mdoc5kymdb9kd3ck141 FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4415 (class 2606 OID 206068)
-- Name: address fk9iyq6jfpe1xg4oaba71hnicr7; Type: FK CONSTRAINT; Schema: address; Owner: postgres
--

ALTER TABLE address.address
    ADD CONSTRAINT fk9iyq6jfpe1xg4oaba71hnicr7 FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 4433 (class 2606 OID 206073)
-- Name: addressxclassificationsecuritytoken fkaoaax8wtnqlmuh9uh7oewam8w; Type: FK CONSTRAINT; Schema: address; Owner: postgres
--

ALTER TABLE address.addressxclassificationsecuritytoken
    ADD CONSTRAINT fkaoaax8wtnqlmuh9uh7oewam8w FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4454 (class 2606 OID 206078)
-- Name: addressxresourceitem fkappieo5e929jb2sushbbrw3xh; Type: FK CONSTRAINT; Schema: address; Owner: postgres
--

ALTER TABLE address.addressxresourceitem
    ADD CONSTRAINT fkappieo5e929jb2sushbbrw3xh FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 4434 (class 2606 OID 206083)
-- Name: addressxclassificationsecuritytoken fkbnb6j2jg2da9a096a6mdqugc2; Type: FK CONSTRAINT; Schema: address; Owner: postgres
--

ALTER TABLE address.addressxclassificationsecuritytoken
    ADD CONSTRAINT fkbnb6j2jg2da9a096a6mdqugc2 FOREIGN KEY (addressxclassificationid, warehousecreateddate) REFERENCES address.addressxclassification (addressxclassificationid, warehousecreateddate);


--
-- TOC entry 4446 (class 2606 OID 206088)
-- Name: addressxgeographysecuritytoken fkcp386daxldghx156aklcouc4y; Type: FK CONSTRAINT; Schema: address; Owner: postgres
--

ALTER TABLE address.addressxgeographysecuritytoken
    ADD CONSTRAINT fkcp386daxldghx156aklcouc4y FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 4460 (class 2606 OID 206093)
-- Name: addressxresourceitemsecuritytoken fkds3id5ml3ium9v870rdxryoav; Type: FK CONSTRAINT; Schema: address; Owner: postgres
--

ALTER TABLE address.addressxresourceitemsecuritytoken
    ADD CONSTRAINT fkds3id5ml3ium9v870rdxryoav FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 4455 (class 2606 OID 206098)
-- Name: addressxresourceitem fkeckhwk0f0a03h24a7we03pi2q; Type: FK CONSTRAINT; Schema: address; Owner: postgres
--

ALTER TABLE address.addressxresourceitem
    ADD CONSTRAINT fkeckhwk0f0a03h24a7we03pi2q FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4416 (class 2606 OID 206103)
-- Name: address fkf5n0oi8hso8dpemjbb6733utp; Type: FK CONSTRAINT; Schema: address; Owner: postgres
--

ALTER TABLE address.address
    ADD CONSTRAINT fkf5n0oi8hso8dpemjbb6733utp FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 4447 (class 2606 OID 206108)
-- Name: addressxgeographysecuritytoken fkfrqil82x8r1wpyn0ral52ypi3; Type: FK CONSTRAINT; Schema: address; Owner: postgres
--

ALTER TABLE address.addressxgeographysecuritytoken
    ADD CONSTRAINT fkfrqil82x8r1wpyn0ral52ypi3 FOREIGN KEY (addressxgeographyid, warehousecreateddate) REFERENCES address.addressxgeography (addressxgeographyid, warehousecreateddate);


--
-- TOC entry 4448 (class 2606 OID 206113)
-- Name: addressxgeographysecuritytoken fkgbtljbtwk7lsj92gxbx6su7dp; Type: FK CONSTRAINT; Schema: address; Owner: postgres
--

ALTER TABLE address.addressxgeographysecuritytoken
    ADD CONSTRAINT fkgbtljbtwk7lsj92gxbx6su7dp FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4421 (class 2606 OID 206118)
-- Name: addresssecuritytoken fkgc74tjta2etx11rg02xqt293l; Type: FK CONSTRAINT; Schema: address; Owner: postgres
--

ALTER TABLE address.addresssecuritytoken
    ADD CONSTRAINT fkgc74tjta2etx11rg02xqt293l FOREIGN KEY (securitytokenid, warehousecreateddate) REFERENCES security.securitytoken (securitytokenid, warehousecreateddate);


--
-- TOC entry 4428 (class 2606 OID 206123)
-- Name: addressxclassification fkgelsst4chd2utcx5ed62or2h7; Type: FK CONSTRAINT; Schema: address; Owner: postgres
--

ALTER TABLE address.addressxclassification
    ADD CONSTRAINT fkgelsst4chd2utcx5ed62or2h7 FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4440 (class 2606 OID 206128)
-- Name: addressxgeography fkgf8llc9jdhc8s0eatod3ja84o; Type: FK CONSTRAINT; Schema: address; Owner: postgres
--

ALTER TABLE address.addressxgeography
    ADD CONSTRAINT fkgf8llc9jdhc8s0eatod3ja84o FOREIGN KEY (geographyid, warehousecreateddate) REFERENCES geography.geography (geographyid, warehousecreateddate);


--
-- TOC entry 4417 (class 2606 OID 206133)
-- Name: address fkgh65u3w9ww52fhyerw4038tvf; Type: FK CONSTRAINT; Schema: address; Owner: postgres
--

ALTER TABLE address.address
    ADD CONSTRAINT fkgh65u3w9ww52fhyerw4038tvf FOREIGN KEY (classificationid, warehousecreateddate) REFERENCES classification.classification (classificationid, warehousecreateddate);


--
-- TOC entry 4449 (class 2606 OID 206138)
-- Name: addressxgeographysecuritytoken fkhp6f4qu8ywoe5aj507yc3gghn; Type: FK CONSTRAINT; Schema: address; Owner: postgres
--

ALTER TABLE address.addressxgeographysecuritytoken
    ADD CONSTRAINT fkhp6f4qu8ywoe5aj507yc3gghn FOREIGN KEY (securitytokenid, warehousecreateddate) REFERENCES security.securitytoken (securitytokenid, warehousecreateddate);


--
-- TOC entry 4435 (class 2606 OID 206143)
-- Name: addressxclassificationsecuritytoken fki37g7k1r7gjvpsmrglofsk4t5; Type: FK CONSTRAINT; Schema: address; Owner: postgres
--

ALTER TABLE address.addressxclassificationsecuritytoken
    ADD CONSTRAINT fki37g7k1r7gjvpsmrglofsk4t5 FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 4422 (class 2606 OID 206148)
-- Name: addresssecuritytoken fki5s2v3sm6uuf3j8sbqbuxcfu6; Type: FK CONSTRAINT; Schema: address; Owner: postgres
--

ALTER TABLE address.addresssecuritytoken
    ADD CONSTRAINT fki5s2v3sm6uuf3j8sbqbuxcfu6 FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 4461 (class 2606 OID 206153)
-- Name: addressxresourceitemsecuritytoken fkjdtgh57fk70hgg6owk878wdrv; Type: FK CONSTRAINT; Schema: address; Owner: postgres
--

ALTER TABLE address.addressxresourceitemsecuritytoken
    ADD CONSTRAINT fkjdtgh57fk70hgg6owk878wdrv FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4456 (class 2606 OID 206158)
-- Name: addressxresourceitem fkm191nhp3l761p7sgm5r0nafnl; Type: FK CONSTRAINT; Schema: address; Owner: postgres
--

ALTER TABLE address.addressxresourceitem
    ADD CONSTRAINT fkm191nhp3l761p7sgm5r0nafnl FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 4441 (class 2606 OID 206163)
-- Name: addressxgeography fkm2u3jmfqc5akhsq86tod7q6nc; Type: FK CONSTRAINT; Schema: address; Owner: postgres
--

ALTER TABLE address.addressxgeography
    ADD CONSTRAINT fkm2u3jmfqc5akhsq86tod7q6nc FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4423 (class 2606 OID 206168)
-- Name: addresssecuritytoken fknc36gmq9xrt6k0cj6clmbh13p; Type: FK CONSTRAINT; Schema: address; Owner: postgres
--

ALTER TABLE address.addresssecuritytoken
    ADD CONSTRAINT fknc36gmq9xrt6k0cj6clmbh13p FOREIGN KEY (addressid, warehousecreateddate) REFERENCES address.address (addressid, warehousecreateddate);


--
-- TOC entry 4442 (class 2606 OID 206173)
-- Name: addressxgeography fknjongjciautlpcno0p44ekxn1; Type: FK CONSTRAINT; Schema: address; Owner: postgres
--

ALTER TABLE address.addressxgeography
    ADD CONSTRAINT fknjongjciautlpcno0p44ekxn1 FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4429 (class 2606 OID 206178)
-- Name: addressxclassification fkobw11cghkuvasdoprpf8jeax5; Type: FK CONSTRAINT; Schema: address; Owner: postgres
--

ALTER TABLE address.addressxclassification
    ADD CONSTRAINT fkobw11cghkuvasdoprpf8jeax5 FOREIGN KEY (classificationid, warehousecreateddate) REFERENCES classification.classification (classificationid, warehousecreateddate);


--
-- TOC entry 4424 (class 2606 OID 206183)
-- Name: addresssecuritytoken fkog9cqhou7pe3e0cwc7uavblc0; Type: FK CONSTRAINT; Schema: address; Owner: postgres
--

ALTER TABLE address.addresssecuritytoken
    ADD CONSTRAINT fkog9cqhou7pe3e0cwc7uavblc0 FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4418 (class 2606 OID 206188)
-- Name: address fkoj04ku2v02yibdyt7p9nuphyf; Type: FK CONSTRAINT; Schema: address; Owner: postgres
--

ALTER TABLE address.address
    ADD CONSTRAINT fkoj04ku2v02yibdyt7p9nuphyf FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4443 (class 2606 OID 206193)
-- Name: addressxgeography fkoni218klw7gbcm5ksonbvvvld; Type: FK CONSTRAINT; Schema: address; Owner: postgres
--

ALTER TABLE address.addressxgeography
    ADD CONSTRAINT fkoni218klw7gbcm5ksonbvvvld FOREIGN KEY (classificationid, warehousecreateddate) REFERENCES classification.classification (classificationid, warehousecreateddate);


--
-- TOC entry 4436 (class 2606 OID 206198)
-- Name: addressxclassificationsecuritytoken fkoo05nqmk8a03k1nje9outisjd; Type: FK CONSTRAINT; Schema: address; Owner: postgres
--

ALTER TABLE address.addressxclassificationsecuritytoken
    ADD CONSTRAINT fkoo05nqmk8a03k1nje9outisjd FOREIGN KEY (securitytokenid, warehousecreateddate) REFERENCES security.securitytoken (securitytokenid, warehousecreateddate);


--
-- TOC entry 4444 (class 2606 OID 206203)
-- Name: addressxgeography fkpn4h36o5q70i13pbhluyugaxw; Type: FK CONSTRAINT; Schema: address; Owner: postgres
--

ALTER TABLE address.addressxgeography
    ADD CONSTRAINT fkpn4h36o5q70i13pbhluyugaxw FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 4450 (class 2606 OID 206208)
-- Name: addressxgeographysecuritytoken fkq6uacmwuavt3j9bani8jg5k50; Type: FK CONSTRAINT; Schema: address; Owner: postgres
--

ALTER TABLE address.addressxgeographysecuritytoken
    ADD CONSTRAINT fkq6uacmwuavt3j9bani8jg5k50 FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 4462 (class 2606 OID 206213)
-- Name: addressxresourceitemsecuritytoken fkqcrw559ebisww70vhco1yn1l3; Type: FK CONSTRAINT; Schema: address; Owner: postgres
--

ALTER TABLE address.addressxresourceitemsecuritytoken
    ADD CONSTRAINT fkqcrw559ebisww70vhco1yn1l3 FOREIGN KEY (addressxresourceitemid, warehousecreateddate) REFERENCES address.addressxresourceitem (addressxresourceitemid, warehousecreateddate);


--
-- TOC entry 4430 (class 2606 OID 206218)
-- Name: addressxclassification fkqemdst9c0gqhhpeqrixd4567g; Type: FK CONSTRAINT; Schema: address; Owner: postgres
--

ALTER TABLE address.addressxclassification
    ADD CONSTRAINT fkqemdst9c0gqhhpeqrixd4567g FOREIGN KEY (addressid, warehousecreateddate) REFERENCES address.address (addressid, warehousecreateddate);


--
-- TOC entry 4457 (class 2606 OID 206223)
-- Name: addressxresourceitem fks421flo2pfa9xgnh0kaaju64f; Type: FK CONSTRAINT; Schema: address; Owner: postgres
--

ALTER TABLE address.addressxresourceitem
    ADD CONSTRAINT fks421flo2pfa9xgnh0kaaju64f FOREIGN KEY (resourceitemid, warehousecreateddate) REFERENCES resource.resourceitem (resourceitemid, warehousecreateddate);


--
-- TOC entry 4431 (class 2606 OID 206228)
-- Name: addressxclassification fks747gghpsg2w01eb6xycbkem; Type: FK CONSTRAINT; Schema: address; Owner: postgres
--

ALTER TABLE address.addressxclassification
    ADD CONSTRAINT fks747gghpsg2w01eb6xycbkem FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4425 (class 2606 OID 206233)
-- Name: addresssecuritytoken fks8ohxcn7hgiuyyyy7koaught4; Type: FK CONSTRAINT; Schema: address; Owner: postgres
--

ALTER TABLE address.addresssecuritytoken
    ADD CONSTRAINT fks8ohxcn7hgiuyyyy7koaught4 FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4419 (class 2606 OID 206238)
-- Name: address fksx02yl44uidocj3ryaqom1urb; Type: FK CONSTRAINT; Schema: address; Owner: postgres
--

ALTER TABLE address.address
    ADD CONSTRAINT fksx02yl44uidocj3ryaqom1urb FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4463 (class 2606 OID 206243)
-- Name: addressxresourceitemsecuritytoken fksx2i6hs70nkrw122gnxi1oljt; Type: FK CONSTRAINT; Schema: address; Owner: postgres
--

ALTER TABLE address.addressxresourceitemsecuritytoken
    ADD CONSTRAINT fksx2i6hs70nkrw122gnxi1oljt FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 4437 (class 2606 OID 206248)
-- Name: addressxclassificationsecuritytoken fktnrs65dum0qh9qgh0mlyhkf27; Type: FK CONSTRAINT; Schema: address; Owner: postgres
--

ALTER TABLE address.addressxclassificationsecuritytoken
    ADD CONSTRAINT fktnrs65dum0qh9qgh0mlyhkf27 FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 5431 (class 2606 OID 213716)
-- Name: arrangementxresourceitem fk10auu483ou8vross23ilkm601; Type: FK CONSTRAINT; Schema: arrangement; Owner: postgres
--

ALTER TABLE arrangement.arrangementxresourceitem
    ADD CONSTRAINT fk10auu483ou8vross23ilkm601 FOREIGN KEY (classificationid, warehousecreateddate) REFERENCES classification.classification (classificationid, warehousecreateddate);


--
-- TOC entry 5464 (class 2606 OID 213886)
-- Name: arrangementxrulestypesecuritytoken fk11a4s30v3c7vosoqpy1210fhu; Type: FK CONSTRAINT; Schema: arrangement; Owner: postgres
--

ALTER TABLE arrangement.arrangementxrulestypesecuritytoken
    ADD CONSTRAINT fk11a4s30v3c7vosoqpy1210fhu FOREIGN KEY (arrangementxrulestypeid, warehousecreateddate) REFERENCES arrangement.arrangementxrulestype (arrangementxrulestypeid, warehousecreateddate);


--
-- TOC entry 5367 (class 2606 OID 213406)
-- Name: arrangementxarrangement fk1evl6ld81fkqmo674i0ip26v; Type: FK CONSTRAINT; Schema: arrangement; Owner: postgres
--

ALTER TABLE arrangement.arrangementxarrangement
    ADD CONSTRAINT fk1evl6ld81fkqmo674i0ip26v FOREIGN KEY (parentarrangementid, warehousecreateddate) REFERENCES arrangement.arrangement (arrangementid, warehousecreateddate);


--
-- TOC entry 5349 (class 2606 OID 213296)
-- Name: arrangementtypesecuritytoken fk1kek1dppp202jrr8e9f692syg; Type: FK CONSTRAINT; Schema: arrangement; Owner: postgres
--

ALTER TABLE arrangement.arrangementtypesecuritytoken
    ADD CONSTRAINT fk1kek1dppp202jrr8e9f692syg FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 5418 (class 2606 OID 213641)
-- Name: arrangementxproduct fk1xsuwtqoogpaxitg2wjqk2rs3; Type: FK CONSTRAINT; Schema: arrangement; Owner: postgres
--

ALTER TABLE arrangement.arrangementxproduct
    ADD CONSTRAINT fk1xsuwtqoogpaxitg2wjqk2rs3 FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 5374 (class 2606 OID 213421)
-- Name: arrangementxarrangementsecuritytoken fk2fi4ltyv1r2frptrof3moedig; Type: FK CONSTRAINT; Schema: arrangement; Owner: postgres
--

ALTER TABLE arrangement.arrangementxarrangementsecuritytoken
    ADD CONSTRAINT fk2fi4ltyv1r2frptrof3moedig FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 5375 (class 2606 OID 213426)
-- Name: arrangementxarrangementsecuritytoken fk2hmmp93bfci8j7pwlv14p7m9; Type: FK CONSTRAINT; Schema: arrangement; Owner: postgres
--

ALTER TABLE arrangement.arrangementxarrangementsecuritytoken
    ADD CONSTRAINT fk2hmmp93bfci8j7pwlv14p7m9 FOREIGN KEY (securitytokenid, warehousecreateddate) REFERENCES security.securitytoken (securitytokenid, warehousecreateddate);


--
-- TOC entry 5425 (class 2606 OID 213666)
-- Name: arrangementxproductsecuritytoken fk2mf0po3jsf10wa7ypxoyj2t0q; Type: FK CONSTRAINT; Schema: arrangement; Owner: postgres
--

ALTER TABLE arrangement.arrangementxproductsecuritytoken
    ADD CONSTRAINT fk2mf0po3jsf10wa7ypxoyj2t0q FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 5405 (class 2606 OID 213571)
-- Name: arrangementxinvolvedparty fk2nbjk1qgv7xcqx60p5k9fpknv; Type: FK CONSTRAINT; Schema: arrangement; Owner: postgres
--

ALTER TABLE arrangement.arrangementxinvolvedparty
    ADD CONSTRAINT fk2nbjk1qgv7xcqx60p5k9fpknv FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 5451 (class 2606 OID 213811)
-- Name: arrangementxrulessecuritytoken fk33pq6ks4wh6ap23lm8cmv11e3; Type: FK CONSTRAINT; Schema: arrangement; Owner: postgres
--

ALTER TABLE arrangement.arrangementxrulessecuritytoken
    ADD CONSTRAINT fk33pq6ks4wh6ap23lm8cmv11e3 FOREIGN KEY (securitytokenid, warehousecreateddate) REFERENCES security.securitytoken (securitytokenid, warehousecreateddate);


--
-- TOC entry 5355 (class 2606 OID 213326)
-- Name: arrangementtypexclassification fk3cu1a03o9dfjsrxdgg5f8ljc1; Type: FK CONSTRAINT; Schema: arrangement; Owner: postgres
--

ALTER TABLE arrangement.arrangementtypexclassification
    ADD CONSTRAINT fk3cu1a03o9dfjsrxdgg5f8ljc1 FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 5465 (class 2606 OID 213866)
-- Name: arrangementxrulestypesecuritytoken fk3plf0o1h097qqbme256dh14ms; Type: FK CONSTRAINT; Schema: arrangement; Owner: postgres
--

ALTER TABLE arrangement.arrangementxrulestypesecuritytoken
    ADD CONSTRAINT fk3plf0o1h097qqbme256dh14ms FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 5444 (class 2606 OID 213761)
-- Name: arrangementxrules fk3q3s3y2ypar6b6yao94qiavib; Type: FK CONSTRAINT; Schema: arrangement; Owner: postgres
--

ALTER TABLE arrangement.arrangementxrules
    ADD CONSTRAINT fk3q3s3y2ypar6b6yao94qiavib FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 5419 (class 2606 OID 213661)
-- Name: arrangementxproduct fk3uwrmqh1e9msanm5ujxk06mqh; Type: FK CONSTRAINT; Schema: arrangement; Owner: postgres
--

ALTER TABLE arrangement.arrangementxproduct
    ADD CONSTRAINT fk3uwrmqh1e9msanm5ujxk06mqh FOREIGN KEY (productid, warehousecreateddate) REFERENCES product.product (productid, warehousecreateddate);


--
-- TOC entry 5376 (class 2606 OID 213416)
-- Name: arrangementxarrangementsecuritytoken fk4b5sc95gghbab83uot4y8kma4; Type: FK CONSTRAINT; Schema: arrangement; Owner: postgres
--

ALTER TABLE arrangement.arrangementxarrangementsecuritytoken
    ADD CONSTRAINT fk4b5sc95gghbab83uot4y8kma4 FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 5387 (class 2606 OID 213501)
-- Name: arrangementxarrangementtypesecuritytoken fk4f2d6k0rr9lafbgy0j4w59405; Type: FK CONSTRAINT; Schema: arrangement; Owner: postgres
--

ALTER TABLE arrangement.arrangementxarrangementtypesecuritytoken
    ADD CONSTRAINT fk4f2d6k0rr9lafbgy0j4w59405 FOREIGN KEY (arrangementxarrangementtypeid, warehousecreateddate) REFERENCES arrangement.arrangementxarrangementtype (arrangementxarrangementtypeid, warehousecreateddate);


--
-- TOC entry 5350 (class 2606 OID 213291)
-- Name: arrangementtypesecuritytoken fk4kogklx4xlk4ck1ol3nc3xuf3; Type: FK CONSTRAINT; Schema: arrangement; Owner: postgres
--

ALTER TABLE arrangement.arrangementtypesecuritytoken
    ADD CONSTRAINT fk4kogklx4xlk4ck1ol3nc3xuf3 FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 5438 (class 2606 OID 213746)
-- Name: arrangementxresourceitemsecuritytoken fk4lwhxuk5gfc02ycby5kfro576; Type: FK CONSTRAINT; Schema: arrangement; Owner: postgres
--

ALTER TABLE arrangement.arrangementxresourceitemsecuritytoken
    ADD CONSTRAINT fk4lwhxuk5gfc02ycby5kfro576 FOREIGN KEY (securitytokenid, warehousecreateddate) REFERENCES security.securitytoken (securitytokenid, warehousecreateddate);


--
-- TOC entry 5466 (class 2606 OID 213871)
-- Name: arrangementxrulestypesecuritytoken fk51besipwxf9c4wsjao3x1b2y1; Type: FK CONSTRAINT; Schema: arrangement; Owner: postgres
--

ALTER TABLE arrangement.arrangementxrulestypesecuritytoken
    ADD CONSTRAINT fk51besipwxf9c4wsjao3x1b2y1 FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 5467 (class 2606 OID 213876)
-- Name: arrangementxrulestypesecuritytoken fk533wd6s99uye7jfrvnscmp7h0; Type: FK CONSTRAINT; Schema: arrangement; Owner: postgres
--

ALTER TABLE arrangement.arrangementxrulestypesecuritytoken
    ADD CONSTRAINT fk533wd6s99uye7jfrvnscmp7h0 FOREIGN KEY (securitytokenid, warehousecreateddate) REFERENCES security.securitytoken (securitytokenid, warehousecreateddate);


--
-- TOC entry 5432 (class 2606 OID 213726)
-- Name: arrangementxresourceitem fk5m4kptkleha73alk03p761cau; Type: FK CONSTRAINT; Schema: arrangement; Owner: postgres
--

ALTER TABLE arrangement.arrangementxresourceitem
    ADD CONSTRAINT fk5m4kptkleha73alk03p761cau FOREIGN KEY (resourceitemid, warehousecreateddate) REFERENCES resource.resourceitem (resourceitemid, warehousecreateddate);


--
-- TOC entry 5468 (class 2606 OID 213861)
-- Name: arrangementxrulestypesecuritytoken fk5m5vtq233eq18lrww0cu5isj9; Type: FK CONSTRAINT; Schema: arrangement; Owner: postgres
--

ALTER TABLE arrangement.arrangementxrulestypesecuritytoken
    ADD CONSTRAINT fk5m5vtq233eq18lrww0cu5isj9 FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 5412 (class 2606 OID 213611)
-- Name: arrangementxinvolvedpartysecuritytoken fk5nm7udu4cx5rwgqsxiy10vdmj; Type: FK CONSTRAINT; Schema: arrangement; Owner: postgres
--

ALTER TABLE arrangement.arrangementxinvolvedpartysecuritytoken
    ADD CONSTRAINT fk5nm7udu4cx5rwgqsxiy10vdmj FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 5335 (class 2606 OID 213216)
-- Name: arrangement fk5qltxdf1yl3w935f1v6c1s3te; Type: FK CONSTRAINT; Schema: arrangement; Owner: postgres
--

ALTER TABLE arrangement.arrangement
    ADD CONSTRAINT fk5qltxdf1yl3w935f1v6c1s3te FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 5393 (class 2606 OID 213531)
-- Name: arrangementxclassification fk5rajkqbykrmn8tkkrqg2clkk1; Type: FK CONSTRAINT; Schema: arrangement; Owner: postgres
--

ALTER TABLE arrangement.arrangementxclassification
    ADD CONSTRAINT fk5rajkqbykrmn8tkkrqg2clkk1 FOREIGN KEY (arrangementid, warehousecreateddate) REFERENCES arrangement.arrangement (arrangementid, warehousecreateddate);


--
-- TOC entry 5368 (class 2606 OID 213376)
-- Name: arrangementxarrangement fk5x95ig2e720s90163vs5mru16; Type: FK CONSTRAINT; Schema: arrangement; Owner: postgres
--

ALTER TABLE arrangement.arrangementxarrangement
    ADD CONSTRAINT fk5x95ig2e720s90163vs5mru16 FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 5452 (class 2606 OID 213806)
-- Name: arrangementxrulessecuritytoken fk62jqx0o1s2q6r66fn8nrjo4bf; Type: FK CONSTRAINT; Schema: arrangement; Owner: postgres
--

ALTER TABLE arrangement.arrangementxrulessecuritytoken
    ADD CONSTRAINT fk62jqx0o1s2q6r66fn8nrjo4bf FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 5457 (class 2606 OID 213826)
-- Name: arrangementxrulestype fk6343urwgxbykoau8p7h0i58gj; Type: FK CONSTRAINT; Schema: arrangement; Owner: postgres
--

ALTER TABLE arrangement.arrangementxrulestype
    ADD CONSTRAINT fk6343urwgxbykoau8p7h0i58gj FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 5388 (class 2606 OID 213481)
-- Name: arrangementxarrangementtypesecuritytoken fk67y1klef35wuyfupagyk4roic; Type: FK CONSTRAINT; Schema: arrangement; Owner: postgres
--

ALTER TABLE arrangement.arrangementxarrangementtypesecuritytoken
    ADD CONSTRAINT fk67y1klef35wuyfupagyk4roic FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 5458 (class 2606 OID 213856)
-- Name: arrangementxrulestype fk69m07mb95iafnyyk11228ihmc; Type: FK CONSTRAINT; Schema: arrangement; Owner: postgres
--

ALTER TABLE arrangement.arrangementxrulestype
    ADD CONSTRAINT fk69m07mb95iafnyyk11228ihmc FOREIGN KEY (rulestypeid, warehousecreateddate) REFERENCES rules.rulestype (rulestypeid, warehousecreateddate);


--
-- TOC entry 5406 (class 2606 OID 213581)
-- Name: arrangementxinvolvedparty fk6c7521dhlo7ou6o4oeosk7eop; Type: FK CONSTRAINT; Schema: arrangement; Owner: postgres
--

ALTER TABLE arrangement.arrangementxinvolvedparty
    ADD CONSTRAINT fk6c7521dhlo7ou6o4oeosk7eop FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 5407 (class 2606 OID 213586)
-- Name: arrangementxinvolvedparty fk6ka1ls0qmv9y1iaya2rkr8m5r; Type: FK CONSTRAINT; Schema: arrangement; Owner: postgres
--

ALTER TABLE arrangement.arrangementxinvolvedparty
    ADD CONSTRAINT fk6ka1ls0qmv9y1iaya2rkr8m5r FOREIGN KEY (classificationid, warehousecreateddate) REFERENCES classification.classification (classificationid, warehousecreateddate);


--
-- TOC entry 5459 (class 2606 OID 213841)
-- Name: arrangementxrulestype fk6rx9646bu0edq22qa8nol81vv; Type: FK CONSTRAINT; Schema: arrangement; Owner: postgres
--

ALTER TABLE arrangement.arrangementxrulestype
    ADD CONSTRAINT fk6rx9646bu0edq22qa8nol81vv FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 5399 (class 2606 OID 213536)
-- Name: arrangementxclassificationsecuritytoken fk6tsk1t4h6o94591ke2oy8fcte; Type: FK CONSTRAINT; Schema: arrangement; Owner: postgres
--

ALTER TABLE arrangement.arrangementxclassificationsecuritytoken
    ADD CONSTRAINT fk6tsk1t4h6o94591ke2oy8fcte FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 5453 (class 2606 OID 213801)
-- Name: arrangementxrulessecuritytoken fk6tttie9brrhlxxh2adq02kffo; Type: FK CONSTRAINT; Schema: arrangement; Owner: postgres
--

ALTER TABLE arrangement.arrangementxrulessecuritytoken
    ADD CONSTRAINT fk6tttie9brrhlxxh2adq02kffo FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 5408 (class 2606 OID 213591)
-- Name: arrangementxinvolvedparty fk71wpg1p7v2qn02lh0cw5drs95; Type: FK CONSTRAINT; Schema: arrangement; Owner: postgres
--

ALTER TABLE arrangement.arrangementxinvolvedparty
    ADD CONSTRAINT fk71wpg1p7v2qn02lh0cw5drs95 FOREIGN KEY (arrangementid, warehousecreateddate) REFERENCES arrangement.arrangement (arrangementid, warehousecreateddate);


--
-- TOC entry 5400 (class 2606 OID 213556)
-- Name: arrangementxclassificationsecuritytoken fk7h39db92fgksv3rwgkq72hsd1; Type: FK CONSTRAINT; Schema: arrangement; Owner: postgres
--

ALTER TABLE arrangement.arrangementxclassificationsecuritytoken
    ADD CONSTRAINT fk7h39db92fgksv3rwgkq72hsd1 FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 5389 (class 2606 OID 213496)
-- Name: arrangementxarrangementtypesecuritytoken fk7y5i3hvgtvn2u9mtx8wljsh9a; Type: FK CONSTRAINT; Schema: arrangement; Owner: postgres
--

ALTER TABLE arrangement.arrangementxarrangementtypesecuritytoken
    ADD CONSTRAINT fk7y5i3hvgtvn2u9mtx8wljsh9a FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 5426 (class 2606 OID 213676)
-- Name: arrangementxproductsecuritytoken fk7ychxphmo3yvikab4txsecg1h; Type: FK CONSTRAINT; Schema: arrangement; Owner: postgres
--

ALTER TABLE arrangement.arrangementxproductsecuritytoken
    ADD CONSTRAINT fk7ychxphmo3yvikab4txsecg1h FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 5427 (class 2606 OID 213691)
-- Name: arrangementxproductsecuritytoken fk82ys481oxq2v892sryshuytj0; Type: FK CONSTRAINT; Schema: arrangement; Owner: postgres
--

ALTER TABLE arrangement.arrangementxproductsecuritytoken
    ADD CONSTRAINT fk82ys481oxq2v892sryshuytj0 FOREIGN KEY (arrangementxproductid, warehousecreateddate) REFERENCES arrangement.arrangementxproduct (arrangementxproductid, warehousecreateddate);


--
-- TOC entry 5439 (class 2606 OID 213731)
-- Name: arrangementxresourceitemsecuritytoken fk8bgg9lfof21l7yvjet4et71xj; Type: FK CONSTRAINT; Schema: arrangement; Owner: postgres
--

ALTER TABLE arrangement.arrangementxresourceitemsecuritytoken
    ADD CONSTRAINT fk8bgg9lfof21l7yvjet4et71xj FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 5339 (class 2606 OID 213256)
-- Name: arrangementsecuritytoken fk8h3yhvjy6fw0rvr2uyxq8voh5; Type: FK CONSTRAINT; Schema: arrangement; Owner: postgres
--

ALTER TABLE arrangement.arrangementsecuritytoken
    ADD CONSTRAINT fk8h3yhvjy6fw0rvr2uyxq8voh5 FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 5401 (class 2606 OID 213551)
-- Name: arrangementxclassificationsecuritytoken fk8jucexc7kuoodu11v39lpwxj9; Type: FK CONSTRAINT; Schema: arrangement; Owner: postgres
--

ALTER TABLE arrangement.arrangementxclassificationsecuritytoken
    ADD CONSTRAINT fk8jucexc7kuoodu11v39lpwxj9 FOREIGN KEY (securitytokenid, warehousecreateddate) REFERENCES security.securitytoken (securitytokenid, warehousecreateddate);


--
-- TOC entry 5351 (class 2606 OID 213311)
-- Name: arrangementtypesecuritytoken fk8to2ic6e1hst2vlk01q5p7cw1; Type: FK CONSTRAINT; Schema: arrangement; Owner: postgres
--

ALTER TABLE arrangement.arrangementtypesecuritytoken
    ADD CONSTRAINT fk8to2ic6e1hst2vlk01q5p7cw1 FOREIGN KEY (arrangementtypeid, warehousecreateddate) REFERENCES arrangement.arrangementtype (arrangementtypeid, warehousecreateddate);


--
-- TOC entry 5402 (class 2606 OID 213541)
-- Name: arrangementxclassificationsecuritytoken fk9c1o9pupbc18y3et9022u6etc; Type: FK CONSTRAINT; Schema: arrangement; Owner: postgres
--

ALTER TABLE arrangement.arrangementxclassificationsecuritytoken
    ADD CONSTRAINT fk9c1o9pupbc18y3et9022u6etc FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 5403 (class 2606 OID 213561)
-- Name: arrangementxclassificationsecuritytoken fk9cdguosd6bmsd6ihjueeay3qu; Type: FK CONSTRAINT; Schema: arrangement; Owner: postgres
--

ALTER TABLE arrangement.arrangementxclassificationsecuritytoken
    ADD CONSTRAINT fk9cdguosd6bmsd6ihjueeay3qu FOREIGN KEY (arrangementxclassificationid, warehousecreateddate) REFERENCES arrangement.arrangementxclassification (arrangementxclassificationid, warehousecreateddate);


--
-- TOC entry 5369 (class 2606 OID 213381)
-- Name: arrangementxarrangement fk9g734gsx3xenay6j6aw1t11dv; Type: FK CONSTRAINT; Schema: arrangement; Owner: postgres
--

ALTER TABLE arrangement.arrangementxarrangement
    ADD CONSTRAINT fk9g734gsx3xenay6j6aw1t11dv FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 5336 (class 2606 OID 213231)
-- Name: arrangement fk9gnhqn13p53nkwow24uj5lllt; Type: FK CONSTRAINT; Schema: arrangement; Owner: postgres
--

ALTER TABLE arrangement.arrangement
    ADD CONSTRAINT fk9gnhqn13p53nkwow24uj5lllt FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 5352 (class 2606 OID 213301)
-- Name: arrangementtypesecuritytoken fk9le4bf2k6hm5nc8ieluakg4fd; Type: FK CONSTRAINT; Schema: arrangement; Owner: postgres
--

ALTER TABLE arrangement.arrangementtypesecuritytoken
    ADD CONSTRAINT fk9le4bf2k6hm5nc8ieluakg4fd FOREIGN KEY (securitytokenid, warehousecreateddate) REFERENCES security.securitytoken (securitytokenid, warehousecreateddate);


--
-- TOC entry 5390 (class 2606 OID 213476)
-- Name: arrangementxarrangementtypesecuritytoken fk9q39hofgnynlo6lfnv57d0wn7; Type: FK CONSTRAINT; Schema: arrangement; Owner: postgres
--

ALTER TABLE arrangement.arrangementxarrangementtypesecuritytoken
    ADD CONSTRAINT fk9q39hofgnynlo6lfnv57d0wn7 FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 5361 (class 2606 OID 213371)
-- Name: arrangementtypexclassificationsecuritytoken fk9veykvob6op2xdb32ad53tu82; Type: FK CONSTRAINT; Schema: arrangement; Owner: postgres
--

ALTER TABLE arrangement.arrangementtypexclassificationsecuritytoken
    ADD CONSTRAINT fk9veykvob6op2xdb32ad53tu82 FOREIGN KEY (arrangementtypexclassificationid, warehousecreateddate) REFERENCES arrangement.arrangementtypexclassification (arrangementtypexclassificationid, warehousecreateddate);


--
-- TOC entry 5394 (class 2606 OID 213526)
-- Name: arrangementxclassification fk9w31er1wt79j9n4v3ugfhb6hn; Type: FK CONSTRAINT; Schema: arrangement; Owner: postgres
--

ALTER TABLE arrangement.arrangementxclassification
    ADD CONSTRAINT fk9w31er1wt79j9n4v3ugfhb6hn FOREIGN KEY (classificationid, warehousecreateddate) REFERENCES classification.classification (classificationid, warehousecreateddate);


--
-- TOC entry 5345 (class 2606 OID 213281)
-- Name: arrangementtype fkaflivu0h1pm24n5y95tw5gv1y; Type: FK CONSTRAINT; Schema: arrangement; Owner: postgres
--

ALTER TABLE arrangement.arrangementtype
    ADD CONSTRAINT fkaflivu0h1pm24n5y95tw5gv1y FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 5420 (class 2606 OID 213651)
-- Name: arrangementxproduct fkalyhg8tgsoumu832w7ol05mqv; Type: FK CONSTRAINT; Schema: arrangement; Owner: postgres
--

ALTER TABLE arrangement.arrangementxproduct
    ADD CONSTRAINT fkalyhg8tgsoumu832w7ol05mqv FOREIGN KEY (classificationid, warehousecreateddate) REFERENCES classification.classification (classificationid, warehousecreateddate);


--
-- TOC entry 5380 (class 2606 OID 213441)
-- Name: arrangementxarrangementtype fkb2mvoggrta8b3xlgvxav3gv68; Type: FK CONSTRAINT; Schema: arrangement; Owner: postgres
--

ALTER TABLE arrangement.arrangementxarrangementtype
    ADD CONSTRAINT fkb2mvoggrta8b3xlgvxav3gv68 FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 5381 (class 2606 OID 213466)
-- Name: arrangementxarrangementtype fkbbug6qxo424aa0l3k0ilpjwh1; Type: FK CONSTRAINT; Schema: arrangement; Owner: postgres
--

ALTER TABLE arrangement.arrangementxarrangementtype
    ADD CONSTRAINT fkbbug6qxo424aa0l3k0ilpjwh1 FOREIGN KEY (arrangementid, warehousecreateddate) REFERENCES arrangement.arrangement (arrangementid, warehousecreateddate);


--
-- TOC entry 5445 (class 2606 OID 213766)
-- Name: arrangementxrules fkbc3k5pxxb18ylj1p929i1t740; Type: FK CONSTRAINT; Schema: arrangement; Owner: postgres
--

ALTER TABLE arrangement.arrangementxrules
    ADD CONSTRAINT fkbc3k5pxxb18ylj1p929i1t740 FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 5382 (class 2606 OID 213451)
-- Name: arrangementxarrangementtype fkbcb78fk9imiu30j55mudj4w9l; Type: FK CONSTRAINT; Schema: arrangement; Owner: postgres
--

ALTER TABLE arrangement.arrangementxarrangementtype
    ADD CONSTRAINT fkbcb78fk9imiu30j55mudj4w9l FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 5395 (class 2606 OID 213511)
-- Name: arrangementxclassification fkbkgxtaag54r6vr8vyt0p6e2uu; Type: FK CONSTRAINT; Schema: arrangement; Owner: postgres
--

ALTER TABLE arrangement.arrangementxclassification
    ADD CONSTRAINT fkbkgxtaag54r6vr8vyt0p6e2uu FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 5370 (class 2606 OID 213401)
-- Name: arrangementxarrangement fkbli36e1w9e3a47g2s5a5unn2n; Type: FK CONSTRAINT; Schema: arrangement; Owner: postgres
--

ALTER TABLE arrangement.arrangementxarrangement
    ADD CONSTRAINT fkbli36e1w9e3a47g2s5a5unn2n FOREIGN KEY (childarrangementid, warehousecreateddate) REFERENCES arrangement.arrangement (arrangementid, warehousecreateddate);


--
-- TOC entry 5377 (class 2606 OID 213436)
-- Name: arrangementxarrangementsecuritytoken fkby5vbby0hxu3nnxqjkgpg71xq; Type: FK CONSTRAINT; Schema: arrangement; Owner: postgres
--

ALTER TABLE arrangement.arrangementxarrangementsecuritytoken
    ADD CONSTRAINT fkby5vbby0hxu3nnxqjkgpg71xq FOREIGN KEY (arrangementxarrangementid, warehousecreateddate) REFERENCES arrangement.arrangementxarrangement (arrangementxarrangementid, warehousecreateddate);


--
-- TOC entry 5409 (class 2606 OID 213576)
-- Name: arrangementxinvolvedparty fkc3wsa0x6xjq8pt6mnp8p2390l; Type: FK CONSTRAINT; Schema: arrangement; Owner: postgres
--

ALTER TABLE arrangement.arrangementxinvolvedparty
    ADD CONSTRAINT fkc3wsa0x6xjq8pt6mnp8p2390l FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 5454 (class 2606 OID 213796)
-- Name: arrangementxrulessecuritytoken fkc4a0rcrpg0hotxs39ny4ki4qw; Type: FK CONSTRAINT; Schema: arrangement; Owner: postgres
--

ALTER TABLE arrangement.arrangementxrulessecuritytoken
    ADD CONSTRAINT fkc4a0rcrpg0hotxs39ny4ki4qw FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 5378 (class 2606 OID 213411)
-- Name: arrangementxarrangementsecuritytoken fkc6bmk80c5n3kl4frhnr5nsn5s; Type: FK CONSTRAINT; Schema: arrangement; Owner: postgres
--

ALTER TABLE arrangement.arrangementxarrangementsecuritytoken
    ADD CONSTRAINT fkc6bmk80c5n3kl4frhnr5nsn5s FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 5460 (class 2606 OID 213831)
-- Name: arrangementxrulestype fkcnkik4eq3xs4h6v2j13ul3rsv; Type: FK CONSTRAINT; Schema: arrangement; Owner: postgres
--

ALTER TABLE arrangement.arrangementxrulestype
    ADD CONSTRAINT fkcnkik4eq3xs4h6v2j13ul3rsv FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 5461 (class 2606 OID 213851)
-- Name: arrangementxrulestype fkd7koulxa7fid1reyw9nyhhuts; Type: FK CONSTRAINT; Schema: arrangement; Owner: postgres
--

ALTER TABLE arrangement.arrangementxrulestype
    ADD CONSTRAINT fkd7koulxa7fid1reyw9nyhhuts FOREIGN KEY (arrangementid, warehousecreateddate) REFERENCES arrangement.arrangement (arrangementid, warehousecreateddate);


--
-- TOC entry 5433 (class 2606 OID 213706)
-- Name: arrangementxresourceitem fked04m2i7mgfyfrsxu60cmf8or; Type: FK CONSTRAINT; Schema: arrangement; Owner: postgres
--

ALTER TABLE arrangement.arrangementxresourceitem
    ADD CONSTRAINT fked04m2i7mgfyfrsxu60cmf8or FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 5421 (class 2606 OID 213636)
-- Name: arrangementxproduct fked4l0kxkvvnw06uq0l4n55e35; Type: FK CONSTRAINT; Schema: arrangement; Owner: postgres
--

ALTER TABLE arrangement.arrangementxproduct
    ADD CONSTRAINT fked4l0kxkvvnw06uq0l4n55e35 FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 5383 (class 2606 OID 213471)
-- Name: arrangementxarrangementtype fkek405uwe4g6xo9tdi9gfhg88y; Type: FK CONSTRAINT; Schema: arrangement; Owner: postgres
--

ALTER TABLE arrangement.arrangementxarrangementtype
    ADD CONSTRAINT fkek405uwe4g6xo9tdi9gfhg88y FOREIGN KEY (arrangementtypeid, warehousecreateddate) REFERENCES arrangement.arrangementtype (arrangementtypeid, warehousecreateddate);


--
-- TOC entry 5396 (class 2606 OID 213506)
-- Name: arrangementxclassification fkesqu1vwy8pm4ori8xt2hlt00y; Type: FK CONSTRAINT; Schema: arrangement; Owner: postgres
--

ALTER TABLE arrangement.arrangementxclassification
    ADD CONSTRAINT fkesqu1vwy8pm4ori8xt2hlt00y FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 5428 (class 2606 OID 213671)
-- Name: arrangementxproductsecuritytoken fkf0liawyl1pg73vaqhl9077ty2; Type: FK CONSTRAINT; Schema: arrangement; Owner: postgres
--

ALTER TABLE arrangement.arrangementxproductsecuritytoken
    ADD CONSTRAINT fkf0liawyl1pg73vaqhl9077ty2 FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 5446 (class 2606 OID 213776)
-- Name: arrangementxrules fkf8qlkxh19e3pedt1xmnwe786o; Type: FK CONSTRAINT; Schema: arrangement; Owner: postgres
--

ALTER TABLE arrangement.arrangementxrules
    ADD CONSTRAINT fkf8qlkxh19e3pedt1xmnwe786o FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 5362 (class 2606 OID 213361)
-- Name: arrangementtypexclassificationsecuritytoken fkfieks9k5klxucyqm2qel3dvyk; Type: FK CONSTRAINT; Schema: arrangement; Owner: postgres
--

ALTER TABLE arrangement.arrangementtypexclassificationsecuritytoken
    ADD CONSTRAINT fkfieks9k5klxucyqm2qel3dvyk FOREIGN KEY (securitytokenid, warehousecreateddate) REFERENCES security.securitytoken (securitytokenid, warehousecreateddate);


--
-- TOC entry 5363 (class 2606 OID 213366)
-- Name: arrangementtypexclassificationsecuritytoken fkfuddtuea83j0rqqy24iwxsddr; Type: FK CONSTRAINT; Schema: arrangement; Owner: postgres
--

ALTER TABLE arrangement.arrangementtypexclassificationsecuritytoken
    ADD CONSTRAINT fkfuddtuea83j0rqqy24iwxsddr FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 5469 (class 2606 OID 213881)
-- Name: arrangementxrulestypesecuritytoken fkfxhigbw4io92wri4583cu4v0l; Type: FK CONSTRAINT; Schema: arrangement; Owner: postgres
--

ALTER TABLE arrangement.arrangementxrulestypesecuritytoken
    ADD CONSTRAINT fkfxhigbw4io92wri4583cu4v0l FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 5455 (class 2606 OID 213821)
-- Name: arrangementxrulessecuritytoken fkgeha0vtd9yo30vppd0an8kcyp; Type: FK CONSTRAINT; Schema: arrangement; Owner: postgres
--

ALTER TABLE arrangement.arrangementxrulessecuritytoken
    ADD CONSTRAINT fkgeha0vtd9yo30vppd0an8kcyp FOREIGN KEY (arrangementxrulesid, warehousecreateddate) REFERENCES arrangement.arrangementxrules (arrangementxrulesid, warehousecreateddate);


--
-- TOC entry 5356 (class 2606 OID 213336)
-- Name: arrangementtypexclassification fkgqxi8s68s3xjnmr59l1mjrm3g; Type: FK CONSTRAINT; Schema: arrangement; Owner: postgres
--

ALTER TABLE arrangement.arrangementtypexclassification
    ADD CONSTRAINT fkgqxi8s68s3xjnmr59l1mjrm3g FOREIGN KEY (classificationid, warehousecreateddate) REFERENCES classification.classification (classificationid, warehousecreateddate);


--
-- TOC entry 5357 (class 2606 OID 213341)
-- Name: arrangementtypexclassification fkhasvr2i2sploam3msr9ujxvg4; Type: FK CONSTRAINT; Schema: arrangement; Owner: postgres
--

ALTER TABLE arrangement.arrangementtypexclassification
    ADD CONSTRAINT fkhasvr2i2sploam3msr9ujxvg4 FOREIGN KEY (arrangementtypeid, warehousecreateddate) REFERENCES arrangement.arrangementtype (arrangementtypeid, warehousecreateddate);


--
-- TOC entry 5456 (class 2606 OID 213816)
-- Name: arrangementxrulessecuritytoken fkhf5rvmrtd7mq9vm4obo2m68eh; Type: FK CONSTRAINT; Schema: arrangement; Owner: postgres
--

ALTER TABLE arrangement.arrangementxrulessecuritytoken
    ADD CONSTRAINT fkhf5rvmrtd7mq9vm4obo2m68eh FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 5364 (class 2606 OID 213356)
-- Name: arrangementtypexclassificationsecuritytoken fkhsvu9ibj7uijfgn34h38l5nh3; Type: FK CONSTRAINT; Schema: arrangement; Owner: postgres
--

ALTER TABLE arrangement.arrangementtypexclassificationsecuritytoken
    ADD CONSTRAINT fkhsvu9ibj7uijfgn34h38l5nh3 FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 5337 (class 2606 OID 213221)
-- Name: arrangement fkhyfnlmxxkjivonnw64dir0iph; Type: FK CONSTRAINT; Schema: arrangement; Owner: postgres
--

ALTER TABLE arrangement.arrangement
    ADD CONSTRAINT fkhyfnlmxxkjivonnw64dir0iph FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 5410 (class 2606 OID 213566)
-- Name: arrangementxinvolvedparty fki29w8ch3051e71mlykw6gt7p0; Type: FK CONSTRAINT; Schema: arrangement; Owner: postgres
--

ALTER TABLE arrangement.arrangementxinvolvedparty
    ADD CONSTRAINT fki29w8ch3051e71mlykw6gt7p0 FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 5422 (class 2606 OID 213646)
-- Name: arrangementxproduct fki2inhxddbgmsdi3bl7mwt7fqi; Type: FK CONSTRAINT; Schema: arrangement; Owner: postgres
--

ALTER TABLE arrangement.arrangementxproduct
    ADD CONSTRAINT fki2inhxddbgmsdi3bl7mwt7fqi FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 5440 (class 2606 OID 213756)
-- Name: arrangementxresourceitemsecuritytoken fki6fgnt16ckmqi9y21s9a8gp47; Type: FK CONSTRAINT; Schema: arrangement; Owner: postgres
--

ALTER TABLE arrangement.arrangementxresourceitemsecuritytoken
    ADD CONSTRAINT fki6fgnt16ckmqi9y21s9a8gp47 FOREIGN KEY (arrangementxresourceitemid, warehousecreateddate) REFERENCES arrangement.arrangementxresourceitem (arrangementxresourceitemid, warehousecreateddate);


--
-- TOC entry 5379 (class 2606 OID 213431)
-- Name: arrangementxarrangementsecuritytoken fkieslcpo9uox0881igyk7rdfrq; Type: FK CONSTRAINT; Schema: arrangement; Owner: postgres
--

ALTER TABLE arrangement.arrangementxarrangementsecuritytoken
    ADD CONSTRAINT fkieslcpo9uox0881igyk7rdfrq FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 5423 (class 2606 OID 213656)
-- Name: arrangementxproduct fkiissfi60gmgix1sr4ik8wp9fa; Type: FK CONSTRAINT; Schema: arrangement; Owner: postgres
--

ALTER TABLE arrangement.arrangementxproduct
    ADD CONSTRAINT fkiissfi60gmgix1sr4ik8wp9fa FOREIGN KEY (arrangementid, warehousecreateddate) REFERENCES arrangement.arrangement (arrangementid, warehousecreateddate);


--
-- TOC entry 5434 (class 2606 OID 213721)
-- Name: arrangementxresourceitem fkiu0pr9p6o39mwwfh9epgoahom; Type: FK CONSTRAINT; Schema: arrangement; Owner: postgres
--

ALTER TABLE arrangement.arrangementxresourceitem
    ADD CONSTRAINT fkiu0pr9p6o39mwwfh9epgoahom FOREIGN KEY (arrangementid, warehousecreateddate) REFERENCES arrangement.arrangement (arrangementid, warehousecreateddate);


--
-- TOC entry 5353 (class 2606 OID 213306)
-- Name: arrangementtypesecuritytoken fkj02k0j5pke3dkpaxhbgmaikqi; Type: FK CONSTRAINT; Schema: arrangement; Owner: postgres
--

ALTER TABLE arrangement.arrangementtypesecuritytoken
    ADD CONSTRAINT fkj02k0j5pke3dkpaxhbgmaikqi FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 5391 (class 2606 OID 213491)
-- Name: arrangementxarrangementtypesecuritytoken fkk5vha5aon2i7roy6wox5r8k1d; Type: FK CONSTRAINT; Schema: arrangement; Owner: postgres
--

ALTER TABLE arrangement.arrangementxarrangementtypesecuritytoken
    ADD CONSTRAINT fkk5vha5aon2i7roy6wox5r8k1d FOREIGN KEY (securitytokenid, warehousecreateddate) REFERENCES security.securitytoken (securitytokenid, warehousecreateddate);


--
-- TOC entry 5429 (class 2606 OID 213681)
-- Name: arrangementxproductsecuritytoken fkkfajnpjwfuc4kt13avqmjs27i; Type: FK CONSTRAINT; Schema: arrangement; Owner: postgres
--

ALTER TABLE arrangement.arrangementxproductsecuritytoken
    ADD CONSTRAINT fkkfajnpjwfuc4kt13avqmjs27i FOREIGN KEY (securitytokenid, warehousecreateddate) REFERENCES security.securitytoken (securitytokenid, warehousecreateddate);


--
-- TOC entry 5371 (class 2606 OID 213391)
-- Name: arrangementxarrangement fkkg32gfwya6793p80k1no6hmk8; Type: FK CONSTRAINT; Schema: arrangement; Owner: postgres
--

ALTER TABLE arrangement.arrangementxarrangement
    ADD CONSTRAINT fkkg32gfwya6793p80k1no6hmk8 FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 5365 (class 2606 OID 213351)
-- Name: arrangementtypexclassificationsecuritytoken fkko0edkl361kq6uwstfhcmp1e7; Type: FK CONSTRAINT; Schema: arrangement; Owner: postgres
--

ALTER TABLE arrangement.arrangementtypexclassificationsecuritytoken
    ADD CONSTRAINT fkko0edkl361kq6uwstfhcmp1e7 FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 5411 (class 2606 OID 213596)
-- Name: arrangementxinvolvedparty fkkpvqwygo5551679i6hds7kada; Type: FK CONSTRAINT; Schema: arrangement; Owner: postgres
--

ALTER TABLE arrangement.arrangementxinvolvedparty
    ADD CONSTRAINT fkkpvqwygo5551679i6hds7kada FOREIGN KEY (involvedpartyid, warehousecreateddate) REFERENCES party.involvedparty (involvedpartyid, warehousecreateddate);


--
-- TOC entry 5413 (class 2606 OID 213626)
-- Name: arrangementxinvolvedpartysecuritytoken fkkyktyh5uldr0apnx4fb7rqqhj; Type: FK CONSTRAINT; Schema: arrangement; Owner: postgres
--

ALTER TABLE arrangement.arrangementxinvolvedpartysecuritytoken
    ADD CONSTRAINT fkkyktyh5uldr0apnx4fb7rqqhj FOREIGN KEY (arrangementxinvolvedpartyid, warehousecreateddate) REFERENCES arrangement.arrangementxinvolvedparty (arrangementxinvolvedpartyid, warehousecreateddate);


--
-- TOC entry 5447 (class 2606 OID 213786)
-- Name: arrangementxrules fkl1taisa4493ad6n8troarks3c; Type: FK CONSTRAINT; Schema: arrangement; Owner: postgres
--

ALTER TABLE arrangement.arrangementxrules
    ADD CONSTRAINT fkl1taisa4493ad6n8troarks3c FOREIGN KEY (arrangementid, warehousecreateddate) REFERENCES arrangement.arrangement (arrangementid, warehousecreateddate);


--
-- TOC entry 5358 (class 2606 OID 213331)
-- Name: arrangementtypexclassification fklexxdvodllklvfn0ua4r0aw0u; Type: FK CONSTRAINT; Schema: arrangement; Owner: postgres
--

ALTER TABLE arrangement.arrangementtypexclassification
    ADD CONSTRAINT fklexxdvodllklvfn0ua4r0aw0u FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 5359 (class 2606 OID 213316)
-- Name: arrangementtypexclassification fklne0s6fbh4wdnpnvija69e2d8; Type: FK CONSTRAINT; Schema: arrangement; Owner: postgres
--

ALTER TABLE arrangement.arrangementtypexclassification
    ADD CONSTRAINT fklne0s6fbh4wdnpnvija69e2d8 FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 5346 (class 2606 OID 213276)
-- Name: arrangementtype fkm3ev8du7vdvw2rislo3gxsvv9; Type: FK CONSTRAINT; Schema: arrangement; Owner: postgres
--

ALTER TABLE arrangement.arrangementtype
    ADD CONSTRAINT fkm3ev8du7vdvw2rislo3gxsvv9 FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 5404 (class 2606 OID 213546)
-- Name: arrangementxclassificationsecuritytoken fkmxvd2rnt5882sxxk831sdbkqq; Type: FK CONSTRAINT; Schema: arrangement; Owner: postgres
--

ALTER TABLE arrangement.arrangementxclassificationsecuritytoken
    ADD CONSTRAINT fkmxvd2rnt5882sxxk831sdbkqq FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 5441 (class 2606 OID 213751)
-- Name: arrangementxresourceitemsecuritytoken fkn7ohgt9uwimsmg8b1kcclgymq; Type: FK CONSTRAINT; Schema: arrangement; Owner: postgres
--

ALTER TABLE arrangement.arrangementxresourceitemsecuritytoken
    ADD CONSTRAINT fkn7ohgt9uwimsmg8b1kcclgymq FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 5340 (class 2606 OID 213246)
-- Name: arrangementsecuritytoken fkngh7orqtfcriw1bpc7ctcu9ay; Type: FK CONSTRAINT; Schema: arrangement; Owner: postgres
--

ALTER TABLE arrangement.arrangementsecuritytoken
    ADD CONSTRAINT fkngh7orqtfcriw1bpc7ctcu9ay FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 5414 (class 2606 OID 213606)
-- Name: arrangementxinvolvedpartysecuritytoken fknhcqo7dtttikbygsv29vbxw64; Type: FK CONSTRAINT; Schema: arrangement; Owner: postgres
--

ALTER TABLE arrangement.arrangementxinvolvedpartysecuritytoken
    ADD CONSTRAINT fknhcqo7dtttikbygsv29vbxw64 FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 5341 (class 2606 OID 213251)
-- Name: arrangementsecuritytoken fknjnehgnp3ybq6wcnfbx6clqi8; Type: FK CONSTRAINT; Schema: arrangement; Owner: postgres
--

ALTER TABLE arrangement.arrangementsecuritytoken
    ADD CONSTRAINT fknjnehgnp3ybq6wcnfbx6clqi8 FOREIGN KEY (securitytokenid, warehousecreateddate) REFERENCES security.securitytoken (securitytokenid, warehousecreateddate);


--
-- TOC entry 5347 (class 2606 OID 213266)
-- Name: arrangementtype fknk2gclr0bkbxp3dvg0yboy59o; Type: FK CONSTRAINT; Schema: arrangement; Owner: postgres
--

ALTER TABLE arrangement.arrangementtype
    ADD CONSTRAINT fknk2gclr0bkbxp3dvg0yboy59o FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 5430 (class 2606 OID 213686)
-- Name: arrangementxproductsecuritytoken fknp4l2srl6e09175ngloxrexer; Type: FK CONSTRAINT; Schema: arrangement; Owner: postgres
--

ALTER TABLE arrangement.arrangementxproductsecuritytoken
    ADD CONSTRAINT fknp4l2srl6e09175ngloxrexer FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 5338 (class 2606 OID 213226)
-- Name: arrangement fko7k9jfu2q1gdbwkhan19cjg6r; Type: FK CONSTRAINT; Schema: arrangement; Owner: postgres
--

ALTER TABLE arrangement.arrangement
    ADD CONSTRAINT fko7k9jfu2q1gdbwkhan19cjg6r FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 5342 (class 2606 OID 213241)
-- Name: arrangementsecuritytoken fkodrogs5m3dhd54wsy89161ika; Type: FK CONSTRAINT; Schema: arrangement; Owner: postgres
--

ALTER TABLE arrangement.arrangementsecuritytoken
    ADD CONSTRAINT fkodrogs5m3dhd54wsy89161ika FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 5442 (class 2606 OID 213741)
-- Name: arrangementxresourceitemsecuritytoken fkofagwms8gkw6geqmiyncnj2cx; Type: FK CONSTRAINT; Schema: arrangement; Owner: postgres
--

ALTER TABLE arrangement.arrangementxresourceitemsecuritytoken
    ADD CONSTRAINT fkofagwms8gkw6geqmiyncnj2cx FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 5384 (class 2606 OID 213456)
-- Name: arrangementxarrangementtype fkofvbeuabms8ebk75wsgnkjbld; Type: FK CONSTRAINT; Schema: arrangement; Owner: postgres
--

ALTER TABLE arrangement.arrangementxarrangementtype
    ADD CONSTRAINT fkofvbeuabms8ebk75wsgnkjbld FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 5443 (class 2606 OID 213736)
-- Name: arrangementxresourceitemsecuritytoken fkotwjcy38jqowrom4f1t9u4sxv; Type: FK CONSTRAINT; Schema: arrangement; Owner: postgres
--

ALTER TABLE arrangement.arrangementxresourceitemsecuritytoken
    ADD CONSTRAINT fkotwjcy38jqowrom4f1t9u4sxv FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 5462 (class 2606 OID 213846)
-- Name: arrangementxrulestype fkov2gqdwxiyblbpqmnwc4np7x9; Type: FK CONSTRAINT; Schema: arrangement; Owner: postgres
--

ALTER TABLE arrangement.arrangementxrulestype
    ADD CONSTRAINT fkov2gqdwxiyblbpqmnwc4np7x9 FOREIGN KEY (classificationid, warehousecreateddate) REFERENCES classification.classification (classificationid, warehousecreateddate);


--
-- TOC entry 5372 (class 2606 OID 213396)
-- Name: arrangementxarrangement fkovmdueeas3icmh2sa2j64g4ck; Type: FK CONSTRAINT; Schema: arrangement; Owner: postgres
--

ALTER TABLE arrangement.arrangementxarrangement
    ADD CONSTRAINT fkovmdueeas3icmh2sa2j64g4ck FOREIGN KEY (classificationid, warehousecreateddate) REFERENCES classification.classification (classificationid, warehousecreateddate);


--
-- TOC entry 5354 (class 2606 OID 213286)
-- Name: arrangementtypesecuritytoken fkp3x2trigpgx2f2q7h92n2tlr2; Type: FK CONSTRAINT; Schema: arrangement; Owner: postgres
--

ALTER TABLE arrangement.arrangementtypesecuritytoken
    ADD CONSTRAINT fkp3x2trigpgx2f2q7h92n2tlr2 FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 5415 (class 2606 OID 213621)
-- Name: arrangementxinvolvedpartysecuritytoken fkp9gl2xg7lrargsi7wloadqsxa; Type: FK CONSTRAINT; Schema: arrangement; Owner: postgres
--

ALTER TABLE arrangement.arrangementxinvolvedpartysecuritytoken
    ADD CONSTRAINT fkp9gl2xg7lrargsi7wloadqsxa FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 5366 (class 2606 OID 213346)
-- Name: arrangementtypexclassificationsecuritytoken fkpe27j1xif8mx3f5q5dh6o41an; Type: FK CONSTRAINT; Schema: arrangement; Owner: postgres
--

ALTER TABLE arrangement.arrangementtypexclassificationsecuritytoken
    ADD CONSTRAINT fkpe27j1xif8mx3f5q5dh6o41an FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 5360 (class 2606 OID 213321)
-- Name: arrangementtypexclassification fkpi8wetfnfovvirlmsfvhu89bh; Type: FK CONSTRAINT; Schema: arrangement; Owner: postgres
--

ALTER TABLE arrangement.arrangementtypexclassification
    ADD CONSTRAINT fkpi8wetfnfovvirlmsfvhu89bh FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 5435 (class 2606 OID 213696)
-- Name: arrangementxresourceitem fkpj4eok42bmisj8g6r7crx0er5; Type: FK CONSTRAINT; Schema: arrangement; Owner: postgres
--

ALTER TABLE arrangement.arrangementxresourceitem
    ADD CONSTRAINT fkpj4eok42bmisj8g6r7crx0er5 FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 5436 (class 2606 OID 213711)
-- Name: arrangementxresourceitem fkpjx2bwph3f67laisvffrnopd8; Type: FK CONSTRAINT; Schema: arrangement; Owner: postgres
--

ALTER TABLE arrangement.arrangementxresourceitem
    ADD CONSTRAINT fkpjx2bwph3f67laisvffrnopd8 FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 5397 (class 2606 OID 213521)
-- Name: arrangementxclassification fkpwmnjntxrxxp88ohtix3f9rld; Type: FK CONSTRAINT; Schema: arrangement; Owner: postgres
--

ALTER TABLE arrangement.arrangementxclassification
    ADD CONSTRAINT fkpwmnjntxrxxp88ohtix3f9rld FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 5463 (class 2606 OID 213836)
-- Name: arrangementxrulestype fkq01c7ongg1cofa4ceibxduma2; Type: FK CONSTRAINT; Schema: arrangement; Owner: postgres
--

ALTER TABLE arrangement.arrangementxrulestype
    ADD CONSTRAINT fkq01c7ongg1cofa4ceibxduma2 FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 5392 (class 2606 OID 213486)
-- Name: arrangementxarrangementtypesecuritytoken fkqi48u6i84yjq1jg357fh1qwgi; Type: FK CONSTRAINT; Schema: arrangement; Owner: postgres
--

ALTER TABLE arrangement.arrangementxarrangementtypesecuritytoken
    ADD CONSTRAINT fkqi48u6i84yjq1jg357fh1qwgi FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 5416 (class 2606 OID 213601)
-- Name: arrangementxinvolvedpartysecuritytoken fkqmjll31vsk7uwahn932wd5vob; Type: FK CONSTRAINT; Schema: arrangement; Owner: postgres
--

ALTER TABLE arrangement.arrangementxinvolvedpartysecuritytoken
    ADD CONSTRAINT fkqmjll31vsk7uwahn932wd5vob FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 5398 (class 2606 OID 213516)
-- Name: arrangementxclassification fkqryq2ja6bavkj7a5bj7w081kp; Type: FK CONSTRAINT; Schema: arrangement; Owner: postgres
--

ALTER TABLE arrangement.arrangementxclassification
    ADD CONSTRAINT fkqryq2ja6bavkj7a5bj7w081kp FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 5417 (class 2606 OID 213616)
-- Name: arrangementxinvolvedpartysecuritytoken fkqup67qfcgs10hc0x651xne7t9; Type: FK CONSTRAINT; Schema: arrangement; Owner: postgres
--

ALTER TABLE arrangement.arrangementxinvolvedpartysecuritytoken
    ADD CONSTRAINT fkqup67qfcgs10hc0x651xne7t9 FOREIGN KEY (securitytokenid, warehousecreateddate) REFERENCES security.securitytoken (securitytokenid, warehousecreateddate);


--
-- TOC entry 5385 (class 2606 OID 213446)
-- Name: arrangementxarrangementtype fkqxy44j74nhe3dhfwf595lguwl; Type: FK CONSTRAINT; Schema: arrangement; Owner: postgres
--

ALTER TABLE arrangement.arrangementxarrangementtype
    ADD CONSTRAINT fkqxy44j74nhe3dhfwf595lguwl FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 5448 (class 2606 OID 213771)
-- Name: arrangementxrules fkqyb5d3jb3i9rsxm0cfo208njj; Type: FK CONSTRAINT; Schema: arrangement; Owner: postgres
--

ALTER TABLE arrangement.arrangementxrules
    ADD CONSTRAINT fkqyb5d3jb3i9rsxm0cfo208njj FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 5437 (class 2606 OID 213701)
-- Name: arrangementxresourceitem fkrc9mg4osgj3ye4dab54n96ttq; Type: FK CONSTRAINT; Schema: arrangement; Owner: postgres
--

ALTER TABLE arrangement.arrangementxresourceitem
    ADD CONSTRAINT fkrc9mg4osgj3ye4dab54n96ttq FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 5343 (class 2606 OID 213261)
-- Name: arrangementsecuritytoken fkrhnkn0cyirv36wdfbx81p2omn; Type: FK CONSTRAINT; Schema: arrangement; Owner: postgres
--

ALTER TABLE arrangement.arrangementsecuritytoken
    ADD CONSTRAINT fkrhnkn0cyirv36wdfbx81p2omn FOREIGN KEY (arrangementid, warehousecreateddate) REFERENCES arrangement.arrangement (arrangementid, warehousecreateddate);


--
-- TOC entry 5348 (class 2606 OID 213271)
-- Name: arrangementtype fkro0fvyrds9erw2wk2s6gderhd; Type: FK CONSTRAINT; Schema: arrangement; Owner: postgres
--

ALTER TABLE arrangement.arrangementtype
    ADD CONSTRAINT fkro0fvyrds9erw2wk2s6gderhd FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 5449 (class 2606 OID 213781)
-- Name: arrangementxrules fkrt9gl1ke0yp5rgncnsr0t2x2m; Type: FK CONSTRAINT; Schema: arrangement; Owner: postgres
--

ALTER TABLE arrangement.arrangementxrules
    ADD CONSTRAINT fkrt9gl1ke0yp5rgncnsr0t2x2m FOREIGN KEY (classificationid, warehousecreateddate) REFERENCES classification.classification (classificationid, warehousecreateddate);


--
-- TOC entry 5386 (class 2606 OID 213461)
-- Name: arrangementxarrangementtype fkshy3d2vx1xpatx6rnu60ag7jg; Type: FK CONSTRAINT; Schema: arrangement; Owner: postgres
--

ALTER TABLE arrangement.arrangementxarrangementtype
    ADD CONSTRAINT fkshy3d2vx1xpatx6rnu60ag7jg FOREIGN KEY (classificationid, warehousecreateddate) REFERENCES classification.classification (classificationid, warehousecreateddate);


--
-- TOC entry 5450 (class 2606 OID 213791)
-- Name: arrangementxrules fksp0nolvpdwpwmjyghyp486frp; Type: FK CONSTRAINT; Schema: arrangement; Owner: postgres
--

ALTER TABLE arrangement.arrangementxrules
    ADD CONSTRAINT fksp0nolvpdwpwmjyghyp486frp FOREIGN KEY (rulesid, warehousecreateddate) REFERENCES rules.rules (rulesid, warehousecreateddate);


--
-- TOC entry 5373 (class 2606 OID 213386)
-- Name: arrangementxarrangement fksw7clyy90t99djoxclxg646im; Type: FK CONSTRAINT; Schema: arrangement; Owner: postgres
--

ALTER TABLE arrangement.arrangementxarrangement
    ADD CONSTRAINT fksw7clyy90t99djoxclxg646im FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 5424 (class 2606 OID 213631)
-- Name: arrangementxproduct fkt25tk83urovuvrxc874cm07hh; Type: FK CONSTRAINT; Schema: arrangement; Owner: postgres
--

ALTER TABLE arrangement.arrangementxproduct
    ADD CONSTRAINT fkt25tk83urovuvrxc874cm07hh FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 5344 (class 2606 OID 213236)
-- Name: arrangementsecuritytoken fkt5ibl9ewo11erwy04qt07riv3; Type: FK CONSTRAINT; Schema: arrangement; Owner: postgres
--

ALTER TABLE arrangement.arrangementsecuritytoken
    ADD CONSTRAINT fkt5ibl9ewo11erwy04qt07riv3 FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 4473 (class 2606 OID 206253)
-- Name: classificationdataconceptsecuritytoken fk104e6i5qwqmf9codleb3r0sq8; Type: FK CONSTRAINT; Schema: classification; Owner: postgres
--

ALTER TABLE classification.classificationdataconceptsecuritytoken
    ADD CONSTRAINT fk104e6i5qwqmf9codleb3r0sq8 FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4485 (class 2606 OID 206258)
-- Name: classificationdataconceptxclassificationsecuritytoken fk11pvel7kmbcntj5cllihder1x; Type: FK CONSTRAINT; Schema: classification; Owner: postgres
--

ALTER TABLE classification.classificationdataconceptxclassificationsecuritytoken
    ADD CONSTRAINT fk11pvel7kmbcntj5cllihder1x FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 4498 (class 2606 OID 206263)
-- Name: classificationdataconceptxresourceitemsecuritytoken fk18b70ps0o5g4y7eo5awrlls3v; Type: FK CONSTRAINT; Schema: classification; Owner: postgres
--

ALTER TABLE classification.classificationdataconceptxresourceitemsecuritytoken
    ADD CONSTRAINT fk18b70ps0o5g4y7eo5awrlls3v FOREIGN KEY (classificationdataconceptxresourceitemid, warehousecreateddate) REFERENCES classification.classificationdataconceptxresourceitem (classificationdataconceptxresourceitemid, warehousecreateddate);


--
-- TOC entry 4486 (class 2606 OID 206268)
-- Name: classificationdataconceptxclassificationsecuritytoken fk1x5i621v0xt7a60cvh4w88fu5; Type: FK CONSTRAINT; Schema: classification; Owner: postgres
--

ALTER TABLE classification.classificationdataconceptxclassificationsecuritytoken
    ADD CONSTRAINT fk1x5i621v0xt7a60cvh4w88fu5 FOREIGN KEY (classificationdataconceptxclassificationid, warehousecreateddate) REFERENCES classification.classificationdataconceptxclassification (classificationdataconceptxclassificationid, warehousecreateddate);


--
-- TOC entry 4529 (class 2606 OID 206273)
-- Name: classificationxresourceitemsecuritytoken fk202whr6s26bncotttl943yl80; Type: FK CONSTRAINT; Schema: classification; Owner: postgres
--

ALTER TABLE classification.classificationxresourceitemsecuritytoken
    ADD CONSTRAINT fk202whr6s26bncotttl943yl80 FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 4491 (class 2606 OID 206278)
-- Name: classificationdataconceptxresourceitem fk26e3u9h3fcxpwbormhncoee4p; Type: FK CONSTRAINT; Schema: classification; Owner: postgres
--

ALTER TABLE classification.classificationdataconceptxresourceitem
    ADD CONSTRAINT fk26e3u9h3fcxpwbormhncoee4p FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 4499 (class 2606 OID 206283)
-- Name: classificationdataconceptxresourceitemsecuritytoken fk31ifcku5xuo9j2f7nyvsheivg; Type: FK CONSTRAINT; Schema: classification; Owner: postgres
--

ALTER TABLE classification.classificationdataconceptxresourceitemsecuritytoken
    ADD CONSTRAINT fk31ifcku5xuo9j2f7nyvsheivg FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 4517 (class 2606 OID 206288)
-- Name: classificationxclassificationsecuritytoken fk382so754rj84e0moqjj7q6cmd; Type: FK CONSTRAINT; Schema: classification; Owner: postgres
--

ALTER TABLE classification.classificationxclassificationsecuritytoken
    ADD CONSTRAINT fk382so754rj84e0moqjj7q6cmd FOREIGN KEY (classificationxclassificationid, warehousecreateddate) REFERENCES classification.classificationxclassification (classificationxclassificationid, warehousecreateddate);


--
-- TOC entry 4504 (class 2606 OID 206293)
-- Name: classificationsecuritytoken fk3pyb4fg9krm6cs8gyymn96d1b; Type: FK CONSTRAINT; Schema: classification; Owner: postgres
--

ALTER TABLE classification.classificationsecuritytoken
    ADD CONSTRAINT fk3pyb4fg9krm6cs8gyymn96d1b FOREIGN KEY (classificationid, warehousecreateddate) REFERENCES classification.classification (classificationid, warehousecreateddate);


--
-- TOC entry 4523 (class 2606 OID 206298)
-- Name: classificationxresourceitem fk41ad965y444vlu7ib0yr8qf96; Type: FK CONSTRAINT; Schema: classification; Owner: postgres
--

ALTER TABLE classification.classificationxresourceitem
    ADD CONSTRAINT fk41ad965y444vlu7ib0yr8qf96 FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4479 (class 2606 OID 206303)
-- Name: classificationdataconceptxclassification fk4wh9c6d6snotj0cdns7wdojty; Type: FK CONSTRAINT; Schema: classification; Owner: postgres
--

ALTER TABLE classification.classificationdataconceptxclassification
    ADD CONSTRAINT fk4wh9c6d6snotj0cdns7wdojty FOREIGN KEY (classificationdataconceptid, warehousecreateddate) REFERENCES classification.classificationdataconcept (classificationdataconceptid, warehousecreateddate);


--
-- TOC entry 4510 (class 2606 OID 206308)
-- Name: classificationxclassification fk5lxvsymahaue1a1gx87ll8w9t; Type: FK CONSTRAINT; Schema: classification; Owner: postgres
--

ALTER TABLE classification.classificationxclassification
    ADD CONSTRAINT fk5lxvsymahaue1a1gx87ll8w9t FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4487 (class 2606 OID 206313)
-- Name: classificationdataconceptxclassificationsecuritytoken fk60x6yebwf17dn491shf6aobv2; Type: FK CONSTRAINT; Schema: classification; Owner: postgres
--

ALTER TABLE classification.classificationdataconceptxclassificationsecuritytoken
    ADD CONSTRAINT fk60x6yebwf17dn491shf6aobv2 FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 4480 (class 2606 OID 206318)
-- Name: classificationdataconceptxclassification fk6p7cqsuyxor559t3sfbxvsrsh; Type: FK CONSTRAINT; Schema: classification; Owner: postgres
--

ALTER TABLE classification.classificationdataconceptxclassification
    ADD CONSTRAINT fk6p7cqsuyxor559t3sfbxvsrsh FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 4492 (class 2606 OID 206323)
-- Name: classificationdataconceptxresourceitem fk70uhig9qru3lmqnjq9yfgsddn; Type: FK CONSTRAINT; Schema: classification; Owner: postgres
--

ALTER TABLE classification.classificationdataconceptxresourceitem
    ADD CONSTRAINT fk70uhig9qru3lmqnjq9yfgsddn FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4469 (class 2606 OID 206328)
-- Name: classificationdataconcept fk7anrd6m4u7jmgosom1ov3sup6; Type: FK CONSTRAINT; Schema: classification; Owner: postgres
--

ALTER TABLE classification.classificationdataconcept
    ADD CONSTRAINT fk7anrd6m4u7jmgosom1ov3sup6 FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4493 (class 2606 OID 206333)
-- Name: classificationdataconceptxresourceitem fk7cngjs2bvw18p1j29fg62svn3; Type: FK CONSTRAINT; Schema: classification; Owner: postgres
--

ALTER TABLE classification.classificationdataconceptxresourceitem
    ADD CONSTRAINT fk7cngjs2bvw18p1j29fg62svn3 FOREIGN KEY (resourceitemid, warehousecreateddate) REFERENCES resource.resourceitem (resourceitemid, warehousecreateddate);


--
-- TOC entry 4488 (class 2606 OID 206338)
-- Name: classificationdataconceptxclassificationsecuritytoken fk86t8j5lcn4fxi8km9e839mn76; Type: FK CONSTRAINT; Schema: classification; Owner: postgres
--

ALTER TABLE classification.classificationdataconceptxclassificationsecuritytoken
    ADD CONSTRAINT fk86t8j5lcn4fxi8km9e839mn76 FOREIGN KEY (securitytokenid, warehousecreateddate) REFERENCES security.securitytoken (securitytokenid, warehousecreateddate);


--
-- TOC entry 4464 (class 2606 OID 206343)
-- Name: classification fk8cywgvl24kx13sapgd5ppnxgn; Type: FK CONSTRAINT; Schema: classification; Owner: postgres
--

ALTER TABLE classification.classification
    ADD CONSTRAINT fk8cywgvl24kx13sapgd5ppnxgn FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 4481 (class 2606 OID 206348)
-- Name: classificationdataconceptxclassification fk8ri5ojfslqput3614qfxh89ko; Type: FK CONSTRAINT; Schema: classification; Owner: postgres
--

ALTER TABLE classification.classificationdataconceptxclassification
    ADD CONSTRAINT fk8ri5ojfslqput3614qfxh89ko FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 4465 (class 2606 OID 206353)
-- Name: classification fk977a6joatob09p0ati780wx3i; Type: FK CONSTRAINT; Schema: classification; Owner: postgres
--

ALTER TABLE classification.classification
    ADD CONSTRAINT fk977a6joatob09p0ati780wx3i FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4511 (class 2606 OID 206358)
-- Name: classificationxclassification fka0edqa9iht684osp3290grh5j; Type: FK CONSTRAINT; Schema: classification; Owner: postgres
--

ALTER TABLE classification.classificationxclassification
    ADD CONSTRAINT fka0edqa9iht684osp3290grh5j FOREIGN KEY (classificationid, warehousecreateddate) REFERENCES classification.classification (classificationid, warehousecreateddate);


--
-- TOC entry 4474 (class 2606 OID 206363)
-- Name: classificationdataconceptsecuritytoken fka96umbw2pt2xsr7dpr7rg59sb; Type: FK CONSTRAINT; Schema: classification; Owner: postgres
--

ALTER TABLE classification.classificationdataconceptsecuritytoken
    ADD CONSTRAINT fka96umbw2pt2xsr7dpr7rg59sb FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 4475 (class 2606 OID 206368)
-- Name: classificationdataconceptsecuritytoken fkaet7sbnxoa25lvh4fttjxq241; Type: FK CONSTRAINT; Schema: classification; Owner: postgres
--

ALTER TABLE classification.classificationdataconceptsecuritytoken
    ADD CONSTRAINT fkaet7sbnxoa25lvh4fttjxq241 FOREIGN KEY (classificationdataconceptid, warehousecreateddate) REFERENCES classification.classificationdataconcept (classificationdataconceptid, warehousecreateddate);


--
-- TOC entry 4482 (class 2606 OID 206373)
-- Name: classificationdataconceptxclassification fkalb9m4tw81jr1nrghxwnhgbsm; Type: FK CONSTRAINT; Schema: classification; Owner: postgres
--

ALTER TABLE classification.classificationdataconceptxclassification
    ADD CONSTRAINT fkalb9m4tw81jr1nrghxwnhgbsm FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4505 (class 2606 OID 206378)
-- Name: classificationsecuritytoken fkalw5xpd22jfxd5le05m2baego; Type: FK CONSTRAINT; Schema: classification; Owner: postgres
--

ALTER TABLE classification.classificationsecuritytoken
    ADD CONSTRAINT fkalw5xpd22jfxd5le05m2baego FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4506 (class 2606 OID 206383)
-- Name: classificationsecuritytoken fkarnkwlgj9y3433pngmhg39j5f; Type: FK CONSTRAINT; Schema: classification; Owner: postgres
--

ALTER TABLE classification.classificationsecuritytoken
    ADD CONSTRAINT fkarnkwlgj9y3433pngmhg39j5f FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4494 (class 2606 OID 206388)
-- Name: classificationdataconceptxresourceitem fkb3i6n3mk43lmc47412g00l07p; Type: FK CONSTRAINT; Schema: classification; Owner: postgres
--

ALTER TABLE classification.classificationdataconceptxresourceitem
    ADD CONSTRAINT fkb3i6n3mk43lmc47412g00l07p FOREIGN KEY (classificationdataconceptid, warehousecreateddate) REFERENCES classification.classificationdataconcept (classificationdataconceptid, warehousecreateddate);


--
-- TOC entry 4483 (class 2606 OID 206393)
-- Name: classificationdataconceptxclassification fkbkxw52ao5d3w2lww1wcgwc6r8; Type: FK CONSTRAINT; Schema: classification; Owner: postgres
--

ALTER TABLE classification.classificationdataconceptxclassification
    ADD CONSTRAINT fkbkxw52ao5d3w2lww1wcgwc6r8 FOREIGN KEY (classificationid, warehousecreateddate) REFERENCES classification.classification (classificationid, warehousecreateddate);


--
-- TOC entry 4512 (class 2606 OID 206398)
-- Name: classificationxclassification fkbutwoudrgvo3cbjn39cd3ftn1; Type: FK CONSTRAINT; Schema: classification; Owner: postgres
--

ALTER TABLE classification.classificationxclassification
    ADD CONSTRAINT fkbutwoudrgvo3cbjn39cd3ftn1 FOREIGN KEY (parentclassificationid, warehousecreateddate) REFERENCES classification.classification (classificationid, warehousecreateddate);


--
-- TOC entry 4495 (class 2606 OID 206403)
-- Name: classificationdataconceptxresourceitem fkbxuns49b2h4ymjifep6bup0y7; Type: FK CONSTRAINT; Schema: classification; Owner: postgres
--

ALTER TABLE classification.classificationdataconceptxresourceitem
    ADD CONSTRAINT fkbxuns49b2h4ymjifep6bup0y7 FOREIGN KEY (classificationid, warehousecreateddate) REFERENCES classification.classification (classificationid, warehousecreateddate);


--
-- TOC entry 4466 (class 2606 OID 206408)
-- Name: classification fkdameu9rwq0ndi6g9pnasalwf2; Type: FK CONSTRAINT; Schema: classification; Owner: postgres
--

ALTER TABLE classification.classification
    ADD CONSTRAINT fkdameu9rwq0ndi6g9pnasalwf2 FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4476 (class 2606 OID 206413)
-- Name: classificationdataconceptsecuritytoken fkdj2s9qftq2ybskhst5rh7kvcs; Type: FK CONSTRAINT; Schema: classification; Owner: postgres
--

ALTER TABLE classification.classificationdataconceptsecuritytoken
    ADD CONSTRAINT fkdj2s9qftq2ybskhst5rh7kvcs FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4524 (class 2606 OID 206418)
-- Name: classificationxresourceitem fkg9sk8egvr7m48ajxuq08kr36a; Type: FK CONSTRAINT; Schema: classification; Owner: postgres
--

ALTER TABLE classification.classificationxresourceitem
    ADD CONSTRAINT fkg9sk8egvr7m48ajxuq08kr36a FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 4489 (class 2606 OID 206423)
-- Name: classificationdataconceptxclassificationsecuritytoken fkglkin1i2uv5gp9dlk1sbgmulg; Type: FK CONSTRAINT; Schema: classification; Owner: postgres
--

ALTER TABLE classification.classificationdataconceptxclassificationsecuritytoken
    ADD CONSTRAINT fkglkin1i2uv5gp9dlk1sbgmulg FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4525 (class 2606 OID 206428)
-- Name: classificationxresourceitem fkgm9umwfpdxs1ao0jb2ye8t4qi; Type: FK CONSTRAINT; Schema: classification; Owner: postgres
--

ALTER TABLE classification.classificationxresourceitem
    ADD CONSTRAINT fkgm9umwfpdxs1ao0jb2ye8t4qi FOREIGN KEY (classificationid, warehousecreateddate) REFERENCES classification.classification (classificationid, warehousecreateddate);


--
-- TOC entry 4513 (class 2606 OID 206433)
-- Name: classificationxclassification fkgt5llwl8oh4q2fo4fv9vfmui2; Type: FK CONSTRAINT; Schema: classification; Owner: postgres
--

ALTER TABLE classification.classificationxclassification
    ADD CONSTRAINT fkgt5llwl8oh4q2fo4fv9vfmui2 FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 4518 (class 2606 OID 206438)
-- Name: classificationxclassificationsecuritytoken fkidlqquwljda1kte0kjjjkkopi; Type: FK CONSTRAINT; Schema: classification; Owner: postgres
--

ALTER TABLE classification.classificationxclassificationsecuritytoken
    ADD CONSTRAINT fkidlqquwljda1kte0kjjjkkopi FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 4526 (class 2606 OID 206443)
-- Name: classificationxresourceitem fkisdanawdo1rw8v7t1jks92pgf; Type: FK CONSTRAINT; Schema: classification; Owner: postgres
--

ALTER TABLE classification.classificationxresourceitem
    ADD CONSTRAINT fkisdanawdo1rw8v7t1jks92pgf FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 4470 (class 2606 OID 206448)
-- Name: classificationdataconcept fkjjkh1tgw58ebgjyfhs55uq0bc; Type: FK CONSTRAINT; Schema: classification; Owner: postgres
--

ALTER TABLE classification.classificationdataconcept
    ADD CONSTRAINT fkjjkh1tgw58ebgjyfhs55uq0bc FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 4500 (class 2606 OID 206453)
-- Name: classificationdataconceptxresourceitemsecuritytoken fkk0lwglw1ekrh07d1rp0qnt3v9; Type: FK CONSTRAINT; Schema: classification; Owner: postgres
--

ALTER TABLE classification.classificationdataconceptxresourceitemsecuritytoken
    ADD CONSTRAINT fkk0lwglw1ekrh07d1rp0qnt3v9 FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4496 (class 2606 OID 206458)
-- Name: classificationdataconceptxresourceitem fkk1tllh3pw17bo91f98qajwv18; Type: FK CONSTRAINT; Schema: classification; Owner: postgres
--

ALTER TABLE classification.classificationdataconceptxresourceitem
    ADD CONSTRAINT fkk1tllh3pw17bo91f98qajwv18 FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 4519 (class 2606 OID 206463)
-- Name: classificationxclassificationsecuritytoken fkktsowuwqxuk0snthieryx7pny; Type: FK CONSTRAINT; Schema: classification; Owner: postgres
--

ALTER TABLE classification.classificationxclassificationsecuritytoken
    ADD CONSTRAINT fkktsowuwqxuk0snthieryx7pny FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 4527 (class 2606 OID 206468)
-- Name: classificationxresourceitem fklhxbcwpk3r33hdnpbirj5q8e0; Type: FK CONSTRAINT; Schema: classification; Owner: postgres
--

ALTER TABLE classification.classificationxresourceitem
    ADD CONSTRAINT fklhxbcwpk3r33hdnpbirj5q8e0 FOREIGN KEY (resourceitemid, warehousecreateddate) REFERENCES resource.resourceitem (resourceitemid, warehousecreateddate);


--
-- TOC entry 4514 (class 2606 OID 206473)
-- Name: classificationxclassification fklolls1qwf58fhw1lo6plg1u2d; Type: FK CONSTRAINT; Schema: classification; Owner: postgres
--

ALTER TABLE classification.classificationxclassification
    ADD CONSTRAINT fklolls1qwf58fhw1lo6plg1u2d FOREIGN KEY (childclassificationid, warehousecreateddate) REFERENCES classification.classification (classificationid, warehousecreateddate);


--
-- TOC entry 4497 (class 2606 OID 206478)
-- Name: classificationdataconceptxresourceitem fkm5et8roankenbjliy9qigclab; Type: FK CONSTRAINT; Schema: classification; Owner: postgres
--

ALTER TABLE classification.classificationdataconceptxresourceitem
    ADD CONSTRAINT fkm5et8roankenbjliy9qigclab FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4515 (class 2606 OID 206483)
-- Name: classificationxclassification fkmdyinn1kd9lrjajlts2832kgp; Type: FK CONSTRAINT; Schema: classification; Owner: postgres
--

ALTER TABLE classification.classificationxclassification
    ADD CONSTRAINT fkmdyinn1kd9lrjajlts2832kgp FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 4530 (class 2606 OID 206488)
-- Name: classificationxresourceitemsecuritytoken fkmxlv95qskrx8l20xdjs55gxwy; Type: FK CONSTRAINT; Schema: classification; Owner: postgres
--

ALTER TABLE classification.classificationxresourceitemsecuritytoken
    ADD CONSTRAINT fkmxlv95qskrx8l20xdjs55gxwy FOREIGN KEY (classificationxresourceitemid, warehousecreateddate) REFERENCES classification.classificationxresourceitem (classificationxresourceitemid, warehousecreateddate);


--
-- TOC entry 4516 (class 2606 OID 206493)
-- Name: classificationxclassification fkng5lu9wyocw42hm2yq6966eic; Type: FK CONSTRAINT; Schema: classification; Owner: postgres
--

ALTER TABLE classification.classificationxclassification
    ADD CONSTRAINT fkng5lu9wyocw42hm2yq6966eic FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4471 (class 2606 OID 206498)
-- Name: classificationdataconcept fknq22rqtgtemjf9w2iwanl89im; Type: FK CONSTRAINT; Schema: classification; Owner: postgres
--

ALTER TABLE classification.classificationdataconcept
    ADD CONSTRAINT fknq22rqtgtemjf9w2iwanl89im FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4531 (class 2606 OID 206503)
-- Name: classificationxresourceitemsecuritytoken fknudqvddewc67sunrqr4wnh0aq; Type: FK CONSTRAINT; Schema: classification; Owner: postgres
--

ALTER TABLE classification.classificationxresourceitemsecuritytoken
    ADD CONSTRAINT fknudqvddewc67sunrqr4wnh0aq FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4477 (class 2606 OID 206508)
-- Name: classificationdataconceptsecuritytoken fko5u4q6stb4e4gp0hfwttgpoof; Type: FK CONSTRAINT; Schema: classification; Owner: postgres
--

ALTER TABLE classification.classificationdataconceptsecuritytoken
    ADD CONSTRAINT fko5u4q6stb4e4gp0hfwttgpoof FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 4501 (class 2606 OID 206513)
-- Name: classificationdataconceptxresourceitemsecuritytoken fkohnmet2w8qiqs1tqgp3bvaj8s; Type: FK CONSTRAINT; Schema: classification; Owner: postgres
--

ALTER TABLE classification.classificationdataconceptxresourceitemsecuritytoken
    ADD CONSTRAINT fkohnmet2w8qiqs1tqgp3bvaj8s FOREIGN KEY (securitytokenid, warehousecreateddate) REFERENCES security.securitytoken (securitytokenid, warehousecreateddate);


--
-- TOC entry 4520 (class 2606 OID 206518)
-- Name: classificationxclassificationsecuritytoken fkovne1f69w7lejinl9o202wq8r; Type: FK CONSTRAINT; Schema: classification; Owner: postgres
--

ALTER TABLE classification.classificationxclassificationsecuritytoken
    ADD CONSTRAINT fkovne1f69w7lejinl9o202wq8r FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4521 (class 2606 OID 206523)
-- Name: classificationxclassificationsecuritytoken fkp0uayhfmnxp6mbojs6b906uo1; Type: FK CONSTRAINT; Schema: classification; Owner: postgres
--

ALTER TABLE classification.classificationxclassificationsecuritytoken
    ADD CONSTRAINT fkp0uayhfmnxp6mbojs6b906uo1 FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4507 (class 2606 OID 206528)
-- Name: classificationsecuritytoken fkpgoqkscc2c9ynucppx7kqfq17; Type: FK CONSTRAINT; Schema: classification; Owner: postgres
--

ALTER TABLE classification.classificationsecuritytoken
    ADD CONSTRAINT fkpgoqkscc2c9ynucppx7kqfq17 FOREIGN KEY (securitytokenid, warehousecreateddate) REFERENCES security.securitytoken (securitytokenid, warehousecreateddate);


--
-- TOC entry 4532 (class 2606 OID 206533)
-- Name: classificationxresourceitemsecuritytoken fkpkswoxe9sd4fcqdc735yo80kc; Type: FK CONSTRAINT; Schema: classification; Owner: postgres
--

ALTER TABLE classification.classificationxresourceitemsecuritytoken
    ADD CONSTRAINT fkpkswoxe9sd4fcqdc735yo80kc FOREIGN KEY (securitytokenid, warehousecreateddate) REFERENCES security.securitytoken (securitytokenid, warehousecreateddate);


--
-- TOC entry 4484 (class 2606 OID 206538)
-- Name: classificationdataconceptxclassification fkptvfaytvks9cf4qvb1ch0nc5q; Type: FK CONSTRAINT; Schema: classification; Owner: postgres
--

ALTER TABLE classification.classificationdataconceptxclassification
    ADD CONSTRAINT fkptvfaytvks9cf4qvb1ch0nc5q FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4528 (class 2606 OID 206543)
-- Name: classificationxresourceitem fkq78r34v2adpvnc8u50fdo2mth; Type: FK CONSTRAINT; Schema: classification; Owner: postgres
--

ALTER TABLE classification.classificationxresourceitem
    ADD CONSTRAINT fkq78r34v2adpvnc8u50fdo2mth FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4472 (class 2606 OID 206548)
-- Name: classificationdataconcept fkq95ppsctlqk23bbukl11jfrk9; Type: FK CONSTRAINT; Schema: classification; Owner: postgres
--

ALTER TABLE classification.classificationdataconcept
    ADD CONSTRAINT fkq95ppsctlqk23bbukl11jfrk9 FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 4490 (class 2606 OID 206553)
-- Name: classificationdataconceptxclassificationsecuritytoken fkqd86yl64ly9efrkoqoo5g6d3a; Type: FK CONSTRAINT; Schema: classification; Owner: postgres
--

ALTER TABLE classification.classificationdataconceptxclassificationsecuritytoken
    ADD CONSTRAINT fkqd86yl64ly9efrkoqoo5g6d3a FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4533 (class 2606 OID 206558)
-- Name: classificationxresourceitemsecuritytoken fkqof2ui2s3pfau54hqtylrpbcf; Type: FK CONSTRAINT; Schema: classification; Owner: postgres
--

ALTER TABLE classification.classificationxresourceitemsecuritytoken
    ADD CONSTRAINT fkqof2ui2s3pfau54hqtylrpbcf FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 4467 (class 2606 OID 206563)
-- Name: classification fkradegxgr2qgtrmpcnr280wrow; Type: FK CONSTRAINT; Schema: classification; Owner: postgres
--

ALTER TABLE classification.classification
    ADD CONSTRAINT fkradegxgr2qgtrmpcnr280wrow FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 4502 (class 2606 OID 206568)
-- Name: classificationdataconceptxresourceitemsecuritytoken fkrnilsvgvwpcbv8fs2yr1p87ei; Type: FK CONSTRAINT; Schema: classification; Owner: postgres
--

ALTER TABLE classification.classificationdataconceptxresourceitemsecuritytoken
    ADD CONSTRAINT fkrnilsvgvwpcbv8fs2yr1p87ei FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4503 (class 2606 OID 206573)
-- Name: classificationdataconceptxresourceitemsecuritytoken fkrq8gtmddaq83y0vq5v5mvun3s; Type: FK CONSTRAINT; Schema: classification; Owner: postgres
--

ALTER TABLE classification.classificationdataconceptxresourceitemsecuritytoken
    ADD CONSTRAINT fkrq8gtmddaq83y0vq5v5mvun3s FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 4478 (class 2606 OID 206578)
-- Name: classificationdataconceptsecuritytoken fks2ygkvtk17e901wed7fjjio7v; Type: FK CONSTRAINT; Schema: classification; Owner: postgres
--

ALTER TABLE classification.classificationdataconceptsecuritytoken
    ADD CONSTRAINT fks2ygkvtk17e901wed7fjjio7v FOREIGN KEY (securitytokenid, warehousecreateddate) REFERENCES security.securitytoken (securitytokenid, warehousecreateddate);


--
-- TOC entry 4534 (class 2606 OID 206583)
-- Name: classificationxresourceitemsecuritytoken fkt2qwo9050jpmg0k59is420r1h; Type: FK CONSTRAINT; Schema: classification; Owner: postgres
--

ALTER TABLE classification.classificationxresourceitemsecuritytoken
    ADD CONSTRAINT fkt2qwo9050jpmg0k59is420r1h FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4508 (class 2606 OID 206588)
-- Name: classificationsecuritytoken fktb0dfsliwpcbdw26ivstchoxa; Type: FK CONSTRAINT; Schema: classification; Owner: postgres
--

ALTER TABLE classification.classificationsecuritytoken
    ADD CONSTRAINT fktb0dfsliwpcbdw26ivstchoxa FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 4522 (class 2606 OID 206593)
-- Name: classificationxclassificationsecuritytoken fktgfix5de1n30m7rvhxrb1yel2; Type: FK CONSTRAINT; Schema: classification; Owner: postgres
--

ALTER TABLE classification.classificationxclassificationsecuritytoken
    ADD CONSTRAINT fktgfix5de1n30m7rvhxrb1yel2 FOREIGN KEY (securitytokenid, warehousecreateddate) REFERENCES security.securitytoken (securitytokenid, warehousecreateddate);


--
-- TOC entry 4468 (class 2606 OID 206598)
-- Name: classification fkuyay717baco8bei1qoxcauux; Type: FK CONSTRAINT; Schema: classification; Owner: postgres
--

ALTER TABLE classification.classification
    ADD CONSTRAINT fkuyay717baco8bei1qoxcauux FOREIGN KEY (classificationdataconceptid, warehousecreateddate) REFERENCES classification.classificationdataconcept (classificationdataconceptid, warehousecreateddate);


--
-- TOC entry 4509 (class 2606 OID 206603)
-- Name: classificationsecuritytoken fkxo8jweipcwyhjky1sj6asi70; Type: FK CONSTRAINT; Schema: classification; Owner: postgres
--

ALTER TABLE classification.classificationsecuritytoken
    ADD CONSTRAINT fkxo8jweipcwyhjky1sj6asi70 FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 4536 (class 2606 OID 206608)
-- Name: activeflagsecuritytoken fk20yjws7ewoq2cqkx3ypki36rx; Type: FK CONSTRAINT; Schema: dbo; Owner: postgres
--

ALTER TABLE dbo.activeflagsecuritytoken
    ADD CONSTRAINT fk20yjws7ewoq2cqkx3ypki36rx FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4553 (class 2606 OID 206613)
-- Name: enterprisesecuritytoken fk3604iuvyi4psepv3rdn69jf76; Type: FK CONSTRAINT; Schema: dbo; Owner: postgres
--

ALTER TABLE dbo.enterprisesecuritytoken
    ADD CONSTRAINT fk3604iuvyi4psepv3rdn69jf76 FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4547 (class 2606 OID 206618)
-- Name: activeflagxclassificationsecuritytoken fk415n0jfxdeuva0evd9mvlooaa; Type: FK CONSTRAINT; Schema: dbo; Owner: postgres
--

ALTER TABLE dbo.activeflagxclassificationsecuritytoken
    ADD CONSTRAINT fk415n0jfxdeuva0evd9mvlooaa FOREIGN KEY (securitytokenid, warehousecreateddate) REFERENCES security.securitytoken (securitytokenid, warehousecreateddate);


--
-- TOC entry 4571 (class 2606 OID 206623)
-- Name: systemssecuritytoken fk569rtrkku5kg7sponne0xwgkf; Type: FK CONSTRAINT; Schema: dbo; Owner: postgres
--

ALTER TABLE dbo.systemssecuritytoken
    ADD CONSTRAINT fk569rtrkku5kg7sponne0xwgkf FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 4563 (class 2606 OID 206628)
-- Name: enterprisexclassificationsecuritytoken fk5l36jvcvedgw6gaa8301xjr62; Type: FK CONSTRAINT; Schema: dbo; Owner: postgres
--

ALTER TABLE dbo.enterprisexclassificationsecuritytoken
    ADD CONSTRAINT fk5l36jvcvedgw6gaa8301xjr62 FOREIGN KEY (securitytokenid, warehousecreateddate) REFERENCES security.securitytoken (securitytokenid, warehousecreateddate);


--
-- TOC entry 4537 (class 2606 OID 206633)
-- Name: activeflagsecuritytoken fk5upfxgyprbf9blrljkanx6dq1; Type: FK CONSTRAINT; Schema: dbo; Owner: postgres
--

ALTER TABLE dbo.activeflagsecuritytoken
    ADD CONSTRAINT fk5upfxgyprbf9blrljkanx6dq1 FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4576 (class 2606 OID 206638)
-- Name: systemxclassification fk6ejo5y1g32jdsi6nh5wufvdg9; Type: FK CONSTRAINT; Schema: dbo; Owner: postgres
--

ALTER TABLE dbo.systemxclassification
    ADD CONSTRAINT fk6ejo5y1g32jdsi6nh5wufvdg9 FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 4548 (class 2606 OID 206643)
-- Name: activeflagxclassificationsecuritytoken fk6lfmv9ervio1pg5wiowy4hvve; Type: FK CONSTRAINT; Schema: dbo; Owner: postgres
--

ALTER TABLE dbo.activeflagxclassificationsecuritytoken
    ADD CONSTRAINT fk6lfmv9ervio1pg5wiowy4hvve FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4564 (class 2606 OID 206648)
-- Name: enterprisexclassificationsecuritytoken fk75tx4r7sp36qtylr8r888gbkr; Type: FK CONSTRAINT; Schema: dbo; Owner: postgres
--

ALTER TABLE dbo.enterprisexclassificationsecuritytoken
    ADD CONSTRAINT fk75tx4r7sp36qtylr8r888gbkr FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 4572 (class 2606 OID 206653)
-- Name: systemssecuritytoken fk78ntmjytf0l9egbfp8w5quysa; Type: FK CONSTRAINT; Schema: dbo; Owner: postgres
--

ALTER TABLE dbo.systemssecuritytoken
    ADD CONSTRAINT fk78ntmjytf0l9egbfp8w5quysa FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 4558 (class 2606 OID 206658)
-- Name: enterprisexclassification fk7dcbw0medved7abfmh1y0btj; Type: FK CONSTRAINT; Schema: dbo; Owner: postgres
--

ALTER TABLE dbo.enterprisexclassification
    ADD CONSTRAINT fk7dcbw0medved7abfmh1y0btj FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 4565 (class 2606 OID 206663)
-- Name: enterprisexclassificationsecuritytoken fk7jfxn7qp93vt98lob9q7g5exc; Type: FK CONSTRAINT; Schema: dbo; Owner: postgres
--

ALTER TABLE dbo.enterprisexclassificationsecuritytoken
    ADD CONSTRAINT fk7jfxn7qp93vt98lob9q7g5exc FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 4542 (class 2606 OID 206668)
-- Name: activeflagxclassification fk7p1a2n77n5ic2exm71aemudot; Type: FK CONSTRAINT; Schema: dbo; Owner: postgres
--

ALTER TABLE dbo.activeflagxclassification
    ADD CONSTRAINT fk7p1a2n77n5ic2exm71aemudot FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4573 (class 2606 OID 206673)
-- Name: systemssecuritytoken fk7vvidgjeuilwrvmphf0pwrdqm; Type: FK CONSTRAINT; Schema: dbo; Owner: postgres
--

ALTER TABLE dbo.systemssecuritytoken
    ADD CONSTRAINT fk7vvidgjeuilwrvmphf0pwrdqm FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4559 (class 2606 OID 206678)
-- Name: enterprisexclassification fk9wfebpqldiybaphp969e7vu9q; Type: FK CONSTRAINT; Schema: dbo; Owner: postgres
--

ALTER TABLE dbo.enterprisexclassification
    ADD CONSTRAINT fk9wfebpqldiybaphp969e7vu9q FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 4549 (class 2606 OID 206683)
-- Name: activeflagxclassificationsecuritytoken fkahuj23jai69r76hk3u9iqos5d; Type: FK CONSTRAINT; Schema: dbo; Owner: postgres
--

ALTER TABLE dbo.activeflagxclassificationsecuritytoken
    ADD CONSTRAINT fkahuj23jai69r76hk3u9iqos5d FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 4581 (class 2606 OID 206688)
-- Name: systemxclassificationsecuritytoken fkammfvlpf58c86bmebac2l5fhy; Type: FK CONSTRAINT; Schema: dbo; Owner: postgres
--

ALTER TABLE dbo.systemxclassificationsecuritytoken
    ADD CONSTRAINT fkammfvlpf58c86bmebac2l5fhy FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 4582 (class 2606 OID 206693)
-- Name: systemxclassificationsecuritytoken fkaxmrulkvngggqqt0yjk39qjx0; Type: FK CONSTRAINT; Schema: dbo; Owner: postgres
--

ALTER TABLE dbo.systemxclassificationsecuritytoken
    ADD CONSTRAINT fkaxmrulkvngggqqt0yjk39qjx0 FOREIGN KEY (securitytokenid, warehousecreateddate) REFERENCES security.securitytoken (securitytokenid, warehousecreateddate);


--
-- TOC entry 4583 (class 2606 OID 206698)
-- Name: systemxclassificationsecuritytoken fkb1iyf0i1s8q5tmc9wrwdfi52i; Type: FK CONSTRAINT; Schema: dbo; Owner: postgres
--

ALTER TABLE dbo.systemxclassificationsecuritytoken
    ADD CONSTRAINT fkb1iyf0i1s8q5tmc9wrwdfi52i FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4543 (class 2606 OID 206703)
-- Name: activeflagxclassification fkblul4g9hdfavb3grhcn5bu32h; Type: FK CONSTRAINT; Schema: dbo; Owner: postgres
--

ALTER TABLE dbo.activeflagxclassification
    ADD CONSTRAINT fkblul4g9hdfavb3grhcn5bu32h FOREIGN KEY (classificationid, warehousecreateddate) REFERENCES classification.classification (classificationid, warehousecreateddate);


--
-- TOC entry 4554 (class 2606 OID 206708)
-- Name: enterprisesecuritytoken fkcja1yq5j2ywm4mearg7gfbeg8; Type: FK CONSTRAINT; Schema: dbo; Owner: postgres
--

ALTER TABLE dbo.enterprisesecuritytoken
    ADD CONSTRAINT fkcja1yq5j2ywm4mearg7gfbeg8 FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 4560 (class 2606 OID 206713)
-- Name: enterprisexclassification fkd8u5qov86csi5bmvn97648jyq; Type: FK CONSTRAINT; Schema: dbo; Owner: postgres
--

ALTER TABLE dbo.enterprisexclassification
    ADD CONSTRAINT fkd8u5qov86csi5bmvn97648jyq FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4538 (class 2606 OID 206718)
-- Name: activeflagsecuritytoken fkf1tv1gusx83k2ytmhi4g3tqbm; Type: FK CONSTRAINT; Schema: dbo; Owner: postgres
--

ALTER TABLE dbo.activeflagsecuritytoken
    ADD CONSTRAINT fkf1tv1gusx83k2ytmhi4g3tqbm FOREIGN KEY (securitytokenid, warehousecreateddate) REFERENCES security.securitytoken (securitytokenid, warehousecreateddate);


--
-- TOC entry 4577 (class 2606 OID 206723)
-- Name: systemxclassification fkf8j3lg5l7fp5qthp3s7v5cpsy; Type: FK CONSTRAINT; Schema: dbo; Owner: postgres
--

ALTER TABLE dbo.systemxclassification
    ADD CONSTRAINT fkf8j3lg5l7fp5qthp3s7v5cpsy FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 4574 (class 2606 OID 206728)
-- Name: systemssecuritytoken fkgq2e9xc2wsw93xl4p5yhaysx; Type: FK CONSTRAINT; Schema: dbo; Owner: postgres
--

ALTER TABLE dbo.systemssecuritytoken
    ADD CONSTRAINT fkgq2e9xc2wsw93xl4p5yhaysx FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4569 (class 2606 OID 206733)
-- Name: systems fkgrtajtg4kade8b7dvqjynt2c6; Type: FK CONSTRAINT; Schema: dbo; Owner: postgres
--

ALTER TABLE dbo.systems
    ADD CONSTRAINT fkgrtajtg4kade8b7dvqjynt2c6 FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 4584 (class 2606 OID 206738)
-- Name: systemxclassificationsecuritytoken fkhpnepymhd6det14tmy3vsx6g8; Type: FK CONSTRAINT; Schema: dbo; Owner: postgres
--

ALTER TABLE dbo.systemxclassificationsecuritytoken
    ADD CONSTRAINT fkhpnepymhd6det14tmy3vsx6g8 FOREIGN KEY (systemxclassificationid, warehousecreateddate) REFERENCES dbo.systemxclassification (systemxclassificationid, warehousecreateddate);


--
-- TOC entry 4555 (class 2606 OID 206743)
-- Name: enterprisesecuritytoken fki2166mtej2yhf4qeegbrtwsom; Type: FK CONSTRAINT; Schema: dbo; Owner: postgres
--

ALTER TABLE dbo.enterprisesecuritytoken
    ADD CONSTRAINT fki2166mtej2yhf4qeegbrtwsom FOREIGN KEY (securitytokenid, warehousecreateddate) REFERENCES security.securitytoken (securitytokenid, warehousecreateddate);


--
-- TOC entry 4556 (class 2606 OID 206748)
-- Name: enterprisesecuritytoken fki27vhcoq25358slocd9mn9481; Type: FK CONSTRAINT; Schema: dbo; Owner: postgres
--

ALTER TABLE dbo.enterprisesecuritytoken
    ADD CONSTRAINT fki27vhcoq25358slocd9mn9481 FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4539 (class 2606 OID 206753)
-- Name: activeflagsecuritytoken fki4tkjwmtimx0nsi9nkmvjejd8; Type: FK CONSTRAINT; Schema: dbo; Owner: postgres
--

ALTER TABLE dbo.activeflagsecuritytoken
    ADD CONSTRAINT fki4tkjwmtimx0nsi9nkmvjejd8 FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 4544 (class 2606 OID 206758)
-- Name: activeflagxclassification fkid9su2w15uts2jocr2ix6p9n6; Type: FK CONSTRAINT; Schema: dbo; Owner: postgres
--

ALTER TABLE dbo.activeflagxclassification
    ADD CONSTRAINT fkid9su2w15uts2jocr2ix6p9n6 FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 4585 (class 2606 OID 206763)
-- Name: systemxclassificationsecuritytoken fkj9spyu6coc19mcroktrbdym0i; Type: FK CONSTRAINT; Schema: dbo; Owner: postgres
--

ALTER TABLE dbo.systemxclassificationsecuritytoken
    ADD CONSTRAINT fkj9spyu6coc19mcroktrbdym0i FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 4545 (class 2606 OID 206768)
-- Name: activeflagxclassification fkjcnjquu3mlf3fqxljya6hhh32; Type: FK CONSTRAINT; Schema: dbo; Owner: postgres
--

ALTER TABLE dbo.activeflagxclassification
    ADD CONSTRAINT fkjcnjquu3mlf3fqxljya6hhh32 FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 4550 (class 2606 OID 206773)
-- Name: activeflagxclassificationsecuritytoken fkjt5o1jc7herohd02v678yyba6; Type: FK CONSTRAINT; Schema: dbo; Owner: postgres
--

ALTER TABLE dbo.activeflagxclassificationsecuritytoken
    ADD CONSTRAINT fkjt5o1jc7herohd02v678yyba6 FOREIGN KEY (activeflagxclassificationid, warehousecreateddate) REFERENCES dbo.activeflagxclassification (activeflagxclassificationid, warehousecreateddate);


--
-- TOC entry 4570 (class 2606 OID 206778)
-- Name: systems fkk9wup45yifecrf4o0fb5lyuy6; Type: FK CONSTRAINT; Schema: dbo; Owner: postgres
--

ALTER TABLE dbo.systems
    ADD CONSTRAINT fkk9wup45yifecrf4o0fb5lyuy6 FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 4557 (class 2606 OID 206783)
-- Name: enterprisesecuritytoken fkkfbukc1416acs2qhjduyaslun; Type: FK CONSTRAINT; Schema: dbo; Owner: postgres
--

ALTER TABLE dbo.enterprisesecuritytoken
    ADD CONSTRAINT fkkfbukc1416acs2qhjduyaslun FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 4551 (class 2606 OID 206788)
-- Name: activeflagxclassificationsecuritytoken fkl8ovwqdvb6bagyf17g7gk46qy; Type: FK CONSTRAINT; Schema: dbo; Owner: postgres
--

ALTER TABLE dbo.activeflagxclassificationsecuritytoken
    ADD CONSTRAINT fkl8ovwqdvb6bagyf17g7gk46qy FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4540 (class 2606 OID 206793)
-- Name: activeflagsecuritytoken fklh0yk0eo7gs9tjd368uaqa0vt; Type: FK CONSTRAINT; Schema: dbo; Owner: postgres
--

ALTER TABLE dbo.activeflagsecuritytoken
    ADD CONSTRAINT fklh0yk0eo7gs9tjd368uaqa0vt FOREIGN KEY (securitytokenactiveflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 4552 (class 2606 OID 206798)
-- Name: activeflagxclassificationsecuritytoken fklmqtxiiqcje4o4vix6egjsg27; Type: FK CONSTRAINT; Schema: dbo; Owner: postgres
--

ALTER TABLE dbo.activeflagxclassificationsecuritytoken
    ADD CONSTRAINT fklmqtxiiqcje4o4vix6egjsg27 FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 4566 (class 2606 OID 206803)
-- Name: enterprisexclassificationsecuritytoken fklojg3xmk98ghle10liw96742m; Type: FK CONSTRAINT; Schema: dbo; Owner: postgres
--

ALTER TABLE dbo.enterprisexclassificationsecuritytoken
    ADD CONSTRAINT fklojg3xmk98ghle10liw96742m FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4535 (class 2606 OID 206808)
-- Name: activeflag fkme13h3ny7lm8n86kltbw69ni1; Type: FK CONSTRAINT; Schema: dbo; Owner: postgres
--

ALTER TABLE dbo.activeflag
    ADD CONSTRAINT fkme13h3ny7lm8n86kltbw69ni1 FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 4561 (class 2606 OID 206813)
-- Name: enterprisexclassification fkmt4djrkvv0w1ef778yy01fone; Type: FK CONSTRAINT; Schema: dbo; Owner: postgres
--

ALTER TABLE dbo.enterprisexclassification
    ADD CONSTRAINT fkmt4djrkvv0w1ef778yy01fone FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4546 (class 2606 OID 206818)
-- Name: activeflagxclassification fko09sv3aidiqrfa034t31l067w; Type: FK CONSTRAINT; Schema: dbo; Owner: postgres
--

ALTER TABLE dbo.activeflagxclassification
    ADD CONSTRAINT fko09sv3aidiqrfa034t31l067w FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4578 (class 2606 OID 206823)
-- Name: systemxclassification fko0c1ywx0xwsytkccp23xtolxd; Type: FK CONSTRAINT; Schema: dbo; Owner: postgres
--

ALTER TABLE dbo.systemxclassification
    ADD CONSTRAINT fko0c1ywx0xwsytkccp23xtolxd FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4567 (class 2606 OID 206828)
-- Name: enterprisexclassificationsecuritytoken fkp7wseksjtv3n3vok7qm68dmdb; Type: FK CONSTRAINT; Schema: dbo; Owner: postgres
--

ALTER TABLE dbo.enterprisexclassificationsecuritytoken
    ADD CONSTRAINT fkp7wseksjtv3n3vok7qm68dmdb FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4562 (class 2606 OID 206833)
-- Name: enterprisexclassification fkpubcpyc3hm0x9l70u19lj96r0; Type: FK CONSTRAINT; Schema: dbo; Owner: postgres
--

ALTER TABLE dbo.enterprisexclassification
    ADD CONSTRAINT fkpubcpyc3hm0x9l70u19lj96r0 FOREIGN KEY (classificationid, warehousecreateddate) REFERENCES classification.classification (classificationid, warehousecreateddate);


--
-- TOC entry 4579 (class 2606 OID 206838)
-- Name: systemxclassification fkq69xnhoy37i0vgl4n0ye0deql; Type: FK CONSTRAINT; Schema: dbo; Owner: postgres
--

ALTER TABLE dbo.systemxclassification
    ADD CONSTRAINT fkq69xnhoy37i0vgl4n0ye0deql FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4568 (class 2606 OID 206843)
-- Name: enterprisexclassificationsecuritytoken fkr6xvbr7r4rmk73etcyyx887ao; Type: FK CONSTRAINT; Schema: dbo; Owner: postgres
--

ALTER TABLE dbo.enterprisexclassificationsecuritytoken
    ADD CONSTRAINT fkr6xvbr7r4rmk73etcyyx887ao FOREIGN KEY (enterprisexclassificationid, warehousecreateddate) REFERENCES dbo.enterprisexclassification (enterprisexclassificationid, warehousecreateddate);


--
-- TOC entry 4541 (class 2606 OID 206848)
-- Name: activeflagsecuritytoken fkr9uxaoa4e7qc848omo02gnv8q; Type: FK CONSTRAINT; Schema: dbo; Owner: postgres
--

ALTER TABLE dbo.activeflagsecuritytoken
    ADD CONSTRAINT fkr9uxaoa4e7qc848omo02gnv8q FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 4575 (class 2606 OID 206853)
-- Name: systemssecuritytoken fks9aoyq05lmsl0jpqnja4gy5g8; Type: FK CONSTRAINT; Schema: dbo; Owner: postgres
--

ALTER TABLE dbo.systemssecuritytoken
    ADD CONSTRAINT fks9aoyq05lmsl0jpqnja4gy5g8 FOREIGN KEY (securitytokenid, warehousecreateddate) REFERENCES security.securitytoken (securitytokenid, warehousecreateddate);


--
-- TOC entry 4580 (class 2606 OID 206858)
-- Name: systemxclassification fksok0wnd9e14j1shyqkn1fkpf3; Type: FK CONSTRAINT; Schema: dbo; Owner: postgres
--

ALTER TABLE dbo.systemxclassification
    ADD CONSTRAINT fksok0wnd9e14j1shyqkn1fkpf3 FOREIGN KEY (classificationid, warehousecreateddate) REFERENCES classification.classification (classificationid, warehousecreateddate);


--
-- TOC entry 4586 (class 2606 OID 206863)
-- Name: systemxclassificationsecuritytoken fktm7py81728d8oor46wotp8y0f; Type: FK CONSTRAINT; Schema: dbo; Owner: postgres
--

ALTER TABLE dbo.systemxclassificationsecuritytoken
    ADD CONSTRAINT fktm7py81728d8oor46wotp8y0f FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4684 (class 2606 OID 206868)
-- Name: eventxinvolvedparty fk14n4sdaea68x8ye5kdnm92m4v; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.eventxinvolvedparty
    ADD CONSTRAINT fk14n4sdaea68x8ye5kdnm92m4v FOREIGN KEY (eventid, warehousecreateddate) REFERENCES event.event (eventid, warehousecreateddate);


--
-- TOC entry 4614 (class 2606 OID 206873)
-- Name: eventxaddresssecuritytoken fk1b8yahv9f17vt9brstr9lu5s; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.eventxaddresssecuritytoken
    ADD CONSTRAINT fk1b8yahv9f17vt9brstr9lu5s FOREIGN KEY (securitytokenid, warehousecreateddate) REFERENCES security.securitytoken (securitytokenid, warehousecreateddate);


--
-- TOC entry 4678 (class 2606 OID 206878)
-- Name: eventxgeographysecuritytoken fk1i4qgb7md1vd911fwvxii42tx; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.eventxgeographysecuritytoken
    ADD CONSTRAINT fk1i4qgb7md1vd911fwvxii42tx FOREIGN KEY (securitytokenid, warehousecreateddate) REFERENCES security.securitytoken (securitytokenid, warehousecreateddate);


--
-- TOC entry 4645 (class 2606 OID 206883)
-- Name: eventxevent fk27gym41nw6dls3e5oll0nkcbr; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.eventxevent
    ADD CONSTRAINT fk27gym41nw6dls3e5oll0nkcbr FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4615 (class 2606 OID 206888)
-- Name: eventxaddresssecuritytoken fk2n5hwft2sainpe6l7cerwj4o6; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.eventxaddresssecuritytoken
    ADD CONSTRAINT fk2n5hwft2sainpe6l7cerwj4o6 FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4658 (class 2606 OID 206893)
-- Name: eventxeventtype fk2s0kehqs4vd8un8pa781t8abk; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.eventxeventtype
    ADD CONSTRAINT fk2s0kehqs4vd8un8pa781t8abk FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4627 (class 2606 OID 206898)
-- Name: eventxarrangementssecuritytoken fk2tudn6gio54xpxa5ecyryl2v0; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.eventxarrangementssecuritytoken
    ADD CONSTRAINT fk2tudn6gio54xpxa5ecyryl2v0 FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4639 (class 2606 OID 206903)
-- Name: eventxclassificationsecuritytoken fk2uwfplnar8o0onw3e93phd69k; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.eventxclassificationsecuritytoken
    ADD CONSTRAINT fk2uwfplnar8o0onw3e93phd69k FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 4697 (class 2606 OID 206908)
-- Name: eventxproduct fk2wg3fp1s1jjl3lng6uak99fr4; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.eventxproduct
    ADD CONSTRAINT fk2wg3fp1s1jjl3lng6uak99fr4 FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4710 (class 2606 OID 206913)
-- Name: eventxresourceitem fk30l7kjmsyo5va5n3exkkl364i; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.eventxresourceitem
    ADD CONSTRAINT fk30l7kjmsyo5va5n3exkkl364i FOREIGN KEY (eventid, warehousecreateddate) REFERENCES event.event (eventid, warehousecreateddate);


--
-- TOC entry 4628 (class 2606 OID 206918)
-- Name: eventxarrangementssecuritytoken fk33w71cp61prmc7i0uboecicda; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.eventxarrangementssecuritytoken
    ADD CONSTRAINT fk33w71cp61prmc7i0uboecicda FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 4597 (class 2606 OID 206923)
-- Name: eventtype fk3a6ig1a98nle8uhil7c00wtxj; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.eventtype
    ADD CONSTRAINT fk3a6ig1a98nle8uhil7c00wtxj FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 4607 (class 2606 OID 206928)
-- Name: eventxaddress fk3bbi34ppkvf938mm8618n7cdi; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.eventxaddress
    ADD CONSTRAINT fk3bbi34ppkvf938mm8618n7cdi FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 4679 (class 2606 OID 206933)
-- Name: eventxgeographysecuritytoken fk3k3bo2t7xd8q3va2g8whp7l7v; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.eventxgeographysecuritytoken
    ADD CONSTRAINT fk3k3bo2t7xd8q3va2g8whp7l7v FOREIGN KEY (eventxgeographyid, warehousecreateddate) REFERENCES event.eventxgeography (eventxgeographyid, warehousecreateddate);


--
-- TOC entry 4723 (class 2606 OID 206938)
-- Name: eventxrules fk3pv6r2calb05a669dg4aaqvy3; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.eventxrules
    ADD CONSTRAINT fk3pv6r2calb05a669dg4aaqvy3 FOREIGN KEY (rulesid, warehousecreateddate) REFERENCES rules.rules (rulesid, warehousecreateddate);


--
-- TOC entry 4601 (class 2606 OID 206943)
-- Name: eventtypessecuritytoken fk3sqx94u3an8m27b0aotdca2wb; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.eventtypessecuritytoken
    ADD CONSTRAINT fk3sqx94u3an8m27b0aotdca2wb FOREIGN KEY (securitytokenid, warehousecreateddate) REFERENCES security.securitytoken (securitytokenid, warehousecreateddate);


--
-- TOC entry 4730 (class 2606 OID 206948)
-- Name: eventxrulessecuritytoken fk4cfg8atxhbgoya17d1vp1bpf7; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.eventxrulessecuritytoken
    ADD CONSTRAINT fk4cfg8atxhbgoya17d1vp1bpf7 FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 4616 (class 2606 OID 206953)
-- Name: eventxaddresssecuritytoken fk4delkrhbqq9uhqpsmqbdc8eh1; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.eventxaddresssecuritytoken
    ADD CONSTRAINT fk4delkrhbqq9uhqpsmqbdc8eh1 FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 4640 (class 2606 OID 206958)
-- Name: eventxclassificationsecuritytoken fk4hjmqpqf99dnag748layycl1d; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.eventxclassificationsecuritytoken
    ADD CONSTRAINT fk4hjmqpqf99dnag748layycl1d FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 4602 (class 2606 OID 206963)
-- Name: eventtypessecuritytoken fk533lul2cwy1gxqwdlkwot3xli; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.eventtypessecuritytoken
    ADD CONSTRAINT fk533lul2cwy1gxqwdlkwot3xli FOREIGN KEY (eventtypesid, warehousecreateddate) REFERENCES event.eventtype (eventtypeid, warehousecreateddate);


--
-- TOC entry 4587 (class 2606 OID 206968)
-- Name: event fk5a6m41a8gggwx8fqsrs4at8tg; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.event
    ADD CONSTRAINT fk5a6m41a8gggwx8fqsrs4at8tg FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4652 (class 2606 OID 206973)
-- Name: eventxeventsecuritytoken fk5ce8ibb6xf53yy6w5nvrna86b; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.eventxeventsecuritytoken
    ADD CONSTRAINT fk5ce8ibb6xf53yy6w5nvrna86b FOREIGN KEY (securitytokenid, warehousecreateddate) REFERENCES security.securitytoken (securitytokenid, warehousecreateddate);


--
-- TOC entry 4591 (class 2606 OID 206978)
-- Name: eventsecuritytoken fk5l2t98n1cb4rsa23u81thgv50; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.eventsecuritytoken
    ADD CONSTRAINT fk5l2t98n1cb4rsa23u81thgv50 FOREIGN KEY (securitytokenid, warehousecreateddate) REFERENCES security.securitytoken (securitytokenid, warehousecreateddate);


--
-- TOC entry 4603 (class 2606 OID 206983)
-- Name: eventtypessecuritytoken fk69no16tl8ojgbi6so0jp3s68a; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.eventtypessecuritytoken
    ADD CONSTRAINT fk69no16tl8ojgbi6so0jp3s68a FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4691 (class 2606 OID 206988)
-- Name: eventxinvolvedpartysecuritytoken fk6ckmfi6y2y2topapiybevk1rg; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.eventxinvolvedpartysecuritytoken
    ADD CONSTRAINT fk6ckmfi6y2y2topapiybevk1rg FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4617 (class 2606 OID 206993)
-- Name: eventxaddresssecuritytoken fk6frs7aq2uychxsnl6r89fi7on; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.eventxaddresssecuritytoken
    ADD CONSTRAINT fk6frs7aq2uychxsnl6r89fi7on FOREIGN KEY (eventxaddressid, warehousecreateddate) REFERENCES event.eventxaddress (eventxaddressid, warehousecreateddate);


--
-- TOC entry 4646 (class 2606 OID 206998)
-- Name: eventxevent fk6gr2cpldlbe8dfgk92iceiksh; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.eventxevent
    ADD CONSTRAINT fk6gr2cpldlbe8dfgk92iceiksh FOREIGN KEY (parenteventid, warehousecreateddate) REFERENCES event.event (eventid, warehousecreateddate);


--
-- TOC entry 4588 (class 2606 OID 207003)
-- Name: event fk6ifge8hyt5n7895pe83e7l8ic; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.event
    ADD CONSTRAINT fk6ifge8hyt5n7895pe83e7l8ic FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 4653 (class 2606 OID 207008)
-- Name: eventxeventsecuritytoken fk6k5gowbbqddlswcein88ndcuf; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.eventxeventsecuritytoken
    ADD CONSTRAINT fk6k5gowbbqddlswcein88ndcuf FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4724 (class 2606 OID 207013)
-- Name: eventxrules fk6kh594jdfv92e12q994afvkq2; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.eventxrules
    ADD CONSTRAINT fk6kh594jdfv92e12q994afvkq2 FOREIGN KEY (classificationid, warehousecreateddate) REFERENCES classification.classification (classificationid, warehousecreateddate);


--
-- TOC entry 4698 (class 2606 OID 207018)
-- Name: eventxproduct fk6kw3pcx56g4uy28x9t6q9w5g3; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.eventxproduct
    ADD CONSTRAINT fk6kw3pcx56g4uy28x9t6q9w5g3 FOREIGN KEY (classificationid, warehousecreateddate) REFERENCES classification.classification (classificationid, warehousecreateddate);


--
-- TOC entry 4665 (class 2606 OID 207023)
-- Name: eventxeventtypesecuritytoken fk70y0xcygmmf2v7dbnjwf15tuu; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.eventxeventtypesecuritytoken
    ADD CONSTRAINT fk70y0xcygmmf2v7dbnjwf15tuu FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4692 (class 2606 OID 207028)
-- Name: eventxinvolvedpartysecuritytoken fk72yo43h7hi9spviftt74k46si; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.eventxinvolvedpartysecuritytoken
    ADD CONSTRAINT fk72yo43h7hi9spviftt74k46si FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4626 (class 2606 OID 213891)
-- Name: eventxarrangement fk7607u935wf0x8yuf84h0xseyl; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.eventxarrangement
    ADD CONSTRAINT fk7607u935wf0x8yuf84h0xseyl FOREIGN KEY (arrangementid, warehousecreateddate) REFERENCES arrangement.arrangement (arrangementid, warehousecreateddate);


--
-- TOC entry 4725 (class 2606 OID 207033)
-- Name: eventxrules fk795v6hn2a3tim8hhf6ery2yf8; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.eventxrules
    ADD CONSTRAINT fk795v6hn2a3tim8hhf6ery2yf8 FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 4598 (class 2606 OID 207038)
-- Name: eventtype fk7bpo42s41tkfou4bopgs8xeqg; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.eventtype
    ADD CONSTRAINT fk7bpo42s41tkfou4bopgs8xeqg FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4633 (class 2606 OID 207043)
-- Name: eventxclassification fk7gqyx4i1xca4ioveotqcctmd3; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.eventxclassification
    ADD CONSTRAINT fk7gqyx4i1xca4ioveotqcctmd3 FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 4654 (class 2606 OID 207048)
-- Name: eventxeventsecuritytoken fk7kw08dh8lncu1lfruwll5ejtm; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.eventxeventsecuritytoken
    ADD CONSTRAINT fk7kw08dh8lncu1lfruwll5ejtm FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4620 (class 2606 OID 207053)
-- Name: eventxarrangement fk7tkxsheat6us20jq1hhsmhkan; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.eventxarrangement
    ADD CONSTRAINT fk7tkxsheat6us20jq1hhsmhkan FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4671 (class 2606 OID 207058)
-- Name: eventxgeography fk7ttvl8fdvn2nghkfr1u5adhgx; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.eventxgeography
    ADD CONSTRAINT fk7ttvl8fdvn2nghkfr1u5adhgx FOREIGN KEY (classificationid, warehousecreateddate) REFERENCES classification.classification (classificationid, warehousecreateddate);


--
-- TOC entry 4704 (class 2606 OID 207063)
-- Name: eventxproductsecuritytoken fk7wsudn2jsus0x9h44mrgv4nad; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.eventxproductsecuritytoken
    ADD CONSTRAINT fk7wsudn2jsus0x9h44mrgv4nad FOREIGN KEY (securitytokenid, warehousecreateddate) REFERENCES security.securitytoken (securitytokenid, warehousecreateddate);


--
-- TOC entry 4680 (class 2606 OID 207068)
-- Name: eventxgeographysecuritytoken fk85ggy74dqr0025fhhn8nva2v1; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.eventxgeographysecuritytoken
    ADD CONSTRAINT fk85ggy74dqr0025fhhn8nva2v1 FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4618 (class 2606 OID 207073)
-- Name: eventxaddresssecuritytoken fk86mt4pshsp9bxjaubl1729701; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.eventxaddresssecuritytoken
    ADD CONSTRAINT fk86mt4pshsp9bxjaubl1729701 FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4647 (class 2606 OID 207078)
-- Name: eventxevent fk8q11uo1x1xogw2rytdqbnhqn1; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.eventxevent
    ADD CONSTRAINT fk8q11uo1x1xogw2rytdqbnhqn1 FOREIGN KEY (childeventid, warehousecreateddate) REFERENCES event.event (eventid, warehousecreateddate);


--
-- TOC entry 4685 (class 2606 OID 207083)
-- Name: eventxinvolvedparty fk8xntnjn0yestyj21n227fwi9t; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.eventxinvolvedparty
    ADD CONSTRAINT fk8xntnjn0yestyj21n227fwi9t FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4599 (class 2606 OID 207088)
-- Name: eventtype fk91msecp4vf69nvn0t6362tmvy; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.eventtype
    ADD CONSTRAINT fk91msecp4vf69nvn0t6362tmvy FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 4589 (class 2606 OID 207093)
-- Name: event fk944ydkbqk682cotb21mlyedhc; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.event
    ADD CONSTRAINT fk944ydkbqk682cotb21mlyedhc FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 4699 (class 2606 OID 207098)
-- Name: eventxproduct fk9dtyhip89oe1wyjki0ph2kynb; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.eventxproduct
    ADD CONSTRAINT fk9dtyhip89oe1wyjki0ph2kynb FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 4686 (class 2606 OID 207103)
-- Name: eventxinvolvedparty fk9eoeiem966sixemrfgnq76211; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.eventxinvolvedparty
    ADD CONSTRAINT fk9eoeiem966sixemrfgnq76211 FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 4672 (class 2606 OID 207108)
-- Name: eventxgeography fk9hobmhai131oqagl3obeablxc; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.eventxgeography
    ADD CONSTRAINT fk9hobmhai131oqagl3obeablxc FOREIGN KEY (eventid, warehousecreateddate) REFERENCES event.event (eventid, warehousecreateddate);


--
-- TOC entry 4726 (class 2606 OID 207113)
-- Name: eventxrules fk9koqxie6a6q0b4elyxadxe9bi; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.eventxrules
    ADD CONSTRAINT fk9koqxie6a6q0b4elyxadxe9bi FOREIGN KEY (eventid, warehousecreateddate) REFERENCES event.event (eventid, warehousecreateddate);


--
-- TOC entry 4621 (class 2606 OID 207118)
-- Name: eventxarrangement fk9l3iwtdok5r27i83ywbjt0nto; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.eventxarrangement
    ADD CONSTRAINT fk9l3iwtdok5r27i83ywbjt0nto FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 4655 (class 2606 OID 207123)
-- Name: eventxeventsecuritytoken fkaddyvw0he74ynmpwy5rtvn26r; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.eventxeventsecuritytoken
    ADD CONSTRAINT fkaddyvw0he74ynmpwy5rtvn26r FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 4727 (class 2606 OID 207128)
-- Name: eventxrules fkavxgy5uwlh1u87pyd3wr431w; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.eventxrules
    ADD CONSTRAINT fkavxgy5uwlh1u87pyd3wr431w FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4656 (class 2606 OID 207133)
-- Name: eventxeventsecuritytoken fkaybh5nk6h1pn25l38m9sb807f; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.eventxeventsecuritytoken
    ADD CONSTRAINT fkaybh5nk6h1pn25l38m9sb807f FOREIGN KEY (eventxeventid, warehousecreateddate) REFERENCES event.eventxevent (eventxeventid, warehousecreateddate);


--
-- TOC entry 4728 (class 2606 OID 207138)
-- Name: eventxrules fkb3ssh2npba39x5f4e6hofnsej; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.eventxrules
    ADD CONSTRAINT fkb3ssh2npba39x5f4e6hofnsej FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4592 (class 2606 OID 207143)
-- Name: eventsecuritytoken fkb5qngfntxfvo5cn45y0afpq6x; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.eventsecuritytoken
    ADD CONSTRAINT fkb5qngfntxfvo5cn45y0afpq6x FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 4673 (class 2606 OID 207148)
-- Name: eventxgeography fkbe9ptj3nqor5lbwjb48yl44jg; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.eventxgeography
    ADD CONSTRAINT fkbe9ptj3nqor5lbwjb48yl44jg FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 4711 (class 2606 OID 207153)
-- Name: eventxresourceitem fkbyjir0fafd1lcw9bta2vjmc1n; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.eventxresourceitem
    ADD CONSTRAINT fkbyjir0fafd1lcw9bta2vjmc1n FOREIGN KEY (classificationid, warehousecreateddate) REFERENCES classification.classification (classificationid, warehousecreateddate);


--
-- TOC entry 4687 (class 2606 OID 207158)
-- Name: eventxinvolvedparty fkcr5fhf97fu39srlrsjwm8bjsw; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.eventxinvolvedparty
    ADD CONSTRAINT fkcr5fhf97fu39srlrsjwm8bjsw FOREIGN KEY (involvedpartyid, warehousecreateddate) REFERENCES party.involvedparty (involvedpartyid, warehousecreateddate);


--
-- TOC entry 4600 (class 2606 OID 207163)
-- Name: eventtype fkd28eytnr3sps354vu7k75r6ds; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.eventtype
    ADD CONSTRAINT fkd28eytnr3sps354vu7k75r6ds FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4731 (class 2606 OID 207168)
-- Name: eventxrulessecuritytoken fkd42bwmq984cam0ylnap2s3wpc; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.eventxrulessecuritytoken
    ADD CONSTRAINT fkd42bwmq984cam0ylnap2s3wpc FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4608 (class 2606 OID 207173)
-- Name: eventxaddress fkd6wdra1kpon3dn8wcfjrk6msl; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.eventxaddress
    ADD CONSTRAINT fkd6wdra1kpon3dn8wcfjrk6msl FOREIGN KEY (addressid, warehousecreateddate) REFERENCES address.address (addressid, warehousecreateddate);


--
-- TOC entry 4619 (class 2606 OID 207178)
-- Name: eventxaddresssecuritytoken fkd7r65wrbjj132a0hama4srqfu; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.eventxaddresssecuritytoken
    ADD CONSTRAINT fkd7r65wrbjj132a0hama4srqfu FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 4666 (class 2606 OID 207183)
-- Name: eventxeventtypesecuritytoken fkd7rws5evj37jj64ccvudlyxmc; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.eventxeventtypesecuritytoken
    ADD CONSTRAINT fkd7rws5evj37jj64ccvudlyxmc FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 4717 (class 2606 OID 207188)
-- Name: eventxresourceitemsecuritytoken fkds5ohuq35i98a2mbbtiv844uq; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.eventxresourceitemsecuritytoken
    ADD CONSTRAINT fkds5ohuq35i98a2mbbtiv844uq FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 4732 (class 2606 OID 207193)
-- Name: eventxrulessecuritytoken fke143eyeb17r5ye7xvqygem8a6; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.eventxrulessecuritytoken
    ADD CONSTRAINT fke143eyeb17r5ye7xvqygem8a6 FOREIGN KEY (securitytokenid, warehousecreateddate) REFERENCES security.securitytoken (securitytokenid, warehousecreateddate);


--
-- TOC entry 4604 (class 2606 OID 207198)
-- Name: eventtypessecuritytoken fke2wss5k3p8r4nxol9e95m3k3q; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.eventtypessecuritytoken
    ADD CONSTRAINT fke2wss5k3p8r4nxol9e95m3k3q FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 4634 (class 2606 OID 207203)
-- Name: eventxclassification fkf1gqpk872jacljscf2kv0i24r; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.eventxclassification
    ADD CONSTRAINT fkf1gqpk872jacljscf2kv0i24r FOREIGN KEY (eventid, warehousecreateddate) REFERENCES event.event (eventid, warehousecreateddate);


--
-- TOC entry 4609 (class 2606 OID 207208)
-- Name: eventxaddress fkf3dfxx8n3ijrkin1n3rr8jcek; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.eventxaddress
    ADD CONSTRAINT fkf3dfxx8n3ijrkin1n3rr8jcek FOREIGN KEY (classificationid, warehousecreateddate) REFERENCES classification.classification (classificationid, warehousecreateddate);


--
-- TOC entry 4667 (class 2606 OID 207213)
-- Name: eventxeventtypesecuritytoken fkfdhh2w146eu56jmq685a7vlo7; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.eventxeventtypesecuritytoken
    ADD CONSTRAINT fkfdhh2w146eu56jmq685a7vlo7 FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4641 (class 2606 OID 207218)
-- Name: eventxclassificationsecuritytoken fkfoq8c32icxr3vxx3t050nwdqm; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.eventxclassificationsecuritytoken
    ADD CONSTRAINT fkfoq8c32icxr3vxx3t050nwdqm FOREIGN KEY (eventxclassificationsid, warehousecreateddate) REFERENCES event.eventxclassification (eventxclassificationid, warehousecreateddate);


--
-- TOC entry 4712 (class 2606 OID 207223)
-- Name: eventxresourceitem fkfpea4o9v60x6us7p60hss1aqu; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.eventxresourceitem
    ADD CONSTRAINT fkfpea4o9v60x6us7p60hss1aqu FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4729 (class 2606 OID 207228)
-- Name: eventxrules fkfsgbiovv87rrf7llv66q5li2d; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.eventxrules
    ADD CONSTRAINT fkfsgbiovv87rrf7llv66q5li2d FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 4718 (class 2606 OID 207233)
-- Name: eventxresourceitemsecuritytoken fkfwm7s7q3f572ajckfelr5c25d; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.eventxresourceitemsecuritytoken
    ADD CONSTRAINT fkfwm7s7q3f572ajckfelr5c25d FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4733 (class 2606 OID 207238)
-- Name: eventxrulessecuritytoken fkfxyf5j49rr38955mbno4ynfqv; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.eventxrulessecuritytoken
    ADD CONSTRAINT fkfxyf5j49rr38955mbno4ynfqv FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4700 (class 2606 OID 207243)
-- Name: eventxproduct fkfyq4xws9l9ndtorw2cgrbtc5b; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.eventxproduct
    ADD CONSTRAINT fkfyq4xws9l9ndtorw2cgrbtc5b FOREIGN KEY (eventid, warehousecreateddate) REFERENCES event.event (eventid, warehousecreateddate);


--
-- TOC entry 4642 (class 2606 OID 207248)
-- Name: eventxclassificationsecuritytoken fkg821wl3o2mwy3k7ysebk2donl; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.eventxclassificationsecuritytoken
    ADD CONSTRAINT fkg821wl3o2mwy3k7ysebk2donl FOREIGN KEY (securitytokenid, warehousecreateddate) REFERENCES security.securitytoken (securitytokenid, warehousecreateddate);


--
-- TOC entry 4693 (class 2606 OID 207253)
-- Name: eventxinvolvedpartysecuritytoken fkgechwpr8prmtfarbxf4s395tu; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.eventxinvolvedpartysecuritytoken
    ADD CONSTRAINT fkgechwpr8prmtfarbxf4s395tu FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 4705 (class 2606 OID 207258)
-- Name: eventxproductsecuritytoken fkgl97bfv9e5ua7l2clt415514f; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.eventxproductsecuritytoken
    ADD CONSTRAINT fkgl97bfv9e5ua7l2clt415514f FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4706 (class 2606 OID 207263)
-- Name: eventxproductsecuritytoken fkgpkp7841e76d4r6jv2drrorjf; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.eventxproductsecuritytoken
    ADD CONSTRAINT fkgpkp7841e76d4r6jv2drrorjf FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 4701 (class 2606 OID 207268)
-- Name: eventxproduct fkgxnd3jbg2f39e8ik5c0ivxdru; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.eventxproduct
    ADD CONSTRAINT fkgxnd3jbg2f39e8ik5c0ivxdru FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 4694 (class 2606 OID 207273)
-- Name: eventxinvolvedpartysecuritytoken fkh04u3m7l7xqsx19gjxiq084nw; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.eventxinvolvedpartysecuritytoken
    ADD CONSTRAINT fkh04u3m7l7xqsx19gjxiq084nw FOREIGN KEY (eventxinvolvedpartyid, warehousecreateddate) REFERENCES event.eventxinvolvedparty (eventxinvolvedpartyid, warehousecreateddate);


--
-- TOC entry 4659 (class 2606 OID 207278)
-- Name: eventxeventtype fkh0rt5ii9pw04gwly7db3xu1k; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.eventxeventtype
    ADD CONSTRAINT fkh0rt5ii9pw04gwly7db3xu1k FOREIGN KEY (eventid, warehousecreateddate) REFERENCES event.event (eventid, warehousecreateddate);


--
-- TOC entry 4657 (class 2606 OID 207283)
-- Name: eventxeventsecuritytoken fkhljdtw3p9eoe701gg8a3o21iy; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.eventxeventsecuritytoken
    ADD CONSTRAINT fkhljdtw3p9eoe701gg8a3o21iy FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 4622 (class 2606 OID 207288)
-- Name: eventxarrangement fkhyhd3s7f217ugwq3wo7pk5t1y; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.eventxarrangement
    ADD CONSTRAINT fkhyhd3s7f217ugwq3wo7pk5t1y FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 4648 (class 2606 OID 207293)
-- Name: eventxevent fki35b9qnbbkiv3vxem4lbvlhov; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.eventxevent
    ADD CONSTRAINT fki35b9qnbbkiv3vxem4lbvlhov FOREIGN KEY (classificationid, warehousecreateddate) REFERENCES classification.classification (classificationid, warehousecreateddate);


--
-- TOC entry 4674 (class 2606 OID 207298)
-- Name: eventxgeography fki5u3ixu9xfcy27ntux56f0gx6; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.eventxgeography
    ADD CONSTRAINT fki5u3ixu9xfcy27ntux56f0gx6 FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 4668 (class 2606 OID 207303)
-- Name: eventxeventtypesecuritytoken fki6cykrbt81enfl7o22ujsidj9; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.eventxeventtypesecuritytoken
    ADD CONSTRAINT fki6cykrbt81enfl7o22ujsidj9 FOREIGN KEY (securitytokenid, warehousecreateddate) REFERENCES security.securitytoken (securitytokenid, warehousecreateddate);


--
-- TOC entry 4660 (class 2606 OID 207308)
-- Name: eventxeventtype fkid8h6sd0odvwwwicoto22miy8; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.eventxeventtype
    ADD CONSTRAINT fkid8h6sd0odvwwwicoto22miy8 FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 4623 (class 2606 OID 207313)
-- Name: eventxarrangement fkii5wwqv91rn4ji3nwgiy3wd2l; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.eventxarrangement
    ADD CONSTRAINT fkii5wwqv91rn4ji3nwgiy3wd2l FOREIGN KEY (eventid, warehousecreateddate) REFERENCES event.event (eventid, warehousecreateddate);


--
-- TOC entry 4695 (class 2606 OID 207318)
-- Name: eventxinvolvedpartysecuritytoken fkin148rfphl52k2k9ihia0993r; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.eventxinvolvedpartysecuritytoken
    ADD CONSTRAINT fkin148rfphl52k2k9ihia0993r FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 4702 (class 2606 OID 207323)
-- Name: eventxproduct fkiskxy3i7nn1pjcegr6e1kmuxh; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.eventxproduct
    ADD CONSTRAINT fkiskxy3i7nn1pjcegr6e1kmuxh FOREIGN KEY (productid, warehousecreateddate) REFERENCES product.product (productid, warehousecreateddate);


--
-- TOC entry 4681 (class 2606 OID 207328)
-- Name: eventxgeographysecuritytoken fkiw30isi83vw6xrvmxj8nou8lf; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.eventxgeographysecuritytoken
    ADD CONSTRAINT fkiw30isi83vw6xrvmxj8nou8lf FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4688 (class 2606 OID 207333)
-- Name: eventxinvolvedparty fkiynqnmd943q3oxlqkjvdah2h; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.eventxinvolvedparty
    ADD CONSTRAINT fkiynqnmd943q3oxlqkjvdah2h FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 4675 (class 2606 OID 207338)
-- Name: eventxgeography fkiyrhx2xjplgljfvo8swoilr1q; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.eventxgeography
    ADD CONSTRAINT fkiyrhx2xjplgljfvo8swoilr1q FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4610 (class 2606 OID 207343)
-- Name: eventxaddress fkjkfm7v3719rvi0mu4nev2xu6d; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.eventxaddress
    ADD CONSTRAINT fkjkfm7v3719rvi0mu4nev2xu6d FOREIGN KEY (eventid, warehousecreateddate) REFERENCES event.event (eventid, warehousecreateddate);


--
-- TOC entry 4713 (class 2606 OID 207348)
-- Name: eventxresourceitem fkk74s5n31kgmbvmr362yle6kvd; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.eventxresourceitem
    ADD CONSTRAINT fkk74s5n31kgmbvmr362yle6kvd FOREIGN KEY (resourceitemid, warehousecreateddate) REFERENCES resource.resourceitem (resourceitemid, warehousecreateddate);


--
-- TOC entry 4643 (class 2606 OID 207353)
-- Name: eventxclassificationsecuritytoken fkkdkwjb6nhj76m65s840einvmw; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.eventxclassificationsecuritytoken
    ADD CONSTRAINT fkkdkwjb6nhj76m65s840einvmw FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4734 (class 2606 OID 207358)
-- Name: eventxrulessecuritytoken fkkosmoasfra60wqac2vrx5qddw; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.eventxrulessecuritytoken
    ADD CONSTRAINT fkkosmoasfra60wqac2vrx5qddw FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 4624 (class 2606 OID 207363)
-- Name: eventxarrangement fklj26m0snewjsmso5n6jskt2a4; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.eventxarrangement
    ADD CONSTRAINT fklj26m0snewjsmso5n6jskt2a4 FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4611 (class 2606 OID 207368)
-- Name: eventxaddress fklmpsaajrvosc033g4idqh17bv; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.eventxaddress
    ADD CONSTRAINT fklmpsaajrvosc033g4idqh17bv FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 4682 (class 2606 OID 207373)
-- Name: eventxgeographysecuritytoken fklr37auqe3m87viorqc1tsmkn; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.eventxgeographysecuritytoken
    ADD CONSTRAINT fklr37auqe3m87viorqc1tsmkn FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 4689 (class 2606 OID 207378)
-- Name: eventxinvolvedparty fkm4w7sl97j4p81cd2rehtd5jpd; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.eventxinvolvedparty
    ADD CONSTRAINT fkm4w7sl97j4p81cd2rehtd5jpd FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4612 (class 2606 OID 207383)
-- Name: eventxaddress fkm52nfxs3cy5syh6ar5mly3k64; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.eventxaddress
    ADD CONSTRAINT fkm52nfxs3cy5syh6ar5mly3k64 FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4649 (class 2606 OID 207388)
-- Name: eventxevent fkm690re4pvdyyeo38mfwgatnhk; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.eventxevent
    ADD CONSTRAINT fkm690re4pvdyyeo38mfwgatnhk FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 4714 (class 2606 OID 207393)
-- Name: eventxresourceitem fkmioseq29u1gk2kh4uhxsrpr5b; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.eventxresourceitem
    ADD CONSTRAINT fkmioseq29u1gk2kh4uhxsrpr5b FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 4719 (class 2606 OID 207398)
-- Name: eventxresourceitemsecuritytoken fkmoagxghmr17ytrthakoidoxvn; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.eventxresourceitemsecuritytoken
    ADD CONSTRAINT fkmoagxghmr17ytrthakoidoxvn FOREIGN KEY (eventxresourceitemid, warehousecreateddate) REFERENCES event.eventxresourceitem (eventxresourceitemid, warehousecreateddate);


--
-- TOC entry 4613 (class 2606 OID 207403)
-- Name: eventxaddress fkmtwd6myq1nc0g02s2s2nhkteo; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.eventxaddress
    ADD CONSTRAINT fkmtwd6myq1nc0g02s2s2nhkteo FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4593 (class 2606 OID 207408)
-- Name: eventsecuritytoken fkmwxej7o84e382w8iy2y6fwlmf; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.eventsecuritytoken
    ADD CONSTRAINT fkmwxej7o84e382w8iy2y6fwlmf FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 4707 (class 2606 OID 207413)
-- Name: eventxproductsecuritytoken fkn5dxxoefbl6hg9dm8wpthnyro; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.eventxproductsecuritytoken
    ADD CONSTRAINT fkn5dxxoefbl6hg9dm8wpthnyro FOREIGN KEY (eventxproductid, warehousecreateddate) REFERENCES event.eventxproduct (eventxproductid, warehousecreateddate);


--
-- TOC entry 4720 (class 2606 OID 207418)
-- Name: eventxresourceitemsecuritytoken fknjjtfvj551fm36x4gwx2ny47f; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.eventxresourceitemsecuritytoken
    ADD CONSTRAINT fknjjtfvj551fm36x4gwx2ny47f FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4669 (class 2606 OID 207423)
-- Name: eventxeventtypesecuritytoken fknq4vx7pf7dug9j4pr7u3mxas7; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.eventxeventtypesecuritytoken
    ADD CONSTRAINT fknq4vx7pf7dug9j4pr7u3mxas7 FOREIGN KEY (eventxeventtypeid, warehousecreateddate) REFERENCES event.eventxeventtype (eventxeventtypeid, warehousecreateddate);


--
-- TOC entry 4715 (class 2606 OID 207428)
-- Name: eventxresourceitem fkns5wkh4kiqo2l7a16029ntkpg; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.eventxresourceitem
    ADD CONSTRAINT fkns5wkh4kiqo2l7a16029ntkpg FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4721 (class 2606 OID 207433)
-- Name: eventxresourceitemsecuritytoken fko04e70wvi9b3hehhsu7pjj30y; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.eventxresourceitemsecuritytoken
    ADD CONSTRAINT fko04e70wvi9b3hehhsu7pjj30y FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 4635 (class 2606 OID 207438)
-- Name: eventxclassification fko28x8wv64sef2ifxjm8a4i2ft; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.eventxclassification
    ADD CONSTRAINT fko28x8wv64sef2ifxjm8a4i2ft FOREIGN KEY (classificationid, warehousecreateddate) REFERENCES classification.classification (classificationid, warehousecreateddate);


--
-- TOC entry 4594 (class 2606 OID 207443)
-- Name: eventsecuritytoken fko5kqswpu433kf5hcc3yh1w10e; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.eventsecuritytoken
    ADD CONSTRAINT fko5kqswpu433kf5hcc3yh1w10e FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4650 (class 2606 OID 207448)
-- Name: eventxevent fko7pb9lg0uhpllekuxaeml1xip; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.eventxevent
    ADD CONSTRAINT fko7pb9lg0uhpllekuxaeml1xip FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4708 (class 2606 OID 207453)
-- Name: eventxproductsecuritytoken fkod2lhedbtyh8y4sd0dg38l3dc; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.eventxproductsecuritytoken
    ADD CONSTRAINT fkod2lhedbtyh8y4sd0dg38l3dc FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4676 (class 2606 OID 207458)
-- Name: eventxgeography fkohfy1ao57carvvjcwcok5sch5; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.eventxgeography
    ADD CONSTRAINT fkohfy1ao57carvvjcwcok5sch5 FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4636 (class 2606 OID 207463)
-- Name: eventxclassification fkojl271yprimpjrrv6u1a2ggb6; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.eventxclassification
    ADD CONSTRAINT fkojl271yprimpjrrv6u1a2ggb6 FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 4629 (class 2606 OID 207468)
-- Name: eventxarrangementssecuritytoken fkom7ax04agkpio3h8xw30vb60m; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.eventxarrangementssecuritytoken
    ADD CONSTRAINT fkom7ax04agkpio3h8xw30vb60m FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 4644 (class 2606 OID 207473)
-- Name: eventxclassificationsecuritytoken fkoq4421g5p0yjlklv0231xbjel; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.eventxclassificationsecuritytoken
    ADD CONSTRAINT fkoq4421g5p0yjlklv0231xbjel FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4735 (class 2606 OID 207478)
-- Name: eventxrulessecuritytoken fkos5etiopqb270oj8wjby9dld9; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.eventxrulessecuritytoken
    ADD CONSTRAINT fkos5etiopqb270oj8wjby9dld9 FOREIGN KEY (eventxrulesid, warehousecreateddate) REFERENCES event.eventxrules (eventxrulesid, warehousecreateddate);


--
-- TOC entry 4637 (class 2606 OID 207483)
-- Name: eventxclassification fkp7fx787hsyq9nf8820wl2rh1x; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.eventxclassification
    ADD CONSTRAINT fkp7fx787hsyq9nf8820wl2rh1x FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4661 (class 2606 OID 207488)
-- Name: eventxeventtype fkp99ppwwnp2w9bnqg5pmf8b2hv; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.eventxeventtype
    ADD CONSTRAINT fkp99ppwwnp2w9bnqg5pmf8b2hv FOREIGN KEY (eventtypeid, warehousecreateddate) REFERENCES event.eventtype (eventtypeid, warehousecreateddate);


--
-- TOC entry 4638 (class 2606 OID 207493)
-- Name: eventxclassification fkpssgfk2o0m8r84lhnb04bh0om; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.eventxclassification
    ADD CONSTRAINT fkpssgfk2o0m8r84lhnb04bh0om FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4662 (class 2606 OID 207498)
-- Name: eventxeventtype fkpurdwniain7du77wld6bi9kfk; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.eventxeventtype
    ADD CONSTRAINT fkpurdwniain7du77wld6bi9kfk FOREIGN KEY (classificationid, warehousecreateddate) REFERENCES classification.classification (classificationid, warehousecreateddate);


--
-- TOC entry 4625 (class 2606 OID 207503)
-- Name: eventxarrangement fkpwoqs4xag1rw5tyrb062tyagd; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.eventxarrangement
    ADD CONSTRAINT fkpwoqs4xag1rw5tyrb062tyagd FOREIGN KEY (classificationid, warehousecreateddate) REFERENCES classification.classification (classificationid, warehousecreateddate);


--
-- TOC entry 4630 (class 2606 OID 207508)
-- Name: eventxarrangementssecuritytoken fkq3ik0e8bbij1nqvdu0jn1unvg; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.eventxarrangementssecuritytoken
    ADD CONSTRAINT fkq3ik0e8bbij1nqvdu0jn1unvg FOREIGN KEY (eventxarrangementsid, warehousecreateddate) REFERENCES event.eventxarrangement (eventxarrangementsid, warehousecreateddate);


--
-- TOC entry 4716 (class 2606 OID 207513)
-- Name: eventxresourceitem fkq5cq69kmrvba5howgpqkpyv2t; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.eventxresourceitem
    ADD CONSTRAINT fkq5cq69kmrvba5howgpqkpyv2t FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 4722 (class 2606 OID 207518)
-- Name: eventxresourceitemsecuritytoken fkq8fobq1u0tsuvj481ya8icdhe; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.eventxresourceitemsecuritytoken
    ADD CONSTRAINT fkq8fobq1u0tsuvj481ya8icdhe FOREIGN KEY (securitytokenid, warehousecreateddate) REFERENCES security.securitytoken (securitytokenid, warehousecreateddate);


--
-- TOC entry 4595 (class 2606 OID 207523)
-- Name: eventsecuritytoken fkqcwlmmiy1hf3p3ol2dy865mx5; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.eventsecuritytoken
    ADD CONSTRAINT fkqcwlmmiy1hf3p3ol2dy865mx5 FOREIGN KEY (eventsid, warehousecreateddate) REFERENCES event.event (eventid, warehousecreateddate);


--
-- TOC entry 4663 (class 2606 OID 207528)
-- Name: eventxeventtype fkqf8vxnj6qupg8u3wuiqcd247x; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.eventxeventtype
    ADD CONSTRAINT fkqf8vxnj6qupg8u3wuiqcd247x FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 4703 (class 2606 OID 207533)
-- Name: eventxproduct fkqj3yu6g3em9mm3gabikm12hke; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.eventxproduct
    ADD CONSTRAINT fkqj3yu6g3em9mm3gabikm12hke FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4677 (class 2606 OID 207538)
-- Name: eventxgeography fkqnbhmdykbnj6recqytrcm5x52; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.eventxgeography
    ADD CONSTRAINT fkqnbhmdykbnj6recqytrcm5x52 FOREIGN KEY (geographyid, warehousecreateddate) REFERENCES geography.geography (geographyid, warehousecreateddate);


--
-- TOC entry 4605 (class 2606 OID 207543)
-- Name: eventtypessecuritytoken fkr2xa1q4402m8pc5evyj1tw9q5; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.eventtypessecuritytoken
    ADD CONSTRAINT fkr2xa1q4402m8pc5evyj1tw9q5 FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 4683 (class 2606 OID 207548)
-- Name: eventxgeographysecuritytoken fkrekwdq9yy06eyoqgiwlh0qw4n; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.eventxgeographysecuritytoken
    ADD CONSTRAINT fkrekwdq9yy06eyoqgiwlh0qw4n FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 4651 (class 2606 OID 207553)
-- Name: eventxevent fks7qe80gktw6nesd2ijfu3j438; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.eventxevent
    ADD CONSTRAINT fks7qe80gktw6nesd2ijfu3j438 FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 4631 (class 2606 OID 207558)
-- Name: eventxarrangementssecuritytoken fks8terumw31sybh6o0x4sdbc2t; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.eventxarrangementssecuritytoken
    ADD CONSTRAINT fks8terumw31sybh6o0x4sdbc2t FOREIGN KEY (securitytokenid, warehousecreateddate) REFERENCES security.securitytoken (securitytokenid, warehousecreateddate);


--
-- TOC entry 4596 (class 2606 OID 207563)
-- Name: eventsecuritytoken fksa47kc62uve787xbc9h6syv0t; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.eventsecuritytoken
    ADD CONSTRAINT fksa47kc62uve787xbc9h6syv0t FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4690 (class 2606 OID 207568)
-- Name: eventxinvolvedparty fksoe05vt9d1tt0ot0rd7ns68l0; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.eventxinvolvedparty
    ADD CONSTRAINT fksoe05vt9d1tt0ot0rd7ns68l0 FOREIGN KEY (classificationid, warehousecreateddate) REFERENCES classification.classification (classificationid, warehousecreateddate);


--
-- TOC entry 4696 (class 2606 OID 207573)
-- Name: eventxinvolvedpartysecuritytoken fkssmudrecemndf6iufso5jpyoi; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.eventxinvolvedpartysecuritytoken
    ADD CONSTRAINT fkssmudrecemndf6iufso5jpyoi FOREIGN KEY (securitytokenid, warehousecreateddate) REFERENCES security.securitytoken (securitytokenid, warehousecreateddate);


--
-- TOC entry 4709 (class 2606 OID 207578)
-- Name: eventxproductsecuritytoken fkt6d41dk6xtiue499wasf738r; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.eventxproductsecuritytoken
    ADD CONSTRAINT fkt6d41dk6xtiue499wasf738r FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 4590 (class 2606 OID 207583)
-- Name: event fkt9syxmvp6c5pcyhyiv9c8qpb1; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.event
    ADD CONSTRAINT fkt9syxmvp6c5pcyhyiv9c8qpb1 FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4606 (class 2606 OID 207588)
-- Name: eventtypessecuritytoken fktcukxvcv6bvhbmcelmt16vroq; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.eventtypessecuritytoken
    ADD CONSTRAINT fktcukxvcv6bvhbmcelmt16vroq FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4632 (class 2606 OID 207593)
-- Name: eventxarrangementssecuritytoken fktiw0truwtwa3ublkae3jexugw; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.eventxarrangementssecuritytoken
    ADD CONSTRAINT fktiw0truwtwa3ublkae3jexugw FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4670 (class 2606 OID 207598)
-- Name: eventxeventtypesecuritytoken fkvymcuqjxqpd4so83dmpnj5mk; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.eventxeventtypesecuritytoken
    ADD CONSTRAINT fkvymcuqjxqpd4so83dmpnj5mk FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 4664 (class 2606 OID 207603)
-- Name: eventxeventtype fkw45a7dsxmiph0mrg1j2infhv; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE event.eventxeventtype
    ADD CONSTRAINT fkw45a7dsxmiph0mrg1j2infhv FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4759 (class 2606 OID 207608)
-- Name: geographyxgeography fk1dvwqevxm9g1ajxfofeyog39o; Type: FK CONSTRAINT; Schema: geography; Owner: postgres
--

ALTER TABLE geography.geographyxgeography
    ADD CONSTRAINT fk1dvwqevxm9g1ajxfofeyog39o FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 4760 (class 2606 OID 207613)
-- Name: geographyxgeography fk1x9ioslmt95w0fjxh1h7tgh22; Type: FK CONSTRAINT; Schema: geography; Owner: postgres
--

ALTER TABLE geography.geographyxgeography
    ADD CONSTRAINT fk1x9ioslmt95w0fjxh1h7tgh22 FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4766 (class 2606 OID 207618)
-- Name: geographyxgeographysecuritytoken fk2ho20x8qldli7owo63ieh7pdk; Type: FK CONSTRAINT; Schema: geography; Owner: postgres
--

ALTER TABLE geography.geographyxgeographysecuritytoken
    ADD CONSTRAINT fk2ho20x8qldli7owo63ieh7pdk FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 4736 (class 2606 OID 207623)
-- Name: geography fk3hp5hnnmkc6wh089ye90tm3o6; Type: FK CONSTRAINT; Schema: geography; Owner: postgres
--

ALTER TABLE geography.geography
    ADD CONSTRAINT fk3hp5hnnmkc6wh089ye90tm3o6 FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 4772 (class 2606 OID 207628)
-- Name: geographyxresourceitem fk4cbs953wqd4v6mb2yco9uu2cy; Type: FK CONSTRAINT; Schema: geography; Owner: postgres
--

ALTER TABLE geography.geographyxresourceitem
    ADD CONSTRAINT fk4cbs953wqd4v6mb2yco9uu2cy FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4779 (class 2606 OID 207633)
-- Name: geographyxresourceitemsecuritytoken fk57l6e5bda4i1n4re0hw21706u; Type: FK CONSTRAINT; Schema: geography; Owner: postgres
--

ALTER TABLE geography.geographyxresourceitemsecuritytoken
    ADD CONSTRAINT fk57l6e5bda4i1n4re0hw21706u FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4780 (class 2606 OID 207638)
-- Name: geographyxresourceitemsecuritytoken fk5j6a8twkt85ffakx17foe5klo; Type: FK CONSTRAINT; Schema: geography; Owner: postgres
--

ALTER TABLE geography.geographyxresourceitemsecuritytoken
    ADD CONSTRAINT fk5j6a8twkt85ffakx17foe5klo FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 4753 (class 2606 OID 207643)
-- Name: geographyxclassificationsecuritytoken fk5jg1k2am65j8ibiky7mc0inxf; Type: FK CONSTRAINT; Schema: geography; Owner: postgres
--

ALTER TABLE geography.geographyxclassificationsecuritytoken
    ADD CONSTRAINT fk5jg1k2am65j8ibiky7mc0inxf FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4747 (class 2606 OID 207648)
-- Name: geographyxclassification fk5ohjj567mkcmt76ptgsq2qsin; Type: FK CONSTRAINT; Schema: geography; Owner: postgres
--

ALTER TABLE geography.geographyxclassification
    ADD CONSTRAINT fk5ohjj567mkcmt76ptgsq2qsin FOREIGN KEY (classificationid, warehousecreateddate) REFERENCES classification.classification (classificationid, warehousecreateddate);


--
-- TOC entry 4748 (class 2606 OID 207653)
-- Name: geographyxclassification fk5p12kjl7g353s2w727ulp384s; Type: FK CONSTRAINT; Schema: geography; Owner: postgres
--

ALTER TABLE geography.geographyxclassification
    ADD CONSTRAINT fk5p12kjl7g353s2w727ulp384s FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4781 (class 2606 OID 207658)
-- Name: geographyxresourceitemsecuritytoken fk6m9u05ntxa7n822r58ewn00e6; Type: FK CONSTRAINT; Schema: geography; Owner: postgres
--

ALTER TABLE geography.geographyxresourceitemsecuritytoken
    ADD CONSTRAINT fk6m9u05ntxa7n822r58ewn00e6 FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 4741 (class 2606 OID 207663)
-- Name: geographysecuritytoken fk7ate5yexxkqmll1407emnf36i; Type: FK CONSTRAINT; Schema: geography; Owner: postgres
--

ALTER TABLE geography.geographysecuritytoken
    ADD CONSTRAINT fk7ate5yexxkqmll1407emnf36i FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 4742 (class 2606 OID 207668)
-- Name: geographysecuritytoken fk8obgl7xdf7mth9vgblc6u057d; Type: FK CONSTRAINT; Schema: geography; Owner: postgres
--

ALTER TABLE geography.geographysecuritytoken
    ADD CONSTRAINT fk8obgl7xdf7mth9vgblc6u057d FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4743 (class 2606 OID 207673)
-- Name: geographysecuritytoken fk9u01t3it5fhcfyw2q3x2r19f6; Type: FK CONSTRAINT; Schema: geography; Owner: postgres
--

ALTER TABLE geography.geographysecuritytoken
    ADD CONSTRAINT fk9u01t3it5fhcfyw2q3x2r19f6 FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4749 (class 2606 OID 207678)
-- Name: geographyxclassification fkafecxpt71ni5iel6aklaku4f5; Type: FK CONSTRAINT; Schema: geography; Owner: postgres
--

ALTER TABLE geography.geographyxclassification
    ADD CONSTRAINT fkafecxpt71ni5iel6aklaku4f5 FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 4773 (class 2606 OID 207683)
-- Name: geographyxresourceitem fkan3li0682x6rj5rnqgo2s3bm3; Type: FK CONSTRAINT; Schema: geography; Owner: postgres
--

ALTER TABLE geography.geographyxresourceitem
    ADD CONSTRAINT fkan3li0682x6rj5rnqgo2s3bm3 FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4744 (class 2606 OID 207688)
-- Name: geographysecuritytoken fkbrt91rfhyvn2hp76aga8vvdoq; Type: FK CONSTRAINT; Schema: geography; Owner: postgres
--

ALTER TABLE geography.geographysecuritytoken
    ADD CONSTRAINT fkbrt91rfhyvn2hp76aga8vvdoq FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 4774 (class 2606 OID 207693)
-- Name: geographyxresourceitem fkbvccen8s30rkpie76wyr1mixd; Type: FK CONSTRAINT; Schema: geography; Owner: postgres
--

ALTER TABLE geography.geographyxresourceitem
    ADD CONSTRAINT fkbvccen8s30rkpie76wyr1mixd FOREIGN KEY (classificationid, warehousecreateddate) REFERENCES classification.classification (classificationid, warehousecreateddate);


--
-- TOC entry 4737 (class 2606 OID 207698)
-- Name: geography fkd30h42gp0snfq2lrm61hxeo7a; Type: FK CONSTRAINT; Schema: geography; Owner: postgres
--

ALTER TABLE geography.geography
    ADD CONSTRAINT fkd30h42gp0snfq2lrm61hxeo7a FOREIGN KEY (classificationid, warehousecreateddate) REFERENCES classification.classification (classificationid, warehousecreateddate);


--
-- TOC entry 4761 (class 2606 OID 207703)
-- Name: geographyxgeography fkdwrnhxis6ef8f2rkhxv56jmgu; Type: FK CONSTRAINT; Schema: geography; Owner: postgres
--

ALTER TABLE geography.geographyxgeography
    ADD CONSTRAINT fkdwrnhxis6ef8f2rkhxv56jmgu FOREIGN KEY (childgeographyid, warehousecreateddate) REFERENCES geography.geography (geographyid, warehousecreateddate);


--
-- TOC entry 4762 (class 2606 OID 207708)
-- Name: geographyxgeography fkdxliyywqpd69bpwfsbm58tia1; Type: FK CONSTRAINT; Schema: geography; Owner: postgres
--

ALTER TABLE geography.geographyxgeography
    ADD CONSTRAINT fkdxliyywqpd69bpwfsbm58tia1 FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4738 (class 2606 OID 207713)
-- Name: geography fke3cvg85u45hqr34o3srx8njbv; Type: FK CONSTRAINT; Schema: geography; Owner: postgres
--

ALTER TABLE geography.geography
    ADD CONSTRAINT fke3cvg85u45hqr34o3srx8njbv FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4767 (class 2606 OID 207718)
-- Name: geographyxgeographysecuritytoken fke94i2ly219wcofp3qlsyepcjx; Type: FK CONSTRAINT; Schema: geography; Owner: postgres
--

ALTER TABLE geography.geographyxgeographysecuritytoken
    ADD CONSTRAINT fke94i2ly219wcofp3qlsyepcjx FOREIGN KEY (securitytokenid, warehousecreateddate) REFERENCES security.securitytoken (securitytokenid, warehousecreateddate);


--
-- TOC entry 4754 (class 2606 OID 207723)
-- Name: geographyxclassificationsecuritytoken fkeenbngjgunycd80bgeoaylrwp; Type: FK CONSTRAINT; Schema: geography; Owner: postgres
--

ALTER TABLE geography.geographyxclassificationsecuritytoken
    ADD CONSTRAINT fkeenbngjgunycd80bgeoaylrwp FOREIGN KEY (securitytokenid, warehousecreateddate) REFERENCES security.securitytoken (securitytokenid, warehousecreateddate);


--
-- TOC entry 4739 (class 2606 OID 207728)
-- Name: geography fkfe6vl7bajuvj2k024mtfxvjwy; Type: FK CONSTRAINT; Schema: geography; Owner: postgres
--

ALTER TABLE geography.geography
    ADD CONSTRAINT fkfe6vl7bajuvj2k024mtfxvjwy FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4755 (class 2606 OID 207733)
-- Name: geographyxclassificationsecuritytoken fkffvnvtxqjjbur12t6x7qcbn2h; Type: FK CONSTRAINT; Schema: geography; Owner: postgres
--

ALTER TABLE geography.geographyxclassificationsecuritytoken
    ADD CONSTRAINT fkffvnvtxqjjbur12t6x7qcbn2h FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 4782 (class 2606 OID 207738)
-- Name: geographyxresourceitemsecuritytoken fkfwajhfr051wxdtw76afh5totc; Type: FK CONSTRAINT; Schema: geography; Owner: postgres
--

ALTER TABLE geography.geographyxresourceitemsecuritytoken
    ADD CONSTRAINT fkfwajhfr051wxdtw76afh5totc FOREIGN KEY (securitytokenid, warehousecreateddate) REFERENCES security.securitytoken (securitytokenid, warehousecreateddate);


--
-- TOC entry 4775 (class 2606 OID 207743)
-- Name: geographyxresourceitem fkgd4x1t5pb1lnp2etg9yptt7n7; Type: FK CONSTRAINT; Schema: geography; Owner: postgres
--

ALTER TABLE geography.geographyxresourceitem
    ADD CONSTRAINT fkgd4x1t5pb1lnp2etg9yptt7n7 FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 4763 (class 2606 OID 207748)
-- Name: geographyxgeography fkglfnoy0sok1ayveu5glm26neo; Type: FK CONSTRAINT; Schema: geography; Owner: postgres
--

ALTER TABLE geography.geographyxgeography
    ADD CONSTRAINT fkglfnoy0sok1ayveu5glm26neo FOREIGN KEY (parentgeographyid, warehousecreateddate) REFERENCES geography.geography (geographyid, warehousecreateddate);


--
-- TOC entry 4750 (class 2606 OID 207753)
-- Name: geographyxclassification fkgt1brdxa60taodmc6cg28r0lb; Type: FK CONSTRAINT; Schema: geography; Owner: postgres
--

ALTER TABLE geography.geographyxclassification
    ADD CONSTRAINT fkgt1brdxa60taodmc6cg28r0lb FOREIGN KEY (geographyid, warehousecreateddate) REFERENCES geography.geography (geographyid, warehousecreateddate);


--
-- TOC entry 4745 (class 2606 OID 207758)
-- Name: geographysecuritytoken fkh6x5jqejxvpq70sg8w8bcddvu; Type: FK CONSTRAINT; Schema: geography; Owner: postgres
--

ALTER TABLE geography.geographysecuritytoken
    ADD CONSTRAINT fkh6x5jqejxvpq70sg8w8bcddvu FOREIGN KEY (geographyid, warehousecreateddate) REFERENCES geography.geography (geographyid, warehousecreateddate);


--
-- TOC entry 4764 (class 2606 OID 207763)
-- Name: geographyxgeography fkh8w4rt46cn0a8rdk7p46i6yw6; Type: FK CONSTRAINT; Schema: geography; Owner: postgres
--

ALTER TABLE geography.geographyxgeography
    ADD CONSTRAINT fkh8w4rt46cn0a8rdk7p46i6yw6 FOREIGN KEY (classificationid, warehousecreateddate) REFERENCES classification.classification (classificationid, warehousecreateddate);


--
-- TOC entry 4740 (class 2606 OID 207768)
-- Name: geography fkhfy9e7gg8xlswihrx45wk0093; Type: FK CONSTRAINT; Schema: geography; Owner: postgres
--

ALTER TABLE geography.geography
    ADD CONSTRAINT fkhfy9e7gg8xlswihrx45wk0093 FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 4776 (class 2606 OID 207773)
-- Name: geographyxresourceitem fkikpxdma2cnfy7ak4hqqskegdt; Type: FK CONSTRAINT; Schema: geography; Owner: postgres
--

ALTER TABLE geography.geographyxresourceitem
    ADD CONSTRAINT fkikpxdma2cnfy7ak4hqqskegdt FOREIGN KEY (resourceitemid, warehousecreateddate) REFERENCES resource.resourceitem (resourceitemid, warehousecreateddate);


--
-- TOC entry 4768 (class 2606 OID 207778)
-- Name: geographyxgeographysecuritytoken fkjqo2qgds42bxfxae3xjj8528w; Type: FK CONSTRAINT; Schema: geography; Owner: postgres
--

ALTER TABLE geography.geographyxgeographysecuritytoken
    ADD CONSTRAINT fkjqo2qgds42bxfxae3xjj8528w FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 4751 (class 2606 OID 207783)
-- Name: geographyxclassification fkl1bl1px429vykkiut1fdl7o0n; Type: FK CONSTRAINT; Schema: geography; Owner: postgres
--

ALTER TABLE geography.geographyxclassification
    ADD CONSTRAINT fkl1bl1px429vykkiut1fdl7o0n FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 4756 (class 2606 OID 207788)
-- Name: geographyxclassificationsecuritytoken fkl5321rf4hio0kms78ic3f8bst; Type: FK CONSTRAINT; Schema: geography; Owner: postgres
--

ALTER TABLE geography.geographyxclassificationsecuritytoken
    ADD CONSTRAINT fkl5321rf4hio0kms78ic3f8bst FOREIGN KEY (geographyxclassificationid, warehousecreateddate) REFERENCES geography.geographyxclassification (geographyxclassificationid, warehousecreateddate);


--
-- TOC entry 4769 (class 2606 OID 207793)
-- Name: geographyxgeographysecuritytoken fklclax5c7qktjee2sf4mysgub8; Type: FK CONSTRAINT; Schema: geography; Owner: postgres
--

ALTER TABLE geography.geographyxgeographysecuritytoken
    ADD CONSTRAINT fklclax5c7qktjee2sf4mysgub8 FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4777 (class 2606 OID 207798)
-- Name: geographyxresourceitem fkmfe0l4k1dgd3890oxlt65lm8g; Type: FK CONSTRAINT; Schema: geography; Owner: postgres
--

ALTER TABLE geography.geographyxresourceitem
    ADD CONSTRAINT fkmfe0l4k1dgd3890oxlt65lm8g FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 4757 (class 2606 OID 207803)
-- Name: geographyxclassificationsecuritytoken fkmhery9mlbon8qfm9i7vymp6jy; Type: FK CONSTRAINT; Schema: geography; Owner: postgres
--

ALTER TABLE geography.geographyxclassificationsecuritytoken
    ADD CONSTRAINT fkmhery9mlbon8qfm9i7vymp6jy FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4746 (class 2606 OID 207808)
-- Name: geographysecuritytoken fkmpal48yq5kf23jj4x9r77v0g7; Type: FK CONSTRAINT; Schema: geography; Owner: postgres
--

ALTER TABLE geography.geographysecuritytoken
    ADD CONSTRAINT fkmpal48yq5kf23jj4x9r77v0g7 FOREIGN KEY (securitytokenid, warehousecreateddate) REFERENCES security.securitytoken (securitytokenid, warehousecreateddate);


--
-- TOC entry 4783 (class 2606 OID 207813)
-- Name: geographyxresourceitemsecuritytoken fkodr0owqhi39gwkkygjyyk3sd9; Type: FK CONSTRAINT; Schema: geography; Owner: postgres
--

ALTER TABLE geography.geographyxresourceitemsecuritytoken
    ADD CONSTRAINT fkodr0owqhi39gwkkygjyyk3sd9 FOREIGN KEY (geographyxresourceitemid, warehousecreateddate) REFERENCES geography.geographyxresourceitem (geographyxresourceitemid, warehousecreateddate);


--
-- TOC entry 4765 (class 2606 OID 207818)
-- Name: geographyxgeography fkp5cwyefygfcshowmbuctqm2bd; Type: FK CONSTRAINT; Schema: geography; Owner: postgres
--

ALTER TABLE geography.geographyxgeography
    ADD CONSTRAINT fkp5cwyefygfcshowmbuctqm2bd FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 4758 (class 2606 OID 207823)
-- Name: geographyxclassificationsecuritytoken fkp6nlv6glhcy1fyqj5bj5hggud; Type: FK CONSTRAINT; Schema: geography; Owner: postgres
--

ALTER TABLE geography.geographyxclassificationsecuritytoken
    ADD CONSTRAINT fkp6nlv6glhcy1fyqj5bj5hggud FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 4784 (class 2606 OID 207828)
-- Name: geographyxresourceitemsecuritytoken fkpj8pp6mw0061pdlxv6p8f9qv8; Type: FK CONSTRAINT; Schema: geography; Owner: postgres
--

ALTER TABLE geography.geographyxresourceitemsecuritytoken
    ADD CONSTRAINT fkpj8pp6mw0061pdlxv6p8f9qv8 FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4778 (class 2606 OID 207833)
-- Name: geographyxresourceitem fkplqcl8yui4k6xm0faaa3v9v3v; Type: FK CONSTRAINT; Schema: geography; Owner: postgres
--

ALTER TABLE geography.geographyxresourceitem
    ADD CONSTRAINT fkplqcl8yui4k6xm0faaa3v9v3v FOREIGN KEY (geographyid, warehousecreateddate) REFERENCES geography.geography (geographyid, warehousecreateddate);


--
-- TOC entry 4770 (class 2606 OID 207838)
-- Name: geographyxgeographysecuritytoken fkqd1qu9501eat5nh9sjc6ejg27; Type: FK CONSTRAINT; Schema: geography; Owner: postgres
--

ALTER TABLE geography.geographyxgeographysecuritytoken
    ADD CONSTRAINT fkqd1qu9501eat5nh9sjc6ejg27 FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4771 (class 2606 OID 207843)
-- Name: geographyxgeographysecuritytoken fkqth25orgkxdx6xvepkv9dkco4; Type: FK CONSTRAINT; Schema: geography; Owner: postgres
--

ALTER TABLE geography.geographyxgeographysecuritytoken
    ADD CONSTRAINT fkqth25orgkxdx6xvepkv9dkco4 FOREIGN KEY (geographyxgeographyid, warehousecreateddate) REFERENCES geography.geographyxgeography (geographyxgeographyid, warehousecreateddate);


--
-- TOC entry 4752 (class 2606 OID 207848)
-- Name: geographyxclassification fkrtu1y36y10o03rcrhrvacvki1; Type: FK CONSTRAINT; Schema: geography; Owner: postgres
--

ALTER TABLE geography.geographyxclassification
    ADD CONSTRAINT fkrtu1y36y10o03rcrhrvacvki1 FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4974 (class 2606 OID 207853)
-- Name: involvedpartyxrules fk1306t8qbngy6vd6gt28t6w515; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyxrules
    ADD CONSTRAINT fk1306t8qbngy6vd6gt28t6w515 FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 4841 (class 2606 OID 207858)
-- Name: involvedpartysecuritytoken fk14xr935tw00ygoqib0cxoxmvf; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartysecuritytoken
    ADD CONSTRAINT fk14xr935tw00ygoqib0cxoxmvf FOREIGN KEY (securitytokenid, warehousecreateddate) REFERENCES security.securitytoken (securitytokenid, warehousecreateddate);


--
-- TOC entry 4915 (class 2606 OID 207863)
-- Name: involvedpartyxinvolvedpartysecuritytoken fk18x9igr3j8p5ug8wm2mattpsn; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyxinvolvedpartysecuritytoken
    ADD CONSTRAINT fk18x9igr3j8p5ug8wm2mattpsn FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4896 (class 2606 OID 207868)
-- Name: involvedpartyxinvolvedpartyidentificationtypesecuritytoken fk1cih29htv8dr2nqs6113mhy20; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyxinvolvedpartyidentificationtypesecuritytoken
    ADD CONSTRAINT fk1cih29htv8dr2nqs6113mhy20 FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4982 (class 2606 OID 207873)
-- Name: involvedpartyxrulessecuritytoken fk1gcn1u4lcsxql11rx2ydvwnbu; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyxrulessecuritytoken
    ADD CONSTRAINT fk1gcn1u4lcsxql11rx2ydvwnbu FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 4870 (class 2606 OID 207878)
-- Name: involvedpartyxclassification fk1k9352drnr2qjxk0itdlpiphl; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyxclassification
    ADD CONSTRAINT fk1k9352drnr2qjxk0itdlpiphl FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 4871 (class 2606 OID 207883)
-- Name: involvedpartyxclassification fk1l1b8kh214rut78airw106ska; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyxclassification
    ADD CONSTRAINT fk1l1b8kh214rut78airw106ska FOREIGN KEY (involvedpartyid, warehousecreateddate) REFERENCES party.involvedparty (involvedpartyid, warehousecreateddate);


--
-- TOC entry 4825 (class 2606 OID 207888)
-- Name: involvedpartyorganicsecuritytoken fk1lqut70vjbvicq402lw3avani; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyorganicsecuritytoken
    ADD CONSTRAINT fk1lqut70vjbvicq402lw3avani FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4793 (class 2606 OID 207893)
-- Name: involvedpartyidentificationtypesecuritytoken fk1o1ufjc0u6yv02pfqokf19xhv; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyidentificationtypesecuritytoken
    ADD CONSTRAINT fk1o1ufjc0u6yv02pfqokf19xhv FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 4882 (class 2606 OID 207898)
-- Name: involvedpartyxinvolvedparty fk1opo02p7o9r0k39gmafbukmdk; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyxinvolvedparty
    ADD CONSTRAINT fk1opo02p7o9r0k39gmafbukmdk FOREIGN KEY (childinvolvedpartyid, warehousecreateddate) REFERENCES party.involvedparty (involvedpartyid, warehousecreateddate);


--
-- TOC entry 4847 (class 2606 OID 207903)
-- Name: involvedpartytype fk1v128xeri7d1pkfnltmnoo1co; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartytype
    ADD CONSTRAINT fk1v128xeri7d1pkfnltmnoo1co FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 4921 (class 2606 OID 207908)
-- Name: involvedpartyxinvolvedpartytype fk1w89pqnk0aoy7o41vm6wmidxd; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyxinvolvedpartytype
    ADD CONSTRAINT fk1w89pqnk0aoy7o41vm6wmidxd FOREIGN KEY (classificationid, warehousecreateddate) REFERENCES classification.classification (classificationid, warehousecreateddate);


--
-- TOC entry 4831 (class 2606 OID 207913)
-- Name: involvedpartyorganictype fk1wmvw7vfk12aop3lrals1o1pj; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyorganictype
    ADD CONSTRAINT fk1wmvw7vfk12aop3lrals1o1pj FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4851 (class 2606 OID 207918)
-- Name: involvedpartytypesecuritytoken fk217dwfh6c779f14rd2w8sm1vi; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartytypesecuritytoken
    ADD CONSTRAINT fk217dwfh6c779f14rd2w8sm1vi FOREIGN KEY (securitytokenid, warehousecreateddate) REFERENCES security.securitytoken (securitytokenid, warehousecreateddate);


--
-- TOC entry 4968 (class 2606 OID 207923)
-- Name: involvedpartyxresourceitemsecuritytoken fk27wqwvha1jdn24hktf0sx5qs9; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyxresourceitemsecuritytoken
    ADD CONSTRAINT fk27wqwvha1jdn24hktf0sx5qs9 FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 4928 (class 2606 OID 207928)
-- Name: involvedpartyxinvolvedpartytypesecuritytoken fk2erptg0ji67dbv45kqtodxb0c; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyxinvolvedpartytypesecuritytoken
    ADD CONSTRAINT fk2erptg0ji67dbv45kqtodxb0c FOREIGN KEY (involvedpartyxinvolvedpartytypeid, warehousecreateddate) REFERENCES party.involvedpartyxinvolvedpartytype (involvedpartyxinvolvedpartytypeid, warehousecreateddate);


--
-- TOC entry 4809 (class 2606 OID 207933)
-- Name: involvedpartynonorganic fk2fa0fq7vy2j3i2fog95gffalm; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartynonorganic
    ADD CONSTRAINT fk2fa0fq7vy2j3i2fog95gffalm FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 4934 (class 2606 OID 207938)
-- Name: involvedpartyxproduct fk2hwee6e8u06db7hbr07aglskd; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyxproduct
    ADD CONSTRAINT fk2hwee6e8u06db7hbr07aglskd FOREIGN KEY (involvedpartyxproductid, warehousecreateddate) REFERENCES party.involvedpartyxproduct (involvedpartyxproductid, warehousecreateddate);


--
-- TOC entry 4948 (class 2606 OID 207943)
-- Name: involvedpartyxproducttype fk2jbtjrhepl2mad2ogocxmdr2h; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyxproducttype
    ADD CONSTRAINT fk2jbtjrhepl2mad2ogocxmdr2h FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4961 (class 2606 OID 207948)
-- Name: involvedpartyxresourceitem fk2lynx18ubx3g0afp6rv021wty; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyxresourceitem
    ADD CONSTRAINT fk2lynx18ubx3g0afp6rv021wty FOREIGN KEY (involvedpartyid, warehousecreateddate) REFERENCES party.involvedparty (involvedpartyid, warehousecreateddate);


--
-- TOC entry 4835 (class 2606 OID 207953)
-- Name: involvedpartyorganictypesecuritytoken fk2rtgc2ltnx9facnuwt0aj6fpe; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyorganictypesecuritytoken
    ADD CONSTRAINT fk2rtgc2ltnx9facnuwt0aj6fpe FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4820 (class 2606 OID 207958)
-- Name: involvedpartyorganic fk2u5a4jkumk881cv7x3ei6vv22; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyorganic
    ADD CONSTRAINT fk2u5a4jkumk881cv7x3ei6vv22 FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4799 (class 2606 OID 207963)
-- Name: involvedpartynametype fk2xslqqsshflrv1q0ps8p5woad; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartynametype
    ADD CONSTRAINT fk2xslqqsshflrv1q0ps8p5woad FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4864 (class 2606 OID 207968)
-- Name: involvedpartyxaddresssecuritytoken fk32jyotpfsamhn8afbs8xgdw5y; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyxaddresssecuritytoken
    ADD CONSTRAINT fk32jyotpfsamhn8afbs8xgdw5y FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4857 (class 2606 OID 207973)
-- Name: involvedpartyxaddress fk3ehfta90cc7ha3uo6wplucc09; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyxaddress
    ADD CONSTRAINT fk3ehfta90cc7ha3uo6wplucc09 FOREIGN KEY (addressid, warehousecreateddate) REFERENCES address.address (addressid, warehousecreateddate);


--
-- TOC entry 4852 (class 2606 OID 207978)
-- Name: involvedpartytypesecuritytoken fk3fs2knats407bhami7ulk0ch9; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartytypesecuritytoken
    ADD CONSTRAINT fk3fs2knats407bhami7ulk0ch9 FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 4962 (class 2606 OID 207983)
-- Name: involvedpartyxresourceitem fk3igrqj1tpl6viwlb4jn4nlr9; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyxresourceitem
    ADD CONSTRAINT fk3igrqj1tpl6viwlb4jn4nlr9 FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 4876 (class 2606 OID 207988)
-- Name: involvedpartyxclassificationsecuritytoken fk3jrcy00rqjcc9lotsouknd88; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyxclassificationsecuritytoken
    ADD CONSTRAINT fk3jrcy00rqjcc9lotsouknd88 FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4922 (class 2606 OID 207993)
-- Name: involvedpartyxinvolvedpartytype fk3pqtk4c0d0raqtidrxy0kpohi; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyxinvolvedpartytype
    ADD CONSTRAINT fk3pqtk4c0d0raqtidrxy0kpohi FOREIGN KEY (involvedpartyid, warehousecreateddate) REFERENCES party.involvedparty (involvedpartyid, warehousecreateddate);


--
-- TOC entry 4858 (class 2606 OID 207998)
-- Name: involvedpartyxaddress fk4flitthmlghp6mj3h7maq4w84; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyxaddress
    ADD CONSTRAINT fk4flitthmlghp6mj3h7maq4w84 FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 4785 (class 2606 OID 208003)
-- Name: involvedparty fk4gadr7utku5dispktvjwbl2cu; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedparty
    ADD CONSTRAINT fk4gadr7utku5dispktvjwbl2cu FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4814 (class 2606 OID 208008)
-- Name: involvedpartynonorganicsecuritytoken fk4igjsit3bwp3b387pa19s1sej; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartynonorganicsecuritytoken
    ADD CONSTRAINT fk4igjsit3bwp3b387pa19s1sej FOREIGN KEY (involvedpartynonorganicid, warehousecreateddate) REFERENCES party.involvedpartynonorganic (involvedpartynonorganicid, warehousecreateddate);


--
-- TOC entry 4929 (class 2606 OID 208013)
-- Name: involvedpartyxinvolvedpartytypesecuritytoken fk4jy62mro7v8k9isc2tc9ikaat; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyxinvolvedpartytypesecuritytoken
    ADD CONSTRAINT fk4jy62mro7v8k9isc2tc9ikaat FOREIGN KEY (securitytokenid, warehousecreateddate) REFERENCES security.securitytoken (securitytokenid, warehousecreateddate);


--
-- TOC entry 4794 (class 2606 OID 208018)
-- Name: involvedpartyidentificationtypesecuritytoken fk4pb209rtplqb1uj5wtx2xr5mg; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyidentificationtypesecuritytoken
    ADD CONSTRAINT fk4pb209rtplqb1uj5wtx2xr5mg FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 4883 (class 2606 OID 208023)
-- Name: involvedpartyxinvolvedparty fk55h5hqcp98fursty4tgy1krgd; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyxinvolvedparty
    ADD CONSTRAINT fk55h5hqcp98fursty4tgy1krgd FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4865 (class 2606 OID 208028)
-- Name: involvedpartyxaddresssecuritytoken fk58kov7xpcnh58m7m0rxhs6g30; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyxaddresssecuritytoken
    ADD CONSTRAINT fk58kov7xpcnh58m7m0rxhs6g30 FOREIGN KEY (involvedpartyxaddressid, warehousecreateddate) REFERENCES party.involvedpartyxaddress (involvedpartyxaddressid, warehousecreateddate);


--
-- TOC entry 4803 (class 2606 OID 208033)
-- Name: involvedpartynametypesecuritytoken fk5dtp2b2133pffte1i7gxtom1t; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartynametypesecuritytoken
    ADD CONSTRAINT fk5dtp2b2133pffte1i7gxtom1t FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 4902 (class 2606 OID 208038)
-- Name: involvedpartyxinvolvedpartynametype fk5x12pwiby030udsbagyaqxluf; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyxinvolvedpartynametype
    ADD CONSTRAINT fk5x12pwiby030udsbagyaqxluf FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 4859 (class 2606 OID 208043)
-- Name: involvedpartyxaddress fk6c2fjdp7lxcfy0ar93ni78dcv; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyxaddress
    ADD CONSTRAINT fk6c2fjdp7lxcfy0ar93ni78dcv FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4832 (class 2606 OID 208048)
-- Name: involvedpartyorganictype fk6ei1dirma8r1y7lh4glpn9cuy; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyorganictype
    ADD CONSTRAINT fk6ei1dirma8r1y7lh4glpn9cuy FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 4975 (class 2606 OID 208053)
-- Name: involvedpartyxrules fk6hhyqulmrh1w7g3212r7jfkaw; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyxrules
    ADD CONSTRAINT fk6hhyqulmrh1w7g3212r7jfkaw FOREIGN KEY (classificationid, warehousecreateddate) REFERENCES classification.classification (classificationid, warehousecreateddate);


--
-- TOC entry 4884 (class 2606 OID 208058)
-- Name: involvedpartyxinvolvedparty fk6ih93cr42n69eamwdjj8a2ygn; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyxinvolvedparty
    ADD CONSTRAINT fk6ih93cr42n69eamwdjj8a2ygn FOREIGN KEY (parentinvolvedpartyid, warehousecreateddate) REFERENCES party.involvedparty (involvedpartyid, warehousecreateddate);


--
-- TOC entry 4916 (class 2606 OID 208063)
-- Name: involvedpartyxinvolvedpartysecuritytoken fk6l4fxgap9wfdttqp33s0wyut4; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyxinvolvedpartysecuritytoken
    ADD CONSTRAINT fk6l4fxgap9wfdttqp33s0wyut4 FOREIGN KEY (securitytokenid, warehousecreateddate) REFERENCES security.securitytoken (securitytokenid, warehousecreateddate);


--
-- TOC entry 4889 (class 2606 OID 208068)
-- Name: involvedpartyxinvolvedpartyidentificationtype fk6mi0299iv61l8tmmegrfwi2w3; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyxinvolvedpartyidentificationtype
    ADD CONSTRAINT fk6mi0299iv61l8tmmegrfwi2w3 FOREIGN KEY (involvedpartyidentificationtypeid, warehousecreateddate) REFERENCES party.involvedpartyidentificationtype (involvedpartyidentificationtypeid, warehousecreateddate);


--
-- TOC entry 4877 (class 2606 OID 208073)
-- Name: involvedpartyxclassificationsecuritytoken fk6pqhjtv3yvs3afa2dg5erfbad; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyxclassificationsecuritytoken
    ADD CONSTRAINT fk6pqhjtv3yvs3afa2dg5erfbad FOREIGN KEY (securitytokenid, warehousecreateddate) REFERENCES security.securitytoken (securitytokenid, warehousecreateddate);


--
-- TOC entry 4942 (class 2606 OID 208078)
-- Name: involvedpartyxproductsecuritytoken fk6tukdpqv5fg05yawnk4f35bun; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyxproductsecuritytoken
    ADD CONSTRAINT fk6tukdpqv5fg05yawnk4f35bun FOREIGN KEY (securitytokenid, warehousecreateddate) REFERENCES security.securitytoken (securitytokenid, warehousecreateddate);


--
-- TOC entry 4848 (class 2606 OID 208083)
-- Name: involvedpartytype fk6wemtilbmlhs3b040hu5tfn7v; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartytype
    ADD CONSTRAINT fk6wemtilbmlhs3b040hu5tfn7v FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4969 (class 2606 OID 208088)
-- Name: involvedpartyxresourceitemsecuritytoken fk72adgv7vc7uthg1eiq4sceq89; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyxresourceitemsecuritytoken
    ADD CONSTRAINT fk72adgv7vc7uthg1eiq4sceq89 FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4795 (class 2606 OID 208093)
-- Name: involvedpartyidentificationtypesecuritytoken fk7dkwueip8veb59dox327ia0a8; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyidentificationtypesecuritytoken
    ADD CONSTRAINT fk7dkwueip8veb59dox327ia0a8 FOREIGN KEY (involvedpartyidentificationtypeid, warehousecreateddate) REFERENCES party.involvedpartyidentificationtype (involvedpartyidentificationtypeid, warehousecreateddate);


--
-- TOC entry 4872 (class 2606 OID 208098)
-- Name: involvedpartyxclassification fk7jbx6dlrjcvu3ifb2s1w0ulct; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyxclassification
    ADD CONSTRAINT fk7jbx6dlrjcvu3ifb2s1w0ulct FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 4897 (class 2606 OID 208103)
-- Name: involvedpartyxinvolvedpartyidentificationtypesecuritytoken fk7ot9s9hc1w0icwm3ogcwhqvd3; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyxinvolvedpartyidentificationtypesecuritytoken
    ADD CONSTRAINT fk7ot9s9hc1w0icwm3ogcwhqvd3 FOREIGN KEY (involvedpartyxinvolvedpartyidentificationtypeid, warehousecreateddate) REFERENCES party.involvedpartyxinvolvedpartyidentificationtype (involvedpartyxinvolvedpartyidentificationtypeid, warehousecreateddate);


--
-- TOC entry 4804 (class 2606 OID 208108)
-- Name: involvedpartynametypesecuritytoken fk7t8d65pscktrxlw1we4idhlyj; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartynametypesecuritytoken
    ADD CONSTRAINT fk7t8d65pscktrxlw1we4idhlyj FOREIGN KEY (involvedpartynametypeid, warehousecreateddate) REFERENCES party.involvedpartynametype (involvedpartynametypeid, warehousecreateddate);


--
-- TOC entry 4909 (class 2606 OID 208113)
-- Name: involvedpartyxinvolvedpartynametypesecuritytoken fk7xl5vyatrbubvu8on89cnkdsj; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyxinvolvedpartynametypesecuritytoken
    ADD CONSTRAINT fk7xl5vyatrbubvu8on89cnkdsj FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 4983 (class 2606 OID 208118)
-- Name: involvedpartyxrulessecuritytoken fk86e07awdx5h97q548mtjxnlp1; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyxrulessecuritytoken
    ADD CONSTRAINT fk86e07awdx5h97q548mtjxnlp1 FOREIGN KEY (securitytokenid, warehousecreateddate) REFERENCES security.securitytoken (securitytokenid, warehousecreateddate);


--
-- TOC entry 4984 (class 2606 OID 208123)
-- Name: involvedpartyxrulessecuritytoken fk86h342si0g1hy4qpnkap8oq08; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyxrulessecuritytoken
    ADD CONSTRAINT fk86h342si0g1hy4qpnkap8oq08 FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4815 (class 2606 OID 208128)
-- Name: involvedpartynonorganicsecuritytoken fk8a7thuoro1mbxsng2amjluimk; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartynonorganicsecuritytoken
    ADD CONSTRAINT fk8a7thuoro1mbxsng2amjluimk FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 4955 (class 2606 OID 208133)
-- Name: involvedpartyxproducttypesecuritytoken fk8ftebinncegtl979d5gsqnkah; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyxproducttypesecuritytoken
    ADD CONSTRAINT fk8ftebinncegtl979d5gsqnkah FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4930 (class 2606 OID 208138)
-- Name: involvedpartyxinvolvedpartytypesecuritytoken fk8kb1lmh3wvovdurs0rq2jtotk; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyxinvolvedpartytypesecuritytoken
    ADD CONSTRAINT fk8kb1lmh3wvovdurs0rq2jtotk FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4976 (class 2606 OID 208143)
-- Name: involvedpartyxrules fk8yk3mt51je6xtf3as7f1n5koa; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyxrules
    ADD CONSTRAINT fk8yk3mt51je6xtf3as7f1n5koa FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 4866 (class 2606 OID 208148)
-- Name: involvedpartyxaddresssecuritytoken fk9h5dpjakcr9j4c61u628tte1b; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyxaddresssecuritytoken
    ADD CONSTRAINT fk9h5dpjakcr9j4c61u628tte1b FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 4890 (class 2606 OID 208153)
-- Name: involvedpartyxinvolvedpartyidentificationtype fk9k1s2vuya90it56mtuij9qk0d; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyxinvolvedpartyidentificationtype
    ADD CONSTRAINT fk9k1s2vuya90it56mtuij9qk0d FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 4949 (class 2606 OID 208158)
-- Name: involvedpartyxproducttype fk9qw1jha2e5bmcbfm5tv9vi4f0; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyxproducttype
    ADD CONSTRAINT fk9qw1jha2e5bmcbfm5tv9vi4f0 FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 4789 (class 2606 OID 208163)
-- Name: involvedpartyidentificationtype fk9t4s53f0ihecoo8ufdth1vbq2; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyidentificationtype
    ADD CONSTRAINT fk9t4s53f0ihecoo8ufdth1vbq2 FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4931 (class 2606 OID 208168)
-- Name: involvedpartyxinvolvedpartytypesecuritytoken fk9y6c21dewoi7fs477176gh0hv; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyxinvolvedpartytypesecuritytoken
    ADD CONSTRAINT fk9y6c21dewoi7fs477176gh0hv FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 4800 (class 2606 OID 208173)
-- Name: involvedpartynametype fka7cnvd016j105uffrbuh94xx5; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartynametype
    ADD CONSTRAINT fka7cnvd016j105uffrbuh94xx5 FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 4891 (class 2606 OID 208178)
-- Name: involvedpartyxinvolvedpartyidentificationtype fka8ayy9j0gddpl8adc2c6ug1y1; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyxinvolvedpartyidentificationtype
    ADD CONSTRAINT fka8ayy9j0gddpl8adc2c6ug1y1 FOREIGN KEY (classificationid, warehousecreateddate) REFERENCES classification.classification (classificationid, warehousecreateddate);


--
-- TOC entry 4801 (class 2606 OID 208183)
-- Name: involvedpartynametype fkaaa9iyxq2koyservqjmrfeqee; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartynametype
    ADD CONSTRAINT fkaaa9iyxq2koyservqjmrfeqee FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 4932 (class 2606 OID 208188)
-- Name: involvedpartyxinvolvedpartytypesecuritytoken fkamb8ax10bbah3k81o43u11kod; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyxinvolvedpartytypesecuritytoken
    ADD CONSTRAINT fkamb8ax10bbah3k81o43u11kod FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 4950 (class 2606 OID 208193)
-- Name: involvedpartyxproducttype fkanja7q0nr3e57ilnlo9mualbo; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyxproducttype
    ADD CONSTRAINT fkanja7q0nr3e57ilnlo9mualbo FOREIGN KEY (classificationid, warehousecreateddate) REFERENCES classification.classification (classificationid, warehousecreateddate);


--
-- TOC entry 4935 (class 2606 OID 208198)
-- Name: involvedpartyxproduct fkau2m5a079qpprk5hhhdqum1e2; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyxproduct
    ADD CONSTRAINT fkau2m5a079qpprk5hhhdqum1e2 FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 4878 (class 2606 OID 208203)
-- Name: involvedpartyxclassificationsecuritytoken fkbd75ir0ahamjur4981w6ki6tu; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyxclassificationsecuritytoken
    ADD CONSTRAINT fkbd75ir0ahamjur4981w6ki6tu FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4943 (class 2606 OID 208208)
-- Name: involvedpartyxproductsecuritytoken fkbfn2hrolgktevx0ena7ia8gr1; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyxproductsecuritytoken
    ADD CONSTRAINT fkbfn2hrolgktevx0ena7ia8gr1 FOREIGN KEY (involvedpartyxproductid, warehousecreateddate) REFERENCES party.involvedpartyxproduct (involvedpartyxproductid, warehousecreateddate);


--
-- TOC entry 4917 (class 2606 OID 208213)
-- Name: involvedpartyxinvolvedpartysecuritytoken fkbjx132wxvnoasynr6sdwopoiv; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyxinvolvedpartysecuritytoken
    ADD CONSTRAINT fkbjx132wxvnoasynr6sdwopoiv FOREIGN KEY (involvedpartyxinvolvedpartyid, warehousecreateddate) REFERENCES party.involvedpartyxinvolvedparty (involvedpartyxinvolvedpartyid, warehousecreateddate);


--
-- TOC entry 4816 (class 2606 OID 208218)
-- Name: involvedpartynonorganicsecuritytoken fkbo9u2l1rksyb0qw3poycrj3y2; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartynonorganicsecuritytoken
    ADD CONSTRAINT fkbo9u2l1rksyb0qw3poycrj3y2 FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4963 (class 2606 OID 208223)
-- Name: involvedpartyxresourceitem fkbtxxv9kh2p77i3qt1nhgi5epp; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyxresourceitem
    ADD CONSTRAINT fkbtxxv9kh2p77i3qt1nhgi5epp FOREIGN KEY (classificationid, warehousecreateddate) REFERENCES classification.classification (classificationid, warehousecreateddate);


--
-- TOC entry 4879 (class 2606 OID 208228)
-- Name: involvedpartyxclassificationsecuritytoken fkbu7cxnreyofsgf9cdr0s3i9a1; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyxclassificationsecuritytoken
    ADD CONSTRAINT fkbu7cxnreyofsgf9cdr0s3i9a1 FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 4970 (class 2606 OID 208233)
-- Name: involvedpartyxresourceitemsecuritytoken fkbv1ja6o5cpxl4jpwm7bneb4ys; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyxresourceitemsecuritytoken
    ADD CONSTRAINT fkbv1ja6o5cpxl4jpwm7bneb4ys FOREIGN KEY (involvedpartyxresourceitemid, warehousecreateddate) REFERENCES party.involvedpartyxresourceitem (involvedpartyxresourceitemid, warehousecreateddate);


--
-- TOC entry 4933 (class 2606 OID 208238)
-- Name: involvedpartyxinvolvedpartytypesecuritytoken fkc10kevx7v3jol7lbh6vw0smi4; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyxinvolvedpartytypesecuritytoken
    ADD CONSTRAINT fkc10kevx7v3jol7lbh6vw0smi4 FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4910 (class 2606 OID 208243)
-- Name: involvedpartyxinvolvedpartynametypesecuritytoken fkc7m7jk9bba59unhsafmgxbu4f; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyxinvolvedpartynametypesecuritytoken
    ADD CONSTRAINT fkc7m7jk9bba59unhsafmgxbu4f FOREIGN KEY (securitytokenid, warehousecreateddate) REFERENCES security.securitytoken (securitytokenid, warehousecreateddate);


--
-- TOC entry 4853 (class 2606 OID 208248)
-- Name: involvedpartytypesecuritytoken fkc8cf751cac8asi0iu3rkexxt0; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartytypesecuritytoken
    ADD CONSTRAINT fkc8cf751cac8asi0iu3rkexxt0 FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4936 (class 2606 OID 208253)
-- Name: involvedpartyxproduct fkchhwxcvsrfev3k76luwrc1di3; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyxproduct
    ADD CONSTRAINT fkchhwxcvsrfev3k76luwrc1di3 FOREIGN KEY (productid, warehousecreateddate) REFERENCES product.product (productid, warehousecreateddate);


--
-- TOC entry 4805 (class 2606 OID 208258)
-- Name: involvedpartynametypesecuritytoken fkclphu45dfvj92ca97kfmhd0il; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartynametypesecuritytoken
    ADD CONSTRAINT fkclphu45dfvj92ca97kfmhd0il FOREIGN KEY (securitytokenid, warehousecreateddate) REFERENCES security.securitytoken (securitytokenid, warehousecreateddate);


--
-- TOC entry 4810 (class 2606 OID 208263)
-- Name: involvedpartynonorganic fkcphls8bnbiwk5rkf278le5kad; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartynonorganic
    ADD CONSTRAINT fkcphls8bnbiwk5rkf278le5kad FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4786 (class 2606 OID 208268)
-- Name: involvedparty fkd9g7suhvo150i79rthg96ktwu; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedparty
    ADD CONSTRAINT fkd9g7suhvo150i79rthg96ktwu FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 4873 (class 2606 OID 208273)
-- Name: involvedpartyxclassification fkdfqy11c8yvskjx2yu9wa2ay8r; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyxclassification
    ADD CONSTRAINT fkdfqy11c8yvskjx2yu9wa2ay8r FOREIGN KEY (classificationid, warehousecreateddate) REFERENCES classification.classification (classificationid, warehousecreateddate);


--
-- TOC entry 4874 (class 2606 OID 208278)
-- Name: involvedpartyxclassification fkdinundrqqh853d336evxy3bcf; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyxclassification
    ADD CONSTRAINT fkdinundrqqh853d336evxy3bcf FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4806 (class 2606 OID 208283)
-- Name: involvedpartynametypesecuritytoken fkdkkd4w22d3p250hyn7vwkrp6a; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartynametypesecuritytoken
    ADD CONSTRAINT fkdkkd4w22d3p250hyn7vwkrp6a FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4854 (class 2606 OID 208288)
-- Name: involvedpartytypesecuritytoken fkdtx295xce0hprr5jy1la820eh; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartytypesecuritytoken
    ADD CONSTRAINT fkdtx295xce0hprr5jy1la820eh FOREIGN KEY (involvedpartytypeid, warehousecreateddate) REFERENCES party.involvedpartytype (involvedpartytypeid, warehousecreateddate);


--
-- TOC entry 4796 (class 2606 OID 208293)
-- Name: involvedpartyidentificationtypesecuritytoken fkdvt59k7lx9fobcggw3naabwkc; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyidentificationtypesecuritytoken
    ADD CONSTRAINT fkdvt59k7lx9fobcggw3naabwkc FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4860 (class 2606 OID 208298)
-- Name: involvedpartyxaddress fkdwkp9qbashp1p04uip3rg2hls; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyxaddress
    ADD CONSTRAINT fkdwkp9qbashp1p04uip3rg2hls FOREIGN KEY (classificationid, warehousecreateddate) REFERENCES classification.classification (classificationid, warehousecreateddate);


--
-- TOC entry 4833 (class 2606 OID 208303)
-- Name: involvedpartyorganictype fke1juu2g32hsp6fwtfw7f9d3jx; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyorganictype
    ADD CONSTRAINT fke1juu2g32hsp6fwtfw7f9d3jx FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4911 (class 2606 OID 208308)
-- Name: involvedpartyxinvolvedpartynametypesecuritytoken fkeafj6qgxpt157ayq3csmbo5sj; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyxinvolvedpartynametypesecuritytoken
    ADD CONSTRAINT fkeafj6qgxpt157ayq3csmbo5sj FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4956 (class 2606 OID 208313)
-- Name: involvedpartyxproducttypesecuritytoken fkeaiel3pwjpomb5v1gyvnswnhi; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyxproducttypesecuritytoken
    ADD CONSTRAINT fkeaiel3pwjpomb5v1gyvnswnhi FOREIGN KEY (involvedpartyxproducttypeid, warehousecreateddate) REFERENCES party.involvedpartyxproducttype (involvedpartyxproducttypeid, warehousecreateddate);


--
-- TOC entry 4957 (class 2606 OID 208318)
-- Name: involvedpartyxproducttypesecuritytoken fkebb1xe5bii4fw0alp6uk7yx3g; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyxproducttypesecuritytoken
    ADD CONSTRAINT fkebb1xe5bii4fw0alp6uk7yx3g FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4918 (class 2606 OID 208323)
-- Name: involvedpartyxinvolvedpartysecuritytoken fkec644efp6gtbnydr4qmfmytrn; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyxinvolvedpartysecuritytoken
    ADD CONSTRAINT fkec644efp6gtbnydr4qmfmytrn FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 4977 (class 2606 OID 208328)
-- Name: involvedpartyxrules fkeiak0rrb2rgx9gest89clwauh; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyxrules
    ADD CONSTRAINT fkeiak0rrb2rgx9gest89clwauh FOREIGN KEY (involvedpartyxrulesid, warehousecreateddate) REFERENCES party.involvedpartyxrules (involvedpartyxrulesid, warehousecreateddate);


--
-- TOC entry 4836 (class 2606 OID 208333)
-- Name: involvedpartyorganictypesecuritytoken fkejueyv4755b9ec8xoyb7wpu4x; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyorganictypesecuritytoken
    ADD CONSTRAINT fkejueyv4755b9ec8xoyb7wpu4x FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4861 (class 2606 OID 208338)
-- Name: involvedpartyxaddress fkejxbg01t325qgchlwnwmpl7a0; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyxaddress
    ADD CONSTRAINT fkejxbg01t325qgchlwnwmpl7a0 FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 4923 (class 2606 OID 208343)
-- Name: involvedpartyxinvolvedpartytype fkelc5v745ispspjwh2cwse92bh; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyxinvolvedpartytype
    ADD CONSTRAINT fkelc5v745ispspjwh2cwse92bh FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4985 (class 2606 OID 208348)
-- Name: involvedpartyxrulessecuritytoken fker91inm2bcxuigrl4g4051u4o; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyxrulessecuritytoken
    ADD CONSTRAINT fker91inm2bcxuigrl4g4051u4o FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 4790 (class 2606 OID 208353)
-- Name: involvedpartyidentificationtype fkeuxx0k0eufg56jpbymmg0wnju; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyidentificationtype
    ADD CONSTRAINT fkeuxx0k0eufg56jpbymmg0wnju FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 4826 (class 2606 OID 208358)
-- Name: involvedpartyorganicsecuritytoken fkeyo56crc5eg0qtbt3o79rblnp; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyorganicsecuritytoken
    ADD CONSTRAINT fkeyo56crc5eg0qtbt3o79rblnp FOREIGN KEY (securitytokenid, warehousecreateddate) REFERENCES security.securitytoken (securitytokenid, warehousecreateddate);


--
-- TOC entry 4862 (class 2606 OID 208363)
-- Name: involvedpartyxaddress fkf0jjy7kksugbaamt8638ktw8a; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyxaddress
    ADD CONSTRAINT fkf0jjy7kksugbaamt8638ktw8a FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4964 (class 2606 OID 208368)
-- Name: involvedpartyxresourceitem fkf1eri71wcx822turop42m1raw; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyxresourceitem
    ADD CONSTRAINT fkf1eri71wcx822turop42m1raw FOREIGN KEY (resourceitemid, warehousecreateddate) REFERENCES resource.resourceitem (resourceitemid, warehousecreateddate);


--
-- TOC entry 4821 (class 2606 OID 208373)
-- Name: involvedpartyorganic fkf7p05lygebyxjhhtrkbn2n2md; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyorganic
    ADD CONSTRAINT fkf7p05lygebyxjhhtrkbn2n2md FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4951 (class 2606 OID 208378)
-- Name: involvedpartyxproducttype fkfwrjy39tsf1ctjt6rx6siphyo; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyxproducttype
    ADD CONSTRAINT fkfwrjy39tsf1ctjt6rx6siphyo FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 4827 (class 2606 OID 208383)
-- Name: involvedpartyorganicsecuritytoken fkg9iyrrj29vcyc3b6sxsgy95on; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyorganicsecuritytoken
    ADD CONSTRAINT fkg9iyrrj29vcyc3b6sxsgy95on FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 4944 (class 2606 OID 208388)
-- Name: involvedpartyxproductsecuritytoken fkghbsln22v6fh0b1jdcblved0n; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyxproductsecuritytoken
    ADD CONSTRAINT fkghbsln22v6fh0b1jdcblved0n FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4811 (class 2606 OID 208393)
-- Name: involvedpartynonorganic fkgjctovxjg820869d686h867uv; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartynonorganic
    ADD CONSTRAINT fkgjctovxjg820869d686h867uv FOREIGN KEY (involvedpartynonorganicid, warehousecreateddate) REFERENCES party.involvedparty (involvedpartyid, warehousecreateddate);


--
-- TOC entry 4842 (class 2606 OID 208398)
-- Name: involvedpartysecuritytoken fkgtrgr11h04p33g04pukt5x1sy; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartysecuritytoken
    ADD CONSTRAINT fkgtrgr11h04p33g04pukt5x1sy FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4919 (class 2606 OID 208403)
-- Name: involvedpartyxinvolvedpartysecuritytoken fkgx4f05xe52w7yxl6f4mgycsyu; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyxinvolvedpartysecuritytoken
    ADD CONSTRAINT fkgx4f05xe52w7yxl6f4mgycsyu FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 4807 (class 2606 OID 208408)
-- Name: involvedpartynametypesecuritytoken fkh3e0lgk341y3qrbdp9y7eo6uf; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartynametypesecuritytoken
    ADD CONSTRAINT fkh3e0lgk341y3qrbdp9y7eo6uf FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 4849 (class 2606 OID 208413)
-- Name: involvedpartytype fkh8ct5p1uw4f7s19svr7525boe; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartytype
    ADD CONSTRAINT fkh8ct5p1uw4f7s19svr7525boe FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 4978 (class 2606 OID 208418)
-- Name: involvedpartyxrules fkhd6xjhmmugixhj5pl6fdocdwy; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyxrules
    ADD CONSTRAINT fkhd6xjhmmugixhj5pl6fdocdwy FOREIGN KEY (rulesid, warehousecreateddate) REFERENCES rules.rules (rulesid, warehousecreateddate);


--
-- TOC entry 4965 (class 2606 OID 208423)
-- Name: involvedpartyxresourceitem fkhkmp4ty2oydnlut2dtgkv8se8; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyxresourceitem
    ADD CONSTRAINT fkhkmp4ty2oydnlut2dtgkv8se8 FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 4863 (class 2606 OID 208428)
-- Name: involvedpartyxaddress fki1pmmm070ua8yd0xrk19qq206; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyxaddress
    ADD CONSTRAINT fki1pmmm070ua8yd0xrk19qq206 FOREIGN KEY (involvedpartyid, warehousecreateddate) REFERENCES party.involvedparty (involvedpartyid, warehousecreateddate);


--
-- TOC entry 4843 (class 2606 OID 208433)
-- Name: involvedpartysecuritytoken fkialc3kro04ovq8q26gg2hce1s; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartysecuritytoken
    ADD CONSTRAINT fkialc3kro04ovq8q26gg2hce1s FOREIGN KEY (involvedpartyid, warehousecreateddate) REFERENCES party.involvedparty (involvedpartyid, warehousecreateddate);


--
-- TOC entry 4903 (class 2606 OID 208438)
-- Name: involvedpartyxinvolvedpartynametype fkiatc66e8wafha0n8xgeslpypc; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyxinvolvedpartynametype
    ADD CONSTRAINT fkiatc66e8wafha0n8xgeslpypc FOREIGN KEY (involvedpartyid, warehousecreateddate) REFERENCES party.involvedparty (involvedpartyid, warehousecreateddate);


--
-- TOC entry 4867 (class 2606 OID 208443)
-- Name: involvedpartyxaddresssecuritytoken fkietr95ln1k6gcutfwaqg26k24; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyxaddresssecuritytoken
    ADD CONSTRAINT fkietr95ln1k6gcutfwaqg26k24 FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4817 (class 2606 OID 208448)
-- Name: involvedpartynonorganicsecuritytoken fkii44sojyhs3kd1d607mnqldov; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartynonorganicsecuritytoken
    ADD CONSTRAINT fkii44sojyhs3kd1d607mnqldov FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4885 (class 2606 OID 208453)
-- Name: involvedpartyxinvolvedparty fkisqmvjyuuk8aqaogyeipmxtmi; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyxinvolvedparty
    ADD CONSTRAINT fkisqmvjyuuk8aqaogyeipmxtmi FOREIGN KEY (classificationid, warehousecreateddate) REFERENCES classification.classification (classificationid, warehousecreateddate);


--
-- TOC entry 4924 (class 2606 OID 208458)
-- Name: involvedpartyxinvolvedpartytype fkj0mx9sjb7tx19f8y6g6aha7ae; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyxinvolvedpartytype
    ADD CONSTRAINT fkj0mx9sjb7tx19f8y6g6aha7ae FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 4855 (class 2606 OID 208463)
-- Name: involvedpartytypesecuritytoken fkjhy9dh04vj80o53adnsk1xi9t; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartytypesecuritytoken
    ADD CONSTRAINT fkjhy9dh04vj80o53adnsk1xi9t FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4958 (class 2606 OID 208468)
-- Name: involvedpartyxproducttypesecuritytoken fkjkwmptgw4rcxlvjycdbevrdbh; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyxproducttypesecuritytoken
    ADD CONSTRAINT fkjkwmptgw4rcxlvjycdbevrdbh FOREIGN KEY (securitytokenid, warehousecreateddate) REFERENCES security.securitytoken (securitytokenid, warehousecreateddate);


--
-- TOC entry 4966 (class 2606 OID 208473)
-- Name: involvedpartyxresourceitem fkjo97cd2ria6n18uyt9y9n1k40; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyxresourceitem
    ADD CONSTRAINT fkjo97cd2ria6n18uyt9y9n1k40 FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4952 (class 2606 OID 208478)
-- Name: involvedpartyxproducttype fkjoxnotjs00eoek4eboy9ob7x3; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyxproducttype
    ADD CONSTRAINT fkjoxnotjs00eoek4eboy9ob7x3 FOREIGN KEY (involvedpartyid, warehousecreateddate) REFERENCES party.involvedparty (involvedpartyid, warehousecreateddate);


--
-- TOC entry 4953 (class 2606 OID 208483)
-- Name: involvedpartyxproducttype fkk7gt22kjj7aajqm1i61s53dto; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyxproducttype
    ADD CONSTRAINT fkk7gt22kjj7aajqm1i61s53dto FOREIGN KEY (producttypeid, warehousecreateddate) REFERENCES product.producttype (producttypeid, warehousecreateddate);


--
-- TOC entry 4892 (class 2606 OID 208488)
-- Name: involvedpartyxinvolvedpartyidentificationtype fkkb48h7qs2y9d208ho8l8lu5my; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyxinvolvedpartyidentificationtype
    ADD CONSTRAINT fkkb48h7qs2y9d208ho8l8lu5my FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4791 (class 2606 OID 208493)
-- Name: involvedpartyidentificationtype fkkeqqy519age78syf29vttmwu9; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyidentificationtype
    ADD CONSTRAINT fkkeqqy519age78syf29vttmwu9 FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 4837 (class 2606 OID 208498)
-- Name: involvedpartyorganictypesecuritytoken fkkj7fqfd2urqo5hgy3vxagu45t; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyorganictypesecuritytoken
    ADD CONSTRAINT fkkj7fqfd2urqo5hgy3vxagu45t FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 4818 (class 2606 OID 208503)
-- Name: involvedpartynonorganicsecuritytoken fkkkks9t7wiiiku7knnyo6vvc3r; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartynonorganicsecuritytoken
    ADD CONSTRAINT fkkkks9t7wiiiku7knnyo6vvc3r FOREIGN KEY (securitytokenid, warehousecreateddate) REFERENCES security.securitytoken (securitytokenid, warehousecreateddate);


--
-- TOC entry 4925 (class 2606 OID 208508)
-- Name: involvedpartyxinvolvedpartytype fkkm9ehabqxotucg1e13vhmcgge; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyxinvolvedpartytype
    ADD CONSTRAINT fkkm9ehabqxotucg1e13vhmcgge FOREIGN KEY (involvedpartytypeid, warehousecreateddate) REFERENCES party.involvedpartytype (involvedpartytypeid, warehousecreateddate);


--
-- TOC entry 4868 (class 2606 OID 208513)
-- Name: involvedpartyxaddresssecuritytoken fkkwbmkde209srp4k0q0s7cd1u3; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyxaddresssecuritytoken
    ADD CONSTRAINT fkkwbmkde209srp4k0q0s7cd1u3 FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 4904 (class 2606 OID 208518)
-- Name: involvedpartyxinvolvedpartynametype fkkxftjgdli2l9ro94w41wclkfx; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyxinvolvedpartynametype
    ADD CONSTRAINT fkkxftjgdli2l9ro94w41wclkfx FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4802 (class 2606 OID 208523)
-- Name: involvedpartynametype fkl18vvxbpd5f6oq57wy3jtnbp8; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartynametype
    ADD CONSTRAINT fkl18vvxbpd5f6oq57wy3jtnbp8 FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4912 (class 2606 OID 208528)
-- Name: involvedpartyxinvolvedpartynametypesecuritytoken fkl8eohlltm8o9u06rc0tqtf5io; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyxinvolvedpartynametypesecuritytoken
    ADD CONSTRAINT fkl8eohlltm8o9u06rc0tqtf5io FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 4812 (class 2606 OID 208533)
-- Name: involvedpartynonorganic fklb5mxasbqff0f09q3tsqkgsi1; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartynonorganic
    ADD CONSTRAINT fklb5mxasbqff0f09q3tsqkgsi1 FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4869 (class 2606 OID 208538)
-- Name: involvedpartyxaddresssecuritytoken fklcmcbwu95pa7hnchv2tw2l6e4; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyxaddresssecuritytoken
    ADD CONSTRAINT fklcmcbwu95pa7hnchv2tw2l6e4 FOREIGN KEY (securitytokenid, warehousecreateddate) REFERENCES security.securitytoken (securitytokenid, warehousecreateddate);


--
-- TOC entry 4886 (class 2606 OID 208543)
-- Name: involvedpartyxinvolvedparty fklrica71csov4p3987b56q8vxg; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyxinvolvedparty
    ADD CONSTRAINT fklrica71csov4p3987b56q8vxg FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 4838 (class 2606 OID 208548)
-- Name: involvedpartyorganictypesecuritytoken fkluot9f3p0qcl04j9vytxoy5w2; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyorganictypesecuritytoken
    ADD CONSTRAINT fkluot9f3p0qcl04j9vytxoy5w2 FOREIGN KEY (involvedpartyorganictypeid, warehousecreateddate) REFERENCES party.involvedpartyorganictype (involvedpartyorganictypeid, warehousecreateddate);


--
-- TOC entry 4945 (class 2606 OID 208553)
-- Name: involvedpartyxproductsecuritytoken fklvpxfkxf414rr3h2hd7qfbt6x; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyxproductsecuritytoken
    ADD CONSTRAINT fklvpxfkxf414rr3h2hd7qfbt6x FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 4792 (class 2606 OID 208558)
-- Name: involvedpartyidentificationtype fklxima3r9ohrv77gnodycrb2su; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyidentificationtype
    ADD CONSTRAINT fklxima3r9ohrv77gnodycrb2su FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4986 (class 2606 OID 208563)
-- Name: involvedpartyxrulessecuritytoken fkm4u2pspis8jhbsy7bpti76wsd; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyxrulessecuritytoken
    ADD CONSTRAINT fkm4u2pspis8jhbsy7bpti76wsd FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4937 (class 2606 OID 208568)
-- Name: involvedpartyxproduct fkm5l1oipl6mvv54tc6xx24yk06; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyxproduct
    ADD CONSTRAINT fkm5l1oipl6mvv54tc6xx24yk06 FOREIGN KEY (classificationid, warehousecreateddate) REFERENCES classification.classification (classificationid, warehousecreateddate);


--
-- TOC entry 4971 (class 2606 OID 208573)
-- Name: involvedpartyxresourceitemsecuritytoken fkm62y4d4wr0udqi7iexcc3gaa5; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyxresourceitemsecuritytoken
    ADD CONSTRAINT fkm62y4d4wr0udqi7iexcc3gaa5 FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4822 (class 2606 OID 208578)
-- Name: involvedpartyorganic fkmkbm0yyns5o1ll48bo5yce5sc; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyorganic
    ADD CONSTRAINT fkmkbm0yyns5o1ll48bo5yce5sc FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 4913 (class 2606 OID 208583)
-- Name: involvedpartyxinvolvedpartynametypesecuritytoken fkmkjv3992oc9plpbavw8qwtqxx; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyxinvolvedpartynametypesecuritytoken
    ADD CONSTRAINT fkmkjv3992oc9plpbavw8qwtqxx FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4880 (class 2606 OID 208588)
-- Name: involvedpartyxclassificationsecuritytoken fkmsyb25jnjb9yam4x4779256w2; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyxclassificationsecuritytoken
    ADD CONSTRAINT fkmsyb25jnjb9yam4x4779256w2 FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 4898 (class 2606 OID 208593)
-- Name: involvedpartyxinvolvedpartyidentificationtypesecuritytoken fkmt218lxijt1k3knyl48ebot0f; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyxinvolvedpartyidentificationtypesecuritytoken
    ADD CONSTRAINT fkmt218lxijt1k3knyl48ebot0f FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4828 (class 2606 OID 208598)
-- Name: involvedpartyorganicsecuritytoken fkmyjwts72ixlq4gmviiba55a6e; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyorganicsecuritytoken
    ADD CONSTRAINT fkmyjwts72ixlq4gmviiba55a6e FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 4905 (class 2606 OID 208603)
-- Name: involvedpartyxinvolvedpartynametype fkn2foayal7pve60wm8tps4eq67; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyxinvolvedpartynametype
    ADD CONSTRAINT fkn2foayal7pve60wm8tps4eq67 FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4926 (class 2606 OID 208608)
-- Name: involvedpartyxinvolvedpartytype fkn68mf1pa4afxo1tx2q83kksx4; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyxinvolvedpartytype
    ADD CONSTRAINT fkn68mf1pa4afxo1tx2q83kksx4 FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 4946 (class 2606 OID 208613)
-- Name: involvedpartyxproductsecuritytoken fkn7l4i9kxjbtg61rjqwin84k52; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyxproductsecuritytoken
    ADD CONSTRAINT fkn7l4i9kxjbtg61rjqwin84k52 FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4893 (class 2606 OID 208618)
-- Name: involvedpartyxinvolvedpartyidentificationtype fkn7qd136m19by1jp2xgntox5nr; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyxinvolvedpartyidentificationtype
    ADD CONSTRAINT fkn7qd136m19by1jp2xgntox5nr FOREIGN KEY (involvedpartyid, warehousecreateddate) REFERENCES party.involvedparty (involvedpartyid, warehousecreateddate);


--
-- TOC entry 4844 (class 2606 OID 208623)
-- Name: involvedpartysecuritytoken fknkgdmsl2don2yegx2qfgnykqw; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartysecuritytoken
    ADD CONSTRAINT fknkgdmsl2don2yegx2qfgnykqw FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 4914 (class 2606 OID 208628)
-- Name: involvedpartyxinvolvedpartynametypesecuritytoken fknl7qjhkyalhn6skoouq0jt8pq; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyxinvolvedpartynametypesecuritytoken
    ADD CONSTRAINT fknl7qjhkyalhn6skoouq0jt8pq FOREIGN KEY (involvedpartyxinvolvedpartynametypeid, warehousecreateddate) REFERENCES party.involvedpartyxinvolvedpartynametype (involvedpartyxinvolvedpartynametypeid, warehousecreateddate);


--
-- TOC entry 4823 (class 2606 OID 208633)
-- Name: involvedpartyorganic fknyksrtkiekj3cd65vtj6d641e; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyorganic
    ADD CONSTRAINT fknyksrtkiekj3cd65vtj6d641e FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 4845 (class 2606 OID 208638)
-- Name: involvedpartysecuritytoken fko24pnibqq5rx8t4soy88rynmd; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartysecuritytoken
    ADD CONSTRAINT fko24pnibqq5rx8t4soy88rynmd FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 4938 (class 2606 OID 208643)
-- Name: involvedpartyxproduct fko98uqwvsmtmebl7e64anytc63; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyxproduct
    ADD CONSTRAINT fko98uqwvsmtmebl7e64anytc63 FOREIGN KEY (involvedpartyid, warehousecreateddate) REFERENCES party.involvedparty (involvedpartyid, warehousecreateddate);


--
-- TOC entry 4887 (class 2606 OID 208648)
-- Name: involvedpartyxinvolvedparty fkocrvxgjfr7ogilbsg5ija9raf; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyxinvolvedparty
    ADD CONSTRAINT fkocrvxgjfr7ogilbsg5ija9raf FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4894 (class 2606 OID 208653)
-- Name: involvedpartyxinvolvedpartyidentificationtype fkoj896cehdnvqta4i4psp2g5c; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyxinvolvedpartyidentificationtype
    ADD CONSTRAINT fkoj896cehdnvqta4i4psp2g5c FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4819 (class 2606 OID 208658)
-- Name: involvedpartynonorganicsecuritytoken fkp0jkfk12oi63j8blb7dvepu5i; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartynonorganicsecuritytoken
    ADD CONSTRAINT fkp0jkfk12oi63j8blb7dvepu5i FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 4920 (class 2606 OID 208663)
-- Name: involvedpartyxinvolvedpartysecuritytoken fkp3f6axkd1puang58hu44ll5dh; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyxinvolvedpartysecuritytoken
    ADD CONSTRAINT fkp3f6axkd1puang58hu44ll5dh FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4939 (class 2606 OID 208668)
-- Name: involvedpartyxproduct fkp5ixt88una33viktp0w23kwj6; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyxproduct
    ADD CONSTRAINT fkp5ixt88una33viktp0w23kwj6 FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 4972 (class 2606 OID 208673)
-- Name: involvedpartyxresourceitemsecuritytoken fkp8fhw7rn2g46bjsnhvss90xt0; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyxresourceitemsecuritytoken
    ADD CONSTRAINT fkp8fhw7rn2g46bjsnhvss90xt0 FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 4927 (class 2606 OID 208678)
-- Name: involvedpartyxinvolvedpartytype fkpfkqk9ya2eb37pvaq9y6pwesj; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyxinvolvedpartytype
    ADD CONSTRAINT fkpfkqk9ya2eb37pvaq9y6pwesj FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4906 (class 2606 OID 208683)
-- Name: involvedpartyxinvolvedpartynametype fkpi2yp7u241j1k9an8b0mttcib; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyxinvolvedpartynametype
    ADD CONSTRAINT fkpi2yp7u241j1k9an8b0mttcib FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 4899 (class 2606 OID 208688)
-- Name: involvedpartyxinvolvedpartyidentificationtypesecuritytoken fkpk4kkstfg7ebwsy3y9pmt6sia; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyxinvolvedpartyidentificationtypesecuritytoken
    ADD CONSTRAINT fkpk4kkstfg7ebwsy3y9pmt6sia FOREIGN KEY (securitytokenid, warehousecreateddate) REFERENCES security.securitytoken (securitytokenid, warehousecreateddate);


--
-- TOC entry 4787 (class 2606 OID 208693)
-- Name: involvedparty fkpni39h3ejdgolqlvxwoltisl2; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedparty
    ADD CONSTRAINT fkpni39h3ejdgolqlvxwoltisl2 FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4846 (class 2606 OID 208698)
-- Name: involvedpartysecuritytoken fkpogqfy578mwtlm6p81g0btv3n; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartysecuritytoken
    ADD CONSTRAINT fkpogqfy578mwtlm6p81g0btv3n FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4834 (class 2606 OID 208703)
-- Name: involvedpartyorganictype fkq2twef17pvkati3vquevv6w8c; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyorganictype
    ADD CONSTRAINT fkq2twef17pvkati3vquevv6w8c FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 4888 (class 2606 OID 208708)
-- Name: involvedpartyxinvolvedparty fkq53n0bblmmvb6j4n4ub3r9tea; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyxinvolvedparty
    ADD CONSTRAINT fkq53n0bblmmvb6j4n4ub3r9tea FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 4788 (class 2606 OID 208713)
-- Name: involvedparty fkq6epu7tek9j6nfmtx80i7jv55; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedparty
    ADD CONSTRAINT fkq6epu7tek9j6nfmtx80i7jv55 FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 4875 (class 2606 OID 208718)
-- Name: involvedpartyxclassification fkq80p3cmg58mvp6pb85vee4twn; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyxclassification
    ADD CONSTRAINT fkq80p3cmg58mvp6pb85vee4twn FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4979 (class 2606 OID 208723)
-- Name: involvedpartyxrules fkqcygcbsndr9g4owqwp22nkrdg; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyxrules
    ADD CONSTRAINT fkqcygcbsndr9g4owqwp22nkrdg FOREIGN KEY (involvedpartyid, warehousecreateddate) REFERENCES party.involvedparty (involvedpartyid, warehousecreateddate);


--
-- TOC entry 4987 (class 2606 OID 208728)
-- Name: involvedpartyxrulessecuritytoken fkqd2pskmew84uw4kpp9tscnftg; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyxrulessecuritytoken
    ADD CONSTRAINT fkqd2pskmew84uw4kpp9tscnftg FOREIGN KEY (involvedpartyxrulesid, warehousecreateddate) REFERENCES party.involvedpartyxrules (involvedpartyxrulesid, warehousecreateddate);


--
-- TOC entry 4967 (class 2606 OID 208733)
-- Name: involvedpartyxresourceitem fkqq23awk6k3o0swj127dcgd0st; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyxresourceitem
    ADD CONSTRAINT fkqq23awk6k3o0swj127dcgd0st FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4959 (class 2606 OID 208738)
-- Name: involvedpartyxproducttypesecuritytoken fkqt1rigtuob9p89owrlanmtlt2; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyxproducttypesecuritytoken
    ADD CONSTRAINT fkqt1rigtuob9p89owrlanmtlt2 FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 4850 (class 2606 OID 208743)
-- Name: involvedpartytype fkqwx9fppibmcrytwu4t30w0f7e; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartytype
    ADD CONSTRAINT fkqwx9fppibmcrytwu4t30w0f7e FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4900 (class 2606 OID 208748)
-- Name: involvedpartyxinvolvedpartyidentificationtypesecuritytoken fkqx276013uinthqhdmr1lqpucn; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyxinvolvedpartyidentificationtypesecuritytoken
    ADD CONSTRAINT fkqx276013uinthqhdmr1lqpucn FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 4839 (class 2606 OID 208753)
-- Name: involvedpartyorganictypesecuritytoken fkr6a971hibsnykjeadhdgnmwm6; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyorganictypesecuritytoken
    ADD CONSTRAINT fkr6a971hibsnykjeadhdgnmwm6 FOREIGN KEY (securitytokenid, warehousecreateddate) REFERENCES security.securitytoken (securitytokenid, warehousecreateddate);


--
-- TOC entry 4808 (class 2606 OID 208758)
-- Name: involvedpartynametypesecuritytoken fkr9iq4s6tpt0gt97prluo7sia; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartynametypesecuritytoken
    ADD CONSTRAINT fkr9iq4s6tpt0gt97prluo7sia FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4840 (class 2606 OID 208763)
-- Name: involvedpartyorganictypesecuritytoken fkrbdy36uy6fi319o482uvgbic1; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyorganictypesecuritytoken
    ADD CONSTRAINT fkrbdy36uy6fi319o482uvgbic1 FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 4954 (class 2606 OID 208768)
-- Name: involvedpartyxproducttype fkrdm87u0ipc3rwnmd9jk22bwrk; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyxproducttype
    ADD CONSTRAINT fkrdm87u0ipc3rwnmd9jk22bwrk FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4829 (class 2606 OID 208773)
-- Name: involvedpartyorganicsecuritytoken fks4ev0kg4ufyi8iw19xqwn2fxb; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyorganicsecuritytoken
    ADD CONSTRAINT fks4ev0kg4ufyi8iw19xqwn2fxb FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4940 (class 2606 OID 208778)
-- Name: involvedpartyxproduct fks9ps3fhh31svi0qpmwv3utwww; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyxproduct
    ADD CONSTRAINT fks9ps3fhh31svi0qpmwv3utwww FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4824 (class 2606 OID 208783)
-- Name: involvedpartyorganic fks9ty7u1vtfr0m9cu70ecqfmio; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyorganic
    ADD CONSTRAINT fks9ty7u1vtfr0m9cu70ecqfmio FOREIGN KEY (involvedpartyorganicid, warehousecreateddate) REFERENCES party.involvedparty (involvedpartyid, warehousecreateddate);


--
-- TOC entry 4980 (class 2606 OID 208788)
-- Name: involvedpartyxrules fkscjxiiwoomhsis6829bfcljai; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyxrules
    ADD CONSTRAINT fkscjxiiwoomhsis6829bfcljai FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4960 (class 2606 OID 208793)
-- Name: involvedpartyxproducttypesecuritytoken fksidcxt1uldnhq44dumo7lesno; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyxproducttypesecuritytoken
    ADD CONSTRAINT fksidcxt1uldnhq44dumo7lesno FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 4881 (class 2606 OID 208798)
-- Name: involvedpartyxclassificationsecuritytoken fksmurlugdvahy37fyxobdauy42; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyxclassificationsecuritytoken
    ADD CONSTRAINT fksmurlugdvahy37fyxobdauy42 FOREIGN KEY (involvedpartyxclassificationid, warehousecreateddate) REFERENCES party.involvedpartyxclassification (involvedpartyxclassificationid, warehousecreateddate);


--
-- TOC entry 4856 (class 2606 OID 208803)
-- Name: involvedpartytypesecuritytoken fksoyq6n0n6er6eyhn229l4oxu6; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartytypesecuritytoken
    ADD CONSTRAINT fksoyq6n0n6er6eyhn229l4oxu6 FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 4797 (class 2606 OID 208808)
-- Name: involvedpartyidentificationtypesecuritytoken fkspj0etctwf7i8sj0gu0d6tvs8; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyidentificationtypesecuritytoken
    ADD CONSTRAINT fkspj0etctwf7i8sj0gu0d6tvs8 FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4941 (class 2606 OID 208813)
-- Name: involvedpartyxproduct fkstmxsovkkg83xipa6jb1v9tqv; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyxproduct
    ADD CONSTRAINT fkstmxsovkkg83xipa6jb1v9tqv FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4830 (class 2606 OID 208818)
-- Name: involvedpartyorganicsecuritytoken fkt03i6kwyod9krg1cpoki9kc5x; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyorganicsecuritytoken
    ADD CONSTRAINT fkt03i6kwyod9krg1cpoki9kc5x FOREIGN KEY (involvedpartyorganicid, warehousecreateddate) REFERENCES party.involvedpartyorganic (involvedpartyorganicid, warehousecreateddate);


--
-- TOC entry 4813 (class 2606 OID 208823)
-- Name: involvedpartynonorganic fkt3m2esc7actxvqhmovs8wly1p; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartynonorganic
    ADD CONSTRAINT fkt3m2esc7actxvqhmovs8wly1p FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 4798 (class 2606 OID 208828)
-- Name: involvedpartyidentificationtypesecuritytoken fkt53nic14klq9pmlooadcird6a; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyidentificationtypesecuritytoken
    ADD CONSTRAINT fkt53nic14klq9pmlooadcird6a FOREIGN KEY (securitytokenid, warehousecreateddate) REFERENCES security.securitytoken (securitytokenid, warehousecreateddate);


--
-- TOC entry 4907 (class 2606 OID 208833)
-- Name: involvedpartyxinvolvedpartynametype fkt5o5c0529tdsv531mn84tmm9f; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyxinvolvedpartynametype
    ADD CONSTRAINT fkt5o5c0529tdsv531mn84tmm9f FOREIGN KEY (involvedpartynametypeid, warehousecreateddate) REFERENCES party.involvedpartynametype (involvedpartynametypeid, warehousecreateddate);


--
-- TOC entry 4895 (class 2606 OID 208838)
-- Name: involvedpartyxinvolvedpartyidentificationtype fktdfx6obcjum68sqbp0u7efean; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyxinvolvedpartyidentificationtype
    ADD CONSTRAINT fktdfx6obcjum68sqbp0u7efean FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 4908 (class 2606 OID 208843)
-- Name: involvedpartyxinvolvedpartynametype fktl0dvsfwf7kt9mg6hean9j9xo; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyxinvolvedpartynametype
    ADD CONSTRAINT fktl0dvsfwf7kt9mg6hean9j9xo FOREIGN KEY (classificationid, warehousecreateddate) REFERENCES classification.classification (classificationid, warehousecreateddate);


--
-- TOC entry 4901 (class 2606 OID 208848)
-- Name: involvedpartyxinvolvedpartyidentificationtypesecuritytoken fktltopjihcyxw2xet9w7toxvm1; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyxinvolvedpartyidentificationtypesecuritytoken
    ADD CONSTRAINT fktltopjihcyxw2xet9w7toxvm1 FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 4973 (class 2606 OID 208853)
-- Name: involvedpartyxresourceitemsecuritytoken fktnbu592x1dn0dtkv0lfo7cs6b; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyxresourceitemsecuritytoken
    ADD CONSTRAINT fktnbu592x1dn0dtkv0lfo7cs6b FOREIGN KEY (securitytokenid, warehousecreateddate) REFERENCES security.securitytoken (securitytokenid, warehousecreateddate);


--
-- TOC entry 4947 (class 2606 OID 208858)
-- Name: involvedpartyxproductsecuritytoken fktoih3sskv7bfum9756f55oveh; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyxproductsecuritytoken
    ADD CONSTRAINT fktoih3sskv7bfum9756f55oveh FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 4981 (class 2606 OID 208863)
-- Name: involvedpartyxrules fkwjuotwflalqakh6pqixxoa1l; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE party.involvedpartyxrules
    ADD CONSTRAINT fkwjuotwflalqakh6pqixxoa1l FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 5002 (class 2606 OID 208868)
-- Name: producttypessecuritytoken fk124dnn13mvx64w9sscl56x0e9; Type: FK CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE product.producttypessecuritytoken
    ADD CONSTRAINT fk124dnn13mvx64w9sscl56x0e9 FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 5020 (class 2606 OID 208873)
-- Name: productxclassification fk170jhdqdisurhraj01enjp07f; Type: FK CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE product.productxclassification
    ADD CONSTRAINT fk170jhdqdisurhraj01enjp07f FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 5039 (class 2606 OID 208878)
-- Name: productxproductsecuritytoken fk1ig8t64jsxnh0bpi3ql9st9ox; Type: FK CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE product.productxproductsecuritytoken
    ADD CONSTRAINT fk1ig8t64jsxnh0bpi3ql9st9ox FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 4988 (class 2606 OID 208883)
-- Name: product fk1uilqm7vj2gtc2d8x638robxd; Type: FK CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE product.product
    ADD CONSTRAINT fk1uilqm7vj2gtc2d8x638robxd FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 5045 (class 2606 OID 208888)
-- Name: productxproducttype fk1v5k0dbqs8u10y8qplq99qoiq; Type: FK CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE product.productxproducttype
    ADD CONSTRAINT fk1v5k0dbqs8u10y8qplq99qoiq FOREIGN KEY (productid, warehousecreateddate) REFERENCES product.product (productid, warehousecreateddate);


--
-- TOC entry 5052 (class 2606 OID 208893)
-- Name: productxproducttypesecuritytoken fk24ny4320upy05vtrp0bl40edh; Type: FK CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE product.productxproducttypesecuritytoken
    ADD CONSTRAINT fk24ny4320upy05vtrp0bl40edh FOREIGN KEY (securitytokenid, warehousecreateddate) REFERENCES security.securitytoken (securitytokenid, warehousecreateddate);


--
-- TOC entry 5026 (class 2606 OID 208898)
-- Name: productxclassificationsecuritytoken fk25k9qm7701i83jtdlw9pma3it; Type: FK CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE product.productxclassificationsecuritytoken
    ADD CONSTRAINT fk25k9qm7701i83jtdlw9pma3it FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 5027 (class 2606 OID 208903)
-- Name: productxclassificationsecuritytoken fk2a1344eckqbgsu5qtw1hqtmii; Type: FK CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE product.productxclassificationsecuritytoken
    ADD CONSTRAINT fk2a1344eckqbgsu5qtw1hqtmii FOREIGN KEY (productxclassificationid, warehousecreateddate) REFERENCES product.productxclassification (productxclassificationid, warehousecreateddate);


--
-- TOC entry 5058 (class 2606 OID 208908)
-- Name: productxresourceitem fk2kfndi7vipw90dmibvhml8luf; Type: FK CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE product.productxresourceitem
    ADD CONSTRAINT fk2kfndi7vipw90dmibvhml8luf FOREIGN KEY (classificationid, warehousecreateddate) REFERENCES classification.classification (classificationid, warehousecreateddate);


--
-- TOC entry 5065 (class 2606 OID 208913)
-- Name: productxresourceitemsecuritytoken fk2n140poncbaodw93p66dr19wm; Type: FK CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE product.productxresourceitemsecuritytoken
    ADD CONSTRAINT fk2n140poncbaodw93p66dr19wm FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 5046 (class 2606 OID 208918)
-- Name: productxproducttype fk2n7njekvkkktqita4lo8o2rop; Type: FK CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE product.productxproducttype
    ADD CONSTRAINT fk2n7njekvkkktqita4lo8o2rop FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 5059 (class 2606 OID 208923)
-- Name: productxresourceitem fk3c9bk1tu9tr4ajj57j50lq61s; Type: FK CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE product.productxresourceitem
    ADD CONSTRAINT fk3c9bk1tu9tr4ajj57j50lq61s FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 5053 (class 2606 OID 208928)
-- Name: productxproducttypesecuritytoken fk3h3ux71dsf7opesl0p4fq9y2n; Type: FK CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE product.productxproducttypesecuritytoken
    ADD CONSTRAINT fk3h3ux71dsf7opesl0p4fq9y2n FOREIGN KEY (productxproducttypeid, warehousecreateddate) REFERENCES product.productxproducttype (productxproducttypeid, warehousecreateddate);


--
-- TOC entry 5060 (class 2606 OID 208933)
-- Name: productxresourceitem fk4ircw0cakrrbxh9eb947do1r1; Type: FK CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE product.productxresourceitem
    ADD CONSTRAINT fk4ircw0cakrrbxh9eb947do1r1 FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 5066 (class 2606 OID 208938)
-- Name: productxresourceitemsecuritytoken fk4pq3n38ujll0fn7l4mw2f0uj2; Type: FK CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE product.productxresourceitemsecuritytoken
    ADD CONSTRAINT fk4pq3n38ujll0fn7l4mw2f0uj2 FOREIGN KEY (productxresourceitemid, warehousecreateddate) REFERENCES product.productxresourceitem (productxresourceitemid, warehousecreateddate);


--
-- TOC entry 5021 (class 2606 OID 208943)
-- Name: productxclassification fk4rxn4f51nq7nachqwenoijt19; Type: FK CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE product.productxclassification
    ADD CONSTRAINT fk4rxn4f51nq7nachqwenoijt19 FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4989 (class 2606 OID 208948)
-- Name: product fk5igcn0xk318avw7alibquq364; Type: FK CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE product.product
    ADD CONSTRAINT fk5igcn0xk318avw7alibquq364 FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 5054 (class 2606 OID 208953)
-- Name: productxproducttypesecuritytoken fk5r6sj8u01cbw7ndxn6nkbqbgs; Type: FK CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE product.productxproducttypesecuritytoken
    ADD CONSTRAINT fk5r6sj8u01cbw7ndxn6nkbqbgs FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 5047 (class 2606 OID 208958)
-- Name: productxproducttype fk64ad5aqtu7abdea2c46wmwsyp; Type: FK CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE product.productxproducttype
    ADD CONSTRAINT fk64ad5aqtu7abdea2c46wmwsyp FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 5032 (class 2606 OID 208963)
-- Name: productxproduct fk6by61ucqfknganlsl2nrl75qi; Type: FK CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE product.productxproduct
    ADD CONSTRAINT fk6by61ucqfknganlsl2nrl75qi FOREIGN KEY (childproductid, warehousecreateddate) REFERENCES product.product (productid, warehousecreateddate);


--
-- TOC entry 5022 (class 2606 OID 208968)
-- Name: productxclassification fk6if9d2r0jph42fstek03stxde; Type: FK CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE product.productxclassification
    ADD CONSTRAINT fk6if9d2r0jph42fstek03stxde FOREIGN KEY (classificationid, warehousecreateddate) REFERENCES classification.classification (classificationid, warehousecreateddate);


--
-- TOC entry 4992 (class 2606 OID 208973)
-- Name: productsecuritytoken fk6nu64a57s4xf2f259fg2ds2vx; Type: FK CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE product.productsecuritytoken
    ADD CONSTRAINT fk6nu64a57s4xf2f259fg2ds2vx FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4998 (class 2606 OID 208978)
-- Name: producttype fk6qo5a8hhlrogyas0wxpefi252; Type: FK CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE product.producttype
    ADD CONSTRAINT fk6qo5a8hhlrogyas0wxpefi252 FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 5033 (class 2606 OID 208983)
-- Name: productxproduct fk77xliloqs4xw4ci0ajbplqpww; Type: FK CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE product.productxproduct
    ADD CONSTRAINT fk77xliloqs4xw4ci0ajbplqpww FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4999 (class 2606 OID 208988)
-- Name: producttype fk7jpihblwon21gvxequinglp4u; Type: FK CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE product.producttype
    ADD CONSTRAINT fk7jpihblwon21gvxequinglp4u FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 5048 (class 2606 OID 208993)
-- Name: productxproducttype fk7qloe9c2vsgtb7r52s8rwam77; Type: FK CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE product.productxproducttype
    ADD CONSTRAINT fk7qloe9c2vsgtb7r52s8rwam77 FOREIGN KEY (producttypeid, warehousecreateddate) REFERENCES product.producttype (producttypeid, warehousecreateddate);


--
-- TOC entry 5040 (class 2606 OID 208998)
-- Name: productxproductsecuritytoken fk87msnwio1ifds9frgcawk703q; Type: FK CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE product.productxproductsecuritytoken
    ADD CONSTRAINT fk87msnwio1ifds9frgcawk703q FOREIGN KEY (securitytokenid, warehousecreateddate) REFERENCES security.securitytoken (securitytokenid, warehousecreateddate);


--
-- TOC entry 5023 (class 2606 OID 209003)
-- Name: productxclassification fk8b81utqms0112fwiuv724pss1; Type: FK CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE product.productxclassification
    ADD CONSTRAINT fk8b81utqms0112fwiuv724pss1 FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 5024 (class 2606 OID 209008)
-- Name: productxclassification fk8bl09vfxm9qhghywxbfk36qra; Type: FK CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE product.productxclassification
    ADD CONSTRAINT fk8bl09vfxm9qhghywxbfk36qra FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 5067 (class 2606 OID 209013)
-- Name: productxresourceitemsecuritytoken fk8ljhn730t04d2yrudqin3b4rx; Type: FK CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE product.productxresourceitemsecuritytoken
    ADD CONSTRAINT fk8ljhn730t04d2yrudqin3b4rx FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 5068 (class 2606 OID 209018)
-- Name: productxresourceitemsecuritytoken fk9rwmunm7mftbfjjefdk9i0ryh; Type: FK CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE product.productxresourceitemsecuritytoken
    ADD CONSTRAINT fk9rwmunm7mftbfjjefdk9i0ryh FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 5003 (class 2606 OID 209023)
-- Name: producttypessecuritytoken fka4hffxoba5up3ewpbck3bydrl; Type: FK CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE product.producttypessecuritytoken
    ADD CONSTRAINT fka4hffxoba5up3ewpbck3bydrl FOREIGN KEY (securitytokenid, warehousecreateddate) REFERENCES security.securitytoken (securitytokenid, warehousecreateddate);


--
-- TOC entry 5000 (class 2606 OID 209028)
-- Name: producttype fkaji1ysqjjctugqxgktlyq4jb7; Type: FK CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE product.producttype
    ADD CONSTRAINT fkaji1ysqjjctugqxgktlyq4jb7 FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 5028 (class 2606 OID 209033)
-- Name: productxclassificationsecuritytoken fkalo1aiwrqa3qvcoyta4wmron0; Type: FK CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE product.productxclassificationsecuritytoken
    ADD CONSTRAINT fkalo1aiwrqa3qvcoyta4wmron0 FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 5004 (class 2606 OID 209038)
-- Name: producttypessecuritytoken fkbw6gf8n6ce2ob0qbvc0hdr876; Type: FK CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE product.producttypessecuritytoken
    ADD CONSTRAINT fkbw6gf8n6ce2ob0qbvc0hdr876 FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 5008 (class 2606 OID 209043)
-- Name: producttypexclassification fkd0fwhreudgdgcdipqbmxnrpw0; Type: FK CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE product.producttypexclassification
    ADD CONSTRAINT fkd0fwhreudgdgcdipqbmxnrpw0 FOREIGN KEY (classificationid, warehousecreateddate) REFERENCES classification.classification (classificationid, warehousecreateddate);


--
-- TOC entry 5034 (class 2606 OID 209048)
-- Name: productxproduct fkdcpq7m2melgfwv5auqyagl72h; Type: FK CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE product.productxproduct
    ADD CONSTRAINT fkdcpq7m2melgfwv5auqyagl72h FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 5061 (class 2606 OID 209053)
-- Name: productxresourceitem fkdlspqxuytfux61envksg2oblc; Type: FK CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE product.productxresourceitem
    ADD CONSTRAINT fkdlspqxuytfux61envksg2oblc FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 5055 (class 2606 OID 209058)
-- Name: productxproducttypesecuritytoken fke8k4xro3jq77djofuxtcqjnb7; Type: FK CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE product.productxproducttypesecuritytoken
    ADD CONSTRAINT fke8k4xro3jq77djofuxtcqjnb7 FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 5009 (class 2606 OID 209063)
-- Name: producttypexclassification fkefusbrg6b297xo4uaifrnf7of; Type: FK CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE product.producttypexclassification
    ADD CONSTRAINT fkefusbrg6b297xo4uaifrnf7of FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 5041 (class 2606 OID 209068)
-- Name: productxproductsecuritytoken fkfov8qx41nggbg75jj8ely4wrp; Type: FK CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE product.productxproductsecuritytoken
    ADD CONSTRAINT fkfov8qx41nggbg75jj8ely4wrp FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 4990 (class 2606 OID 209073)
-- Name: product fkfvbgcjo7xxxjqbqwy6rf9okxe; Type: FK CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE product.product
    ADD CONSTRAINT fkfvbgcjo7xxxjqbqwy6rf9okxe FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 5001 (class 2606 OID 209078)
-- Name: producttype fkg36gb46ujtrxwet1ac4f2k43b; Type: FK CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE product.producttype
    ADD CONSTRAINT fkg36gb46ujtrxwet1ac4f2k43b FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4991 (class 2606 OID 209083)
-- Name: product fkhd5i44wqur3jhd2e66aj1e38; Type: FK CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE product.product
    ADD CONSTRAINT fkhd5i44wqur3jhd2e66aj1e38 FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 5069 (class 2606 OID 209088)
-- Name: productxresourceitemsecuritytoken fkhgl7dki8ue6gmivh1ulwel4j1; Type: FK CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE product.productxresourceitemsecuritytoken
    ADD CONSTRAINT fkhgl7dki8ue6gmivh1ulwel4j1 FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 5014 (class 2606 OID 209093)
-- Name: producttypexclassificationsecuritytoken fkhp82lysbkher7sh9nmdc8j5mt; Type: FK CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE product.producttypexclassificationsecuritytoken
    ADD CONSTRAINT fkhp82lysbkher7sh9nmdc8j5mt FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 5005 (class 2606 OID 209098)
-- Name: producttypessecuritytoken fkhqgmcxurg87rgu1ekgl6xqb4; Type: FK CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE product.producttypessecuritytoken
    ADD CONSTRAINT fkhqgmcxurg87rgu1ekgl6xqb4 FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 4993 (class 2606 OID 209103)
-- Name: productsecuritytoken fkhwbgx5m4cqg3drq0kbl2ln218; Type: FK CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE product.productsecuritytoken
    ADD CONSTRAINT fkhwbgx5m4cqg3drq0kbl2ln218 FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 5015 (class 2606 OID 209108)
-- Name: producttypexclassificationsecuritytoken fki005cybjhowcm4dc6a4rur35f; Type: FK CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE product.producttypexclassificationsecuritytoken
    ADD CONSTRAINT fki005cybjhowcm4dc6a4rur35f FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 5049 (class 2606 OID 209113)
-- Name: productxproducttype fki3kgl08d83biiovc4ipk0i82k; Type: FK CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE product.productxproducttype
    ADD CONSTRAINT fki3kgl08d83biiovc4ipk0i82k FOREIGN KEY (classificationid, warehousecreateddate) REFERENCES classification.classification (classificationid, warehousecreateddate);


--
-- TOC entry 4994 (class 2606 OID 209118)
-- Name: productsecuritytoken fkix4kyq2jvwpfkwr07hb5418wk; Type: FK CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE product.productsecuritytoken
    ADD CONSTRAINT fkix4kyq2jvwpfkwr07hb5418wk FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 5062 (class 2606 OID 209123)
-- Name: productxresourceitem fkjcm094m4k21700pj371gyg28; Type: FK CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE product.productxresourceitem
    ADD CONSTRAINT fkjcm094m4k21700pj371gyg28 FOREIGN KEY (productid, warehousecreateddate) REFERENCES product.product (productid, warehousecreateddate);


--
-- TOC entry 5035 (class 2606 OID 209128)
-- Name: productxproduct fkjtuo8wff4bgrag27r7b90e0cr; Type: FK CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE product.productxproduct
    ADD CONSTRAINT fkjtuo8wff4bgrag27r7b90e0cr FOREIGN KEY (classificationid, warehousecreateddate) REFERENCES classification.classification (classificationid, warehousecreateddate);


--
-- TOC entry 5042 (class 2606 OID 209133)
-- Name: productxproductsecuritytoken fkk3c9qgnvemsrtifm1qkfwwpai; Type: FK CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE product.productxproductsecuritytoken
    ADD CONSTRAINT fkk3c9qgnvemsrtifm1qkfwwpai FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 5063 (class 2606 OID 209138)
-- Name: productxresourceitem fkk4j0vxf6l51756si5xlelvhyx; Type: FK CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE product.productxresourceitem
    ADD CONSTRAINT fkk4j0vxf6l51756si5xlelvhyx FOREIGN KEY (resourceitemid, warehousecreateddate) REFERENCES resource.resourceitem (resourceitemid, warehousecreateddate);


--
-- TOC entry 5036 (class 2606 OID 209143)
-- Name: productxproduct fkkh1fb4vobxxm3gqr902b2sy9q; Type: FK CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE product.productxproduct
    ADD CONSTRAINT fkkh1fb4vobxxm3gqr902b2sy9q FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 5016 (class 2606 OID 209148)
-- Name: producttypexclassificationsecuritytoken fkkpdeivhwgmup6yq5or7mkhh3i; Type: FK CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE product.producttypexclassificationsecuritytoken
    ADD CONSTRAINT fkkpdeivhwgmup6yq5or7mkhh3i FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 5006 (class 2606 OID 209153)
-- Name: producttypessecuritytoken fkkpofjm6j3idsftpk1nilvd5am; Type: FK CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE product.producttypessecuritytoken
    ADD CONSTRAINT fkkpofjm6j3idsftpk1nilvd5am FOREIGN KEY (producttypesid, warehousecreateddate) REFERENCES product.producttype (producttypeid, warehousecreateddate);


--
-- TOC entry 4995 (class 2606 OID 209158)
-- Name: productsecuritytoken fklfavoymys95w6sr5vg7fc2lpd; Type: FK CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE product.productsecuritytoken
    ADD CONSTRAINT fklfavoymys95w6sr5vg7fc2lpd FOREIGN KEY (productid, warehousecreateddate) REFERENCES product.product (productid, warehousecreateddate);


--
-- TOC entry 5010 (class 2606 OID 209163)
-- Name: producttypexclassification fklpladubwvqlvl4m7vv5k7i04a; Type: FK CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE product.producttypexclassification
    ADD CONSTRAINT fklpladubwvqlvl4m7vv5k7i04a FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 5070 (class 2606 OID 209168)
-- Name: productxresourceitemsecuritytoken fklwlxp6u9a4k42nl1xqst9y0y; Type: FK CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE product.productxresourceitemsecuritytoken
    ADD CONSTRAINT fklwlxp6u9a4k42nl1xqst9y0y FOREIGN KEY (securitytokenid, warehousecreateddate) REFERENCES security.securitytoken (securitytokenid, warehousecreateddate);


--
-- TOC entry 5037 (class 2606 OID 209173)
-- Name: productxproduct fklwu619drh230ni7r1dow2jdo1; Type: FK CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE product.productxproduct
    ADD CONSTRAINT fklwu619drh230ni7r1dow2jdo1 FOREIGN KEY (parentproductid, warehousecreateddate) REFERENCES product.product (productid, warehousecreateddate);


--
-- TOC entry 5011 (class 2606 OID 209178)
-- Name: producttypexclassification fkm04vi4rjib6p4wstxivhv5uwb; Type: FK CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE product.producttypexclassification
    ADD CONSTRAINT fkm04vi4rjib6p4wstxivhv5uwb FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 5043 (class 2606 OID 209183)
-- Name: productxproductsecuritytoken fkm2agj4yop5p5pdbbqalr9ys6d; Type: FK CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE product.productxproductsecuritytoken
    ADD CONSTRAINT fkm2agj4yop5p5pdbbqalr9ys6d FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 5017 (class 2606 OID 209188)
-- Name: producttypexclassificationsecuritytoken fkm2t70qvf88y1hc6xxtp5yvedj; Type: FK CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE product.producttypexclassificationsecuritytoken
    ADD CONSTRAINT fkm2t70qvf88y1hc6xxtp5yvedj FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 5018 (class 2606 OID 209193)
-- Name: producttypexclassificationsecuritytoken fkmtyho44wd7jen9pckqv90td0j; Type: FK CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE product.producttypexclassificationsecuritytoken
    ADD CONSTRAINT fkmtyho44wd7jen9pckqv90td0j FOREIGN KEY (producttypexclassificationid, warehousecreateddate) REFERENCES product.producttypexclassification (producttypexclassificationid, warehousecreateddate);


--
-- TOC entry 5019 (class 2606 OID 209198)
-- Name: producttypexclassificationsecuritytoken fkn22khncgjxl940nga25siqctl; Type: FK CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE product.producttypexclassificationsecuritytoken
    ADD CONSTRAINT fkn22khncgjxl940nga25siqctl FOREIGN KEY (securitytokenid, warehousecreateddate) REFERENCES security.securitytoken (securitytokenid, warehousecreateddate);


--
-- TOC entry 5029 (class 2606 OID 209203)
-- Name: productxclassificationsecuritytoken fkncist9vnbkli1wfjeyqeqpbhi; Type: FK CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE product.productxclassificationsecuritytoken
    ADD CONSTRAINT fkncist9vnbkli1wfjeyqeqpbhi FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 5056 (class 2606 OID 209208)
-- Name: productxproducttypesecuritytoken fknnkyxacvaltro761yay2h0a0t; Type: FK CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE product.productxproducttypesecuritytoken
    ADD CONSTRAINT fknnkyxacvaltro761yay2h0a0t FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 5057 (class 2606 OID 209213)
-- Name: productxproducttypesecuritytoken fknr2deq2638s77m37vyx16g6i0; Type: FK CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE product.productxproducttypesecuritytoken
    ADD CONSTRAINT fknr2deq2638s77m37vyx16g6i0 FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 4996 (class 2606 OID 209218)
-- Name: productsecuritytoken fkobdmay5wpjxkq8fmnh5jb4wax; Type: FK CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE product.productsecuritytoken
    ADD CONSTRAINT fkobdmay5wpjxkq8fmnh5jb4wax FOREIGN KEY (securitytokenid, warehousecreateddate) REFERENCES security.securitytoken (securitytokenid, warehousecreateddate);


--
-- TOC entry 5012 (class 2606 OID 209223)
-- Name: producttypexclassification fkon0srs81eq9qmdm4clgh7lf1r; Type: FK CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE product.producttypexclassification
    ADD CONSTRAINT fkon0srs81eq9qmdm4clgh7lf1r FOREIGN KEY (producttypeid, warehousecreateddate) REFERENCES product.producttype (producttypeid, warehousecreateddate);


--
-- TOC entry 5007 (class 2606 OID 209228)
-- Name: producttypessecuritytoken fkpevw33iuc65w72j6c9j1scwi; Type: FK CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE product.producttypessecuritytoken
    ADD CONSTRAINT fkpevw33iuc65w72j6c9j1scwi FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 5030 (class 2606 OID 209233)
-- Name: productxclassificationsecuritytoken fkphp040h86si2e7sthuts7u84a; Type: FK CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE product.productxclassificationsecuritytoken
    ADD CONSTRAINT fkphp040h86si2e7sthuts7u84a FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 5038 (class 2606 OID 209238)
-- Name: productxproduct fkpjcmb3bm0uykh5ycoxw327a6y; Type: FK CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE product.productxproduct
    ADD CONSTRAINT fkpjcmb3bm0uykh5ycoxw327a6y FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 4997 (class 2606 OID 209243)
-- Name: productsecuritytoken fkpp00icvq84wm807qt11c7di0o; Type: FK CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE product.productsecuritytoken
    ADD CONSTRAINT fkpp00icvq84wm807qt11c7di0o FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 5064 (class 2606 OID 209248)
-- Name: productxresourceitem fkq078arvokvft7rp6g01drm5ga; Type: FK CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE product.productxresourceitem
    ADD CONSTRAINT fkq078arvokvft7rp6g01drm5ga FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 5050 (class 2606 OID 209253)
-- Name: productxproducttype fkqx5p60ei4jsps9ajg56g6y75n; Type: FK CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE product.productxproducttype
    ADD CONSTRAINT fkqx5p60ei4jsps9ajg56g6y75n FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 5044 (class 2606 OID 209258)
-- Name: productxproductsecuritytoken fksmhfy43ik4seggjpeh0hd7ohe; Type: FK CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE product.productxproductsecuritytoken
    ADD CONSTRAINT fksmhfy43ik4seggjpeh0hd7ohe FOREIGN KEY (productxproductid, warehousecreateddate) REFERENCES product.productxproduct (productxproductid, warehousecreateddate);


--
-- TOC entry 5031 (class 2606 OID 209263)
-- Name: productxclassificationsecuritytoken fksqaqrh3mqcptl4uk5ucmkn34h; Type: FK CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE product.productxclassificationsecuritytoken
    ADD CONSTRAINT fksqaqrh3mqcptl4uk5ucmkn34h FOREIGN KEY (securitytokenid, warehousecreateddate) REFERENCES security.securitytoken (securitytokenid, warehousecreateddate);


--
-- TOC entry 5013 (class 2606 OID 209268)
-- Name: producttypexclassification fktby5ywfw3h2p0kwing53rbw83; Type: FK CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE product.producttypexclassification
    ADD CONSTRAINT fktby5ywfw3h2p0kwing53rbw83 FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 5025 (class 2606 OID 209273)
-- Name: productxclassification fktjv0q3owa2dvherxn8lwy4umj; Type: FK CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE product.productxclassification
    ADD CONSTRAINT fktjv0q3owa2dvherxn8lwy4umj FOREIGN KEY (productid, warehousecreateddate) REFERENCES product.product (productid, warehousecreateddate);


--
-- TOC entry 5051 (class 2606 OID 209278)
-- Name: productxproducttype fktn5yb54e9i7r3hwtr1bojxh6e; Type: FK CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE product.productxproducttype
    ADD CONSTRAINT fktn5yb54e9i7r3hwtr1bojxh6e FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 5071 (class 2606 OID 209283)
-- Name: quarters_months fkb9r9yk4sx0s7vbxr3h7v1ahf9; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE public.quarters_months
    ADD CONSTRAINT fkb9r9yk4sx0s7vbxr3h7v1ahf9 FOREIGN KEY (quarters_quarterid) REFERENCES "time".quarters (quarterid);


--
-- TOC entry 5073 (class 2606 OID 209288)
-- Name: securityhierarchyparents fkbrpasxc35cefp3eivjag2jq8f; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE public.securityhierarchyparents
    ADD CONSTRAINT fkbrpasxc35cefp3eivjag2jq8f FOREIGN KEY (id) REFERENCES security.securityhierarchy (id);


--
-- TOC entry 5072 (class 2606 OID 209293)
-- Name: quarters_months fkg7qo91crqcfpnvgf4fpaxt8pf; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE public.quarters_months
    ADD CONSTRAINT fkg7qo91crqcfpnvgf4fpaxt8pf FOREIGN KEY (lumonthslist_monthid) REFERENCES "time".months (monthid);


--
-- TOC entry 5078 (class 2606 OID 209298)
-- Name: resourceitemdata fk1fo3xmni8ec47xrr4ga2eff84; Type: FK CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE resource.resourceitemdata
    ADD CONSTRAINT fk1fo3xmni8ec47xrr4ga2eff84 FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 5136 (class 2606 OID 209303)
-- Name: resourceitemxresourceitemsecuritytoken fk1n6u8l8jhlesanln4et9jehgh; Type: FK CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE resource.resourceitemxresourceitemsecuritytoken
    ADD CONSTRAINT fk1n6u8l8jhlesanln4et9jehgh FOREIGN KEY (resourceitemxresourceitemid, warehousecreateddate) REFERENCES resource.resourceitemxresourceitem (resourceitemxresourceitemid, warehousecreateddate);


--
-- TOC entry 5074 (class 2606 OID 209308)
-- Name: resourceitem fk200sir97l7uhxxs2l0b23ok3r; Type: FK CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE resource.resourceitem
    ADD CONSTRAINT fk200sir97l7uhxxs2l0b23ok3r FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 5089 (class 2606 OID 209313)
-- Name: resourceitemdataxclassification fk2dy40tyg4ideqgywl6lh9gly5; Type: FK CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE resource.resourceitemdataxclassification
    ADD CONSTRAINT fk2dy40tyg4ideqgywl6lh9gly5 FOREIGN KEY (resourceitemdataid, warehousecreateddate) REFERENCES resource.resourceitemdata (resourceitemdataid, warehousecreateddate);


--
-- TOC entry 5149 (class 2606 OID 209318)
-- Name: resourceitemxresourceitemtypesecuritytoken fk2ep9v4fv470h60ye8kq7lfybi; Type: FK CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE resource.resourceitemxresourceitemtypesecuritytoken
    ADD CONSTRAINT fk2ep9v4fv470h60ye8kq7lfybi FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 5129 (class 2606 OID 209323)
-- Name: resourceitemxresourceitem fk2ki967a981nc2wof2y4u5kwtw; Type: FK CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE resource.resourceitemxresourceitem
    ADD CONSTRAINT fk2ki967a981nc2wof2y4u5kwtw FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 5123 (class 2606 OID 209328)
-- Name: resourceitemxclassificationsecuritytoken fk3i66vwdq4it2rgp85m6d0jq81; Type: FK CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE resource.resourceitemxclassificationsecuritytoken
    ADD CONSTRAINT fk3i66vwdq4it2rgp85m6d0jq81 FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 5111 (class 2606 OID 209333)
-- Name: resourceitemtypesecuritytoken fk3p3b9iwh15wwgccqgj3lpysqn; Type: FK CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE resource.resourceitemtypesecuritytoken
    ADD CONSTRAINT fk3p3b9iwh15wwgccqgj3lpysqn FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 5083 (class 2606 OID 209338)
-- Name: resourceitemdatasecuritytoken fk3w9axr9msmakpp6ferrlkuvdy; Type: FK CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE resource.resourceitemdatasecuritytoken
    ADD CONSTRAINT fk3w9axr9msmakpp6ferrlkuvdy FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 5117 (class 2606 OID 209343)
-- Name: resourceitemxclassification fk3wmuxdwncxa9duotxup8kots8; Type: FK CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE resource.resourceitemxclassification
    ADD CONSTRAINT fk3wmuxdwncxa9duotxup8kots8 FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 5112 (class 2606 OID 209348)
-- Name: resourceitemtypesecuritytoken fk4cdo57v9c89cmsd70444brarb; Type: FK CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE resource.resourceitemtypesecuritytoken
    ADD CONSTRAINT fk4cdo57v9c89cmsd70444brarb FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 5118 (class 2606 OID 209353)
-- Name: resourceitemxclassification fk5ad2d7e8c7kiwjtnfv4baj6k5; Type: FK CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE resource.resourceitemxclassification
    ADD CONSTRAINT fk5ad2d7e8c7kiwjtnfv4baj6k5 FOREIGN KEY (classificationid, warehousecreateddate) REFERENCES classification.classification (classificationid, warehousecreateddate);


--
-- TOC entry 5130 (class 2606 OID 209358)
-- Name: resourceitemxresourceitem fk5jtsbc1lwwjo7gimmifc90cxv; Type: FK CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE resource.resourceitemxresourceitem
    ADD CONSTRAINT fk5jtsbc1lwwjo7gimmifc90cxv FOREIGN KEY (parentresourceitemid, warehousecreateddate) REFERENCES resource.resourceitem (resourceitemid, warehousecreateddate);


--
-- TOC entry 5113 (class 2606 OID 209363)
-- Name: resourceitemtypesecuritytoken fk5wdvel5htabl0xh6uo6do4nmw; Type: FK CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE resource.resourceitemtypesecuritytoken
    ADD CONSTRAINT fk5wdvel5htabl0xh6uo6do4nmw FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 5090 (class 2606 OID 209368)
-- Name: resourceitemdataxclassification fk663fflx6v3085crppbxdji50p; Type: FK CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE resource.resourceitemdataxclassification
    ADD CONSTRAINT fk663fflx6v3085crppbxdji50p FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 5107 (class 2606 OID 209373)
-- Name: resourceitemtype fk6qq7tnhpgyvvt5vly41nemv50; Type: FK CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE resource.resourceitemtype
    ADD CONSTRAINT fk6qq7tnhpgyvvt5vly41nemv50 FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 5137 (class 2606 OID 209378)
-- Name: resourceitemxresourceitemsecuritytoken fk6sn4lyt8rfdltu9cbblt5uieh; Type: FK CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE resource.resourceitemxresourceitemsecuritytoken
    ADD CONSTRAINT fk6sn4lyt8rfdltu9cbblt5uieh FOREIGN KEY (securitytokenid, warehousecreateddate) REFERENCES security.securitytoken (securitytokenid, warehousecreateddate);


--
-- TOC entry 5095 (class 2606 OID 209383)
-- Name: resourceitemdataxclassificationsecuritytoken fk79odxnrnlexs55g44rmjdw9my; Type: FK CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE resource.resourceitemdataxclassificationsecuritytoken
    ADD CONSTRAINT fk79odxnrnlexs55g44rmjdw9my FOREIGN KEY (resourceitemdataxclassificationid, warehousecreateddate) REFERENCES resource.resourceitemdataxclassification (resourceitemdataxclassificationid, warehousecreateddate);


--
-- TOC entry 5124 (class 2606 OID 209388)
-- Name: resourceitemxclassificationsecuritytoken fk7ns6uhcbucfbl2uioayhiydj2; Type: FK CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE resource.resourceitemxclassificationsecuritytoken
    ADD CONSTRAINT fk7ns6uhcbucfbl2uioayhiydj2 FOREIGN KEY (securitytokenid, warehousecreateddate) REFERENCES security.securitytoken (securitytokenid, warehousecreateddate);


--
-- TOC entry 5096 (class 2606 OID 209393)
-- Name: resourceitemdataxclassificationsecuritytoken fk7phkn8oemhek6rr9400ha8lh3; Type: FK CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE resource.resourceitemdataxclassificationsecuritytoken
    ADD CONSTRAINT fk7phkn8oemhek6rr9400ha8lh3 FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 5125 (class 2606 OID 209398)
-- Name: resourceitemxclassificationsecuritytoken fk7wx6yqn7nc5fajp9dhi1f2mjx; Type: FK CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE resource.resourceitemxclassificationsecuritytoken
    ADD CONSTRAINT fk7wx6yqn7nc5fajp9dhi1f2mjx FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 5101 (class 2606 OID 209403)
-- Name: resourceitemsecuritytoken fk84jknr1n5cnm1wtlwn07d1r64; Type: FK CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE resource.resourceitemsecuritytoken
    ADD CONSTRAINT fk84jknr1n5cnm1wtlwn07d1r64 FOREIGN KEY (securitytokenid, warehousecreateddate) REFERENCES security.securitytoken (securitytokenid, warehousecreateddate);


--
-- TOC entry 5102 (class 2606 OID 209408)
-- Name: resourceitemsecuritytoken fk861bu9d1oka3g8dh0mb85tyc4; Type: FK CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE resource.resourceitemsecuritytoken
    ADD CONSTRAINT fk861bu9d1oka3g8dh0mb85tyc4 FOREIGN KEY (resourceitemid, warehousecreateddate) REFERENCES resource.resourceitem (resourceitemid, warehousecreateddate);


--
-- TOC entry 5075 (class 2606 OID 209413)
-- Name: resourceitem fk8cpmhhiy16t26qcrc94hxyngi; Type: FK CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE resource.resourceitem
    ADD CONSTRAINT fk8cpmhhiy16t26qcrc94hxyngi FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 5119 (class 2606 OID 209418)
-- Name: resourceitemxclassification fk8obyha4m3ud6wg5wugtj0efl3; Type: FK CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE resource.resourceitemxclassification
    ADD CONSTRAINT fk8obyha4m3ud6wg5wugtj0efl3 FOREIGN KEY (resourceitemid, warehousecreateddate) REFERENCES resource.resourceitem (resourceitemid, warehousecreateddate);


--
-- TOC entry 5138 (class 2606 OID 209423)
-- Name: resourceitemxresourceitemsecuritytoken fk8qo9i93o20ejx9qm1wys3xwmt; Type: FK CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE resource.resourceitemxresourceitemsecuritytoken
    ADD CONSTRAINT fk8qo9i93o20ejx9qm1wys3xwmt FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 5076 (class 2606 OID 209428)
-- Name: resourceitem fk94ha303yukem4jhrogbx5e06w; Type: FK CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE resource.resourceitem
    ADD CONSTRAINT fk94ha303yukem4jhrogbx5e06w FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 5097 (class 2606 OID 209433)
-- Name: resourceitemdataxclassificationsecuritytoken fka0xjd26qyqrft0dop3wn7f0np; Type: FK CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE resource.resourceitemdataxclassificationsecuritytoken
    ADD CONSTRAINT fka0xjd26qyqrft0dop3wn7f0np FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 5098 (class 2606 OID 209438)
-- Name: resourceitemdataxclassificationsecuritytoken fkab7vvw2nd2839r3whhm76iel; Type: FK CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE resource.resourceitemdataxclassificationsecuritytoken
    ADD CONSTRAINT fkab7vvw2nd2839r3whhm76iel FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 5099 (class 2606 OID 209443)
-- Name: resourceitemdataxclassificationsecuritytoken fkajq33984mmb4dan1b1ujl28qh; Type: FK CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE resource.resourceitemdataxclassificationsecuritytoken
    ADD CONSTRAINT fkajq33984mmb4dan1b1ujl28qh FOREIGN KEY (securitytokenid, warehousecreateddate) REFERENCES security.securitytoken (securitytokenid, warehousecreateddate);


--
-- TOC entry 5100 (class 2606 OID 209448)
-- Name: resourceitemdataxclassificationsecuritytoken fkb8riptu7wfv9qs6scj4k6bdnh; Type: FK CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE resource.resourceitemdataxclassificationsecuritytoken
    ADD CONSTRAINT fkb8riptu7wfv9qs6scj4k6bdnh FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 5131 (class 2606 OID 209453)
-- Name: resourceitemxresourceitem fkbb8g8afmpudbornwchcbn8d6x; Type: FK CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE resource.resourceitemxresourceitem
    ADD CONSTRAINT fkbb8g8afmpudbornwchcbn8d6x FOREIGN KEY (childresourceitemid, warehousecreateddate) REFERENCES resource.resourceitem (resourceitemid, warehousecreateddate);


--
-- TOC entry 5077 (class 2606 OID 209458)
-- Name: resourceitem fkc07twhlsn4lwl4lmx290qts4r; Type: FK CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE resource.resourceitem
    ADD CONSTRAINT fkc07twhlsn4lwl4lmx290qts4r FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 5150 (class 2606 OID 209463)
-- Name: resourceitemxresourceitemtypesecuritytoken fkc63ne5lom1f43u7t6lfi6imtn; Type: FK CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE resource.resourceitemxresourceitemtypesecuritytoken
    ADD CONSTRAINT fkc63ne5lom1f43u7t6lfi6imtn FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 5151 (class 2606 OID 209468)
-- Name: resourceitemxresourceitemtypesecuritytoken fkchn8hstbo7rkgh5817jqqcxpx; Type: FK CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE resource.resourceitemxresourceitemtypesecuritytoken
    ADD CONSTRAINT fkchn8hstbo7rkgh5817jqqcxpx FOREIGN KEY (securitytokenid, warehousecreateddate) REFERENCES security.securitytoken (securitytokenid, warehousecreateddate);


--
-- TOC entry 5142 (class 2606 OID 209473)
-- Name: resourceitemxresourceitemtype fkcvs3qfiioi6vtrvsx9fydc5w9; Type: FK CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE resource.resourceitemxresourceitemtype
    ADD CONSTRAINT fkcvs3qfiioi6vtrvsx9fydc5w9 FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 5132 (class 2606 OID 209478)
-- Name: resourceitemxresourceitem fkcwb9vx2pur7f8if9mh62f8yta; Type: FK CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE resource.resourceitemxresourceitem
    ADD CONSTRAINT fkcwb9vx2pur7f8if9mh62f8yta FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 5079 (class 2606 OID 209483)
-- Name: resourceitemdata fkdc81hbiavl5m699l1e4n4qtp; Type: FK CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE resource.resourceitemdata
    ADD CONSTRAINT fkdc81hbiavl5m699l1e4n4qtp FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 5133 (class 2606 OID 209488)
-- Name: resourceitemxresourceitem fkesiucs3j6sufylpxxpldbhwbl; Type: FK CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE resource.resourceitemxresourceitem
    ADD CONSTRAINT fkesiucs3j6sufylpxxpldbhwbl FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 5103 (class 2606 OID 209493)
-- Name: resourceitemsecuritytoken fkeut88pwpr6r3by6poh8s3lgn5; Type: FK CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE resource.resourceitemsecuritytoken
    ADD CONSTRAINT fkeut88pwpr6r3by6poh8s3lgn5 FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 5139 (class 2606 OID 209498)
-- Name: resourceitemxresourceitemsecuritytoken fkexfo0hib3thx9lan7x40hj7gr; Type: FK CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE resource.resourceitemxresourceitemsecuritytoken
    ADD CONSTRAINT fkexfo0hib3thx9lan7x40hj7gr FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 5084 (class 2606 OID 209503)
-- Name: resourceitemdatasecuritytoken fkfx83b2i5f4bu61o46gd6jpq3m; Type: FK CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE resource.resourceitemdatasecuritytoken
    ADD CONSTRAINT fkfx83b2i5f4bu61o46gd6jpq3m FOREIGN KEY (resourceitemdataid, warehousecreateddate) REFERENCES resource.resourceitemdata (resourceitemdataid, warehousecreateddate);


--
-- TOC entry 5143 (class 2606 OID 209508)
-- Name: resourceitemxresourceitemtype fkg86r3f2ftu2ex3xkplrd9tar7; Type: FK CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE resource.resourceitemxresourceitemtype
    ADD CONSTRAINT fkg86r3f2ftu2ex3xkplrd9tar7 FOREIGN KEY (resourceitemid, warehousecreateddate) REFERENCES resource.resourceitem (resourceitemid, warehousecreateddate);


--
-- TOC entry 5134 (class 2606 OID 209513)
-- Name: resourceitemxresourceitem fkgguetxp2rpaak5nhtais4qipi; Type: FK CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE resource.resourceitemxresourceitem
    ADD CONSTRAINT fkgguetxp2rpaak5nhtais4qipi FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 5120 (class 2606 OID 209518)
-- Name: resourceitemxclassification fkgjx0hpxxegn5wge4pdrchmidh; Type: FK CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE resource.resourceitemxclassification
    ADD CONSTRAINT fkgjx0hpxxegn5wge4pdrchmidh FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 5108 (class 2606 OID 209523)
-- Name: resourceitemtype fkgxtvsg5e3c8sq9j7y8bx0ib5v; Type: FK CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE resource.resourceitemtype
    ADD CONSTRAINT fkgxtvsg5e3c8sq9j7y8bx0ib5v FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 5109 (class 2606 OID 209528)
-- Name: resourceitemtype fkh8avb7iv2w72vgk90sh1d2p4f; Type: FK CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE resource.resourceitemtype
    ADD CONSTRAINT fkh8avb7iv2w72vgk90sh1d2p4f FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 5085 (class 2606 OID 209533)
-- Name: resourceitemdatasecuritytoken fkhcwacfqh36t292fibcvgdtr3m; Type: FK CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE resource.resourceitemdatasecuritytoken
    ADD CONSTRAINT fkhcwacfqh36t292fibcvgdtr3m FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 5086 (class 2606 OID 209538)
-- Name: resourceitemdatasecuritytoken fkhpb19eadjrkws43r16fgy3mjl; Type: FK CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE resource.resourceitemdatasecuritytoken
    ADD CONSTRAINT fkhpb19eadjrkws43r16fgy3mjl FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 5080 (class 2606 OID 209543)
-- Name: resourceitemdata fkht0uy53uvuomcxp1m9d76l2f2; Type: FK CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE resource.resourceitemdata
    ADD CONSTRAINT fkht0uy53uvuomcxp1m9d76l2f2 FOREIGN KEY (resourceitemid, warehousecreateddate) REFERENCES resource.resourceitem (resourceitemid, warehousecreateddate);


--
-- TOC entry 5152 (class 2606 OID 209548)
-- Name: resourceitemxresourceitemtypesecuritytoken fki8ap9tegp5e5e2687a8rlolq3; Type: FK CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE resource.resourceitemxresourceitemtypesecuritytoken
    ADD CONSTRAINT fki8ap9tegp5e5e2687a8rlolq3 FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 5110 (class 2606 OID 209553)
-- Name: resourceitemtype fki9pjt06s4hopvhur99agga4k6; Type: FK CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE resource.resourceitemtype
    ADD CONSTRAINT fki9pjt06s4hopvhur99agga4k6 FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 5091 (class 2606 OID 209558)
-- Name: resourceitemdataxclassification fkiaq7u3ym056htigwqw14ilcsn; Type: FK CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE resource.resourceitemdataxclassification
    ADD CONSTRAINT fkiaq7u3ym056htigwqw14ilcsn FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 5081 (class 2606 OID 209563)
-- Name: resourceitemdata fkigdv809qbjivrueu1pbkvr9j3; Type: FK CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE resource.resourceitemdata
    ADD CONSTRAINT fkigdv809qbjivrueu1pbkvr9j3 FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 5153 (class 2606 OID 209568)
-- Name: resourceitemxresourceitemtypesecuritytoken fkiwc4nixnum6cvlrtlpopjlrrp; Type: FK CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE resource.resourceitemxresourceitemtypesecuritytoken
    ADD CONSTRAINT fkiwc4nixnum6cvlrtlpopjlrrp FOREIGN KEY (resourceitemxresourceitemtypeid, warehousecreateddate) REFERENCES resource.resourceitemxresourceitemtype (resourceitemxresourceitemtypeid, warehousecreateddate);


--
-- TOC entry 5121 (class 2606 OID 209573)
-- Name: resourceitemxclassification fkjrhfr20kvirghjffwvd80r39m; Type: FK CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE resource.resourceitemxclassification
    ADD CONSTRAINT fkjrhfr20kvirghjffwvd80r39m FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 5092 (class 2606 OID 209578)
-- Name: resourceitemdataxclassification fkjsk9t2sreiwpv88hanu3khr74; Type: FK CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE resource.resourceitemdataxclassification
    ADD CONSTRAINT fkjsk9t2sreiwpv88hanu3khr74 FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 5154 (class 2606 OID 209583)
-- Name: resourceitemxresourceitemtypesecuritytoken fkkj0hvw87ec6j3mlt4m96ky91p; Type: FK CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE resource.resourceitemxresourceitemtypesecuritytoken
    ADD CONSTRAINT fkkj0hvw87ec6j3mlt4m96ky91p FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 5087 (class 2606 OID 209588)
-- Name: resourceitemdatasecuritytoken fkll81jjs16jumy0yll1ql0c21; Type: FK CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE resource.resourceitemdatasecuritytoken
    ADD CONSTRAINT fkll81jjs16jumy0yll1ql0c21 FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 5104 (class 2606 OID 209593)
-- Name: resourceitemsecuritytoken fklri3qjyh3vwi43xe3crxm8xdq; Type: FK CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE resource.resourceitemsecuritytoken
    ADD CONSTRAINT fklri3qjyh3vwi43xe3crxm8xdq FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 5144 (class 2606 OID 209598)
-- Name: resourceitemxresourceitemtype fklsy82htwl5f124qkuxb5cuq8y; Type: FK CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE resource.resourceitemxresourceitemtype
    ADD CONSTRAINT fklsy82htwl5f124qkuxb5cuq8y FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 5140 (class 2606 OID 209603)
-- Name: resourceitemxresourceitemsecuritytoken fkm9ls44frfnxy77f2yitjmfb4w; Type: FK CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE resource.resourceitemxresourceitemsecuritytoken
    ADD CONSTRAINT fkm9ls44frfnxy77f2yitjmfb4w FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 5114 (class 2606 OID 209608)
-- Name: resourceitemtypesecuritytoken fkml0sk2cyf2m3s7w1idm5b1fv2; Type: FK CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE resource.resourceitemtypesecuritytoken
    ADD CONSTRAINT fkml0sk2cyf2m3s7w1idm5b1fv2 FOREIGN KEY (securitytokenid, warehousecreateddate) REFERENCES security.securitytoken (securitytokenid, warehousecreateddate);


--
-- TOC entry 5141 (class 2606 OID 209613)
-- Name: resourceitemxresourceitemsecuritytoken fkmylvnsbdyx59j60lktojr074f; Type: FK CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE resource.resourceitemxresourceitemsecuritytoken
    ADD CONSTRAINT fkmylvnsbdyx59j60lktojr074f FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 5126 (class 2606 OID 209618)
-- Name: resourceitemxclassificationsecuritytoken fknvnmjioardv2gat6o4shntlxd; Type: FK CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE resource.resourceitemxclassificationsecuritytoken
    ADD CONSTRAINT fknvnmjioardv2gat6o4shntlxd FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 5105 (class 2606 OID 209623)
-- Name: resourceitemsecuritytoken fko0923krxdcp1issfcfcl7a0a7; Type: FK CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE resource.resourceitemsecuritytoken
    ADD CONSTRAINT fko0923krxdcp1issfcfcl7a0a7 FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 5122 (class 2606 OID 209628)
-- Name: resourceitemxclassification fko1fubrmjfvsi58rt1amf7u7fw; Type: FK CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE resource.resourceitemxclassification
    ADD CONSTRAINT fko1fubrmjfvsi58rt1amf7u7fw FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 5115 (class 2606 OID 209633)
-- Name: resourceitemtypesecuritytoken fko2usew1i3g60p11uwfuv2ajmd; Type: FK CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE resource.resourceitemtypesecuritytoken
    ADD CONSTRAINT fko2usew1i3g60p11uwfuv2ajmd FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 5127 (class 2606 OID 209638)
-- Name: resourceitemxclassificationsecuritytoken fko7a0ts75p7f0b5ekl3e5nr2jx; Type: FK CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE resource.resourceitemxclassificationsecuritytoken
    ADD CONSTRAINT fko7a0ts75p7f0b5ekl3e5nr2jx FOREIGN KEY (resourceitemxclassificationid, warehousecreateddate) REFERENCES resource.resourceitemxclassification (resourceitemxclassificationid, warehousecreateddate);


--
-- TOC entry 5082 (class 2606 OID 209643)
-- Name: resourceitemdata fkole6gj7jyg1pdvt918naap8n5; Type: FK CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE resource.resourceitemdata
    ADD CONSTRAINT fkole6gj7jyg1pdvt918naap8n5 FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 5088 (class 2606 OID 209648)
-- Name: resourceitemdatasecuritytoken fkomv4xy8vibg1lxewrtq0lj42t; Type: FK CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE resource.resourceitemdatasecuritytoken
    ADD CONSTRAINT fkomv4xy8vibg1lxewrtq0lj42t FOREIGN KEY (securitytokenid, warehousecreateddate) REFERENCES security.securitytoken (securitytokenid, warehousecreateddate);


--
-- TOC entry 5093 (class 2606 OID 209653)
-- Name: resourceitemdataxclassification fkox913icfxl6rbqwjw04y7odq6; Type: FK CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE resource.resourceitemdataxclassification
    ADD CONSTRAINT fkox913icfxl6rbqwjw04y7odq6 FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 5135 (class 2606 OID 209658)
-- Name: resourceitemxresourceitem fkoyv9alf1u6nf5wbkwvj207pd6; Type: FK CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE resource.resourceitemxresourceitem
    ADD CONSTRAINT fkoyv9alf1u6nf5wbkwvj207pd6 FOREIGN KEY (classificationid, warehousecreateddate) REFERENCES classification.classification (classificationid, warehousecreateddate);


--
-- TOC entry 5094 (class 2606 OID 209663)
-- Name: resourceitemdataxclassification fkp9oe45dlyale0o4ug986voncj; Type: FK CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE resource.resourceitemdataxclassification
    ADD CONSTRAINT fkp9oe45dlyale0o4ug986voncj FOREIGN KEY (classificationid, warehousecreateddate) REFERENCES classification.classification (classificationid, warehousecreateddate);


--
-- TOC entry 5145 (class 2606 OID 209668)
-- Name: resourceitemxresourceitemtype fkpirwchdhynd810k5mp231e2sh; Type: FK CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE resource.resourceitemxresourceitemtype
    ADD CONSTRAINT fkpirwchdhynd810k5mp231e2sh FOREIGN KEY (resourceitemtypeid, warehousecreateddate) REFERENCES resource.resourceitemtype (resourceitemtypeid, warehousecreateddate);


--
-- TOC entry 5128 (class 2606 OID 209673)
-- Name: resourceitemxclassificationsecuritytoken fkq4lxbn9s7559mqng8hmhau4s4; Type: FK CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE resource.resourceitemxclassificationsecuritytoken
    ADD CONSTRAINT fkq4lxbn9s7559mqng8hmhau4s4 FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 5146 (class 2606 OID 209678)
-- Name: resourceitemxresourceitemtype fkqbn3rldlgiftql2sxfoc020k7; Type: FK CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE resource.resourceitemxresourceitemtype
    ADD CONSTRAINT fkqbn3rldlgiftql2sxfoc020k7 FOREIGN KEY (classificationid, warehousecreateddate) REFERENCES classification.classification (classificationid, warehousecreateddate);


--
-- TOC entry 5147 (class 2606 OID 209683)
-- Name: resourceitemxresourceitemtype fkr19kv8ueg4bkxh2pvj156qvwk; Type: FK CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE resource.resourceitemxresourceitemtype
    ADD CONSTRAINT fkr19kv8ueg4bkxh2pvj156qvwk FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 5116 (class 2606 OID 209688)
-- Name: resourceitemtypesecuritytoken fkrpcr2w5s8sdyuh9e5bsiqpsl; Type: FK CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE resource.resourceitemtypesecuritytoken
    ADD CONSTRAINT fkrpcr2w5s8sdyuh9e5bsiqpsl FOREIGN KEY (resourceitemtypeid, warehousecreateddate) REFERENCES resource.resourceitemtype (resourceitemtypeid, warehousecreateddate);


--
-- TOC entry 5148 (class 2606 OID 209693)
-- Name: resourceitemxresourceitemtype fksw4ntk83q20lccxb0t56uvvun; Type: FK CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE resource.resourceitemxresourceitemtype
    ADD CONSTRAINT fksw4ntk83q20lccxb0t56uvvun FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 5106 (class 2606 OID 209698)
-- Name: resourceitemsecuritytoken fktj25xftljow24gev8hl3dsjw8; Type: FK CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE resource.resourceitemsecuritytoken
    ADD CONSTRAINT fktj25xftljow24gev8hl3dsjw8 FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 5165 (class 2606 OID 209703)
-- Name: rulestype fk11qj2su8f2sy9nvcjss76yfqm; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE rules.rulestype
    ADD CONSTRAINT fk11qj2su8f2sy9nvcjss76yfqm FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 5169 (class 2606 OID 209708)
-- Name: rulestypessecuritytoken fk12fsootndlsrm2gkytfg2gfw0; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE rules.rulestypessecuritytoken
    ADD CONSTRAINT fk12fsootndlsrm2gkytfg2gfw0 FOREIGN KEY (securitytokenid, warehousecreateddate) REFERENCES security.securitytoken (securitytokenid, warehousecreateddate);


--
-- TOC entry 5251 (class 2606 OID 209713)
-- Name: rulesxresourceitem fk19cj3ortt8x7wa4mnkohkpebi; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE rules.rulesxresourceitem
    ADD CONSTRAINT fk19cj3ortt8x7wa4mnkohkpebi FOREIGN KEY (rulesid, warehousecreateddate) REFERENCES rules.rules (rulesid, warehousecreateddate);


--
-- TOC entry 5245 (class 2606 OID 209718)
-- Name: rulesxproductsecuritytoken fk1inr52n9q3ax5rhqtjhthh78r; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE rules.rulesxproductsecuritytoken
    ADD CONSTRAINT fk1inr52n9q3ax5rhqtjhthh78r FOREIGN KEY (rulesxproductid, warehousecreateddate) REFERENCES rules.rulesxproduct (rulesxproductid, warehousecreateddate);


--
-- TOC entry 5258 (class 2606 OID 209723)
-- Name: rulesxresourceitemsecuritytoken fk23yuc0ofnmca2ig2dd1wx5ol8; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE rules.rulesxresourceitemsecuritytoken
    ADD CONSTRAINT fk23yuc0ofnmca2ig2dd1wx5ol8 FOREIGN KEY (rulesxresourceitemid, warehousecreateddate) REFERENCES rules.rulesxresourceitem (rulesxresourceitemid, warehousecreateddate);


--
-- TOC entry 5207 (class 2606 OID 209728)
-- Name: rulesxarrangementssecuritytoken fk2xvs1pi7rc32lemnyvj01j301; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE rules.rulesxarrangementssecuritytoken
    ADD CONSTRAINT fk2xvs1pi7rc32lemnyvj01j301 FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 5264 (class 2606 OID 209733)
-- Name: rulesxrules fk31s09rv16v0yhe5aac7l6ele3; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE rules.rulesxrules
    ADD CONSTRAINT fk31s09rv16v0yhe5aac7l6ele3 FOREIGN KEY (classificationid, warehousecreateddate) REFERENCES classification.classification (classificationid, warehousecreateddate);


--
-- TOC entry 5265 (class 2606 OID 209738)
-- Name: rulesxrules fk37pin4ub4l14xqqanw037awej; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE rules.rulesxrules
    ADD CONSTRAINT fk37pin4ub4l14xqqanw037awej FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 5213 (class 2606 OID 209743)
-- Name: rulesxclassification fk38k4xit5ty330vs01iv18sikh; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE rules.rulesxclassification
    ADD CONSTRAINT fk38k4xit5ty330vs01iv18sikh FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 5225 (class 2606 OID 209748)
-- Name: rulesxinvolvedparty fk3djrrw59r13qf53lxmls4da7g; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE rules.rulesxinvolvedparty
    ADD CONSTRAINT fk3djrrw59r13qf53lxmls4da7g FOREIGN KEY (involvedpartyid, warehousecreateddate) REFERENCES party.involvedparty (involvedpartyid, warehousecreateddate);


--
-- TOC entry 5175 (class 2606 OID 209753)
-- Name: rulestypexclassification fk3fotawtun2be400jw1c5mcxr5; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE rules.rulestypexclassification
    ADD CONSTRAINT fk3fotawtun2be400jw1c5mcxr5 FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 5181 (class 2606 OID 209758)
-- Name: rulestypexclassificationsecuritytoken fk3nsh18vueud5iybj4xk2t4j6r; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE rules.rulestypexclassificationsecuritytoken
    ADD CONSTRAINT fk3nsh18vueud5iybj4xk2t4j6r FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 5232 (class 2606 OID 209763)
-- Name: rulesxinvolvedpartysecuritytoken fk3qdk5vxgo4txkaaj5a8y8giwd; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE rules.rulesxinvolvedpartysecuritytoken
    ADD CONSTRAINT fk3qdk5vxgo4txkaaj5a8y8giwd FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 5252 (class 2606 OID 209768)
-- Name: rulesxresourceitem fk41dmnx4ijklqr44tlpomg7it5; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE rules.rulesxresourceitem
    ADD CONSTRAINT fk41dmnx4ijklqr44tlpomg7it5 FOREIGN KEY (classificationid, warehousecreateddate) REFERENCES classification.classification (classificationid, warehousecreateddate);


--
-- TOC entry 5208 (class 2606 OID 209773)
-- Name: rulesxarrangementssecuritytoken fk4dxpxc1hj2n1ik4s3ms69w3kw; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE rules.rulesxarrangementssecuritytoken
    ADD CONSTRAINT fk4dxpxc1hj2n1ik4s3ms69w3kw FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 5246 (class 2606 OID 209778)
-- Name: rulesxproductsecuritytoken fk4g0qn2ufofwya9mua7k1bip3n; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE rules.rulesxproductsecuritytoken
    ADD CONSTRAINT fk4g0qn2ufofwya9mua7k1bip3n FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 5271 (class 2606 OID 209783)
-- Name: rulesxrulessecuritytoken fk4kklxff6jc1n1bo39g3sdqrix; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE rules.rulesxrulessecuritytoken
    ADD CONSTRAINT fk4kklxff6jc1n1bo39g3sdqrix FOREIGN KEY (rulesxrulesid, warehousecreateddate) REFERENCES rules.rulesxrules (rulesxrulesid, warehousecreateddate);


--
-- TOC entry 5200 (class 2606 OID 209788)
-- Name: rulesxarrangement fk4nj9r8fu6b1ymd9o8tdryxrtx; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE rules.rulesxarrangement
    ADD CONSTRAINT fk4nj9r8fu6b1ymd9o8tdryxrtx FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 5182 (class 2606 OID 209793)
-- Name: rulestypexclassificationsecuritytoken fk4rylunbc8qipcw0n020nhab5b; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE rules.rulestypexclassificationsecuritytoken
    ADD CONSTRAINT fk4rylunbc8qipcw0n020nhab5b FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 5247 (class 2606 OID 209798)
-- Name: rulesxproductsecuritytoken fk50a41rvtkyewefjmf4e8l52sw; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE rules.rulesxproductsecuritytoken
    ADD CONSTRAINT fk50a41rvtkyewefjmf4e8l52sw FOREIGN KEY (securitytokenid, warehousecreateddate) REFERENCES security.securitytoken (securitytokenid, warehousecreateddate);


--
-- TOC entry 5159 (class 2606 OID 209803)
-- Name: rulessecuritytoken fk57dd0fcy24o8k2spnqrykgk2a; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE rules.rulessecuritytoken
    ADD CONSTRAINT fk57dd0fcy24o8k2spnqrykgk2a FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 5238 (class 2606 OID 209808)
-- Name: rulesxproduct fk57dubv678gaj1eatqjof0x0nb; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE rules.rulesxproduct
    ADD CONSTRAINT fk57dubv678gaj1eatqjof0x0nb FOREIGN KEY (classificationid, warehousecreateddate) REFERENCES classification.classification (classificationid, warehousecreateddate);


--
-- TOC entry 5259 (class 2606 OID 209813)
-- Name: rulesxresourceitemsecuritytoken fk5dv1vwep6v9958vjdse4henw0; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE rules.rulesxresourceitemsecuritytoken
    ADD CONSTRAINT fk5dv1vwep6v9958vjdse4henw0 FOREIGN KEY (securitytokenid, warehousecreateddate) REFERENCES security.securitytoken (securitytokenid, warehousecreateddate);


--
-- TOC entry 5253 (class 2606 OID 209818)
-- Name: rulesxresourceitem fk5fe2nqc5oe2o0pqnptrrciyra; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE rules.rulesxresourceitem
    ADD CONSTRAINT fk5fe2nqc5oe2o0pqnptrrciyra FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 5160 (class 2606 OID 209823)
-- Name: rulessecuritytoken fk5hjfvbnd3js59ca3fowm88h6t; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE rules.rulessecuritytoken
    ADD CONSTRAINT fk5hjfvbnd3js59ca3fowm88h6t FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 5239 (class 2606 OID 209828)
-- Name: rulesxproduct fk5m9wrb374b5g1wpwkj3jr09y6; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE rules.rulesxproduct
    ADD CONSTRAINT fk5m9wrb374b5g1wpwkj3jr09y6 FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 5170 (class 2606 OID 209833)
-- Name: rulestypessecuritytoken fk5tck1lwdgo3i3lbwyphxlsilw; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE rules.rulestypessecuritytoken
    ADD CONSTRAINT fk5tck1lwdgo3i3lbwyphxlsilw FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 5226 (class 2606 OID 209838)
-- Name: rulesxinvolvedparty fk5ujdw4x8upupcaml412wan0p7; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE rules.rulesxinvolvedparty
    ADD CONSTRAINT fk5ujdw4x8upupcaml412wan0p7 FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 5227 (class 2606 OID 209843)
-- Name: rulesxinvolvedparty fk611oa399omtv631o66ausvb55; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE rules.rulesxinvolvedparty
    ADD CONSTRAINT fk611oa399omtv631o66ausvb55 FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 5240 (class 2606 OID 209848)
-- Name: rulesxproduct fk6a018wimk1mn1ucjsg7m6v42e; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE rules.rulesxproduct
    ADD CONSTRAINT fk6a018wimk1mn1ucjsg7m6v42e FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 5187 (class 2606 OID 209853)
-- Name: rulestypexresourceitem fk6hivvn41g61n8v941egrae19i; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE rules.rulestypexresourceitem
    ADD CONSTRAINT fk6hivvn41g61n8v941egrae19i FOREIGN KEY (rulestypeid, warehousecreateddate) REFERENCES rules.rulestype (rulestypeid, warehousecreateddate);


--
-- TOC entry 5277 (class 2606 OID 209858)
-- Name: rulesxrulestype fk710wj6aj8es1ht1o9fxdehtua; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE rules.rulesxrulestype
    ADD CONSTRAINT fk710wj6aj8es1ht1o9fxdehtua FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 5188 (class 2606 OID 209863)
-- Name: rulestypexresourceitem fk7an7eg5j3seoaxajls3knmrwt; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE rules.rulestypexresourceitem
    ADD CONSTRAINT fk7an7eg5j3seoaxajls3knmrwt FOREIGN KEY (classificationid, warehousecreateddate) REFERENCES classification.classification (classificationid, warehousecreateddate);


--
-- TOC entry 5201 (class 2606 OID 209868)
-- Name: rulesxarrangement fk7kbnsdojwukxxly9vxqkraun2; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE rules.rulesxarrangement
    ADD CONSTRAINT fk7kbnsdojwukxxly9vxqkraun2 FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 5202 (class 2606 OID 209873)
-- Name: rulesxarrangement fk7kfpfoyfd5mn1x45215dlee28; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE rules.rulesxarrangement
    ADD CONSTRAINT fk7kfpfoyfd5mn1x45215dlee28 FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 5233 (class 2606 OID 209878)
-- Name: rulesxinvolvedpartysecuritytoken fk7lvh9pa4gweufl5x0ubtl38my; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE rules.rulesxinvolvedpartysecuritytoken
    ADD CONSTRAINT fk7lvh9pa4gweufl5x0ubtl38my FOREIGN KEY (securitytokenid, warehousecreateddate) REFERENCES security.securitytoken (securitytokenid, warehousecreateddate);


--
-- TOC entry 5214 (class 2606 OID 209883)
-- Name: rulesxclassification fk7nyvj635fp8si7pq0qetkfhpb; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE rules.rulesxclassification
    ADD CONSTRAINT fk7nyvj635fp8si7pq0qetkfhpb FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 5166 (class 2606 OID 209888)
-- Name: rulestype fk7v51o36ciw802dj5m82giq97q; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE rules.rulestype
    ADD CONSTRAINT fk7v51o36ciw802dj5m82giq97q FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 5176 (class 2606 OID 209893)
-- Name: rulestypexclassification fk87mojdqprestm5uqmw925bqf1; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE rules.rulestypexclassification
    ADD CONSTRAINT fk87mojdqprestm5uqmw925bqf1 FOREIGN KEY (rulestypeid, warehousecreateddate) REFERENCES rules.rulestype (rulestypeid, warehousecreateddate);


--
-- TOC entry 5278 (class 2606 OID 209898)
-- Name: rulesxrulestype fk8hvei78rfy1byxtb36lwwst06; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE rules.rulesxrulestype
    ADD CONSTRAINT fk8hvei78rfy1byxtb36lwwst06 FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 5228 (class 2606 OID 209903)
-- Name: rulesxinvolvedparty fk8i91cawihcqkxe2fr9oawrshw; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE rules.rulesxinvolvedparty
    ADD CONSTRAINT fk8i91cawihcqkxe2fr9oawrshw FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 5215 (class 2606 OID 209908)
-- Name: rulesxclassification fk8txg0h4hi98qbgslaodff7dyt; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE rules.rulesxclassification
    ADD CONSTRAINT fk8txg0h4hi98qbgslaodff7dyt FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 5194 (class 2606 OID 209913)
-- Name: rulestypexresourceitemsecuritytoken fk8xs3tib17edep24d44482bmjp; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE rules.rulestypexresourceitemsecuritytoken
    ADD CONSTRAINT fk8xs3tib17edep24d44482bmjp FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 5234 (class 2606 OID 209918)
-- Name: rulesxinvolvedpartysecuritytoken fk903el1e8i3wxjag53njlscvkg; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE rules.rulesxinvolvedpartysecuritytoken
    ADD CONSTRAINT fk903el1e8i3wxjag53njlscvkg FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 5177 (class 2606 OID 209923)
-- Name: rulestypexclassification fk9662odo6kpnj3a66uyqqh9gph; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE rules.rulestypexclassification
    ADD CONSTRAINT fk9662odo6kpnj3a66uyqqh9gph FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 5171 (class 2606 OID 209928)
-- Name: rulestypessecuritytoken fk9aexqcupg3q8il9en4moulmo1; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE rules.rulestypessecuritytoken
    ADD CONSTRAINT fk9aexqcupg3q8il9en4moulmo1 FOREIGN KEY (rulestypesid, warehousecreateddate) REFERENCES rules.rulestype (rulestypeid, warehousecreateddate);


--
-- TOC entry 5203 (class 2606 OID 213896)
-- Name: rulesxarrangement fk9gfetl178gc0bjfak6tny0keu; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE rules.rulesxarrangement
    ADD CONSTRAINT fk9gfetl178gc0bjfak6tny0keu FOREIGN KEY (arrangementid, warehousecreateddate) REFERENCES arrangement.arrangement (arrangementid, warehousecreateddate);


--
-- TOC entry 5241 (class 2606 OID 209933)
-- Name: rulesxproduct fk9t2smyfxndgfnuoht0l8q42rb; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE rules.rulesxproduct
    ADD CONSTRAINT fk9t2smyfxndgfnuoht0l8q42rb FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 5248 (class 2606 OID 209938)
-- Name: rulesxproductsecuritytoken fka1ai0uri46j0quxeqcikkdclv; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE rules.rulesxproductsecuritytoken
    ADD CONSTRAINT fka1ai0uri46j0quxeqcikkdclv FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 5183 (class 2606 OID 209943)
-- Name: rulestypexclassificationsecuritytoken fkamvwb2lq6eh8s3gobs9amn1ns; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE rules.rulestypexclassificationsecuritytoken
    ADD CONSTRAINT fkamvwb2lq6eh8s3gobs9amn1ns FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 5219 (class 2606 OID 209948)
-- Name: rulesxclassificationsecuritytoken fkb0vp45d3tkxwvy5938ua9n809; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE rules.rulesxclassificationsecuritytoken
    ADD CONSTRAINT fkb0vp45d3tkxwvy5938ua9n809 FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 5216 (class 2606 OID 209953)
-- Name: rulesxclassification fkbbvhygyarc5j4gi7wjnd052p3; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE rules.rulesxclassification
    ADD CONSTRAINT fkbbvhygyarc5j4gi7wjnd052p3 FOREIGN KEY (rulesid, warehousecreateddate) REFERENCES rules.rules (rulesid, warehousecreateddate);


--
-- TOC entry 5284 (class 2606 OID 209958)
-- Name: rulesxrulestypesecuritytoken fkbn0acfejqvxbal40304ibsbr5; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE rules.rulesxrulestypesecuritytoken
    ADD CONSTRAINT fkbn0acfejqvxbal40304ibsbr5 FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 5279 (class 2606 OID 209963)
-- Name: rulesxrulestype fkbn4oo9asbe39dle7ow6buqvt7; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE rules.rulesxrulestype
    ADD CONSTRAINT fkbn4oo9asbe39dle7ow6buqvt7 FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 5280 (class 2606 OID 209968)
-- Name: rulesxrulestype fkbohku2sim3tc8ey86rq8psqpt; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE rules.rulesxrulestype
    ADD CONSTRAINT fkbohku2sim3tc8ey86rq8psqpt FOREIGN KEY (rulesid, warehousecreateddate) REFERENCES rules.rules (rulesid, warehousecreateddate);


--
-- TOC entry 5155 (class 2606 OID 209973)
-- Name: rules fkbpbgfk1w8xjpcvla2wtr28rfm; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE rules.rules
    ADD CONSTRAINT fkbpbgfk1w8xjpcvla2wtr28rfm FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 5285 (class 2606 OID 209978)
-- Name: rulesxrulestypesecuritytoken fkby3adp3oxnvi7q571ij36jj0v; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE rules.rulesxrulestypesecuritytoken
    ADD CONSTRAINT fkby3adp3oxnvi7q571ij36jj0v FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 5195 (class 2606 OID 209983)
-- Name: rulestypexresourceitemsecuritytoken fkcdu908ybhdghbjvhquhpfucsr; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE rules.rulestypexresourceitemsecuritytoken
    ADD CONSTRAINT fkcdu908ybhdghbjvhquhpfucsr FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 5178 (class 2606 OID 209988)
-- Name: rulestypexclassification fkcmpyykrmpk1epxo9jmicem78i; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE rules.rulestypexclassification
    ADD CONSTRAINT fkcmpyykrmpk1epxo9jmicem78i FOREIGN KEY (classificationid, warehousecreateddate) REFERENCES classification.classification (classificationid, warehousecreateddate);


--
-- TOC entry 5184 (class 2606 OID 209993)
-- Name: rulestypexclassificationsecuritytoken fkcn088ejtlnauiohbrpvvufegu; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE rules.rulestypexclassificationsecuritytoken
    ADD CONSTRAINT fkcn088ejtlnauiohbrpvvufegu FOREIGN KEY (securitytokenid, warehousecreateddate) REFERENCES security.securitytoken (securitytokenid, warehousecreateddate);


--
-- TOC entry 5209 (class 2606 OID 209998)
-- Name: rulesxarrangementssecuritytoken fkd1ylioumsfoo6scfb0fdouur7; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE rules.rulesxarrangementssecuritytoken
    ADD CONSTRAINT fkd1ylioumsfoo6scfb0fdouur7 FOREIGN KEY (securitytokenid, warehousecreateddate) REFERENCES security.securitytoken (securitytokenid, warehousecreateddate);


--
-- TOC entry 5179 (class 2606 OID 210003)
-- Name: rulestypexclassification fkdck5vk5rjw51e9u8ducstsysg; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE rules.rulestypexclassification
    ADD CONSTRAINT fkdck5vk5rjw51e9u8ducstsysg FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 5281 (class 2606 OID 210008)
-- Name: rulesxrulestype fkdly04cjef5yj3ne8v1uwb8h12; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE rules.rulesxrulestype
    ADD CONSTRAINT fkdly04cjef5yj3ne8v1uwb8h12 FOREIGN KEY (classificationid, warehousecreateddate) REFERENCES classification.classification (classificationid, warehousecreateddate);


--
-- TOC entry 5172 (class 2606 OID 210013)
-- Name: rulestypessecuritytoken fkeciu87p8mk0yiel2t1bwipg9a; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE rules.rulestypessecuritytoken
    ADD CONSTRAINT fkeciu87p8mk0yiel2t1bwipg9a FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 5161 (class 2606 OID 210018)
-- Name: rulessecuritytoken fkenyc4oy7p7bpluwr2incgf2m6; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE rules.rulessecuritytoken
    ADD CONSTRAINT fkenyc4oy7p7bpluwr2incgf2m6 FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 5204 (class 2606 OID 210023)
-- Name: rulesxarrangement fkeu7bn1gb6dg7xt097ognxjwh5; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE rules.rulesxarrangement
    ADD CONSTRAINT fkeu7bn1gb6dg7xt097ognxjwh5 FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 5242 (class 2606 OID 210028)
-- Name: rulesxproduct fkf3t7xp42nph7ypmy38rtkr9c8; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE rules.rulesxproduct
    ADD CONSTRAINT fkf3t7xp42nph7ypmy38rtkr9c8 FOREIGN KEY (rulesid, warehousecreateddate) REFERENCES rules.rules (rulesid, warehousecreateddate);


--
-- TOC entry 5167 (class 2606 OID 210033)
-- Name: rulestype fkf7030hfrlt8o2dxdarmt5ftrp; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE rules.rulestype
    ADD CONSTRAINT fkf7030hfrlt8o2dxdarmt5ftrp FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 5205 (class 2606 OID 210038)
-- Name: rulesxarrangement fkfj9ondul2lkfugd0u6vdxq1yi; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE rules.rulesxarrangement
    ADD CONSTRAINT fkfj9ondul2lkfugd0u6vdxq1yi FOREIGN KEY (rulesid, warehousecreateddate) REFERENCES rules.rules (rulesid, warehousecreateddate);


--
-- TOC entry 5210 (class 2606 OID 210043)
-- Name: rulesxarrangementssecuritytoken fkflgh5an92uoxffkehd7g7tp8j; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE rules.rulesxarrangementssecuritytoken
    ADD CONSTRAINT fkflgh5an92uoxffkehd7g7tp8j FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 5272 (class 2606 OID 210048)
-- Name: rulesxrulessecuritytoken fkfv9sfhe1kdjwa8dgu136k90ut; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE rules.rulesxrulessecuritytoken
    ADD CONSTRAINT fkfv9sfhe1kdjwa8dgu136k90ut FOREIGN KEY (securitytokenid, warehousecreateddate) REFERENCES security.securitytoken (securitytokenid, warehousecreateddate);


--
-- TOC entry 5235 (class 2606 OID 210053)
-- Name: rulesxinvolvedpartysecuritytoken fkg0ntb8lcpw9n6p49kpubuyle2; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE rules.rulesxinvolvedpartysecuritytoken
    ADD CONSTRAINT fkg0ntb8lcpw9n6p49kpubuyle2 FOREIGN KEY (rulesxinvolvedpartyid, warehousecreateddate) REFERENCES rules.rulesxinvolvedparty (rulesxinvolvedpartyid, warehousecreateddate);


--
-- TOC entry 5249 (class 2606 OID 210058)
-- Name: rulesxproductsecuritytoken fkgenrresjjjhv02rh0slya5d88; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE rules.rulesxproductsecuritytoken
    ADD CONSTRAINT fkgenrresjjjhv02rh0slya5d88 FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 5266 (class 2606 OID 210063)
-- Name: rulesxrules fkgivavwvb91qbin4sxepd6wbs5; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE rules.rulesxrules
    ADD CONSTRAINT fkgivavwvb91qbin4sxepd6wbs5 FOREIGN KEY (parentrulesid, warehousecreateddate) REFERENCES rules.rules (rulesid, warehousecreateddate);


--
-- TOC entry 5254 (class 2606 OID 210068)
-- Name: rulesxresourceitem fkhubiaglxhl6fy8gx1oei33qbc; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE rules.rulesxresourceitem
    ADD CONSTRAINT fkhubiaglxhl6fy8gx1oei33qbc FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 5286 (class 2606 OID 210073)
-- Name: rulesxrulestypesecuritytoken fkibc3w3kdwgur69j3lhbi6l0k5; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE rules.rulesxrulestypesecuritytoken
    ADD CONSTRAINT fkibc3w3kdwgur69j3lhbi6l0k5 FOREIGN KEY (securitytokenid, warehousecreateddate) REFERENCES security.securitytoken (securitytokenid, warehousecreateddate);


--
-- TOC entry 5173 (class 2606 OID 210078)
-- Name: rulestypessecuritytoken fkic2121k0p1edg44c55hog6oe6; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE rules.rulestypessecuritytoken
    ADD CONSTRAINT fkic2121k0p1edg44c55hog6oe6 FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 5156 (class 2606 OID 210083)
-- Name: rules fkihoftg6akowf7mjyheyfmty0i; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE rules.rules
    ADD CONSTRAINT fkihoftg6akowf7mjyheyfmty0i FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 5217 (class 2606 OID 210088)
-- Name: rulesxclassification fkiyjrx7w183fwo2nhmwpqh0stw; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE rules.rulesxclassification
    ADD CONSTRAINT fkiyjrx7w183fwo2nhmwpqh0stw FOREIGN KEY (classificationid, warehousecreateddate) REFERENCES classification.classification (classificationid, warehousecreateddate);


--
-- TOC entry 5220 (class 2606 OID 210093)
-- Name: rulesxclassificationsecuritytoken fkj60pj71vbrlqc7ijopnpprev7; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE rules.rulesxclassificationsecuritytoken
    ADD CONSTRAINT fkj60pj71vbrlqc7ijopnpprev7 FOREIGN KEY (securitytokenid, warehousecreateddate) REFERENCES security.securitytoken (securitytokenid, warehousecreateddate);


--
-- TOC entry 5229 (class 2606 OID 210098)
-- Name: rulesxinvolvedparty fkj8719e5kkmd7kd8bpqr71rt3a; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE rules.rulesxinvolvedparty
    ADD CONSTRAINT fkj8719e5kkmd7kd8bpqr71rt3a FOREIGN KEY (classificationid, warehousecreateddate) REFERENCES classification.classification (classificationid, warehousecreateddate);


--
-- TOC entry 5260 (class 2606 OID 210103)
-- Name: rulesxresourceitemsecuritytoken fkji8ugnof4vkss37iu1mfuy2l4; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE rules.rulesxresourceitemsecuritytoken
    ADD CONSTRAINT fkji8ugnof4vkss37iu1mfuy2l4 FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 5221 (class 2606 OID 210108)
-- Name: rulesxclassificationsecuritytoken fkjklxc6ukplqhj7wa03ah80qh0; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE rules.rulesxclassificationsecuritytoken
    ADD CONSTRAINT fkjklxc6ukplqhj7wa03ah80qh0 FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 5287 (class 2606 OID 210113)
-- Name: rulesxrulestypesecuritytoken fkjr2flixl5hw7uqmh9chynqt4h; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE rules.rulesxrulestypesecuritytoken
    ADD CONSTRAINT fkjr2flixl5hw7uqmh9chynqt4h FOREIGN KEY (rulesxrulestypeid, warehousecreateddate) REFERENCES rules.rulesxrulestype (rulesxrulestypeid, warehousecreateddate);


--
-- TOC entry 5236 (class 2606 OID 210118)
-- Name: rulesxinvolvedpartysecuritytoken fkju1w7gcf2bu52ok8ibtd40gk4; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE rules.rulesxinvolvedpartysecuritytoken
    ADD CONSTRAINT fkju1w7gcf2bu52ok8ibtd40gk4 FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 5261 (class 2606 OID 210123)
-- Name: rulesxresourceitemsecuritytoken fkju938jbh27xnvuokmjxlnlqos; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE rules.rulesxresourceitemsecuritytoken
    ADD CONSTRAINT fkju938jbh27xnvuokmjxlnlqos FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 5189 (class 2606 OID 210128)
-- Name: rulestypexresourceitem fkkl9dib27d8h3g00pp40q3vpw4; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE rules.rulestypexresourceitem
    ADD CONSTRAINT fkkl9dib27d8h3g00pp40q3vpw4 FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 5180 (class 2606 OID 210133)
-- Name: rulestypexclassification fkl2497yt06thxeacoue8jbbaoh; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE rules.rulestypexclassification
    ADD CONSTRAINT fkl2497yt06thxeacoue8jbbaoh FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 5250 (class 2606 OID 210138)
-- Name: rulesxproductsecuritytoken fklaeiodujthn91pwnj8qag1ao6; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE rules.rulesxproductsecuritytoken
    ADD CONSTRAINT fklaeiodujthn91pwnj8qag1ao6 FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 5273 (class 2606 OID 210143)
-- Name: rulesxrulessecuritytoken fklbxxoxpoelx3t2p1g96nqv90q; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE rules.rulesxrulessecuritytoken
    ADD CONSTRAINT fklbxxoxpoelx3t2p1g96nqv90q FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 5196 (class 2606 OID 210148)
-- Name: rulestypexresourceitemsecuritytoken fklhnbp8n1wy13okhhtnxwqu5hy; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE rules.rulestypexresourceitemsecuritytoken
    ADD CONSTRAINT fklhnbp8n1wy13okhhtnxwqu5hy FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 5218 (class 2606 OID 210153)
-- Name: rulesxclassification fklog5hewe9u4c7wb35u4xvv84c; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE rules.rulesxclassification
    ADD CONSTRAINT fklog5hewe9u4c7wb35u4xvv84c FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 5288 (class 2606 OID 210158)
-- Name: rulesxrulestypesecuritytoken fkm2rtvaw8pdy1l3rvbc9wicom3; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE rules.rulesxrulestypesecuritytoken
    ADD CONSTRAINT fkm2rtvaw8pdy1l3rvbc9wicom3 FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 5197 (class 2606 OID 210163)
-- Name: rulestypexresourceitemsecuritytoken fkm6jgd6lrdywbn56w6e0s5awd1; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE rules.rulestypexresourceitemsecuritytoken
    ADD CONSTRAINT fkm6jgd6lrdywbn56w6e0s5awd1 FOREIGN KEY (securitytokenid, warehousecreateddate) REFERENCES security.securitytoken (securitytokenid, warehousecreateddate);


--
-- TOC entry 5168 (class 2606 OID 210168)
-- Name: rulestype fkm7jdyuyikwo9qlfe6qae0bv3q; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE rules.rulestype
    ADD CONSTRAINT fkm7jdyuyikwo9qlfe6qae0bv3q FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 5185 (class 2606 OID 210173)
-- Name: rulestypexclassificationsecuritytoken fkmjbouj2e7loyrlu87avtm4gtm; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE rules.rulestypexclassificationsecuritytoken
    ADD CONSTRAINT fkmjbouj2e7loyrlu87avtm4gtm FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 5222 (class 2606 OID 210178)
-- Name: rulesxclassificationsecuritytoken fkmno9p3edeufft6bgiv94tecpa; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE rules.rulesxclassificationsecuritytoken
    ADD CONSTRAINT fkmno9p3edeufft6bgiv94tecpa FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 5162 (class 2606 OID 210183)
-- Name: rulessecuritytoken fkmsl2uqlxr7q5n3bq6e14nknk; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE rules.rulessecuritytoken
    ADD CONSTRAINT fkmsl2uqlxr7q5n3bq6e14nknk FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 5289 (class 2606 OID 210188)
-- Name: rulesxrulestypesecuritytoken fkmst7g4r9cabb1d78ysr4hvppe; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE rules.rulesxrulestypesecuritytoken
    ADD CONSTRAINT fkmst7g4r9cabb1d78ysr4hvppe FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 5163 (class 2606 OID 210193)
-- Name: rulessecuritytoken fkmtk7w2kpskec0ebnwnrmo8rmp; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE rules.rulessecuritytoken
    ADD CONSTRAINT fkmtk7w2kpskec0ebnwnrmo8rmp FOREIGN KEY (rulesid, warehousecreateddate) REFERENCES rules.rules (rulesid, warehousecreateddate);


--
-- TOC entry 5157 (class 2606 OID 210198)
-- Name: rules fknh64xmjpd3alokexjnuqr0unw; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE rules.rules
    ADD CONSTRAINT fknh64xmjpd3alokexjnuqr0unw FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 5164 (class 2606 OID 210203)
-- Name: rulessecuritytoken fknj1ss0ctynt0u0t9nx5wprick; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE rules.rulessecuritytoken
    ADD CONSTRAINT fknj1ss0ctynt0u0t9nx5wprick FOREIGN KEY (securitytokenid, warehousecreateddate) REFERENCES security.securitytoken (securitytokenid, warehousecreateddate);


--
-- TOC entry 5186 (class 2606 OID 210208)
-- Name: rulestypexclassificationsecuritytoken fknn81qs2b5rstr3u748lvjbl61; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE rules.rulestypexclassificationsecuritytoken
    ADD CONSTRAINT fknn81qs2b5rstr3u748lvjbl61 FOREIGN KEY (rulestypexclassificationid, warehousecreateddate) REFERENCES rules.rulestypexclassification (rulestypexclassificationid, warehousecreateddate);


--
-- TOC entry 5274 (class 2606 OID 210213)
-- Name: rulesxrulessecuritytoken fknwhg52uwedumbo0s6msu4kvu4; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE rules.rulesxrulessecuritytoken
    ADD CONSTRAINT fknwhg52uwedumbo0s6msu4kvu4 FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 5190 (class 2606 OID 210218)
-- Name: rulestypexresourceitem fko8drrcbtv7to8utrafqb942a; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE rules.rulestypexresourceitem
    ADD CONSTRAINT fko8drrcbtv7to8utrafqb942a FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 5275 (class 2606 OID 210223)
-- Name: rulesxrulessecuritytoken fkohvvumprn80d89qp41tyeesd3; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE rules.rulesxrulessecuritytoken
    ADD CONSTRAINT fkohvvumprn80d89qp41tyeesd3 FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 5282 (class 2606 OID 210228)
-- Name: rulesxrulestype fkonxshkblvrhnt4fasmfxmnyrf; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE rules.rulesxrulestype
    ADD CONSTRAINT fkonxshkblvrhnt4fasmfxmnyrf FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 5198 (class 2606 OID 210233)
-- Name: rulestypexresourceitemsecuritytoken fkotlbdpur4kwiwymopsenwumbg; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE rules.rulestypexresourceitemsecuritytoken
    ADD CONSTRAINT fkotlbdpur4kwiwymopsenwumbg FOREIGN KEY (rulestypexresourceitemid, warehousecreateddate) REFERENCES rules.rulestypexresourceitem (rulestypexresourceitemid, warehousecreateddate);


--
-- TOC entry 5223 (class 2606 OID 210238)
-- Name: rulesxclassificationsecuritytoken fkowvdqrq0fiqefy3g85fj60rgq; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE rules.rulesxclassificationsecuritytoken
    ADD CONSTRAINT fkowvdqrq0fiqefy3g85fj60rgq FOREIGN KEY (rulesxclassificationid, warehousecreateddate) REFERENCES rules.rulesxclassification (rulesxclassificationid, warehousecreateddate);


--
-- TOC entry 5267 (class 2606 OID 210243)
-- Name: rulesxrules fkpahnm53g0d7kjo5tfrask49up; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE rules.rulesxrules
    ADD CONSTRAINT fkpahnm53g0d7kjo5tfrask49up FOREIGN KEY (childrulesid, warehousecreateddate) REFERENCES rules.rules (rulesid, warehousecreateddate);


--
-- TOC entry 5224 (class 2606 OID 210248)
-- Name: rulesxclassificationsecuritytoken fkq49rw6kbi9gykntudl1mdodl7; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE rules.rulesxclassificationsecuritytoken
    ADD CONSTRAINT fkq49rw6kbi9gykntudl1mdodl7 FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 5230 (class 2606 OID 210253)
-- Name: rulesxinvolvedparty fkqcugfk4tgg63qe2e7fk4h1jou; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE rules.rulesxinvolvedparty
    ADD CONSTRAINT fkqcugfk4tgg63qe2e7fk4h1jou FOREIGN KEY (rulesid, warehousecreateddate) REFERENCES rules.rules (rulesid, warehousecreateddate);


--
-- TOC entry 5158 (class 2606 OID 210258)
-- Name: rules fkqkhxbrmhfk0ou8sfgrq6w04yn; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE rules.rules
    ADD CONSTRAINT fkqkhxbrmhfk0ou8sfgrq6w04yn FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 5268 (class 2606 OID 210263)
-- Name: rulesxrules fkqmjt8nooh7ch2vr14euld3bnk; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE rules.rulesxrules
    ADD CONSTRAINT fkqmjt8nooh7ch2vr14euld3bnk FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 5191 (class 2606 OID 210268)
-- Name: rulestypexresourceitem fkqygy5t9dhj5r3nefikdhaxdb6; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE rules.rulestypexresourceitem
    ADD CONSTRAINT fkqygy5t9dhj5r3nefikdhaxdb6 FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 5255 (class 2606 OID 210273)
-- Name: rulesxresourceitem fkr1sg6ky0tcfxt3faiqbpvq72o; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE rules.rulesxresourceitem
    ADD CONSTRAINT fkr1sg6ky0tcfxt3faiqbpvq72o FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 5243 (class 2606 OID 210278)
-- Name: rulesxproduct fkr9hc797wqs7k8u8iun1psqhi0; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE rules.rulesxproduct
    ADD CONSTRAINT fkr9hc797wqs7k8u8iun1psqhi0 FOREIGN KEY (productid, warehousecreateddate) REFERENCES product.product (productid, warehousecreateddate);


--
-- TOC entry 5199 (class 2606 OID 210283)
-- Name: rulestypexresourceitemsecuritytoken fkrbpyq0298hcisaevbl3mp0m5d; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE rules.rulestypexresourceitemsecuritytoken
    ADD CONSTRAINT fkrbpyq0298hcisaevbl3mp0m5d FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 5192 (class 2606 OID 210288)
-- Name: rulestypexresourceitem fkrdh9f21yu45alpi37mrexx5sy; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE rules.rulestypexresourceitem
    ADD CONSTRAINT fkrdh9f21yu45alpi37mrexx5sy FOREIGN KEY (resourceitemid, warehousecreateddate) REFERENCES resource.resourceitem (resourceitemid, warehousecreateddate);


--
-- TOC entry 5206 (class 2606 OID 210293)
-- Name: rulesxarrangement fkrkfqm429ef4j7jwneu7jxeye4; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE rules.rulesxarrangement
    ADD CONSTRAINT fkrkfqm429ef4j7jwneu7jxeye4 FOREIGN KEY (classificationid, warehousecreateddate) REFERENCES classification.classification (classificationid, warehousecreateddate);


--
-- TOC entry 5269 (class 2606 OID 210298)
-- Name: rulesxrules fkrncrmhaj7pk6audwswshmudqj; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE rules.rulesxrules
    ADD CONSTRAINT fkrncrmhaj7pk6audwswshmudqj FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 5283 (class 2606 OID 210303)
-- Name: rulesxrulestype fkrv7w522dypyy838vgcr1r566e; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE rules.rulesxrulestype
    ADD CONSTRAINT fkrv7w522dypyy838vgcr1r566e FOREIGN KEY (rulestypeid, warehousecreateddate) REFERENCES rules.rulestype (rulestypeid, warehousecreateddate);


--
-- TOC entry 5244 (class 2606 OID 210308)
-- Name: rulesxproduct fkrwm05r7gimvp4itjeem7u6sk6; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE rules.rulesxproduct
    ADD CONSTRAINT fkrwm05r7gimvp4itjeem7u6sk6 FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 5276 (class 2606 OID 210313)
-- Name: rulesxrulessecuritytoken fks16mt02lac7ao524fhu5liiys; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE rules.rulesxrulessecuritytoken
    ADD CONSTRAINT fks16mt02lac7ao524fhu5liiys FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 5211 (class 2606 OID 210318)
-- Name: rulesxarrangementssecuritytoken fks5w3o4wg63awg0iuc76km8e1n; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE rules.rulesxarrangementssecuritytoken
    ADD CONSTRAINT fks5w3o4wg63awg0iuc76km8e1n FOREIGN KEY (rulesxarrangementsid, warehousecreateddate) REFERENCES rules.rulesxarrangement (rulesxarrangementsid, warehousecreateddate);


--
-- TOC entry 5174 (class 2606 OID 210323)
-- Name: rulestypessecuritytoken fks5xpq0ecpqefbfuwqkymvncsn; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE rules.rulestypessecuritytoken
    ADD CONSTRAINT fks5xpq0ecpqefbfuwqkymvncsn FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 5193 (class 2606 OID 210328)
-- Name: rulestypexresourceitem fksbm8nex7yud5ymui9t5m2wuiw; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE rules.rulestypexresourceitem
    ADD CONSTRAINT fksbm8nex7yud5ymui9t5m2wuiw FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 5262 (class 2606 OID 210333)
-- Name: rulesxresourceitemsecuritytoken fksm3jfs67p3ly7mjl1jkrualak; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE rules.rulesxresourceitemsecuritytoken
    ADD CONSTRAINT fksm3jfs67p3ly7mjl1jkrualak FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 5231 (class 2606 OID 210338)
-- Name: rulesxinvolvedparty fksqp0envk694667mbnl8hndily; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE rules.rulesxinvolvedparty
    ADD CONSTRAINT fksqp0envk694667mbnl8hndily FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 5270 (class 2606 OID 210343)
-- Name: rulesxrules fkt5ep7dex0ykbws23o3yk8sctu; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE rules.rulesxrules
    ADD CONSTRAINT fkt5ep7dex0ykbws23o3yk8sctu FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 5212 (class 2606 OID 210348)
-- Name: rulesxarrangementssecuritytoken fkte09hu3a7vurplykt3w97714k; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE rules.rulesxarrangementssecuritytoken
    ADD CONSTRAINT fkte09hu3a7vurplykt3w97714k FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 5263 (class 2606 OID 210353)
-- Name: rulesxresourceitemsecuritytoken fktlm1dpqcyom9yo1lefe0yqowj; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE rules.rulesxresourceitemsecuritytoken
    ADD CONSTRAINT fktlm1dpqcyom9yo1lefe0yqowj FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 5256 (class 2606 OID 210358)
-- Name: rulesxresourceitem fktlnk6dgimet9xmad6ajypb5xh; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE rules.rulesxresourceitem
    ADD CONSTRAINT fktlnk6dgimet9xmad6ajypb5xh FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 5237 (class 2606 OID 210363)
-- Name: rulesxinvolvedpartysecuritytoken fkunriof269jly3lkmqk8jsp4g; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE rules.rulesxinvolvedpartysecuritytoken
    ADD CONSTRAINT fkunriof269jly3lkmqk8jsp4g FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 5257 (class 2606 OID 210368)
-- Name: rulesxresourceitem fkvs83cy05gattyxwtir1yyv2s; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE rules.rulesxresourceitem
    ADD CONSTRAINT fkvs83cy05gattyxwtir1yyv2s FOREIGN KEY (resourceitemid, warehousecreateddate) REFERENCES resource.resourceitem (resourceitemid, warehousecreateddate);


--
-- TOC entry 5313 (class 2606 OID 210373)
-- Name: securitytokenxclassificationsecuritytoken fk23j9r000kvoovvj3lpurf7lro; Type: FK CONSTRAINT; Schema: security; Owner: postgres
--

ALTER TABLE security.securitytokenxclassificationsecuritytoken
    ADD CONSTRAINT fk23j9r000kvoovvj3lpurf7lro FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 5290 (class 2606 OID 210378)
-- Name: securitytoken fk3dojhs6jjsp6g4xtwg1gfsg7i; Type: FK CONSTRAINT; Schema: security; Owner: postgres
--

ALTER TABLE security.securitytoken
    ADD CONSTRAINT fk3dojhs6jjsp6g4xtwg1gfsg7i FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 5314 (class 2606 OID 210383)
-- Name: securitytokenxclassificationsecuritytoken fk7b8h1egk35meq3khexlt58p04; Type: FK CONSTRAINT; Schema: security; Owner: postgres
--

ALTER TABLE security.securitytokenxclassificationsecuritytoken
    ADD CONSTRAINT fk7b8h1egk35meq3khexlt58p04 FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 5295 (class 2606 OID 210388)
-- Name: securitytokenssecuritytoken fk7d5c4foji5ocaal45ulv0uaxl; Type: FK CONSTRAINT; Schema: security; Owner: postgres
--

ALTER TABLE security.securitytokenssecuritytoken
    ADD CONSTRAINT fk7d5c4foji5ocaal45ulv0uaxl FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 5291 (class 2606 OID 210393)
-- Name: securitytoken fk7oknl73tuuwwt63nkgdcm4abq; Type: FK CONSTRAINT; Schema: security; Owner: postgres
--

ALTER TABLE security.securitytoken
    ADD CONSTRAINT fk7oknl73tuuwwt63nkgdcm4abq FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 5301 (class 2606 OID 210398)
-- Name: securitytokensxsecuritytokensecuritytoken fk7thwp5xsmuj0tlkpvobud9um7; Type: FK CONSTRAINT; Schema: security; Owner: postgres
--

ALTER TABLE security.securitytokensxsecuritytokensecuritytoken
    ADD CONSTRAINT fk7thwp5xsmuj0tlkpvobud9um7 FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 5302 (class 2606 OID 210403)
-- Name: securitytokensxsecuritytokensecuritytoken fk8ts5kdhf2u71ss5se0eydoyx5; Type: FK CONSTRAINT; Schema: security; Owner: postgres
--

ALTER TABLE security.securitytokensxsecuritytokensecuritytoken
    ADD CONSTRAINT fk8ts5kdhf2u71ss5se0eydoyx5 FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 5319 (class 2606 OID 210408)
-- Name: securitytokenxsecuritytoken fkaa10y63not65gkhd88hn83qke; Type: FK CONSTRAINT; Schema: security; Owner: postgres
--

ALTER TABLE security.securitytokenxsecuritytoken
    ADD CONSTRAINT fkaa10y63not65gkhd88hn83qke FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 5303 (class 2606 OID 210413)
-- Name: securitytokensxsecuritytokensecuritytoken fkamoh9xumd90l12fw52p4t2lr1; Type: FK CONSTRAINT; Schema: security; Owner: postgres
--

ALTER TABLE security.securitytokensxsecuritytokensecuritytoken
    ADD CONSTRAINT fkamoh9xumd90l12fw52p4t2lr1 FOREIGN KEY (securitytokenid, warehousecreateddate) REFERENCES security.securitytoken (securitytokenid, warehousecreateddate);


--
-- TOC entry 5307 (class 2606 OID 210418)
-- Name: securitytokenxclassification fkbeh76kxkmk54rmc3f6m4wsjaf; Type: FK CONSTRAINT; Schema: security; Owner: postgres
--

ALTER TABLE security.securitytokenxclassification
    ADD CONSTRAINT fkbeh76kxkmk54rmc3f6m4wsjaf FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 5320 (class 2606 OID 210423)
-- Name: securitytokenxsecuritytoken fkbi1t0uojqlur84bdht26o1b9a; Type: FK CONSTRAINT; Schema: security; Owner: postgres
--

ALTER TABLE security.securitytokenxsecuritytoken
    ADD CONSTRAINT fkbi1t0uojqlur84bdht26o1b9a FOREIGN KEY (childsecuritytokenid, warehousecreateddate) REFERENCES security.securitytoken (securitytokenid, warehousecreateddate);


--
-- TOC entry 5296 (class 2606 OID 210428)
-- Name: securitytokenssecuritytoken fkbohmlwb2yuw6vjbohxmmuhu2u; Type: FK CONSTRAINT; Schema: security; Owner: postgres
--

ALTER TABLE security.securitytokenssecuritytoken
    ADD CONSTRAINT fkbohmlwb2yuw6vjbohxmmuhu2u FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 5315 (class 2606 OID 210433)
-- Name: securitytokenxclassificationsecuritytoken fkctvl0v696371fygrxpr48xfh6; Type: FK CONSTRAINT; Schema: security; Owner: postgres
--

ALTER TABLE security.securitytokenxclassificationsecuritytoken
    ADD CONSTRAINT fkctvl0v696371fygrxpr48xfh6 FOREIGN KEY (securitytokenid, warehousecreateddate) REFERENCES security.securitytoken (securitytokenid, warehousecreateddate);


--
-- TOC entry 5292 (class 2606 OID 210438)
-- Name: securitytoken fkdxgncdf26cjimvl7qyfqcpv5l; Type: FK CONSTRAINT; Schema: security; Owner: postgres
--

ALTER TABLE security.securitytoken
    ADD CONSTRAINT fkdxgncdf26cjimvl7qyfqcpv5l FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 5316 (class 2606 OID 210443)
-- Name: securitytokenxclassificationsecuritytoken fke3gxxm2i6buv2rvlr17em38aj; Type: FK CONSTRAINT; Schema: security; Owner: postgres
--

ALTER TABLE security.securitytokenxclassificationsecuritytoken
    ADD CONSTRAINT fke3gxxm2i6buv2rvlr17em38aj FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 5308 (class 2606 OID 210448)
-- Name: securitytokenxclassification fkf0ve8e1o43mpjqbkv0eor80ks; Type: FK CONSTRAINT; Schema: security; Owner: postgres
--

ALTER TABLE security.securitytokenxclassification
    ADD CONSTRAINT fkf0ve8e1o43mpjqbkv0eor80ks FOREIGN KEY (classificationid, warehousecreateddate) REFERENCES classification.classification (classificationid, warehousecreateddate);


--
-- TOC entry 5321 (class 2606 OID 210453)
-- Name: securitytokenxsecuritytoken fkfcmwpt8n1j6p4l9y51o4s4b99; Type: FK CONSTRAINT; Schema: security; Owner: postgres
--

ALTER TABLE security.securitytokenxsecuritytoken
    ADD CONSTRAINT fkfcmwpt8n1j6p4l9y51o4s4b99 FOREIGN KEY (classificationid, warehousecreateddate) REFERENCES classification.classification (classificationid, warehousecreateddate);


--
-- TOC entry 5293 (class 2606 OID 210458)
-- Name: securitytoken fkg5dnn3ygqgg2j80h4ijca6kdp; Type: FK CONSTRAINT; Schema: security; Owner: postgres
--

ALTER TABLE security.securitytoken
    ADD CONSTRAINT fkg5dnn3ygqgg2j80h4ijca6kdp FOREIGN KEY (securitytokenclassificationid, warehousecreateddate) REFERENCES classification.classification (classificationid, warehousecreateddate);


--
-- TOC entry 5297 (class 2606 OID 210463)
-- Name: securitytokenssecuritytoken fkgm9ke6kkuxlw3dm67iseaofu8; Type: FK CONSTRAINT; Schema: security; Owner: postgres
--

ALTER TABLE security.securitytokenssecuritytoken
    ADD CONSTRAINT fkgm9ke6kkuxlw3dm67iseaofu8 FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 5322 (class 2606 OID 210468)
-- Name: securitytokenxsecuritytoken fkh8wabwfov1k3ah9r2jmm11fg8; Type: FK CONSTRAINT; Schema: security; Owner: postgres
--

ALTER TABLE security.securitytokenxsecuritytoken
    ADD CONSTRAINT fkh8wabwfov1k3ah9r2jmm11fg8 FOREIGN KEY (parentsecuritytokenid, warehousecreateddate) REFERENCES security.securitytoken (securitytokenid, warehousecreateddate);


--
-- TOC entry 5323 (class 2606 OID 210473)
-- Name: securitytokenxsecuritytoken fkix4kxmf6f9elii179h4fv9opk; Type: FK CONSTRAINT; Schema: security; Owner: postgres
--

ALTER TABLE security.securitytokenxsecuritytoken
    ADD CONSTRAINT fkix4kxmf6f9elii179h4fv9opk FOREIGN KEY (activeflagid, warehousecreateddate) REFERENCES dbo.activeflag (activeflagid, warehousecreateddate);


--
-- TOC entry 5309 (class 2606 OID 210478)
-- Name: securitytokenxclassification fkiy9192vtwu1t1tucigro1dhbr; Type: FK CONSTRAINT; Schema: security; Owner: postgres
--

ALTER TABLE security.securitytokenxclassification
    ADD CONSTRAINT fkiy9192vtwu1t1tucigro1dhbr FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 5324 (class 2606 OID 210483)
-- Name: securitytokenxsecuritytoken fkkhrctfxn8jbkmugeuq13or9y2; Type: FK CONSTRAINT; Schema: security; Owner: postgres
--

ALTER TABLE security.securitytokenxsecuritytoken
    ADD CONSTRAINT fkkhrctfxn8jbkmugeuq13or9y2 FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 5310 (class 2606 OID 210488)
-- Name: securitytokenxclassification fkkpec2fqi1saqvfqyim4qibvhc; Type: FK CONSTRAINT; Schema: security; Owner: postgres
--

ALTER TABLE security.securitytokenxclassification
    ADD CONSTRAINT fkkpec2fqi1saqvfqyim4qibvhc FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 5325 (class 2606 OID 210493)
-- Name: securitytokenxsecuritytoken fklyogqrmsivjjwwmqyhs3w8eey; Type: FK CONSTRAINT; Schema: security; Owner: postgres
--

ALTER TABLE security.securitytokenxsecuritytoken
    ADD CONSTRAINT fklyogqrmsivjjwwmqyhs3w8eey FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 5317 (class 2606 OID 210498)
-- Name: securitytokenxclassificationsecuritytoken fkmvagw1psni1acp68r86p98326; Type: FK CONSTRAINT; Schema: security; Owner: postgres
--

ALTER TABLE security.securitytokenxclassificationsecuritytoken
    ADD CONSTRAINT fkmvagw1psni1acp68r86p98326 FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 5298 (class 2606 OID 210503)
-- Name: securitytokenssecuritytoken fkn5jt48ip0t79bbac760wnpk4u; Type: FK CONSTRAINT; Schema: security; Owner: postgres
--

ALTER TABLE security.securitytokenssecuritytoken
    ADD CONSTRAINT fkn5jt48ip0t79bbac760wnpk4u FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 5304 (class 2606 OID 210508)
-- Name: securitytokensxsecuritytokensecuritytoken fkr9dd79mvxphy35j0l30f60wjo; Type: FK CONSTRAINT; Schema: security; Owner: postgres
--

ALTER TABLE security.securitytokensxsecuritytokensecuritytoken
    ADD CONSTRAINT fkr9dd79mvxphy35j0l30f60wjo FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 5305 (class 2606 OID 210513)
-- Name: securitytokensxsecuritytokensecuritytoken fkraqvstjiad8yqfg3be6ae1e1r; Type: FK CONSTRAINT; Schema: security; Owner: postgres
--

ALTER TABLE security.securitytokensxsecuritytokensecuritytoken
    ADD CONSTRAINT fkraqvstjiad8yqfg3be6ae1e1r FOREIGN KEY (systemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 5311 (class 2606 OID 210518)
-- Name: securitytokenxclassification fkrct8kx4cxa9ys52h7kp02yda0; Type: FK CONSTRAINT; Schema: security; Owner: postgres
--

ALTER TABLE security.securitytokenxclassification
    ADD CONSTRAINT fkrct8kx4cxa9ys52h7kp02yda0 FOREIGN KEY (originalsourcesystemid, warehousecreateddate) REFERENCES dbo.systems (systemid, warehousecreateddate);


--
-- TOC entry 5318 (class 2606 OID 210523)
-- Name: securitytokenxclassificationsecuritytoken fkrde1i5gxqtmm8pxu5g9lwepd2; Type: FK CONSTRAINT; Schema: security; Owner: postgres
--

ALTER TABLE security.securitytokenxclassificationsecuritytoken
    ADD CONSTRAINT fkrde1i5gxqtmm8pxu5g9lwepd2 FOREIGN KEY (securitytokenxclassificationid, warehousecreateddate) REFERENCES security.securitytokenxclassification (securitytokenxclassificationid, warehousecreateddate);


--
-- TOC entry 5306 (class 2606 OID 210528)
-- Name: securitytokensxsecuritytokensecuritytoken fks48ohap0caelpi8nu60sp55i6; Type: FK CONSTRAINT; Schema: security; Owner: postgres
--

ALTER TABLE security.securitytokensxsecuritytokensecuritytoken
    ADD CONSTRAINT fks48ohap0caelpi8nu60sp55i6 FOREIGN KEY (securitytokenxsecuritytokenid, warehousecreateddate) REFERENCES security.securitytokenxsecuritytoken (securitytokenxsecuritytokenid, warehousecreateddate);


--
-- TOC entry 5299 (class 2606 OID 210533)
-- Name: securitytokenssecuritytoken fksoxo8an11ilt212h41x4c9ofj; Type: FK CONSTRAINT; Schema: security; Owner: postgres
--

ALTER TABLE security.securitytokenssecuritytoken
    ADD CONSTRAINT fksoxo8an11ilt212h41x4c9ofj FOREIGN KEY (securitytokenid, warehousecreateddate) REFERENCES security.securitytoken (securitytokenid, warehousecreateddate);


--
-- TOC entry 5294 (class 2606 OID 210538)
-- Name: securitytoken fksqp2hjm3vhxqy62do1twwtrmf; Type: FK CONSTRAINT; Schema: security; Owner: postgres
--

ALTER TABLE security.securitytoken
    ADD CONSTRAINT fksqp2hjm3vhxqy62do1twwtrmf FOREIGN KEY (enterpriseid, warehousecreateddate) REFERENCES dbo.enterprise (enterpriseid, warehousecreateddate);


--
-- TOC entry 5312 (class 2606 OID 210543)
-- Name: securitytokenxclassification fktis44ugn011fdb281jb5onjsv; Type: FK CONSTRAINT; Schema: security; Owner: postgres
--

ALTER TABLE security.securitytokenxclassification
    ADD CONSTRAINT fktis44ugn011fdb281jb5onjsv FOREIGN KEY (securitytokenid, warehousecreateddate) REFERENCES security.securitytoken (securitytokenid, warehousecreateddate);


--
-- TOC entry 5300 (class 2606 OID 210548)
-- Name: securitytokenssecuritytoken fkujfypgbk5b3c90rftiwmwv0m; Type: FK CONSTRAINT; Schema: security; Owner: postgres
--

ALTER TABLE security.securitytokenssecuritytoken
    ADD CONSTRAINT fkujfypgbk5b3c90rftiwmwv0m FOREIGN KEY (securitytokentoid, warehousecreateddate) REFERENCES security.securitytoken (securitytokenid, warehousecreateddate);


--
-- TOC entry 5326 (class 2606 OID 210553)
-- Name: days fk4g0xilxoif8vy1brjn2vpdpcw; Type: FK CONSTRAINT; Schema: time; Owner: postgres
--

ALTER TABLE "time".days
    ADD CONSTRAINT fk4g0xilxoif8vy1brjn2vpdpcw FOREIGN KEY (daynameid) REFERENCES "time".daynames (daynameid);


--
-- TOC entry 5331 (class 2606 OID 210558)
-- Name: months fk6sf1pjb60myyfca1rj11q2l9r; Type: FK CONSTRAINT; Schema: time; Owner: postgres
--

ALTER TABLE "time".months
    ADD CONSTRAINT fk6sf1pjb60myyfca1rj11q2l9r FOREIGN KEY (monthofyearid) REFERENCES "time".monthofyear (monthofyearid);


--
-- TOC entry 5329 (class 2606 OID 210563)
-- Name: halfhourdayparts fk8n2nuitlg5wf0pb64jbncrdfa; Type: FK CONSTRAINT; Schema: time; Owner: postgres
--

ALTER TABLE "time".halfhourdayparts
    ADD CONSTRAINT fk8n2nuitlg5wf0pb64jbncrdfa FOREIGN KEY (daypartid) REFERENCES "time".dayparts (daypartid);


--
-- TOC entry 5332 (class 2606 OID 210568)
-- Name: months fka234h7trk9pm9pqry7hc2dfac; Type: FK CONSTRAINT; Schema: time; Owner: postgres
--

ALTER TABLE "time".months
    ADD CONSTRAINT fka234h7trk9pm9pqry7hc2dfac FOREIGN KEY (quarterid) REFERENCES "time".quarters (quarterid);


--
-- TOC entry 5327 (class 2606 OID 210573)
-- Name: days fkcnplvut6yx0iu2itr0q11oa6l; Type: FK CONSTRAINT; Schema: time; Owner: postgres
--

ALTER TABLE "time".days
    ADD CONSTRAINT fkcnplvut6yx0iu2itr0q11oa6l FOREIGN KEY (weekid) REFERENCES "time".weeks (weekid);


--
-- TOC entry 5334 (class 2606 OID 210578)
-- Name: time fkejnxl506r8u5bm8fds1lu09h5; Type: FK CONSTRAINT; Schema: time; Owner: postgres
--

ALTER TABLE "time"."time"
    ADD CONSTRAINT fkejnxl506r8u5bm8fds1lu09h5 FOREIGN KEY (hourid) REFERENCES "time".hours (hourid);


--
-- TOC entry 5328 (class 2606 OID 210583)
-- Name: days fkgbuiu0f44kxjymtkhyj4vlgc2; Type: FK CONSTRAINT; Schema: time; Owner: postgres
--

ALTER TABLE "time".days
    ADD CONSTRAINT fkgbuiu0f44kxjymtkhyj4vlgc2 FOREIGN KEY (monthid) REFERENCES "time".months (monthid);


--
-- TOC entry 5333 (class 2606 OID 210588)
-- Name: quarters fkhb7po9kbr3vc89jkxgy4ik2e5; Type: FK CONSTRAINT; Schema: time; Owner: postgres
--

ALTER TABLE "time".quarters
    ADD CONSTRAINT fkhb7po9kbr3vc89jkxgy4ik2e5 FOREIGN KEY (yearid) REFERENCES "time".years (yearid);


--
-- TOC entry 5330 (class 2606 OID 210593)
-- Name: halfhours fkmsowgve3eajsco5ty4ud5lc2v; Type: FK CONSTRAINT; Schema: time; Owner: postgres
--

ALTER TABLE "time".halfhours
    ADD CONSTRAINT fkmsowgve3eajsco5ty4ud5lc2v FOREIGN KEY (hourid) REFERENCES "time".hours (hourid);
