package com.guicedee.activitymaster.core.db.entities.involvedparty.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.core.db.entities.involvedparty.InvolvedParty;
import com.guicedee.activitymaster.core.db.entities.involvedparty.InvolvedPartyXInvolvedParty;
import com.guicedee.activitymaster.core.db.entities.involvedparty.InvolvedPartyXInvolvedPartySecurityToken;
import com.guicedee.activitymaster.core.db.entities.involvedparty.InvolvedPartyXInvolvedParty_;

import javax.persistence.metamodel.Attribute;

public class InvolvedPartyXInvolvedPartyQueryBuilder
		extends QueryBuilderRelationshipClassification<InvolvedParty, InvolvedParty, InvolvedPartyXInvolvedPartyQueryBuilder,
						                                              InvolvedPartyXInvolvedParty, Long, InvolvedPartyXInvolvedPartySecurityToken>
{
	@Override
	public Attribute getPrimaryAttribute()
	{
		return InvolvedPartyXInvolvedParty_.parentInvolvedPartyID;
	}

	@Override
	public Attribute getSecondaryAttribute()
	{
		return InvolvedPartyXInvolvedParty_.childInvolvedPartyID;
	}
}
