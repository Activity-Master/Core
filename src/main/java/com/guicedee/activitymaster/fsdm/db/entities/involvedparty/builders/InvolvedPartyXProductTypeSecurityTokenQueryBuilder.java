package com.guicedee.activitymaster.fsdm.db.entities.involvedparty.builders;

import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.fsdm.db.entities.involvedparty.InvolvedPartyXProductTypeSecurityToken;
import com.guicedee.activitymaster.fsdm.db.entities.involvedparty.InvolvedPartyXProductTypeSecurityToken_;
import jakarta.persistence.metamodel.Attribute;

public class InvolvedPartyXProductTypeSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<InvolvedPartyXProductTypeSecurityTokenQueryBuilder, InvolvedPartyXProductTypeSecurityToken, java.util.UUID>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return InvolvedPartyXProductTypeSecurityToken_.base;
	}
}
