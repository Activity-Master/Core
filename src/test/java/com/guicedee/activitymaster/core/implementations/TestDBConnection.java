package com.guicedee.activitymaster.core.implementations;

import com.guicedee.activitymaster.core.db.ActivityMasterDBModule;
import com.guicedee.activitymaster.core.db.entities.activeflag.ActiveFlag;
import com.guicedee.activitymaster.core.db.entities.enterprise.Enterprise;
import com.guicedee.activitymaster.core.db.entities.involvedparty.InvolvedParty;
import com.guicedee.activitymaster.core.db.hierarchies.SecurityHierarchyView;
import com.guicedee.activitymaster.core.db.entities.security.SecurityToken;
import com.guicedee.activitymaster.core.db.entities.security.SecurityToken_;
import com.guicedee.activitymaster.core.services.dto.IEnterprise;
import com.guicedee.activitymaster.core.services.types.IdentificationTypes;
import com.guicedee.guicedinjection.GuiceContext;
import com.guicedee.logger.LogFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.logging.Level;

import static com.entityassist.enumerations.Operand.*;

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
