--
-- PostgreSQL database dump
--

-- Dumped from database version 14.3
-- Dumped by pg_dump version 14.3

-- Started on 2022-05-21 06:01:53

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 6 (class 2615 OID 28280)
-- Name: address; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA address;


ALTER SCHEMA address OWNER TO postgres;

--
-- TOC entry 14 (class 2615 OID 28281)
-- Name: classification; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA classification;


ALTER SCHEMA classification OWNER TO postgres;

--
-- TOC entry 16 (class 2615 OID 28279)
-- Name: dbo; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA dbo;


ALTER SCHEMA dbo OWNER TO postgres;

--
-- TOC entry 5 (class 2615 OID 28282)
-- Name: event; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA event;


ALTER SCHEMA event OWNER TO postgres;

--
-- TOC entry 13 (class 2615 OID 28283)
-- Name: geography; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA geography;


ALTER SCHEMA geography OWNER TO postgres;

--
-- TOC entry 11 (class 2615 OID 28284)
-- Name: party; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA party;


ALTER SCHEMA party OWNER TO postgres;

--
-- TOC entry 7 (class 2615 OID 28285)
-- Name: product; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA product;


ALTER SCHEMA product OWNER TO postgres;

--
-- TOC entry 12 (class 2615 OID 28286)
-- Name: resource; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA resource;


ALTER SCHEMA resource OWNER TO postgres;

--
-- TOC entry 8 (class 2615 OID 28287)
-- Name: rules; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA rules;


ALTER SCHEMA rules OWNER TO postgres;

--
-- TOC entry 4 (class 2615 OID 28288)
-- Name: security; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA security;


ALTER SCHEMA security OWNER TO postgres;

--
-- TOC entry 10 (class 2615 OID 28289)
-- Name: time; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA "time";


ALTER SCHEMA "time" OWNER TO postgres;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 220 (class 1259 OID 28290)
-- Name: address; Type: TABLE; Schema: address; Owner: postgres
--

CREATE TABLE address.address (
    addressid character varying(36) NOT NULL,
    effectivefromdate timestamp(6) without time zone NOT NULL,
    effectivetodate timestamp(6) without time zone NOT NULL,
    warehousecreatedtimestamp timestamp(6) without time zone NOT NULL,
    warehouselastupdatedtimestamp timestamp(6) without time zone NOT NULL,
    originalsourcesystemuniqueid character varying(255) NOT NULL,
    value character varying(255) NOT NULL,
    activeflagid character varying(36) NOT NULL,
    enterpriseid character varying(36) NOT NULL,
    systemid character varying(36) NOT NULL,
    originalsourcesystemid character varying(36) NOT NULL,
    classificationid character varying(36) NOT NULL
);


ALTER TABLE address.address OWNER TO postgres;

--
-- TOC entry 221 (class 1259 OID 28297)
-- Name: addresssecuritytoken; Type: TABLE; Schema: address; Owner: postgres
--

CREATE TABLE address.addresssecuritytoken (
    addresssecuritytokenid character varying(36) NOT NULL,
    effectivefromdate timestamp(6) without time zone NOT NULL,
    effectivetodate timestamp(6) without time zone NOT NULL,
    warehousecreatedtimestamp timestamp(6) without time zone NOT NULL,
    warehouselastupdatedtimestamp timestamp(6) without time zone NOT NULL,
    createallowed integer NOT NULL,
    deleteallowed integer NOT NULL,
    originalsourcesystemuniqueid character varying(255) NOT NULL,
    readallowed integer NOT NULL,
    updateallowed integer NOT NULL,
    activeflagid character varying(36) NOT NULL,
    enterpriseid character varying(36) NOT NULL,
    originalsourcesystemid character varying(36) NOT NULL,
    securitytokenid character varying(36) NOT NULL,
    systemid character varying(36) NOT NULL,
    addressid character varying(36) NOT NULL
);


ALTER TABLE address.addresssecuritytoken OWNER TO postgres;

--
-- TOC entry 222 (class 1259 OID 28304)
-- Name: addressxclassification; Type: TABLE; Schema: address; Owner: postgres
--

CREATE TABLE address.addressxclassification (
    addressxclassificationid character varying(36) NOT NULL,
    effectivefromdate timestamp(6) without time zone NOT NULL,
    effectivetodate timestamp(6) without time zone NOT NULL,
    warehousecreatedtimestamp timestamp(6) without time zone NOT NULL,
    warehouselastupdatedtimestamp timestamp(6) without time zone NOT NULL,
    originalsourcesystemuniqueid character varying(255) NOT NULL,
    value character varying(255) NOT NULL,
    activeflagid character varying(36) NOT NULL,
    enterpriseid character varying(36) NOT NULL,
    systemid character varying(36) NOT NULL,
    originalsourcesystemid character varying(36) NOT NULL,
    classificationid character varying(36) NOT NULL,
    addressid character varying(36) NOT NULL
);


ALTER TABLE address.addressxclassification OWNER TO postgres;

--
-- TOC entry 223 (class 1259 OID 28311)
-- Name: addressxclassificationsecuritytoken; Type: TABLE; Schema: address; Owner: postgres
--

CREATE TABLE address.addressxclassificationsecuritytoken (
    addressxclassificationsecuritytokenid character varying(36) NOT NULL,
    effectivefromdate timestamp(6) without time zone NOT NULL,
    effectivetodate timestamp(6) without time zone NOT NULL,
    warehousecreatedtimestamp timestamp(6) without time zone NOT NULL,
    warehouselastupdatedtimestamp timestamp(6) without time zone NOT NULL,
    createallowed integer NOT NULL,
    deleteallowed integer NOT NULL,
    originalsourcesystemuniqueid character varying(255) NOT NULL,
    readallowed integer NOT NULL,
    updateallowed integer NOT NULL,
    activeflagid character varying(36) NOT NULL,
    enterpriseid character varying(36) NOT NULL,
    originalsourcesystemid character varying(36) NOT NULL,
    securitytokenid character varying(36) NOT NULL,
    systemid character varying(36) NOT NULL,
    addressxclassificationid character varying(36) NOT NULL
);


ALTER TABLE address.addressxclassificationsecuritytoken OWNER TO postgres;

--
-- TOC entry 224 (class 1259 OID 28318)
-- Name: addressxgeography; Type: TABLE; Schema: address; Owner: postgres
--

CREATE TABLE address.addressxgeography (
    addressxgeographyid character varying(36) NOT NULL,
    effectivefromdate timestamp(6) without time zone NOT NULL,
    effectivetodate timestamp(6) without time zone NOT NULL,
    warehousecreatedtimestamp timestamp(6) without time zone NOT NULL,
    warehouselastupdatedtimestamp timestamp(6) without time zone NOT NULL,
    originalsourcesystemuniqueid character varying(255) NOT NULL,
    value character varying(255) NOT NULL,
    activeflagid character varying(36) NOT NULL,
    enterpriseid character varying(36) NOT NULL,
    systemid character varying(36) NOT NULL,
    originalsourcesystemid character varying(36) NOT NULL,
    classificationid character varying(36) NOT NULL,
    addressid character varying(36) NOT NULL,
    geographyid character varying(36) NOT NULL
);


ALTER TABLE address.addressxgeography OWNER TO postgres;

--
-- TOC entry 225 (class 1259 OID 28325)
-- Name: addressxgeographysecuritytoken; Type: TABLE; Schema: address; Owner: postgres
--

CREATE TABLE address.addressxgeographysecuritytoken (
    addressxgeographysecuritytokenid character varying(36) NOT NULL,
    effectivefromdate timestamp(6) without time zone NOT NULL,
    effectivetodate timestamp(6) without time zone NOT NULL,
    warehousecreatedtimestamp timestamp(6) without time zone NOT NULL,
    warehouselastupdatedtimestamp timestamp(6) without time zone NOT NULL,
    createallowed integer NOT NULL,
    deleteallowed integer NOT NULL,
    originalsourcesystemuniqueid character varying(255) NOT NULL,
    readallowed integer NOT NULL,
    updateallowed integer NOT NULL,
    activeflagid character varying(36) NOT NULL,
    enterpriseid character varying(36) NOT NULL,
    originalsourcesystemid character varying(36) NOT NULL,
    securitytokenid character varying(36) NOT NULL,
    systemid character varying(36) NOT NULL,
    addressxgeographyid character varying(36) NOT NULL
);


ALTER TABLE address.addressxgeographysecuritytoken OWNER TO postgres;

--
-- TOC entry 226 (class 1259 OID 28332)
-- Name: addressxresourceitem; Type: TABLE; Schema: address; Owner: postgres
--

CREATE TABLE address.addressxresourceitem (
    addressxresourceitemid character varying(36) NOT NULL,
    effectivefromdate timestamp(6) without time zone NOT NULL,
    effectivetodate timestamp(6) without time zone NOT NULL,
    warehousecreatedtimestamp timestamp(6) without time zone NOT NULL,
    warehouselastupdatedtimestamp timestamp(6) without time zone NOT NULL,
    originalsourcesystemuniqueid character varying(255) NOT NULL,
    value character varying(255) NOT NULL,
    activeflagid character varying(36) NOT NULL,
    enterpriseid character varying(36) NOT NULL,
    systemid character varying(36) NOT NULL,
    originalsourcesystemid character varying(36) NOT NULL,
    classificationid character varying(36) NOT NULL,
    addressid character varying(36) NOT NULL,
    resourceitemid character varying(36) NOT NULL
);


ALTER TABLE address.addressxresourceitem OWNER TO postgres;

--
-- TOC entry 227 (class 1259 OID 28339)
-- Name: addressxresourceitemsecuritytoken; Type: TABLE; Schema: address; Owner: postgres
--

CREATE TABLE address.addressxresourceitemsecuritytoken (
    addressxresourceitemsecuritytokenid character varying(36) NOT NULL,
    effectivefromdate timestamp(6) without time zone NOT NULL,
    effectivetodate timestamp(6) without time zone NOT NULL,
    warehousecreatedtimestamp timestamp(6) without time zone NOT NULL,
    warehouselastupdatedtimestamp timestamp(6) without time zone NOT NULL,
    createallowed integer NOT NULL,
    deleteallowed integer NOT NULL,
    originalsourcesystemuniqueid character varying(255) NOT NULL,
    readallowed integer NOT NULL,
    updateallowed integer NOT NULL,
    activeflagid character varying(36) NOT NULL,
    enterpriseid character varying(36) NOT NULL,
    originalsourcesystemid character varying(36) NOT NULL,
    securitytokenid character varying(36) NOT NULL,
    systemid character varying(36) NOT NULL,
    addressxresourceitemid character varying(36) NOT NULL
);


ALTER TABLE address.addressxresourceitemsecuritytoken OWNER TO postgres;

--
-- TOC entry 228 (class 1259 OID 28346)
-- Name: classification; Type: TABLE; Schema: classification; Owner: postgres
--

CREATE TABLE classification.classification (
    classificationid character varying(36) NOT NULL,
    effectivefromdate timestamp(6) without time zone NOT NULL,
    effectivetodate timestamp(6) without time zone NOT NULL,
    warehousecreatedtimestamp timestamp(6) without time zone NOT NULL,
    warehouselastupdatedtimestamp timestamp(6) without time zone NOT NULL,
    originalsourcesystemuniqueid character varying(255) NOT NULL,
    classificationsequencenumber integer NOT NULL,
    classificationdesc character varying(500) NOT NULL,
    classificationname character varying(100) NOT NULL,
    activeflagid character varying(36) NOT NULL,
    enterpriseid character varying(36) NOT NULL,
    systemid character varying(36) NOT NULL,
    originalsourcesystemid character varying(36) NOT NULL,
    classificationdataconceptid character varying(36) NOT NULL
);


ALTER TABLE classification.classification OWNER TO postgres;

--
-- TOC entry 229 (class 1259 OID 28353)
-- Name: classificationdataconcept; Type: TABLE; Schema: classification; Owner: postgres
--

CREATE TABLE classification.classificationdataconcept (
    classificationdataconceptid character varying(36) NOT NULL,
    effectivefromdate timestamp(6) without time zone NOT NULL,
    effectivetodate timestamp(6) without time zone NOT NULL,
    warehousecreatedtimestamp timestamp(6) without time zone NOT NULL,
    warehouselastupdatedtimestamp timestamp(6) without time zone NOT NULL,
    originalsourcesystemuniqueid character varying(255) NOT NULL,
    classificationdataconceptdesc character varying(1500) NOT NULL,
    classificationdataconceptname character varying(100) NOT NULL,
    activeflagid character varying(36) NOT NULL,
    enterpriseid character varying(36) NOT NULL,
    systemid character varying(36) NOT NULL,
    originalsourcesystemid character varying(36) NOT NULL
);


ALTER TABLE classification.classificationdataconcept OWNER TO postgres;

--
-- TOC entry 230 (class 1259 OID 28360)
-- Name: classificationdataconceptsecuritytoken; Type: TABLE; Schema: classification; Owner: postgres
--

CREATE TABLE classification.classificationdataconceptsecuritytoken (
    classificationdataconceptsecuritytokenid character varying(36) NOT NULL,
    effectivefromdate timestamp(6) without time zone NOT NULL,
    effectivetodate timestamp(6) without time zone NOT NULL,
    warehousecreatedtimestamp timestamp(6) without time zone NOT NULL,
    warehouselastupdatedtimestamp timestamp(6) without time zone NOT NULL,
    createallowed integer NOT NULL,
    deleteallowed integer NOT NULL,
    originalsourcesystemuniqueid character varying(255) NOT NULL,
    readallowed integer NOT NULL,
    updateallowed integer NOT NULL,
    activeflagid character varying(36) NOT NULL,
    enterpriseid character varying(36) NOT NULL,
    originalsourcesystemid character varying(36) NOT NULL,
    securitytokenid character varying(36) NOT NULL,
    systemid character varying(36) NOT NULL,
    classificationdataconceptid character varying(36) NOT NULL
);


ALTER TABLE classification.classificationdataconceptsecuritytoken OWNER TO postgres;

--
-- TOC entry 231 (class 1259 OID 28367)
-- Name: classificationdataconceptxclassification; Type: TABLE; Schema: classification; Owner: postgres
--

CREATE TABLE classification.classificationdataconceptxclassification (
    classificationdataconceptxclassificationid character varying(36) NOT NULL,
    effectivefromdate timestamp(6) without time zone NOT NULL,
    effectivetodate timestamp(6) without time zone NOT NULL,
    warehousecreatedtimestamp timestamp(6) without time zone NOT NULL,
    warehouselastupdatedtimestamp timestamp(6) without time zone NOT NULL,
    originalsourcesystemuniqueid character varying(255) NOT NULL,
    value character varying(255) NOT NULL,
    activeflagid character varying(36) NOT NULL,
    enterpriseid character varying(36) NOT NULL,
    systemid character varying(36) NOT NULL,
    originalsourcesystemid character varying(36) NOT NULL,
    classificationid character varying(36) NOT NULL,
    classificationdataconceptid character varying(36) NOT NULL
);


ALTER TABLE classification.classificationdataconceptxclassification OWNER TO postgres;

--
-- TOC entry 232 (class 1259 OID 28374)
-- Name: classificationdataconceptxclassificationsecuritytoken; Type: TABLE; Schema: classification; Owner: postgres
--

CREATE TABLE classification.classificationdataconceptxclassificationsecuritytoken (
    classificationdataconceptxclassificationsecuritytokenid character varying(36) NOT NULL,
    effectivefromdate timestamp(6) without time zone NOT NULL,
    effectivetodate timestamp(6) without time zone NOT NULL,
    warehousecreatedtimestamp timestamp(6) without time zone NOT NULL,
    warehouselastupdatedtimestamp timestamp(6) without time zone NOT NULL,
    createallowed integer NOT NULL,
    deleteallowed integer NOT NULL,
    originalsourcesystemuniqueid character varying(255) NOT NULL,
    readallowed integer NOT NULL,
    updateallowed integer NOT NULL,
    activeflagid character varying(36) NOT NULL,
    enterpriseid character varying(36) NOT NULL,
    originalsourcesystemid character varying(36) NOT NULL,
    securitytokenid character varying(36) NOT NULL,
    systemid character varying(36) NOT NULL,
    classificationdataconceptxclassificationid character varying(36) NOT NULL
);


ALTER TABLE classification.classificationdataconceptxclassificationsecuritytoken OWNER TO postgres;

--
-- TOC entry 233 (class 1259 OID 28381)
-- Name: classificationdataconceptxresourceitem; Type: TABLE; Schema: classification; Owner: postgres
--

CREATE TABLE classification.classificationdataconceptxresourceitem (
    classificationdataconceptxresourceitemid character varying(36) NOT NULL,
    effectivefromdate timestamp(6) without time zone NOT NULL,
    effectivetodate timestamp(6) without time zone NOT NULL,
    warehousecreatedtimestamp timestamp(6) without time zone NOT NULL,
    warehouselastupdatedtimestamp timestamp(6) without time zone NOT NULL,
    originalsourcesystemuniqueid character varying(255) NOT NULL,
    value character varying(255) NOT NULL,
    activeflagid character varying(36) NOT NULL,
    enterpriseid character varying(36) NOT NULL,
    systemid character varying(36) NOT NULL,
    originalsourcesystemid character varying(36) NOT NULL,
    classificationid character varying(36) NOT NULL,
    classificationdataconceptid character varying(36) NOT NULL,
    resourceitemid character varying(36) NOT NULL
);


ALTER TABLE classification.classificationdataconceptxresourceitem OWNER TO postgres;

--
-- TOC entry 234 (class 1259 OID 28388)
-- Name: classificationdataconceptxresourceitemsecuritytoken; Type: TABLE; Schema: classification; Owner: postgres
--

CREATE TABLE classification.classificationdataconceptxresourceitemsecuritytoken (
    classificationdataconceptxresourceitemsecuritytokenid character varying(36) NOT NULL,
    effectivefromdate timestamp(6) without time zone NOT NULL,
    effectivetodate timestamp(6) without time zone NOT NULL,
    warehousecreatedtimestamp timestamp(6) without time zone NOT NULL,
    warehouselastupdatedtimestamp timestamp(6) without time zone NOT NULL,
    createallowed integer NOT NULL,
    deleteallowed integer NOT NULL,
    originalsourcesystemuniqueid character varying(255) NOT NULL,
    readallowed integer NOT NULL,
    updateallowed integer NOT NULL,
    activeflagid character varying(36) NOT NULL,
    enterpriseid character varying(36) NOT NULL,
    originalsourcesystemid character varying(36) NOT NULL,
    securitytokenid character varying(36) NOT NULL,
    systemid character varying(36) NOT NULL,
    classificationdataconceptxresourceitemid character varying(36) NOT NULL
);


ALTER TABLE classification.classificationdataconceptxresourceitemsecuritytoken OWNER TO postgres;

--
-- TOC entry 235 (class 1259 OID 28395)
-- Name: classificationsecuritytoken; Type: TABLE; Schema: classification; Owner: postgres
--

CREATE TABLE classification.classificationsecuritytoken (
    classificationsecuritytokenid character varying(36) NOT NULL,
    effectivefromdate timestamp(6) without time zone NOT NULL,
    effectivetodate timestamp(6) without time zone NOT NULL,
    warehousecreatedtimestamp timestamp(6) without time zone NOT NULL,
    warehouselastupdatedtimestamp timestamp(6) without time zone NOT NULL,
    createallowed integer NOT NULL,
    deleteallowed integer NOT NULL,
    originalsourcesystemuniqueid character varying(255) NOT NULL,
    readallowed integer NOT NULL,
    updateallowed integer NOT NULL,
    activeflagid character varying(36) NOT NULL,
    enterpriseid character varying(36) NOT NULL,
    originalsourcesystemid character varying(36) NOT NULL,
    securitytokenid character varying(36) NOT NULL,
    systemid character varying(36) NOT NULL,
    classificationid character varying(36) NOT NULL
);


ALTER TABLE classification.classificationsecuritytoken OWNER TO postgres;

--
-- TOC entry 236 (class 1259 OID 28402)
-- Name: classificationxclassification; Type: TABLE; Schema: classification; Owner: postgres
--

CREATE TABLE classification.classificationxclassification (
    classificationxclassificationid character varying(36) NOT NULL,
    effectivefromdate timestamp(6) without time zone NOT NULL,
    effectivetodate timestamp(6) without time zone NOT NULL,
    warehousecreatedtimestamp timestamp(6) without time zone NOT NULL,
    warehouselastupdatedtimestamp timestamp(6) without time zone NOT NULL,
    originalsourcesystemuniqueid character varying(255) NOT NULL,
    value character varying(255) NOT NULL,
    activeflagid character varying(36) NOT NULL,
    enterpriseid character varying(36) NOT NULL,
    systemid character varying(36) NOT NULL,
    originalsourcesystemid character varying(36) NOT NULL,
    classificationid character varying(36) NOT NULL,
    childclassificationid character varying(36) NOT NULL,
    parentclassificationid character varying(36) NOT NULL
);


ALTER TABLE classification.classificationxclassification OWNER TO postgres;

--
-- TOC entry 237 (class 1259 OID 28409)
-- Name: classificationxclassificationsecuritytoken; Type: TABLE; Schema: classification; Owner: postgres
--

CREATE TABLE classification.classificationxclassificationsecuritytoken (
    classificationxclassificationsecuritytokenid character varying(36) NOT NULL,
    effectivefromdate timestamp(6) without time zone NOT NULL,
    effectivetodate timestamp(6) without time zone NOT NULL,
    warehousecreatedtimestamp timestamp(6) without time zone NOT NULL,
    warehouselastupdatedtimestamp timestamp(6) without time zone NOT NULL,
    createallowed integer NOT NULL,
    deleteallowed integer NOT NULL,
    originalsourcesystemuniqueid character varying(255) NOT NULL,
    readallowed integer NOT NULL,
    updateallowed integer NOT NULL,
    activeflagid character varying(36) NOT NULL,
    enterpriseid character varying(36) NOT NULL,
    originalsourcesystemid character varying(36) NOT NULL,
    securitytokenid character varying(36) NOT NULL,
    systemid character varying(36) NOT NULL,
    classificationxclassificationid character varying(36) NOT NULL
);


ALTER TABLE classification.classificationxclassificationsecuritytoken OWNER TO postgres;

--
-- TOC entry 238 (class 1259 OID 28416)
-- Name: classificationxresourceitem; Type: TABLE; Schema: classification; Owner: postgres
--

CREATE TABLE classification.classificationxresourceitem (
    classificationxresourceitemid character varying(36) NOT NULL,
    effectivefromdate timestamp(6) without time zone NOT NULL,
    effectivetodate timestamp(6) without time zone NOT NULL,
    warehousecreatedtimestamp timestamp(6) without time zone NOT NULL,
    warehouselastupdatedtimestamp timestamp(6) without time zone NOT NULL,
    originalsourcesystemuniqueid character varying(255) NOT NULL,
    value character varying(255) NOT NULL,
    activeflagid character varying(36) NOT NULL,
    enterpriseid character varying(36) NOT NULL,
    systemid character varying(36) NOT NULL,
    originalsourcesystemid character varying(36) NOT NULL,
    classificationid character varying(36) NOT NULL,
    resourceitemid character varying(36) NOT NULL
);


ALTER TABLE classification.classificationxresourceitem OWNER TO postgres;

--
-- TOC entry 239 (class 1259 OID 28423)
-- Name: classificationxresourceitemsecuritytoken; Type: TABLE; Schema: classification; Owner: postgres
--

CREATE TABLE classification.classificationxresourceitemsecuritytoken (
    classificationxresourceitemsecuritytokenid character varying(36) NOT NULL,
    effectivefromdate timestamp(6) without time zone NOT NULL,
    effectivetodate timestamp(6) without time zone NOT NULL,
    warehousecreatedtimestamp timestamp(6) without time zone NOT NULL,
    warehouselastupdatedtimestamp timestamp(6) without time zone NOT NULL,
    createallowed integer NOT NULL,
    deleteallowed integer NOT NULL,
    originalsourcesystemuniqueid character varying(255) NOT NULL,
    readallowed integer NOT NULL,
    updateallowed integer NOT NULL,
    activeflagid character varying(36) NOT NULL,
    enterpriseid character varying(36) NOT NULL,
    originalsourcesystemid character varying(36) NOT NULL,
    securitytokenid character varying(36) NOT NULL,
    systemid character varying(36) NOT NULL,
    classificationxresourceitemid character varying(36) NOT NULL
);


ALTER TABLE classification.classificationxresourceitemsecuritytoken OWNER TO postgres;

--
-- TOC entry 240 (class 1259 OID 28430)
-- Name: activeflag; Type: TABLE; Schema: dbo; Owner: postgres
--

CREATE TABLE dbo.activeflag (
    activeflagid character varying(36) NOT NULL,
    effectivefromdate timestamp(6) without time zone NOT NULL,
    effectivetodate timestamp(6) without time zone NOT NULL,
    warehousecreatedtimestamp timestamp(6) without time zone NOT NULL,
    warehouselastupdatedtimestamp timestamp(6) without time zone NOT NULL,
    allowaccess integer NOT NULL,
    activeflagdescription character varying(100) NOT NULL,
    activeflagname character varying(100) NOT NULL,
    enterpriseid character varying(36) NOT NULL
);


ALTER TABLE dbo.activeflag OWNER TO postgres;

--
-- TOC entry 241 (class 1259 OID 28435)
-- Name: activeflagsecuritytoken; Type: TABLE; Schema: dbo; Owner: postgres
--

CREATE TABLE dbo.activeflagsecuritytoken (
    activeflagsecuritytokenid character varying(36) NOT NULL,
    effectivefromdate timestamp(6) without time zone NOT NULL,
    effectivetodate timestamp(6) without time zone NOT NULL,
    warehousecreatedtimestamp timestamp(6) without time zone NOT NULL,
    warehouselastupdatedtimestamp timestamp(6) without time zone NOT NULL,
    createallowed integer NOT NULL,
    deleteallowed integer NOT NULL,
    originalsourcesystemuniqueid character varying(255) NOT NULL,
    readallowed integer NOT NULL,
    updateallowed integer NOT NULL,
    activeflagid character varying(36) NOT NULL,
    enterpriseid character varying(36) NOT NULL,
    originalsourcesystemid character varying(36) NOT NULL,
    securitytokenid character varying(36) NOT NULL,
    systemid character varying(36) NOT NULL,
    securitytokenactiveflagid character varying(36) NOT NULL
);


ALTER TABLE dbo.activeflagsecuritytoken OWNER TO postgres;

--
-- TOC entry 242 (class 1259 OID 28442)
-- Name: activeflagxclassification; Type: TABLE; Schema: dbo; Owner: postgres
--

CREATE TABLE dbo.activeflagxclassification (
    activeflagxclassificationid character varying(36) NOT NULL,
    effectivefromdate timestamp(6) without time zone NOT NULL,
    effectivetodate timestamp(6) without time zone NOT NULL,
    warehousecreatedtimestamp timestamp(6) without time zone NOT NULL,
    warehouselastupdatedtimestamp timestamp(6) without time zone NOT NULL,
    originalsourcesystemuniqueid character varying(255) NOT NULL,
    value character varying(255) NOT NULL,
    activeflagid character varying(36) NOT NULL,
    enterpriseid character varying(36) NOT NULL,
    systemid character varying(36) NOT NULL,
    originalsourcesystemid character varying(36) NOT NULL,
    classificationid character varying(36) NOT NULL
);


ALTER TABLE dbo.activeflagxclassification OWNER TO postgres;

--
-- TOC entry 243 (class 1259 OID 28449)
-- Name: activeflagxclassificationsecuritytoken; Type: TABLE; Schema: dbo; Owner: postgres
--

CREATE TABLE dbo.activeflagxclassificationsecuritytoken (
    activeflagxclassificationsecuritytokenid character varying(36) NOT NULL,
    effectivefromdate timestamp(6) without time zone NOT NULL,
    effectivetodate timestamp(6) without time zone NOT NULL,
    warehousecreatedtimestamp timestamp(6) without time zone NOT NULL,
    warehouselastupdatedtimestamp timestamp(6) without time zone NOT NULL,
    createallowed integer NOT NULL,
    deleteallowed integer NOT NULL,
    originalsourcesystemuniqueid character varying(255) NOT NULL,
    readallowed integer NOT NULL,
    updateallowed integer NOT NULL,
    activeflagid character varying(36) NOT NULL,
    enterpriseid character varying(36) NOT NULL,
    originalsourcesystemid character varying(36) NOT NULL,
    securitytokenid character varying(36) NOT NULL,
    systemid character varying(36) NOT NULL,
    activeflagxclassificationid character varying(36) NOT NULL
);


ALTER TABLE dbo.activeflagxclassificationsecuritytoken OWNER TO postgres;

--
-- TOC entry 244 (class 1259 OID 28456)
-- Name: enterprise; Type: TABLE; Schema: dbo; Owner: postgres
--

CREATE TABLE dbo.enterprise (
    enterpriseid character varying(36) NOT NULL,
    effectivefromdate timestamp(6) without time zone NOT NULL,
    effectivetodate timestamp(6) without time zone NOT NULL,
    warehousecreatedtimestamp timestamp(6) without time zone NOT NULL,
    warehouselastupdatedtimestamp timestamp(6) without time zone NOT NULL,
    enterprisedesc character varying(255) NOT NULL,
    enterprisename character varying(255) NOT NULL
);


ALTER TABLE dbo.enterprise OWNER TO postgres;

--
-- TOC entry 245 (class 1259 OID 28463)
-- Name: enterprisesecuritytoken; Type: TABLE; Schema: dbo; Owner: postgres
--

CREATE TABLE dbo.enterprisesecuritytoken (
    enterprisesecuritytokenid character varying(36) NOT NULL,
    effectivefromdate timestamp(6) without time zone NOT NULL,
    effectivetodate timestamp(6) without time zone NOT NULL,
    warehousecreatedtimestamp timestamp(6) without time zone NOT NULL,
    warehouselastupdatedtimestamp timestamp(6) without time zone NOT NULL,
    createallowed integer NOT NULL,
    deleteallowed integer NOT NULL,
    originalsourcesystemuniqueid character varying(255) NOT NULL,
    readallowed integer NOT NULL,
    updateallowed integer NOT NULL,
    activeflagid character varying(36) NOT NULL,
    enterpriseid character varying(36) NOT NULL,
    originalsourcesystemid character varying(36) NOT NULL,
    securitytokenid character varying(36) NOT NULL,
    systemid character varying(36) NOT NULL
);


ALTER TABLE dbo.enterprisesecuritytoken OWNER TO postgres;

--
-- TOC entry 246 (class 1259 OID 28468)
-- Name: enterprisexclassification; Type: TABLE; Schema: dbo; Owner: postgres
--

CREATE TABLE dbo.enterprisexclassification (
    enterprisexclassificationid character varying(36) NOT NULL,
    effectivefromdate timestamp(6) without time zone NOT NULL,
    effectivetodate timestamp(6) without time zone NOT NULL,
    warehousecreatedtimestamp timestamp(6) without time zone NOT NULL,
    warehouselastupdatedtimestamp timestamp(6) without time zone NOT NULL,
    originalsourcesystemuniqueid character varying(255) NOT NULL,
    value character varying(255) NOT NULL,
    activeflagid character varying(36) NOT NULL,
    enterpriseid character varying(36) NOT NULL,
    systemid character varying(36) NOT NULL,
    originalsourcesystemid character varying(36) NOT NULL,
    classificationid character varying(36) NOT NULL
);


ALTER TABLE dbo.enterprisexclassification OWNER TO postgres;

--
-- TOC entry 247 (class 1259 OID 28475)
-- Name: enterprisexclassificationsecuritytoken; Type: TABLE; Schema: dbo; Owner: postgres
--

CREATE TABLE dbo.enterprisexclassificationsecuritytoken (
    enterprisexclassificationsecuritytokenid character varying(36) NOT NULL,
    effectivefromdate timestamp(6) without time zone NOT NULL,
    effectivetodate timestamp(6) without time zone NOT NULL,
    warehousecreatedtimestamp timestamp(6) without time zone NOT NULL,
    warehouselastupdatedtimestamp timestamp(6) without time zone NOT NULL,
    createallowed integer NOT NULL,
    deleteallowed integer NOT NULL,
    originalsourcesystemuniqueid character varying(255) NOT NULL,
    readallowed integer NOT NULL,
    updateallowed integer NOT NULL,
    activeflagid character varying(36) NOT NULL,
    enterpriseid character varying(36) NOT NULL,
    originalsourcesystemid character varying(36) NOT NULL,
    securitytokenid character varying(36) NOT NULL,
    systemid character varying(36) NOT NULL,
    enterprisexclassificationid character varying(36) NOT NULL
);


ALTER TABLE dbo.enterprisexclassificationsecuritytoken OWNER TO postgres;

--
-- TOC entry 248 (class 1259 OID 28482)
-- Name: systems; Type: TABLE; Schema: dbo; Owner: postgres
--

CREATE TABLE dbo.systems (
    systemid character varying(36) NOT NULL,
    effectivefromdate timestamp(6) without time zone NOT NULL,
    effectivetodate timestamp(6) without time zone NOT NULL,
    warehousecreatedtimestamp timestamp(6) without time zone NOT NULL,
    warehouselastupdatedtimestamp timestamp(6) without time zone NOT NULL,
    systemdesc character varying(250) NOT NULL,
    systemname character varying(150) NOT NULL,
    systemhistoryname character varying(250) NOT NULL,
    activeflagid character varying(36) NOT NULL,
    enterpriseid character varying(36) NOT NULL
);


ALTER TABLE dbo.systems OWNER TO postgres;

--
-- TOC entry 249 (class 1259 OID 28489)
-- Name: systemssecuritytoken; Type: TABLE; Schema: dbo; Owner: postgres
--

CREATE TABLE dbo.systemssecuritytoken (
    systemssecuritytokenid character varying(36) NOT NULL,
    effectivefromdate timestamp(6) without time zone NOT NULL,
    effectivetodate timestamp(6) without time zone NOT NULL,
    warehousecreatedtimestamp timestamp(6) without time zone NOT NULL,
    warehouselastupdatedtimestamp timestamp(6) without time zone NOT NULL,
    createallowed integer NOT NULL,
    deleteallowed integer NOT NULL,
    originalsourcesystemuniqueid character varying(255) NOT NULL,
    readallowed integer NOT NULL,
    updateallowed integer NOT NULL,
    activeflagid character varying(36) NOT NULL,
    enterpriseid character varying(36) NOT NULL,
    originalsourcesystemid character varying(36) NOT NULL,
    securitytokenid character varying(36) NOT NULL,
    systemid character varying(36) NOT NULL
);


ALTER TABLE dbo.systemssecuritytoken OWNER TO postgres;

--
-- TOC entry 250 (class 1259 OID 28494)
-- Name: systemxclassification; Type: TABLE; Schema: dbo; Owner: postgres
--

CREATE TABLE dbo.systemxclassification (
    systemxclassificationid character varying(36) NOT NULL,
    effectivefromdate timestamp(6) without time zone NOT NULL,
    effectivetodate timestamp(6) without time zone NOT NULL,
    warehousecreatedtimestamp timestamp(6) without time zone NOT NULL,
    warehouselastupdatedtimestamp timestamp(6) without time zone NOT NULL,
    originalsourcesystemuniqueid character varying(255) NOT NULL,
    value character varying(255) NOT NULL,
    activeflagid character varying(36) NOT NULL,
    enterpriseid character varying(36) NOT NULL,
    systemid character varying(36) NOT NULL,
    originalsourcesystemid character varying(36) NOT NULL,
    classificationid character varying(36) NOT NULL
);


ALTER TABLE dbo.systemxclassification OWNER TO postgres;

--
-- TOC entry 251 (class 1259 OID 28501)
-- Name: systemxclassificationsecuritytoken; Type: TABLE; Schema: dbo; Owner: postgres
--

CREATE TABLE dbo.systemxclassificationsecuritytoken (
    systemxclassificationsecuritytokenid character varying(36) NOT NULL,
    effectivefromdate timestamp(6) without time zone NOT NULL,
    effectivetodate timestamp(6) without time zone NOT NULL,
    warehousecreatedtimestamp timestamp(6) without time zone NOT NULL,
    warehouselastupdatedtimestamp timestamp(6) without time zone NOT NULL,
    createallowed integer NOT NULL,
    deleteallowed integer NOT NULL,
    originalsourcesystemuniqueid character varying(255) NOT NULL,
    readallowed integer NOT NULL,
    updateallowed integer NOT NULL,
    activeflagid character varying(36) NOT NULL,
    enterpriseid character varying(36) NOT NULL,
    originalsourcesystemid character varying(36) NOT NULL,
    securitytokenid character varying(36) NOT NULL,
    systemid character varying(36) NOT NULL,
    systemxclassificationid character varying(36) NOT NULL
);


ALTER TABLE dbo.systemxclassificationsecuritytoken OWNER TO postgres;

--
-- TOC entry 252 (class 1259 OID 28508)
-- Name: event; Type: TABLE; Schema: event; Owner: postgres
--

CREATE TABLE event.event (
    eventid character varying(36) NOT NULL,
    effectivefromdate timestamp(6) without time zone NOT NULL,
    effectivetodate timestamp(6) without time zone NOT NULL,
    warehousecreatedtimestamp timestamp(6) without time zone NOT NULL,
    warehouselastupdatedtimestamp timestamp(6) without time zone NOT NULL,
    originalsourcesystemuniqueid character varying(255) NOT NULL,
    dayid integer NOT NULL,
    hourid integer NOT NULL,
    minuteid integer NOT NULL,
    activeflagid character varying(36) NOT NULL,
    enterpriseid character varying(36) NOT NULL,
    systemid character varying(36) NOT NULL,
    originalsourcesystemid character varying(36) NOT NULL
);


ALTER TABLE event.event OWNER TO postgres;

--
-- TOC entry 253 (class 1259 OID 28513)
-- Name: eventsecuritytoken; Type: TABLE; Schema: event; Owner: postgres
--

CREATE TABLE event.eventsecuritytoken (
    eventssecuritytokenid character varying(36) NOT NULL,
    effectivefromdate timestamp(6) without time zone NOT NULL,
    effectivetodate timestamp(6) without time zone NOT NULL,
    warehousecreatedtimestamp timestamp(6) without time zone NOT NULL,
    warehouselastupdatedtimestamp timestamp(6) without time zone NOT NULL,
    createallowed integer NOT NULL,
    deleteallowed integer NOT NULL,
    originalsourcesystemuniqueid character varying(255) NOT NULL,
    readallowed integer NOT NULL,
    updateallowed integer NOT NULL,
    activeflagid character varying(36) NOT NULL,
    enterpriseid character varying(36) NOT NULL,
    originalsourcesystemid character varying(36) NOT NULL,
    securitytokenid character varying(36) NOT NULL,
    systemid character varying(36) NOT NULL,
    eventsid character varying(36) NOT NULL
);


ALTER TABLE event.eventsecuritytoken OWNER TO postgres;

--
-- TOC entry 254 (class 1259 OID 28520)
-- Name: eventtype; Type: TABLE; Schema: event; Owner: postgres
--

CREATE TABLE event.eventtype (
    eventtypeid character varying(36) NOT NULL,
    effectivefromdate timestamp(6) without time zone NOT NULL,
    effectivetodate timestamp(6) without time zone NOT NULL,
    warehousecreatedtimestamp timestamp(6) without time zone NOT NULL,
    warehouselastupdatedtimestamp timestamp(6) without time zone NOT NULL,
    originalsourcesystemuniqueid character varying(255) NOT NULL,
    eventtypedesc character varying(200) NOT NULL,
    eventtypename character varying(200) NOT NULL,
    activeflagid character varying(36) NOT NULL,
    enterpriseid character varying(36) NOT NULL,
    systemid character varying(36) NOT NULL,
    originalsourcesystemid character varying(36) NOT NULL
);


ALTER TABLE event.eventtype OWNER TO postgres;

--
-- TOC entry 255 (class 1259 OID 28527)
-- Name: eventtypessecuritytoken; Type: TABLE; Schema: event; Owner: postgres
--

CREATE TABLE event.eventtypessecuritytoken (
    eventtypessecuritytokenid character varying(36) NOT NULL,
    effectivefromdate timestamp(6) without time zone NOT NULL,
    effectivetodate timestamp(6) without time zone NOT NULL,
    warehousecreatedtimestamp timestamp(6) without time zone NOT NULL,
    warehouselastupdatedtimestamp timestamp(6) without time zone NOT NULL,
    createallowed integer NOT NULL,
    deleteallowed integer NOT NULL,
    originalsourcesystemuniqueid character varying(255) NOT NULL,
    readallowed integer NOT NULL,
    updateallowed integer NOT NULL,
    activeflagid character varying(36) NOT NULL,
    enterpriseid character varying(36) NOT NULL,
    originalsourcesystemid character varying(36) NOT NULL,
    securitytokenid character varying(36) NOT NULL,
    systemid character varying(36) NOT NULL,
    eventtypesid character varying(36) NOT NULL
);


ALTER TABLE event.eventtypessecuritytoken OWNER TO postgres;

--
-- TOC entry 256 (class 1259 OID 28534)
-- Name: eventxaddress; Type: TABLE; Schema: event; Owner: postgres
--

CREATE TABLE event.eventxaddress (
    eventxaddressid character varying(36) NOT NULL,
    effectivefromdate timestamp(6) without time zone NOT NULL,
    effectivetodate timestamp(6) without time zone NOT NULL,
    warehousecreatedtimestamp timestamp(6) without time zone NOT NULL,
    warehouselastupdatedtimestamp timestamp(6) without time zone NOT NULL,
    originalsourcesystemuniqueid character varying(255) NOT NULL,
    value character varying(255) NOT NULL,
    activeflagid character varying(36) NOT NULL,
    enterpriseid character varying(36) NOT NULL,
    systemid character varying(36) NOT NULL,
    originalsourcesystemid character varying(36) NOT NULL,
    classificationid character varying(36) NOT NULL,
    addressid character varying(36) NOT NULL,
    eventid character varying(36) NOT NULL
);


ALTER TABLE event.eventxaddress OWNER TO postgres;

--
-- TOC entry 257 (class 1259 OID 28541)
-- Name: eventxaddresssecuritytoken; Type: TABLE; Schema: event; Owner: postgres
--

CREATE TABLE event.eventxaddresssecuritytoken (
    eventxaddresssecuritytokenid character varying(36) NOT NULL,
    effectivefromdate timestamp(6) without time zone NOT NULL,
    effectivetodate timestamp(6) without time zone NOT NULL,
    warehousecreatedtimestamp timestamp(6) without time zone NOT NULL,
    warehouselastupdatedtimestamp timestamp(6) without time zone NOT NULL,
    createallowed integer NOT NULL,
    deleteallowed integer NOT NULL,
    originalsourcesystemuniqueid character varying(255) NOT NULL,
    readallowed integer NOT NULL,
    updateallowed integer NOT NULL,
    activeflagid character varying(36) NOT NULL,
    enterpriseid character varying(36) NOT NULL,
    originalsourcesystemid character varying(36) NOT NULL,
    securitytokenid character varying(36) NOT NULL,
    systemid character varying(36) NOT NULL,
    eventxaddressid character varying(36) NOT NULL
);


ALTER TABLE event.eventxaddresssecuritytoken OWNER TO postgres;

--
-- TOC entry 258 (class 1259 OID 28548)
-- Name: eventxarrangement; Type: TABLE; Schema: event; Owner: postgres
--

CREATE TABLE event.eventxarrangement (
    eventxarrangementsid character varying(36) NOT NULL,
    effectivefromdate timestamp(6) without time zone NOT NULL,
    effectivetodate timestamp(6) without time zone NOT NULL,
    warehousecreatedtimestamp timestamp(6) without time zone NOT NULL,
    warehouselastupdatedtimestamp timestamp(6) without time zone NOT NULL,
    originalsourcesystemuniqueid character varying(255) NOT NULL,
    value character varying(255) NOT NULL,
    activeflagid character varying(36) NOT NULL,
    enterpriseid character varying(36) NOT NULL,
    systemid character varying(36) NOT NULL,
    originalsourcesystemid character varying(36) NOT NULL,
    classificationid character varying(36) NOT NULL,
    arrangementid character varying(36) NOT NULL,
    eventid character varying(36) NOT NULL
);


ALTER TABLE event.eventxarrangement OWNER TO postgres;

--
-- TOC entry 259 (class 1259 OID 28555)
-- Name: eventxarrangementssecuritytoken; Type: TABLE; Schema: event; Owner: postgres
--

CREATE TABLE event.eventxarrangementssecuritytoken (
    eventxarrangementssecuritytokenid character varying(36) NOT NULL,
    effectivefromdate timestamp(6) without time zone NOT NULL,
    effectivetodate timestamp(6) without time zone NOT NULL,
    warehousecreatedtimestamp timestamp(6) without time zone NOT NULL,
    warehouselastupdatedtimestamp timestamp(6) without time zone NOT NULL,
    createallowed integer NOT NULL,
    deleteallowed integer NOT NULL,
    originalsourcesystemuniqueid character varying(255) NOT NULL,
    readallowed integer NOT NULL,
    updateallowed integer NOT NULL,
    activeflagid character varying(36) NOT NULL,
    enterpriseid character varying(36) NOT NULL,
    originalsourcesystemid character varying(36) NOT NULL,
    securitytokenid character varying(36) NOT NULL,
    systemid character varying(36) NOT NULL,
    eventxarrangementsid character varying(36) NOT NULL
);


ALTER TABLE event.eventxarrangementssecuritytoken OWNER TO postgres;

--
-- TOC entry 260 (class 1259 OID 28562)
-- Name: eventxclassification; Type: TABLE; Schema: event; Owner: postgres
--

CREATE TABLE event.eventxclassification (
    eventxclassificationid character varying(36) NOT NULL,
    effectivefromdate timestamp(6) without time zone NOT NULL,
    effectivetodate timestamp(6) without time zone NOT NULL,
    warehousecreatedtimestamp timestamp(6) without time zone NOT NULL,
    warehouselastupdatedtimestamp timestamp(6) without time zone NOT NULL,
    originalsourcesystemuniqueid character varying(255) NOT NULL,
    value character varying(255) NOT NULL,
    activeflagid character varying(36) NOT NULL,
    enterpriseid character varying(36) NOT NULL,
    systemid character varying(36) NOT NULL,
    originalsourcesystemid character varying(36) NOT NULL,
    classificationid character varying(36) NOT NULL,
    eventid character varying(36) NOT NULL
);


ALTER TABLE event.eventxclassification OWNER TO postgres;

--
-- TOC entry 261 (class 1259 OID 28569)
-- Name: eventxclassificationsecuritytoken; Type: TABLE; Schema: event; Owner: postgres
--

CREATE TABLE event.eventxclassificationsecuritytoken (
    eventxclassificationssecuritytokenid character varying(36) NOT NULL,
    effectivefromdate timestamp(6) without time zone NOT NULL,
    effectivetodate timestamp(6) without time zone NOT NULL,
    warehousecreatedtimestamp timestamp(6) without time zone NOT NULL,
    warehouselastupdatedtimestamp timestamp(6) without time zone NOT NULL,
    createallowed integer NOT NULL,
    deleteallowed integer NOT NULL,
    originalsourcesystemuniqueid character varying(255) NOT NULL,
    readallowed integer NOT NULL,
    updateallowed integer NOT NULL,
    activeflagid character varying(36) NOT NULL,
    enterpriseid character varying(36) NOT NULL,
    originalsourcesystemid character varying(36) NOT NULL,
    securitytokenid character varying(36) NOT NULL,
    systemid character varying(36) NOT NULL,
    eventxclassificationsid character varying(36) NOT NULL
);


ALTER TABLE event.eventxclassificationsecuritytoken OWNER TO postgres;

--
-- TOC entry 262 (class 1259 OID 28576)
-- Name: eventxevent; Type: TABLE; Schema: event; Owner: postgres
--

CREATE TABLE event.eventxevent (
    eventxeventid character varying(36) NOT NULL,
    effectivefromdate timestamp(6) without time zone NOT NULL,
    effectivetodate timestamp(6) without time zone NOT NULL,
    warehousecreatedtimestamp timestamp(6) without time zone NOT NULL,
    warehouselastupdatedtimestamp timestamp(6) without time zone NOT NULL,
    originalsourcesystemuniqueid character varying(255) NOT NULL,
    value character varying(255) NOT NULL,
    activeflagid character varying(36) NOT NULL,
    enterpriseid character varying(36) NOT NULL,
    systemid character varying(36) NOT NULL,
    originalsourcesystemid character varying(36) NOT NULL,
    classificationid character varying(36) NOT NULL,
    childeventid character varying(36) NOT NULL,
    parenteventid character varying(36) NOT NULL
);


ALTER TABLE event.eventxevent OWNER TO postgres;

--
-- TOC entry 263 (class 1259 OID 28583)
-- Name: eventxeventsecuritytoken; Type: TABLE; Schema: event; Owner: postgres
--

CREATE TABLE event.eventxeventsecuritytoken (
    eventxeventsecuritytokenid character varying(36) NOT NULL,
    effectivefromdate timestamp(6) without time zone NOT NULL,
    effectivetodate timestamp(6) without time zone NOT NULL,
    warehousecreatedtimestamp timestamp(6) without time zone NOT NULL,
    warehouselastupdatedtimestamp timestamp(6) without time zone NOT NULL,
    createallowed integer NOT NULL,
    deleteallowed integer NOT NULL,
    originalsourcesystemuniqueid character varying(255) NOT NULL,
    readallowed integer NOT NULL,
    updateallowed integer NOT NULL,
    activeflagid character varying(36) NOT NULL,
    enterpriseid character varying(36) NOT NULL,
    originalsourcesystemid character varying(36) NOT NULL,
    securitytokenid character varying(36) NOT NULL,
    systemid character varying(36) NOT NULL,
    eventxeventid character varying(36) NOT NULL
);


ALTER TABLE event.eventxeventsecuritytoken OWNER TO postgres;

--
-- TOC entry 264 (class 1259 OID 28590)
-- Name: eventxeventtype; Type: TABLE; Schema: event; Owner: postgres
--

CREATE TABLE event.eventxeventtype (
    eventxeventtypeid character varying(36) NOT NULL,
    effectivefromdate timestamp(6) without time zone NOT NULL,
    effectivetodate timestamp(6) without time zone NOT NULL,
    warehousecreatedtimestamp timestamp(6) without time zone NOT NULL,
    warehouselastupdatedtimestamp timestamp(6) without time zone NOT NULL,
    originalsourcesystemuniqueid character varying(255) NOT NULL,
    value character varying(255) NOT NULL,
    activeflagid character varying(36) NOT NULL,
    enterpriseid character varying(36) NOT NULL,
    systemid character varying(36) NOT NULL,
    originalsourcesystemid character varying(36) NOT NULL,
    classificationid character varying(36) NOT NULL,
    eventid character varying(36) NOT NULL,
    eventtypeid character varying(36) NOT NULL
);


ALTER TABLE event.eventxeventtype OWNER TO postgres;

--
-- TOC entry 265 (class 1259 OID 28597)
-- Name: eventxeventtypesecuritytoken; Type: TABLE; Schema: event; Owner: postgres
--

CREATE TABLE event.eventxeventtypesecuritytoken (
    eventxeventtypesecuritytokenid character varying(36) NOT NULL,
    effectivefromdate timestamp(6) without time zone NOT NULL,
    effectivetodate timestamp(6) without time zone NOT NULL,
    warehousecreatedtimestamp timestamp(6) without time zone NOT NULL,
    warehouselastupdatedtimestamp timestamp(6) without time zone NOT NULL,
    createallowed integer NOT NULL,
    deleteallowed integer NOT NULL,
    originalsourcesystemuniqueid character varying(255) NOT NULL,
    readallowed integer NOT NULL,
    updateallowed integer NOT NULL,
    activeflagid character varying(36) NOT NULL,
    enterpriseid character varying(36) NOT NULL,
    originalsourcesystemid character varying(36) NOT NULL,
    securitytokenid character varying(36) NOT NULL,
    systemid character varying(36) NOT NULL,
    eventxeventtypeid character varying(36) NOT NULL
);


ALTER TABLE event.eventxeventtypesecuritytoken OWNER TO postgres;

--
-- TOC entry 266 (class 1259 OID 28604)
-- Name: eventxgeography; Type: TABLE; Schema: event; Owner: postgres
--

CREATE TABLE event.eventxgeography (
    eventxgeographyid character varying(36) NOT NULL,
    effectivefromdate timestamp(6) without time zone NOT NULL,
    effectivetodate timestamp(6) without time zone NOT NULL,
    warehousecreatedtimestamp timestamp(6) without time zone NOT NULL,
    warehouselastupdatedtimestamp timestamp(6) without time zone NOT NULL,
    originalsourcesystemuniqueid character varying(255) NOT NULL,
    value character varying(255) NOT NULL,
    activeflagid character varying(36) NOT NULL,
    enterpriseid character varying(36) NOT NULL,
    systemid character varying(36) NOT NULL,
    originalsourcesystemid character varying(36) NOT NULL,
    classificationid character varying(36) NOT NULL,
    eventid character varying(36) NOT NULL,
    geographyid character varying(36) NOT NULL
);


ALTER TABLE event.eventxgeography OWNER TO postgres;

--
-- TOC entry 267 (class 1259 OID 28611)
-- Name: eventxgeographysecuritytoken; Type: TABLE; Schema: event; Owner: postgres
--

CREATE TABLE event.eventxgeographysecuritytoken (
    eventxgeographysecuritytokenid character varying(36) NOT NULL,
    effectivefromdate timestamp(6) without time zone NOT NULL,
    effectivetodate timestamp(6) without time zone NOT NULL,
    warehousecreatedtimestamp timestamp(6) without time zone NOT NULL,
    warehouselastupdatedtimestamp timestamp(6) without time zone NOT NULL,
    createallowed integer NOT NULL,
    deleteallowed integer NOT NULL,
    originalsourcesystemuniqueid character varying(255) NOT NULL,
    readallowed integer NOT NULL,
    updateallowed integer NOT NULL,
    activeflagid character varying(36) NOT NULL,
    enterpriseid character varying(36) NOT NULL,
    originalsourcesystemid character varying(36) NOT NULL,
    securitytokenid character varying(36) NOT NULL,
    systemid character varying(36) NOT NULL,
    eventxgeographyid character varying(36) NOT NULL
);


ALTER TABLE event.eventxgeographysecuritytoken OWNER TO postgres;

--
-- TOC entry 268 (class 1259 OID 28618)
-- Name: eventxinvolvedparty; Type: TABLE; Schema: event; Owner: postgres
--

CREATE TABLE event.eventxinvolvedparty (
    eventxinvolvedpartyid character varying(36) NOT NULL,
    effectivefromdate timestamp(6) without time zone NOT NULL,
    effectivetodate timestamp(6) without time zone NOT NULL,
    warehousecreatedtimestamp timestamp(6) without time zone NOT NULL,
    warehouselastupdatedtimestamp timestamp(6) without time zone NOT NULL,
    originalsourcesystemuniqueid character varying(255) NOT NULL,
    value character varying(255) NOT NULL,
    activeflagid character varying(36) NOT NULL,
    enterpriseid character varying(36) NOT NULL,
    systemid character varying(36) NOT NULL,
    originalsourcesystemid character varying(36) NOT NULL,
    classificationid character varying(36) NOT NULL,
    eventid character varying(36) NOT NULL,
    involvedpartyid character varying(36) NOT NULL
);


ALTER TABLE event.eventxinvolvedparty OWNER TO postgres;

--
-- TOC entry 269 (class 1259 OID 28625)
-- Name: eventxinvolvedpartysecuritytoken; Type: TABLE; Schema: event; Owner: postgres
--

CREATE TABLE event.eventxinvolvedpartysecuritytoken (
    eventxinvolvedpartysecuritytokenid character varying(36) NOT NULL,
    effectivefromdate timestamp(6) without time zone NOT NULL,
    effectivetodate timestamp(6) without time zone NOT NULL,
    warehousecreatedtimestamp timestamp(6) without time zone NOT NULL,
    warehouselastupdatedtimestamp timestamp(6) without time zone NOT NULL,
    createallowed integer NOT NULL,
    deleteallowed integer NOT NULL,
    originalsourcesystemuniqueid character varying(255) NOT NULL,
    readallowed integer NOT NULL,
    updateallowed integer NOT NULL,
    activeflagid character varying(36) NOT NULL,
    enterpriseid character varying(36) NOT NULL,
    originalsourcesystemid character varying(36) NOT NULL,
    securitytokenid character varying(36) NOT NULL,
    systemid character varying(36) NOT NULL,
    eventxinvolvedpartyid character varying(36) NOT NULL
);


ALTER TABLE event.eventxinvolvedpartysecuritytoken OWNER TO postgres;

--
-- TOC entry 270 (class 1259 OID 28632)
-- Name: eventxproduct; Type: TABLE; Schema: event; Owner: postgres
--

CREATE TABLE event.eventxproduct (
    eventxproductid character varying(36) NOT NULL,
    effectivefromdate timestamp(6) without time zone NOT NULL,
    effectivetodate timestamp(6) without time zone NOT NULL,
    warehousecreatedtimestamp timestamp(6) without time zone NOT NULL,
    warehouselastupdatedtimestamp timestamp(6) without time zone NOT NULL,
    originalsourcesystemuniqueid character varying(255) NOT NULL,
    value character varying(255) NOT NULL,
    activeflagid character varying(36) NOT NULL,
    enterpriseid character varying(36) NOT NULL,
    systemid character varying(36) NOT NULL,
    originalsourcesystemid character varying(36) NOT NULL,
    classificationid character varying(36) NOT NULL,
    eventid character varying(36) NOT NULL,
    productid character varying(36) NOT NULL
);


ALTER TABLE event.eventxproduct OWNER TO postgres;

--
-- TOC entry 271 (class 1259 OID 28639)
-- Name: eventxproductsecuritytoken; Type: TABLE; Schema: event; Owner: postgres
--

CREATE TABLE event.eventxproductsecuritytoken (
    eventxproductsecuritytokenid character varying(36) NOT NULL,
    effectivefromdate timestamp(6) without time zone NOT NULL,
    effectivetodate timestamp(6) without time zone NOT NULL,
    warehousecreatedtimestamp timestamp(6) without time zone NOT NULL,
    warehouselastupdatedtimestamp timestamp(6) without time zone NOT NULL,
    createallowed integer NOT NULL,
    deleteallowed integer NOT NULL,
    originalsourcesystemuniqueid character varying(255) NOT NULL,
    readallowed integer NOT NULL,
    updateallowed integer NOT NULL,
    activeflagid character varying(36) NOT NULL,
    enterpriseid character varying(36) NOT NULL,
    originalsourcesystemid character varying(36) NOT NULL,
    securitytokenid character varying(36) NOT NULL,
    systemid character varying(36) NOT NULL,
    eventxproductid character varying(36) NOT NULL
);


ALTER TABLE event.eventxproductsecuritytoken OWNER TO postgres;

--
-- TOC entry 272 (class 1259 OID 28646)
-- Name: eventxresourceitem; Type: TABLE; Schema: event; Owner: postgres
--

CREATE TABLE event.eventxresourceitem (
    eventxresourceitemid character varying(36) NOT NULL,
    effectivefromdate timestamp(6) without time zone NOT NULL,
    effectivetodate timestamp(6) without time zone NOT NULL,
    warehousecreatedtimestamp timestamp(6) without time zone NOT NULL,
    warehouselastupdatedtimestamp timestamp(6) without time zone NOT NULL,
    originalsourcesystemuniqueid character varying(255) NOT NULL,
    value character varying(255) NOT NULL,
    activeflagid character varying(36) NOT NULL,
    enterpriseid character varying(36) NOT NULL,
    systemid character varying(36) NOT NULL,
    originalsourcesystemid character varying(36) NOT NULL,
    classificationid character varying(36) NOT NULL,
    eventid character varying(36) NOT NULL,
    resourceitemid character varying(36) NOT NULL
);


ALTER TABLE event.eventxresourceitem OWNER TO postgres;

--
-- TOC entry 273 (class 1259 OID 28653)
-- Name: eventxresourceitemsecuritytoken; Type: TABLE; Schema: event; Owner: postgres
--

CREATE TABLE event.eventxresourceitemsecuritytoken (
    eventxresourceitemsecuritytokenid character varying(36) NOT NULL,
    effectivefromdate timestamp(6) without time zone NOT NULL,
    effectivetodate timestamp(6) without time zone NOT NULL,
    warehousecreatedtimestamp timestamp(6) without time zone NOT NULL,
    warehouselastupdatedtimestamp timestamp(6) without time zone NOT NULL,
    createallowed integer NOT NULL,
    deleteallowed integer NOT NULL,
    originalsourcesystemuniqueid character varying(255) NOT NULL,
    readallowed integer NOT NULL,
    updateallowed integer NOT NULL,
    activeflagid character varying(36) NOT NULL,
    enterpriseid character varying(36) NOT NULL,
    originalsourcesystemid character varying(36) NOT NULL,
    securitytokenid character varying(36) NOT NULL,
    systemid character varying(36) NOT NULL,
    eventxresourceitemid character varying(36) NOT NULL
);


ALTER TABLE event.eventxresourceitemsecuritytoken OWNER TO postgres;

--
-- TOC entry 274 (class 1259 OID 28660)
-- Name: eventxrules; Type: TABLE; Schema: event; Owner: postgres
--

CREATE TABLE event.eventxrules (
    eventxrulesid character varying(36) NOT NULL,
    effectivefromdate timestamp(6) without time zone NOT NULL,
    effectivetodate timestamp(6) without time zone NOT NULL,
    warehousecreatedtimestamp timestamp(6) without time zone NOT NULL,
    warehouselastupdatedtimestamp timestamp(6) without time zone NOT NULL,
    originalsourcesystemuniqueid character varying(255) NOT NULL,
    value character varying(255) NOT NULL,
    activeflagid character varying(36) NOT NULL,
    enterpriseid character varying(36) NOT NULL,
    systemid character varying(36) NOT NULL,
    originalsourcesystemid character varying(36) NOT NULL,
    classificationid character varying(36) NOT NULL,
    eventid character varying(36) NOT NULL,
    rulesid character varying(36) NOT NULL
);


ALTER TABLE event.eventxrules OWNER TO postgres;

--
-- TOC entry 275 (class 1259 OID 28667)
-- Name: eventxrulessecuritytoken; Type: TABLE; Schema: event; Owner: postgres
--

CREATE TABLE event.eventxrulessecuritytoken (
    eventxrulessecuritytokenid character varying(36) NOT NULL,
    effectivefromdate timestamp(6) without time zone NOT NULL,
    effectivetodate timestamp(6) without time zone NOT NULL,
    warehousecreatedtimestamp timestamp(6) without time zone NOT NULL,
    warehouselastupdatedtimestamp timestamp(6) without time zone NOT NULL,
    createallowed integer NOT NULL,
    deleteallowed integer NOT NULL,
    originalsourcesystemuniqueid character varying(255) NOT NULL,
    readallowed integer NOT NULL,
    updateallowed integer NOT NULL,
    activeflagid character varying(36) NOT NULL,
    enterpriseid character varying(36) NOT NULL,
    originalsourcesystemid character varying(36) NOT NULL,
    securitytokenid character varying(36) NOT NULL,
    systemid character varying(36) NOT NULL,
    eventxrulesid character varying(36) NOT NULL
);


ALTER TABLE event.eventxrulessecuritytoken OWNER TO postgres;

--
-- TOC entry 276 (class 1259 OID 28674)
-- Name: geography; Type: TABLE; Schema: geography; Owner: postgres
--

CREATE TABLE geography.geography (
    geographyid character varying(36) NOT NULL,
    effectivefromdate timestamp(6) without time zone NOT NULL,
    effectivetodate timestamp(6) without time zone NOT NULL,
    warehousecreatedtimestamp timestamp(6) without time zone NOT NULL,
    warehouselastupdatedtimestamp timestamp(6) without time zone NOT NULL,
    originalsourcesystemuniqueid character varying(255) NOT NULL,
    geographydesc character varying(500) NOT NULL,
    geographyname character varying(500) NOT NULL,
    activeflagid character varying(36) NOT NULL,
    enterpriseid character varying(36) NOT NULL,
    systemid character varying(36) NOT NULL,
    originalsourcesystemid character varying(36) NOT NULL,
    classificationid character varying(36) NOT NULL
);


ALTER TABLE geography.geography OWNER TO postgres;

--
-- TOC entry 277 (class 1259 OID 28681)
-- Name: geographysecuritytoken; Type: TABLE; Schema: geography; Owner: postgres
--

CREATE TABLE geography.geographysecuritytoken (
    geographysecuritytokenid character varying(36) NOT NULL,
    effectivefromdate timestamp(6) without time zone NOT NULL,
    effectivetodate timestamp(6) without time zone NOT NULL,
    warehousecreatedtimestamp timestamp(6) without time zone NOT NULL,
    warehouselastupdatedtimestamp timestamp(6) without time zone NOT NULL,
    createallowed integer NOT NULL,
    deleteallowed integer NOT NULL,
    originalsourcesystemuniqueid character varying(255) NOT NULL,
    readallowed integer NOT NULL,
    updateallowed integer NOT NULL,
    activeflagid character varying(36) NOT NULL,
    enterpriseid character varying(36) NOT NULL,
    originalsourcesystemid character varying(36) NOT NULL,
    securitytokenid character varying(36) NOT NULL,
    systemid character varying(36) NOT NULL,
    geographyid character varying(36) NOT NULL
);


ALTER TABLE geography.geographysecuritytoken OWNER TO postgres;

--
-- TOC entry 278 (class 1259 OID 28688)
-- Name: geographyxclassification; Type: TABLE; Schema: geography; Owner: postgres
--

CREATE TABLE geography.geographyxclassification (
    geographyxclassificationid character varying(36) NOT NULL,
    effectivefromdate timestamp(6) without time zone NOT NULL,
    effectivetodate timestamp(6) without time zone NOT NULL,
    warehousecreatedtimestamp timestamp(6) without time zone NOT NULL,
    warehouselastupdatedtimestamp timestamp(6) without time zone NOT NULL,
    originalsourcesystemuniqueid character varying(255) NOT NULL,
    value character varying(255) NOT NULL,
    activeflagid character varying(36) NOT NULL,
    enterpriseid character varying(36) NOT NULL,
    systemid character varying(36) NOT NULL,
    originalsourcesystemid character varying(36) NOT NULL,
    classificationid character varying(36) NOT NULL,
    geographyid character varying(36) NOT NULL
);


ALTER TABLE geography.geographyxclassification OWNER TO postgres;

--
-- TOC entry 279 (class 1259 OID 28695)
-- Name: geographyxclassificationsecuritytoken; Type: TABLE; Schema: geography; Owner: postgres
--

CREATE TABLE geography.geographyxclassificationsecuritytoken (
    geographyxclassificationsecuritytokenid character varying(36) NOT NULL,
    effectivefromdate timestamp(6) without time zone NOT NULL,
    effectivetodate timestamp(6) without time zone NOT NULL,
    warehousecreatedtimestamp timestamp(6) without time zone NOT NULL,
    warehouselastupdatedtimestamp timestamp(6) without time zone NOT NULL,
    createallowed integer NOT NULL,
    deleteallowed integer NOT NULL,
    originalsourcesystemuniqueid character varying(255) NOT NULL,
    readallowed integer NOT NULL,
    updateallowed integer NOT NULL,
    activeflagid character varying(36) NOT NULL,
    enterpriseid character varying(36) NOT NULL,
    originalsourcesystemid character varying(36) NOT NULL,
    securitytokenid character varying(36) NOT NULL,
    systemid character varying(36) NOT NULL,
    geographyxclassificationid character varying(36) NOT NULL
);


ALTER TABLE geography.geographyxclassificationsecuritytoken OWNER TO postgres;

--
-- TOC entry 280 (class 1259 OID 28702)
-- Name: geographyxgeography; Type: TABLE; Schema: geography; Owner: postgres
--

CREATE TABLE geography.geographyxgeography (
    geographyxgeographyid character varying(36) NOT NULL,
    effectivefromdate timestamp(6) without time zone NOT NULL,
    effectivetodate timestamp(6) without time zone NOT NULL,
    warehousecreatedtimestamp timestamp(6) without time zone NOT NULL,
    warehouselastupdatedtimestamp timestamp(6) without time zone NOT NULL,
    originalsourcesystemuniqueid character varying(255) NOT NULL,
    value character varying(255) NOT NULL,
    activeflagid character varying(36) NOT NULL,
    enterpriseid character varying(36) NOT NULL,
    systemid character varying(36) NOT NULL,
    originalsourcesystemid character varying(36) NOT NULL,
    classificationid character varying(36) NOT NULL,
    childgeographyid character varying(36) NOT NULL,
    parentgeographyid character varying(36) NOT NULL
);


ALTER TABLE geography.geographyxgeography OWNER TO postgres;

--
-- TOC entry 281 (class 1259 OID 28709)
-- Name: geographyxgeographysecuritytoken; Type: TABLE; Schema: geography; Owner: postgres
--

CREATE TABLE geography.geographyxgeographysecuritytoken (
    geographyxgeographysecuritytokenid character varying(36) NOT NULL,
    effectivefromdate timestamp(6) without time zone NOT NULL,
    effectivetodate timestamp(6) without time zone NOT NULL,
    warehousecreatedtimestamp timestamp(6) without time zone NOT NULL,
    warehouselastupdatedtimestamp timestamp(6) without time zone NOT NULL,
    createallowed integer NOT NULL,
    deleteallowed integer NOT NULL,
    originalsourcesystemuniqueid character varying(255) NOT NULL,
    readallowed integer NOT NULL,
    updateallowed integer NOT NULL,
    activeflagid character varying(36) NOT NULL,
    enterpriseid character varying(36) NOT NULL,
    originalsourcesystemid character varying(36) NOT NULL,
    securitytokenid character varying(36) NOT NULL,
    systemid character varying(36) NOT NULL,
    geographyxgeographyid character varying(36) NOT NULL
);


ALTER TABLE geography.geographyxgeographysecuritytoken OWNER TO postgres;

--
-- TOC entry 282 (class 1259 OID 28716)
-- Name: geographyxresourceitem; Type: TABLE; Schema: geography; Owner: postgres
--

CREATE TABLE geography.geographyxresourceitem (
    geographyxresourceitemid character varying(36) NOT NULL,
    effectivefromdate timestamp(6) without time zone NOT NULL,
    effectivetodate timestamp(6) without time zone NOT NULL,
    warehousecreatedtimestamp timestamp(6) without time zone NOT NULL,
    warehouselastupdatedtimestamp timestamp(6) without time zone NOT NULL,
    originalsourcesystemuniqueid character varying(255) NOT NULL,
    value character varying(255) NOT NULL,
    activeflagid character varying(36) NOT NULL,
    enterpriseid character varying(36) NOT NULL,
    systemid character varying(36) NOT NULL,
    originalsourcesystemid character varying(36) NOT NULL,
    classificationid character varying(36) NOT NULL,
    geographyid character varying(36) NOT NULL,
    resourceitemid character varying(36) NOT NULL
);


ALTER TABLE geography.geographyxresourceitem OWNER TO postgres;

--
-- TOC entry 283 (class 1259 OID 28723)
-- Name: geographyxresourceitemsecuritytoken; Type: TABLE; Schema: geography; Owner: postgres
--

CREATE TABLE geography.geographyxresourceitemsecuritytoken (
    geographyxresourceitemsecuritytokenid character varying(36) NOT NULL,
    effectivefromdate timestamp(6) without time zone NOT NULL,
    effectivetodate timestamp(6) without time zone NOT NULL,
    warehousecreatedtimestamp timestamp(6) without time zone NOT NULL,
    warehouselastupdatedtimestamp timestamp(6) without time zone NOT NULL,
    createallowed integer NOT NULL,
    deleteallowed integer NOT NULL,
    originalsourcesystemuniqueid character varying(255) NOT NULL,
    readallowed integer NOT NULL,
    updateallowed integer NOT NULL,
    activeflagid character varying(36) NOT NULL,
    enterpriseid character varying(36) NOT NULL,
    originalsourcesystemid character varying(36) NOT NULL,
    securitytokenid character varying(36) NOT NULL,
    systemid character varying(36) NOT NULL,
    geographyxresourceitemid character varying(36) NOT NULL
);


ALTER TABLE geography.geographyxresourceitemsecuritytoken OWNER TO postgres;

--
-- TOC entry 284 (class 1259 OID 28730)
-- Name: involvedparty; Type: TABLE; Schema: party; Owner: postgres
--

CREATE TABLE party.involvedparty (
    involvedpartyid character varying(36) NOT NULL,
    effectivefromdate timestamp(6) without time zone NOT NULL,
    effectivetodate timestamp(6) without time zone NOT NULL,
    warehousecreatedtimestamp timestamp(6) without time zone NOT NULL,
    warehouselastupdatedtimestamp timestamp(6) without time zone NOT NULL,
    originalsourcesystemuniqueid character varying(255) NOT NULL,
    activeflagid character varying(36) NOT NULL,
    enterpriseid character varying(36) NOT NULL,
    systemid character varying(36) NOT NULL,
    originalsourcesystemid character varying(36) NOT NULL
);


ALTER TABLE party.involvedparty OWNER TO postgres;

--
-- TOC entry 285 (class 1259 OID 28735)
-- Name: involvedpartyidentificationtype; Type: TABLE; Schema: party; Owner: postgres
--

CREATE TABLE party.involvedpartyidentificationtype (
    involvedpartyidentificationtypeid character varying(36) NOT NULL,
    effectivefromdate timestamp(6) without time zone NOT NULL,
    effectivetodate timestamp(6) without time zone NOT NULL,
    warehousecreatedtimestamp timestamp(6) without time zone NOT NULL,
    warehouselastupdatedtimestamp timestamp(6) without time zone NOT NULL,
    originalsourcesystemuniqueid character varying(255) NOT NULL,
    involvedpartyidentificationdesc character varying(500) NOT NULL,
    involvedpartyidentificationname character varying(150) NOT NULL,
    activeflagid character varying(36) NOT NULL,
    enterpriseid character varying(36) NOT NULL,
    systemid character varying(36) NOT NULL,
    originalsourcesystemid character varying(36) NOT NULL
);


ALTER TABLE party.involvedpartyidentificationtype OWNER TO postgres;

--
-- TOC entry 286 (class 1259 OID 28742)
-- Name: involvedpartyidentificationtypesecuritytoken; Type: TABLE; Schema: party; Owner: postgres
--

CREATE TABLE party.involvedpartyidentificationtypesecuritytoken (
    involvedpartyidentificationtypesecuritytokenid character varying(36) NOT NULL,
    effectivefromdate timestamp(6) without time zone NOT NULL,
    effectivetodate timestamp(6) without time zone NOT NULL,
    warehousecreatedtimestamp timestamp(6) without time zone NOT NULL,
    warehouselastupdatedtimestamp timestamp(6) without time zone NOT NULL,
    createallowed integer NOT NULL,
    deleteallowed integer NOT NULL,
    originalsourcesystemuniqueid character varying(255) NOT NULL,
    readallowed integer NOT NULL,
    updateallowed integer NOT NULL,
    activeflagid character varying(36) NOT NULL,
    enterpriseid character varying(36) NOT NULL,
    originalsourcesystemid character varying(36) NOT NULL,
    securitytokenid character varying(36) NOT NULL,
    systemid character varying(36) NOT NULL,
    involvedpartyidentificationtypeid character varying(36) NOT NULL
);


ALTER TABLE party.involvedpartyidentificationtypesecuritytoken OWNER TO postgres;

--
-- TOC entry 287 (class 1259 OID 28749)
-- Name: involvedpartynametype; Type: TABLE; Schema: party; Owner: postgres
--

CREATE TABLE party.involvedpartynametype (
    involvedpartynametypeid character varying(36) NOT NULL,
    effectivefromdate timestamp(6) without time zone NOT NULL,
    effectivetodate timestamp(6) without time zone NOT NULL,
    warehousecreatedtimestamp timestamp(6) without time zone NOT NULL,
    warehouselastupdatedtimestamp timestamp(6) without time zone NOT NULL,
    originalsourcesystemuniqueid character varying(255) NOT NULL,
    involvedpartynametypedescr character varying(500) NOT NULL,
    involvedpartynametypename character varying(500) NOT NULL,
    activeflagid character varying(36) NOT NULL,
    enterpriseid character varying(36) NOT NULL,
    systemid character varying(36) NOT NULL,
    originalsourcesystemid character varying(36) NOT NULL
);


ALTER TABLE party.involvedpartynametype OWNER TO postgres;

--
-- TOC entry 288 (class 1259 OID 28756)
-- Name: involvedpartynametypesecuritytoken; Type: TABLE; Schema: party; Owner: postgres
--

CREATE TABLE party.involvedpartynametypesecuritytoken (
    involvedpartynametypesecuritytokenid character varying(36) NOT NULL,
    effectivefromdate timestamp(6) without time zone NOT NULL,
    effectivetodate timestamp(6) without time zone NOT NULL,
    warehousecreatedtimestamp timestamp(6) without time zone NOT NULL,
    warehouselastupdatedtimestamp timestamp(6) without time zone NOT NULL,
    createallowed integer NOT NULL,
    deleteallowed integer NOT NULL,
    originalsourcesystemuniqueid character varying(255) NOT NULL,
    readallowed integer NOT NULL,
    updateallowed integer NOT NULL,
    activeflagid character varying(36) NOT NULL,
    enterpriseid character varying(36) NOT NULL,
    originalsourcesystemid character varying(36) NOT NULL,
    securitytokenid character varying(36) NOT NULL,
    systemid character varying(36) NOT NULL,
    involvedpartynametypeid character varying(36) NOT NULL
);


ALTER TABLE party.involvedpartynametypesecuritytoken OWNER TO postgres;

--
-- TOC entry 289 (class 1259 OID 28763)
-- Name: involvedpartynonorganic; Type: TABLE; Schema: party; Owner: postgres
--

CREATE TABLE party.involvedpartynonorganic (
    involvedpartynonorganicid character varying(36) NOT NULL,
    effectivefromdate timestamp(6) without time zone NOT NULL,
    effectivetodate timestamp(6) without time zone NOT NULL,
    warehousecreatedtimestamp timestamp(6) without time zone NOT NULL,
    warehouselastupdatedtimestamp timestamp(6) without time zone NOT NULL,
    originalsourcesystemuniqueid character varying(255) NOT NULL,
    activeflagid character varying(36) NOT NULL,
    enterpriseid character varying(36) NOT NULL,
    systemid character varying(36) NOT NULL,
    originalsourcesystemid character varying(36) NOT NULL
);


ALTER TABLE party.involvedpartynonorganic OWNER TO postgres;

--
-- TOC entry 290 (class 1259 OID 28768)
-- Name: involvedpartynonorganicsecuritytoken; Type: TABLE; Schema: party; Owner: postgres
--

CREATE TABLE party.involvedpartynonorganicsecuritytoken (
    involvedpartynonorganicsecuritytokenid character varying(36) NOT NULL,
    effectivefromdate timestamp(6) without time zone NOT NULL,
    effectivetodate timestamp(6) without time zone NOT NULL,
    warehousecreatedtimestamp timestamp(6) without time zone NOT NULL,
    warehouselastupdatedtimestamp timestamp(6) without time zone NOT NULL,
    createallowed integer NOT NULL,
    deleteallowed integer NOT NULL,
    originalsourcesystemuniqueid character varying(255) NOT NULL,
    readallowed integer NOT NULL,
    updateallowed integer NOT NULL,
    activeflagid character varying(36) NOT NULL,
    enterpriseid character varying(36) NOT NULL,
    originalsourcesystemid character varying(36) NOT NULL,
    securitytokenid character varying(36) NOT NULL,
    systemid character varying(36) NOT NULL,
    involvedpartynonorganicid character varying(36) NOT NULL
);


ALTER TABLE party.involvedpartynonorganicsecuritytoken OWNER TO postgres;

--
-- TOC entry 291 (class 1259 OID 28775)
-- Name: involvedpartyorganic; Type: TABLE; Schema: party; Owner: postgres
--

CREATE TABLE party.involvedpartyorganic (
    involvedpartyorganicid character varying(36) NOT NULL,
    effectivefromdate timestamp(6) without time zone NOT NULL,
    effectivetodate timestamp(6) without time zone NOT NULL,
    warehousecreatedtimestamp timestamp(6) without time zone NOT NULL,
    warehouselastupdatedtimestamp timestamp(6) without time zone NOT NULL,
    originalsourcesystemuniqueid character varying(255) NOT NULL,
    activeflagid character varying(36) NOT NULL,
    enterpriseid character varying(36) NOT NULL,
    systemid character varying(36) NOT NULL,
    originalsourcesystemid character varying(36) NOT NULL
);


ALTER TABLE party.involvedpartyorganic OWNER TO postgres;

--
-- TOC entry 292 (class 1259 OID 28780)
-- Name: involvedpartyorganicsecuritytoken; Type: TABLE; Schema: party; Owner: postgres
--

CREATE TABLE party.involvedpartyorganicsecuritytoken (
    involvedpartyorganicsecuritytokenid character varying(36) NOT NULL,
    effectivefromdate timestamp(6) without time zone NOT NULL,
    effectivetodate timestamp(6) without time zone NOT NULL,
    warehousecreatedtimestamp timestamp(6) without time zone NOT NULL,
    warehouselastupdatedtimestamp timestamp(6) without time zone NOT NULL,
    createallowed integer NOT NULL,
    deleteallowed integer NOT NULL,
    originalsourcesystemuniqueid character varying(255) NOT NULL,
    readallowed integer NOT NULL,
    updateallowed integer NOT NULL,
    activeflagid character varying(36) NOT NULL,
    enterpriseid character varying(36) NOT NULL,
    originalsourcesystemid character varying(36) NOT NULL,
    securitytokenid character varying(36) NOT NULL,
    systemid character varying(36) NOT NULL,
    involvedpartyorganicid character varying(36) NOT NULL
);


ALTER TABLE party.involvedpartyorganicsecuritytoken OWNER TO postgres;

--
-- TOC entry 293 (class 1259 OID 28787)
-- Name: involvedpartyorganictype; Type: TABLE; Schema: party; Owner: postgres
--

CREATE TABLE party.involvedpartyorganictype (
    involvedpartyorganictypeid character varying(36) NOT NULL,
    effectivefromdate timestamp(6) without time zone NOT NULL,
    effectivetodate timestamp(6) without time zone NOT NULL,
    warehousecreatedtimestamp timestamp(6) without time zone NOT NULL,
    warehouselastupdatedtimestamp timestamp(6) without time zone NOT NULL,
    originalsourcesystemuniqueid character varying(255) NOT NULL,
    involvedpartytypedesc character varying(500) NOT NULL,
    involvedpartytypename character varying(200) NOT NULL,
    activeflagid character varying(36) NOT NULL,
    enterpriseid character varying(36) NOT NULL,
    systemid character varying(36) NOT NULL,
    originalsourcesystemid character varying(36) NOT NULL
);


ALTER TABLE party.involvedpartyorganictype OWNER TO postgres;

--
-- TOC entry 294 (class 1259 OID 28794)
-- Name: involvedpartyorganictypesecuritytoken; Type: TABLE; Schema: party; Owner: postgres
--

CREATE TABLE party.involvedpartyorganictypesecuritytoken (
    involvedpartyorganictypesecuritytokenid character varying(36) NOT NULL,
    effectivefromdate timestamp(6) without time zone NOT NULL,
    effectivetodate timestamp(6) without time zone NOT NULL,
    warehousecreatedtimestamp timestamp(6) without time zone NOT NULL,
    warehouselastupdatedtimestamp timestamp(6) without time zone NOT NULL,
    createallowed integer NOT NULL,
    deleteallowed integer NOT NULL,
    originalsourcesystemuniqueid character varying(255) NOT NULL,
    readallowed integer NOT NULL,
    updateallowed integer NOT NULL,
    activeflagid character varying(36) NOT NULL,
    enterpriseid character varying(36) NOT NULL,
    originalsourcesystemid character varying(36) NOT NULL,
    securitytokenid character varying(36) NOT NULL,
    systemid character varying(36) NOT NULL,
    involvedpartyorganictypeid character varying(36) NOT NULL
);


ALTER TABLE party.involvedpartyorganictypesecuritytoken OWNER TO postgres;

--
-- TOC entry 295 (class 1259 OID 28801)
-- Name: involvedpartysecuritytoken; Type: TABLE; Schema: party; Owner: postgres
--

CREATE TABLE party.involvedpartysecuritytoken (
    involvedpartysecuritytokenid character varying(36) NOT NULL,
    effectivefromdate timestamp(6) without time zone NOT NULL,
    effectivetodate timestamp(6) without time zone NOT NULL,
    warehousecreatedtimestamp timestamp(6) without time zone NOT NULL,
    warehouselastupdatedtimestamp timestamp(6) without time zone NOT NULL,
    createallowed integer NOT NULL,
    deleteallowed integer NOT NULL,
    originalsourcesystemuniqueid character varying(255) NOT NULL,
    readallowed integer NOT NULL,
    updateallowed integer NOT NULL,
    activeflagid character varying(36) NOT NULL,
    enterpriseid character varying(36) NOT NULL,
    originalsourcesystemid character varying(36) NOT NULL,
    securitytokenid character varying(36) NOT NULL,
    systemid character varying(36) NOT NULL,
    involvedpartyid character varying(36) NOT NULL
);


ALTER TABLE party.involvedpartysecuritytoken OWNER TO postgres;

--
-- TOC entry 296 (class 1259 OID 28808)
-- Name: involvedpartytype; Type: TABLE; Schema: party; Owner: postgres
--

CREATE TABLE party.involvedpartytype (
    involvedpartytypeid character varying(36) NOT NULL,
    effectivefromdate timestamp(6) without time zone NOT NULL,
    effectivetodate timestamp(6) without time zone NOT NULL,
    warehousecreatedtimestamp timestamp(6) without time zone NOT NULL,
    warehouselastupdatedtimestamp timestamp(6) without time zone NOT NULL,
    originalsourcesystemuniqueid character varying(255) NOT NULL,
    involvedpartytypedesc character varying(255) NOT NULL,
    involvedpartytypename character varying(100) NOT NULL,
    activeflagid character varying(36) NOT NULL,
    enterpriseid character varying(36) NOT NULL,
    systemid character varying(36) NOT NULL,
    originalsourcesystemid character varying(36) NOT NULL
);


ALTER TABLE party.involvedpartytype OWNER TO postgres;

--
-- TOC entry 297 (class 1259 OID 28815)
-- Name: involvedpartytypesecuritytoken; Type: TABLE; Schema: party; Owner: postgres
--

CREATE TABLE party.involvedpartytypesecuritytoken (
    involvedpartytypesecuritytokenid character varying(36) NOT NULL,
    effectivefromdate timestamp(6) without time zone NOT NULL,
    effectivetodate timestamp(6) without time zone NOT NULL,
    warehousecreatedtimestamp timestamp(6) without time zone NOT NULL,
    warehouselastupdatedtimestamp timestamp(6) without time zone NOT NULL,
    createallowed integer NOT NULL,
    deleteallowed integer NOT NULL,
    originalsourcesystemuniqueid character varying(255) NOT NULL,
    readallowed integer NOT NULL,
    updateallowed integer NOT NULL,
    activeflagid character varying(36) NOT NULL,
    enterpriseid character varying(36) NOT NULL,
    originalsourcesystemid character varying(36) NOT NULL,
    securitytokenid character varying(36) NOT NULL,
    systemid character varying(36) NOT NULL,
    involvedpartytypeid character varying(36) NOT NULL
);


ALTER TABLE party.involvedpartytypesecuritytoken OWNER TO postgres;

--
-- TOC entry 298 (class 1259 OID 28822)
-- Name: involvedpartyxaddress; Type: TABLE; Schema: party; Owner: postgres
--

CREATE TABLE party.involvedpartyxaddress (
    involvedpartyxaddressid character varying(36) NOT NULL,
    effectivefromdate timestamp(6) without time zone NOT NULL,
    effectivetodate timestamp(6) without time zone NOT NULL,
    warehousecreatedtimestamp timestamp(6) without time zone NOT NULL,
    warehouselastupdatedtimestamp timestamp(6) without time zone NOT NULL,
    originalsourcesystemuniqueid character varying(255) NOT NULL,
    value character varying(255) NOT NULL,
    activeflagid character varying(36) NOT NULL,
    enterpriseid character varying(36) NOT NULL,
    systemid character varying(36) NOT NULL,
    originalsourcesystemid character varying(36) NOT NULL,
    classificationid character varying(36) NOT NULL,
    addressid character varying(36) NOT NULL,
    involvedpartyid character varying(36) NOT NULL
);


ALTER TABLE party.involvedpartyxaddress OWNER TO postgres;

--
-- TOC entry 299 (class 1259 OID 28829)
-- Name: involvedpartyxaddresssecuritytoken; Type: TABLE; Schema: party; Owner: postgres
--

CREATE TABLE party.involvedpartyxaddresssecuritytoken (
    involvedpartyxaddresssecuritytokenid character varying(36) NOT NULL,
    effectivefromdate timestamp(6) without time zone NOT NULL,
    effectivetodate timestamp(6) without time zone NOT NULL,
    warehousecreatedtimestamp timestamp(6) without time zone NOT NULL,
    warehouselastupdatedtimestamp timestamp(6) without time zone NOT NULL,
    createallowed integer NOT NULL,
    deleteallowed integer NOT NULL,
    originalsourcesystemuniqueid character varying(255) NOT NULL,
    readallowed integer NOT NULL,
    updateallowed integer NOT NULL,
    activeflagid character varying(36) NOT NULL,
    enterpriseid character varying(36) NOT NULL,
    originalsourcesystemid character varying(36) NOT NULL,
    securitytokenid character varying(36) NOT NULL,
    systemid character varying(36) NOT NULL,
    involvedpartyxaddressid character varying(36) NOT NULL
);


ALTER TABLE party.involvedpartyxaddresssecuritytoken OWNER TO postgres;

--
-- TOC entry 300 (class 1259 OID 28836)
-- Name: involvedpartyxclassification; Type: TABLE; Schema: party; Owner: postgres
--

CREATE TABLE party.involvedpartyxclassification (
    involvedpartyxclassificationid character varying(36) NOT NULL,
    effectivefromdate timestamp(6) without time zone NOT NULL,
    effectivetodate timestamp(6) without time zone NOT NULL,
    warehousecreatedtimestamp timestamp(6) without time zone NOT NULL,
    warehouselastupdatedtimestamp timestamp(6) without time zone NOT NULL,
    originalsourcesystemuniqueid character varying(255) NOT NULL,
    value character varying(255) NOT NULL,
    activeflagid character varying(36) NOT NULL,
    enterpriseid character varying(36) NOT NULL,
    systemid character varying(36) NOT NULL,
    originalsourcesystemid character varying(36) NOT NULL,
    classificationid character varying(36) NOT NULL,
    involvedpartyid character varying(36) NOT NULL
);


ALTER TABLE party.involvedpartyxclassification OWNER TO postgres;

--
-- TOC entry 301 (class 1259 OID 28843)
-- Name: involvedpartyxclassificationsecuritytoken; Type: TABLE; Schema: party; Owner: postgres
--

CREATE TABLE party.involvedpartyxclassificationsecuritytoken (
    involvedpartyxclassificationsecuritytokenid character varying(36) NOT NULL,
    effectivefromdate timestamp(6) without time zone NOT NULL,
    effectivetodate timestamp(6) without time zone NOT NULL,
    warehousecreatedtimestamp timestamp(6) without time zone NOT NULL,
    warehouselastupdatedtimestamp timestamp(6) without time zone NOT NULL,
    createallowed integer NOT NULL,
    deleteallowed integer NOT NULL,
    originalsourcesystemuniqueid character varying(255) NOT NULL,
    readallowed integer NOT NULL,
    updateallowed integer NOT NULL,
    activeflagid character varying(36) NOT NULL,
    enterpriseid character varying(36) NOT NULL,
    originalsourcesystemid character varying(36) NOT NULL,
    securitytokenid character varying(36) NOT NULL,
    systemid character varying(36) NOT NULL,
    involvedpartyxclassificationid character varying(36) NOT NULL
);


ALTER TABLE party.involvedpartyxclassificationsecuritytoken OWNER TO postgres;

--
-- TOC entry 302 (class 1259 OID 28850)
-- Name: involvedpartyxinvolvedparty; Type: TABLE; Schema: party; Owner: postgres
--

CREATE TABLE party.involvedpartyxinvolvedparty (
    involvedpartyxinvolvedpartyid character varying(36) NOT NULL,
    effectivefromdate timestamp(6) without time zone NOT NULL,
    effectivetodate timestamp(6) without time zone NOT NULL,
    warehousecreatedtimestamp timestamp(6) without time zone NOT NULL,
    warehouselastupdatedtimestamp timestamp(6) without time zone NOT NULL,
    originalsourcesystemuniqueid character varying(255) NOT NULL,
    value character varying(255) NOT NULL,
    activeflagid character varying(36) NOT NULL,
    enterpriseid character varying(36) NOT NULL,
    systemid character varying(36) NOT NULL,
    originalsourcesystemid character varying(36) NOT NULL,
    classificationid character varying(36) NOT NULL,
    childinvolvedpartyid character varying(36) NOT NULL,
    parentinvolvedpartyid character varying(36) NOT NULL
);


ALTER TABLE party.involvedpartyxinvolvedparty OWNER TO postgres;

--
-- TOC entry 303 (class 1259 OID 28857)
-- Name: involvedpartyxinvolvedpartyidentificationtype; Type: TABLE; Schema: party; Owner: postgres
--

CREATE TABLE party.involvedpartyxinvolvedpartyidentificationtype (
    involvedpartyxinvolvedpartyidentificationtypeid character varying(36) NOT NULL,
    effectivefromdate timestamp(6) without time zone NOT NULL,
    effectivetodate timestamp(6) without time zone NOT NULL,
    warehousecreatedtimestamp timestamp(6) without time zone NOT NULL,
    warehouselastupdatedtimestamp timestamp(6) without time zone NOT NULL,
    originalsourcesystemuniqueid character varying(255) NOT NULL,
    value character varying(255) NOT NULL,
    activeflagid character varying(36) NOT NULL,
    enterpriseid character varying(36) NOT NULL,
    systemid character varying(36) NOT NULL,
    originalsourcesystemid character varying(36) NOT NULL,
    classificationid character varying(36) NOT NULL,
    involvedpartyid character varying(36) NOT NULL,
    involvedpartyidentificationtypeid character varying(36) NOT NULL
);


ALTER TABLE party.involvedpartyxinvolvedpartyidentificationtype OWNER TO postgres;

--
-- TOC entry 304 (class 1259 OID 28864)
-- Name: involvedpartyxinvolvedpartyidentificationtypesecuritytoken; Type: TABLE; Schema: party; Owner: postgres
--

CREATE TABLE party.involvedpartyxinvolvedpartyidentificationtypesecuritytoken (
    involvedpartyxinvolvedpartyidentificationtypesecuritytokenid character varying(36) NOT NULL,
    effectivefromdate timestamp(6) without time zone NOT NULL,
    effectivetodate timestamp(6) without time zone NOT NULL,
    warehousecreatedtimestamp timestamp(6) without time zone NOT NULL,
    warehouselastupdatedtimestamp timestamp(6) without time zone NOT NULL,
    createallowed integer NOT NULL,
    deleteallowed integer NOT NULL,
    originalsourcesystemuniqueid character varying(255) NOT NULL,
    readallowed integer NOT NULL,
    updateallowed integer NOT NULL,
    activeflagid character varying(36) NOT NULL,
    enterpriseid character varying(36) NOT NULL,
    originalsourcesystemid character varying(36) NOT NULL,
    securitytokenid character varying(36) NOT NULL,
    systemid character varying(36) NOT NULL,
    involvedpartyxinvolvedpartyidentificationtypeid character varying(36) NOT NULL
);


ALTER TABLE party.involvedpartyxinvolvedpartyidentificationtypesecuritytoken OWNER TO postgres;

--
-- TOC entry 305 (class 1259 OID 28871)
-- Name: involvedpartyxinvolvedpartynametype; Type: TABLE; Schema: party; Owner: postgres
--

CREATE TABLE party.involvedpartyxinvolvedpartynametype (
    involvedpartyxinvolvedpartynametypeid character varying(36) NOT NULL,
    effectivefromdate timestamp(6) without time zone NOT NULL,
    effectivetodate timestamp(6) without time zone NOT NULL,
    warehousecreatedtimestamp timestamp(6) without time zone NOT NULL,
    warehouselastupdatedtimestamp timestamp(6) without time zone NOT NULL,
    originalsourcesystemuniqueid character varying(255) NOT NULL,
    value character varying(255) NOT NULL,
    activeflagid character varying(36) NOT NULL,
    enterpriseid character varying(36) NOT NULL,
    systemid character varying(36) NOT NULL,
    originalsourcesystemid character varying(36) NOT NULL,
    classificationid character varying(36) NOT NULL,
    involvedpartyid character varying(36) NOT NULL,
    involvedpartynametypeid character varying(36) NOT NULL
);


ALTER TABLE party.involvedpartyxinvolvedpartynametype OWNER TO postgres;

--
-- TOC entry 306 (class 1259 OID 28878)
-- Name: involvedpartyxinvolvedpartynametypesecuritytoken; Type: TABLE; Schema: party; Owner: postgres
--

CREATE TABLE party.involvedpartyxinvolvedpartynametypesecuritytoken (
    involvedpartyxinvolvedpartynametypesecuritytokenid character varying(36) NOT NULL,
    effectivefromdate timestamp(6) without time zone NOT NULL,
    effectivetodate timestamp(6) without time zone NOT NULL,
    warehousecreatedtimestamp timestamp(6) without time zone NOT NULL,
    warehouselastupdatedtimestamp timestamp(6) without time zone NOT NULL,
    createallowed integer NOT NULL,
    deleteallowed integer NOT NULL,
    originalsourcesystemuniqueid character varying(255) NOT NULL,
    readallowed integer NOT NULL,
    updateallowed integer NOT NULL,
    activeflagid character varying(36) NOT NULL,
    enterpriseid character varying(36) NOT NULL,
    originalsourcesystemid character varying(36) NOT NULL,
    securitytokenid character varying(36) NOT NULL,
    systemid character varying(36) NOT NULL,
    involvedpartyxinvolvedpartynametypeid character varying(36) NOT NULL
);


ALTER TABLE party.involvedpartyxinvolvedpartynametypesecuritytoken OWNER TO postgres;

--
-- TOC entry 307 (class 1259 OID 28885)
-- Name: involvedpartyxinvolvedpartysecuritytoken; Type: TABLE; Schema: party; Owner: postgres
--

CREATE TABLE party.involvedpartyxinvolvedpartysecuritytoken (
    involvedpartyxinvolvedpartysecuritytokenid character varying(36) NOT NULL,
    effectivefromdate timestamp(6) without time zone NOT NULL,
    effectivetodate timestamp(6) without time zone NOT NULL,
    warehousecreatedtimestamp timestamp(6) without time zone NOT NULL,
    warehouselastupdatedtimestamp timestamp(6) without time zone NOT NULL,
    createallowed integer NOT NULL,
    deleteallowed integer NOT NULL,
    originalsourcesystemuniqueid character varying(255) NOT NULL,
    readallowed integer NOT NULL,
    updateallowed integer NOT NULL,
    activeflagid character varying(36) NOT NULL,
    enterpriseid character varying(36) NOT NULL,
    originalsourcesystemid character varying(36) NOT NULL,
    securitytokenid character varying(36) NOT NULL,
    systemid character varying(36) NOT NULL,
    involvedpartyxinvolvedpartyid character varying(36) NOT NULL
);


ALTER TABLE party.involvedpartyxinvolvedpartysecuritytoken OWNER TO postgres;

--
-- TOC entry 308 (class 1259 OID 28892)
-- Name: involvedpartyxinvolvedpartytype; Type: TABLE; Schema: party; Owner: postgres
--

CREATE TABLE party.involvedpartyxinvolvedpartytype (
    involvedpartyxinvolvedpartytypeid character varying(36) NOT NULL,
    effectivefromdate timestamp(6) without time zone NOT NULL,
    effectivetodate timestamp(6) without time zone NOT NULL,
    warehousecreatedtimestamp timestamp(6) without time zone NOT NULL,
    warehouselastupdatedtimestamp timestamp(6) without time zone NOT NULL,
    originalsourcesystemuniqueid character varying(255) NOT NULL,
    value character varying(255) NOT NULL,
    activeflagid character varying(36) NOT NULL,
    enterpriseid character varying(36) NOT NULL,
    systemid character varying(36) NOT NULL,
    originalsourcesystemid character varying(36) NOT NULL,
    classificationid character varying(36) NOT NULL,
    involvedpartyid character varying(36) NOT NULL,
    involvedpartytypeid character varying(36) NOT NULL
);


ALTER TABLE party.involvedpartyxinvolvedpartytype OWNER TO postgres;

--
-- TOC entry 309 (class 1259 OID 28899)
-- Name: involvedpartyxinvolvedpartytypesecuritytoken; Type: TABLE; Schema: party; Owner: postgres
--

CREATE TABLE party.involvedpartyxinvolvedpartytypesecuritytoken (
    involvedpartyxinvolvedpartytypesecuritytokenid character varying(36) NOT NULL,
    effectivefromdate timestamp(6) without time zone NOT NULL,
    effectivetodate timestamp(6) without time zone NOT NULL,
    warehousecreatedtimestamp timestamp(6) without time zone NOT NULL,
    warehouselastupdatedtimestamp timestamp(6) without time zone NOT NULL,
    createallowed integer NOT NULL,
    deleteallowed integer NOT NULL,
    originalsourcesystemuniqueid character varying(255) NOT NULL,
    readallowed integer NOT NULL,
    updateallowed integer NOT NULL,
    activeflagid character varying(36) NOT NULL,
    enterpriseid character varying(36) NOT NULL,
    originalsourcesystemid character varying(36) NOT NULL,
    securitytokenid character varying(36) NOT NULL,
    systemid character varying(36) NOT NULL,
    involvedpartyxinvolvedpartytypeid character varying(36) NOT NULL
);


ALTER TABLE party.involvedpartyxinvolvedpartytypesecuritytoken OWNER TO postgres;

--
-- TOC entry 310 (class 1259 OID 28906)
-- Name: involvedpartyxproduct; Type: TABLE; Schema: party; Owner: postgres
--

CREATE TABLE party.involvedpartyxproduct (
    involvedpartyxproductid character varying(36) NOT NULL,
    effectivefromdate timestamp(6) without time zone NOT NULL,
    effectivetodate timestamp(6) without time zone NOT NULL,
    warehousecreatedtimestamp timestamp(6) without time zone NOT NULL,
    warehouselastupdatedtimestamp timestamp(6) without time zone NOT NULL,
    originalsourcesystemuniqueid character varying(255) NOT NULL,
    value character varying(255) NOT NULL,
    activeflagid character varying(36) NOT NULL,
    enterpriseid character varying(36) NOT NULL,
    systemid character varying(36) NOT NULL,
    originalsourcesystemid character varying(36) NOT NULL,
    classificationid character varying(36) NOT NULL,
    involvedpartyid character varying(36) NOT NULL,
    productid character varying(36) NOT NULL
);


ALTER TABLE party.involvedpartyxproduct OWNER TO postgres;

--
-- TOC entry 311 (class 1259 OID 28913)
-- Name: involvedpartyxproductsecuritytoken; Type: TABLE; Schema: party; Owner: postgres
--

CREATE TABLE party.involvedpartyxproductsecuritytoken (
    involvedpartyxproductsecuritytokenid character varying(36) NOT NULL,
    effectivefromdate timestamp(6) without time zone NOT NULL,
    effectivetodate timestamp(6) without time zone NOT NULL,
    warehousecreatedtimestamp timestamp(6) without time zone NOT NULL,
    warehouselastupdatedtimestamp timestamp(6) without time zone NOT NULL,
    createallowed integer NOT NULL,
    deleteallowed integer NOT NULL,
    originalsourcesystemuniqueid character varying(255) NOT NULL,
    readallowed integer NOT NULL,
    updateallowed integer NOT NULL,
    activeflagid character varying(36) NOT NULL,
    enterpriseid character varying(36) NOT NULL,
    originalsourcesystemid character varying(36) NOT NULL,
    securitytokenid character varying(36) NOT NULL,
    systemid character varying(36) NOT NULL,
    involvedpartyxproductid character varying(36) NOT NULL
);


ALTER TABLE party.involvedpartyxproductsecuritytoken OWNER TO postgres;

--
-- TOC entry 312 (class 1259 OID 28920)
-- Name: involvedpartyxproducttype; Type: TABLE; Schema: party; Owner: postgres
--

CREATE TABLE party.involvedpartyxproducttype (
    involvedpartyxproducttypeid character varying(36) NOT NULL,
    effectivefromdate timestamp(6) without time zone NOT NULL,
    effectivetodate timestamp(6) without time zone NOT NULL,
    warehousecreatedtimestamp timestamp(6) without time zone NOT NULL,
    warehouselastupdatedtimestamp timestamp(6) without time zone NOT NULL,
    originalsourcesystemuniqueid character varying(255) NOT NULL,
    value character varying(255) NOT NULL,
    activeflagid character varying(36) NOT NULL,
    enterpriseid character varying(36) NOT NULL,
    systemid character varying(36) NOT NULL,
    originalsourcesystemid character varying(36) NOT NULL,
    classificationid character varying(36) NOT NULL,
    involvedpartyid character varying(36) NOT NULL,
    producttypeid character varying(36) NOT NULL
);


ALTER TABLE party.involvedpartyxproducttype OWNER TO postgres;

--
-- TOC entry 313 (class 1259 OID 28927)
-- Name: involvedpartyxproducttypesecuritytoken; Type: TABLE; Schema: party; Owner: postgres
--

CREATE TABLE party.involvedpartyxproducttypesecuritytoken (
    involvedpartyxproducttypesecuritytokenid character varying(36) NOT NULL,
    effectivefromdate timestamp(6) without time zone NOT NULL,
    effectivetodate timestamp(6) without time zone NOT NULL,
    warehousecreatedtimestamp timestamp(6) without time zone NOT NULL,
    warehouselastupdatedtimestamp timestamp(6) without time zone NOT NULL,
    createallowed integer NOT NULL,
    deleteallowed integer NOT NULL,
    originalsourcesystemuniqueid character varying(255) NOT NULL,
    readallowed integer NOT NULL,
    updateallowed integer NOT NULL,
    activeflagid character varying(36) NOT NULL,
    enterpriseid character varying(36) NOT NULL,
    originalsourcesystemid character varying(36) NOT NULL,
    securitytokenid character varying(36) NOT NULL,
    systemid character varying(36) NOT NULL,
    involvedpartyxproducttypeid character varying(36) NOT NULL
);


ALTER TABLE party.involvedpartyxproducttypesecuritytoken OWNER TO postgres;

--
-- TOC entry 314 (class 1259 OID 28934)
-- Name: involvedpartyxresourceitem; Type: TABLE; Schema: party; Owner: postgres
--

CREATE TABLE party.involvedpartyxresourceitem (
    involvedpartyxresourceitemid character varying(36) NOT NULL,
    effectivefromdate timestamp(6) without time zone NOT NULL,
    effectivetodate timestamp(6) without time zone NOT NULL,
    warehousecreatedtimestamp timestamp(6) without time zone NOT NULL,
    warehouselastupdatedtimestamp timestamp(6) without time zone NOT NULL,
    originalsourcesystemuniqueid character varying(255) NOT NULL,
    value character varying(255) NOT NULL,
    activeflagid character varying(36) NOT NULL,
    enterpriseid character varying(36) NOT NULL,
    systemid character varying(36) NOT NULL,
    originalsourcesystemid character varying(36) NOT NULL,
    classificationid character varying(36) NOT NULL,
    involvedpartyid character varying(36) NOT NULL,
    resourceitemid character varying(36) NOT NULL
);


ALTER TABLE party.involvedpartyxresourceitem OWNER TO postgres;

--
-- TOC entry 315 (class 1259 OID 28941)
-- Name: involvedpartyxresourceitemsecuritytoken; Type: TABLE; Schema: party; Owner: postgres
--

CREATE TABLE party.involvedpartyxresourceitemsecuritytoken (
    involvedpartyxresourceitemsecuritytokenid character varying(36) NOT NULL,
    effectivefromdate timestamp(6) without time zone NOT NULL,
    effectivetodate timestamp(6) without time zone NOT NULL,
    warehousecreatedtimestamp timestamp(6) without time zone NOT NULL,
    warehouselastupdatedtimestamp timestamp(6) without time zone NOT NULL,
    createallowed integer NOT NULL,
    deleteallowed integer NOT NULL,
    originalsourcesystemuniqueid character varying(255) NOT NULL,
    readallowed integer NOT NULL,
    updateallowed integer NOT NULL,
    activeflagid character varying(36) NOT NULL,
    enterpriseid character varying(36) NOT NULL,
    originalsourcesystemid character varying(36) NOT NULL,
    securitytokenid character varying(36) NOT NULL,
    systemid character varying(36) NOT NULL,
    involvedpartyxresourceitemid character varying(36) NOT NULL
);


ALTER TABLE party.involvedpartyxresourceitemsecuritytoken OWNER TO postgres;

--
-- TOC entry 316 (class 1259 OID 28948)
-- Name: involvedpartyxrules; Type: TABLE; Schema: party; Owner: postgres
--

CREATE TABLE party.involvedpartyxrules (
    involvedpartyxrulesid character varying(36) NOT NULL,
    effectivefromdate timestamp(6) without time zone NOT NULL,
    effectivetodate timestamp(6) without time zone NOT NULL,
    warehousecreatedtimestamp timestamp(6) without time zone NOT NULL,
    warehouselastupdatedtimestamp timestamp(6) without time zone NOT NULL,
    originalsourcesystemuniqueid character varying(255) NOT NULL,
    value character varying(255) NOT NULL,
    activeflagid character varying(36) NOT NULL,
    enterpriseid character varying(36) NOT NULL,
    systemid character varying(36) NOT NULL,
    originalsourcesystemid character varying(36) NOT NULL,
    classificationid character varying(36) NOT NULL,
    involvedpartyid character varying(36) NOT NULL,
    rulesid character varying(36) NOT NULL
);


ALTER TABLE party.involvedpartyxrules OWNER TO postgres;

--
-- TOC entry 317 (class 1259 OID 28955)
-- Name: involvedpartyxrulessecuritytoken; Type: TABLE; Schema: party; Owner: postgres
--

CREATE TABLE party.involvedpartyxrulessecuritytoken (
    involvedpartyxrulessecuritytokenid character varying(36) NOT NULL,
    effectivefromdate timestamp(6) without time zone NOT NULL,
    effectivetodate timestamp(6) without time zone NOT NULL,
    warehousecreatedtimestamp timestamp(6) without time zone NOT NULL,
    warehouselastupdatedtimestamp timestamp(6) without time zone NOT NULL,
    createallowed integer NOT NULL,
    deleteallowed integer NOT NULL,
    originalsourcesystemuniqueid character varying(255) NOT NULL,
    readallowed integer NOT NULL,
    updateallowed integer NOT NULL,
    activeflagid character varying(36) NOT NULL,
    enterpriseid character varying(36) NOT NULL,
    originalsourcesystemid character varying(36) NOT NULL,
    securitytokenid character varying(36) NOT NULL,
    systemid character varying(36) NOT NULL,
    involvedpartyxrulesid character varying(36) NOT NULL
);


ALTER TABLE party.involvedpartyxrulessecuritytoken OWNER TO postgres;

--
-- TOC entry 318 (class 1259 OID 28962)
-- Name: product; Type: TABLE; Schema: product; Owner: postgres
--

CREATE TABLE product.product (
    productid character varying(36) NOT NULL,
    effectivefromdate timestamp(6) without time zone NOT NULL,
    effectivetodate timestamp(6) without time zone NOT NULL,
    warehousecreatedtimestamp timestamp(6) without time zone NOT NULL,
    warehouselastupdatedtimestamp timestamp(6) without time zone NOT NULL,
    originalsourcesystemuniqueid character varying(255) NOT NULL,
    productdesc character varying(250) NOT NULL,
    productname character varying(150) NOT NULL,
    productcode character varying(10) NOT NULL,
    activeflagid character varying(36) NOT NULL,
    enterpriseid character varying(36) NOT NULL,
    systemid character varying(36) NOT NULL,
    originalsourcesystemid character varying(36) NOT NULL
);


ALTER TABLE product.product OWNER TO postgres;

--
-- TOC entry 319 (class 1259 OID 28969)
-- Name: productsecuritytoken; Type: TABLE; Schema: product; Owner: postgres
--

CREATE TABLE product.productsecuritytoken (
    productsecuritytokenid character varying(36) NOT NULL,
    effectivefromdate timestamp(6) without time zone NOT NULL,
    effectivetodate timestamp(6) without time zone NOT NULL,
    warehousecreatedtimestamp timestamp(6) without time zone NOT NULL,
    warehouselastupdatedtimestamp timestamp(6) without time zone NOT NULL,
    createallowed integer NOT NULL,
    deleteallowed integer NOT NULL,
    originalsourcesystemuniqueid character varying(255) NOT NULL,
    readallowed integer NOT NULL,
    updateallowed integer NOT NULL,
    activeflagid character varying(36) NOT NULL,
    enterpriseid character varying(36) NOT NULL,
    originalsourcesystemid character varying(36) NOT NULL,
    securitytokenid character varying(36) NOT NULL,
    systemid character varying(36) NOT NULL,
    productid character varying(36) NOT NULL
);


ALTER TABLE product.productsecuritytoken OWNER TO postgres;

--
-- TOC entry 320 (class 1259 OID 28976)
-- Name: producttype; Type: TABLE; Schema: product; Owner: postgres
--

CREATE TABLE product.producttype (
    producttypeid character varying(36) NOT NULL,
    effectivefromdate timestamp(6) without time zone NOT NULL,
    effectivetodate timestamp(6) without time zone NOT NULL,
    warehousecreatedtimestamp timestamp(6) without time zone NOT NULL,
    warehouselastupdatedtimestamp timestamp(6) without time zone NOT NULL,
    originalsourcesystemuniqueid character varying(255) NOT NULL,
    producttypedesc character varying(200) NOT NULL,
    producttypename character varying(200) NOT NULL,
    activeflagid character varying(36) NOT NULL,
    enterpriseid character varying(36) NOT NULL,
    systemid character varying(36) NOT NULL,
    originalsourcesystemid character varying(36) NOT NULL
);


ALTER TABLE product.producttype OWNER TO postgres;

--
-- TOC entry 321 (class 1259 OID 28983)
-- Name: producttypessecuritytoken; Type: TABLE; Schema: product; Owner: postgres
--

CREATE TABLE product.producttypessecuritytoken (
    producttypessecuritytokenid character varying(36) NOT NULL,
    effectivefromdate timestamp(6) without time zone NOT NULL,
    effectivetodate timestamp(6) without time zone NOT NULL,
    warehousecreatedtimestamp timestamp(6) without time zone NOT NULL,
    warehouselastupdatedtimestamp timestamp(6) without time zone NOT NULL,
    createallowed integer NOT NULL,
    deleteallowed integer NOT NULL,
    originalsourcesystemuniqueid character varying(255) NOT NULL,
    readallowed integer NOT NULL,
    updateallowed integer NOT NULL,
    activeflagid character varying(36) NOT NULL,
    enterpriseid character varying(36) NOT NULL,
    originalsourcesystemid character varying(36) NOT NULL,
    securitytokenid character varying(36) NOT NULL,
    systemid character varying(36) NOT NULL,
    producttypesid character varying(36) NOT NULL
);


ALTER TABLE product.producttypessecuritytoken OWNER TO postgres;

--
-- TOC entry 322 (class 1259 OID 28990)
-- Name: producttypexclassification; Type: TABLE; Schema: product; Owner: postgres
--

CREATE TABLE product.producttypexclassification (
    producttypexclassificationid character varying(36) NOT NULL,
    effectivefromdate timestamp(6) without time zone NOT NULL,
    effectivetodate timestamp(6) without time zone NOT NULL,
    warehousecreatedtimestamp timestamp(6) without time zone NOT NULL,
    warehouselastupdatedtimestamp timestamp(6) without time zone NOT NULL,
    originalsourcesystemuniqueid character varying(255) NOT NULL,
    value character varying(255) NOT NULL,
    activeflagid character varying(36) NOT NULL,
    enterpriseid character varying(36) NOT NULL,
    systemid character varying(36) NOT NULL,
    originalsourcesystemid character varying(36) NOT NULL,
    classificationid character varying(36) NOT NULL,
    producttypeid character varying(36) NOT NULL
);


ALTER TABLE product.producttypexclassification OWNER TO postgres;

--
-- TOC entry 323 (class 1259 OID 28997)
-- Name: producttypexclassificationsecuritytoken; Type: TABLE; Schema: product; Owner: postgres
--

CREATE TABLE product.producttypexclassificationsecuritytoken (
    producttypexclassificationsecuritytokenid character varying(36) NOT NULL,
    effectivefromdate timestamp(6) without time zone NOT NULL,
    effectivetodate timestamp(6) without time zone NOT NULL,
    warehousecreatedtimestamp timestamp(6) without time zone NOT NULL,
    warehouselastupdatedtimestamp timestamp(6) without time zone NOT NULL,
    createallowed integer NOT NULL,
    deleteallowed integer NOT NULL,
    originalsourcesystemuniqueid character varying(255) NOT NULL,
    readallowed integer NOT NULL,
    updateallowed integer NOT NULL,
    activeflagid character varying(36) NOT NULL,
    enterpriseid character varying(36) NOT NULL,
    originalsourcesystemid character varying(36) NOT NULL,
    securitytokenid character varying(36) NOT NULL,
    systemid character varying(36) NOT NULL,
    producttypexclassificationid character varying(36) NOT NULL
);


ALTER TABLE product.producttypexclassificationsecuritytoken OWNER TO postgres;

--
-- TOC entry 324 (class 1259 OID 29004)
-- Name: productxclassification; Type: TABLE; Schema: product; Owner: postgres
--

CREATE TABLE product.productxclassification (
    productxclassificationid character varying(36) NOT NULL,
    effectivefromdate timestamp(6) without time zone NOT NULL,
    effectivetodate timestamp(6) without time zone NOT NULL,
    warehousecreatedtimestamp timestamp(6) without time zone NOT NULL,
    warehouselastupdatedtimestamp timestamp(6) without time zone NOT NULL,
    originalsourcesystemuniqueid character varying(255) NOT NULL,
    value character varying(255) NOT NULL,
    activeflagid character varying(36) NOT NULL,
    enterpriseid character varying(36) NOT NULL,
    systemid character varying(36) NOT NULL,
    originalsourcesystemid character varying(36) NOT NULL,
    classificationid character varying(36) NOT NULL,
    productid character varying(36) NOT NULL
);


ALTER TABLE product.productxclassification OWNER TO postgres;

--
-- TOC entry 325 (class 1259 OID 29011)
-- Name: productxclassificationsecuritytoken; Type: TABLE; Schema: product; Owner: postgres
--

CREATE TABLE product.productxclassificationsecuritytoken (
    productxclassificationsecuritytokenid character varying(36) NOT NULL,
    effectivefromdate timestamp(6) without time zone NOT NULL,
    effectivetodate timestamp(6) without time zone NOT NULL,
    warehousecreatedtimestamp timestamp(6) without time zone NOT NULL,
    warehouselastupdatedtimestamp timestamp(6) without time zone NOT NULL,
    createallowed integer NOT NULL,
    deleteallowed integer NOT NULL,
    originalsourcesystemuniqueid character varying(255) NOT NULL,
    readallowed integer NOT NULL,
    updateallowed integer NOT NULL,
    activeflagid character varying(36) NOT NULL,
    enterpriseid character varying(36) NOT NULL,
    originalsourcesystemid character varying(36) NOT NULL,
    securitytokenid character varying(36) NOT NULL,
    systemid character varying(36) NOT NULL,
    productxclassificationid character varying(36) NOT NULL
);


ALTER TABLE product.productxclassificationsecuritytoken OWNER TO postgres;

--
-- TOC entry 326 (class 1259 OID 29018)
-- Name: productxproduct; Type: TABLE; Schema: product; Owner: postgres
--

CREATE TABLE product.productxproduct (
    productxproductid character varying(36) NOT NULL,
    effectivefromdate timestamp(6) without time zone NOT NULL,
    effectivetodate timestamp(6) without time zone NOT NULL,
    warehousecreatedtimestamp timestamp(6) without time zone NOT NULL,
    warehouselastupdatedtimestamp timestamp(6) without time zone NOT NULL,
    originalsourcesystemuniqueid character varying(255) NOT NULL,
    value character varying(255) NOT NULL,
    activeflagid character varying(36) NOT NULL,
    enterpriseid character varying(36) NOT NULL,
    systemid character varying(36) NOT NULL,
    originalsourcesystemid character varying(36) NOT NULL,
    classificationid character varying(36) NOT NULL,
    childproductid character varying(36) NOT NULL,
    parentproductid character varying(36) NOT NULL
);


ALTER TABLE product.productxproduct OWNER TO postgres;

--
-- TOC entry 327 (class 1259 OID 29025)
-- Name: productxproductsecuritytoken; Type: TABLE; Schema: product; Owner: postgres
--

CREATE TABLE product.productxproductsecuritytoken (
    productxproductsecuritytokenid character varying(36) NOT NULL,
    effectivefromdate timestamp(6) without time zone NOT NULL,
    effectivetodate timestamp(6) without time zone NOT NULL,
    warehousecreatedtimestamp timestamp(6) without time zone NOT NULL,
    warehouselastupdatedtimestamp timestamp(6) without time zone NOT NULL,
    createallowed integer NOT NULL,
    deleteallowed integer NOT NULL,
    originalsourcesystemuniqueid character varying(255) NOT NULL,
    readallowed integer NOT NULL,
    updateallowed integer NOT NULL,
    activeflagid character varying(36) NOT NULL,
    enterpriseid character varying(36) NOT NULL,
    originalsourcesystemid character varying(36) NOT NULL,
    securitytokenid character varying(36) NOT NULL,
    systemid character varying(36) NOT NULL,
    productxproductid character varying(36) NOT NULL
);


ALTER TABLE product.productxproductsecuritytoken OWNER TO postgres;

--
-- TOC entry 328 (class 1259 OID 29032)
-- Name: productxproducttype; Type: TABLE; Schema: product; Owner: postgres
--

CREATE TABLE product.productxproducttype (
    productxproducttypeid character varying(36) NOT NULL,
    effectivefromdate timestamp(6) without time zone NOT NULL,
    effectivetodate timestamp(6) without time zone NOT NULL,
    warehousecreatedtimestamp timestamp(6) without time zone NOT NULL,
    warehouselastupdatedtimestamp timestamp(6) without time zone NOT NULL,
    originalsourcesystemuniqueid character varying(255) NOT NULL,
    value character varying(255) NOT NULL,
    activeflagid character varying(36) NOT NULL,
    enterpriseid character varying(36) NOT NULL,
    systemid character varying(36) NOT NULL,
    originalsourcesystemid character varying(36) NOT NULL,
    classificationid character varying(36) NOT NULL,
    productid character varying(36) NOT NULL,
    producttypeid character varying(36) NOT NULL
);


ALTER TABLE product.productxproducttype OWNER TO postgres;

--
-- TOC entry 329 (class 1259 OID 29039)
-- Name: productxproducttypesecuritytoken; Type: TABLE; Schema: product; Owner: postgres
--

CREATE TABLE product.productxproducttypesecuritytoken (
    productxproducttypesecuritytokenid character varying(36) NOT NULL,
    effectivefromdate timestamp(6) without time zone NOT NULL,
    effectivetodate timestamp(6) without time zone NOT NULL,
    warehousecreatedtimestamp timestamp(6) without time zone NOT NULL,
    warehouselastupdatedtimestamp timestamp(6) without time zone NOT NULL,
    createallowed integer NOT NULL,
    deleteallowed integer NOT NULL,
    originalsourcesystemuniqueid character varying(255) NOT NULL,
    readallowed integer NOT NULL,
    updateallowed integer NOT NULL,
    activeflagid character varying(36) NOT NULL,
    enterpriseid character varying(36) NOT NULL,
    originalsourcesystemid character varying(36) NOT NULL,
    securitytokenid character varying(36) NOT NULL,
    systemid character varying(36) NOT NULL,
    productxproducttypeid character varying(36) NOT NULL
);


ALTER TABLE product.productxproducttypesecuritytoken OWNER TO postgres;

--
-- TOC entry 330 (class 1259 OID 29046)
-- Name: productxresourceitem; Type: TABLE; Schema: product; Owner: postgres
--

CREATE TABLE product.productxresourceitem (
    productxresourceitemid character varying(36) NOT NULL,
    effectivefromdate timestamp(6) without time zone NOT NULL,
    effectivetodate timestamp(6) without time zone NOT NULL,
    warehousecreatedtimestamp timestamp(6) without time zone NOT NULL,
    warehouselastupdatedtimestamp timestamp(6) without time zone NOT NULL,
    originalsourcesystemuniqueid character varying(255) NOT NULL,
    value character varying(255) NOT NULL,
    activeflagid character varying(36) NOT NULL,
    enterpriseid character varying(36) NOT NULL,
    systemid character varying(36) NOT NULL,
    originalsourcesystemid character varying(36) NOT NULL,
    classificationid character varying(36) NOT NULL,
    productid character varying(36) NOT NULL,
    resourceitemid character varying(36) NOT NULL
);


ALTER TABLE product.productxresourceitem OWNER TO postgres;

--
-- TOC entry 331 (class 1259 OID 29053)
-- Name: productxresourceitemsecuritytoken; Type: TABLE; Schema: product; Owner: postgres
--

CREATE TABLE product.productxresourceitemsecuritytoken (
    productxresourceitemsecuritytokenid character varying(36) NOT NULL,
    effectivefromdate timestamp(6) without time zone NOT NULL,
    effectivetodate timestamp(6) without time zone NOT NULL,
    warehousecreatedtimestamp timestamp(6) without time zone NOT NULL,
    warehouselastupdatedtimestamp timestamp(6) without time zone NOT NULL,
    createallowed integer NOT NULL,
    deleteallowed integer NOT NULL,
    originalsourcesystemuniqueid character varying(255) NOT NULL,
    readallowed integer NOT NULL,
    updateallowed integer NOT NULL,
    activeflagid character varying(36) NOT NULL,
    enterpriseid character varying(36) NOT NULL,
    originalsourcesystemid character varying(36) NOT NULL,
    securitytokenid character varying(36) NOT NULL,
    systemid character varying(36) NOT NULL,
    productxresourceitemid character varying(36) NOT NULL
);


ALTER TABLE product.productxresourceitemsecuritytoken OWNER TO postgres;

--
-- TOC entry 393 (class 1259 OID 29453)
-- Name: arrangementhierarchyview; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.arrangementhierarchyview (
    id character varying(36) NOT NULL,
    name character varying(255),
    one integer,
    parentid character varying(36),
    path character varying(255),
    pather character varying(255)
);


ALTER TABLE public.arrangementhierarchyview OWNER TO postgres;

--
-- TOC entry 394 (class 1259 OID 29460)
-- Name: classificationhierarchyview; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.classificationhierarchyview (
    id character varying(36) NOT NULL,
    name character varying(255),
    one integer,
    parentid character varying(36),
    path character varying(255),
    pather character varying(255)
);


ALTER TABLE public.classificationhierarchyview OWNER TO postgres;

--
-- TOC entry 395 (class 1259 OID 29467)
-- Name: geographyhierarchyview; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.geographyhierarchyview (
    id character varying(36) NOT NULL,
    name character varying(255),
    one integer,
    parentid character varying(36),
    path character varying(255),
    pather character varying(255)
);


ALTER TABLE public.geographyhierarchyview OWNER TO postgres;

--
-- TOC entry 396 (class 1259 OID 29474)
-- Name: producthierarchyview; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.producthierarchyview (
    id character varying(36) NOT NULL,
    name character varying(255),
    one integer,
    parentid character varying(36),
    path character varying(255),
    pather character varying(255)
);


ALTER TABLE public.producthierarchyview OWNER TO postgres;

--
-- TOC entry 397 (class 1259 OID 29481)
-- Name: quarters_months; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.quarters_months (
    quarters_quarterid integer NOT NULL,
    lumonthslist_monthid integer NOT NULL
);


ALTER TABLE public.quarters_months OWNER TO postgres;

--
-- TOC entry 398 (class 1259 OID 29484)
-- Name: resourceitemhierarchyview; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.resourceitemhierarchyview (
    id character varying(36) NOT NULL,
    name character varying(255),
    one integer,
    parentid character varying(36),
    path character varying(255),
    pather character varying(255)
);


ALTER TABLE public.resourceitemhierarchyview OWNER TO postgres;

--
-- TOC entry 399 (class 1259 OID 29491)
-- Name: ruleshierarchyview; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.ruleshierarchyview (
    id character varying(36) NOT NULL,
    name character varying(255),
    one integer,
    parentid character varying(36),
    path character varying(255),
    pather character varying(255)
);


ALTER TABLE public.ruleshierarchyview OWNER TO postgres;

--
-- TOC entry 400 (class 1259 OID 29498)
-- Name: securityhierarchyparents; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.securityhierarchyparents (
    id character varying(36) NOT NULL,
    value bigint
);


ALTER TABLE public.securityhierarchyparents OWNER TO postgres;

--
-- TOC entry 332 (class 1259 OID 29060)
-- Name: resourceitem; Type: TABLE; Schema: resource; Owner: postgres
--

CREATE TABLE resource.resourceitem (
    resourceitemid character varying(36) NOT NULL,
    effectivefromdate timestamp(6) without time zone NOT NULL,
    effectivetodate timestamp(6) without time zone NOT NULL,
    warehousecreatedtimestamp timestamp(6) without time zone NOT NULL,
    warehouselastupdatedtimestamp timestamp(6) without time zone NOT NULL,
    originalsourcesystemuniqueid character varying(255) NOT NULL,
    resourceitemdatatype character varying(150) NOT NULL,
    activeflagid character varying(36) NOT NULL,
    enterpriseid character varying(36) NOT NULL,
    systemid character varying(36) NOT NULL,
    originalsourcesystemid character varying(36) NOT NULL
);


ALTER TABLE resource.resourceitem OWNER TO postgres;

--
-- TOC entry 333 (class 1259 OID 29067)
-- Name: resourceitemdata; Type: TABLE; Schema: resource; Owner: postgres
--

CREATE TABLE resource.resourceitemdata (
    resourceitemdataid character varying(36) NOT NULL,
    effectivefromdate timestamp(6) without time zone NOT NULL,
    effectivetodate timestamp(6) without time zone NOT NULL,
    warehousecreatedtimestamp timestamp(6) without time zone NOT NULL,
    warehouselastupdatedtimestamp timestamp(6) without time zone NOT NULL,
    originalsourcesystemuniqueid character varying(255) NOT NULL,
    resourceitemdata oid NOT NULL,
    activeflagid character varying(36) NOT NULL,
    enterpriseid character varying(36) NOT NULL,
    systemid character varying(36) NOT NULL,
    originalsourcesystemid character varying(36) NOT NULL,
    resourceitemid character varying(36) NOT NULL
);


ALTER TABLE resource.resourceitemdata OWNER TO postgres;

--
-- TOC entry 334 (class 1259 OID 29072)
-- Name: resourceitemdatasecuritytoken; Type: TABLE; Schema: resource; Owner: postgres
--

CREATE TABLE resource.resourceitemdatasecuritytoken (
    resourceitemdatasecuritytokenid character varying(36) NOT NULL,
    effectivefromdate timestamp(6) without time zone NOT NULL,
    effectivetodate timestamp(6) without time zone NOT NULL,
    warehousecreatedtimestamp timestamp(6) without time zone NOT NULL,
    warehouselastupdatedtimestamp timestamp(6) without time zone NOT NULL,
    createallowed integer NOT NULL,
    deleteallowed integer NOT NULL,
    originalsourcesystemuniqueid character varying(255) NOT NULL,
    readallowed integer NOT NULL,
    updateallowed integer NOT NULL,
    activeflagid character varying(36) NOT NULL,
    enterpriseid character varying(36) NOT NULL,
    originalsourcesystemid character varying(36) NOT NULL,
    securitytokenid character varying(36) NOT NULL,
    systemid character varying(36) NOT NULL,
    resourceitemdataid character varying(36) NOT NULL
);


ALTER TABLE resource.resourceitemdatasecuritytoken OWNER TO postgres;

--
-- TOC entry 335 (class 1259 OID 29079)
-- Name: resourceitemdataxclassification; Type: TABLE; Schema: resource; Owner: postgres
--

CREATE TABLE resource.resourceitemdataxclassification (
    resourceitemdataxclassificationid character varying(36) NOT NULL,
    effectivefromdate timestamp(6) without time zone NOT NULL,
    effectivetodate timestamp(6) without time zone NOT NULL,
    warehousecreatedtimestamp timestamp(6) without time zone NOT NULL,
    warehouselastupdatedtimestamp timestamp(6) without time zone NOT NULL,
    originalsourcesystemuniqueid character varying(255) NOT NULL,
    value character varying(255) NOT NULL,
    activeflagid character varying(36) NOT NULL,
    enterpriseid character varying(36) NOT NULL,
    systemid character varying(36) NOT NULL,
    originalsourcesystemid character varying(36) NOT NULL,
    classificationid character varying(36) NOT NULL,
    resourceitemdataid character varying(36) NOT NULL
);


ALTER TABLE resource.resourceitemdataxclassification OWNER TO postgres;

--
-- TOC entry 336 (class 1259 OID 29086)
-- Name: resourceitemdataxclassificationsecuritytoken; Type: TABLE; Schema: resource; Owner: postgres
--

CREATE TABLE resource.resourceitemdataxclassificationsecuritytoken (
    resourceitemdataxclassificationsecuritytokenid character varying(36) NOT NULL,
    effectivefromdate timestamp(6) without time zone NOT NULL,
    effectivetodate timestamp(6) without time zone NOT NULL,
    warehousecreatedtimestamp timestamp(6) without time zone NOT NULL,
    warehouselastupdatedtimestamp timestamp(6) without time zone NOT NULL,
    createallowed integer NOT NULL,
    deleteallowed integer NOT NULL,
    originalsourcesystemuniqueid character varying(255) NOT NULL,
    readallowed integer NOT NULL,
    updateallowed integer NOT NULL,
    activeflagid character varying(36) NOT NULL,
    enterpriseid character varying(36) NOT NULL,
    originalsourcesystemid character varying(36) NOT NULL,
    securitytokenid character varying(36) NOT NULL,
    systemid character varying(36) NOT NULL,
    resourceitemdataxclassificationid character varying(36) NOT NULL
);


ALTER TABLE resource.resourceitemdataxclassificationsecuritytoken OWNER TO postgres;

--
-- TOC entry 337 (class 1259 OID 29093)
-- Name: resourceitemsecuritytoken; Type: TABLE; Schema: resource; Owner: postgres
--

CREATE TABLE resource.resourceitemsecuritytoken (
    resourceitemsecuritytokenid character varying(36) NOT NULL,
    effectivefromdate timestamp(6) without time zone NOT NULL,
    effectivetodate timestamp(6) without time zone NOT NULL,
    warehousecreatedtimestamp timestamp(6) without time zone NOT NULL,
    warehouselastupdatedtimestamp timestamp(6) without time zone NOT NULL,
    createallowed integer NOT NULL,
    deleteallowed integer NOT NULL,
    originalsourcesystemuniqueid character varying(255) NOT NULL,
    readallowed integer NOT NULL,
    updateallowed integer NOT NULL,
    activeflagid character varying(36) NOT NULL,
    enterpriseid character varying(36) NOT NULL,
    originalsourcesystemid character varying(36) NOT NULL,
    securitytokenid character varying(36) NOT NULL,
    systemid character varying(36) NOT NULL,
    resourceitemid character varying(36) NOT NULL
);


ALTER TABLE resource.resourceitemsecuritytoken OWNER TO postgres;

--
-- TOC entry 338 (class 1259 OID 29100)
-- Name: resourceitemtype; Type: TABLE; Schema: resource; Owner: postgres
--

CREATE TABLE resource.resourceitemtype (
    resourceitemtypeid character varying(36) NOT NULL,
    effectivefromdate timestamp(6) without time zone NOT NULL,
    effectivetodate timestamp(6) without time zone NOT NULL,
    warehousecreatedtimestamp timestamp(6) without time zone NOT NULL,
    warehouselastupdatedtimestamp timestamp(6) without time zone NOT NULL,
    originalsourcesystemuniqueid character varying(255) NOT NULL,
    resourceitemtypedesc character varying(255) NOT NULL,
    resourceitemtypename character varying(100) NOT NULL,
    activeflagid character varying(36) NOT NULL,
    enterpriseid character varying(36) NOT NULL,
    systemid character varying(36) NOT NULL,
    originalsourcesystemid character varying(36) NOT NULL
);


ALTER TABLE resource.resourceitemtype OWNER TO postgres;

--
-- TOC entry 339 (class 1259 OID 29107)
-- Name: resourceitemtypesecuritytoken; Type: TABLE; Schema: resource; Owner: postgres
--

CREATE TABLE resource.resourceitemtypesecuritytoken (
    resourceitemtypesecuritytokenid character varying(36) NOT NULL,
    effectivefromdate timestamp(6) without time zone NOT NULL,
    effectivetodate timestamp(6) without time zone NOT NULL,
    warehousecreatedtimestamp timestamp(6) without time zone NOT NULL,
    warehouselastupdatedtimestamp timestamp(6) without time zone NOT NULL,
    createallowed integer NOT NULL,
    deleteallowed integer NOT NULL,
    originalsourcesystemuniqueid character varying(255) NOT NULL,
    readallowed integer NOT NULL,
    updateallowed integer NOT NULL,
    activeflagid character varying(36) NOT NULL,
    enterpriseid character varying(36) NOT NULL,
    originalsourcesystemid character varying(36) NOT NULL,
    securitytokenid character varying(36) NOT NULL,
    systemid character varying(36) NOT NULL,
    resourceitemtypeid character varying(36) NOT NULL
);


ALTER TABLE resource.resourceitemtypesecuritytoken OWNER TO postgres;

--
-- TOC entry 340 (class 1259 OID 29114)
-- Name: resourceitemxclassification; Type: TABLE; Schema: resource; Owner: postgres
--

CREATE TABLE resource.resourceitemxclassification (
    resourceitemxclassificationid character varying(36) NOT NULL,
    effectivefromdate timestamp(6) without time zone NOT NULL,
    effectivetodate timestamp(6) without time zone NOT NULL,
    warehousecreatedtimestamp timestamp(6) without time zone NOT NULL,
    warehouselastupdatedtimestamp timestamp(6) without time zone NOT NULL,
    originalsourcesystemuniqueid character varying(255) NOT NULL,
    value character varying(255) NOT NULL,
    activeflagid character varying(36) NOT NULL,
    enterpriseid character varying(36) NOT NULL,
    systemid character varying(36) NOT NULL,
    originalsourcesystemid character varying(36) NOT NULL,
    classificationid character varying(36) NOT NULL,
    resourceitemid character varying(36) NOT NULL
);


ALTER TABLE resource.resourceitemxclassification OWNER TO postgres;

--
-- TOC entry 341 (class 1259 OID 29121)
-- Name: resourceitemxclassificationsecuritytoken; Type: TABLE; Schema: resource; Owner: postgres
--

CREATE TABLE resource.resourceitemxclassificationsecuritytoken (
    resourceitemxclassificationsecuritytokenid character varying(36) NOT NULL,
    effectivefromdate timestamp(6) without time zone NOT NULL,
    effectivetodate timestamp(6) without time zone NOT NULL,
    warehousecreatedtimestamp timestamp(6) without time zone NOT NULL,
    warehouselastupdatedtimestamp timestamp(6) without time zone NOT NULL,
    createallowed integer NOT NULL,
    deleteallowed integer NOT NULL,
    originalsourcesystemuniqueid character varying(255) NOT NULL,
    readallowed integer NOT NULL,
    updateallowed integer NOT NULL,
    activeflagid character varying(36) NOT NULL,
    enterpriseid character varying(36) NOT NULL,
    originalsourcesystemid character varying(36) NOT NULL,
    securitytokenid character varying(36) NOT NULL,
    systemid character varying(36) NOT NULL,
    resourceitemxclassificationid character varying(36) NOT NULL
);


ALTER TABLE resource.resourceitemxclassificationsecuritytoken OWNER TO postgres;

--
-- TOC entry 342 (class 1259 OID 29128)
-- Name: resourceitemxresourceitem; Type: TABLE; Schema: resource; Owner: postgres
--

CREATE TABLE resource.resourceitemxresourceitem (
    resourceitemxresourceitemid character varying(36) NOT NULL,
    effectivefromdate timestamp(6) without time zone NOT NULL,
    effectivetodate timestamp(6) without time zone NOT NULL,
    warehousecreatedtimestamp timestamp(6) without time zone NOT NULL,
    warehouselastupdatedtimestamp timestamp(6) without time zone NOT NULL,
    originalsourcesystemuniqueid character varying(255) NOT NULL,
    value character varying(255) NOT NULL,
    activeflagid character varying(36) NOT NULL,
    enterpriseid character varying(36) NOT NULL,
    systemid character varying(36) NOT NULL,
    originalsourcesystemid character varying(36) NOT NULL,
    classificationid character varying(36) NOT NULL,
    childresourceitemid character varying(36) NOT NULL,
    parentresourceitemid character varying(36) NOT NULL
);


ALTER TABLE resource.resourceitemxresourceitem OWNER TO postgres;

--
-- TOC entry 343 (class 1259 OID 29135)
-- Name: resourceitemxresourceitemsecuritytoken; Type: TABLE; Schema: resource; Owner: postgres
--

CREATE TABLE resource.resourceitemxresourceitemsecuritytoken (
    resourceitemxresourceitemsecuritytokenid character varying(36) NOT NULL,
    effectivefromdate timestamp(6) without time zone NOT NULL,
    effectivetodate timestamp(6) without time zone NOT NULL,
    warehousecreatedtimestamp timestamp(6) without time zone NOT NULL,
    warehouselastupdatedtimestamp timestamp(6) without time zone NOT NULL,
    createallowed integer NOT NULL,
    deleteallowed integer NOT NULL,
    originalsourcesystemuniqueid character varying(255) NOT NULL,
    readallowed integer NOT NULL,
    updateallowed integer NOT NULL,
    activeflagid character varying(36) NOT NULL,
    enterpriseid character varying(36) NOT NULL,
    originalsourcesystemid character varying(36) NOT NULL,
    securitytokenid character varying(36) NOT NULL,
    systemid character varying(36) NOT NULL,
    resourceitemxresourceitemid character varying(36) NOT NULL
);


ALTER TABLE resource.resourceitemxresourceitemsecuritytoken OWNER TO postgres;

--
-- TOC entry 344 (class 1259 OID 29142)
-- Name: resourceitemxresourceitemtype; Type: TABLE; Schema: resource; Owner: postgres
--

CREATE TABLE resource.resourceitemxresourceitemtype (
    resourceitemxresourceitemtypeid character varying(36) NOT NULL,
    effectivefromdate timestamp(6) without time zone NOT NULL,
    effectivetodate timestamp(6) without time zone NOT NULL,
    warehousecreatedtimestamp timestamp(6) without time zone NOT NULL,
    warehouselastupdatedtimestamp timestamp(6) without time zone NOT NULL,
    originalsourcesystemuniqueid character varying(255) NOT NULL,
    value character varying(255) NOT NULL,
    activeflagid character varying(36) NOT NULL,
    enterpriseid character varying(36) NOT NULL,
    systemid character varying(36) NOT NULL,
    originalsourcesystemid character varying(36) NOT NULL,
    classificationid character varying(36) NOT NULL,
    resourceitemid character varying(36) NOT NULL,
    resourceitemtypeid character varying(36) NOT NULL
);


ALTER TABLE resource.resourceitemxresourceitemtype OWNER TO postgres;

--
-- TOC entry 345 (class 1259 OID 29149)
-- Name: resourceitemxresourceitemtypesecuritytoken; Type: TABLE; Schema: resource; Owner: postgres
--

CREATE TABLE resource.resourceitemxresourceitemtypesecuritytoken (
    resourceitemxresourceitemtypesecuritytokenid character varying(36) NOT NULL,
    effectivefromdate timestamp(6) without time zone NOT NULL,
    effectivetodate timestamp(6) without time zone NOT NULL,
    warehousecreatedtimestamp timestamp(6) without time zone NOT NULL,
    warehouselastupdatedtimestamp timestamp(6) without time zone NOT NULL,
    createallowed integer NOT NULL,
    deleteallowed integer NOT NULL,
    originalsourcesystemuniqueid character varying(255) NOT NULL,
    readallowed integer NOT NULL,
    updateallowed integer NOT NULL,
    activeflagid character varying(36) NOT NULL,
    enterpriseid character varying(36) NOT NULL,
    originalsourcesystemid character varying(36) NOT NULL,
    securitytokenid character varying(36) NOT NULL,
    systemid character varying(36) NOT NULL,
    resourceitemxresourceitemtypeid character varying(36) NOT NULL
);


ALTER TABLE resource.resourceitemxresourceitemtypesecuritytoken OWNER TO postgres;

--
-- TOC entry 346 (class 1259 OID 29156)
-- Name: rules; Type: TABLE; Schema: rules; Owner: postgres
--

CREATE TABLE rules.rules (
    rulesid character varying(36) NOT NULL,
    effectivefromdate timestamp(6) without time zone NOT NULL,
    effectivetodate timestamp(6) without time zone NOT NULL,
    warehousecreatedtimestamp timestamp(6) without time zone NOT NULL,
    warehouselastupdatedtimestamp timestamp(6) without time zone NOT NULL,
    originalsourcesystemuniqueid character varying(255) NOT NULL,
    rulesetdescription character varying(250) NOT NULL,
    rulesetname character varying(150) NOT NULL,
    activeflagid character varying(36) NOT NULL,
    enterpriseid character varying(36) NOT NULL,
    systemid character varying(36) NOT NULL,
    originalsourcesystemid character varying(36) NOT NULL
);


ALTER TABLE rules.rules OWNER TO postgres;

--
-- TOC entry 347 (class 1259 OID 29163)
-- Name: rulessecuritytoken; Type: TABLE; Schema: rules; Owner: postgres
--

CREATE TABLE rules.rulessecuritytoken (
    rulessecuritytokenid character varying(36) NOT NULL,
    effectivefromdate timestamp(6) without time zone NOT NULL,
    effectivetodate timestamp(6) without time zone NOT NULL,
    warehousecreatedtimestamp timestamp(6) without time zone NOT NULL,
    warehouselastupdatedtimestamp timestamp(6) without time zone NOT NULL,
    createallowed integer NOT NULL,
    deleteallowed integer NOT NULL,
    originalsourcesystemuniqueid character varying(255) NOT NULL,
    readallowed integer NOT NULL,
    updateallowed integer NOT NULL,
    activeflagid character varying(36) NOT NULL,
    enterpriseid character varying(36) NOT NULL,
    originalsourcesystemid character varying(36) NOT NULL,
    securitytokenid character varying(36) NOT NULL,
    systemid character varying(36) NOT NULL,
    rulesid character varying(36) NOT NULL
);


ALTER TABLE rules.rulessecuritytoken OWNER TO postgres;

--
-- TOC entry 348 (class 1259 OID 29170)
-- Name: rulestype; Type: TABLE; Schema: rules; Owner: postgres
--

CREATE TABLE rules.rulestype (
    rulestypeid character varying(36) NOT NULL,
    effectivefromdate timestamp(6) without time zone NOT NULL,
    effectivetodate timestamp(6) without time zone NOT NULL,
    warehousecreatedtimestamp timestamp(6) without time zone NOT NULL,
    warehouselastupdatedtimestamp timestamp(6) without time zone NOT NULL,
    originalsourcesystemuniqueid character varying(255) NOT NULL,
    rulestypedesc character varying(200) NOT NULL,
    rulestypename character varying(200) NOT NULL,
    activeflagid character varying(36) NOT NULL,
    enterpriseid character varying(36) NOT NULL,
    systemid character varying(36) NOT NULL,
    originalsourcesystemid character varying(36) NOT NULL
);


ALTER TABLE rules.rulestype OWNER TO postgres;

--
-- TOC entry 349 (class 1259 OID 29177)
-- Name: rulestypessecuritytoken; Type: TABLE; Schema: rules; Owner: postgres
--

CREATE TABLE rules.rulestypessecuritytoken (
    rulestypessecuritytokenid character varying(36) NOT NULL,
    effectivefromdate timestamp(6) without time zone NOT NULL,
    effectivetodate timestamp(6) without time zone NOT NULL,
    warehousecreatedtimestamp timestamp(6) without time zone NOT NULL,
    warehouselastupdatedtimestamp timestamp(6) without time zone NOT NULL,
    createallowed integer NOT NULL,
    deleteallowed integer NOT NULL,
    originalsourcesystemuniqueid character varying(255) NOT NULL,
    readallowed integer NOT NULL,
    updateallowed integer NOT NULL,
    activeflagid character varying(36) NOT NULL,
    enterpriseid character varying(36) NOT NULL,
    originalsourcesystemid character varying(36) NOT NULL,
    securitytokenid character varying(36) NOT NULL,
    systemid character varying(36) NOT NULL,
    rulestypesid character varying(36) NOT NULL
);


ALTER TABLE rules.rulestypessecuritytoken OWNER TO postgres;

--
-- TOC entry 350 (class 1259 OID 29184)
-- Name: rulestypexclassification; Type: TABLE; Schema: rules; Owner: postgres
--

CREATE TABLE rules.rulestypexclassification (
    rulestypexclassificationid character varying(36) NOT NULL,
    effectivefromdate timestamp(6) without time zone NOT NULL,
    effectivetodate timestamp(6) without time zone NOT NULL,
    warehousecreatedtimestamp timestamp(6) without time zone NOT NULL,
    warehouselastupdatedtimestamp timestamp(6) without time zone NOT NULL,
    originalsourcesystemuniqueid character varying(255) NOT NULL,
    value character varying(255) NOT NULL,
    activeflagid character varying(36) NOT NULL,
    enterpriseid character varying(36) NOT NULL,
    systemid character varying(36) NOT NULL,
    originalsourcesystemid character varying(36) NOT NULL,
    classificationid character varying(36) NOT NULL,
    rulestypeid character varying(36) NOT NULL
);


ALTER TABLE rules.rulestypexclassification OWNER TO postgres;

--
-- TOC entry 351 (class 1259 OID 29191)
-- Name: rulestypexclassificationsecuritytoken; Type: TABLE; Schema: rules; Owner: postgres
--

CREATE TABLE rules.rulestypexclassificationsecuritytoken (
    rulestypexclassificationsecuritytokenid character varying(36) NOT NULL,
    effectivefromdate timestamp(6) without time zone NOT NULL,
    effectivetodate timestamp(6) without time zone NOT NULL,
    warehousecreatedtimestamp timestamp(6) without time zone NOT NULL,
    warehouselastupdatedtimestamp timestamp(6) without time zone NOT NULL,
    createallowed integer NOT NULL,
    deleteallowed integer NOT NULL,
    originalsourcesystemuniqueid character varying(255) NOT NULL,
    readallowed integer NOT NULL,
    updateallowed integer NOT NULL,
    activeflagid character varying(36) NOT NULL,
    enterpriseid character varying(36) NOT NULL,
    originalsourcesystemid character varying(36) NOT NULL,
    securitytokenid character varying(36) NOT NULL,
    systemid character varying(36) NOT NULL,
    rulestypexclassificationid character varying(36) NOT NULL
);


ALTER TABLE rules.rulestypexclassificationsecuritytoken OWNER TO postgres;

--
-- TOC entry 352 (class 1259 OID 29198)
-- Name: rulestypexresourceitem; Type: TABLE; Schema: rules; Owner: postgres
--

CREATE TABLE rules.rulestypexresourceitem (
    rulestypexresourceitemid character varying(36) NOT NULL,
    effectivefromdate timestamp(6) without time zone NOT NULL,
    effectivetodate timestamp(6) without time zone NOT NULL,
    warehousecreatedtimestamp timestamp(6) without time zone NOT NULL,
    warehouselastupdatedtimestamp timestamp(6) without time zone NOT NULL,
    originalsourcesystemuniqueid character varying(255) NOT NULL,
    value character varying(255) NOT NULL,
    activeflagid character varying(36) NOT NULL,
    enterpriseid character varying(36) NOT NULL,
    systemid character varying(36) NOT NULL,
    originalsourcesystemid character varying(36) NOT NULL,
    classificationid character varying(36) NOT NULL,
    resourceitemid character varying(36) NOT NULL,
    rulestypeid character varying(36) NOT NULL
);


ALTER TABLE rules.rulestypexresourceitem OWNER TO postgres;

--
-- TOC entry 353 (class 1259 OID 29205)
-- Name: rulestypexresourceitemsecuritytoken; Type: TABLE; Schema: rules; Owner: postgres
--

CREATE TABLE rules.rulestypexresourceitemsecuritytoken (
    rulestypexresourceitemsecuritytokenid character varying(36) NOT NULL,
    effectivefromdate timestamp(6) without time zone NOT NULL,
    effectivetodate timestamp(6) without time zone NOT NULL,
    warehousecreatedtimestamp timestamp(6) without time zone NOT NULL,
    warehouselastupdatedtimestamp timestamp(6) without time zone NOT NULL,
    createallowed integer NOT NULL,
    deleteallowed integer NOT NULL,
    originalsourcesystemuniqueid character varying(255) NOT NULL,
    readallowed integer NOT NULL,
    updateallowed integer NOT NULL,
    activeflagid character varying(36) NOT NULL,
    enterpriseid character varying(36) NOT NULL,
    originalsourcesystemid character varying(36) NOT NULL,
    securitytokenid character varying(36) NOT NULL,
    systemid character varying(36) NOT NULL,
    rulestypexresourceitemid character varying(36) NOT NULL
);


ALTER TABLE rules.rulestypexresourceitemsecuritytoken OWNER TO postgres;

--
-- TOC entry 354 (class 1259 OID 29212)
-- Name: rulesxarrangement; Type: TABLE; Schema: rules; Owner: postgres
--

CREATE TABLE rules.rulesxarrangement (
    rulesxarrangementsid character varying(36) NOT NULL,
    effectivefromdate timestamp(6) without time zone NOT NULL,
    effectivetodate timestamp(6) without time zone NOT NULL,
    warehousecreatedtimestamp timestamp(6) without time zone NOT NULL,
    warehouselastupdatedtimestamp timestamp(6) without time zone NOT NULL,
    originalsourcesystemuniqueid character varying(255) NOT NULL,
    value character varying(255) NOT NULL,
    activeflagid character varying(36) NOT NULL,
    enterpriseid character varying(36) NOT NULL,
    systemid character varying(36) NOT NULL,
    originalsourcesystemid character varying(36) NOT NULL,
    classificationid character varying(36) NOT NULL,
    arrangementid character varying(36) NOT NULL,
    rulesid character varying(36) NOT NULL
);


ALTER TABLE rules.rulesxarrangement OWNER TO postgres;

--
-- TOC entry 355 (class 1259 OID 29219)
-- Name: rulesxarrangementssecuritytoken; Type: TABLE; Schema: rules; Owner: postgres
--

CREATE TABLE rules.rulesxarrangementssecuritytoken (
    rulesxarrangementssecuritytokenid character varying(36) NOT NULL,
    effectivefromdate timestamp(6) without time zone NOT NULL,
    effectivetodate timestamp(6) without time zone NOT NULL,
    warehousecreatedtimestamp timestamp(6) without time zone NOT NULL,
    warehouselastupdatedtimestamp timestamp(6) without time zone NOT NULL,
    createallowed integer NOT NULL,
    deleteallowed integer NOT NULL,
    originalsourcesystemuniqueid character varying(255) NOT NULL,
    readallowed integer NOT NULL,
    updateallowed integer NOT NULL,
    activeflagid character varying(36) NOT NULL,
    enterpriseid character varying(36) NOT NULL,
    originalsourcesystemid character varying(36) NOT NULL,
    securitytokenid character varying(36) NOT NULL,
    systemid character varying(36) NOT NULL,
    rulesxarrangementsid character varying(36) NOT NULL
);


ALTER TABLE rules.rulesxarrangementssecuritytoken OWNER TO postgres;

--
-- TOC entry 356 (class 1259 OID 29226)
-- Name: rulesxclassification; Type: TABLE; Schema: rules; Owner: postgres
--

CREATE TABLE rules.rulesxclassification (
    rulesxclassificationid character varying(36) NOT NULL,
    effectivefromdate timestamp(6) without time zone NOT NULL,
    effectivetodate timestamp(6) without time zone NOT NULL,
    warehousecreatedtimestamp timestamp(6) without time zone NOT NULL,
    warehouselastupdatedtimestamp timestamp(6) without time zone NOT NULL,
    originalsourcesystemuniqueid character varying(255) NOT NULL,
    value character varying(255) NOT NULL,
    activeflagid character varying(36) NOT NULL,
    enterpriseid character varying(36) NOT NULL,
    systemid character varying(36) NOT NULL,
    originalsourcesystemid character varying(36) NOT NULL,
    classificationid character varying(36) NOT NULL,
    rulesid character varying(36) NOT NULL
);


ALTER TABLE rules.rulesxclassification OWNER TO postgres;

--
-- TOC entry 357 (class 1259 OID 29233)
-- Name: rulesxclassificationsecuritytoken; Type: TABLE; Schema: rules; Owner: postgres
--

CREATE TABLE rules.rulesxclassificationsecuritytoken (
    rulesxclassificationsecuritytokenid character varying(36) NOT NULL,
    effectivefromdate timestamp(6) without time zone NOT NULL,
    effectivetodate timestamp(6) without time zone NOT NULL,
    warehousecreatedtimestamp timestamp(6) without time zone NOT NULL,
    warehouselastupdatedtimestamp timestamp(6) without time zone NOT NULL,
    createallowed integer NOT NULL,
    deleteallowed integer NOT NULL,
    originalsourcesystemuniqueid character varying(255) NOT NULL,
    readallowed integer NOT NULL,
    updateallowed integer NOT NULL,
    activeflagid character varying(36) NOT NULL,
    enterpriseid character varying(36) NOT NULL,
    originalsourcesystemid character varying(36) NOT NULL,
    securitytokenid character varying(36) NOT NULL,
    systemid character varying(36) NOT NULL,
    rulesxclassificationid character varying(36) NOT NULL
);


ALTER TABLE rules.rulesxclassificationsecuritytoken OWNER TO postgres;

--
-- TOC entry 358 (class 1259 OID 29240)
-- Name: rulesxinvolvedparty; Type: TABLE; Schema: rules; Owner: postgres
--

CREATE TABLE rules.rulesxinvolvedparty (
    rulesxinvolvedpartyid character varying(36) NOT NULL,
    effectivefromdate timestamp(6) without time zone NOT NULL,
    effectivetodate timestamp(6) without time zone NOT NULL,
    warehousecreatedtimestamp timestamp(6) without time zone NOT NULL,
    warehouselastupdatedtimestamp timestamp(6) without time zone NOT NULL,
    originalsourcesystemuniqueid character varying(255) NOT NULL,
    value character varying(255) NOT NULL,
    activeflagid character varying(36) NOT NULL,
    enterpriseid character varying(36) NOT NULL,
    systemid character varying(36) NOT NULL,
    originalsourcesystemid character varying(36) NOT NULL,
    classificationid character varying(36) NOT NULL,
    involvedpartyid character varying(36) NOT NULL,
    rulesid character varying(36) NOT NULL
);


ALTER TABLE rules.rulesxinvolvedparty OWNER TO postgres;

--
-- TOC entry 359 (class 1259 OID 29247)
-- Name: rulesxinvolvedpartysecuritytoken; Type: TABLE; Schema: rules; Owner: postgres
--

CREATE TABLE rules.rulesxinvolvedpartysecuritytoken (
    rulesxinvolvedpartysecuritytokenid character varying(36) NOT NULL,
    effectivefromdate timestamp(6) without time zone NOT NULL,
    effectivetodate timestamp(6) without time zone NOT NULL,
    warehousecreatedtimestamp timestamp(6) without time zone NOT NULL,
    warehouselastupdatedtimestamp timestamp(6) without time zone NOT NULL,
    createallowed integer NOT NULL,
    deleteallowed integer NOT NULL,
    originalsourcesystemuniqueid character varying(255) NOT NULL,
    readallowed integer NOT NULL,
    updateallowed integer NOT NULL,
    activeflagid character varying(36) NOT NULL,
    enterpriseid character varying(36) NOT NULL,
    originalsourcesystemid character varying(36) NOT NULL,
    securitytokenid character varying(36) NOT NULL,
    systemid character varying(36) NOT NULL,
    rulesxinvolvedpartyid character varying(36) NOT NULL
);


ALTER TABLE rules.rulesxinvolvedpartysecuritytoken OWNER TO postgres;

--
-- TOC entry 360 (class 1259 OID 29254)
-- Name: rulesxproduct; Type: TABLE; Schema: rules; Owner: postgres
--

CREATE TABLE rules.rulesxproduct (
    rulesxproductid character varying(36) NOT NULL,
    effectivefromdate timestamp(6) without time zone NOT NULL,
    effectivetodate timestamp(6) without time zone NOT NULL,
    warehousecreatedtimestamp timestamp(6) without time zone NOT NULL,
    warehouselastupdatedtimestamp timestamp(6) without time zone NOT NULL,
    originalsourcesystemuniqueid character varying(255) NOT NULL,
    value character varying(255) NOT NULL,
    activeflagid character varying(36) NOT NULL,
    enterpriseid character varying(36) NOT NULL,
    systemid character varying(36) NOT NULL,
    originalsourcesystemid character varying(36) NOT NULL,
    classificationid character varying(36) NOT NULL,
    productid character varying(36) NOT NULL,
    rulesid character varying(36) NOT NULL
);


ALTER TABLE rules.rulesxproduct OWNER TO postgres;

--
-- TOC entry 361 (class 1259 OID 29261)
-- Name: rulesxproductsecuritytoken; Type: TABLE; Schema: rules; Owner: postgres
--

CREATE TABLE rules.rulesxproductsecuritytoken (
    rulesxproductsecuritytokenid character varying(36) NOT NULL,
    effectivefromdate timestamp(6) without time zone NOT NULL,
    effectivetodate timestamp(6) without time zone NOT NULL,
    warehousecreatedtimestamp timestamp(6) without time zone NOT NULL,
    warehouselastupdatedtimestamp timestamp(6) without time zone NOT NULL,
    createallowed integer NOT NULL,
    deleteallowed integer NOT NULL,
    originalsourcesystemuniqueid character varying(255) NOT NULL,
    readallowed integer NOT NULL,
    updateallowed integer NOT NULL,
    activeflagid character varying(36) NOT NULL,
    enterpriseid character varying(36) NOT NULL,
    originalsourcesystemid character varying(36) NOT NULL,
    securitytokenid character varying(36) NOT NULL,
    systemid character varying(36) NOT NULL,
    rulesxproductid character varying(36) NOT NULL
);


ALTER TABLE rules.rulesxproductsecuritytoken OWNER TO postgres;

--
-- TOC entry 362 (class 1259 OID 29268)
-- Name: rulesxresourceitem; Type: TABLE; Schema: rules; Owner: postgres
--

CREATE TABLE rules.rulesxresourceitem (
    rulesxresourceitemid character varying(36) NOT NULL,
    effectivefromdate timestamp(6) without time zone NOT NULL,
    effectivetodate timestamp(6) without time zone NOT NULL,
    warehousecreatedtimestamp timestamp(6) without time zone NOT NULL,
    warehouselastupdatedtimestamp timestamp(6) without time zone NOT NULL,
    originalsourcesystemuniqueid character varying(255) NOT NULL,
    value character varying(255) NOT NULL,
    activeflagid character varying(36) NOT NULL,
    enterpriseid character varying(36) NOT NULL,
    systemid character varying(36) NOT NULL,
    originalsourcesystemid character varying(36) NOT NULL,
    classificationid character varying(36) NOT NULL,
    resourceitemid character varying(36) NOT NULL,
    rulesid character varying(36) NOT NULL
);


ALTER TABLE rules.rulesxresourceitem OWNER TO postgres;

--
-- TOC entry 363 (class 1259 OID 29275)
-- Name: rulesxresourceitemsecuritytoken; Type: TABLE; Schema: rules; Owner: postgres
--

CREATE TABLE rules.rulesxresourceitemsecuritytoken (
    rulesxresourceitemsecuritytokenid character varying(36) NOT NULL,
    effectivefromdate timestamp(6) without time zone NOT NULL,
    effectivetodate timestamp(6) without time zone NOT NULL,
    warehousecreatedtimestamp timestamp(6) without time zone NOT NULL,
    warehouselastupdatedtimestamp timestamp(6) without time zone NOT NULL,
    createallowed integer NOT NULL,
    deleteallowed integer NOT NULL,
    originalsourcesystemuniqueid character varying(255) NOT NULL,
    readallowed integer NOT NULL,
    updateallowed integer NOT NULL,
    activeflagid character varying(36) NOT NULL,
    enterpriseid character varying(36) NOT NULL,
    originalsourcesystemid character varying(36) NOT NULL,
    securitytokenid character varying(36) NOT NULL,
    systemid character varying(36) NOT NULL,
    rulesxresourceitemid character varying(36) NOT NULL
);


ALTER TABLE rules.rulesxresourceitemsecuritytoken OWNER TO postgres;

--
-- TOC entry 364 (class 1259 OID 29282)
-- Name: rulesxrules; Type: TABLE; Schema: rules; Owner: postgres
--

CREATE TABLE rules.rulesxrules (
    rulesxrulesid character varying(36) NOT NULL,
    effectivefromdate timestamp(6) without time zone NOT NULL,
    effectivetodate timestamp(6) without time zone NOT NULL,
    warehousecreatedtimestamp timestamp(6) without time zone NOT NULL,
    warehouselastupdatedtimestamp timestamp(6) without time zone NOT NULL,
    originalsourcesystemuniqueid character varying(255) NOT NULL,
    value character varying(255) NOT NULL,
    activeflagid character varying(36) NOT NULL,
    enterpriseid character varying(36) NOT NULL,
    systemid character varying(36) NOT NULL,
    originalsourcesystemid character varying(36) NOT NULL,
    classificationid character varying(36) NOT NULL,
    childrulesid character varying(36) NOT NULL,
    parentrulesid character varying(36) NOT NULL
);


ALTER TABLE rules.rulesxrules OWNER TO postgres;

--
-- TOC entry 365 (class 1259 OID 29289)
-- Name: rulesxrulessecuritytoken; Type: TABLE; Schema: rules; Owner: postgres
--

CREATE TABLE rules.rulesxrulessecuritytoken (
    rulesxrulessecuritytokenid character varying(36) NOT NULL,
    effectivefromdate timestamp(6) without time zone NOT NULL,
    effectivetodate timestamp(6) without time zone NOT NULL,
    warehousecreatedtimestamp timestamp(6) without time zone NOT NULL,
    warehouselastupdatedtimestamp timestamp(6) without time zone NOT NULL,
    createallowed integer NOT NULL,
    deleteallowed integer NOT NULL,
    originalsourcesystemuniqueid character varying(255) NOT NULL,
    readallowed integer NOT NULL,
    updateallowed integer NOT NULL,
    activeflagid character varying(36) NOT NULL,
    enterpriseid character varying(36) NOT NULL,
    originalsourcesystemid character varying(36) NOT NULL,
    securitytokenid character varying(36) NOT NULL,
    systemid character varying(36) NOT NULL,
    rulesxrulesid character varying(36) NOT NULL
);


ALTER TABLE rules.rulesxrulessecuritytoken OWNER TO postgres;

--
-- TOC entry 366 (class 1259 OID 29296)
-- Name: rulesxrulestype; Type: TABLE; Schema: rules; Owner: postgres
--

CREATE TABLE rules.rulesxrulestype (
    rulesxrulestypeid character varying(36) NOT NULL,
    effectivefromdate timestamp(6) without time zone NOT NULL,
    effectivetodate timestamp(6) without time zone NOT NULL,
    warehousecreatedtimestamp timestamp(6) without time zone NOT NULL,
    warehouselastupdatedtimestamp timestamp(6) without time zone NOT NULL,
    originalsourcesystemuniqueid character varying(255) NOT NULL,
    value character varying(255) NOT NULL,
    activeflagid character varying(36) NOT NULL,
    enterpriseid character varying(36) NOT NULL,
    systemid character varying(36) NOT NULL,
    originalsourcesystemid character varying(36) NOT NULL,
    classificationid character varying(36) NOT NULL,
    rulesid character varying(36) NOT NULL,
    rulestypeid character varying(36) NOT NULL
);


ALTER TABLE rules.rulesxrulestype OWNER TO postgres;

--
-- TOC entry 367 (class 1259 OID 29303)
-- Name: rulesxrulestypesecuritytoken; Type: TABLE; Schema: rules; Owner: postgres
--

CREATE TABLE rules.rulesxrulestypesecuritytoken (
    rulesxrulestypesecuritytokenid character varying(36) NOT NULL,
    effectivefromdate timestamp(6) without time zone NOT NULL,
    effectivetodate timestamp(6) without time zone NOT NULL,
    warehousecreatedtimestamp timestamp(6) without time zone NOT NULL,
    warehouselastupdatedtimestamp timestamp(6) without time zone NOT NULL,
    createallowed integer NOT NULL,
    deleteallowed integer NOT NULL,
    originalsourcesystemuniqueid character varying(255) NOT NULL,
    readallowed integer NOT NULL,
    updateallowed integer NOT NULL,
    activeflagid character varying(36) NOT NULL,
    enterpriseid character varying(36) NOT NULL,
    originalsourcesystemid character varying(36) NOT NULL,
    securitytokenid character varying(36) NOT NULL,
    systemid character varying(36) NOT NULL,
    rulesxrulestypeid character varying(36) NOT NULL
);


ALTER TABLE rules.rulesxrulestypesecuritytoken OWNER TO postgres;

--
-- TOC entry 368 (class 1259 OID 29310)
-- Name: securityhierarchy; Type: TABLE; Schema: security; Owner: postgres
--

CREATE TABLE security.securityhierarchy (
    id character varying(36) NOT NULL,
    name character varying(255),
    one integer,
    parentid character varying(36),
    path character varying(255),
    pather character varying(255)
);


ALTER TABLE security.securityhierarchy OWNER TO postgres;

--
-- TOC entry 369 (class 1259 OID 29317)
-- Name: securitytoken; Type: TABLE; Schema: security; Owner: postgres
--

CREATE TABLE security.securitytoken (
    securitytokenid character varying(36) NOT NULL,
    effectivefromdate timestamp(6) without time zone NOT NULL,
    effectivetodate timestamp(6) without time zone NOT NULL,
    warehousecreatedtimestamp timestamp(6) without time zone NOT NULL,
    warehouselastupdatedtimestamp timestamp(6) without time zone NOT NULL,
    originalsourcesystemuniqueid character varying(255) NOT NULL,
    securitytokenfriendlydescription character varying(255) NOT NULL,
    securitytokenfriendlyname character varying(255) NOT NULL,
    securitytoken character varying(128) NOT NULL,
    activeflagid character varying(36) NOT NULL,
    enterpriseid character varying(36) NOT NULL,
    systemid character varying(36) NOT NULL,
    originalsourcesystemid character varying(36) NOT NULL,
    securitytokenclassificationid character varying(36) NOT NULL
);


ALTER TABLE security.securitytoken OWNER TO postgres;

--
-- TOC entry 370 (class 1259 OID 29324)
-- Name: securitytokenssecuritytoken; Type: TABLE; Schema: security; Owner: postgres
--

CREATE TABLE security.securitytokenssecuritytoken (
    securitytokenaccessid character varying(36) NOT NULL,
    effectivefromdate timestamp(6) without time zone NOT NULL,
    effectivetodate timestamp(6) without time zone NOT NULL,
    warehousecreatedtimestamp timestamp(6) without time zone NOT NULL,
    warehouselastupdatedtimestamp timestamp(6) without time zone NOT NULL,
    createallowed integer NOT NULL,
    deleteallowed integer NOT NULL,
    originalsourcesystemuniqueid character varying(255) NOT NULL,
    readallowed integer NOT NULL,
    updateallowed integer NOT NULL,
    activeflagid character varying(36) NOT NULL,
    enterpriseid character varying(36) NOT NULL,
    originalsourcesystemid character varying(36) NOT NULL,
    securitytokenid character varying(36) NOT NULL,
    systemid character varying(36) NOT NULL,
    securitytokentoid character varying(36) NOT NULL
);


ALTER TABLE security.securitytokenssecuritytoken OWNER TO postgres;

--
-- TOC entry 371 (class 1259 OID 29331)
-- Name: securitytokensxsecuritytokensecuritytoken; Type: TABLE; Schema: security; Owner: postgres
--

CREATE TABLE security.securitytokensxsecuritytokensecuritytoken (
    securitytokenxsecuritytokensecuritytokenid character varying(36) NOT NULL,
    effectivefromdate timestamp(6) without time zone NOT NULL,
    effectivetodate timestamp(6) without time zone NOT NULL,
    warehousecreatedtimestamp timestamp(6) without time zone NOT NULL,
    warehouselastupdatedtimestamp timestamp(6) without time zone NOT NULL,
    createallowed integer NOT NULL,
    deleteallowed integer NOT NULL,
    originalsourcesystemuniqueid character varying(255) NOT NULL,
    readallowed integer NOT NULL,
    updateallowed integer NOT NULL,
    activeflagid character varying(36) NOT NULL,
    enterpriseid character varying(36) NOT NULL,
    originalsourcesystemid character varying(36) NOT NULL,
    securitytokenid character varying(36) NOT NULL,
    systemid character varying(36) NOT NULL,
    securitytokenxsecuritytokenid character varying(36) NOT NULL
);


ALTER TABLE security.securitytokensxsecuritytokensecuritytoken OWNER TO postgres;

--
-- TOC entry 372 (class 1259 OID 29338)
-- Name: securitytokenxclassification; Type: TABLE; Schema: security; Owner: postgres
--

CREATE TABLE security.securitytokenxclassification (
    securitytokenxclassificationid character varying(36) NOT NULL,
    effectivefromdate timestamp(6) without time zone NOT NULL,
    effectivetodate timestamp(6) without time zone NOT NULL,
    warehousecreatedtimestamp timestamp(6) without time zone NOT NULL,
    warehouselastupdatedtimestamp timestamp(6) without time zone NOT NULL,
    originalsourcesystemuniqueid character varying(255) NOT NULL,
    value character varying(255) NOT NULL,
    activeflagid character varying(36) NOT NULL,
    enterpriseid character varying(36) NOT NULL,
    systemid character varying(36) NOT NULL,
    originalsourcesystemid character varying(36) NOT NULL,
    classificationid character varying(36) NOT NULL,
    securitytokenid character varying(36) NOT NULL
);


ALTER TABLE security.securitytokenxclassification OWNER TO postgres;

--
-- TOC entry 373 (class 1259 OID 29345)
-- Name: securitytokenxclassificationsecuritytoken; Type: TABLE; Schema: security; Owner: postgres
--

CREATE TABLE security.securitytokenxclassificationsecuritytoken (
    securitytokenxclassificationsecuritytokenid character varying(36) NOT NULL,
    effectivefromdate timestamp(6) without time zone NOT NULL,
    effectivetodate timestamp(6) without time zone NOT NULL,
    warehousecreatedtimestamp timestamp(6) without time zone NOT NULL,
    warehouselastupdatedtimestamp timestamp(6) without time zone NOT NULL,
    createallowed integer NOT NULL,
    deleteallowed integer NOT NULL,
    originalsourcesystemuniqueid character varying(255) NOT NULL,
    readallowed integer NOT NULL,
    updateallowed integer NOT NULL,
    activeflagid character varying(36) NOT NULL,
    enterpriseid character varying(36) NOT NULL,
    originalsourcesystemid character varying(36) NOT NULL,
    securitytokenid character varying(36) NOT NULL,
    systemid character varying(36) NOT NULL,
    securitytokenxclassificationid character varying(36) NOT NULL
);


ALTER TABLE security.securitytokenxclassificationsecuritytoken OWNER TO postgres;

--
-- TOC entry 374 (class 1259 OID 29352)
-- Name: securitytokenxsecuritytoken; Type: TABLE; Schema: security; Owner: postgres
--

CREATE TABLE security.securitytokenxsecuritytoken (
    securitytokenxsecuritytokenid character varying(36) NOT NULL,
    effectivefromdate timestamp(6) without time zone NOT NULL,
    effectivetodate timestamp(6) without time zone NOT NULL,
    warehousecreatedtimestamp timestamp(6) without time zone NOT NULL,
    warehouselastupdatedtimestamp timestamp(6) without time zone NOT NULL,
    originalsourcesystemuniqueid character varying(255) NOT NULL,
    value character varying(255) NOT NULL,
    activeflagid character varying(36) NOT NULL,
    enterpriseid character varying(36) NOT NULL,
    systemid character varying(36) NOT NULL,
    originalsourcesystemid character varying(36) NOT NULL,
    classificationid character varying(36) NOT NULL,
    childsecuritytokenid character varying(36) NOT NULL,
    parentsecuritytokenid character varying(36) NOT NULL
);


ALTER TABLE security.securitytokenxsecuritytoken OWNER TO postgres;

--
-- TOC entry 375 (class 1259 OID 29359)
-- Name: daynames; Type: TABLE; Schema: time; Owner: postgres
--

CREATE TABLE "time".daynames (
    daynameid integer NOT NULL,
    dayabbreviation character varying(50) NOT NULL,
    daybusinessdayclassification character varying(50) NOT NULL,
    dayisbusinessday smallint NOT NULL,
    daylongabbreviation character varying(50) NOT NULL,
    dayname character varying(100) NOT NULL,
    dayshortname character varying(200) NOT NULL,
    daysortorder integer NOT NULL
);


ALTER TABLE "time".daynames OWNER TO postgres;

--
-- TOC entry 376 (class 1259 OID 29364)
-- Name: dayparts; Type: TABLE; Schema: time; Owner: postgres
--

CREATE TABLE "time".dayparts (
    daypartid integer NOT NULL,
    daypartdescription character varying(100) NOT NULL,
    daypartname character varying(100) NOT NULL,
    daypartsortorder integer NOT NULL
);


ALTER TABLE "time".dayparts OWNER TO postgres;

--
-- TOC entry 377 (class 1259 OID 29369)
-- Name: days; Type: TABLE; Schema: time; Owner: postgres
--

CREATE TABLE "time".days (
    dayid integer NOT NULL,
    dayddmmyyyydescription character varying(50) NOT NULL,
    dayddmmyyyyhyphendescription character varying(50) NOT NULL,
    dayddmmyyyyslashdescription character varying(50) NOT NULL,
    daydate date NOT NULL,
    daydatetime timestamp(6) without time zone NOT NULL,
    dayfulldescription character varying(50) NOT NULL,
    dayinmonth integer NOT NULL,
    dayinyear integer NOT NULL,
    dayispublicholiday smallint NOT NULL,
    daylongdescription character varying(50) NOT NULL,
    daymmqqdescription character varying(50) NOT NULL,
    daymonthdescription character varying(50) NOT NULL,
    dayyyyymmdescription character varying(50) NOT NULL,
    lastdayid integer NOT NULL,
    lastmonthid integer NOT NULL,
    lastquarterid integer NOT NULL,
    lastyearid integer NOT NULL,
    quarterid integer NOT NULL,
    yearid integer NOT NULL,
    daynameid integer NOT NULL,
    monthid integer NOT NULL,
    weekid integer NOT NULL
);


ALTER TABLE "time".days OWNER TO postgres;

--
-- TOC entry 378 (class 1259 OID 29374)
-- Name: halfhourdayparts; Type: TABLE; Schema: time; Owner: postgres
--

CREATE TABLE "time".halfhourdayparts (
    halfhourdaypartid integer NOT NULL,
    hourid integer NOT NULL,
    minuteid integer NOT NULL,
    daypartid integer NOT NULL
);


ALTER TABLE "time".halfhourdayparts OWNER TO postgres;

--
-- TOC entry 379 (class 1259 OID 29379)
-- Name: halfhours; Type: TABLE; Schema: time; Owner: postgres
--

CREATE TABLE "time".halfhours (
    hourid integer NOT NULL,
    minuteid integer NOT NULL,
    ampmdesc character varying(5) NOT NULL,
    previoushalfhourminuteid integer NOT NULL,
    previoushourid integer NOT NULL,
    twelvehourclockdesc character varying(10) NOT NULL,
    twentyfourhourclockdesc character varying(10) NOT NULL
);


ALTER TABLE "time".halfhours OWNER TO postgres;

--
-- TOC entry 380 (class 1259 OID 29384)
-- Name: hours; Type: TABLE; Schema: time; Owner: postgres
--

CREATE TABLE "time".hours (
    hourid integer NOT NULL,
    ampmdesc character varying(5) NOT NULL,
    previoushourid integer NOT NULL,
    twelvehourclockdesc character varying(10) NOT NULL,
    twentyfourhourclockdesc character varying(10) NOT NULL
);


ALTER TABLE "time".hours OWNER TO postgres;

--
-- TOC entry 381 (class 1259 OID 29389)
-- Name: monthofyear; Type: TABLE; Schema: time; Owner: postgres
--

CREATE TABLE "time".monthofyear (
    monthofyearid integer NOT NULL,
    monthofyearabbreviation character varying(50) NOT NULL,
    monthofyearname character varying(50) NOT NULL,
    monthofyearshortname character varying(50) NOT NULL,
    monthinyearnumber integer NOT NULL
);


ALTER TABLE "time".monthofyear OWNER TO postgres;

--
-- TOC entry 382 (class 1259 OID 29394)
-- Name: months; Type: TABLE; Schema: time; Owner: postgres
--

CREATE TABLE "time".months (
    monthid integer NOT NULL,
    lastmonthid integer NOT NULL,
    lastquarterid integer NOT NULL,
    lastyearid integer NOT NULL,
    monthdayduration smallint NOT NULL,
    monthdescription character varying(50) NOT NULL,
    monthmmmyydescription character varying(50) NOT NULL,
    monthmmyyyydescription character varying(50) NOT NULL,
    monthnameyyyydescription character varying(50) NOT NULL,
    monthshortdescription character varying(50) NOT NULL,
    monthyydescription character varying(50) NOT NULL,
    yearid integer NOT NULL,
    monthofyearid integer NOT NULL,
    quarterid integer NOT NULL
);


ALTER TABLE "time".months OWNER TO postgres;

--
-- TOC entry 383 (class 1259 OID 29399)
-- Name: publicholidays; Type: TABLE; Schema: time; Owner: postgres
--

CREATE TABLE "time".publicholidays (
    publicholidayid integer NOT NULL,
    dayid integer NOT NULL,
    publicholidayname character varying(250) NOT NULL,
    publicholidaytype character varying(250) NOT NULL
);


ALTER TABLE "time".publicholidays OWNER TO postgres;

--
-- TOC entry 384 (class 1259 OID 29406)
-- Name: quarters; Type: TABLE; Schema: time; Owner: postgres
--

CREATE TABLE "time".quarters (
    quarterid integer NOT NULL,
    lastquarterid smallint NOT NULL,
    lastyearid smallint NOT NULL,
    quarterdescription character varying(50) NOT NULL,
    quartergraphdescription character varying(50) NOT NULL,
    quartergriddescription character varying(50) NOT NULL,
    quarterinyear integer NOT NULL,
    quarterqqmmdescription character varying(50) NOT NULL,
    quartersmalldescription character varying(50) NOT NULL,
    quarteryymmdescription character varying(50) NOT NULL,
    quarteryeardescription character varying(50) NOT NULL,
    yearid smallint NOT NULL
);


ALTER TABLE "time".quarters OWNER TO postgres;

--
-- TOC entry 385 (class 1259 OID 29411)
-- Name: time; Type: TABLE; Schema: time; Owner: postgres
--

CREATE TABLE "time"."time" (
    hourid integer NOT NULL,
    minuteid integer NOT NULL,
    ampmdesc character varying(5) NOT NULL,
    previoushourid integer NOT NULL,
    previousminuteid integer NOT NULL,
    twelvehourclockdesc character varying(10) NOT NULL,
    twentyfourhourclockdesc character varying(10) NOT NULL
);


ALTER TABLE "time"."time" OWNER TO postgres;

--
-- TOC entry 386 (class 1259 OID 29416)
-- Name: trans_fiscal; Type: TABLE; Schema: time; Owner: postgres
--

CREATE TABLE "time".trans_fiscal (
    dayid integer NOT NULL,
    fiscaldayid integer NOT NULL
);


ALTER TABLE "time".trans_fiscal OWNER TO postgres;

--
-- TOC entry 387 (class 1259 OID 29421)
-- Name: trans_mtd; Type: TABLE; Schema: time; Owner: postgres
--

CREATE TABLE "time".trans_mtd (
    dayid integer NOT NULL,
    mtddayid integer NOT NULL
);


ALTER TABLE "time".trans_mtd OWNER TO postgres;

--
-- TOC entry 388 (class 1259 OID 29426)
-- Name: trans_qtd; Type: TABLE; Schema: time; Owner: postgres
--

CREATE TABLE "time".trans_qtd (
    dayid integer NOT NULL,
    qtddayid integer NOT NULL
);


ALTER TABLE "time".trans_qtd OWNER TO postgres;

--
-- TOC entry 389 (class 1259 OID 29431)
-- Name: trans_qtm; Type: TABLE; Schema: time; Owner: postgres
--

CREATE TABLE "time".trans_qtm (
    monthid integer NOT NULL,
    qtm_monthid integer NOT NULL
);


ALTER TABLE "time".trans_qtm OWNER TO postgres;

--
-- TOC entry 390 (class 1259 OID 29436)
-- Name: trans_ytd; Type: TABLE; Schema: time; Owner: postgres
--

CREATE TABLE "time".trans_ytd (
    dayid integer NOT NULL,
    ytddayid integer NOT NULL
);


ALTER TABLE "time".trans_ytd OWNER TO postgres;

--
-- TOC entry 391 (class 1259 OID 29441)
-- Name: weeks; Type: TABLE; Schema: time; Owner: postgres
--

CREATE TABLE "time".weeks (
    weekid integer NOT NULL,
    monthid integer NOT NULL,
    quarterid integer NOT NULL,
    weekdescription character varying(50) NOT NULL,
    weekofmonth integer NOT NULL,
    weekofyear integer NOT NULL,
    weekshortdescription character varying(50) NOT NULL,
    yearid integer NOT NULL
);


ALTER TABLE "time".weeks OWNER TO postgres;

--
-- TOC entry 392 (class 1259 OID 29446)
-- Name: years; Type: TABLE; Schema: time; Owner: postgres
--

CREATE TABLE "time".years (
    yearid smallint NOT NULL,
    century smallint NOT NULL,
    lastyearid smallint NOT NULL,
    leapyearflag smallint NOT NULL,
    yyname character varying(2) NOT NULL,
    yyyname character varying(3) NOT NULL,
    yearfullname character varying(250) NOT NULL,
    yearname character varying(10) NOT NULL
);


ALTER TABLE "time".years OWNER TO postgres;

--
-- TOC entry 5315 (class 0 OID 28290)
-- Dependencies: 220
-- Data for Name: address; Type: TABLE DATA; Schema: address; Owner: postgres
--

COPY address.address (addressid, effectivefromdate, effectivetodate, warehousecreatedtimestamp, warehouselastupdatedtimestamp, originalsourcesystemuniqueid, value, activeflagid, enterpriseid, systemid, originalsourcesystemid, classificationid) FROM stdin;
\.


--
-- TOC entry 5316 (class 0 OID 28297)
-- Dependencies: 221
-- Data for Name: addresssecuritytoken; Type: TABLE DATA; Schema: address; Owner: postgres
--

COPY address.addresssecuritytoken (addresssecuritytokenid, effectivefromdate, effectivetodate, warehousecreatedtimestamp, warehouselastupdatedtimestamp, createallowed, deleteallowed, originalsourcesystemuniqueid, readallowed, updateallowed, activeflagid, enterpriseid, originalsourcesystemid, securitytokenid, systemid, addressid) FROM stdin;
\.


--
-- TOC entry 5317 (class 0 OID 28304)
-- Dependencies: 222
-- Data for Name: addressxclassification; Type: TABLE DATA; Schema: address; Owner: postgres
--

COPY address.addressxclassification (addressxclassificationid, effectivefromdate, effectivetodate, warehousecreatedtimestamp, warehouselastupdatedtimestamp, originalsourcesystemuniqueid, value, activeflagid, enterpriseid, systemid, originalsourcesystemid, classificationid, addressid) FROM stdin;
\.


--
-- TOC entry 5318 (class 0 OID 28311)
-- Dependencies: 223
-- Data for Name: addressxclassificationsecuritytoken; Type: TABLE DATA; Schema: address; Owner: postgres
--

COPY address.addressxclassificationsecuritytoken (addressxclassificationsecuritytokenid, effectivefromdate, effectivetodate, warehousecreatedtimestamp, warehouselastupdatedtimestamp, createallowed, deleteallowed, originalsourcesystemuniqueid, readallowed, updateallowed, activeflagid, enterpriseid, originalsourcesystemid, securitytokenid, systemid, addressxclassificationid) FROM stdin;
\.


--
-- TOC entry 5319 (class 0 OID 28318)
-- Dependencies: 224
-- Data for Name: addressxgeography; Type: TABLE DATA; Schema: address; Owner: postgres
--

COPY address.addressxgeography (addressxgeographyid, effectivefromdate, effectivetodate, warehousecreatedtimestamp, warehouselastupdatedtimestamp, originalsourcesystemuniqueid, value, activeflagid, enterpriseid, systemid, originalsourcesystemid, classificationid, addressid, geographyid) FROM stdin;
\.


--
-- TOC entry 5320 (class 0 OID 28325)
-- Dependencies: 225
-- Data for Name: addressxgeographysecuritytoken; Type: TABLE DATA; Schema: address; Owner: postgres
--

COPY address.addressxgeographysecuritytoken (addressxgeographysecuritytokenid, effectivefromdate, effectivetodate, warehousecreatedtimestamp, warehouselastupdatedtimestamp, createallowed, deleteallowed, originalsourcesystemuniqueid, readallowed, updateallowed, activeflagid, enterpriseid, originalsourcesystemid, securitytokenid, systemid, addressxgeographyid) FROM stdin;
\.


--
-- TOC entry 5321 (class 0 OID 28332)
-- Dependencies: 226
-- Data for Name: addressxresourceitem; Type: TABLE DATA; Schema: address; Owner: postgres
--

COPY address.addressxresourceitem (addressxresourceitemid, effectivefromdate, effectivetodate, warehousecreatedtimestamp, warehouselastupdatedtimestamp, originalsourcesystemuniqueid, value, activeflagid, enterpriseid, systemid, originalsourcesystemid, classificationid, addressid, resourceitemid) FROM stdin;
\.


--
-- TOC entry 5322 (class 0 OID 28339)
-- Dependencies: 227
-- Data for Name: addressxresourceitemsecuritytoken; Type: TABLE DATA; Schema: address; Owner: postgres
--

COPY address.addressxresourceitemsecuritytoken (addressxresourceitemsecuritytokenid, effectivefromdate, effectivetodate, warehousecreatedtimestamp, warehouselastupdatedtimestamp, createallowed, deleteallowed, originalsourcesystemuniqueid, readallowed, updateallowed, activeflagid, enterpriseid, originalsourcesystemid, securitytokenid, systemid, addressxresourceitemid) FROM stdin;
\.


--
-- TOC entry 5323 (class 0 OID 28346)
-- Dependencies: 228
-- Data for Name: classification; Type: TABLE DATA; Schema: classification; Owner: postgres
--

COPY classification.classification (classificationid, effectivefromdate, effectivetodate, warehousecreatedtimestamp, warehouselastupdatedtimestamp, originalsourcesystemuniqueid, classificationsequencenumber, classificationdesc, classificationname, activeflagid, enterpriseid, systemid, originalsourcesystemid, classificationdataconceptid) FROM stdin;
\.


--
-- TOC entry 5324 (class 0 OID 28353)
-- Dependencies: 229
-- Data for Name: classificationdataconcept; Type: TABLE DATA; Schema: classification; Owner: postgres
--

COPY classification.classificationdataconcept (classificationdataconceptid, effectivefromdate, effectivetodate, warehousecreatedtimestamp, warehouselastupdatedtimestamp, originalsourcesystemuniqueid, classificationdataconceptdesc, classificationdataconceptname, activeflagid, enterpriseid, systemid, originalsourcesystemid) FROM stdin;
\.


--
-- TOC entry 5325 (class 0 OID 28360)
-- Dependencies: 230
-- Data for Name: classificationdataconceptsecuritytoken; Type: TABLE DATA; Schema: classification; Owner: postgres
--

COPY classification.classificationdataconceptsecuritytoken (classificationdataconceptsecuritytokenid, effectivefromdate, effectivetodate, warehousecreatedtimestamp, warehouselastupdatedtimestamp, createallowed, deleteallowed, originalsourcesystemuniqueid, readallowed, updateallowed, activeflagid, enterpriseid, originalsourcesystemid, securitytokenid, systemid, classificationdataconceptid) FROM stdin;
\.


--
-- TOC entry 5326 (class 0 OID 28367)
-- Dependencies: 231
-- Data for Name: classificationdataconceptxclassification; Type: TABLE DATA; Schema: classification; Owner: postgres
--

COPY classification.classificationdataconceptxclassification (classificationdataconceptxclassificationid, effectivefromdate, effectivetodate, warehousecreatedtimestamp, warehouselastupdatedtimestamp, originalsourcesystemuniqueid, value, activeflagid, enterpriseid, systemid, originalsourcesystemid, classificationid, classificationdataconceptid) FROM stdin;
\.


--
-- TOC entry 5327 (class 0 OID 28374)
-- Dependencies: 232
-- Data for Name: classificationdataconceptxclassificationsecuritytoken; Type: TABLE DATA; Schema: classification; Owner: postgres
--

COPY classification.classificationdataconceptxclassificationsecuritytoken (classificationdataconceptxclassificationsecuritytokenid, effectivefromdate, effectivetodate, warehousecreatedtimestamp, warehouselastupdatedtimestamp, createallowed, deleteallowed, originalsourcesystemuniqueid, readallowed, updateallowed, activeflagid, enterpriseid, originalsourcesystemid, securitytokenid, systemid, classificationdataconceptxclassificationid) FROM stdin;
\.


--
-- TOC entry 5328 (class 0 OID 28381)
-- Dependencies: 233
-- Data for Name: classificationdataconceptxresourceitem; Type: TABLE DATA; Schema: classification; Owner: postgres
--

COPY classification.classificationdataconceptxresourceitem (classificationdataconceptxresourceitemid, effectivefromdate, effectivetodate, warehousecreatedtimestamp, warehouselastupdatedtimestamp, originalsourcesystemuniqueid, value, activeflagid, enterpriseid, systemid, originalsourcesystemid, classificationid, classificationdataconceptid, resourceitemid) FROM stdin;
\.


--
-- TOC entry 5329 (class 0 OID 28388)
-- Dependencies: 234
-- Data for Name: classificationdataconceptxresourceitemsecuritytoken; Type: TABLE DATA; Schema: classification; Owner: postgres
--

COPY classification.classificationdataconceptxresourceitemsecuritytoken (classificationdataconceptxresourceitemsecuritytokenid, effectivefromdate, effectivetodate, warehousecreatedtimestamp, warehouselastupdatedtimestamp, createallowed, deleteallowed, originalsourcesystemuniqueid, readallowed, updateallowed, activeflagid, enterpriseid, originalsourcesystemid, securitytokenid, systemid, classificationdataconceptxresourceitemid) FROM stdin;
\.


--
-- TOC entry 5330 (class 0 OID 28395)
-- Dependencies: 235
-- Data for Name: classificationsecuritytoken; Type: TABLE DATA; Schema: classification; Owner: postgres
--

COPY classification.classificationsecuritytoken (classificationsecuritytokenid, effectivefromdate, effectivetodate, warehousecreatedtimestamp, warehouselastupdatedtimestamp, createallowed, deleteallowed, originalsourcesystemuniqueid, readallowed, updateallowed, activeflagid, enterpriseid, originalsourcesystemid, securitytokenid, systemid, classificationid) FROM stdin;
\.


--
-- TOC entry 5331 (class 0 OID 28402)
-- Dependencies: 236
-- Data for Name: classificationxclassification; Type: TABLE DATA; Schema: classification; Owner: postgres
--

COPY classification.classificationxclassification (classificationxclassificationid, effectivefromdate, effectivetodate, warehousecreatedtimestamp, warehouselastupdatedtimestamp, originalsourcesystemuniqueid, value, activeflagid, enterpriseid, systemid, originalsourcesystemid, classificationid, childclassificationid, parentclassificationid) FROM stdin;
\.


--
-- TOC entry 5332 (class 0 OID 28409)
-- Dependencies: 237
-- Data for Name: classificationxclassificationsecuritytoken; Type: TABLE DATA; Schema: classification; Owner: postgres
--

COPY classification.classificationxclassificationsecuritytoken (classificationxclassificationsecuritytokenid, effectivefromdate, effectivetodate, warehousecreatedtimestamp, warehouselastupdatedtimestamp, createallowed, deleteallowed, originalsourcesystemuniqueid, readallowed, updateallowed, activeflagid, enterpriseid, originalsourcesystemid, securitytokenid, systemid, classificationxclassificationid) FROM stdin;
\.


--
-- TOC entry 5333 (class 0 OID 28416)
-- Dependencies: 238
-- Data for Name: classificationxresourceitem; Type: TABLE DATA; Schema: classification; Owner: postgres
--

COPY classification.classificationxresourceitem (classificationxresourceitemid, effectivefromdate, effectivetodate, warehousecreatedtimestamp, warehouselastupdatedtimestamp, originalsourcesystemuniqueid, value, activeflagid, enterpriseid, systemid, originalsourcesystemid, classificationid, resourceitemid) FROM stdin;
\.


--
-- TOC entry 5334 (class 0 OID 28423)
-- Dependencies: 239
-- Data for Name: classificationxresourceitemsecuritytoken; Type: TABLE DATA; Schema: classification; Owner: postgres
--

COPY classification.classificationxresourceitemsecuritytoken (classificationxresourceitemsecuritytokenid, effectivefromdate, effectivetodate, warehousecreatedtimestamp, warehouselastupdatedtimestamp, createallowed, deleteallowed, originalsourcesystemuniqueid, readallowed, updateallowed, activeflagid, enterpriseid, originalsourcesystemid, securitytokenid, systemid, classificationxresourceitemid) FROM stdin;
\.


--
-- TOC entry 5335 (class 0 OID 28430)
-- Dependencies: 240
-- Data for Name: activeflag; Type: TABLE DATA; Schema: dbo; Owner: postgres
--

COPY dbo.activeflag (activeflagid, effectivefromdate, effectivetodate, warehousecreatedtimestamp, warehouselastupdatedtimestamp, allowaccess, activeflagdescription, activeflagname, enterpriseid) FROM stdin;
\.


--
-- TOC entry 5336 (class 0 OID 28435)
-- Dependencies: 241
-- Data for Name: activeflagsecuritytoken; Type: TABLE DATA; Schema: dbo; Owner: postgres
--

COPY dbo.activeflagsecuritytoken (activeflagsecuritytokenid, effectivefromdate, effectivetodate, warehousecreatedtimestamp, warehouselastupdatedtimestamp, createallowed, deleteallowed, originalsourcesystemuniqueid, readallowed, updateallowed, activeflagid, enterpriseid, originalsourcesystemid, securitytokenid, systemid, securitytokenactiveflagid) FROM stdin;
\.


--
-- TOC entry 5337 (class 0 OID 28442)
-- Dependencies: 242
-- Data for Name: activeflagxclassification; Type: TABLE DATA; Schema: dbo; Owner: postgres
--

COPY dbo.activeflagxclassification (activeflagxclassificationid, effectivefromdate, effectivetodate, warehousecreatedtimestamp, warehouselastupdatedtimestamp, originalsourcesystemuniqueid, value, activeflagid, enterpriseid, systemid, originalsourcesystemid, classificationid) FROM stdin;
\.


--
-- TOC entry 5338 (class 0 OID 28449)
-- Dependencies: 243
-- Data for Name: activeflagxclassificationsecuritytoken; Type: TABLE DATA; Schema: dbo; Owner: postgres
--

COPY dbo.activeflagxclassificationsecuritytoken (activeflagxclassificationsecuritytokenid, effectivefromdate, effectivetodate, warehousecreatedtimestamp, warehouselastupdatedtimestamp, createallowed, deleteallowed, originalsourcesystemuniqueid, readallowed, updateallowed, activeflagid, enterpriseid, originalsourcesystemid, securitytokenid, systemid, activeflagxclassificationid) FROM stdin;
\.


--
-- TOC entry 5339 (class 0 OID 28456)
-- Dependencies: 244
-- Data for Name: enterprise; Type: TABLE DATA; Schema: dbo; Owner: postgres
--

COPY dbo.enterprise (enterpriseid, effectivefromdate, effectivetodate, warehousecreatedtimestamp, warehouselastupdatedtimestamp, enterprisedesc, enterprisename) FROM stdin;
\.


--
-- TOC entry 5340 (class 0 OID 28463)
-- Dependencies: 245
-- Data for Name: enterprisesecuritytoken; Type: TABLE DATA; Schema: dbo; Owner: postgres
--

COPY dbo.enterprisesecuritytoken (enterprisesecuritytokenid, effectivefromdate, effectivetodate, warehousecreatedtimestamp, warehouselastupdatedtimestamp, createallowed, deleteallowed, originalsourcesystemuniqueid, readallowed, updateallowed, activeflagid, enterpriseid, originalsourcesystemid, securitytokenid, systemid) FROM stdin;
\.


--
-- TOC entry 5341 (class 0 OID 28468)
-- Dependencies: 246
-- Data for Name: enterprisexclassification; Type: TABLE DATA; Schema: dbo; Owner: postgres
--

COPY dbo.enterprisexclassification (enterprisexclassificationid, effectivefromdate, effectivetodate, warehousecreatedtimestamp, warehouselastupdatedtimestamp, originalsourcesystemuniqueid, value, activeflagid, enterpriseid, systemid, originalsourcesystemid, classificationid) FROM stdin;
\.


--
-- TOC entry 5342 (class 0 OID 28475)
-- Dependencies: 247
-- Data for Name: enterprisexclassificationsecuritytoken; Type: TABLE DATA; Schema: dbo; Owner: postgres
--

COPY dbo.enterprisexclassificationsecuritytoken (enterprisexclassificationsecuritytokenid, effectivefromdate, effectivetodate, warehousecreatedtimestamp, warehouselastupdatedtimestamp, createallowed, deleteallowed, originalsourcesystemuniqueid, readallowed, updateallowed, activeflagid, enterpriseid, originalsourcesystemid, securitytokenid, systemid, enterprisexclassificationid) FROM stdin;
\.


--
-- TOC entry 5343 (class 0 OID 28482)
-- Dependencies: 248
-- Data for Name: systems; Type: TABLE DATA; Schema: dbo; Owner: postgres
--

COPY dbo.systems (systemid, effectivefromdate, effectivetodate, warehousecreatedtimestamp, warehouselastupdatedtimestamp, systemdesc, systemname, systemhistoryname, activeflagid, enterpriseid) FROM stdin;
\.


--
-- TOC entry 5344 (class 0 OID 28489)
-- Dependencies: 249
-- Data for Name: systemssecuritytoken; Type: TABLE DATA; Schema: dbo; Owner: postgres
--

COPY dbo.systemssecuritytoken (systemssecuritytokenid, effectivefromdate, effectivetodate, warehousecreatedtimestamp, warehouselastupdatedtimestamp, createallowed, deleteallowed, originalsourcesystemuniqueid, readallowed, updateallowed, activeflagid, enterpriseid, originalsourcesystemid, securitytokenid, systemid) FROM stdin;
\.


--
-- TOC entry 5345 (class 0 OID 28494)
-- Dependencies: 250
-- Data for Name: systemxclassification; Type: TABLE DATA; Schema: dbo; Owner: postgres
--

COPY dbo.systemxclassification (systemxclassificationid, effectivefromdate, effectivetodate, warehousecreatedtimestamp, warehouselastupdatedtimestamp, originalsourcesystemuniqueid, value, activeflagid, enterpriseid, systemid, originalsourcesystemid, classificationid) FROM stdin;
\.


--
-- TOC entry 5346 (class 0 OID 28501)
-- Dependencies: 251
-- Data for Name: systemxclassificationsecuritytoken; Type: TABLE DATA; Schema: dbo; Owner: postgres
--

COPY dbo.systemxclassificationsecuritytoken (systemxclassificationsecuritytokenid, effectivefromdate, effectivetodate, warehousecreatedtimestamp, warehouselastupdatedtimestamp, createallowed, deleteallowed, originalsourcesystemuniqueid, readallowed, updateallowed, activeflagid, enterpriseid, originalsourcesystemid, securitytokenid, systemid, systemxclassificationid) FROM stdin;
\.


--
-- TOC entry 5347 (class 0 OID 28508)
-- Dependencies: 252
-- Data for Name: event; Type: TABLE DATA; Schema: event; Owner: postgres
--

COPY event.event (eventid, effectivefromdate, effectivetodate, warehousecreatedtimestamp, warehouselastupdatedtimestamp, originalsourcesystemuniqueid, dayid, hourid, minuteid, activeflagid, enterpriseid, systemid, originalsourcesystemid) FROM stdin;
\.


--
-- TOC entry 5348 (class 0 OID 28513)
-- Dependencies: 253
-- Data for Name: eventsecuritytoken; Type: TABLE DATA; Schema: event; Owner: postgres
--

COPY event.eventsecuritytoken (eventssecuritytokenid, effectivefromdate, effectivetodate, warehousecreatedtimestamp, warehouselastupdatedtimestamp, createallowed, deleteallowed, originalsourcesystemuniqueid, readallowed, updateallowed, activeflagid, enterpriseid, originalsourcesystemid, securitytokenid, systemid, eventsid) FROM stdin;
\.


--
-- TOC entry 5349 (class 0 OID 28520)
-- Dependencies: 254
-- Data for Name: eventtype; Type: TABLE DATA; Schema: event; Owner: postgres
--

COPY event.eventtype (eventtypeid, effectivefromdate, effectivetodate, warehousecreatedtimestamp, warehouselastupdatedtimestamp, originalsourcesystemuniqueid, eventtypedesc, eventtypename, activeflagid, enterpriseid, systemid, originalsourcesystemid) FROM stdin;
\.


--
-- TOC entry 5350 (class 0 OID 28527)
-- Dependencies: 255
-- Data for Name: eventtypessecuritytoken; Type: TABLE DATA; Schema: event; Owner: postgres
--

COPY event.eventtypessecuritytoken (eventtypessecuritytokenid, effectivefromdate, effectivetodate, warehousecreatedtimestamp, warehouselastupdatedtimestamp, createallowed, deleteallowed, originalsourcesystemuniqueid, readallowed, updateallowed, activeflagid, enterpriseid, originalsourcesystemid, securitytokenid, systemid, eventtypesid) FROM stdin;
\.


--
-- TOC entry 5351 (class 0 OID 28534)
-- Dependencies: 256
-- Data for Name: eventxaddress; Type: TABLE DATA; Schema: event; Owner: postgres
--

COPY event.eventxaddress (eventxaddressid, effectivefromdate, effectivetodate, warehousecreatedtimestamp, warehouselastupdatedtimestamp, originalsourcesystemuniqueid, value, activeflagid, enterpriseid, systemid, originalsourcesystemid, classificationid, addressid, eventid) FROM stdin;
\.


--
-- TOC entry 5352 (class 0 OID 28541)
-- Dependencies: 257
-- Data for Name: eventxaddresssecuritytoken; Type: TABLE DATA; Schema: event; Owner: postgres
--

COPY event.eventxaddresssecuritytoken (eventxaddresssecuritytokenid, effectivefromdate, effectivetodate, warehousecreatedtimestamp, warehouselastupdatedtimestamp, createallowed, deleteallowed, originalsourcesystemuniqueid, readallowed, updateallowed, activeflagid, enterpriseid, originalsourcesystemid, securitytokenid, systemid, eventxaddressid) FROM stdin;
\.


--
-- TOC entry 5353 (class 0 OID 28548)
-- Dependencies: 258
-- Data for Name: eventxarrangement; Type: TABLE DATA; Schema: event; Owner: postgres
--

COPY event.eventxarrangement (eventxarrangementsid, effectivefromdate, effectivetodate, warehousecreatedtimestamp, warehouselastupdatedtimestamp, originalsourcesystemuniqueid, value, activeflagid, enterpriseid, systemid, originalsourcesystemid, classificationid, arrangementid, eventid) FROM stdin;
\.


--
-- TOC entry 5354 (class 0 OID 28555)
-- Dependencies: 259
-- Data for Name: eventxarrangementssecuritytoken; Type: TABLE DATA; Schema: event; Owner: postgres
--

COPY event.eventxarrangementssecuritytoken (eventxarrangementssecuritytokenid, effectivefromdate, effectivetodate, warehousecreatedtimestamp, warehouselastupdatedtimestamp, createallowed, deleteallowed, originalsourcesystemuniqueid, readallowed, updateallowed, activeflagid, enterpriseid, originalsourcesystemid, securitytokenid, systemid, eventxarrangementsid) FROM stdin;
\.


--
-- TOC entry 5355 (class 0 OID 28562)
-- Dependencies: 260
-- Data for Name: eventxclassification; Type: TABLE DATA; Schema: event; Owner: postgres
--

COPY event.eventxclassification (eventxclassificationid, effectivefromdate, effectivetodate, warehousecreatedtimestamp, warehouselastupdatedtimestamp, originalsourcesystemuniqueid, value, activeflagid, enterpriseid, systemid, originalsourcesystemid, classificationid, eventid) FROM stdin;
\.


--
-- TOC entry 5356 (class 0 OID 28569)
-- Dependencies: 261
-- Data for Name: eventxclassificationsecuritytoken; Type: TABLE DATA; Schema: event; Owner: postgres
--

COPY event.eventxclassificationsecuritytoken (eventxclassificationssecuritytokenid, effectivefromdate, effectivetodate, warehousecreatedtimestamp, warehouselastupdatedtimestamp, createallowed, deleteallowed, originalsourcesystemuniqueid, readallowed, updateallowed, activeflagid, enterpriseid, originalsourcesystemid, securitytokenid, systemid, eventxclassificationsid) FROM stdin;
\.


--
-- TOC entry 5357 (class 0 OID 28576)
-- Dependencies: 262
-- Data for Name: eventxevent; Type: TABLE DATA; Schema: event; Owner: postgres
--

COPY event.eventxevent (eventxeventid, effectivefromdate, effectivetodate, warehousecreatedtimestamp, warehouselastupdatedtimestamp, originalsourcesystemuniqueid, value, activeflagid, enterpriseid, systemid, originalsourcesystemid, classificationid, childeventid, parenteventid) FROM stdin;
\.


--
-- TOC entry 5358 (class 0 OID 28583)
-- Dependencies: 263
-- Data for Name: eventxeventsecuritytoken; Type: TABLE DATA; Schema: event; Owner: postgres
--

COPY event.eventxeventsecuritytoken (eventxeventsecuritytokenid, effectivefromdate, effectivetodate, warehousecreatedtimestamp, warehouselastupdatedtimestamp, createallowed, deleteallowed, originalsourcesystemuniqueid, readallowed, updateallowed, activeflagid, enterpriseid, originalsourcesystemid, securitytokenid, systemid, eventxeventid) FROM stdin;
\.


--
-- TOC entry 5359 (class 0 OID 28590)
-- Dependencies: 264
-- Data for Name: eventxeventtype; Type: TABLE DATA; Schema: event; Owner: postgres
--

COPY event.eventxeventtype (eventxeventtypeid, effectivefromdate, effectivetodate, warehousecreatedtimestamp, warehouselastupdatedtimestamp, originalsourcesystemuniqueid, value, activeflagid, enterpriseid, systemid, originalsourcesystemid, classificationid, eventid, eventtypeid) FROM stdin;
\.


--
-- TOC entry 5360 (class 0 OID 28597)
-- Dependencies: 265
-- Data for Name: eventxeventtypesecuritytoken; Type: TABLE DATA; Schema: event; Owner: postgres
--

COPY event.eventxeventtypesecuritytoken (eventxeventtypesecuritytokenid, effectivefromdate, effectivetodate, warehousecreatedtimestamp, warehouselastupdatedtimestamp, createallowed, deleteallowed, originalsourcesystemuniqueid, readallowed, updateallowed, activeflagid, enterpriseid, originalsourcesystemid, securitytokenid, systemid, eventxeventtypeid) FROM stdin;
\.


--
-- TOC entry 5361 (class 0 OID 28604)
-- Dependencies: 266
-- Data for Name: eventxgeography; Type: TABLE DATA; Schema: event; Owner: postgres
--

COPY event.eventxgeography (eventxgeographyid, effectivefromdate, effectivetodate, warehousecreatedtimestamp, warehouselastupdatedtimestamp, originalsourcesystemuniqueid, value, activeflagid, enterpriseid, systemid, originalsourcesystemid, classificationid, eventid, geographyid) FROM stdin;
\.


--
-- TOC entry 5362 (class 0 OID 28611)
-- Dependencies: 267
-- Data for Name: eventxgeographysecuritytoken; Type: TABLE DATA; Schema: event; Owner: postgres
--

COPY event.eventxgeographysecuritytoken (eventxgeographysecuritytokenid, effectivefromdate, effectivetodate, warehousecreatedtimestamp, warehouselastupdatedtimestamp, createallowed, deleteallowed, originalsourcesystemuniqueid, readallowed, updateallowed, activeflagid, enterpriseid, originalsourcesystemid, securitytokenid, systemid, eventxgeographyid) FROM stdin;
\.


--
-- TOC entry 5363 (class 0 OID 28618)
-- Dependencies: 268
-- Data for Name: eventxinvolvedparty; Type: TABLE DATA; Schema: event; Owner: postgres
--

COPY event.eventxinvolvedparty (eventxinvolvedpartyid, effectivefromdate, effectivetodate, warehousecreatedtimestamp, warehouselastupdatedtimestamp, originalsourcesystemuniqueid, value, activeflagid, enterpriseid, systemid, originalsourcesystemid, classificationid, eventid, involvedpartyid) FROM stdin;
\.


--
-- TOC entry 5364 (class 0 OID 28625)
-- Dependencies: 269
-- Data for Name: eventxinvolvedpartysecuritytoken; Type: TABLE DATA; Schema: event; Owner: postgres
--

COPY event.eventxinvolvedpartysecuritytoken (eventxinvolvedpartysecuritytokenid, effectivefromdate, effectivetodate, warehousecreatedtimestamp, warehouselastupdatedtimestamp, createallowed, deleteallowed, originalsourcesystemuniqueid, readallowed, updateallowed, activeflagid, enterpriseid, originalsourcesystemid, securitytokenid, systemid, eventxinvolvedpartyid) FROM stdin;
\.


--
-- TOC entry 5365 (class 0 OID 28632)
-- Dependencies: 270
-- Data for Name: eventxproduct; Type: TABLE DATA; Schema: event; Owner: postgres
--

COPY event.eventxproduct (eventxproductid, effectivefromdate, effectivetodate, warehousecreatedtimestamp, warehouselastupdatedtimestamp, originalsourcesystemuniqueid, value, activeflagid, enterpriseid, systemid, originalsourcesystemid, classificationid, eventid, productid) FROM stdin;
\.


--
-- TOC entry 5366 (class 0 OID 28639)
-- Dependencies: 271
-- Data for Name: eventxproductsecuritytoken; Type: TABLE DATA; Schema: event; Owner: postgres
--

COPY event.eventxproductsecuritytoken (eventxproductsecuritytokenid, effectivefromdate, effectivetodate, warehousecreatedtimestamp, warehouselastupdatedtimestamp, createallowed, deleteallowed, originalsourcesystemuniqueid, readallowed, updateallowed, activeflagid, enterpriseid, originalsourcesystemid, securitytokenid, systemid, eventxproductid) FROM stdin;
\.


--
-- TOC entry 5367 (class 0 OID 28646)
-- Dependencies: 272
-- Data for Name: eventxresourceitem; Type: TABLE DATA; Schema: event; Owner: postgres
--

COPY event.eventxresourceitem (eventxresourceitemid, effectivefromdate, effectivetodate, warehousecreatedtimestamp, warehouselastupdatedtimestamp, originalsourcesystemuniqueid, value, activeflagid, enterpriseid, systemid, originalsourcesystemid, classificationid, eventid, resourceitemid) FROM stdin;
\.


--
-- TOC entry 5368 (class 0 OID 28653)
-- Dependencies: 273
-- Data for Name: eventxresourceitemsecuritytoken; Type: TABLE DATA; Schema: event; Owner: postgres
--

COPY event.eventxresourceitemsecuritytoken (eventxresourceitemsecuritytokenid, effectivefromdate, effectivetodate, warehousecreatedtimestamp, warehouselastupdatedtimestamp, createallowed, deleteallowed, originalsourcesystemuniqueid, readallowed, updateallowed, activeflagid, enterpriseid, originalsourcesystemid, securitytokenid, systemid, eventxresourceitemid) FROM stdin;
\.


--
-- TOC entry 5369 (class 0 OID 28660)
-- Dependencies: 274
-- Data for Name: eventxrules; Type: TABLE DATA; Schema: event; Owner: postgres
--

COPY event.eventxrules (eventxrulesid, effectivefromdate, effectivetodate, warehousecreatedtimestamp, warehouselastupdatedtimestamp, originalsourcesystemuniqueid, value, activeflagid, enterpriseid, systemid, originalsourcesystemid, classificationid, eventid, rulesid) FROM stdin;
\.


--
-- TOC entry 5370 (class 0 OID 28667)
-- Dependencies: 275
-- Data for Name: eventxrulessecuritytoken; Type: TABLE DATA; Schema: event; Owner: postgres
--

COPY event.eventxrulessecuritytoken (eventxrulessecuritytokenid, effectivefromdate, effectivetodate, warehousecreatedtimestamp, warehouselastupdatedtimestamp, createallowed, deleteallowed, originalsourcesystemuniqueid, readallowed, updateallowed, activeflagid, enterpriseid, originalsourcesystemid, securitytokenid, systemid, eventxrulesid) FROM stdin;
\.


--
-- TOC entry 5371 (class 0 OID 28674)
-- Dependencies: 276
-- Data for Name: geography; Type: TABLE DATA; Schema: geography; Owner: postgres
--

COPY geography.geography (geographyid, effectivefromdate, effectivetodate, warehousecreatedtimestamp, warehouselastupdatedtimestamp, originalsourcesystemuniqueid, geographydesc, geographyname, activeflagid, enterpriseid, systemid, originalsourcesystemid, classificationid) FROM stdin;
\.


--
-- TOC entry 5372 (class 0 OID 28681)
-- Dependencies: 277
-- Data for Name: geographysecuritytoken; Type: TABLE DATA; Schema: geography; Owner: postgres
--

COPY geography.geographysecuritytoken (geographysecuritytokenid, effectivefromdate, effectivetodate, warehousecreatedtimestamp, warehouselastupdatedtimestamp, createallowed, deleteallowed, originalsourcesystemuniqueid, readallowed, updateallowed, activeflagid, enterpriseid, originalsourcesystemid, securitytokenid, systemid, geographyid) FROM stdin;
\.


--
-- TOC entry 5373 (class 0 OID 28688)
-- Dependencies: 278
-- Data for Name: geographyxclassification; Type: TABLE DATA; Schema: geography; Owner: postgres
--

COPY geography.geographyxclassification (geographyxclassificationid, effectivefromdate, effectivetodate, warehousecreatedtimestamp, warehouselastupdatedtimestamp, originalsourcesystemuniqueid, value, activeflagid, enterpriseid, systemid, originalsourcesystemid, classificationid, geographyid) FROM stdin;
\.


--
-- TOC entry 5374 (class 0 OID 28695)
-- Dependencies: 279
-- Data for Name: geographyxclassificationsecuritytoken; Type: TABLE DATA; Schema: geography; Owner: postgres
--

COPY geography.geographyxclassificationsecuritytoken (geographyxclassificationsecuritytokenid, effectivefromdate, effectivetodate, warehousecreatedtimestamp, warehouselastupdatedtimestamp, createallowed, deleteallowed, originalsourcesystemuniqueid, readallowed, updateallowed, activeflagid, enterpriseid, originalsourcesystemid, securitytokenid, systemid, geographyxclassificationid) FROM stdin;
\.


--
-- TOC entry 5375 (class 0 OID 28702)
-- Dependencies: 280
-- Data for Name: geographyxgeography; Type: TABLE DATA; Schema: geography; Owner: postgres
--

COPY geography.geographyxgeography (geographyxgeographyid, effectivefromdate, effectivetodate, warehousecreatedtimestamp, warehouselastupdatedtimestamp, originalsourcesystemuniqueid, value, activeflagid, enterpriseid, systemid, originalsourcesystemid, classificationid, childgeographyid, parentgeographyid) FROM stdin;
\.


--
-- TOC entry 5376 (class 0 OID 28709)
-- Dependencies: 281
-- Data for Name: geographyxgeographysecuritytoken; Type: TABLE DATA; Schema: geography; Owner: postgres
--

COPY geography.geographyxgeographysecuritytoken (geographyxgeographysecuritytokenid, effectivefromdate, effectivetodate, warehousecreatedtimestamp, warehouselastupdatedtimestamp, createallowed, deleteallowed, originalsourcesystemuniqueid, readallowed, updateallowed, activeflagid, enterpriseid, originalsourcesystemid, securitytokenid, systemid, geographyxgeographyid) FROM stdin;
\.


--
-- TOC entry 5377 (class 0 OID 28716)
-- Dependencies: 282
-- Data for Name: geographyxresourceitem; Type: TABLE DATA; Schema: geography; Owner: postgres
--

COPY geography.geographyxresourceitem (geographyxresourceitemid, effectivefromdate, effectivetodate, warehousecreatedtimestamp, warehouselastupdatedtimestamp, originalsourcesystemuniqueid, value, activeflagid, enterpriseid, systemid, originalsourcesystemid, classificationid, geographyid, resourceitemid) FROM stdin;
\.


--
-- TOC entry 5378 (class 0 OID 28723)
-- Dependencies: 283
-- Data for Name: geographyxresourceitemsecuritytoken; Type: TABLE DATA; Schema: geography; Owner: postgres
--

COPY geography.geographyxresourceitemsecuritytoken (geographyxresourceitemsecuritytokenid, effectivefromdate, effectivetodate, warehousecreatedtimestamp, warehouselastupdatedtimestamp, createallowed, deleteallowed, originalsourcesystemuniqueid, readallowed, updateallowed, activeflagid, enterpriseid, originalsourcesystemid, securitytokenid, systemid, geographyxresourceitemid) FROM stdin;
\.


--
-- TOC entry 5379 (class 0 OID 28730)
-- Dependencies: 284
-- Data for Name: involvedparty; Type: TABLE DATA; Schema: party; Owner: postgres
--

COPY party.involvedparty (involvedpartyid, effectivefromdate, effectivetodate, warehousecreatedtimestamp, warehouselastupdatedtimestamp, originalsourcesystemuniqueid, activeflagid, enterpriseid, systemid, originalsourcesystemid) FROM stdin;
\.


--
-- TOC entry 5380 (class 0 OID 28735)
-- Dependencies: 285
-- Data for Name: involvedpartyidentificationtype; Type: TABLE DATA; Schema: party; Owner: postgres
--

COPY party.involvedpartyidentificationtype (involvedpartyidentificationtypeid, effectivefromdate, effectivetodate, warehousecreatedtimestamp, warehouselastupdatedtimestamp, originalsourcesystemuniqueid, involvedpartyidentificationdesc, involvedpartyidentificationname, activeflagid, enterpriseid, systemid, originalsourcesystemid) FROM stdin;
\.


--
-- TOC entry 5381 (class 0 OID 28742)
-- Dependencies: 286
-- Data for Name: involvedpartyidentificationtypesecuritytoken; Type: TABLE DATA; Schema: party; Owner: postgres
--

COPY party.involvedpartyidentificationtypesecuritytoken (involvedpartyidentificationtypesecuritytokenid, effectivefromdate, effectivetodate, warehousecreatedtimestamp, warehouselastupdatedtimestamp, createallowed, deleteallowed, originalsourcesystemuniqueid, readallowed, updateallowed, activeflagid, enterpriseid, originalsourcesystemid, securitytokenid, systemid, involvedpartyidentificationtypeid) FROM stdin;
\.


--
-- TOC entry 5382 (class 0 OID 28749)
-- Dependencies: 287
-- Data for Name: involvedpartynametype; Type: TABLE DATA; Schema: party; Owner: postgres
--

COPY party.involvedpartynametype (involvedpartynametypeid, effectivefromdate, effectivetodate, warehousecreatedtimestamp, warehouselastupdatedtimestamp, originalsourcesystemuniqueid, involvedpartynametypedescr, involvedpartynametypename, activeflagid, enterpriseid, systemid, originalsourcesystemid) FROM stdin;
\.


--
-- TOC entry 5383 (class 0 OID 28756)
-- Dependencies: 288
-- Data for Name: involvedpartynametypesecuritytoken; Type: TABLE DATA; Schema: party; Owner: postgres
--

COPY party.involvedpartynametypesecuritytoken (involvedpartynametypesecuritytokenid, effectivefromdate, effectivetodate, warehousecreatedtimestamp, warehouselastupdatedtimestamp, createallowed, deleteallowed, originalsourcesystemuniqueid, readallowed, updateallowed, activeflagid, enterpriseid, originalsourcesystemid, securitytokenid, systemid, involvedpartynametypeid) FROM stdin;
\.


--
-- TOC entry 5384 (class 0 OID 28763)
-- Dependencies: 289
-- Data for Name: involvedpartynonorganic; Type: TABLE DATA; Schema: party; Owner: postgres
--

COPY party.involvedpartynonorganic (involvedpartynonorganicid, effectivefromdate, effectivetodate, warehousecreatedtimestamp, warehouselastupdatedtimestamp, originalsourcesystemuniqueid, activeflagid, enterpriseid, systemid, originalsourcesystemid) FROM stdin;
\.


--
-- TOC entry 5385 (class 0 OID 28768)
-- Dependencies: 290
-- Data for Name: involvedpartynonorganicsecuritytoken; Type: TABLE DATA; Schema: party; Owner: postgres
--

COPY party.involvedpartynonorganicsecuritytoken (involvedpartynonorganicsecuritytokenid, effectivefromdate, effectivetodate, warehousecreatedtimestamp, warehouselastupdatedtimestamp, createallowed, deleteallowed, originalsourcesystemuniqueid, readallowed, updateallowed, activeflagid, enterpriseid, originalsourcesystemid, securitytokenid, systemid, involvedpartynonorganicid) FROM stdin;
\.


--
-- TOC entry 5386 (class 0 OID 28775)
-- Dependencies: 291
-- Data for Name: involvedpartyorganic; Type: TABLE DATA; Schema: party; Owner: postgres
--

COPY party.involvedpartyorganic (involvedpartyorganicid, effectivefromdate, effectivetodate, warehousecreatedtimestamp, warehouselastupdatedtimestamp, originalsourcesystemuniqueid, activeflagid, enterpriseid, systemid, originalsourcesystemid) FROM stdin;
\.


--
-- TOC entry 5387 (class 0 OID 28780)
-- Dependencies: 292
-- Data for Name: involvedpartyorganicsecuritytoken; Type: TABLE DATA; Schema: party; Owner: postgres
--

COPY party.involvedpartyorganicsecuritytoken (involvedpartyorganicsecuritytokenid, effectivefromdate, effectivetodate, warehousecreatedtimestamp, warehouselastupdatedtimestamp, createallowed, deleteallowed, originalsourcesystemuniqueid, readallowed, updateallowed, activeflagid, enterpriseid, originalsourcesystemid, securitytokenid, systemid, involvedpartyorganicid) FROM stdin;
\.


--
-- TOC entry 5388 (class 0 OID 28787)
-- Dependencies: 293
-- Data for Name: involvedpartyorganictype; Type: TABLE DATA; Schema: party; Owner: postgres
--

COPY party.involvedpartyorganictype (involvedpartyorganictypeid, effectivefromdate, effectivetodate, warehousecreatedtimestamp, warehouselastupdatedtimestamp, originalsourcesystemuniqueid, involvedpartytypedesc, involvedpartytypename, activeflagid, enterpriseid, systemid, originalsourcesystemid) FROM stdin;
\.


--
-- TOC entry 5389 (class 0 OID 28794)
-- Dependencies: 294
-- Data for Name: involvedpartyorganictypesecuritytoken; Type: TABLE DATA; Schema: party; Owner: postgres
--

COPY party.involvedpartyorganictypesecuritytoken (involvedpartyorganictypesecuritytokenid, effectivefromdate, effectivetodate, warehousecreatedtimestamp, warehouselastupdatedtimestamp, createallowed, deleteallowed, originalsourcesystemuniqueid, readallowed, updateallowed, activeflagid, enterpriseid, originalsourcesystemid, securitytokenid, systemid, involvedpartyorganictypeid) FROM stdin;
\.


--
-- TOC entry 5390 (class 0 OID 28801)
-- Dependencies: 295
-- Data for Name: involvedpartysecuritytoken; Type: TABLE DATA; Schema: party; Owner: postgres
--

COPY party.involvedpartysecuritytoken (involvedpartysecuritytokenid, effectivefromdate, effectivetodate, warehousecreatedtimestamp, warehouselastupdatedtimestamp, createallowed, deleteallowed, originalsourcesystemuniqueid, readallowed, updateallowed, activeflagid, enterpriseid, originalsourcesystemid, securitytokenid, systemid, involvedpartyid) FROM stdin;
\.


--
-- TOC entry 5391 (class 0 OID 28808)
-- Dependencies: 296
-- Data for Name: involvedpartytype; Type: TABLE DATA; Schema: party; Owner: postgres
--

COPY party.involvedpartytype (involvedpartytypeid, effectivefromdate, effectivetodate, warehousecreatedtimestamp, warehouselastupdatedtimestamp, originalsourcesystemuniqueid, involvedpartytypedesc, involvedpartytypename, activeflagid, enterpriseid, systemid, originalsourcesystemid) FROM stdin;
\.


--
-- TOC entry 5392 (class 0 OID 28815)
-- Dependencies: 297
-- Data for Name: involvedpartytypesecuritytoken; Type: TABLE DATA; Schema: party; Owner: postgres
--

COPY party.involvedpartytypesecuritytoken (involvedpartytypesecuritytokenid, effectivefromdate, effectivetodate, warehousecreatedtimestamp, warehouselastupdatedtimestamp, createallowed, deleteallowed, originalsourcesystemuniqueid, readallowed, updateallowed, activeflagid, enterpriseid, originalsourcesystemid, securitytokenid, systemid, involvedpartytypeid) FROM stdin;
\.


--
-- TOC entry 5393 (class 0 OID 28822)
-- Dependencies: 298
-- Data for Name: involvedpartyxaddress; Type: TABLE DATA; Schema: party; Owner: postgres
--

COPY party.involvedpartyxaddress (involvedpartyxaddressid, effectivefromdate, effectivetodate, warehousecreatedtimestamp, warehouselastupdatedtimestamp, originalsourcesystemuniqueid, value, activeflagid, enterpriseid, systemid, originalsourcesystemid, classificationid, addressid, involvedpartyid) FROM stdin;
\.


--
-- TOC entry 5394 (class 0 OID 28829)
-- Dependencies: 299
-- Data for Name: involvedpartyxaddresssecuritytoken; Type: TABLE DATA; Schema: party; Owner: postgres
--

COPY party.involvedpartyxaddresssecuritytoken (involvedpartyxaddresssecuritytokenid, effectivefromdate, effectivetodate, warehousecreatedtimestamp, warehouselastupdatedtimestamp, createallowed, deleteallowed, originalsourcesystemuniqueid, readallowed, updateallowed, activeflagid, enterpriseid, originalsourcesystemid, securitytokenid, systemid, involvedpartyxaddressid) FROM stdin;
\.


--
-- TOC entry 5395 (class 0 OID 28836)
-- Dependencies: 300
-- Data for Name: involvedpartyxclassification; Type: TABLE DATA; Schema: party; Owner: postgres
--

COPY party.involvedpartyxclassification (involvedpartyxclassificationid, effectivefromdate, effectivetodate, warehousecreatedtimestamp, warehouselastupdatedtimestamp, originalsourcesystemuniqueid, value, activeflagid, enterpriseid, systemid, originalsourcesystemid, classificationid, involvedpartyid) FROM stdin;
\.


--
-- TOC entry 5396 (class 0 OID 28843)
-- Dependencies: 301
-- Data for Name: involvedpartyxclassificationsecuritytoken; Type: TABLE DATA; Schema: party; Owner: postgres
--

COPY party.involvedpartyxclassificationsecuritytoken (involvedpartyxclassificationsecuritytokenid, effectivefromdate, effectivetodate, warehousecreatedtimestamp, warehouselastupdatedtimestamp, createallowed, deleteallowed, originalsourcesystemuniqueid, readallowed, updateallowed, activeflagid, enterpriseid, originalsourcesystemid, securitytokenid, systemid, involvedpartyxclassificationid) FROM stdin;
\.


--
-- TOC entry 5397 (class 0 OID 28850)
-- Dependencies: 302
-- Data for Name: involvedpartyxinvolvedparty; Type: TABLE DATA; Schema: party; Owner: postgres
--

COPY party.involvedpartyxinvolvedparty (involvedpartyxinvolvedpartyid, effectivefromdate, effectivetodate, warehousecreatedtimestamp, warehouselastupdatedtimestamp, originalsourcesystemuniqueid, value, activeflagid, enterpriseid, systemid, originalsourcesystemid, classificationid, childinvolvedpartyid, parentinvolvedpartyid) FROM stdin;
\.


--
-- TOC entry 5398 (class 0 OID 28857)
-- Dependencies: 303
-- Data for Name: involvedpartyxinvolvedpartyidentificationtype; Type: TABLE DATA; Schema: party; Owner: postgres
--

COPY party.involvedpartyxinvolvedpartyidentificationtype (involvedpartyxinvolvedpartyidentificationtypeid, effectivefromdate, effectivetodate, warehousecreatedtimestamp, warehouselastupdatedtimestamp, originalsourcesystemuniqueid, value, activeflagid, enterpriseid, systemid, originalsourcesystemid, classificationid, involvedpartyid, involvedpartyidentificationtypeid) FROM stdin;
\.


--
-- TOC entry 5399 (class 0 OID 28864)
-- Dependencies: 304
-- Data for Name: involvedpartyxinvolvedpartyidentificationtypesecuritytoken; Type: TABLE DATA; Schema: party; Owner: postgres
--

COPY party.involvedpartyxinvolvedpartyidentificationtypesecuritytoken (involvedpartyxinvolvedpartyidentificationtypesecuritytokenid, effectivefromdate, effectivetodate, warehousecreatedtimestamp, warehouselastupdatedtimestamp, createallowed, deleteallowed, originalsourcesystemuniqueid, readallowed, updateallowed, activeflagid, enterpriseid, originalsourcesystemid, securitytokenid, systemid, involvedpartyxinvolvedpartyidentificationtypeid) FROM stdin;
\.


--
-- TOC entry 5400 (class 0 OID 28871)
-- Dependencies: 305
-- Data for Name: involvedpartyxinvolvedpartynametype; Type: TABLE DATA; Schema: party; Owner: postgres
--

COPY party.involvedpartyxinvolvedpartynametype (involvedpartyxinvolvedpartynametypeid, effectivefromdate, effectivetodate, warehousecreatedtimestamp, warehouselastupdatedtimestamp, originalsourcesystemuniqueid, value, activeflagid, enterpriseid, systemid, originalsourcesystemid, classificationid, involvedpartyid, involvedpartynametypeid) FROM stdin;
\.


--
-- TOC entry 5401 (class 0 OID 28878)
-- Dependencies: 306
-- Data for Name: involvedpartyxinvolvedpartynametypesecuritytoken; Type: TABLE DATA; Schema: party; Owner: postgres
--

COPY party.involvedpartyxinvolvedpartynametypesecuritytoken (involvedpartyxinvolvedpartynametypesecuritytokenid, effectivefromdate, effectivetodate, warehousecreatedtimestamp, warehouselastupdatedtimestamp, createallowed, deleteallowed, originalsourcesystemuniqueid, readallowed, updateallowed, activeflagid, enterpriseid, originalsourcesystemid, securitytokenid, systemid, involvedpartyxinvolvedpartynametypeid) FROM stdin;
\.


--
-- TOC entry 5402 (class 0 OID 28885)
-- Dependencies: 307
-- Data for Name: involvedpartyxinvolvedpartysecuritytoken; Type: TABLE DATA; Schema: party; Owner: postgres
--

COPY party.involvedpartyxinvolvedpartysecuritytoken (involvedpartyxinvolvedpartysecuritytokenid, effectivefromdate, effectivetodate, warehousecreatedtimestamp, warehouselastupdatedtimestamp, createallowed, deleteallowed, originalsourcesystemuniqueid, readallowed, updateallowed, activeflagid, enterpriseid, originalsourcesystemid, securitytokenid, systemid, involvedpartyxinvolvedpartyid) FROM stdin;
\.


--
-- TOC entry 5403 (class 0 OID 28892)
-- Dependencies: 308
-- Data for Name: involvedpartyxinvolvedpartytype; Type: TABLE DATA; Schema: party; Owner: postgres
--

COPY party.involvedpartyxinvolvedpartytype (involvedpartyxinvolvedpartytypeid, effectivefromdate, effectivetodate, warehousecreatedtimestamp, warehouselastupdatedtimestamp, originalsourcesystemuniqueid, value, activeflagid, enterpriseid, systemid, originalsourcesystemid, classificationid, involvedpartyid, involvedpartytypeid) FROM stdin;
\.


--
-- TOC entry 5404 (class 0 OID 28899)
-- Dependencies: 309
-- Data for Name: involvedpartyxinvolvedpartytypesecuritytoken; Type: TABLE DATA; Schema: party; Owner: postgres
--

COPY party.involvedpartyxinvolvedpartytypesecuritytoken (involvedpartyxinvolvedpartytypesecuritytokenid, effectivefromdate, effectivetodate, warehousecreatedtimestamp, warehouselastupdatedtimestamp, createallowed, deleteallowed, originalsourcesystemuniqueid, readallowed, updateallowed, activeflagid, enterpriseid, originalsourcesystemid, securitytokenid, systemid, involvedpartyxinvolvedpartytypeid) FROM stdin;
\.


--
-- TOC entry 5405 (class 0 OID 28906)
-- Dependencies: 310
-- Data for Name: involvedpartyxproduct; Type: TABLE DATA; Schema: party; Owner: postgres
--

COPY party.involvedpartyxproduct (involvedpartyxproductid, effectivefromdate, effectivetodate, warehousecreatedtimestamp, warehouselastupdatedtimestamp, originalsourcesystemuniqueid, value, activeflagid, enterpriseid, systemid, originalsourcesystemid, classificationid, involvedpartyid, productid) FROM stdin;
\.


--
-- TOC entry 5406 (class 0 OID 28913)
-- Dependencies: 311
-- Data for Name: involvedpartyxproductsecuritytoken; Type: TABLE DATA; Schema: party; Owner: postgres
--

COPY party.involvedpartyxproductsecuritytoken (involvedpartyxproductsecuritytokenid, effectivefromdate, effectivetodate, warehousecreatedtimestamp, warehouselastupdatedtimestamp, createallowed, deleteallowed, originalsourcesystemuniqueid, readallowed, updateallowed, activeflagid, enterpriseid, originalsourcesystemid, securitytokenid, systemid, involvedpartyxproductid) FROM stdin;
\.


--
-- TOC entry 5407 (class 0 OID 28920)
-- Dependencies: 312
-- Data for Name: involvedpartyxproducttype; Type: TABLE DATA; Schema: party; Owner: postgres
--

COPY party.involvedpartyxproducttype (involvedpartyxproducttypeid, effectivefromdate, effectivetodate, warehousecreatedtimestamp, warehouselastupdatedtimestamp, originalsourcesystemuniqueid, value, activeflagid, enterpriseid, systemid, originalsourcesystemid, classificationid, involvedpartyid, producttypeid) FROM stdin;
\.


--
-- TOC entry 5408 (class 0 OID 28927)
-- Dependencies: 313
-- Data for Name: involvedpartyxproducttypesecuritytoken; Type: TABLE DATA; Schema: party; Owner: postgres
--

COPY party.involvedpartyxproducttypesecuritytoken (involvedpartyxproducttypesecuritytokenid, effectivefromdate, effectivetodate, warehousecreatedtimestamp, warehouselastupdatedtimestamp, createallowed, deleteallowed, originalsourcesystemuniqueid, readallowed, updateallowed, activeflagid, enterpriseid, originalsourcesystemid, securitytokenid, systemid, involvedpartyxproducttypeid) FROM stdin;
\.


--
-- TOC entry 5409 (class 0 OID 28934)
-- Dependencies: 314
-- Data for Name: involvedpartyxresourceitem; Type: TABLE DATA; Schema: party; Owner: postgres
--

COPY party.involvedpartyxresourceitem (involvedpartyxresourceitemid, effectivefromdate, effectivetodate, warehousecreatedtimestamp, warehouselastupdatedtimestamp, originalsourcesystemuniqueid, value, activeflagid, enterpriseid, systemid, originalsourcesystemid, classificationid, involvedpartyid, resourceitemid) FROM stdin;
\.


--
-- TOC entry 5410 (class 0 OID 28941)
-- Dependencies: 315
-- Data for Name: involvedpartyxresourceitemsecuritytoken; Type: TABLE DATA; Schema: party; Owner: postgres
--

COPY party.involvedpartyxresourceitemsecuritytoken (involvedpartyxresourceitemsecuritytokenid, effectivefromdate, effectivetodate, warehousecreatedtimestamp, warehouselastupdatedtimestamp, createallowed, deleteallowed, originalsourcesystemuniqueid, readallowed, updateallowed, activeflagid, enterpriseid, originalsourcesystemid, securitytokenid, systemid, involvedpartyxresourceitemid) FROM stdin;
\.


--
-- TOC entry 5411 (class 0 OID 28948)
-- Dependencies: 316
-- Data for Name: involvedpartyxrules; Type: TABLE DATA; Schema: party; Owner: postgres
--

COPY party.involvedpartyxrules (involvedpartyxrulesid, effectivefromdate, effectivetodate, warehousecreatedtimestamp, warehouselastupdatedtimestamp, originalsourcesystemuniqueid, value, activeflagid, enterpriseid, systemid, originalsourcesystemid, classificationid, involvedpartyid, rulesid) FROM stdin;
\.


--
-- TOC entry 5412 (class 0 OID 28955)
-- Dependencies: 317
-- Data for Name: involvedpartyxrulessecuritytoken; Type: TABLE DATA; Schema: party; Owner: postgres
--

COPY party.involvedpartyxrulessecuritytoken (involvedpartyxrulessecuritytokenid, effectivefromdate, effectivetodate, warehousecreatedtimestamp, warehouselastupdatedtimestamp, createallowed, deleteallowed, originalsourcesystemuniqueid, readallowed, updateallowed, activeflagid, enterpriseid, originalsourcesystemid, securitytokenid, systemid, involvedpartyxrulesid) FROM stdin;
\.


--
-- TOC entry 5413 (class 0 OID 28962)
-- Dependencies: 318
-- Data for Name: product; Type: TABLE DATA; Schema: product; Owner: postgres
--

COPY product.product (productid, effectivefromdate, effectivetodate, warehousecreatedtimestamp, warehouselastupdatedtimestamp, originalsourcesystemuniqueid, productdesc, productname, productcode, activeflagid, enterpriseid, systemid, originalsourcesystemid) FROM stdin;
\.


--
-- TOC entry 5414 (class 0 OID 28969)
-- Dependencies: 319
-- Data for Name: productsecuritytoken; Type: TABLE DATA; Schema: product; Owner: postgres
--

COPY product.productsecuritytoken (productsecuritytokenid, effectivefromdate, effectivetodate, warehousecreatedtimestamp, warehouselastupdatedtimestamp, createallowed, deleteallowed, originalsourcesystemuniqueid, readallowed, updateallowed, activeflagid, enterpriseid, originalsourcesystemid, securitytokenid, systemid, productid) FROM stdin;
\.


--
-- TOC entry 5415 (class 0 OID 28976)
-- Dependencies: 320
-- Data for Name: producttype; Type: TABLE DATA; Schema: product; Owner: postgres
--

COPY product.producttype (producttypeid, effectivefromdate, effectivetodate, warehousecreatedtimestamp, warehouselastupdatedtimestamp, originalsourcesystemuniqueid, producttypedesc, producttypename, activeflagid, enterpriseid, systemid, originalsourcesystemid) FROM stdin;
\.


--
-- TOC entry 5416 (class 0 OID 28983)
-- Dependencies: 321
-- Data for Name: producttypessecuritytoken; Type: TABLE DATA; Schema: product; Owner: postgres
--

COPY product.producttypessecuritytoken (producttypessecuritytokenid, effectivefromdate, effectivetodate, warehousecreatedtimestamp, warehouselastupdatedtimestamp, createallowed, deleteallowed, originalsourcesystemuniqueid, readallowed, updateallowed, activeflagid, enterpriseid, originalsourcesystemid, securitytokenid, systemid, producttypesid) FROM stdin;
\.


--
-- TOC entry 5417 (class 0 OID 28990)
-- Dependencies: 322
-- Data for Name: producttypexclassification; Type: TABLE DATA; Schema: product; Owner: postgres
--

COPY product.producttypexclassification (producttypexclassificationid, effectivefromdate, effectivetodate, warehousecreatedtimestamp, warehouselastupdatedtimestamp, originalsourcesystemuniqueid, value, activeflagid, enterpriseid, systemid, originalsourcesystemid, classificationid, producttypeid) FROM stdin;
\.


--
-- TOC entry 5418 (class 0 OID 28997)
-- Dependencies: 323
-- Data for Name: producttypexclassificationsecuritytoken; Type: TABLE DATA; Schema: product; Owner: postgres
--

COPY product.producttypexclassificationsecuritytoken (producttypexclassificationsecuritytokenid, effectivefromdate, effectivetodate, warehousecreatedtimestamp, warehouselastupdatedtimestamp, createallowed, deleteallowed, originalsourcesystemuniqueid, readallowed, updateallowed, activeflagid, enterpriseid, originalsourcesystemid, securitytokenid, systemid, producttypexclassificationid) FROM stdin;
\.


--
-- TOC entry 5419 (class 0 OID 29004)
-- Dependencies: 324
-- Data for Name: productxclassification; Type: TABLE DATA; Schema: product; Owner: postgres
--

COPY product.productxclassification (productxclassificationid, effectivefromdate, effectivetodate, warehousecreatedtimestamp, warehouselastupdatedtimestamp, originalsourcesystemuniqueid, value, activeflagid, enterpriseid, systemid, originalsourcesystemid, classificationid, productid) FROM stdin;
\.


--
-- TOC entry 5420 (class 0 OID 29011)
-- Dependencies: 325
-- Data for Name: productxclassificationsecuritytoken; Type: TABLE DATA; Schema: product; Owner: postgres
--

COPY product.productxclassificationsecuritytoken (productxclassificationsecuritytokenid, effectivefromdate, effectivetodate, warehousecreatedtimestamp, warehouselastupdatedtimestamp, createallowed, deleteallowed, originalsourcesystemuniqueid, readallowed, updateallowed, activeflagid, enterpriseid, originalsourcesystemid, securitytokenid, systemid, productxclassificationid) FROM stdin;
\.


--
-- TOC entry 5421 (class 0 OID 29018)
-- Dependencies: 326
-- Data for Name: productxproduct; Type: TABLE DATA; Schema: product; Owner: postgres
--

COPY product.productxproduct (productxproductid, effectivefromdate, effectivetodate, warehousecreatedtimestamp, warehouselastupdatedtimestamp, originalsourcesystemuniqueid, value, activeflagid, enterpriseid, systemid, originalsourcesystemid, classificationid, childproductid, parentproductid) FROM stdin;
\.


--
-- TOC entry 5422 (class 0 OID 29025)
-- Dependencies: 327
-- Data for Name: productxproductsecuritytoken; Type: TABLE DATA; Schema: product; Owner: postgres
--

COPY product.productxproductsecuritytoken (productxproductsecuritytokenid, effectivefromdate, effectivetodate, warehousecreatedtimestamp, warehouselastupdatedtimestamp, createallowed, deleteallowed, originalsourcesystemuniqueid, readallowed, updateallowed, activeflagid, enterpriseid, originalsourcesystemid, securitytokenid, systemid, productxproductid) FROM stdin;
\.


--
-- TOC entry 5423 (class 0 OID 29032)
-- Dependencies: 328
-- Data for Name: productxproducttype; Type: TABLE DATA; Schema: product; Owner: postgres
--

COPY product.productxproducttype (productxproducttypeid, effectivefromdate, effectivetodate, warehousecreatedtimestamp, warehouselastupdatedtimestamp, originalsourcesystemuniqueid, value, activeflagid, enterpriseid, systemid, originalsourcesystemid, classificationid, productid, producttypeid) FROM stdin;
\.


--
-- TOC entry 5424 (class 0 OID 29039)
-- Dependencies: 329
-- Data for Name: productxproducttypesecuritytoken; Type: TABLE DATA; Schema: product; Owner: postgres
--

COPY product.productxproducttypesecuritytoken (productxproducttypesecuritytokenid, effectivefromdate, effectivetodate, warehousecreatedtimestamp, warehouselastupdatedtimestamp, createallowed, deleteallowed, originalsourcesystemuniqueid, readallowed, updateallowed, activeflagid, enterpriseid, originalsourcesystemid, securitytokenid, systemid, productxproducttypeid) FROM stdin;
\.


--
-- TOC entry 5425 (class 0 OID 29046)
-- Dependencies: 330
-- Data for Name: productxresourceitem; Type: TABLE DATA; Schema: product; Owner: postgres
--

COPY product.productxresourceitem (productxresourceitemid, effectivefromdate, effectivetodate, warehousecreatedtimestamp, warehouselastupdatedtimestamp, originalsourcesystemuniqueid, value, activeflagid, enterpriseid, systemid, originalsourcesystemid, classificationid, productid, resourceitemid) FROM stdin;
\.


--
-- TOC entry 5426 (class 0 OID 29053)
-- Dependencies: 331
-- Data for Name: productxresourceitemsecuritytoken; Type: TABLE DATA; Schema: product; Owner: postgres
--

COPY product.productxresourceitemsecuritytoken (productxresourceitemsecuritytokenid, effectivefromdate, effectivetodate, warehousecreatedtimestamp, warehouselastupdatedtimestamp, createallowed, deleteallowed, originalsourcesystemuniqueid, readallowed, updateallowed, activeflagid, enterpriseid, originalsourcesystemid, securitytokenid, systemid, productxresourceitemid) FROM stdin;
\.


--
-- TOC entry 5488 (class 0 OID 29453)
-- Dependencies: 393
-- Data for Name: arrangementhierarchyview; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.arrangementhierarchyview (id, name, one, parentid, path, pather) FROM stdin;
\.


--
-- TOC entry 5489 (class 0 OID 29460)
-- Dependencies: 394
-- Data for Name: classificationhierarchyview; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.classificationhierarchyview (id, name, one, parentid, path, pather) FROM stdin;
\.


--
-- TOC entry 5490 (class 0 OID 29467)
-- Dependencies: 395
-- Data for Name: geographyhierarchyview; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.geographyhierarchyview (id, name, one, parentid, path, pather) FROM stdin;
\.


--
-- TOC entry 5491 (class 0 OID 29474)
-- Dependencies: 396
-- Data for Name: producthierarchyview; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.producthierarchyview (id, name, one, parentid, path, pather) FROM stdin;
\.


--
-- TOC entry 5492 (class 0 OID 29481)
-- Dependencies: 397
-- Data for Name: quarters_months; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.quarters_months (quarters_quarterid, lumonthslist_monthid) FROM stdin;
\.


--
-- TOC entry 5493 (class 0 OID 29484)
-- Dependencies: 398
-- Data for Name: resourceitemhierarchyview; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.resourceitemhierarchyview (id, name, one, parentid, path, pather) FROM stdin;
\.


--
-- TOC entry 5494 (class 0 OID 29491)
-- Dependencies: 399
-- Data for Name: ruleshierarchyview; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.ruleshierarchyview (id, name, one, parentid, path, pather) FROM stdin;
\.


--
-- TOC entry 5495 (class 0 OID 29498)
-- Dependencies: 400
-- Data for Name: securityhierarchyparents; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.securityhierarchyparents (id, value) FROM stdin;
\.


--
-- TOC entry 5427 (class 0 OID 29060)
-- Dependencies: 332
-- Data for Name: resourceitem; Type: TABLE DATA; Schema: resource; Owner: postgres
--

COPY resource.resourceitem (resourceitemid, effectivefromdate, effectivetodate, warehousecreatedtimestamp, warehouselastupdatedtimestamp, originalsourcesystemuniqueid, resourceitemdatatype, activeflagid, enterpriseid, systemid, originalsourcesystemid) FROM stdin;
\.


--
-- TOC entry 5428 (class 0 OID 29067)
-- Dependencies: 333
-- Data for Name: resourceitemdata; Type: TABLE DATA; Schema: resource; Owner: postgres
--

COPY resource.resourceitemdata (resourceitemdataid, effectivefromdate, effectivetodate, warehousecreatedtimestamp, warehouselastupdatedtimestamp, originalsourcesystemuniqueid, resourceitemdata, activeflagid, enterpriseid, systemid, originalsourcesystemid, resourceitemid) FROM stdin;
\.


--
-- TOC entry 5429 (class 0 OID 29072)
-- Dependencies: 334
-- Data for Name: resourceitemdatasecuritytoken; Type: TABLE DATA; Schema: resource; Owner: postgres
--

COPY resource.resourceitemdatasecuritytoken (resourceitemdatasecuritytokenid, effectivefromdate, effectivetodate, warehousecreatedtimestamp, warehouselastupdatedtimestamp, createallowed, deleteallowed, originalsourcesystemuniqueid, readallowed, updateallowed, activeflagid, enterpriseid, originalsourcesystemid, securitytokenid, systemid, resourceitemdataid) FROM stdin;
\.


--
-- TOC entry 5430 (class 0 OID 29079)
-- Dependencies: 335
-- Data for Name: resourceitemdataxclassification; Type: TABLE DATA; Schema: resource; Owner: postgres
--

COPY resource.resourceitemdataxclassification (resourceitemdataxclassificationid, effectivefromdate, effectivetodate, warehousecreatedtimestamp, warehouselastupdatedtimestamp, originalsourcesystemuniqueid, value, activeflagid, enterpriseid, systemid, originalsourcesystemid, classificationid, resourceitemdataid) FROM stdin;
\.


--
-- TOC entry 5431 (class 0 OID 29086)
-- Dependencies: 336
-- Data for Name: resourceitemdataxclassificationsecuritytoken; Type: TABLE DATA; Schema: resource; Owner: postgres
--

COPY resource.resourceitemdataxclassificationsecuritytoken (resourceitemdataxclassificationsecuritytokenid, effectivefromdate, effectivetodate, warehousecreatedtimestamp, warehouselastupdatedtimestamp, createallowed, deleteallowed, originalsourcesystemuniqueid, readallowed, updateallowed, activeflagid, enterpriseid, originalsourcesystemid, securitytokenid, systemid, resourceitemdataxclassificationid) FROM stdin;
\.


--
-- TOC entry 5432 (class 0 OID 29093)
-- Dependencies: 337
-- Data for Name: resourceitemsecuritytoken; Type: TABLE DATA; Schema: resource; Owner: postgres
--

COPY resource.resourceitemsecuritytoken (resourceitemsecuritytokenid, effectivefromdate, effectivetodate, warehousecreatedtimestamp, warehouselastupdatedtimestamp, createallowed, deleteallowed, originalsourcesystemuniqueid, readallowed, updateallowed, activeflagid, enterpriseid, originalsourcesystemid, securitytokenid, systemid, resourceitemid) FROM stdin;
\.


--
-- TOC entry 5433 (class 0 OID 29100)
-- Dependencies: 338
-- Data for Name: resourceitemtype; Type: TABLE DATA; Schema: resource; Owner: postgres
--

COPY resource.resourceitemtype (resourceitemtypeid, effectivefromdate, effectivetodate, warehousecreatedtimestamp, warehouselastupdatedtimestamp, originalsourcesystemuniqueid, resourceitemtypedesc, resourceitemtypename, activeflagid, enterpriseid, systemid, originalsourcesystemid) FROM stdin;
\.


--
-- TOC entry 5434 (class 0 OID 29107)
-- Dependencies: 339
-- Data for Name: resourceitemtypesecuritytoken; Type: TABLE DATA; Schema: resource; Owner: postgres
--

COPY resource.resourceitemtypesecuritytoken (resourceitemtypesecuritytokenid, effectivefromdate, effectivetodate, warehousecreatedtimestamp, warehouselastupdatedtimestamp, createallowed, deleteallowed, originalsourcesystemuniqueid, readallowed, updateallowed, activeflagid, enterpriseid, originalsourcesystemid, securitytokenid, systemid, resourceitemtypeid) FROM stdin;
\.


--
-- TOC entry 5435 (class 0 OID 29114)
-- Dependencies: 340
-- Data for Name: resourceitemxclassification; Type: TABLE DATA; Schema: resource; Owner: postgres
--

COPY resource.resourceitemxclassification (resourceitemxclassificationid, effectivefromdate, effectivetodate, warehousecreatedtimestamp, warehouselastupdatedtimestamp, originalsourcesystemuniqueid, value, activeflagid, enterpriseid, systemid, originalsourcesystemid, classificationid, resourceitemid) FROM stdin;
\.


--
-- TOC entry 5436 (class 0 OID 29121)
-- Dependencies: 341
-- Data for Name: resourceitemxclassificationsecuritytoken; Type: TABLE DATA; Schema: resource; Owner: postgres
--

COPY resource.resourceitemxclassificationsecuritytoken (resourceitemxclassificationsecuritytokenid, effectivefromdate, effectivetodate, warehousecreatedtimestamp, warehouselastupdatedtimestamp, createallowed, deleteallowed, originalsourcesystemuniqueid, readallowed, updateallowed, activeflagid, enterpriseid, originalsourcesystemid, securitytokenid, systemid, resourceitemxclassificationid) FROM stdin;
\.


--
-- TOC entry 5437 (class 0 OID 29128)
-- Dependencies: 342
-- Data for Name: resourceitemxresourceitem; Type: TABLE DATA; Schema: resource; Owner: postgres
--

COPY resource.resourceitemxresourceitem (resourceitemxresourceitemid, effectivefromdate, effectivetodate, warehousecreatedtimestamp, warehouselastupdatedtimestamp, originalsourcesystemuniqueid, value, activeflagid, enterpriseid, systemid, originalsourcesystemid, classificationid, childresourceitemid, parentresourceitemid) FROM stdin;
\.


--
-- TOC entry 5438 (class 0 OID 29135)
-- Dependencies: 343
-- Data for Name: resourceitemxresourceitemsecuritytoken; Type: TABLE DATA; Schema: resource; Owner: postgres
--

COPY resource.resourceitemxresourceitemsecuritytoken (resourceitemxresourceitemsecuritytokenid, effectivefromdate, effectivetodate, warehousecreatedtimestamp, warehouselastupdatedtimestamp, createallowed, deleteallowed, originalsourcesystemuniqueid, readallowed, updateallowed, activeflagid, enterpriseid, originalsourcesystemid, securitytokenid, systemid, resourceitemxresourceitemid) FROM stdin;
\.


--
-- TOC entry 5439 (class 0 OID 29142)
-- Dependencies: 344
-- Data for Name: resourceitemxresourceitemtype; Type: TABLE DATA; Schema: resource; Owner: postgres
--

COPY resource.resourceitemxresourceitemtype (resourceitemxresourceitemtypeid, effectivefromdate, effectivetodate, warehousecreatedtimestamp, warehouselastupdatedtimestamp, originalsourcesystemuniqueid, value, activeflagid, enterpriseid, systemid, originalsourcesystemid, classificationid, resourceitemid, resourceitemtypeid) FROM stdin;
\.


--
-- TOC entry 5440 (class 0 OID 29149)
-- Dependencies: 345
-- Data for Name: resourceitemxresourceitemtypesecuritytoken; Type: TABLE DATA; Schema: resource; Owner: postgres
--

COPY resource.resourceitemxresourceitemtypesecuritytoken (resourceitemxresourceitemtypesecuritytokenid, effectivefromdate, effectivetodate, warehousecreatedtimestamp, warehouselastupdatedtimestamp, createallowed, deleteallowed, originalsourcesystemuniqueid, readallowed, updateallowed, activeflagid, enterpriseid, originalsourcesystemid, securitytokenid, systemid, resourceitemxresourceitemtypeid) FROM stdin;
\.


--
-- TOC entry 5441 (class 0 OID 29156)
-- Dependencies: 346
-- Data for Name: rules; Type: TABLE DATA; Schema: rules; Owner: postgres
--

COPY rules.rules (rulesid, effectivefromdate, effectivetodate, warehousecreatedtimestamp, warehouselastupdatedtimestamp, originalsourcesystemuniqueid, rulesetdescription, rulesetname, activeflagid, enterpriseid, systemid, originalsourcesystemid) FROM stdin;
\.


--
-- TOC entry 5442 (class 0 OID 29163)
-- Dependencies: 347
-- Data for Name: rulessecuritytoken; Type: TABLE DATA; Schema: rules; Owner: postgres
--

COPY rules.rulessecuritytoken (rulessecuritytokenid, effectivefromdate, effectivetodate, warehousecreatedtimestamp, warehouselastupdatedtimestamp, createallowed, deleteallowed, originalsourcesystemuniqueid, readallowed, updateallowed, activeflagid, enterpriseid, originalsourcesystemid, securitytokenid, systemid, rulesid) FROM stdin;
\.


--
-- TOC entry 5443 (class 0 OID 29170)
-- Dependencies: 348
-- Data for Name: rulestype; Type: TABLE DATA; Schema: rules; Owner: postgres
--

COPY rules.rulestype (rulestypeid, effectivefromdate, effectivetodate, warehousecreatedtimestamp, warehouselastupdatedtimestamp, originalsourcesystemuniqueid, rulestypedesc, rulestypename, activeflagid, enterpriseid, systemid, originalsourcesystemid) FROM stdin;
\.


--
-- TOC entry 5444 (class 0 OID 29177)
-- Dependencies: 349
-- Data for Name: rulestypessecuritytoken; Type: TABLE DATA; Schema: rules; Owner: postgres
--

COPY rules.rulestypessecuritytoken (rulestypessecuritytokenid, effectivefromdate, effectivetodate, warehousecreatedtimestamp, warehouselastupdatedtimestamp, createallowed, deleteallowed, originalsourcesystemuniqueid, readallowed, updateallowed, activeflagid, enterpriseid, originalsourcesystemid, securitytokenid, systemid, rulestypesid) FROM stdin;
\.


--
-- TOC entry 5445 (class 0 OID 29184)
-- Dependencies: 350
-- Data for Name: rulestypexclassification; Type: TABLE DATA; Schema: rules; Owner: postgres
--

COPY rules.rulestypexclassification (rulestypexclassificationid, effectivefromdate, effectivetodate, warehousecreatedtimestamp, warehouselastupdatedtimestamp, originalsourcesystemuniqueid, value, activeflagid, enterpriseid, systemid, originalsourcesystemid, classificationid, rulestypeid) FROM stdin;
\.


--
-- TOC entry 5446 (class 0 OID 29191)
-- Dependencies: 351
-- Data for Name: rulestypexclassificationsecuritytoken; Type: TABLE DATA; Schema: rules; Owner: postgres
--

COPY rules.rulestypexclassificationsecuritytoken (rulestypexclassificationsecuritytokenid, effectivefromdate, effectivetodate, warehousecreatedtimestamp, warehouselastupdatedtimestamp, createallowed, deleteallowed, originalsourcesystemuniqueid, readallowed, updateallowed, activeflagid, enterpriseid, originalsourcesystemid, securitytokenid, systemid, rulestypexclassificationid) FROM stdin;
\.


--
-- TOC entry 5447 (class 0 OID 29198)
-- Dependencies: 352
-- Data for Name: rulestypexresourceitem; Type: TABLE DATA; Schema: rules; Owner: postgres
--

COPY rules.rulestypexresourceitem (rulestypexresourceitemid, effectivefromdate, effectivetodate, warehousecreatedtimestamp, warehouselastupdatedtimestamp, originalsourcesystemuniqueid, value, activeflagid, enterpriseid, systemid, originalsourcesystemid, classificationid, resourceitemid, rulestypeid) FROM stdin;
\.


--
-- TOC entry 5448 (class 0 OID 29205)
-- Dependencies: 353
-- Data for Name: rulestypexresourceitemsecuritytoken; Type: TABLE DATA; Schema: rules; Owner: postgres
--

COPY rules.rulestypexresourceitemsecuritytoken (rulestypexresourceitemsecuritytokenid, effectivefromdate, effectivetodate, warehousecreatedtimestamp, warehouselastupdatedtimestamp, createallowed, deleteallowed, originalsourcesystemuniqueid, readallowed, updateallowed, activeflagid, enterpriseid, originalsourcesystemid, securitytokenid, systemid, rulestypexresourceitemid) FROM stdin;
\.


--
-- TOC entry 5449 (class 0 OID 29212)
-- Dependencies: 354
-- Data for Name: rulesxarrangement; Type: TABLE DATA; Schema: rules; Owner: postgres
--

COPY rules.rulesxarrangement (rulesxarrangementsid, effectivefromdate, effectivetodate, warehousecreatedtimestamp, warehouselastupdatedtimestamp, originalsourcesystemuniqueid, value, activeflagid, enterpriseid, systemid, originalsourcesystemid, classificationid, arrangementid, rulesid) FROM stdin;
\.


--
-- TOC entry 5450 (class 0 OID 29219)
-- Dependencies: 355
-- Data for Name: rulesxarrangementssecuritytoken; Type: TABLE DATA; Schema: rules; Owner: postgres
--

COPY rules.rulesxarrangementssecuritytoken (rulesxarrangementssecuritytokenid, effectivefromdate, effectivetodate, warehousecreatedtimestamp, warehouselastupdatedtimestamp, createallowed, deleteallowed, originalsourcesystemuniqueid, readallowed, updateallowed, activeflagid, enterpriseid, originalsourcesystemid, securitytokenid, systemid, rulesxarrangementsid) FROM stdin;
\.


--
-- TOC entry 5451 (class 0 OID 29226)
-- Dependencies: 356
-- Data for Name: rulesxclassification; Type: TABLE DATA; Schema: rules; Owner: postgres
--

COPY rules.rulesxclassification (rulesxclassificationid, effectivefromdate, effectivetodate, warehousecreatedtimestamp, warehouselastupdatedtimestamp, originalsourcesystemuniqueid, value, activeflagid, enterpriseid, systemid, originalsourcesystemid, classificationid, rulesid) FROM stdin;
\.


--
-- TOC entry 5452 (class 0 OID 29233)
-- Dependencies: 357
-- Data for Name: rulesxclassificationsecuritytoken; Type: TABLE DATA; Schema: rules; Owner: postgres
--

COPY rules.rulesxclassificationsecuritytoken (rulesxclassificationsecuritytokenid, effectivefromdate, effectivetodate, warehousecreatedtimestamp, warehouselastupdatedtimestamp, createallowed, deleteallowed, originalsourcesystemuniqueid, readallowed, updateallowed, activeflagid, enterpriseid, originalsourcesystemid, securitytokenid, systemid, rulesxclassificationid) FROM stdin;
\.


--
-- TOC entry 5453 (class 0 OID 29240)
-- Dependencies: 358
-- Data for Name: rulesxinvolvedparty; Type: TABLE DATA; Schema: rules; Owner: postgres
--

COPY rules.rulesxinvolvedparty (rulesxinvolvedpartyid, effectivefromdate, effectivetodate, warehousecreatedtimestamp, warehouselastupdatedtimestamp, originalsourcesystemuniqueid, value, activeflagid, enterpriseid, systemid, originalsourcesystemid, classificationid, involvedpartyid, rulesid) FROM stdin;
\.


--
-- TOC entry 5454 (class 0 OID 29247)
-- Dependencies: 359
-- Data for Name: rulesxinvolvedpartysecuritytoken; Type: TABLE DATA; Schema: rules; Owner: postgres
--

COPY rules.rulesxinvolvedpartysecuritytoken (rulesxinvolvedpartysecuritytokenid, effectivefromdate, effectivetodate, warehousecreatedtimestamp, warehouselastupdatedtimestamp, createallowed, deleteallowed, originalsourcesystemuniqueid, readallowed, updateallowed, activeflagid, enterpriseid, originalsourcesystemid, securitytokenid, systemid, rulesxinvolvedpartyid) FROM stdin;
\.


--
-- TOC entry 5455 (class 0 OID 29254)
-- Dependencies: 360
-- Data for Name: rulesxproduct; Type: TABLE DATA; Schema: rules; Owner: postgres
--

COPY rules.rulesxproduct (rulesxproductid, effectivefromdate, effectivetodate, warehousecreatedtimestamp, warehouselastupdatedtimestamp, originalsourcesystemuniqueid, value, activeflagid, enterpriseid, systemid, originalsourcesystemid, classificationid, productid, rulesid) FROM stdin;
\.


--
-- TOC entry 5456 (class 0 OID 29261)
-- Dependencies: 361
-- Data for Name: rulesxproductsecuritytoken; Type: TABLE DATA; Schema: rules; Owner: postgres
--

COPY rules.rulesxproductsecuritytoken (rulesxproductsecuritytokenid, effectivefromdate, effectivetodate, warehousecreatedtimestamp, warehouselastupdatedtimestamp, createallowed, deleteallowed, originalsourcesystemuniqueid, readallowed, updateallowed, activeflagid, enterpriseid, originalsourcesystemid, securitytokenid, systemid, rulesxproductid) FROM stdin;
\.


--
-- TOC entry 5457 (class 0 OID 29268)
-- Dependencies: 362
-- Data for Name: rulesxresourceitem; Type: TABLE DATA; Schema: rules; Owner: postgres
--

COPY rules.rulesxresourceitem (rulesxresourceitemid, effectivefromdate, effectivetodate, warehousecreatedtimestamp, warehouselastupdatedtimestamp, originalsourcesystemuniqueid, value, activeflagid, enterpriseid, systemid, originalsourcesystemid, classificationid, resourceitemid, rulesid) FROM stdin;
\.


--
-- TOC entry 5458 (class 0 OID 29275)
-- Dependencies: 363
-- Data for Name: rulesxresourceitemsecuritytoken; Type: TABLE DATA; Schema: rules; Owner: postgres
--

COPY rules.rulesxresourceitemsecuritytoken (rulesxresourceitemsecuritytokenid, effectivefromdate, effectivetodate, warehousecreatedtimestamp, warehouselastupdatedtimestamp, createallowed, deleteallowed, originalsourcesystemuniqueid, readallowed, updateallowed, activeflagid, enterpriseid, originalsourcesystemid, securitytokenid, systemid, rulesxresourceitemid) FROM stdin;
\.


--
-- TOC entry 5459 (class 0 OID 29282)
-- Dependencies: 364
-- Data for Name: rulesxrules; Type: TABLE DATA; Schema: rules; Owner: postgres
--

COPY rules.rulesxrules (rulesxrulesid, effectivefromdate, effectivetodate, warehousecreatedtimestamp, warehouselastupdatedtimestamp, originalsourcesystemuniqueid, value, activeflagid, enterpriseid, systemid, originalsourcesystemid, classificationid, childrulesid, parentrulesid) FROM stdin;
\.


--
-- TOC entry 5460 (class 0 OID 29289)
-- Dependencies: 365
-- Data for Name: rulesxrulessecuritytoken; Type: TABLE DATA; Schema: rules; Owner: postgres
--

COPY rules.rulesxrulessecuritytoken (rulesxrulessecuritytokenid, effectivefromdate, effectivetodate, warehousecreatedtimestamp, warehouselastupdatedtimestamp, createallowed, deleteallowed, originalsourcesystemuniqueid, readallowed, updateallowed, activeflagid, enterpriseid, originalsourcesystemid, securitytokenid, systemid, rulesxrulesid) FROM stdin;
\.


--
-- TOC entry 5461 (class 0 OID 29296)
-- Dependencies: 366
-- Data for Name: rulesxrulestype; Type: TABLE DATA; Schema: rules; Owner: postgres
--

COPY rules.rulesxrulestype (rulesxrulestypeid, effectivefromdate, effectivetodate, warehousecreatedtimestamp, warehouselastupdatedtimestamp, originalsourcesystemuniqueid, value, activeflagid, enterpriseid, systemid, originalsourcesystemid, classificationid, rulesid, rulestypeid) FROM stdin;
\.


--
-- TOC entry 5462 (class 0 OID 29303)
-- Dependencies: 367
-- Data for Name: rulesxrulestypesecuritytoken; Type: TABLE DATA; Schema: rules; Owner: postgres
--

COPY rules.rulesxrulestypesecuritytoken (rulesxrulestypesecuritytokenid, effectivefromdate, effectivetodate, warehousecreatedtimestamp, warehouselastupdatedtimestamp, createallowed, deleteallowed, originalsourcesystemuniqueid, readallowed, updateallowed, activeflagid, enterpriseid, originalsourcesystemid, securitytokenid, systemid, rulesxrulestypeid) FROM stdin;
\.


--
-- TOC entry 5463 (class 0 OID 29310)
-- Dependencies: 368
-- Data for Name: securityhierarchy; Type: TABLE DATA; Schema: security; Owner: postgres
--

COPY security.securityhierarchy (id, name, one, parentid, path, pather) FROM stdin;
\.


--
-- TOC entry 5464 (class 0 OID 29317)
-- Dependencies: 369
-- Data for Name: securitytoken; Type: TABLE DATA; Schema: security; Owner: postgres
--

COPY security.securitytoken (securitytokenid, effectivefromdate, effectivetodate, warehousecreatedtimestamp, warehouselastupdatedtimestamp, originalsourcesystemuniqueid, securitytokenfriendlydescription, securitytokenfriendlyname, securitytoken, activeflagid, enterpriseid, systemid, originalsourcesystemid, securitytokenclassificationid) FROM stdin;
\.


--
-- TOC entry 5465 (class 0 OID 29324)
-- Dependencies: 370
-- Data for Name: securitytokenssecuritytoken; Type: TABLE DATA; Schema: security; Owner: postgres
--

COPY security.securitytokenssecuritytoken (securitytokenaccessid, effectivefromdate, effectivetodate, warehousecreatedtimestamp, warehouselastupdatedtimestamp, createallowed, deleteallowed, originalsourcesystemuniqueid, readallowed, updateallowed, activeflagid, enterpriseid, originalsourcesystemid, securitytokenid, systemid, securitytokentoid) FROM stdin;
\.


--
-- TOC entry 5466 (class 0 OID 29331)
-- Dependencies: 371
-- Data for Name: securitytokensxsecuritytokensecuritytoken; Type: TABLE DATA; Schema: security; Owner: postgres
--

COPY security.securitytokensxsecuritytokensecuritytoken (securitytokenxsecuritytokensecuritytokenid, effectivefromdate, effectivetodate, warehousecreatedtimestamp, warehouselastupdatedtimestamp, createallowed, deleteallowed, originalsourcesystemuniqueid, readallowed, updateallowed, activeflagid, enterpriseid, originalsourcesystemid, securitytokenid, systemid, securitytokenxsecuritytokenid) FROM stdin;
\.


--
-- TOC entry 5467 (class 0 OID 29338)
-- Dependencies: 372
-- Data for Name: securitytokenxclassification; Type: TABLE DATA; Schema: security; Owner: postgres
--

COPY security.securitytokenxclassification (securitytokenxclassificationid, effectivefromdate, effectivetodate, warehousecreatedtimestamp, warehouselastupdatedtimestamp, originalsourcesystemuniqueid, value, activeflagid, enterpriseid, systemid, originalsourcesystemid, classificationid, securitytokenid) FROM stdin;
\.


--
-- TOC entry 5468 (class 0 OID 29345)
-- Dependencies: 373
-- Data for Name: securitytokenxclassificationsecuritytoken; Type: TABLE DATA; Schema: security; Owner: postgres
--

COPY security.securitytokenxclassificationsecuritytoken (securitytokenxclassificationsecuritytokenid, effectivefromdate, effectivetodate, warehousecreatedtimestamp, warehouselastupdatedtimestamp, createallowed, deleteallowed, originalsourcesystemuniqueid, readallowed, updateallowed, activeflagid, enterpriseid, originalsourcesystemid, securitytokenid, systemid, securitytokenxclassificationid) FROM stdin;
\.


--
-- TOC entry 5469 (class 0 OID 29352)
-- Dependencies: 374
-- Data for Name: securitytokenxsecuritytoken; Type: TABLE DATA; Schema: security; Owner: postgres
--

COPY security.securitytokenxsecuritytoken (securitytokenxsecuritytokenid, effectivefromdate, effectivetodate, warehousecreatedtimestamp, warehouselastupdatedtimestamp, originalsourcesystemuniqueid, value, activeflagid, enterpriseid, systemid, originalsourcesystemid, classificationid, childsecuritytokenid, parentsecuritytokenid) FROM stdin;
\.


--
-- TOC entry 5470 (class 0 OID 29359)
-- Dependencies: 375
-- Data for Name: daynames; Type: TABLE DATA; Schema: time; Owner: postgres
--

COPY "time".daynames (daynameid, dayabbreviation, daybusinessdayclassification, dayisbusinessday, daylongabbreviation, dayname, dayshortname, daysortorder) FROM stdin;
1	S	Weekend	0	Su	Sunday	Sun	0
2	M	Weekday	1	Mo	Monday	Mon	1
3	T	Weekday	1	Tu	Tuesday	Tue	2
4	W	Weekday	1	We	Wednesday	Wed	3
5	T	Weekday	1	Th	Thursday	Thur	4
6	F	Weekday	1	Fr	Friday	Fri	5
7	S	Weekend	0	Sa	Saturday	Sat	6
\.


--
-- TOC entry 5471 (class 0 OID 29364)
-- Dependencies: 376
-- Data for Name: dayparts; Type: TABLE DATA; Schema: time; Owner: postgres
--

COPY "time".dayparts (daypartid, daypartdescription, daypartname, daypartsortorder) FROM stdin;
2	Between 3.30am and 6.30 am	Early Morning	2
3	Between 6.30am and 9am	Morning	3
4	Between 9am and 10.30am	Late Morning	4
5	Between 10.30 and 12pm	Early Afternoon	5
6	Between 12pm and 2pm	Afternoon	6
7	Between 2pm and 3.30pm	Late Afternoon	7
8	Between 3.30pm and 4.30pm	Early Evening	8
9	Between 4.30pm and 7pm	Evening	9
10	Between 7pm and 9.30pm	Late Evening	10
11	Between 9.30pm and 12am	Midnight Evening	11
12	Between 12am and 3.30am	Midnight Morning	1
\.


--
-- TOC entry 5472 (class 0 OID 29369)
-- Dependencies: 377
-- Data for Name: days; Type: TABLE DATA; Schema: time; Owner: postgres
--

COPY "time".days (dayid, dayddmmyyyydescription, dayddmmyyyyhyphendescription, dayddmmyyyyslashdescription, daydate, daydatetime, dayfulldescription, dayinmonth, dayinyear, dayispublicholiday, daylongdescription, daymmqqdescription, daymonthdescription, dayyyyymmdescription, lastdayid, lastmonthid, lastquarterid, lastyearid, quarterid, yearid, daynameid, monthid, weekid) FROM stdin;
\.


--
-- TOC entry 5473 (class 0 OID 29374)
-- Dependencies: 378
-- Data for Name: halfhourdayparts; Type: TABLE DATA; Schema: time; Owner: postgres
--

COPY "time".halfhourdayparts (halfhourdaypartid, hourid, minuteid, daypartid) FROM stdin;
\.


--
-- TOC entry 5474 (class 0 OID 29379)
-- Dependencies: 379
-- Data for Name: halfhours; Type: TABLE DATA; Schema: time; Owner: postgres
--

COPY "time".halfhours (hourid, minuteid, ampmdesc, previoushalfhourminuteid, previoushourid, twelvehourclockdesc, twentyfourhourclockdesc) FROM stdin;
\.


--
-- TOC entry 5475 (class 0 OID 29384)
-- Dependencies: 380
-- Data for Name: hours; Type: TABLE DATA; Schema: time; Owner: postgres
--

COPY "time".hours (hourid, ampmdesc, previoushourid, twelvehourclockdesc, twentyfourhourclockdesc) FROM stdin;
\.


--
-- TOC entry 5476 (class 0 OID 29389)
-- Dependencies: 381
-- Data for Name: monthofyear; Type: TABLE DATA; Schema: time; Owner: postgres
--

COPY "time".monthofyear (monthofyearid, monthofyearabbreviation, monthofyearname, monthofyearshortname, monthinyearnumber) FROM stdin;
1	J	January	Jan	0
2	F	February	Feb	1
3	M	March	Mar	2
4	A	April	Apr	3
5	M	May	May	4
6	J	June	Jun	5
7	J	July	Jul	6
8	A	August	Aug	7
9	S	September	Sep	8
10	O	October	Oct	9
11	N	November	Nov	10
12	D	December	Dec	11
\.


--
-- TOC entry 5477 (class 0 OID 29394)
-- Dependencies: 382
-- Data for Name: months; Type: TABLE DATA; Schema: time; Owner: postgres
--

COPY "time".months (monthid, lastmonthid, lastquarterid, lastyearid, monthdayduration, monthdescription, monthmmmyydescription, monthmmyyyydescription, monthnameyyyydescription, monthshortdescription, monthyydescription, yearid, monthofyearid, quarterid) FROM stdin;
\.


--
-- TOC entry 5478 (class 0 OID 29399)
-- Dependencies: 383
-- Data for Name: publicholidays; Type: TABLE DATA; Schema: time; Owner: postgres
--

COPY "time".publicholidays (publicholidayid, dayid, publicholidayname, publicholidaytype) FROM stdin;
\.


--
-- TOC entry 5479 (class 0 OID 29406)
-- Dependencies: 384
-- Data for Name: quarters; Type: TABLE DATA; Schema: time; Owner: postgres
--

COPY "time".quarters (quarterid, lastquarterid, lastyearid, quarterdescription, quartergraphdescription, quartergriddescription, quarterinyear, quarterqqmmdescription, quartersmalldescription, quarteryymmdescription, quarteryeardescription, yearid) FROM stdin;
\.


--
-- TOC entry 5480 (class 0 OID 29411)
-- Dependencies: 385
-- Data for Name: time; Type: TABLE DATA; Schema: time; Owner: postgres
--

COPY "time"."time" (hourid, minuteid, ampmdesc, previoushourid, previousminuteid, twelvehourclockdesc, twentyfourhourclockdesc) FROM stdin;
\.


--
-- TOC entry 5481 (class 0 OID 29416)
-- Dependencies: 386
-- Data for Name: trans_fiscal; Type: TABLE DATA; Schema: time; Owner: postgres
--

COPY "time".trans_fiscal (dayid, fiscaldayid) FROM stdin;
\.


--
-- TOC entry 5482 (class 0 OID 29421)
-- Dependencies: 387
-- Data for Name: trans_mtd; Type: TABLE DATA; Schema: time; Owner: postgres
--

COPY "time".trans_mtd (dayid, mtddayid) FROM stdin;
\.


--
-- TOC entry 5483 (class 0 OID 29426)
-- Dependencies: 388
-- Data for Name: trans_qtd; Type: TABLE DATA; Schema: time; Owner: postgres
--

COPY "time".trans_qtd (dayid, qtddayid) FROM stdin;
\.


--
-- TOC entry 5484 (class 0 OID 29431)
-- Dependencies: 389
-- Data for Name: trans_qtm; Type: TABLE DATA; Schema: time; Owner: postgres
--

COPY "time".trans_qtm (monthid, qtm_monthid) FROM stdin;
\.


--
-- TOC entry 5485 (class 0 OID 29436)
-- Dependencies: 390
-- Data for Name: trans_ytd; Type: TABLE DATA; Schema: time; Owner: postgres
--

COPY "time".trans_ytd (dayid, ytddayid) FROM stdin;
\.


--
-- TOC entry 5486 (class 0 OID 29441)
-- Dependencies: 391
-- Data for Name: weeks; Type: TABLE DATA; Schema: time; Owner: postgres
--

COPY "time".weeks (weekid, monthid, quarterid, weekdescription, weekofmonth, weekofyear, weekshortdescription, yearid) FROM stdin;
\.


--
-- TOC entry 5487 (class 0 OID 29446)
-- Dependencies: 392
-- Data for Name: years; Type: TABLE DATA; Schema: time; Owner: postgres
--

COPY "time".years (yearid, century, lastyearid, leapyearflag, yyname, yyyname, yearfullname, yearname) FROM stdin;
\.


--
-- TOC entry 3895 (class 2606 OID 28296)
-- Name: address address_pkey; Type: CONSTRAINT; Schema: address; Owner: postgres
--

ALTER TABLE ONLY address.address
    ADD CONSTRAINT address_pkey PRIMARY KEY (addressid);


--
-- TOC entry 3897 (class 2606 OID 28303)
-- Name: addresssecuritytoken addresssecuritytoken_pkey; Type: CONSTRAINT; Schema: address; Owner: postgres
--

ALTER TABLE ONLY address.addresssecuritytoken
    ADD CONSTRAINT addresssecuritytoken_pkey PRIMARY KEY (addresssecuritytokenid);


--
-- TOC entry 3899 (class 2606 OID 28310)
-- Name: addressxclassification addressxclassification_pkey; Type: CONSTRAINT; Schema: address; Owner: postgres
--

ALTER TABLE ONLY address.addressxclassification
    ADD CONSTRAINT addressxclassification_pkey PRIMARY KEY (addressxclassificationid);


--
-- TOC entry 3901 (class 2606 OID 28317)
-- Name: addressxclassificationsecuritytoken addressxclassificationsecuritytoken_pkey; Type: CONSTRAINT; Schema: address; Owner: postgres
--

ALTER TABLE ONLY address.addressxclassificationsecuritytoken
    ADD CONSTRAINT addressxclassificationsecuritytoken_pkey PRIMARY KEY (addressxclassificationsecuritytokenid);


--
-- TOC entry 3903 (class 2606 OID 28324)
-- Name: addressxgeography addressxgeography_pkey; Type: CONSTRAINT; Schema: address; Owner: postgres
--

ALTER TABLE ONLY address.addressxgeography
    ADD CONSTRAINT addressxgeography_pkey PRIMARY KEY (addressxgeographyid);


--
-- TOC entry 3905 (class 2606 OID 28331)
-- Name: addressxgeographysecuritytoken addressxgeographysecuritytoken_pkey; Type: CONSTRAINT; Schema: address; Owner: postgres
--

ALTER TABLE ONLY address.addressxgeographysecuritytoken
    ADD CONSTRAINT addressxgeographysecuritytoken_pkey PRIMARY KEY (addressxgeographysecuritytokenid);


--
-- TOC entry 3907 (class 2606 OID 28338)
-- Name: addressxresourceitem addressxresourceitem_pkey; Type: CONSTRAINT; Schema: address; Owner: postgres
--

ALTER TABLE ONLY address.addressxresourceitem
    ADD CONSTRAINT addressxresourceitem_pkey PRIMARY KEY (addressxresourceitemid);


--
-- TOC entry 3909 (class 2606 OID 28345)
-- Name: addressxresourceitemsecuritytoken addressxresourceitemsecuritytoken_pkey; Type: CONSTRAINT; Schema: address; Owner: postgres
--

ALTER TABLE ONLY address.addressxresourceitemsecuritytoken
    ADD CONSTRAINT addressxresourceitemsecuritytoken_pkey PRIMARY KEY (addressxresourceitemsecuritytokenid);


--
-- TOC entry 3911 (class 2606 OID 28352)
-- Name: classification classification_pkey; Type: CONSTRAINT; Schema: classification; Owner: postgres
--

ALTER TABLE ONLY classification.classification
    ADD CONSTRAINT classification_pkey PRIMARY KEY (classificationid);


--
-- TOC entry 3913 (class 2606 OID 28359)
-- Name: classificationdataconcept classificationdataconcept_pkey; Type: CONSTRAINT; Schema: classification; Owner: postgres
--

ALTER TABLE ONLY classification.classificationdataconcept
    ADD CONSTRAINT classificationdataconcept_pkey PRIMARY KEY (classificationdataconceptid);


--
-- TOC entry 3915 (class 2606 OID 28366)
-- Name: classificationdataconceptsecuritytoken classificationdataconceptsecuritytoken_pkey; Type: CONSTRAINT; Schema: classification; Owner: postgres
--

ALTER TABLE ONLY classification.classificationdataconceptsecuritytoken
    ADD CONSTRAINT classificationdataconceptsecuritytoken_pkey PRIMARY KEY (classificationdataconceptsecuritytokenid);


--
-- TOC entry 3917 (class 2606 OID 28373)
-- Name: classificationdataconceptxclassification classificationdataconceptxclassification_pkey; Type: CONSTRAINT; Schema: classification; Owner: postgres
--

ALTER TABLE ONLY classification.classificationdataconceptxclassification
    ADD CONSTRAINT classificationdataconceptxclassification_pkey PRIMARY KEY (classificationdataconceptxclassificationid);


--
-- TOC entry 3919 (class 2606 OID 28380)
-- Name: classificationdataconceptxclassificationsecuritytoken classificationdataconceptxclassificationsecuritytoken_pkey; Type: CONSTRAINT; Schema: classification; Owner: postgres
--

ALTER TABLE ONLY classification.classificationdataconceptxclassificationsecuritytoken
    ADD CONSTRAINT classificationdataconceptxclassificationsecuritytoken_pkey PRIMARY KEY (classificationdataconceptxclassificationsecuritytokenid);


--
-- TOC entry 3921 (class 2606 OID 28387)
-- Name: classificationdataconceptxresourceitem classificationdataconceptxresourceitem_pkey; Type: CONSTRAINT; Schema: classification; Owner: postgres
--

ALTER TABLE ONLY classification.classificationdataconceptxresourceitem
    ADD CONSTRAINT classificationdataconceptxresourceitem_pkey PRIMARY KEY (classificationdataconceptxresourceitemid);


--
-- TOC entry 3923 (class 2606 OID 28394)
-- Name: classificationdataconceptxresourceitemsecuritytoken classificationdataconceptxresourceitemsecuritytoken_pkey; Type: CONSTRAINT; Schema: classification; Owner: postgres
--

ALTER TABLE ONLY classification.classificationdataconceptxresourceitemsecuritytoken
    ADD CONSTRAINT classificationdataconceptxresourceitemsecuritytoken_pkey PRIMARY KEY (classificationdataconceptxresourceitemsecuritytokenid);


--
-- TOC entry 3925 (class 2606 OID 28401)
-- Name: classificationsecuritytoken classificationsecuritytoken_pkey; Type: CONSTRAINT; Schema: classification; Owner: postgres
--

ALTER TABLE ONLY classification.classificationsecuritytoken
    ADD CONSTRAINT classificationsecuritytoken_pkey PRIMARY KEY (classificationsecuritytokenid);


--
-- TOC entry 3927 (class 2606 OID 28408)
-- Name: classificationxclassification classificationxclassification_pkey; Type: CONSTRAINT; Schema: classification; Owner: postgres
--

ALTER TABLE ONLY classification.classificationxclassification
    ADD CONSTRAINT classificationxclassification_pkey PRIMARY KEY (classificationxclassificationid);


--
-- TOC entry 3929 (class 2606 OID 28415)
-- Name: classificationxclassificationsecuritytoken classificationxclassificationsecuritytoken_pkey; Type: CONSTRAINT; Schema: classification; Owner: postgres
--

ALTER TABLE ONLY classification.classificationxclassificationsecuritytoken
    ADD CONSTRAINT classificationxclassificationsecuritytoken_pkey PRIMARY KEY (classificationxclassificationsecuritytokenid);


--
-- TOC entry 3931 (class 2606 OID 28422)
-- Name: classificationxresourceitem classificationxresourceitem_pkey; Type: CONSTRAINT; Schema: classification; Owner: postgres
--

ALTER TABLE ONLY classification.classificationxresourceitem
    ADD CONSTRAINT classificationxresourceitem_pkey PRIMARY KEY (classificationxresourceitemid);


--
-- TOC entry 3933 (class 2606 OID 28429)
-- Name: classificationxresourceitemsecuritytoken classificationxresourceitemsecuritytoken_pkey; Type: CONSTRAINT; Schema: classification; Owner: postgres
--

ALTER TABLE ONLY classification.classificationxresourceitemsecuritytoken
    ADD CONSTRAINT classificationxresourceitemsecuritytoken_pkey PRIMARY KEY (classificationxresourceitemsecuritytokenid);


--
-- TOC entry 3935 (class 2606 OID 28434)
-- Name: activeflag activeflag_pkey; Type: CONSTRAINT; Schema: dbo; Owner: postgres
--

ALTER TABLE ONLY dbo.activeflag
    ADD CONSTRAINT activeflag_pkey PRIMARY KEY (activeflagid);


--
-- TOC entry 3937 (class 2606 OID 28441)
-- Name: activeflagsecuritytoken activeflagsecuritytoken_pkey; Type: CONSTRAINT; Schema: dbo; Owner: postgres
--

ALTER TABLE ONLY dbo.activeflagsecuritytoken
    ADD CONSTRAINT activeflagsecuritytoken_pkey PRIMARY KEY (activeflagsecuritytokenid);


--
-- TOC entry 3939 (class 2606 OID 28448)
-- Name: activeflagxclassification activeflagxclassification_pkey; Type: CONSTRAINT; Schema: dbo; Owner: postgres
--

ALTER TABLE ONLY dbo.activeflagxclassification
    ADD CONSTRAINT activeflagxclassification_pkey PRIMARY KEY (activeflagxclassificationid);


--
-- TOC entry 3941 (class 2606 OID 28455)
-- Name: activeflagxclassificationsecuritytoken activeflagxclassificationsecuritytoken_pkey; Type: CONSTRAINT; Schema: dbo; Owner: postgres
--

ALTER TABLE ONLY dbo.activeflagxclassificationsecuritytoken
    ADD CONSTRAINT activeflagxclassificationsecuritytoken_pkey PRIMARY KEY (activeflagxclassificationsecuritytokenid);


--
-- TOC entry 3943 (class 2606 OID 28462)
-- Name: enterprise enterprise_pkey; Type: CONSTRAINT; Schema: dbo; Owner: postgres
--

ALTER TABLE ONLY dbo.enterprise
    ADD CONSTRAINT enterprise_pkey PRIMARY KEY (enterpriseid);


--
-- TOC entry 3945 (class 2606 OID 28467)
-- Name: enterprisesecuritytoken enterprisesecuritytoken_pkey; Type: CONSTRAINT; Schema: dbo; Owner: postgres
--

ALTER TABLE ONLY dbo.enterprisesecuritytoken
    ADD CONSTRAINT enterprisesecuritytoken_pkey PRIMARY KEY (enterprisesecuritytokenid);


--
-- TOC entry 3947 (class 2606 OID 28474)
-- Name: enterprisexclassification enterprisexclassification_pkey; Type: CONSTRAINT; Schema: dbo; Owner: postgres
--

ALTER TABLE ONLY dbo.enterprisexclassification
    ADD CONSTRAINT enterprisexclassification_pkey PRIMARY KEY (enterprisexclassificationid);


--
-- TOC entry 3949 (class 2606 OID 28481)
-- Name: enterprisexclassificationsecuritytoken enterprisexclassificationsecuritytoken_pkey; Type: CONSTRAINT; Schema: dbo; Owner: postgres
--

ALTER TABLE ONLY dbo.enterprisexclassificationsecuritytoken
    ADD CONSTRAINT enterprisexclassificationsecuritytoken_pkey PRIMARY KEY (enterprisexclassificationsecuritytokenid);


--
-- TOC entry 3951 (class 2606 OID 28488)
-- Name: systems systems_pkey; Type: CONSTRAINT; Schema: dbo; Owner: postgres
--

ALTER TABLE ONLY dbo.systems
    ADD CONSTRAINT systems_pkey PRIMARY KEY (systemid);


--
-- TOC entry 3953 (class 2606 OID 28493)
-- Name: systemssecuritytoken systemssecuritytoken_pkey; Type: CONSTRAINT; Schema: dbo; Owner: postgres
--

ALTER TABLE ONLY dbo.systemssecuritytoken
    ADD CONSTRAINT systemssecuritytoken_pkey PRIMARY KEY (systemssecuritytokenid);


--
-- TOC entry 3955 (class 2606 OID 28500)
-- Name: systemxclassification systemxclassification_pkey; Type: CONSTRAINT; Schema: dbo; Owner: postgres
--

ALTER TABLE ONLY dbo.systemxclassification
    ADD CONSTRAINT systemxclassification_pkey PRIMARY KEY (systemxclassificationid);


--
-- TOC entry 3957 (class 2606 OID 28507)
-- Name: systemxclassificationsecuritytoken systemxclassificationsecuritytoken_pkey; Type: CONSTRAINT; Schema: dbo; Owner: postgres
--

ALTER TABLE ONLY dbo.systemxclassificationsecuritytoken
    ADD CONSTRAINT systemxclassificationsecuritytoken_pkey PRIMARY KEY (systemxclassificationsecuritytokenid);


--
-- TOC entry 3959 (class 2606 OID 28512)
-- Name: event event_pkey; Type: CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE ONLY event.event
    ADD CONSTRAINT event_pkey PRIMARY KEY (eventid);


--
-- TOC entry 3961 (class 2606 OID 28519)
-- Name: eventsecuritytoken eventsecuritytoken_pkey; Type: CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE ONLY event.eventsecuritytoken
    ADD CONSTRAINT eventsecuritytoken_pkey PRIMARY KEY (eventssecuritytokenid);


--
-- TOC entry 3963 (class 2606 OID 28526)
-- Name: eventtype eventtype_pkey; Type: CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE ONLY event.eventtype
    ADD CONSTRAINT eventtype_pkey PRIMARY KEY (eventtypeid);


--
-- TOC entry 3965 (class 2606 OID 28533)
-- Name: eventtypessecuritytoken eventtypessecuritytoken_pkey; Type: CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE ONLY event.eventtypessecuritytoken
    ADD CONSTRAINT eventtypessecuritytoken_pkey PRIMARY KEY (eventtypessecuritytokenid);


--
-- TOC entry 3967 (class 2606 OID 28540)
-- Name: eventxaddress eventxaddress_pkey; Type: CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE ONLY event.eventxaddress
    ADD CONSTRAINT eventxaddress_pkey PRIMARY KEY (eventxaddressid);


--
-- TOC entry 3969 (class 2606 OID 28547)
-- Name: eventxaddresssecuritytoken eventxaddresssecuritytoken_pkey; Type: CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE ONLY event.eventxaddresssecuritytoken
    ADD CONSTRAINT eventxaddresssecuritytoken_pkey PRIMARY KEY (eventxaddresssecuritytokenid);


--
-- TOC entry 3971 (class 2606 OID 28554)
-- Name: eventxarrangement eventxarrangement_pkey; Type: CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE ONLY event.eventxarrangement
    ADD CONSTRAINT eventxarrangement_pkey PRIMARY KEY (eventxarrangementsid);


--
-- TOC entry 3973 (class 2606 OID 28561)
-- Name: eventxarrangementssecuritytoken eventxarrangementssecuritytoken_pkey; Type: CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE ONLY event.eventxarrangementssecuritytoken
    ADD CONSTRAINT eventxarrangementssecuritytoken_pkey PRIMARY KEY (eventxarrangementssecuritytokenid);


--
-- TOC entry 3975 (class 2606 OID 28568)
-- Name: eventxclassification eventxclassification_pkey; Type: CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE ONLY event.eventxclassification
    ADD CONSTRAINT eventxclassification_pkey PRIMARY KEY (eventxclassificationid);


--
-- TOC entry 3977 (class 2606 OID 28575)
-- Name: eventxclassificationsecuritytoken eventxclassificationsecuritytoken_pkey; Type: CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE ONLY event.eventxclassificationsecuritytoken
    ADD CONSTRAINT eventxclassificationsecuritytoken_pkey PRIMARY KEY (eventxclassificationssecuritytokenid);


--
-- TOC entry 3979 (class 2606 OID 28582)
-- Name: eventxevent eventxevent_pkey; Type: CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE ONLY event.eventxevent
    ADD CONSTRAINT eventxevent_pkey PRIMARY KEY (eventxeventid);


--
-- TOC entry 3981 (class 2606 OID 28589)
-- Name: eventxeventsecuritytoken eventxeventsecuritytoken_pkey; Type: CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE ONLY event.eventxeventsecuritytoken
    ADD CONSTRAINT eventxeventsecuritytoken_pkey PRIMARY KEY (eventxeventsecuritytokenid);


--
-- TOC entry 3983 (class 2606 OID 28596)
-- Name: eventxeventtype eventxeventtype_pkey; Type: CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE ONLY event.eventxeventtype
    ADD CONSTRAINT eventxeventtype_pkey PRIMARY KEY (eventxeventtypeid);


--
-- TOC entry 3985 (class 2606 OID 28603)
-- Name: eventxeventtypesecuritytoken eventxeventtypesecuritytoken_pkey; Type: CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE ONLY event.eventxeventtypesecuritytoken
    ADD CONSTRAINT eventxeventtypesecuritytoken_pkey PRIMARY KEY (eventxeventtypesecuritytokenid);


--
-- TOC entry 3987 (class 2606 OID 28610)
-- Name: eventxgeography eventxgeography_pkey; Type: CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE ONLY event.eventxgeography
    ADD CONSTRAINT eventxgeography_pkey PRIMARY KEY (eventxgeographyid);


--
-- TOC entry 3989 (class 2606 OID 28617)
-- Name: eventxgeographysecuritytoken eventxgeographysecuritytoken_pkey; Type: CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE ONLY event.eventxgeographysecuritytoken
    ADD CONSTRAINT eventxgeographysecuritytoken_pkey PRIMARY KEY (eventxgeographysecuritytokenid);


--
-- TOC entry 3991 (class 2606 OID 28624)
-- Name: eventxinvolvedparty eventxinvolvedparty_pkey; Type: CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE ONLY event.eventxinvolvedparty
    ADD CONSTRAINT eventxinvolvedparty_pkey PRIMARY KEY (eventxinvolvedpartyid);


--
-- TOC entry 3993 (class 2606 OID 28631)
-- Name: eventxinvolvedpartysecuritytoken eventxinvolvedpartysecuritytoken_pkey; Type: CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE ONLY event.eventxinvolvedpartysecuritytoken
    ADD CONSTRAINT eventxinvolvedpartysecuritytoken_pkey PRIMARY KEY (eventxinvolvedpartysecuritytokenid);


--
-- TOC entry 3995 (class 2606 OID 28638)
-- Name: eventxproduct eventxproduct_pkey; Type: CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE ONLY event.eventxproduct
    ADD CONSTRAINT eventxproduct_pkey PRIMARY KEY (eventxproductid);


--
-- TOC entry 3997 (class 2606 OID 28645)
-- Name: eventxproductsecuritytoken eventxproductsecuritytoken_pkey; Type: CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE ONLY event.eventxproductsecuritytoken
    ADD CONSTRAINT eventxproductsecuritytoken_pkey PRIMARY KEY (eventxproductsecuritytokenid);


--
-- TOC entry 3999 (class 2606 OID 28652)
-- Name: eventxresourceitem eventxresourceitem_pkey; Type: CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE ONLY event.eventxresourceitem
    ADD CONSTRAINT eventxresourceitem_pkey PRIMARY KEY (eventxresourceitemid);


--
-- TOC entry 4001 (class 2606 OID 28659)
-- Name: eventxresourceitemsecuritytoken eventxresourceitemsecuritytoken_pkey; Type: CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE ONLY event.eventxresourceitemsecuritytoken
    ADD CONSTRAINT eventxresourceitemsecuritytoken_pkey PRIMARY KEY (eventxresourceitemsecuritytokenid);


--
-- TOC entry 4003 (class 2606 OID 28666)
-- Name: eventxrules eventxrules_pkey; Type: CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE ONLY event.eventxrules
    ADD CONSTRAINT eventxrules_pkey PRIMARY KEY (eventxrulesid);


--
-- TOC entry 4005 (class 2606 OID 28673)
-- Name: eventxrulessecuritytoken eventxrulessecuritytoken_pkey; Type: CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE ONLY event.eventxrulessecuritytoken
    ADD CONSTRAINT eventxrulessecuritytoken_pkey PRIMARY KEY (eventxrulessecuritytokenid);


--
-- TOC entry 4007 (class 2606 OID 28680)
-- Name: geography geography_pkey; Type: CONSTRAINT; Schema: geography; Owner: postgres
--

ALTER TABLE ONLY geography.geography
    ADD CONSTRAINT geography_pkey PRIMARY KEY (geographyid);


--
-- TOC entry 4009 (class 2606 OID 28687)
-- Name: geographysecuritytoken geographysecuritytoken_pkey; Type: CONSTRAINT; Schema: geography; Owner: postgres
--

ALTER TABLE ONLY geography.geographysecuritytoken
    ADD CONSTRAINT geographysecuritytoken_pkey PRIMARY KEY (geographysecuritytokenid);


--
-- TOC entry 4011 (class 2606 OID 28694)
-- Name: geographyxclassification geographyxclassification_pkey; Type: CONSTRAINT; Schema: geography; Owner: postgres
--

ALTER TABLE ONLY geography.geographyxclassification
    ADD CONSTRAINT geographyxclassification_pkey PRIMARY KEY (geographyxclassificationid);


--
-- TOC entry 4013 (class 2606 OID 28701)
-- Name: geographyxclassificationsecuritytoken geographyxclassificationsecuritytoken_pkey; Type: CONSTRAINT; Schema: geography; Owner: postgres
--

ALTER TABLE ONLY geography.geographyxclassificationsecuritytoken
    ADD CONSTRAINT geographyxclassificationsecuritytoken_pkey PRIMARY KEY (geographyxclassificationsecuritytokenid);


--
-- TOC entry 4015 (class 2606 OID 28708)
-- Name: geographyxgeography geographyxgeography_pkey; Type: CONSTRAINT; Schema: geography; Owner: postgres
--

ALTER TABLE ONLY geography.geographyxgeography
    ADD CONSTRAINT geographyxgeography_pkey PRIMARY KEY (geographyxgeographyid);


--
-- TOC entry 4017 (class 2606 OID 28715)
-- Name: geographyxgeographysecuritytoken geographyxgeographysecuritytoken_pkey; Type: CONSTRAINT; Schema: geography; Owner: postgres
--

ALTER TABLE ONLY geography.geographyxgeographysecuritytoken
    ADD CONSTRAINT geographyxgeographysecuritytoken_pkey PRIMARY KEY (geographyxgeographysecuritytokenid);


--
-- TOC entry 4019 (class 2606 OID 28722)
-- Name: geographyxresourceitem geographyxresourceitem_pkey; Type: CONSTRAINT; Schema: geography; Owner: postgres
--

ALTER TABLE ONLY geography.geographyxresourceitem
    ADD CONSTRAINT geographyxresourceitem_pkey PRIMARY KEY (geographyxresourceitemid);


--
-- TOC entry 4021 (class 2606 OID 28729)
-- Name: geographyxresourceitemsecuritytoken geographyxresourceitemsecuritytoken_pkey; Type: CONSTRAINT; Schema: geography; Owner: postgres
--

ALTER TABLE ONLY geography.geographyxresourceitemsecuritytoken
    ADD CONSTRAINT geographyxresourceitemsecuritytoken_pkey PRIMARY KEY (geographyxresourceitemsecuritytokenid);


--
-- TOC entry 4023 (class 2606 OID 28734)
-- Name: involvedparty involvedparty_pkey; Type: CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedparty
    ADD CONSTRAINT involvedparty_pkey PRIMARY KEY (involvedpartyid);


--
-- TOC entry 4025 (class 2606 OID 28741)
-- Name: involvedpartyidentificationtype involvedpartyidentificationtype_pkey; Type: CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyidentificationtype
    ADD CONSTRAINT involvedpartyidentificationtype_pkey PRIMARY KEY (involvedpartyidentificationtypeid);


--
-- TOC entry 4027 (class 2606 OID 28748)
-- Name: involvedpartyidentificationtypesecuritytoken involvedpartyidentificationtypesecuritytoken_pkey; Type: CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyidentificationtypesecuritytoken
    ADD CONSTRAINT involvedpartyidentificationtypesecuritytoken_pkey PRIMARY KEY (involvedpartyidentificationtypesecuritytokenid);


--
-- TOC entry 4029 (class 2606 OID 28755)
-- Name: involvedpartynametype involvedpartynametype_pkey; Type: CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartynametype
    ADD CONSTRAINT involvedpartynametype_pkey PRIMARY KEY (involvedpartynametypeid);


--
-- TOC entry 4031 (class 2606 OID 28762)
-- Name: involvedpartynametypesecuritytoken involvedpartynametypesecuritytoken_pkey; Type: CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartynametypesecuritytoken
    ADD CONSTRAINT involvedpartynametypesecuritytoken_pkey PRIMARY KEY (involvedpartynametypesecuritytokenid);


--
-- TOC entry 4033 (class 2606 OID 28767)
-- Name: involvedpartynonorganic involvedpartynonorganic_pkey; Type: CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartynonorganic
    ADD CONSTRAINT involvedpartynonorganic_pkey PRIMARY KEY (involvedpartynonorganicid);


--
-- TOC entry 4035 (class 2606 OID 28774)
-- Name: involvedpartynonorganicsecuritytoken involvedpartynonorganicsecuritytoken_pkey; Type: CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartynonorganicsecuritytoken
    ADD CONSTRAINT involvedpartynonorganicsecuritytoken_pkey PRIMARY KEY (involvedpartynonorganicsecuritytokenid);


--
-- TOC entry 4037 (class 2606 OID 28779)
-- Name: involvedpartyorganic involvedpartyorganic_pkey; Type: CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyorganic
    ADD CONSTRAINT involvedpartyorganic_pkey PRIMARY KEY (involvedpartyorganicid);


--
-- TOC entry 4039 (class 2606 OID 28786)
-- Name: involvedpartyorganicsecuritytoken involvedpartyorganicsecuritytoken_pkey; Type: CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyorganicsecuritytoken
    ADD CONSTRAINT involvedpartyorganicsecuritytoken_pkey PRIMARY KEY (involvedpartyorganicsecuritytokenid);


--
-- TOC entry 4041 (class 2606 OID 28793)
-- Name: involvedpartyorganictype involvedpartyorganictype_pkey; Type: CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyorganictype
    ADD CONSTRAINT involvedpartyorganictype_pkey PRIMARY KEY (involvedpartyorganictypeid);


--
-- TOC entry 4043 (class 2606 OID 28800)
-- Name: involvedpartyorganictypesecuritytoken involvedpartyorganictypesecuritytoken_pkey; Type: CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyorganictypesecuritytoken
    ADD CONSTRAINT involvedpartyorganictypesecuritytoken_pkey PRIMARY KEY (involvedpartyorganictypesecuritytokenid);


--
-- TOC entry 4045 (class 2606 OID 28807)
-- Name: involvedpartysecuritytoken involvedpartysecuritytoken_pkey; Type: CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartysecuritytoken
    ADD CONSTRAINT involvedpartysecuritytoken_pkey PRIMARY KEY (involvedpartysecuritytokenid);


--
-- TOC entry 4047 (class 2606 OID 28814)
-- Name: involvedpartytype involvedpartytype_pkey; Type: CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartytype
    ADD CONSTRAINT involvedpartytype_pkey PRIMARY KEY (involvedpartytypeid);


--
-- TOC entry 4049 (class 2606 OID 28821)
-- Name: involvedpartytypesecuritytoken involvedpartytypesecuritytoken_pkey; Type: CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartytypesecuritytoken
    ADD CONSTRAINT involvedpartytypesecuritytoken_pkey PRIMARY KEY (involvedpartytypesecuritytokenid);


--
-- TOC entry 4051 (class 2606 OID 28828)
-- Name: involvedpartyxaddress involvedpartyxaddress_pkey; Type: CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyxaddress
    ADD CONSTRAINT involvedpartyxaddress_pkey PRIMARY KEY (involvedpartyxaddressid);


--
-- TOC entry 4053 (class 2606 OID 28835)
-- Name: involvedpartyxaddresssecuritytoken involvedpartyxaddresssecuritytoken_pkey; Type: CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyxaddresssecuritytoken
    ADD CONSTRAINT involvedpartyxaddresssecuritytoken_pkey PRIMARY KEY (involvedpartyxaddresssecuritytokenid);


--
-- TOC entry 4055 (class 2606 OID 28842)
-- Name: involvedpartyxclassification involvedpartyxclassification_pkey; Type: CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyxclassification
    ADD CONSTRAINT involvedpartyxclassification_pkey PRIMARY KEY (involvedpartyxclassificationid);


--
-- TOC entry 4057 (class 2606 OID 28849)
-- Name: involvedpartyxclassificationsecuritytoken involvedpartyxclassificationsecuritytoken_pkey; Type: CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyxclassificationsecuritytoken
    ADD CONSTRAINT involvedpartyxclassificationsecuritytoken_pkey PRIMARY KEY (involvedpartyxclassificationsecuritytokenid);


--
-- TOC entry 4059 (class 2606 OID 28856)
-- Name: involvedpartyxinvolvedparty involvedpartyxinvolvedparty_pkey; Type: CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyxinvolvedparty
    ADD CONSTRAINT involvedpartyxinvolvedparty_pkey PRIMARY KEY (involvedpartyxinvolvedpartyid);


--
-- TOC entry 4061 (class 2606 OID 28863)
-- Name: involvedpartyxinvolvedpartyidentificationtype involvedpartyxinvolvedpartyidentificationtype_pkey; Type: CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyxinvolvedpartyidentificationtype
    ADD CONSTRAINT involvedpartyxinvolvedpartyidentificationtype_pkey PRIMARY KEY (involvedpartyxinvolvedpartyidentificationtypeid);


--
-- TOC entry 4063 (class 2606 OID 28870)
-- Name: involvedpartyxinvolvedpartyidentificationtypesecuritytoken involvedpartyxinvolvedpartyidentificationtypesecuritytoken_pkey; Type: CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyxinvolvedpartyidentificationtypesecuritytoken
    ADD CONSTRAINT involvedpartyxinvolvedpartyidentificationtypesecuritytoken_pkey PRIMARY KEY (involvedpartyxinvolvedpartyidentificationtypesecuritytokenid);


--
-- TOC entry 4065 (class 2606 OID 28877)
-- Name: involvedpartyxinvolvedpartynametype involvedpartyxinvolvedpartynametype_pkey; Type: CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyxinvolvedpartynametype
    ADD CONSTRAINT involvedpartyxinvolvedpartynametype_pkey PRIMARY KEY (involvedpartyxinvolvedpartynametypeid);


--
-- TOC entry 4067 (class 2606 OID 28884)
-- Name: involvedpartyxinvolvedpartynametypesecuritytoken involvedpartyxinvolvedpartynametypesecuritytoken_pkey; Type: CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyxinvolvedpartynametypesecuritytoken
    ADD CONSTRAINT involvedpartyxinvolvedpartynametypesecuritytoken_pkey PRIMARY KEY (involvedpartyxinvolvedpartynametypesecuritytokenid);


--
-- TOC entry 4069 (class 2606 OID 28891)
-- Name: involvedpartyxinvolvedpartysecuritytoken involvedpartyxinvolvedpartysecuritytoken_pkey; Type: CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyxinvolvedpartysecuritytoken
    ADD CONSTRAINT involvedpartyxinvolvedpartysecuritytoken_pkey PRIMARY KEY (involvedpartyxinvolvedpartysecuritytokenid);


--
-- TOC entry 4071 (class 2606 OID 28898)
-- Name: involvedpartyxinvolvedpartytype involvedpartyxinvolvedpartytype_pkey; Type: CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyxinvolvedpartytype
    ADD CONSTRAINT involvedpartyxinvolvedpartytype_pkey PRIMARY KEY (involvedpartyxinvolvedpartytypeid);


--
-- TOC entry 4073 (class 2606 OID 28905)
-- Name: involvedpartyxinvolvedpartytypesecuritytoken involvedpartyxinvolvedpartytypesecuritytoken_pkey; Type: CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyxinvolvedpartytypesecuritytoken
    ADD CONSTRAINT involvedpartyxinvolvedpartytypesecuritytoken_pkey PRIMARY KEY (involvedpartyxinvolvedpartytypesecuritytokenid);


--
-- TOC entry 4075 (class 2606 OID 28912)
-- Name: involvedpartyxproduct involvedpartyxproduct_pkey; Type: CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyxproduct
    ADD CONSTRAINT involvedpartyxproduct_pkey PRIMARY KEY (involvedpartyxproductid);


--
-- TOC entry 4077 (class 2606 OID 28919)
-- Name: involvedpartyxproductsecuritytoken involvedpartyxproductsecuritytoken_pkey; Type: CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyxproductsecuritytoken
    ADD CONSTRAINT involvedpartyxproductsecuritytoken_pkey PRIMARY KEY (involvedpartyxproductsecuritytokenid);


--
-- TOC entry 4079 (class 2606 OID 28926)
-- Name: involvedpartyxproducttype involvedpartyxproducttype_pkey; Type: CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyxproducttype
    ADD CONSTRAINT involvedpartyxproducttype_pkey PRIMARY KEY (involvedpartyxproducttypeid);


--
-- TOC entry 4081 (class 2606 OID 28933)
-- Name: involvedpartyxproducttypesecuritytoken involvedpartyxproducttypesecuritytoken_pkey; Type: CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyxproducttypesecuritytoken
    ADD CONSTRAINT involvedpartyxproducttypesecuritytoken_pkey PRIMARY KEY (involvedpartyxproducttypesecuritytokenid);


--
-- TOC entry 4083 (class 2606 OID 28940)
-- Name: involvedpartyxresourceitem involvedpartyxresourceitem_pkey; Type: CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyxresourceitem
    ADD CONSTRAINT involvedpartyxresourceitem_pkey PRIMARY KEY (involvedpartyxresourceitemid);


--
-- TOC entry 4085 (class 2606 OID 28947)
-- Name: involvedpartyxresourceitemsecuritytoken involvedpartyxresourceitemsecuritytoken_pkey; Type: CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyxresourceitemsecuritytoken
    ADD CONSTRAINT involvedpartyxresourceitemsecuritytoken_pkey PRIMARY KEY (involvedpartyxresourceitemsecuritytokenid);


--
-- TOC entry 4087 (class 2606 OID 28954)
-- Name: involvedpartyxrules involvedpartyxrules_pkey; Type: CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyxrules
    ADD CONSTRAINT involvedpartyxrules_pkey PRIMARY KEY (involvedpartyxrulesid);


--
-- TOC entry 4089 (class 2606 OID 28961)
-- Name: involvedpartyxrulessecuritytoken involvedpartyxrulessecuritytoken_pkey; Type: CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyxrulessecuritytoken
    ADD CONSTRAINT involvedpartyxrulessecuritytoken_pkey PRIMARY KEY (involvedpartyxrulessecuritytokenid);


--
-- TOC entry 4091 (class 2606 OID 28968)
-- Name: product product_pkey; Type: CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE ONLY product.product
    ADD CONSTRAINT product_pkey PRIMARY KEY (productid);


--
-- TOC entry 4093 (class 2606 OID 28975)
-- Name: productsecuritytoken productsecuritytoken_pkey; Type: CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE ONLY product.productsecuritytoken
    ADD CONSTRAINT productsecuritytoken_pkey PRIMARY KEY (productsecuritytokenid);


--
-- TOC entry 4095 (class 2606 OID 28982)
-- Name: producttype producttype_pkey; Type: CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE ONLY product.producttype
    ADD CONSTRAINT producttype_pkey PRIMARY KEY (producttypeid);


--
-- TOC entry 4097 (class 2606 OID 28989)
-- Name: producttypessecuritytoken producttypessecuritytoken_pkey; Type: CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE ONLY product.producttypessecuritytoken
    ADD CONSTRAINT producttypessecuritytoken_pkey PRIMARY KEY (producttypessecuritytokenid);


--
-- TOC entry 4099 (class 2606 OID 28996)
-- Name: producttypexclassification producttypexclassification_pkey; Type: CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE ONLY product.producttypexclassification
    ADD CONSTRAINT producttypexclassification_pkey PRIMARY KEY (producttypexclassificationid);


--
-- TOC entry 4101 (class 2606 OID 29003)
-- Name: producttypexclassificationsecuritytoken producttypexclassificationsecuritytoken_pkey; Type: CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE ONLY product.producttypexclassificationsecuritytoken
    ADD CONSTRAINT producttypexclassificationsecuritytoken_pkey PRIMARY KEY (producttypexclassificationsecuritytokenid);


--
-- TOC entry 4103 (class 2606 OID 29010)
-- Name: productxclassification productxclassification_pkey; Type: CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE ONLY product.productxclassification
    ADD CONSTRAINT productxclassification_pkey PRIMARY KEY (productxclassificationid);


--
-- TOC entry 4105 (class 2606 OID 29017)
-- Name: productxclassificationsecuritytoken productxclassificationsecuritytoken_pkey; Type: CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE ONLY product.productxclassificationsecuritytoken
    ADD CONSTRAINT productxclassificationsecuritytoken_pkey PRIMARY KEY (productxclassificationsecuritytokenid);


--
-- TOC entry 4107 (class 2606 OID 29024)
-- Name: productxproduct productxproduct_pkey; Type: CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE ONLY product.productxproduct
    ADD CONSTRAINT productxproduct_pkey PRIMARY KEY (productxproductid);


--
-- TOC entry 4109 (class 2606 OID 29031)
-- Name: productxproductsecuritytoken productxproductsecuritytoken_pkey; Type: CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE ONLY product.productxproductsecuritytoken
    ADD CONSTRAINT productxproductsecuritytoken_pkey PRIMARY KEY (productxproductsecuritytokenid);


--
-- TOC entry 4111 (class 2606 OID 29038)
-- Name: productxproducttype productxproducttype_pkey; Type: CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE ONLY product.productxproducttype
    ADD CONSTRAINT productxproducttype_pkey PRIMARY KEY (productxproducttypeid);


--
-- TOC entry 4113 (class 2606 OID 29045)
-- Name: productxproducttypesecuritytoken productxproducttypesecuritytoken_pkey; Type: CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE ONLY product.productxproducttypesecuritytoken
    ADD CONSTRAINT productxproducttypesecuritytoken_pkey PRIMARY KEY (productxproducttypesecuritytokenid);


--
-- TOC entry 4115 (class 2606 OID 29052)
-- Name: productxresourceitem productxresourceitem_pkey; Type: CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE ONLY product.productxresourceitem
    ADD CONSTRAINT productxresourceitem_pkey PRIMARY KEY (productxresourceitemid);


--
-- TOC entry 4117 (class 2606 OID 29059)
-- Name: productxresourceitemsecuritytoken productxresourceitemsecuritytoken_pkey; Type: CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE ONLY product.productxresourceitemsecuritytoken
    ADD CONSTRAINT productxresourceitemsecuritytoken_pkey PRIMARY KEY (productxresourceitemsecuritytokenid);


--
-- TOC entry 4243 (class 2606 OID 29459)
-- Name: arrangementhierarchyview arrangementhierarchyview_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.arrangementhierarchyview
    ADD CONSTRAINT arrangementhierarchyview_pkey PRIMARY KEY (id);


--
-- TOC entry 4245 (class 2606 OID 29466)
-- Name: classificationhierarchyview classificationhierarchyview_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.classificationhierarchyview
    ADD CONSTRAINT classificationhierarchyview_pkey PRIMARY KEY (id);


--
-- TOC entry 4247 (class 2606 OID 29473)
-- Name: geographyhierarchyview geographyhierarchyview_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.geographyhierarchyview
    ADD CONSTRAINT geographyhierarchyview_pkey PRIMARY KEY (id);


--
-- TOC entry 4249 (class 2606 OID 29480)
-- Name: producthierarchyview producthierarchyview_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.producthierarchyview
    ADD CONSTRAINT producthierarchyview_pkey PRIMARY KEY (id);


--
-- TOC entry 4253 (class 2606 OID 29490)
-- Name: resourceitemhierarchyview resourceitemhierarchyview_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.resourceitemhierarchyview
    ADD CONSTRAINT resourceitemhierarchyview_pkey PRIMARY KEY (id);


--
-- TOC entry 4255 (class 2606 OID 29497)
-- Name: ruleshierarchyview ruleshierarchyview_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.ruleshierarchyview
    ADD CONSTRAINT ruleshierarchyview_pkey PRIMARY KEY (id);


--
-- TOC entry 4257 (class 2606 OID 29502)
-- Name: securityhierarchyparents securityhierarchyparents_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.securityhierarchyparents
    ADD CONSTRAINT securityhierarchyparents_pkey PRIMARY KEY (id);


--
-- TOC entry 4251 (class 2606 OID 29504)
-- Name: quarters_months uk_snpn7ecm4aaxya8sdplfi575j; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.quarters_months
    ADD CONSTRAINT uk_snpn7ecm4aaxya8sdplfi575j UNIQUE (lumonthslist_monthid);


--
-- TOC entry 4119 (class 2606 OID 29066)
-- Name: resourceitem resourceitem_pkey; Type: CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE ONLY resource.resourceitem
    ADD CONSTRAINT resourceitem_pkey PRIMARY KEY (resourceitemid);


--
-- TOC entry 4121 (class 2606 OID 29071)
-- Name: resourceitemdata resourceitemdata_pkey; Type: CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE ONLY resource.resourceitemdata
    ADD CONSTRAINT resourceitemdata_pkey PRIMARY KEY (resourceitemdataid);


--
-- TOC entry 4123 (class 2606 OID 29078)
-- Name: resourceitemdatasecuritytoken resourceitemdatasecuritytoken_pkey; Type: CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE ONLY resource.resourceitemdatasecuritytoken
    ADD CONSTRAINT resourceitemdatasecuritytoken_pkey PRIMARY KEY (resourceitemdatasecuritytokenid);


--
-- TOC entry 4125 (class 2606 OID 29085)
-- Name: resourceitemdataxclassification resourceitemdataxclassification_pkey; Type: CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE ONLY resource.resourceitemdataxclassification
    ADD CONSTRAINT resourceitemdataxclassification_pkey PRIMARY KEY (resourceitemdataxclassificationid);


--
-- TOC entry 4127 (class 2606 OID 29092)
-- Name: resourceitemdataxclassificationsecuritytoken resourceitemdataxclassificationsecuritytoken_pkey; Type: CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE ONLY resource.resourceitemdataxclassificationsecuritytoken
    ADD CONSTRAINT resourceitemdataxclassificationsecuritytoken_pkey PRIMARY KEY (resourceitemdataxclassificationsecuritytokenid);


--
-- TOC entry 4129 (class 2606 OID 29099)
-- Name: resourceitemsecuritytoken resourceitemsecuritytoken_pkey; Type: CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE ONLY resource.resourceitemsecuritytoken
    ADD CONSTRAINT resourceitemsecuritytoken_pkey PRIMARY KEY (resourceitemsecuritytokenid);


--
-- TOC entry 4131 (class 2606 OID 29106)
-- Name: resourceitemtype resourceitemtype_pkey; Type: CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE ONLY resource.resourceitemtype
    ADD CONSTRAINT resourceitemtype_pkey PRIMARY KEY (resourceitemtypeid);


--
-- TOC entry 4133 (class 2606 OID 29113)
-- Name: resourceitemtypesecuritytoken resourceitemtypesecuritytoken_pkey; Type: CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE ONLY resource.resourceitemtypesecuritytoken
    ADD CONSTRAINT resourceitemtypesecuritytoken_pkey PRIMARY KEY (resourceitemtypesecuritytokenid);


--
-- TOC entry 4135 (class 2606 OID 29120)
-- Name: resourceitemxclassification resourceitemxclassification_pkey; Type: CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE ONLY resource.resourceitemxclassification
    ADD CONSTRAINT resourceitemxclassification_pkey PRIMARY KEY (resourceitemxclassificationid);


--
-- TOC entry 4137 (class 2606 OID 29127)
-- Name: resourceitemxclassificationsecuritytoken resourceitemxclassificationsecuritytoken_pkey; Type: CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE ONLY resource.resourceitemxclassificationsecuritytoken
    ADD CONSTRAINT resourceitemxclassificationsecuritytoken_pkey PRIMARY KEY (resourceitemxclassificationsecuritytokenid);


--
-- TOC entry 4139 (class 2606 OID 29134)
-- Name: resourceitemxresourceitem resourceitemxresourceitem_pkey; Type: CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE ONLY resource.resourceitemxresourceitem
    ADD CONSTRAINT resourceitemxresourceitem_pkey PRIMARY KEY (resourceitemxresourceitemid);


--
-- TOC entry 4141 (class 2606 OID 29141)
-- Name: resourceitemxresourceitemsecuritytoken resourceitemxresourceitemsecuritytoken_pkey; Type: CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE ONLY resource.resourceitemxresourceitemsecuritytoken
    ADD CONSTRAINT resourceitemxresourceitemsecuritytoken_pkey PRIMARY KEY (resourceitemxresourceitemsecuritytokenid);


--
-- TOC entry 4143 (class 2606 OID 29148)
-- Name: resourceitemxresourceitemtype resourceitemxresourceitemtype_pkey; Type: CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE ONLY resource.resourceitemxresourceitemtype
    ADD CONSTRAINT resourceitemxresourceitemtype_pkey PRIMARY KEY (resourceitemxresourceitemtypeid);


--
-- TOC entry 4145 (class 2606 OID 29155)
-- Name: resourceitemxresourceitemtypesecuritytoken resourceitemxresourceitemtypesecuritytoken_pkey; Type: CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE ONLY resource.resourceitemxresourceitemtypesecuritytoken
    ADD CONSTRAINT resourceitemxresourceitemtypesecuritytoken_pkey PRIMARY KEY (resourceitemxresourceitemtypesecuritytokenid);


--
-- TOC entry 4147 (class 2606 OID 29162)
-- Name: rules rules_pkey; Type: CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE ONLY rules.rules
    ADD CONSTRAINT rules_pkey PRIMARY KEY (rulesid);


--
-- TOC entry 4149 (class 2606 OID 29169)
-- Name: rulessecuritytoken rulessecuritytoken_pkey; Type: CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE ONLY rules.rulessecuritytoken
    ADD CONSTRAINT rulessecuritytoken_pkey PRIMARY KEY (rulessecuritytokenid);


--
-- TOC entry 4151 (class 2606 OID 29176)
-- Name: rulestype rulestype_pkey; Type: CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE ONLY rules.rulestype
    ADD CONSTRAINT rulestype_pkey PRIMARY KEY (rulestypeid);


--
-- TOC entry 4153 (class 2606 OID 29183)
-- Name: rulestypessecuritytoken rulestypessecuritytoken_pkey; Type: CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE ONLY rules.rulestypessecuritytoken
    ADD CONSTRAINT rulestypessecuritytoken_pkey PRIMARY KEY (rulestypessecuritytokenid);


--
-- TOC entry 4155 (class 2606 OID 29190)
-- Name: rulestypexclassification rulestypexclassification_pkey; Type: CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE ONLY rules.rulestypexclassification
    ADD CONSTRAINT rulestypexclassification_pkey PRIMARY KEY (rulestypexclassificationid);


--
-- TOC entry 4157 (class 2606 OID 29197)
-- Name: rulestypexclassificationsecuritytoken rulestypexclassificationsecuritytoken_pkey; Type: CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE ONLY rules.rulestypexclassificationsecuritytoken
    ADD CONSTRAINT rulestypexclassificationsecuritytoken_pkey PRIMARY KEY (rulestypexclassificationsecuritytokenid);


--
-- TOC entry 4159 (class 2606 OID 29204)
-- Name: rulestypexresourceitem rulestypexresourceitem_pkey; Type: CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE ONLY rules.rulestypexresourceitem
    ADD CONSTRAINT rulestypexresourceitem_pkey PRIMARY KEY (rulestypexresourceitemid);


--
-- TOC entry 4161 (class 2606 OID 29211)
-- Name: rulestypexresourceitemsecuritytoken rulestypexresourceitemsecuritytoken_pkey; Type: CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE ONLY rules.rulestypexresourceitemsecuritytoken
    ADD CONSTRAINT rulestypexresourceitemsecuritytoken_pkey PRIMARY KEY (rulestypexresourceitemsecuritytokenid);


--
-- TOC entry 4163 (class 2606 OID 29218)
-- Name: rulesxarrangement rulesxarrangement_pkey; Type: CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE ONLY rules.rulesxarrangement
    ADD CONSTRAINT rulesxarrangement_pkey PRIMARY KEY (rulesxarrangementsid);


--
-- TOC entry 4165 (class 2606 OID 29225)
-- Name: rulesxarrangementssecuritytoken rulesxarrangementssecuritytoken_pkey; Type: CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE ONLY rules.rulesxarrangementssecuritytoken
    ADD CONSTRAINT rulesxarrangementssecuritytoken_pkey PRIMARY KEY (rulesxarrangementssecuritytokenid);


--
-- TOC entry 4167 (class 2606 OID 29232)
-- Name: rulesxclassification rulesxclassification_pkey; Type: CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE ONLY rules.rulesxclassification
    ADD CONSTRAINT rulesxclassification_pkey PRIMARY KEY (rulesxclassificationid);


--
-- TOC entry 4169 (class 2606 OID 29239)
-- Name: rulesxclassificationsecuritytoken rulesxclassificationsecuritytoken_pkey; Type: CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE ONLY rules.rulesxclassificationsecuritytoken
    ADD CONSTRAINT rulesxclassificationsecuritytoken_pkey PRIMARY KEY (rulesxclassificationsecuritytokenid);


--
-- TOC entry 4171 (class 2606 OID 29246)
-- Name: rulesxinvolvedparty rulesxinvolvedparty_pkey; Type: CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE ONLY rules.rulesxinvolvedparty
    ADD CONSTRAINT rulesxinvolvedparty_pkey PRIMARY KEY (rulesxinvolvedpartyid);


--
-- TOC entry 4173 (class 2606 OID 29253)
-- Name: rulesxinvolvedpartysecuritytoken rulesxinvolvedpartysecuritytoken_pkey; Type: CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE ONLY rules.rulesxinvolvedpartysecuritytoken
    ADD CONSTRAINT rulesxinvolvedpartysecuritytoken_pkey PRIMARY KEY (rulesxinvolvedpartysecuritytokenid);


--
-- TOC entry 4175 (class 2606 OID 29260)
-- Name: rulesxproduct rulesxproduct_pkey; Type: CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE ONLY rules.rulesxproduct
    ADD CONSTRAINT rulesxproduct_pkey PRIMARY KEY (rulesxproductid);


--
-- TOC entry 4177 (class 2606 OID 29267)
-- Name: rulesxproductsecuritytoken rulesxproductsecuritytoken_pkey; Type: CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE ONLY rules.rulesxproductsecuritytoken
    ADD CONSTRAINT rulesxproductsecuritytoken_pkey PRIMARY KEY (rulesxproductsecuritytokenid);


--
-- TOC entry 4179 (class 2606 OID 29274)
-- Name: rulesxresourceitem rulesxresourceitem_pkey; Type: CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE ONLY rules.rulesxresourceitem
    ADD CONSTRAINT rulesxresourceitem_pkey PRIMARY KEY (rulesxresourceitemid);


--
-- TOC entry 4181 (class 2606 OID 29281)
-- Name: rulesxresourceitemsecuritytoken rulesxresourceitemsecuritytoken_pkey; Type: CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE ONLY rules.rulesxresourceitemsecuritytoken
    ADD CONSTRAINT rulesxresourceitemsecuritytoken_pkey PRIMARY KEY (rulesxresourceitemsecuritytokenid);


--
-- TOC entry 4183 (class 2606 OID 29288)
-- Name: rulesxrules rulesxrules_pkey; Type: CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE ONLY rules.rulesxrules
    ADD CONSTRAINT rulesxrules_pkey PRIMARY KEY (rulesxrulesid);


--
-- TOC entry 4185 (class 2606 OID 29295)
-- Name: rulesxrulessecuritytoken rulesxrulessecuritytoken_pkey; Type: CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE ONLY rules.rulesxrulessecuritytoken
    ADD CONSTRAINT rulesxrulessecuritytoken_pkey PRIMARY KEY (rulesxrulessecuritytokenid);


--
-- TOC entry 4187 (class 2606 OID 29302)
-- Name: rulesxrulestype rulesxrulestype_pkey; Type: CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE ONLY rules.rulesxrulestype
    ADD CONSTRAINT rulesxrulestype_pkey PRIMARY KEY (rulesxrulestypeid);


--
-- TOC entry 4189 (class 2606 OID 29309)
-- Name: rulesxrulestypesecuritytoken rulesxrulestypesecuritytoken_pkey; Type: CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE ONLY rules.rulesxrulestypesecuritytoken
    ADD CONSTRAINT rulesxrulestypesecuritytoken_pkey PRIMARY KEY (rulesxrulestypesecuritytokenid);


--
-- TOC entry 4191 (class 2606 OID 29316)
-- Name: securityhierarchy securityhierarchy_pkey; Type: CONSTRAINT; Schema: security; Owner: postgres
--

ALTER TABLE ONLY security.securityhierarchy
    ADD CONSTRAINT securityhierarchy_pkey PRIMARY KEY (id);


--
-- TOC entry 4193 (class 2606 OID 29323)
-- Name: securitytoken securitytoken_pkey; Type: CONSTRAINT; Schema: security; Owner: postgres
--

ALTER TABLE ONLY security.securitytoken
    ADD CONSTRAINT securitytoken_pkey PRIMARY KEY (securitytokenid);


--
-- TOC entry 4195 (class 2606 OID 29330)
-- Name: securitytokenssecuritytoken securitytokenssecuritytoken_pkey; Type: CONSTRAINT; Schema: security; Owner: postgres
--

ALTER TABLE ONLY security.securitytokenssecuritytoken
    ADD CONSTRAINT securitytokenssecuritytoken_pkey PRIMARY KEY (securitytokenaccessid);


--
-- TOC entry 4197 (class 2606 OID 29337)
-- Name: securitytokensxsecuritytokensecuritytoken securitytokensxsecuritytokensecuritytoken_pkey; Type: CONSTRAINT; Schema: security; Owner: postgres
--

ALTER TABLE ONLY security.securitytokensxsecuritytokensecuritytoken
    ADD CONSTRAINT securitytokensxsecuritytokensecuritytoken_pkey PRIMARY KEY (securitytokenxsecuritytokensecuritytokenid);


--
-- TOC entry 4199 (class 2606 OID 29344)
-- Name: securitytokenxclassification securitytokenxclassification_pkey; Type: CONSTRAINT; Schema: security; Owner: postgres
--

ALTER TABLE ONLY security.securitytokenxclassification
    ADD CONSTRAINT securitytokenxclassification_pkey PRIMARY KEY (securitytokenxclassificationid);


--
-- TOC entry 4201 (class 2606 OID 29351)
-- Name: securitytokenxclassificationsecuritytoken securitytokenxclassificationsecuritytoken_pkey; Type: CONSTRAINT; Schema: security; Owner: postgres
--

ALTER TABLE ONLY security.securitytokenxclassificationsecuritytoken
    ADD CONSTRAINT securitytokenxclassificationsecuritytoken_pkey PRIMARY KEY (securitytokenxclassificationsecuritytokenid);


--
-- TOC entry 4203 (class 2606 OID 29358)
-- Name: securitytokenxsecuritytoken securitytokenxsecuritytoken_pkey; Type: CONSTRAINT; Schema: security; Owner: postgres
--

ALTER TABLE ONLY security.securitytokenxsecuritytoken
    ADD CONSTRAINT securitytokenxsecuritytoken_pkey PRIMARY KEY (securitytokenxsecuritytokenid);


--
-- TOC entry 4205 (class 2606 OID 29363)
-- Name: daynames daynames_pkey; Type: CONSTRAINT; Schema: time; Owner: postgres
--

ALTER TABLE ONLY "time".daynames
    ADD CONSTRAINT daynames_pkey PRIMARY KEY (daynameid);


--
-- TOC entry 4207 (class 2606 OID 29368)
-- Name: dayparts dayparts_pkey; Type: CONSTRAINT; Schema: time; Owner: postgres
--

ALTER TABLE ONLY "time".dayparts
    ADD CONSTRAINT dayparts_pkey PRIMARY KEY (daypartid);


--
-- TOC entry 4209 (class 2606 OID 29373)
-- Name: days days_pkey; Type: CONSTRAINT; Schema: time; Owner: postgres
--

ALTER TABLE ONLY "time".days
    ADD CONSTRAINT days_pkey PRIMARY KEY (dayid);


--
-- TOC entry 4211 (class 2606 OID 29378)
-- Name: halfhourdayparts halfhourdayparts_pkey; Type: CONSTRAINT; Schema: time; Owner: postgres
--

ALTER TABLE ONLY "time".halfhourdayparts
    ADD CONSTRAINT halfhourdayparts_pkey PRIMARY KEY (halfhourdaypartid);


--
-- TOC entry 4215 (class 2606 OID 29383)
-- Name: halfhours halfhours_pkey; Type: CONSTRAINT; Schema: time; Owner: postgres
--

ALTER TABLE ONLY "time".halfhours
    ADD CONSTRAINT halfhours_pkey PRIMARY KEY (hourid, minuteid);


--
-- TOC entry 4217 (class 2606 OID 29388)
-- Name: hours hours_pkey; Type: CONSTRAINT; Schema: time; Owner: postgres
--

ALTER TABLE ONLY "time".hours
    ADD CONSTRAINT hours_pkey PRIMARY KEY (hourid);


--
-- TOC entry 4219 (class 2606 OID 29393)
-- Name: monthofyear monthofyear_pkey; Type: CONSTRAINT; Schema: time; Owner: postgres
--

ALTER TABLE ONLY "time".monthofyear
    ADD CONSTRAINT monthofyear_pkey PRIMARY KEY (monthofyearid);


--
-- TOC entry 4221 (class 2606 OID 29398)
-- Name: months months_pkey; Type: CONSTRAINT; Schema: time; Owner: postgres
--

ALTER TABLE ONLY "time".months
    ADD CONSTRAINT months_pkey PRIMARY KEY (monthid);


--
-- TOC entry 4223 (class 2606 OID 29405)
-- Name: publicholidays publicholidays_pkey; Type: CONSTRAINT; Schema: time; Owner: postgres
--

ALTER TABLE ONLY "time".publicholidays
    ADD CONSTRAINT publicholidays_pkey PRIMARY KEY (publicholidayid);


--
-- TOC entry 4225 (class 2606 OID 29410)
-- Name: quarters quarters_pkey; Type: CONSTRAINT; Schema: time; Owner: postgres
--

ALTER TABLE ONLY "time".quarters
    ADD CONSTRAINT quarters_pkey PRIMARY KEY (quarterid);


--
-- TOC entry 4227 (class 2606 OID 29415)
-- Name: time time_pkey; Type: CONSTRAINT; Schema: time; Owner: postgres
--

ALTER TABLE ONLY "time"."time"
    ADD CONSTRAINT time_pkey PRIMARY KEY (hourid, minuteid);


--
-- TOC entry 4229 (class 2606 OID 29420)
-- Name: trans_fiscal trans_fiscal_pkey; Type: CONSTRAINT; Schema: time; Owner: postgres
--

ALTER TABLE ONLY "time".trans_fiscal
    ADD CONSTRAINT trans_fiscal_pkey PRIMARY KEY (dayid);


--
-- TOC entry 4231 (class 2606 OID 29425)
-- Name: trans_mtd trans_mtd_pkey; Type: CONSTRAINT; Schema: time; Owner: postgres
--

ALTER TABLE ONLY "time".trans_mtd
    ADD CONSTRAINT trans_mtd_pkey PRIMARY KEY (dayid, mtddayid);


--
-- TOC entry 4233 (class 2606 OID 29430)
-- Name: trans_qtd trans_qtd_pkey; Type: CONSTRAINT; Schema: time; Owner: postgres
--

ALTER TABLE ONLY "time".trans_qtd
    ADD CONSTRAINT trans_qtd_pkey PRIMARY KEY (dayid, qtddayid);


--
-- TOC entry 4235 (class 2606 OID 29435)
-- Name: trans_qtm trans_qtm_pkey; Type: CONSTRAINT; Schema: time; Owner: postgres
--

ALTER TABLE ONLY "time".trans_qtm
    ADD CONSTRAINT trans_qtm_pkey PRIMARY KEY (monthid, qtm_monthid);


--
-- TOC entry 4237 (class 2606 OID 29440)
-- Name: trans_ytd trans_ytd_pkey; Type: CONSTRAINT; Schema: time; Owner: postgres
--

ALTER TABLE ONLY "time".trans_ytd
    ADD CONSTRAINT trans_ytd_pkey PRIMARY KEY (dayid, ytddayid);


--
-- TOC entry 4213 (class 2606 OID 29452)
-- Name: halfhourdayparts uked29i3cg4n4acdg569otn4r08; Type: CONSTRAINT; Schema: time; Owner: postgres
--

ALTER TABLE ONLY "time".halfhourdayparts
    ADD CONSTRAINT uked29i3cg4n4acdg569otn4r08 UNIQUE (hourid, minuteid);


--
-- TOC entry 4239 (class 2606 OID 29445)
-- Name: weeks weeks_pkey; Type: CONSTRAINT; Schema: time; Owner: postgres
--

ALTER TABLE ONLY "time".weeks
    ADD CONSTRAINT weeks_pkey PRIMARY KEY (weekid);


--
-- TOC entry 4241 (class 2606 OID 29450)
-- Name: years years_pkey; Type: CONSTRAINT; Schema: time; Owner: postgres
--

ALTER TABLE ONLY "time".years
    ADD CONSTRAINT years_pkey PRIMARY KEY (yearid);


--
-- TOC entry 4270 (class 2606 OID 29565)
-- Name: addressxclassification fk10gn5jpbgrhhp4e99c6i8p9ps; Type: FK CONSTRAINT; Schema: address; Owner: postgres
--

ALTER TABLE ONLY address.addressxclassification
    ADD CONSTRAINT fk10gn5jpbgrhhp4e99c6i8p9ps FOREIGN KEY (enterpriseid) REFERENCES dbo.enterprise(enterpriseid);


--
-- TOC entry 4286 (class 2606 OID 29645)
-- Name: addressxgeography fk1jym0y5b721wadwh42kt6jhl7; Type: FK CONSTRAINT; Schema: address; Owner: postgres
--

ALTER TABLE ONLY address.addressxgeography
    ADD CONSTRAINT fk1jym0y5b721wadwh42kt6jhl7 FOREIGN KEY (addressid) REFERENCES address.address(addressid);


--
-- TOC entry 4304 (class 2606 OID 29735)
-- Name: addressxresourceitemsecuritytoken fk3483wmiodjy481digc0kciwho; Type: FK CONSTRAINT; Schema: address; Owner: postgres
--

ALTER TABLE ONLY address.addressxresourceitemsecuritytoken
    ADD CONSTRAINT fk3483wmiodjy481digc0kciwho FOREIGN KEY (securitytokenid) REFERENCES security.securitytoken(securitytokenid);


--
-- TOC entry 4292 (class 2606 OID 29675)
-- Name: addressxgeographysecuritytoken fk44g7fv09en02ov1n5mtvwm9g0; Type: FK CONSTRAINT; Schema: address; Owner: postgres
--

ALTER TABLE ONLY address.addressxgeographysecuritytoken
    ADD CONSTRAINT fk44g7fv09en02ov1n5mtvwm9g0 FOREIGN KEY (systemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4298 (class 2606 OID 29705)
-- Name: addressxresourceitem fk4fdkipv2j9v5vo3ejwd5k2lkc; Type: FK CONSTRAINT; Schema: address; Owner: postgres
--

ALTER TABLE ONLY address.addressxresourceitem
    ADD CONSTRAINT fk4fdkipv2j9v5vo3ejwd5k2lkc FOREIGN KEY (classificationid) REFERENCES classification.classification(classificationid);


--
-- TOC entry 4264 (class 2606 OID 29535)
-- Name: addresssecuritytoken fk4iwbt1caylolasmb7919al3vo; Type: FK CONSTRAINT; Schema: address; Owner: postgres
--

ALTER TABLE ONLY address.addresssecuritytoken
    ADD CONSTRAINT fk4iwbt1caylolasmb7919al3vo FOREIGN KEY (enterpriseid) REFERENCES dbo.enterprise(enterpriseid);


--
-- TOC entry 4299 (class 2606 OID 29710)
-- Name: addressxresourceitem fk6gvqnudvt7tsnt1gxo59d96di; Type: FK CONSTRAINT; Schema: address; Owner: postgres
--

ALTER TABLE ONLY address.addressxresourceitem
    ADD CONSTRAINT fk6gvqnudvt7tsnt1gxo59d96di FOREIGN KEY (addressid) REFERENCES address.address(addressid);


--
-- TOC entry 4269 (class 2606 OID 29560)
-- Name: addressxclassification fk6lyv1kvg7y348bj3pmbionfkr; Type: FK CONSTRAINT; Schema: address; Owner: postgres
--

ALTER TABLE ONLY address.addressxclassification
    ADD CONSTRAINT fk6lyv1kvg7y348bj3pmbionfkr FOREIGN KEY (activeflagid) REFERENCES dbo.activeflag(activeflagid);


--
-- TOC entry 4297 (class 2606 OID 29700)
-- Name: addressxresourceitem fk8233bmyvh84df7grsv59kx99x; Type: FK CONSTRAINT; Schema: address; Owner: postgres
--

ALTER TABLE ONLY address.addressxresourceitem
    ADD CONSTRAINT fk8233bmyvh84df7grsv59kx99x FOREIGN KEY (originalsourcesystemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4275 (class 2606 OID 29590)
-- Name: addressxclassificationsecuritytoken fk8r4rrxr1r79me66xvgxk7rnrr; Type: FK CONSTRAINT; Schema: address; Owner: postgres
--

ALTER TABLE ONLY address.addressxclassificationsecuritytoken
    ADD CONSTRAINT fk8r4rrxr1r79me66xvgxk7rnrr FOREIGN KEY (activeflagid) REFERENCES dbo.activeflag(activeflagid);


--
-- TOC entry 4282 (class 2606 OID 29625)
-- Name: addressxgeography fk8tb5og3v55dnhqp8aw31nwwmt; Type: FK CONSTRAINT; Schema: address; Owner: postgres
--

ALTER TABLE ONLY address.addressxgeography
    ADD CONSTRAINT fk8tb5og3v55dnhqp8aw31nwwmt FOREIGN KEY (enterpriseid) REFERENCES dbo.enterprise(enterpriseid);


--
-- TOC entry 4303 (class 2606 OID 29730)
-- Name: addressxresourceitemsecuritytoken fk94ahf4mdoc5kymdb9kd3ck141; Type: FK CONSTRAINT; Schema: address; Owner: postgres
--

ALTER TABLE ONLY address.addressxresourceitemsecuritytoken
    ADD CONSTRAINT fk94ahf4mdoc5kymdb9kd3ck141 FOREIGN KEY (originalsourcesystemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4258 (class 2606 OID 29505)
-- Name: address fk9iyq6jfpe1xg4oaba71hnicr7; Type: FK CONSTRAINT; Schema: address; Owner: postgres
--

ALTER TABLE ONLY address.address
    ADD CONSTRAINT fk9iyq6jfpe1xg4oaba71hnicr7 FOREIGN KEY (activeflagid) REFERENCES dbo.activeflag(activeflagid);


--
-- TOC entry 4279 (class 2606 OID 29610)
-- Name: addressxclassificationsecuritytoken fkaoaax8wtnqlmuh9uh7oewam8w; Type: FK CONSTRAINT; Schema: address; Owner: postgres
--

ALTER TABLE ONLY address.addressxclassificationsecuritytoken
    ADD CONSTRAINT fkaoaax8wtnqlmuh9uh7oewam8w FOREIGN KEY (systemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4295 (class 2606 OID 29690)
-- Name: addressxresourceitem fkappieo5e929jb2sushbbrw3xh; Type: FK CONSTRAINT; Schema: address; Owner: postgres
--

ALTER TABLE ONLY address.addressxresourceitem
    ADD CONSTRAINT fkappieo5e929jb2sushbbrw3xh FOREIGN KEY (enterpriseid) REFERENCES dbo.enterprise(enterpriseid);


--
-- TOC entry 4280 (class 2606 OID 29615)
-- Name: addressxclassificationsecuritytoken fkbnb6j2jg2da9a096a6mdqugc2; Type: FK CONSTRAINT; Schema: address; Owner: postgres
--

ALTER TABLE ONLY address.addressxclassificationsecuritytoken
    ADD CONSTRAINT fkbnb6j2jg2da9a096a6mdqugc2 FOREIGN KEY (addressxclassificationid) REFERENCES address.addressxclassification(addressxclassificationid);


--
-- TOC entry 4288 (class 2606 OID 29655)
-- Name: addressxgeographysecuritytoken fkcp386daxldghx156aklcouc4y; Type: FK CONSTRAINT; Schema: address; Owner: postgres
--

ALTER TABLE ONLY address.addressxgeographysecuritytoken
    ADD CONSTRAINT fkcp386daxldghx156aklcouc4y FOREIGN KEY (activeflagid) REFERENCES dbo.activeflag(activeflagid);


--
-- TOC entry 4302 (class 2606 OID 29725)
-- Name: addressxresourceitemsecuritytoken fkds3id5ml3ium9v870rdxryoav; Type: FK CONSTRAINT; Schema: address; Owner: postgres
--

ALTER TABLE ONLY address.addressxresourceitemsecuritytoken
    ADD CONSTRAINT fkds3id5ml3ium9v870rdxryoav FOREIGN KEY (enterpriseid) REFERENCES dbo.enterprise(enterpriseid);


--
-- TOC entry 4296 (class 2606 OID 29695)
-- Name: addressxresourceitem fkeckhwk0f0a03h24a7we03pi2q; Type: FK CONSTRAINT; Schema: address; Owner: postgres
--

ALTER TABLE ONLY address.addressxresourceitem
    ADD CONSTRAINT fkeckhwk0f0a03h24a7we03pi2q FOREIGN KEY (systemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4259 (class 2606 OID 29510)
-- Name: address fkf5n0oi8hso8dpemjbb6733utp; Type: FK CONSTRAINT; Schema: address; Owner: postgres
--

ALTER TABLE ONLY address.address
    ADD CONSTRAINT fkf5n0oi8hso8dpemjbb6733utp FOREIGN KEY (enterpriseid) REFERENCES dbo.enterprise(enterpriseid);


--
-- TOC entry 4293 (class 2606 OID 29680)
-- Name: addressxgeographysecuritytoken fkfrqil82x8r1wpyn0ral52ypi3; Type: FK CONSTRAINT; Schema: address; Owner: postgres
--

ALTER TABLE ONLY address.addressxgeographysecuritytoken
    ADD CONSTRAINT fkfrqil82x8r1wpyn0ral52ypi3 FOREIGN KEY (addressxgeographyid) REFERENCES address.addressxgeography(addressxgeographyid);


--
-- TOC entry 4290 (class 2606 OID 29665)
-- Name: addressxgeographysecuritytoken fkgbtljbtwk7lsj92gxbx6su7dp; Type: FK CONSTRAINT; Schema: address; Owner: postgres
--

ALTER TABLE ONLY address.addressxgeographysecuritytoken
    ADD CONSTRAINT fkgbtljbtwk7lsj92gxbx6su7dp FOREIGN KEY (originalsourcesystemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4266 (class 2606 OID 29545)
-- Name: addresssecuritytoken fkgc74tjta2etx11rg02xqt293l; Type: FK CONSTRAINT; Schema: address; Owner: postgres
--

ALTER TABLE ONLY address.addresssecuritytoken
    ADD CONSTRAINT fkgc74tjta2etx11rg02xqt293l FOREIGN KEY (securitytokenid) REFERENCES security.securitytoken(securitytokenid);


--
-- TOC entry 4271 (class 2606 OID 29570)
-- Name: addressxclassification fkgelsst4chd2utcx5ed62or2h7; Type: FK CONSTRAINT; Schema: address; Owner: postgres
--

ALTER TABLE ONLY address.addressxclassification
    ADD CONSTRAINT fkgelsst4chd2utcx5ed62or2h7 FOREIGN KEY (systemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4287 (class 2606 OID 29650)
-- Name: addressxgeography fkgf8llc9jdhc8s0eatod3ja84o; Type: FK CONSTRAINT; Schema: address; Owner: postgres
--

ALTER TABLE ONLY address.addressxgeography
    ADD CONSTRAINT fkgf8llc9jdhc8s0eatod3ja84o FOREIGN KEY (geographyid) REFERENCES geography.geography(geographyid);


--
-- TOC entry 4260 (class 2606 OID 29525)
-- Name: address fkgh65u3w9ww52fhyerw4038tvf; Type: FK CONSTRAINT; Schema: address; Owner: postgres
--

ALTER TABLE ONLY address.address
    ADD CONSTRAINT fkgh65u3w9ww52fhyerw4038tvf FOREIGN KEY (classificationid) REFERENCES classification.classification(classificationid);


--
-- TOC entry 4291 (class 2606 OID 29670)
-- Name: addressxgeographysecuritytoken fkhp6f4qu8ywoe5aj507yc3gghn; Type: FK CONSTRAINT; Schema: address; Owner: postgres
--

ALTER TABLE ONLY address.addressxgeographysecuritytoken
    ADD CONSTRAINT fkhp6f4qu8ywoe5aj507yc3gghn FOREIGN KEY (securitytokenid) REFERENCES security.securitytoken(securitytokenid);


--
-- TOC entry 4276 (class 2606 OID 29595)
-- Name: addressxclassificationsecuritytoken fki37g7k1r7gjvpsmrglofsk4t5; Type: FK CONSTRAINT; Schema: address; Owner: postgres
--

ALTER TABLE ONLY address.addressxclassificationsecuritytoken
    ADD CONSTRAINT fki37g7k1r7gjvpsmrglofsk4t5 FOREIGN KEY (enterpriseid) REFERENCES dbo.enterprise(enterpriseid);


--
-- TOC entry 4263 (class 2606 OID 29530)
-- Name: addresssecuritytoken fki5s2v3sm6uuf3j8sbqbuxcfu6; Type: FK CONSTRAINT; Schema: address; Owner: postgres
--

ALTER TABLE ONLY address.addresssecuritytoken
    ADD CONSTRAINT fki5s2v3sm6uuf3j8sbqbuxcfu6 FOREIGN KEY (activeflagid) REFERENCES dbo.activeflag(activeflagid);


--
-- TOC entry 4305 (class 2606 OID 29740)
-- Name: addressxresourceitemsecuritytoken fkjdtgh57fk70hgg6owk878wdrv; Type: FK CONSTRAINT; Schema: address; Owner: postgres
--

ALTER TABLE ONLY address.addressxresourceitemsecuritytoken
    ADD CONSTRAINT fkjdtgh57fk70hgg6owk878wdrv FOREIGN KEY (systemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4294 (class 2606 OID 29685)
-- Name: addressxresourceitem fkm191nhp3l761p7sgm5r0nafnl; Type: FK CONSTRAINT; Schema: address; Owner: postgres
--

ALTER TABLE ONLY address.addressxresourceitem
    ADD CONSTRAINT fkm191nhp3l761p7sgm5r0nafnl FOREIGN KEY (activeflagid) REFERENCES dbo.activeflag(activeflagid);


--
-- TOC entry 4283 (class 2606 OID 29630)
-- Name: addressxgeography fkm2u3jmfqc5akhsq86tod7q6nc; Type: FK CONSTRAINT; Schema: address; Owner: postgres
--

ALTER TABLE ONLY address.addressxgeography
    ADD CONSTRAINT fkm2u3jmfqc5akhsq86tod7q6nc FOREIGN KEY (systemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4268 (class 2606 OID 29555)
-- Name: addresssecuritytoken fknc36gmq9xrt6k0cj6clmbh13p; Type: FK CONSTRAINT; Schema: address; Owner: postgres
--

ALTER TABLE ONLY address.addresssecuritytoken
    ADD CONSTRAINT fknc36gmq9xrt6k0cj6clmbh13p FOREIGN KEY (addressid) REFERENCES address.address(addressid);


--
-- TOC entry 4284 (class 2606 OID 29635)
-- Name: addressxgeography fknjongjciautlpcno0p44ekxn1; Type: FK CONSTRAINT; Schema: address; Owner: postgres
--

ALTER TABLE ONLY address.addressxgeography
    ADD CONSTRAINT fknjongjciautlpcno0p44ekxn1 FOREIGN KEY (originalsourcesystemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4273 (class 2606 OID 29580)
-- Name: addressxclassification fkobw11cghkuvasdoprpf8jeax5; Type: FK CONSTRAINT; Schema: address; Owner: postgres
--

ALTER TABLE ONLY address.addressxclassification
    ADD CONSTRAINT fkobw11cghkuvasdoprpf8jeax5 FOREIGN KEY (classificationid) REFERENCES classification.classification(classificationid);


--
-- TOC entry 4267 (class 2606 OID 29550)
-- Name: addresssecuritytoken fkog9cqhou7pe3e0cwc7uavblc0; Type: FK CONSTRAINT; Schema: address; Owner: postgres
--

ALTER TABLE ONLY address.addresssecuritytoken
    ADD CONSTRAINT fkog9cqhou7pe3e0cwc7uavblc0 FOREIGN KEY (systemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4261 (class 2606 OID 29520)
-- Name: address fkoj04ku2v02yibdyt7p9nuphyf; Type: FK CONSTRAINT; Schema: address; Owner: postgres
--

ALTER TABLE ONLY address.address
    ADD CONSTRAINT fkoj04ku2v02yibdyt7p9nuphyf FOREIGN KEY (originalsourcesystemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4285 (class 2606 OID 29640)
-- Name: addressxgeography fkoni218klw7gbcm5ksonbvvvld; Type: FK CONSTRAINT; Schema: address; Owner: postgres
--

ALTER TABLE ONLY address.addressxgeography
    ADD CONSTRAINT fkoni218klw7gbcm5ksonbvvvld FOREIGN KEY (classificationid) REFERENCES classification.classification(classificationid);


--
-- TOC entry 4278 (class 2606 OID 29605)
-- Name: addressxclassificationsecuritytoken fkoo05nqmk8a03k1nje9outisjd; Type: FK CONSTRAINT; Schema: address; Owner: postgres
--

ALTER TABLE ONLY address.addressxclassificationsecuritytoken
    ADD CONSTRAINT fkoo05nqmk8a03k1nje9outisjd FOREIGN KEY (securitytokenid) REFERENCES security.securitytoken(securitytokenid);


--
-- TOC entry 4281 (class 2606 OID 29620)
-- Name: addressxgeography fkpn4h36o5q70i13pbhluyugaxw; Type: FK CONSTRAINT; Schema: address; Owner: postgres
--

ALTER TABLE ONLY address.addressxgeography
    ADD CONSTRAINT fkpn4h36o5q70i13pbhluyugaxw FOREIGN KEY (activeflagid) REFERENCES dbo.activeflag(activeflagid);


--
-- TOC entry 4289 (class 2606 OID 29660)
-- Name: addressxgeographysecuritytoken fkq6uacmwuavt3j9bani8jg5k50; Type: FK CONSTRAINT; Schema: address; Owner: postgres
--

ALTER TABLE ONLY address.addressxgeographysecuritytoken
    ADD CONSTRAINT fkq6uacmwuavt3j9bani8jg5k50 FOREIGN KEY (enterpriseid) REFERENCES dbo.enterprise(enterpriseid);


--
-- TOC entry 4306 (class 2606 OID 29745)
-- Name: addressxresourceitemsecuritytoken fkqcrw559ebisww70vhco1yn1l3; Type: FK CONSTRAINT; Schema: address; Owner: postgres
--

ALTER TABLE ONLY address.addressxresourceitemsecuritytoken
    ADD CONSTRAINT fkqcrw559ebisww70vhco1yn1l3 FOREIGN KEY (addressxresourceitemid) REFERENCES address.addressxresourceitem(addressxresourceitemid);


--
-- TOC entry 4274 (class 2606 OID 29585)
-- Name: addressxclassification fkqemdst9c0gqhhpeqrixd4567g; Type: FK CONSTRAINT; Schema: address; Owner: postgres
--

ALTER TABLE ONLY address.addressxclassification
    ADD CONSTRAINT fkqemdst9c0gqhhpeqrixd4567g FOREIGN KEY (addressid) REFERENCES address.address(addressid);


--
-- TOC entry 4300 (class 2606 OID 29715)
-- Name: addressxresourceitem fks421flo2pfa9xgnh0kaaju64f; Type: FK CONSTRAINT; Schema: address; Owner: postgres
--

ALTER TABLE ONLY address.addressxresourceitem
    ADD CONSTRAINT fks421flo2pfa9xgnh0kaaju64f FOREIGN KEY (resourceitemid) REFERENCES resource.resourceitem(resourceitemid);


--
-- TOC entry 4272 (class 2606 OID 29575)
-- Name: addressxclassification fks747gghpsg2w01eb6xycbkem; Type: FK CONSTRAINT; Schema: address; Owner: postgres
--

ALTER TABLE ONLY address.addressxclassification
    ADD CONSTRAINT fks747gghpsg2w01eb6xycbkem FOREIGN KEY (originalsourcesystemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4265 (class 2606 OID 29540)
-- Name: addresssecuritytoken fks8ohxcn7hgiuyyyy7koaught4; Type: FK CONSTRAINT; Schema: address; Owner: postgres
--

ALTER TABLE ONLY address.addresssecuritytoken
    ADD CONSTRAINT fks8ohxcn7hgiuyyyy7koaught4 FOREIGN KEY (originalsourcesystemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4262 (class 2606 OID 29515)
-- Name: address fksx02yl44uidocj3ryaqom1urb; Type: FK CONSTRAINT; Schema: address; Owner: postgres
--

ALTER TABLE ONLY address.address
    ADD CONSTRAINT fksx02yl44uidocj3ryaqom1urb FOREIGN KEY (systemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4301 (class 2606 OID 29720)
-- Name: addressxresourceitemsecuritytoken fksx2i6hs70nkrw122gnxi1oljt; Type: FK CONSTRAINT; Schema: address; Owner: postgres
--

ALTER TABLE ONLY address.addressxresourceitemsecuritytoken
    ADD CONSTRAINT fksx2i6hs70nkrw122gnxi1oljt FOREIGN KEY (activeflagid) REFERENCES dbo.activeflag(activeflagid);


--
-- TOC entry 4277 (class 2606 OID 29600)
-- Name: addressxclassificationsecuritytoken fktnrs65dum0qh9qgh0mlyhkf27; Type: FK CONSTRAINT; Schema: address; Owner: postgres
--

ALTER TABLE ONLY address.addressxclassificationsecuritytoken
    ADD CONSTRAINT fktnrs65dum0qh9qgh0mlyhkf27 FOREIGN KEY (originalsourcesystemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4320 (class 2606 OID 29815)
-- Name: classificationdataconceptsecuritytoken fk104e6i5qwqmf9codleb3r0sq8; Type: FK CONSTRAINT; Schema: classification; Owner: postgres
--

ALTER TABLE ONLY classification.classificationdataconceptsecuritytoken
    ADD CONSTRAINT fk104e6i5qwqmf9codleb3r0sq8 FOREIGN KEY (systemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4328 (class 2606 OID 29855)
-- Name: classificationdataconceptxclassificationsecuritytoken fk11pvel7kmbcntj5cllihder1x; Type: FK CONSTRAINT; Schema: classification; Owner: postgres
--

ALTER TABLE ONLY classification.classificationdataconceptxclassificationsecuritytoken
    ADD CONSTRAINT fk11pvel7kmbcntj5cllihder1x FOREIGN KEY (activeflagid) REFERENCES dbo.activeflag(activeflagid);


--
-- TOC entry 4346 (class 2606 OID 29945)
-- Name: classificationdataconceptxresourceitemsecuritytoken fk18b70ps0o5g4y7eo5awrlls3v; Type: FK CONSTRAINT; Schema: classification; Owner: postgres
--

ALTER TABLE ONLY classification.classificationdataconceptxresourceitemsecuritytoken
    ADD CONSTRAINT fk18b70ps0o5g4y7eo5awrlls3v FOREIGN KEY (classificationdataconceptxresourceitemid) REFERENCES classification.classificationdataconceptxresourceitem(classificationdataconceptxresourceitemid);


--
-- TOC entry 4333 (class 2606 OID 29880)
-- Name: classificationdataconceptxclassificationsecuritytoken fk1x5i621v0xt7a60cvh4w88fu5; Type: FK CONSTRAINT; Schema: classification; Owner: postgres
--

ALTER TABLE ONLY classification.classificationdataconceptxclassificationsecuritytoken
    ADD CONSTRAINT fk1x5i621v0xt7a60cvh4w88fu5 FOREIGN KEY (classificationdataconceptxclassificationid) REFERENCES classification.classificationdataconceptxclassification(classificationdataconceptxclassificationid);


--
-- TOC entry 4372 (class 2606 OID 30075)
-- Name: classificationxresourceitemsecuritytoken fk202whr6s26bncotttl943yl80; Type: FK CONSTRAINT; Schema: classification; Owner: postgres
--

ALTER TABLE ONLY classification.classificationxresourceitemsecuritytoken
    ADD CONSTRAINT fk202whr6s26bncotttl943yl80 FOREIGN KEY (activeflagid) REFERENCES dbo.activeflag(activeflagid);


--
-- TOC entry 4335 (class 2606 OID 29890)
-- Name: classificationdataconceptxresourceitem fk26e3u9h3fcxpwbormhncoee4p; Type: FK CONSTRAINT; Schema: classification; Owner: postgres
--

ALTER TABLE ONLY classification.classificationdataconceptxresourceitem
    ADD CONSTRAINT fk26e3u9h3fcxpwbormhncoee4p FOREIGN KEY (enterpriseid) REFERENCES dbo.enterprise(enterpriseid);


--
-- TOC entry 4342 (class 2606 OID 29925)
-- Name: classificationdataconceptxresourceitemsecuritytoken fk31ifcku5xuo9j2f7nyvsheivg; Type: FK CONSTRAINT; Schema: classification; Owner: postgres
--

ALTER TABLE ONLY classification.classificationdataconceptxresourceitemsecuritytoken
    ADD CONSTRAINT fk31ifcku5xuo9j2f7nyvsheivg FOREIGN KEY (enterpriseid) REFERENCES dbo.enterprise(enterpriseid);


--
-- TOC entry 4365 (class 2606 OID 30040)
-- Name: classificationxclassificationsecuritytoken fk382so754rj84e0moqjj7q6cmd; Type: FK CONSTRAINT; Schema: classification; Owner: postgres
--

ALTER TABLE ONLY classification.classificationxclassificationsecuritytoken
    ADD CONSTRAINT fk382so754rj84e0moqjj7q6cmd FOREIGN KEY (classificationxclassificationid) REFERENCES classification.classificationxclassification(classificationxclassificationid);


--
-- TOC entry 4352 (class 2606 OID 29975)
-- Name: classificationsecuritytoken fk3pyb4fg9krm6cs8gyymn96d1b; Type: FK CONSTRAINT; Schema: classification; Owner: postgres
--

ALTER TABLE ONLY classification.classificationsecuritytoken
    ADD CONSTRAINT fk3pyb4fg9krm6cs8gyymn96d1b FOREIGN KEY (classificationid) REFERENCES classification.classification(classificationid);


--
-- TOC entry 4368 (class 2606 OID 30055)
-- Name: classificationxresourceitem fk41ad965y444vlu7ib0yr8qf96; Type: FK CONSTRAINT; Schema: classification; Owner: postgres
--

ALTER TABLE ONLY classification.classificationxresourceitem
    ADD CONSTRAINT fk41ad965y444vlu7ib0yr8qf96 FOREIGN KEY (systemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4327 (class 2606 OID 29850)
-- Name: classificationdataconceptxclassification fk4wh9c6d6snotj0cdns7wdojty; Type: FK CONSTRAINT; Schema: classification; Owner: postgres
--

ALTER TABLE ONLY classification.classificationdataconceptxclassification
    ADD CONSTRAINT fk4wh9c6d6snotj0cdns7wdojty FOREIGN KEY (classificationdataconceptid) REFERENCES classification.classificationdataconcept(classificationdataconceptid);


--
-- TOC entry 4355 (class 2606 OID 29990)
-- Name: classificationxclassification fk5lxvsymahaue1a1gx87ll8w9t; Type: FK CONSTRAINT; Schema: classification; Owner: postgres
--

ALTER TABLE ONLY classification.classificationxclassification
    ADD CONSTRAINT fk5lxvsymahaue1a1gx87ll8w9t FOREIGN KEY (systemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4329 (class 2606 OID 29860)
-- Name: classificationdataconceptxclassificationsecuritytoken fk60x6yebwf17dn491shf6aobv2; Type: FK CONSTRAINT; Schema: classification; Owner: postgres
--

ALTER TABLE ONLY classification.classificationdataconceptxclassificationsecuritytoken
    ADD CONSTRAINT fk60x6yebwf17dn491shf6aobv2 FOREIGN KEY (enterpriseid) REFERENCES dbo.enterprise(enterpriseid);


--
-- TOC entry 4322 (class 2606 OID 29825)
-- Name: classificationdataconceptxclassification fk6p7cqsuyxor559t3sfbxvsrsh; Type: FK CONSTRAINT; Schema: classification; Owner: postgres
--

ALTER TABLE ONLY classification.classificationdataconceptxclassification
    ADD CONSTRAINT fk6p7cqsuyxor559t3sfbxvsrsh FOREIGN KEY (activeflagid) REFERENCES dbo.activeflag(activeflagid);


--
-- TOC entry 4336 (class 2606 OID 29895)
-- Name: classificationdataconceptxresourceitem fk70uhig9qru3lmqnjq9yfgsddn; Type: FK CONSTRAINT; Schema: classification; Owner: postgres
--

ALTER TABLE ONLY classification.classificationdataconceptxresourceitem
    ADD CONSTRAINT fk70uhig9qru3lmqnjq9yfgsddn FOREIGN KEY (systemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4312 (class 2606 OID 29785)
-- Name: classificationdataconcept fk7anrd6m4u7jmgosom1ov3sup6; Type: FK CONSTRAINT; Schema: classification; Owner: postgres
--

ALTER TABLE ONLY classification.classificationdataconcept
    ADD CONSTRAINT fk7anrd6m4u7jmgosom1ov3sup6 FOREIGN KEY (systemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4340 (class 2606 OID 29915)
-- Name: classificationdataconceptxresourceitem fk7cngjs2bvw18p1j29fg62svn3; Type: FK CONSTRAINT; Schema: classification; Owner: postgres
--

ALTER TABLE ONLY classification.classificationdataconceptxresourceitem
    ADD CONSTRAINT fk7cngjs2bvw18p1j29fg62svn3 FOREIGN KEY (resourceitemid) REFERENCES resource.resourceitem(resourceitemid);


--
-- TOC entry 4331 (class 2606 OID 29870)
-- Name: classificationdataconceptxclassificationsecuritytoken fk86t8j5lcn4fxi8km9e839mn76; Type: FK CONSTRAINT; Schema: classification; Owner: postgres
--

ALTER TABLE ONLY classification.classificationdataconceptxclassificationsecuritytoken
    ADD CONSTRAINT fk86t8j5lcn4fxi8km9e839mn76 FOREIGN KEY (securitytokenid) REFERENCES security.securitytoken(securitytokenid);


--
-- TOC entry 4307 (class 2606 OID 29750)
-- Name: classification fk8cywgvl24kx13sapgd5ppnxgn; Type: FK CONSTRAINT; Schema: classification; Owner: postgres
--

ALTER TABLE ONLY classification.classification
    ADD CONSTRAINT fk8cywgvl24kx13sapgd5ppnxgn FOREIGN KEY (activeflagid) REFERENCES dbo.activeflag(activeflagid);


--
-- TOC entry 4323 (class 2606 OID 29830)
-- Name: classificationdataconceptxclassification fk8ri5ojfslqput3614qfxh89ko; Type: FK CONSTRAINT; Schema: classification; Owner: postgres
--

ALTER TABLE ONLY classification.classificationdataconceptxclassification
    ADD CONSTRAINT fk8ri5ojfslqput3614qfxh89ko FOREIGN KEY (enterpriseid) REFERENCES dbo.enterprise(enterpriseid);


--
-- TOC entry 4308 (class 2606 OID 29765)
-- Name: classification fk977a6joatob09p0ati780wx3i; Type: FK CONSTRAINT; Schema: classification; Owner: postgres
--

ALTER TABLE ONLY classification.classification
    ADD CONSTRAINT fk977a6joatob09p0ati780wx3i FOREIGN KEY (originalsourcesystemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4357 (class 2606 OID 30000)
-- Name: classificationxclassification fka0edqa9iht684osp3290grh5j; Type: FK CONSTRAINT; Schema: classification; Owner: postgres
--

ALTER TABLE ONLY classification.classificationxclassification
    ADD CONSTRAINT fka0edqa9iht684osp3290grh5j FOREIGN KEY (classificationid) REFERENCES classification.classification(classificationid);


--
-- TOC entry 4316 (class 2606 OID 29795)
-- Name: classificationdataconceptsecuritytoken fka96umbw2pt2xsr7dpr7rg59sb; Type: FK CONSTRAINT; Schema: classification; Owner: postgres
--

ALTER TABLE ONLY classification.classificationdataconceptsecuritytoken
    ADD CONSTRAINT fka96umbw2pt2xsr7dpr7rg59sb FOREIGN KEY (activeflagid) REFERENCES dbo.activeflag(activeflagid);


--
-- TOC entry 4321 (class 2606 OID 29820)
-- Name: classificationdataconceptsecuritytoken fkaet7sbnxoa25lvh4fttjxq241; Type: FK CONSTRAINT; Schema: classification; Owner: postgres
--

ALTER TABLE ONLY classification.classificationdataconceptsecuritytoken
    ADD CONSTRAINT fkaet7sbnxoa25lvh4fttjxq241 FOREIGN KEY (classificationdataconceptid) REFERENCES classification.classificationdataconcept(classificationdataconceptid);


--
-- TOC entry 4324 (class 2606 OID 29835)
-- Name: classificationdataconceptxclassification fkalb9m4tw81jr1nrghxwnhgbsm; Type: FK CONSTRAINT; Schema: classification; Owner: postgres
--

ALTER TABLE ONLY classification.classificationdataconceptxclassification
    ADD CONSTRAINT fkalb9m4tw81jr1nrghxwnhgbsm FOREIGN KEY (systemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4349 (class 2606 OID 29960)
-- Name: classificationsecuritytoken fkalw5xpd22jfxd5le05m2baego; Type: FK CONSTRAINT; Schema: classification; Owner: postgres
--

ALTER TABLE ONLY classification.classificationsecuritytoken
    ADD CONSTRAINT fkalw5xpd22jfxd5le05m2baego FOREIGN KEY (originalsourcesystemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4351 (class 2606 OID 29970)
-- Name: classificationsecuritytoken fkarnkwlgj9y3433pngmhg39j5f; Type: FK CONSTRAINT; Schema: classification; Owner: postgres
--

ALTER TABLE ONLY classification.classificationsecuritytoken
    ADD CONSTRAINT fkarnkwlgj9y3433pngmhg39j5f FOREIGN KEY (systemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4339 (class 2606 OID 29910)
-- Name: classificationdataconceptxresourceitem fkb3i6n3mk43lmc47412g00l07p; Type: FK CONSTRAINT; Schema: classification; Owner: postgres
--

ALTER TABLE ONLY classification.classificationdataconceptxresourceitem
    ADD CONSTRAINT fkb3i6n3mk43lmc47412g00l07p FOREIGN KEY (classificationdataconceptid) REFERENCES classification.classificationdataconcept(classificationdataconceptid);


--
-- TOC entry 4326 (class 2606 OID 29845)
-- Name: classificationdataconceptxclassification fkbkxw52ao5d3w2lww1wcgwc6r8; Type: FK CONSTRAINT; Schema: classification; Owner: postgres
--

ALTER TABLE ONLY classification.classificationdataconceptxclassification
    ADD CONSTRAINT fkbkxw52ao5d3w2lww1wcgwc6r8 FOREIGN KEY (classificationid) REFERENCES classification.classification(classificationid);


--
-- TOC entry 4359 (class 2606 OID 30010)
-- Name: classificationxclassification fkbutwoudrgvo3cbjn39cd3ftn1; Type: FK CONSTRAINT; Schema: classification; Owner: postgres
--

ALTER TABLE ONLY classification.classificationxclassification
    ADD CONSTRAINT fkbutwoudrgvo3cbjn39cd3ftn1 FOREIGN KEY (parentclassificationid) REFERENCES classification.classification(classificationid);


--
-- TOC entry 4338 (class 2606 OID 29905)
-- Name: classificationdataconceptxresourceitem fkbxuns49b2h4ymjifep6bup0y7; Type: FK CONSTRAINT; Schema: classification; Owner: postgres
--

ALTER TABLE ONLY classification.classificationdataconceptxresourceitem
    ADD CONSTRAINT fkbxuns49b2h4ymjifep6bup0y7 FOREIGN KEY (classificationid) REFERENCES classification.classification(classificationid);


--
-- TOC entry 4309 (class 2606 OID 29760)
-- Name: classification fkdameu9rwq0ndi6g9pnasalwf2; Type: FK CONSTRAINT; Schema: classification; Owner: postgres
--

ALTER TABLE ONLY classification.classification
    ADD CONSTRAINT fkdameu9rwq0ndi6g9pnasalwf2 FOREIGN KEY (systemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4318 (class 2606 OID 29805)
-- Name: classificationdataconceptsecuritytoken fkdj2s9qftq2ybskhst5rh7kvcs; Type: FK CONSTRAINT; Schema: classification; Owner: postgres
--

ALTER TABLE ONLY classification.classificationdataconceptsecuritytoken
    ADD CONSTRAINT fkdj2s9qftq2ybskhst5rh7kvcs FOREIGN KEY (originalsourcesystemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4367 (class 2606 OID 30050)
-- Name: classificationxresourceitem fkg9sk8egvr7m48ajxuq08kr36a; Type: FK CONSTRAINT; Schema: classification; Owner: postgres
--

ALTER TABLE ONLY classification.classificationxresourceitem
    ADD CONSTRAINT fkg9sk8egvr7m48ajxuq08kr36a FOREIGN KEY (enterpriseid) REFERENCES dbo.enterprise(enterpriseid);


--
-- TOC entry 4332 (class 2606 OID 29875)
-- Name: classificationdataconceptxclassificationsecuritytoken fkglkin1i2uv5gp9dlk1sbgmulg; Type: FK CONSTRAINT; Schema: classification; Owner: postgres
--

ALTER TABLE ONLY classification.classificationdataconceptxclassificationsecuritytoken
    ADD CONSTRAINT fkglkin1i2uv5gp9dlk1sbgmulg FOREIGN KEY (systemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4370 (class 2606 OID 30065)
-- Name: classificationxresourceitem fkgm9umwfpdxs1ao0jb2ye8t4qi; Type: FK CONSTRAINT; Schema: classification; Owner: postgres
--

ALTER TABLE ONLY classification.classificationxresourceitem
    ADD CONSTRAINT fkgm9umwfpdxs1ao0jb2ye8t4qi FOREIGN KEY (classificationid) REFERENCES classification.classification(classificationid);


--
-- TOC entry 4353 (class 2606 OID 29980)
-- Name: classificationxclassification fkgt5llwl8oh4q2fo4fv9vfmui2; Type: FK CONSTRAINT; Schema: classification; Owner: postgres
--

ALTER TABLE ONLY classification.classificationxclassification
    ADD CONSTRAINT fkgt5llwl8oh4q2fo4fv9vfmui2 FOREIGN KEY (activeflagid) REFERENCES dbo.activeflag(activeflagid);


--
-- TOC entry 4360 (class 2606 OID 30015)
-- Name: classificationxclassificationsecuritytoken fkidlqquwljda1kte0kjjjkkopi; Type: FK CONSTRAINT; Schema: classification; Owner: postgres
--

ALTER TABLE ONLY classification.classificationxclassificationsecuritytoken
    ADD CONSTRAINT fkidlqquwljda1kte0kjjjkkopi FOREIGN KEY (activeflagid) REFERENCES dbo.activeflag(activeflagid);


--
-- TOC entry 4366 (class 2606 OID 30045)
-- Name: classificationxresourceitem fkisdanawdo1rw8v7t1jks92pgf; Type: FK CONSTRAINT; Schema: classification; Owner: postgres
--

ALTER TABLE ONLY classification.classificationxresourceitem
    ADD CONSTRAINT fkisdanawdo1rw8v7t1jks92pgf FOREIGN KEY (activeflagid) REFERENCES dbo.activeflag(activeflagid);


--
-- TOC entry 4313 (class 2606 OID 29780)
-- Name: classificationdataconcept fkjjkh1tgw58ebgjyfhs55uq0bc; Type: FK CONSTRAINT; Schema: classification; Owner: postgres
--

ALTER TABLE ONLY classification.classificationdataconcept
    ADD CONSTRAINT fkjjkh1tgw58ebgjyfhs55uq0bc FOREIGN KEY (enterpriseid) REFERENCES dbo.enterprise(enterpriseid);


--
-- TOC entry 4343 (class 2606 OID 29930)
-- Name: classificationdataconceptxresourceitemsecuritytoken fkk0lwglw1ekrh07d1rp0qnt3v9; Type: FK CONSTRAINT; Schema: classification; Owner: postgres
--

ALTER TABLE ONLY classification.classificationdataconceptxresourceitemsecuritytoken
    ADD CONSTRAINT fkk0lwglw1ekrh07d1rp0qnt3v9 FOREIGN KEY (originalsourcesystemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4334 (class 2606 OID 29885)
-- Name: classificationdataconceptxresourceitem fkk1tllh3pw17bo91f98qajwv18; Type: FK CONSTRAINT; Schema: classification; Owner: postgres
--

ALTER TABLE ONLY classification.classificationdataconceptxresourceitem
    ADD CONSTRAINT fkk1tllh3pw17bo91f98qajwv18 FOREIGN KEY (activeflagid) REFERENCES dbo.activeflag(activeflagid);


--
-- TOC entry 4361 (class 2606 OID 30020)
-- Name: classificationxclassificationsecuritytoken fkktsowuwqxuk0snthieryx7pny; Type: FK CONSTRAINT; Schema: classification; Owner: postgres
--

ALTER TABLE ONLY classification.classificationxclassificationsecuritytoken
    ADD CONSTRAINT fkktsowuwqxuk0snthieryx7pny FOREIGN KEY (enterpriseid) REFERENCES dbo.enterprise(enterpriseid);


--
-- TOC entry 4371 (class 2606 OID 30070)
-- Name: classificationxresourceitem fklhxbcwpk3r33hdnpbirj5q8e0; Type: FK CONSTRAINT; Schema: classification; Owner: postgres
--

ALTER TABLE ONLY classification.classificationxresourceitem
    ADD CONSTRAINT fklhxbcwpk3r33hdnpbirj5q8e0 FOREIGN KEY (resourceitemid) REFERENCES resource.resourceitem(resourceitemid);


--
-- TOC entry 4358 (class 2606 OID 30005)
-- Name: classificationxclassification fklolls1qwf58fhw1lo6plg1u2d; Type: FK CONSTRAINT; Schema: classification; Owner: postgres
--

ALTER TABLE ONLY classification.classificationxclassification
    ADD CONSTRAINT fklolls1qwf58fhw1lo6plg1u2d FOREIGN KEY (childclassificationid) REFERENCES classification.classification(classificationid);


--
-- TOC entry 4337 (class 2606 OID 29900)
-- Name: classificationdataconceptxresourceitem fkm5et8roankenbjliy9qigclab; Type: FK CONSTRAINT; Schema: classification; Owner: postgres
--

ALTER TABLE ONLY classification.classificationdataconceptxresourceitem
    ADD CONSTRAINT fkm5et8roankenbjliy9qigclab FOREIGN KEY (originalsourcesystemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4354 (class 2606 OID 29985)
-- Name: classificationxclassification fkmdyinn1kd9lrjajlts2832kgp; Type: FK CONSTRAINT; Schema: classification; Owner: postgres
--

ALTER TABLE ONLY classification.classificationxclassification
    ADD CONSTRAINT fkmdyinn1kd9lrjajlts2832kgp FOREIGN KEY (enterpriseid) REFERENCES dbo.enterprise(enterpriseid);


--
-- TOC entry 4377 (class 2606 OID 30100)
-- Name: classificationxresourceitemsecuritytoken fkmxlv95qskrx8l20xdjs55gxwy; Type: FK CONSTRAINT; Schema: classification; Owner: postgres
--

ALTER TABLE ONLY classification.classificationxresourceitemsecuritytoken
    ADD CONSTRAINT fkmxlv95qskrx8l20xdjs55gxwy FOREIGN KEY (classificationxresourceitemid) REFERENCES classification.classificationxresourceitem(classificationxresourceitemid);


--
-- TOC entry 4356 (class 2606 OID 29995)
-- Name: classificationxclassification fkng5lu9wyocw42hm2yq6966eic; Type: FK CONSTRAINT; Schema: classification; Owner: postgres
--

ALTER TABLE ONLY classification.classificationxclassification
    ADD CONSTRAINT fkng5lu9wyocw42hm2yq6966eic FOREIGN KEY (originalsourcesystemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4314 (class 2606 OID 29790)
-- Name: classificationdataconcept fknq22rqtgtemjf9w2iwanl89im; Type: FK CONSTRAINT; Schema: classification; Owner: postgres
--

ALTER TABLE ONLY classification.classificationdataconcept
    ADD CONSTRAINT fknq22rqtgtemjf9w2iwanl89im FOREIGN KEY (originalsourcesystemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4376 (class 2606 OID 30095)
-- Name: classificationxresourceitemsecuritytoken fknudqvddewc67sunrqr4wnh0aq; Type: FK CONSTRAINT; Schema: classification; Owner: postgres
--

ALTER TABLE ONLY classification.classificationxresourceitemsecuritytoken
    ADD CONSTRAINT fknudqvddewc67sunrqr4wnh0aq FOREIGN KEY (systemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4317 (class 2606 OID 29800)
-- Name: classificationdataconceptsecuritytoken fko5u4q6stb4e4gp0hfwttgpoof; Type: FK CONSTRAINT; Schema: classification; Owner: postgres
--

ALTER TABLE ONLY classification.classificationdataconceptsecuritytoken
    ADD CONSTRAINT fko5u4q6stb4e4gp0hfwttgpoof FOREIGN KEY (enterpriseid) REFERENCES dbo.enterprise(enterpriseid);


--
-- TOC entry 4344 (class 2606 OID 29935)
-- Name: classificationdataconceptxresourceitemsecuritytoken fkohnmet2w8qiqs1tqgp3bvaj8s; Type: FK CONSTRAINT; Schema: classification; Owner: postgres
--

ALTER TABLE ONLY classification.classificationdataconceptxresourceitemsecuritytoken
    ADD CONSTRAINT fkohnmet2w8qiqs1tqgp3bvaj8s FOREIGN KEY (securitytokenid) REFERENCES security.securitytoken(securitytokenid);


--
-- TOC entry 4362 (class 2606 OID 30025)
-- Name: classificationxclassificationsecuritytoken fkovne1f69w7lejinl9o202wq8r; Type: FK CONSTRAINT; Schema: classification; Owner: postgres
--

ALTER TABLE ONLY classification.classificationxclassificationsecuritytoken
    ADD CONSTRAINT fkovne1f69w7lejinl9o202wq8r FOREIGN KEY (originalsourcesystemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4364 (class 2606 OID 30035)
-- Name: classificationxclassificationsecuritytoken fkp0uayhfmnxp6mbojs6b906uo1; Type: FK CONSTRAINT; Schema: classification; Owner: postgres
--

ALTER TABLE ONLY classification.classificationxclassificationsecuritytoken
    ADD CONSTRAINT fkp0uayhfmnxp6mbojs6b906uo1 FOREIGN KEY (systemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4350 (class 2606 OID 29965)
-- Name: classificationsecuritytoken fkpgoqkscc2c9ynucppx7kqfq17; Type: FK CONSTRAINT; Schema: classification; Owner: postgres
--

ALTER TABLE ONLY classification.classificationsecuritytoken
    ADD CONSTRAINT fkpgoqkscc2c9ynucppx7kqfq17 FOREIGN KEY (securitytokenid) REFERENCES security.securitytoken(securitytokenid);


--
-- TOC entry 4375 (class 2606 OID 30090)
-- Name: classificationxresourceitemsecuritytoken fkpkswoxe9sd4fcqdc735yo80kc; Type: FK CONSTRAINT; Schema: classification; Owner: postgres
--

ALTER TABLE ONLY classification.classificationxresourceitemsecuritytoken
    ADD CONSTRAINT fkpkswoxe9sd4fcqdc735yo80kc FOREIGN KEY (securitytokenid) REFERENCES security.securitytoken(securitytokenid);


--
-- TOC entry 4325 (class 2606 OID 29840)
-- Name: classificationdataconceptxclassification fkptvfaytvks9cf4qvb1ch0nc5q; Type: FK CONSTRAINT; Schema: classification; Owner: postgres
--

ALTER TABLE ONLY classification.classificationdataconceptxclassification
    ADD CONSTRAINT fkptvfaytvks9cf4qvb1ch0nc5q FOREIGN KEY (originalsourcesystemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4369 (class 2606 OID 30060)
-- Name: classificationxresourceitem fkq78r34v2adpvnc8u50fdo2mth; Type: FK CONSTRAINT; Schema: classification; Owner: postgres
--

ALTER TABLE ONLY classification.classificationxresourceitem
    ADD CONSTRAINT fkq78r34v2adpvnc8u50fdo2mth FOREIGN KEY (originalsourcesystemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4315 (class 2606 OID 29775)
-- Name: classificationdataconcept fkq95ppsctlqk23bbukl11jfrk9; Type: FK CONSTRAINT; Schema: classification; Owner: postgres
--

ALTER TABLE ONLY classification.classificationdataconcept
    ADD CONSTRAINT fkq95ppsctlqk23bbukl11jfrk9 FOREIGN KEY (activeflagid) REFERENCES dbo.activeflag(activeflagid);


--
-- TOC entry 4330 (class 2606 OID 29865)
-- Name: classificationdataconceptxclassificationsecuritytoken fkqd86yl64ly9efrkoqoo5g6d3a; Type: FK CONSTRAINT; Schema: classification; Owner: postgres
--

ALTER TABLE ONLY classification.classificationdataconceptxclassificationsecuritytoken
    ADD CONSTRAINT fkqd86yl64ly9efrkoqoo5g6d3a FOREIGN KEY (originalsourcesystemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4373 (class 2606 OID 30080)
-- Name: classificationxresourceitemsecuritytoken fkqof2ui2s3pfau54hqtylrpbcf; Type: FK CONSTRAINT; Schema: classification; Owner: postgres
--

ALTER TABLE ONLY classification.classificationxresourceitemsecuritytoken
    ADD CONSTRAINT fkqof2ui2s3pfau54hqtylrpbcf FOREIGN KEY (enterpriseid) REFERENCES dbo.enterprise(enterpriseid);


--
-- TOC entry 4310 (class 2606 OID 29755)
-- Name: classification fkradegxgr2qgtrmpcnr280wrow; Type: FK CONSTRAINT; Schema: classification; Owner: postgres
--

ALTER TABLE ONLY classification.classification
    ADD CONSTRAINT fkradegxgr2qgtrmpcnr280wrow FOREIGN KEY (enterpriseid) REFERENCES dbo.enterprise(enterpriseid);


--
-- TOC entry 4345 (class 2606 OID 29940)
-- Name: classificationdataconceptxresourceitemsecuritytoken fkrnilsvgvwpcbv8fs2yr1p87ei; Type: FK CONSTRAINT; Schema: classification; Owner: postgres
--

ALTER TABLE ONLY classification.classificationdataconceptxresourceitemsecuritytoken
    ADD CONSTRAINT fkrnilsvgvwpcbv8fs2yr1p87ei FOREIGN KEY (systemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4341 (class 2606 OID 29920)
-- Name: classificationdataconceptxresourceitemsecuritytoken fkrq8gtmddaq83y0vq5v5mvun3s; Type: FK CONSTRAINT; Schema: classification; Owner: postgres
--

ALTER TABLE ONLY classification.classificationdataconceptxresourceitemsecuritytoken
    ADD CONSTRAINT fkrq8gtmddaq83y0vq5v5mvun3s FOREIGN KEY (activeflagid) REFERENCES dbo.activeflag(activeflagid);


--
-- TOC entry 4319 (class 2606 OID 29810)
-- Name: classificationdataconceptsecuritytoken fks2ygkvtk17e901wed7fjjio7v; Type: FK CONSTRAINT; Schema: classification; Owner: postgres
--

ALTER TABLE ONLY classification.classificationdataconceptsecuritytoken
    ADD CONSTRAINT fks2ygkvtk17e901wed7fjjio7v FOREIGN KEY (securitytokenid) REFERENCES security.securitytoken(securitytokenid);


--
-- TOC entry 4374 (class 2606 OID 30085)
-- Name: classificationxresourceitemsecuritytoken fkt2qwo9050jpmg0k59is420r1h; Type: FK CONSTRAINT; Schema: classification; Owner: postgres
--

ALTER TABLE ONLY classification.classificationxresourceitemsecuritytoken
    ADD CONSTRAINT fkt2qwo9050jpmg0k59is420r1h FOREIGN KEY (originalsourcesystemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4348 (class 2606 OID 29955)
-- Name: classificationsecuritytoken fktb0dfsliwpcbdw26ivstchoxa; Type: FK CONSTRAINT; Schema: classification; Owner: postgres
--

ALTER TABLE ONLY classification.classificationsecuritytoken
    ADD CONSTRAINT fktb0dfsliwpcbdw26ivstchoxa FOREIGN KEY (enterpriseid) REFERENCES dbo.enterprise(enterpriseid);


--
-- TOC entry 4363 (class 2606 OID 30030)
-- Name: classificationxclassificationsecuritytoken fktgfix5de1n30m7rvhxrb1yel2; Type: FK CONSTRAINT; Schema: classification; Owner: postgres
--

ALTER TABLE ONLY classification.classificationxclassificationsecuritytoken
    ADD CONSTRAINT fktgfix5de1n30m7rvhxrb1yel2 FOREIGN KEY (securitytokenid) REFERENCES security.securitytoken(securitytokenid);


--
-- TOC entry 4311 (class 2606 OID 29770)
-- Name: classification fkuyay717baco8bei1qoxcauux; Type: FK CONSTRAINT; Schema: classification; Owner: postgres
--

ALTER TABLE ONLY classification.classification
    ADD CONSTRAINT fkuyay717baco8bei1qoxcauux FOREIGN KEY (classificationdataconceptid) REFERENCES classification.classificationdataconcept(classificationdataconceptid);


--
-- TOC entry 4347 (class 2606 OID 29950)
-- Name: classificationsecuritytoken fkxo8jweipcwyhjky1sj6asi70; Type: FK CONSTRAINT; Schema: classification; Owner: postgres
--

ALTER TABLE ONLY classification.classificationsecuritytoken
    ADD CONSTRAINT fkxo8jweipcwyhjky1sj6asi70 FOREIGN KEY (activeflagid) REFERENCES dbo.activeflag(activeflagid);


--
-- TOC entry 4383 (class 2606 OID 30130)
-- Name: activeflagsecuritytoken fk20yjws7ewoq2cqkx3ypki36rx; Type: FK CONSTRAINT; Schema: dbo; Owner: postgres
--

ALTER TABLE ONLY dbo.activeflagsecuritytoken
    ADD CONSTRAINT fk20yjws7ewoq2cqkx3ypki36rx FOREIGN KEY (systemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4396 (class 2606 OID 30215)
-- Name: enterprisesecuritytoken fk3604iuvyi4psepv3rdn69jf76; Type: FK CONSTRAINT; Schema: dbo; Owner: postgres
--

ALTER TABLE ONLY dbo.enterprisesecuritytoken
    ADD CONSTRAINT fk3604iuvyi4psepv3rdn69jf76 FOREIGN KEY (systemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4393 (class 2606 OID 30180)
-- Name: activeflagxclassificationsecuritytoken fk415n0jfxdeuva0evd9mvlooaa; Type: FK CONSTRAINT; Schema: dbo; Owner: postgres
--

ALTER TABLE ONLY dbo.activeflagxclassificationsecuritytoken
    ADD CONSTRAINT fk415n0jfxdeuva0evd9mvlooaa FOREIGN KEY (securitytokenid) REFERENCES security.securitytoken(securitytokenid);


--
-- TOC entry 4414 (class 2606 OID 30290)
-- Name: systemssecuritytoken fk569rtrkku5kg7sponne0xwgkf; Type: FK CONSTRAINT; Schema: dbo; Owner: postgres
--

ALTER TABLE ONLY dbo.systemssecuritytoken
    ADD CONSTRAINT fk569rtrkku5kg7sponne0xwgkf FOREIGN KEY (enterpriseid) REFERENCES dbo.enterprise(enterpriseid);


--
-- TOC entry 4409 (class 2606 OID 30260)
-- Name: enterprisexclassificationsecuritytoken fk5l36jvcvedgw6gaa8301xjr62; Type: FK CONSTRAINT; Schema: dbo; Owner: postgres
--

ALTER TABLE ONLY dbo.enterprisexclassificationsecuritytoken
    ADD CONSTRAINT fk5l36jvcvedgw6gaa8301xjr62 FOREIGN KEY (securitytokenid) REFERENCES security.securitytoken(securitytokenid);


--
-- TOC entry 4381 (class 2606 OID 30120)
-- Name: activeflagsecuritytoken fk5upfxgyprbf9blrljkanx6dq1; Type: FK CONSTRAINT; Schema: dbo; Owner: postgres
--

ALTER TABLE ONLY dbo.activeflagsecuritytoken
    ADD CONSTRAINT fk5upfxgyprbf9blrljkanx6dq1 FOREIGN KEY (originalsourcesystemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4419 (class 2606 OID 30315)
-- Name: systemxclassification fk6ejo5y1g32jdsi6nh5wufvdg9; Type: FK CONSTRAINT; Schema: dbo; Owner: postgres
--

ALTER TABLE ONLY dbo.systemxclassification
    ADD CONSTRAINT fk6ejo5y1g32jdsi6nh5wufvdg9 FOREIGN KEY (enterpriseid) REFERENCES dbo.enterprise(enterpriseid);


--
-- TOC entry 4394 (class 2606 OID 30185)
-- Name: activeflagxclassificationsecuritytoken fk6lfmv9ervio1pg5wiowy4hvve; Type: FK CONSTRAINT; Schema: dbo; Owner: postgres
--

ALTER TABLE ONLY dbo.activeflagxclassificationsecuritytoken
    ADD CONSTRAINT fk6lfmv9ervio1pg5wiowy4hvve FOREIGN KEY (systemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4406 (class 2606 OID 30245)
-- Name: enterprisexclassificationsecuritytoken fk75tx4r7sp36qtylr8r888gbkr; Type: FK CONSTRAINT; Schema: dbo; Owner: postgres
--

ALTER TABLE ONLY dbo.enterprisexclassificationsecuritytoken
    ADD CONSTRAINT fk75tx4r7sp36qtylr8r888gbkr FOREIGN KEY (activeflagid) REFERENCES dbo.activeflag(activeflagid);


--
-- TOC entry 4415 (class 2606 OID 30285)
-- Name: systemssecuritytoken fk78ntmjytf0l9egbfp8w5quysa; Type: FK CONSTRAINT; Schema: dbo; Owner: postgres
--

ALTER TABLE ONLY dbo.systemssecuritytoken
    ADD CONSTRAINT fk78ntmjytf0l9egbfp8w5quysa FOREIGN KEY (activeflagid) REFERENCES dbo.activeflag(activeflagid);


--
-- TOC entry 4401 (class 2606 OID 30225)
-- Name: enterprisexclassification fk7dcbw0medved7abfmh1y0btj; Type: FK CONSTRAINT; Schema: dbo; Owner: postgres
--

ALTER TABLE ONLY dbo.enterprisexclassification
    ADD CONSTRAINT fk7dcbw0medved7abfmh1y0btj FOREIGN KEY (enterpriseid) REFERENCES dbo.enterprise(enterpriseid);


--
-- TOC entry 4407 (class 2606 OID 30250)
-- Name: enterprisexclassificationsecuritytoken fk7jfxn7qp93vt98lob9q7g5exc; Type: FK CONSTRAINT; Schema: dbo; Owner: postgres
--

ALTER TABLE ONLY dbo.enterprisexclassificationsecuritytoken
    ADD CONSTRAINT fk7jfxn7qp93vt98lob9q7g5exc FOREIGN KEY (enterpriseid) REFERENCES dbo.enterprise(enterpriseid);


--
-- TOC entry 4385 (class 2606 OID 30155)
-- Name: activeflagxclassification fk7p1a2n77n5ic2exm71aemudot; Type: FK CONSTRAINT; Schema: dbo; Owner: postgres
--

ALTER TABLE ONLY dbo.activeflagxclassification
    ADD CONSTRAINT fk7p1a2n77n5ic2exm71aemudot FOREIGN KEY (originalsourcesystemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4416 (class 2606 OID 30305)
-- Name: systemssecuritytoken fk7vvidgjeuilwrvmphf0pwrdqm; Type: FK CONSTRAINT; Schema: dbo; Owner: postgres
--

ALTER TABLE ONLY dbo.systemssecuritytoken
    ADD CONSTRAINT fk7vvidgjeuilwrvmphf0pwrdqm FOREIGN KEY (systemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4402 (class 2606 OID 30220)
-- Name: enterprisexclassification fk9wfebpqldiybaphp969e7vu9q; Type: FK CONSTRAINT; Schema: dbo; Owner: postgres
--

ALTER TABLE ONLY dbo.enterprisexclassification
    ADD CONSTRAINT fk9wfebpqldiybaphp969e7vu9q FOREIGN KEY (activeflagid) REFERENCES dbo.activeflag(activeflagid);


--
-- TOC entry 4391 (class 2606 OID 30170)
-- Name: activeflagxclassificationsecuritytoken fkahuj23jai69r76hk3u9iqos5d; Type: FK CONSTRAINT; Schema: dbo; Owner: postgres
--

ALTER TABLE ONLY dbo.activeflagxclassificationsecuritytoken
    ADD CONSTRAINT fkahuj23jai69r76hk3u9iqos5d FOREIGN KEY (enterpriseid) REFERENCES dbo.enterprise(enterpriseid);


--
-- TOC entry 4425 (class 2606 OID 30340)
-- Name: systemxclassificationsecuritytoken fkammfvlpf58c86bmebac2l5fhy; Type: FK CONSTRAINT; Schema: dbo; Owner: postgres
--

ALTER TABLE ONLY dbo.systemxclassificationsecuritytoken
    ADD CONSTRAINT fkammfvlpf58c86bmebac2l5fhy FOREIGN KEY (enterpriseid) REFERENCES dbo.enterprise(enterpriseid);


--
-- TOC entry 4427 (class 2606 OID 30350)
-- Name: systemxclassificationsecuritytoken fkaxmrulkvngggqqt0yjk39qjx0; Type: FK CONSTRAINT; Schema: dbo; Owner: postgres
--

ALTER TABLE ONLY dbo.systemxclassificationsecuritytoken
    ADD CONSTRAINT fkaxmrulkvngggqqt0yjk39qjx0 FOREIGN KEY (securitytokenid) REFERENCES security.securitytoken(securitytokenid);


--
-- TOC entry 4428 (class 2606 OID 30355)
-- Name: systemxclassificationsecuritytoken fkb1iyf0i1s8q5tmc9wrwdfi52i; Type: FK CONSTRAINT; Schema: dbo; Owner: postgres
--

ALTER TABLE ONLY dbo.systemxclassificationsecuritytoken
    ADD CONSTRAINT fkb1iyf0i1s8q5tmc9wrwdfi52i FOREIGN KEY (systemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4386 (class 2606 OID 30160)
-- Name: activeflagxclassification fkblul4g9hdfavb3grhcn5bu32h; Type: FK CONSTRAINT; Schema: dbo; Owner: postgres
--

ALTER TABLE ONLY dbo.activeflagxclassification
    ADD CONSTRAINT fkblul4g9hdfavb3grhcn5bu32h FOREIGN KEY (classificationid) REFERENCES classification.classification(classificationid);


--
-- TOC entry 4397 (class 2606 OID 30195)
-- Name: enterprisesecuritytoken fkcja1yq5j2ywm4mearg7gfbeg8; Type: FK CONSTRAINT; Schema: dbo; Owner: postgres
--

ALTER TABLE ONLY dbo.enterprisesecuritytoken
    ADD CONSTRAINT fkcja1yq5j2ywm4mearg7gfbeg8 FOREIGN KEY (activeflagid) REFERENCES dbo.activeflag(activeflagid);


--
-- TOC entry 4403 (class 2606 OID 30235)
-- Name: enterprisexclassification fkd8u5qov86csi5bmvn97648jyq; Type: FK CONSTRAINT; Schema: dbo; Owner: postgres
--

ALTER TABLE ONLY dbo.enterprisexclassification
    ADD CONSTRAINT fkd8u5qov86csi5bmvn97648jyq FOREIGN KEY (originalsourcesystemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4382 (class 2606 OID 30125)
-- Name: activeflagsecuritytoken fkf1tv1gusx83k2ytmhi4g3tqbm; Type: FK CONSTRAINT; Schema: dbo; Owner: postgres
--

ALTER TABLE ONLY dbo.activeflagsecuritytoken
    ADD CONSTRAINT fkf1tv1gusx83k2ytmhi4g3tqbm FOREIGN KEY (securitytokenid) REFERENCES security.securitytoken(securitytokenid);


--
-- TOC entry 4420 (class 2606 OID 30310)
-- Name: systemxclassification fkf8j3lg5l7fp5qthp3s7v5cpsy; Type: FK CONSTRAINT; Schema: dbo; Owner: postgres
--

ALTER TABLE ONLY dbo.systemxclassification
    ADD CONSTRAINT fkf8j3lg5l7fp5qthp3s7v5cpsy FOREIGN KEY (activeflagid) REFERENCES dbo.activeflag(activeflagid);


--
-- TOC entry 4417 (class 2606 OID 30295)
-- Name: systemssecuritytoken fkgq2e9xc2wsw93xl4p5yhaysx; Type: FK CONSTRAINT; Schema: dbo; Owner: postgres
--

ALTER TABLE ONLY dbo.systemssecuritytoken
    ADD CONSTRAINT fkgq2e9xc2wsw93xl4p5yhaysx FOREIGN KEY (originalsourcesystemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4412 (class 2606 OID 30280)
-- Name: systems fkgrtajtg4kade8b7dvqjynt2c6; Type: FK CONSTRAINT; Schema: dbo; Owner: postgres
--

ALTER TABLE ONLY dbo.systems
    ADD CONSTRAINT fkgrtajtg4kade8b7dvqjynt2c6 FOREIGN KEY (enterpriseid) REFERENCES dbo.enterprise(enterpriseid);


--
-- TOC entry 4429 (class 2606 OID 30360)
-- Name: systemxclassificationsecuritytoken fkhpnepymhd6det14tmy3vsx6g8; Type: FK CONSTRAINT; Schema: dbo; Owner: postgres
--

ALTER TABLE ONLY dbo.systemxclassificationsecuritytoken
    ADD CONSTRAINT fkhpnepymhd6det14tmy3vsx6g8 FOREIGN KEY (systemxclassificationid) REFERENCES dbo.systemxclassification(systemxclassificationid);


--
-- TOC entry 4398 (class 2606 OID 30210)
-- Name: enterprisesecuritytoken fki2166mtej2yhf4qeegbrtwsom; Type: FK CONSTRAINT; Schema: dbo; Owner: postgres
--

ALTER TABLE ONLY dbo.enterprisesecuritytoken
    ADD CONSTRAINT fki2166mtej2yhf4qeegbrtwsom FOREIGN KEY (securitytokenid) REFERENCES security.securitytoken(securitytokenid);


--
-- TOC entry 4399 (class 2606 OID 30205)
-- Name: enterprisesecuritytoken fki27vhcoq25358slocd9mn9481; Type: FK CONSTRAINT; Schema: dbo; Owner: postgres
--

ALTER TABLE ONLY dbo.enterprisesecuritytoken
    ADD CONSTRAINT fki27vhcoq25358slocd9mn9481 FOREIGN KEY (originalsourcesystemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4379 (class 2606 OID 30110)
-- Name: activeflagsecuritytoken fki4tkjwmtimx0nsi9nkmvjejd8; Type: FK CONSTRAINT; Schema: dbo; Owner: postgres
--

ALTER TABLE ONLY dbo.activeflagsecuritytoken
    ADD CONSTRAINT fki4tkjwmtimx0nsi9nkmvjejd8 FOREIGN KEY (activeflagid) REFERENCES dbo.activeflag(activeflagid);


--
-- TOC entry 4387 (class 2606 OID 30145)
-- Name: activeflagxclassification fkid9su2w15uts2jocr2ix6p9n6; Type: FK CONSTRAINT; Schema: dbo; Owner: postgres
--

ALTER TABLE ONLY dbo.activeflagxclassification
    ADD CONSTRAINT fkid9su2w15uts2jocr2ix6p9n6 FOREIGN KEY (enterpriseid) REFERENCES dbo.enterprise(enterpriseid);


--
-- TOC entry 4424 (class 2606 OID 30335)
-- Name: systemxclassificationsecuritytoken fkj9spyu6coc19mcroktrbdym0i; Type: FK CONSTRAINT; Schema: dbo; Owner: postgres
--

ALTER TABLE ONLY dbo.systemxclassificationsecuritytoken
    ADD CONSTRAINT fkj9spyu6coc19mcroktrbdym0i FOREIGN KEY (activeflagid) REFERENCES dbo.activeflag(activeflagid);


--
-- TOC entry 4388 (class 2606 OID 30140)
-- Name: activeflagxclassification fkjcnjquu3mlf3fqxljya6hhh32; Type: FK CONSTRAINT; Schema: dbo; Owner: postgres
--

ALTER TABLE ONLY dbo.activeflagxclassification
    ADD CONSTRAINT fkjcnjquu3mlf3fqxljya6hhh32 FOREIGN KEY (activeflagid) REFERENCES dbo.activeflag(activeflagid);


--
-- TOC entry 4395 (class 2606 OID 30190)
-- Name: activeflagxclassificationsecuritytoken fkjt5o1jc7herohd02v678yyba6; Type: FK CONSTRAINT; Schema: dbo; Owner: postgres
--

ALTER TABLE ONLY dbo.activeflagxclassificationsecuritytoken
    ADD CONSTRAINT fkjt5o1jc7herohd02v678yyba6 FOREIGN KEY (activeflagxclassificationid) REFERENCES dbo.activeflagxclassification(activeflagxclassificationid);


--
-- TOC entry 4413 (class 2606 OID 30275)
-- Name: systems fkk9wup45yifecrf4o0fb5lyuy6; Type: FK CONSTRAINT; Schema: dbo; Owner: postgres
--

ALTER TABLE ONLY dbo.systems
    ADD CONSTRAINT fkk9wup45yifecrf4o0fb5lyuy6 FOREIGN KEY (activeflagid) REFERENCES dbo.activeflag(activeflagid);


--
-- TOC entry 4400 (class 2606 OID 30200)
-- Name: enterprisesecuritytoken fkkfbukc1416acs2qhjduyaslun; Type: FK CONSTRAINT; Schema: dbo; Owner: postgres
--

ALTER TABLE ONLY dbo.enterprisesecuritytoken
    ADD CONSTRAINT fkkfbukc1416acs2qhjduyaslun FOREIGN KEY (enterpriseid) REFERENCES dbo.enterprise(enterpriseid);


--
-- TOC entry 4392 (class 2606 OID 30175)
-- Name: activeflagxclassificationsecuritytoken fkl8ovwqdvb6bagyf17g7gk46qy; Type: FK CONSTRAINT; Schema: dbo; Owner: postgres
--

ALTER TABLE ONLY dbo.activeflagxclassificationsecuritytoken
    ADD CONSTRAINT fkl8ovwqdvb6bagyf17g7gk46qy FOREIGN KEY (originalsourcesystemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4384 (class 2606 OID 30135)
-- Name: activeflagsecuritytoken fklh0yk0eo7gs9tjd368uaqa0vt; Type: FK CONSTRAINT; Schema: dbo; Owner: postgres
--

ALTER TABLE ONLY dbo.activeflagsecuritytoken
    ADD CONSTRAINT fklh0yk0eo7gs9tjd368uaqa0vt FOREIGN KEY (securitytokenactiveflagid) REFERENCES dbo.activeflag(activeflagid);


--
-- TOC entry 4390 (class 2606 OID 30165)
-- Name: activeflagxclassificationsecuritytoken fklmqtxiiqcje4o4vix6egjsg27; Type: FK CONSTRAINT; Schema: dbo; Owner: postgres
--

ALTER TABLE ONLY dbo.activeflagxclassificationsecuritytoken
    ADD CONSTRAINT fklmqtxiiqcje4o4vix6egjsg27 FOREIGN KEY (activeflagid) REFERENCES dbo.activeflag(activeflagid);


--
-- TOC entry 4410 (class 2606 OID 30265)
-- Name: enterprisexclassificationsecuritytoken fklojg3xmk98ghle10liw96742m; Type: FK CONSTRAINT; Schema: dbo; Owner: postgres
--

ALTER TABLE ONLY dbo.enterprisexclassificationsecuritytoken
    ADD CONSTRAINT fklojg3xmk98ghle10liw96742m FOREIGN KEY (systemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4378 (class 2606 OID 30105)
-- Name: activeflag fkme13h3ny7lm8n86kltbw69ni1; Type: FK CONSTRAINT; Schema: dbo; Owner: postgres
--

ALTER TABLE ONLY dbo.activeflag
    ADD CONSTRAINT fkme13h3ny7lm8n86kltbw69ni1 FOREIGN KEY (enterpriseid) REFERENCES dbo.enterprise(enterpriseid);


--
-- TOC entry 4404 (class 2606 OID 30230)
-- Name: enterprisexclassification fkmt4djrkvv0w1ef778yy01fone; Type: FK CONSTRAINT; Schema: dbo; Owner: postgres
--

ALTER TABLE ONLY dbo.enterprisexclassification
    ADD CONSTRAINT fkmt4djrkvv0w1ef778yy01fone FOREIGN KEY (systemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4389 (class 2606 OID 30150)
-- Name: activeflagxclassification fko09sv3aidiqrfa034t31l067w; Type: FK CONSTRAINT; Schema: dbo; Owner: postgres
--

ALTER TABLE ONLY dbo.activeflagxclassification
    ADD CONSTRAINT fko09sv3aidiqrfa034t31l067w FOREIGN KEY (systemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4421 (class 2606 OID 30325)
-- Name: systemxclassification fko0c1ywx0xwsytkccp23xtolxd; Type: FK CONSTRAINT; Schema: dbo; Owner: postgres
--

ALTER TABLE ONLY dbo.systemxclassification
    ADD CONSTRAINT fko0c1ywx0xwsytkccp23xtolxd FOREIGN KEY (originalsourcesystemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4408 (class 2606 OID 30255)
-- Name: enterprisexclassificationsecuritytoken fkp7wseksjtv3n3vok7qm68dmdb; Type: FK CONSTRAINT; Schema: dbo; Owner: postgres
--

ALTER TABLE ONLY dbo.enterprisexclassificationsecuritytoken
    ADD CONSTRAINT fkp7wseksjtv3n3vok7qm68dmdb FOREIGN KEY (originalsourcesystemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4405 (class 2606 OID 30240)
-- Name: enterprisexclassification fkpubcpyc3hm0x9l70u19lj96r0; Type: FK CONSTRAINT; Schema: dbo; Owner: postgres
--

ALTER TABLE ONLY dbo.enterprisexclassification
    ADD CONSTRAINT fkpubcpyc3hm0x9l70u19lj96r0 FOREIGN KEY (classificationid) REFERENCES classification.classification(classificationid);


--
-- TOC entry 4422 (class 2606 OID 30320)
-- Name: systemxclassification fkq69xnhoy37i0vgl4n0ye0deql; Type: FK CONSTRAINT; Schema: dbo; Owner: postgres
--

ALTER TABLE ONLY dbo.systemxclassification
    ADD CONSTRAINT fkq69xnhoy37i0vgl4n0ye0deql FOREIGN KEY (systemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4411 (class 2606 OID 30270)
-- Name: enterprisexclassificationsecuritytoken fkr6xvbr7r4rmk73etcyyx887ao; Type: FK CONSTRAINT; Schema: dbo; Owner: postgres
--

ALTER TABLE ONLY dbo.enterprisexclassificationsecuritytoken
    ADD CONSTRAINT fkr6xvbr7r4rmk73etcyyx887ao FOREIGN KEY (enterprisexclassificationid) REFERENCES dbo.enterprisexclassification(enterprisexclassificationid);


--
-- TOC entry 4380 (class 2606 OID 30115)
-- Name: activeflagsecuritytoken fkr9uxaoa4e7qc848omo02gnv8q; Type: FK CONSTRAINT; Schema: dbo; Owner: postgres
--

ALTER TABLE ONLY dbo.activeflagsecuritytoken
    ADD CONSTRAINT fkr9uxaoa4e7qc848omo02gnv8q FOREIGN KEY (enterpriseid) REFERENCES dbo.enterprise(enterpriseid);


--
-- TOC entry 4418 (class 2606 OID 30300)
-- Name: systemssecuritytoken fks9aoyq05lmsl0jpqnja4gy5g8; Type: FK CONSTRAINT; Schema: dbo; Owner: postgres
--

ALTER TABLE ONLY dbo.systemssecuritytoken
    ADD CONSTRAINT fks9aoyq05lmsl0jpqnja4gy5g8 FOREIGN KEY (securitytokenid) REFERENCES security.securitytoken(securitytokenid);


--
-- TOC entry 4423 (class 2606 OID 30330)
-- Name: systemxclassification fksok0wnd9e14j1shyqkn1fkpf3; Type: FK CONSTRAINT; Schema: dbo; Owner: postgres
--

ALTER TABLE ONLY dbo.systemxclassification
    ADD CONSTRAINT fksok0wnd9e14j1shyqkn1fkpf3 FOREIGN KEY (classificationid) REFERENCES classification.classification(classificationid);


--
-- TOC entry 4426 (class 2606 OID 30345)
-- Name: systemxclassificationsecuritytoken fktm7py81728d8oor46wotp8y0f; Type: FK CONSTRAINT; Schema: dbo; Owner: postgres
--

ALTER TABLE ONLY dbo.systemxclassificationsecuritytoken
    ADD CONSTRAINT fktm7py81728d8oor46wotp8y0f FOREIGN KEY (originalsourcesystemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4531 (class 2606 OID 30870)
-- Name: eventxinvolvedparty fk14n4sdaea68x8ye5kdnm92m4v; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE ONLY event.eventxinvolvedparty
    ADD CONSTRAINT fk14n4sdaea68x8ye5kdnm92m4v FOREIGN KEY (eventid) REFERENCES event.event(eventid);


--
-- TOC entry 4460 (class 2606 OID 30515)
-- Name: eventxaddresssecuritytoken fk1b8yahv9f17vt9brstr9lu5s; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE ONLY event.eventxaddresssecuritytoken
    ADD CONSTRAINT fk1b8yahv9f17vt9brstr9lu5s FOREIGN KEY (securitytokenid) REFERENCES security.securitytoken(securitytokenid);


--
-- TOC entry 4523 (class 2606 OID 30830)
-- Name: eventxgeographysecuritytoken fk1i4qgb7md1vd911fwvxii42tx; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE ONLY event.eventxgeographysecuritytoken
    ADD CONSTRAINT fk1i4qgb7md1vd911fwvxii42tx FOREIGN KEY (securitytokenid) REFERENCES security.securitytoken(securitytokenid);


--
-- TOC entry 4490 (class 2606 OID 30665)
-- Name: eventxevent fk27gym41nw6dls3e5oll0nkcbr; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE ONLY event.eventxevent
    ADD CONSTRAINT fk27gym41nw6dls3e5oll0nkcbr FOREIGN KEY (originalsourcesystemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4461 (class 2606 OID 30520)
-- Name: eventxaddresssecuritytoken fk2n5hwft2sainpe6l7cerwj4o6; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE ONLY event.eventxaddresssecuritytoken
    ADD CONSTRAINT fk2n5hwft2sainpe6l7cerwj4o6 FOREIGN KEY (systemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4503 (class 2606 OID 30730)
-- Name: eventxeventtype fk2s0kehqs4vd8un8pa781t8abk; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE ONLY event.eventxeventtype
    ADD CONSTRAINT fk2s0kehqs4vd8un8pa781t8abk FOREIGN KEY (originalsourcesystemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4473 (class 2606 OID 30580)
-- Name: eventxarrangementssecuritytoken fk2tudn6gio54xpxa5ecyryl2v0; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE ONLY event.eventxarrangementssecuritytoken
    ADD CONSTRAINT fk2tudn6gio54xpxa5ecyryl2v0 FOREIGN KEY (systemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4482 (class 2606 OID 30625)
-- Name: eventxclassificationsecuritytoken fk2uwfplnar8o0onw3e93phd69k; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE ONLY event.eventxclassificationsecuritytoken
    ADD CONSTRAINT fk2uwfplnar8o0onw3e93phd69k FOREIGN KEY (enterpriseid) REFERENCES dbo.enterprise(enterpriseid);


--
-- TOC entry 4542 (class 2606 OID 30925)
-- Name: eventxproduct fk2wg3fp1s1jjl3lng6uak99fr4; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE ONLY event.eventxproduct
    ADD CONSTRAINT fk2wg3fp1s1jjl3lng6uak99fr4 FOREIGN KEY (originalsourcesystemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4557 (class 2606 OID 31000)
-- Name: eventxresourceitem fk30l7kjmsyo5va5n3exkkl364i; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE ONLY event.eventxresourceitem
    ADD CONSTRAINT fk30l7kjmsyo5va5n3exkkl364i FOREIGN KEY (eventid) REFERENCES event.event(eventid);


--
-- TOC entry 4470 (class 2606 OID 30565)
-- Name: eventxarrangementssecuritytoken fk33w71cp61prmc7i0uboecicda; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE ONLY event.eventxarrangementssecuritytoken
    ADD CONSTRAINT fk33w71cp61prmc7i0uboecicda FOREIGN KEY (enterpriseid) REFERENCES dbo.enterprise(enterpriseid);


--
-- TOC entry 4440 (class 2606 OID 30415)
-- Name: eventtype fk3a6ig1a98nle8uhil7c00wtxj; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE ONLY event.eventtype
    ADD CONSTRAINT fk3a6ig1a98nle8uhil7c00wtxj FOREIGN KEY (activeflagid) REFERENCES dbo.activeflag(activeflagid);


--
-- TOC entry 4451 (class 2606 OID 30470)
-- Name: eventxaddress fk3bbi34ppkvf938mm8618n7cdi; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE ONLY event.eventxaddress
    ADD CONSTRAINT fk3bbi34ppkvf938mm8618n7cdi FOREIGN KEY (enterpriseid) REFERENCES dbo.enterprise(enterpriseid);


--
-- TOC entry 4525 (class 2606 OID 30840)
-- Name: eventxgeographysecuritytoken fk3k3bo2t7xd8q3va2g8whp7l7v; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE ONLY event.eventxgeographysecuritytoken
    ADD CONSTRAINT fk3k3bo2t7xd8q3va2g8whp7l7v FOREIGN KEY (eventxgeographyid) REFERENCES event.eventxgeography(eventxgeographyid);


--
-- TOC entry 4571 (class 2606 OID 31070)
-- Name: eventxrules fk3pv6r2calb05a669dg4aaqvy3; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE ONLY event.eventxrules
    ADD CONSTRAINT fk3pv6r2calb05a669dg4aaqvy3 FOREIGN KEY (rulesid) REFERENCES rules.rules(rulesid);


--
-- TOC entry 4447 (class 2606 OID 30450)
-- Name: eventtypessecuritytoken fk3sqx94u3an8m27b0aotdca2wb; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE ONLY event.eventtypessecuritytoken
    ADD CONSTRAINT fk3sqx94u3an8m27b0aotdca2wb FOREIGN KEY (securitytokenid) REFERENCES security.securitytoken(securitytokenid);


--
-- TOC entry 4572 (class 2606 OID 31075)
-- Name: eventxrulessecuritytoken fk4cfg8atxhbgoya17d1vp1bpf7; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE ONLY event.eventxrulessecuritytoken
    ADD CONSTRAINT fk4cfg8atxhbgoya17d1vp1bpf7 FOREIGN KEY (activeflagid) REFERENCES dbo.activeflag(activeflagid);


--
-- TOC entry 4458 (class 2606 OID 30505)
-- Name: eventxaddresssecuritytoken fk4delkrhbqq9uhqpsmqbdc8eh1; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE ONLY event.eventxaddresssecuritytoken
    ADD CONSTRAINT fk4delkrhbqq9uhqpsmqbdc8eh1 FOREIGN KEY (enterpriseid) REFERENCES dbo.enterprise(enterpriseid);


--
-- TOC entry 4481 (class 2606 OID 30620)
-- Name: eventxclassificationsecuritytoken fk4hjmqpqf99dnag748layycl1d; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE ONLY event.eventxclassificationsecuritytoken
    ADD CONSTRAINT fk4hjmqpqf99dnag748layycl1d FOREIGN KEY (activeflagid) REFERENCES dbo.activeflag(activeflagid);


--
-- TOC entry 4449 (class 2606 OID 30460)
-- Name: eventtypessecuritytoken fk533lul2cwy1gxqwdlkwot3xli; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE ONLY event.eventtypessecuritytoken
    ADD CONSTRAINT fk533lul2cwy1gxqwdlkwot3xli FOREIGN KEY (eventtypesid) REFERENCES event.eventtype(eventtypeid);


--
-- TOC entry 4430 (class 2606 OID 30380)
-- Name: event fk5a6m41a8gggwx8fqsrs4at8tg; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE ONLY event.event
    ADD CONSTRAINT fk5a6m41a8gggwx8fqsrs4at8tg FOREIGN KEY (originalsourcesystemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4497 (class 2606 OID 30700)
-- Name: eventxeventsecuritytoken fk5ce8ibb6xf53yy6w5nvrna86b; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE ONLY event.eventxeventsecuritytoken
    ADD CONSTRAINT fk5ce8ibb6xf53yy6w5nvrna86b FOREIGN KEY (securitytokenid) REFERENCES security.securitytoken(securitytokenid);


--
-- TOC entry 4437 (class 2606 OID 30400)
-- Name: eventsecuritytoken fk5l2t98n1cb4rsa23u81thgv50; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE ONLY event.eventsecuritytoken
    ADD CONSTRAINT fk5l2t98n1cb4rsa23u81thgv50 FOREIGN KEY (securitytokenid) REFERENCES security.securitytoken(securitytokenid);


--
-- TOC entry 4446 (class 2606 OID 30445)
-- Name: eventtypessecuritytoken fk69no16tl8ojgbi6so0jp3s68a; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE ONLY event.eventtypessecuritytoken
    ADD CONSTRAINT fk69no16tl8ojgbi6so0jp3s68a FOREIGN KEY (originalsourcesystemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4537 (class 2606 OID 30900)
-- Name: eventxinvolvedpartysecuritytoken fk6ckmfi6y2y2topapiybevk1rg; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE ONLY event.eventxinvolvedpartysecuritytoken
    ADD CONSTRAINT fk6ckmfi6y2y2topapiybevk1rg FOREIGN KEY (systemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4462 (class 2606 OID 30525)
-- Name: eventxaddresssecuritytoken fk6frs7aq2uychxsnl6r89fi7on; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE ONLY event.eventxaddresssecuritytoken
    ADD CONSTRAINT fk6frs7aq2uychxsnl6r89fi7on FOREIGN KEY (eventxaddressid) REFERENCES event.eventxaddress(eventxaddressid);


--
-- TOC entry 4493 (class 2606 OID 30680)
-- Name: eventxevent fk6gr2cpldlbe8dfgk92iceiksh; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE ONLY event.eventxevent
    ADD CONSTRAINT fk6gr2cpldlbe8dfgk92iceiksh FOREIGN KEY (parenteventid) REFERENCES event.event(eventid);


--
-- TOC entry 4431 (class 2606 OID 30370)
-- Name: event fk6ifge8hyt5n7895pe83e7l8ic; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE ONLY event.event
    ADD CONSTRAINT fk6ifge8hyt5n7895pe83e7l8ic FOREIGN KEY (enterpriseid) REFERENCES dbo.enterprise(enterpriseid);


--
-- TOC entry 4498 (class 2606 OID 30705)
-- Name: eventxeventsecuritytoken fk6k5gowbbqddlswcein88ndcuf; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE ONLY event.eventxeventsecuritytoken
    ADD CONSTRAINT fk6k5gowbbqddlswcein88ndcuf FOREIGN KEY (systemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4569 (class 2606 OID 31060)
-- Name: eventxrules fk6kh594jdfv92e12q994afvkq2; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE ONLY event.eventxrules
    ADD CONSTRAINT fk6kh594jdfv92e12q994afvkq2 FOREIGN KEY (classificationid) REFERENCES classification.classification(classificationid);


--
-- TOC entry 4543 (class 2606 OID 30930)
-- Name: eventxproduct fk6kw3pcx56g4uy28x9t6q9w5g3; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE ONLY event.eventxproduct
    ADD CONSTRAINT fk6kw3pcx56g4uy28x9t6q9w5g3 FOREIGN KEY (classificationid) REFERENCES classification.classification(classificationid);


--
-- TOC entry 4509 (class 2606 OID 30760)
-- Name: eventxeventtypesecuritytoken fk70y0xcygmmf2v7dbnjwf15tuu; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE ONLY event.eventxeventtypesecuritytoken
    ADD CONSTRAINT fk70y0xcygmmf2v7dbnjwf15tuu FOREIGN KEY (originalsourcesystemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4535 (class 2606 OID 30890)
-- Name: eventxinvolvedpartysecuritytoken fk72yo43h7hi9spviftt74k46si; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE ONLY event.eventxinvolvedpartysecuritytoken
    ADD CONSTRAINT fk72yo43h7hi9spviftt74k46si FOREIGN KEY (originalsourcesystemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4565 (class 2606 OID 31040)
-- Name: eventxrules fk795v6hn2a3tim8hhf6ery2yf8; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE ONLY event.eventxrules
    ADD CONSTRAINT fk795v6hn2a3tim8hhf6ery2yf8 FOREIGN KEY (activeflagid) REFERENCES dbo.activeflag(activeflagid);


--
-- TOC entry 4441 (class 2606 OID 30430)
-- Name: eventtype fk7bpo42s41tkfou4bopgs8xeqg; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE ONLY event.eventtype
    ADD CONSTRAINT fk7bpo42s41tkfou4bopgs8xeqg FOREIGN KEY (originalsourcesystemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4476 (class 2606 OID 30595)
-- Name: eventxclassification fk7gqyx4i1xca4ioveotqcctmd3; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE ONLY event.eventxclassification
    ADD CONSTRAINT fk7gqyx4i1xca4ioveotqcctmd3 FOREIGN KEY (enterpriseid) REFERENCES dbo.enterprise(enterpriseid);


--
-- TOC entry 4496 (class 2606 OID 30695)
-- Name: eventxeventsecuritytoken fk7kw08dh8lncu1lfruwll5ejtm; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE ONLY event.eventxeventsecuritytoken
    ADD CONSTRAINT fk7kw08dh8lncu1lfruwll5ejtm FOREIGN KEY (originalsourcesystemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4466 (class 2606 OID 30545)
-- Name: eventxarrangement fk7tkxsheat6us20jq1hhsmhkan; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE ONLY event.eventxarrangement
    ADD CONSTRAINT fk7tkxsheat6us20jq1hhsmhkan FOREIGN KEY (originalsourcesystemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4517 (class 2606 OID 30800)
-- Name: eventxgeography fk7ttvl8fdvn2nghkfr1u5adhgx; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE ONLY event.eventxgeography
    ADD CONSTRAINT fk7ttvl8fdvn2nghkfr1u5adhgx FOREIGN KEY (classificationid) REFERENCES classification.classification(classificationid);


--
-- TOC entry 4549 (class 2606 OID 30960)
-- Name: eventxproductsecuritytoken fk7wsudn2jsus0x9h44mrgv4nad; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE ONLY event.eventxproductsecuritytoken
    ADD CONSTRAINT fk7wsudn2jsus0x9h44mrgv4nad FOREIGN KEY (securitytokenid) REFERENCES security.securitytoken(securitytokenid);


--
-- TOC entry 4522 (class 2606 OID 30825)
-- Name: eventxgeographysecuritytoken fk85ggy74dqr0025fhhn8nva2v1; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE ONLY event.eventxgeographysecuritytoken
    ADD CONSTRAINT fk85ggy74dqr0025fhhn8nva2v1 FOREIGN KEY (originalsourcesystemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4459 (class 2606 OID 30510)
-- Name: eventxaddresssecuritytoken fk86mt4pshsp9bxjaubl1729701; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE ONLY event.eventxaddresssecuritytoken
    ADD CONSTRAINT fk86mt4pshsp9bxjaubl1729701 FOREIGN KEY (originalsourcesystemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4492 (class 2606 OID 30675)
-- Name: eventxevent fk8q11uo1x1xogw2rytdqbnhqn1; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE ONLY event.eventxevent
    ADD CONSTRAINT fk8q11uo1x1xogw2rytdqbnhqn1 FOREIGN KEY (childeventid) REFERENCES event.event(eventid);


--
-- TOC entry 4529 (class 2606 OID 30860)
-- Name: eventxinvolvedparty fk8xntnjn0yestyj21n227fwi9t; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE ONLY event.eventxinvolvedparty
    ADD CONSTRAINT fk8xntnjn0yestyj21n227fwi9t FOREIGN KEY (originalsourcesystemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4442 (class 2606 OID 30420)
-- Name: eventtype fk91msecp4vf69nvn0t6362tmvy; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE ONLY event.eventtype
    ADD CONSTRAINT fk91msecp4vf69nvn0t6362tmvy FOREIGN KEY (enterpriseid) REFERENCES dbo.enterprise(enterpriseid);


--
-- TOC entry 4432 (class 2606 OID 30365)
-- Name: event fk944ydkbqk682cotb21mlyedhc; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE ONLY event.event
    ADD CONSTRAINT fk944ydkbqk682cotb21mlyedhc FOREIGN KEY (activeflagid) REFERENCES dbo.activeflag(activeflagid);


--
-- TOC entry 4539 (class 2606 OID 30910)
-- Name: eventxproduct fk9dtyhip89oe1wyjki0ph2kynb; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE ONLY event.eventxproduct
    ADD CONSTRAINT fk9dtyhip89oe1wyjki0ph2kynb FOREIGN KEY (activeflagid) REFERENCES dbo.activeflag(activeflagid);


--
-- TOC entry 4527 (class 2606 OID 30850)
-- Name: eventxinvolvedparty fk9eoeiem966sixemrfgnq76211; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE ONLY event.eventxinvolvedparty
    ADD CONSTRAINT fk9eoeiem966sixemrfgnq76211 FOREIGN KEY (enterpriseid) REFERENCES dbo.enterprise(enterpriseid);


--
-- TOC entry 4518 (class 2606 OID 30805)
-- Name: eventxgeography fk9hobmhai131oqagl3obeablxc; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE ONLY event.eventxgeography
    ADD CONSTRAINT fk9hobmhai131oqagl3obeablxc FOREIGN KEY (eventid) REFERENCES event.event(eventid);


--
-- TOC entry 4570 (class 2606 OID 31065)
-- Name: eventxrules fk9koqxie6a6q0b4elyxadxe9bi; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE ONLY event.eventxrules
    ADD CONSTRAINT fk9koqxie6a6q0b4elyxadxe9bi FOREIGN KEY (eventid) REFERENCES event.event(eventid);


--
-- TOC entry 4464 (class 2606 OID 30535)
-- Name: eventxarrangement fk9l3iwtdok5r27i83ywbjt0nto; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE ONLY event.eventxarrangement
    ADD CONSTRAINT fk9l3iwtdok5r27i83ywbjt0nto FOREIGN KEY (enterpriseid) REFERENCES dbo.enterprise(enterpriseid);


--
-- TOC entry 4495 (class 2606 OID 30690)
-- Name: eventxeventsecuritytoken fkaddyvw0he74ynmpwy5rtvn26r; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE ONLY event.eventxeventsecuritytoken
    ADD CONSTRAINT fkaddyvw0he74ynmpwy5rtvn26r FOREIGN KEY (enterpriseid) REFERENCES dbo.enterprise(enterpriseid);


--
-- TOC entry 4567 (class 2606 OID 31050)
-- Name: eventxrules fkavxgy5uwlh1u87pyd3wr431w; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE ONLY event.eventxrules
    ADD CONSTRAINT fkavxgy5uwlh1u87pyd3wr431w FOREIGN KEY (systemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4499 (class 2606 OID 30710)
-- Name: eventxeventsecuritytoken fkaybh5nk6h1pn25l38m9sb807f; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE ONLY event.eventxeventsecuritytoken
    ADD CONSTRAINT fkaybh5nk6h1pn25l38m9sb807f FOREIGN KEY (eventxeventid) REFERENCES event.eventxevent(eventxeventid);


--
-- TOC entry 4568 (class 2606 OID 31055)
-- Name: eventxrules fkb3ssh2npba39x5f4e6hofnsej; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE ONLY event.eventxrules
    ADD CONSTRAINT fkb3ssh2npba39x5f4e6hofnsej FOREIGN KEY (originalsourcesystemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4435 (class 2606 OID 30390)
-- Name: eventsecuritytoken fkb5qngfntxfvo5cn45y0afpq6x; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE ONLY event.eventsecuritytoken
    ADD CONSTRAINT fkb5qngfntxfvo5cn45y0afpq6x FOREIGN KEY (enterpriseid) REFERENCES dbo.enterprise(enterpriseid);


--
-- TOC entry 4513 (class 2606 OID 30780)
-- Name: eventxgeography fkbe9ptj3nqor5lbwjb48yl44jg; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE ONLY event.eventxgeography
    ADD CONSTRAINT fkbe9ptj3nqor5lbwjb48yl44jg FOREIGN KEY (activeflagid) REFERENCES dbo.activeflag(activeflagid);


--
-- TOC entry 4556 (class 2606 OID 30995)
-- Name: eventxresourceitem fkbyjir0fafd1lcw9bta2vjmc1n; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE ONLY event.eventxresourceitem
    ADD CONSTRAINT fkbyjir0fafd1lcw9bta2vjmc1n FOREIGN KEY (classificationid) REFERENCES classification.classification(classificationid);


--
-- TOC entry 4532 (class 2606 OID 30875)
-- Name: eventxinvolvedparty fkcr5fhf97fu39srlrsjwm8bjsw; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE ONLY event.eventxinvolvedparty
    ADD CONSTRAINT fkcr5fhf97fu39srlrsjwm8bjsw FOREIGN KEY (involvedpartyid) REFERENCES party.involvedparty(involvedpartyid);


--
-- TOC entry 4443 (class 2606 OID 30425)
-- Name: eventtype fkd28eytnr3sps354vu7k75r6ds; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE ONLY event.eventtype
    ADD CONSTRAINT fkd28eytnr3sps354vu7k75r6ds FOREIGN KEY (systemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4574 (class 2606 OID 31085)
-- Name: eventxrulessecuritytoken fkd42bwmq984cam0ylnap2s3wpc; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE ONLY event.eventxrulessecuritytoken
    ADD CONSTRAINT fkd42bwmq984cam0ylnap2s3wpc FOREIGN KEY (originalsourcesystemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4455 (class 2606 OID 30490)
-- Name: eventxaddress fkd6wdra1kpon3dn8wcfjrk6msl; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE ONLY event.eventxaddress
    ADD CONSTRAINT fkd6wdra1kpon3dn8wcfjrk6msl FOREIGN KEY (addressid) REFERENCES address.address(addressid);


--
-- TOC entry 4457 (class 2606 OID 30500)
-- Name: eventxaddresssecuritytoken fkd7r65wrbjj132a0hama4srqfu; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE ONLY event.eventxaddresssecuritytoken
    ADD CONSTRAINT fkd7r65wrbjj132a0hama4srqfu FOREIGN KEY (activeflagid) REFERENCES dbo.activeflag(activeflagid);


--
-- TOC entry 4508 (class 2606 OID 30755)
-- Name: eventxeventtypesecuritytoken fkd7rws5evj37jj64ccvudlyxmc; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE ONLY event.eventxeventtypesecuritytoken
    ADD CONSTRAINT fkd7rws5evj37jj64ccvudlyxmc FOREIGN KEY (enterpriseid) REFERENCES dbo.enterprise(enterpriseid);


--
-- TOC entry 4559 (class 2606 OID 31010)
-- Name: eventxresourceitemsecuritytoken fkds5ohuq35i98a2mbbtiv844uq; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE ONLY event.eventxresourceitemsecuritytoken
    ADD CONSTRAINT fkds5ohuq35i98a2mbbtiv844uq FOREIGN KEY (activeflagid) REFERENCES dbo.activeflag(activeflagid);


--
-- TOC entry 4575 (class 2606 OID 31090)
-- Name: eventxrulessecuritytoken fke143eyeb17r5ye7xvqygem8a6; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE ONLY event.eventxrulessecuritytoken
    ADD CONSTRAINT fke143eyeb17r5ye7xvqygem8a6 FOREIGN KEY (securitytokenid) REFERENCES security.securitytoken(securitytokenid);


--
-- TOC entry 4444 (class 2606 OID 30435)
-- Name: eventtypessecuritytoken fke2wss5k3p8r4nxol9e95m3k3q; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE ONLY event.eventtypessecuritytoken
    ADD CONSTRAINT fke2wss5k3p8r4nxol9e95m3k3q FOREIGN KEY (activeflagid) REFERENCES dbo.activeflag(activeflagid);


--
-- TOC entry 4480 (class 2606 OID 30615)
-- Name: eventxclassification fkf1gqpk872jacljscf2kv0i24r; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE ONLY event.eventxclassification
    ADD CONSTRAINT fkf1gqpk872jacljscf2kv0i24r FOREIGN KEY (eventid) REFERENCES event.event(eventid);


--
-- TOC entry 4454 (class 2606 OID 30485)
-- Name: eventxaddress fkf3dfxx8n3ijrkin1n3rr8jcek; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE ONLY event.eventxaddress
    ADD CONSTRAINT fkf3dfxx8n3ijrkin1n3rr8jcek FOREIGN KEY (classificationid) REFERENCES classification.classification(classificationid);


--
-- TOC entry 4511 (class 2606 OID 30770)
-- Name: eventxeventtypesecuritytoken fkfdhh2w146eu56jmq685a7vlo7; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE ONLY event.eventxeventtypesecuritytoken
    ADD CONSTRAINT fkfdhh2w146eu56jmq685a7vlo7 FOREIGN KEY (systemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4486 (class 2606 OID 30645)
-- Name: eventxclassificationsecuritytoken fkfoq8c32icxr3vxx3t050nwdqm; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE ONLY event.eventxclassificationsecuritytoken
    ADD CONSTRAINT fkfoq8c32icxr3vxx3t050nwdqm FOREIGN KEY (eventxclassificationsid) REFERENCES event.eventxclassification(eventxclassificationid);


--
-- TOC entry 4555 (class 2606 OID 30990)
-- Name: eventxresourceitem fkfpea4o9v60x6us7p60hss1aqu; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE ONLY event.eventxresourceitem
    ADD CONSTRAINT fkfpea4o9v60x6us7p60hss1aqu FOREIGN KEY (originalsourcesystemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4566 (class 2606 OID 31045)
-- Name: eventxrules fkfsgbiovv87rrf7llv66q5li2d; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE ONLY event.eventxrules
    ADD CONSTRAINT fkfsgbiovv87rrf7llv66q5li2d FOREIGN KEY (enterpriseid) REFERENCES dbo.enterprise(enterpriseid);


--
-- TOC entry 4561 (class 2606 OID 31020)
-- Name: eventxresourceitemsecuritytoken fkfwm7s7q3f572ajckfelr5c25d; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE ONLY event.eventxresourceitemsecuritytoken
    ADD CONSTRAINT fkfwm7s7q3f572ajckfelr5c25d FOREIGN KEY (originalsourcesystemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4576 (class 2606 OID 31095)
-- Name: eventxrulessecuritytoken fkfxyf5j49rr38955mbno4ynfqv; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE ONLY event.eventxrulessecuritytoken
    ADD CONSTRAINT fkfxyf5j49rr38955mbno4ynfqv FOREIGN KEY (systemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4544 (class 2606 OID 30935)
-- Name: eventxproduct fkfyq4xws9l9ndtorw2cgrbtc5b; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE ONLY event.eventxproduct
    ADD CONSTRAINT fkfyq4xws9l9ndtorw2cgrbtc5b FOREIGN KEY (eventid) REFERENCES event.event(eventid);


--
-- TOC entry 4484 (class 2606 OID 30635)
-- Name: eventxclassificationsecuritytoken fkg821wl3o2mwy3k7ysebk2donl; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE ONLY event.eventxclassificationsecuritytoken
    ADD CONSTRAINT fkg821wl3o2mwy3k7ysebk2donl FOREIGN KEY (securitytokenid) REFERENCES security.securitytoken(securitytokenid);


--
-- TOC entry 4534 (class 2606 OID 30885)
-- Name: eventxinvolvedpartysecuritytoken fkgechwpr8prmtfarbxf4s395tu; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE ONLY event.eventxinvolvedpartysecuritytoken
    ADD CONSTRAINT fkgechwpr8prmtfarbxf4s395tu FOREIGN KEY (enterpriseid) REFERENCES dbo.enterprise(enterpriseid);


--
-- TOC entry 4548 (class 2606 OID 30955)
-- Name: eventxproductsecuritytoken fkgl97bfv9e5ua7l2clt415514f; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE ONLY event.eventxproductsecuritytoken
    ADD CONSTRAINT fkgl97bfv9e5ua7l2clt415514f FOREIGN KEY (originalsourcesystemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4547 (class 2606 OID 30950)
-- Name: eventxproductsecuritytoken fkgpkp7841e76d4r6jv2drrorjf; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE ONLY event.eventxproductsecuritytoken
    ADD CONSTRAINT fkgpkp7841e76d4r6jv2drrorjf FOREIGN KEY (enterpriseid) REFERENCES dbo.enterprise(enterpriseid);


--
-- TOC entry 4540 (class 2606 OID 30915)
-- Name: eventxproduct fkgxnd3jbg2f39e8ik5c0ivxdru; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE ONLY event.eventxproduct
    ADD CONSTRAINT fkgxnd3jbg2f39e8ik5c0ivxdru FOREIGN KEY (enterpriseid) REFERENCES dbo.enterprise(enterpriseid);


--
-- TOC entry 4538 (class 2606 OID 30905)
-- Name: eventxinvolvedpartysecuritytoken fkh04u3m7l7xqsx19gjxiq084nw; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE ONLY event.eventxinvolvedpartysecuritytoken
    ADD CONSTRAINT fkh04u3m7l7xqsx19gjxiq084nw FOREIGN KEY (eventxinvolvedpartyid) REFERENCES event.eventxinvolvedparty(eventxinvolvedpartyid);


--
-- TOC entry 4505 (class 2606 OID 30740)
-- Name: eventxeventtype fkh0rt5ii9pw04gwly7db3xu1k; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE ONLY event.eventxeventtype
    ADD CONSTRAINT fkh0rt5ii9pw04gwly7db3xu1k FOREIGN KEY (eventid) REFERENCES event.event(eventid);


--
-- TOC entry 4494 (class 2606 OID 30685)
-- Name: eventxeventsecuritytoken fkhljdtw3p9eoe701gg8a3o21iy; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE ONLY event.eventxeventsecuritytoken
    ADD CONSTRAINT fkhljdtw3p9eoe701gg8a3o21iy FOREIGN KEY (activeflagid) REFERENCES dbo.activeflag(activeflagid);


--
-- TOC entry 4463 (class 2606 OID 30530)
-- Name: eventxarrangement fkhyhd3s7f217ugwq3wo7pk5t1y; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE ONLY event.eventxarrangement
    ADD CONSTRAINT fkhyhd3s7f217ugwq3wo7pk5t1y FOREIGN KEY (activeflagid) REFERENCES dbo.activeflag(activeflagid);


--
-- TOC entry 4491 (class 2606 OID 30670)
-- Name: eventxevent fki35b9qnbbkiv3vxem4lbvlhov; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE ONLY event.eventxevent
    ADD CONSTRAINT fki35b9qnbbkiv3vxem4lbvlhov FOREIGN KEY (classificationid) REFERENCES classification.classification(classificationid);


--
-- TOC entry 4514 (class 2606 OID 30785)
-- Name: eventxgeography fki5u3ixu9xfcy27ntux56f0gx6; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE ONLY event.eventxgeography
    ADD CONSTRAINT fki5u3ixu9xfcy27ntux56f0gx6 FOREIGN KEY (enterpriseid) REFERENCES dbo.enterprise(enterpriseid);


--
-- TOC entry 4510 (class 2606 OID 30765)
-- Name: eventxeventtypesecuritytoken fki6cykrbt81enfl7o22ujsidj9; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE ONLY event.eventxeventtypesecuritytoken
    ADD CONSTRAINT fki6cykrbt81enfl7o22ujsidj9 FOREIGN KEY (securitytokenid) REFERENCES security.securitytoken(securitytokenid);


--
-- TOC entry 4500 (class 2606 OID 30715)
-- Name: eventxeventtype fkid8h6sd0odvwwwicoto22miy8; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE ONLY event.eventxeventtype
    ADD CONSTRAINT fkid8h6sd0odvwwwicoto22miy8 FOREIGN KEY (activeflagid) REFERENCES dbo.activeflag(activeflagid);


--
-- TOC entry 4468 (class 2606 OID 30555)
-- Name: eventxarrangement fkii5wwqv91rn4ji3nwgiy3wd2l; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE ONLY event.eventxarrangement
    ADD CONSTRAINT fkii5wwqv91rn4ji3nwgiy3wd2l FOREIGN KEY (eventid) REFERENCES event.event(eventid);


--
-- TOC entry 4533 (class 2606 OID 30880)
-- Name: eventxinvolvedpartysecuritytoken fkin148rfphl52k2k9ihia0993r; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE ONLY event.eventxinvolvedpartysecuritytoken
    ADD CONSTRAINT fkin148rfphl52k2k9ihia0993r FOREIGN KEY (activeflagid) REFERENCES dbo.activeflag(activeflagid);


--
-- TOC entry 4545 (class 2606 OID 30940)
-- Name: eventxproduct fkiskxy3i7nn1pjcegr6e1kmuxh; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE ONLY event.eventxproduct
    ADD CONSTRAINT fkiskxy3i7nn1pjcegr6e1kmuxh FOREIGN KEY (productid) REFERENCES product.product(productid);


--
-- TOC entry 4524 (class 2606 OID 30835)
-- Name: eventxgeographysecuritytoken fkiw30isi83vw6xrvmxj8nou8lf; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE ONLY event.eventxgeographysecuritytoken
    ADD CONSTRAINT fkiw30isi83vw6xrvmxj8nou8lf FOREIGN KEY (systemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4526 (class 2606 OID 30845)
-- Name: eventxinvolvedparty fkiynqnmd943q3oxlqkjvdah2h; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE ONLY event.eventxinvolvedparty
    ADD CONSTRAINT fkiynqnmd943q3oxlqkjvdah2h FOREIGN KEY (activeflagid) REFERENCES dbo.activeflag(activeflagid);


--
-- TOC entry 4515 (class 2606 OID 30790)
-- Name: eventxgeography fkiyrhx2xjplgljfvo8swoilr1q; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE ONLY event.eventxgeography
    ADD CONSTRAINT fkiyrhx2xjplgljfvo8swoilr1q FOREIGN KEY (systemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4456 (class 2606 OID 30495)
-- Name: eventxaddress fkjkfm7v3719rvi0mu4nev2xu6d; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE ONLY event.eventxaddress
    ADD CONSTRAINT fkjkfm7v3719rvi0mu4nev2xu6d FOREIGN KEY (eventid) REFERENCES event.event(eventid);


--
-- TOC entry 4558 (class 2606 OID 31005)
-- Name: eventxresourceitem fkk74s5n31kgmbvmr362yle6kvd; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE ONLY event.eventxresourceitem
    ADD CONSTRAINT fkk74s5n31kgmbvmr362yle6kvd FOREIGN KEY (resourceitemid) REFERENCES resource.resourceitem(resourceitemid);


--
-- TOC entry 4483 (class 2606 OID 30630)
-- Name: eventxclassificationsecuritytoken fkkdkwjb6nhj76m65s840einvmw; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE ONLY event.eventxclassificationsecuritytoken
    ADD CONSTRAINT fkkdkwjb6nhj76m65s840einvmw FOREIGN KEY (originalsourcesystemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4573 (class 2606 OID 31080)
-- Name: eventxrulessecuritytoken fkkosmoasfra60wqac2vrx5qddw; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE ONLY event.eventxrulessecuritytoken
    ADD CONSTRAINT fkkosmoasfra60wqac2vrx5qddw FOREIGN KEY (enterpriseid) REFERENCES dbo.enterprise(enterpriseid);


--
-- TOC entry 4465 (class 2606 OID 30540)
-- Name: eventxarrangement fklj26m0snewjsmso5n6jskt2a4; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE ONLY event.eventxarrangement
    ADD CONSTRAINT fklj26m0snewjsmso5n6jskt2a4 FOREIGN KEY (systemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4450 (class 2606 OID 30465)
-- Name: eventxaddress fklmpsaajrvosc033g4idqh17bv; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE ONLY event.eventxaddress
    ADD CONSTRAINT fklmpsaajrvosc033g4idqh17bv FOREIGN KEY (activeflagid) REFERENCES dbo.activeflag(activeflagid);


--
-- TOC entry 4521 (class 2606 OID 30820)
-- Name: eventxgeographysecuritytoken fklr37auqe3m87viorqc1tsmkn; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE ONLY event.eventxgeographysecuritytoken
    ADD CONSTRAINT fklr37auqe3m87viorqc1tsmkn FOREIGN KEY (enterpriseid) REFERENCES dbo.enterprise(enterpriseid);


--
-- TOC entry 4528 (class 2606 OID 30855)
-- Name: eventxinvolvedparty fkm4w7sl97j4p81cd2rehtd5jpd; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE ONLY event.eventxinvolvedparty
    ADD CONSTRAINT fkm4w7sl97j4p81cd2rehtd5jpd FOREIGN KEY (systemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4452 (class 2606 OID 30475)
-- Name: eventxaddress fkm52nfxs3cy5syh6ar5mly3k64; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE ONLY event.eventxaddress
    ADD CONSTRAINT fkm52nfxs3cy5syh6ar5mly3k64 FOREIGN KEY (systemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4488 (class 2606 OID 30655)
-- Name: eventxevent fkm690re4pvdyyeo38mfwgatnhk; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE ONLY event.eventxevent
    ADD CONSTRAINT fkm690re4pvdyyeo38mfwgatnhk FOREIGN KEY (enterpriseid) REFERENCES dbo.enterprise(enterpriseid);


--
-- TOC entry 4553 (class 2606 OID 30980)
-- Name: eventxresourceitem fkmioseq29u1gk2kh4uhxsrpr5b; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE ONLY event.eventxresourceitem
    ADD CONSTRAINT fkmioseq29u1gk2kh4uhxsrpr5b FOREIGN KEY (enterpriseid) REFERENCES dbo.enterprise(enterpriseid);


--
-- TOC entry 4564 (class 2606 OID 31035)
-- Name: eventxresourceitemsecuritytoken fkmoagxghmr17ytrthakoidoxvn; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE ONLY event.eventxresourceitemsecuritytoken
    ADD CONSTRAINT fkmoagxghmr17ytrthakoidoxvn FOREIGN KEY (eventxresourceitemid) REFERENCES event.eventxresourceitem(eventxresourceitemid);


--
-- TOC entry 4453 (class 2606 OID 30480)
-- Name: eventxaddress fkmtwd6myq1nc0g02s2s2nhkteo; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE ONLY event.eventxaddress
    ADD CONSTRAINT fkmtwd6myq1nc0g02s2s2nhkteo FOREIGN KEY (originalsourcesystemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4434 (class 2606 OID 30385)
-- Name: eventsecuritytoken fkmwxej7o84e382w8iy2y6fwlmf; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE ONLY event.eventsecuritytoken
    ADD CONSTRAINT fkmwxej7o84e382w8iy2y6fwlmf FOREIGN KEY (activeflagid) REFERENCES dbo.activeflag(activeflagid);


--
-- TOC entry 4551 (class 2606 OID 30970)
-- Name: eventxproductsecuritytoken fkn5dxxoefbl6hg9dm8wpthnyro; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE ONLY event.eventxproductsecuritytoken
    ADD CONSTRAINT fkn5dxxoefbl6hg9dm8wpthnyro FOREIGN KEY (eventxproductid) REFERENCES event.eventxproduct(eventxproductid);


--
-- TOC entry 4563 (class 2606 OID 31030)
-- Name: eventxresourceitemsecuritytoken fknjjtfvj551fm36x4gwx2ny47f; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE ONLY event.eventxresourceitemsecuritytoken
    ADD CONSTRAINT fknjjtfvj551fm36x4gwx2ny47f FOREIGN KEY (systemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4512 (class 2606 OID 30775)
-- Name: eventxeventtypesecuritytoken fknq4vx7pf7dug9j4pr7u3mxas7; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE ONLY event.eventxeventtypesecuritytoken
    ADD CONSTRAINT fknq4vx7pf7dug9j4pr7u3mxas7 FOREIGN KEY (eventxeventtypeid) REFERENCES event.eventxeventtype(eventxeventtypeid);


--
-- TOC entry 4554 (class 2606 OID 30985)
-- Name: eventxresourceitem fkns5wkh4kiqo2l7a16029ntkpg; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE ONLY event.eventxresourceitem
    ADD CONSTRAINT fkns5wkh4kiqo2l7a16029ntkpg FOREIGN KEY (systemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4560 (class 2606 OID 31015)
-- Name: eventxresourceitemsecuritytoken fko04e70wvi9b3hehhsu7pjj30y; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE ONLY event.eventxresourceitemsecuritytoken
    ADD CONSTRAINT fko04e70wvi9b3hehhsu7pjj30y FOREIGN KEY (enterpriseid) REFERENCES dbo.enterprise(enterpriseid);


--
-- TOC entry 4479 (class 2606 OID 30610)
-- Name: eventxclassification fko28x8wv64sef2ifxjm8a4i2ft; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE ONLY event.eventxclassification
    ADD CONSTRAINT fko28x8wv64sef2ifxjm8a4i2ft FOREIGN KEY (classificationid) REFERENCES classification.classification(classificationid);


--
-- TOC entry 4438 (class 2606 OID 30405)
-- Name: eventsecuritytoken fko5kqswpu433kf5hcc3yh1w10e; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE ONLY event.eventsecuritytoken
    ADD CONSTRAINT fko5kqswpu433kf5hcc3yh1w10e FOREIGN KEY (systemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4489 (class 2606 OID 30660)
-- Name: eventxevent fko7pb9lg0uhpllekuxaeml1xip; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE ONLY event.eventxevent
    ADD CONSTRAINT fko7pb9lg0uhpllekuxaeml1xip FOREIGN KEY (systemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4550 (class 2606 OID 30965)
-- Name: eventxproductsecuritytoken fkod2lhedbtyh8y4sd0dg38l3dc; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE ONLY event.eventxproductsecuritytoken
    ADD CONSTRAINT fkod2lhedbtyh8y4sd0dg38l3dc FOREIGN KEY (systemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4516 (class 2606 OID 30795)
-- Name: eventxgeography fkohfy1ao57carvvjcwcok5sch5; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE ONLY event.eventxgeography
    ADD CONSTRAINT fkohfy1ao57carvvjcwcok5sch5 FOREIGN KEY (originalsourcesystemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4475 (class 2606 OID 30590)
-- Name: eventxclassification fkojl271yprimpjrrv6u1a2ggb6; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE ONLY event.eventxclassification
    ADD CONSTRAINT fkojl271yprimpjrrv6u1a2ggb6 FOREIGN KEY (activeflagid) REFERENCES dbo.activeflag(activeflagid);


--
-- TOC entry 4469 (class 2606 OID 30560)
-- Name: eventxarrangementssecuritytoken fkom7ax04agkpio3h8xw30vb60m; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE ONLY event.eventxarrangementssecuritytoken
    ADD CONSTRAINT fkom7ax04agkpio3h8xw30vb60m FOREIGN KEY (activeflagid) REFERENCES dbo.activeflag(activeflagid);


--
-- TOC entry 4485 (class 2606 OID 30640)
-- Name: eventxclassificationsecuritytoken fkoq4421g5p0yjlklv0231xbjel; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE ONLY event.eventxclassificationsecuritytoken
    ADD CONSTRAINT fkoq4421g5p0yjlklv0231xbjel FOREIGN KEY (systemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4577 (class 2606 OID 31100)
-- Name: eventxrulessecuritytoken fkos5etiopqb270oj8wjby9dld9; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE ONLY event.eventxrulessecuritytoken
    ADD CONSTRAINT fkos5etiopqb270oj8wjby9dld9 FOREIGN KEY (eventxrulesid) REFERENCES event.eventxrules(eventxrulesid);


--
-- TOC entry 4478 (class 2606 OID 30605)
-- Name: eventxclassification fkp7fx787hsyq9nf8820wl2rh1x; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE ONLY event.eventxclassification
    ADD CONSTRAINT fkp7fx787hsyq9nf8820wl2rh1x FOREIGN KEY (originalsourcesystemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4506 (class 2606 OID 30745)
-- Name: eventxeventtype fkp99ppwwnp2w9bnqg5pmf8b2hv; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE ONLY event.eventxeventtype
    ADD CONSTRAINT fkp99ppwwnp2w9bnqg5pmf8b2hv FOREIGN KEY (eventtypeid) REFERENCES event.eventtype(eventtypeid);


--
-- TOC entry 4477 (class 2606 OID 30600)
-- Name: eventxclassification fkpssgfk2o0m8r84lhnb04bh0om; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE ONLY event.eventxclassification
    ADD CONSTRAINT fkpssgfk2o0m8r84lhnb04bh0om FOREIGN KEY (systemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4504 (class 2606 OID 30735)
-- Name: eventxeventtype fkpurdwniain7du77wld6bi9kfk; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE ONLY event.eventxeventtype
    ADD CONSTRAINT fkpurdwniain7du77wld6bi9kfk FOREIGN KEY (classificationid) REFERENCES classification.classification(classificationid);


--
-- TOC entry 4467 (class 2606 OID 30550)
-- Name: eventxarrangement fkpwoqs4xag1rw5tyrb062tyagd; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE ONLY event.eventxarrangement
    ADD CONSTRAINT fkpwoqs4xag1rw5tyrb062tyagd FOREIGN KEY (classificationid) REFERENCES classification.classification(classificationid);


--
-- TOC entry 4474 (class 2606 OID 30585)
-- Name: eventxarrangementssecuritytoken fkq3ik0e8bbij1nqvdu0jn1unvg; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE ONLY event.eventxarrangementssecuritytoken
    ADD CONSTRAINT fkq3ik0e8bbij1nqvdu0jn1unvg FOREIGN KEY (eventxarrangementsid) REFERENCES event.eventxarrangement(eventxarrangementsid);


--
-- TOC entry 4552 (class 2606 OID 30975)
-- Name: eventxresourceitem fkq5cq69kmrvba5howgpqkpyv2t; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE ONLY event.eventxresourceitem
    ADD CONSTRAINT fkq5cq69kmrvba5howgpqkpyv2t FOREIGN KEY (activeflagid) REFERENCES dbo.activeflag(activeflagid);


--
-- TOC entry 4562 (class 2606 OID 31025)
-- Name: eventxresourceitemsecuritytoken fkq8fobq1u0tsuvj481ya8icdhe; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE ONLY event.eventxresourceitemsecuritytoken
    ADD CONSTRAINT fkq8fobq1u0tsuvj481ya8icdhe FOREIGN KEY (securitytokenid) REFERENCES security.securitytoken(securitytokenid);


--
-- TOC entry 4439 (class 2606 OID 30410)
-- Name: eventsecuritytoken fkqcwlmmiy1hf3p3ol2dy865mx5; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE ONLY event.eventsecuritytoken
    ADD CONSTRAINT fkqcwlmmiy1hf3p3ol2dy865mx5 FOREIGN KEY (eventsid) REFERENCES event.event(eventid);


--
-- TOC entry 4501 (class 2606 OID 30720)
-- Name: eventxeventtype fkqf8vxnj6qupg8u3wuiqcd247x; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE ONLY event.eventxeventtype
    ADD CONSTRAINT fkqf8vxnj6qupg8u3wuiqcd247x FOREIGN KEY (enterpriseid) REFERENCES dbo.enterprise(enterpriseid);


--
-- TOC entry 4541 (class 2606 OID 30920)
-- Name: eventxproduct fkqj3yu6g3em9mm3gabikm12hke; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE ONLY event.eventxproduct
    ADD CONSTRAINT fkqj3yu6g3em9mm3gabikm12hke FOREIGN KEY (systemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4519 (class 2606 OID 30810)
-- Name: eventxgeography fkqnbhmdykbnj6recqytrcm5x52; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE ONLY event.eventxgeography
    ADD CONSTRAINT fkqnbhmdykbnj6recqytrcm5x52 FOREIGN KEY (geographyid) REFERENCES geography.geography(geographyid);


--
-- TOC entry 4445 (class 2606 OID 30440)
-- Name: eventtypessecuritytoken fkr2xa1q4402m8pc5evyj1tw9q5; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE ONLY event.eventtypessecuritytoken
    ADD CONSTRAINT fkr2xa1q4402m8pc5evyj1tw9q5 FOREIGN KEY (enterpriseid) REFERENCES dbo.enterprise(enterpriseid);


--
-- TOC entry 4520 (class 2606 OID 30815)
-- Name: eventxgeographysecuritytoken fkrekwdq9yy06eyoqgiwlh0qw4n; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE ONLY event.eventxgeographysecuritytoken
    ADD CONSTRAINT fkrekwdq9yy06eyoqgiwlh0qw4n FOREIGN KEY (activeflagid) REFERENCES dbo.activeflag(activeflagid);


--
-- TOC entry 4487 (class 2606 OID 30650)
-- Name: eventxevent fks7qe80gktw6nesd2ijfu3j438; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE ONLY event.eventxevent
    ADD CONSTRAINT fks7qe80gktw6nesd2ijfu3j438 FOREIGN KEY (activeflagid) REFERENCES dbo.activeflag(activeflagid);


--
-- TOC entry 4472 (class 2606 OID 30575)
-- Name: eventxarrangementssecuritytoken fks8terumw31sybh6o0x4sdbc2t; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE ONLY event.eventxarrangementssecuritytoken
    ADD CONSTRAINT fks8terumw31sybh6o0x4sdbc2t FOREIGN KEY (securitytokenid) REFERENCES security.securitytoken(securitytokenid);


--
-- TOC entry 4436 (class 2606 OID 30395)
-- Name: eventsecuritytoken fksa47kc62uve787xbc9h6syv0t; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE ONLY event.eventsecuritytoken
    ADD CONSTRAINT fksa47kc62uve787xbc9h6syv0t FOREIGN KEY (originalsourcesystemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4530 (class 2606 OID 30865)
-- Name: eventxinvolvedparty fksoe05vt9d1tt0ot0rd7ns68l0; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE ONLY event.eventxinvolvedparty
    ADD CONSTRAINT fksoe05vt9d1tt0ot0rd7ns68l0 FOREIGN KEY (classificationid) REFERENCES classification.classification(classificationid);


--
-- TOC entry 4536 (class 2606 OID 30895)
-- Name: eventxinvolvedpartysecuritytoken fkssmudrecemndf6iufso5jpyoi; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE ONLY event.eventxinvolvedpartysecuritytoken
    ADD CONSTRAINT fkssmudrecemndf6iufso5jpyoi FOREIGN KEY (securitytokenid) REFERENCES security.securitytoken(securitytokenid);


--
-- TOC entry 4546 (class 2606 OID 30945)
-- Name: eventxproductsecuritytoken fkt6d41dk6xtiue499wasf738r; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE ONLY event.eventxproductsecuritytoken
    ADD CONSTRAINT fkt6d41dk6xtiue499wasf738r FOREIGN KEY (activeflagid) REFERENCES dbo.activeflag(activeflagid);


--
-- TOC entry 4433 (class 2606 OID 30375)
-- Name: event fkt9syxmvp6c5pcyhyiv9c8qpb1; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE ONLY event.event
    ADD CONSTRAINT fkt9syxmvp6c5pcyhyiv9c8qpb1 FOREIGN KEY (systemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4448 (class 2606 OID 30455)
-- Name: eventtypessecuritytoken fktcukxvcv6bvhbmcelmt16vroq; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE ONLY event.eventtypessecuritytoken
    ADD CONSTRAINT fktcukxvcv6bvhbmcelmt16vroq FOREIGN KEY (systemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4471 (class 2606 OID 30570)
-- Name: eventxarrangementssecuritytoken fktiw0truwtwa3ublkae3jexugw; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE ONLY event.eventxarrangementssecuritytoken
    ADD CONSTRAINT fktiw0truwtwa3ublkae3jexugw FOREIGN KEY (originalsourcesystemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4507 (class 2606 OID 30750)
-- Name: eventxeventtypesecuritytoken fkvymcuqjxqpd4so83dmpnj5mk; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE ONLY event.eventxeventtypesecuritytoken
    ADD CONSTRAINT fkvymcuqjxqpd4so83dmpnj5mk FOREIGN KEY (activeflagid) REFERENCES dbo.activeflag(activeflagid);


--
-- TOC entry 4502 (class 2606 OID 30725)
-- Name: eventxeventtype fkw45a7dsxmiph0mrg1j2infhv; Type: FK CONSTRAINT; Schema: event; Owner: postgres
--

ALTER TABLE ONLY event.eventxeventtype
    ADD CONSTRAINT fkw45a7dsxmiph0mrg1j2infhv FOREIGN KEY (systemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4601 (class 2606 OID 31220)
-- Name: geographyxgeography fk1dvwqevxm9g1ajxfofeyog39o; Type: FK CONSTRAINT; Schema: geography; Owner: postgres
--

ALTER TABLE ONLY geography.geographyxgeography
    ADD CONSTRAINT fk1dvwqevxm9g1ajxfofeyog39o FOREIGN KEY (activeflagid) REFERENCES dbo.activeflag(activeflagid);


--
-- TOC entry 4604 (class 2606 OID 31235)
-- Name: geographyxgeography fk1x9ioslmt95w0fjxh1h7tgh22; Type: FK CONSTRAINT; Schema: geography; Owner: postgres
--

ALTER TABLE ONLY geography.geographyxgeography
    ADD CONSTRAINT fk1x9ioslmt95w0fjxh1h7tgh22 FOREIGN KEY (originalsourcesystemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4609 (class 2606 OID 31260)
-- Name: geographyxgeographysecuritytoken fk2ho20x8qldli7owo63ieh7pdk; Type: FK CONSTRAINT; Schema: geography; Owner: postgres
--

ALTER TABLE ONLY geography.geographyxgeographysecuritytoken
    ADD CONSTRAINT fk2ho20x8qldli7owo63ieh7pdk FOREIGN KEY (enterpriseid) REFERENCES dbo.enterprise(enterpriseid);


--
-- TOC entry 4578 (class 2606 OID 31105)
-- Name: geography fk3hp5hnnmkc6wh089ye90tm3o6; Type: FK CONSTRAINT; Schema: geography; Owner: postgres
--

ALTER TABLE ONLY geography.geography
    ADD CONSTRAINT fk3hp5hnnmkc6wh089ye90tm3o6 FOREIGN KEY (activeflagid) REFERENCES dbo.activeflag(activeflagid);


--
-- TOC entry 4616 (class 2606 OID 31295)
-- Name: geographyxresourceitem fk4cbs953wqd4v6mb2yco9uu2cy; Type: FK CONSTRAINT; Schema: geography; Owner: postgres
--

ALTER TABLE ONLY geography.geographyxresourceitem
    ADD CONSTRAINT fk4cbs953wqd4v6mb2yco9uu2cy FOREIGN KEY (systemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4623 (class 2606 OID 31330)
-- Name: geographyxresourceitemsecuritytoken fk57l6e5bda4i1n4re0hw21706u; Type: FK CONSTRAINT; Schema: geography; Owner: postgres
--

ALTER TABLE ONLY geography.geographyxresourceitemsecuritytoken
    ADD CONSTRAINT fk57l6e5bda4i1n4re0hw21706u FOREIGN KEY (originalsourcesystemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4621 (class 2606 OID 31320)
-- Name: geographyxresourceitemsecuritytoken fk5j6a8twkt85ffakx17foe5klo; Type: FK CONSTRAINT; Schema: geography; Owner: postgres
--

ALTER TABLE ONLY geography.geographyxresourceitemsecuritytoken
    ADD CONSTRAINT fk5j6a8twkt85ffakx17foe5klo FOREIGN KEY (activeflagid) REFERENCES dbo.activeflag(activeflagid);


--
-- TOC entry 4599 (class 2606 OID 31210)
-- Name: geographyxclassificationsecuritytoken fk5jg1k2am65j8ibiky7mc0inxf; Type: FK CONSTRAINT; Schema: geography; Owner: postgres
--

ALTER TABLE ONLY geography.geographyxclassificationsecuritytoken
    ADD CONSTRAINT fk5jg1k2am65j8ibiky7mc0inxf FOREIGN KEY (systemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4593 (class 2606 OID 31180)
-- Name: geographyxclassification fk5ohjj567mkcmt76ptgsq2qsin; Type: FK CONSTRAINT; Schema: geography; Owner: postgres
--

ALTER TABLE ONLY geography.geographyxclassification
    ADD CONSTRAINT fk5ohjj567mkcmt76ptgsq2qsin FOREIGN KEY (classificationid) REFERENCES classification.classification(classificationid);


--
-- TOC entry 4592 (class 2606 OID 31175)
-- Name: geographyxclassification fk5p12kjl7g353s2w727ulp384s; Type: FK CONSTRAINT; Schema: geography; Owner: postgres
--

ALTER TABLE ONLY geography.geographyxclassification
    ADD CONSTRAINT fk5p12kjl7g353s2w727ulp384s FOREIGN KEY (originalsourcesystemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4622 (class 2606 OID 31325)
-- Name: geographyxresourceitemsecuritytoken fk6m9u05ntxa7n822r58ewn00e6; Type: FK CONSTRAINT; Schema: geography; Owner: postgres
--

ALTER TABLE ONLY geography.geographyxresourceitemsecuritytoken
    ADD CONSTRAINT fk6m9u05ntxa7n822r58ewn00e6 FOREIGN KEY (enterpriseid) REFERENCES dbo.enterprise(enterpriseid);


--
-- TOC entry 4583 (class 2606 OID 31130)
-- Name: geographysecuritytoken fk7ate5yexxkqmll1407emnf36i; Type: FK CONSTRAINT; Schema: geography; Owner: postgres
--

ALTER TABLE ONLY geography.geographysecuritytoken
    ADD CONSTRAINT fk7ate5yexxkqmll1407emnf36i FOREIGN KEY (activeflagid) REFERENCES dbo.activeflag(activeflagid);


--
-- TOC entry 4585 (class 2606 OID 31140)
-- Name: geographysecuritytoken fk8obgl7xdf7mth9vgblc6u057d; Type: FK CONSTRAINT; Schema: geography; Owner: postgres
--

ALTER TABLE ONLY geography.geographysecuritytoken
    ADD CONSTRAINT fk8obgl7xdf7mth9vgblc6u057d FOREIGN KEY (originalsourcesystemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4587 (class 2606 OID 31150)
-- Name: geographysecuritytoken fk9u01t3it5fhcfyw2q3x2r19f6; Type: FK CONSTRAINT; Schema: geography; Owner: postgres
--

ALTER TABLE ONLY geography.geographysecuritytoken
    ADD CONSTRAINT fk9u01t3it5fhcfyw2q3x2r19f6 FOREIGN KEY (systemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4590 (class 2606 OID 31165)
-- Name: geographyxclassification fkafecxpt71ni5iel6aklaku4f5; Type: FK CONSTRAINT; Schema: geography; Owner: postgres
--

ALTER TABLE ONLY geography.geographyxclassification
    ADD CONSTRAINT fkafecxpt71ni5iel6aklaku4f5 FOREIGN KEY (enterpriseid) REFERENCES dbo.enterprise(enterpriseid);


--
-- TOC entry 4617 (class 2606 OID 31300)
-- Name: geographyxresourceitem fkan3li0682x6rj5rnqgo2s3bm3; Type: FK CONSTRAINT; Schema: geography; Owner: postgres
--

ALTER TABLE ONLY geography.geographyxresourceitem
    ADD CONSTRAINT fkan3li0682x6rj5rnqgo2s3bm3 FOREIGN KEY (originalsourcesystemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4584 (class 2606 OID 31135)
-- Name: geographysecuritytoken fkbrt91rfhyvn2hp76aga8vvdoq; Type: FK CONSTRAINT; Schema: geography; Owner: postgres
--

ALTER TABLE ONLY geography.geographysecuritytoken
    ADD CONSTRAINT fkbrt91rfhyvn2hp76aga8vvdoq FOREIGN KEY (enterpriseid) REFERENCES dbo.enterprise(enterpriseid);


--
-- TOC entry 4618 (class 2606 OID 31305)
-- Name: geographyxresourceitem fkbvccen8s30rkpie76wyr1mixd; Type: FK CONSTRAINT; Schema: geography; Owner: postgres
--

ALTER TABLE ONLY geography.geographyxresourceitem
    ADD CONSTRAINT fkbvccen8s30rkpie76wyr1mixd FOREIGN KEY (classificationid) REFERENCES classification.classification(classificationid);


--
-- TOC entry 4579 (class 2606 OID 31125)
-- Name: geography fkd30h42gp0snfq2lrm61hxeo7a; Type: FK CONSTRAINT; Schema: geography; Owner: postgres
--

ALTER TABLE ONLY geography.geography
    ADD CONSTRAINT fkd30h42gp0snfq2lrm61hxeo7a FOREIGN KEY (classificationid) REFERENCES classification.classification(classificationid);


--
-- TOC entry 4606 (class 2606 OID 31245)
-- Name: geographyxgeography fkdwrnhxis6ef8f2rkhxv56jmgu; Type: FK CONSTRAINT; Schema: geography; Owner: postgres
--

ALTER TABLE ONLY geography.geographyxgeography
    ADD CONSTRAINT fkdwrnhxis6ef8f2rkhxv56jmgu FOREIGN KEY (childgeographyid) REFERENCES geography.geography(geographyid);


--
-- TOC entry 4603 (class 2606 OID 31230)
-- Name: geographyxgeography fkdxliyywqpd69bpwfsbm58tia1; Type: FK CONSTRAINT; Schema: geography; Owner: postgres
--

ALTER TABLE ONLY geography.geographyxgeography
    ADD CONSTRAINT fkdxliyywqpd69bpwfsbm58tia1 FOREIGN KEY (systemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4580 (class 2606 OID 31115)
-- Name: geography fke3cvg85u45hqr34o3srx8njbv; Type: FK CONSTRAINT; Schema: geography; Owner: postgres
--

ALTER TABLE ONLY geography.geography
    ADD CONSTRAINT fke3cvg85u45hqr34o3srx8njbv FOREIGN KEY (systemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4611 (class 2606 OID 31270)
-- Name: geographyxgeographysecuritytoken fke94i2ly219wcofp3qlsyepcjx; Type: FK CONSTRAINT; Schema: geography; Owner: postgres
--

ALTER TABLE ONLY geography.geographyxgeographysecuritytoken
    ADD CONSTRAINT fke94i2ly219wcofp3qlsyepcjx FOREIGN KEY (securitytokenid) REFERENCES security.securitytoken(securitytokenid);


--
-- TOC entry 4598 (class 2606 OID 31205)
-- Name: geographyxclassificationsecuritytoken fkeenbngjgunycd80bgeoaylrwp; Type: FK CONSTRAINT; Schema: geography; Owner: postgres
--

ALTER TABLE ONLY geography.geographyxclassificationsecuritytoken
    ADD CONSTRAINT fkeenbngjgunycd80bgeoaylrwp FOREIGN KEY (securitytokenid) REFERENCES security.securitytoken(securitytokenid);


--
-- TOC entry 4581 (class 2606 OID 31120)
-- Name: geography fkfe6vl7bajuvj2k024mtfxvjwy; Type: FK CONSTRAINT; Schema: geography; Owner: postgres
--

ALTER TABLE ONLY geography.geography
    ADD CONSTRAINT fkfe6vl7bajuvj2k024mtfxvjwy FOREIGN KEY (originalsourcesystemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4595 (class 2606 OID 31190)
-- Name: geographyxclassificationsecuritytoken fkffvnvtxqjjbur12t6x7qcbn2h; Type: FK CONSTRAINT; Schema: geography; Owner: postgres
--

ALTER TABLE ONLY geography.geographyxclassificationsecuritytoken
    ADD CONSTRAINT fkffvnvtxqjjbur12t6x7qcbn2h FOREIGN KEY (activeflagid) REFERENCES dbo.activeflag(activeflagid);


--
-- TOC entry 4624 (class 2606 OID 31335)
-- Name: geographyxresourceitemsecuritytoken fkfwajhfr051wxdtw76afh5totc; Type: FK CONSTRAINT; Schema: geography; Owner: postgres
--

ALTER TABLE ONLY geography.geographyxresourceitemsecuritytoken
    ADD CONSTRAINT fkfwajhfr051wxdtw76afh5totc FOREIGN KEY (securitytokenid) REFERENCES security.securitytoken(securitytokenid);


--
-- TOC entry 4615 (class 2606 OID 31290)
-- Name: geographyxresourceitem fkgd4x1t5pb1lnp2etg9yptt7n7; Type: FK CONSTRAINT; Schema: geography; Owner: postgres
--

ALTER TABLE ONLY geography.geographyxresourceitem
    ADD CONSTRAINT fkgd4x1t5pb1lnp2etg9yptt7n7 FOREIGN KEY (enterpriseid) REFERENCES dbo.enterprise(enterpriseid);


--
-- TOC entry 4607 (class 2606 OID 31250)
-- Name: geographyxgeography fkglfnoy0sok1ayveu5glm26neo; Type: FK CONSTRAINT; Schema: geography; Owner: postgres
--

ALTER TABLE ONLY geography.geographyxgeography
    ADD CONSTRAINT fkglfnoy0sok1ayveu5glm26neo FOREIGN KEY (parentgeographyid) REFERENCES geography.geography(geographyid);


--
-- TOC entry 4594 (class 2606 OID 31185)
-- Name: geographyxclassification fkgt1brdxa60taodmc6cg28r0lb; Type: FK CONSTRAINT; Schema: geography; Owner: postgres
--

ALTER TABLE ONLY geography.geographyxclassification
    ADD CONSTRAINT fkgt1brdxa60taodmc6cg28r0lb FOREIGN KEY (geographyid) REFERENCES geography.geography(geographyid);


--
-- TOC entry 4588 (class 2606 OID 31155)
-- Name: geographysecuritytoken fkh6x5jqejxvpq70sg8w8bcddvu; Type: FK CONSTRAINT; Schema: geography; Owner: postgres
--

ALTER TABLE ONLY geography.geographysecuritytoken
    ADD CONSTRAINT fkh6x5jqejxvpq70sg8w8bcddvu FOREIGN KEY (geographyid) REFERENCES geography.geography(geographyid);


--
-- TOC entry 4605 (class 2606 OID 31240)
-- Name: geographyxgeography fkh8w4rt46cn0a8rdk7p46i6yw6; Type: FK CONSTRAINT; Schema: geography; Owner: postgres
--

ALTER TABLE ONLY geography.geographyxgeography
    ADD CONSTRAINT fkh8w4rt46cn0a8rdk7p46i6yw6 FOREIGN KEY (classificationid) REFERENCES classification.classification(classificationid);


--
-- TOC entry 4582 (class 2606 OID 31110)
-- Name: geography fkhfy9e7gg8xlswihrx45wk0093; Type: FK CONSTRAINT; Schema: geography; Owner: postgres
--

ALTER TABLE ONLY geography.geography
    ADD CONSTRAINT fkhfy9e7gg8xlswihrx45wk0093 FOREIGN KEY (enterpriseid) REFERENCES dbo.enterprise(enterpriseid);


--
-- TOC entry 4620 (class 2606 OID 31315)
-- Name: geographyxresourceitem fkikpxdma2cnfy7ak4hqqskegdt; Type: FK CONSTRAINT; Schema: geography; Owner: postgres
--

ALTER TABLE ONLY geography.geographyxresourceitem
    ADD CONSTRAINT fkikpxdma2cnfy7ak4hqqskegdt FOREIGN KEY (resourceitemid) REFERENCES resource.resourceitem(resourceitemid);


--
-- TOC entry 4608 (class 2606 OID 31255)
-- Name: geographyxgeographysecuritytoken fkjqo2qgds42bxfxae3xjj8528w; Type: FK CONSTRAINT; Schema: geography; Owner: postgres
--

ALTER TABLE ONLY geography.geographyxgeographysecuritytoken
    ADD CONSTRAINT fkjqo2qgds42bxfxae3xjj8528w FOREIGN KEY (activeflagid) REFERENCES dbo.activeflag(activeflagid);


--
-- TOC entry 4589 (class 2606 OID 31160)
-- Name: geographyxclassification fkl1bl1px429vykkiut1fdl7o0n; Type: FK CONSTRAINT; Schema: geography; Owner: postgres
--

ALTER TABLE ONLY geography.geographyxclassification
    ADD CONSTRAINT fkl1bl1px429vykkiut1fdl7o0n FOREIGN KEY (activeflagid) REFERENCES dbo.activeflag(activeflagid);


--
-- TOC entry 4600 (class 2606 OID 31215)
-- Name: geographyxclassificationsecuritytoken fkl5321rf4hio0kms78ic3f8bst; Type: FK CONSTRAINT; Schema: geography; Owner: postgres
--

ALTER TABLE ONLY geography.geographyxclassificationsecuritytoken
    ADD CONSTRAINT fkl5321rf4hio0kms78ic3f8bst FOREIGN KEY (geographyxclassificationid) REFERENCES geography.geographyxclassification(geographyxclassificationid);


--
-- TOC entry 4610 (class 2606 OID 31265)
-- Name: geographyxgeographysecuritytoken fklclax5c7qktjee2sf4mysgub8; Type: FK CONSTRAINT; Schema: geography; Owner: postgres
--

ALTER TABLE ONLY geography.geographyxgeographysecuritytoken
    ADD CONSTRAINT fklclax5c7qktjee2sf4mysgub8 FOREIGN KEY (originalsourcesystemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4614 (class 2606 OID 31285)
-- Name: geographyxresourceitem fkmfe0l4k1dgd3890oxlt65lm8g; Type: FK CONSTRAINT; Schema: geography; Owner: postgres
--

ALTER TABLE ONLY geography.geographyxresourceitem
    ADD CONSTRAINT fkmfe0l4k1dgd3890oxlt65lm8g FOREIGN KEY (activeflagid) REFERENCES dbo.activeflag(activeflagid);


--
-- TOC entry 4597 (class 2606 OID 31200)
-- Name: geographyxclassificationsecuritytoken fkmhery9mlbon8qfm9i7vymp6jy; Type: FK CONSTRAINT; Schema: geography; Owner: postgres
--

ALTER TABLE ONLY geography.geographyxclassificationsecuritytoken
    ADD CONSTRAINT fkmhery9mlbon8qfm9i7vymp6jy FOREIGN KEY (originalsourcesystemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4586 (class 2606 OID 31145)
-- Name: geographysecuritytoken fkmpal48yq5kf23jj4x9r77v0g7; Type: FK CONSTRAINT; Schema: geography; Owner: postgres
--

ALTER TABLE ONLY geography.geographysecuritytoken
    ADD CONSTRAINT fkmpal48yq5kf23jj4x9r77v0g7 FOREIGN KEY (securitytokenid) REFERENCES security.securitytoken(securitytokenid);


--
-- TOC entry 4626 (class 2606 OID 31345)
-- Name: geographyxresourceitemsecuritytoken fkodr0owqhi39gwkkygjyyk3sd9; Type: FK CONSTRAINT; Schema: geography; Owner: postgres
--

ALTER TABLE ONLY geography.geographyxresourceitemsecuritytoken
    ADD CONSTRAINT fkodr0owqhi39gwkkygjyyk3sd9 FOREIGN KEY (geographyxresourceitemid) REFERENCES geography.geographyxresourceitem(geographyxresourceitemid);


--
-- TOC entry 4602 (class 2606 OID 31225)
-- Name: geographyxgeography fkp5cwyefygfcshowmbuctqm2bd; Type: FK CONSTRAINT; Schema: geography; Owner: postgres
--

ALTER TABLE ONLY geography.geographyxgeography
    ADD CONSTRAINT fkp5cwyefygfcshowmbuctqm2bd FOREIGN KEY (enterpriseid) REFERENCES dbo.enterprise(enterpriseid);


--
-- TOC entry 4596 (class 2606 OID 31195)
-- Name: geographyxclassificationsecuritytoken fkp6nlv6glhcy1fyqj5bj5hggud; Type: FK CONSTRAINT; Schema: geography; Owner: postgres
--

ALTER TABLE ONLY geography.geographyxclassificationsecuritytoken
    ADD CONSTRAINT fkp6nlv6glhcy1fyqj5bj5hggud FOREIGN KEY (enterpriseid) REFERENCES dbo.enterprise(enterpriseid);


--
-- TOC entry 4625 (class 2606 OID 31340)
-- Name: geographyxresourceitemsecuritytoken fkpj8pp6mw0061pdlxv6p8f9qv8; Type: FK CONSTRAINT; Schema: geography; Owner: postgres
--

ALTER TABLE ONLY geography.geographyxresourceitemsecuritytoken
    ADD CONSTRAINT fkpj8pp6mw0061pdlxv6p8f9qv8 FOREIGN KEY (systemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4619 (class 2606 OID 31310)
-- Name: geographyxresourceitem fkplqcl8yui4k6xm0faaa3v9v3v; Type: FK CONSTRAINT; Schema: geography; Owner: postgres
--

ALTER TABLE ONLY geography.geographyxresourceitem
    ADD CONSTRAINT fkplqcl8yui4k6xm0faaa3v9v3v FOREIGN KEY (geographyid) REFERENCES geography.geography(geographyid);


--
-- TOC entry 4612 (class 2606 OID 31275)
-- Name: geographyxgeographysecuritytoken fkqd1qu9501eat5nh9sjc6ejg27; Type: FK CONSTRAINT; Schema: geography; Owner: postgres
--

ALTER TABLE ONLY geography.geographyxgeographysecuritytoken
    ADD CONSTRAINT fkqd1qu9501eat5nh9sjc6ejg27 FOREIGN KEY (systemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4613 (class 2606 OID 31280)
-- Name: geographyxgeographysecuritytoken fkqth25orgkxdx6xvepkv9dkco4; Type: FK CONSTRAINT; Schema: geography; Owner: postgres
--

ALTER TABLE ONLY geography.geographyxgeographysecuritytoken
    ADD CONSTRAINT fkqth25orgkxdx6xvepkv9dkco4 FOREIGN KEY (geographyxgeographyid) REFERENCES geography.geographyxgeography(geographyxgeographyid);


--
-- TOC entry 4591 (class 2606 OID 31170)
-- Name: geographyxclassification fkrtu1y36y10o03rcrhrvacvki1; Type: FK CONSTRAINT; Schema: geography; Owner: postgres
--

ALTER TABLE ONLY geography.geographyxclassification
    ADD CONSTRAINT fkrtu1y36y10o03rcrhrvacvki1 FOREIGN KEY (systemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4816 (class 2606 OID 32295)
-- Name: involvedpartyxrules fk1306t8qbngy6vd6gt28t6w515; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyxrules
    ADD CONSTRAINT fk1306t8qbngy6vd6gt28t6w515 FOREIGN KEY (activeflagid) REFERENCES dbo.activeflag(activeflagid);


--
-- TOC entry 4686 (class 2606 OID 31645)
-- Name: involvedpartysecuritytoken fk14xr935tw00ygoqib0cxoxmvf; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartysecuritytoken
    ADD CONSTRAINT fk14xr935tw00ygoqib0cxoxmvf FOREIGN KEY (securitytokenid) REFERENCES security.securitytoken(securitytokenid);


--
-- TOC entry 4761 (class 2606 OID 32020)
-- Name: involvedpartyxinvolvedpartysecuritytoken fk18x9igr3j8p5ug8wm2mattpsn; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyxinvolvedpartysecuritytoken
    ADD CONSTRAINT fk18x9igr3j8p5ug8wm2mattpsn FOREIGN KEY (systemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4740 (class 2606 OID 31915)
-- Name: involvedpartyxinvolvedpartyidentificationtypesecuritytoken fk1cih29htv8dr2nqs6113mhy20; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyxinvolvedpartyidentificationtypesecuritytoken
    ADD CONSTRAINT fk1cih29htv8dr2nqs6113mhy20 FOREIGN KEY (originalsourcesystemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4824 (class 2606 OID 32335)
-- Name: involvedpartyxrulessecuritytoken fk1gcn1u4lcsxql11rx2ydvwnbu; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyxrulessecuritytoken
    ADD CONSTRAINT fk1gcn1u4lcsxql11rx2ydvwnbu FOREIGN KEY (activeflagid) REFERENCES dbo.activeflag(activeflagid);


--
-- TOC entry 4712 (class 2606 OID 31775)
-- Name: involvedpartyxclassification fk1k9352drnr2qjxk0itdlpiphl; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyxclassification
    ADD CONSTRAINT fk1k9352drnr2qjxk0itdlpiphl FOREIGN KEY (activeflagid) REFERENCES dbo.activeflag(activeflagid);


--
-- TOC entry 4717 (class 2606 OID 31800)
-- Name: involvedpartyxclassification fk1l1b8kh214rut78airw106ska; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyxclassification
    ADD CONSTRAINT fk1l1b8kh214rut78airw106ska FOREIGN KEY (involvedpartyid) REFERENCES party.involvedparty(involvedpartyid);


--
-- TOC entry 4669 (class 2606 OID 31560)
-- Name: involvedpartyorganicsecuritytoken fk1lqut70vjbvicq402lw3avani; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyorganicsecuritytoken
    ADD CONSTRAINT fk1lqut70vjbvicq402lw3avani FOREIGN KEY (originalsourcesystemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4636 (class 2606 OID 31395)
-- Name: involvedpartyidentificationtypesecuritytoken fk1o1ufjc0u6yv02pfqokf19xhv; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyidentificationtypesecuritytoken
    ADD CONSTRAINT fk1o1ufjc0u6yv02pfqokf19xhv FOREIGN KEY (enterpriseid) REFERENCES dbo.enterprise(enterpriseid);


--
-- TOC entry 4729 (class 2606 OID 31860)
-- Name: involvedpartyxinvolvedparty fk1opo02p7o9r0k39gmafbukmdk; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyxinvolvedparty
    ADD CONSTRAINT fk1opo02p7o9r0k39gmafbukmdk FOREIGN KEY (childinvolvedpartyid) REFERENCES party.involvedparty(involvedpartyid);


--
-- TOC entry 4689 (class 2606 OID 31665)
-- Name: involvedpartytype fk1v128xeri7d1pkfnltmnoo1co; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartytype
    ADD CONSTRAINT fk1v128xeri7d1pkfnltmnoo1co FOREIGN KEY (enterpriseid) REFERENCES dbo.enterprise(enterpriseid);


--
-- TOC entry 4767 (class 2606 OID 32050)
-- Name: involvedpartyxinvolvedpartytype fk1w89pqnk0aoy7o41vm6wmidxd; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyxinvolvedpartytype
    ADD CONSTRAINT fk1w89pqnk0aoy7o41vm6wmidxd FOREIGN KEY (classificationid) REFERENCES classification.classification(classificationid);


--
-- TOC entry 4673 (class 2606 OID 31590)
-- Name: involvedpartyorganictype fk1wmvw7vfk12aop3lrals1o1pj; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyorganictype
    ADD CONSTRAINT fk1wmvw7vfk12aop3lrals1o1pj FOREIGN KEY (systemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4696 (class 2606 OID 31695)
-- Name: involvedpartytypesecuritytoken fk217dwfh6c779f14rd2w8sm1vi; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartytypesecuritytoken
    ADD CONSTRAINT fk217dwfh6c779f14rd2w8sm1vi FOREIGN KEY (securitytokenid) REFERENCES security.securitytoken(securitytokenid);


--
-- TOC entry 4810 (class 2606 OID 32265)
-- Name: involvedpartyxresourceitemsecuritytoken fk27wqwvha1jdn24hktf0sx5qs9; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyxresourceitemsecuritytoken
    ADD CONSTRAINT fk27wqwvha1jdn24hktf0sx5qs9 FOREIGN KEY (activeflagid) REFERENCES dbo.activeflag(activeflagid);


--
-- TOC entry 4775 (class 2606 OID 32090)
-- Name: involvedpartyxinvolvedpartytypesecuritytoken fk2erptg0ji67dbv45kqtodxb0c; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyxinvolvedpartytypesecuritytoken
    ADD CONSTRAINT fk2erptg0ji67dbv45kqtodxb0c FOREIGN KEY (involvedpartyxinvolvedpartytypeid) REFERENCES party.involvedpartyxinvolvedpartytype(involvedpartyxinvolvedpartytypeid);


--
-- TOC entry 4651 (class 2606 OID 31475)
-- Name: involvedpartynonorganic fk2fa0fq7vy2j3i2fog95gffalm; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartynonorganic
    ADD CONSTRAINT fk2fa0fq7vy2j3i2fog95gffalm FOREIGN KEY (enterpriseid) REFERENCES dbo.enterprise(enterpriseid);


--
-- TOC entry 4783 (class 2606 OID 32130)
-- Name: involvedpartyxproduct fk2hwee6e8u06db7hbr07aglskd; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyxproduct
    ADD CONSTRAINT fk2hwee6e8u06db7hbr07aglskd FOREIGN KEY (involvedpartyxproductid) REFERENCES party.involvedpartyxproduct(involvedpartyxproductid);


--
-- TOC entry 4792 (class 2606 OID 32175)
-- Name: involvedpartyxproducttype fk2jbtjrhepl2mad2ogocxmdr2h; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyxproducttype
    ADD CONSTRAINT fk2jbtjrhepl2mad2ogocxmdr2h FOREIGN KEY (systemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4808 (class 2606 OID 32255)
-- Name: involvedpartyxresourceitem fk2lynx18ubx3g0afp6rv021wty; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyxresourceitem
    ADD CONSTRAINT fk2lynx18ubx3g0afp6rv021wty FOREIGN KEY (involvedpartyid) REFERENCES party.involvedparty(involvedpartyid);


--
-- TOC entry 4679 (class 2606 OID 31610)
-- Name: involvedpartyorganictypesecuritytoken fk2rtgc2ltnx9facnuwt0aj6fpe; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyorganictypesecuritytoken
    ADD CONSTRAINT fk2rtgc2ltnx9facnuwt0aj6fpe FOREIGN KEY (originalsourcesystemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4662 (class 2606 OID 31540)
-- Name: involvedpartyorganic fk2u5a4jkumk881cv7x3ei6vv22; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyorganic
    ADD CONSTRAINT fk2u5a4jkumk881cv7x3ei6vv22 FOREIGN KEY (originalsourcesystemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4641 (class 2606 OID 31435)
-- Name: involvedpartynametype fk2xslqqsshflrv1q0ps8p5woad; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartynametype
    ADD CONSTRAINT fk2xslqqsshflrv1q0ps8p5woad FOREIGN KEY (originalsourcesystemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4708 (class 2606 OID 31755)
-- Name: involvedpartyxaddresssecuritytoken fk32jyotpfsamhn8afbs8xgdw5y; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyxaddresssecuritytoken
    ADD CONSTRAINT fk32jyotpfsamhn8afbs8xgdw5y FOREIGN KEY (originalsourcesystemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4704 (class 2606 OID 31735)
-- Name: involvedpartyxaddress fk3ehfta90cc7ha3uo6wplucc09; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyxaddress
    ADD CONSTRAINT fk3ehfta90cc7ha3uo6wplucc09 FOREIGN KEY (addressid) REFERENCES address.address(addressid);


--
-- TOC entry 4694 (class 2606 OID 31685)
-- Name: involvedpartytypesecuritytoken fk3fs2knats407bhami7ulk0ch9; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartytypesecuritytoken
    ADD CONSTRAINT fk3fs2knats407bhami7ulk0ch9 FOREIGN KEY (enterpriseid) REFERENCES dbo.enterprise(enterpriseid);


--
-- TOC entry 4803 (class 2606 OID 32230)
-- Name: involvedpartyxresourceitem fk3igrqj1tpl6viwlb4jn4nlr9; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyxresourceitem
    ADD CONSTRAINT fk3igrqj1tpl6viwlb4jn4nlr9 FOREIGN KEY (activeflagid) REFERENCES dbo.activeflag(activeflagid);


--
-- TOC entry 4722 (class 2606 OID 31825)
-- Name: involvedpartyxclassificationsecuritytoken fk3jrcy00rqjcc9lotsouknd88; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyxclassificationsecuritytoken
    ADD CONSTRAINT fk3jrcy00rqjcc9lotsouknd88 FOREIGN KEY (systemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4768 (class 2606 OID 32055)
-- Name: involvedpartyxinvolvedpartytype fk3pqtk4c0d0raqtidrxy0kpohi; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyxinvolvedpartytype
    ADD CONSTRAINT fk3pqtk4c0d0raqtidrxy0kpohi FOREIGN KEY (involvedpartyid) REFERENCES party.involvedparty(involvedpartyid);


--
-- TOC entry 4700 (class 2606 OID 31715)
-- Name: involvedpartyxaddress fk4flitthmlghp6mj3h7maq4w84; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyxaddress
    ADD CONSTRAINT fk4flitthmlghp6mj3h7maq4w84 FOREIGN KEY (enterpriseid) REFERENCES dbo.enterprise(enterpriseid);


--
-- TOC entry 4627 (class 2606 OID 31360)
-- Name: involvedparty fk4gadr7utku5dispktvjwbl2cu; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedparty
    ADD CONSTRAINT fk4gadr7utku5dispktvjwbl2cu FOREIGN KEY (systemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4661 (class 2606 OID 31520)
-- Name: involvedpartynonorganicsecuritytoken fk4igjsit3bwp3b387pa19s1sej; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartynonorganicsecuritytoken
    ADD CONSTRAINT fk4igjsit3bwp3b387pa19s1sej FOREIGN KEY (involvedpartynonorganicid) REFERENCES party.involvedpartynonorganic(involvedpartynonorganicid);


--
-- TOC entry 4773 (class 2606 OID 32080)
-- Name: involvedpartyxinvolvedpartytypesecuritytoken fk4jy62mro7v8k9isc2tc9ikaat; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyxinvolvedpartytypesecuritytoken
    ADD CONSTRAINT fk4jy62mro7v8k9isc2tc9ikaat FOREIGN KEY (securitytokenid) REFERENCES security.securitytoken(securitytokenid);


--
-- TOC entry 4635 (class 2606 OID 31390)
-- Name: involvedpartyidentificationtypesecuritytoken fk4pb209rtplqb1uj5wtx2xr5mg; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyidentificationtypesecuritytoken
    ADD CONSTRAINT fk4pb209rtplqb1uj5wtx2xr5mg FOREIGN KEY (activeflagid) REFERENCES dbo.activeflag(activeflagid);


--
-- TOC entry 4726 (class 2606 OID 31845)
-- Name: involvedpartyxinvolvedparty fk55h5hqcp98fursty4tgy1krgd; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyxinvolvedparty
    ADD CONSTRAINT fk55h5hqcp98fursty4tgy1krgd FOREIGN KEY (systemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4711 (class 2606 OID 31770)
-- Name: involvedpartyxaddresssecuritytoken fk58kov7xpcnh58m7m0rxhs6g30; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyxaddresssecuritytoken
    ADD CONSTRAINT fk58kov7xpcnh58m7m0rxhs6g30 FOREIGN KEY (involvedpartyxaddressid) REFERENCES party.involvedpartyxaddress(involvedpartyxaddressid);


--
-- TOC entry 4646 (class 2606 OID 31445)
-- Name: involvedpartynametypesecuritytoken fk5dtp2b2133pffte1i7gxtom1t; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartynametypesecuritytoken
    ADD CONSTRAINT fk5dtp2b2133pffte1i7gxtom1t FOREIGN KEY (enterpriseid) REFERENCES dbo.enterprise(enterpriseid);


--
-- TOC entry 4745 (class 2606 OID 31940)
-- Name: involvedpartyxinvolvedpartynametype fk5x12pwiby030udsbagyaqxluf; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyxinvolvedpartynametype
    ADD CONSTRAINT fk5x12pwiby030udsbagyaqxluf FOREIGN KEY (enterpriseid) REFERENCES dbo.enterprise(enterpriseid);


--
-- TOC entry 4702 (class 2606 OID 31725)
-- Name: involvedpartyxaddress fk6c2fjdp7lxcfy0ar93ni78dcv; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyxaddress
    ADD CONSTRAINT fk6c2fjdp7lxcfy0ar93ni78dcv FOREIGN KEY (originalsourcesystemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4674 (class 2606 OID 31580)
-- Name: involvedpartyorganictype fk6ei1dirma8r1y7lh4glpn9cuy; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyorganictype
    ADD CONSTRAINT fk6ei1dirma8r1y7lh4glpn9cuy FOREIGN KEY (activeflagid) REFERENCES dbo.activeflag(activeflagid);


--
-- TOC entry 4820 (class 2606 OID 32315)
-- Name: involvedpartyxrules fk6hhyqulmrh1w7g3212r7jfkaw; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyxrules
    ADD CONSTRAINT fk6hhyqulmrh1w7g3212r7jfkaw FOREIGN KEY (classificationid) REFERENCES classification.classification(classificationid);


--
-- TOC entry 4730 (class 2606 OID 31865)
-- Name: involvedpartyxinvolvedparty fk6ih93cr42n69eamwdjj8a2ygn; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyxinvolvedparty
    ADD CONSTRAINT fk6ih93cr42n69eamwdjj8a2ygn FOREIGN KEY (parentinvolvedpartyid) REFERENCES party.involvedparty(involvedpartyid);


--
-- TOC entry 4760 (class 2606 OID 32015)
-- Name: involvedpartyxinvolvedpartysecuritytoken fk6l4fxgap9wfdttqp33s0wyut4; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyxinvolvedpartysecuritytoken
    ADD CONSTRAINT fk6l4fxgap9wfdttqp33s0wyut4 FOREIGN KEY (securitytokenid) REFERENCES security.securitytoken(securitytokenid);


--
-- TOC entry 4737 (class 2606 OID 31900)
-- Name: involvedpartyxinvolvedpartyidentificationtype fk6mi0299iv61l8tmmegrfwi2w3; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyxinvolvedpartyidentificationtype
    ADD CONSTRAINT fk6mi0299iv61l8tmmegrfwi2w3 FOREIGN KEY (involvedpartyidentificationtypeid) REFERENCES party.involvedpartyidentificationtype(involvedpartyidentificationtypeid);


--
-- TOC entry 4721 (class 2606 OID 31820)
-- Name: involvedpartyxclassificationsecuritytoken fk6pqhjtv3yvs3afa2dg5erfbad; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyxclassificationsecuritytoken
    ADD CONSTRAINT fk6pqhjtv3yvs3afa2dg5erfbad FOREIGN KEY (securitytokenid) REFERENCES security.securitytoken(securitytokenid);


--
-- TOC entry 4787 (class 2606 OID 32150)
-- Name: involvedpartyxproductsecuritytoken fk6tukdpqv5fg05yawnk4f35bun; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyxproductsecuritytoken
    ADD CONSTRAINT fk6tukdpqv5fg05yawnk4f35bun FOREIGN KEY (securitytokenid) REFERENCES security.securitytoken(securitytokenid);


--
-- TOC entry 4690 (class 2606 OID 31675)
-- Name: involvedpartytype fk6wemtilbmlhs3b040hu5tfn7v; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartytype
    ADD CONSTRAINT fk6wemtilbmlhs3b040hu5tfn7v FOREIGN KEY (originalsourcesystemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4814 (class 2606 OID 32285)
-- Name: involvedpartyxresourceitemsecuritytoken fk72adgv7vc7uthg1eiq4sceq89; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyxresourceitemsecuritytoken
    ADD CONSTRAINT fk72adgv7vc7uthg1eiq4sceq89 FOREIGN KEY (systemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4640 (class 2606 OID 31415)
-- Name: involvedpartyidentificationtypesecuritytoken fk7dkwueip8veb59dox327ia0a8; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyidentificationtypesecuritytoken
    ADD CONSTRAINT fk7dkwueip8veb59dox327ia0a8 FOREIGN KEY (involvedpartyidentificationtypeid) REFERENCES party.involvedpartyidentificationtype(involvedpartyidentificationtypeid);


--
-- TOC entry 4713 (class 2606 OID 31780)
-- Name: involvedpartyxclassification fk7jbx6dlrjcvu3ifb2s1w0ulct; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyxclassification
    ADD CONSTRAINT fk7jbx6dlrjcvu3ifb2s1w0ulct FOREIGN KEY (enterpriseid) REFERENCES dbo.enterprise(enterpriseid);


--
-- TOC entry 4743 (class 2606 OID 31930)
-- Name: involvedpartyxinvolvedpartyidentificationtypesecuritytoken fk7ot9s9hc1w0icwm3ogcwhqvd3; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyxinvolvedpartyidentificationtypesecuritytoken
    ADD CONSTRAINT fk7ot9s9hc1w0icwm3ogcwhqvd3 FOREIGN KEY (involvedpartyxinvolvedpartyidentificationtypeid) REFERENCES party.involvedpartyxinvolvedpartyidentificationtype(involvedpartyxinvolvedpartyidentificationtypeid);


--
-- TOC entry 4650 (class 2606 OID 31465)
-- Name: involvedpartynametypesecuritytoken fk7t8d65pscktrxlw1we4idhlyj; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartynametypesecuritytoken
    ADD CONSTRAINT fk7t8d65pscktrxlw1we4idhlyj FOREIGN KEY (involvedpartynametypeid) REFERENCES party.involvedpartynametype(involvedpartynametypeid);


--
-- TOC entry 4752 (class 2606 OID 31975)
-- Name: involvedpartyxinvolvedpartynametypesecuritytoken fk7xl5vyatrbubvu8on89cnkdsj; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyxinvolvedpartynametypesecuritytoken
    ADD CONSTRAINT fk7xl5vyatrbubvu8on89cnkdsj FOREIGN KEY (enterpriseid) REFERENCES dbo.enterprise(enterpriseid);


--
-- TOC entry 4827 (class 2606 OID 32350)
-- Name: involvedpartyxrulessecuritytoken fk86e07awdx5h97q548mtjxnlp1; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyxrulessecuritytoken
    ADD CONSTRAINT fk86e07awdx5h97q548mtjxnlp1 FOREIGN KEY (securitytokenid) REFERENCES security.securitytoken(securitytokenid);


--
-- TOC entry 4828 (class 2606 OID 32355)
-- Name: involvedpartyxrulessecuritytoken fk86h342si0g1hy4qpnkap8oq08; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyxrulessecuritytoken
    ADD CONSTRAINT fk86h342si0g1hy4qpnkap8oq08 FOREIGN KEY (systemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4656 (class 2606 OID 31495)
-- Name: involvedpartynonorganicsecuritytoken fk8a7thuoro1mbxsng2amjluimk; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartynonorganicsecuritytoken
    ADD CONSTRAINT fk8a7thuoro1mbxsng2amjluimk FOREIGN KEY (activeflagid) REFERENCES dbo.activeflag(activeflagid);


--
-- TOC entry 4799 (class 2606 OID 32210)
-- Name: involvedpartyxproducttypesecuritytoken fk8ftebinncegtl979d5gsqnkah; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyxproducttypesecuritytoken
    ADD CONSTRAINT fk8ftebinncegtl979d5gsqnkah FOREIGN KEY (originalsourcesystemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4774 (class 2606 OID 32085)
-- Name: involvedpartyxinvolvedpartytypesecuritytoken fk8kb1lmh3wvovdurs0rq2jtotk; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyxinvolvedpartytypesecuritytoken
    ADD CONSTRAINT fk8kb1lmh3wvovdurs0rq2jtotk FOREIGN KEY (systemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4817 (class 2606 OID 32300)
-- Name: involvedpartyxrules fk8yk3mt51je6xtf3as7f1n5koa; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyxrules
    ADD CONSTRAINT fk8yk3mt51je6xtf3as7f1n5koa FOREIGN KEY (enterpriseid) REFERENCES dbo.enterprise(enterpriseid);


--
-- TOC entry 4706 (class 2606 OID 31745)
-- Name: involvedpartyxaddresssecuritytoken fk9h5dpjakcr9j4c61u628tte1b; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyxaddresssecuritytoken
    ADD CONSTRAINT fk9h5dpjakcr9j4c61u628tte1b FOREIGN KEY (activeflagid) REFERENCES dbo.activeflag(activeflagid);


--
-- TOC entry 4732 (class 2606 OID 31875)
-- Name: involvedpartyxinvolvedpartyidentificationtype fk9k1s2vuya90it56mtuij9qk0d; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyxinvolvedpartyidentificationtype
    ADD CONSTRAINT fk9k1s2vuya90it56mtuij9qk0d FOREIGN KEY (enterpriseid) REFERENCES dbo.enterprise(enterpriseid);


--
-- TOC entry 4791 (class 2606 OID 32170)
-- Name: involvedpartyxproducttype fk9qw1jha2e5bmcbfm5tv9vi4f0; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyxproducttype
    ADD CONSTRAINT fk9qw1jha2e5bmcbfm5tv9vi4f0 FOREIGN KEY (enterpriseid) REFERENCES dbo.enterprise(enterpriseid);


--
-- TOC entry 4631 (class 2606 OID 31380)
-- Name: involvedpartyidentificationtype fk9t4s53f0ihecoo8ufdth1vbq2; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyidentificationtype
    ADD CONSTRAINT fk9t4s53f0ihecoo8ufdth1vbq2 FOREIGN KEY (systemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4771 (class 2606 OID 32070)
-- Name: involvedpartyxinvolvedpartytypesecuritytoken fk9y6c21dewoi7fs477176gh0hv; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyxinvolvedpartytypesecuritytoken
    ADD CONSTRAINT fk9y6c21dewoi7fs477176gh0hv FOREIGN KEY (enterpriseid) REFERENCES dbo.enterprise(enterpriseid);


--
-- TOC entry 4642 (class 2606 OID 31420)
-- Name: involvedpartynametype fka7cnvd016j105uffrbuh94xx5; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartynametype
    ADD CONSTRAINT fka7cnvd016j105uffrbuh94xx5 FOREIGN KEY (activeflagid) REFERENCES dbo.activeflag(activeflagid);


--
-- TOC entry 4735 (class 2606 OID 31890)
-- Name: involvedpartyxinvolvedpartyidentificationtype fka8ayy9j0gddpl8adc2c6ug1y1; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyxinvolvedpartyidentificationtype
    ADD CONSTRAINT fka8ayy9j0gddpl8adc2c6ug1y1 FOREIGN KEY (classificationid) REFERENCES classification.classification(classificationid);


--
-- TOC entry 4643 (class 2606 OID 31425)
-- Name: involvedpartynametype fkaaa9iyxq2koyservqjmrfeqee; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartynametype
    ADD CONSTRAINT fkaaa9iyxq2koyservqjmrfeqee FOREIGN KEY (enterpriseid) REFERENCES dbo.enterprise(enterpriseid);


--
-- TOC entry 4770 (class 2606 OID 32065)
-- Name: involvedpartyxinvolvedpartytypesecuritytoken fkamb8ax10bbah3k81o43u11kod; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyxinvolvedpartytypesecuritytoken
    ADD CONSTRAINT fkamb8ax10bbah3k81o43u11kod FOREIGN KEY (activeflagid) REFERENCES dbo.activeflag(activeflagid);


--
-- TOC entry 4794 (class 2606 OID 32185)
-- Name: involvedpartyxproducttype fkanja7q0nr3e57ilnlo9mualbo; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyxproducttype
    ADD CONSTRAINT fkanja7q0nr3e57ilnlo9mualbo FOREIGN KEY (classificationid) REFERENCES classification.classification(classificationid);


--
-- TOC entry 4776 (class 2606 OID 32095)
-- Name: involvedpartyxproduct fkau2m5a079qpprk5hhhdqum1e2; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyxproduct
    ADD CONSTRAINT fkau2m5a079qpprk5hhhdqum1e2 FOREIGN KEY (activeflagid) REFERENCES dbo.activeflag(activeflagid);


--
-- TOC entry 4720 (class 2606 OID 31815)
-- Name: involvedpartyxclassificationsecuritytoken fkbd75ir0ahamjur4981w6ki6tu; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyxclassificationsecuritytoken
    ADD CONSTRAINT fkbd75ir0ahamjur4981w6ki6tu FOREIGN KEY (originalsourcesystemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4789 (class 2606 OID 32160)
-- Name: involvedpartyxproductsecuritytoken fkbfn2hrolgktevx0ena7ia8gr1; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyxproductsecuritytoken
    ADD CONSTRAINT fkbfn2hrolgktevx0ena7ia8gr1 FOREIGN KEY (involvedpartyxproductid) REFERENCES party.involvedpartyxproduct(involvedpartyxproductid);


--
-- TOC entry 4762 (class 2606 OID 32025)
-- Name: involvedpartyxinvolvedpartysecuritytoken fkbjx132wxvnoasynr6sdwopoiv; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyxinvolvedpartysecuritytoken
    ADD CONSTRAINT fkbjx132wxvnoasynr6sdwopoiv FOREIGN KEY (involvedpartyxinvolvedpartyid) REFERENCES party.involvedpartyxinvolvedparty(involvedpartyxinvolvedpartyid);


--
-- TOC entry 4658 (class 2606 OID 31505)
-- Name: involvedpartynonorganicsecuritytoken fkbo9u2l1rksyb0qw3poycrj3y2; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartynonorganicsecuritytoken
    ADD CONSTRAINT fkbo9u2l1rksyb0qw3poycrj3y2 FOREIGN KEY (originalsourcesystemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4807 (class 2606 OID 32250)
-- Name: involvedpartyxresourceitem fkbtxxv9kh2p77i3qt1nhgi5epp; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyxresourceitem
    ADD CONSTRAINT fkbtxxv9kh2p77i3qt1nhgi5epp FOREIGN KEY (classificationid) REFERENCES classification.classification(classificationid);


--
-- TOC entry 4719 (class 2606 OID 31810)
-- Name: involvedpartyxclassificationsecuritytoken fkbu7cxnreyofsgf9cdr0s3i9a1; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyxclassificationsecuritytoken
    ADD CONSTRAINT fkbu7cxnreyofsgf9cdr0s3i9a1 FOREIGN KEY (enterpriseid) REFERENCES dbo.enterprise(enterpriseid);


--
-- TOC entry 4815 (class 2606 OID 32290)
-- Name: involvedpartyxresourceitemsecuritytoken fkbv1ja6o5cpxl4jpwm7bneb4ys; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyxresourceitemsecuritytoken
    ADD CONSTRAINT fkbv1ja6o5cpxl4jpwm7bneb4ys FOREIGN KEY (involvedpartyxresourceitemid) REFERENCES party.involvedpartyxresourceitem(involvedpartyxresourceitemid);


--
-- TOC entry 4772 (class 2606 OID 32075)
-- Name: involvedpartyxinvolvedpartytypesecuritytoken fkc10kevx7v3jol7lbh6vw0smi4; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyxinvolvedpartytypesecuritytoken
    ADD CONSTRAINT fkc10kevx7v3jol7lbh6vw0smi4 FOREIGN KEY (originalsourcesystemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4754 (class 2606 OID 31985)
-- Name: involvedpartyxinvolvedpartynametypesecuritytoken fkc7m7jk9bba59unhsafmgxbu4f; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyxinvolvedpartynametypesecuritytoken
    ADD CONSTRAINT fkc7m7jk9bba59unhsafmgxbu4f FOREIGN KEY (securitytokenid) REFERENCES security.securitytoken(securitytokenid);


--
-- TOC entry 4697 (class 2606 OID 31700)
-- Name: involvedpartytypesecuritytoken fkc8cf751cac8asi0iu3rkexxt0; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartytypesecuritytoken
    ADD CONSTRAINT fkc8cf751cac8asi0iu3rkexxt0 FOREIGN KEY (systemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4782 (class 2606 OID 32125)
-- Name: involvedpartyxproduct fkchhwxcvsrfev3k76luwrc1di3; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyxproduct
    ADD CONSTRAINT fkchhwxcvsrfev3k76luwrc1di3 FOREIGN KEY (productid) REFERENCES product.product(productid);


--
-- TOC entry 4648 (class 2606 OID 31455)
-- Name: involvedpartynametypesecuritytoken fkclphu45dfvj92ca97kfmhd0il; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartynametypesecuritytoken
    ADD CONSTRAINT fkclphu45dfvj92ca97kfmhd0il FOREIGN KEY (securitytokenid) REFERENCES security.securitytoken(securitytokenid);


--
-- TOC entry 4652 (class 2606 OID 31480)
-- Name: involvedpartynonorganic fkcphls8bnbiwk5rkf278le5kad; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartynonorganic
    ADD CONSTRAINT fkcphls8bnbiwk5rkf278le5kad FOREIGN KEY (systemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4628 (class 2606 OID 31355)
-- Name: involvedparty fkd9g7suhvo150i79rthg96ktwu; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedparty
    ADD CONSTRAINT fkd9g7suhvo150i79rthg96ktwu FOREIGN KEY (enterpriseid) REFERENCES dbo.enterprise(enterpriseid);


--
-- TOC entry 4716 (class 2606 OID 31795)
-- Name: involvedpartyxclassification fkdfqy11c8yvskjx2yu9wa2ay8r; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyxclassification
    ADD CONSTRAINT fkdfqy11c8yvskjx2yu9wa2ay8r FOREIGN KEY (classificationid) REFERENCES classification.classification(classificationid);


--
-- TOC entry 4715 (class 2606 OID 31790)
-- Name: involvedpartyxclassification fkdinundrqqh853d336evxy3bcf; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyxclassification
    ADD CONSTRAINT fkdinundrqqh853d336evxy3bcf FOREIGN KEY (originalsourcesystemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4647 (class 2606 OID 31450)
-- Name: involvedpartynametypesecuritytoken fkdkkd4w22d3p250hyn7vwkrp6a; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartynametypesecuritytoken
    ADD CONSTRAINT fkdkkd4w22d3p250hyn7vwkrp6a FOREIGN KEY (originalsourcesystemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4698 (class 2606 OID 31705)
-- Name: involvedpartytypesecuritytoken fkdtx295xce0hprr5jy1la820eh; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartytypesecuritytoken
    ADD CONSTRAINT fkdtx295xce0hprr5jy1la820eh FOREIGN KEY (involvedpartytypeid) REFERENCES party.involvedpartytype(involvedpartytypeid);


--
-- TOC entry 4637 (class 2606 OID 31400)
-- Name: involvedpartyidentificationtypesecuritytoken fkdvt59k7lx9fobcggw3naabwkc; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyidentificationtypesecuritytoken
    ADD CONSTRAINT fkdvt59k7lx9fobcggw3naabwkc FOREIGN KEY (originalsourcesystemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4703 (class 2606 OID 31730)
-- Name: involvedpartyxaddress fkdwkp9qbashp1p04uip3rg2hls; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyxaddress
    ADD CONSTRAINT fkdwkp9qbashp1p04uip3rg2hls FOREIGN KEY (classificationid) REFERENCES classification.classification(classificationid);


--
-- TOC entry 4675 (class 2606 OID 31595)
-- Name: involvedpartyorganictype fke1juu2g32hsp6fwtfw7f9d3jx; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyorganictype
    ADD CONSTRAINT fke1juu2g32hsp6fwtfw7f9d3jx FOREIGN KEY (originalsourcesystemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4753 (class 2606 OID 31980)
-- Name: involvedpartyxinvolvedpartynametypesecuritytoken fkeafj6qgxpt157ayq3csmbo5sj; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyxinvolvedpartynametypesecuritytoken
    ADD CONSTRAINT fkeafj6qgxpt157ayq3csmbo5sj FOREIGN KEY (originalsourcesystemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4802 (class 2606 OID 32225)
-- Name: involvedpartyxproducttypesecuritytoken fkeaiel3pwjpomb5v1gyvnswnhi; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyxproducttypesecuritytoken
    ADD CONSTRAINT fkeaiel3pwjpomb5v1gyvnswnhi FOREIGN KEY (involvedpartyxproducttypeid) REFERENCES party.involvedpartyxproducttype(involvedpartyxproducttypeid);


--
-- TOC entry 4801 (class 2606 OID 32220)
-- Name: involvedpartyxproducttypesecuritytoken fkebb1xe5bii4fw0alp6uk7yx3g; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyxproducttypesecuritytoken
    ADD CONSTRAINT fkebb1xe5bii4fw0alp6uk7yx3g FOREIGN KEY (systemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4758 (class 2606 OID 32005)
-- Name: involvedpartyxinvolvedpartysecuritytoken fkec644efp6gtbnydr4qmfmytrn; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyxinvolvedpartysecuritytoken
    ADD CONSTRAINT fkec644efp6gtbnydr4qmfmytrn FOREIGN KEY (enterpriseid) REFERENCES dbo.enterprise(enterpriseid);


--
-- TOC entry 4823 (class 2606 OID 32330)
-- Name: involvedpartyxrules fkeiak0rrb2rgx9gest89clwauh; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyxrules
    ADD CONSTRAINT fkeiak0rrb2rgx9gest89clwauh FOREIGN KEY (involvedpartyxrulesid) REFERENCES party.involvedpartyxrules(involvedpartyxrulesid);


--
-- TOC entry 4681 (class 2606 OID 31620)
-- Name: involvedpartyorganictypesecuritytoken fkejueyv4755b9ec8xoyb7wpu4x; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyorganictypesecuritytoken
    ADD CONSTRAINT fkejueyv4755b9ec8xoyb7wpu4x FOREIGN KEY (systemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4699 (class 2606 OID 31710)
-- Name: involvedpartyxaddress fkejxbg01t325qgchlwnwmpl7a0; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyxaddress
    ADD CONSTRAINT fkejxbg01t325qgchlwnwmpl7a0 FOREIGN KEY (activeflagid) REFERENCES dbo.activeflag(activeflagid);


--
-- TOC entry 4765 (class 2606 OID 32040)
-- Name: involvedpartyxinvolvedpartytype fkelc5v745ispspjwh2cwse92bh; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyxinvolvedpartytype
    ADD CONSTRAINT fkelc5v745ispspjwh2cwse92bh FOREIGN KEY (systemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4825 (class 2606 OID 32340)
-- Name: involvedpartyxrulessecuritytoken fker91inm2bcxuigrl4g4051u4o; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyxrulessecuritytoken
    ADD CONSTRAINT fker91inm2bcxuigrl4g4051u4o FOREIGN KEY (enterpriseid) REFERENCES dbo.enterprise(enterpriseid);


--
-- TOC entry 4632 (class 2606 OID 31375)
-- Name: involvedpartyidentificationtype fkeuxx0k0eufg56jpbymmg0wnju; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyidentificationtype
    ADD CONSTRAINT fkeuxx0k0eufg56jpbymmg0wnju FOREIGN KEY (enterpriseid) REFERENCES dbo.enterprise(enterpriseid);


--
-- TOC entry 4670 (class 2606 OID 31565)
-- Name: involvedpartyorganicsecuritytoken fkeyo56crc5eg0qtbt3o79rblnp; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyorganicsecuritytoken
    ADD CONSTRAINT fkeyo56crc5eg0qtbt3o79rblnp FOREIGN KEY (securitytokenid) REFERENCES security.securitytoken(securitytokenid);


--
-- TOC entry 4701 (class 2606 OID 31720)
-- Name: involvedpartyxaddress fkf0jjy7kksugbaamt8638ktw8a; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyxaddress
    ADD CONSTRAINT fkf0jjy7kksugbaamt8638ktw8a FOREIGN KEY (systemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4809 (class 2606 OID 32260)
-- Name: involvedpartyxresourceitem fkf1eri71wcx822turop42m1raw; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyxresourceitem
    ADD CONSTRAINT fkf1eri71wcx822turop42m1raw FOREIGN KEY (resourceitemid) REFERENCES resource.resourceitem(resourceitemid);


--
-- TOC entry 4663 (class 2606 OID 31535)
-- Name: involvedpartyorganic fkf7p05lygebyxjhhtrkbn2n2md; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyorganic
    ADD CONSTRAINT fkf7p05lygebyxjhhtrkbn2n2md FOREIGN KEY (systemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4790 (class 2606 OID 32165)
-- Name: involvedpartyxproducttype fkfwrjy39tsf1ctjt6rx6siphyo; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyxproducttype
    ADD CONSTRAINT fkfwrjy39tsf1ctjt6rx6siphyo FOREIGN KEY (activeflagid) REFERENCES dbo.activeflag(activeflagid);


--
-- TOC entry 4667 (class 2606 OID 31550)
-- Name: involvedpartyorganicsecuritytoken fkg9iyrrj29vcyc3b6sxsgy95on; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyorganicsecuritytoken
    ADD CONSTRAINT fkg9iyrrj29vcyc3b6sxsgy95on FOREIGN KEY (activeflagid) REFERENCES dbo.activeflag(activeflagid);


--
-- TOC entry 4786 (class 2606 OID 32145)
-- Name: involvedpartyxproductsecuritytoken fkghbsln22v6fh0b1jdcblved0n; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyxproductsecuritytoken
    ADD CONSTRAINT fkghbsln22v6fh0b1jdcblved0n FOREIGN KEY (originalsourcesystemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4653 (class 2606 OID 31490)
-- Name: involvedpartynonorganic fkgjctovxjg820869d686h867uv; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartynonorganic
    ADD CONSTRAINT fkgjctovxjg820869d686h867uv FOREIGN KEY (involvedpartynonorganicid) REFERENCES party.involvedparty(involvedpartyid);


--
-- TOC entry 4685 (class 2606 OID 31640)
-- Name: involvedpartysecuritytoken fkgtrgr11h04p33g04pukt5x1sy; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartysecuritytoken
    ADD CONSTRAINT fkgtrgr11h04p33g04pukt5x1sy FOREIGN KEY (originalsourcesystemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4757 (class 2606 OID 32000)
-- Name: involvedpartyxinvolvedpartysecuritytoken fkgx4f05xe52w7yxl6f4mgycsyu; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyxinvolvedpartysecuritytoken
    ADD CONSTRAINT fkgx4f05xe52w7yxl6f4mgycsyu FOREIGN KEY (activeflagid) REFERENCES dbo.activeflag(activeflagid);


--
-- TOC entry 4645 (class 2606 OID 31440)
-- Name: involvedpartynametypesecuritytoken fkh3e0lgk341y3qrbdp9y7eo6uf; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartynametypesecuritytoken
    ADD CONSTRAINT fkh3e0lgk341y3qrbdp9y7eo6uf FOREIGN KEY (activeflagid) REFERENCES dbo.activeflag(activeflagid);


--
-- TOC entry 4691 (class 2606 OID 31660)
-- Name: involvedpartytype fkh8ct5p1uw4f7s19svr7525boe; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartytype
    ADD CONSTRAINT fkh8ct5p1uw4f7s19svr7525boe FOREIGN KEY (activeflagid) REFERENCES dbo.activeflag(activeflagid);


--
-- TOC entry 4822 (class 2606 OID 32325)
-- Name: involvedpartyxrules fkhd6xjhmmugixhj5pl6fdocdwy; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyxrules
    ADD CONSTRAINT fkhd6xjhmmugixhj5pl6fdocdwy FOREIGN KEY (rulesid) REFERENCES rules.rules(rulesid);


--
-- TOC entry 4804 (class 2606 OID 32235)
-- Name: involvedpartyxresourceitem fkhkmp4ty2oydnlut2dtgkv8se8; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyxresourceitem
    ADD CONSTRAINT fkhkmp4ty2oydnlut2dtgkv8se8 FOREIGN KEY (enterpriseid) REFERENCES dbo.enterprise(enterpriseid);


--
-- TOC entry 4705 (class 2606 OID 31740)
-- Name: involvedpartyxaddress fki1pmmm070ua8yd0xrk19qq206; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyxaddress
    ADD CONSTRAINT fki1pmmm070ua8yd0xrk19qq206 FOREIGN KEY (involvedpartyid) REFERENCES party.involvedparty(involvedpartyid);


--
-- TOC entry 4688 (class 2606 OID 31655)
-- Name: involvedpartysecuritytoken fkialc3kro04ovq8q26gg2hce1s; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartysecuritytoken
    ADD CONSTRAINT fkialc3kro04ovq8q26gg2hce1s FOREIGN KEY (involvedpartyid) REFERENCES party.involvedparty(involvedpartyid);


--
-- TOC entry 4749 (class 2606 OID 31960)
-- Name: involvedpartyxinvolvedpartynametype fkiatc66e8wafha0n8xgeslpypc; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyxinvolvedpartynametype
    ADD CONSTRAINT fkiatc66e8wafha0n8xgeslpypc FOREIGN KEY (involvedpartyid) REFERENCES party.involvedparty(involvedpartyid);


--
-- TOC entry 4710 (class 2606 OID 31765)
-- Name: involvedpartyxaddresssecuritytoken fkietr95ln1k6gcutfwaqg26k24; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyxaddresssecuritytoken
    ADD CONSTRAINT fkietr95ln1k6gcutfwaqg26k24 FOREIGN KEY (systemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4660 (class 2606 OID 31515)
-- Name: involvedpartynonorganicsecuritytoken fkii44sojyhs3kd1d607mnqldov; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartynonorganicsecuritytoken
    ADD CONSTRAINT fkii44sojyhs3kd1d607mnqldov FOREIGN KEY (systemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4728 (class 2606 OID 31855)
-- Name: involvedpartyxinvolvedparty fkisqmvjyuuk8aqaogyeipmxtmi; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyxinvolvedparty
    ADD CONSTRAINT fkisqmvjyuuk8aqaogyeipmxtmi FOREIGN KEY (classificationid) REFERENCES classification.classification(classificationid);


--
-- TOC entry 4764 (class 2606 OID 32035)
-- Name: involvedpartyxinvolvedpartytype fkj0mx9sjb7tx19f8y6g6aha7ae; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyxinvolvedpartytype
    ADD CONSTRAINT fkj0mx9sjb7tx19f8y6g6aha7ae FOREIGN KEY (enterpriseid) REFERENCES dbo.enterprise(enterpriseid);


--
-- TOC entry 4695 (class 2606 OID 31690)
-- Name: involvedpartytypesecuritytoken fkjhy9dh04vj80o53adnsk1xi9t; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartytypesecuritytoken
    ADD CONSTRAINT fkjhy9dh04vj80o53adnsk1xi9t FOREIGN KEY (originalsourcesystemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4800 (class 2606 OID 32215)
-- Name: involvedpartyxproducttypesecuritytoken fkjkwmptgw4rcxlvjycdbevrdbh; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyxproducttypesecuritytoken
    ADD CONSTRAINT fkjkwmptgw4rcxlvjycdbevrdbh FOREIGN KEY (securitytokenid) REFERENCES security.securitytoken(securitytokenid);


--
-- TOC entry 4806 (class 2606 OID 32245)
-- Name: involvedpartyxresourceitem fkjo97cd2ria6n18uyt9y9n1k40; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyxresourceitem
    ADD CONSTRAINT fkjo97cd2ria6n18uyt9y9n1k40 FOREIGN KEY (originalsourcesystemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4795 (class 2606 OID 32190)
-- Name: involvedpartyxproducttype fkjoxnotjs00eoek4eboy9ob7x3; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyxproducttype
    ADD CONSTRAINT fkjoxnotjs00eoek4eboy9ob7x3 FOREIGN KEY (involvedpartyid) REFERENCES party.involvedparty(involvedpartyid);


--
-- TOC entry 4796 (class 2606 OID 32195)
-- Name: involvedpartyxproducttype fkk7gt22kjj7aajqm1i61s53dto; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyxproducttype
    ADD CONSTRAINT fkk7gt22kjj7aajqm1i61s53dto FOREIGN KEY (producttypeid) REFERENCES product.producttype(producttypeid);


--
-- TOC entry 4733 (class 2606 OID 31880)
-- Name: involvedpartyxinvolvedpartyidentificationtype fkkb48h7qs2y9d208ho8l8lu5my; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyxinvolvedpartyidentificationtype
    ADD CONSTRAINT fkkb48h7qs2y9d208ho8l8lu5my FOREIGN KEY (systemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4633 (class 2606 OID 31370)
-- Name: involvedpartyidentificationtype fkkeqqy519age78syf29vttmwu9; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyidentificationtype
    ADD CONSTRAINT fkkeqqy519age78syf29vttmwu9 FOREIGN KEY (activeflagid) REFERENCES dbo.activeflag(activeflagid);


--
-- TOC entry 4678 (class 2606 OID 31605)
-- Name: involvedpartyorganictypesecuritytoken fkkj7fqfd2urqo5hgy3vxagu45t; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyorganictypesecuritytoken
    ADD CONSTRAINT fkkj7fqfd2urqo5hgy3vxagu45t FOREIGN KEY (enterpriseid) REFERENCES dbo.enterprise(enterpriseid);


--
-- TOC entry 4659 (class 2606 OID 31510)
-- Name: involvedpartynonorganicsecuritytoken fkkkks9t7wiiiku7knnyo6vvc3r; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartynonorganicsecuritytoken
    ADD CONSTRAINT fkkkks9t7wiiiku7knnyo6vvc3r FOREIGN KEY (securitytokenid) REFERENCES security.securitytoken(securitytokenid);


--
-- TOC entry 4769 (class 2606 OID 32060)
-- Name: involvedpartyxinvolvedpartytype fkkm9ehabqxotucg1e13vhmcgge; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyxinvolvedpartytype
    ADD CONSTRAINT fkkm9ehabqxotucg1e13vhmcgge FOREIGN KEY (involvedpartytypeid) REFERENCES party.involvedpartytype(involvedpartytypeid);


--
-- TOC entry 4707 (class 2606 OID 31750)
-- Name: involvedpartyxaddresssecuritytoken fkkwbmkde209srp4k0q0s7cd1u3; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyxaddresssecuritytoken
    ADD CONSTRAINT fkkwbmkde209srp4k0q0s7cd1u3 FOREIGN KEY (enterpriseid) REFERENCES dbo.enterprise(enterpriseid);


--
-- TOC entry 4747 (class 2606 OID 31950)
-- Name: involvedpartyxinvolvedpartynametype fkkxftjgdli2l9ro94w41wclkfx; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyxinvolvedpartynametype
    ADD CONSTRAINT fkkxftjgdli2l9ro94w41wclkfx FOREIGN KEY (originalsourcesystemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4644 (class 2606 OID 31430)
-- Name: involvedpartynametype fkl18vvxbpd5f6oq57wy3jtnbp8; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartynametype
    ADD CONSTRAINT fkl18vvxbpd5f6oq57wy3jtnbp8 FOREIGN KEY (systemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4751 (class 2606 OID 31970)
-- Name: involvedpartyxinvolvedpartynametypesecuritytoken fkl8eohlltm8o9u06rc0tqtf5io; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyxinvolvedpartynametypesecuritytoken
    ADD CONSTRAINT fkl8eohlltm8o9u06rc0tqtf5io FOREIGN KEY (activeflagid) REFERENCES dbo.activeflag(activeflagid);


--
-- TOC entry 4654 (class 2606 OID 31485)
-- Name: involvedpartynonorganic fklb5mxasbqff0f09q3tsqkgsi1; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartynonorganic
    ADD CONSTRAINT fklb5mxasbqff0f09q3tsqkgsi1 FOREIGN KEY (originalsourcesystemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4709 (class 2606 OID 31760)
-- Name: involvedpartyxaddresssecuritytoken fklcmcbwu95pa7hnchv2tw2l6e4; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyxaddresssecuritytoken
    ADD CONSTRAINT fklcmcbwu95pa7hnchv2tw2l6e4 FOREIGN KEY (securitytokenid) REFERENCES security.securitytoken(securitytokenid);


--
-- TOC entry 4724 (class 2606 OID 31835)
-- Name: involvedpartyxinvolvedparty fklrica71csov4p3987b56q8vxg; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyxinvolvedparty
    ADD CONSTRAINT fklrica71csov4p3987b56q8vxg FOREIGN KEY (activeflagid) REFERENCES dbo.activeflag(activeflagid);


--
-- TOC entry 4682 (class 2606 OID 31625)
-- Name: involvedpartyorganictypesecuritytoken fkluot9f3p0qcl04j9vytxoy5w2; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyorganictypesecuritytoken
    ADD CONSTRAINT fkluot9f3p0qcl04j9vytxoy5w2 FOREIGN KEY (involvedpartyorganictypeid) REFERENCES party.involvedpartyorganictype(involvedpartyorganictypeid);


--
-- TOC entry 4785 (class 2606 OID 32140)
-- Name: involvedpartyxproductsecuritytoken fklvpxfkxf414rr3h2hd7qfbt6x; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyxproductsecuritytoken
    ADD CONSTRAINT fklvpxfkxf414rr3h2hd7qfbt6x FOREIGN KEY (enterpriseid) REFERENCES dbo.enterprise(enterpriseid);


--
-- TOC entry 4634 (class 2606 OID 31385)
-- Name: involvedpartyidentificationtype fklxima3r9ohrv77gnodycrb2su; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyidentificationtype
    ADD CONSTRAINT fklxima3r9ohrv77gnodycrb2su FOREIGN KEY (originalsourcesystemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4826 (class 2606 OID 32345)
-- Name: involvedpartyxrulessecuritytoken fkm4u2pspis8jhbsy7bpti76wsd; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyxrulessecuritytoken
    ADD CONSTRAINT fkm4u2pspis8jhbsy7bpti76wsd FOREIGN KEY (originalsourcesystemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4780 (class 2606 OID 32115)
-- Name: involvedpartyxproduct fkm5l1oipl6mvv54tc6xx24yk06; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyxproduct
    ADD CONSTRAINT fkm5l1oipl6mvv54tc6xx24yk06 FOREIGN KEY (classificationid) REFERENCES classification.classification(classificationid);


--
-- TOC entry 4812 (class 2606 OID 32275)
-- Name: involvedpartyxresourceitemsecuritytoken fkm62y4d4wr0udqi7iexcc3gaa5; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyxresourceitemsecuritytoken
    ADD CONSTRAINT fkm62y4d4wr0udqi7iexcc3gaa5 FOREIGN KEY (originalsourcesystemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4664 (class 2606 OID 31525)
-- Name: involvedpartyorganic fkmkbm0yyns5o1ll48bo5yce5sc; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyorganic
    ADD CONSTRAINT fkmkbm0yyns5o1ll48bo5yce5sc FOREIGN KEY (activeflagid) REFERENCES dbo.activeflag(activeflagid);


--
-- TOC entry 4755 (class 2606 OID 31990)
-- Name: involvedpartyxinvolvedpartynametypesecuritytoken fkmkjv3992oc9plpbavw8qwtqxx; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyxinvolvedpartynametypesecuritytoken
    ADD CONSTRAINT fkmkjv3992oc9plpbavw8qwtqxx FOREIGN KEY (systemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4718 (class 2606 OID 31805)
-- Name: involvedpartyxclassificationsecuritytoken fkmsyb25jnjb9yam4x4779256w2; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyxclassificationsecuritytoken
    ADD CONSTRAINT fkmsyb25jnjb9yam4x4779256w2 FOREIGN KEY (activeflagid) REFERENCES dbo.activeflag(activeflagid);


--
-- TOC entry 4742 (class 2606 OID 31925)
-- Name: involvedpartyxinvolvedpartyidentificationtypesecuritytoken fkmt218lxijt1k3knyl48ebot0f; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyxinvolvedpartyidentificationtypesecuritytoken
    ADD CONSTRAINT fkmt218lxijt1k3knyl48ebot0f FOREIGN KEY (systemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4668 (class 2606 OID 31555)
-- Name: involvedpartyorganicsecuritytoken fkmyjwts72ixlq4gmviiba55a6e; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyorganicsecuritytoken
    ADD CONSTRAINT fkmyjwts72ixlq4gmviiba55a6e FOREIGN KEY (enterpriseid) REFERENCES dbo.enterprise(enterpriseid);


--
-- TOC entry 4746 (class 2606 OID 31945)
-- Name: involvedpartyxinvolvedpartynametype fkn2foayal7pve60wm8tps4eq67; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyxinvolvedpartynametype
    ADD CONSTRAINT fkn2foayal7pve60wm8tps4eq67 FOREIGN KEY (systemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4763 (class 2606 OID 32030)
-- Name: involvedpartyxinvolvedpartytype fkn68mf1pa4afxo1tx2q83kksx4; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyxinvolvedpartytype
    ADD CONSTRAINT fkn68mf1pa4afxo1tx2q83kksx4 FOREIGN KEY (activeflagid) REFERENCES dbo.activeflag(activeflagid);


--
-- TOC entry 4788 (class 2606 OID 32155)
-- Name: involvedpartyxproductsecuritytoken fkn7l4i9kxjbtg61rjqwin84k52; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyxproductsecuritytoken
    ADD CONSTRAINT fkn7l4i9kxjbtg61rjqwin84k52 FOREIGN KEY (systemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4736 (class 2606 OID 31895)
-- Name: involvedpartyxinvolvedpartyidentificationtype fkn7qd136m19by1jp2xgntox5nr; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyxinvolvedpartyidentificationtype
    ADD CONSTRAINT fkn7qd136m19by1jp2xgntox5nr FOREIGN KEY (involvedpartyid) REFERENCES party.involvedparty(involvedpartyid);


--
-- TOC entry 4684 (class 2606 OID 31635)
-- Name: involvedpartysecuritytoken fknkgdmsl2don2yegx2qfgnykqw; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartysecuritytoken
    ADD CONSTRAINT fknkgdmsl2don2yegx2qfgnykqw FOREIGN KEY (enterpriseid) REFERENCES dbo.enterprise(enterpriseid);


--
-- TOC entry 4756 (class 2606 OID 31995)
-- Name: involvedpartyxinvolvedpartynametypesecuritytoken fknl7qjhkyalhn6skoouq0jt8pq; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyxinvolvedpartynametypesecuritytoken
    ADD CONSTRAINT fknl7qjhkyalhn6skoouq0jt8pq FOREIGN KEY (involvedpartyxinvolvedpartynametypeid) REFERENCES party.involvedpartyxinvolvedpartynametype(involvedpartyxinvolvedpartynametypeid);


--
-- TOC entry 4665 (class 2606 OID 31530)
-- Name: involvedpartyorganic fknyksrtkiekj3cd65vtj6d641e; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyorganic
    ADD CONSTRAINT fknyksrtkiekj3cd65vtj6d641e FOREIGN KEY (enterpriseid) REFERENCES dbo.enterprise(enterpriseid);


--
-- TOC entry 4683 (class 2606 OID 31630)
-- Name: involvedpartysecuritytoken fko24pnibqq5rx8t4soy88rynmd; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartysecuritytoken
    ADD CONSTRAINT fko24pnibqq5rx8t4soy88rynmd FOREIGN KEY (activeflagid) REFERENCES dbo.activeflag(activeflagid);


--
-- TOC entry 4781 (class 2606 OID 32120)
-- Name: involvedpartyxproduct fko98uqwvsmtmebl7e64anytc63; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyxproduct
    ADD CONSTRAINT fko98uqwvsmtmebl7e64anytc63 FOREIGN KEY (involvedpartyid) REFERENCES party.involvedparty(involvedpartyid);


--
-- TOC entry 4727 (class 2606 OID 31850)
-- Name: involvedpartyxinvolvedparty fkocrvxgjfr7ogilbsg5ija9raf; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyxinvolvedparty
    ADD CONSTRAINT fkocrvxgjfr7ogilbsg5ija9raf FOREIGN KEY (originalsourcesystemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4734 (class 2606 OID 31885)
-- Name: involvedpartyxinvolvedpartyidentificationtype fkoj896cehdnvqta4i4psp2g5c; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyxinvolvedpartyidentificationtype
    ADD CONSTRAINT fkoj896cehdnvqta4i4psp2g5c FOREIGN KEY (originalsourcesystemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4657 (class 2606 OID 31500)
-- Name: involvedpartynonorganicsecuritytoken fkp0jkfk12oi63j8blb7dvepu5i; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartynonorganicsecuritytoken
    ADD CONSTRAINT fkp0jkfk12oi63j8blb7dvepu5i FOREIGN KEY (enterpriseid) REFERENCES dbo.enterprise(enterpriseid);


--
-- TOC entry 4759 (class 2606 OID 32010)
-- Name: involvedpartyxinvolvedpartysecuritytoken fkp3f6axkd1puang58hu44ll5dh; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyxinvolvedpartysecuritytoken
    ADD CONSTRAINT fkp3f6axkd1puang58hu44ll5dh FOREIGN KEY (originalsourcesystemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4777 (class 2606 OID 32100)
-- Name: involvedpartyxproduct fkp5ixt88una33viktp0w23kwj6; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyxproduct
    ADD CONSTRAINT fkp5ixt88una33viktp0w23kwj6 FOREIGN KEY (enterpriseid) REFERENCES dbo.enterprise(enterpriseid);


--
-- TOC entry 4811 (class 2606 OID 32270)
-- Name: involvedpartyxresourceitemsecuritytoken fkp8fhw7rn2g46bjsnhvss90xt0; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyxresourceitemsecuritytoken
    ADD CONSTRAINT fkp8fhw7rn2g46bjsnhvss90xt0 FOREIGN KEY (enterpriseid) REFERENCES dbo.enterprise(enterpriseid);


--
-- TOC entry 4766 (class 2606 OID 32045)
-- Name: involvedpartyxinvolvedpartytype fkpfkqk9ya2eb37pvaq9y6pwesj; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyxinvolvedpartytype
    ADD CONSTRAINT fkpfkqk9ya2eb37pvaq9y6pwesj FOREIGN KEY (originalsourcesystemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4744 (class 2606 OID 31935)
-- Name: involvedpartyxinvolvedpartynametype fkpi2yp7u241j1k9an8b0mttcib; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyxinvolvedpartynametype
    ADD CONSTRAINT fkpi2yp7u241j1k9an8b0mttcib FOREIGN KEY (activeflagid) REFERENCES dbo.activeflag(activeflagid);


--
-- TOC entry 4741 (class 2606 OID 31920)
-- Name: involvedpartyxinvolvedpartyidentificationtypesecuritytoken fkpk4kkstfg7ebwsy3y9pmt6sia; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyxinvolvedpartyidentificationtypesecuritytoken
    ADD CONSTRAINT fkpk4kkstfg7ebwsy3y9pmt6sia FOREIGN KEY (securitytokenid) REFERENCES security.securitytoken(securitytokenid);


--
-- TOC entry 4629 (class 2606 OID 31365)
-- Name: involvedparty fkpni39h3ejdgolqlvxwoltisl2; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedparty
    ADD CONSTRAINT fkpni39h3ejdgolqlvxwoltisl2 FOREIGN KEY (originalsourcesystemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4687 (class 2606 OID 31650)
-- Name: involvedpartysecuritytoken fkpogqfy578mwtlm6p81g0btv3n; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartysecuritytoken
    ADD CONSTRAINT fkpogqfy578mwtlm6p81g0btv3n FOREIGN KEY (systemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4676 (class 2606 OID 31585)
-- Name: involvedpartyorganictype fkq2twef17pvkati3vquevv6w8c; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyorganictype
    ADD CONSTRAINT fkq2twef17pvkati3vquevv6w8c FOREIGN KEY (enterpriseid) REFERENCES dbo.enterprise(enterpriseid);


--
-- TOC entry 4725 (class 2606 OID 31840)
-- Name: involvedpartyxinvolvedparty fkq53n0bblmmvb6j4n4ub3r9tea; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyxinvolvedparty
    ADD CONSTRAINT fkq53n0bblmmvb6j4n4ub3r9tea FOREIGN KEY (enterpriseid) REFERENCES dbo.enterprise(enterpriseid);


--
-- TOC entry 4630 (class 2606 OID 31350)
-- Name: involvedparty fkq6epu7tek9j6nfmtx80i7jv55; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedparty
    ADD CONSTRAINT fkq6epu7tek9j6nfmtx80i7jv55 FOREIGN KEY (activeflagid) REFERENCES dbo.activeflag(activeflagid);


--
-- TOC entry 4714 (class 2606 OID 31785)
-- Name: involvedpartyxclassification fkq80p3cmg58mvp6pb85vee4twn; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyxclassification
    ADD CONSTRAINT fkq80p3cmg58mvp6pb85vee4twn FOREIGN KEY (systemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4821 (class 2606 OID 32320)
-- Name: involvedpartyxrules fkqcygcbsndr9g4owqwp22nkrdg; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyxrules
    ADD CONSTRAINT fkqcygcbsndr9g4owqwp22nkrdg FOREIGN KEY (involvedpartyid) REFERENCES party.involvedparty(involvedpartyid);


--
-- TOC entry 4829 (class 2606 OID 32360)
-- Name: involvedpartyxrulessecuritytoken fkqd2pskmew84uw4kpp9tscnftg; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyxrulessecuritytoken
    ADD CONSTRAINT fkqd2pskmew84uw4kpp9tscnftg FOREIGN KEY (involvedpartyxrulesid) REFERENCES party.involvedpartyxrules(involvedpartyxrulesid);


--
-- TOC entry 4805 (class 2606 OID 32240)
-- Name: involvedpartyxresourceitem fkqq23awk6k3o0swj127dcgd0st; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyxresourceitem
    ADD CONSTRAINT fkqq23awk6k3o0swj127dcgd0st FOREIGN KEY (systemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4798 (class 2606 OID 32205)
-- Name: involvedpartyxproducttypesecuritytoken fkqt1rigtuob9p89owrlanmtlt2; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyxproducttypesecuritytoken
    ADD CONSTRAINT fkqt1rigtuob9p89owrlanmtlt2 FOREIGN KEY (enterpriseid) REFERENCES dbo.enterprise(enterpriseid);


--
-- TOC entry 4692 (class 2606 OID 31670)
-- Name: involvedpartytype fkqwx9fppibmcrytwu4t30w0f7e; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartytype
    ADD CONSTRAINT fkqwx9fppibmcrytwu4t30w0f7e FOREIGN KEY (systemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4739 (class 2606 OID 31910)
-- Name: involvedpartyxinvolvedpartyidentificationtypesecuritytoken fkqx276013uinthqhdmr1lqpucn; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyxinvolvedpartyidentificationtypesecuritytoken
    ADD CONSTRAINT fkqx276013uinthqhdmr1lqpucn FOREIGN KEY (enterpriseid) REFERENCES dbo.enterprise(enterpriseid);


--
-- TOC entry 4680 (class 2606 OID 31615)
-- Name: involvedpartyorganictypesecuritytoken fkr6a971hibsnykjeadhdgnmwm6; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyorganictypesecuritytoken
    ADD CONSTRAINT fkr6a971hibsnykjeadhdgnmwm6 FOREIGN KEY (securitytokenid) REFERENCES security.securitytoken(securitytokenid);


--
-- TOC entry 4649 (class 2606 OID 31460)
-- Name: involvedpartynametypesecuritytoken fkr9iq4s6tpt0gt97prluo7sia; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartynametypesecuritytoken
    ADD CONSTRAINT fkr9iq4s6tpt0gt97prluo7sia FOREIGN KEY (systemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4677 (class 2606 OID 31600)
-- Name: involvedpartyorganictypesecuritytoken fkrbdy36uy6fi319o482uvgbic1; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyorganictypesecuritytoken
    ADD CONSTRAINT fkrbdy36uy6fi319o482uvgbic1 FOREIGN KEY (activeflagid) REFERENCES dbo.activeflag(activeflagid);


--
-- TOC entry 4793 (class 2606 OID 32180)
-- Name: involvedpartyxproducttype fkrdm87u0ipc3rwnmd9jk22bwrk; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyxproducttype
    ADD CONSTRAINT fkrdm87u0ipc3rwnmd9jk22bwrk FOREIGN KEY (originalsourcesystemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4671 (class 2606 OID 31570)
-- Name: involvedpartyorganicsecuritytoken fks4ev0kg4ufyi8iw19xqwn2fxb; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyorganicsecuritytoken
    ADD CONSTRAINT fks4ev0kg4ufyi8iw19xqwn2fxb FOREIGN KEY (systemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4778 (class 2606 OID 32105)
-- Name: involvedpartyxproduct fks9ps3fhh31svi0qpmwv3utwww; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyxproduct
    ADD CONSTRAINT fks9ps3fhh31svi0qpmwv3utwww FOREIGN KEY (systemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4666 (class 2606 OID 31545)
-- Name: involvedpartyorganic fks9ty7u1vtfr0m9cu70ecqfmio; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyorganic
    ADD CONSTRAINT fks9ty7u1vtfr0m9cu70ecqfmio FOREIGN KEY (involvedpartyorganicid) REFERENCES party.involvedparty(involvedpartyid);


--
-- TOC entry 4819 (class 2606 OID 32310)
-- Name: involvedpartyxrules fkscjxiiwoomhsis6829bfcljai; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyxrules
    ADD CONSTRAINT fkscjxiiwoomhsis6829bfcljai FOREIGN KEY (originalsourcesystemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4797 (class 2606 OID 32200)
-- Name: involvedpartyxproducttypesecuritytoken fksidcxt1uldnhq44dumo7lesno; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyxproducttypesecuritytoken
    ADD CONSTRAINT fksidcxt1uldnhq44dumo7lesno FOREIGN KEY (activeflagid) REFERENCES dbo.activeflag(activeflagid);


--
-- TOC entry 4723 (class 2606 OID 31830)
-- Name: involvedpartyxclassificationsecuritytoken fksmurlugdvahy37fyxobdauy42; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyxclassificationsecuritytoken
    ADD CONSTRAINT fksmurlugdvahy37fyxobdauy42 FOREIGN KEY (involvedpartyxclassificationid) REFERENCES party.involvedpartyxclassification(involvedpartyxclassificationid);


--
-- TOC entry 4693 (class 2606 OID 31680)
-- Name: involvedpartytypesecuritytoken fksoyq6n0n6er6eyhn229l4oxu6; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartytypesecuritytoken
    ADD CONSTRAINT fksoyq6n0n6er6eyhn229l4oxu6 FOREIGN KEY (activeflagid) REFERENCES dbo.activeflag(activeflagid);


--
-- TOC entry 4639 (class 2606 OID 31410)
-- Name: involvedpartyidentificationtypesecuritytoken fkspj0etctwf7i8sj0gu0d6tvs8; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyidentificationtypesecuritytoken
    ADD CONSTRAINT fkspj0etctwf7i8sj0gu0d6tvs8 FOREIGN KEY (systemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4779 (class 2606 OID 32110)
-- Name: involvedpartyxproduct fkstmxsovkkg83xipa6jb1v9tqv; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyxproduct
    ADD CONSTRAINT fkstmxsovkkg83xipa6jb1v9tqv FOREIGN KEY (originalsourcesystemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4672 (class 2606 OID 31575)
-- Name: involvedpartyorganicsecuritytoken fkt03i6kwyod9krg1cpoki9kc5x; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyorganicsecuritytoken
    ADD CONSTRAINT fkt03i6kwyod9krg1cpoki9kc5x FOREIGN KEY (involvedpartyorganicid) REFERENCES party.involvedpartyorganic(involvedpartyorganicid);


--
-- TOC entry 4655 (class 2606 OID 31470)
-- Name: involvedpartynonorganic fkt3m2esc7actxvqhmovs8wly1p; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartynonorganic
    ADD CONSTRAINT fkt3m2esc7actxvqhmovs8wly1p FOREIGN KEY (activeflagid) REFERENCES dbo.activeflag(activeflagid);


--
-- TOC entry 4638 (class 2606 OID 31405)
-- Name: involvedpartyidentificationtypesecuritytoken fkt53nic14klq9pmlooadcird6a; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyidentificationtypesecuritytoken
    ADD CONSTRAINT fkt53nic14klq9pmlooadcird6a FOREIGN KEY (securitytokenid) REFERENCES security.securitytoken(securitytokenid);


--
-- TOC entry 4750 (class 2606 OID 31965)
-- Name: involvedpartyxinvolvedpartynametype fkt5o5c0529tdsv531mn84tmm9f; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyxinvolvedpartynametype
    ADD CONSTRAINT fkt5o5c0529tdsv531mn84tmm9f FOREIGN KEY (involvedpartynametypeid) REFERENCES party.involvedpartynametype(involvedpartynametypeid);


--
-- TOC entry 4731 (class 2606 OID 31870)
-- Name: involvedpartyxinvolvedpartyidentificationtype fktdfx6obcjum68sqbp0u7efean; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyxinvolvedpartyidentificationtype
    ADD CONSTRAINT fktdfx6obcjum68sqbp0u7efean FOREIGN KEY (activeflagid) REFERENCES dbo.activeflag(activeflagid);


--
-- TOC entry 4748 (class 2606 OID 31955)
-- Name: involvedpartyxinvolvedpartynametype fktl0dvsfwf7kt9mg6hean9j9xo; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyxinvolvedpartynametype
    ADD CONSTRAINT fktl0dvsfwf7kt9mg6hean9j9xo FOREIGN KEY (classificationid) REFERENCES classification.classification(classificationid);


--
-- TOC entry 4738 (class 2606 OID 31905)
-- Name: involvedpartyxinvolvedpartyidentificationtypesecuritytoken fktltopjihcyxw2xet9w7toxvm1; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyxinvolvedpartyidentificationtypesecuritytoken
    ADD CONSTRAINT fktltopjihcyxw2xet9w7toxvm1 FOREIGN KEY (activeflagid) REFERENCES dbo.activeflag(activeflagid);


--
-- TOC entry 4813 (class 2606 OID 32280)
-- Name: involvedpartyxresourceitemsecuritytoken fktnbu592x1dn0dtkv0lfo7cs6b; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyxresourceitemsecuritytoken
    ADD CONSTRAINT fktnbu592x1dn0dtkv0lfo7cs6b FOREIGN KEY (securitytokenid) REFERENCES security.securitytoken(securitytokenid);


--
-- TOC entry 4784 (class 2606 OID 32135)
-- Name: involvedpartyxproductsecuritytoken fktoih3sskv7bfum9756f55oveh; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyxproductsecuritytoken
    ADD CONSTRAINT fktoih3sskv7bfum9756f55oveh FOREIGN KEY (activeflagid) REFERENCES dbo.activeflag(activeflagid);


--
-- TOC entry 4818 (class 2606 OID 32305)
-- Name: involvedpartyxrules fkwjuotwflalqakh6pqixxoa1l; Type: FK CONSTRAINT; Schema: party; Owner: postgres
--

ALTER TABLE ONLY party.involvedpartyxrules
    ADD CONSTRAINT fkwjuotwflalqakh6pqixxoa1l FOREIGN KEY (systemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4848 (class 2606 OID 32455)
-- Name: producttypessecuritytoken fk124dnn13mvx64w9sscl56x0e9; Type: FK CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE ONLY product.producttypessecuritytoken
    ADD CONSTRAINT fk124dnn13mvx64w9sscl56x0e9 FOREIGN KEY (systemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4865 (class 2606 OID 32540)
-- Name: productxclassification fk170jhdqdisurhraj01enjp07f; Type: FK CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE ONLY product.productxclassification
    ADD CONSTRAINT fk170jhdqdisurhraj01enjp07f FOREIGN KEY (originalsourcesystemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4882 (class 2606 OID 32625)
-- Name: productxproductsecuritytoken fk1ig8t64jsxnh0bpi3ql9st9ox; Type: FK CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE ONLY product.productxproductsecuritytoken
    ADD CONSTRAINT fk1ig8t64jsxnh0bpi3ql9st9ox FOREIGN KEY (enterpriseid) REFERENCES dbo.enterprise(enterpriseid);


--
-- TOC entry 4830 (class 2606 OID 32365)
-- Name: product fk1uilqm7vj2gtc2d8x638robxd; Type: FK CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE ONLY product.product
    ADD CONSTRAINT fk1uilqm7vj2gtc2d8x638robxd FOREIGN KEY (activeflagid) REFERENCES dbo.activeflag(activeflagid);


--
-- TOC entry 4892 (class 2606 OID 32675)
-- Name: productxproducttype fk1v5k0dbqs8u10y8qplq99qoiq; Type: FK CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE ONLY product.productxproducttype
    ADD CONSTRAINT fk1v5k0dbqs8u10y8qplq99qoiq FOREIGN KEY (productid) REFERENCES product.product(productid);


--
-- TOC entry 4897 (class 2606 OID 32700)
-- Name: productxproducttypesecuritytoken fk24ny4320upy05vtrp0bl40edh; Type: FK CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE ONLY product.productxproducttypesecuritytoken
    ADD CONSTRAINT fk24ny4320upy05vtrp0bl40edh FOREIGN KEY (securitytokenid) REFERENCES security.securitytoken(securitytokenid);


--
-- TOC entry 4869 (class 2606 OID 32560)
-- Name: productxclassificationsecuritytoken fk25k9qm7701i83jtdlw9pma3it; Type: FK CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE ONLY product.productxclassificationsecuritytoken
    ADD CONSTRAINT fk25k9qm7701i83jtdlw9pma3it FOREIGN KEY (enterpriseid) REFERENCES dbo.enterprise(enterpriseid);


--
-- TOC entry 4873 (class 2606 OID 32580)
-- Name: productxclassificationsecuritytoken fk2a1344eckqbgsu5qtw1hqtmii; Type: FK CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE ONLY product.productxclassificationsecuritytoken
    ADD CONSTRAINT fk2a1344eckqbgsu5qtw1hqtmii FOREIGN KEY (productxclassificationid) REFERENCES product.productxclassification(productxclassificationid);


--
-- TOC entry 4904 (class 2606 OID 32735)
-- Name: productxresourceitem fk2kfndi7vipw90dmibvhml8luf; Type: FK CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE ONLY product.productxresourceitem
    ADD CONSTRAINT fk2kfndi7vipw90dmibvhml8luf FOREIGN KEY (classificationid) REFERENCES classification.classification(classificationid);


--
-- TOC entry 4908 (class 2606 OID 32755)
-- Name: productxresourceitemsecuritytoken fk2n140poncbaodw93p66dr19wm; Type: FK CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE ONLY product.productxresourceitemsecuritytoken
    ADD CONSTRAINT fk2n140poncbaodw93p66dr19wm FOREIGN KEY (enterpriseid) REFERENCES dbo.enterprise(enterpriseid);


--
-- TOC entry 4889 (class 2606 OID 32660)
-- Name: productxproducttype fk2n7njekvkkktqita4lo8o2rop; Type: FK CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE ONLY product.productxproducttype
    ADD CONSTRAINT fk2n7njekvkkktqita4lo8o2rop FOREIGN KEY (systemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4901 (class 2606 OID 32720)
-- Name: productxresourceitem fk3c9bk1tu9tr4ajj57j50lq61s; Type: FK CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE ONLY product.productxresourceitem
    ADD CONSTRAINT fk3c9bk1tu9tr4ajj57j50lq61s FOREIGN KEY (enterpriseid) REFERENCES dbo.enterprise(enterpriseid);


--
-- TOC entry 4899 (class 2606 OID 32710)
-- Name: productxproducttypesecuritytoken fk3h3ux71dsf7opesl0p4fq9y2n; Type: FK CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE ONLY product.productxproducttypesecuritytoken
    ADD CONSTRAINT fk3h3ux71dsf7opesl0p4fq9y2n FOREIGN KEY (productxproducttypeid) REFERENCES product.productxproducttype(productxproducttypeid);


--
-- TOC entry 4900 (class 2606 OID 32715)
-- Name: productxresourceitem fk4ircw0cakrrbxh9eb947do1r1; Type: FK CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE ONLY product.productxresourceitem
    ADD CONSTRAINT fk4ircw0cakrrbxh9eb947do1r1 FOREIGN KEY (activeflagid) REFERENCES dbo.activeflag(activeflagid);


--
-- TOC entry 4912 (class 2606 OID 32775)
-- Name: productxresourceitemsecuritytoken fk4pq3n38ujll0fn7l4mw2f0uj2; Type: FK CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE ONLY product.productxresourceitemsecuritytoken
    ADD CONSTRAINT fk4pq3n38ujll0fn7l4mw2f0uj2 FOREIGN KEY (productxresourceitemid) REFERENCES product.productxresourceitem(productxresourceitemid);


--
-- TOC entry 4864 (class 2606 OID 32535)
-- Name: productxclassification fk4rxn4f51nq7nachqwenoijt19; Type: FK CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE ONLY product.productxclassification
    ADD CONSTRAINT fk4rxn4f51nq7nachqwenoijt19 FOREIGN KEY (systemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4831 (class 2606 OID 32375)
-- Name: product fk5igcn0xk318avw7alibquq364; Type: FK CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE ONLY product.product
    ADD CONSTRAINT fk5igcn0xk318avw7alibquq364 FOREIGN KEY (systemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4894 (class 2606 OID 32685)
-- Name: productxproducttypesecuritytoken fk5r6sj8u01cbw7ndxn6nkbqbgs; Type: FK CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE ONLY product.productxproducttypesecuritytoken
    ADD CONSTRAINT fk5r6sj8u01cbw7ndxn6nkbqbgs FOREIGN KEY (activeflagid) REFERENCES dbo.activeflag(activeflagid);


--
-- TOC entry 4887 (class 2606 OID 32650)
-- Name: productxproducttype fk64ad5aqtu7abdea2c46wmwsyp; Type: FK CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE ONLY product.productxproducttype
    ADD CONSTRAINT fk64ad5aqtu7abdea2c46wmwsyp FOREIGN KEY (activeflagid) REFERENCES dbo.activeflag(activeflagid);


--
-- TOC entry 4879 (class 2606 OID 32610)
-- Name: productxproduct fk6by61ucqfknganlsl2nrl75qi; Type: FK CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE ONLY product.productxproduct
    ADD CONSTRAINT fk6by61ucqfknganlsl2nrl75qi FOREIGN KEY (childproductid) REFERENCES product.product(productid);


--
-- TOC entry 4866 (class 2606 OID 32545)
-- Name: productxclassification fk6if9d2r0jph42fstek03stxde; Type: FK CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE ONLY product.productxclassification
    ADD CONSTRAINT fk6if9d2r0jph42fstek03stxde FOREIGN KEY (classificationid) REFERENCES classification.classification(classificationid);


--
-- TOC entry 4836 (class 2606 OID 32395)
-- Name: productsecuritytoken fk6nu64a57s4xf2f259fg2ds2vx; Type: FK CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE ONLY product.productsecuritytoken
    ADD CONSTRAINT fk6nu64a57s4xf2f259fg2ds2vx FOREIGN KEY (originalsourcesystemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4840 (class 2606 OID 32415)
-- Name: producttype fk6qo5a8hhlrogyas0wxpefi252; Type: FK CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE ONLY product.producttype
    ADD CONSTRAINT fk6qo5a8hhlrogyas0wxpefi252 FOREIGN KEY (activeflagid) REFERENCES dbo.activeflag(activeflagid);


--
-- TOC entry 4876 (class 2606 OID 32595)
-- Name: productxproduct fk77xliloqs4xw4ci0ajbplqpww; Type: FK CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE ONLY product.productxproduct
    ADD CONSTRAINT fk77xliloqs4xw4ci0ajbplqpww FOREIGN KEY (systemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4841 (class 2606 OID 32420)
-- Name: producttype fk7jpihblwon21gvxequinglp4u; Type: FK CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE ONLY product.producttype
    ADD CONSTRAINT fk7jpihblwon21gvxequinglp4u FOREIGN KEY (enterpriseid) REFERENCES dbo.enterprise(enterpriseid);


--
-- TOC entry 4893 (class 2606 OID 32680)
-- Name: productxproducttype fk7qloe9c2vsgtb7r52s8rwam77; Type: FK CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE ONLY product.productxproducttype
    ADD CONSTRAINT fk7qloe9c2vsgtb7r52s8rwam77 FOREIGN KEY (producttypeid) REFERENCES product.producttype(producttypeid);


--
-- TOC entry 4884 (class 2606 OID 32635)
-- Name: productxproductsecuritytoken fk87msnwio1ifds9frgcawk703q; Type: FK CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE ONLY product.productxproductsecuritytoken
    ADD CONSTRAINT fk87msnwio1ifds9frgcawk703q FOREIGN KEY (securitytokenid) REFERENCES security.securitytoken(securitytokenid);


--
-- TOC entry 4862 (class 2606 OID 32525)
-- Name: productxclassification fk8b81utqms0112fwiuv724pss1; Type: FK CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE ONLY product.productxclassification
    ADD CONSTRAINT fk8b81utqms0112fwiuv724pss1 FOREIGN KEY (activeflagid) REFERENCES dbo.activeflag(activeflagid);


--
-- TOC entry 4863 (class 2606 OID 32530)
-- Name: productxclassification fk8bl09vfxm9qhghywxbfk36qra; Type: FK CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE ONLY product.productxclassification
    ADD CONSTRAINT fk8bl09vfxm9qhghywxbfk36qra FOREIGN KEY (enterpriseid) REFERENCES dbo.enterprise(enterpriseid);


--
-- TOC entry 4909 (class 2606 OID 32760)
-- Name: productxresourceitemsecuritytoken fk8ljhn730t04d2yrudqin3b4rx; Type: FK CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE ONLY product.productxresourceitemsecuritytoken
    ADD CONSTRAINT fk8ljhn730t04d2yrudqin3b4rx FOREIGN KEY (originalsourcesystemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4907 (class 2606 OID 32750)
-- Name: productxresourceitemsecuritytoken fk9rwmunm7mftbfjjefdk9i0ryh; Type: FK CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE ONLY product.productxresourceitemsecuritytoken
    ADD CONSTRAINT fk9rwmunm7mftbfjjefdk9i0ryh FOREIGN KEY (activeflagid) REFERENCES dbo.activeflag(activeflagid);


--
-- TOC entry 4847 (class 2606 OID 32450)
-- Name: producttypessecuritytoken fka4hffxoba5up3ewpbck3bydrl; Type: FK CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE ONLY product.producttypessecuritytoken
    ADD CONSTRAINT fka4hffxoba5up3ewpbck3bydrl FOREIGN KEY (securitytokenid) REFERENCES security.securitytoken(securitytokenid);


--
-- TOC entry 4842 (class 2606 OID 32425)
-- Name: producttype fkaji1ysqjjctugqxgktlyq4jb7; Type: FK CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE ONLY product.producttype
    ADD CONSTRAINT fkaji1ysqjjctugqxgktlyq4jb7 FOREIGN KEY (systemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4870 (class 2606 OID 32565)
-- Name: productxclassificationsecuritytoken fkalo1aiwrqa3qvcoyta4wmron0; Type: FK CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE ONLY product.productxclassificationsecuritytoken
    ADD CONSTRAINT fkalo1aiwrqa3qvcoyta4wmron0 FOREIGN KEY (originalsourcesystemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4846 (class 2606 OID 32445)
-- Name: producttypessecuritytoken fkbw6gf8n6ce2ob0qbvc0hdr876; Type: FK CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE ONLY product.producttypessecuritytoken
    ADD CONSTRAINT fkbw6gf8n6ce2ob0qbvc0hdr876 FOREIGN KEY (originalsourcesystemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4854 (class 2606 OID 32485)
-- Name: producttypexclassification fkd0fwhreudgdgcdipqbmxnrpw0; Type: FK CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE ONLY product.producttypexclassification
    ADD CONSTRAINT fkd0fwhreudgdgcdipqbmxnrpw0 FOREIGN KEY (classificationid) REFERENCES classification.classification(classificationid);


--
-- TOC entry 4874 (class 2606 OID 32585)
-- Name: productxproduct fkdcpq7m2melgfwv5auqyagl72h; Type: FK CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE ONLY product.productxproduct
    ADD CONSTRAINT fkdcpq7m2melgfwv5auqyagl72h FOREIGN KEY (activeflagid) REFERENCES dbo.activeflag(activeflagid);


--
-- TOC entry 4902 (class 2606 OID 32725)
-- Name: productxresourceitem fkdlspqxuytfux61envksg2oblc; Type: FK CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE ONLY product.productxresourceitem
    ADD CONSTRAINT fkdlspqxuytfux61envksg2oblc FOREIGN KEY (systemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4896 (class 2606 OID 32695)
-- Name: productxproducttypesecuritytoken fke8k4xro3jq77djofuxtcqjnb7; Type: FK CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE ONLY product.productxproducttypesecuritytoken
    ADD CONSTRAINT fke8k4xro3jq77djofuxtcqjnb7 FOREIGN KEY (originalsourcesystemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4852 (class 2606 OID 32475)
-- Name: producttypexclassification fkefusbrg6b297xo4uaifrnf7of; Type: FK CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE ONLY product.producttypexclassification
    ADD CONSTRAINT fkefusbrg6b297xo4uaifrnf7of FOREIGN KEY (systemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4881 (class 2606 OID 32620)
-- Name: productxproductsecuritytoken fkfov8qx41nggbg75jj8ely4wrp; Type: FK CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE ONLY product.productxproductsecuritytoken
    ADD CONSTRAINT fkfov8qx41nggbg75jj8ely4wrp FOREIGN KEY (activeflagid) REFERENCES dbo.activeflag(activeflagid);


--
-- TOC entry 4832 (class 2606 OID 32370)
-- Name: product fkfvbgcjo7xxxjqbqwy6rf9okxe; Type: FK CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE ONLY product.product
    ADD CONSTRAINT fkfvbgcjo7xxxjqbqwy6rf9okxe FOREIGN KEY (enterpriseid) REFERENCES dbo.enterprise(enterpriseid);


--
-- TOC entry 4843 (class 2606 OID 32430)
-- Name: producttype fkg36gb46ujtrxwet1ac4f2k43b; Type: FK CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE ONLY product.producttype
    ADD CONSTRAINT fkg36gb46ujtrxwet1ac4f2k43b FOREIGN KEY (originalsourcesystemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4833 (class 2606 OID 32380)
-- Name: product fkhd5i44wqur3jhd2e66aj1e38; Type: FK CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE ONLY product.product
    ADD CONSTRAINT fkhd5i44wqur3jhd2e66aj1e38 FOREIGN KEY (originalsourcesystemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4911 (class 2606 OID 32770)
-- Name: productxresourceitemsecuritytoken fkhgl7dki8ue6gmivh1ulwel4j1; Type: FK CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE ONLY product.productxresourceitemsecuritytoken
    ADD CONSTRAINT fkhgl7dki8ue6gmivh1ulwel4j1 FOREIGN KEY (systemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4856 (class 2606 OID 32495)
-- Name: producttypexclassificationsecuritytoken fkhp82lysbkher7sh9nmdc8j5mt; Type: FK CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE ONLY product.producttypexclassificationsecuritytoken
    ADD CONSTRAINT fkhp82lysbkher7sh9nmdc8j5mt FOREIGN KEY (activeflagid) REFERENCES dbo.activeflag(activeflagid);


--
-- TOC entry 4844 (class 2606 OID 32435)
-- Name: producttypessecuritytoken fkhqgmcxurg87rgu1ekgl6xqb4; Type: FK CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE ONLY product.producttypessecuritytoken
    ADD CONSTRAINT fkhqgmcxurg87rgu1ekgl6xqb4 FOREIGN KEY (activeflagid) REFERENCES dbo.activeflag(activeflagid);


--
-- TOC entry 4838 (class 2606 OID 32405)
-- Name: productsecuritytoken fkhwbgx5m4cqg3drq0kbl2ln218; Type: FK CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE ONLY product.productsecuritytoken
    ADD CONSTRAINT fkhwbgx5m4cqg3drq0kbl2ln218 FOREIGN KEY (systemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4858 (class 2606 OID 32505)
-- Name: producttypexclassificationsecuritytoken fki005cybjhowcm4dc6a4rur35f; Type: FK CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE ONLY product.producttypexclassificationsecuritytoken
    ADD CONSTRAINT fki005cybjhowcm4dc6a4rur35f FOREIGN KEY (originalsourcesystemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4891 (class 2606 OID 32670)
-- Name: productxproducttype fki3kgl08d83biiovc4ipk0i82k; Type: FK CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE ONLY product.productxproducttype
    ADD CONSTRAINT fki3kgl08d83biiovc4ipk0i82k FOREIGN KEY (classificationid) REFERENCES classification.classification(classificationid);


--
-- TOC entry 4835 (class 2606 OID 32390)
-- Name: productsecuritytoken fkix4kyq2jvwpfkwr07hb5418wk; Type: FK CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE ONLY product.productsecuritytoken
    ADD CONSTRAINT fkix4kyq2jvwpfkwr07hb5418wk FOREIGN KEY (enterpriseid) REFERENCES dbo.enterprise(enterpriseid);


--
-- TOC entry 4905 (class 2606 OID 32740)
-- Name: productxresourceitem fkjcm094m4k21700pj371gyg28; Type: FK CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE ONLY product.productxresourceitem
    ADD CONSTRAINT fkjcm094m4k21700pj371gyg28 FOREIGN KEY (productid) REFERENCES product.product(productid);


--
-- TOC entry 4878 (class 2606 OID 32605)
-- Name: productxproduct fkjtuo8wff4bgrag27r7b90e0cr; Type: FK CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE ONLY product.productxproduct
    ADD CONSTRAINT fkjtuo8wff4bgrag27r7b90e0cr FOREIGN KEY (classificationid) REFERENCES classification.classification(classificationid);


--
-- TOC entry 4883 (class 2606 OID 32630)
-- Name: productxproductsecuritytoken fkk3c9qgnvemsrtifm1qkfwwpai; Type: FK CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE ONLY product.productxproductsecuritytoken
    ADD CONSTRAINT fkk3c9qgnvemsrtifm1qkfwwpai FOREIGN KEY (originalsourcesystemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4906 (class 2606 OID 32745)
-- Name: productxresourceitem fkk4j0vxf6l51756si5xlelvhyx; Type: FK CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE ONLY product.productxresourceitem
    ADD CONSTRAINT fkk4j0vxf6l51756si5xlelvhyx FOREIGN KEY (resourceitemid) REFERENCES resource.resourceitem(resourceitemid);


--
-- TOC entry 4875 (class 2606 OID 32590)
-- Name: productxproduct fkkh1fb4vobxxm3gqr902b2sy9q; Type: FK CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE ONLY product.productxproduct
    ADD CONSTRAINT fkkh1fb4vobxxm3gqr902b2sy9q FOREIGN KEY (enterpriseid) REFERENCES dbo.enterprise(enterpriseid);


--
-- TOC entry 4857 (class 2606 OID 32500)
-- Name: producttypexclassificationsecuritytoken fkkpdeivhwgmup6yq5or7mkhh3i; Type: FK CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE ONLY product.producttypexclassificationsecuritytoken
    ADD CONSTRAINT fkkpdeivhwgmup6yq5or7mkhh3i FOREIGN KEY (enterpriseid) REFERENCES dbo.enterprise(enterpriseid);


--
-- TOC entry 4849 (class 2606 OID 32460)
-- Name: producttypessecuritytoken fkkpofjm6j3idsftpk1nilvd5am; Type: FK CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE ONLY product.producttypessecuritytoken
    ADD CONSTRAINT fkkpofjm6j3idsftpk1nilvd5am FOREIGN KEY (producttypesid) REFERENCES product.producttype(producttypeid);


--
-- TOC entry 4839 (class 2606 OID 32410)
-- Name: productsecuritytoken fklfavoymys95w6sr5vg7fc2lpd; Type: FK CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE ONLY product.productsecuritytoken
    ADD CONSTRAINT fklfavoymys95w6sr5vg7fc2lpd FOREIGN KEY (productid) REFERENCES product.product(productid);


--
-- TOC entry 4850 (class 2606 OID 32465)
-- Name: producttypexclassification fklpladubwvqlvl4m7vv5k7i04a; Type: FK CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE ONLY product.producttypexclassification
    ADD CONSTRAINT fklpladubwvqlvl4m7vv5k7i04a FOREIGN KEY (activeflagid) REFERENCES dbo.activeflag(activeflagid);


--
-- TOC entry 4910 (class 2606 OID 32765)
-- Name: productxresourceitemsecuritytoken fklwlxp6u9a4k42nl1xqst9y0y; Type: FK CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE ONLY product.productxresourceitemsecuritytoken
    ADD CONSTRAINT fklwlxp6u9a4k42nl1xqst9y0y FOREIGN KEY (securitytokenid) REFERENCES security.securitytoken(securitytokenid);


--
-- TOC entry 4880 (class 2606 OID 32615)
-- Name: productxproduct fklwu619drh230ni7r1dow2jdo1; Type: FK CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE ONLY product.productxproduct
    ADD CONSTRAINT fklwu619drh230ni7r1dow2jdo1 FOREIGN KEY (parentproductid) REFERENCES product.product(productid);


--
-- TOC entry 4853 (class 2606 OID 32480)
-- Name: producttypexclassification fkm04vi4rjib6p4wstxivhv5uwb; Type: FK CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE ONLY product.producttypexclassification
    ADD CONSTRAINT fkm04vi4rjib6p4wstxivhv5uwb FOREIGN KEY (originalsourcesystemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4885 (class 2606 OID 32640)
-- Name: productxproductsecuritytoken fkm2agj4yop5p5pdbbqalr9ys6d; Type: FK CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE ONLY product.productxproductsecuritytoken
    ADD CONSTRAINT fkm2agj4yop5p5pdbbqalr9ys6d FOREIGN KEY (systemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4860 (class 2606 OID 32515)
-- Name: producttypexclassificationsecuritytoken fkm2t70qvf88y1hc6xxtp5yvedj; Type: FK CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE ONLY product.producttypexclassificationsecuritytoken
    ADD CONSTRAINT fkm2t70qvf88y1hc6xxtp5yvedj FOREIGN KEY (systemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4861 (class 2606 OID 32520)
-- Name: producttypexclassificationsecuritytoken fkmtyho44wd7jen9pckqv90td0j; Type: FK CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE ONLY product.producttypexclassificationsecuritytoken
    ADD CONSTRAINT fkmtyho44wd7jen9pckqv90td0j FOREIGN KEY (producttypexclassificationid) REFERENCES product.producttypexclassification(producttypexclassificationid);


--
-- TOC entry 4859 (class 2606 OID 32510)
-- Name: producttypexclassificationsecuritytoken fkn22khncgjxl940nga25siqctl; Type: FK CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE ONLY product.producttypexclassificationsecuritytoken
    ADD CONSTRAINT fkn22khncgjxl940nga25siqctl FOREIGN KEY (securitytokenid) REFERENCES security.securitytoken(securitytokenid);


--
-- TOC entry 4872 (class 2606 OID 32575)
-- Name: productxclassificationsecuritytoken fkncist9vnbkli1wfjeyqeqpbhi; Type: FK CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE ONLY product.productxclassificationsecuritytoken
    ADD CONSTRAINT fkncist9vnbkli1wfjeyqeqpbhi FOREIGN KEY (systemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4898 (class 2606 OID 32705)
-- Name: productxproducttypesecuritytoken fknnkyxacvaltro761yay2h0a0t; Type: FK CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE ONLY product.productxproducttypesecuritytoken
    ADD CONSTRAINT fknnkyxacvaltro761yay2h0a0t FOREIGN KEY (systemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4895 (class 2606 OID 32690)
-- Name: productxproducttypesecuritytoken fknr2deq2638s77m37vyx16g6i0; Type: FK CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE ONLY product.productxproducttypesecuritytoken
    ADD CONSTRAINT fknr2deq2638s77m37vyx16g6i0 FOREIGN KEY (enterpriseid) REFERENCES dbo.enterprise(enterpriseid);


--
-- TOC entry 4837 (class 2606 OID 32400)
-- Name: productsecuritytoken fkobdmay5wpjxkq8fmnh5jb4wax; Type: FK CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE ONLY product.productsecuritytoken
    ADD CONSTRAINT fkobdmay5wpjxkq8fmnh5jb4wax FOREIGN KEY (securitytokenid) REFERENCES security.securitytoken(securitytokenid);


--
-- TOC entry 4855 (class 2606 OID 32490)
-- Name: producttypexclassification fkon0srs81eq9qmdm4clgh7lf1r; Type: FK CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE ONLY product.producttypexclassification
    ADD CONSTRAINT fkon0srs81eq9qmdm4clgh7lf1r FOREIGN KEY (producttypeid) REFERENCES product.producttype(producttypeid);


--
-- TOC entry 4845 (class 2606 OID 32440)
-- Name: producttypessecuritytoken fkpevw33iuc65w72j6c9j1scwi; Type: FK CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE ONLY product.producttypessecuritytoken
    ADD CONSTRAINT fkpevw33iuc65w72j6c9j1scwi FOREIGN KEY (enterpriseid) REFERENCES dbo.enterprise(enterpriseid);


--
-- TOC entry 4868 (class 2606 OID 32555)
-- Name: productxclassificationsecuritytoken fkphp040h86si2e7sthuts7u84a; Type: FK CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE ONLY product.productxclassificationsecuritytoken
    ADD CONSTRAINT fkphp040h86si2e7sthuts7u84a FOREIGN KEY (activeflagid) REFERENCES dbo.activeflag(activeflagid);


--
-- TOC entry 4877 (class 2606 OID 32600)
-- Name: productxproduct fkpjcmb3bm0uykh5ycoxw327a6y; Type: FK CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE ONLY product.productxproduct
    ADD CONSTRAINT fkpjcmb3bm0uykh5ycoxw327a6y FOREIGN KEY (originalsourcesystemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4834 (class 2606 OID 32385)
-- Name: productsecuritytoken fkpp00icvq84wm807qt11c7di0o; Type: FK CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE ONLY product.productsecuritytoken
    ADD CONSTRAINT fkpp00icvq84wm807qt11c7di0o FOREIGN KEY (activeflagid) REFERENCES dbo.activeflag(activeflagid);


--
-- TOC entry 4903 (class 2606 OID 32730)
-- Name: productxresourceitem fkq078arvokvft7rp6g01drm5ga; Type: FK CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE ONLY product.productxresourceitem
    ADD CONSTRAINT fkq078arvokvft7rp6g01drm5ga FOREIGN KEY (originalsourcesystemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4890 (class 2606 OID 32665)
-- Name: productxproducttype fkqx5p60ei4jsps9ajg56g6y75n; Type: FK CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE ONLY product.productxproducttype
    ADD CONSTRAINT fkqx5p60ei4jsps9ajg56g6y75n FOREIGN KEY (originalsourcesystemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4886 (class 2606 OID 32645)
-- Name: productxproductsecuritytoken fksmhfy43ik4seggjpeh0hd7ohe; Type: FK CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE ONLY product.productxproductsecuritytoken
    ADD CONSTRAINT fksmhfy43ik4seggjpeh0hd7ohe FOREIGN KEY (productxproductid) REFERENCES product.productxproduct(productxproductid);


--
-- TOC entry 4871 (class 2606 OID 32570)
-- Name: productxclassificationsecuritytoken fksqaqrh3mqcptl4uk5ucmkn34h; Type: FK CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE ONLY product.productxclassificationsecuritytoken
    ADD CONSTRAINT fksqaqrh3mqcptl4uk5ucmkn34h FOREIGN KEY (securitytokenid) REFERENCES security.securitytoken(securitytokenid);


--
-- TOC entry 4851 (class 2606 OID 32470)
-- Name: producttypexclassification fktby5ywfw3h2p0kwing53rbw83; Type: FK CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE ONLY product.producttypexclassification
    ADD CONSTRAINT fktby5ywfw3h2p0kwing53rbw83 FOREIGN KEY (enterpriseid) REFERENCES dbo.enterprise(enterpriseid);


--
-- TOC entry 4867 (class 2606 OID 32550)
-- Name: productxclassification fktjv0q3owa2dvherxn8lwy4umj; Type: FK CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE ONLY product.productxclassification
    ADD CONSTRAINT fktjv0q3owa2dvherxn8lwy4umj FOREIGN KEY (productid) REFERENCES product.product(productid);


--
-- TOC entry 4888 (class 2606 OID 32655)
-- Name: productxproducttype fktn5yb54e9i7r3hwtr1bojxh6e; Type: FK CONSTRAINT; Schema: product; Owner: postgres
--

ALTER TABLE ONLY product.productxproducttype
    ADD CONSTRAINT fktn5yb54e9i7r3hwtr1bojxh6e FOREIGN KEY (enterpriseid) REFERENCES dbo.enterprise(enterpriseid);


--
-- TOC entry 5173 (class 2606 OID 34085)
-- Name: quarters_months fkb9r9yk4sx0s7vbxr3h7v1ahf9; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.quarters_months
    ADD CONSTRAINT fkb9r9yk4sx0s7vbxr3h7v1ahf9 FOREIGN KEY (quarters_quarterid) REFERENCES "time".quarters(quarterid);


--
-- TOC entry 5175 (class 2606 OID 34090)
-- Name: securityhierarchyparents fkbrpasxc35cefp3eivjag2jq8f; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.securityhierarchyparents
    ADD CONSTRAINT fkbrpasxc35cefp3eivjag2jq8f FOREIGN KEY (id) REFERENCES security.securityhierarchy(id);


--
-- TOC entry 5174 (class 2606 OID 34080)
-- Name: quarters_months fkg7qo91crqcfpnvgf4fpaxt8pf; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.quarters_months
    ADD CONSTRAINT fkg7qo91crqcfpnvgf4fpaxt8pf FOREIGN KEY (lumonthslist_monthid) REFERENCES "time".months(monthid);


--
-- TOC entry 4917 (class 2606 OID 32810)
-- Name: resourceitemdata fk1fo3xmni8ec47xrr4ga2eff84; Type: FK CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE ONLY resource.resourceitemdata
    ADD CONSTRAINT fk1fo3xmni8ec47xrr4ga2eff84 FOREIGN KEY (systemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4975 (class 2606 OID 33115)
-- Name: resourceitemxresourceitemsecuritytoken fk1n6u8l8jhlesanln4et9jehgh; Type: FK CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE ONLY resource.resourceitemxresourceitemsecuritytoken
    ADD CONSTRAINT fk1n6u8l8jhlesanln4et9jehgh FOREIGN KEY (resourceitemxresourceitemid) REFERENCES resource.resourceitemxresourceitem(resourceitemxresourceitemid);


--
-- TOC entry 4913 (class 2606 OID 32785)
-- Name: resourceitem fk200sir97l7uhxxs2l0b23ok3r; Type: FK CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE ONLY resource.resourceitem
    ADD CONSTRAINT fk200sir97l7uhxxs2l0b23ok3r FOREIGN KEY (enterpriseid) REFERENCES dbo.enterprise(enterpriseid);


--
-- TOC entry 4933 (class 2606 OID 32880)
-- Name: resourceitemdataxclassification fk2dy40tyg4ideqgywl6lh9gly5; Type: FK CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE ONLY resource.resourceitemdataxclassification
    ADD CONSTRAINT fk2dy40tyg4ideqgywl6lh9gly5 FOREIGN KEY (resourceitemdataid) REFERENCES resource.resourceitemdata(resourceitemdataid);


--
-- TOC entry 4988 (class 2606 OID 33155)
-- Name: resourceitemxresourceitemtypesecuritytoken fk2ep9v4fv470h60ye8kq7lfybi; Type: FK CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE ONLY resource.resourceitemxresourceitemtypesecuritytoken
    ADD CONSTRAINT fk2ep9v4fv470h60ye8kq7lfybi FOREIGN KEY (activeflagid) REFERENCES dbo.activeflag(activeflagid);


--
-- TOC entry 4971 (class 2606 OID 33070)
-- Name: resourceitemxresourceitem fk2ki967a981nc2wof2y4u5kwtw; Type: FK CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE ONLY resource.resourceitemxresourceitem
    ADD CONSTRAINT fk2ki967a981nc2wof2y4u5kwtw FOREIGN KEY (originalsourcesystemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4962 (class 2606 OID 33025)
-- Name: resourceitemxclassificationsecuritytoken fk3i66vwdq4it2rgp85m6d0jq81; Type: FK CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE ONLY resource.resourceitemxclassificationsecuritytoken
    ADD CONSTRAINT fk3i66vwdq4it2rgp85m6d0jq81 FOREIGN KEY (activeflagid) REFERENCES dbo.activeflag(activeflagid);


--
-- TOC entry 4950 (class 2606 OID 32970)
-- Name: resourceitemtypesecuritytoken fk3p3b9iwh15wwgccqgj3lpysqn; Type: FK CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE ONLY resource.resourceitemtypesecuritytoken
    ADD CONSTRAINT fk3p3b9iwh15wwgccqgj3lpysqn FOREIGN KEY (enterpriseid) REFERENCES dbo.enterprise(enterpriseid);


--
-- TOC entry 4922 (class 2606 OID 32825)
-- Name: resourceitemdatasecuritytoken fk3w9axr9msmakpp6ferrlkuvdy; Type: FK CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE ONLY resource.resourceitemdatasecuritytoken
    ADD CONSTRAINT fk3w9axr9msmakpp6ferrlkuvdy FOREIGN KEY (activeflagid) REFERENCES dbo.activeflag(activeflagid);


--
-- TOC entry 4956 (class 2606 OID 32995)
-- Name: resourceitemxclassification fk3wmuxdwncxa9duotxup8kots8; Type: FK CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE ONLY resource.resourceitemxclassification
    ADD CONSTRAINT fk3wmuxdwncxa9duotxup8kots8 FOREIGN KEY (activeflagid) REFERENCES dbo.activeflag(activeflagid);


--
-- TOC entry 4951 (class 2606 OID 32985)
-- Name: resourceitemtypesecuritytoken fk4cdo57v9c89cmsd70444brarb; Type: FK CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE ONLY resource.resourceitemtypesecuritytoken
    ADD CONSTRAINT fk4cdo57v9c89cmsd70444brarb FOREIGN KEY (systemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4957 (class 2606 OID 33015)
-- Name: resourceitemxclassification fk5ad2d7e8c7kiwjtnfv4baj6k5; Type: FK CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE ONLY resource.resourceitemxclassification
    ADD CONSTRAINT fk5ad2d7e8c7kiwjtnfv4baj6k5 FOREIGN KEY (classificationid) REFERENCES classification.classification(classificationid);


--
-- TOC entry 4974 (class 2606 OID 33085)
-- Name: resourceitemxresourceitem fk5jtsbc1lwwjo7gimmifc90cxv; Type: FK CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE ONLY resource.resourceitemxresourceitem
    ADD CONSTRAINT fk5jtsbc1lwwjo7gimmifc90cxv FOREIGN KEY (parentresourceitemid) REFERENCES resource.resourceitem(resourceitemid);


--
-- TOC entry 4952 (class 2606 OID 32965)
-- Name: resourceitemtypesecuritytoken fk5wdvel5htabl0xh6uo6do4nmw; Type: FK CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE ONLY resource.resourceitemtypesecuritytoken
    ADD CONSTRAINT fk5wdvel5htabl0xh6uo6do4nmw FOREIGN KEY (activeflagid) REFERENCES dbo.activeflag(activeflagid);


--
-- TOC entry 4930 (class 2606 OID 32865)
-- Name: resourceitemdataxclassification fk663fflx6v3085crppbxdji50p; Type: FK CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE ONLY resource.resourceitemdataxclassification
    ADD CONSTRAINT fk663fflx6v3085crppbxdji50p FOREIGN KEY (systemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4946 (class 2606 OID 32955)
-- Name: resourceitemtype fk6qq7tnhpgyvvt5vly41nemv50; Type: FK CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE ONLY resource.resourceitemtype
    ADD CONSTRAINT fk6qq7tnhpgyvvt5vly41nemv50 FOREIGN KEY (systemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4976 (class 2606 OID 33105)
-- Name: resourceitemxresourceitemsecuritytoken fk6sn4lyt8rfdltu9cbblt5uieh; Type: FK CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE ONLY resource.resourceitemxresourceitemsecuritytoken
    ADD CONSTRAINT fk6sn4lyt8rfdltu9cbblt5uieh FOREIGN KEY (securitytokenid) REFERENCES security.securitytoken(securitytokenid);


--
-- TOC entry 4934 (class 2606 OID 32910)
-- Name: resourceitemdataxclassificationsecuritytoken fk79odxnrnlexs55g44rmjdw9my; Type: FK CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE ONLY resource.resourceitemdataxclassificationsecuritytoken
    ADD CONSTRAINT fk79odxnrnlexs55g44rmjdw9my FOREIGN KEY (resourceitemdataxclassificationid) REFERENCES resource.resourceitemdataxclassification(resourceitemdataxclassificationid);


--
-- TOC entry 4963 (class 2606 OID 33040)
-- Name: resourceitemxclassificationsecuritytoken fk7ns6uhcbucfbl2uioayhiydj2; Type: FK CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE ONLY resource.resourceitemxclassificationsecuritytoken
    ADD CONSTRAINT fk7ns6uhcbucfbl2uioayhiydj2 FOREIGN KEY (securitytokenid) REFERENCES security.securitytoken(securitytokenid);


--
-- TOC entry 4935 (class 2606 OID 32905)
-- Name: resourceitemdataxclassificationsecuritytoken fk7phkn8oemhek6rr9400ha8lh3; Type: FK CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE ONLY resource.resourceitemdataxclassificationsecuritytoken
    ADD CONSTRAINT fk7phkn8oemhek6rr9400ha8lh3 FOREIGN KEY (systemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4964 (class 2606 OID 33045)
-- Name: resourceitemxclassificationsecuritytoken fk7wx6yqn7nc5fajp9dhi1f2mjx; Type: FK CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE ONLY resource.resourceitemxclassificationsecuritytoken
    ADD CONSTRAINT fk7wx6yqn7nc5fajp9dhi1f2mjx FOREIGN KEY (systemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4940 (class 2606 OID 32930)
-- Name: resourceitemsecuritytoken fk84jknr1n5cnm1wtlwn07d1r64; Type: FK CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE ONLY resource.resourceitemsecuritytoken
    ADD CONSTRAINT fk84jknr1n5cnm1wtlwn07d1r64 FOREIGN KEY (securitytokenid) REFERENCES security.securitytoken(securitytokenid);


--
-- TOC entry 4941 (class 2606 OID 32940)
-- Name: resourceitemsecuritytoken fk861bu9d1oka3g8dh0mb85tyc4; Type: FK CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE ONLY resource.resourceitemsecuritytoken
    ADD CONSTRAINT fk861bu9d1oka3g8dh0mb85tyc4 FOREIGN KEY (resourceitemid) REFERENCES resource.resourceitem(resourceitemid);


--
-- TOC entry 4914 (class 2606 OID 32780)
-- Name: resourceitem fk8cpmhhiy16t26qcrc94hxyngi; Type: FK CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE ONLY resource.resourceitem
    ADD CONSTRAINT fk8cpmhhiy16t26qcrc94hxyngi FOREIGN KEY (activeflagid) REFERENCES dbo.activeflag(activeflagid);


--
-- TOC entry 4958 (class 2606 OID 33020)
-- Name: resourceitemxclassification fk8obyha4m3ud6wg5wugtj0efl3; Type: FK CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE ONLY resource.resourceitemxclassification
    ADD CONSTRAINT fk8obyha4m3ud6wg5wugtj0efl3 FOREIGN KEY (resourceitemid) REFERENCES resource.resourceitem(resourceitemid);


--
-- TOC entry 4977 (class 2606 OID 33100)
-- Name: resourceitemxresourceitemsecuritytoken fk8qo9i93o20ejx9qm1wys3xwmt; Type: FK CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE ONLY resource.resourceitemxresourceitemsecuritytoken
    ADD CONSTRAINT fk8qo9i93o20ejx9qm1wys3xwmt FOREIGN KEY (originalsourcesystemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4915 (class 2606 OID 32790)
-- Name: resourceitem fk94ha303yukem4jhrogbx5e06w; Type: FK CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE ONLY resource.resourceitem
    ADD CONSTRAINT fk94ha303yukem4jhrogbx5e06w FOREIGN KEY (systemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4936 (class 2606 OID 32885)
-- Name: resourceitemdataxclassificationsecuritytoken fka0xjd26qyqrft0dop3wn7f0np; Type: FK CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE ONLY resource.resourceitemdataxclassificationsecuritytoken
    ADD CONSTRAINT fka0xjd26qyqrft0dop3wn7f0np FOREIGN KEY (activeflagid) REFERENCES dbo.activeflag(activeflagid);


--
-- TOC entry 4937 (class 2606 OID 32895)
-- Name: resourceitemdataxclassificationsecuritytoken fkab7vvw2nd2839r3whhm76iel; Type: FK CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE ONLY resource.resourceitemdataxclassificationsecuritytoken
    ADD CONSTRAINT fkab7vvw2nd2839r3whhm76iel FOREIGN KEY (originalsourcesystemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4938 (class 2606 OID 32900)
-- Name: resourceitemdataxclassificationsecuritytoken fkajq33984mmb4dan1b1ujl28qh; Type: FK CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE ONLY resource.resourceitemdataxclassificationsecuritytoken
    ADD CONSTRAINT fkajq33984mmb4dan1b1ujl28qh FOREIGN KEY (securitytokenid) REFERENCES security.securitytoken(securitytokenid);


--
-- TOC entry 4939 (class 2606 OID 32890)
-- Name: resourceitemdataxclassificationsecuritytoken fkb8riptu7wfv9qs6scj4k6bdnh; Type: FK CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE ONLY resource.resourceitemdataxclassificationsecuritytoken
    ADD CONSTRAINT fkb8riptu7wfv9qs6scj4k6bdnh FOREIGN KEY (enterpriseid) REFERENCES dbo.enterprise(enterpriseid);


--
-- TOC entry 4973 (class 2606 OID 33080)
-- Name: resourceitemxresourceitem fkbb8g8afmpudbornwchcbn8d6x; Type: FK CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE ONLY resource.resourceitemxresourceitem
    ADD CONSTRAINT fkbb8g8afmpudbornwchcbn8d6x FOREIGN KEY (childresourceitemid) REFERENCES resource.resourceitem(resourceitemid);


--
-- TOC entry 4916 (class 2606 OID 32795)
-- Name: resourceitem fkc07twhlsn4lwl4lmx290qts4r; Type: FK CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE ONLY resource.resourceitem
    ADD CONSTRAINT fkc07twhlsn4lwl4lmx290qts4r FOREIGN KEY (originalsourcesystemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4989 (class 2606 OID 33165)
-- Name: resourceitemxresourceitemtypesecuritytoken fkc63ne5lom1f43u7t6lfi6imtn; Type: FK CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE ONLY resource.resourceitemxresourceitemtypesecuritytoken
    ADD CONSTRAINT fkc63ne5lom1f43u7t6lfi6imtn FOREIGN KEY (originalsourcesystemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4990 (class 2606 OID 33170)
-- Name: resourceitemxresourceitemtypesecuritytoken fkchn8hstbo7rkgh5817jqqcxpx; Type: FK CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE ONLY resource.resourceitemxresourceitemtypesecuritytoken
    ADD CONSTRAINT fkchn8hstbo7rkgh5817jqqcxpx FOREIGN KEY (securitytokenid) REFERENCES security.securitytoken(securitytokenid);


--
-- TOC entry 4984 (class 2606 OID 33135)
-- Name: resourceitemxresourceitemtype fkcvs3qfiioi6vtrvsx9fydc5w9; Type: FK CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE ONLY resource.resourceitemxresourceitemtype
    ADD CONSTRAINT fkcvs3qfiioi6vtrvsx9fydc5w9 FOREIGN KEY (originalsourcesystemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4970 (class 2606 OID 33065)
-- Name: resourceitemxresourceitem fkcwb9vx2pur7f8if9mh62f8yta; Type: FK CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE ONLY resource.resourceitemxresourceitem
    ADD CONSTRAINT fkcwb9vx2pur7f8if9mh62f8yta FOREIGN KEY (systemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4918 (class 2606 OID 32800)
-- Name: resourceitemdata fkdc81hbiavl5m699l1e4n4qtp; Type: FK CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE ONLY resource.resourceitemdata
    ADD CONSTRAINT fkdc81hbiavl5m699l1e4n4qtp FOREIGN KEY (activeflagid) REFERENCES dbo.activeflag(activeflagid);


--
-- TOC entry 4968 (class 2606 OID 33055)
-- Name: resourceitemxresourceitem fkesiucs3j6sufylpxxpldbhwbl; Type: FK CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE ONLY resource.resourceitemxresourceitem
    ADD CONSTRAINT fkesiucs3j6sufylpxxpldbhwbl FOREIGN KEY (activeflagid) REFERENCES dbo.activeflag(activeflagid);


--
-- TOC entry 4942 (class 2606 OID 32915)
-- Name: resourceitemsecuritytoken fkeut88pwpr6r3by6poh8s3lgn5; Type: FK CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE ONLY resource.resourceitemsecuritytoken
    ADD CONSTRAINT fkeut88pwpr6r3by6poh8s3lgn5 FOREIGN KEY (activeflagid) REFERENCES dbo.activeflag(activeflagid);


--
-- TOC entry 4978 (class 2606 OID 33110)
-- Name: resourceitemxresourceitemsecuritytoken fkexfo0hib3thx9lan7x40hj7gr; Type: FK CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE ONLY resource.resourceitemxresourceitemsecuritytoken
    ADD CONSTRAINT fkexfo0hib3thx9lan7x40hj7gr FOREIGN KEY (systemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4927 (class 2606 OID 32850)
-- Name: resourceitemdatasecuritytoken fkfx83b2i5f4bu61o46gd6jpq3m; Type: FK CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE ONLY resource.resourceitemdatasecuritytoken
    ADD CONSTRAINT fkfx83b2i5f4bu61o46gd6jpq3m FOREIGN KEY (resourceitemdataid) REFERENCES resource.resourceitemdata(resourceitemdataid);


--
-- TOC entry 4986 (class 2606 OID 33145)
-- Name: resourceitemxresourceitemtype fkg86r3f2ftu2ex3xkplrd9tar7; Type: FK CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE ONLY resource.resourceitemxresourceitemtype
    ADD CONSTRAINT fkg86r3f2ftu2ex3xkplrd9tar7 FOREIGN KEY (resourceitemid) REFERENCES resource.resourceitem(resourceitemid);


--
-- TOC entry 4969 (class 2606 OID 33060)
-- Name: resourceitemxresourceitem fkgguetxp2rpaak5nhtais4qipi; Type: FK CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE ONLY resource.resourceitemxresourceitem
    ADD CONSTRAINT fkgguetxp2rpaak5nhtais4qipi FOREIGN KEY (enterpriseid) REFERENCES dbo.enterprise(enterpriseid);


--
-- TOC entry 4959 (class 2606 OID 33000)
-- Name: resourceitemxclassification fkgjx0hpxxegn5wge4pdrchmidh; Type: FK CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE ONLY resource.resourceitemxclassification
    ADD CONSTRAINT fkgjx0hpxxegn5wge4pdrchmidh FOREIGN KEY (enterpriseid) REFERENCES dbo.enterprise(enterpriseid);


--
-- TOC entry 4947 (class 2606 OID 32960)
-- Name: resourceitemtype fkgxtvsg5e3c8sq9j7y8bx0ib5v; Type: FK CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE ONLY resource.resourceitemtype
    ADD CONSTRAINT fkgxtvsg5e3c8sq9j7y8bx0ib5v FOREIGN KEY (originalsourcesystemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4948 (class 2606 OID 32950)
-- Name: resourceitemtype fkh8avb7iv2w72vgk90sh1d2p4f; Type: FK CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE ONLY resource.resourceitemtype
    ADD CONSTRAINT fkh8avb7iv2w72vgk90sh1d2p4f FOREIGN KEY (enterpriseid) REFERENCES dbo.enterprise(enterpriseid);


--
-- TOC entry 4924 (class 2606 OID 32835)
-- Name: resourceitemdatasecuritytoken fkhcwacfqh36t292fibcvgdtr3m; Type: FK CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE ONLY resource.resourceitemdatasecuritytoken
    ADD CONSTRAINT fkhcwacfqh36t292fibcvgdtr3m FOREIGN KEY (originalsourcesystemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4923 (class 2606 OID 32830)
-- Name: resourceitemdatasecuritytoken fkhpb19eadjrkws43r16fgy3mjl; Type: FK CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE ONLY resource.resourceitemdatasecuritytoken
    ADD CONSTRAINT fkhpb19eadjrkws43r16fgy3mjl FOREIGN KEY (enterpriseid) REFERENCES dbo.enterprise(enterpriseid);


--
-- TOC entry 4919 (class 2606 OID 32820)
-- Name: resourceitemdata fkht0uy53uvuomcxp1m9d76l2f2; Type: FK CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE ONLY resource.resourceitemdata
    ADD CONSTRAINT fkht0uy53uvuomcxp1m9d76l2f2 FOREIGN KEY (resourceitemid) REFERENCES resource.resourceitem(resourceitemid);


--
-- TOC entry 4991 (class 2606 OID 33175)
-- Name: resourceitemxresourceitemtypesecuritytoken fki8ap9tegp5e5e2687a8rlolq3; Type: FK CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE ONLY resource.resourceitemxresourceitemtypesecuritytoken
    ADD CONSTRAINT fki8ap9tegp5e5e2687a8rlolq3 FOREIGN KEY (systemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4949 (class 2606 OID 32945)
-- Name: resourceitemtype fki9pjt06s4hopvhur99agga4k6; Type: FK CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE ONLY resource.resourceitemtype
    ADD CONSTRAINT fki9pjt06s4hopvhur99agga4k6 FOREIGN KEY (activeflagid) REFERENCES dbo.activeflag(activeflagid);


--
-- TOC entry 4931 (class 2606 OID 32870)
-- Name: resourceitemdataxclassification fkiaq7u3ym056htigwqw14ilcsn; Type: FK CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE ONLY resource.resourceitemdataxclassification
    ADD CONSTRAINT fkiaq7u3ym056htigwqw14ilcsn FOREIGN KEY (originalsourcesystemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4920 (class 2606 OID 32805)
-- Name: resourceitemdata fkigdv809qbjivrueu1pbkvr9j3; Type: FK CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE ONLY resource.resourceitemdata
    ADD CONSTRAINT fkigdv809qbjivrueu1pbkvr9j3 FOREIGN KEY (enterpriseid) REFERENCES dbo.enterprise(enterpriseid);


--
-- TOC entry 4992 (class 2606 OID 33180)
-- Name: resourceitemxresourceitemtypesecuritytoken fkiwc4nixnum6cvlrtlpopjlrrp; Type: FK CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE ONLY resource.resourceitemxresourceitemtypesecuritytoken
    ADD CONSTRAINT fkiwc4nixnum6cvlrtlpopjlrrp FOREIGN KEY (resourceitemxresourceitemtypeid) REFERENCES resource.resourceitemxresourceitemtype(resourceitemxresourceitemtypeid);


--
-- TOC entry 4960 (class 2606 OID 33010)
-- Name: resourceitemxclassification fkjrhfr20kvirghjffwvd80r39m; Type: FK CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE ONLY resource.resourceitemxclassification
    ADD CONSTRAINT fkjrhfr20kvirghjffwvd80r39m FOREIGN KEY (originalsourcesystemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4928 (class 2606 OID 32855)
-- Name: resourceitemdataxclassification fkjsk9t2sreiwpv88hanu3khr74; Type: FK CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE ONLY resource.resourceitemdataxclassification
    ADD CONSTRAINT fkjsk9t2sreiwpv88hanu3khr74 FOREIGN KEY (activeflagid) REFERENCES dbo.activeflag(activeflagid);


--
-- TOC entry 4993 (class 2606 OID 33160)
-- Name: resourceitemxresourceitemtypesecuritytoken fkkj0hvw87ec6j3mlt4m96ky91p; Type: FK CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE ONLY resource.resourceitemxresourceitemtypesecuritytoken
    ADD CONSTRAINT fkkj0hvw87ec6j3mlt4m96ky91p FOREIGN KEY (enterpriseid) REFERENCES dbo.enterprise(enterpriseid);


--
-- TOC entry 4926 (class 2606 OID 32845)
-- Name: resourceitemdatasecuritytoken fkll81jjs16jumy0yll1ql0c21; Type: FK CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE ONLY resource.resourceitemdatasecuritytoken
    ADD CONSTRAINT fkll81jjs16jumy0yll1ql0c21 FOREIGN KEY (systemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4943 (class 2606 OID 32920)
-- Name: resourceitemsecuritytoken fklri3qjyh3vwi43xe3crxm8xdq; Type: FK CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE ONLY resource.resourceitemsecuritytoken
    ADD CONSTRAINT fklri3qjyh3vwi43xe3crxm8xdq FOREIGN KEY (enterpriseid) REFERENCES dbo.enterprise(enterpriseid);


--
-- TOC entry 4981 (class 2606 OID 33120)
-- Name: resourceitemxresourceitemtype fklsy82htwl5f124qkuxb5cuq8y; Type: FK CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE ONLY resource.resourceitemxresourceitemtype
    ADD CONSTRAINT fklsy82htwl5f124qkuxb5cuq8y FOREIGN KEY (activeflagid) REFERENCES dbo.activeflag(activeflagid);


--
-- TOC entry 4979 (class 2606 OID 33095)
-- Name: resourceitemxresourceitemsecuritytoken fkm9ls44frfnxy77f2yitjmfb4w; Type: FK CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE ONLY resource.resourceitemxresourceitemsecuritytoken
    ADD CONSTRAINT fkm9ls44frfnxy77f2yitjmfb4w FOREIGN KEY (enterpriseid) REFERENCES dbo.enterprise(enterpriseid);


--
-- TOC entry 4953 (class 2606 OID 32980)
-- Name: resourceitemtypesecuritytoken fkml0sk2cyf2m3s7w1idm5b1fv2; Type: FK CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE ONLY resource.resourceitemtypesecuritytoken
    ADD CONSTRAINT fkml0sk2cyf2m3s7w1idm5b1fv2 FOREIGN KEY (securitytokenid) REFERENCES security.securitytoken(securitytokenid);


--
-- TOC entry 4980 (class 2606 OID 33090)
-- Name: resourceitemxresourceitemsecuritytoken fkmylvnsbdyx59j60lktojr074f; Type: FK CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE ONLY resource.resourceitemxresourceitemsecuritytoken
    ADD CONSTRAINT fkmylvnsbdyx59j60lktojr074f FOREIGN KEY (activeflagid) REFERENCES dbo.activeflag(activeflagid);


--
-- TOC entry 4965 (class 2606 OID 33030)
-- Name: resourceitemxclassificationsecuritytoken fknvnmjioardv2gat6o4shntlxd; Type: FK CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE ONLY resource.resourceitemxclassificationsecuritytoken
    ADD CONSTRAINT fknvnmjioardv2gat6o4shntlxd FOREIGN KEY (enterpriseid) REFERENCES dbo.enterprise(enterpriseid);


--
-- TOC entry 4944 (class 2606 OID 32925)
-- Name: resourceitemsecuritytoken fko0923krxdcp1issfcfcl7a0a7; Type: FK CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE ONLY resource.resourceitemsecuritytoken
    ADD CONSTRAINT fko0923krxdcp1issfcfcl7a0a7 FOREIGN KEY (originalsourcesystemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4961 (class 2606 OID 33005)
-- Name: resourceitemxclassification fko1fubrmjfvsi58rt1amf7u7fw; Type: FK CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE ONLY resource.resourceitemxclassification
    ADD CONSTRAINT fko1fubrmjfvsi58rt1amf7u7fw FOREIGN KEY (systemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4954 (class 2606 OID 32975)
-- Name: resourceitemtypesecuritytoken fko2usew1i3g60p11uwfuv2ajmd; Type: FK CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE ONLY resource.resourceitemtypesecuritytoken
    ADD CONSTRAINT fko2usew1i3g60p11uwfuv2ajmd FOREIGN KEY (originalsourcesystemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4966 (class 2606 OID 33050)
-- Name: resourceitemxclassificationsecuritytoken fko7a0ts75p7f0b5ekl3e5nr2jx; Type: FK CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE ONLY resource.resourceitemxclassificationsecuritytoken
    ADD CONSTRAINT fko7a0ts75p7f0b5ekl3e5nr2jx FOREIGN KEY (resourceitemxclassificationid) REFERENCES resource.resourceitemxclassification(resourceitemxclassificationid);


--
-- TOC entry 4921 (class 2606 OID 32815)
-- Name: resourceitemdata fkole6gj7jyg1pdvt918naap8n5; Type: FK CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE ONLY resource.resourceitemdata
    ADD CONSTRAINT fkole6gj7jyg1pdvt918naap8n5 FOREIGN KEY (originalsourcesystemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4925 (class 2606 OID 32840)
-- Name: resourceitemdatasecuritytoken fkomv4xy8vibg1lxewrtq0lj42t; Type: FK CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE ONLY resource.resourceitemdatasecuritytoken
    ADD CONSTRAINT fkomv4xy8vibg1lxewrtq0lj42t FOREIGN KEY (securitytokenid) REFERENCES security.securitytoken(securitytokenid);


--
-- TOC entry 4929 (class 2606 OID 32860)
-- Name: resourceitemdataxclassification fkox913icfxl6rbqwjw04y7odq6; Type: FK CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE ONLY resource.resourceitemdataxclassification
    ADD CONSTRAINT fkox913icfxl6rbqwjw04y7odq6 FOREIGN KEY (enterpriseid) REFERENCES dbo.enterprise(enterpriseid);


--
-- TOC entry 4972 (class 2606 OID 33075)
-- Name: resourceitemxresourceitem fkoyv9alf1u6nf5wbkwvj207pd6; Type: FK CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE ONLY resource.resourceitemxresourceitem
    ADD CONSTRAINT fkoyv9alf1u6nf5wbkwvj207pd6 FOREIGN KEY (classificationid) REFERENCES classification.classification(classificationid);


--
-- TOC entry 4932 (class 2606 OID 32875)
-- Name: resourceitemdataxclassification fkp9oe45dlyale0o4ug986voncj; Type: FK CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE ONLY resource.resourceitemdataxclassification
    ADD CONSTRAINT fkp9oe45dlyale0o4ug986voncj FOREIGN KEY (classificationid) REFERENCES classification.classification(classificationid);


--
-- TOC entry 4987 (class 2606 OID 33150)
-- Name: resourceitemxresourceitemtype fkpirwchdhynd810k5mp231e2sh; Type: FK CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE ONLY resource.resourceitemxresourceitemtype
    ADD CONSTRAINT fkpirwchdhynd810k5mp231e2sh FOREIGN KEY (resourceitemtypeid) REFERENCES resource.resourceitemtype(resourceitemtypeid);


--
-- TOC entry 4967 (class 2606 OID 33035)
-- Name: resourceitemxclassificationsecuritytoken fkq4lxbn9s7559mqng8hmhau4s4; Type: FK CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE ONLY resource.resourceitemxclassificationsecuritytoken
    ADD CONSTRAINT fkq4lxbn9s7559mqng8hmhau4s4 FOREIGN KEY (originalsourcesystemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4985 (class 2606 OID 33140)
-- Name: resourceitemxresourceitemtype fkqbn3rldlgiftql2sxfoc020k7; Type: FK CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE ONLY resource.resourceitemxresourceitemtype
    ADD CONSTRAINT fkqbn3rldlgiftql2sxfoc020k7 FOREIGN KEY (classificationid) REFERENCES classification.classification(classificationid);


--
-- TOC entry 4983 (class 2606 OID 33130)
-- Name: resourceitemxresourceitemtype fkr19kv8ueg4bkxh2pvj156qvwk; Type: FK CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE ONLY resource.resourceitemxresourceitemtype
    ADD CONSTRAINT fkr19kv8ueg4bkxh2pvj156qvwk FOREIGN KEY (systemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4955 (class 2606 OID 32990)
-- Name: resourceitemtypesecuritytoken fkrpcr2w5s8sdyuh9e5bsiqpsl; Type: FK CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE ONLY resource.resourceitemtypesecuritytoken
    ADD CONSTRAINT fkrpcr2w5s8sdyuh9e5bsiqpsl FOREIGN KEY (resourceitemtypeid) REFERENCES resource.resourceitemtype(resourceitemtypeid);


--
-- TOC entry 4982 (class 2606 OID 33125)
-- Name: resourceitemxresourceitemtype fksw4ntk83q20lccxb0t56uvvun; Type: FK CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE ONLY resource.resourceitemxresourceitemtype
    ADD CONSTRAINT fksw4ntk83q20lccxb0t56uvvun FOREIGN KEY (enterpriseid) REFERENCES dbo.enterprise(enterpriseid);


--
-- TOC entry 4945 (class 2606 OID 32935)
-- Name: resourceitemsecuritytoken fktj25xftljow24gev8hl3dsjw8; Type: FK CONSTRAINT; Schema: resource; Owner: postgres
--

ALTER TABLE ONLY resource.resourceitemsecuritytoken
    ADD CONSTRAINT fktj25xftljow24gev8hl3dsjw8 FOREIGN KEY (systemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 5004 (class 2606 OID 33250)
-- Name: rulestype fk11qj2su8f2sy9nvcjss76yfqm; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE ONLY rules.rulestype
    ADD CONSTRAINT fk11qj2su8f2sy9nvcjss76yfqm FOREIGN KEY (originalsourcesystemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 5008 (class 2606 OID 33270)
-- Name: rulestypessecuritytoken fk12fsootndlsrm2gkytfg2gfw0; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE ONLY rules.rulestypessecuritytoken
    ADD CONSTRAINT fk12fsootndlsrm2gkytfg2gfw0 FOREIGN KEY (securitytokenid) REFERENCES security.securitytoken(securitytokenid);


--
-- TOC entry 5095 (class 2606 OID 33690)
-- Name: rulesxresourceitem fk19cj3ortt8x7wa4mnkohkpebi; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE ONLY rules.rulesxresourceitem
    ADD CONSTRAINT fk19cj3ortt8x7wa4mnkohkpebi FOREIGN KEY (rulesid) REFERENCES rules.rules(rulesid);


--
-- TOC entry 5083 (class 2606 OID 33655)
-- Name: rulesxproductsecuritytoken fk1inr52n9q3ax5rhqtjhthh78r; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE ONLY rules.rulesxproductsecuritytoken
    ADD CONSTRAINT fk1inr52n9q3ax5rhqtjhthh78r FOREIGN KEY (rulesxproductid) REFERENCES rules.rulesxproduct(rulesxproductid);


--
-- TOC entry 5096 (class 2606 OID 33720)
-- Name: rulesxresourceitemsecuritytoken fk23yuc0ofnmca2ig2dd1wx5ol8; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE ONLY rules.rulesxresourceitemsecuritytoken
    ADD CONSTRAINT fk23yuc0ofnmca2ig2dd1wx5ol8 FOREIGN KEY (rulesxresourceitemid) REFERENCES rules.rulesxresourceitem(rulesxresourceitemid);


--
-- TOC entry 5045 (class 2606 OID 33445)
-- Name: rulesxarrangementssecuritytoken fk2xvs1pi7rc32lemnyvj01j301; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE ONLY rules.rulesxarrangementssecuritytoken
    ADD CONSTRAINT fk2xvs1pi7rc32lemnyvj01j301 FOREIGN KEY (enterpriseid) REFERENCES dbo.enterprise(enterpriseid);


--
-- TOC entry 5106 (class 2606 OID 33745)
-- Name: rulesxrules fk31s09rv16v0yhe5aac7l6ele3; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE ONLY rules.rulesxrules
    ADD CONSTRAINT fk31s09rv16v0yhe5aac7l6ele3 FOREIGN KEY (classificationid) REFERENCES classification.classification(classificationid);


--
-- TOC entry 5102 (class 2606 OID 33725)
-- Name: rulesxrules fk37pin4ub4l14xqqanw037awej; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE ONLY rules.rulesxrules
    ADD CONSTRAINT fk37pin4ub4l14xqqanw037awej FOREIGN KEY (activeflagid) REFERENCES dbo.activeflag(activeflagid);


--
-- TOC entry 5051 (class 2606 OID 33475)
-- Name: rulesxclassification fk38k4xit5ty330vs01iv18sikh; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE ONLY rules.rulesxclassification
    ADD CONSTRAINT fk38k4xit5ty330vs01iv18sikh FOREIGN KEY (enterpriseid) REFERENCES dbo.enterprise(enterpriseid);


--
-- TOC entry 5068 (class 2606 OID 33555)
-- Name: rulesxinvolvedparty fk3djrrw59r13qf53lxmls4da7g; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE ONLY rules.rulesxinvolvedparty
    ADD CONSTRAINT fk3djrrw59r13qf53lxmls4da7g FOREIGN KEY (involvedpartyid) REFERENCES party.involvedparty(involvedpartyid);


--
-- TOC entry 5014 (class 2606 OID 33300)
-- Name: rulestypexclassification fk3fotawtun2be400jw1c5mcxr5; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE ONLY rules.rulestypexclassification
    ADD CONSTRAINT fk3fotawtun2be400jw1c5mcxr5 FOREIGN KEY (originalsourcesystemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 5020 (class 2606 OID 33320)
-- Name: rulestypexclassificationsecuritytoken fk3nsh18vueud5iybj4xk2t4j6r; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE ONLY rules.rulestypexclassificationsecuritytoken
    ADD CONSTRAINT fk3nsh18vueud5iybj4xk2t4j6r FOREIGN KEY (enterpriseid) REFERENCES dbo.enterprise(enterpriseid);


--
-- TOC entry 5070 (class 2606 OID 33585)
-- Name: rulesxinvolvedpartysecuritytoken fk3qdk5vxgo4txkaaj5a8y8giwd; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE ONLY rules.rulesxinvolvedpartysecuritytoken
    ADD CONSTRAINT fk3qdk5vxgo4txkaaj5a8y8giwd FOREIGN KEY (systemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 5093 (class 2606 OID 33680)
-- Name: rulesxresourceitem fk41dmnx4ijklqr44tlpomg7it5; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE ONLY rules.rulesxresourceitem
    ADD CONSTRAINT fk41dmnx4ijklqr44tlpomg7it5 FOREIGN KEY (classificationid) REFERENCES classification.classification(classificationid);


--
-- TOC entry 5046 (class 2606 OID 33440)
-- Name: rulesxarrangementssecuritytoken fk4dxpxc1hj2n1ik4s3ms69w3kw; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE ONLY rules.rulesxarrangementssecuritytoken
    ADD CONSTRAINT fk4dxpxc1hj2n1ik4s3ms69w3kw FOREIGN KEY (activeflagid) REFERENCES dbo.activeflag(activeflagid);


--
-- TOC entry 5084 (class 2606 OID 33635)
-- Name: rulesxproductsecuritytoken fk4g0qn2ufofwya9mua7k1bip3n; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE ONLY rules.rulesxproductsecuritytoken
    ADD CONSTRAINT fk4g0qn2ufofwya9mua7k1bip3n FOREIGN KEY (enterpriseid) REFERENCES dbo.enterprise(enterpriseid);


--
-- TOC entry 5109 (class 2606 OID 33785)
-- Name: rulesxrulessecuritytoken fk4kklxff6jc1n1bo39g3sdqrix; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE ONLY rules.rulesxrulessecuritytoken
    ADD CONSTRAINT fk4kklxff6jc1n1bo39g3sdqrix FOREIGN KEY (rulesxrulesid) REFERENCES rules.rulesxrules(rulesxrulesid);


--
-- TOC entry 5039 (class 2606 OID 33425)
-- Name: rulesxarrangement fk4nj9r8fu6b1ymd9o8tdryxrtx; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE ONLY rules.rulesxarrangement
    ADD CONSTRAINT fk4nj9r8fu6b1ymd9o8tdryxrtx FOREIGN KEY (originalsourcesystemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 5021 (class 2606 OID 33335)
-- Name: rulestypexclassificationsecuritytoken fk4rylunbc8qipcw0n020nhab5b; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE ONLY rules.rulestypexclassificationsecuritytoken
    ADD CONSTRAINT fk4rylunbc8qipcw0n020nhab5b FOREIGN KEY (systemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 5085 (class 2606 OID 33645)
-- Name: rulesxproductsecuritytoken fk50a41rvtkyewefjmf4e8l52sw; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE ONLY rules.rulesxproductsecuritytoken
    ADD CONSTRAINT fk50a41rvtkyewefjmf4e8l52sw FOREIGN KEY (securitytokenid) REFERENCES security.securitytoken(securitytokenid);


--
-- TOC entry 4998 (class 2606 OID 33210)
-- Name: rulessecuritytoken fk57dd0fcy24o8k2spnqrykgk2a; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE ONLY rules.rulessecuritytoken
    ADD CONSTRAINT fk57dd0fcy24o8k2spnqrykgk2a FOREIGN KEY (enterpriseid) REFERENCES dbo.enterprise(enterpriseid);


--
-- TOC entry 5080 (class 2606 OID 33615)
-- Name: rulesxproduct fk57dubv678gaj1eatqjof0x0nb; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE ONLY rules.rulesxproduct
    ADD CONSTRAINT fk57dubv678gaj1eatqjof0x0nb FOREIGN KEY (classificationid) REFERENCES classification.classification(classificationid);


--
-- TOC entry 5097 (class 2606 OID 33710)
-- Name: rulesxresourceitemsecuritytoken fk5dv1vwep6v9958vjdse4henw0; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE ONLY rules.rulesxresourceitemsecuritytoken
    ADD CONSTRAINT fk5dv1vwep6v9958vjdse4henw0 FOREIGN KEY (securitytokenid) REFERENCES security.securitytoken(securitytokenid);


--
-- TOC entry 5090 (class 2606 OID 33665)
-- Name: rulesxresourceitem fk5fe2nqc5oe2o0pqnptrrciyra; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE ONLY rules.rulesxresourceitem
    ADD CONSTRAINT fk5fe2nqc5oe2o0pqnptrrciyra FOREIGN KEY (enterpriseid) REFERENCES dbo.enterprise(enterpriseid);


--
-- TOC entry 4999 (class 2606 OID 33215)
-- Name: rulessecuritytoken fk5hjfvbnd3js59ca3fowm88h6t; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE ONLY rules.rulessecuritytoken
    ADD CONSTRAINT fk5hjfvbnd3js59ca3fowm88h6t FOREIGN KEY (originalsourcesystemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 5076 (class 2606 OID 33595)
-- Name: rulesxproduct fk5m9wrb374b5g1wpwkj3jr09y6; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE ONLY rules.rulesxproduct
    ADD CONSTRAINT fk5m9wrb374b5g1wpwkj3jr09y6 FOREIGN KEY (activeflagid) REFERENCES dbo.activeflag(activeflagid);


--
-- TOC entry 5009 (class 2606 OID 33255)
-- Name: rulestypessecuritytoken fk5tck1lwdgo3i3lbwyphxlsilw; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE ONLY rules.rulestypessecuritytoken
    ADD CONSTRAINT fk5tck1lwdgo3i3lbwyphxlsilw FOREIGN KEY (activeflagid) REFERENCES dbo.activeflag(activeflagid);


--
-- TOC entry 5066 (class 2606 OID 33545)
-- Name: rulesxinvolvedparty fk5ujdw4x8upupcaml412wan0p7; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE ONLY rules.rulesxinvolvedparty
    ADD CONSTRAINT fk5ujdw4x8upupcaml412wan0p7 FOREIGN KEY (originalsourcesystemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 5064 (class 2606 OID 33535)
-- Name: rulesxinvolvedparty fk611oa399omtv631o66ausvb55; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE ONLY rules.rulesxinvolvedparty
    ADD CONSTRAINT fk611oa399omtv631o66ausvb55 FOREIGN KEY (enterpriseid) REFERENCES dbo.enterprise(enterpriseid);


--
-- TOC entry 5077 (class 2606 OID 33600)
-- Name: rulesxproduct fk6a018wimk1mn1ucjsg7m6v42e; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE ONLY rules.rulesxproduct
    ADD CONSTRAINT fk6a018wimk1mn1ucjsg7m6v42e FOREIGN KEY (enterpriseid) REFERENCES dbo.enterprise(enterpriseid);


--
-- TOC entry 5032 (class 2606 OID 33375)
-- Name: rulestypexresourceitem fk6hivvn41g61n8v941egrae19i; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE ONLY rules.rulestypexresourceitem
    ADD CONSTRAINT fk6hivvn41g61n8v941egrae19i FOREIGN KEY (rulestypeid) REFERENCES rules.rulestype(rulestypeid);


--
-- TOC entry 5117 (class 2606 OID 33800)
-- Name: rulesxrulestype fk710wj6aj8es1ht1o9fxdehtua; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE ONLY rules.rulesxrulestype
    ADD CONSTRAINT fk710wj6aj8es1ht1o9fxdehtua FOREIGN KEY (systemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 5030 (class 2606 OID 33365)
-- Name: rulestypexresourceitem fk7an7eg5j3seoaxajls3knmrwt; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE ONLY rules.rulestypexresourceitem
    ADD CONSTRAINT fk7an7eg5j3seoaxajls3knmrwt FOREIGN KEY (classificationid) REFERENCES classification.classification(classificationid);


--
-- TOC entry 5040 (class 2606 OID 33410)
-- Name: rulesxarrangement fk7kbnsdojwukxxly9vxqkraun2; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE ONLY rules.rulesxarrangement
    ADD CONSTRAINT fk7kbnsdojwukxxly9vxqkraun2 FOREIGN KEY (activeflagid) REFERENCES dbo.activeflag(activeflagid);


--
-- TOC entry 5041 (class 2606 OID 33420)
-- Name: rulesxarrangement fk7kfpfoyfd5mn1x45215dlee28; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE ONLY rules.rulesxarrangement
    ADD CONSTRAINT fk7kfpfoyfd5mn1x45215dlee28 FOREIGN KEY (systemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 5071 (class 2606 OID 33580)
-- Name: rulesxinvolvedpartysecuritytoken fk7lvh9pa4gweufl5x0ubtl38my; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE ONLY rules.rulesxinvolvedpartysecuritytoken
    ADD CONSTRAINT fk7lvh9pa4gweufl5x0ubtl38my FOREIGN KEY (securitytokenid) REFERENCES security.securitytoken(securitytokenid);


--
-- TOC entry 5052 (class 2606 OID 33480)
-- Name: rulesxclassification fk7nyvj635fp8si7pq0qetkfhpb; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE ONLY rules.rulesxclassification
    ADD CONSTRAINT fk7nyvj635fp8si7pq0qetkfhpb FOREIGN KEY (systemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 5005 (class 2606 OID 33240)
-- Name: rulestype fk7v51o36ciw802dj5m82giq97q; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE ONLY rules.rulestype
    ADD CONSTRAINT fk7v51o36ciw802dj5m82giq97q FOREIGN KEY (enterpriseid) REFERENCES dbo.enterprise(enterpriseid);


--
-- TOC entry 5015 (class 2606 OID 33310)
-- Name: rulestypexclassification fk87mojdqprestm5uqmw925bqf1; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE ONLY rules.rulestypexclassification
    ADD CONSTRAINT fk87mojdqprestm5uqmw925bqf1 FOREIGN KEY (rulestypeid) REFERENCES rules.rulestype(rulestypeid);


--
-- TOC entry 5118 (class 2606 OID 33805)
-- Name: rulesxrulestype fk8hvei78rfy1byxtb36lwwst06; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE ONLY rules.rulesxrulestype
    ADD CONSTRAINT fk8hvei78rfy1byxtb36lwwst06 FOREIGN KEY (originalsourcesystemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 5065 (class 2606 OID 33540)
-- Name: rulesxinvolvedparty fk8i91cawihcqkxe2fr9oawrshw; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE ONLY rules.rulesxinvolvedparty
    ADD CONSTRAINT fk8i91cawihcqkxe2fr9oawrshw FOREIGN KEY (systemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 5053 (class 2606 OID 33470)
-- Name: rulesxclassification fk8txg0h4hi98qbgslaodff7dyt; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE ONLY rules.rulesxclassification
    ADD CONSTRAINT fk8txg0h4hi98qbgslaodff7dyt FOREIGN KEY (activeflagid) REFERENCES dbo.activeflag(activeflagid);


--
-- TOC entry 5033 (class 2606 OID 33385)
-- Name: rulestypexresourceitemsecuritytoken fk8xs3tib17edep24d44482bmjp; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE ONLY rules.rulestypexresourceitemsecuritytoken
    ADD CONSTRAINT fk8xs3tib17edep24d44482bmjp FOREIGN KEY (enterpriseid) REFERENCES dbo.enterprise(enterpriseid);


--
-- TOC entry 5072 (class 2606 OID 33575)
-- Name: rulesxinvolvedpartysecuritytoken fk903el1e8i3wxjag53njlscvkg; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE ONLY rules.rulesxinvolvedpartysecuritytoken
    ADD CONSTRAINT fk903el1e8i3wxjag53njlscvkg FOREIGN KEY (originalsourcesystemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 5016 (class 2606 OID 33290)
-- Name: rulestypexclassification fk9662odo6kpnj3a66uyqqh9gph; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE ONLY rules.rulestypexclassification
    ADD CONSTRAINT fk9662odo6kpnj3a66uyqqh9gph FOREIGN KEY (enterpriseid) REFERENCES dbo.enterprise(enterpriseid);


--
-- TOC entry 5010 (class 2606 OID 33280)
-- Name: rulestypessecuritytoken fk9aexqcupg3q8il9en4moulmo1; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE ONLY rules.rulestypessecuritytoken
    ADD CONSTRAINT fk9aexqcupg3q8il9en4moulmo1 FOREIGN KEY (rulestypesid) REFERENCES rules.rulestype(rulestypeid);


--
-- TOC entry 5078 (class 2606 OID 33605)
-- Name: rulesxproduct fk9t2smyfxndgfnuoht0l8q42rb; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE ONLY rules.rulesxproduct
    ADD CONSTRAINT fk9t2smyfxndgfnuoht0l8q42rb FOREIGN KEY (systemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 5086 (class 2606 OID 33650)
-- Name: rulesxproductsecuritytoken fka1ai0uri46j0quxeqcikkdclv; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE ONLY rules.rulesxproductsecuritytoken
    ADD CONSTRAINT fka1ai0uri46j0quxeqcikkdclv FOREIGN KEY (systemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 5022 (class 2606 OID 33315)
-- Name: rulestypexclassificationsecuritytoken fkamvwb2lq6eh8s3gobs9amn1ns; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE ONLY rules.rulestypexclassificationsecuritytoken
    ADD CONSTRAINT fkamvwb2lq6eh8s3gobs9amn1ns FOREIGN KEY (activeflagid) REFERENCES dbo.activeflag(activeflagid);


--
-- TOC entry 5057 (class 2606 OID 33500)
-- Name: rulesxclassificationsecuritytoken fkb0vp45d3tkxwvy5938ua9n809; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE ONLY rules.rulesxclassificationsecuritytoken
    ADD CONSTRAINT fkb0vp45d3tkxwvy5938ua9n809 FOREIGN KEY (activeflagid) REFERENCES dbo.activeflag(activeflagid);


--
-- TOC entry 5054 (class 2606 OID 33495)
-- Name: rulesxclassification fkbbvhygyarc5j4gi7wjnd052p3; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE ONLY rules.rulesxclassification
    ADD CONSTRAINT fkbbvhygyarc5j4gi7wjnd052p3 FOREIGN KEY (rulesid) REFERENCES rules.rules(rulesid);


--
-- TOC entry 5122 (class 2606 OID 33825)
-- Name: rulesxrulestypesecuritytoken fkbn0acfejqvxbal40304ibsbr5; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE ONLY rules.rulesxrulestypesecuritytoken
    ADD CONSTRAINT fkbn0acfejqvxbal40304ibsbr5 FOREIGN KEY (activeflagid) REFERENCES dbo.activeflag(activeflagid);


--
-- TOC entry 5115 (class 2606 OID 33790)
-- Name: rulesxrulestype fkbn4oo9asbe39dle7ow6buqvt7; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE ONLY rules.rulesxrulestype
    ADD CONSTRAINT fkbn4oo9asbe39dle7ow6buqvt7 FOREIGN KEY (activeflagid) REFERENCES dbo.activeflag(activeflagid);


--
-- TOC entry 5120 (class 2606 OID 33815)
-- Name: rulesxrulestype fkbohku2sim3tc8ey86rq8psqpt; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE ONLY rules.rulesxrulestype
    ADD CONSTRAINT fkbohku2sim3tc8ey86rq8psqpt FOREIGN KEY (rulesid) REFERENCES rules.rules(rulesid);


--
-- TOC entry 4994 (class 2606 OID 33190)
-- Name: rules fkbpbgfk1w8xjpcvla2wtr28rfm; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE ONLY rules.rules
    ADD CONSTRAINT fkbpbgfk1w8xjpcvla2wtr28rfm FOREIGN KEY (enterpriseid) REFERENCES dbo.enterprise(enterpriseid);


--
-- TOC entry 5123 (class 2606 OID 33835)
-- Name: rulesxrulestypesecuritytoken fkby3adp3oxnvi7q571ij36jj0v; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE ONLY rules.rulesxrulestypesecuritytoken
    ADD CONSTRAINT fkby3adp3oxnvi7q571ij36jj0v FOREIGN KEY (originalsourcesystemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 5034 (class 2606 OID 33380)
-- Name: rulestypexresourceitemsecuritytoken fkcdu908ybhdghbjvhquhpfucsr; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE ONLY rules.rulestypexresourceitemsecuritytoken
    ADD CONSTRAINT fkcdu908ybhdghbjvhquhpfucsr FOREIGN KEY (activeflagid) REFERENCES dbo.activeflag(activeflagid);


--
-- TOC entry 5017 (class 2606 OID 33305)
-- Name: rulestypexclassification fkcmpyykrmpk1epxo9jmicem78i; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE ONLY rules.rulestypexclassification
    ADD CONSTRAINT fkcmpyykrmpk1epxo9jmicem78i FOREIGN KEY (classificationid) REFERENCES classification.classification(classificationid);


--
-- TOC entry 5023 (class 2606 OID 33330)
-- Name: rulestypexclassificationsecuritytoken fkcn088ejtlnauiohbrpvvufegu; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE ONLY rules.rulestypexclassificationsecuritytoken
    ADD CONSTRAINT fkcn088ejtlnauiohbrpvvufegu FOREIGN KEY (securitytokenid) REFERENCES security.securitytoken(securitytokenid);


--
-- TOC entry 5047 (class 2606 OID 33455)
-- Name: rulesxarrangementssecuritytoken fkd1ylioumsfoo6scfb0fdouur7; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE ONLY rules.rulesxarrangementssecuritytoken
    ADD CONSTRAINT fkd1ylioumsfoo6scfb0fdouur7 FOREIGN KEY (securitytokenid) REFERENCES security.securitytoken(securitytokenid);


--
-- TOC entry 5018 (class 2606 OID 33295)
-- Name: rulestypexclassification fkdck5vk5rjw51e9u8ducstsysg; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE ONLY rules.rulestypexclassification
    ADD CONSTRAINT fkdck5vk5rjw51e9u8ducstsysg FOREIGN KEY (systemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 5119 (class 2606 OID 33810)
-- Name: rulesxrulestype fkdly04cjef5yj3ne8v1uwb8h12; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE ONLY rules.rulesxrulestype
    ADD CONSTRAINT fkdly04cjef5yj3ne8v1uwb8h12 FOREIGN KEY (classificationid) REFERENCES classification.classification(classificationid);


--
-- TOC entry 5011 (class 2606 OID 33275)
-- Name: rulestypessecuritytoken fkeciu87p8mk0yiel2t1bwipg9a; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE ONLY rules.rulestypessecuritytoken
    ADD CONSTRAINT fkeciu87p8mk0yiel2t1bwipg9a FOREIGN KEY (systemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 5000 (class 2606 OID 33225)
-- Name: rulessecuritytoken fkenyc4oy7p7bpluwr2incgf2m6; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE ONLY rules.rulessecuritytoken
    ADD CONSTRAINT fkenyc4oy7p7bpluwr2incgf2m6 FOREIGN KEY (systemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 5042 (class 2606 OID 33415)
-- Name: rulesxarrangement fkeu7bn1gb6dg7xt097ognxjwh5; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE ONLY rules.rulesxarrangement
    ADD CONSTRAINT fkeu7bn1gb6dg7xt097ognxjwh5 FOREIGN KEY (enterpriseid) REFERENCES dbo.enterprise(enterpriseid);


--
-- TOC entry 5082 (class 2606 OID 33625)
-- Name: rulesxproduct fkf3t7xp42nph7ypmy38rtkr9c8; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE ONLY rules.rulesxproduct
    ADD CONSTRAINT fkf3t7xp42nph7ypmy38rtkr9c8 FOREIGN KEY (rulesid) REFERENCES rules.rules(rulesid);


--
-- TOC entry 5006 (class 2606 OID 33245)
-- Name: rulestype fkf7030hfrlt8o2dxdarmt5ftrp; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE ONLY rules.rulestype
    ADD CONSTRAINT fkf7030hfrlt8o2dxdarmt5ftrp FOREIGN KEY (systemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 5043 (class 2606 OID 33435)
-- Name: rulesxarrangement fkfj9ondul2lkfugd0u6vdxq1yi; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE ONLY rules.rulesxarrangement
    ADD CONSTRAINT fkfj9ondul2lkfugd0u6vdxq1yi FOREIGN KEY (rulesid) REFERENCES rules.rules(rulesid);


--
-- TOC entry 5048 (class 2606 OID 33450)
-- Name: rulesxarrangementssecuritytoken fkflgh5an92uoxffkehd7g7tp8j; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE ONLY rules.rulesxarrangementssecuritytoken
    ADD CONSTRAINT fkflgh5an92uoxffkehd7g7tp8j FOREIGN KEY (originalsourcesystemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 5110 (class 2606 OID 33775)
-- Name: rulesxrulessecuritytoken fkfv9sfhe1kdjwa8dgu136k90ut; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE ONLY rules.rulesxrulessecuritytoken
    ADD CONSTRAINT fkfv9sfhe1kdjwa8dgu136k90ut FOREIGN KEY (securitytokenid) REFERENCES security.securitytoken(securitytokenid);


--
-- TOC entry 5073 (class 2606 OID 33590)
-- Name: rulesxinvolvedpartysecuritytoken fkg0ntb8lcpw9n6p49kpubuyle2; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE ONLY rules.rulesxinvolvedpartysecuritytoken
    ADD CONSTRAINT fkg0ntb8lcpw9n6p49kpubuyle2 FOREIGN KEY (rulesxinvolvedpartyid) REFERENCES rules.rulesxinvolvedparty(rulesxinvolvedpartyid);


--
-- TOC entry 5087 (class 2606 OID 33630)
-- Name: rulesxproductsecuritytoken fkgenrresjjjhv02rh0slya5d88; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE ONLY rules.rulesxproductsecuritytoken
    ADD CONSTRAINT fkgenrresjjjhv02rh0slya5d88 FOREIGN KEY (activeflagid) REFERENCES dbo.activeflag(activeflagid);


--
-- TOC entry 5108 (class 2606 OID 33755)
-- Name: rulesxrules fkgivavwvb91qbin4sxepd6wbs5; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE ONLY rules.rulesxrules
    ADD CONSTRAINT fkgivavwvb91qbin4sxepd6wbs5 FOREIGN KEY (parentrulesid) REFERENCES rules.rules(rulesid);


--
-- TOC entry 5089 (class 2606 OID 33660)
-- Name: rulesxresourceitem fkhubiaglxhl6fy8gx1oei33qbc; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE ONLY rules.rulesxresourceitem
    ADD CONSTRAINT fkhubiaglxhl6fy8gx1oei33qbc FOREIGN KEY (activeflagid) REFERENCES dbo.activeflag(activeflagid);


--
-- TOC entry 5124 (class 2606 OID 33840)
-- Name: rulesxrulestypesecuritytoken fkibc3w3kdwgur69j3lhbi6l0k5; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE ONLY rules.rulesxrulestypesecuritytoken
    ADD CONSTRAINT fkibc3w3kdwgur69j3lhbi6l0k5 FOREIGN KEY (securitytokenid) REFERENCES security.securitytoken(securitytokenid);


--
-- TOC entry 5012 (class 2606 OID 33265)
-- Name: rulestypessecuritytoken fkic2121k0p1edg44c55hog6oe6; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE ONLY rules.rulestypessecuritytoken
    ADD CONSTRAINT fkic2121k0p1edg44c55hog6oe6 FOREIGN KEY (originalsourcesystemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 4995 (class 2606 OID 33195)
-- Name: rules fkihoftg6akowf7mjyheyfmty0i; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE ONLY rules.rules
    ADD CONSTRAINT fkihoftg6akowf7mjyheyfmty0i FOREIGN KEY (systemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 5055 (class 2606 OID 33490)
-- Name: rulesxclassification fkiyjrx7w183fwo2nhmwpqh0stw; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE ONLY rules.rulesxclassification
    ADD CONSTRAINT fkiyjrx7w183fwo2nhmwpqh0stw FOREIGN KEY (classificationid) REFERENCES classification.classification(classificationid);


--
-- TOC entry 5058 (class 2606 OID 33515)
-- Name: rulesxclassificationsecuritytoken fkj60pj71vbrlqc7ijopnpprev7; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE ONLY rules.rulesxclassificationsecuritytoken
    ADD CONSTRAINT fkj60pj71vbrlqc7ijopnpprev7 FOREIGN KEY (securitytokenid) REFERENCES security.securitytoken(securitytokenid);


--
-- TOC entry 5067 (class 2606 OID 33550)
-- Name: rulesxinvolvedparty fkj8719e5kkmd7kd8bpqr71rt3a; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE ONLY rules.rulesxinvolvedparty
    ADD CONSTRAINT fkj8719e5kkmd7kd8bpqr71rt3a FOREIGN KEY (classificationid) REFERENCES classification.classification(classificationid);


--
-- TOC entry 5098 (class 2606 OID 33705)
-- Name: rulesxresourceitemsecuritytoken fkji8ugnof4vkss37iu1mfuy2l4; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE ONLY rules.rulesxresourceitemsecuritytoken
    ADD CONSTRAINT fkji8ugnof4vkss37iu1mfuy2l4 FOREIGN KEY (originalsourcesystemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 5059 (class 2606 OID 33510)
-- Name: rulesxclassificationsecuritytoken fkjklxc6ukplqhj7wa03ah80qh0; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE ONLY rules.rulesxclassificationsecuritytoken
    ADD CONSTRAINT fkjklxc6ukplqhj7wa03ah80qh0 FOREIGN KEY (originalsourcesystemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 5125 (class 2606 OID 33850)
-- Name: rulesxrulestypesecuritytoken fkjr2flixl5hw7uqmh9chynqt4h; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE ONLY rules.rulesxrulestypesecuritytoken
    ADD CONSTRAINT fkjr2flixl5hw7uqmh9chynqt4h FOREIGN KEY (rulesxrulestypeid) REFERENCES rules.rulesxrulestype(rulesxrulestypeid);


--
-- TOC entry 5074 (class 2606 OID 33570)
-- Name: rulesxinvolvedpartysecuritytoken fkju1w7gcf2bu52ok8ibtd40gk4; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE ONLY rules.rulesxinvolvedpartysecuritytoken
    ADD CONSTRAINT fkju1w7gcf2bu52ok8ibtd40gk4 FOREIGN KEY (enterpriseid) REFERENCES dbo.enterprise(enterpriseid);


--
-- TOC entry 5099 (class 2606 OID 33695)
-- Name: rulesxresourceitemsecuritytoken fkju938jbh27xnvuokmjxlnlqos; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE ONLY rules.rulesxresourceitemsecuritytoken
    ADD CONSTRAINT fkju938jbh27xnvuokmjxlnlqos FOREIGN KEY (activeflagid) REFERENCES dbo.activeflag(activeflagid);


--
-- TOC entry 5027 (class 2606 OID 33350)
-- Name: rulestypexresourceitem fkkl9dib27d8h3g00pp40q3vpw4; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE ONLY rules.rulestypexresourceitem
    ADD CONSTRAINT fkkl9dib27d8h3g00pp40q3vpw4 FOREIGN KEY (enterpriseid) REFERENCES dbo.enterprise(enterpriseid);


--
-- TOC entry 5019 (class 2606 OID 33285)
-- Name: rulestypexclassification fkl2497yt06thxeacoue8jbbaoh; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE ONLY rules.rulestypexclassification
    ADD CONSTRAINT fkl2497yt06thxeacoue8jbbaoh FOREIGN KEY (activeflagid) REFERENCES dbo.activeflag(activeflagid);


--
-- TOC entry 5088 (class 2606 OID 33640)
-- Name: rulesxproductsecuritytoken fklaeiodujthn91pwnj8qag1ao6; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE ONLY rules.rulesxproductsecuritytoken
    ADD CONSTRAINT fklaeiodujthn91pwnj8qag1ao6 FOREIGN KEY (originalsourcesystemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 5111 (class 2606 OID 33765)
-- Name: rulesxrulessecuritytoken fklbxxoxpoelx3t2p1g96nqv90q; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE ONLY rules.rulesxrulessecuritytoken
    ADD CONSTRAINT fklbxxoxpoelx3t2p1g96nqv90q FOREIGN KEY (enterpriseid) REFERENCES dbo.enterprise(enterpriseid);


--
-- TOC entry 5035 (class 2606 OID 33400)
-- Name: rulestypexresourceitemsecuritytoken fklhnbp8n1wy13okhhtnxwqu5hy; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE ONLY rules.rulestypexresourceitemsecuritytoken
    ADD CONSTRAINT fklhnbp8n1wy13okhhtnxwqu5hy FOREIGN KEY (systemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 5056 (class 2606 OID 33485)
-- Name: rulesxclassification fklog5hewe9u4c7wb35u4xvv84c; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE ONLY rules.rulesxclassification
    ADD CONSTRAINT fklog5hewe9u4c7wb35u4xvv84c FOREIGN KEY (originalsourcesystemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 5126 (class 2606 OID 33830)
-- Name: rulesxrulestypesecuritytoken fkm2rtvaw8pdy1l3rvbc9wicom3; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE ONLY rules.rulesxrulestypesecuritytoken
    ADD CONSTRAINT fkm2rtvaw8pdy1l3rvbc9wicom3 FOREIGN KEY (enterpriseid) REFERENCES dbo.enterprise(enterpriseid);


--
-- TOC entry 5036 (class 2606 OID 33395)
-- Name: rulestypexresourceitemsecuritytoken fkm6jgd6lrdywbn56w6e0s5awd1; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE ONLY rules.rulestypexresourceitemsecuritytoken
    ADD CONSTRAINT fkm6jgd6lrdywbn56w6e0s5awd1 FOREIGN KEY (securitytokenid) REFERENCES security.securitytoken(securitytokenid);


--
-- TOC entry 5007 (class 2606 OID 33235)
-- Name: rulestype fkm7jdyuyikwo9qlfe6qae0bv3q; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE ONLY rules.rulestype
    ADD CONSTRAINT fkm7jdyuyikwo9qlfe6qae0bv3q FOREIGN KEY (activeflagid) REFERENCES dbo.activeflag(activeflagid);


--
-- TOC entry 5024 (class 2606 OID 33325)
-- Name: rulestypexclassificationsecuritytoken fkmjbouj2e7loyrlu87avtm4gtm; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE ONLY rules.rulestypexclassificationsecuritytoken
    ADD CONSTRAINT fkmjbouj2e7loyrlu87avtm4gtm FOREIGN KEY (originalsourcesystemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 5060 (class 2606 OID 33520)
-- Name: rulesxclassificationsecuritytoken fkmno9p3edeufft6bgiv94tecpa; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE ONLY rules.rulesxclassificationsecuritytoken
    ADD CONSTRAINT fkmno9p3edeufft6bgiv94tecpa FOREIGN KEY (systemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 5001 (class 2606 OID 33205)
-- Name: rulessecuritytoken fkmsl2uqlxr7q5n3bq6e14nknk; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE ONLY rules.rulessecuritytoken
    ADD CONSTRAINT fkmsl2uqlxr7q5n3bq6e14nknk FOREIGN KEY (activeflagid) REFERENCES dbo.activeflag(activeflagid);


--
-- TOC entry 5127 (class 2606 OID 33845)
-- Name: rulesxrulestypesecuritytoken fkmst7g4r9cabb1d78ysr4hvppe; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE ONLY rules.rulesxrulestypesecuritytoken
    ADD CONSTRAINT fkmst7g4r9cabb1d78ysr4hvppe FOREIGN KEY (systemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 5002 (class 2606 OID 33230)
-- Name: rulessecuritytoken fkmtk7w2kpskec0ebnwnrmo8rmp; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE ONLY rules.rulessecuritytoken
    ADD CONSTRAINT fkmtk7w2kpskec0ebnwnrmo8rmp FOREIGN KEY (rulesid) REFERENCES rules.rules(rulesid);


--
-- TOC entry 4996 (class 2606 OID 33185)
-- Name: rules fknh64xmjpd3alokexjnuqr0unw; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE ONLY rules.rules
    ADD CONSTRAINT fknh64xmjpd3alokexjnuqr0unw FOREIGN KEY (activeflagid) REFERENCES dbo.activeflag(activeflagid);


--
-- TOC entry 5003 (class 2606 OID 33220)
-- Name: rulessecuritytoken fknj1ss0ctynt0u0t9nx5wprick; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE ONLY rules.rulessecuritytoken
    ADD CONSTRAINT fknj1ss0ctynt0u0t9nx5wprick FOREIGN KEY (securitytokenid) REFERENCES security.securitytoken(securitytokenid);


--
-- TOC entry 5025 (class 2606 OID 33340)
-- Name: rulestypexclassificationsecuritytoken fknn81qs2b5rstr3u748lvjbl61; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE ONLY rules.rulestypexclassificationsecuritytoken
    ADD CONSTRAINT fknn81qs2b5rstr3u748lvjbl61 FOREIGN KEY (rulestypexclassificationid) REFERENCES rules.rulestypexclassification(rulestypexclassificationid);


--
-- TOC entry 5112 (class 2606 OID 33780)
-- Name: rulesxrulessecuritytoken fknwhg52uwedumbo0s6msu4kvu4; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE ONLY rules.rulesxrulessecuritytoken
    ADD CONSTRAINT fknwhg52uwedumbo0s6msu4kvu4 FOREIGN KEY (systemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 5028 (class 2606 OID 33355)
-- Name: rulestypexresourceitem fko8drrcbtv7to8utrafqb942a; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE ONLY rules.rulestypexresourceitem
    ADD CONSTRAINT fko8drrcbtv7to8utrafqb942a FOREIGN KEY (systemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 5113 (class 2606 OID 33760)
-- Name: rulesxrulessecuritytoken fkohvvumprn80d89qp41tyeesd3; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE ONLY rules.rulesxrulessecuritytoken
    ADD CONSTRAINT fkohvvumprn80d89qp41tyeesd3 FOREIGN KEY (activeflagid) REFERENCES dbo.activeflag(activeflagid);


--
-- TOC entry 5116 (class 2606 OID 33795)
-- Name: rulesxrulestype fkonxshkblvrhnt4fasmfxmnyrf; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE ONLY rules.rulesxrulestype
    ADD CONSTRAINT fkonxshkblvrhnt4fasmfxmnyrf FOREIGN KEY (enterpriseid) REFERENCES dbo.enterprise(enterpriseid);


--
-- TOC entry 5037 (class 2606 OID 33405)
-- Name: rulestypexresourceitemsecuritytoken fkotlbdpur4kwiwymopsenwumbg; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE ONLY rules.rulestypexresourceitemsecuritytoken
    ADD CONSTRAINT fkotlbdpur4kwiwymopsenwumbg FOREIGN KEY (rulestypexresourceitemid) REFERENCES rules.rulestypexresourceitem(rulestypexresourceitemid);


--
-- TOC entry 5061 (class 2606 OID 33525)
-- Name: rulesxclassificationsecuritytoken fkowvdqrq0fiqefy3g85fj60rgq; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE ONLY rules.rulesxclassificationsecuritytoken
    ADD CONSTRAINT fkowvdqrq0fiqefy3g85fj60rgq FOREIGN KEY (rulesxclassificationid) REFERENCES rules.rulesxclassification(rulesxclassificationid);


--
-- TOC entry 5107 (class 2606 OID 33750)
-- Name: rulesxrules fkpahnm53g0d7kjo5tfrask49up; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE ONLY rules.rulesxrules
    ADD CONSTRAINT fkpahnm53g0d7kjo5tfrask49up FOREIGN KEY (childrulesid) REFERENCES rules.rules(rulesid);


--
-- TOC entry 5062 (class 2606 OID 33505)
-- Name: rulesxclassificationsecuritytoken fkq49rw6kbi9gykntudl1mdodl7; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE ONLY rules.rulesxclassificationsecuritytoken
    ADD CONSTRAINT fkq49rw6kbi9gykntudl1mdodl7 FOREIGN KEY (enterpriseid) REFERENCES dbo.enterprise(enterpriseid);


--
-- TOC entry 5069 (class 2606 OID 33560)
-- Name: rulesxinvolvedparty fkqcugfk4tgg63qe2e7fk4h1jou; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE ONLY rules.rulesxinvolvedparty
    ADD CONSTRAINT fkqcugfk4tgg63qe2e7fk4h1jou FOREIGN KEY (rulesid) REFERENCES rules.rules(rulesid);


--
-- TOC entry 4997 (class 2606 OID 33200)
-- Name: rules fkqkhxbrmhfk0ou8sfgrq6w04yn; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE ONLY rules.rules
    ADD CONSTRAINT fkqkhxbrmhfk0ou8sfgrq6w04yn FOREIGN KEY (originalsourcesystemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 5103 (class 2606 OID 33730)
-- Name: rulesxrules fkqmjt8nooh7ch2vr14euld3bnk; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE ONLY rules.rulesxrules
    ADD CONSTRAINT fkqmjt8nooh7ch2vr14euld3bnk FOREIGN KEY (enterpriseid) REFERENCES dbo.enterprise(enterpriseid);


--
-- TOC entry 5026 (class 2606 OID 33345)
-- Name: rulestypexresourceitem fkqygy5t9dhj5r3nefikdhaxdb6; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE ONLY rules.rulestypexresourceitem
    ADD CONSTRAINT fkqygy5t9dhj5r3nefikdhaxdb6 FOREIGN KEY (activeflagid) REFERENCES dbo.activeflag(activeflagid);


--
-- TOC entry 5092 (class 2606 OID 33675)
-- Name: rulesxresourceitem fkr1sg6ky0tcfxt3faiqbpvq72o; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE ONLY rules.rulesxresourceitem
    ADD CONSTRAINT fkr1sg6ky0tcfxt3faiqbpvq72o FOREIGN KEY (originalsourcesystemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 5081 (class 2606 OID 33620)
-- Name: rulesxproduct fkr9hc797wqs7k8u8iun1psqhi0; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE ONLY rules.rulesxproduct
    ADD CONSTRAINT fkr9hc797wqs7k8u8iun1psqhi0 FOREIGN KEY (productid) REFERENCES product.product(productid);


--
-- TOC entry 5038 (class 2606 OID 33390)
-- Name: rulestypexresourceitemsecuritytoken fkrbpyq0298hcisaevbl3mp0m5d; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE ONLY rules.rulestypexresourceitemsecuritytoken
    ADD CONSTRAINT fkrbpyq0298hcisaevbl3mp0m5d FOREIGN KEY (originalsourcesystemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 5031 (class 2606 OID 33370)
-- Name: rulestypexresourceitem fkrdh9f21yu45alpi37mrexx5sy; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE ONLY rules.rulestypexresourceitem
    ADD CONSTRAINT fkrdh9f21yu45alpi37mrexx5sy FOREIGN KEY (resourceitemid) REFERENCES resource.resourceitem(resourceitemid);


--
-- TOC entry 5044 (class 2606 OID 33430)
-- Name: rulesxarrangement fkrkfqm429ef4j7jwneu7jxeye4; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE ONLY rules.rulesxarrangement
    ADD CONSTRAINT fkrkfqm429ef4j7jwneu7jxeye4 FOREIGN KEY (classificationid) REFERENCES classification.classification(classificationid);


--
-- TOC entry 5104 (class 2606 OID 33735)
-- Name: rulesxrules fkrncrmhaj7pk6audwswshmudqj; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE ONLY rules.rulesxrules
    ADD CONSTRAINT fkrncrmhaj7pk6audwswshmudqj FOREIGN KEY (systemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 5121 (class 2606 OID 33820)
-- Name: rulesxrulestype fkrv7w522dypyy838vgcr1r566e; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE ONLY rules.rulesxrulestype
    ADD CONSTRAINT fkrv7w522dypyy838vgcr1r566e FOREIGN KEY (rulestypeid) REFERENCES rules.rulestype(rulestypeid);


--
-- TOC entry 5079 (class 2606 OID 33610)
-- Name: rulesxproduct fkrwm05r7gimvp4itjeem7u6sk6; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE ONLY rules.rulesxproduct
    ADD CONSTRAINT fkrwm05r7gimvp4itjeem7u6sk6 FOREIGN KEY (originalsourcesystemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 5114 (class 2606 OID 33770)
-- Name: rulesxrulessecuritytoken fks16mt02lac7ao524fhu5liiys; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE ONLY rules.rulesxrulessecuritytoken
    ADD CONSTRAINT fks16mt02lac7ao524fhu5liiys FOREIGN KEY (originalsourcesystemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 5049 (class 2606 OID 33465)
-- Name: rulesxarrangementssecuritytoken fks5w3o4wg63awg0iuc76km8e1n; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE ONLY rules.rulesxarrangementssecuritytoken
    ADD CONSTRAINT fks5w3o4wg63awg0iuc76km8e1n FOREIGN KEY (rulesxarrangementsid) REFERENCES rules.rulesxarrangement(rulesxarrangementsid);


--
-- TOC entry 5013 (class 2606 OID 33260)
-- Name: rulestypessecuritytoken fks5xpq0ecpqefbfuwqkymvncsn; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE ONLY rules.rulestypessecuritytoken
    ADD CONSTRAINT fks5xpq0ecpqefbfuwqkymvncsn FOREIGN KEY (enterpriseid) REFERENCES dbo.enterprise(enterpriseid);


--
-- TOC entry 5029 (class 2606 OID 33360)
-- Name: rulestypexresourceitem fksbm8nex7yud5ymui9t5m2wuiw; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE ONLY rules.rulestypexresourceitem
    ADD CONSTRAINT fksbm8nex7yud5ymui9t5m2wuiw FOREIGN KEY (originalsourcesystemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 5100 (class 2606 OID 33715)
-- Name: rulesxresourceitemsecuritytoken fksm3jfs67p3ly7mjl1jkrualak; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE ONLY rules.rulesxresourceitemsecuritytoken
    ADD CONSTRAINT fksm3jfs67p3ly7mjl1jkrualak FOREIGN KEY (systemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 5063 (class 2606 OID 33530)
-- Name: rulesxinvolvedparty fksqp0envk694667mbnl8hndily; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE ONLY rules.rulesxinvolvedparty
    ADD CONSTRAINT fksqp0envk694667mbnl8hndily FOREIGN KEY (activeflagid) REFERENCES dbo.activeflag(activeflagid);


--
-- TOC entry 5105 (class 2606 OID 33740)
-- Name: rulesxrules fkt5ep7dex0ykbws23o3yk8sctu; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE ONLY rules.rulesxrules
    ADD CONSTRAINT fkt5ep7dex0ykbws23o3yk8sctu FOREIGN KEY (originalsourcesystemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 5050 (class 2606 OID 33460)
-- Name: rulesxarrangementssecuritytoken fkte09hu3a7vurplykt3w97714k; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE ONLY rules.rulesxarrangementssecuritytoken
    ADD CONSTRAINT fkte09hu3a7vurplykt3w97714k FOREIGN KEY (systemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 5101 (class 2606 OID 33700)
-- Name: rulesxresourceitemsecuritytoken fktlm1dpqcyom9yo1lefe0yqowj; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE ONLY rules.rulesxresourceitemsecuritytoken
    ADD CONSTRAINT fktlm1dpqcyom9yo1lefe0yqowj FOREIGN KEY (enterpriseid) REFERENCES dbo.enterprise(enterpriseid);


--
-- TOC entry 5091 (class 2606 OID 33670)
-- Name: rulesxresourceitem fktlnk6dgimet9xmad6ajypb5xh; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE ONLY rules.rulesxresourceitem
    ADD CONSTRAINT fktlnk6dgimet9xmad6ajypb5xh FOREIGN KEY (systemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 5075 (class 2606 OID 33565)
-- Name: rulesxinvolvedpartysecuritytoken fkunriof269jly3lkmqk8jsp4g; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE ONLY rules.rulesxinvolvedpartysecuritytoken
    ADD CONSTRAINT fkunriof269jly3lkmqk8jsp4g FOREIGN KEY (activeflagid) REFERENCES dbo.activeflag(activeflagid);


--
-- TOC entry 5094 (class 2606 OID 33685)
-- Name: rulesxresourceitem fkvs83cy05gattyxwtir1yyv2s; Type: FK CONSTRAINT; Schema: rules; Owner: postgres
--

ALTER TABLE ONLY rules.rulesxresourceitem
    ADD CONSTRAINT fkvs83cy05gattyxwtir1yyv2s FOREIGN KEY (resourceitemid) REFERENCES resource.resourceitem(resourceitemid);


--
-- TOC entry 5151 (class 2606 OID 33990)
-- Name: securitytokenxclassificationsecuritytoken fk23j9r000kvoovvj3lpurf7lro; Type: FK CONSTRAINT; Schema: security; Owner: postgres
--

ALTER TABLE ONLY security.securitytokenxclassificationsecuritytoken
    ADD CONSTRAINT fk23j9r000kvoovvj3lpurf7lro FOREIGN KEY (systemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 5128 (class 2606 OID 33870)
-- Name: securitytoken fk3dojhs6jjsp6g4xtwg1gfsg7i; Type: FK CONSTRAINT; Schema: security; Owner: postgres
--

ALTER TABLE ONLY security.securitytoken
    ADD CONSTRAINT fk3dojhs6jjsp6g4xtwg1gfsg7i FOREIGN KEY (originalsourcesystemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 5152 (class 2606 OID 33975)
-- Name: securitytokenxclassificationsecuritytoken fk7b8h1egk35meq3khexlt58p04; Type: FK CONSTRAINT; Schema: security; Owner: postgres
--

ALTER TABLE ONLY security.securitytokenxclassificationsecuritytoken
    ADD CONSTRAINT fk7b8h1egk35meq3khexlt58p04 FOREIGN KEY (enterpriseid) REFERENCES dbo.enterprise(enterpriseid);


--
-- TOC entry 5133 (class 2606 OID 33885)
-- Name: securitytokenssecuritytoken fk7d5c4foji5ocaal45ulv0uaxl; Type: FK CONSTRAINT; Schema: security; Owner: postgres
--

ALTER TABLE ONLY security.securitytokenssecuritytoken
    ADD CONSTRAINT fk7d5c4foji5ocaal45ulv0uaxl FOREIGN KEY (enterpriseid) REFERENCES dbo.enterprise(enterpriseid);


--
-- TOC entry 5129 (class 2606 OID 33865)
-- Name: securitytoken fk7oknl73tuuwwt63nkgdcm4abq; Type: FK CONSTRAINT; Schema: security; Owner: postgres
--

ALTER TABLE ONLY security.securitytoken
    ADD CONSTRAINT fk7oknl73tuuwwt63nkgdcm4abq FOREIGN KEY (systemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 5139 (class 2606 OID 33910)
-- Name: securitytokensxsecuritytokensecuritytoken fk7thwp5xsmuj0tlkpvobud9um7; Type: FK CONSTRAINT; Schema: security; Owner: postgres
--

ALTER TABLE ONLY security.securitytokensxsecuritytokensecuritytoken
    ADD CONSTRAINT fk7thwp5xsmuj0tlkpvobud9um7 FOREIGN KEY (activeflagid) REFERENCES dbo.activeflag(activeflagid);


--
-- TOC entry 5140 (class 2606 OID 33915)
-- Name: securitytokensxsecuritytokensecuritytoken fk8ts5kdhf2u71ss5se0eydoyx5; Type: FK CONSTRAINT; Schema: security; Owner: postgres
--

ALTER TABLE ONLY security.securitytokensxsecuritytokensecuritytoken
    ADD CONSTRAINT fk8ts5kdhf2u71ss5se0eydoyx5 FOREIGN KEY (enterpriseid) REFERENCES dbo.enterprise(enterpriseid);


--
-- TOC entry 5158 (class 2606 OID 34005)
-- Name: securitytokenxsecuritytoken fkaa10y63not65gkhd88hn83qke; Type: FK CONSTRAINT; Schema: security; Owner: postgres
--

ALTER TABLE ONLY security.securitytokenxsecuritytoken
    ADD CONSTRAINT fkaa10y63not65gkhd88hn83qke FOREIGN KEY (enterpriseid) REFERENCES dbo.enterprise(enterpriseid);


--
-- TOC entry 5141 (class 2606 OID 33925)
-- Name: securitytokensxsecuritytokensecuritytoken fkamoh9xumd90l12fw52p4t2lr1; Type: FK CONSTRAINT; Schema: security; Owner: postgres
--

ALTER TABLE ONLY security.securitytokensxsecuritytokensecuritytoken
    ADD CONSTRAINT fkamoh9xumd90l12fw52p4t2lr1 FOREIGN KEY (securitytokenid) REFERENCES security.securitytoken(securitytokenid);


--
-- TOC entry 5145 (class 2606 OID 33940)
-- Name: securitytokenxclassification fkbeh76kxkmk54rmc3f6m4wsjaf; Type: FK CONSTRAINT; Schema: security; Owner: postgres
--

ALTER TABLE ONLY security.securitytokenxclassification
    ADD CONSTRAINT fkbeh76kxkmk54rmc3f6m4wsjaf FOREIGN KEY (activeflagid) REFERENCES dbo.activeflag(activeflagid);


--
-- TOC entry 5162 (class 2606 OID 34025)
-- Name: securitytokenxsecuritytoken fkbi1t0uojqlur84bdht26o1b9a; Type: FK CONSTRAINT; Schema: security; Owner: postgres
--

ALTER TABLE ONLY security.securitytokenxsecuritytoken
    ADD CONSTRAINT fkbi1t0uojqlur84bdht26o1b9a FOREIGN KEY (childsecuritytokenid) REFERENCES security.securitytoken(securitytokenid);


--
-- TOC entry 5134 (class 2606 OID 33880)
-- Name: securitytokenssecuritytoken fkbohmlwb2yuw6vjbohxmmuhu2u; Type: FK CONSTRAINT; Schema: security; Owner: postgres
--

ALTER TABLE ONLY security.securitytokenssecuritytoken
    ADD CONSTRAINT fkbohmlwb2yuw6vjbohxmmuhu2u FOREIGN KEY (activeflagid) REFERENCES dbo.activeflag(activeflagid);


--
-- TOC entry 5153 (class 2606 OID 33985)
-- Name: securitytokenxclassificationsecuritytoken fkctvl0v696371fygrxpr48xfh6; Type: FK CONSTRAINT; Schema: security; Owner: postgres
--

ALTER TABLE ONLY security.securitytokenxclassificationsecuritytoken
    ADD CONSTRAINT fkctvl0v696371fygrxpr48xfh6 FOREIGN KEY (securitytokenid) REFERENCES security.securitytoken(securitytokenid);


--
-- TOC entry 5130 (class 2606 OID 33855)
-- Name: securitytoken fkdxgncdf26cjimvl7qyfqcpv5l; Type: FK CONSTRAINT; Schema: security; Owner: postgres
--

ALTER TABLE ONLY security.securitytoken
    ADD CONSTRAINT fkdxgncdf26cjimvl7qyfqcpv5l FOREIGN KEY (activeflagid) REFERENCES dbo.activeflag(activeflagid);


--
-- TOC entry 5154 (class 2606 OID 33970)
-- Name: securitytokenxclassificationsecuritytoken fke3gxxm2i6buv2rvlr17em38aj; Type: FK CONSTRAINT; Schema: security; Owner: postgres
--

ALTER TABLE ONLY security.securitytokenxclassificationsecuritytoken
    ADD CONSTRAINT fke3gxxm2i6buv2rvlr17em38aj FOREIGN KEY (activeflagid) REFERENCES dbo.activeflag(activeflagid);


--
-- TOC entry 5146 (class 2606 OID 33960)
-- Name: securitytokenxclassification fkf0ve8e1o43mpjqbkv0eor80ks; Type: FK CONSTRAINT; Schema: security; Owner: postgres
--

ALTER TABLE ONLY security.securitytokenxclassification
    ADD CONSTRAINT fkf0ve8e1o43mpjqbkv0eor80ks FOREIGN KEY (classificationid) REFERENCES classification.classification(classificationid);


--
-- TOC entry 5161 (class 2606 OID 34020)
-- Name: securitytokenxsecuritytoken fkfcmwpt8n1j6p4l9y51o4s4b99; Type: FK CONSTRAINT; Schema: security; Owner: postgres
--

ALTER TABLE ONLY security.securitytokenxsecuritytoken
    ADD CONSTRAINT fkfcmwpt8n1j6p4l9y51o4s4b99 FOREIGN KEY (classificationid) REFERENCES classification.classification(classificationid);


--
-- TOC entry 5131 (class 2606 OID 33875)
-- Name: securitytoken fkg5dnn3ygqgg2j80h4ijca6kdp; Type: FK CONSTRAINT; Schema: security; Owner: postgres
--

ALTER TABLE ONLY security.securitytoken
    ADD CONSTRAINT fkg5dnn3ygqgg2j80h4ijca6kdp FOREIGN KEY (securitytokenclassificationid) REFERENCES classification.classification(classificationid);


--
-- TOC entry 5135 (class 2606 OID 33900)
-- Name: securitytokenssecuritytoken fkgm9ke6kkuxlw3dm67iseaofu8; Type: FK CONSTRAINT; Schema: security; Owner: postgres
--

ALTER TABLE ONLY security.securitytokenssecuritytoken
    ADD CONSTRAINT fkgm9ke6kkuxlw3dm67iseaofu8 FOREIGN KEY (systemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 5163 (class 2606 OID 34030)
-- Name: securitytokenxsecuritytoken fkh8wabwfov1k3ah9r2jmm11fg8; Type: FK CONSTRAINT; Schema: security; Owner: postgres
--

ALTER TABLE ONLY security.securitytokenxsecuritytoken
    ADD CONSTRAINT fkh8wabwfov1k3ah9r2jmm11fg8 FOREIGN KEY (parentsecuritytokenid) REFERENCES security.securitytoken(securitytokenid);


--
-- TOC entry 5157 (class 2606 OID 34000)
-- Name: securitytokenxsecuritytoken fkix4kxmf6f9elii179h4fv9opk; Type: FK CONSTRAINT; Schema: security; Owner: postgres
--

ALTER TABLE ONLY security.securitytokenxsecuritytoken
    ADD CONSTRAINT fkix4kxmf6f9elii179h4fv9opk FOREIGN KEY (activeflagid) REFERENCES dbo.activeflag(activeflagid);


--
-- TOC entry 5147 (class 2606 OID 33945)
-- Name: securitytokenxclassification fkiy9192vtwu1t1tucigro1dhbr; Type: FK CONSTRAINT; Schema: security; Owner: postgres
--

ALTER TABLE ONLY security.securitytokenxclassification
    ADD CONSTRAINT fkiy9192vtwu1t1tucigro1dhbr FOREIGN KEY (enterpriseid) REFERENCES dbo.enterprise(enterpriseid);


--
-- TOC entry 5160 (class 2606 OID 34015)
-- Name: securitytokenxsecuritytoken fkkhrctfxn8jbkmugeuq13or9y2; Type: FK CONSTRAINT; Schema: security; Owner: postgres
--

ALTER TABLE ONLY security.securitytokenxsecuritytoken
    ADD CONSTRAINT fkkhrctfxn8jbkmugeuq13or9y2 FOREIGN KEY (originalsourcesystemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 5148 (class 2606 OID 33950)
-- Name: securitytokenxclassification fkkpec2fqi1saqvfqyim4qibvhc; Type: FK CONSTRAINT; Schema: security; Owner: postgres
--

ALTER TABLE ONLY security.securitytokenxclassification
    ADD CONSTRAINT fkkpec2fqi1saqvfqyim4qibvhc FOREIGN KEY (systemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 5159 (class 2606 OID 34010)
-- Name: securitytokenxsecuritytoken fklyogqrmsivjjwwmqyhs3w8eey; Type: FK CONSTRAINT; Schema: security; Owner: postgres
--

ALTER TABLE ONLY security.securitytokenxsecuritytoken
    ADD CONSTRAINT fklyogqrmsivjjwwmqyhs3w8eey FOREIGN KEY (systemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 5155 (class 2606 OID 33980)
-- Name: securitytokenxclassificationsecuritytoken fkmvagw1psni1acp68r86p98326; Type: FK CONSTRAINT; Schema: security; Owner: postgres
--

ALTER TABLE ONLY security.securitytokenxclassificationsecuritytoken
    ADD CONSTRAINT fkmvagw1psni1acp68r86p98326 FOREIGN KEY (originalsourcesystemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 5136 (class 2606 OID 33890)
-- Name: securitytokenssecuritytoken fkn5jt48ip0t79bbac760wnpk4u; Type: FK CONSTRAINT; Schema: security; Owner: postgres
--

ALTER TABLE ONLY security.securitytokenssecuritytoken
    ADD CONSTRAINT fkn5jt48ip0t79bbac760wnpk4u FOREIGN KEY (originalsourcesystemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 5142 (class 2606 OID 33920)
-- Name: securitytokensxsecuritytokensecuritytoken fkr9dd79mvxphy35j0l30f60wjo; Type: FK CONSTRAINT; Schema: security; Owner: postgres
--

ALTER TABLE ONLY security.securitytokensxsecuritytokensecuritytoken
    ADD CONSTRAINT fkr9dd79mvxphy35j0l30f60wjo FOREIGN KEY (originalsourcesystemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 5143 (class 2606 OID 33930)
-- Name: securitytokensxsecuritytokensecuritytoken fkraqvstjiad8yqfg3be6ae1e1r; Type: FK CONSTRAINT; Schema: security; Owner: postgres
--

ALTER TABLE ONLY security.securitytokensxsecuritytokensecuritytoken
    ADD CONSTRAINT fkraqvstjiad8yqfg3be6ae1e1r FOREIGN KEY (systemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 5149 (class 2606 OID 33955)
-- Name: securitytokenxclassification fkrct8kx4cxa9ys52h7kp02yda0; Type: FK CONSTRAINT; Schema: security; Owner: postgres
--

ALTER TABLE ONLY security.securitytokenxclassification
    ADD CONSTRAINT fkrct8kx4cxa9ys52h7kp02yda0 FOREIGN KEY (originalsourcesystemid) REFERENCES dbo.systems(systemid);


--
-- TOC entry 5156 (class 2606 OID 33995)
-- Name: securitytokenxclassificationsecuritytoken fkrde1i5gxqtmm8pxu5g9lwepd2; Type: FK CONSTRAINT; Schema: security; Owner: postgres
--

ALTER TABLE ONLY security.securitytokenxclassificationsecuritytoken
    ADD CONSTRAINT fkrde1i5gxqtmm8pxu5g9lwepd2 FOREIGN KEY (securitytokenxclassificationid) REFERENCES security.securitytokenxclassification(securitytokenxclassificationid);


--
-- TOC entry 5144 (class 2606 OID 33935)
-- Name: securitytokensxsecuritytokensecuritytoken fks48ohap0caelpi8nu60sp55i6; Type: FK CONSTRAINT; Schema: security; Owner: postgres
--

ALTER TABLE ONLY security.securitytokensxsecuritytokensecuritytoken
    ADD CONSTRAINT fks48ohap0caelpi8nu60sp55i6 FOREIGN KEY (securitytokenxsecuritytokenid) REFERENCES security.securitytokenxsecuritytoken(securitytokenxsecuritytokenid);


--
-- TOC entry 5137 (class 2606 OID 33895)
-- Name: securitytokenssecuritytoken fksoxo8an11ilt212h41x4c9ofj; Type: FK CONSTRAINT; Schema: security; Owner: postgres
--

ALTER TABLE ONLY security.securitytokenssecuritytoken
    ADD CONSTRAINT fksoxo8an11ilt212h41x4c9ofj FOREIGN KEY (securitytokenid) REFERENCES security.securitytoken(securitytokenid);


--
-- TOC entry 5132 (class 2606 OID 33860)
-- Name: securitytoken fksqp2hjm3vhxqy62do1twwtrmf; Type: FK CONSTRAINT; Schema: security; Owner: postgres
--

ALTER TABLE ONLY security.securitytoken
    ADD CONSTRAINT fksqp2hjm3vhxqy62do1twwtrmf FOREIGN KEY (enterpriseid) REFERENCES dbo.enterprise(enterpriseid);


--
-- TOC entry 5150 (class 2606 OID 33965)
-- Name: securitytokenxclassification fktis44ugn011fdb281jb5onjsv; Type: FK CONSTRAINT; Schema: security; Owner: postgres
--

ALTER TABLE ONLY security.securitytokenxclassification
    ADD CONSTRAINT fktis44ugn011fdb281jb5onjsv FOREIGN KEY (securitytokenid) REFERENCES security.securitytoken(securitytokenid);


--
-- TOC entry 5138 (class 2606 OID 33905)
-- Name: securitytokenssecuritytoken fkujfypgbk5b3c90rftiwmwv0m; Type: FK CONSTRAINT; Schema: security; Owner: postgres
--

ALTER TABLE ONLY security.securitytokenssecuritytoken
    ADD CONSTRAINT fkujfypgbk5b3c90rftiwmwv0m FOREIGN KEY (securitytokentoid) REFERENCES security.securitytoken(securitytokenid);


--
-- TOC entry 5164 (class 2606 OID 34035)
-- Name: days fk4g0xilxoif8vy1brjn2vpdpcw; Type: FK CONSTRAINT; Schema: time; Owner: postgres
--

ALTER TABLE ONLY "time".days
    ADD CONSTRAINT fk4g0xilxoif8vy1brjn2vpdpcw FOREIGN KEY (daynameid) REFERENCES "time".daynames(daynameid);


--
-- TOC entry 5169 (class 2606 OID 34060)
-- Name: months fk6sf1pjb60myyfca1rj11q2l9r; Type: FK CONSTRAINT; Schema: time; Owner: postgres
--

ALTER TABLE ONLY "time".months
    ADD CONSTRAINT fk6sf1pjb60myyfca1rj11q2l9r FOREIGN KEY (monthofyearid) REFERENCES "time".monthofyear(monthofyearid);


--
-- TOC entry 5167 (class 2606 OID 34050)
-- Name: halfhourdayparts fk8n2nuitlg5wf0pb64jbncrdfa; Type: FK CONSTRAINT; Schema: time; Owner: postgres
--

ALTER TABLE ONLY "time".halfhourdayparts
    ADD CONSTRAINT fk8n2nuitlg5wf0pb64jbncrdfa FOREIGN KEY (daypartid) REFERENCES "time".dayparts(daypartid);


--
-- TOC entry 5170 (class 2606 OID 34065)
-- Name: months fka234h7trk9pm9pqry7hc2dfac; Type: FK CONSTRAINT; Schema: time; Owner: postgres
--

ALTER TABLE ONLY "time".months
    ADD CONSTRAINT fka234h7trk9pm9pqry7hc2dfac FOREIGN KEY (quarterid) REFERENCES "time".quarters(quarterid);


--
-- TOC entry 5165 (class 2606 OID 34045)
-- Name: days fkcnplvut6yx0iu2itr0q11oa6l; Type: FK CONSTRAINT; Schema: time; Owner: postgres
--

ALTER TABLE ONLY "time".days
    ADD CONSTRAINT fkcnplvut6yx0iu2itr0q11oa6l FOREIGN KEY (weekid) REFERENCES "time".weeks(weekid);


--
-- TOC entry 5172 (class 2606 OID 34075)
-- Name: time fkejnxl506r8u5bm8fds1lu09h5; Type: FK CONSTRAINT; Schema: time; Owner: postgres
--

ALTER TABLE ONLY "time"."time"
    ADD CONSTRAINT fkejnxl506r8u5bm8fds1lu09h5 FOREIGN KEY (hourid) REFERENCES "time".hours(hourid);


--
-- TOC entry 5166 (class 2606 OID 34040)
-- Name: days fkgbuiu0f44kxjymtkhyj4vlgc2; Type: FK CONSTRAINT; Schema: time; Owner: postgres
--

ALTER TABLE ONLY "time".days
    ADD CONSTRAINT fkgbuiu0f44kxjymtkhyj4vlgc2 FOREIGN KEY (monthid) REFERENCES "time".months(monthid);


--
-- TOC entry 5171 (class 2606 OID 34070)
-- Name: quarters fkhb7po9kbr3vc89jkxgy4ik2e5; Type: FK CONSTRAINT; Schema: time; Owner: postgres
--

ALTER TABLE ONLY "time".quarters
    ADD CONSTRAINT fkhb7po9kbr3vc89jkxgy4ik2e5 FOREIGN KEY (yearid) REFERENCES "time".years(yearid);


--
-- TOC entry 5168 (class 2606 OID 34055)
-- Name: halfhours fkmsowgve3eajsco5ty4ud5lc2v; Type: FK CONSTRAINT; Schema: time; Owner: postgres
--

ALTER TABLE ONLY "time".halfhours
    ADD CONSTRAINT fkmsowgve3eajsco5ty4ud5lc2v FOREIGN KEY (hourid) REFERENCES "time".hours(hourid);


-- Completed on 2022-05-21 06:01:54

--
-- PostgreSQL database dump complete
--
CREATE EXTENSION IF NOT EXISTS tablefunc;
