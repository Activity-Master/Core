package com.armineasy.activitymaster.activitymaster.db.entities.involvedparty.builders;

import com.armineasy.activitymaster.activitymaster.db.abstraction.builders.QueryBuilderSecurities;
import com.armineasy.activitymaster.activitymaster.db.entities.involvedparty.InvolvedPartyXAddressSecurityToken;
import com.armineasy.activitymaster.activitymaster.db.entities.involvedparty.InvolvedPartyXAddressSecurityToken_;

import javax.persistence.metamodel.Attribute;

public class InvolvedPartyXAddressSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<InvolvedPartyXAddressSecurityTokenQueryBuilder, InvolvedPartyXAddressSecurityToken, Long>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return InvolvedPartyXAddressSecurityToken_.base;
	}
}
