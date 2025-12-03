package com.guicedee.activitymaster.fsdm.injections;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.google.inject.Inject;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.party.*;
import com.guicedee.activitymaster.fsdm.client.services.deserializers.*;
import com.guicedee.client.services.lifecycle.IGuicePostStartup;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import lombok.extern.log4j.Log4j2;

import java.util.List;

import static com.guicedee.client.implementations.ObjectBinderKeys.*;

@Log4j2
public class ActivityMasterPostStartup implements IGuicePostStartup<ActivityMasterPostStartup>
{
	@Inject
	private Vertx vertx;

	@Override
	public List<Future<Boolean>> postLoad()
	{
		return List.of(vertx.executeBlocking(() -> {
			log.info("Configuration Jackson JSON for types in FSDM");
			com.guicedee.client.IGuiceContext.get(DefaultObjectMapper)
			                                 .registerModule(new SimpleModule("ActivityMasterJsonModule", Version.unknownVersion())
					                                 .addDeserializer(IEnterprise.class, new EnterpriseDeserializer())
					                                 .addDeserializer(IInvolvedPartyType.class, new InvolvedPartyTypeDeserializer())
					                                 .addDeserializer(IInvolvedPartyNameType.class, new InvolvedPartyNameTypeDeserializer())
					                                 .addDeserializer(IInvolvedPartyIdentificationType.class, new InvolvedPartyIdentificationTypeDeserializer())
					                                 .addDeserializer(IInvolvedParty.class, new InvolvedPartyDeserializer())
			                                 );
			return true;
		}));

	}
	
	@Override
	public Integer sortOrder()
	{
		return Integer.MIN_VALUE + 1000;
	}
}
