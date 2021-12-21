package com.guicedee.activitymaster.fsdm.db.entities.involvedparty.builders;

import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.fsdm.db.entities.involvedparty.InvolvedPartyXAddressSecurityToken;
import com.guicedee.activitymaster.fsdm.db.entities.involvedparty.InvolvedPartyXAddressSecurityToken_;
import jakarta.persistence.metamodel.Attribute;

public class InvolvedPartyXAddressSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<InvolvedPartyXAddressSecurityTokenQueryBuilder, InvolvedPartyXAddressSecurityToken, java.util.UUID>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return InvolvedPartyXAddressSecurityToken_.base;
	}
}
