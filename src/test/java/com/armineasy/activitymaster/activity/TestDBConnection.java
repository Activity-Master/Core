package com.armineasy.activitymaster.activity;

import com.armineasy.activitymaster.activity.configs.DefaultTestConfig;
import com.armineasy.activitymaster.activitymaster.db.ActivityMasterDBModule;
import com.armineasy.activitymaster.activitymaster.db.entities.activeflag.ActiveFlag;
import com.armineasy.activitymaster.activitymaster.db.entities.enterprise.Enterprise;
import com.armineasy.activitymaster.activitymaster.db.entities.involvedparty.InvolvedParty;
import com.armineasy.activitymaster.activitymaster.db.entities.involvedparty.InvolvedPartyIdentificationType;
import com.armineasy.activitymaster.activitymaster.db.hierarchies.SecurityHierarchyView;
import com.armineasy.activitymaster.activitymaster.db.entities.security.SecurityToken;
import com.armineasy.activitymaster.activitymaster.db.entities.security.SecurityToken_;
import com.armineasy.activitymaster.activitymaster.services.classifications.securitytokens.SecurityTokenClassifications;
import com.armineasy.activitymaster.activitymaster.services.dto.IEnterprise;
import com.armineasy.activitymaster.activitymaster.services.types.IdentificationTypes;
import com.jwebmp.guicedinjection.GuiceContext;
import com.jwebmp.logger.LogFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.logging.Level;

import static com.jwebmp.entityassist.enumerations.Operand.*;

@ExtendWith(DefaultTestConfig.class)
public class TestDBConnection
{
	@Test
	public void testIt()
	{

		ActivityMasterDBModule.persistenceUnitName = "ActivityMasterUT";

		LogFactory.configureConsoleColourOutput(Level.FINE);
		System.out.println(Long.MIN_VALUE);
		GuiceContext.inject();
		new ActiveFlag().builder()
		                .getAll();
		new SecurityHierarchyView().builder()
		                           .getAll();

		new SecurityToken().builder()
		                   .where(SecurityToken_.id, Equals, -9223372036854775808L)
		                   .get();

		IEnterprise<?> enterprise = new Enterprise().builder()
		                                            .get(true)
		                                            .get();
		new InvolvedParty().builder()
		                   .findByIdentificationType(new Enterprise(1L), IdentificationTypes.IdentificationTypeUUID)
		                   .get();



	}
}
