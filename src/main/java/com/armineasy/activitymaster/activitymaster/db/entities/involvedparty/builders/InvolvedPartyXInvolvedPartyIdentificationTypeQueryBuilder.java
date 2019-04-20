package com.armineasy.activitymaster.activitymaster.db.entities.involvedparty.builders;

import com.armineasy.activitymaster.activitymaster.db.abstraction.builders.QueryBuilderRelationship;
import com.armineasy.activitymaster.activitymaster.db.entities.involvedparty.*;

import javax.persistence.metamodel.Attribute;

public class InvolvedPartyXInvolvedPartyIdentificationTypeQueryBuilder
		extends QueryBuilderRelationship<InvolvedParty,
				                                InvolvedPartyIdentificationType,
				                                InvolvedPartyXInvolvedPartyIdentificationTypeQueryBuilder,
				                                InvolvedPartyXInvolvedPartyIdentificationType,
				                                Long,
				                                InvolvedPartyXInvolvedPartyIdentificationTypeSecurityToken>
{
	@Override
	public Attribute getPrimaryAttribute()
	{
		return InvolvedPartyXInvolvedPartyIdentificationType_.involvedPartyID;
	}

	@Override
	public Attribute getSecondaryAttribute()
	{
		return InvolvedPartyXInvolvedPartyIdentificationType_.involvedPartyIdentificationTypeID;
	}
}
