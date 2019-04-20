import com.armineasy.activitymaster.activitymaster.db.ActivityMasterDBModule;
import com.armineasy.activitymaster.activitymaster.injections.ActivityMasterBinder;
import com.armineasy.activitymaster.activitymaster.services.IActivityMasterSystem;
import com.armineasy.activitymaster.activitymaster.systems.*;
import com.jwebmp.guicedinjection.interfaces.IGuiceModule;

module com.armineasy.activitymaster.activitymaster {
	exports com.armineasy.activitymaster.activitymaster ;
	exports com.armineasy.activitymaster.activitymaster.implementations ;
	exports com.armineasy.activitymaster.activitymaster.db ;
	exports com.armineasy.activitymaster.activitymaster.db.abstraction ;
	exports com.armineasy.activitymaster.activitymaster.db.abstraction.assists ;
	exports com.armineasy.activitymaster.activitymaster.db.abstraction.builders ;
	exports com.armineasy.activitymaster.activitymaster.db.abstraction.builders.assists ;
	exports com.armineasy.activitymaster.activitymaster.db.entities.enterprise ;
	exports com.armineasy.activitymaster.activitymaster.db.entities.enterprise.builders ;
	exports com.armineasy.activitymaster.activitymaster.db.entities.activeflag ;
	exports com.armineasy.activitymaster.activitymaster.db.entities.activeflag.builders ;
	exports com.armineasy.activitymaster.activitymaster.db.entities.address ;
	exports com.armineasy.activitymaster.activitymaster.db.entities.address.builders ;
	exports com.armineasy.activitymaster.activitymaster.db.entities.arrangement ;
	exports com.armineasy.activitymaster.activitymaster.db.entities.arrangement.builders ;
	exports com.armineasy.activitymaster.activitymaster.db.entities.classifications ;
	exports com.armineasy.activitymaster.activitymaster.db.entities.classifications.builders ;
	exports com.armineasy.activitymaster.activitymaster.db.entities.events ;
	exports com.armineasy.activitymaster.activitymaster.db.entities.events.builders ;
	exports com.armineasy.activitymaster.activitymaster.db.entities.geography ;
	exports com.armineasy.activitymaster.activitymaster.db.entities.geography.builders ;
	exports com.armineasy.activitymaster.activitymaster.db.entities.involvedparty ;
	exports com.armineasy.activitymaster.activitymaster.db.entities.involvedparty.builders ;
	exports com.armineasy.activitymaster.activitymaster.db.entities.product ;
	exports com.armineasy.activitymaster.activitymaster.db.entities.product.builders ;
	exports com.armineasy.activitymaster.activitymaster.db.entities.resourceitem ;
	exports com.armineasy.activitymaster.activitymaster.db.entities.resourceitem.builders ;
	exports com.armineasy.activitymaster.activitymaster.db.entities.security ;
	exports com.armineasy.activitymaster.activitymaster.db.entities.security.builders ;
	exports com.armineasy.activitymaster.activitymaster.db.entities.systems ;
	exports com.armineasy.activitymaster.activitymaster.db.entities.systems.builders ;
	exports com.armineasy.activitymaster.activitymaster.db.entities.yesno ;
	exports com.armineasy.activitymaster.activitymaster.db.entities.yesno.builders ;
	exports com.armineasy.activitymaster.activitymaster.db.hierarchies ;
	exports com.armineasy.activitymaster.activitymaster.db.hierarchies.builders ;
	exports com.armineasy.activitymaster.activitymaster.injections ;
	exports com.armineasy.activitymaster.activitymaster.services ;
	exports com.armineasy.activitymaster.activitymaster.services.capabilities ;
	exports com.armineasy.activitymaster.activitymaster.services.classifications.enterprise;

	exports com.armineasy.activitymaster.activitymaster.services.classifications.address ;
	exports com.armineasy.activitymaster.activitymaster.services.classifications.arrangement ;
	exports com.armineasy.activitymaster.activitymaster.services.classifications.events ;
	exports com.armineasy.activitymaster.activitymaster.services.concepts ;
	exports com.armineasy.activitymaster.activitymaster.services.exceptions ;
	exports com.armineasy.activitymaster.activitymaster.services.security ;
	exports com.armineasy.activitymaster.activitymaster.services.system ;
	exports com.armineasy.activitymaster.activitymaster.services.types ;
	exports com.armineasy.activitymaster.activitymaster.systems;

	requires io.github.classgraph;

	requires com.jwebmp.guicedpersistence;
	requires com.jwebmp.guicedpersistence.btm;
	requires com.jwebmp.entityassist;
	requires com.jwebmp.guicedinjection;
	requires com.google.common;

	requires java.persistence;
	requires java.xml.bind;
	requires java.validation;

	//requires org.apache.commons.io;

	requires java.naming;

	requires java.logging;

	requires com.google.guice;

	requires org.hibernate.orm.core;
	requires org.hibernate.validator;

	requires lombok;

	requires cache.api;

	provides IGuiceModule with ActivityMasterDBModule;

	provides IActivityMasterSystem with EnterpriseSystem, ActiveFlagSystem, SystemsSystem, ClassificationsDataConceptSystem,
			                               ClassificationsSystem, YesNoSystem, AddressSystem, InvolvedPartySystem, SecurityTokenSystem
			                               , ArrangementsSystem, EventsSystem, ResourceItemSystem, ProductsSystem;

	provides com.jwebmp.guicedinjection.interfaces.IGuiceDefaultBinder with ActivityMasterBinder;

	uses IActivityMasterSystem;
	
	opens com.armineasy.activitymaster.activitymaster to com.google.guice, org.hibernate.orm.core,com.jwebmp.entityassist;
	opens com.armineasy.activitymaster.activitymaster.implementations to com.google.guice, org.hibernate.orm.core,com.jwebmp.entityassist;
	opens com.armineasy.activitymaster.activitymaster.db to com.google.guice, org.hibernate.orm.core,com.jwebmp.entityassist;
	opens com.armineasy.activitymaster.activitymaster.db.abstraction to com.google.guice, org.hibernate.orm.core,com.jwebmp.entityassist;
	opens com.armineasy.activitymaster.activitymaster.db.abstraction.assists to com.google.guice, org.hibernate.orm.core,com.jwebmp.entityassist;
	opens com.armineasy.activitymaster.activitymaster.db.abstraction.builders to com.google.guice, org.hibernate.orm.core,com.jwebmp.entityassist;
	opens com.armineasy.activitymaster.activitymaster.db.abstraction.builders.assists to com.google.guice, org.hibernate.orm.core,com.jwebmp.entityassist;
	opens com.armineasy.activitymaster.activitymaster.db.entities.enterprise to com.google.guice, org.hibernate.orm.core,com.jwebmp.entityassist;
	opens com.armineasy.activitymaster.activitymaster.db.entities.enterprise.builders to com.google.guice, org.hibernate.orm.core,com.jwebmp.entityassist;
	opens com.armineasy.activitymaster.activitymaster.db.entities.activeflag to com.google.guice, org.hibernate.orm.core,com.jwebmp.entityassist;
	opens com.armineasy.activitymaster.activitymaster.db.entities.activeflag.builders to com.google.guice, org.hibernate.orm.core,com.jwebmp.entityassist;
	opens com.armineasy.activitymaster.activitymaster.db.entities.address to com.google.guice, org.hibernate.orm.core,com.jwebmp.entityassist;
	opens com.armineasy.activitymaster.activitymaster.db.entities.address.builders to com.google.guice, org.hibernate.orm.core,com.jwebmp.entityassist;
	opens com.armineasy.activitymaster.activitymaster.db.entities.arrangement to com.google.guice, org.hibernate.orm.core,com.jwebmp.entityassist;
	opens com.armineasy.activitymaster.activitymaster.db.entities.arrangement.builders to com.google.guice, org.hibernate.orm.core,com.jwebmp.entityassist;
	opens com.armineasy.activitymaster.activitymaster.db.entities.classifications to com.google.guice, org.hibernate.orm.core,com.jwebmp.entityassist;
	opens com.armineasy.activitymaster.activitymaster.db.entities.classifications.builders to com.google.guice, org.hibernate.orm.core,com.jwebmp.entityassist;
	opens com.armineasy.activitymaster.activitymaster.db.entities.events to com.google.guice, org.hibernate.orm.core,com.jwebmp.entityassist;
	opens com.armineasy.activitymaster.activitymaster.db.entities.events.builders to com.google.guice, org.hibernate.orm.core,com.jwebmp.entityassist;
	opens com.armineasy.activitymaster.activitymaster.db.entities.geography to com.google.guice, org.hibernate.orm.core,com.jwebmp.entityassist;
	opens com.armineasy.activitymaster.activitymaster.db.entities.geography.builders to com.google.guice, org.hibernate.orm.core,com.jwebmp.entityassist;
	opens com.armineasy.activitymaster.activitymaster.db.entities.involvedparty to com.google.guice, org.hibernate.orm.core,com.jwebmp.entityassist;
	opens com.armineasy.activitymaster.activitymaster.db.entities.involvedparty.builders to com.google.guice, org.hibernate.orm.core,com.jwebmp.entityassist;
	opens com.armineasy.activitymaster.activitymaster.db.entities.product to com.google.guice, org.hibernate.orm.core,com.jwebmp.entityassist;
	opens com.armineasy.activitymaster.activitymaster.db.entities.product.builders to com.google.guice, org.hibernate.orm.core,com.jwebmp.entityassist;
	opens com.armineasy.activitymaster.activitymaster.db.entities.resourceitem to com.google.guice, org.hibernate.orm.core,com.jwebmp.entityassist;
	opens com.armineasy.activitymaster.activitymaster.db.entities.resourceitem.builders to com.google.guice, org.hibernate.orm.core,com.jwebmp.entityassist;
	opens com.armineasy.activitymaster.activitymaster.db.entities.security to com.google.guice, org.hibernate.orm.core,com.jwebmp.entityassist;
	opens com.armineasy.activitymaster.activitymaster.db.entities.security.builders to com.google.guice, org.hibernate.orm.core,com.jwebmp.entityassist;
	opens com.armineasy.activitymaster.activitymaster.db.entities.systems to com.google.guice, org.hibernate.orm.core,com.jwebmp.entityassist;
	opens com.armineasy.activitymaster.activitymaster.db.entities.systems.builders to com.google.guice, org.hibernate.orm.core,com.jwebmp.entityassist;
	opens com.armineasy.activitymaster.activitymaster.db.entities.yesno to com.google.guice, org.hibernate.orm.core,com.jwebmp.entityassist;
	opens com.armineasy.activitymaster.activitymaster.db.entities.yesno.builders to com.google.guice, org.hibernate.orm.core,com.jwebmp.entityassist;
	opens com.armineasy.activitymaster.activitymaster.db.hierarchies to com.google.guice, org.hibernate.orm.core,com.jwebmp.entityassist;
	opens com.armineasy.activitymaster.activitymaster.db.hierarchies.builders to com.google.guice, org.hibernate.orm.core,com.jwebmp.entityassist;
	opens com.armineasy.activitymaster.activitymaster.injections to com.google.guice, org.hibernate.orm.core,com.jwebmp.entityassist;
	opens com.armineasy.activitymaster.activitymaster.services to com.google.guice, org.hibernate.orm.core,com.jwebmp.entityassist;
	opens com.armineasy.activitymaster.activitymaster.services.capabilities to com.google.guice, org.hibernate.orm.core,com.jwebmp.entityassist;
	opens com.armineasy.activitymaster.activitymaster.services.classifications to com.google.guice, org.hibernate.orm.core,com.jwebmp.entityassist;
	opens com.armineasy.activitymaster.activitymaster.services.classifications.address to com.google.guice, org.hibernate.orm.core,com.jwebmp.entityassist;
	opens com.armineasy.activitymaster.activitymaster.services.classifications.arrangement to com.google.guice, org.hibernate.orm.core,com.jwebmp.entityassist;
	opens com.armineasy.activitymaster.activitymaster.services.classifications.events to com.google.guice, org.hibernate.orm.core,com.jwebmp.entityassist;
	opens com.armineasy.activitymaster.activitymaster.services.concepts to com.google.guice, org.hibernate.orm.core,com.jwebmp.entityassist;
	opens com.armineasy.activitymaster.activitymaster.services.exceptions to com.google.guice, org.hibernate.orm.core,com.jwebmp.entityassist;
	opens com.armineasy.activitymaster.activitymaster.services.security to com.google.guice, org.hibernate.orm.core,com.jwebmp.entityassist;
	opens com.armineasy.activitymaster.activitymaster.services.system to com.google.guice, org.hibernate.orm.core,com.jwebmp.entityassist;
	opens com.armineasy.activitymaster.activitymaster.services.types to com.google.guice, org.hibernate.orm.core,com.jwebmp.entityassist;
	opens com.armineasy.activitymaster.activitymaster.systems to com.google.guice, org.hibernate.orm.core,com.jwebmp.entityassist;
	exports com.armineasy.activitymaster.activitymaster.services.classifications.resourceitems;
	exports com.armineasy.activitymaster.activitymaster.services.classifications.involvedparty;
	exports com.armineasy.activitymaster.activitymaster.services.classifications.securitytokens;

}
