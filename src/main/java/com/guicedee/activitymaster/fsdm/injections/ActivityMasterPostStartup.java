package com.guicedee.activitymaster.fsdm.injections;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.party.*;
import com.guicedee.activitymaster.fsdm.client.services.deserializers.*;
import com.guicedee.guicedinjection.GuiceContext;
import com.guicedee.guicedinjection.interfaces.IGuicePostStartup;
import lombok.extern.java.Log;

import static com.guicedee.guicedinjection.interfaces.ObjectBinderKeys.*;

@Log
public class ActivityMasterPostStartup implements IGuicePostStartup<ActivityMasterPostStartup>
{
	@Override
	public void postLoad()
	{
		log.info("Configuration Jackson JSON for types in FSDM");
		GuiceContext.get(DefaultObjectMapper)
		            .registerModule(new SimpleModule("ActivityMasterJsonModule", Version.unknownVersion())
				            .addDeserializer(IEnterprise.class, new EnterpriseDeserializer())
				            .addDeserializer(IInvolvedPartyType.class, new InvolvedPartyTypeDeserializer())
				            .addDeserializer(IInvolvedPartyNameType.class, new InvolvedPartyNameTypeDeserializer())
				            .addDeserializer(IInvolvedPartyIdentificationType.class, new InvolvedPartyIdentificationTypeDeserializer())
				            .addDeserializer(IInvolvedParty.class, new InvolvedPartyDeserializer())
				            
				            
				            
		            );
	}
	
	@Override
	public Integer sortOrder()
	{
		return Integer.MAX_VALUE;
	}
}
