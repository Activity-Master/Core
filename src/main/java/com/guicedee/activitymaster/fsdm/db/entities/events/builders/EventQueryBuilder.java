package com.guicedee.activitymaster.fsdm.db.entities.events.builders;

import com.guicedee.activitymaster.fsdm.client.services.ITimeService;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.events.IEventQueryBuilder;
import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderTable;
import com.guicedee.activitymaster.fsdm.db.entities.events.Event;
import com.guicedee.activitymaster.fsdm.services.system.ITimeSystem;

import java.sql.Date;
import java.time.LocalDateTime;

public class EventQueryBuilder
		extends QueryBuilderTable<EventQueryBuilder, Event, java.lang.String,EventSecurityTokenQueryBuilder>
		implements IEventQueryBuilder<EventQueryBuilder, Event>
{
	
	@Override
	public boolean onCreate(Event entity)
	{
		ITimeService time = com.guicedee.client.IGuiceContext.get(ITimeService.class);
		
		LocalDateTime localDateTime = convertToLocalDateTime(entity.getEffectiveFromDate());
		
		com.guicedee.client.IGuiceContext.get(ITimeSystem.class)
		            .getDay(Date.from(entity.getEffectiveFromDate().toInstant()));
		
		entity.setDayID(time.getDayID(localDateTime));
		entity.setHourID(time.getHourID(localDateTime));
		entity.setMinuteID(time.getMinuteID(localDateTime));
		return super.onCreate(entity);
	}
	

}
