package com.guicedee.activitymaster.fsdm.db.entities.events.builders;

import com.guicedee.activitymaster.fsdm.client.services.ITimeService;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.events.IEventQueryBuilder;
import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderTable;
import com.guicedee.activitymaster.fsdm.db.entities.events.Event;
import com.guicedee.activitymaster.fsdm.services.system.ITimeSystem;
import com.guicedee.guicedinjection.GuiceContext;

import java.sql.Date;
import java.time.ZoneOffset;

public class EventQueryBuilder
		extends QueryBuilderTable<EventQueryBuilder, Event, java.util.UUID>
		implements IEventQueryBuilder<EventQueryBuilder, Event>
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
