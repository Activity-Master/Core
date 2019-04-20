package com.armineasy.activitymaster.activitymaster.db.entities.address.builders;

import com.armineasy.activitymaster.activitymaster.db.abstraction.builders.QueryBuilderSecurities;
import com.armineasy.activitymaster.activitymaster.db.entities.address.AddressXClassificationSecurityToken;
import com.armineasy.activitymaster.activitymaster.db.entities.address.AddressXClassificationSecurityToken_;

import javax.persistence.metamodel.Attribute;

public class AddressXClassificationSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<AddressXClassificationSecurityTokenQueryBuilder, AddressXClassificationSecurityToken, Long>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return AddressXClassificationSecurityToken_.base;
	}
}
