package com.guicedee.activitymaster.core.db.entities.events.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.core.db.entities.address.Address;
import com.guicedee.activitymaster.core.db.entities.events.Event;
import com.guicedee.activitymaster.core.db.entities.events.EventXAddress;
import com.guicedee.activitymaster.core.db.entities.events.EventXAddressSecurityToken;
import com.guicedee.activitymaster.core.db.entities.events.EventXAddress_;

import jakarta.persistence.metamodel.Attribute;

public class EventXAddressQueryBuilder
		extends QueryBuilderRelationshipClassification<Event, Address, EventXAddressQueryBuilder,
				                                              EventXAddress, java.util.UUID, EventXAddressSecurityToken>
{
	@Override
	public Attribute getPrimaryAttribute()
	{
		return EventXAddress_.eventID;
	}

	@Override
	public Attribute getSecondaryAttribute()
	{
		return EventXAddress_.addressID;
	}
}
