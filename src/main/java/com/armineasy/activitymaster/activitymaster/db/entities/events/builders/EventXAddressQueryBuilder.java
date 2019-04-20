package com.armineasy.activitymaster.activitymaster.db.entities.events.builders;

import com.armineasy.activitymaster.activitymaster.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.armineasy.activitymaster.activitymaster.db.entities.address.Address;
import com.armineasy.activitymaster.activitymaster.db.entities.events.Event;
import com.armineasy.activitymaster.activitymaster.db.entities.events.EventXAddress;
import com.armineasy.activitymaster.activitymaster.db.entities.events.EventXAddressSecurityToken;
import com.armineasy.activitymaster.activitymaster.db.entities.events.EventXAddress_;

import javax.persistence.metamodel.Attribute;

public class EventXAddressQueryBuilder
		extends QueryBuilderRelationshipClassification<Event, Address, EventXAddressQueryBuilder,
				                                              EventXAddress, Long, EventXAddressSecurityToken>
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
