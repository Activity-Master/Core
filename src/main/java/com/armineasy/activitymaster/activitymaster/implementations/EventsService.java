package com.armineasy.activitymaster.activitymaster.implementations;

import com.armineasy.activitymaster.activitymaster.ActivityMasterConfiguration;
import com.armineasy.activitymaster.activitymaster.db.entities.enterprise.Enterprise;
import com.armineasy.activitymaster.activitymaster.db.entities.events.Event;
import com.armineasy.activitymaster.activitymaster.db.entities.events.EventType;
import com.armineasy.activitymaster.activitymaster.db.entities.systems.Systems;
import com.armineasy.activitymaster.activitymaster.services.dto.IEvent;
import com.armineasy.activitymaster.activitymaster.services.dto.IEventType;
import com.armineasy.activitymaster.activitymaster.services.enumtypes.IEventTypeValue;
import com.armineasy.activitymaster.activitymaster.services.dto.IEnterprise;
import com.armineasy.activitymaster.activitymaster.services.dto.ISystems;
import com.armineasy.activitymaster.activitymaster.services.system.IActiveFlagService;
import com.armineasy.activitymaster.activitymaster.services.system.IEventService;
import com.google.inject.Singleton;
import com.jwebmp.guicedinjection.GuiceContext;

import javax.cache.annotation.CacheKey;
import javax.cache.annotation.CacheResult;
import java.util.Optional;
import java.util.UUID;

import static com.armineasy.activitymaster.activitymaster.services.classifications.classification.Classifications.*;
import static com.jwebmp.guicedinjection.GuiceContext.*;

@Singleton
public class EventsService
		implements IEventService<EventsService>
{
	@Override
	public IEvent<?> createEvent(IEventTypeValue<?> eventType, ISystems<?> originatingSystem, UUID... identityToken)
	{
		Event event = new Event();
		event.setEnterpriseID((Enterprise) originatingSystem.getEnterpriseID());
		event.setSystemID((Systems) originatingSystem);
		event.setOriginalSourceSystemID((Systems) originatingSystem);
		event.setActiveFlagID(get(IActiveFlagService.class)
				                      .getActiveFlag(originatingSystem.getEnterpriseID(), identityToken));
		event.persist();
		event.createDefaultSecurity(originatingSystem, identityToken);
		event.add(NoClassification,eventType, "", originatingSystem, identityToken);
		return event;
	}

	@Override
	public IEventType<?> createEventType(IEventTypeValue<?> eventType, ISystems<?> originatingSystem, UUID... identityToken)
	{
		Optional<EventType> typeExists = ActivityMasterConfiguration
				                                 .get()
				                                 .isDoubleCheckDisabled() ? Optional.empty() :
		                                 new EventType().builder()
		                                                .findByName(eventType.name())
		                                                .withEnterprise(originatingSystem.getEnterpriseID())
		                                                .inActiveRange(originatingSystem.getEnterpriseID())
		                                                .inDateRange()
		                                                .get();
		if (typeExists.isEmpty())
		{
			EventType type = new EventType();
			type.setName(eventType.name());
			type.setDescription(eventType.classificationValue());
			type.setSystemID((Systems) originatingSystem);
			type.setEnterpriseID((Enterprise) originatingSystem.getEnterpriseID());
			type.setActiveFlagID(GuiceContext.get(IActiveFlagService.class)
			                                 .getActiveFlag(originatingSystem.getEnterpriseID(), identityToken));
			type.setOriginalSourceSystemID((Systems) originatingSystem);
			type.persist();
			if (GuiceContext.get(ActivityMasterConfiguration.class)
			                .isSecurityEnabled())
			{
				type.createDefaultSecurity(originatingSystem, identityToken);
			}
			typeExists = Optional.of(type);
		}
		return typeExists.get();
	}

	@Override
	@CacheResult(cacheName = "EventTypes")
	public IEventType<?> findEventType(@CacheKey IEventTypeValue<?> eventType, @CacheKey IEnterprise<?> enterprise, @CacheKey UUID... identityToken)
	{
		return new EventType().builder()
		                      .findByName(eventType.name())
		                      .withEnterprise(enterprise)
		                      .inActiveRange(enterprise, identityToken)
		                      .inDateRange()
		                      .canRead(enterprise, identityToken)
		                      .get()
		                      .get();
	}
}
