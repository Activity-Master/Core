package com.guicedee.activitymaster.core.db.entities.events.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.core.db.entities.events.Event;
import com.guicedee.activitymaster.core.db.entities.events.EventXRules;
import com.guicedee.activitymaster.core.db.entities.events.EventXRulesSecurityToken;
import com.guicedee.activitymaster.core.db.entities.events.EventXRules_;
import com.guicedee.activitymaster.core.db.entities.rules.Rules;
import jakarta.persistence.metamodel.Attribute;

public class EventXRulesQueryBuilder
		extends QueryBuilderRelationshipClassification<Event, Rules, EventXRulesQueryBuilder,
		EventXRules, java.util.UUID, EventXRulesSecurityToken>
{
	@Override
	public Attribute getPrimaryAttribute()
	{
		return EventXRules_.eventID;
	}

	@Override
	public Attribute getSecondaryAttribute()
	{
		return EventXRules_.rulesID;
	}
}
