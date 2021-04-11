package com.guicedee.activitymaster.fsdm.db.entities.events.builders;

import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.events.IEventTypeQueryBuilder;
import com.guicedee.activitymaster.fsdm.db.abstraction.builders.assists.QueryBuilderSCDNameDescription;
import com.guicedee.activitymaster.fsdm.db.entities.events.EventType;

public class EventTypeQueryBuilder
		extends QueryBuilderSCDNameDescription<EventTypeQueryBuilder, EventType, java.util.UUID>
		implements IEventTypeQueryBuilder<EventTypeQueryBuilder,EventType>
{

}
