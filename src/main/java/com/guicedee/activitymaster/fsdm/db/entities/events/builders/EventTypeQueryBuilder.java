package com.guicedee.activitymaster.fsdm.db.entities.events.builders;

import com.guicedee.activitymaster.fsdm.client.services.builders.IQueryBuilderNamesAndDescriptions;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.events.IEventTypeQueryBuilder;
import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderSCD;
import com.guicedee.activitymaster.fsdm.db.entities.events.EventType;

public class EventTypeQueryBuilder
		extends QueryBuilderSCD<EventTypeQueryBuilder, EventType, String, EventTypeSecurityTokenQueryBuilder>
		implements IEventTypeQueryBuilder<EventTypeQueryBuilder, EventType>,
		           IQueryBuilderNamesAndDescriptions<EventTypeQueryBuilder, EventType, String>
{

}
