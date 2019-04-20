package com.armineasy.activitymaster.activitymaster.db.entities.events.builders;

import com.armineasy.activitymaster.activitymaster.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.armineasy.activitymaster.activitymaster.db.entities.events.Event;
import com.armineasy.activitymaster.activitymaster.db.entities.events.EventXProduct;
import com.armineasy.activitymaster.activitymaster.db.entities.events.EventXProductSecurityToken;
import com.armineasy.activitymaster.activitymaster.db.entities.events.EventXProduct_;
import com.armineasy.activitymaster.activitymaster.db.entities.product.Product;

import javax.persistence.metamodel.Attribute;

public class EventXProductQueryBuilder
		extends QueryBuilderRelationshipClassification<Event, Product, EventXProductQueryBuilder,
				                                              EventXProduct, Long, EventXProductSecurityToken>
{
	@Override
	public Attribute getPrimaryAttribute()
	{
		return EventXProduct_.eventID;
	}

	@Override
	public Attribute getSecondaryAttribute()
	{
		return EventXProduct_.productID;
	}
}
