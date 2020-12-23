package com.guicedee.activitymaster.core.db.entities.events.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.core.db.entities.events.Event;
import com.guicedee.activitymaster.core.db.entities.events.EventXResourceItem;
import com.guicedee.activitymaster.core.db.entities.events.EventXResourceItemSecurityToken;
import com.guicedee.activitymaster.core.db.entities.events.EventXResourceItem_;
import com.guicedee.activitymaster.core.db.entities.resourceitem.ResourceItem;

import jakarta.persistence.metamodel.Attribute;

public class EventXResourceItemQueryBuilder
		extends QueryBuilderRelationshipClassification<Event, ResourceItem, EventXResourceItemQueryBuilder,
				                                              EventXResourceItem, java.util.UUID, EventXResourceItemSecurityToken>
{
	@Override
	public Attribute getPrimaryAttribute()
	{
		return EventXResourceItem_.eventID;
	}

	@Override
	public Attribute getSecondaryAttribute()
	{
		return EventXResourceItem_.resourceItemID;
	}
}
