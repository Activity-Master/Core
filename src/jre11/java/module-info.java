import com.guicedee.activitymaster.fsdm.client.services.events.IOnSystemInstall;
import com.guicedee.activitymaster.fsdm.client.services.events.IOnSystemUpdate;
import com.guicedee.activitymaster.fsdm.client.services.systems.IActivityMasterSystem;
import com.guicedee.activitymaster.fsdm.db.ActivityMasterDBModule;
import com.guicedee.activitymaster.fsdm.implementations.*;
import com.guicedee.activitymaster.fsdm.injections.*;
import com.guicedee.activitymaster.fsdm.systems.*;
import com.guicedee.guicedhazelcast.services.IGuicedHazelcastServerConfig;
import com.guicedee.guicedinjection.interfaces.*;

module com.guicedee.activitymaster.fsdm {
	requires transitive com.guicedee.activitymaster.fsdm.client;
	
	exports com.guicedee.activitymaster.fsdm;
	
	exports com.guicedee.activitymaster.fsdm.async;
	exports com.guicedee.activitymaster.fsdm.services;
	
	exports com.guicedee.activitymaster.fsdm.services.system;
	exports com.guicedee.activitymaster.fsdm.db.entities.time;
	
	exports com.guicedee.activitymaster.fsdm.threads;
	exports com.guicedee.activitymaster.fsdm.implementations.interceptors;
	
	
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
	
	
	requires static lombok;
	requires org.apache.commons.compress;

	provides IGuiceModule with ActivityMasterDBModule,
			ActivityMasterBinder,
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
			RulesBinder,
			ActivityMasterSystemBinder,
			PasswordsServiceBinder;
	
	provides IGuiceDefaultBinder with EventInterceptorsBinder;
	
	provides IGuiceConfigurator with ActivityMasterScanConfiguration;
	
	provides IActivityMasterSystem with EnterpriseSystem,
			ActiveFlagSystem,
			SystemsSystem,
			ClassificationsDataConceptSystem,
			ClassificationsSystem,
			AddressSystem,
			InvolvedPartySystem,
			SecurityTokenSystem,
			ArrangementsSystem,
			EventsSystem,
			ResourceItemSystem,
			ProductsSystem
			;
	
	provides IGuicedHazelcastServerConfig with HazelcastServerConfig;
	provides com.guicedee.guicedhazelcast.services.IGuicedHazelcastClientConfig with HazelcastClientConfig;
	provides com.guicedee.guicedinjection.interfaces.IGuiceScanModuleInclusions with ActivityMasterModuleInclusion;
	
	provides IGuicePreStartup with FSDMHazelcastPreStartup;
	provides IGuicePostStartup with ActivityMasterPostStartup;
	
	
	uses IOnSystemUpdate;
	uses IOnSystemInstall;
	
	
	opens com.guicedee.activitymaster.fsdm to com.google.guice, org.hibernate.orm.core, com.entityassist, com.fasterxml.jackson.databind;
	opens com.guicedee.activitymaster.fsdm.async to com.google.guice, org.hibernate.orm.core, com.entityassist, com.fasterxml.jackson.databind;
	opens com.guicedee.activitymaster.fsdm.implementations to com.google.guice, org.hibernate.orm.core, com.entityassist, com.fasterxml.jackson.databind, com.guicedee.activitymaster.geography;
	opens com.guicedee.activitymaster.fsdm.db to com.google.guice, org.hibernate.orm.core, com.entityassist, com.guicedee.guicedinjection, com.fasterxml.jackson.databind;
	opens com.guicedee.activitymaster.fsdm.db.abstraction to com.google.guice, org.hibernate.orm.core, com.entityassist, com.guicedee.guicedinjection, com.fasterxml.jackson.databind;
	opens com.guicedee.activitymaster.fsdm.db.abstraction.assists to com.google.guice, org.hibernate.orm.core, com.entityassist, com.guicedee.guicedinjection, com.fasterxml.jackson.databind;
	exports com.guicedee.activitymaster.fsdm.db.abstraction.builders;
	opens com.guicedee.activitymaster.fsdm.db.abstraction.builders to com.google.guice, org.hibernate.orm.core, com.entityassist, com.guicedee.guicedinjection, com.fasterxml.jackson.databind;
	
	exports com.guicedee.activitymaster.fsdm.db.abstraction.builders.assists;
	opens com.guicedee.activitymaster.fsdm.db.abstraction.builders.assists to com.google.guice, org.hibernate.orm.core, com.entityassist, com.guicedee.guicedinjection, com.fasterxml.jackson.databind;
	
	
	opens com.guicedee.activitymaster.fsdm.db.entities.enterprise to com.google.guice, org.hibernate.orm.core, com.entityassist, com.guicedee.guicedinjection, com.fasterxml.jackson.databind, com.guicedee.activitymaster.geography;
	
	exports com.guicedee.activitymaster.fsdm.db.entities.enterprise.builders;
	opens com.guicedee.activitymaster.fsdm.db.entities.enterprise.builders to com.google.guice, org.hibernate.orm.core, com.entityassist, com.guicedee.guicedinjection, com.fasterxml.jackson.databind, com.guicedee.activitymaster.geography;

	opens com.guicedee.activitymaster.fsdm.db.entities.activeflag to com.google.guice, org.hibernate.orm.core, com.entityassist, com.guicedee.guicedinjection, com.fasterxml.jackson.databind;

	exports com.guicedee.activitymaster.fsdm.db.entities.activeflag.builders;
	opens com.guicedee.activitymaster.fsdm.db.entities.activeflag.builders to com.google.guice, org.hibernate.orm.core, com.entityassist, com.guicedee.guicedinjection, com.fasterxml.jackson.databind;

	opens com.guicedee.activitymaster.fsdm.db.entities.address to com.google.guice, org.hibernate.orm.core, com.entityassist, com.guicedee.guicedinjection, com.fasterxml.jackson.databind;


	exports com.guicedee.activitymaster.fsdm.db.entities.address.builders;
	opens com.guicedee.activitymaster.fsdm.db.entities.address.builders to com.google.guice, org.hibernate.orm.core, com.entityassist, com.guicedee.guicedinjection, com.fasterxml.jackson.databind;

	opens com.guicedee.activitymaster.fsdm.db.entities.arrangement to com.google.guice, org.hibernate.orm.core, com.entityassist, com.guicedee.guicedinjection, com.fasterxml.jackson.databind;
	
	
	exports com.guicedee.activitymaster.fsdm.db.entities.arrangement.builders;
	opens com.guicedee.activitymaster.fsdm.db.entities.arrangement.builders to com.google.guice, org.hibernate.orm.core, com.entityassist, com.guicedee.guicedinjection, com.fasterxml.jackson.databind;
	
	opens com.guicedee.activitymaster.fsdm.db.entities.classifications to com.google.guice, org.hibernate.orm.core, com.entityassist, com.guicedee.guicedinjection, com.fasterxml.jackson.databind, com.guicedee.activitymaster.geography;
	
	exports com.guicedee.activitymaster.fsdm.db.entities.classifications.builders;
	opens com.guicedee.activitymaster.fsdm.db.entities.classifications.builders to com.google.guice, org.hibernate.orm.core, com.entityassist, com.guicedee.guicedinjection, com.fasterxml.jackson.databind, com.guicedee.activitymaster.geography;
	
	opens com.guicedee.activitymaster.fsdm.db.entities.events to com.google.guice, org.hibernate.orm.core, com.entityassist, com.guicedee.guicedinjection, com.fasterxml.jackson.databind;
	
	exports com.guicedee.activitymaster.fsdm.db.entities.events.builders;
	opens com.guicedee.activitymaster.fsdm.db.entities.events.builders to com.google.guice, org.hibernate.orm.core, com.entityassist, com.guicedee.guicedinjection, com.fasterxml.jackson.databind;
	
	opens com.guicedee.activitymaster.fsdm.db.entities.geography to com.google.guice, org.hibernate.orm.core, com.entityassist, com.guicedee.guicedinjection, com.fasterxml.jackson.databind, com.guicedee.activitymaster.geography;
	
	
	exports com.guicedee.activitymaster.fsdm.db.entities.geography.builders;
	opens com.guicedee.activitymaster.fsdm.db.entities.geography.builders to com.google.guice, org.hibernate.orm.core, com.entityassist, com.guicedee.guicedinjection, com.fasterxml.jackson.databind, com.guicedee.activitymaster.geography;
	
	
	opens com.guicedee.activitymaster.fsdm.db.entities.involvedparty to com.google.guice, org.hibernate.orm.core, com.entityassist, com.guicedee.guicedinjection, com.fasterxml.jackson.databind;
	exports com.guicedee.activitymaster.fsdm.db.entities.involvedparty.builders;
	opens com.guicedee.activitymaster.fsdm.db.entities.involvedparty.builders to com.google.guice, org.hibernate.orm.core, com.entityassist, com.guicedee.guicedinjection, com.fasterxml.jackson.databind;
	
	
	opens com.guicedee.activitymaster.fsdm.db.entities.product to com.google.guice, org.hibernate.orm.core, com.entityassist, com.guicedee.guicedinjection, com.fasterxml.jackson.databind;
	
	
	exports com.guicedee.activitymaster.fsdm.db.entities.product.builders;
	opens com.guicedee.activitymaster.fsdm.db.entities.product.builders to com.google.guice, org.hibernate.orm.core, com.entityassist, com.guicedee.guicedinjection, com.fasterxml.jackson.databind;
	
	opens com.guicedee.activitymaster.fsdm.db.entities.rules to com.google.guice, org.hibernate.orm.core, com.entityassist, com.guicedee.guicedinjection, com.fasterxml.jackson.databind;
	
	exports com.guicedee.activitymaster.fsdm.db.entities.rules.builders;
	opens com.guicedee.activitymaster.fsdm.db.entities.rules.builders to com.google.guice, org.hibernate.orm.core, com.entityassist, com.guicedee.guicedinjection, com.fasterxml.jackson.databind;
	
	
	opens com.guicedee.activitymaster.fsdm.db.entities.resourceitem to com.google.guice, org.hibernate.orm.core, com.entityassist, com.guicedee.guicedinjection, com.fasterxml.jackson.databind;
	exports com.guicedee.activitymaster.fsdm.db.entities.resourceitem.builders;
	opens com.guicedee.activitymaster.fsdm.db.entities.resourceitem.builders to com.google.guice, org.hibernate.orm.core, com.entityassist, com.guicedee.guicedinjection, com.fasterxml.jackson.databind;
	
	opens com.guicedee.activitymaster.fsdm.db.entities.security to com.google.guice, org.hibernate.orm.core, com.entityassist, com.guicedee.guicedinjection, com.fasterxml.jackson.databind;
	
	
	exports com.guicedee.activitymaster.fsdm.db.entities.security.builders;
	opens com.guicedee.activitymaster.fsdm.db.entities.security.builders to com.google.guice, org.hibernate.orm.core, com.entityassist, com.guicedee.guicedinjection, com.fasterxml.jackson.databind;
	
	opens com.guicedee.activitymaster.fsdm.db.entities.systems to com.google.guice, org.hibernate.orm.core, com.entityassist, com.guicedee.guicedinjection, com.fasterxml.jackson.databind, com.guicedee.activitymaster.geography;

	exports com.guicedee.activitymaster.fsdm.db.entities.systems.builders;
	opens com.guicedee.activitymaster.fsdm.db.entities.systems.builders to com.google.guice, org.hibernate.orm.core, com.entityassist, com.guicedee.guicedinjection, com.fasterxml.jackson.databind, com.guicedee.activitymaster.geography;
	
	opens com.guicedee.activitymaster.fsdm.db.entities.time to com.google.guice, org.hibernate.orm.core, com.entityassist, com.guicedee.guicedinjection, com.fasterxml.jackson.databind;
	exports com.guicedee.activitymaster.fsdm.db.entities.time.builders;
	opens com.guicedee.activitymaster.fsdm.db.entities.time.builders to com.google.guice, org.hibernate.orm.core, com.entityassist, com.guicedee.guicedinjection, com.fasterxml.jackson.databind;
	
	opens com.guicedee.activitymaster.fsdm.db.hierarchies to com.google.guice, org.hibernate.orm.core, com.entityassist, com.guicedee.guicedinjection, com.fasterxml.jackson.databind;
	
	exports com.guicedee.activitymaster.fsdm.db.hierarchies.builders;
	opens com.guicedee.activitymaster.fsdm.db.hierarchies.builders to com.google.guice, org.hibernate.orm.core, com.entityassist, com.guicedee.guicedinjection, com.fasterxml.jackson.databind;
	
	
	exports com.guicedee.activitymaster.fsdm.injections;
	opens com.guicedee.activitymaster.fsdm.injections to com.google.guice, org.hibernate.orm.core, com.entityassist, com.fasterxml.jackson.databind;
	opens com.guicedee.activitymaster.fsdm.injections.updates to com.google.guice, org.hibernate.orm.core, com.entityassist, com.fasterxml.jackson.databind;
	
	
	opens com.guicedee.activitymaster.fsdm.services to com.google.guice, org.hibernate.orm.core, com.entityassist, com.fasterxml.jackson.databind;
	
	
	exports com.guicedee.activitymaster.fsdm.services.providers;
	opens com.guicedee.activitymaster.fsdm.services.providers to com.google.guice, org.hibernate.orm.core, com.entityassist, com.fasterxml.jackson.databind;
	
	
	opens com.guicedee.activitymaster.fsdm.services.system to com.google.guice, org.hibernate.orm.core, com.entityassist, com.fasterxml.jackson.databind;
	
	
	exports com.guicedee.activitymaster.fsdm.systems;
	opens com.guicedee.activitymaster.fsdm.systems to com.google.guice, org.hibernate.orm.core, com.entityassist, com.fasterxml.jackson.databind;
	
	
	opens com.guicedee.activitymaster.fsdm.services.threads to com.google.guice, com.entityassist, com.fasterxml.jackson.databind;
	opens com.guicedee.activitymaster.fsdm.threads to com.google.guice, com.entityassist, com.fasterxml.jackson.databind;
	
	opens com.guicedee.activitymaster.fsdm.implementations.interceptors to com.google.guice;
	
	exports com.guicedee.activitymaster.fsdm.db.entities.systems to com.guicedee.activitymaster.geography;
	exports com.guicedee.activitymaster.fsdm.db.entities.enterprise to com.guicedee.activitymaster.geography;
	exports com.guicedee.activitymaster.fsdm.db.entities.classifications to com.guicedee.activitymaster.geography;

	exports com.guicedee.activitymaster.fsdm.db.entities.geography to com.guicedee.activitymaster.geography;
	exports com.guicedee.activitymaster.fsdm.db.entities.rules to com.guicedee.activitymaster.geography;
	exports com.guicedee.activitymaster.fsdm.db.entities.resourceitem to com.guicedee.activitymaster.geography;
	exports com.guicedee.activitymaster.fsdm.implementations to com.guicedee.activitymaster.geography;

	exports com.guicedee.activitymaster.fsdm.db;
}
