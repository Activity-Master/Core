package com.guicedee.activitymaster.core.db.entities.events.builders;

import com.guicedee.activitymaster.client.services.builders.warehouse.events.IEventTypeQueryBuilder;
import com.guicedee.activitymaster.core.db.abstraction.builders.assists.QueryBuilderSCDNameDescription;
import com.guicedee.activitymaster.core.db.entities.events.EventType;

public class EventTypeQueryBuilder
		extends QueryBuilderSCDNameDescription<EventTypeQueryBuilder, EventType, java.util.UUID>
		implements IEventTypeQueryBuilder<EventTypeQueryBuilder,EventType>
{

}
