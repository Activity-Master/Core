package com.guicedee.activitymaster.core.db.entities.involvedparty.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.core.db.entities.classifications.Classification;
import com.guicedee.activitymaster.core.db.entities.involvedparty.InvolvedParty;
import com.guicedee.activitymaster.core.db.entities.involvedparty.InvolvedPartyXClassification;
import com.guicedee.activitymaster.core.db.entities.involvedparty.InvolvedPartyXClassificationSecurityToken;
import com.guicedee.activitymaster.core.db.entities.involvedparty.InvolvedPartyXClassification_;

import javax.persistence.metamodel.Attribute;

public class InvolvedPartyXClassificationQueryBuilder
		extends QueryBuilderRelationshipClassification<InvolvedParty, Classification, InvolvedPartyXClassificationQueryBuilder,
						                                              InvolvedPartyXClassification, Long, InvolvedPartyXClassificationSecurityToken>
{
	@Override
	public Attribute getPrimaryAttribute()
	{
		return InvolvedPartyXClassification_.involvedPartyID;
	}

	@Override
	public Attribute getSecondaryAttribute()
	{
		return InvolvedPartyXClassification_.classificationID;
	}
}
