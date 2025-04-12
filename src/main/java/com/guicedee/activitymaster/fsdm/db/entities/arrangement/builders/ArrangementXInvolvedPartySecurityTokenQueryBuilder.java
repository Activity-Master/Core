package com.guicedee.activitymaster.fsdm.db.entities.arrangement.builders;

import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.fsdm.db.entities.arrangement.ArrangementXInvolvedPartySecurityToken;
import com.guicedee.activitymaster.fsdm.db.entities.arrangement.ArrangementXInvolvedPartySecurityToken_;
import jakarta.persistence.metamodel.Attribute;

import java.util.UUID;

public class ArrangementXInvolvedPartySecurityTokenQueryBuilder
		extends QueryBuilderSecurities<ArrangementXInvolvedPartySecurityTokenQueryBuilder, ArrangementXInvolvedPartySecurityToken, UUID>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return ArrangementXInvolvedPartySecurityToken_.base;
	}
}
