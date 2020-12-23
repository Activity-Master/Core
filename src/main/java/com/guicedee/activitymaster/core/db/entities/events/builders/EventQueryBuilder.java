package com.guicedee.activitymaster.core.db.entities.events.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderTable;
import com.guicedee.activitymaster.core.db.abstraction.builders.handlers.IContainsClassificationsQueryBuilder;
import com.guicedee.activitymaster.core.db.entities.events.Event;
import com.guicedee.activitymaster.core.db.entities.events.EventSecurityToken;
import com.guicedee.activitymaster.core.db.entities.events.EventXClassification;
import com.guicedee.activitymaster.core.services.system.ITimeService;
import com.guicedee.activitymaster.core.services.system.ITimeSystem;
import com.guicedee.guicedinjection.GuiceContext;

import java.sql.Date;
import java.time.ZoneOffset;

public class EventQueryBuilder
		extends QueryBuilderTable<EventQueryBuilder, Event, java.util.UUID, EventSecurityToken>
		implements IContainsClassificationsQueryBuilder<EventQueryBuilder, Event, java.util.UUID, EventXClassification>
{

	@Override
	public boolean onCreate(Event entity)
	{
		ITimeService time = GuiceContext.get(ITimeService.class);
		GuiceContext.get(ITimeSystem.class)
		            .getDay(Date.from(entity.getEffectiveFromDate()
		                                    .toInstant(ZoneOffset.ofHours(2))));

		entity.setDayID(time.getDayID(entity.getEffectiveFromDate()));
		entity.setHourID(time.getHourID(entity.getEffectiveFromDate()));
		entity.setMinuteID(time.getMinuteID(entity.getEffectiveFromDate()));
		return super.onCreate(entity);
	}
}
