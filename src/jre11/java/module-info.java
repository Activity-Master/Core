import com.guicedee.activitymaster.client.services.events.IOnSystemInstall;
import com.guicedee.activitymaster.client.services.events.IOnSystemUpdate;
import com.guicedee.activitymaster.client.services.systems.IActivityMasterSystem;
import com.guicedee.activitymaster.core.db.ActivityMasterDBModule;
import com.guicedee.activitymaster.core.implementations.*;
import com.guicedee.activitymaster.core.injections.*;
import com.guicedee.activitymaster.core.systems.*;
import com.guicedee.guicedhazelcast.services.IGuicedHazelcastServerConfig;
import com.guicedee.guicedinjection.interfaces.IGuiceConfigurator;
import com.guicedee.guicedinjection.interfaces.IGuiceModule;

module com.guicedee.activitymaster.core {
	exports com.guicedee.activitymaster.core;
	
	exports com.guicedee.activitymaster.core.async;
	exports com.guicedee.activitymaster.core.services;

	exports com.guicedee.activitymaster.core.services.system;
	
	exports com.guicedee.activitymaster.core.db.entities.time;
	
	//exports com.guicedee.activitymaster.core.systems;
	
	exports com.guicedee.activitymaster.core.threads;
	
	exports com.guicedee.activitymaster.core.implementations.interceptors;
	
	requires com.guicedee.guicedinjection;
	requires com.guicedee.guicedpersistence;

	requires com.entityassist;
	
	requires com.google.common;

	requires com.microsoft.sqlserver.jdbc;

	requires jakarta.activation;
	requires jakarta.validation;
	
	requires cache.annotations.ri.guice;
	
	requires java.naming;
	
	requires java.logging;
	
	requires com.google.guice;
	
	requires org.hibernate.validator;
	
	requires com.guicedee.guicedhazelcast.hibernate;
	requires com.guicedee.guicedhazelcast;
	
	
	requires transitive com.guicedee.activitymaster.client;
	
	requires static lombok;
	
	provides IGuiceModule with ActivityMasterDBModule,ActivityMasterBinder,
			EnterpriseBinder,
			ClassificationConceptsBinder,
			ClassificationsBinder,
			SystemsBinder,
			InvolvedPartiesBinder,
			ResourceItemBinder,
			SecurityTokensBinder,
			ActiveFlagBinder,
			EventsBinder,
			AddressBinder,
			ArrangementsBinder,
			ProductsBinder,
			RulesBinder;
	
	provides IGuiceConfigurator with ActivityMasterScanConfiguration;
	
	provides IActivityMasterSystem with EnterpriseSystem, ActiveFlagSystem, SystemsSystem, ClassificationsDataConceptSystem,
			ClassificationsSystem, AddressSystem, InvolvedPartySystem, SecurityTokenSystem
			, ArrangementsSystem, EventsSystem, ResourceItemSystem, ProductsSystem
			;
	
	provides IGuicedHazelcastServerConfig with HazelcastServerConfig;
	provides com.guicedee.guicedhazelcast.services.IGuicedHazelcastClientConfig with HazelcastClientConfig;

	provides com.guicedee.guicedinjection.interfaces.IGuiceScanModuleInclusions with ActivityMasterModuleInclusion;
	
	//uses IActivityMasterSystem;
	
	
	uses IOnSystemUpdate;
	uses IOnSystemInstall;
	
	
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
	opens com.guicedee.activitymaster.core.injections.updates to com.google.guice, org.hibernate.orm.core, com.entityassist, com.fasterxml.jackson.databind;
	
	
	opens com.guicedee.activitymaster.core.services to com.google.guice, org.hibernate.orm.core, com.entityassist, com.fasterxml.jackson.databind;
	//opens com.guicedee.activitymaster.core.services.classifications to com.google.guice, org.hibernate.orm.core,com.entityassist,com.fasterxml.jackson.databind;
	//opens com.guicedee.activitymaster.core.services.classifications.events to com.google.guice, org.hibernate.orm.core, com.entityassist, com.fasterxml.jackson.databind;

	
	//opens com.guicedee.activitymaster.core.services.concepts to com.google.guice, org.hibernate.orm.core, com.entityassist, com.fasterxml.jackson.databind;
	opens com.guicedee.activitymaster.core.services.providers to com.google.guice, org.hibernate.orm.core, com.entityassist, com.fasterxml.jackson.databind;
//	opens com.guicedee.activitymaster.client.services.exceptions to com.google.guice, org.hibernate.orm.core, com.entityassist, com.fasterxml.jackson.databind;
	opens com.guicedee.activitymaster.core.services.system to com.google.guice, org.hibernate.orm.core, com.entityassist, com.fasterxml.jackson.databind;
	opens com.guicedee.activitymaster.core.systems to com.google.guice, org.hibernate.orm.core, com.entityassist, com.fasterxml.jackson.databind;
	
	opens com.guicedee.activitymaster.core.services.threads to com.google.guice, com.entityassist, com.fasterxml.jackson.databind;
	opens com.guicedee.activitymaster.core.threads to com.google.guice, com.entityassist, com.fasterxml.jackson.databind;
	
	
//	opens com.guicedee.activitymaster.core.api  to com.google.guice, com.entityassist, com.fasterxml.jackson.databind,com.guicedee.guicedservlets.rest;
	
	
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
