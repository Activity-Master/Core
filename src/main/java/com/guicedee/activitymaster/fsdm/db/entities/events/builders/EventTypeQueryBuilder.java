package com.guicedee.activitymaster.fsdm.db.entities.events.builders;

import com.guicedee.activitymaster.fsdm.client.services.builders.IQueryBuilderNamesAndDescriptions;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.events.IEventTypeQueryBuilder;
import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderSCD;
import com.guicedee.activitymaster.fsdm.db.entities.events.EventType;

import java.util.UUID;

public class EventTypeQueryBuilder
		extends QueryBuilderSCD<EventTypeQueryBuilder, EventType, UUID, EventTypeSecurityTokenQueryBuilder>
		implements IEventTypeQueryBuilder<EventTypeQueryBuilder, EventType>,
		           IQueryBuilderNamesAndDescriptions<EventTypeQueryBuilder, EventType, UUID>
{

}
