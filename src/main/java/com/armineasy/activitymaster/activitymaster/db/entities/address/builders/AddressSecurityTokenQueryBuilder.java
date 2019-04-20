package com.armineasy.activitymaster.activitymaster.db.entities.address.builders;

import com.armineasy.activitymaster.activitymaster.db.abstraction.builders.QueryBuilderSecurities;
import com.armineasy.activitymaster.activitymaster.db.entities.address.AddressSecurityToken;
import com.armineasy.activitymaster.activitymaster.db.entities.address.AddressSecurityToken_;

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
