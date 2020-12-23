package com.guicedee.activitymaster.core.db.entities.address.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.core.db.entities.address.AddressSecurityToken;
import com.guicedee.activitymaster.core.db.entities.address.AddressSecurityToken_;

import jakarta.persistence.metamodel.Attribute;

public class AddressSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<AddressSecurityTokenQueryBuilder, AddressSecurityToken, java.util.UUID>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return AddressSecurityToken_.base;
	}
}
