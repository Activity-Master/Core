package com.armineasy.activitymaster.activitymaster.db.entities.address.builders;

import com.armineasy.activitymaster.activitymaster.db.abstraction.builders.QueryBuilderSecurities;
import com.armineasy.activitymaster.activitymaster.db.entities.address.AddressXResourceItemSecurityToken;
import com.armineasy.activitymaster.activitymaster.db.entities.address.AddressXResourceItemSecurityToken_;

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
