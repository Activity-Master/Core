package com.armineasy.activitymaster.activitymaster.db.entities.address.builders;

import com.armineasy.activitymaster.activitymaster.db.abstraction.builders.QueryBuilderSecurities;
import com.armineasy.activitymaster.activitymaster.db.entities.address.AddressXGeographySecurityToken;
import com.armineasy.activitymaster.activitymaster.db.entities.address.AddressXGeographySecurityToken_;

import javax.persistence.metamodel.Attribute;

public class AddressXGeographySecurityTokenQueryBuilder
		extends QueryBuilderSecurities<AddressXGeographySecurityTokenQueryBuilder, AddressXGeographySecurityToken, Long>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return AddressXGeographySecurityToken_.base;
	}
}
