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
		extends QueryBuilderTable<EventQueryBuilder, Event, java.lang.String>
		implements IEventQueryBuilder<EventQueryBuilder, Event>
{
	
	@Override
	public boolean onCreate(Event entity)
	{
		ITimeService time = GuiceContext.get(ITimeService.class);
		GuiceContext.get(ITimeSystem.class)
		            .getDay(Date.from(entity.getEffectiveFromDate().withOffsetSameInstant(ZoneOffset.of(ZoneOffset.systemDefault().getId())).toInstant()));
		
		entity.setDayID(time.getDayID(entity.getEffectiveFromDate().withOffsetSameInstant(ZoneOffset.of(ZoneOffset.systemDefault().getId())).toLocalDateTime()));
		entity.setHourID(time.getHourID(entity.getEffectiveFromDate().withOffsetSameInstant(ZoneOffset.of(ZoneOffset.systemDefault().getId())).toLocalDateTime()));
		entity.setMinuteID(time.getMinuteID(entity.getEffectiveFromDate().withOffsetSameInstant(ZoneOffset.of(ZoneOffset.systemDefault().getId())).toLocalDateTime()));
		return super.onCreate(entity);
	}
}
