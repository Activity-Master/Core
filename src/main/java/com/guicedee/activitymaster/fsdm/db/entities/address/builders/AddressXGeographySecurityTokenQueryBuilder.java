package com.guicedee.activitymaster.fsdm.db.entities.address.builders;

import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.fsdm.db.entities.address.AddressXGeographySecurityToken;
import com.guicedee.activitymaster.fsdm.db.entities.address.AddressXGeographySecurityToken_;
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
