package com.guicedee.activitymaster.core.db.entities.events.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.core.db.entities.events.Event;
import com.guicedee.activitymaster.core.db.entities.events.EventXResourceItem;
import com.guicedee.activitymaster.core.db.entities.events.EventXResourceItemSecurityToken;
import com.guicedee.activitymaster.core.db.entities.events.EventXResourceItem_;
import com.guicedee.activitymaster.core.db.entities.resourceitem.ResourceItem;

import jakarta.persistence.metamodel.Attribute;
import jakarta.persistence.metamodel.SingularAttribute;

public class EventXResourceItemQueryBuilder
		extends QueryBuilderRelationshipClassification<Event, ResourceItem, EventXResourceItemQueryBuilder,
				                                              EventXResourceItem, java.util.UUID, EventXResourceItemSecurityToken>
{
	@Override
	public SingularAttribute<EventXResourceItem, Event> getPrimaryAttribute()
	{
		return EventXResourceItem_.eventID;
	}

	@Override
	public  SingularAttribute<EventXResourceItem, ResourceItem> getSecondaryAttribute()
	{
		return EventXResourceItem_.resourceItemID;
	}
}
