package com.armineasy.activitymaster.activitymaster.db.entities.events.builders;

import com.armineasy.activitymaster.activitymaster.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.armineasy.activitymaster.activitymaster.db.entities.events.Event;
import com.armineasy.activitymaster.activitymaster.db.entities.events.EventXResourceItem;
import com.armineasy.activitymaster.activitymaster.db.entities.events.EventXResourceItemSecurityToken;
import com.armineasy.activitymaster.activitymaster.db.entities.events.EventXResourceItem_;
import com.armineasy.activitymaster.activitymaster.db.entities.resourceitem.ResourceItem;

import javax.persistence.metamodel.Attribute;

public class EventXResourceItemQueryBuilder
		extends QueryBuilderRelationshipClassification<Event, ResourceItem, EventXResourceItemQueryBuilder,
				                                              EventXResourceItem, Long, EventXResourceItemSecurityToken>
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
