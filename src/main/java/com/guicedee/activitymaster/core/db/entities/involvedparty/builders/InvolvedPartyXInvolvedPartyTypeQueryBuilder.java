package com.guicedee.activitymaster.core.db.entities.involvedparty.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderRelationship;
import com.guicedee.activitymaster.core.db.entities.involvedparty.*;
import com.guicedee.activitymaster.core.db.entities.involvedparty.*;

import javax.persistence.metamodel.Attribute;

public class InvolvedPartyXInvolvedPartyTypeQueryBuilder
		extends QueryBuilderRelationship<InvolvedParty, InvolvedPartyType,
						                                InvolvedPartyXInvolvedPartyTypeQueryBuilder,
				                                InvolvedPartyXInvolvedPartyType, Long,
				                                InvolvedPartyXInvolvedPartyTypeSecurityToken>
{
	@Override
	public Attribute getPrimaryAttribute()
	{
		return InvolvedPartyXInvolvedPartyType_.involvedPartyID;
	}

	@Override
	public Attribute getSecondaryAttribute()
	{
		return InvolvedPartyXInvolvedPartyType_.involvedPartyTypeID;
	}
}
