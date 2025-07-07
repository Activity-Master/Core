package com.guicedee.activitymaster.fsdm.db.entities.events.builders;

import com.guicedee.activitymaster.fsdm.client.services.ITimeService;
import com.guicedee.activitymaster.fsdm.client.services.builders.IQueryBuilderSCD;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.events.IEventQueryBuilder;
import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderSCD;
import com.guicedee.activitymaster.fsdm.db.entities.events.Event;
import com.guicedee.activitymaster.fsdm.services.system.ITimeSystem;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.UUID;

public class EventQueryBuilder
		extends QueryBuilderSCD<EventQueryBuilder, Event, UUID,EventSecurityTokenQueryBuilder>
		implements IEventQueryBuilder<EventQueryBuilder, Event>
{

	@Override
	public boolean onCreate(Event entity)
	{
		ITimeService time = com.guicedee.client.IGuiceContext.get(ITimeService.class);

		LocalDateTime localDateTime = IQueryBuilderSCD.convertToLocalDateTime(entity.getEffectiveFromDate());
		
		com.guicedee.client.IGuiceContext.get(ITimeSystem.class)
		            .getDay(Date.from(entity.getEffectiveFromDate().toInstant()));
		
		entity.setDayID(time.getDayID(localDateTime));
		entity.setHourID(time.getHourID(localDateTime));
		entity.setMinuteID(time.getMinuteID(localDateTime));
		return super.onCreate(entity);
	}
	

}
