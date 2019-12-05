package com.guicedee.activitymaster.core.db.entities.arrangement.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.core.db.entities.arrangement.ArrangementXInvolvedPartySecurityToken;
import com.guicedee.activitymaster.core.db.entities.arrangement.ArrangementXInvolvedPartySecurityToken_;

import javax.persistence.metamodel.Attribute;

public class ArrangementXInvolvedPartySecurityTokenQueryBuilder
		extends QueryBuilderSecurities<ArrangementXInvolvedPartySecurityTokenQueryBuilder, ArrangementXInvolvedPartySecurityToken, Long>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return ArrangementXInvolvedPartySecurityToken_.base;
	}
}
