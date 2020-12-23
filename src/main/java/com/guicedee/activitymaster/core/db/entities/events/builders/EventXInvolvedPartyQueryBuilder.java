package com.guicedee.activitymaster.core.db.entities.events.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.core.db.entities.events.Event;
import com.guicedee.activitymaster.core.db.entities.events.EventXInvolvedParty;
import com.guicedee.activitymaster.core.db.entities.events.EventXInvolvedPartySecurityToken;
import com.guicedee.activitymaster.core.db.entities.events.EventXInvolvedParty_;
import com.guicedee.activitymaster.core.db.entities.involvedparty.InvolvedParty;

import jakarta.persistence.metamodel.Attribute;

public class EventXInvolvedPartyQueryBuilder
		extends QueryBuilderRelationshipClassification<Event, InvolvedParty, EventXInvolvedPartyQueryBuilder,
				                                              EventXInvolvedParty, java.util.UUID, EventXInvolvedPartySecurityToken>
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
