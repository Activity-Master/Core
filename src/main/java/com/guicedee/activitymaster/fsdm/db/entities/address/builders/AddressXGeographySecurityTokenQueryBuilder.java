package com.guicedee.activitymaster.fsdm.db.entities.address.builders;

import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.fsdm.db.entities.address.AddressXGeographySecurityToken;
import com.guicedee.activitymaster.fsdm.db.entities.address.AddressXGeographySecurityToken_;
import jakarta.persistence.metamodel.Attribute;

import java.util.UUID;

public class AddressXGeographySecurityTokenQueryBuilder
		extends QueryBuilderSecurities<AddressXGeographySecurityTokenQueryBuilder, AddressXGeographySecurityToken, UUID>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return AddressXGeographySecurityToken_.base;
	}
}
