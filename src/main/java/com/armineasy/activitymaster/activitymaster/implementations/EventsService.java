package com.armineasy.activitymaster.activitymaster.implementations;

import com.armineasy.activitymaster.activitymaster.ActivityMasterConfiguration;
import com.armineasy.activitymaster.activitymaster.db.entities.enterprise.Enterprise;
import com.armineasy.activitymaster.activitymaster.db.entities.events.Event;
import com.armineasy.activitymaster.activitymaster.db.entities.events.EventType;
import com.armineasy.activitymaster.activitymaster.db.entities.involvedparty.InvolvedParty;
import com.armineasy.activitymaster.activitymaster.db.entities.systems.SystemXClassification;
import com.armineasy.activitymaster.activitymaster.db.entities.systems.Systems;
import com.armineasy.activitymaster.activitymaster.services.IEventTypeValue;
import com.armineasy.activitymaster.activitymaster.services.system.IActiveFlagService;
import com.google.inject.Singleton;
import com.jwebmp.guicedinjection.GuiceContext;

import javax.cache.annotation.CacheKey;
import javax.cache.annotation.CacheResult;
import java.util.Optional;
import java.util.UUID;

@Singleton
public class EventsService
{
	public Event createEvent(IEventTypeValue<?> eventType, InvolvedParty originatingParty, Systems originatingSystem)
	{
		return null;
	}


	public EventType createEventType(IEventTypeValue<?> eventType, Systems originatingSystem, UUID... identityToken)
	{
		Optional<EventType> typeExists = new EventType().builder()
		                                                .findByName(eventType.name())
		                                                .withEnterprise(originatingSystem.getEnterpriseID())
		                                                .inActiveRange(originatingSystem.getEnterpriseID())
		                                                .inDateRange()
		                                                .get();
		if(typeExists.isEmpty())
		{
			EventType type = new EventType();
			type.setName(eventType.name());
			type.setDescription(eventType.classificationValue());
			type.setSystemID(originatingSystem);
			type.setEnterpriseID(originatingSystem.getEnterpriseID());
			type.setActiveFlagID(GuiceContext.get(IActiveFlagService.class)
			                                 .getActiveFlag(originatingSystem.getEnterpriseID(), identityToken));
			type.setOriginalSourceSystemID(originatingSystem);
			type.persist();
			if(GuiceContext.get(ActivityMasterConfiguration.class).isSecurityEnabled())
			{
				type.createDefaultSecurity(originatingSystem);
			}
			typeExists = Optional.of(type);
		}
		return typeExists.get();
	}

	@CacheResult(cacheName = "EventTypes")
	public EventType findEventType(@CacheKey IEventTypeValue<?> eventType,@CacheKey Enterprise enterprise,@CacheKey UUID... identityToken)
	{
		return new EventType().builder()
		                      .findByName(eventType.name())
		                      .withEnterprise(enterprise)
		                      .inActiveRange(enterprise)
		                      .inDateRange()
		                      .canRead(enterprise, identityToken)
		                      .get()
				.get();
	}


}
