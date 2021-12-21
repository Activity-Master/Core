package com.guicedee.activitymaster.fsdm.db.entities.arrangement.builders;

import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.fsdm.db.entities.arrangement.ArrangementXInvolvedPartySecurityToken;
import com.guicedee.activitymaster.fsdm.db.entities.arrangement.ArrangementXInvolvedPartySecurityToken_;
import jakarta.persistence.metamodel.Attribute;

public class ArrangementXInvolvedPartySecurityTokenQueryBuilder
		extends QueryBuilderSecurities<ArrangementXInvolvedPartySecurityTokenQueryBuilder, ArrangementXInvolvedPartySecurityToken, java.util.UUID>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return ArrangementXInvolvedPartySecurityToken_.base;
	}
}
