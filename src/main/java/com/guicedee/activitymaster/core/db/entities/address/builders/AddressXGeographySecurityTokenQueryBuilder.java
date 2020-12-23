package com.guicedee.activitymaster.core.db.entities.address.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.core.db.entities.address.AddressXGeographySecurityToken;
import com.guicedee.activitymaster.core.db.entities.address.AddressXGeographySecurityToken_;

import jakarta.persistence.metamodel.Attribute;

public class AddressXGeographySecurityTokenQueryBuilder
		extends QueryBuilderSecurities<AddressXGeographySecurityTokenQueryBuilder, AddressXGeographySecurityToken, java.util.UUID>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return AddressXGeographySecurityToken_.base;
	}
}
