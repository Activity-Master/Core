package com.armineasy.activitymaster.activitymaster.db.entities.events.builders;

import com.armineasy.activitymaster.activitymaster.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.armineasy.activitymaster.activitymaster.db.entities.events.Event;
import com.armineasy.activitymaster.activitymaster.db.entities.events.EventXInvolvedParty;
import com.armineasy.activitymaster.activitymaster.db.entities.events.EventXInvolvedPartySecurityToken;
import com.armineasy.activitymaster.activitymaster.db.entities.events.EventXInvolvedParty_;
import com.armineasy.activitymaster.activitymaster.db.entities.involvedparty.InvolvedParty;

import javax.persistence.metamodel.Attribute;

public class EventXInvolvedPartyQueryBuilder
		extends QueryBuilderRelationshipClassification<Event, InvolvedParty, EventXInvolvedPartyQueryBuilder,
				                                              EventXInvolvedParty, Long, EventXInvolvedPartySecurityToken>
{
	@Override
	public Attribute getPrimaryAttribute()
	{
		return EventXInvolvedParty_.eventID;
	}

	@Override
	public Attribute getSecondaryAttribute()
	{
		return EventXInvolvedParty_.involvedPartyID;
	}
}
