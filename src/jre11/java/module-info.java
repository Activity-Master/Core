import com.guicedee.activitymaster.core.db.ActivityMasterDBModule;
import com.guicedee.activitymaster.core.injections.ActivityMasterBinder;
import com.guicedee.activitymaster.core.injections.ActivityMasterModuleInclusion;
import com.guicedee.activitymaster.core.injections.HazelcastClientConfig;
import com.guicedee.activitymaster.core.injections.HazelcastServerConfig;
import com.guicedee.activitymaster.core.services.IActivityMasterSystem;
import com.guicedee.activitymaster.core.systems.*;
import com.guicedee.guicedhazelcast.services.IGuicedHazelcastServerConfig;
import com.guicedee.guicedinjection.interfaces.IGuiceModule;

module com.guicedee.activitymaster.core {
	exports com.guicedee.activitymaster.core;
	
	exports com.guicedee.activitymaster.core.async;
	exports com.guicedee.activitymaster.core.services;
	exports com.guicedee.activitymaster.core.services.dto;
	exports com.guicedee.activitymaster.core.services.enumtypes;
	exports com.guicedee.activitymaster.core.services.capabilities;
	exports com.guicedee.activitymaster.core.services.classifications.enterprise;
	
	exports com.guicedee.activitymaster.core.services.classifications.address;
	exports com.guicedee.activitymaster.core.services.classifications.arrangement;
	exports com.guicedee.activitymaster.core.services.classifications.events;
	exports com.guicedee.activitymaster.core.services.concepts;
	exports com.guicedee.activitymaster.core.services.exceptions;
	exports com.guicedee.activitymaster.core.services.security;
	exports com.guicedee.activitymaster.core.services.system;
	exports com.guicedee.activitymaster.core.services.types;
	
	exports com.guicedee.activitymaster.core.services.classifications.activeflag;
	exports com.guicedee.activitymaster.core.services.classifications.classification;
	exports com.guicedee.activitymaster.core.services.classifications.classificationdataconcepts;
	exports com.guicedee.activitymaster.core.services.classifications.geography;
	exports com.guicedee.activitymaster.core.services.classifications.involvedparty;
	exports com.guicedee.activitymaster.core.services.classifications.product;
	exports com.guicedee.activitymaster.core.services.classifications.resourceitems;
	exports com.guicedee.activitymaster.core.services.classifications.securitytokens;
	exports com.guicedee.activitymaster.core.services.classifications.systems;
	exports com.guicedee.activitymaster.core.services.classifications.rules;
	
	//exports com.guicedee.activitymaster.core.systems;
	
	exports com.guicedee.activitymaster.core.threads;
	
	exports com.guicedee.activitymaster.core.updates;
	
	requires io.github.classgraph;
	requires com.fasterxml.jackson.databind;
	requires com.fasterxml.jackson.annotation;
	
	requires com.guicedee.guicedinjection;
	requires com.guicedee.guicedpersistence;
	
	requires com.entityassist;
	
	requires com.google.common;
	
	requires java.sql;
	requires com.microsoft.sqlserver.jdbc;
	
	requires jakarta.persistence;
	
	requires jakarta.xml.bind;
	
	requires jakarta.activation;
	requires jakarta.validation;
	
	requires com.google.guice.extensions.servlet;
	requires cache.annotations.ri.guice;
	
	requires java.naming;
	
	requires java.logging;
	
	requires com.google.guice;
	
	requires org.hibernate.orm.core;
	requires org.hibernate.validator;
	
	requires com.guicedee.guicedhazelcast.hibernate;
	requires com.guicedee.guicedhazelcast;
	
	requires cache.api;
	
	requires static lombok;
	
	provides IGuiceModule with ActivityMasterDBModule;
	
	provides IActivityMasterSystem with EnterpriseSystem, ActiveFlagSystem, SystemsSystem, ClassificationsDataConceptSystem,
			ClassificationsSystem, AddressSystem, InvolvedPartySystem, SecurityTokenSystem
			, ArrangementsSystem, EventsSystem, ResourceItemSystem, ProductsSystem;
	 
	provides com.guicedee.guicedinjection.interfaces.IGuiceDefaultBinder with ActivityMasterBinder;
	provides IGuicedHazelcastServerConfig with HazelcastServerConfig;
	provides com.guicedee.guicedhazelcast.services.IGuicedHazelcastClientConfig with HazelcastClientConfig;

	provides com.guicedee.guicedinjection.interfaces.IGuiceScanModuleInclusions with ActivityMasterModuleInclusion;
	
	uses IActivityMasterSystem;
	
	opens com.guicedee.activitymaster.core to com.google.guice, org.hibernate.orm.core, com.entityassist, com.fasterxml.jackson.databind;
	opens com.guicedee.activitymaster.core.async to com.google.guice, org.hibernate.orm.core, com.entityassist, com.fasterxml.jackson.databind;
	opens com.guicedee.activitymaster.core.implementations to com.google.guice, org.hibernate.orm.core, com.entityassist, com.fasterxml.jackson.databind, com.guicedee.activitymaster.geography;
	opens com.guicedee.activitymaster.core.db to com.google.guice, org.hibernate.orm.core, com.entityassist, com.guicedee.guicedinjection, com.fasterxml.jackson.databind;
	opens com.guicedee.activitymaster.core.db.abstraction to com.google.guice, org.hibernate.orm.core, com.entityassist, com.guicedee.guicedinjection, com.fasterxml.jackson.databind;
	opens com.guicedee.activitymaster.core.db.abstraction.assists to com.google.guice, org.hibernate.orm.core, com.entityassist, com.guicedee.guicedinjection, com.fasterxml.jackson.databind;
	opens com.guicedee.activitymaster.core.db.abstraction.builders to com.google.guice, org.hibernate.orm.core, com.entityassist, com.guicedee.guicedinjection, com.fasterxml.jackson.databind;
	opens com.guicedee.activitymaster.core.db.abstraction.builders.assists to com.google.guice, org.hibernate.orm.core, com.entityassist, com.guicedee.guicedinjection, com.fasterxml.jackson.databind;
	opens com.guicedee.activitymaster.core.db.entities.enterprise to com.google.guice, org.hibernate.orm.core, com.entityassist, com.guicedee.guicedinjection, com.fasterxml.jackson.databind, com.guicedee.activitymaster.geography;
	opens com.guicedee.activitymaster.core.db.entities.enterprise.builders to com.google.guice, org.hibernate.orm.core, com.entityassist, com.guicedee.guicedinjection, com.fasterxml.jackson.databind, com.guicedee.activitymaster.geography;
	opens com.guicedee.activitymaster.core.db.entities.activeflag to com.google.guice, org.hibernate.orm.core, com.entityassist, com.guicedee.guicedinjection, com.fasterxml.jackson.databind;
	opens com.guicedee.activitymaster.core.db.entities.activeflag.builders to com.google.guice, org.hibernate.orm.core, com.entityassist, com.guicedee.guicedinjection, com.fasterxml.jackson.databind;
	opens com.guicedee.activitymaster.core.db.entities.address to com.google.guice, org.hibernate.orm.core, com.entityassist, com.guicedee.guicedinjection, com.fasterxml.jackson.databind;
	opens com.guicedee.activitymaster.core.db.entities.address.builders to com.google.guice, org.hibernate.orm.core, com.entityassist, com.guicedee.guicedinjection, com.fasterxml.jackson.databind;
	opens com.guicedee.activitymaster.core.db.entities.arrangement to com.google.guice, org.hibernate.orm.core, com.entityassist, com.guicedee.guicedinjection, com.fasterxml.jackson.databind;
	opens com.guicedee.activitymaster.core.db.entities.arrangement.builders to com.google.guice, org.hibernate.orm.core, com.entityassist, com.guicedee.guicedinjection, com.fasterxml.jackson.databind;
	opens com.guicedee.activitymaster.core.db.entities.classifications to com.google.guice, org.hibernate.orm.core, com.entityassist, com.guicedee.guicedinjection, com.fasterxml.jackson.databind, com.guicedee.activitymaster.geography;
	opens com.guicedee.activitymaster.core.db.entities.classifications.builders to com.google.guice, org.hibernate.orm.core, com.entityassist, com.guicedee.guicedinjection, com.fasterxml.jackson.databind, com.guicedee.activitymaster.geography;
	opens com.guicedee.activitymaster.core.db.entities.events to com.google.guice, org.hibernate.orm.core, com.entityassist, com.guicedee.guicedinjection, com.fasterxml.jackson.databind;
	opens com.guicedee.activitymaster.core.db.entities.events.builders to com.google.guice, org.hibernate.orm.core, com.entityassist, com.guicedee.guicedinjection, com.fasterxml.jackson.databind;
	opens com.guicedee.activitymaster.core.db.entities.geography to com.google.guice, org.hibernate.orm.core, com.entityassist, com.guicedee.guicedinjection, com.fasterxml.jackson.databind, com.guicedee.activitymaster.geography;
	opens com.guicedee.activitymaster.core.db.entities.geography.builders to com.google.guice, org.hibernate.orm.core, com.entityassist, com.guicedee.guicedinjection, com.fasterxml.jackson.databind, com.guicedee.activitymaster.geography;
	opens com.guicedee.activitymaster.core.db.entities.involvedparty to com.google.guice, org.hibernate.orm.core, com.entityassist, com.guicedee.guicedinjection, com.fasterxml.jackson.databind;
	opens com.guicedee.activitymaster.core.db.entities.involvedparty.builders to com.google.guice, org.hibernate.orm.core, com.entityassist, com.guicedee.guicedinjection, com.fasterxml.jackson.databind;
	opens com.guicedee.activitymaster.core.db.entities.product to com.google.guice, org.hibernate.orm.core, com.entityassist, com.guicedee.guicedinjection, com.fasterxml.jackson.databind;
	opens com.guicedee.activitymaster.core.db.entities.product.builders to com.google.guice, org.hibernate.orm.core, com.entityassist, com.guicedee.guicedinjection, com.fasterxml.jackson.databind;
	opens com.guicedee.activitymaster.core.db.entities.rules to com.google.guice, org.hibernate.orm.core, com.entityassist, com.guicedee.guicedinjection, com.fasterxml.jackson.databind;
	opens com.guicedee.activitymaster.core.db.entities.rules.builders to com.google.guice, org.hibernate.orm.core, com.entityassist, com.guicedee.guicedinjection, com.fasterxml.jackson.databind;
	opens com.guicedee.activitymaster.core.db.entities.resourceitem to com.google.guice, org.hibernate.orm.core, com.entityassist, com.guicedee.guicedinjection, com.fasterxml.jackson.databind;
	opens com.guicedee.activitymaster.core.db.entities.resourceitem.builders to com.google.guice, org.hibernate.orm.core, com.entityassist, com.guicedee.guicedinjection, com.fasterxml.jackson.databind;
	opens com.guicedee.activitymaster.core.db.entities.security to com.google.guice, org.hibernate.orm.core, com.entityassist, com.guicedee.guicedinjection, com.fasterxml.jackson.databind;
	opens com.guicedee.activitymaster.core.db.entities.security.builders to com.google.guice, org.hibernate.orm.core, com.entityassist, com.guicedee.guicedinjection, com.fasterxml.jackson.databind;
	opens com.guicedee.activitymaster.core.db.entities.systems to com.google.guice, org.hibernate.orm.core, com.entityassist, com.guicedee.guicedinjection, com.fasterxml.jackson.databind, com.guicedee.activitymaster.geography;
	opens com.guicedee.activitymaster.core.db.entities.systems.builders to com.google.guice, org.hibernate.orm.core, com.entityassist, com.guicedee.guicedinjection, com.fasterxml.jackson.databind, com.guicedee.activitymaster.geography;
	opens com.guicedee.activitymaster.core.db.entities.time to com.google.guice, org.hibernate.orm.core, com.entityassist, com.guicedee.guicedinjection, com.fasterxml.jackson.databind;
	opens com.guicedee.activitymaster.core.db.entities.time.builders to com.google.guice, org.hibernate.orm.core, com.entityassist, com.guicedee.guicedinjection, com.fasterxml.jackson.databind;
	
	opens com.guicedee.activitymaster.core.db.hierarchies to com.google.guice, org.hibernate.orm.core, com.entityassist, com.guicedee.guicedinjection, com.fasterxml.jackson.databind;
	opens com.guicedee.activitymaster.core.db.hierarchies.builders to com.google.guice, org.hibernate.orm.core, com.entityassist, com.guicedee.guicedinjection, com.fasterxml.jackson.databind;
	opens com.guicedee.activitymaster.core.injections to com.google.guice, org.hibernate.orm.core, com.entityassist, com.fasterxml.jackson.databind;
	opens com.guicedee.activitymaster.core.services to com.google.guice, org.hibernate.orm.core, com.entityassist, com.fasterxml.jackson.databind;
	opens com.guicedee.activitymaster.core.services.capabilities to com.google.guice, org.hibernate.orm.core, com.entityassist, com.fasterxml.jackson.databind;
	//opens com.guicedee.activitymaster.core.services.classifications to com.google.guice, org.hibernate.orm.core,com.entityassist,com.fasterxml.jackson.databind;
	opens com.guicedee.activitymaster.core.services.classifications.activeflag to com.google.guice, org.hibernate.orm.core, com.entityassist, com.fasterxml.jackson.databind;
	opens com.guicedee.activitymaster.core.services.classifications.address to com.google.guice, org.hibernate.orm.core, com.entityassist, com.fasterxml.jackson.databind;
	opens com.guicedee.activitymaster.core.services.classifications.arrangement to com.google.guice, org.hibernate.orm.core, com.entityassist, com.fasterxml.jackson.databind;
	opens com.guicedee.activitymaster.core.services.classifications.classification to com.google.guice, org.hibernate.orm.core, com.entityassist, com.fasterxml.jackson.databind;
	opens com.guicedee.activitymaster.core.services.classifications.classificationdataconcepts to com.google.guice, org.hibernate.orm.core, com.entityassist, com.fasterxml.jackson.databind;
	opens com.guicedee.activitymaster.core.services.classifications.enterprise to com.google.guice, org.hibernate.orm.core, com.entityassist, com.fasterxml.jackson.databind;
	opens com.guicedee.activitymaster.core.services.classifications.events to com.google.guice, org.hibernate.orm.core, com.entityassist, com.fasterxml.jackson.databind;
	opens com.guicedee.activitymaster.core.services.classifications.geography to com.google.guice, org.hibernate.orm.core, com.entityassist, com.fasterxml.jackson.databind;
	opens com.guicedee.activitymaster.core.services.classifications.involvedparty to com.google.guice, org.hibernate.orm.core, com.entityassist, com.fasterxml.jackson.databind;
	opens com.guicedee.activitymaster.core.services.classifications.product to com.google.guice, org.hibernate.orm.core, com.entityassist, com.fasterxml.jackson.databind;
	opens com.guicedee.activitymaster.core.services.classifications.resourceitems to com.google.guice, org.hibernate.orm.core, com.entityassist, com.fasterxml.jackson.databind;
	opens com.guicedee.activitymaster.core.services.classifications.securitytokens to com.google.guice, org.hibernate.orm.core, com.entityassist, com.fasterxml.jackson.databind;
	opens com.guicedee.activitymaster.core.services.classifications.systems to com.google.guice, org.hibernate.orm.core, com.entityassist, com.fasterxml.jackson.databind;
	
	opens com.guicedee.activitymaster.core.services.concepts to com.google.guice, org.hibernate.orm.core, com.entityassist, com.fasterxml.jackson.databind;
	opens com.guicedee.activitymaster.core.services.exceptions to com.google.guice, org.hibernate.orm.core, com.entityassist, com.fasterxml.jackson.databind;
	opens com.guicedee.activitymaster.core.services.security to com.google.guice, org.hibernate.orm.core, com.entityassist, com.fasterxml.jackson.databind;
	opens com.guicedee.activitymaster.core.services.system to com.google.guice, org.hibernate.orm.core, com.entityassist, com.fasterxml.jackson.databind;
	opens com.guicedee.activitymaster.core.services.types to com.google.guice, org.hibernate.orm.core, com.entityassist, com.fasterxml.jackson.databind;
	opens com.guicedee.activitymaster.core.systems to com.google.guice, org.hibernate.orm.core, com.entityassist, com.fasterxml.jackson.databind;
	opens com.guicedee.activitymaster.core.services.enumtypes to com.google.guice, org.hibernate.orm.core, com.entityassist, com.fasterxml.jackson.databind;
	
	opens com.guicedee.activitymaster.core.services.threads to com.google.guice, com.entityassist, com.fasterxml.jackson.databind;
	opens com.guicedee.activitymaster.core.threads to com.google.guice, com.entityassist, com.fasterxml.jackson.databind;
	
	exports com.guicedee.activitymaster.core.db.entities.systems to com.guicedee.activitymaster.geography;
	exports com.guicedee.activitymaster.core.db.entities.enterprise to com.guicedee.activitymaster.geography;
	exports com.guicedee.activitymaster.core.db.entities.classifications to com.guicedee.activitymaster.geography;
	exports com.guicedee.activitymaster.core.db.entities.classifications.builders to com.guicedee.activitymaster.geography;
	exports com.guicedee.activitymaster.core.db.entities.geography to com.guicedee.activitymaster.geography;
	exports com.guicedee.activitymaster.core.db.entities.rules to com.guicedee.activitymaster.geography;
	exports com.guicedee.activitymaster.core.db.entities.resourceitem to com.guicedee.activitymaster.geography;
	exports com.guicedee.activitymaster.core.implementations to com.guicedee.activitymaster.geography;
	exports com.guicedee.activitymaster.core.db.entities.geography.builders to com.guicedee.activitymaster.geography;
	exports com.guicedee.activitymaster.core.db;
	//exports com.guicedee.activitymaster.core.db;
	
	
}
