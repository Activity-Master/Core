package com.guicedee.activitymaster.core.db.entities.involvedparty.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.core.db.entities.involvedparty.InvolvedParty;
import com.guicedee.activitymaster.core.db.entities.involvedparty.InvolvedPartyXInvolvedParty;
import com.guicedee.activitymaster.core.db.entities.involvedparty.InvolvedPartyXInvolvedPartySecurityToken;
import com.guicedee.activitymaster.core.db.entities.involvedparty.InvolvedPartyXInvolvedParty_;

import jakarta.persistence.metamodel.Attribute;
import jakarta.persistence.metamodel.SingularAttribute;

public class InvolvedPartyXInvolvedPartyQueryBuilder
		extends QueryBuilderRelationshipClassification<InvolvedParty, InvolvedParty, InvolvedPartyXInvolvedPartyQueryBuilder,
						                                              InvolvedPartyXInvolvedParty, java.util.UUID, InvolvedPartyXInvolvedPartySecurityToken>
{
	@Override
	public SingularAttribute<InvolvedPartyXInvolvedParty, InvolvedParty> getPrimaryAttribute()
	{
		return InvolvedPartyXInvolvedParty_.parentInvolvedPartyID;
	}

	@Override
	public SingularAttribute<InvolvedPartyXInvolvedParty, InvolvedParty> getSecondaryAttribute()
	{
		return InvolvedPartyXInvolvedParty_.childInvolvedPartyID;
	}
}
