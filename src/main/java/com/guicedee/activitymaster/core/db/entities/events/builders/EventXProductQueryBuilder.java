package com.guicedee.activitymaster.core.db.entities.events.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.core.db.entities.events.Event;
import com.guicedee.activitymaster.core.db.entities.events.EventXProduct;
import com.guicedee.activitymaster.core.db.entities.events.EventXProductSecurityToken;
import com.guicedee.activitymaster.core.db.entities.events.EventXProduct_;
import com.guicedee.activitymaster.core.db.entities.product.Product;

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
