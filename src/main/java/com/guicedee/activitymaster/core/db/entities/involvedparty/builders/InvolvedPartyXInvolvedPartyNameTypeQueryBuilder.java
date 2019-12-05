package com.guicedee.activitymaster.core.db.entities.involvedparty.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderRelationship;
import com.guicedee.activitymaster.core.db.entities.involvedparty.*;
import com.guicedee.activitymaster.core.db.entities.involvedparty.*;

import javax.persistence.metamodel.Attribute;

public class InvolvedPartyXInvolvedPartyNameTypeQueryBuilder
		extends QueryBuilderRelationship<InvolvedParty, InvolvedPartyNameType, InvolvedPartyXInvolvedPartyNameTypeQueryBuilder,
				                                InvolvedPartyXInvolvedPartyNameType, Long, InvolvedPartyXInvolvedPartyNameTypeSecurityToken>
{
	@Override
	public Attribute getPrimaryAttribute()
	{
		return InvolvedPartyXInvolvedPartyNameType_.involvedPartyID;
	}

	@Override
	public Attribute getSecondaryAttribute()
	{
		return InvolvedPartyXInvolvedPartyNameType_.involvedPartyNameTypeID;
	}
}
