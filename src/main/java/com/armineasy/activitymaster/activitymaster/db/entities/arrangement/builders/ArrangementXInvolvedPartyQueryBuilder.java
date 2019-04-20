package com.armineasy.activitymaster.activitymaster.db.entities.arrangement.builders;

import com.armineasy.activitymaster.activitymaster.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.armineasy.activitymaster.activitymaster.db.entities.arrangement.Arrangement;
import com.armineasy.activitymaster.activitymaster.db.entities.arrangement.ArrangementXInvolvedParty;
import com.armineasy.activitymaster.activitymaster.db.entities.arrangement.ArrangementXInvolvedPartySecurityToken;
import com.armineasy.activitymaster.activitymaster.db.entities.arrangement.ArrangementXInvolvedParty_;
import com.armineasy.activitymaster.activitymaster.db.entities.involvedparty.InvolvedParty;

import javax.persistence.metamodel.Attribute;

public class ArrangementXInvolvedPartyQueryBuilder
		extends QueryBuilderRelationshipClassification<Arrangement, InvolvedParty, ArrangementXInvolvedPartyQueryBuilder,
				                                              ArrangementXInvolvedParty, Long, ArrangementXInvolvedPartySecurityToken>
{
	@Override
	public Attribute getPrimaryAttribute()
	{
		return ArrangementXInvolvedParty_.arrangementID;
	}

	@Override
	public Attribute getSecondaryAttribute()
	{
		return ArrangementXInvolvedParty_.involvedPartyID;
	}
}
