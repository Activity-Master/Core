package com.armineasy.activitymaster.activitymaster.db.entities.involvedparty.builders;

import com.armineasy.activitymaster.activitymaster.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.armineasy.activitymaster.activitymaster.db.entities.classifications.Classification;
import com.armineasy.activitymaster.activitymaster.db.entities.involvedparty.InvolvedParty;
import com.armineasy.activitymaster.activitymaster.db.entities.involvedparty.InvolvedPartyXClassification;
import com.armineasy.activitymaster.activitymaster.db.entities.involvedparty.InvolvedPartyXClassificationSecurityToken;
import com.armineasy.activitymaster.activitymaster.db.entities.involvedparty.InvolvedPartyXClassification_;

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
