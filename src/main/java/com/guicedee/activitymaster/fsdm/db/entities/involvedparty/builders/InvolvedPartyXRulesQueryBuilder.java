package com.guicedee.activitymaster.fsdm.db.entities.involvedparty.builders;

import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.fsdm.db.entities.involvedparty.*;
import com.guicedee.activitymaster.fsdm.db.entities.rules.Rules;
import jakarta.persistence.metamodel.SingularAttribute;

public class InvolvedPartyXRulesQueryBuilder
		extends QueryBuilderRelationshipClassification<InvolvedParty, Rules, InvolvedPartyXRulesQueryBuilder,
		InvolvedPartyXRules, java.lang.String>
{
	@Override
	public SingularAttribute<InvolvedPartyXRules, InvolvedParty> getPrimaryAttribute()
	{
		return InvolvedPartyXRules_.involvedPartyID;
	}
	
	@Override
	public SingularAttribute<InvolvedPartyXRules, Rules> getSecondaryAttribute()
	{
		return InvolvedPartyXRules_.rulesID;
	}
}
