package com.guicedee.activitymaster.core.db.entities.address.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.core.db.entities.address.AddressSecurityToken;
import com.guicedee.activitymaster.core.db.entities.address.AddressSecurityToken_;

import javax.persistence.metamodel.Attribute;

public class AddressSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<AddressSecurityTokenQueryBuilder, AddressSecurityToken, Long>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return AddressSecurityToken_.base;
	}
}
