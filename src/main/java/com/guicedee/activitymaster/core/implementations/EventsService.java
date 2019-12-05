package com.guicedee.activitymaster.core.implementations;

import com.guicedee.activitymaster.core.ActivityMasterConfiguration;
import com.guicedee.activitymaster.core.db.entities.activeflag.ActiveFlag;
import com.guicedee.activitymaster.core.db.entities.enterprise.Enterprise;
import com.guicedee.activitymaster.core.db.entities.events.Event;
import com.guicedee.activitymaster.core.db.entities.events.EventType;
import com.guicedee.activitymaster.core.db.entities.systems.Systems;
import com.guicedee.activitymaster.core.services.dto.IEvent;
import com.guicedee.activitymaster.core.services.dto.IEventType;
import com.guicedee.activitymaster.core.services.enumtypes.IEventTypeValue;
import com.guicedee.activitymaster.core.services.dto.IEnterprise;
import com.guicedee.activitymaster.core.services.dto.ISystems;
import com.guicedee.activitymaster.core.services.system.IActiveFlagService;
import com.guicedee.activitymaster.core.services.system.IEventService;
import com.google.inject.Singleton;
import com.guicedee.guicedinjection.GuiceContext;

import javax.cache.annotation.CacheKey;
import javax.cache.annotation.CacheResult;
import java.util.Optional;
import java.util.UUID;

import static com.guicedee.activitymaster.core.services.classifications.classification.Classifications.*;
import static com.guicedee.guicedinjection.GuiceContext.*;

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
		event.setActiveFlagID((ActiveFlag) get(IActiveFlagService.class)
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
			type.setActiveFlagID((ActiveFlag)GuiceContext.get(IActiveFlagService.class)
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
