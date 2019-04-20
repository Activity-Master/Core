package com.armineasy.activitymaster.activitymaster.db.entities.involvedparty.builders;

import com.armineasy.activitymaster.activitymaster.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.armineasy.activitymaster.activitymaster.db.entities.involvedparty.InvolvedParty;
import com.armineasy.activitymaster.activitymaster.db.entities.involvedparty.InvolvedPartyXInvolvedParty;
import com.armineasy.activitymaster.activitymaster.db.entities.involvedparty.InvolvedPartyXInvolvedPartySecurityToken;
import com.armineasy.activitymaster.activitymaster.db.entities.involvedparty.InvolvedPartyXInvolvedParty_;

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
