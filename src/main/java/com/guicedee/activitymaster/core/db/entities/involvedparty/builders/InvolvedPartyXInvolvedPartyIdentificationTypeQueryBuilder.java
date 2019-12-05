package com.guicedee.activitymaster.core.db.entities.involvedparty.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderRelationship;
import com.guicedee.activitymaster.core.db.entities.involvedparty.*;
import com.guicedee.activitymaster.core.db.entities.involvedparty.*;

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
