package com.guicedee.activitymaster.core.db.entities.involvedparty.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.core.db.entities.involvedparty.InvolvedParty;
import com.guicedee.activitymaster.core.db.entities.involvedparty.InvolvedPartyXRules;
import com.guicedee.activitymaster.core.db.entities.involvedparty.InvolvedPartyXRulesSecurityToken;
import com.guicedee.activitymaster.core.db.entities.involvedparty.InvolvedPartyXRules_;
import com.guicedee.activitymaster.core.db.entities.rules.Rules;
import jakarta.persistence.metamodel.Attribute;

public class InvolvedPartyXRulesQueryBuilder
		extends QueryBuilderRelationshipClassification<InvolvedParty, Rules, InvolvedPartyXRulesQueryBuilder,
						                                              InvolvedPartyXRules, java.util.UUID, InvolvedPartyXRulesSecurityToken>
{
	@Override
	public Attribute getPrimaryAttribute()
	{
		return InvolvedPartyXRules_.involvedPartyID;
	}

	@Override
	public Attribute getSecondaryAttribute()
	{
		return InvolvedPartyXRules_.rulesID;
	}
}
