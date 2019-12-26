package com.guicedee.activitymaster.core.db.entities.events.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilder;
import com.guicedee.activitymaster.core.db.entities.events.Event;
import com.guicedee.activitymaster.core.db.entities.events.EventSecurityToken;
import com.guicedee.activitymaster.core.services.system.ITimeService;
import com.guicedee.activitymaster.core.services.system.ITimeSystem;
import com.guicedee.guicedinjection.GuiceContext;

import java.sql.Date;
import java.time.ZoneOffset;

public class EventQueryBuilder
		extends QueryBuilder<EventQueryBuilder, Event, Long, EventSecurityToken>
{

	@Override
	protected boolean onCreate(Event entity)
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
