package com.guicedee.activitymaster.fsdm.db.entities.events.builders;

import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.fsdm.db.entities.events.*;
import com.guicedee.activitymaster.fsdm.db.entities.resourceitem.ResourceItem;
import jakarta.persistence.metamodel.SingularAttribute;

public class EventXResourceItemQueryBuilder
		extends QueryBuilderRelationshipClassification<Event, ResourceItem, EventXResourceItemQueryBuilder,
		EventXResourceItem, java.util.UUID>
{
	@Override
	public SingularAttribute<EventXResourceItem, Event> getPrimaryAttribute()
	{
		return EventXResourceItem_.eventID;
	}
	
	@Override
	public SingularAttribute<EventXResourceItem, ResourceItem> getSecondaryAttribute()
	{
		return EventXResourceItem_.resourceItemID;
	}
}
