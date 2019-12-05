package com.guicedee.activitymaster.core.db.entities.address.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.core.db.entities.address.AddressXResourceItemSecurityToken;
import com.guicedee.activitymaster.core.db.entities.address.AddressXResourceItemSecurityToken_;

import javax.persistence.metamodel.Attribute;

public class AddressXResourceItemSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<AddressXResourceItemSecurityTokenQueryBuilder, AddressXResourceItemSecurityToken, Long>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return AddressXResourceItemSecurityToken_.base;
	}
}
